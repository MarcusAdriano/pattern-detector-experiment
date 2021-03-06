package org.apache.lucene.index;

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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.store.Directory;

/** Embeds a [read-only] SegmentInfo and adds per-commit
 *  fields.
 *
 *  @lucene.experimental */
public class SegmentInfoPerCommit { // TODO (DVU_RENAME) to SegmentCommitInfo
  
  /** The {@link SegmentInfo} that we wrap. */
  public final SegmentInfo info;

  // How many deleted docs in the segment:
  private int delCount;

  // Generation number of the live docs file (-1 if there
  // are no deletes yet):
  private long delGen;

  // Normally 1+delGen, unless an exception was hit on last
  // attempt to write:
  private long nextWriteDelGen;

  // Generation number of the FieldInfos (-1 if there are no updates)
  private long fieldInfosGen;
  
  // Normally 1 + fieldInfosGen, unless an exception was hit on last attempt to
  // write
  private long nextWriteFieldInfosGen;

  // Tracks the files with field updates
  private Set<String> updatesFiles = new HashSet<String>();
  
  private volatile long sizeInBytes = -1;

  /**
   * Sole constructor.
   * 
   * @param info
   *          {@link SegmentInfo} that we wrap
   * @param delCount
   *          number of deleted documents in this segment
   * @param delGen
   *          deletion generation number (used to name deletion files)
   * @param fieldInfosGen
   *          FieldInfos generation number (used to name field-infos files)
   **/
  public SegmentInfoPerCommit(SegmentInfo info, int delCount, long delGen, long fieldInfosGen) {
    this.info = info;
    this.delCount = delCount;
    this.delGen = delGen;
    if (delGen == -1) {
      nextWriteDelGen = 1;
    } else {
      nextWriteDelGen = delGen+1;
    }
    
    this.fieldInfosGen = fieldInfosGen;
    if (fieldInfosGen == -1) {
      nextWriteFieldInfosGen = 1;
    } else {
      nextWriteFieldInfosGen = fieldInfosGen + 1;
    }
  }

  /** Returns the files which contains field updates. */
  public Set<String> getUpdatesFiles() {
    return new HashSet<String>(updatesFiles);
  }
  
  /** Called when we succeed in writing field updates. */
  public void addUpdatesFiles(Set<String> files) {
    updatesFiles.addAll(files);
  }
  
  /** Called when we succeed in writing deletes */
  void advanceDelGen() {
    delGen = nextWriteDelGen;
    nextWriteDelGen = delGen+1;
    sizeInBytes = -1;
  }

  /** Called if there was an exception while writing
   *  deletes, so that we don't try to write to the same
   *  file more than once. */
  void advanceNextWriteDelGen() {
    nextWriteDelGen++;
  }
  
  /** Called when we succeed in writing a new FieldInfos generation. */
  void advanceFieldInfosGen() {
    fieldInfosGen = nextWriteFieldInfosGen;
    nextWriteFieldInfosGen = fieldInfosGen + 1;
    sizeInBytes = -1;
  }
  
  /**
   * Called if there was an exception while writing a new generation of
   * FieldInfos, so that we don't try to write to the same file more than once.
   */
  void advanceNextWriteFieldInfosGen() {
    nextWriteFieldInfosGen++;
  }

  /** Returns total size in bytes of all files for this
   *  segment. */
  public long sizeInBytes() throws IOException {
    if (sizeInBytes == -1) {
      long sum = 0;
      for (final String fileName : files()) {
        sum += info.dir.fileLength(fileName);
      }
      sizeInBytes = sum;
    }

    return sizeInBytes;
  }

  /** Returns all files in use by this segment. */
  public Collection<String> files() throws IOException {
    // Start from the wrapped info's files:
    Collection<String> files = new HashSet<String>(info.files());

    // TODO we could rely on TrackingDir.getCreatedFiles() (like we do for
    // updates) and then maybe even be able to remove LiveDocsFormat.files().
    
    // Must separately add any live docs files:
    info.getCodec().liveDocsFormat().files(this, files);

    // Must separately add any field updates files
    files.addAll(updatesFiles);
    
    return files;
  }

  // NOTE: only used in-RAM by IW to track buffered deletes;
  // this is never written to/read from the Directory
  private long bufferedDeletesGen;
  
  long getBufferedDeletesGen() {
    return bufferedDeletesGen;
  }

  void setBufferedDeletesGen(long v) {
    bufferedDeletesGen = v;
    sizeInBytes =  -1;
  }
  
  /** Returns true if there are any deletions for the 
   * segment at this commit. */
  public boolean hasDeletions() {
    return delGen != -1;
  }

  /** Returns true if there are any field updates for the segment in this commit. */
  public boolean hasFieldUpdates() {
    return fieldInfosGen != -1;
  }
  
  /** Returns the next available generation number of the FieldInfos files. */
  public long getNextFieldInfosGen() {
    return nextWriteFieldInfosGen;
  }
  
  /**
   * Returns the generation number of the field infos file or -1 if there are no
   * field updates yet.
   */
  public long getFieldInfosGen() {
    return fieldInfosGen;
  }
  
  /**
   * Returns the next available generation number
   * of the live docs file.
   */
  public long getNextDelGen() {
    return nextWriteDelGen;
  }

  /**
   * Returns generation number of the live docs file 
   * or -1 if there are no deletes yet.
   */
  public long getDelGen() {
    return delGen;
  }
  
  /**
   * Returns the number of deleted docs in the segment.
   */
  public int getDelCount() {
    return delCount;
  }

  void setDelCount(int delCount) {
    this.delCount = delCount;
    assert delCount <= info.getDocCount();
  }

  /** Returns a description of this segment. */
  public String toString(Directory dir, int pendingDelCount) {
    return info.toString(dir, delCount + pendingDelCount);
  }

  @Override
  public String toString() {
    String s = info.toString(info.dir, delCount);
    if (delGen != -1) {
      s += ":delGen=" + delGen;
    }
    if (fieldInfosGen != -1) {
      s += ":fieldInfosGen=" + fieldInfosGen;
    }
    return s;
  }

  @Override
  public SegmentInfoPerCommit clone() {
    SegmentInfoPerCommit other = new SegmentInfoPerCommit(info, delCount, delGen, fieldInfosGen);
    // Not clear that we need to carry over nextWriteDelGen
    // (i.e. do we ever clone after a failed write and
    // before the next successful write?), but just do it to
    // be safe:
    other.nextWriteDelGen = nextWriteDelGen;
    other.nextWriteFieldInfosGen = nextWriteFieldInfosGen;
    
    other.updatesFiles.addAll(updatesFiles);
    
    return other;
  }
}
