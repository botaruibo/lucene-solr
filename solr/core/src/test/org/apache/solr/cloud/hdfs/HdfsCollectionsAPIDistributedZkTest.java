package org.apache.solr.cloud.hdfs;

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

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.lucene.util.LuceneTestCase.Slow;
import org.apache.solr.cloud.CollectionsAPIDistributedZkTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope;
import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope.Scope;

@Slow
@ThreadLeakScope(Scope.NONE) // hdfs client currently leaks thread (HADOOP-9049)
public class HdfsCollectionsAPIDistributedZkTest extends CollectionsAPIDistributedZkTest {
  private static MiniDFSCluster dfsCluster;
  
  @BeforeClass
  public static void setupClass() throws Exception {
    dfsCluster = HdfsTestUtil.setupClass(new File(TEMP_DIR,
        HdfsCollectionsAPIDistributedZkTest.class.getName() + "_"
            + System.currentTimeMillis()).getAbsolutePath());
    
    System.setProperty("solr.hdfs.home", dfsCluster.getURI().toString() + "/solr");
    System.setProperty("solr.hdfs.blockcache.enabled", "false");
    
  }
  
  @AfterClass
  public static void teardownClass() throws Exception {
    HdfsTestUtil.teardownClass(dfsCluster);
    System.clearProperty("solr.hdfs.home");
    System.clearProperty("solr.hdfs.blockcache.enabled");
    dfsCluster = null;
  }

  
  @Override
  protected String getDataDir(String dataDir) throws IOException {
    return HdfsTestUtil.getDataDir(dfsCluster, dataDir);
  }

}
