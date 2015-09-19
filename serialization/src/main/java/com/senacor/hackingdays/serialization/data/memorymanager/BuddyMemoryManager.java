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

import com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory2;

import java.nio.ByteBuffer;

/**
 * Implementation of Buddy Allocator.
 * Based on information from http://bitsquid.blogspot.de/2015/08/allocation-adventures-3-buddy-allocator.html
 *
 * @author ccharles
 * @version $LastChangedVersion$
 */
public class BuddyMemoryManager implements MemoryManager {

  private static final int SIZE_OF_LONG = 8;

  private int numberOfLevels;

  private int poolExponent;

  private int blockExponent;

  private long poolSize;

  private long[] freelistHeads;

  private ByteBuffer splitIndex;

  private ByteBuffer allocatedIndex;

  private UnsafeMemory2 memory;

  public BuddyMemoryManager(final long blocksize, final long poolsize) {
    if (!isPowerOfTwo(blocksize)) {
      throw new IllegalArgumentException("blocksize is not a power of 2.");
    }
    if (!isPowerOfTwo(poolsize)) {
      throw new IllegalArgumentException("poolsize is not a power of 2.");
    }
    if (blocksize > poolsize) {
      throw new IllegalArgumentException("blocksize must not be greater than poolsize.");
    }
    if (blocksize < 16) {
      throw new IllegalArgumentException("blocksize must be at least 16.");
    }

    memory = new UnsafeMemory2(poolsize);

    blockExponent = (int) (Math.log(blocksize) / Math.log(2));
    poolExponent = (int) (Math.log(poolsize) / Math.log(2));

    numberOfLevels = poolExponent - blockExponent + 1;
    freelistHeads = new long[numberOfLevels];
    for (int i = 1; i < numberOfLevels; i++) {
      freelistHeads[i] = -1;
    }
    setPrev(0, -1);
    setNext(0, -1);

    // 1 bit per block, except for maxlevel blocks (they cannot be splitted)
    final int splitIndexSize =  (1 << numberOfLevels - 1);
    splitIndex = ByteBuffer.allocateDirect(splitIndexSize);

    // 1 bit per block
    final int allocatedIndexSize =  (1 << numberOfLevels);
    allocatedIndex = ByteBuffer.allocateDirect(allocatedIndexSize);

    this.poolSize = poolsize;
  }

  private static boolean isPowerOfTwo(long number) {
    if (number <= 0) {
      throw new IllegalArgumentException("number must not be negative: " + number);
    }
    return ((number & (number - 1)) == 0);
  }

  @Override
  public long malloc(long size) {
    final int level = determineLevel(size);
    return allocateBlockAtLevel(level);
  }

  @Override
  public void put(final long address, byte[] values) {
    memory.putByteArray(address, values);
  }

  @Override
  public byte[] get(long address, int size) {
    return new byte[0];
  }

  @Override
  public void free(long address) {
    final int level = findLevelOfAllocatedBlock(address);
    final int bitIndex = bitIndexOfBlockInLevel(address, level);
    unsetBit(allocatedIndex, bitIndex);
    insertFreeBlock(address, level);
    mergeFreeBuddies(address, level);
  }

  private void mergeFreeBuddies(final long address, final int level) {
    if (level == 0) {
      return;
    }
    final long buddy = getBuddy(address, level);
    final int buddyBitIndex = bitIndexOfBlockInLevel(buddy, level);
    long leftBuddy;
    if (address < buddy) {
      leftBuddy = address;
    } else {
      leftBuddy = buddy;
    }
    if (!isBitSet(allocatedIndex, buddyBitIndex)) {
      final int higherLevelBitIndex = bitIndexOfBlockInLevel(leftBuddy, level - 1);
      unsetBit(allocatedIndex, higherLevelBitIndex);
      unsetBit(splitIndex, higherLevelBitIndex);
      removeFreeBlockFromLevel(address, level);
      removeFreeBlockFromLevel(buddy, level);
      insertFreeBlock(leftBuddy, level - 1);
      mergeFreeBuddies(leftBuddy, level - 1);
    }
  }

  private long allocateBlockAtLevel(final int level) {
    if (level < 0) {
      throw new MMOutOfMemoryException();
    }
    if (!hasFreeBlockAtLevel(level)) {
      final long higherLevelBlock = allocateBlockAtLevel(level - 1);
      splitBlock(higherLevelBlock, level - 1);
      final long secondBlock = getBuddy(higherLevelBlock, level);
      insertFreeBlock(secondBlock, level);
      insertFreeBlock(higherLevelBlock, level);
    }
    return allocateFreeBlockAtLevel(level);
  }

