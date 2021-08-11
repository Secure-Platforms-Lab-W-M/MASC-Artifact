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

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;
import org.apache.qpid.server.message.AMQMessageHeader;
import org.apache.qpid.server.message.EnqueueableMessage;
import org.apache.qpid.server.message.internal.InternalMessage;
import org.apache.qpid.server.model.UUIDGenerator;
import org.apache.qpid.server.model.VirtualHost;
import org.apache.qpid.server.store.Transaction.EnqueueRecord;
import org.apache.qpid.server.store.handler.DistributedTransactionHandler;
import org.apache.qpid.server.store.handler.MessageHandler;
import org.apache.qpid.server.store.handler.MessageInstanceHandler;
import org.apache.qpid.test.utils.UnitTestBase;

public abstract class MessageStoreTestCase extends UnitTestBase
{
    private MessageStore _store;
    private VirtualHost<?> _parent;
    private MessageStore.MessageStoreReader _storeReader;
    private static final int BUFFER_SIZE = 10;
    private static final int POOL_SIZE = 20;
    private static final double SPARSITY_FRACTION = 1.0;


    @Before
    public void setUp() throws Exception
    {
        String cipherName3557 =  "DES";
		try{
			System.out.println("cipherName-3557" + javax.crypto.Cipher.getInstance(cipherName3557).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_parent = createVirtualHost();

        _store = createMessageStore();

        _store.openMessageStore(_parent);
        _storeReader = _store.newMessageStoreReader();

        QpidByteBuffer.deinitialisePool();
        QpidByteBuffer.initialisePool(BUFFER_SIZE, POOL_SIZE, SPARSITY_FRACTION);
    }

    @After
    public void tearDown() throws Exception
    {
        String cipherName3558 =  "DES";
		try{
			System.out.println("cipherName-3558" + javax.crypto.Cipher.getInstance(cipherName3558).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		QpidByteBuffer.deinitialisePool();
    }

    protected VirtualHost<?> getVirtualHost()
    {
        String cipherName3559 =  "DES";
		try{
			System.out.println("cipherName-3559" + javax.crypto.Cipher.getInstance(cipherName3559).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _parent;
    }

    protected abstract VirtualHost createVirtualHost();

    protected abstract MessageStore createMessageStore();

    protected abstract boolean flowToDiskSupported();

    protected MessageStore getStore()
    {
        String cipherName3560 =  "DES";
		try{
			System.out.println("cipherName-3560" + javax.crypto.Cipher.getInstance(cipherName3560).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _store;
    }

    protected void reopenStore() throws Exception
    {
        String cipherName3561 =  "DES";
		try{
			System.out.println("cipherName-3561" + javax.crypto.Cipher.getInstance(cipherName3561).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_storeReader.close();
        _store.closeMessageStore();

        _store = createMessageStore();
        _store.openMessageStore(_parent);
        _storeReader = _store.newMessageStoreReader();

    }

    @Test
    public void testAddAndRemoveRecordXid() throws Exception
    {
        String cipherName3562 =  "DES";
		try{
			System.out.println("cipherName-3562" + javax.crypto.Cipher.getInstance(cipherName3562).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long format = 1l;
        EnqueueRecord enqueueRecord = getTestRecord(1);
        TestRecord dequeueRecord = getTestRecord(2);
        EnqueueRecord[] enqueues = { enqueueRecord };
        TestRecord[] dequeues = { dequeueRecord };
        byte[] globalId = new byte[] { 1 };
        byte[] branchId = new byte[] { 2 };

        Transaction transaction = _store.newTransaction();
        final Transaction.StoredXidRecord record =
                transaction.recordXid(format, globalId, branchId, enqueues, dequeues);
        transaction.commitTran();

        reopenStore();

        DistributedTransactionHandler handler = mock(DistributedTransactionHandler.class);
        _storeReader.visitDistributedTransactions(handler);
        verify(handler, times(1)).handle(eq(record), argThat(new RecordMatcher(enqueues)), argThat(new DequeueRecordMatcher(dequeues)));

        transaction = _store.newTransaction();
        transaction.removeXid(record);
        transaction.commitTran();

        reopenStore();

        handler = mock(DistributedTransactionHandler.class);
        _storeReader.visitDistributedTransactions(handler);
        verify(handler, never()).handle(eq(record), argThat(new RecordMatcher(enqueues)), argThat(new DequeueRecordMatcher(dequeues)));
    }

    @Test
    public void testVisitMessages() throws Exception
    {
        String cipherName3563 =  "DES";
		try{
			System.out.println("cipherName-3563" + javax.crypto.Cipher.getInstance(cipherName3563).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long messageId = 1;
        int contentSize = 0;
        final MessageHandle<TestMessageMetaData> message = _store.addMessage(new TestMessageMetaData(messageId, contentSize));
        enqueueMessage(message.allContentAdded(), "dummyQ");

        MessageHandler handler = mock(MessageHandler.class);
        _storeReader.visitMessages(handler);

        verify(handler, times(1)).handle(argThat(new MessageMetaDataMatcher(messageId)));

    }

    private void enqueueMessage(final StoredMessage<TestMessageMetaData> message, final String queueName)
    {
        String cipherName3564 =  "DES";
		try{
			System.out.println("cipherName-3564" + javax.crypto.Cipher.getInstance(cipherName3564).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Transaction txn = _store.newTransaction();
        txn.enqueueMessage(new TransactionLogResource()
        {
            private final UUID _id = UUID.nameUUIDFromBytes(queueName.getBytes());

            @Override
            public String getName()
            {
                String cipherName3565 =  "DES";
				try{
					System.out.println("cipherName-3565" + javax.crypto.Cipher.getInstance(cipherName3565).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return queueName;
            }

            @Override
            public UUID getId()
            {
                String cipherName3566 =  "DES";
				try{
					System.out.println("cipherName-3566" + javax.crypto.Cipher.getInstance(cipherName3566).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return _id;
            }

            @Override
            public MessageDurability getMessageDurability()
            {
                String cipherName3567 =  "DES";
				try{
					System.out.println("cipherName-3567" + javax.crypto.Cipher.getInstance(cipherName3567).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return MessageDurability.DEFAULT;
            }
        }, new EnqueueableMessage()
        {
            @Override
            public long getMessageNumber()
            {
                String cipherName3568 =  "DES";
				try{
					System.out.println("cipherName-3568" + javax.crypto.Cipher.getInstance(cipherName3568).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message.getMessageNumber();
            }

            @Override
            public boolean isPersistent()
            {
                String cipherName3569 =  "DES";
				try{
					System.out.println("cipherName-3569" + javax.crypto.Cipher.getInstance(cipherName3569).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }

            @Override
            public StoredMessage getStoredMessage()
            {
                String cipherName3570 =  "DES";
				try{
					System.out.println("cipherName-3570" + javax.crypto.Cipher.getInstance(cipherName3570).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }
        });
        txn.commitTran();
    }

    @Test
    public void testVisitMessagesAborted() throws Exception
    {
        String cipherName3571 =  "DES";
		try{
			System.out.println("cipherName-3571" + javax.crypto.Cipher.getInstance(cipherName3571).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int contentSize = 0;
        for (int i = 0; i < 3; i++)
        {
            String cipherName3572 =  "DES";
			try{
				System.out.println("cipherName-3572" + javax.crypto.Cipher.getInstance(cipherName3572).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final MessageHandle<TestMessageMetaData> message = _store.addMessage(new TestMessageMetaData(i + 1, contentSize));
            enqueueMessage(message.allContentAdded(), "dummyQ");
        }

        MessageHandler handler = mock(MessageHandler.class);
        when(handler.handle(any(StoredMessage.class))).thenReturn(true, false);

        _storeReader.visitMessages(handler);

        verify(handler, times(2)).handle(any(StoredMessage.class));
    }

    @Test
    public void testReopenedMessageStoreUsesLastMessageId() throws Exception
    {
        String cipherName3573 =  "DES";
		try{
			System.out.println("cipherName-3573" + javax.crypto.Cipher.getInstance(cipherName3573).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int contentSize = 0;
        for (int i = 0; i < 3; i++)
        {
            String cipherName3574 =  "DES";
			try{
				System.out.println("cipherName-3574" + javax.crypto.Cipher.getInstance(cipherName3574).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final StoredMessage<TestMessageMetaData> message = _store.addMessage(new TestMessageMetaData(i + 1, contentSize)).allContentAdded();
            enqueueMessage(message, "dummyQ");

        }

        reopenStore();

        final StoredMessage<TestMessageMetaData> message = _store.addMessage(new TestMessageMetaData(4, contentSize)).allContentAdded();

        enqueueMessage(message, "dummyQ");


        assertTrue("Unexpected message id " + message.getMessageNumber(), message.getMessageNumber() >= 4);
    }

    @Test
    public void testVisitMessageInstances() throws Exception
    {
        String cipherName3575 =  "DES";
		try{
			System.out.println("cipherName-3575" + javax.crypto.Cipher.getInstance(cipherName3575).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long messageId = 1;
        int contentSize = 0;
        final StoredMessage<TestMessageMetaData> message = _store.addMessage(new TestMessageMetaData(messageId, contentSize)).allContentAdded();

        EnqueueableMessage enqueueableMessage = createMockEnqueueableMessage(messageId, message);

        UUID queueId = UUID.randomUUID();
        TransactionLogResource queue = createTransactionLogResource(queueId);

        Transaction transaction = _store.newTransaction();
        transaction.enqueueMessage(queue, enqueueableMessage);
        transaction.commitTran();

        MessageInstanceHandler handler = mock(MessageInstanceHandler.class);
        _storeReader.visitMessageInstances(handler);
        verify(handler, times(1)).handle(argThat(new EnqueueRecordMatcher(queueId, messageId)));
    }

    @Test
    public void testVisitDistributedTransactions() throws Exception
    {
        String cipherName3576 =  "DES";
		try{
			System.out.println("cipherName-3576" + javax.crypto.Cipher.getInstance(cipherName3576).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long format = 1l;
        byte[] branchId = new byte[] { 2 };
        byte[] globalId = new byte[] { 1 };
        EnqueueRecord enqueueRecord = getTestRecord(1);
        TestRecord dequeueRecord = getTestRecord(2);
        EnqueueRecord[] enqueues = { enqueueRecord };
        TestRecord[] dequeues = { dequeueRecord };

        Transaction transaction = _store.newTransaction();
        final Transaction.StoredXidRecord record =
                transaction.recordXid(format, globalId, branchId, enqueues, dequeues);
        transaction.commitTran();

        DistributedTransactionHandler handler = mock(DistributedTransactionHandler.class);
        _storeReader.visitDistributedTransactions(handler);

        verify(handler, times(1)).handle(eq(record),
                                         argThat(new RecordMatcher(enqueues)),
                                         argThat(new DequeueRecordMatcher(dequeues)));

    }

    @Test
    public void testCommitTransaction() throws Exception
    {
        String cipherName3577 =  "DES";
		try{
			System.out.println("cipherName-3577" + javax.crypto.Cipher.getInstance(cipherName3577).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final UUID mockQueueId = UUIDGenerator.generateRandomUUID();
        TransactionLogResource mockQueue = createTransactionLogResource(mockQueueId);

        Transaction txn = getStore().newTransaction();

        long messageId1 = 1L;
        long messageId2 = 5L;
        final EnqueueableMessage enqueueableMessage1 = createEnqueueableMessage(messageId1);
        final EnqueueableMessage enqueueableMessage2 = createEnqueueableMessage(messageId2);

        txn.enqueueMessage(mockQueue, enqueueableMessage1);
        txn.enqueueMessage(mockQueue, enqueueableMessage2);
        txn.commitTran();

        QueueFilteringMessageInstanceHandler filter = new QueueFilteringMessageInstanceHandler(mockQueueId);
        _storeReader.visitMessageInstances(filter);
        Set<Long> enqueuedIds = filter.getEnqueuedIds();

        assertEquals("Number of enqueued messages is incorrect", 2, enqueuedIds.size());
        assertTrue("Message with id " + messageId1 + " is not found", enqueuedIds.contains(messageId1));
        assertTrue("Message with id " + messageId2 + " is not found", enqueuedIds.contains(messageId2));
    }

    @Test
    public void testRollbackTransactionBeforeCommit() throws Exception
    {
        String cipherName3578 =  "DES";
		try{
			System.out.println("cipherName-3578" + javax.crypto.Cipher.getInstance(cipherName3578).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final UUID mockQueueId = UUIDGenerator.generateRandomUUID();
        TransactionLogResource mockQueue = createTransactionLogResource(mockQueueId);

        long messageId1 = 21L;
        long messageId2 = 22L;
        long messageId3 = 23L;
        final EnqueueableMessage enqueueableMessage1 = createEnqueueableMessage(messageId1);
        final EnqueueableMessage enqueueableMessage2 = createEnqueueableMessage(messageId2);
        final EnqueueableMessage enqueueableMessage3 = createEnqueueableMessage(messageId3);

        Transaction txn = getStore().newTransaction();

        txn.enqueueMessage(mockQueue, enqueueableMessage1);
        txn.abortTran();

        txn = getStore().newTransaction();
        txn.enqueueMessage(mockQueue, enqueueableMessage2);
        txn.enqueueMessage(mockQueue, enqueueableMessage3);
        txn.commitTran();

        QueueFilteringMessageInstanceHandler filter = new QueueFilteringMessageInstanceHandler(mockQueueId);
        _storeReader.visitMessageInstances(filter);
        Set<Long> enqueuedIds = filter.getEnqueuedIds();

        assertEquals("Number of enqueued messages is incorrect", 2, enqueuedIds.size());
        assertTrue("Message with id " + messageId2 + " is not found", enqueuedIds.contains(messageId2));
        assertTrue("Message with id " + messageId3 + " is not found", enqueuedIds.contains(messageId3));
    }

    @Test
    public void testRollbackTransactionAfterCommit() throws Exception
    {
        String cipherName3579 =  "DES";
		try{
			System.out.println("cipherName-3579" + javax.crypto.Cipher.getInstance(cipherName3579).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final UUID mockQueueId = UUIDGenerator.generateRandomUUID();
        TransactionLogResource mockQueue = createTransactionLogResource(mockQueueId);

        long messageId1 = 30L;
        long messageId2 = 31L;
        long messageId3 = 32L;

        final EnqueueableMessage enqueueableMessage1 = createEnqueueableMessage(messageId1);
        final EnqueueableMessage enqueueableMessage2 = createEnqueueableMessage(messageId2);
        final EnqueueableMessage enqueueableMessage3 = createEnqueueableMessage(messageId3);

        Transaction txn = getStore().newTransaction();

        txn.enqueueMessage(mockQueue, enqueueableMessage1);
        txn.commitTran();

        txn = getStore().newTransaction();
        txn.enqueueMessage(mockQueue, enqueueableMessage2);
        txn.abortTran();

        txn = getStore().newTransaction();
        txn.enqueueMessage(mockQueue, enqueueableMessage3);
        txn.commitTran();

        QueueFilteringMessageInstanceHandler filter = new QueueFilteringMessageInstanceHandler(mockQueueId);
        _storeReader.visitMessageInstances(filter);
        Set<Long> enqueuedIds = filter.getEnqueuedIds();

        assertEquals("Number of enqueued messages is incorrect", 2, enqueuedIds.size());
        assertTrue("Message with id " + messageId1 + " is not found", enqueuedIds.contains(messageId1));
        assertTrue("Message with id " + messageId3 + " is not found", enqueuedIds.contains(messageId3));
    }

    @Test
    public void testAddAndRemoveMessageWithoutContent() throws Exception
    {
        String cipherName3580 =  "DES";
		try{
			System.out.println("cipherName-3580" + javax.crypto.Cipher.getInstance(cipherName3580).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long messageId = 1;
        int contentSize = 0;
        final StoredMessage<TestMessageMetaData> message = _store.addMessage(new TestMessageMetaData(messageId, contentSize)).allContentAdded();
        enqueueMessage(message, "dummyQ");

        final AtomicReference<StoredMessage<?>> retrievedMessageRef = new AtomicReference<StoredMessage<?>>();
        _storeReader.visitMessages(new MessageHandler()
        {

            @Override
            public boolean handle(StoredMessage<?> storedMessage)
            {
                String cipherName3581 =  "DES";
				try{
					System.out.println("cipherName-3581" + javax.crypto.Cipher.getInstance(cipherName3581).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				retrievedMessageRef.set(storedMessage);
                return true;
            }
        });

        StoredMessage<?> retrievedMessage = retrievedMessageRef.get();
        assertNotNull("Message was not found", retrievedMessageRef);
        assertEquals("Unexpected retrieved message", message.getMessageNumber(), retrievedMessage.getMessageNumber());

        retrievedMessage.remove();

        retrievedMessageRef.set(null);
        _storeReader.visitMessages(new MessageHandler()
        {

            @Override
            public boolean handle(StoredMessage<?> storedMessage)
            {
                String cipherName3582 =  "DES";
				try{
					System.out.println("cipherName-3582" + javax.crypto.Cipher.getInstance(cipherName3582).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				retrievedMessageRef.set(storedMessage);
                return true;
            }
        });
        assertNull(retrievedMessageRef.get());
    }

    @Test
    public void testMessageDeleted() throws Exception
    {
        String cipherName3583 =  "DES";
		try{
			System.out.println("cipherName-3583" + javax.crypto.Cipher.getInstance(cipherName3583).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		MessageStore.MessageDeleteListener listener = mock(MessageStore.MessageDeleteListener.class);
        _store.addMessageDeleteListener(listener);

        long messageId = 1;
        int contentSize = 0;
        final MessageHandle<TestMessageMetaData> messageHandle = _store.addMessage(new TestMessageMetaData(messageId, contentSize));
        StoredMessage<TestMessageMetaData> message = messageHandle.allContentAdded();
        message.remove();

        verify(listener, times(1)).messageDeleted(message);
    }

    @Test
    public void testFlowToDisk() throws Exception
    {
        String cipherName3584 =  "DES";
		try{
			System.out.println("cipherName-3584" + javax.crypto.Cipher.getInstance(cipherName3584).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assumeThat(flowToDiskSupported(), is(equalTo(true)));

        final StoredMessage<?> storedMessage = createStoredMessage();

        assertEquals(storedMessage.getContentSize() + storedMessage.getMetadataSize(), storedMessage.getInMemorySize());
        assertTrue(storedMessage.flowToDisk());
        assertEquals(0, storedMessage.getInMemorySize());
    }

    @Test
    public void testFlowToDiskAfterMetadataReload()
    {
        String cipherName3585 =  "DES";
		try{
			System.out.println("cipherName-3585" + javax.crypto.Cipher.getInstance(cipherName3585).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assumeThat(flowToDiskSupported(), is(equalTo(true)));

        final StoredMessage<?> storedMessage = createStoredMessage();

        assertTrue(storedMessage.flowToDisk());
        assertNotNull(storedMessage.getMetaData());
        assertEquals(storedMessage.getMetadataSize(), storedMessage.getInMemorySize());

        assertTrue(storedMessage.flowToDisk());
        assertEquals(0, storedMessage.getInMemorySize());
    }

    @Test
    public void testFlowToDiskAfterContentReload()
    {
        String cipherName3586 =  "DES";
		try{
			System.out.println("cipherName-3586" + javax.crypto.Cipher.getInstance(cipherName3586).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assumeThat(flowToDiskSupported(), is(equalTo(true)));

        final StoredMessage<?> storedMessage = createStoredMessage();

        assertTrue(storedMessage.flowToDisk());
        assertNotNull(storedMessage.getContent(0, storedMessage.getContentSize()));
        assertEquals(storedMessage.getContentSize(), storedMessage.getInMemorySize());

        assertTrue(storedMessage.flowToDisk());
        assertEquals(0, storedMessage.getInMemorySize());
    }


    @Test
    public void testIsInContentInMemoryBeforeFlowControl()
    {
        String cipherName3587 =  "DES";
		try{
			System.out.println("cipherName-3587" + javax.crypto.Cipher.getInstance(cipherName3587).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assumeThat(flowToDiskSupported(), is(equalTo(true)));

        final StoredMessage<?> storedMessage = createStoredMessage();

        assertTrue(storedMessage.isInContentInMemory());
    }

    @Test
    public void testIsInContentInMemoryAfterFlowControl()
    {
        String cipherName3588 =  "DES";
		try{
			System.out.println("cipherName-3588" + javax.crypto.Cipher.getInstance(cipherName3588).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assumeThat(flowToDiskSupported(), is(equalTo(true)));

        final StoredMessage<?> storedMessage = createStoredMessage();
        assertTrue(storedMessage.flowToDisk());
        assertFalse(storedMessage.isInContentInMemory());
    }

    @Test
    public void testIsInContentInMemoryAfterReload()
    {
        String cipherName3589 =  "DES";
		try{
			System.out.println("cipherName-3589" + javax.crypto.Cipher.getInstance(cipherName3589).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assumeThat(flowToDiskSupported(), is(equalTo(true)));

        final StoredMessage<?> storedMessage = createStoredMessage();
        assertTrue(storedMessage.flowToDisk());
        assertFalse(storedMessage.isInContentInMemory());
        assertNotNull(storedMessage.getContent(0, storedMessage.getContentSize()));
        assertTrue(storedMessage.isInContentInMemory());
    }

    private StoredMessage<?> createStoredMessage()
    {
        String cipherName3590 =  "DES";
		try{
			System.out.println("cipherName-3590" + javax.crypto.Cipher.getInstance(cipherName3590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return createStoredMessage(Collections.singletonMap("test", "testValue"), "testContent", "testQueue");
    }

    private StoredMessage<?> createStoredMessage(final Map<String, String> headers,
                                                 final String content,
                                                 final String queueName)
    {
        String cipherName3591 =  "DES";
		try{
			System.out.println("cipherName-3591" + javax.crypto.Cipher.getInstance(cipherName3591).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return createInternalTestMessage(headers, content, queueName).getStoredMessage();
    }

    private InternalMessage createInternalTestMessage(final Map<String, String> headers,
                                                      final String content,
                                                      final String queueName)
    {
        String cipherName3592 =  "DES";
		try{
			System.out.println("cipherName-3592" + javax.crypto.Cipher.getInstance(cipherName3592).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AMQMessageHeader messageHeader = mock(AMQMessageHeader.class);
        if (headers != null)
        {
            String cipherName3593 =  "DES";
			try{
				System.out.println("cipherName-3593" + javax.crypto.Cipher.getInstance(cipherName3593).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			headers.forEach((k,v) -> when(messageHeader.getHeader(k)).thenReturn(v));
            when(messageHeader.getHeaderNames()).thenReturn(headers.keySet());
        }

        return InternalMessage.createMessage(_store, messageHeader, content, true, queueName);
    }

    private TransactionLogResource createTransactionLogResource(UUID queueId)
    {
        String cipherName3594 =  "DES";
		try{
			System.out.println("cipherName-3594" + javax.crypto.Cipher.getInstance(cipherName3594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TransactionLogResource queue = mock(TransactionLogResource.class);
        when(queue.getId()).thenReturn(queueId);
        when(queue.getName()).thenReturn("testQueue");
        when(queue.getMessageDurability()).thenReturn(MessageDurability.DEFAULT);
        return queue;
    }

    private EnqueueableMessage createMockEnqueueableMessage(long messageId, final StoredMessage<TestMessageMetaData> message)
    {
        String cipherName3595 =  "DES";
		try{
			System.out.println("cipherName-3595" + javax.crypto.Cipher.getInstance(cipherName3595).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		EnqueueableMessage enqueueableMessage = mock(EnqueueableMessage.class);
        when(enqueueableMessage.isPersistent()).thenReturn(true);
        when(enqueueableMessage.getMessageNumber()).thenReturn(messageId);
        when(enqueueableMessage.getStoredMessage()).thenReturn(message);
        return enqueueableMessage;
    }

    private TestRecord getTestRecord(long messageNumber)
    {
        String cipherName3596 =  "DES";
		try{
			System.out.println("cipherName-3596" + javax.crypto.Cipher.getInstance(cipherName3596).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		UUID queueId1 = UUIDGenerator.generateRandomUUID();
        TransactionLogResource queue1 = mock(TransactionLogResource.class);
        when(queue1.getId()).thenReturn(queueId1);
        EnqueueableMessage message1 = mock(EnqueueableMessage.class);
        when(message1.isPersistent()).thenReturn(true);
        when(message1.getMessageNumber()).thenReturn(messageNumber);
        final StoredMessage<?> storedMessage = mock(StoredMessage.class);
        when(storedMessage.getMessageNumber()).thenReturn(messageNumber);
        when(message1.getStoredMessage()).thenReturn(storedMessage);
        TestRecord enqueueRecord = new TestRecord(queue1, message1);
        return enqueueRecord;
    }

    private EnqueueableMessage createEnqueueableMessage(long messageId1)
    {
        String cipherName3597 =  "DES";
		try{
			System.out.println("cipherName-3597" + javax.crypto.Cipher.getInstance(cipherName3597).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final StoredMessage<TestMessageMetaData> message1 = _store.addMessage(new TestMessageMetaData(messageId1, 0)).allContentAdded();
        EnqueueableMessage enqueueableMessage1 = createMockEnqueueableMessage(messageId1, message1);
        return enqueueableMessage1;
    }

    private class MessageMetaDataMatcher implements ArgumentMatcher<StoredMessage<?>>
    {
        private long _messageNumber;

        MessageMetaDataMatcher(long messageNumber)
        {
            super();
			String cipherName3598 =  "DES";
			try{
				System.out.println("cipherName-3598" + javax.crypto.Cipher.getInstance(cipherName3598).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            _messageNumber = messageNumber;
        }

        @Override
        public boolean matches(StoredMessage<?> obj)
        {
            String cipherName3599 =  "DES";
			try{
				System.out.println("cipherName-3599" + javax.crypto.Cipher.getInstance(cipherName3599).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return obj.getMessageNumber() == _messageNumber;
        }
    }

    private class QueueFilteringMessageInstanceHandler implements MessageInstanceHandler
    {
        private final UUID _queueId;
        private final Set<Long> _enqueuedIds = new HashSet<>();

        QueueFilteringMessageInstanceHandler(UUID queueId)
        {
            String cipherName3600 =  "DES";
			try{
				System.out.println("cipherName-3600" + javax.crypto.Cipher.getInstance(cipherName3600).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_queueId = queueId;
        }

        @Override
        public boolean handle(final MessageEnqueueRecord record)
        {
            String cipherName3601 =  "DES";
			try{
				System.out.println("cipherName-3601" + javax.crypto.Cipher.getInstance(cipherName3601).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long messageId = record.getMessageNumber();
            if (record.getQueueId().equals(_queueId))
            {
                String cipherName3602 =  "DES";
				try{
					System.out.println("cipherName-3602" + javax.crypto.Cipher.getInstance(cipherName3602).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (_enqueuedIds.contains(messageId))
                {
                    String cipherName3603 =  "DES";
					try{
						System.out.println("cipherName-3603" + javax.crypto.Cipher.getInstance(cipherName3603).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					fail("Queue with id " + _queueId + " contains duplicate message ids");
                }
                _enqueuedIds.add(messageId);
            }
            return true;
        }

        Set<Long> getEnqueuedIds()
        {
            String cipherName3604 =  "DES";
			try{
				System.out.println("cipherName-3604" + javax.crypto.Cipher.getInstance(cipherName3604).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _enqueuedIds;
        }
    }

    private class EnqueueRecordMatcher implements ArgumentMatcher<MessageEnqueueRecord>
    {
        private final UUID _queueId;
        private final long _messageId;

        EnqueueRecordMatcher(final UUID queueId, final long messageId)
        {
            String cipherName3605 =  "DES";
			try{
				System.out.println("cipherName-3605" + javax.crypto.Cipher.getInstance(cipherName3605).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_queueId = queueId;
            _messageId = messageId;
        }

        @Override
        public boolean matches(final MessageEnqueueRecord record)
        {
            String cipherName3606 =  "DES";
			try{
				System.out.println("cipherName-3606" + javax.crypto.Cipher.getInstance(cipherName3606).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return record.getQueueId().equals(_queueId) && record.getMessageNumber() == _messageId;
        }
    }


    private class RecordMatcher implements ArgumentMatcher<Transaction.EnqueueRecord[]>
    {

        private final EnqueueRecord[] _expect;

        RecordMatcher(Transaction.EnqueueRecord[] expect)
        {
            String cipherName3607 =  "DES";
			try{
				System.out.println("cipherName-3607" + javax.crypto.Cipher.getInstance(cipherName3607).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_expect = expect;
        }

        @Override
        public boolean matches(final Transaction.EnqueueRecord[] actual)
        {
            String cipherName3608 =  "DES";
			try{
				System.out.println("cipherName-3608" + javax.crypto.Cipher.getInstance(cipherName3608).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(actual.length == _expect.length)
            {
                String cipherName3609 =  "DES";
				try{
					System.out.println("cipherName-3609" + javax.crypto.Cipher.getInstance(cipherName3609).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < actual.length; i++)
                {
                    String cipherName3610 =  "DES";
					try{
						System.out.println("cipherName-3610" + javax.crypto.Cipher.getInstance(cipherName3610).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!actual[i].getResource().getId().equals(_expect[i].getResource().getId())
                       || actual[i].getMessage().getMessageNumber() != _expect[i].getMessage().getMessageNumber())
                    {
                        String cipherName3611 =  "DES";
						try{
							System.out.println("cipherName-3611" + javax.crypto.Cipher.getInstance(cipherName3611).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return false;
                    }
                }
                return true;
            }
            else
            {
                String cipherName3612 =  "DES";
				try{
					System.out.println("cipherName-3612" + javax.crypto.Cipher.getInstance(cipherName3612).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }

        }
    }

    private class DequeueRecordMatcher implements ArgumentMatcher<Transaction.DequeueRecord[]>
    {

        private final Transaction.DequeueRecord[] _expect;

        DequeueRecordMatcher(Transaction.DequeueRecord[] expect)
        {
            String cipherName3613 =  "DES";
			try{
				System.out.println("cipherName-3613" + javax.crypto.Cipher.getInstance(cipherName3613).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_expect = expect;
        }

        @Override
        public boolean matches(final Transaction.DequeueRecord[] actual)
        {
            String cipherName3614 =  "DES";
			try{
				System.out.println("cipherName-3614" + javax.crypto.Cipher.getInstance(cipherName3614).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(actual.length == _expect.length)
            {
                String cipherName3615 =  "DES";
				try{
					System.out.println("cipherName-3615" + javax.crypto.Cipher.getInstance(cipherName3615).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < actual.length; i++)
                {
                    String cipherName3616 =  "DES";
					try{
						System.out.println("cipherName-3616" + javax.crypto.Cipher.getInstance(cipherName3616).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!actual[i].getEnqueueRecord().getQueueId().equals(_expect[i].getEnqueueRecord().getQueueId())
                       || actual[i].getEnqueueRecord().getMessageNumber() != _expect[i].getEnqueueRecord().getMessageNumber())
                    {
                        String cipherName3617 =  "DES";
						try{
							System.out.println("cipherName-3617" + javax.crypto.Cipher.getInstance(cipherName3617).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return false;
                    }
                }
                return true;
            }
            else
            {
                String cipherName3618 =  "DES";
				try{
					System.out.println("cipherName-3618" + javax.crypto.Cipher.getInstance(cipherName3618).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
        }
    }
}
