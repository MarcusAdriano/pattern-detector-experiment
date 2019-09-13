/*
 * SonarQube Java
 * Copyright (C) 2012-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.checks;

import com.google.common.base.MoreObjects;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import org.sonar.check.Rule;
import org.sonar.java.matcher.MethodMatcher;
import org.sonar.java.matcher.MethodMatcherCollection;
import org.sonar.java.matcher.NameCriteria;
import org.sonar.java.matcher.TypeCriteria;
import org.sonar.java.model.ModifiersUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Modifier;
import org.sonar.plugins.java.api.tree.ReturnStatementTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.TryStatementTree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "S2970")
public class AssertionsCompletenessCheck extends BaseTreeVisitor implements JavaFileScanner {

  private static final String FEST_ASSERT_SUPERTYPE = "org.fest.assertions.Assert";
  private static final String ASSERTJ_SUPERTYPE = "org.assertj.core.api.AbstractAssert";
  private static final String TRUTH_SUPERTYPE = "com.google.common.truth.TestVerb";
  private static final String TRUTH_8_SUPERTYPE = "com.google.common.truth.StandardSubjectBuilder";
  private static final MethodMatcher MOCKITO_VERIFY = MethodMatcher.create()
    .typeDefinition("org.mockito.Mockito").name("verify").withAnyParameters();
  private static final MethodMatcher ASSERTJ_ASSERT_ALL = MethodMatcher.create()
    .typeDefinition(TypeCriteria.subtypeOf("org.assertj.core.api.SoftAssertions")).name("assertAll").withAnyParameters();
  private static final MethodMatcher ASSERTJ_ASSERT_THAT = MethodMatcher.create()
    .typeDefinition(TypeCriteria.subtypeOf("org.assertj.core.api.AbstractSoftAssertions"))
    .name(NameCriteria.startsWith("assertThat"))
    .withAnyParameters();

  private static final MethodMatcherCollection FEST_LIKE_ASSERT_THAT = MethodMatcherCollection.create(
    // Fest 1.X
    assertThatOnType("org.fest.assertions.Assertions"),
    // Fest 2.X
    assertThatOnType("org.fest.assertions.api.Assertions"),
    // AssertJ 1.X
    assertThatOnType("org.assertj.core.api.AbstractSoftAssertions"),
    // AssertJ 2.X
    assertThatOnType("org.assertj.core.api.Assertions"),
    assertThatOnType("org.assertj.core.api.AbstractStandardSoftAssertions"),
    // AssertJ 3.X
    assertThatOnType("org.assertj.core.api.StrictAssertions"),
    // Truth 0.29
    methodWithName("com.google.common.truth.Truth", NameCriteria.startsWith("assert")),
    methodWithName("com.google.common.truth.Truth8", NameCriteria.startsWith("assert"))
  );

  private static final MethodMatcherCollection FEST_LIKE_EXCLUSIONS = MethodMatcherCollection.create(
    methodWithName(FEST_ASSERT_SUPERTYPE, NameCriteria.startsWith("as")),
    methodWithName(FEST_ASSERT_SUPERTYPE, NameCriteria.startsWith("using")),
    methodWithName(FEST_ASSERT_SUPERTYPE, NameCriteria.startsWith("with")),
    methodWithName(FEST_ASSERT_SUPERTYPE, NameCriteria.is("describedAs")),
    methodWithName(FEST_ASSERT_SUPERTYPE, NameCriteria.is("overridingErrorMessage")),
    methodWithName(ASSERTJ_SUPERTYPE, NameCriteria.startsWith("as")),
    methodWithName(ASSERTJ_SUPERTYPE, NameCriteria.startsWith("using")),
    methodWithName(ASSERTJ_SUPERTYPE, NameCriteria.startsWith("with")),
    methodWithName(ASSERTJ_SUPERTYPE, NameCriteria.is("describedAs")),
    methodWithName(ASSERTJ_SUPERTYPE, NameCriteria.is("overridingErrorMessage")),
    methodWithName(TRUTH_SUPERTYPE, NameCriteria.is("that")),
    methodWithName(TRUTH_8_SUPERTYPE, NameCriteria.is("that"))
  );

  private Boolean chainedToAnyMethodButFestExclusions = null;
  private JavaFileScannerContext context;
  private final Deque<Boolean> containsAssertThatWithoutAssertAll = new ArrayDeque<>();

  private static MethodMatcher assertThatOnType(String type) {
    return MethodMatcher.create().typeDefinition(type).name("assertThat").addParameter(TypeCriteria.anyType());
  }

  private static MethodMatcher methodWithName(String superType, NameCriteria nameCriteria) {
    return MethodMatcher.create().typeDefinition(TypeCriteria.subtypeOf(superType)).name(nameCriteria).withAnyParameters();
  }

  @Override
  public void scanFile(final JavaFileScannerContext context) {
    this.context = context;
    scan(context.getTree());
  }

  @Override
  public void visitVariable(VariableTree tree) {
    // skip variable assignments
  }

  @Override
  public void visitReturnStatement(ReturnStatementTree tree) {
    // skip return statements
  }

  @Override
  public void visitMethod(MethodTree methodTree) {
    if (ModifiersUtils.hasModifier(methodTree.modifiers(), Modifier.ABSTRACT)) {
      return;
    }
    containsAssertThatWithoutAssertAll.push(false);
    super.visitMethod(methodTree);
    if (Boolean.TRUE.equals(containsAssertThatWithoutAssertAll.pop())) {
      context.reportIssue(this, methodTree.block().closeBraceToken(), "Add a call to 'assertAll' after all 'assertThat'.");
    }
  }

  @Override
  public void visitMethodInvocation(MethodInvocationTree mit) {
    checkForAssertJSoftAssertions(mit);
    if (incompleteAssertion(mit)) {
      return;
    }
    Boolean previous = chainedToAnyMethodButFestExclusions;
    chainedToAnyMethodButFestExclusions = MoreObjects.firstNonNull(chainedToAnyMethodButFestExclusions, false) || !FEST_LIKE_EXCLUSIONS.anyMatch(mit);
    scan(mit.methodSelect());
    // skip arguments
    chainedToAnyMethodButFestExclusions = previous;
  }

  @Override
  public void visitTryStatement(TryStatementTree tree) {
    boolean hasAutoCloseableSoftAssertion = tree.resourceList().stream()
      .map(AssertionsCompletenessCheck::resourceSymbol)
      .map(Symbol::type)
      .filter(Objects::nonNull)
      .anyMatch(type -> type.isSubtypeOf("org.assertj.core.api.AutoCloseableSoftAssertions"));
    super.visitTryStatement(tree);
    if (hasAutoCloseableSoftAssertion) {
      checkAssertJAssertAll(tree.block().closeBraceToken(), "Add one or more 'assertThat' before the end of this try block.");
    }
  }

  private static Symbol resourceSymbol(Tree tree) {
    switch (tree.kind()) {
      case VARIABLE:
        return ((VariableTree) tree).symbol();
      case IDENTIFIER:
        return ((IdentifierTree) tree).symbol();
      case MEMBER_SELECT:
        return ((MemberSelectExpressionTree) tree).identifier().symbol();
      default:
        throw new IllegalArgumentException("Tree is not try-with-resources resource");
    }
  }

  private void checkForAssertJSoftAssertions(MethodInvocationTree mit) {
    if (ASSERTJ_ASSERT_ALL.matches(mit)) {
      checkAssertJAssertAll(mit.methodSelect(), "Add one or more 'assertThat' before 'assertAll'.");
    } else if (ASSERTJ_ASSERT_THAT.matches(mit) && !isJUnitSoftAssertions(mit)) {
      set(containsAssertThatWithoutAssertAll, true);
    }
  }

  private void checkAssertJAssertAll(Tree issueLocation, String issueMessage) {
    if (Boolean.TRUE.equals(containsAssertThatWithoutAssertAll.peek())) {
      set(containsAssertThatWithoutAssertAll, false);
    } else {
      context.reportIssue(this, issueLocation, issueMessage);
    }
  }

  private static boolean isJUnitSoftAssertions(MethodInvocationTree mit) {
    ExpressionTree expressionTree = mit.methodSelect();
    if (expressionTree.is(Tree.Kind.MEMBER_SELECT)) {
      Type type = ((MemberSelectExpressionTree) expressionTree).expression().symbolType();
      return type.isSubtypeOf("org.assertj.core.api.JUnitSoftAssertions") ||
        type.isSubtypeOf("org.assertj.core.api.Java6JUnitSoftAssertions");
    }
    return false;
  }

  private static void set(Deque<Boolean> collection, boolean value) {
    if (Boolean.TRUE.equals(collection.peek()) != value) {
      collection.pop();
      collection.push(value);
    }
  }

  private boolean incompleteAssertion(MethodInvocationTree mit) {
    if (((FEST_LIKE_ASSERT_THAT.anyMatch(mit) && (mit.arguments().size() == 1)) || MOCKITO_VERIFY.matches(mit)) && !Boolean.TRUE.equals(chainedToAnyMethodButFestExclusions)) {
      context.reportIssue(this, mit.methodSelect(), "Complete the assertion.");
      return true;
    }
    return false;
  }

}
