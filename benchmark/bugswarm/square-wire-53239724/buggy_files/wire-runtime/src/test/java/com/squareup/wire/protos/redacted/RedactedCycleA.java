// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ../wire-runtime/src/test/proto/redacted_test.proto
package com.squareup.wire.protos.redacted;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

public final class RedactedCycleA extends Message {

  @ProtoField(tag = 1)
  public final RedactedCycleB b;

  public RedactedCycleA(RedactedCycleB b) {
    this.b = b;
  }

  private RedactedCycleA(Builder builder) {
    this(builder.b);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RedactedCycleA)) return false;
    return equals(b, ((RedactedCycleA) other).b);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = b != null ? b.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<RedactedCycleA> {

    public RedactedCycleB b;

    public Builder() {
    }

    public Builder(RedactedCycleA message) {
      super(message);
      if (message == null) return;
      this.b = message.b;
    }

    public Builder b(RedactedCycleB b) {
      this.b = b;
      return this;
    }

    @Override
    public RedactedCycleA build() {
      return new RedactedCycleA(this);
    }
  }
}
