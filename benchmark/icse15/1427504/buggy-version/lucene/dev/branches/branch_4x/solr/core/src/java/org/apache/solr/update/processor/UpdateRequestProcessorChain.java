  Merged /lucene/dev/trunk/lucene/CHANGES.txt:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/core/src/test/org/apache/lucene/index/index.40.nocfs.zip:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/core/src/test/org/apache/lucene/index/index.40.cfs.zip:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/core/src/test/org/apache/lucene/index/index.40.optimized.nocfs.zip:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/core/src/test/org/apache/lucene/index/TestBackwardsCompatibility.java:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/core/src/test/org/apache/lucene/index/index.40.optimized.cfs.zip:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/core:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/sandbox:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/highlighter:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/join:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/LICENSE.txt:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/site:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/licenses:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/SYSTEM_REQUIREMENTS.txt:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/MIGRATE.txt:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/memory:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/queries:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/queryparser:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/facet:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/analysis/icu/src/java/org/apache/lucene/collation/ICUCollationKeyFilterFactory.java:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/analysis:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/benchmark:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/grouping:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/misc:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/spatial:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/build.xml:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/NOTICE.txt:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/tools:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/codecs:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/backwards:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/ivy-settings.xml:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/test-framework:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/README.txt:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/JRE_VERSION_MIGRATION.txt:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/BUILD.txt:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/suggest:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/module-build.xml:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/common-build.xml:r1426373,1427037
  Merged /lucene/dev/trunk/lucene/demo:r1426373,1427037
  Merged /lucene/dev/trunk/lucene:r1426373,1427037
  Merged /lucene/dev/trunk/dev-tools:r1426373,1427037
  Merged /lucene/dev/trunk/solr/site:r1426373,1427037
  Merged /lucene/dev/trunk/solr/SYSTEM_REQUIREMENTS.txt:r1426373,1427037
  Merged /lucene/dev/trunk/solr/licenses/httpclient-LICENSE-ASL.txt:r1426373,1427037
  Merged /lucene/dev/trunk/solr/licenses/httpclient-NOTICE.txt:r1426373,1427037
  Merged /lucene/dev/trunk/solr/licenses/httpmime-LICENSE-ASL.txt:r1426373,1427037
  Merged /lucene/dev/trunk/solr/licenses/httpcore-NOTICE.txt:r1426373,1427037
  Merged /lucene/dev/trunk/solr/licenses/httpcore-LICENSE-ASL.txt:r1426373,1427037
  Merged /lucene/dev/trunk/solr/licenses/httpmime-NOTICE.txt:r1426373,1427037
  Merged /lucene/dev/trunk/solr/licenses:r1426373,1427037
  Merged /lucene/dev/trunk/solr/test-framework:r1426373,1427037
  Merged /lucene/dev/trunk/solr/README.txt:r1426373,1427037
  Merged /lucene/dev/trunk/solr/webapp:r1426373,1427037
  Merged /lucene/dev/trunk/solr/testlogging.properties:r1426373,1427037
  Merged /lucene/dev/trunk/solr/cloud-dev:r1426373,1427037
  Merged /lucene/dev/trunk/solr/common-build.xml:r1426373,1427037
  Merged /lucene/dev/trunk/solr/scripts:r1426373,1427037
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

package org.apache.solr.update.processor;

import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.plugin.PluginInfoInitialized;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.SolrException;
import org.apache.solr.core.PluginInfo;
import org.apache.solr.core.SolrCore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * Manages a chain of UpdateRequestProcessorFactories.
 * <p>
 * Chain can be configured via solrconfig.xml:
 * </p>
 * <pre class="prettyprint">
 * &lt;updateRequestProcessors name="key" default="true"&gt;
 *   &lt;processor class="PathToClass1" /&gt;
 *   &lt;processor class="PathToClass2" /&gt;
 *   &lt;processor class="solr.LogUpdateProcessorFactory" &gt;
 *     &lt;int name="maxNumToLog"&gt;100&lt;/int&gt;
 *   &lt;/processor&gt;
 *   &lt;processor class="solr.RunUpdateProcessorFactory" /&gt;
 * &lt;/updateRequestProcessors&gt;
 * </pre>
 * <p>
 * Allmost all processor chains should end with an instance of 
 * {@link RunUpdateProcessorFactory} unless the user is explicitly 
 * executing the update commands in an alternative custom 
 * <code>UpdateRequestProcessorFactory</code>.
 * </p>
 *
 * @see UpdateRequestProcessorFactory
 * @see #init
 * @see #createProcessor
 * @since solr 1.3
 */
