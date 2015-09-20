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
public interface SizeAware {

  /**
   * Returns the deep size of the object in memory, including the referenced objects.
   */
  long getDeepSize();

  /**
   * Returns the shallow size of the object in memory, exluding the referenced objects.
   */
  long getShallowSize();

}
