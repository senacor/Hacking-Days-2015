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
package com.senacor.hackingdays.serialization.data.memorymanager;

/**
 * @author ccharles
 * @version $LastChangedVersion$
 */
public interface MemoryManager {

  long malloc(long size);

  void put(long address, byte[] values);

  byte[] get(long address, int size);

  void free(long address);
}
