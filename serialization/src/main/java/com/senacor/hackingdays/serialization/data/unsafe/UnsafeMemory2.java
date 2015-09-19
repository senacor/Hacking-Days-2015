/*
 * Project       MKP
 * Copyright (c) 2009,2010,2011 DP IT Services GmbH
 *
 * All rights reserved.
 *
 * $Rev: $ 
 * $Date: $ 
 * $Author: $ 
 */
package com.senacor.hackingdays.serialization.data.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Based on http://mechanical-sympathy.blogspot.de/2012/07/native-cc-like-performance-for-java.html
 * by Martin Thompson
 *
 * @author ccharles
 * @version $LastChangedVersion$
 */
public class UnsafeMemory2 {

  private static final Unsafe unsafe;

  private static final Field stringValueField;

  static {
    try {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      unsafe = (Unsafe) field.get(null);
      stringValueField = String.class.getDeclaredField("value");
      stringValueField.setAccessible(true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static final long byteArrayOffset = unsafe.arrayBaseOffset(byte[].class);

  private long bufferSize;

  private long bufferAddress;

  public UnsafeMemory2(final long size) {
    this.bufferAddress = unsafe.allocateMemory(size);
    this.bufferSize = size;
  }


  public void putByteArray(final long address, final byte[] values) {
    long bytesToCopy = values.length;
    unsafe.copyMemory(values, byteArrayOffset,
                      null, bufferAddress + address,
                      bytesToCopy);
  }

  public byte[] getByteArray(final long address, final int size) {
    byte[] values = new byte[size];

    unsafe.copyMemory(null, bufferAddress + address,
                      values, byteArrayOffset,
                      size);
    return values;
  }

  public void putLong(final long address, final long longValue) {
    unsafe.putLong(bufferAddress + address, longValue);
  }

  public long getLong(final long address) {
    return unsafe.getLong(bufferAddress + address);
  }

  static long toAddress(Object obj) {
    Object[] array = new Object[] {obj};
    long baseOffset = unsafe.arrayBaseOffset(Object[].class);
    return unsafe.getLong(array, baseOffset);
  }
}