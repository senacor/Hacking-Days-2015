/**
 * Autogenerated by Thrift Compiler (1.0.0-dev)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.senacor.hackingdays.serialization.data.thrift;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum Gender implements TEnum {
  Male(0),
  Female(1),
  Disambiguous(2);

  private final int value;

  private Gender(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static Gender findByValue(int value) { 
    switch (value) {
      case 0:
        return Male;
      case 1:
        return Female;
      case 2:
        return Disambiguous;
      default:
        return null;
    }
  }
}