public final class UpdateRequestProcessorChain implements PluginInfoInitialized
{
  public final static Logger log = LoggerFactory.getLogger(UpdateRequestProcessorChain.class);

  private UpdateRequestProcessorFactory[] chain;
  private final SolrCore solrCore;

  public UpdateRequestProcessorChain(SolrCore solrCore) {
    this.solrCore = solrCore;
  }

  /**
   * Initializes the chain using the factories specified by the <code>PluginInfo</code>.
   * if the chain includes the <code>RunUpdateProcessorFactory</code>, but 
   * does not include an implementation of the 
   * <code>DistributingUpdateProcessorFactory</code> interface, then an 
   * instance of <code>DistributedUpdateProcessorFactory</code> will be 
   * injected immediately prior to the <code>RunUpdateProcessorFactory</code>.
   *
   * @see DistributingUpdateProcessorFactory
   * @see RunUpdateProcessorFactory
   * @see DistributedUpdateProcessorFactory
   */
  @Override
  public void init(PluginInfo info) {
    final String infomsg = "updateRequestProcessorChain \"" + 
      (null != info.name ? info.name : "") + "\"" + 
      (info.isDefault() ? " (default)" : "");

    // wrap in an ArrayList so we know we know we can do fast index lookups 
    // and that add(int,Object) is supported
    List<UpdateRequestProcessorFactory> list = new ArrayList
      (solrCore.initPlugins(info.getChildren("processor"),UpdateRequestProcessorFactory.class,null));

    if(list.isEmpty()){
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
                              infomsg + " require at least one processor");
    }

    int numDistrib = 0;
    int runIndex = -1;
    // hi->lo incase multiple run instances, add before first one
    // (no idea why someone might use multiple run instances, but just in case)
    for (int i = list.size()-1; 0 <= i; i--) {
      UpdateRequestProcessorFactory factory = list.get(i);
      if (factory instanceof DistributingUpdateProcessorFactory) {
        numDistrib++;
      }
      if (factory instanceof RunUpdateProcessorFactory) {
        runIndex = i;
      }
    }
    if (1 < numDistrib) {
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
                              infomsg + " may not contain more then one " +
                              "instance of DistributingUpdateProcessorFactory");
    }
    if (0 <= runIndex && 0 == numDistrib) {
      // by default, add distrib processor immediately before run
      DistributedUpdateProcessorFactory distrib 
        = new DistributedUpdateProcessorFactory();
      distrib.init(new NamedList());
      list.add(runIndex, distrib);

      log.info("inserting DistributedUpdateProcessorFactory into " + infomsg);
    }

    chain = list.toArray(new UpdateRequestProcessorFactory[list.size()]); 
  }

  /**
   * Creates a chain backed directly by the specified array. Modifications to 
   * the array will affect future calls to <code>createProcessor</code>
   */
  public UpdateRequestProcessorChain( UpdateRequestProcessorFactory[] chain, 
                                      SolrCore solrCore) {
    this.chain = chain;
    this.solrCore =  solrCore;
  }


  /**
   * Uses the factories in this chain to creates a new 
   * <code>UpdateRequestProcessor</code> instance specific for this request.  
   * If the <code>DISTRIB_UPDATE_PARAM</code> is present in the request and is 
   * non-blank, then any factory in this chain prior to the instance of 
   * <code>{@link DistributingUpdateProcessorFactory}</code> will be skipped, 
   * and the <code>UpdateRequestProcessor</code> returned will be from that 
   * <code>DistributingUpdateProcessorFactory</code>
   *
   * @see UpdateRequestProcessorFactory#getInstance
   * @see DistributingUpdateProcessorFactory#DISTRIB_UPDATE_PARAM
   */
  public UpdateRequestProcessor createProcessor(SolrQueryRequest req, 
                                                SolrQueryResponse rsp) 
  {
    UpdateRequestProcessor processor = null;
    UpdateRequestProcessor last = null;
    
    final String distribPhase = req.getParams().get
      (DistributingUpdateProcessorFactory.DISTRIB_UPDATE_PARAM, "");
    final boolean skipToDistrib = ! distribPhase.trim().isEmpty();

    for (int i = chain.length-1; i>=0; i--) {
      processor = chain[i].getInstance(req, rsp, last);
      last = processor == null ? last : processor;
      if (skipToDistrib 
          && chain[i] instanceof DistributingUpdateProcessorFactory) {
        break;
      }
    }
    return last;
  }

  /**
   * Returns the underlying array of factories used in this chain.  
   * Modifications to the array will affect future calls to 
   * <code>createProcessor</code>
   */
  public UpdateRequestProcessorFactory[] getFactories() {
    return chain;
  }

}
