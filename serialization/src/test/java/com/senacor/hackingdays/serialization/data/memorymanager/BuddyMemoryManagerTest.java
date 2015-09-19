package com.senacor.hackingdays.serialization.data.memorymanager;

import org.junit.Test;

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
public class BuddyMemoryManagerTest {

  @Test
  public void test() {
    final BuddyMemoryManager subjectUnderTest = new BuddyMemoryManager(16, 8*16);
    final long a1 = subjectUnderTest.malloc(7);
    final long a2 = subjectUnderTest.malloc(15);
    final long a3 = subjectUnderTest.malloc(26);
    final long a4 = subjectUnderTest.malloc(64);

    subjectUnderTest.put(a1, new byte[]{1,2,3,4});
    subjectUnderTest.put(a2, new byte[]{5,6,7,8});


    subjectUnderTest.free(a3);
    subjectUnderTest.free(a4);
    subjectUnderTest.free(a1);
    subjectUnderTest.free(a2);
  }

}