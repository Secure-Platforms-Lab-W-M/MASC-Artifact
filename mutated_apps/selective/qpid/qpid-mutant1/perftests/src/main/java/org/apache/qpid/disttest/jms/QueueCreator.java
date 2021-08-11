/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.qpid.disttest.jms;

import java.util.List;

import javax.jms.Connection;
import javax.jms.Session;

import org.apache.qpid.disttest.controller.config.QueueConfig;

public interface QueueCreator
{
    String QUEUE_CREATOR_DRAIN_POLL_TIMEOUT = "qpid.disttest.queue.creator.drainPollTime";
    String QUEUE_CREATOR_DRAIN_QUEUE_BEFORE_DELETE = "qpid.disttest.queue.creator.drainQueueBeforeDelete";
    String QUEUE_CREATOR_THREAD_POOL_SIZE = "qpid.disttest.queue.creator.threadPoolSize";

    void createQueues(Connection connection, Session session, List<QueueConfig> configs);
    void deleteQueues(Connection connection, Session session, List<QueueConfig> configs);

    String getProtocolVersion(Connection connection);
    String getProviderVersion(Connection connection);
}
