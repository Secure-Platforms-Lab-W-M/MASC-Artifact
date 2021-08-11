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
package org.apache.qpid.server.txn;

import java.util.Collection;
import java.util.List;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.message.EnqueueableMessage;
import org.apache.qpid.server.message.MessageInstance;
import org.apache.qpid.server.queue.BaseQueue;
import org.apache.qpid.server.store.MessageEnqueueRecord;
import org.apache.qpid.server.store.MessageStore;
import org.apache.qpid.server.store.Transaction;
import org.apache.qpid.server.store.TransactionLogResource;

/**
 * An implementation of ServerTransaction where each enqueue/dequeue
 * operation takes place within it own transaction.
 *
 * Since there is no long-lived transaction, the commit and rollback methods of
 * this implementation are empty.
 */
public class AsyncAutoCommitTransaction implements ServerTransaction
{
    static final String QPID_STRICT_ORDER_WITH_MIXED_DELIVERY_MODE = "qpid.strict_order_with_mixed_delivery_mode";

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncAutoCommitTransaction.class);

    private final MessageStore _messageStore;
    private final FutureRecorder _futureRecorder;

    //Set true to ensure strict ordering when enqueuing messages with mixed delivery mode, i.e. disable async persistence
    private boolean _strictOrderWithMixedDeliveryMode = Boolean.getBoolean(QPID_STRICT_ORDER_WITH_MIXED_DELIVERY_MODE);

    public interface FutureRecorder
    {
        void recordFuture(ListenableFuture<Void> future, Action action);

    }

    public AsyncAutoCommitTransaction(MessageStore transactionLog, FutureRecorder recorder)
    {
        String cipherName5928 =  "DES";
		try{
			System.out.println("cipherName-5928" + javax.crypto.Cipher.getInstance(cipherName5928).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_messageStore = transactionLog;
        _futureRecorder = recorder;
    }

    @Override
    public long getTransactionStartTime()
    {
        String cipherName5929 =  "DES";
		try{
			System.out.println("cipherName-5929" + javax.crypto.Cipher.getInstance(cipherName5929).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 0L;
    }

    @Override
    public long getTransactionUpdateTime()
    {
        String cipherName5930 =  "DES";
		try{
			System.out.println("cipherName-5930" + javax.crypto.Cipher.getInstance(cipherName5930).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 0L;
    }

    /**
     * Since AutoCommitTransaction have no concept of a long lived transaction, any Actions registered
     * by the caller are executed immediately.
     */
    @Override
    public void addPostTransactionAction(final Action immediateAction)
    {
        String cipherName5931 =  "DES";
		try{
			System.out.println("cipherName-5931" + javax.crypto.Cipher.getInstance(cipherName5931).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		addFuture(Futures.<Void>immediateFuture(null), immediateAction);

    }

    @Override
    public void dequeue(MessageEnqueueRecord record, Action postTransactionAction)
    {
        String cipherName5932 =  "DES";
		try{
			System.out.println("cipherName-5932" + javax.crypto.Cipher.getInstance(cipherName5932).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Transaction txn = null;
        try
        {
            String cipherName5933 =  "DES";
			try{
				System.out.println("cipherName-5933" + javax.crypto.Cipher.getInstance(cipherName5933).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ListenableFuture<Void> future;
            if(record != null)
            {
                String cipherName5934 =  "DES";
				try{
					System.out.println("cipherName-5934" + javax.crypto.Cipher.getInstance(cipherName5934).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.debug("Dequeue of message number {} from transaction log. Queue : {}", record.getMessageNumber(), record.getQueueId());

                txn = _messageStore.newTransaction();
                txn.dequeueMessage(record);
                future = txn.commitTranAsync((Void) null);

                txn = null;
            }
            else
            {
                String cipherName5935 =  "DES";
				try{
					System.out.println("cipherName-5935" + javax.crypto.Cipher.getInstance(cipherName5935).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				future = Futures.immediateFuture(null);
            }
            addFuture(future, postTransactionAction);
            postTransactionAction = null;
        }
        finally
        {
            String cipherName5936 =  "DES";
			try{
				System.out.println("cipherName-5936" + javax.crypto.Cipher.getInstance(cipherName5936).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rollbackIfNecessary(postTransactionAction, txn);
        }

    }


    private void addFuture(final ListenableFuture<Void> future, final Action action)
    {
        String cipherName5937 =  "DES";
		try{
			System.out.println("cipherName-5937" + javax.crypto.Cipher.getInstance(cipherName5937).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(action != null)
        {
            String cipherName5938 =  "DES";
			try{
				System.out.println("cipherName-5938" + javax.crypto.Cipher.getInstance(cipherName5938).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(future.isDone())
            {
                String cipherName5939 =  "DES";
				try{
					System.out.println("cipherName-5939" + javax.crypto.Cipher.getInstance(cipherName5939).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				action.postCommit();
            }
            else
            {
                String cipherName5940 =  "DES";
				try{
					System.out.println("cipherName-5940" + javax.crypto.Cipher.getInstance(cipherName5940).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_futureRecorder.recordFuture(future, action);
            }
        }
    }

    private void addEnqueueFuture(final ListenableFuture<Void> future, final Action action, boolean persistent)
    {
        String cipherName5941 =  "DES";
		try{
			System.out.println("cipherName-5941" + javax.crypto.Cipher.getInstance(cipherName5941).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(action != null)
        {
            String cipherName5942 =  "DES";
			try{
				System.out.println("cipherName-5942" + javax.crypto.Cipher.getInstance(cipherName5942).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// For persistent messages, do not synchronously invoke postCommit even if the future  is completed.
            // Otherwise, postCommit (which actually does the enqueuing) might be called on successive messages out of order.
            if(future.isDone() && !persistent && !_strictOrderWithMixedDeliveryMode)
            {
                String cipherName5943 =  "DES";
				try{
					System.out.println("cipherName-5943" + javax.crypto.Cipher.getInstance(cipherName5943).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				action.postCommit();
            }
            else
            {
                String cipherName5944 =  "DES";
				try{
					System.out.println("cipherName-5944" + javax.crypto.Cipher.getInstance(cipherName5944).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_futureRecorder.recordFuture(future, action);
            }
        }
    }

    @Override
    public void dequeue(Collection<MessageInstance> queueEntries, Action postTransactionAction)
    {
        String cipherName5945 =  "DES";
		try{
			System.out.println("cipherName-5945" + javax.crypto.Cipher.getInstance(cipherName5945).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Transaction txn = null;
        try
        {
            String cipherName5946 =  "DES";
			try{
				System.out.println("cipherName-5946" + javax.crypto.Cipher.getInstance(cipherName5946).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(MessageInstance entry : queueEntries)
            {
                String cipherName5947 =  "DES";
				try{
					System.out.println("cipherName-5947" + javax.crypto.Cipher.getInstance(cipherName5947).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				MessageEnqueueRecord record = entry.getEnqueueRecord();

                if(record != null)
                {
                    String cipherName5948 =  "DES";
					try{
						System.out.println("cipherName-5948" + javax.crypto.Cipher.getInstance(cipherName5948).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.debug("Dequeue of message number {} from transaction log. Queue : {}", record.getMessageNumber(), record.getQueueId());

                    if(txn == null)
                    {
                        String cipherName5949 =  "DES";
						try{
							System.out.println("cipherName-5949" + javax.crypto.Cipher.getInstance(cipherName5949).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						txn = _messageStore.newTransaction();
                    }

                    txn.dequeueMessage(record);
                }

            }
            ListenableFuture<Void> future;
            if(txn != null)
            {
                String cipherName5950 =  "DES";
				try{
					System.out.println("cipherName-5950" + javax.crypto.Cipher.getInstance(cipherName5950).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				future = txn.commitTranAsync((Void) null);
                txn = null;
            }
            else
            {
                String cipherName5951 =  "DES";
				try{
					System.out.println("cipherName-5951" + javax.crypto.Cipher.getInstance(cipherName5951).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				future = Futures.immediateFuture(null);
            }
            addFuture(future, postTransactionAction);
            postTransactionAction = null;
        }
        finally
        {
            String cipherName5952 =  "DES";
			try{
				System.out.println("cipherName-5952" + javax.crypto.Cipher.getInstance(cipherName5952).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rollbackIfNecessary(postTransactionAction, txn);
        }

    }


    @Override
    public void enqueue(TransactionLogResource queue, EnqueueableMessage message, EnqueueAction postTransactionAction)
    {
        String cipherName5953 =  "DES";
		try{
			System.out.println("cipherName-5953" + javax.crypto.Cipher.getInstance(cipherName5953).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Transaction txn = null;
        try
        {
            String cipherName5954 =  "DES";
			try{
				System.out.println("cipherName-5954" + javax.crypto.Cipher.getInstance(cipherName5954).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ListenableFuture<Void> future;
            final MessageEnqueueRecord enqueueRecord;
            if(queue.getMessageDurability().persist(message.isPersistent()))
            {
                String cipherName5955 =  "DES";
				try{
					System.out.println("cipherName-5955" + javax.crypto.Cipher.getInstance(cipherName5955).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.debug("Enqueue of message number {} to transaction log. Queue : {}", message.getMessageNumber(), queue.getName());

                txn = _messageStore.newTransaction();
                enqueueRecord = txn.enqueueMessage(queue, message);
                future = txn.commitTranAsync(null);
                txn = null;
            }
            else
            {
                String cipherName5956 =  "DES";
				try{
					System.out.println("cipherName-5956" + javax.crypto.Cipher.getInstance(cipherName5956).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				future = Futures.immediateFuture(null);
                enqueueRecord = null;
            }
            final EnqueueAction underlying = postTransactionAction;
            addEnqueueFuture(future, new Action()
            {
                @Override
                public void postCommit()
                {
                    String cipherName5957 =  "DES";
					try{
						System.out.println("cipherName-5957" + javax.crypto.Cipher.getInstance(cipherName5957).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					underlying.postCommit(enqueueRecord);
                }

                @Override
                public void onRollback()
                {
                    String cipherName5958 =  "DES";
					try{
						System.out.println("cipherName-5958" + javax.crypto.Cipher.getInstance(cipherName5958).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					underlying.onRollback();
                }
            }, message.isPersistent());
            postTransactionAction = null;
        }
        finally
        {
            String cipherName5959 =  "DES";
			try{
				System.out.println("cipherName-5959" + javax.crypto.Cipher.getInstance(cipherName5959).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final EnqueueAction underlying = postTransactionAction;

            rollbackIfNecessary(new Action()
            {
                @Override
                public void postCommit()
                {
					String cipherName5960 =  "DES";
					try{
						System.out.println("cipherName-5960" + javax.crypto.Cipher.getInstance(cipherName5960).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}

                }

                @Override
                public void onRollback()
                {
                    String cipherName5961 =  "DES";
					try{
						System.out.println("cipherName-5961" + javax.crypto.Cipher.getInstance(cipherName5961).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(underlying != null)
                    {
                        String cipherName5962 =  "DES";
						try{
							System.out.println("cipherName-5962" + javax.crypto.Cipher.getInstance(cipherName5962).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						underlying.onRollback();
                    }
                }
            }, txn);
        }


    }

    @Override
    public void enqueue(Collection<? extends BaseQueue> queues, EnqueueableMessage message, EnqueueAction postTransactionAction)
    {
        String cipherName5963 =  "DES";
		try{
			System.out.println("cipherName-5963" + javax.crypto.Cipher.getInstance(cipherName5963).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Transaction txn = null;
        try
        {
            String cipherName5964 =  "DES";
			try{
				System.out.println("cipherName-5964" + javax.crypto.Cipher.getInstance(cipherName5964).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final MessageEnqueueRecord[] records = new MessageEnqueueRecord[queues.size()];
            int i = 0;
            for(BaseQueue queue : queues)
            {
                String cipherName5965 =  "DES";
				try{
					System.out.println("cipherName-5965" + javax.crypto.Cipher.getInstance(cipherName5965).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (queue.getMessageDurability().persist(message.isPersistent()))
                {
                    String cipherName5966 =  "DES";
					try{
						System.out.println("cipherName-5966" + javax.crypto.Cipher.getInstance(cipherName5966).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.debug("Enqueue of message number {} to transaction log. Queue : {}", message.getMessageNumber(), queue.getName());

                    if (txn == null)
                    {
                        String cipherName5967 =  "DES";
						try{
							System.out.println("cipherName-5967" + javax.crypto.Cipher.getInstance(cipherName5967).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						txn = _messageStore.newTransaction();
                    }
                    records[i] = txn.enqueueMessage(queue, message);


                }
                i++;
            }

            ListenableFuture<Void> future;
            if (txn != null)
            {
                String cipherName5968 =  "DES";
				try{
					System.out.println("cipherName-5968" + javax.crypto.Cipher.getInstance(cipherName5968).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				future = txn.commitTranAsync((Void) null);
                txn = null;
            }
            else
            {
                String cipherName5969 =  "DES";
				try{
					System.out.println("cipherName-5969" + javax.crypto.Cipher.getInstance(cipherName5969).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				future = Futures.immediateFuture(null);
            }
            final EnqueueAction underlying = postTransactionAction;
            addEnqueueFuture(future, new Action()
            {
                @Override
                public void postCommit()
                {
                    String cipherName5970 =  "DES";
					try{
						System.out.println("cipherName-5970" + javax.crypto.Cipher.getInstance(cipherName5970).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(underlying != null)
                    {
                        String cipherName5971 =  "DES";
						try{
							System.out.println("cipherName-5971" + javax.crypto.Cipher.getInstance(cipherName5971).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						underlying.postCommit(records);
                    }
                }

                @Override
                public void onRollback()
                {
                     String cipherName5972 =  "DES";
					try{
						System.out.println("cipherName-5972" + javax.crypto.Cipher.getInstance(cipherName5972).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					underlying.onRollback();
                }
            }, message.isPersistent());
            postTransactionAction = null;


        }
        finally
        {
            String cipherName5973 =  "DES";
			try{
				System.out.println("cipherName-5973" + javax.crypto.Cipher.getInstance(cipherName5973).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final EnqueueAction underlying = postTransactionAction;

            rollbackIfNecessary(new Action()
            {
                @Override
                public void postCommit()
                {
					String cipherName5974 =  "DES";
					try{
						System.out.println("cipherName-5974" + javax.crypto.Cipher.getInstance(cipherName5974).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}

                }

                @Override
                public void onRollback()
                {
                    String cipherName5975 =  "DES";
					try{
						System.out.println("cipherName-5975" + javax.crypto.Cipher.getInstance(cipherName5975).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(underlying != null)
                    {
                        String cipherName5976 =  "DES";
						try{
							System.out.println("cipherName-5976" + javax.crypto.Cipher.getInstance(cipherName5976).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						underlying.onRollback();
                    }
                }
            }, txn);
        }

    }


    @Override
    public void commit(final Runnable immediatePostTransactionAction)
    {
        String cipherName5977 =  "DES";
		try{
			System.out.println("cipherName-5977" + javax.crypto.Cipher.getInstance(cipherName5977).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(immediatePostTransactionAction != null)
        {
            String cipherName5978 =  "DES";
			try{
				System.out.println("cipherName-5978" + javax.crypto.Cipher.getInstance(cipherName5978).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addFuture(Futures.<Void>immediateFuture(null), new Action()
            {
                @Override
                public void postCommit()
                {
                    String cipherName5979 =  "DES";
					try{
						System.out.println("cipherName-5979" + javax.crypto.Cipher.getInstance(cipherName5979).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					immediatePostTransactionAction.run();
                }

                @Override
                public void onRollback()
                {
					String cipherName5980 =  "DES";
					try{
						System.out.println("cipherName-5980" + javax.crypto.Cipher.getInstance(cipherName5980).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                }
            });
        }
    }

    @Override
    public void commit()
    {
		String cipherName5981 =  "DES";
		try{
			System.out.println("cipherName-5981" + javax.crypto.Cipher.getInstance(cipherName5981).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void rollback()
    {
		String cipherName5982 =  "DES";
		try{
			System.out.println("cipherName-5982" + javax.crypto.Cipher.getInstance(cipherName5982).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public boolean isTransactional()
    {
        String cipherName5983 =  "DES";
		try{
			System.out.println("cipherName-5983" + javax.crypto.Cipher.getInstance(cipherName5983).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    private void rollbackIfNecessary(Action postTransactionAction, Transaction txn)
    {
        String cipherName5984 =  "DES";
		try{
			System.out.println("cipherName-5984" + javax.crypto.Cipher.getInstance(cipherName5984).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (txn != null)
        {
            String cipherName5985 =  "DES";
			try{
				System.out.println("cipherName-5985" + javax.crypto.Cipher.getInstance(cipherName5985).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			txn.abortTran();
        }
        if (postTransactionAction != null)
        {
            String cipherName5986 =  "DES";
			try{
				System.out.println("cipherName-5986" + javax.crypto.Cipher.getInstance(cipherName5986).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			postTransactionAction.onRollback();
        }
    }

}
