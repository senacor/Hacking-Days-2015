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
public class UnsafeMemory {
  private static final Unsafe unsafe;

  static {
    try {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      unsafe = (Unsafe) field.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static final long byteArrayOffset = unsafe.arrayBaseOffset(byte[].class);

  private static final long longArrayOffset = unsafe.arrayBaseOffset(long[].class);

  private static final long doubleArrayOffset = unsafe.arrayBaseOffset(double[].class);

  private static final int SIZE_OF_BOOLEAN = 1;

  private static final int SIZE_OF_INT = 4;

  private static final int SIZE_OF_LONG = 8;

  private int pos = 0;

  private byte[] buffer;

  public UnsafeMemory(final byte[] buffer) {
    if (null == buffer) {
      throw new NullPointerException("buffer cannot be null");
    }

    this.buffer = buffer;
  }

  public void reset() {
    this.pos = 0;
  }

  public void setBuffer(final byte[] buffer) {
    this.buffer = buffer;
  }

  public void putBoolean(final boolean value) throws BufferTooSmallException {
    if (pos + SIZE_OF_BOOLEAN > buffer.length) {
      throw new BufferTooSmallException();
    }
    unsafe.putBoolean(buffer, byteArrayOffset + pos, value);
    pos += SIZE_OF_BOOLEAN;
  }

  public boolean getBoolean() {
    boolean value = unsafe.getBoolean(buffer, byteArrayOffset + pos);
    pos += SIZE_OF_BOOLEAN;

    return value;
  }

  public void putInt(final int value) throws BufferTooSmallException {
    if (pos + SIZE_OF_INT > buffer.length) {
      throw new BufferTooSmallException();
    }
    unsafe.putInt(buffer, byteArrayOffset + pos, value);
    pos += SIZE_OF_INT;
  }

  public int getInt() {
    int value = unsafe.getInt(buffer, byteArrayOffset + pos);
    pos += SIZE_OF_INT;

    return value;
  }

  public void putLong(final long value) throws BufferTooSmallException {
    if (pos + SIZE_OF_LONG > buffer.length) {
      throw new BufferTooSmallException();
    }
    unsafe.putLong(buffer, byteArrayOffset + pos, value);
    pos += SIZE_OF_LONG;
  }

  public long getLong() {
    long value = unsafe.getLong(buffer, byteArrayOffset + pos);
    pos += SIZE_OF_LONG;

    return value;
  }

  public void putLongArray(final long[] values) throws BufferTooSmallException {
    putInt(values.length);

    long bytesToCopy = values.length << 3;
    if (pos + bytesToCopy > buffer.length) {
      throw new BufferTooSmallException();
    }

    unsafe.copyMemory(values, longArrayOffset,
                      buffer, byteArrayOffset + pos,
                      bytesToCopy);
    pos += bytesToCopy;
  }

  public long[] getLongArray() {
    int arraySize = getInt();
    long[] values = new long[arraySize];

    long bytesToCopy = values.length << 3;
    unsafe.copyMemory(buffer, byteArrayOffset + pos,
                      values, longArrayOffset,
                      bytesToCopy);
    pos += bytesToCopy;

    return values;
  }

  public void putByteArray(final byte[] values) throws BufferTooSmallException {
    putInt(values.length);

    long bytesToCopy = values.length;
    if (pos + bytesToCopy > buffer.length) {
      throw new BufferTooSmallException();
    }
    unsafe.copyMemory(values, byteArrayOffset,
                      buffer, byteArrayOffset + pos,
                      bytesToCopy);
    pos += bytesToCopy;
  }

  public byte[] getByteArray() {
    int arraySize = getInt();
    byte[] values = new byte[arraySize];

    long bytesToCopy = values.length;
    unsafe.copyMemory(buffer, byteArrayOffset + pos,
                      values, byteArrayOffset,
                      bytesToCopy);
    pos += bytesToCopy;

    return values;
  }

  public void putDoubleArray(final double[] values) throws BufferTooSmallException {
    putInt(values.length);

    long bytesToCopy = values.length << 3;
    if (pos + bytesToCopy > buffer.length) {
      throw new BufferTooSmallException();
    }
    unsafe.copyMemory(values, doubleArrayOffset,
                      buffer, byteArrayOffset + pos,
                      bytesToCopy);
    pos += bytesToCopy;
  }

  public double[] getDoubleArray() {
    int arraySize = getInt();
    double[] values = new double[arraySize];

    long bytesToCopy = values.length << 3;
    unsafe.copyMemory(buffer, byteArrayOffset + pos,
                      values, doubleArrayOffset,
                      bytesToCopy);
    pos += bytesToCopy;

    return values;
  }
}