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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.message.MessageInstanceConsumer;
import org.apache.qpid.server.message.ServerMessage;
import org.apache.qpid.server.store.MessageEnqueueRecord;
import org.apache.qpid.server.txn.AutoCommitTransaction;
import org.apache.qpid.server.txn.ServerTransaction;

public class LastValueQueueList extends OrderedQueueEntryList
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LastValueQueueList.class);

    private static final HeadCreator HEAD_CREATOR = new HeadCreator()
    {

        @Override
        public ConflationQueueEntry createHead(final QueueEntryList list)
        {
            String cipherName12281 =  "DES";
			try{
				System.out.println("cipherName-12281" + javax.crypto.Cipher.getInstance(cipherName12281).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return ((LastValueQueueList)list).createHead();
        }
    };

    private final String _conflationKey;
    private final ConcurrentMap<Object, AtomicReference<ConflationQueueEntry>> _latestValuesMap =
        new ConcurrentHashMap<Object, AtomicReference<ConflationQueueEntry>>();

    private final ConflationQueueEntry _deleteInProgress = new ConflationQueueEntry(this);
    private final ConflationQueueEntry _newerEntryAlreadyBeenAndGone = new ConflationQueueEntry(this);

    public LastValueQueueList(LastValueQueue<?> queue, QueueStatistics queueStatistics)
    {
        super(queue, queueStatistics, HEAD_CREATOR);
		String cipherName12282 =  "DES";
		try{
			System.out.println("cipherName-12282" + javax.crypto.Cipher.getInstance(cipherName12282).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _conflationKey = queue.getLvqKey();
    }

    private ConflationQueueEntry createHead()
    {
        String cipherName12283 =  "DES";
		try{
			System.out.println("cipherName-12283" + javax.crypto.Cipher.getInstance(cipherName12283).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new ConflationQueueEntry(this);
    }

    @Override
    protected ConflationQueueEntry createQueueEntry(ServerMessage message,
                                                    final MessageEnqueueRecord enqueueRecord)
    {
        String cipherName12284 =  "DES";
		try{
			System.out.println("cipherName-12284" + javax.crypto.Cipher.getInstance(cipherName12284).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new ConflationQueueEntry(this, message, enqueueRecord);
    }



    /**
     * Updates the list using super.add and also updates {@link #_latestValuesMap} and discards entries as necessary.
     */
    @Override
    public ConflationQueueEntry add(final ServerMessage message, final MessageEnqueueRecord enqueueRecord)
    {
        String cipherName12285 =  "DES";
		try{
			System.out.println("cipherName-12285" + javax.crypto.Cipher.getInstance(cipherName12285).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final ConflationQueueEntry addedEntry = (ConflationQueueEntry) super.add(message, enqueueRecord);

        final Object keyValue = message.getMessageHeader().getHeader(_conflationKey);
        if (keyValue != null)
        {
            String cipherName12286 =  "DES";
			try{
				System.out.println("cipherName-12286" + javax.crypto.Cipher.getInstance(cipherName12286).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(LOGGER.isDebugEnabled())
            {
                String cipherName12287 =  "DES";
				try{
					System.out.println("cipherName-12287" + javax.crypto.Cipher.getInstance(cipherName12287).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.debug("Adding entry " + addedEntry + " for message " + message.getMessageNumber() + " with conflation key " + keyValue);
            }

            final AtomicReference<ConflationQueueEntry> referenceToEntry = new AtomicReference<ConflationQueueEntry>(addedEntry);
            AtomicReference<ConflationQueueEntry> entryReferenceFromMap;
            ConflationQueueEntry entryFromMap;

            // Iterate until we have got a valid atomic reference object and either the referent is newer than the current
            // entry, or the current entry has replaced it in the reference. Note that the _deletedEntryPlaceholder is a special value
            // indicating that the reference object is no longer valid (it is being removed from the map).
            boolean keepTryingToUpdateEntryReference;
            do
            {
                String cipherName12288 =  "DES";
				try{
					System.out.println("cipherName-12288" + javax.crypto.Cipher.getInstance(cipherName12288).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				do
                {
                    String cipherName12289 =  "DES";
					try{
						System.out.println("cipherName-12289" + javax.crypto.Cipher.getInstance(cipherName12289).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					entryReferenceFromMap = getOrPutIfAbsent(keyValue, referenceToEntry);

                    // entryFromMap can be either an older entry, a newer entry (added recently by another thread), or addedEntry (if it's for a new key value)  
                    entryFromMap = entryReferenceFromMap.get();
                }
                while(entryFromMap == _deleteInProgress);

                boolean entryFromMapIsOlder = entryFromMap != _newerEntryAlreadyBeenAndGone && entryFromMap.compareTo(addedEntry) < 0;

                keepTryingToUpdateEntryReference = entryFromMapIsOlder
                        && !entryReferenceFromMap.compareAndSet(entryFromMap, addedEntry);
            }
            while(keepTryingToUpdateEntryReference);

            if (entryFromMap == _newerEntryAlreadyBeenAndGone)
            {
                String cipherName12290 =  "DES";
				try{
					System.out.println("cipherName-12290" + javax.crypto.Cipher.getInstance(cipherName12290).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				discardEntry(addedEntry);
            }
            else if (entryFromMap.compareTo(addedEntry) > 0)
            {
                String cipherName12291 =  "DES";
				try{
					System.out.println("cipherName-12291" + javax.crypto.Cipher.getInstance(cipherName12291).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(LOGGER.isDebugEnabled())
                {
                    String cipherName12292 =  "DES";
					try{
						System.out.println("cipherName-12292" + javax.crypto.Cipher.getInstance(cipherName12292).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.debug("New entry " + addedEntry.getEntryId() + " for message " + addedEntry.getMessage().getMessageNumber() + " being immediately discarded because a newer entry arrived. The newer entry is: " + entryFromMap + " for message " + entryFromMap.getMessage().getMessageNumber());
                }
                discardEntry(addedEntry);
            }
            else if (entryFromMap.compareTo(addedEntry) < 0)
            {
                String cipherName12293 =  "DES";
				try{
					System.out.println("cipherName-12293" + javax.crypto.Cipher.getInstance(cipherName12293).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(LOGGER.isDebugEnabled())
                {
                    String cipherName12294 =  "DES";
					try{
						System.out.println("cipherName-12294" + javax.crypto.Cipher.getInstance(cipherName12294).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.debug("Entry " + addedEntry + " for message " + addedEntry.getMessage().getMessageNumber() + " replacing older entry " + entryFromMap + " for message " + entryFromMap.getMessage().getMessageNumber());
                }
                discardEntry(entryFromMap);
            }

            addedEntry.setLatestValueReference(entryReferenceFromMap);
        }

        return addedEntry;
    }

    @Override
    public QueueEntry getLeastSignificantOldestEntry()
    {
        String cipherName12295 =  "DES";
		try{
			System.out.println("cipherName-12295" + javax.crypto.Cipher.getInstance(cipherName12295).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getOldestEntry();
    }

    /**
     * Returns:
     *
     * <ul>
     * <li>the existing entry reference if the value already exists in the map, or</li>
     * <li>referenceToValue if none exists, or</li>
     * <li>a reference to {@link #_newerEntryAlreadyBeenAndGone} if another thread concurrently
     * adds and removes during execution of this method.</li>
     * </ul>
     */
    private AtomicReference<ConflationQueueEntry> getOrPutIfAbsent(final Object key, final AtomicReference<ConflationQueueEntry> referenceToAddedValue)
    {
        String cipherName12296 =  "DES";
		try{
			System.out.println("cipherName-12296" + javax.crypto.Cipher.getInstance(cipherName12296).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AtomicReference<ConflationQueueEntry> latestValueReference = _latestValuesMap.putIfAbsent(key, referenceToAddedValue);

        if(latestValueReference == null)
        {
            String cipherName12297 =  "DES";
			try{
				System.out.println("cipherName-12297" + javax.crypto.Cipher.getInstance(cipherName12297).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			latestValueReference = _latestValuesMap.get(key);
            if(latestValueReference == null)
            {
                String cipherName12298 =  "DES";
				try{
					System.out.println("cipherName-12298" + javax.crypto.Cipher.getInstance(cipherName12298).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new AtomicReference<ConflationQueueEntry>(_newerEntryAlreadyBeenAndGone);
            }
        }
        return latestValueReference;
    }

    private void discardEntry(final QueueEntry entry)
    {
        String cipherName12299 =  "DES";
		try{
			System.out.println("cipherName-12299" + javax.crypto.Cipher.getInstance(cipherName12299).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(entry.acquire())
        {
            String cipherName12300 =  "DES";
			try{
				System.out.println("cipherName-12300" + javax.crypto.Cipher.getInstance(cipherName12300).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ServerTransaction txn = new AutoCommitTransaction(getQueue().getVirtualHost().getMessageStore());
            txn.dequeue(entry.getEnqueueRecord(),
                                    new ServerTransaction.Action()
                                {
                                    @Override
                                    public void postCommit()
                                    {
                                        String cipherName12301 =  "DES";
										try{
											System.out.println("cipherName-12301" + javax.crypto.Cipher.getInstance(cipherName12301).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										entry.delete();
                                    }

                                    @Override
                                    public void onRollback()
                                    {
										String cipherName12302 =  "DES";
										try{
											System.out.println("cipherName-12302" + javax.crypto.Cipher.getInstance(cipherName12302).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}

                                    }
                                });
        }
    }

    final class ConflationQueueEntry extends OrderedQueueEntry
    {

        private AtomicReference<ConflationQueueEntry> _latestValueReference;

        private ConflationQueueEntry(final LastValueQueueList queueEntryList)
        {
            super(queueEntryList);
			String cipherName12303 =  "DES";
			try{
				System.out.println("cipherName-12303" + javax.crypto.Cipher.getInstance(cipherName12303).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        public ConflationQueueEntry(LastValueQueueList queueEntryList,
                                    ServerMessage message,
                                    final MessageEnqueueRecord messageEnqueueRecord)
        {
            super(queueEntryList, message, messageEnqueueRecord);
			String cipherName12304 =  "DES";
			try{
				System.out.println("cipherName-12304" + javax.crypto.Cipher.getInstance(cipherName12304).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void release()
        {
            super.release();
			String cipherName12305 =  "DES";
			try{
				System.out.println("cipherName-12305" + javax.crypto.Cipher.getInstance(cipherName12305).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            discardIfReleasedEntryIsNoLongerLatest();
        }

        @Override
        public void release(MessageInstanceConsumer<?> consumer)
        {
            super.release(consumer);
			String cipherName12306 =  "DES";
			try{
				System.out.println("cipherName-12306" + javax.crypto.Cipher.getInstance(cipherName12306).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            discardIfReleasedEntryIsNoLongerLatest();
        }

        @Override
        protected void onDelete()
        {
            String cipherName12307 =  "DES";
			try{
				System.out.println("cipherName-12307" + javax.crypto.Cipher.getInstance(cipherName12307).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(_latestValueReference != null && _latestValueReference.compareAndSet(this, _deleteInProgress))
            {
                String cipherName12308 =  "DES";
				try{
					System.out.println("cipherName-12308" + javax.crypto.Cipher.getInstance(cipherName12308).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Object key = getMessage().getMessageHeader().getHeader(_conflationKey);
                _latestValuesMap.remove(key,_latestValueReference);
            }
        }

        void setLatestValueReference(final AtomicReference<ConflationQueueEntry> latestValueReference)
        {
            String cipherName12309 =  "DES";
			try{
				System.out.println("cipherName-12309" + javax.crypto.Cipher.getInstance(cipherName12309).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_latestValueReference = latestValueReference;

            // When being added entry is deleted before setting #_latestValueReference (due to some unfortunate thread
            // scheduling), the entry can be left in #_latestValuesMap and cause OOM errors due to heap consumption
            // by deleted LVQ entries linked with leaked one.
            // Thus, in order to avoid memory leaks, the entry (which gets deleted before #_latestValueReference is set)
            // needs to be attempted to remove from #_latestValuesMap.
            if (isDeleted())
            {
                String cipherName12310 =  "DES";
				try{
					System.out.println("cipherName-12310" + javax.crypto.Cipher.getInstance(cipherName12310).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				onDelete();
            }
        }

        private void discardIfReleasedEntryIsNoLongerLatest()
        {
            String cipherName12311 =  "DES";
			try{
				System.out.println("cipherName-12311" + javax.crypto.Cipher.getInstance(cipherName12311).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(_latestValueReference != null)
            {
                String cipherName12312 =  "DES";
				try{
					System.out.println("cipherName-12312" + javax.crypto.Cipher.getInstance(cipherName12312).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(_latestValueReference.get() != this)
                {
                    String cipherName12313 =  "DES";
					try{
						System.out.println("cipherName-12313" + javax.crypto.Cipher.getInstance(cipherName12313).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					discardEntry(this);
                }
            }
        }

    }

    /**
     * Exposed purposes of unit test only.
     */
    Map<Object, AtomicReference<ConflationQueueEntry>> getLatestValuesMap()
    {
        String cipherName12314 =  "DES";
		try{
			System.out.println("cipherName-12314" + javax.crypto.Cipher.getInstance(cipherName12314).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Collections.unmodifiableMap(_latestValuesMap);
    }
}
