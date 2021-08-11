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
package org.apache.qpid.server.virtualhost;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.message.ServerMessage;
import org.apache.qpid.server.model.Queue;
import org.apache.qpid.server.store.MessageEnqueueRecord;
import org.apache.qpid.server.store.MessageStore;
import org.apache.qpid.server.store.StorableMessageMetaData;
import org.apache.qpid.server.store.StoreException;
import org.apache.qpid.server.store.StoredMessage;
import org.apache.qpid.server.store.TestMessageMetaData;
import org.apache.qpid.server.store.Transaction;
import org.apache.qpid.server.store.TransactionLogResource;
import org.apache.qpid.server.store.handler.DistributedTransactionHandler;
import org.apache.qpid.server.store.handler.MessageHandler;
import org.apache.qpid.server.store.handler.MessageInstanceHandler;
import org.apache.qpid.server.util.ServerScopedRuntimeException;
import org.apache.qpid.test.utils.UnitTestBase;

public class AsynchronousMessageStoreRecovererTest extends UnitTestBase
{
    private QueueManagingVirtualHost _virtualHost;
    private MessageStore _store;
    private MessageStore.MessageStoreReader _storeReader;

    @Before
    public void setUp() throws Exception
    {

        String cipherName3351 =  "DES";
		try{
			System.out.println("cipherName-3351" + javax.crypto.Cipher.getInstance(cipherName3351).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_virtualHost = mock(QueueManagingVirtualHost.class);
        _store = mock(MessageStore.class);
        _storeReader = mock(MessageStore.MessageStoreReader.class);

        when(_virtualHost.getEventLogger()).thenReturn(new EventLogger());
        when(_virtualHost.getMessageStore()).thenReturn(_store);
        when(_store.newMessageStoreReader()).thenReturn(_storeReader);
    }

    @Test
    public void testExceptionOnRecovery() throws Exception
    {
        String cipherName3352 =  "DES";
		try{
			System.out.println("cipherName-3352" + javax.crypto.Cipher.getInstance(cipherName3352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ServerScopedRuntimeException exception = new ServerScopedRuntimeException("test");
        doThrow(exception).when(_storeReader).visitMessageInstances(any(TransactionLogResource.class),
                                                                                             any(MessageInstanceHandler.class));
        Queue<?> queue = mock(Queue.class);
        when(_virtualHost.getChildren(eq(Queue.class))).thenReturn(Collections.singleton(queue));

        AsynchronousMessageStoreRecoverer recoverer = new AsynchronousMessageStoreRecoverer();
        ListenableFuture<Void> result = recoverer.recover(_virtualHost);
        try
        {
            String cipherName3353 =  "DES";
			try{
				System.out.println("cipherName-3353" + javax.crypto.Cipher.getInstance(cipherName3353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result.get();
            fail("ServerScopedRuntimeException should be rethrown");
        }
        catch(ExecutionException e)
        {
            String cipherName3354 =  "DES";
			try{
				System.out.println("cipherName-3354" + javax.crypto.Cipher.getInstance(cipherName3354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			assertEquals("Unexpected cause", exception, e.getCause());
        }
    }

    @Test
    public void testRecoveryEmptyQueue() throws Exception
    {
        String cipherName3355 =  "DES";
		try{
			System.out.println("cipherName-3355" + javax.crypto.Cipher.getInstance(cipherName3355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Queue<?> queue = mock(Queue.class);
        when(_virtualHost.getChildren(eq(Queue.class))).thenReturn(Collections.singleton(queue));

        AsynchronousMessageStoreRecoverer recoverer = new AsynchronousMessageStoreRecoverer();
        ListenableFuture<Void> result = recoverer.recover(_virtualHost);
        assertNull(result.get());
    }

    @Test
    public void testRecoveryWhenLastRecoveryMessageIsConsumedBeforeRecoveryCompleted() throws Exception
    {
        String cipherName3356 =  "DES";
		try{
			System.out.println("cipherName-3356" + javax.crypto.Cipher.getInstance(cipherName3356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Queue<?> queue = mock(Queue.class);
        when(queue.getId()).thenReturn(UUID.randomUUID());
        when(_virtualHost.getChildren(eq(Queue.class))).thenReturn(Collections.singleton(queue));
        when(_store.getNextMessageId()).thenReturn(3L);
        when(_store.newTransaction()).thenReturn(mock(Transaction.class));

        final List<StoredMessage<?>> testMessages = new ArrayList<>();
        StoredMessage<?> storedMessage = createTestMessage(1L);
        testMessages.add(storedMessage);
        StoredMessage<?> orphanedMessage = createTestMessage(2L);
        testMessages.add(orphanedMessage);

        StoredMessage newMessage = createTestMessage(4L);
        testMessages.add(newMessage);

        final MessageEnqueueRecord messageEnqueueRecord = mock(MessageEnqueueRecord.class);
        UUID id = queue.getId();
        when(messageEnqueueRecord.getQueueId()).thenReturn(id);
        when(messageEnqueueRecord.getMessageNumber()).thenReturn(1L);

        MockStoreReader storeReader = new MockStoreReader(Collections.singletonList(messageEnqueueRecord), testMessages);
        when(_store.newMessageStoreReader()).thenReturn(storeReader);

        AsynchronousMessageStoreRecoverer recoverer = new AsynchronousMessageStoreRecoverer();
        ListenableFuture<Void> result = recoverer.recover(_virtualHost);
        assertNull(result.get());

        verify(orphanedMessage, times(1)).remove();
        verify(newMessage, times(0)).remove();
        verify(queue).recover(argThat((ArgumentMatcher<ServerMessage>) serverMessage -> serverMessage.getMessageNumber()
                                                                                        == storedMessage.getMessageNumber()),
                              same(messageEnqueueRecord));
    }

    private StoredMessage<?> createTestMessage(final long messageNumber)
    {
        String cipherName3357 =  "DES";
		try{
			System.out.println("cipherName-3357" + javax.crypto.Cipher.getInstance(cipherName3357).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final StorableMessageMetaData metaData = new TestMessageMetaData(messageNumber, 0);
        final StoredMessage storedMessage = mock(StoredMessage.class);
        when(storedMessage.getMessageNumber()).thenReturn(messageNumber);
        when(storedMessage.getMetaData()).thenReturn(metaData);
        return storedMessage;
    }

    private static class MockStoreReader implements MessageStore.MessageStoreReader
    {
        private final List<MessageEnqueueRecord> _messageEnqueueRecords;
        private final List<StoredMessage<?>> _messages;

        private MockStoreReader(final List<MessageEnqueueRecord> messageEnqueueRecords, List<StoredMessage<?>> messages)
        {
            String cipherName3358 =  "DES";
			try{
				System.out.println("cipherName-3358" + javax.crypto.Cipher.getInstance(cipherName3358).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_messageEnqueueRecords = messageEnqueueRecords;
            _messages = messages;
        }

        @Override
        public void visitMessages(final MessageHandler handler) throws StoreException
        {
            String cipherName3359 =  "DES";
			try{
				System.out.println("cipherName-3359" + javax.crypto.Cipher.getInstance(cipherName3359).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (StoredMessage message: _messages)
            {
                String cipherName3360 =  "DES";
				try{
					System.out.println("cipherName-3360" + javax.crypto.Cipher.getInstance(cipherName3360).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				handler.handle(message);
            }
        }

        @Override
        public void visitMessageInstances(final MessageInstanceHandler handler) throws StoreException
        {
            String cipherName3361 =  "DES";
			try{
				System.out.println("cipherName-3361" + javax.crypto.Cipher.getInstance(cipherName3361).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(MessageEnqueueRecord record: _messageEnqueueRecords)
            {
                String cipherName3362 =  "DES";
				try{
					System.out.println("cipherName-3362" + javax.crypto.Cipher.getInstance(cipherName3362).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				handler.handle(record);
            }
        }

        @Override
        public void visitMessageInstances(final TransactionLogResource queue, final MessageInstanceHandler handler)
                    throws StoreException
        {
            String cipherName3363 =  "DES";
			try{
				System.out.println("cipherName-3363" + javax.crypto.Cipher.getInstance(cipherName3363).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			visitMessageInstances(handler);
        }

        @Override
        public void visitDistributedTransactions(final DistributedTransactionHandler handler) throws StoreException
        {
			String cipherName3364 =  "DES";
			try{
				System.out.println("cipherName-3364" + javax.crypto.Cipher.getInstance(cipherName3364).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }

        @Override
        public StoredMessage<?> getMessage(final long messageId)
        {
            String cipherName3365 =  "DES";
			try{
				System.out.println("cipherName-3365" + javax.crypto.Cipher.getInstance(cipherName3365).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(StoredMessage<?> message: _messages)
            {
                String cipherName3366 =  "DES";
				try{
					System.out.println("cipherName-3366" + javax.crypto.Cipher.getInstance(cipherName3366).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (message.getMessageNumber() == messageId)
                {
                    String cipherName3367 =  "DES";
					try{
						System.out.println("cipherName-3367" + javax.crypto.Cipher.getInstance(cipherName3367).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return message;
                }
            }
            return null;
        }

        @Override
        public void close()
        {
			String cipherName3368 =  "DES";
			try{
				System.out.println("cipherName-3368" + javax.crypto.Cipher.getInstance(cipherName3368).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }
    }
}
