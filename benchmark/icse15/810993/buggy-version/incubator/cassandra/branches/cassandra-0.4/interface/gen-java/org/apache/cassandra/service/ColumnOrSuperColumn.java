/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package org.apache.cassandra.service;
/*
 * 
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
 * 
 */


import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.BitSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.thrift.*;
import org.apache.thrift.meta_data.*;
import org.apache.thrift.protocol.*;

public class ColumnOrSuperColumn implements TBase, java.io.Serializable, Cloneable, Comparable<ColumnOrSuperColumn> {
  private static final TStruct STRUCT_DESC = new TStruct("ColumnOrSuperColumn");
  private static final TField COLUMN_FIELD_DESC = new TField("column", TType.STRUCT, (short)1);
  private static final TField SUPER_COLUMN_FIELD_DESC = new TField("super_column", TType.STRUCT, (short)2);

  public Column column;
  public static final int COLUMN = 1;
  public SuperColumn super_column;
  public static final int SUPER_COLUMN = 2;

  // isset id assignments

  public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
    put(COLUMN, new FieldMetaData("column", TFieldRequirementType.OPTIONAL, 
        new StructMetaData(TType.STRUCT, Column.class)));
    put(SUPER_COLUMN, new FieldMetaData("super_column", TFieldRequirementType.OPTIONAL, 
        new StructMetaData(TType.STRUCT, SuperColumn.class)));
  }});

  static {
    FieldMetaData.addStructMetaDataMap(ColumnOrSuperColumn.class, metaDataMap);
  }

  public ColumnOrSuperColumn() {
  }

  public ColumnOrSuperColumn(
    Column column,
    SuperColumn super_column)
  {
    this();
    this.column = column;
    this.super_column = super_column;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ColumnOrSuperColumn(ColumnOrSuperColumn other) {
    if (other.isSetColumn()) {
      this.column = new Column(other.column);
    }
    if (other.isSetSuper_column()) {
      this.super_column = new SuperColumn(other.super_column);
    }
  }

  @Override
  public ColumnOrSuperColumn clone() {
    return new ColumnOrSuperColumn(this);
  }

  public Column getColumn() {
    return this.column;
  }

  public ColumnOrSuperColumn setColumn(Column column) {
    this.column = column;
    return this;
  }

  public void unsetColumn() {
    this.column = null;
  }

  // Returns true if field column is set (has been asigned a value) and false otherwise
  public boolean isSetColumn() {
    return this.column != null;
  }

  public void setColumnIsSet(boolean value) {
    if (!value) {
      this.column = null;
    }
  }

  public SuperColumn getSuper_column() {
    return this.super_column;
  }

  public ColumnOrSuperColumn setSuper_column(SuperColumn super_column) {
    this.super_column = super_column;
    return this;
  }

  public void unsetSuper_column() {
    this.super_column = null;
  }

  // Returns true if field super_column is set (has been asigned a value) and false otherwise
  public boolean isSetSuper_column() {
    return this.super_column != null;
  }

  public void setSuper_columnIsSet(boolean value) {
    if (!value) {
      this.super_column = null;
    }
  }

  public void setFieldValue(int fieldID, Object value) {
    switch (fieldID) {
    case COLUMN:
      if (value == null) {
        unsetColumn();
      } else {
        setColumn((Column)value);
      }
      break;

    case SUPER_COLUMN:
      if (value == null) {
        unsetSuper_column();
      } else {
        setSuper_column((SuperColumn)value);
      }
      break;

    default:
      throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
    }
  }

  public Object getFieldValue(int fieldID) {
    switch (fieldID) {
    case COLUMN:
      return getColumn();

    case SUPER_COLUMN:
      return getSuper_column();

    default:
      throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
    }
  }

  // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
  public boolean isSet(int fieldID) {
    switch (fieldID) {
    case COLUMN:
      return isSetColumn();
    case SUPER_COLUMN:
      return isSetSuper_column();
    default:
      throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
    }
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ColumnOrSuperColumn)
      return this.equals((ColumnOrSuperColumn)that);
    return false;
  }

  public boolean equals(ColumnOrSuperColumn that) {
    if (that == null)
      return false;

    boolean this_present_column = true && this.isSetColumn();
    boolean that_present_column = true && that.isSetColumn();
    if (this_present_column || that_present_column) {
      if (!(this_present_column && that_present_column))
        return false;
      if (!this.column.equals(that.column))
        return false;
    }

    boolean this_present_super_column = true && this.isSetSuper_column();
    boolean that_present_super_column = true && that.isSetSuper_column();
    if (this_present_super_column || that_present_super_column) {
      if (!(this_present_super_column && that_present_super_column))
        return false;
      if (!this.super_column.equals(that.super_column))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(ColumnOrSuperColumn other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    ColumnOrSuperColumn typedOther = (ColumnOrSuperColumn)other;

    lastComparison = Boolean.valueOf(isSetColumn()).compareTo(isSetColumn());
    if (lastComparison != 0) {
      return lastComparison;
    }
    lastComparison = TBaseHelper.compareTo(column, typedOther.column);
    if (lastComparison != 0) {
      return lastComparison;
    }
    lastComparison = Boolean.valueOf(isSetSuper_column()).compareTo(isSetSuper_column());
    if (lastComparison != 0) {
      return lastComparison;
    }
    lastComparison = TBaseHelper.compareTo(super_column, typedOther.super_column);
    if (lastComparison != 0) {
      return lastComparison;
    }
    return 0;
  }

  public void read(TProtocol iprot) throws TException {
    TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == TType.STOP) { 
        break;
      }
      switch (field.id)
      {
        case COLUMN:
          if (field.type == TType.STRUCT) {
            this.column = new Column();
            this.column.read(iprot);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case SUPER_COLUMN:
          if (field.type == TType.STRUCT) {
            this.super_column = new SuperColumn();
            this.super_column.read(iprot);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          TProtocolUtil.skip(iprot, field.type);
          break;
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();


    // check for required fields of primitive type, which can't be checked in the validate method
    validate();
  }

  public void write(TProtocol oprot) throws TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.column != null) {
      if (isSetColumn()) {
        oprot.writeFieldBegin(COLUMN_FIELD_DESC);
        this.column.write(oprot);
        oprot.writeFieldEnd();
      }
    }
    if (this.super_column != null) {
      if (isSetSuper_column()) {
        oprot.writeFieldBegin(SUPER_COLUMN_FIELD_DESC);
        this.super_column.write(oprot);
        oprot.writeFieldEnd();
      }
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ColumnOrSuperColumn(");
    boolean first = true;

    if (isSetColumn()) {
      sb.append("column:");
      if (this.column == null) {
        sb.append("null");
      } else {
        sb.append(this.column);
      }
      first = false;
    }
    if (isSetSuper_column()) {
      if (!first) sb.append(", ");
      sb.append("super_column:");
      if (this.super_column == null) {
        sb.append("null");
      } else {
        sb.append(this.super_column);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
    // check that fields of type enum have valid values
  }

}
