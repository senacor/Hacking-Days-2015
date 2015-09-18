// Generated by Cap'n Proto compiler, DO NOT EDIT
// source: Profile.schema

package com.senacor.hackingdays.serializer;

public final class CapnProtoProfile {
  public static class ProfileStruct {
    public static final org.capnproto.StructSize STRUCT_SIZE = new org.capnproto.StructSize((short)4,(short)4);
    public static final class Factory extends org.capnproto.StructFactory<Builder, Reader> {
      public Factory() {
      }
      public final Reader constructReader(org.capnproto.SegmentReader segment, int data,int pointers, int dataSize, short pointerCount, int nestingLimit) {
        return new Reader(segment,data,pointers,dataSize,pointerCount,nestingLimit);
      }
      public final Builder constructBuilder(org.capnproto.SegmentBuilder segment, int data,int pointers, int dataSize, short pointerCount) {
        return new Builder(segment, data, pointers, dataSize, pointerCount);
      }
      public final org.capnproto.StructSize structSize() {
        return ProfileStruct.STRUCT_SIZE;
      }
      public final Reader asReader(Builder builder) {
        return builder.asReader();
      }
    }
    public static final Factory factory = new Factory();
    public static final org.capnproto.StructList.Factory<Builder,Reader> listFactory =
      new org.capnproto.StructList.Factory<Builder, Reader>(factory);
    public static final class Builder extends org.capnproto.StructBuilder {
      Builder(org.capnproto.SegmentBuilder segment, int data, int pointers,int dataSize, short pointerCount){
        super(segment, data, pointers, dataSize, pointerCount);
      }
      public final Reader asReader() {
        return new Reader(segment, data, pointers, dataSize, pointerCount, 0x7fffffff);
      }
      public final int getAge() {
        return _getIntField(0);
      }
      public final void setAge(int value) {
        _setIntField(0, value);
      }

      public final boolean hasName() {
        return !_pointerFieldIsNull(0);
      }
      public final org.capnproto.Text.Builder getName() {
        return _getPointerField(org.capnproto.Text.factory, 0, null, 0, 0);
      }
      public final void setName(org.capnproto.Text.Reader value) {
        _setPointerField(org.capnproto.Text.factory, 0, value);
      }
      public final void setName(String value) {
        _setPointerField(org.capnproto.Text.factory, 0, new org.capnproto.Text.Reader(value));
      }
      public final org.capnproto.Text.Builder initName(int size) {
        return _initPointerField(org.capnproto.Text.factory, 0, size);
      }
      public final CapnProtoProfile.ProfileStruct.Gender getGender() {
        switch(_getShortField(2)) {
          case 0 : return CapnProtoProfile.ProfileStruct.Gender.MALE;
          case 1 : return CapnProtoProfile.ProfileStruct.Gender.FEMALE;
          case 2 : return CapnProtoProfile.ProfileStruct.Gender.DISAMBIGUOUS;
          default: return CapnProtoProfile.ProfileStruct.Gender._NOT_IN_SCHEMA;
        }
      }
      public final void setGender(CapnProtoProfile.ProfileStruct.Gender value) {
        _setShortField(2, (short)value.ordinal());
      }

      public final CapnProtoProfile.ProfileStruct.RelationShipStatus getRelationShip() {
        switch(_getShortField(3)) {
          case 0 : return CapnProtoProfile.ProfileStruct.RelationShipStatus.DIVORCED;
          case 1 : return CapnProtoProfile.ProfileStruct.RelationShipStatus.MARIED;
          case 2 : return CapnProtoProfile.ProfileStruct.RelationShipStatus.SINGLE;
          default: return CapnProtoProfile.ProfileStruct.RelationShipStatus._NOT_IN_SCHEMA;
        }
      }
      public final void setRelationShip(CapnProtoProfile.ProfileStruct.RelationShipStatus value) {
        _setShortField(3, (short)value.ordinal());
      }

      public final boolean getSmoker() {
        return _getBooleanField(64);
      }
      public final void setSmoker(boolean value) {
        _setBooleanField(64, value);
      }

