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
 *
 */
package org.apache.qpid.server.queue;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.message.AMQMessageHeader;
import org.apache.qpid.server.message.MessageReference;
import org.apache.qpid.server.message.ServerMessage;
import org.apache.qpid.server.model.BrokerTestHelper;
import org.apache.qpid.server.model.LifetimePolicy;
import org.apache.qpid.server.model.Queue;
import org.apache.qpid.server.store.TransactionLogResource;
import org.apache.qpid.server.virtualhost.QueueManagingVirtualHost;
public class SortedQueueEntryTest extends QueueEntryImplTestBase
{

    public final static String keys[] = { "CCC", "AAA", "BBB" };

    private SelfValidatingSortedQueueEntryList _queueEntryList;

    @Before
    public void setUp() throws Exception
    {
        Map<String,Object> attributes = new HashMap<String,Object>();
        attributes.put(Queue.ID,UUID.randomUUID());
        attributes.put(Queue.NAME, getTestName());
        attributes.put(Queue.DURABLE, false);
        attributes.put(Queue.LIFETIME_POLICY, LifetimePolicy.PERMANENT);
        attributes.put(SortedQueue.SORT_KEY, "KEY");

        final QueueManagingVirtualHost virtualHost = BrokerTestHelper.createVirtualHost("testVH", this);
        SortedQueueImpl queue = new SortedQueueImpl(attributes, virtualHost)
        {
            SelfValidatingSortedQueueEntryList _entries;
            @Override
            protected void onOpen()
            {
                super.onOpen();
                _entries = new SelfValidatingSortedQueueEntryList(this);
            }

            @Override
            SelfValidatingSortedQueueEntryList getEntries()
            {
                return _entries;
            }
        };
        queue.open();
        _queueEntryList = (SelfValidatingSortedQueueEntryList) queue.getEntries();
        super.setUp();
    }

    @Override
    public QueueEntryImpl getQueueEntryImpl(int msgId)
    {
        final ServerMessage message = mock(ServerMessage.class);
        AMQMessageHeader hdr = mock(AMQMessageHeader.class);
        when(message.getMessageHeader()).thenReturn(hdr);
        when(hdr.getHeader(eq("KEY"))).thenReturn(keys[msgId-1]);
        when(hdr.containsHeader(eq("KEY"))).thenReturn(true);
        when(hdr.getHeaderNames()).thenReturn(Collections.singleton("KEY"));

        final MessageReference reference = mock(MessageReference.class);
        when(reference.getMessage()).thenReturn(message);
        when(message.newReference()).thenReturn(reference);
        when(message.newReference(any(TransactionLogResource.class))).thenReturn(reference);
        return _queueEntryList.add(message, null);
    }

    @Override
    @Test
    public void testCompareTo()
    {
        assertTrue(_queueEntry.compareTo(_queueEntry2) > 0);
        assertTrue(_queueEntry.compareTo(_queueEntry3) > 0);

        assertTrue(_queueEntry2.compareTo(_queueEntry3) < 0);
        assertTrue(_queueEntry2.compareTo(_queueEntry) < 0);

        assertTrue(_queueEntry3.compareTo(_queueEntry2) > 0);
        assertTrue(_queueEntry3.compareTo(_queueEntry) < 0);

        assertTrue(_queueEntry.compareTo(_queueEntry) == 0);
        assertTrue(_queueEntry2.compareTo(_queueEntry2) == 0);
        assertTrue(_queueEntry3.compareTo(_queueEntry3) == 0);
    }

    @Override
    @Test
    public void testTraverseWithNoDeletedEntries()
    {
        QueueEntry current = _queueEntry2;

        current = current.getNextValidEntry();
        assertSame("Unexpected current entry", _queueEntry3, current);

        current = current.getNextValidEntry();
        assertSame("Unexpected current entry", _queueEntry, current);

        current = current.getNextValidEntry();
        assertNull(current);
    }

    @Override
    @Test
    public void testTraverseWithDeletedEntries()
    {
        // Delete 2nd queue entry
        _queueEntry3.acquire();
        _queueEntry3.delete();
        assertTrue(_queueEntry3.isDeleted());

        QueueEntry current = _queueEntry2;

        current = current.getNextValidEntry();
        assertSame("Unexpected current entry", _queueEntry, current);

        current = current.getNextValidEntry();
        assertNull(current);
    }
}
