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

/**
 * @author ccharles
 * @version $LastChangedVersion$
 */
public interface UnsafeSerializable {

  void serializeUnsafe(final UnsafeMemory memory) throws BufferTooSmallException;

}
