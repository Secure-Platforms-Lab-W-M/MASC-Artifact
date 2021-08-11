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

package org.apache.qpid.server.virtualhost;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.AccessController;

import org.junit.Test;

import org.apache.qpid.test.utils.UnitTestBase;

public class ConnectionPrincipalStatisticsCheckingTaskTest extends UnitTestBase
{
    @Test
    public void execute()
    {
        final QueueManagingVirtualHost vh = mock(QueueManagingVirtualHost.class);
        when(vh.getName()).thenReturn(getTestName());
        final ConnectionPrincipalStatisticsRegistry registry = mock(ConnectionPrincipalStatisticsRegistry.class);
        ConnectionPrincipalStatisticsCheckingTask task =
                new ConnectionPrincipalStatisticsCheckingTask(vh, AccessController.getContext(), registry);

        task.execute();

        verify(registry).reevaluateConnectionStatistics();
    }
}
