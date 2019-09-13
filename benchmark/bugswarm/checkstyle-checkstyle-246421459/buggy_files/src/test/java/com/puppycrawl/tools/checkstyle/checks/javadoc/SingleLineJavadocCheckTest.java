////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2017 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks.javadoc;

import static com.puppycrawl.tools.checkstyle.checks.javadoc.SingleLineJavadocCheck.MSG_KEY;
import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.BaseCheckTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class SingleLineJavadocCheckTest extends BaseCheckTestSupport {
    @Override
    protected String getPath(String filename) throws IOException {
        return super.getPath("checks" + File.separator
                + "javadoc" + File.separator + filename);
    }

    @Test
    public void testAcceptableTokens() {
        final SingleLineJavadocCheck checkObj = new SingleLineJavadocCheck();
        final int[] expected = {TokenTypes.BLOCK_COMMENT_BEGIN };
        assertArrayEquals(expected, checkObj.getAcceptableTokens());
    }

    @Test
    public void testGetRequiredTokens() {
        final SingleLineJavadocCheck checkObj = new SingleLineJavadocCheck();
        final int[] expected = {TokenTypes.BLOCK_COMMENT_BEGIN };
        assertArrayEquals(expected, checkObj.getRequiredTokens());
    }

    @Test
    public void simpleTest() throws Exception {
        final DefaultConfiguration checkConfig =
                createCheckConfig(SingleLineJavadocCheck.class);
        final String[] expected = {
            "12: " + getCheckMessage(MSG_KEY),
            "28: " + getCheckMessage(MSG_KEY),
            "40: " + getCheckMessage(MSG_KEY),
            "43: " + getCheckMessage(MSG_KEY),
            "49: " + getCheckMessage(MSG_KEY),
        };
        verify(checkConfig, getPath("InputSingleLineJavadoc.java"), expected);
    }

    @Test
    public void testIgnoredTags() throws Exception {
        final DefaultConfiguration checkConfig =
                createCheckConfig(SingleLineJavadocCheck.class);
        checkConfig.addAttribute("ignoredTags", "@inheritDoc, @throws,  "
            + "@ignoredCustomTag");
        checkConfig.addAttribute("ignoreInlineTags", "false");

        final String[] expected = {
            "4: " + getCheckMessage(MSG_KEY),
            "34: " + getCheckMessage(MSG_KEY),
            "37: " + getCheckMessage(MSG_KEY),
            "40: " + getCheckMessage(MSG_KEY),
            "46: " + getCheckMessage(MSG_KEY),
            "49: " + getCheckMessage(MSG_KEY),
        };
        verify(checkConfig, getPath("InputSingleLineJavadoc.java"), expected);
    }
}