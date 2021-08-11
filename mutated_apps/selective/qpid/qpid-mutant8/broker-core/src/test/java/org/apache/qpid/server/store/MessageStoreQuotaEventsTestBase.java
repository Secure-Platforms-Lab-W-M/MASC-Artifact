/*
 *
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
package org.apache.qpid.server.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;
import org.apache.qpid.server.message.EnqueueableMessage;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.VirtualHost;
import org.apache.qpid.server.util.FileUtils;
import org.apache.qpid.test.utils.UnitTestBase;

public abstract class MessageStoreQuotaEventsTestBase extends UnitTestBase implements EventListener, TransactionLogResource
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageStoreQuotaEventsTestBase.class);

    protected static final byte[] MESSAGE_DATA = new byte[32 * 1024];

    private MessageStore _store;
    private File _storeLocation;

    private List<Event> _events;
    private UUID _transactionResource;

    protected abstract MessageStore createStore() throws Exception;
    protected abstract int getNumberOfMessagesToFillStore();

    @Before
    public void setUp() throws Exception
    {
        _storeLocation = new File(new File(TMP_FOLDER), getTestName());
        FileUtils.delete(_storeLocation, true);

        _store = createStore();

        ConfiguredObject<?> parent = createVirtualHost(_storeLocation.getAbsolutePath());

        _store.openMessageStore(parent);

        _transactionResource = UUID.randomUUID();
        _events = new ArrayList<Event>();
        _store.addEventListener(this, Event.PERSISTENT_MESSAGE_SIZE_OVERFULL, Event.PERSISTENT_MESSAGE_SIZE_UNDERFULL);
    }

    protected abstract VirtualHost createVirtualHost(String storeLocation);

    @After
    public void tearDown() throws Exception
    {
        if (_store != null)
        {
            _store.closeMessageStore();
        }
        if (_storeLocation != null)
        {
            FileUtils.delete(_storeLocation, true);
        }
    }

    @Test
    public void testOverflow() throws Exception
    {
        Transaction transaction = _store.newTransaction();

        List<EnqueueableMessage> messages = new ArrayList<EnqueueableMessage>();
        for (int i = 0; i < getNumberOfMessagesToFillStore(); i++)
        {
            EnqueueableMessage m = addMessage(i);
            messages.add(m);
            transaction.enqueueMessage(this, m);
        }
        transaction.commitTran();

        assertEvent(1, Event.PERSISTENT_MESSAGE_SIZE_OVERFULL);

        for (EnqueueableMessage m : messages)
        {
            m.getStoredMessage().remove();
        }

        assertEvent(2, Event.PERSISTENT_MESSAGE_SIZE_UNDERFULL);
    }

    protected EnqueueableMessage addMessage(long id)
    {
        StorableMessageMetaData metaData = createMetaData(id, MESSAGE_DATA.length);
        MessageHandle<?> handle = _store.addMessage(metaData);
        handle.addContent(QpidByteBuffer.wrap(MESSAGE_DATA));
        StoredMessage<? extends StorableMessageMetaData> storedMessage = handle.allContentAdded();
        TestMessage message = new TestMessage(id, storedMessage);
        return message;
    }

    private StorableMessageMetaData createMetaData(long id, int length)
    {
        return new TestMessageMetaData(id, length);
    }

    @Override
    public void event(Event event)
    {
        LOGGER.debug("Test event listener received event " + event);
        _events.add(event);
    }

    private void assertEvent(int expectedNumberOfEvents, Event... expectedEvents)
    {
        assertEquals("Unexpected number of events received ", expectedNumberOfEvents, _events.size());
        for (Event event : expectedEvents)
        {
            assertTrue("Expected event is not found:" + event, _events.contains(event));
        }
    }

    @Override
    public UUID getId()
    {
        return _transactionResource;
    }

    private static class TestMessage implements EnqueueableMessage
    {
        private final StoredMessage<?> _handle;
        private final long _messageId;

        public TestMessage(long messageId, StoredMessage<?> handle)
        {
            _messageId = messageId;
            _handle = handle;
        }

        @Override
        public long getMessageNumber()
        {
            return _messageId;
        }

        @Override
        public boolean isPersistent()
        {
            return true;
        }

        @Override
        public StoredMessage<?> getStoredMessage()
        {
            return _handle;
        }
    }

    @Override
    public MessageDurability getMessageDurability()
    {
        return MessageDurability.DEFAULT;
    }

    @Override
    public String getName()
    {
        return getTestName();
    }
}
