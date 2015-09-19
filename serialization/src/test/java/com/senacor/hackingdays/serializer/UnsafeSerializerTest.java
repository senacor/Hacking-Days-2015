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

import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.unsafe.BufferTooSmallException;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory;
import org.junit.Test;

/**
 * @author ccharles
 * @version $LastChangedVersion$
 */
public class UnsafeSerializerTest {
  @Test
  public void profileSizes() throws BufferTooSmallException {
    final Profile profile = ProfileGenerator.newProfile();
    System.out.println("size of profile: " + profile.getDeepSize());

    final byte[] buffer = new byte[1000];
    final UnsafeMemory memory = new UnsafeMemory(buffer);
    profile.serializeUnsafe(memory);
    System.out.println("size of profile serialization: " + memory.getPos());
  }

}