  private long allocateFreeBlockAtLevel(int level) {
    final long address = freelistHeads[level];
    final int bitPosition = bitIndexOfBlockInLevel(address, level);
    setBit(allocatedIndex, bitPosition);
    removeFirstFreeBlock(level);
    return address;
  }

  private long getBuddy(long block, int level) {
    final long indexOfBlock = index_in_level(block, level);
    if (indexOfBlock % 2 == 0) {
      return block + sizeOfBlockInLevel(level);
    } else {
      return block - sizeOfBlockInLevel(level);
    }
  }

  private void splitBlock(long block, int level) {
    final int bitPosition = bitIndexOfBlockInLevel(block, level);
    setBit(splitIndex, bitPosition);
  }

  private int determineLevel(final long size) {
    final int desiredExponent = (int) (Math.log(nextPowerOf2(size)) / Math.log(2));
    return poolExponent - Math.max(desiredExponent, blockExponent);
  }

  private long nextPowerOf2(final long value) {
    return (long) (Math.pow(2, Math.ceil(Math.log(value) / Math.log(2))));
  }

  private boolean hasFreeBlockAtLevel(int level) {
    return freelistHeads[level] != -1;
  }

  private void insertFreeBlock(long block, int level) {
    setPrev(block, -1);
    if (freelistHeads[level] == -1) {
      setNext(block, -1);
    } else {
      setPrev(freelistHeads[level], block);
      setNext(block, freelistHeads[level]);
    }
    freelistHeads[level] = block;
  }

  private void removeFirstFreeBlock(int level) {
    final long firstFreeBlock = freelistHeads[level];
    final long secondFreeBlock = getNext(firstFreeBlock);
    if (secondFreeBlock != -1) {
      setPrev(secondFreeBlock, -1);
      freelistHeads[level] = secondFreeBlock;
    } else {
      freelistHeads[level] = -1;
    }
  }

  private void removeFreeBlockFromLevel(long blockToRemove, int level) {
    if (blockToRemove == freelistHeads[level]) {
      freelistHeads[level] = getNext(blockToRemove);
      return;
    }
    long currentBlock = getNext(freelistHeads[level]);
    while (currentBlock != -1) {
      if (currentBlock == blockToRemove) {
        final long nextBlock = getNext(currentBlock);
        final long prevBlock = getPrev(currentBlock);
        if (nextBlock != -1) {
          setPrev(nextBlock, prevBlock);
        }
        if (prevBlock != -1) {
          setNext(prevBlock, nextBlock);
        }
        return;
      }
    }
    throw new IllegalStateException("block to remove from freelist not found");
  }

  private int findLevelOfAllocatedBlock(final long block) {
    int n = numberOfLevels - 1;
    while (n > 0) {
      if (block_has_been_split(block, n - 1)) {
        return n;
      }
      n--;
    }
    return 0;
  }

  private boolean block_has_been_split(final long block, final int level) {
    return isBitSet(splitIndex, bitIndexOfBlockInLevel(block, level));
  }

  private boolean isBitSet(final ByteBuffer buffer, int bitIndex) {
    int byteIndex = bitIndex / 8;
    final byte splitByte = buffer.get(byteIndex);
    int bitPosition = bitIndex % 8;
    return (splitByte >> bitPosition & 1) == 1;
  }

  private void setBit(final ByteBuffer buffer, int bitIndex) {
    int byteIndex = bitIndex / 8;
    byte splitByte = buffer.get(byteIndex);
    int bitPosition = bitIndex % 8;
    splitByte |= (1 << bitPosition);
    buffer.put(byteIndex, splitByte);
  }

  private void unsetBit(final ByteBuffer buffer, int bitIndex) {
    int byteIndex = bitIndex / 8;
    byte splitByte = buffer.get(byteIndex);
    int bitPosition = bitIndex % 8;
    splitByte &= ~(1 << bitPosition);
    buffer.put(byteIndex, splitByte);
  }

  private int bitIndexOfBlockInLevel(final long block, final int level) {
    return (int) ((1 << level) + index_in_level(block, level) - 1);
  }

  private long index_in_level(final long block, final int level) {
    return block / sizeOfBlockInLevel(level);
  }

  private long sizeOfBlockInLevel(final int level) {
    return poolSize / (1 << level);
  }

  private long getPrev(final long block) {
    return memory.getLong(block);
  }

  private void setPrev(final long block, final long next) {
    memory.putLong(block, next);
  }

  private long getNext(final long block) {
    return memory.getLong(block + SIZE_OF_LONG);
  }

  private void setNext(final long block, final long next) {
    memory.putLong(block + SIZE_OF_LONG, next);
  }


}
