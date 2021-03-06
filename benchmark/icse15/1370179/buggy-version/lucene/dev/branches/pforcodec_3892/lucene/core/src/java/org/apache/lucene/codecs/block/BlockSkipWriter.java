package org.apache.lucene.codecs.block;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.codecs.MultiLevelSkipListWriter;

// nocommit may need to decouple
// baseSkipInterval & theRestSkipInterval?

/**
 * Write skip lists with multiple levels, and support skip within block ints.
 *
 * Assume that docFreq = 28, skipInterval = blockSize = 12
 *
 *  |       block#0       | |      block#1        | |vInts|
 *  d d d d d d d d d d d d d d d d d d d d d d d d d d d d (posting list)
 *                          ^                       ^       (level 0 skip point)
 *
 * Note that skipWriter will ignore first document in block#0, since 
 * it is useless as a skip point.  Also, we'll never skip into the vInts
 * block, only record skip data at the start its start point(if it exist).
 *
 * For each skip point, we will record: 
 * 1. lastDocID, 
 * 2. its related file points(position, payload), 
 * 3. related numbers or uptos(position, payload).
 * 4. start offset.
 *
 */
final class BlockSkipWriter extends MultiLevelSkipListWriter {
  private boolean DEBUG = BlockPostingsReader.DEBUG;
  
  private int[] lastSkipDoc;
  private long[] lastSkipDocPointer;
  private long[] lastSkipPosPointer;
  private long[] lastSkipPayPointer;
  private int[] lastStartOffset;
  private int[] lastPayloadByteUpto;

  private final IndexOutput docOut;
  private final IndexOutput posOut;
  private final IndexOutput payOut;

  private int curDoc;
  private long curDocPointer;
  private long curPosPointer;
  private long curPayPointer;
  private int curPosBufferUpto;
  private int curStartOffset;
  private int curPayloadByteUpto;
  private boolean fieldHasPositions;
  private boolean fieldHasOffsets;
  private boolean fieldHasPayloads;

  public BlockSkipWriter(int maxSkipLevels, int blockSize, int docCount, IndexOutput docOut, IndexOutput posOut, IndexOutput payOut) {
    // nocommit figure out what skipMultiplier is best (4 is
    // total guess):
    super(blockSize, 4, maxSkipLevels, docCount);
    this.docOut = docOut;
    this.posOut = posOut;
    this.payOut = payOut;
    
    lastSkipDoc = new int[maxSkipLevels];
    lastSkipDocPointer = new long[maxSkipLevels];
    if (posOut != null) {
      lastSkipPosPointer = new long[maxSkipLevels];
      if (payOut != null) {
        lastSkipPayPointer = new long[maxSkipLevels];
      }
      lastStartOffset = new int[maxSkipLevels];
      lastPayloadByteUpto = new int[maxSkipLevels];
    }
  }

  public void setField(boolean fieldHasPositions, boolean fieldHasOffsets, boolean fieldHasPayloads) {
    this.fieldHasPositions = fieldHasPositions;
    this.fieldHasOffsets = fieldHasOffsets;
    this.fieldHasPayloads = fieldHasPayloads;
  }

  @Override
  public void resetSkip() {
    super.resetSkip();
    Arrays.fill(lastSkipDoc, 0);
    Arrays.fill(lastSkipDocPointer, docOut.getFilePointer());
    if (fieldHasPositions) {
      Arrays.fill(lastSkipPosPointer, posOut.getFilePointer());
      if (fieldHasOffsets) {
        Arrays.fill(lastStartOffset, 0);
      }
      if (fieldHasPayloads) {
        Arrays.fill(lastPayloadByteUpto, 0);
      }
      if (fieldHasOffsets || fieldHasPayloads) {
        Arrays.fill(lastSkipPayPointer, payOut.getFilePointer());
      }
    }
  }

  /**
   * Sets the values for the current skip data. 
   */
  public void bufferSkip(int doc, int numDocs, long posFP, long payFP, int posBufferUpto, int startOffset, int payloadByteUpto) throws IOException {
    this.curDoc = doc;
    this.curDocPointer = docOut.getFilePointer();
    this.curPosPointer = posFP;
    this.curPayPointer = payFP;
    this.curPosBufferUpto = posBufferUpto;
    this.curPayloadByteUpto = payloadByteUpto;
    this.curStartOffset = startOffset;
    bufferSkip(numDocs);
  }
  
  @Override
  protected void writeSkipData(int level, IndexOutput skipBuffer) throws IOException {
    int delta = curDoc - lastSkipDoc[level];
    if (DEBUG) {
      System.out.println("writeSkipData level=" + level + " lastDoc=" + curDoc + " delta=" + delta + " curDocPointer=" + curDocPointer);
    }
    skipBuffer.writeVInt(delta);
    lastSkipDoc[level] = curDoc;

    skipBuffer.writeVInt((int) (curDocPointer - lastSkipDocPointer[level]));
    lastSkipDocPointer[level] = curDocPointer;

    if (fieldHasPositions) {
      if (DEBUG) {
        System.out.println("  curPosPointer=" + curPosPointer + " curPosBufferUpto=" + curPosBufferUpto);
      }
      skipBuffer.writeVInt((int) (curPosPointer - lastSkipPosPointer[level]));
      lastSkipPosPointer[level] = curPosPointer;
      skipBuffer.writeVInt(curPosBufferUpto);

      if (fieldHasPayloads) {
        skipBuffer.writeVInt(curPayloadByteUpto);
      }

      if (fieldHasOffsets) {
        skipBuffer.writeVInt(curStartOffset - lastStartOffset[level]);
        lastStartOffset[level] = curStartOffset;
      }

      if (fieldHasOffsets || fieldHasPayloads) {
        skipBuffer.writeVInt((int) (curPayPointer - lastSkipPayPointer[level]));
        lastSkipPayPointer[level] = curPayPointer;
      }
    }
  }
}