      public final boolean hasLocationState() {
        return !_pointerFieldIsNull(1);
      }
      public final org.capnproto.Text.Builder getLocationState() {
        return _getPointerField(org.capnproto.Text.factory, 1, null, 0, 0);
      }
      public final void setLocationState(org.capnproto.Text.Reader value) {
        _setPointerField(org.capnproto.Text.factory, 1, value);
      }
      public final void setLocationState(String value) {
        _setPointerField(org.capnproto.Text.factory, 1, new org.capnproto.Text.Reader(value));
      }
      public final org.capnproto.Text.Builder initLocationState(int size) {
        return _initPointerField(org.capnproto.Text.factory, 1, size);
      }
      public final boolean hasLocationCity() {
        return !_pointerFieldIsNull(2);
      }
      public final org.capnproto.Text.Builder getLocationCity() {
        return _getPointerField(org.capnproto.Text.factory, 2, null, 0, 0);
      }
      public final void setLocationCity(org.capnproto.Text.Reader value) {
        _setPointerField(org.capnproto.Text.factory, 2, value);
      }
      public final void setLocationCity(String value) {
        _setPointerField(org.capnproto.Text.factory, 2, new org.capnproto.Text.Reader(value));
      }
      public final org.capnproto.Text.Builder initLocationCity(int size) {
        return _initPointerField(org.capnproto.Text.factory, 2, size);
      }
      public final boolean hasLocationZip() {
        return !_pointerFieldIsNull(3);
      }
      public final org.capnproto.Text.Builder getLocationZip() {
        return _getPointerField(org.capnproto.Text.factory, 3, null, 0, 0);
      }
      public final void setLocationZip(org.capnproto.Text.Reader value) {
        _setPointerField(org.capnproto.Text.factory, 3, value);
      }
      public final void setLocationZip(String value) {
        _setPointerField(org.capnproto.Text.factory, 3, new org.capnproto.Text.Reader(value));
      }
      public final org.capnproto.Text.Builder initLocationZip(int size) {
        return _initPointerField(org.capnproto.Text.factory, 3, size);
      }
      public final CapnProtoProfile.ProfileStruct.Gender getSeekingGender() {
        switch(_getShortField(5)) {
          case 0 : return CapnProtoProfile.ProfileStruct.Gender.MALE;
          case 1 : return CapnProtoProfile.ProfileStruct.Gender.FEMALE;
          case 2 : return CapnProtoProfile.ProfileStruct.Gender.DISAMBIGUOUS;
          default: return CapnProtoProfile.ProfileStruct.Gender._NOT_IN_SCHEMA;
        }
      }
      public final void setSeekingGender(CapnProtoProfile.ProfileStruct.Gender value) {
        _setShortField(5, (short)value.ordinal());
      }

      public final int getSeekingRangeLower() {
        return _getIntField(3);
      }
      public final void setSeekingRangeLower(int value) {
        _setIntField(3, value);
      }

      public final int getSeekingRangeUpper() {
        return _getIntField(4);
      }
      public final void setSeekingRangeUpper(int value) {
        _setIntField(4, value);
      }

      public final long getActivityLastLogin() {
        return _getLongField(3);
      }
      public final void setActivityLastLogin(long value) {
        _setLongField(3, value);
      }

      public final int getActivityLoginCount() {
        return _getIntField(5);
      }
      public final void setActivityLoginCount(int value) {
        _setIntField(5, value);
      }

    }

    public static final class Reader extends org.capnproto.StructReader {
      Reader(org.capnproto.SegmentReader segment, int data, int pointers,int dataSize, short pointerCount, int nestingLimit){
        super(segment, data, pointers, dataSize, pointerCount, nestingLimit);
      }

      public final int getAge() {
        return _getIntField(0);
      }

      public boolean hasName() {
        return !_pointerFieldIsNull(0);
      }
      public org.capnproto.Text.Reader getName() {
        return _getPointerField(org.capnproto.Text.factory, 0, null, 0, 0);
      }

