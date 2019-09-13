// Copyright 2013 Square, Inc.
package retrofit.converter;

import com.squareup.okhttp.MediaType;
import com.squareup.wire.Message;
import com.squareup.wire.Wire;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/** A {@link Converter} that reads and writes protocol buffers using Wire. */
public class WireConverter implements Converter {
  private static final MediaType MIME_TYPE = MediaType.parse("application/x-protobuf");

  private final Wire wire;

  /** Create a converter with a default {@link Wire} instance. */
  public WireConverter() {
    this(new Wire());
  }

  /** Create a converter using the supplied {@link Wire} instance. */
  public WireConverter(Wire wire) {
    this.wire = wire;
  }

  @SuppressWarnings("unchecked") //
  @Override public Object fromBody(TypedInput body, Type type) throws IOException {
    if (!(type instanceof Class<?>)) {
      throw new IllegalArgumentException("Expected a raw Class<?> but was " + type);
    }
    Class<?> c = (Class<?>) type;
    if (!Message.class.isAssignableFrom(c)) {
      throw new IllegalArgumentException("Expected a proto message but was " + c.getName());
    }

    InputStream in = null;
    try {
      in = body.in();
      return wire.parseFrom(in, (Class<Message>) c);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException ignored) {
        }
      }
    }
  }

  @Override public TypedOutput toBody(Object object, Type type) {
    if (!(object instanceof Message)) {
      throw new IllegalArgumentException(
          "Expected a proto message but was " + (object != null ? object.getClass().getName()
              : "null"));
    }
    byte[] bytes = ((Message) object).toByteArray();
    return new TypedByteArray(bytes, MIME_TYPE);
  }
}
