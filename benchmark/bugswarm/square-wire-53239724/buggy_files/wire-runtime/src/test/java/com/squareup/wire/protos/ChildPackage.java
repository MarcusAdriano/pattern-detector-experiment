// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ../wire-runtime/src/test/proto/child_pkg.proto
package com.squareup.wire.protos;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import com.squareup.wire.protos.foreign.ForeignEnum;

import static com.squareup.wire.Message.Datatype.ENUM;

public final class ChildPackage extends Message {

  public static final ForeignEnum DEFAULT_INNER_FOREIGN_ENUM = ForeignEnum.BAV;

  @ProtoField(tag = 1, type = ENUM)
  public final ForeignEnum inner_foreign_enum;

  public ChildPackage(ForeignEnum inner_foreign_enum) {
    this.inner_foreign_enum = inner_foreign_enum;
  }

  private ChildPackage(Builder builder) {
    this(builder.inner_foreign_enum);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ChildPackage)) return false;
    return equals(inner_foreign_enum, ((ChildPackage) other).inner_foreign_enum);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = inner_foreign_enum != null ? inner_foreign_enum.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<ChildPackage> {

    public ForeignEnum inner_foreign_enum;

    public Builder() {
    }

    public Builder(ChildPackage message) {
      super(message);
      if (message == null) return;
      this.inner_foreign_enum = message.inner_foreign_enum;
    }

    public Builder inner_foreign_enum(ForeignEnum inner_foreign_enum) {
      this.inner_foreign_enum = inner_foreign_enum;
      return this;
    }

    @Override
    public ChildPackage build() {
      return new ChildPackage(this);
    }
  }
}