      public final CapnProtoProfile.ProfileStruct.Gender getGender() {
        switch(_getShortField(2)) {
          case 0 : return CapnProtoProfile.ProfileStruct.Gender.MALE;
          case 1 : return CapnProtoProfile.ProfileStruct.Gender.FEMALE;
          case 2 : return CapnProtoProfile.ProfileStruct.Gender.DISAMBIGUOUS;
          default: return CapnProtoProfile.ProfileStruct.Gender._NOT_IN_SCHEMA;
        }
      }

      public final CapnProtoProfile.ProfileStruct.RelationShipStatus getRelationShip() {
        switch(_getShortField(3)) {
          case 0 : return CapnProtoProfile.ProfileStruct.RelationShipStatus.DIVORCED;
          case 1 : return CapnProtoProfile.ProfileStruct.RelationShipStatus.MARIED;
          case 2 : return CapnProtoProfile.ProfileStruct.RelationShipStatus.SINGLE;
          default: return CapnProtoProfile.ProfileStruct.RelationShipStatus._NOT_IN_SCHEMA;
        }
      }

      public final boolean getSmoker() {
        return _getBooleanField(64);
      }

      public boolean hasLocationState() {
        return !_pointerFieldIsNull(1);
      }
      public org.capnproto.Text.Reader getLocationState() {
        return _getPointerField(org.capnproto.Text.factory, 1, null, 0, 0);
      }

      public boolean hasLocationCity() {
        return !_pointerFieldIsNull(2);
      }
      public org.capnproto.Text.Reader getLocationCity() {
        return _getPointerField(org.capnproto.Text.factory, 2, null, 0, 0);
      }

      public boolean hasLocationZip() {
        return !_pointerFieldIsNull(3);
      }
      public org.capnproto.Text.Reader getLocationZip() {
        return _getPointerField(org.capnproto.Text.factory, 3, null, 0, 0);
      }

      public final CapnProtoProfile.ProfileStruct.Gender getSeekingGender() {
        switch(_getShortField(5)) {
          case 0 : return CapnProtoProfile.ProfileStruct.Gender.MALE;
          case 1 : return CapnProtoProfile.ProfileStruct.Gender.FEMALE;
          case 2 : return CapnProtoProfile.ProfileStruct.Gender.DISAMBIGUOUS;
          default: return CapnProtoProfile.ProfileStruct.Gender._NOT_IN_SCHEMA;
        }
      }

      public final int getSeekingRangeLower() {
        return _getIntField(3);
      }

      public final int getSeekingRangeUpper() {
        return _getIntField(4);
      }

      public final long getActivityLastLogin() {
        return _getLongField(3);
      }

      public final int getActivityLoginCount() {
        return _getIntField(5);
      }

    }

    public enum Gender {
      MALE,
      FEMALE,
      DISAMBIGUOUS,
      _NOT_IN_SCHEMA,
    }

