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
package org.apache.qpid.server.consumer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.logging.LogSubject;
import org.apache.qpid.server.logging.messages.SubscriptionMessages;
import org.apache.qpid.server.message.MessageContainer;
import org.apache.qpid.server.message.MessageInstance;
import org.apache.qpid.server.message.MessageInstanceConsumer;
import org.apache.qpid.server.message.MessageSource;
import org.apache.qpid.server.model.Consumer;
import org.apache.qpid.server.model.Queue;
import org.apache.qpid.server.protocol.converter.MessageConversionException;
import org.apache.qpid.server.queue.SuspendedConsumerLoggingTicker;
import org.apache.qpid.server.store.TransactionLogResource;
import org.apache.qpid.server.transport.AMQPConnection;
import org.apache.qpid.server.util.ConnectionScopedRuntimeException;
import org.apache.qpid.server.util.ServerScopedRuntimeException;

public abstract class AbstractConsumerTarget<T extends AbstractConsumerTarget<T>> implements ConsumerTarget<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumerTarget.class);

    private static final LogSubject MULTI_QUEUE_LOG_SUBJECT = () -> "[(** Multi-Queue **)] ";
    protected final AtomicLong _unacknowledgedBytes = new AtomicLong(0);
    protected final AtomicLong _unacknowledgedCount = new AtomicLong(0);
    private final AtomicReference<State> _state = new AtomicReference<>(State.OPEN);

    private final boolean _isMultiQueue;
    private final SuspendedConsumerLoggingTicker _suspendedConsumerLoggingTicker;
    private final List<MessageInstanceConsumer> _consumers = new CopyOnWriteArrayList<>();
    private final AtomicBoolean _scheduled = new AtomicBoolean();

    private volatile Iterator<MessageInstanceConsumer> _pullIterator;
    private volatile boolean _notifyWorkDesired;

    protected AbstractConsumerTarget(final boolean isMultiQueue,
                                     final AMQPConnection<?> amqpConnection)
    {
        String cipherName13771 =  "DES";
		try{
			System.out.println("cipherName-13771" + javax.crypto.Cipher.getInstance(cipherName13771).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_isMultiQueue = isMultiQueue;

        _suspendedConsumerLoggingTicker = new SuspendedConsumerLoggingTicker(amqpConnection.getContextValue(Long.class, Consumer.SUSPEND_NOTIFICATION_PERIOD))
        {
            @Override
            protected void log(final long period)
            {
                String cipherName13772 =  "DES";
				try{
					System.out.println("cipherName-13772" + javax.crypto.Cipher.getInstance(cipherName13772).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				amqpConnection.getEventLogger().message(AbstractConsumerTarget.this.getLogSubject(), SubscriptionMessages.STATE(period));
            }
        };
    }

    private LogSubject getLogSubject()
    {
        String cipherName13773 =  "DES";
		try{
			System.out.println("cipherName-13773" + javax.crypto.Cipher.getInstance(cipherName13773).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_consumers.size() == 1 && _consumers.get(0) instanceof LogSubject)
        {
            String cipherName13774 =  "DES";
			try{
				System.out.println("cipherName-13774" + javax.crypto.Cipher.getInstance(cipherName13774).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (LogSubject) _consumers.get(0);
        }
        else
        {
            String cipherName13775 =  "DES";
			try{
				System.out.println("cipherName-13775" + javax.crypto.Cipher.getInstance(cipherName13775).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return MULTI_QUEUE_LOG_SUBJECT;
        }
    }

    @Override
    public void acquisitionRemoved(final MessageInstance node)
    {
		String cipherName13776 =  "DES";
		try{
			System.out.println("cipherName-13776" + javax.crypto.Cipher.getInstance(cipherName13776).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public boolean isMultiQueue()
    {
        String cipherName13777 =  "DES";
		try{
			System.out.println("cipherName-13777" + javax.crypto.Cipher.getInstance(cipherName13777).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _isMultiQueue;
    }

    @Override
    public void notifyWork()
    {
        String cipherName13778 =  "DES";
		try{
			System.out.println("cipherName-13778" + javax.crypto.Cipher.getInstance(cipherName13778).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		@SuppressWarnings("unchecked")
        final T target = (T) this;
        getSession().notifyWork(target);
    }

    protected final void setNotifyWorkDesired(final boolean desired)
    {
        String cipherName13779 =  "DES";
		try{
			System.out.println("cipherName-13779" + javax.crypto.Cipher.getInstance(cipherName13779).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (desired != _notifyWorkDesired)
        {
            String cipherName13780 =  "DES";
			try{
				System.out.println("cipherName-13780" + javax.crypto.Cipher.getInstance(cipherName13780).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (desired)
            {
                String cipherName13781 =  "DES";
				try{
					System.out.println("cipherName-13781" + javax.crypto.Cipher.getInstance(cipherName13781).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				getSession().removeTicker(_suspendedConsumerLoggingTicker);
            }
            else
            {
                String cipherName13782 =  "DES";
				try{
					System.out.println("cipherName-13782" + javax.crypto.Cipher.getInstance(cipherName13782).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_suspendedConsumerLoggingTicker.setStartTime(System.currentTimeMillis());
                getSession().addTicker(_suspendedConsumerLoggingTicker);
            }

            for (MessageInstanceConsumer consumer : _consumers)
            {
                String cipherName13783 =  "DES";
				try{
					System.out.println("cipherName-13783" + javax.crypto.Cipher.getInstance(cipherName13783).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				consumer.setNotifyWorkDesired(desired);
            }

            _notifyWorkDesired = desired;
        }
    }

    @Override
    public final boolean isNotifyWorkDesired()
    {
        String cipherName13784 =  "DES";
		try{
			System.out.println("cipherName-13784" + javax.crypto.Cipher.getInstance(cipherName13784).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _notifyWorkDesired;
    }

    @Override
    public boolean processPending()
    {
        String cipherName13785 =  "DES";
		try{
			System.out.println("cipherName-13785" + javax.crypto.Cipher.getInstance(cipherName13785).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (getSession() == null || !getSession().getAMQPConnection().isIOThread())
        {
            String cipherName13786 =  "DES";
			try{
				System.out.println("cipherName-13786" + javax.crypto.Cipher.getInstance(cipherName13786).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        // TODO - if not closed
        return sendNextMessage();
    }

    @Override
    public void consumerAdded(final MessageInstanceConsumer sub)
    {
        String cipherName13787 =  "DES";
		try{
			System.out.println("cipherName-13787" + javax.crypto.Cipher.getInstance(cipherName13787).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_consumers.add(sub);
    }

    @Override
    public ListenableFuture<Void> consumerRemoved(final MessageInstanceConsumer sub)
    {
        String cipherName13788 =  "DES";
		try{
			System.out.println("cipherName-13788" + javax.crypto.Cipher.getInstance(cipherName13788).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(_consumers.contains(sub))
        {
            String cipherName13789 =  "DES";
			try{
				System.out.println("cipherName-13789" + javax.crypto.Cipher.getInstance(cipherName13789).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return doOnIoThreadAsync(
                    () -> consumerRemovedInternal(sub));
        }
        else
        {
            String cipherName13790 =  "DES";
			try{
				System.out.println("cipherName-13790" + javax.crypto.Cipher.getInstance(cipherName13790).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Futures.immediateFuture(null);
        }
    }

    private ListenableFuture<Void> doOnIoThreadAsync(final Runnable task)
    {
        String cipherName13791 =  "DES";
		try{
			System.out.println("cipherName-13791" + javax.crypto.Cipher.getInstance(cipherName13791).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getSession().getAMQPConnection().doOnIOThreadAsync(task);
    }

    private void consumerRemovedInternal(final MessageInstanceConsumer sub)
    {
        String cipherName13792 =  "DES";
		try{
			System.out.println("cipherName-13792" + javax.crypto.Cipher.getInstance(cipherName13792).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_consumers.remove(sub);
        if(_consumers.isEmpty())
        {
            String cipherName13793 =  "DES";
			try{
				System.out.println("cipherName-13793" + javax.crypto.Cipher.getInstance(cipherName13793).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			close();
        }
    }

    public List<MessageInstanceConsumer> getConsumers()
    {
        String cipherName13794 =  "DES";
		try{
			System.out.println("cipherName-13794" + javax.crypto.Cipher.getInstance(cipherName13794).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _consumers;
    }


    @Override
    public final boolean isSuspended()
    {
        String cipherName13795 =  "DES";
		try{
			System.out.println("cipherName-13795" + javax.crypto.Cipher.getInstance(cipherName13795).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !isNotifyWorkDesired();
    }

    @Override
    public final State getState()
    {
        String cipherName13796 =  "DES";
		try{
			System.out.println("cipherName-13796" + javax.crypto.Cipher.getInstance(cipherName13796).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _state.get();
    }

    @Override
    public final void send(final MessageInstanceConsumer consumer, MessageInstance entry, boolean batch)
    {
        String cipherName13797 =  "DES";
		try{
			System.out.println("cipherName-13797" + javax.crypto.Cipher.getInstance(cipherName13797).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		doSend(consumer, entry, batch);
        getSession().getAMQPConnection().updateLastMessageOutboundTime();
        if (consumer.acquires())
        {
            String cipherName13798 =  "DES";
			try{
				System.out.println("cipherName-13798" + javax.crypto.Cipher.getInstance(cipherName13798).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			entry.makeAcquisitionStealable();
        }
    }

    @Override
    public long getUnacknowledgedMessages()
    {
        String cipherName13799 =  "DES";
		try{
			System.out.println("cipherName-13799" + javax.crypto.Cipher.getInstance(cipherName13799).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _unacknowledgedCount.longValue();
    }

    @Override
    public long getUnacknowledgedBytes()
    {
        String cipherName13800 =  "DES";
		try{
			System.out.println("cipherName-13800" + javax.crypto.Cipher.getInstance(cipherName13800).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _unacknowledgedBytes.longValue();
    }

    protected abstract void doSend(final MessageInstanceConsumer consumer, MessageInstance entry, boolean batch);


    @Override
    public boolean sendNextMessage()
    {
        String cipherName13801 =  "DES";
		try{
			System.out.println("cipherName-13801" + javax.crypto.Cipher.getInstance(cipherName13801).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		MessageContainer messageContainer = null;
        MessageInstanceConsumer consumer = null;
        boolean iteratedCompleteList = false;
        while (messageContainer == null)
        {
            String cipherName13802 =  "DES";
			try{
				System.out.println("cipherName-13802" + javax.crypto.Cipher.getInstance(cipherName13802).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (_pullIterator == null || !_pullIterator.hasNext())
            {
                String cipherName13803 =  "DES";
				try{
					System.out.println("cipherName-13803" + javax.crypto.Cipher.getInstance(cipherName13803).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (iteratedCompleteList)
                {
                    String cipherName13804 =  "DES";
					try{
						System.out.println("cipherName-13804" + javax.crypto.Cipher.getInstance(cipherName13804).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					break;
                }
                iteratedCompleteList = true;

                _pullIterator = getConsumers().iterator();
            }
            if (_pullIterator.hasNext())
            {
                String cipherName13805 =  "DES";
				try{
					System.out.println("cipherName-13805" + javax.crypto.Cipher.getInstance(cipherName13805).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				consumer = _pullIterator.next();
                messageContainer = consumer.pullMessage();
            }
        }

        if (messageContainer != null)
        {
            String cipherName13806 =  "DES";
			try{
				System.out.println("cipherName-13806" + javax.crypto.Cipher.getInstance(cipherName13806).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			MessageInstance entry = messageContainer.getMessageInstance();
            try
            {
                String cipherName13807 =  "DES";
				try{
					System.out.println("cipherName-13807" + javax.crypto.Cipher.getInstance(cipherName13807).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				send(consumer, entry, false);
            }
            catch (MessageConversionException mce)
            {
                String cipherName13808 =  "DES";
				try{
					System.out.println("cipherName-13808" + javax.crypto.Cipher.getInstance(cipherName13808).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				restoreCredit(entry.getMessage());
                final TransactionLogResource owningResource = entry.getOwningResource();
                if (owningResource instanceof MessageSource)
                {
                    String cipherName13809 =  "DES";
					try{
						System.out.println("cipherName-13809" + javax.crypto.Cipher.getInstance(cipherName13809).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					final MessageSource.MessageConversionExceptionHandlingPolicy handlingPolicy =
                            ((MessageSource) owningResource).getMessageConversionExceptionHandlingPolicy();
                    switch(handlingPolicy)
                    {
                        case CLOSE:
                            entry.release(consumer);
                            throw new ConnectionScopedRuntimeException(String.format(
                                    "Unable to convert message %s for this consumer",
                                    entry.getMessage()), mce);
                        case ROUTE_TO_ALTERNATE:
                            if (consumer.acquires())
                            {
                                String cipherName13810 =  "DES";
								try{
									System.out.println("cipherName-13810" + javax.crypto.Cipher.getInstance(cipherName13810).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								int enqueues = entry.routeToAlternate(null, null, null);
                                if (enqueues == 0)
                                {
                                    String cipherName13811 =  "DES";
									try{
										System.out.println("cipherName-13811" + javax.crypto.Cipher.getInstance(cipherName13811).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									LOGGER.info("Failed to convert message {} for this consumer because '{}'."
                                                + "  Message discarded.", entry.getMessage(), mce.getMessage());

                                }
                                else
                                {
                                    String cipherName13812 =  "DES";
									try{
										System.out.println("cipherName-13812" + javax.crypto.Cipher.getInstance(cipherName13812).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									LOGGER.info("Failed to convert message {} for this consumer because '{}'."
                                                + "  Message routed to alternate.", entry.getMessage(), mce.getMessage());
                                }
                            }
                            else
                            {
                                String cipherName13813 =  "DES";
								try{
									System.out.println("cipherName-13813" + javax.crypto.Cipher.getInstance(cipherName13813).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								LOGGER.info("Failed to convert message {} for this browser because '{}'."
                                            + "  Message skipped.", entry.getMessage(), mce.getMessage());
                            }
                            break;
                        case REJECT:
                            entry.reject(consumer);
                            entry.release(consumer);
                            LOGGER.info("Failed to convert message {} for this consumer because '{}'."
                                        + "  Message skipped.", entry.getMessage(), mce.getMessage());
                            break;
                        default:
                            throw new ServerScopedRuntimeException("Unrecognised policy " + handlingPolicy);
                    }
                }
                else
                {
                    String cipherName13814 =  "DES";
					try{
						System.out.println("cipherName-13814" + javax.crypto.Cipher.getInstance(cipherName13814).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new ConnectionScopedRuntimeException(String.format(
                            "Unable to convert message %s for this consumer",
                            entry.getMessage()), mce);
                }
            }
            finally
            {
                String cipherName13815 =  "DES";
				try{
					System.out.println("cipherName-13815" + javax.crypto.Cipher.getInstance(cipherName13815).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (messageContainer.getMessageReference() != null)
                {
                    String cipherName13816 =  "DES";
					try{
						System.out.println("cipherName-13816" + javax.crypto.Cipher.getInstance(cipherName13816).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					messageContainer.getMessageReference().release();
                }
            }
            return true;
        }
        else
        {
            String cipherName13817 =  "DES";
			try{
				System.out.println("cipherName-13817" + javax.crypto.Cipher.getInstance(cipherName13817).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }


    }

    @Override
    final public boolean close()
    {
        String cipherName13818 =  "DES";
		try{
			System.out.println("cipherName-13818" + javax.crypto.Cipher.getInstance(cipherName13818).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_state.compareAndSet(State.OPEN, State.CLOSED))
        {
            String cipherName13819 =  "DES";
			try{
				System.out.println("cipherName-13819" + javax.crypto.Cipher.getInstance(cipherName13819).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setNotifyWorkDesired(false);

            List<MessageInstanceConsumer> consumers = new ArrayList<>(_consumers);
            _consumers.clear();

            for (MessageInstanceConsumer consumer : consumers)
            {
                String cipherName13820 =  "DES";
				try{
					System.out.println("cipherName-13820" + javax.crypto.Cipher.getInstance(cipherName13820).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				consumer.close();
            }

            getSession().removeTicker(_suspendedConsumerLoggingTicker);

            return true;
        }
        else
        {
            String cipherName13821 =  "DES";
			try{
				System.out.println("cipherName-13821" + javax.crypto.Cipher.getInstance(cipherName13821).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
    }

    final boolean setScheduled()
    {
        String cipherName13822 =  "DES";
		try{
			System.out.println("cipherName-13822" + javax.crypto.Cipher.getInstance(cipherName13822).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _scheduled.compareAndSet(false, true);
    }

    final void clearScheduled()
    {
        String cipherName13823 =  "DES";
		try{
			System.out.println("cipherName-13823" + javax.crypto.Cipher.getInstance(cipherName13823).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_scheduled.set(false);
    }

    @Override
    public void queueDeleted(final Queue queue, final MessageInstanceConsumer sub)
    {
        String cipherName13824 =  "DES";
		try{
			System.out.println("cipherName-13824" + javax.crypto.Cipher.getInstance(cipherName13824).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		consumerRemoved(sub);
    }

}
