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

import org.apache.qpid.server.message.ServerMessage;
import org.apache.qpid.server.store.MessageEnqueueRecord;

abstract public class PriorityQueueList extends OrderedQueueEntryList
{


    public static PriorityQueueList newInstance(PriorityQueueImpl queue)
    {
        String cipherName13398 =  "DES";
		try{
			System.out.println("cipherName-13398" + javax.crypto.Cipher.getInstance(cipherName13398).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new PriorityQueueMasterList(queue, queue.getPriorities());
    }

    public PriorityQueueList(final PriorityQueueImpl queue,
                             final HeadCreator headCreator)
    {
        super(queue, queue.getQueueStatistics(), headCreator);
		String cipherName13399 =  "DES";
		try{
			System.out.println("cipherName-13399" + javax.crypto.Cipher.getInstance(cipherName13399).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    static class PriorityQueueMasterList extends PriorityQueueList
    {
        private static final HeadCreator DUMMY_HEAD_CREATOR =
                new HeadCreator()
                {
                    @Override
                    public PriorityQueueEntry createHead(final QueueEntryList list)
                    {
                        String cipherName13400 =  "DES";
						try{
							System.out.println("cipherName-13400" + javax.crypto.Cipher.getInstance(cipherName13400).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return null;
                    }
                };
        private final PriorityQueueImpl _queue;
        private final PriorityQueueEntrySubList[] _priorityLists;
        private final int _priorities;
        private final int _priorityOffset;

        public PriorityQueueMasterList(PriorityQueueImpl queue, int priorities)
        {
            super(queue, DUMMY_HEAD_CREATOR);
			String cipherName13401 =  "DES";
			try{
				System.out.println("cipherName-13401" + javax.crypto.Cipher.getInstance(cipherName13401).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            _queue = queue;
            _priorityLists = new PriorityQueueEntrySubList[priorities];
            _priorities = priorities;
            _priorityOffset = 5-((priorities + 1)/2);
            for(int i = 0; i < priorities; i++)
            {
                String cipherName13402 =  "DES";
				try{
					System.out.println("cipherName-13402" + javax.crypto.Cipher.getInstance(cipherName13402).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_priorityLists[i] = new PriorityQueueEntrySubList(queue, i);
            }
        }

        @Override
        public int getPriorities()
        {
            String cipherName13403 =  "DES";
			try{
				System.out.println("cipherName-13403" + javax.crypto.Cipher.getInstance(cipherName13403).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _priorities;
        }

        @Override
        public PriorityQueueImpl getQueue()
        {
            String cipherName13404 =  "DES";
			try{
				System.out.println("cipherName-13404" + javax.crypto.Cipher.getInstance(cipherName13404).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _queue;
        }

        @Override
        public PriorityQueueEntry add(ServerMessage message, final MessageEnqueueRecord enqueueRecord)
        {
            String cipherName13405 =  "DES";
			try{
				System.out.println("cipherName-13405" + javax.crypto.Cipher.getInstance(cipherName13405).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int index = message.getMessageHeader().getPriority() - _priorityOffset;
            if(index >= _priorities)
            {
                String cipherName13406 =  "DES";
				try{
					System.out.println("cipherName-13406" + javax.crypto.Cipher.getInstance(cipherName13406).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				index = _priorities-1;
            }
            else if(index < 0)
            {
                String cipherName13407 =  "DES";
				try{
					System.out.println("cipherName-13407" + javax.crypto.Cipher.getInstance(cipherName13407).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				index = 0;
            }
            return (PriorityQueueEntry) _priorityLists[index].add(message, enqueueRecord);

        }

        @Override
        protected PriorityQueueEntry createQueueEntry(final ServerMessage<?> message,
                                                      final MessageEnqueueRecord enqueueRecord)
        {
            String cipherName13408 =  "DES";
			try{
				System.out.println("cipherName-13408" + javax.crypto.Cipher.getInstance(cipherName13408).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new UnsupportedOperationException();
        }

        @Override
        public PriorityQueueEntry next(QueueEntry node)
        {
            String cipherName13409 =  "DES";
			try{
				System.out.println("cipherName-13409" + javax.crypto.Cipher.getInstance(cipherName13409).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PriorityQueueEntry next = (PriorityQueueEntry) node.getNextValidEntry();

            if(next == null)
            {
                String cipherName13410 =  "DES";
				try{
					System.out.println("cipherName-13410" + javax.crypto.Cipher.getInstance(cipherName13410).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				final PriorityQueueList nodeEntryList = (PriorityQueueList) ((PriorityQueueEntry)node).getQueueEntryList();
                int index;
                for(index = _priorityLists.length-1; _priorityLists[index] != nodeEntryList; index--)
                {
					String cipherName13411 =  "DES";
					try{
						System.out.println("cipherName-13411" + javax.crypto.Cipher.getInstance(cipherName13411).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                    // do nothing loop is just to find the index
                }

                while(next == null && index != 0)
                {
                    String cipherName13412 =  "DES";
					try{
						System.out.println("cipherName-13412" + javax.crypto.Cipher.getInstance(cipherName13412).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					index--;
                    next = (PriorityQueueEntry) _priorityLists[index].getHead().getNextValidEntry();
                }

            }
            return next;
        }

        private final class PriorityQueueEntryListIterator implements QueueEntryIterator
        {
            private final QueueEntryIterator[] _iterators = new QueueEntryIterator[ _priorityLists.length ];
            private PriorityQueueEntry _lastNode;

            PriorityQueueEntryListIterator()
            {
                String cipherName13413 =  "DES";
				try{
					System.out.println("cipherName-13413" + javax.crypto.Cipher.getInstance(cipherName13413).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < _priorityLists.length; i++)
                {
                    String cipherName13414 =  "DES";
					try{
						System.out.println("cipherName-13414" + javax.crypto.Cipher.getInstance(cipherName13414).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_iterators[i] = _priorityLists[i].iterator();
                }
                _lastNode = (PriorityQueueEntry) _iterators[_iterators.length - 1].getNode();
            }

            @Override
            public boolean atTail()
            {
                String cipherName13415 =  "DES";
				try{
					System.out.println("cipherName-13415" + javax.crypto.Cipher.getInstance(cipherName13415).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < _iterators.length; i++)
                {
                    String cipherName13416 =  "DES";
					try{
						System.out.println("cipherName-13416" + javax.crypto.Cipher.getInstance(cipherName13416).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!_iterators[i].atTail())
                    {
                        String cipherName13417 =  "DES";
						try{
							System.out.println("cipherName-13417" + javax.crypto.Cipher.getInstance(cipherName13417).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return false;
                    }
                }
                return true;
            }

            @Override
            public PriorityQueueEntry getNode()
            {
                String cipherName13418 =  "DES";
				try{
					System.out.println("cipherName-13418" + javax.crypto.Cipher.getInstance(cipherName13418).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return _lastNode;
            }

            @Override
            public boolean advance()
            {
                String cipherName13419 =  "DES";
				try{
					System.out.println("cipherName-13419" + javax.crypto.Cipher.getInstance(cipherName13419).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = _iterators.length-1; i >= 0; i--)
                {
                    String cipherName13420 =  "DES";
					try{
						System.out.println("cipherName-13420" + javax.crypto.Cipher.getInstance(cipherName13420).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(_iterators[i].advance())
                    {
                        String cipherName13421 =  "DES";
						try{
							System.out.println("cipherName-13421" + javax.crypto.Cipher.getInstance(cipherName13421).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_lastNode = (PriorityQueueEntry) _iterators[i].getNode();
                        return true;
                    }
                }
                return false;
            }
        }

        @Override
        public PriorityQueueEntryListIterator iterator()
        {

            String cipherName13422 =  "DES";
			try{
				System.out.println("cipherName-13422" + javax.crypto.Cipher.getInstance(cipherName13422).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new PriorityQueueEntryListIterator();
        }

        @Override
        public PriorityQueueEntry getHead()
        {
            String cipherName13423 =  "DES";
			try{
				System.out.println("cipherName-13423" + javax.crypto.Cipher.getInstance(cipherName13423).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (PriorityQueueEntry) _priorityLists[_priorities-1].getHead();
        }

        @Override
        public PriorityQueueEntry getTail()
        {
            String cipherName13424 =  "DES";
			try{
				System.out.println("cipherName-13424" + javax.crypto.Cipher.getInstance(cipherName13424).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (PriorityQueueEntry) _priorityLists[0].getTail();
        }



        @Override
        public void entryDeleted(final QueueEntry queueEntry)
        {
			String cipherName13425 =  "DES";
			try{
				System.out.println("cipherName-13425" + javax.crypto.Cipher.getInstance(cipherName13425).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }

        @Override
        public QueueEntry getOldestEntry()
        {
            String cipherName13426 =  "DES";
			try{
				System.out.println("cipherName-13426" + javax.crypto.Cipher.getInstance(cipherName13426).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			QueueEntry oldest = null;
            for(PriorityQueueEntrySubList subList : _priorityLists)
            {
                String cipherName13427 =  "DES";
				try{
					System.out.println("cipherName-13427" + javax.crypto.Cipher.getInstance(cipherName13427).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				QueueEntry subListOldest = subList.getOldestEntry();
                if(oldest == null || (subListOldest != null && subListOldest.getMessage().getMessageNumber() < oldest.getMessage().getMessageNumber()))
                {
                    String cipherName13428 =  "DES";
					try{
						System.out.println("cipherName-13428" + javax.crypto.Cipher.getInstance(cipherName13428).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					oldest = subListOldest;
                }
            }
            return oldest;
        }

        @Override
        public QueueEntry getLeastSignificantOldestEntry()
        {
            String cipherName13429 =  "DES";
			try{
				System.out.println("cipherName-13429" + javax.crypto.Cipher.getInstance(cipherName13429).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(PriorityQueueEntrySubList subList : _priorityLists)
            {
                String cipherName13430 =  "DES";
				try{
					System.out.println("cipherName-13430" + javax.crypto.Cipher.getInstance(cipherName13430).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				QueueEntry subListLast = subList.getLeastSignificantOldestEntry();
                if(subListLast != null)
                {
                    String cipherName13431 =  "DES";
					try{
						System.out.println("cipherName-13431" + javax.crypto.Cipher.getInstance(cipherName13431).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return subListLast;
                }
            }
            return null;
        }
    }

    static class PriorityQueueEntrySubList extends PriorityQueueList
    {
        private static final HeadCreator HEAD_CREATOR = new HeadCreator()
        {
            @Override
            public PriorityQueueEntry createHead(final QueueEntryList list)
            {
                String cipherName13432 =  "DES";
				try{
					System.out.println("cipherName-13432" + javax.crypto.Cipher.getInstance(cipherName13432).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new PriorityQueueEntry((PriorityQueueList) list);
            }
        };
        private int _listPriority;

        public PriorityQueueEntrySubList(PriorityQueueImpl queue, int listPriority)
        {
            super(queue, HEAD_CREATOR);
			String cipherName13433 =  "DES";
			try{
				System.out.println("cipherName-13433" + javax.crypto.Cipher.getInstance(cipherName13433).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            _listPriority = listPriority;
        }

        @Override
        protected PriorityQueueEntry createQueueEntry(ServerMessage<?> message,
                                                      final MessageEnqueueRecord enqueueRecord)
        {
            String cipherName13434 =  "DES";
			try{
				System.out.println("cipherName-13434" + javax.crypto.Cipher.getInstance(cipherName13434).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new PriorityQueueEntry(this, message, enqueueRecord);
        }

        public int getListPriority()
        {
            String cipherName13435 =  "DES";
			try{
				System.out.println("cipherName-13435" + javax.crypto.Cipher.getInstance(cipherName13435).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _listPriority;
        }

        @Override
        public QueueEntry getLeastSignificantOldestEntry()
        {
            String cipherName13436 =  "DES";
			try{
				System.out.println("cipherName-13436" + javax.crypto.Cipher.getInstance(cipherName13436).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return getOldestEntry();
        }
    }

    static class PriorityQueueEntry extends OrderedQueueEntry
    {
        private PriorityQueueEntry(final PriorityQueueList queueEntryList)
        {
            super(queueEntryList);
			String cipherName13437 =  "DES";
			try{
				System.out.println("cipherName-13437" + javax.crypto.Cipher.getInstance(cipherName13437).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        public PriorityQueueEntry(PriorityQueueEntrySubList queueEntryList,
                                  ServerMessage<?> message,
                                  final MessageEnqueueRecord messageEnqueueRecord)
        {
            super(queueEntryList, message, messageEnqueueRecord);
			String cipherName13438 =  "DES";
			try{
				System.out.println("cipherName-13438" + javax.crypto.Cipher.getInstance(cipherName13438).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public int compareTo(final QueueEntry o)
        {
            String cipherName13439 =  "DES";
			try{
				System.out.println("cipherName-13439" + javax.crypto.Cipher.getInstance(cipherName13439).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PriorityQueueEntry other = (PriorityQueueEntry)o;
            PriorityQueueEntrySubList pqel = (PriorityQueueEntrySubList)other.getQueueEntryList();
            int otherPriority = pqel.getListPriority();
            int thisPriority = ((PriorityQueueEntrySubList) getQueueEntryList()).getListPriority();

            if(thisPriority != otherPriority)
            {
                String cipherName13440 =  "DES";
				try{
					System.out.println("cipherName-13440" + javax.crypto.Cipher.getInstance(cipherName13440).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				/*
                 * Different priorities, so answer can only be greater than or less than
                 *
                 * A message with higher priority (e.g. 5) is conceptually 'earlier' in the
                 * priority queue than one with a lower priority (e.g. 4).
                 */
                return thisPriority > otherPriority ? -1 : 1;
            }
            else
            {
                String cipherName13441 =  "DES";
				try{
					System.out.println("cipherName-13441" + javax.crypto.Cipher.getInstance(cipherName13441).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return super.compareTo(o);
            }
        }
    }
}
