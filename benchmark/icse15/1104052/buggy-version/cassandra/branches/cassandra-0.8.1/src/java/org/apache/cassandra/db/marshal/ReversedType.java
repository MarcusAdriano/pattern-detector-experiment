/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cassandra.db.marshal;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class ReversedType<T> extends AbstractType<T>
{
    // interning instances
    private static final Map<AbstractType, ReversedType> instances = new HashMap<AbstractType, ReversedType>();

    // package protected for unit tests sake
    final AbstractType<T> baseType;

    public static synchronized <T> ReversedType<T> getInstance(AbstractType<T> baseType)
    {
        ReversedType type = instances.get(baseType);
        if (type == null)
        {
            type = new ReversedType(baseType);
            instances.put(baseType, type);
        }
        return (ReversedType<T>) type;
    }

    private ReversedType(AbstractType<T> baseType)
    {
        this.baseType = baseType;
    }

    public int compare(ByteBuffer o1, ByteBuffer o2)
    {
        return -baseType.compare(o1, o2);
    }

    public String getString(ByteBuffer bytes)
    {
        return baseType.getString(bytes);
    }

    public ByteBuffer fromString(String source)
    {
        return baseType.fromString(source);
    }

    public void validate(ByteBuffer bytes) throws MarshalException
    {
        baseType.validate(bytes);
    }

    public T compose(ByteBuffer bytes)
    {
        return baseType.compose(bytes);
    }

    public ByteBuffer decompose(T value)
    {
        return baseType.decompose(value);
    }

    public Class<T> getType()
    {
        return baseType.getType();
    }

    public String toString(T t)
    {
        return baseType.toString(t);
    }

    public boolean isSigned()
    {
        return baseType.isSigned();
    }

    public boolean isCaseSensitive()
    {
        return baseType.isCaseSensitive();
    }

    public boolean isCurrency()
    {
        return baseType.isCurrency();
    }

    public int getPrecision(T obj)
    {
        return baseType.getPrecision(obj);
    }

    public int getScale(T obj)
    {
        return baseType.getScale(obj);
    }

    public int getJdbcType()
    {
        return baseType.getJdbcType();
    }

    public boolean needsQuotes()
    {
        return baseType.needsQuotes();
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "(" + baseType + ")";
    }
}
