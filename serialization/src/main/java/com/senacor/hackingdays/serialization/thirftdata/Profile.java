/**
 * Autogenerated by Thrift Compiler (1.0.0-dev)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.senacor.hackingdays.serialization.thirftdata;

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
public class Profile implements org.apache.thrift.TBase<Profile, Profile._Fields>, java.io.Serializable, Cloneable, Comparable<Profile> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Profile");

  private static final org.apache.thrift.protocol.TField NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("name", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField GENDER_FIELD_DESC = new org.apache.thrift.protocol.TField("gender", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField AGE_FIELD_DESC = new org.apache.thrift.protocol.TField("age", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField LOCATION_FIELD_DESC = new org.apache.thrift.protocol.TField("location", org.apache.thrift.protocol.TType.STRUCT, (short)4);
  private static final org.apache.thrift.protocol.TField RELATION_SHIP_FIELD_DESC = new org.apache.thrift.protocol.TField("relationShip", org.apache.thrift.protocol.TType.I32, (short)5);
  private static final org.apache.thrift.protocol.TField SMOKER_FIELD_DESC = new org.apache.thrift.protocol.TField("smoker", org.apache.thrift.protocol.TType.BOOL, (short)6);
  private static final org.apache.thrift.protocol.TField SEEKING_FIELD_DESC = new org.apache.thrift.protocol.TField("seeking", org.apache.thrift.protocol.TType.STRUCT, (short)7);
  private static final org.apache.thrift.protocol.TField ACTIVITY_FIELD_DESC = new org.apache.thrift.protocol.TField("activity", org.apache.thrift.protocol.TType.STRUCT, (short)8);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ProfileStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ProfileTupleSchemeFactory());
  }

  public String name; // required
  public int gender; // required
  public int age; // required
  public Location location; // required
  public int relationShip; // required
  public boolean smoker; // required
  public Seeking seeking; // required
  public Activity activity; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NAME((short)1, "name"),
    GENDER((short)2, "gender"),
    AGE((short)3, "age"),
    LOCATION((short)4, "location"),
    RELATION_SHIP((short)5, "relationShip"),
    SMOKER((short)6, "smoker"),
    SEEKING((short)7, "seeking"),
    ACTIVITY((short)8, "activity");

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
        case 1: // NAME
          return NAME;
        case 2: // GENDER
          return GENDER;
        case 3: // AGE
          return AGE;
        case 4: // LOCATION
          return LOCATION;
        case 5: // RELATION_SHIP
          return RELATION_SHIP;
        case 6: // SMOKER
          return SMOKER;
        case 7: // SEEKING
          return SEEKING;
        case 8: // ACTIVITY
          return ACTIVITY;
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
  private static final int __GENDER_ISSET_ID = 0;
  private static final int __AGE_ISSET_ID = 1;
  private static final int __RELATIONSHIP_ISSET_ID = 2;
  private static final int __SMOKER_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NAME, new org.apache.thrift.meta_data.FieldMetaData("name", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.GENDER, new org.apache.thrift.meta_data.FieldMetaData("gender", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.AGE, new org.apache.thrift.meta_data.FieldMetaData("age", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.LOCATION, new org.apache.thrift.meta_data.FieldMetaData("location", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRUCT        , "Location")));
    tmpMap.put(_Fields.RELATION_SHIP, new org.apache.thrift.meta_data.FieldMetaData("relationShip", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.SMOKER, new org.apache.thrift.meta_data.FieldMetaData("smoker", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.SEEKING, new org.apache.thrift.meta_data.FieldMetaData("seeking", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRUCT        , "Seeking")));
    tmpMap.put(_Fields.ACTIVITY, new org.apache.thrift.meta_data.FieldMetaData("activity", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRUCT        , "Activity")));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Profile.class, metaDataMap);
  }

  public Profile() {
  }

  public Profile(
    String name,
    int gender,
    int age,
    Location location,
    int relationShip,
    boolean smoker,
    Seeking seeking,
    Activity activity)
  {
    this();
    this.name = name;
    this.gender = gender;
    setGenderIsSet(true);
    this.age = age;
    setAgeIsSet(true);
    this.location = location;
    this.relationShip = relationShip;
    setRelationShipIsSet(true);
    this.smoker = smoker;
    setSmokerIsSet(true);
    this.seeking = seeking;
    this.activity = activity;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Profile(Profile other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetName()) {
      this.name = other.name;
    }
    this.gender = other.gender;
    this.age = other.age;
    if (other.isSetLocation()) {
      this.location = other.location;
    }
    this.relationShip = other.relationShip;
    this.smoker = other.smoker;
    if (other.isSetSeeking()) {
      this.seeking = other.seeking;
    }
    if (other.isSetActivity()) {
      this.activity = other.activity;
    }
  }

  public Profile deepCopy() {
    return new Profile(this);
  }

  @Override
  public void clear() {
    this.name = null;
    setGenderIsSet(false);
    this.gender = 0;
    setAgeIsSet(false);
    this.age = 0;
    this.location = null;
    setRelationShipIsSet(false);
    this.relationShip = 0;
    setSmokerIsSet(false);
    this.smoker = false;
    this.seeking = null;
    this.activity = null;
  }

  public String getName() {
    return this.name;
  }

  public Profile setName(String name) {
    this.name = name;
    return this;
  }

  public void unsetName() {
    this.name = null;
  }

  /** Returns true if field name is set (has been assigned a value) and false otherwise */
  public boolean isSetName() {
    return this.name != null;
  }

  public void setNameIsSet(boolean value) {
    if (!value) {
      this.name = null;
    }
  }

  public int getGender() {
    return this.gender;
  }

  public Profile setGender(int gender) {
    this.gender = gender;
    setGenderIsSet(true);
    return this;
  }

  public void unsetGender() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __GENDER_ISSET_ID);
  }

  /** Returns true if field gender is set (has been assigned a value) and false otherwise */
  public boolean isSetGender() {
    return EncodingUtils.testBit(__isset_bitfield, __GENDER_ISSET_ID);
  }

  public void setGenderIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __GENDER_ISSET_ID, value);
  }

  public int getAge() {
    return this.age;
  }

  public Profile setAge(int age) {
    this.age = age;
    setAgeIsSet(true);
    return this;
  }

  public void unsetAge() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __AGE_ISSET_ID);
  }

  /** Returns true if field age is set (has been assigned a value) and false otherwise */
  public boolean isSetAge() {
    return EncodingUtils.testBit(__isset_bitfield, __AGE_ISSET_ID);
  }

  public void setAgeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __AGE_ISSET_ID, value);
  }

  public Location getLocation() {
    return this.location;
  }

  public Profile setLocation(Location location) {
    this.location = location;
    return this;
  }

  public void unsetLocation() {
    this.location = null;
  }

  /** Returns true if field location is set (has been assigned a value) and false otherwise */
  public boolean isSetLocation() {
    return this.location != null;
  }

  public void setLocationIsSet(boolean value) {
    if (!value) {
      this.location = null;
    }
  }

  public int getRelationShip() {
    return this.relationShip;
  }

  public Profile setRelationShip(int relationShip) {
    this.relationShip = relationShip;
    setRelationShipIsSet(true);
    return this;
  }

  public void unsetRelationShip() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __RELATIONSHIP_ISSET_ID);
  }

  /** Returns true if field relationShip is set (has been assigned a value) and false otherwise */
  public boolean isSetRelationShip() {
    return EncodingUtils.testBit(__isset_bitfield, __RELATIONSHIP_ISSET_ID);
  }

  public void setRelationShipIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __RELATIONSHIP_ISSET_ID, value);
  }

  public boolean isSmoker() {
    return this.smoker;
  }

  public Profile setSmoker(boolean smoker) {
    this.smoker = smoker;
    setSmokerIsSet(true);
    return this;
  }

  public void unsetSmoker() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SMOKER_ISSET_ID);
  }

  /** Returns true if field smoker is set (has been assigned a value) and false otherwise */
  public boolean isSetSmoker() {
    return EncodingUtils.testBit(__isset_bitfield, __SMOKER_ISSET_ID);
  }

  public void setSmokerIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SMOKER_ISSET_ID, value);
  }

  public Seeking getSeeking() {
    return this.seeking;
  }

  public Profile setSeeking(Seeking seeking) {
    this.seeking = seeking;
    return this;
  }

  public void unsetSeeking() {
    this.seeking = null;
  }

  /** Returns true if field seeking is set (has been assigned a value) and false otherwise */
  public boolean isSetSeeking() {
    return this.seeking != null;
  }

  public void setSeekingIsSet(boolean value) {
    if (!value) {
      this.seeking = null;
    }
  }

  public Activity getActivity() {
    return this.activity;
  }

  public Profile setActivity(Activity activity) {
    this.activity = activity;
    return this;
  }

  public void unsetActivity() {
    this.activity = null;
  }

  /** Returns true if field activity is set (has been assigned a value) and false otherwise */
  public boolean isSetActivity() {
    return this.activity != null;
  }

  public void setActivityIsSet(boolean value) {
    if (!value) {
      this.activity = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case NAME:
      if (value == null) {
        unsetName();
      } else {
        setName((String)value);
      }
      break;

    case GENDER:
      if (value == null) {
        unsetGender();
      } else {
        setGender((Integer)value);
      }
      break;

    case AGE:
      if (value == null) {
        unsetAge();
      } else {
        setAge((Integer)value);
      }
      break;

    case LOCATION:
      if (value == null) {
        unsetLocation();
      } else {
        setLocation((Location)value);
      }
      break;

    case RELATION_SHIP:
      if (value == null) {
        unsetRelationShip();
      } else {
        setRelationShip((Integer)value);
      }
      break;

    case SMOKER:
      if (value == null) {
        unsetSmoker();
      } else {
        setSmoker((Boolean)value);
      }
      break;

    case SEEKING:
      if (value == null) {
        unsetSeeking();
      } else {
        setSeeking((Seeking)value);
      }
      break;

    case ACTIVITY:
      if (value == null) {
        unsetActivity();
      } else {
        setActivity((Activity)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case NAME:
      return getName();

    case GENDER:
      return getGender();

    case AGE:
      return getAge();

    case LOCATION:
      return getLocation();

    case RELATION_SHIP:
      return getRelationShip();

    case SMOKER:
      return isSmoker();

    case SEEKING:
      return getSeeking();

    case ACTIVITY:
      return getActivity();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case NAME:
      return isSetName();
    case GENDER:
      return isSetGender();
    case AGE:
      return isSetAge();
    case LOCATION:
      return isSetLocation();
    case RELATION_SHIP:
      return isSetRelationShip();
    case SMOKER:
      return isSetSmoker();
    case SEEKING:
      return isSetSeeking();
    case ACTIVITY:
      return isSetActivity();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Profile)
      return this.equals((Profile)that);
    return false;
  }

  public boolean equals(Profile that) {
    if (that == null)
      return false;

    boolean this_present_name = true && this.isSetName();
    boolean that_present_name = true && that.isSetName();
    if (this_present_name || that_present_name) {
      if (!(this_present_name && that_present_name))
        return false;
      if (!this.name.equals(that.name))
        return false;
    }

    boolean this_present_gender = true;
    boolean that_present_gender = true;
    if (this_present_gender || that_present_gender) {
      if (!(this_present_gender && that_present_gender))
        return false;
      if (this.gender != that.gender)
        return false;
    }

    boolean this_present_age = true;
    boolean that_present_age = true;
    if (this_present_age || that_present_age) {
      if (!(this_present_age && that_present_age))
        return false;
      if (this.age != that.age)
        return false;
    }

    boolean this_present_location = true && this.isSetLocation();
    boolean that_present_location = true && that.isSetLocation();
    if (this_present_location || that_present_location) {
      if (!(this_present_location && that_present_location))
        return false;
      if (!this.location.equals(that.location))
        return false;
    }

    boolean this_present_relationShip = true;
    boolean that_present_relationShip = true;
    if (this_present_relationShip || that_present_relationShip) {
      if (!(this_present_relationShip && that_present_relationShip))
        return false;
      if (this.relationShip != that.relationShip)
        return false;
    }

    boolean this_present_smoker = true;
    boolean that_present_smoker = true;
    if (this_present_smoker || that_present_smoker) {
      if (!(this_present_smoker && that_present_smoker))
        return false;
      if (this.smoker != that.smoker)
        return false;
    }

    boolean this_present_seeking = true && this.isSetSeeking();
    boolean that_present_seeking = true && that.isSetSeeking();
    if (this_present_seeking || that_present_seeking) {
      if (!(this_present_seeking && that_present_seeking))
        return false;
      if (!this.seeking.equals(that.seeking))
        return false;
    }

    boolean this_present_activity = true && this.isSetActivity();
    boolean that_present_activity = true && that.isSetActivity();
    if (this_present_activity || that_present_activity) {
      if (!(this_present_activity && that_present_activity))
        return false;
      if (!this.activity.equals(that.activity))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_name = true && (isSetName());
    list.add(present_name);
    if (present_name)
      list.add(name);

    boolean present_gender = true;
    list.add(present_gender);
    if (present_gender)
      list.add(gender);

    boolean present_age = true;
    list.add(present_age);
    if (present_age)
      list.add(age);

    boolean present_location = true && (isSetLocation());
    list.add(present_location);
    if (present_location)
      list.add(location);

    boolean present_relationShip = true;
    list.add(present_relationShip);
    if (present_relationShip)
      list.add(relationShip);

    boolean present_smoker = true;
    list.add(present_smoker);
    if (present_smoker)
      list.add(smoker);

    boolean present_seeking = true && (isSetSeeking());
    list.add(present_seeking);
    if (present_seeking)
      list.add(seeking);

    boolean present_activity = true && (isSetActivity());
    list.add(present_activity);
    if (present_activity)
      list.add(activity);

    return list.hashCode();
  }

  @Override
  public int compareTo(Profile other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetName()).compareTo(other.isSetName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.name, other.name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetGender()).compareTo(other.isSetGender());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGender()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.gender, other.gender);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAge()).compareTo(other.isSetAge());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAge()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.age, other.age);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetLocation()).compareTo(other.isSetLocation());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLocation()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.location, other.location);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRelationShip()).compareTo(other.isSetRelationShip());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRelationShip()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.relationShip, other.relationShip);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSmoker()).compareTo(other.isSetSmoker());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSmoker()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.smoker, other.smoker);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSeeking()).compareTo(other.isSetSeeking());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSeeking()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.seeking, other.seeking);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetActivity()).compareTo(other.isSetActivity());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetActivity()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.activity, other.activity);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Profile(");
    boolean first = true;

    sb.append("name:");
    if (this.name == null) {
      sb.append("null");
    } else {
      sb.append(this.name);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("gender:");
    sb.append(this.gender);
    first = false;
    if (!first) sb.append(", ");
    sb.append("age:");
    sb.append(this.age);
    first = false;
    if (!first) sb.append(", ");
    sb.append("location:");
    if (this.location == null) {
      sb.append("null");
    } else {
      sb.append(this.location);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("relationShip:");
    sb.append(this.relationShip);
    first = false;
    if (!first) sb.append(", ");
    sb.append("smoker:");
    sb.append(this.smoker);
    first = false;
    if (!first) sb.append(", ");
    sb.append("seeking:");
    if (this.seeking == null) {
      sb.append("null");
    } else {
      sb.append(this.seeking);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("activity:");
    if (this.activity == null) {
      sb.append("null");
    } else {
      sb.append(this.activity);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ProfileStandardSchemeFactory implements SchemeFactory {
    public ProfileStandardScheme getScheme() {
      return new ProfileStandardScheme();
    }
  }

  private static class ProfileStandardScheme extends StandardScheme<Profile> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Profile struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.name = iprot.readString();
              struct.setNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // GENDER
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.gender = iprot.readI32();
              struct.setGenderIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // AGE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.age = iprot.readI32();
              struct.setAgeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // LOCATION
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.location = new Location();
              struct.location.read(iprot);
              struct.setLocationIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // RELATION_SHIP
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.relationShip = iprot.readI32();
              struct.setRelationShipIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // SMOKER
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.smoker = iprot.readBool();
              struct.setSmokerIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // SEEKING
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.seeking = new Seeking();
              struct.seeking.read(iprot);
              struct.setSeekingIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 8: // ACTIVITY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.activity = new Activity();
              struct.activity.read(iprot);
              struct.setActivityIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Profile struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.name != null) {
        oprot.writeFieldBegin(NAME_FIELD_DESC);
        oprot.writeString(struct.name);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(GENDER_FIELD_DESC);
      oprot.writeI32(struct.gender);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(AGE_FIELD_DESC);
      oprot.writeI32(struct.age);
      oprot.writeFieldEnd();
      if (struct.location != null) {
        oprot.writeFieldBegin(LOCATION_FIELD_DESC);
        struct.location.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(RELATION_SHIP_FIELD_DESC);
      oprot.writeI32(struct.relationShip);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(SMOKER_FIELD_DESC);
      oprot.writeBool(struct.smoker);
      oprot.writeFieldEnd();
      if (struct.seeking != null) {
        oprot.writeFieldBegin(SEEKING_FIELD_DESC);
        struct.seeking.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.activity != null) {
        oprot.writeFieldBegin(ACTIVITY_FIELD_DESC);
        struct.activity.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ProfileTupleSchemeFactory implements SchemeFactory {
    public ProfileTupleScheme getScheme() {
      return new ProfileTupleScheme();
    }
  }

  private static class ProfileTupleScheme extends TupleScheme<Profile> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Profile struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetName()) {
        optionals.set(0);
      }
      if (struct.isSetGender()) {
        optionals.set(1);
      }
      if (struct.isSetAge()) {
        optionals.set(2);
      }
      if (struct.isSetLocation()) {
        optionals.set(3);
      }
      if (struct.isSetRelationShip()) {
        optionals.set(4);
      }
      if (struct.isSetSmoker()) {
        optionals.set(5);
      }
      if (struct.isSetSeeking()) {
        optionals.set(6);
      }
      if (struct.isSetActivity()) {
        optionals.set(7);
      }
      oprot.writeBitSet(optionals, 8);
      if (struct.isSetName()) {
        oprot.writeString(struct.name);
      }
      if (struct.isSetGender()) {
        oprot.writeI32(struct.gender);
      }
      if (struct.isSetAge()) {
        oprot.writeI32(struct.age);
      }
      if (struct.isSetLocation()) {
        struct.location.write(oprot);
      }
      if (struct.isSetRelationShip()) {
        oprot.writeI32(struct.relationShip);
      }
      if (struct.isSetSmoker()) {
        oprot.writeBool(struct.smoker);
      }
      if (struct.isSetSeeking()) {
        struct.seeking.write(oprot);
      }
      if (struct.isSetActivity()) {
        struct.activity.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Profile struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(8);
      if (incoming.get(0)) {
        struct.name = iprot.readString();
        struct.setNameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.gender = iprot.readI32();
        struct.setGenderIsSet(true);
      }
      if (incoming.get(2)) {
        struct.age = iprot.readI32();
        struct.setAgeIsSet(true);
      }
      if (incoming.get(3)) {
        struct.location = new Location();
        struct.location.read(iprot);
        struct.setLocationIsSet(true);
      }
      if (incoming.get(4)) {
        struct.relationShip = iprot.readI32();
        struct.setRelationShipIsSet(true);
      }
      if (incoming.get(5)) {
        struct.smoker = iprot.readBool();
        struct.setSmokerIsSet(true);
      }
      if (incoming.get(6)) {
        struct.seeking = new Seeking();
        struct.seeking.read(iprot);
        struct.setSeekingIsSet(true);
      }
      if (incoming.get(7)) {
        struct.activity = new Activity();
        struct.activity.read(iprot);
        struct.setActivityIsSet(true);
      }
    }
  }

}

