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
package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.unsafe.BufferTooSmallException;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory;

/**
 * @author ccharles
 * @version $LastChangedVersion$
 */
public class UnsafeSerializer extends JSerializer {

  private static final int INITIAL_BUFFER_SIZE = 10;
  private static final int BUFFER_INCREASE_DELTA = 10;

  static byte[] buffer = new byte[INITIAL_BUFFER_SIZE];
  final static UnsafeMemory memory = new UnsafeMemory(buffer);

  @Override
  public Object fromBinaryJava(byte[] bytes, Class<?> aClass) {
    memory.setBuffer(bytes);
    memory.reset();
    return Profile.deserializeUnsafe(memory);
  }

  @Override
  public int identifier() {
    return 218831;
  }

  @Override
  public byte[] toBinary(Object o) {
    while (true) {
      try {
        memory.setBuffer(buffer);
        memory.reset();
        ((Profile) o).serializeUnsafe(memory);
        break;
      } catch (BufferTooSmallException e) {
        buffer = new byte[buffer.length + BUFFER_INCREASE_DELTA];
      }
    }
    return buffer;
  }

  @Override
  public boolean includeManifest() {
    return false;
  }
}
