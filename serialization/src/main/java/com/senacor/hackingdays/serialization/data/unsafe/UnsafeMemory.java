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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Based on http://mechanical-sympathy.blogspot.de/2012/07/native-cc-like-performance-for-java.html
 * by Martin Thompson
 *
 * @author ccharles
 * @version $LastChangedVersion$
 */
public class UnsafeMemory {
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

  private static final long longArrayOffset = unsafe.arrayBaseOffset(long[].class);

  private static final long doubleArrayOffset = unsafe.arrayBaseOffset(double[].class);

  private static final int SIZE_OF_BOOLEAN = 1;

  private static final int SIZE_OF_INT = 4;

  private static final int SIZE_OF_LONG = 8;

  private long pos = 0;

  private long bufferSize;

  private long bufferOffset;

  private Object buffer;

  public UnsafeMemory(final byte[] buffer) {
    if (null == buffer) {
      throw new NullPointerException("buffer cannot be null");
    }

    this.buffer = buffer;
    this.bufferSize = buffer.length;
    this.bufferOffset = byteArrayOffset;
  }

  public UnsafeMemory(final long size) {
    this.buffer = fromAddress(unsafe.allocateMemory(size));
    this.bufferSize = size;
    this.bufferOffset = 0;
  }

  private static Object fromAddress(long address) {
    Object[] array = new Object[] {null};
    long baseOffset = unsafe.arrayBaseOffset(Object[].class);
    unsafe.putLong(array, baseOffset, address);
    return array[0];
  }

  public void reset() {
    this.pos = 0;
  }

  public void setBuffer(final byte[] buffer) {
    this.buffer = buffer;
    this.bufferSize = buffer.length;
    this.bufferOffset = byteArrayOffset;
  }

  public long getPos() {
    return pos;
  }

  public void putBoolean(final boolean value) throws BufferTooSmallException {
    if (pos + SIZE_OF_BOOLEAN > bufferSize) {
      throw new BufferTooSmallException();
    }
    unsafe.putBoolean(buffer, bufferOffset + pos, value);
    pos += SIZE_OF_BOOLEAN;
  }

  public boolean getBoolean() {
    boolean value = unsafe.getBoolean(buffer, bufferOffset + pos);
    pos += SIZE_OF_BOOLEAN;

    return value;
  }

  public void putInt(final int value) throws BufferTooSmallException {
    if (pos + SIZE_OF_INT > bufferSize) {
      throw new BufferTooSmallException();
    }
    unsafe.putInt(buffer, bufferOffset + pos, value);
    pos += SIZE_OF_INT;
  }

  public int getInt() {
    int value = unsafe.getInt(buffer, bufferOffset + pos);
    pos += SIZE_OF_INT;

    return value;
  }

  public void putLong(final long value) throws BufferTooSmallException {
    if (pos + SIZE_OF_LONG > bufferSize) {
      throw new BufferTooSmallException();
    }
    unsafe.putLong(buffer, bufferOffset + pos, value);
    pos += SIZE_OF_LONG;
  }

  public long getLong() {
    long value = unsafe.getLong(buffer, bufferOffset + pos);
    pos += SIZE_OF_LONG;

    return value;
  }

  public void putLongArray(final long[] values) throws BufferTooSmallException {
    putInt(values.length);

    long bytesToCopy = values.length << 3;
    if (pos + bytesToCopy > bufferSize) {
      throw new BufferTooSmallException();
    }

    unsafe.copyMemory(values, longArrayOffset,
                      buffer, bufferOffset + pos,
                      bytesToCopy);
    pos += bytesToCopy;
  }

  public long[] getLongArray() {
    int arraySize = getInt();
    long[] values = new long[arraySize];

    long bytesToCopy = values.length << 3;
    unsafe.copyMemory(buffer, bufferOffset + pos,
                      values, longArrayOffset,
                      bytesToCopy);
    pos += bytesToCopy;

    return values;
  }

  public void putByteArray(final byte[] values) throws BufferTooSmallException {
    putInt(values.length);

    long bytesToCopy = values.length;
    if (pos + bytesToCopy > bufferSize) {
      throw new BufferTooSmallException();
    }
    unsafe.copyMemory(values, byteArrayOffset,
                      buffer, bufferOffset + pos,
                      bytesToCopy);
    pos += bytesToCopy;
  }

  public byte[] getByteArray() {
    int arraySize = getInt();
    byte[] values = new byte[arraySize];

    long bytesToCopy = values.length;
    unsafe.copyMemory(buffer, bufferOffset + pos,
                      values, byteArrayOffset,
                      bytesToCopy);
    pos += bytesToCopy;

    return values;
  }

  public void putByteArray(final long address, final byte[] values) {
    long bytesToCopy = values.length;
    unsafe.copyMemory(values, byteArrayOffset,
                      buffer, bufferOffset + address,
                      bytesToCopy);
  }

  public byte[] getByteArray(final long address, final int size) {
    byte[] values = new byte[size];

    unsafe.copyMemory(buffer, bufferOffset + address,
                      values, byteArrayOffset,
                      size);
    return values;
  }

  public void putLong(final long address, final long longValue) {
    unsafe.putLong(buffer, bufferOffset + address, longValue);
  }

  public long getLong(final long address) {
    return unsafe.getLong(buffer, bufferOffset + address);
  }

  public void putDoubleArray(final double[] values) throws BufferTooSmallException {
    putInt(values.length);

    long bytesToCopy = values.length << 3;
    if (pos + bytesToCopy > bufferSize) {
      throw new BufferTooSmallException();
    }
    unsafe.copyMemory(values, doubleArrayOffset,
                      buffer, bufferOffset + pos,
                      bytesToCopy);
    pos += bytesToCopy;
  }

  public double[] getDoubleArray() {
    int arraySize = getInt();
    double[] values = new double[arraySize];

    long bytesToCopy = values.length << 3;
    unsafe.copyMemory(buffer, bufferOffset + pos,
                      values, doubleArrayOffset,
                      bytesToCopy);
    pos += bytesToCopy;

    return values;
  }

  public static long sizeOf(final Object object) {
    Class clazz = object.getClass();
    if (clazz.isArray()) {
      int base = unsafe.arrayBaseOffset(clazz);
      int scale = unsafe.arrayIndexScale(clazz);
      long size = base + (scale * Array.getLength(object));
      if ((size % 8) != 0) {
        size += 8 - (size % 8);
      }
      return size;
    }
    long maximumOffset = 0;
    do {
      for (Field f : clazz.getDeclaredFields()) {
        if (!Modifier.isStatic(f.getModifiers())) {
          maximumOffset = Math.max(maximumOffset, unsafe.objectFieldOffset(f));
        }
      }
    } while ((clazz = clazz.getSuperclass()) != null);
    long objectSize = maximumOffset + 8;
    // add the size of the char[] that backs the string
    if (object instanceof String) {
      objectSize += sizeOfStringValue((String)object);
    }
    return objectSize;
  }

  private static long sizeOfStringValue(final String string) {
    try {
      final char[] stringValue = (char[]) stringValueField.get(string);
      return UnsafeMemory.sizeOf(stringValue);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}