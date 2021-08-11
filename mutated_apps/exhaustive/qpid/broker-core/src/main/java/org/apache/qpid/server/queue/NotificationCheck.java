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
package org.apache.qpid.server.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.message.ServerMessage;
import org.apache.qpid.server.model.Queue;
import org.apache.qpid.server.model.QueueNotificationListener;


public enum NotificationCheck
{

    MESSAGE_COUNT_ALERT
    {
        @Override
        public boolean notifyIfNecessary(ServerMessage<?> msg, Queue<?> queue, QueueNotificationListener listener)
        {
            String cipherName13382 =  "DES";
			try{
				System.out.println("cipherName-13382" + javax.crypto.Cipher.getInstance(cipherName13382).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int msgCount;
            final long maximumMessageCount = queue.getAlertThresholdQueueDepthMessages();
            if (maximumMessageCount!= 0 && (msgCount =  queue.getQueueDepthMessages()) >= maximumMessageCount)
            {
                String cipherName13383 =  "DES";
				try{
					System.out.println("cipherName-13383" + javax.crypto.Cipher.getInstance(cipherName13383).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String notificationMsg = msgCount + ": Maximum count on queue threshold ("+ maximumMessageCount +") breached.";

                logNotification(this, queue, notificationMsg);
                listener.notifyClients(this, queue, notificationMsg);
                return true;
            }
            return false;
        }
    },
    MESSAGE_SIZE_ALERT(true, true)
    {
        @Override
        public boolean notifyIfNecessary(ServerMessage<?> msg, Queue<?> queue, QueueNotificationListener  listener)
        {
            String cipherName13384 =  "DES";
			try{
				System.out.println("cipherName-13384" + javax.crypto.Cipher.getInstance(cipherName13384).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final long maximumMessageSize = queue.getAlertThresholdMessageSize();
            if(maximumMessageSize != 0)
            {
                String cipherName13385 =  "DES";
				try{
					System.out.println("cipherName-13385" + javax.crypto.Cipher.getInstance(cipherName13385).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// Check for threshold message size
                long messageSize;
                messageSize = (msg == null) ? 0 : msg.getSizeIncludingHeader();

                if (messageSize >= maximumMessageSize)
                {
                    String cipherName13386 =  "DES";
					try{
						System.out.println("cipherName-13386" + javax.crypto.Cipher.getInstance(cipherName13386).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String notificationMsg = messageSize + "b : Maximum message size threshold ("+ maximumMessageSize +") breached. [Message ID=" + msg.getMessageNumber() + "]";

                    logNotification(this, queue, notificationMsg);
                    listener.notifyClients(this, queue, notificationMsg);
                    return true;
                }
            }
            return false;
        }

    },
    QUEUE_DEPTH_ALERT
    {
        @Override
        public boolean notifyIfNecessary(ServerMessage<?> msg, Queue<?> queue, QueueNotificationListener  listener)
        {
            String cipherName13387 =  "DES";
			try{
				System.out.println("cipherName-13387" + javax.crypto.Cipher.getInstance(cipherName13387).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Check for threshold queue depth in bytes
            final long maximumQueueDepth = queue.getAlertThresholdQueueDepthBytes();

            if(maximumQueueDepth != 0)
            {
                String cipherName13388 =  "DES";
				try{
					System.out.println("cipherName-13388" + javax.crypto.Cipher.getInstance(cipherName13388).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				final long queueDepth = queue.getQueueDepthBytes();

                if (queueDepth >= maximumQueueDepth)
                {
                    String cipherName13389 =  "DES";
					try{
						System.out.println("cipherName-13389" + javax.crypto.Cipher.getInstance(cipherName13389).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String notificationMsg = (queueDepth>>10) + "Kb : Maximum queue depth threshold ("+(maximumQueueDepth>>10)+"Kb) breached.";

                    logNotification(this, queue, notificationMsg);
                    listener.notifyClients(this, queue, notificationMsg);
                    return true;
                }
            }
            return false;
        }

    },
    MESSAGE_AGE_ALERT(false, false)
    {
        @Override
        public boolean notifyIfNecessary(ServerMessage<?> msg, Queue<?> queue, QueueNotificationListener  listener)
        {

            String cipherName13390 =  "DES";
			try{
				System.out.println("cipherName-13390" + javax.crypto.Cipher.getInstance(cipherName13390).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final long maxMessageAge = queue.getAlertThresholdMessageAge();
            if(maxMessageAge != 0)
            {
                String cipherName13391 =  "DES";
				try{
					System.out.println("cipherName-13391" + javax.crypto.Cipher.getInstance(cipherName13391).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				final long currentTime = System.currentTimeMillis();
                final long thresholdTime = currentTime - maxMessageAge;
                final long firstArrivalTime = queue.getOldestMessageArrivalTime();

                if(firstArrivalTime != 0 && firstArrivalTime < thresholdTime)
                {
                    String cipherName13392 =  "DES";
					try{
						System.out.println("cipherName-13392" + javax.crypto.Cipher.getInstance(cipherName13392).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					long oldestAge = currentTime - firstArrivalTime;
                    String notificationMsg = (oldestAge/1000) + "s : Maximum age on queue threshold ("+(maxMessageAge /1000)+"s) breached.";

                    logNotification(this, queue, notificationMsg);
                    listener.notifyClients(this, queue, notificationMsg);

                    return true;
                }
            }
            return false;

        }

    }
    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationCheck.class);

    private final boolean _messageSpecific;
    private final boolean _checkOnMessageArrival;


    NotificationCheck()
    {
        this(false, true);
		String cipherName13393 =  "DES";
		try{
			System.out.println("cipherName-13393" + javax.crypto.Cipher.getInstance(cipherName13393).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    NotificationCheck(boolean messageSpecific, final boolean checkOnMessageArrival)
    {
        String cipherName13394 =  "DES";
		try{
			System.out.println("cipherName-13394" + javax.crypto.Cipher.getInstance(cipherName13394).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_messageSpecific = messageSpecific;
        _checkOnMessageArrival = checkOnMessageArrival;
    }

    public boolean isMessageSpecific()
    {
        String cipherName13395 =  "DES";
		try{
			System.out.println("cipherName-13395" + javax.crypto.Cipher.getInstance(cipherName13395).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _messageSpecific;
    }

    public boolean isCheckOnMessageArrival()
    {
        String cipherName13396 =  "DES";
		try{
			System.out.println("cipherName-13396" + javax.crypto.Cipher.getInstance(cipherName13396).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _checkOnMessageArrival;
    }

    public abstract boolean notifyIfNecessary(ServerMessage<?> msg, Queue<?> queue, QueueNotificationListener  listener);

    //A bit of a hack, only for use until we do the logging listener
    private static void logNotification(NotificationCheck notification, Queue<?> queue, String notificationMsg)
    {
        String cipherName13397 =  "DES";
		try{
			System.out.println("cipherName-13397" + javax.crypto.Cipher.getInstance(cipherName13397).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LOGGER.info(notification.name() + " On Queue " + queue.getName() + " - " + notificationMsg);
    }
}
