/**
 * Autogenerated by Thrift Compiler (1.0.0-dev)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.senacor.hackingdays.serialization.data.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (1.0.0-dev)", date = "2015-09-18")
public class Range implements org.apache.thrift.TBase<Range, Range._Fields>, java.io.Serializable, Cloneable, Comparable<Range> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Range");

  private static final org.apache.thrift.protocol.TField LOWER_FIELD_DESC = new org.apache.thrift.protocol.TField("lower", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField UPPER_FIELD_DESC = new org.apache.thrift.protocol.TField("upper", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new RangeStandardSchemeFactory());
    schemes.put(TupleScheme.class, new RangeTupleSchemeFactory());
  }

  public int lower; // required
  public int upper; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LOWER((short)1, "lower"),
    UPPER((short)2, "upper");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // LOWER
          return LOWER;
        case 2: // UPPER
          return UPPER;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __LOWER_ISSET_ID = 0;
  private static final int __UPPER_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LOWER, new org.apache.thrift.meta_data.FieldMetaData("lower", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.UPPER, new org.apache.thrift.meta_data.FieldMetaData("upper", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Range.class, metaDataMap);
  }

  public Range() {
  }

  public Range(
    int lower,
    int upper)
  {
    this();
    this.lower = lower;
    setLowerIsSet(true);
    this.upper = upper;
    setUpperIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Range(Range other) {
    __isset_bitfield = other.__isset_bitfield;
    this.lower = other.lower;
    this.upper = other.upper;
  }

  public Range deepCopy() {
    return new Range(this);
  }

  @Override
  public void clear() {
    setLowerIsSet(false);
    this.lower = 0;
    setUpperIsSet(false);
    this.upper = 0;
  }

  public int getLower() {
    return this.lower;
  }

  public Range setLower(int lower) {
    this.lower = lower;
    setLowerIsSet(true);
    return this;
  }

  public void unsetLower() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __LOWER_ISSET_ID);
  }

  /** Returns true if field lower is set (has been assigned a value) and false otherwise */
  public boolean isSetLower() {
    return EncodingUtils.testBit(__isset_bitfield, __LOWER_ISSET_ID);
  }

  public void setLowerIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __LOWER_ISSET_ID, value);
  }

  public int getUpper() {
    return this.upper;
  }

  public Range setUpper(int upper) {
    this.upper = upper;
    setUpperIsSet(true);
    return this;
  }

  public void unsetUpper() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __UPPER_ISSET_ID);
  }

  /** Returns true if field upper is set (has been assigned a value) and false otherwise */
  public boolean isSetUpper() {
    return EncodingUtils.testBit(__isset_bitfield, __UPPER_ISSET_ID);
  }

  public void setUpperIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __UPPER_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case LOWER:
      if (value == null) {
        unsetLower();
      } else {
        setLower((Integer)value);
      }
      break;

    case UPPER:
      if (value == null) {
        unsetUpper();
      } else {
        setUpper((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case LOWER:
      return getLower();

    case UPPER:
      return getUpper();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case LOWER:
      return isSetLower();
    case UPPER:
      return isSetUpper();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Range)
      return this.equals((Range)that);
    return false;
  }

  public boolean equals(Range that) {
    if (that == null)
      return false;

    boolean this_present_lower = true;
    boolean that_present_lower = true;
    if (this_present_lower || that_present_lower) {
      if (!(this_present_lower && that_present_lower))
        return false;
      if (this.lower != that.lower)
        return false;
    }

    boolean this_present_upper = true;
    boolean that_present_upper = true;
    if (this_present_upper || that_present_upper) {
      if (!(this_present_upper && that_present_upper))
        return false;
      if (this.upper != that.upper)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_lower = true;
    list.add(present_lower);
    if (present_lower)
      list.add(lower);

    boolean present_upper = true;
    list.add(present_upper);
    if (present_upper)
      list.add(upper);

    return list.hashCode();
  }

  @Override
  public int compareTo(Range other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetLower()).compareTo(other.isSetLower());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLower()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.lower, other.lower);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUpper()).compareTo(other.isSetUpper());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUpper()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.upper, other.upper);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Range(");
    boolean first = true;

    sb.append("lower:");
    sb.append(this.lower);
    first = false;
    if (!first) sb.append(", ");
    sb.append("upper:");
    sb.append(this.upper);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RangeStandardSchemeFactory implements SchemeFactory {
    public RangeStandardScheme getScheme() {
      return new RangeStandardScheme();
    }
  }

  private static class RangeStandardScheme extends StandardScheme<Range> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Range struct) throws TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LOWER
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.lower = iprot.readI32();
              struct.setLowerIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // UPPER
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.upper = iprot.readI32();
              struct.setUpperIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Range struct) throws TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(LOWER_FIELD_DESC);
      oprot.writeI32(struct.lower);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(UPPER_FIELD_DESC);
      oprot.writeI32(struct.upper);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RangeTupleSchemeFactory implements SchemeFactory {
    public RangeTupleScheme getScheme() {
      return new RangeTupleScheme();
    }
  }

  private static class RangeTupleScheme extends TupleScheme<Range> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Range struct) throws TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetLower()) {
        optionals.set(0);
      }
      if (struct.isSetUpper()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetLower()) {
        oprot.writeI32(struct.lower);
      }
      if (struct.isSetUpper()) {
        oprot.writeI32(struct.upper);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Range struct) throws TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.lower = iprot.readI32();
        struct.setLowerIsSet(true);
      }
      if (incoming.get(1)) {
        struct.upper = iprot.readI32();
        struct.setUpperIsSet(true);
      }
    }
  }

}