    public enum RelationShipStatus {
      DIVORCED,
      MARIED,
      SINGLE,
      _NOT_IN_SCHEMA,
    }

  }



public static final class Schemas {
public static final org.capnproto.SegmentReader b_bc2f1293c7791b93 =
   org.capnproto.GeneratedClassSupport.decodeRawBytes(
   "\u0000\u0000\u0000\u0000\u0005\u0000\u0006\u0000" +
   "\u0093\u001b\u0079\u00c7\u0093\u0012\u002f\u00bc" +
   "\u000f\u0000\u0000\u0000\u0001\u0000\u0004\u0000" +
   "\u0074\u00e1\u006e\u00f8\u0019\u002e\u00b3\u009e" +
   "\u0004\u0000\u0007\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0015\u0000\u0000\u0000\u00ea\u0000\u0000\u0000" +
   "\u0021\u0000\u0000\u0000\u0027\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u003d\u0000\u0000\u0000\u00df\u0002\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0050\u0072\u006f\u0066\u0069\u006c\u0065\u002e" +
   "\u0073\u0063\u0068\u0065\u006d\u0061\u003a\u0050" +
   "\u0072\u006f\u0066\u0069\u006c\u0065\u0053\u0074" +
   "\u0072\u0075\u0063\u0074\u0000\u0000\u0000\u0000" +
   "\u0008\u0000\u0000\u0000\u0001\u0000\u0001\u0000" +
   "\u00e5\u0081\u0092\u0060\"\u005d\u0057\u00c4" +
   "\u0009\u0000\u0000\u0000\u003a\u0000\u0000\u0000" +
   "\u00e1\u00a9\u003c\u009f\u0008\u0083\u004b\u009a" +
   "\u0005\u0000\u0000\u0000\u009a\u0000\u0000\u0000" +
   "\u0047\u0065\u006e\u0064\u0065\u0072\u0000\u0000" +
   "\u0052\u0065\u006c\u0061\u0074\u0069\u006f\u006e" +
   "\u0053\u0068\u0069\u0070\u0053\u0074\u0061\u0074" +
   "\u0075\u0073\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0034\u0000\u0000\u0000\u0003\u0000\u0004\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u005d\u0001\u0000\u0000\"\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0058\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0064\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0001\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0061\u0001\u0000\u0000\u002a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\\\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0068\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0002\u0000\u0000\u0000\u0002\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0002\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0065\u0001\u0000\u0000\u003a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0060\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u006c\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0003\u0000\u0000\u0000\u0003\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0003\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0069\u0001\u0000\u0000\u006a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0068\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0074\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0004\u0000\u0000\u0000\u0040\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0004\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0071\u0001\u0000\u0000\u003a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u006c\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0078\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0005\u0000\u0000\u0000\u0001\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0005\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0075\u0001\u0000\u0000\u0072\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0074\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0080\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0006\u0000\u0000\u0000\u0002\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0006\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u007d\u0001\u0000\u0000\u006a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u007c\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0088\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0007\u0000\u0000\u0000\u0003\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0007\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0085\u0001\u0000\u0000\u0062\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0084\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0090\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0008\u0000\u0000\u0000\u0005\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0008\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u008d\u0001\u0000\u0000\u0072\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u008c\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0098\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0009\u0000\u0000\u0000\u0003\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0009\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0095\u0001\u0000\u0000\u0092\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0098\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u00a4\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\n\u0000\u0000\u0000\u0004\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\n\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00a1\u0001\u0000\u0000\u0092\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00a4\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u00b0\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u000b\u0000\u0000\u0000\u0003\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u000b\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00ad\u0001\u0000\u0000\u0092\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00b0\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u00bc\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u000c\u0000\u0000\u0000\u0005\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u000c\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00b9\u0001\u0000\u0000\u009a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00bc\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u00c8\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0061\u0067\u0065\u0000\u0000\u0000\u0000\u0000" +
   "\u0008\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0008\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u006e\u0061\u006d\u0065\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0067\u0065\u006e\u0064\u0065\u0072\u0000\u0000" +
   "\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00e5\u0081\u0092\u0060\"\u005d\u0057\u00c4" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0072\u0065\u006c\u0061\u0074\u0069\u006f\u006e" +
   "\u0053\u0068\u0069\u0070\u0000\u0000\u0000\u0000" +
   "\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00e1\u00a9\u003c\u009f\u0008\u0083\u004b\u009a" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0073\u006d\u006f\u006b\u0065\u0072\u0000\u0000" +
   "\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u006c\u006f\u0063\u0061\u0074\u0069\u006f\u006e" +
   "\u0053\u0074\u0061\u0074\u0065\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u006c\u006f\u0063\u0061\u0074\u0069\u006f\u006e" +
   "\u0043\u0069\u0074\u0079\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u006c\u006f\u0063\u0061\u0074\u0069\u006f\u006e" +
   "\u005a\u0069\u0070\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0073\u0065\u0065\u006b\u0069\u006e\u0067\u0047" +
   "\u0065\u006e\u0064\u0065\u0072\u0000\u0000\u0000" +
   "\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00e5\u0081\u0092\u0060\"\u005d\u0057\u00c4" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0073\u0065\u0065\u006b\u0069\u006e\u0067\u0052" +
   "\u0061\u006e\u0067\u0065\u004c\u006f\u0077\u0065" +
   "\u0072\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0008\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0008\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0073\u0065\u0065\u006b\u0069\u006e\u0067\u0052" +
   "\u0061\u006e\u0067\u0065\u0055\u0070\u0070\u0065" +
   "\u0072\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0008\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0008\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0061\u0063\u0074\u0069\u0076\u0069\u0074\u0079" +
   "\u004c\u0061\u0073\u0074\u004c\u006f\u0067\u0069" +
   "\u006e\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0061\u0063\u0074\u0069\u0076\u0069\u0074\u0079" +
   "\u004c\u006f\u0067\u0069\u006e\u0043\u006f\u0075" +
   "\u006e\u0074\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0008\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0008\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + "");
public static final org.capnproto.SegmentReader b_c4575d22609281e5 =
   org.capnproto.GeneratedClassSupport.decodeRawBytes(
   "\u0000\u0000\u0000\u0000\u0005\u0000\u0006\u0000" +
   "\u00e5\u0081\u0092\u0060\"\u005d\u0057\u00c4" +
   "\u001d\u0000\u0000\u0000\u0002\u0000\u0000\u0000" +
   "\u0093\u001b\u0079\u00c7\u0093\u0012\u002f\u00bc" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0015\u0000\u0000\u0000\"\u0001\u0000\u0000" +
   "\u0025\u0000\u0000\u0000\u0007\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0021\u0000\u0000\u0000\u004f\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0050\u0072\u006f\u0066\u0069\u006c\u0065\u002e" +
   "\u0073\u0063\u0068\u0065\u006d\u0061\u003a\u0050" +
   "\u0072\u006f\u0066\u0069\u006c\u0065\u0053\u0074" +
   "\u0072\u0075\u0063\u0074\u002e\u0047\u0065\u006e" +
   "\u0064\u0065\u0072\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0001\u0000\u0001\u0000" +
   "\u000c\u0000\u0000\u0000\u0001\u0000\u0002\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u001d\u0000\u0000\u0000\u002a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0015\u0000\u0000\u0000\u003a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0002\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\r\u0000\u0000\u0000\u006a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u006d\u0061\u006c\u0065\u0000\u0000\u0000\u0000" +
   "\u0066\u0065\u006d\u0061\u006c\u0065\u0000\u0000" +
   "\u0064\u0069\u0073\u0061\u006d\u0062\u0069\u0067" +
   "\u0075\u006f\u0075\u0073\u0000\u0000\u0000\u0000" + "");
public static final org.capnproto.SegmentReader b_9a4b83089f3ca9e1 =
   org.capnproto.GeneratedClassSupport.decodeRawBytes(
   "\u0000\u0000\u0000\u0000\u0005\u0000\u0006\u0000" +
   "\u00e1\u00a9\u003c\u009f\u0008\u0083\u004b\u009a" +
   "\u001d\u0000\u0000\u0000\u0002\u0000\u0000\u0000" +
   "\u0093\u001b\u0079\u00c7\u0093\u0012\u002f\u00bc" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0015\u0000\u0000\u0000\u0082\u0001\u0000\u0000" +
   "\u0029\u0000\u0000\u0000\u0007\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0025\u0000\u0000\u0000\u004f\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0050\u0072\u006f\u0066\u0069\u006c\u0065\u002e" +
   "\u0073\u0063\u0068\u0065\u006d\u0061\u003a\u0050" +
   "\u0072\u006f\u0066\u0069\u006c\u0065\u0053\u0074" +
   "\u0072\u0075\u0063\u0074\u002e\u0052\u0065\u006c" +
   "\u0061\u0074\u0069\u006f\u006e\u0053\u0068\u0069" +
   "\u0070\u0053\u0074\u0061\u0074\u0075\u0073\u0000" +
   "\u0000\u0000\u0000\u0000\u0001\u0000\u0001\u0000" +
   "\u000c\u0000\u0000\u0000\u0001\u0000\u0002\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u001d\u0000\u0000\u0000\u004a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0019\u0000\u0000\u0000\u003a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0002\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0011\u0000\u0000\u0000\u003a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0064\u0069\u0076\u006f\u0072\u0063\u0065\u0064" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u006d\u0061\u0072\u0069\u0065\u0064\u0000\u0000" +
   "\u0073\u0069\u006e\u0067\u006c\u0065\u0000\u0000" + "");
}
}

