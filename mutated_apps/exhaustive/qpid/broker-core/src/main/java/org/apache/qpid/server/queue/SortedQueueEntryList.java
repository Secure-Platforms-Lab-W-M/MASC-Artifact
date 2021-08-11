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
import org.apache.qpid.server.queue.SortedQueueEntry.Colour;
import org.apache.qpid.server.store.MessageEnqueueRecord;

/**
 * A sorted implementation of QueueEntryList.
 * Uses the red/black tree algorithm specified in "Introduction to Algorithms".
 * ISBN-10: 0262033844
 * ISBN-13: 978-0262033848
 * see http://en.wikipedia.org/wiki/Red-black_tree
 */
public class SortedQueueEntryList extends AbstractQueueEntryList
{
    private final SortedQueueEntry _head;
    private SortedQueueEntry _root;
    private long _entryId = Long.MIN_VALUE;
    private final Object _lock = new Object();
    private final SortedQueueImpl _queue;
    private final String _propertyName;

    public SortedQueueEntryList(final SortedQueueImpl queue, final QueueStatistics queueStatistics)
    {
        super(queue, queueStatistics);
		String cipherName13448 =  "DES";
		try{
			System.out.println("cipherName-13448" + javax.crypto.Cipher.getInstance(cipherName13448).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _queue = queue;
        _head = new SortedQueueEntry(this);
        _propertyName = queue.getSortKey();
    }

    @Override
    public SortedQueueImpl getQueue()
    {
        String cipherName13449 =  "DES";
		try{
			System.out.println("cipherName-13449" + javax.crypto.Cipher.getInstance(cipherName13449).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _queue;
    }

    @Override
    public SortedQueueEntry add(final ServerMessage message, final MessageEnqueueRecord enqueueRecord)
    {
        String cipherName13450 =  "DES";
		try{
			System.out.println("cipherName-13450" + javax.crypto.Cipher.getInstance(cipherName13450).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		synchronized(_lock)
        {
            String cipherName13451 =  "DES";
			try{
				System.out.println("cipherName-13451" + javax.crypto.Cipher.getInstance(cipherName13451).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String key = null;
            final Object val = message.getMessageHeader().getHeader(_propertyName);
            if(val != null)
            {
                String cipherName13452 =  "DES";
				try{
					System.out.println("cipherName-13452" + javax.crypto.Cipher.getInstance(cipherName13452).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				key = val.toString();
            }

            final SortedQueueEntry entry = new SortedQueueEntry(this,message, ++_entryId, enqueueRecord);
            updateStatsOnEnqueue(entry);

            entry.setKey(key);

            insert(entry);

            return entry;
        }
    }

    /**
     * Red Black Tree insert implementation.
     * @param entry the entry to insert.
     */
    private void insert(final SortedQueueEntry entry)
    {
        String cipherName13453 =  "DES";
		try{
			System.out.println("cipherName-13453" + javax.crypto.Cipher.getInstance(cipherName13453).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SortedQueueEntry node;
        if((node = _root) == null)
        {
            String cipherName13454 =  "DES";
			try{
				System.out.println("cipherName-13454" + javax.crypto.Cipher.getInstance(cipherName13454).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_root = entry;
            _head.setNext(entry);
            entry.setPrev(_head);
            return;
        }
        else
        {
            String cipherName13455 =  "DES";
			try{
				System.out.println("cipherName-13455" + javax.crypto.Cipher.getInstance(cipherName13455).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SortedQueueEntry parent = null;
            while(node != null)
            {
                String cipherName13456 =  "DES";
				try{
					System.out.println("cipherName-13456" + javax.crypto.Cipher.getInstance(cipherName13456).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				parent = node;
                if(entry.compareTo(node) < 0)
                {
                    String cipherName13457 =  "DES";
					try{
						System.out.println("cipherName-13457" + javax.crypto.Cipher.getInstance(cipherName13457).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					node = node.getLeft();
                }
                else
                {
                    String cipherName13458 =  "DES";
					try{
						System.out.println("cipherName-13458" + javax.crypto.Cipher.getInstance(cipherName13458).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					node = node.getRight();
                }
            }
            entry.setParent(parent);

            if(entry.compareTo(parent) < 0)
            {
                String cipherName13459 =  "DES";
				try{
					System.out.println("cipherName-13459" + javax.crypto.Cipher.getInstance(cipherName13459).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				parent.setLeft(entry);
                final SortedQueueEntry prev = parent.getPrev();
                entry.setNext(parent);
                prev.setNext(entry);
                entry.setPrev(prev);
                parent.setPrev(entry);
            }
            else
            {
                String cipherName13460 =  "DES";
				try{
					System.out.println("cipherName-13460" + javax.crypto.Cipher.getInstance(cipherName13460).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				parent.setRight(entry);

                final SortedQueueEntry next = parent.getNextValidEntry();
                entry.setNext(next);
                parent.setNext(entry);

                if(next != null)
                {
                    String cipherName13461 =  "DES";
					try{
						System.out.println("cipherName-13461" + javax.crypto.Cipher.getInstance(cipherName13461).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					next.setPrev(entry);
                }
                entry.setPrev(parent);
            }
        }
        entry.setColour(Colour.RED);
        insertFixup(entry);
    }

    private void insertFixup(SortedQueueEntry entry)
    {
        String cipherName13462 =  "DES";
		try{
			System.out.println("cipherName-13462" + javax.crypto.Cipher.getInstance(cipherName13462).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		while(isParentColour(entry, Colour.RED))
        {
            String cipherName13463 =  "DES";
			try{
				System.out.println("cipherName-13463" + javax.crypto.Cipher.getInstance(cipherName13463).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final SortedQueueEntry grandparent = nodeGrandparent(entry);

            if(nodeParent(entry) == leftChild(grandparent))
            {
                String cipherName13464 =  "DES";
				try{
					System.out.println("cipherName-13464" + javax.crypto.Cipher.getInstance(cipherName13464).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				final SortedQueueEntry y = rightChild(grandparent);
                if(isNodeColour(y, Colour.RED))
                {
                    String cipherName13465 =  "DES";
					try{
						System.out.println("cipherName-13465" + javax.crypto.Cipher.getInstance(cipherName13465).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setColour(nodeParent(entry), Colour.BLACK);
                    setColour(y, Colour.BLACK);
                    setColour(grandparent, Colour.RED);
                    entry = grandparent;
                }
                else
                {
                    String cipherName13466 =  "DES";
					try{
						System.out.println("cipherName-13466" + javax.crypto.Cipher.getInstance(cipherName13466).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(entry == rightChild(nodeParent(entry)))
                    {
                        String cipherName13467 =  "DES";
						try{
							System.out.println("cipherName-13467" + javax.crypto.Cipher.getInstance(cipherName13467).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						entry = nodeParent(entry);
                        leftRotate(entry);
                    }
                    setColour(nodeParent(entry), Colour.BLACK);
                    setColour(nodeGrandparent(entry), Colour.RED);
                    rightRotate(nodeGrandparent(entry));
                }
            }
            else
            {
                String cipherName13468 =  "DES";
				try{
					System.out.println("cipherName-13468" + javax.crypto.Cipher.getInstance(cipherName13468).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				final SortedQueueEntry y = leftChild(grandparent);
                if(isNodeColour(y, Colour.RED))
                {
                    String cipherName13469 =  "DES";
					try{
						System.out.println("cipherName-13469" + javax.crypto.Cipher.getInstance(cipherName13469).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setColour(nodeParent(entry), Colour.BLACK);
                    setColour(y, Colour.BLACK);
                    setColour(grandparent, Colour.RED);
                    entry = grandparent;
                }
                else
                {
                    String cipherName13470 =  "DES";
					try{
						System.out.println("cipherName-13470" + javax.crypto.Cipher.getInstance(cipherName13470).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(entry == leftChild(nodeParent(entry)))
                    {
                        String cipherName13471 =  "DES";
						try{
							System.out.println("cipherName-13471" + javax.crypto.Cipher.getInstance(cipherName13471).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						entry = nodeParent(entry);
                        rightRotate(entry);
                    }
                    setColour(nodeParent(entry), Colour.BLACK);
                    setColour(nodeGrandparent(entry), Colour.RED);
                    leftRotate(nodeGrandparent(entry));
                }
            }
        }
        _root.setColour(Colour.BLACK);
    }

    private void leftRotate(final SortedQueueEntry entry)
    {
        String cipherName13472 =  "DES";
		try{
			System.out.println("cipherName-13472" + javax.crypto.Cipher.getInstance(cipherName13472).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(entry != null)
        {
            String cipherName13473 =  "DES";
			try{
				System.out.println("cipherName-13473" + javax.crypto.Cipher.getInstance(cipherName13473).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final SortedQueueEntry rightChild = rightChild(entry);
            entry.setRight(rightChild.getLeft());
            if(entry.getRight() != null)
            {
                String cipherName13474 =  "DES";
				try{
					System.out.println("cipherName-13474" + javax.crypto.Cipher.getInstance(cipherName13474).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				entry.getRight().setParent(entry);
            }
            rightChild.setParent(entry.getParent());
            if(entry.getParent() == null)
            {
                String cipherName13475 =  "DES";
				try{
					System.out.println("cipherName-13475" + javax.crypto.Cipher.getInstance(cipherName13475).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_root = rightChild;
            }
            else if(entry == entry.getParent().getLeft())
            {
                String cipherName13476 =  "DES";
				try{
					System.out.println("cipherName-13476" + javax.crypto.Cipher.getInstance(cipherName13476).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				entry.getParent().setLeft(rightChild);
            }
            else
            {
                String cipherName13477 =  "DES";
				try{
					System.out.println("cipherName-13477" + javax.crypto.Cipher.getInstance(cipherName13477).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				entry.getParent().setRight(rightChild);
            }
            rightChild.setLeft(entry);
            entry.setParent(rightChild);
        }
    }

    private void rightRotate(final SortedQueueEntry entry)
    {
        String cipherName13478 =  "DES";
		try{
			System.out.println("cipherName-13478" + javax.crypto.Cipher.getInstance(cipherName13478).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(entry != null)
        {
            String cipherName13479 =  "DES";
			try{
				System.out.println("cipherName-13479" + javax.crypto.Cipher.getInstance(cipherName13479).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final SortedQueueEntry leftChild = leftChild(entry);
            entry.setLeft(leftChild.getRight());
            if(entry.getLeft() != null)
            {
                String cipherName13480 =  "DES";
				try{
					System.out.println("cipherName-13480" + javax.crypto.Cipher.getInstance(cipherName13480).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				leftChild.getRight().setParent(entry);
            }
            leftChild.setParent(entry.getParent());
            if(leftChild.getParent() == null)
            {
                String cipherName13481 =  "DES";
				try{
					System.out.println("cipherName-13481" + javax.crypto.Cipher.getInstance(cipherName13481).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_root = leftChild;
            }
            else if(entry == entry.getParent().getRight())
            {
                String cipherName13482 =  "DES";
				try{
					System.out.println("cipherName-13482" + javax.crypto.Cipher.getInstance(cipherName13482).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				entry.getParent().setRight(leftChild);
            }
            else
            {
                String cipherName13483 =  "DES";
				try{
					System.out.println("cipherName-13483" + javax.crypto.Cipher.getInstance(cipherName13483).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				entry.getParent().setLeft(leftChild);
            }
            leftChild.setRight(entry);
            entry.setParent(leftChild);
        }
    }

    private void setColour(final SortedQueueEntry node, final Colour colour)
    {
        String cipherName13484 =  "DES";
		try{
			System.out.println("cipherName-13484" + javax.crypto.Cipher.getInstance(cipherName13484).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(node != null)
        {
            String cipherName13485 =  "DES";
			try{
				System.out.println("cipherName-13485" + javax.crypto.Cipher.getInstance(cipherName13485).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			node.setColour(colour);
        }
    }

    private SortedQueueEntry leftChild(final SortedQueueEntry node)
    {
        String cipherName13486 =  "DES";
		try{
			System.out.println("cipherName-13486" + javax.crypto.Cipher.getInstance(cipherName13486).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node == null ? null : node.getLeft();
    }

    private SortedQueueEntry rightChild(final SortedQueueEntry node)
    {
        String cipherName13487 =  "DES";
		try{
			System.out.println("cipherName-13487" + javax.crypto.Cipher.getInstance(cipherName13487).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node == null ? null : node.getRight();
    }

    private SortedQueueEntry nodeParent(final SortedQueueEntry node)
    {
        String cipherName13488 =  "DES";
		try{
			System.out.println("cipherName-13488" + javax.crypto.Cipher.getInstance(cipherName13488).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node == null ? null : node.getParent();
    }

    private SortedQueueEntry nodeGrandparent(final SortedQueueEntry node)
    {
        String cipherName13489 =  "DES";
		try{
			System.out.println("cipherName-13489" + javax.crypto.Cipher.getInstance(cipherName13489).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return nodeParent(nodeParent(node));
    }

    private boolean isParentColour(final SortedQueueEntry node, final SortedQueueEntry.Colour colour)
    {

        String cipherName13490 =  "DES";
		try{
			System.out.println("cipherName-13490" + javax.crypto.Cipher.getInstance(cipherName13490).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node != null && isNodeColour(node.getParent(), colour);
    }

    protected boolean isNodeColour(final SortedQueueEntry node, final SortedQueueEntry.Colour colour)
    {
        String cipherName13491 =  "DES";
		try{
			System.out.println("cipherName-13491" + javax.crypto.Cipher.getInstance(cipherName13491).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (node == null ? Colour.BLACK : node.getColour()) == colour;
    }

    @Override
    public SortedQueueEntry next(final QueueEntry entry)
    {
        String cipherName13492 =  "DES";
		try{
			System.out.println("cipherName-13492" + javax.crypto.Cipher.getInstance(cipherName13492).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SortedQueueEntry node = (SortedQueueEntry)entry;
        synchronized(_lock)
        {
            String cipherName13493 =  "DES";
			try{
				System.out.println("cipherName-13493" + javax.crypto.Cipher.getInstance(cipherName13493).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(node.isDeleted() && _head != node)
            {
                String cipherName13494 =  "DES";
				try{
					System.out.println("cipherName-13494" + javax.crypto.Cipher.getInstance(cipherName13494).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SortedQueueEntry current = _head;
                SortedQueueEntry next;
                while(current != null)
                {
                    String cipherName13495 =  "DES";
					try{
						System.out.println("cipherName-13495" + javax.crypto.Cipher.getInstance(cipherName13495).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					next = current.getNextValidEntry();
                    if(current.compareTo(node)>0 && !current.isDeleted())
                    {
                        String cipherName13496 =  "DES";
						try{
							System.out.println("cipherName-13496" + javax.crypto.Cipher.getInstance(cipherName13496).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						break;
                    }
                    else
                    {
                        String cipherName13497 =  "DES";
						try{
							System.out.println("cipherName-13497" + javax.crypto.Cipher.getInstance(cipherName13497).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						current = next;
                    }
                }
                return current;
            }
            else
            {
                String cipherName13498 =  "DES";
				try{
					System.out.println("cipherName-13498" + javax.crypto.Cipher.getInstance(cipherName13498).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return node.getNextValidEntry();
            }
        }
    }

    @Override
    public QueueEntryIterator iterator()
    {
        String cipherName13499 =  "DES";
		try{
			System.out.println("cipherName-13499" + javax.crypto.Cipher.getInstance(cipherName13499).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new QueueEntryIteratorImpl(_head);
    }

    @Override
    public SortedQueueEntry getHead()
    {
        String cipherName13500 =  "DES";
		try{
			System.out.println("cipherName-13500" + javax.crypto.Cipher.getInstance(cipherName13500).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _head;
    }


    @Override
    public SortedQueueEntry getTail()
    {
        String cipherName13501 =  "DES";
		try{
			System.out.println("cipherName-13501" + javax.crypto.Cipher.getInstance(cipherName13501).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SortedQueueEntry current = _head;
        SortedQueueEntry next;
        while((next = next(current))!=null)
        {
            String cipherName13502 =  "DES";
			try{
				System.out.println("cipherName-13502" + javax.crypto.Cipher.getInstance(cipherName13502).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			current = next;
        }
        return current;
    }


    @Override
    public QueueEntry getOldestEntry()
    {
        String cipherName13503 =  "DES";
		try{
			System.out.println("cipherName-13503" + javax.crypto.Cipher.getInstance(cipherName13503).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		QueueEntry oldestEntry = null;
        QueueEntryIterator iter = iterator();
        while (iter.advance())
        {
            String cipherName13504 =  "DES";
			try{
				System.out.println("cipherName-13504" + javax.crypto.Cipher.getInstance(cipherName13504).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			QueueEntry node = iter.getNode();
            if (node != null && !node.isDeleted())
            {
                String cipherName13505 =  "DES";
				try{
					System.out.println("cipherName-13505" + javax.crypto.Cipher.getInstance(cipherName13505).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ServerMessage msg = node.getMessage();
                if(msg != null && (oldestEntry == null || oldestEntry.getMessage().getMessageNumber() > msg.getMessageNumber()))
                {
                    String cipherName13506 =  "DES";
					try{
						System.out.println("cipherName-13506" + javax.crypto.Cipher.getInstance(cipherName13506).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					oldestEntry = node;
                }
            }
        }
        return oldestEntry;
    }

    protected SortedQueueEntry getRoot()
    {
        String cipherName13507 =  "DES";
		try{
			System.out.println("cipherName-13507" + javax.crypto.Cipher.getInstance(cipherName13507).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _root;
    }

    @Override
    public void entryDeleted(final QueueEntry e)
    {
        String cipherName13508 =  "DES";
		try{
			System.out.println("cipherName-13508" + javax.crypto.Cipher.getInstance(cipherName13508).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SortedQueueEntry entry = (SortedQueueEntry)e;
        synchronized(_lock)
        {
            String cipherName13509 =  "DES";
			try{
				System.out.println("cipherName-13509" + javax.crypto.Cipher.getInstance(cipherName13509).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// If the node to be removed has two children, we swap the position
            // of the node and its successor in the tree
            if(leftChild(entry) != null && rightChild(entry) != null)
            {
                String cipherName13510 =  "DES";
				try{
					System.out.println("cipherName-13510" + javax.crypto.Cipher.getInstance(cipherName13510).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				swapWithSuccessor(entry);
            }

            // Then deal with the easy doubly linked list deletion (need to do
            // this after the swap as the swap uses next
            final SortedQueueEntry prev = entry.getPrev();
            if(prev != null)
            {
                String cipherName13511 =  "DES";
				try{
					System.out.println("cipherName-13511" + javax.crypto.Cipher.getInstance(cipherName13511).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				prev.setNext(entry.getNextValidEntry());
            }

            final SortedQueueEntry next = entry.getNextValidEntry();
            if(next != null)
            {
                String cipherName13512 =  "DES";
				try{
					System.out.println("cipherName-13512" + javax.crypto.Cipher.getInstance(cipherName13512).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next.setPrev(prev);
            }

            // now deal with splicing
            final SortedQueueEntry chosenChild;

            if(leftChild(entry) != null)
            {
                String cipherName13513 =  "DES";
				try{
					System.out.println("cipherName-13513" + javax.crypto.Cipher.getInstance(cipherName13513).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				chosenChild = leftChild(entry);
            }
            else
            {
                String cipherName13514 =  "DES";
				try{
					System.out.println("cipherName-13514" + javax.crypto.Cipher.getInstance(cipherName13514).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				chosenChild = rightChild(entry);
            }

            if(chosenChild != null)
            {
                String cipherName13515 =  "DES";
				try{
					System.out.println("cipherName-13515" + javax.crypto.Cipher.getInstance(cipherName13515).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// we have one child (x), we can move it up to replace x
                chosenChild.setParent(entry.getParent());
                if(chosenChild.getParent() == null)
                {
                    String cipherName13516 =  "DES";
					try{
						System.out.println("cipherName-13516" + javax.crypto.Cipher.getInstance(cipherName13516).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_root = chosenChild;
                }
                else if(entry == entry.getParent().getLeft())
                {
                    String cipherName13517 =  "DES";
					try{
						System.out.println("cipherName-13517" + javax.crypto.Cipher.getInstance(cipherName13517).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					entry.getParent().setLeft(chosenChild);
                }
                else
                {
                    String cipherName13518 =  "DES";
					try{
						System.out.println("cipherName-13518" + javax.crypto.Cipher.getInstance(cipherName13518).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					entry.getParent().setRight(chosenChild);
                }

                entry.setLeft(null);
                entry.setRight(null);
                entry.setParent(null);

                if(entry.getColour() == Colour.BLACK)
                {
                    String cipherName13519 =  "DES";
					try{
						System.out.println("cipherName-13519" + javax.crypto.Cipher.getInstance(cipherName13519).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					deleteFixup(chosenChild);
                }

            }
            else
            {
                String cipherName13520 =  "DES";
				try{
					System.out.println("cipherName-13520" + javax.crypto.Cipher.getInstance(cipherName13520).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// no children
                if(entry.getParent() == null)
                {
                    String cipherName13521 =  "DES";
					try{
						System.out.println("cipherName-13521" + javax.crypto.Cipher.getInstance(cipherName13521).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// no parent either - the tree is empty
                    _root = null;
                }
                else
                {
                    String cipherName13522 =  "DES";
					try{
						System.out.println("cipherName-13522" + javax.crypto.Cipher.getInstance(cipherName13522).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(entry.getColour() == Colour.BLACK)
                    {
                        String cipherName13523 =  "DES";
						try{
							System.out.println("cipherName-13523" + javax.crypto.Cipher.getInstance(cipherName13523).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						deleteFixup(entry);
                    }

                    if(entry.getParent() != null)
                    {
                        String cipherName13524 =  "DES";
						try{
							System.out.println("cipherName-13524" + javax.crypto.Cipher.getInstance(cipherName13524).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(entry.getParent().getLeft() == entry)
                        {
                            String cipherName13525 =  "DES";
							try{
								System.out.println("cipherName-13525" + javax.crypto.Cipher.getInstance(cipherName13525).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							entry.getParent().setLeft(null);
                        }
                        else if(entry.getParent().getRight() == entry)
                        {
                            String cipherName13526 =  "DES";
							try{
								System.out.println("cipherName-13526" + javax.crypto.Cipher.getInstance(cipherName13526).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							entry.getParent().setRight(null);
                        }
                        entry.setParent(null);
                    }
                }
            }

        }
    }

    @Override
    public int getPriorities()
    {
        String cipherName13527 =  "DES";
		try{
			System.out.println("cipherName-13527" + javax.crypto.Cipher.getInstance(cipherName13527).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 0;
    }

    @Override
    public QueueEntry getLeastSignificantOldestEntry()
    {
        String cipherName13528 =  "DES";
		try{
			System.out.println("cipherName-13528" + javax.crypto.Cipher.getInstance(cipherName13528).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getOldestEntry();
    }

    /**
     * Swaps the position of the node in the tree with it's successor
     * (that is the node with the next highest key)
     * @param entry the entry to be swapped with its successor
     */
    private void swapWithSuccessor(final SortedQueueEntry entry)
    {
        String cipherName13529 =  "DES";
		try{
			System.out.println("cipherName-13529" + javax.crypto.Cipher.getInstance(cipherName13529).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final SortedQueueEntry next = entry.getNextValidEntry();
        final SortedQueueEntry nextParent = next.getParent();
        final SortedQueueEntry nextLeft = next.getLeft();
        final SortedQueueEntry nextRight = next.getRight();
        final Colour nextColour = next.getColour();

        // Special case - the successor is the right child of the node
        if(next == entry.getRight())
        {
            String cipherName13530 =  "DES";
			try{
				System.out.println("cipherName-13530" + javax.crypto.Cipher.getInstance(cipherName13530).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			next.setParent(entry.getParent());
            if(next.getParent() == null)
            {
                String cipherName13531 =  "DES";
				try{
					System.out.println("cipherName-13531" + javax.crypto.Cipher.getInstance(cipherName13531).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_root = next;
            }
            else if(next.getParent().getLeft() == entry)
            {
                String cipherName13532 =  "DES";
				try{
					System.out.println("cipherName-13532" + javax.crypto.Cipher.getInstance(cipherName13532).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next.getParent().setLeft(next);
            }
            else
            {
                String cipherName13533 =  "DES";
				try{
					System.out.println("cipherName-13533" + javax.crypto.Cipher.getInstance(cipherName13533).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next.getParent().setRight(next);
            }

            next.setRight(entry);
            entry.setParent(next);
            next.setLeft(entry.getLeft());

            if(next.getLeft() != null)
            {
                String cipherName13534 =  "DES";
				try{
					System.out.println("cipherName-13534" + javax.crypto.Cipher.getInstance(cipherName13534).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next.getLeft().setParent(next);
            }

            next.setColour(entry.getColour());
            entry.setColour(nextColour);
            entry.setLeft(nextLeft);

            if(nextLeft != null)
            {
                String cipherName13535 =  "DES";
				try{
					System.out.println("cipherName-13535" + javax.crypto.Cipher.getInstance(cipherName13535).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				nextLeft.setParent(entry);
            }
            entry.setRight(nextRight);
            if(nextRight != null)
            {
                String cipherName13536 =  "DES";
				try{
					System.out.println("cipherName-13536" + javax.crypto.Cipher.getInstance(cipherName13536).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				nextRight.setParent(entry);
            }
        }
        else
        {
            String cipherName13537 =  "DES";
			try{
				System.out.println("cipherName-13537" + javax.crypto.Cipher.getInstance(cipherName13537).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			next.setParent(entry.getParent());
            if(next.getParent() == null)
            {
                String cipherName13538 =  "DES";
				try{
					System.out.println("cipherName-13538" + javax.crypto.Cipher.getInstance(cipherName13538).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_root = next;
            }
            else if(next.getParent().getLeft() == entry)
            {
                String cipherName13539 =  "DES";
				try{
					System.out.println("cipherName-13539" + javax.crypto.Cipher.getInstance(cipherName13539).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next.getParent().setLeft(next);
            }
            else
            {
                String cipherName13540 =  "DES";
				try{
					System.out.println("cipherName-13540" + javax.crypto.Cipher.getInstance(cipherName13540).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next.getParent().setRight(next);
            }

            next.setLeft(entry.getLeft());
            if(next.getLeft() != null)
            {
                String cipherName13541 =  "DES";
				try{
					System.out.println("cipherName-13541" + javax.crypto.Cipher.getInstance(cipherName13541).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next.getLeft().setParent(next);
            }
            next.setRight(entry.getRight());
            if(next.getRight() != null)
            {
                String cipherName13542 =  "DES";
				try{
					System.out.println("cipherName-13542" + javax.crypto.Cipher.getInstance(cipherName13542).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next.getRight().setParent(next);
            }
            next.setColour(entry.getColour());

            entry.setParent(nextParent);
            if(nextParent.getLeft() == next)
            {
                String cipherName13543 =  "DES";
				try{
					System.out.println("cipherName-13543" + javax.crypto.Cipher.getInstance(cipherName13543).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				nextParent.setLeft(entry);
            }
            else
            {
                String cipherName13544 =  "DES";
				try{
					System.out.println("cipherName-13544" + javax.crypto.Cipher.getInstance(cipherName13544).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				nextParent.setRight(entry);
            }

            entry.setLeft(nextLeft);
            if(nextLeft != null)
            {
                String cipherName13545 =  "DES";
				try{
					System.out.println("cipherName-13545" + javax.crypto.Cipher.getInstance(cipherName13545).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				nextLeft.setParent(entry);
            }
            entry.setRight(nextRight);
            if(nextRight != null)
            {
                String cipherName13546 =  "DES";
				try{
					System.out.println("cipherName-13546" + javax.crypto.Cipher.getInstance(cipherName13546).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				nextRight.setParent(entry);
            }
            entry.setColour(nextColour);
        }
    }

    private void deleteFixup(SortedQueueEntry entry)
    {
        String cipherName13547 =  "DES";
		try{
			System.out.println("cipherName-13547" + javax.crypto.Cipher.getInstance(cipherName13547).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int i = 0;
        while(entry != null && entry != _root
                && isNodeColour(entry, Colour.BLACK))
        {
            String cipherName13548 =  "DES";
			try{
				System.out.println("cipherName-13548" + javax.crypto.Cipher.getInstance(cipherName13548).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			i++;

            if(i > 1000)
            {
                String cipherName13549 =  "DES";
				try{
					System.out.println("cipherName-13549" + javax.crypto.Cipher.getInstance(cipherName13549).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }

            if(entry == leftChild(nodeParent(entry)))
            {
                String cipherName13550 =  "DES";
				try{
					System.out.println("cipherName-13550" + javax.crypto.Cipher.getInstance(cipherName13550).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SortedQueueEntry rightSibling = rightChild(nodeParent(entry));
                if(isNodeColour(rightSibling, Colour.RED))
                {
                    String cipherName13551 =  "DES";
					try{
						System.out.println("cipherName-13551" + javax.crypto.Cipher.getInstance(cipherName13551).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setColour(rightSibling, Colour.BLACK);
                    nodeParent(entry).setColour(Colour.RED);
                    leftRotate(nodeParent(entry));
                    rightSibling = rightChild(nodeParent(entry));
                }

                if(isNodeColour(leftChild(rightSibling), Colour.BLACK)
                        && isNodeColour(rightChild(rightSibling), Colour.BLACK))
                {
                    String cipherName13552 =  "DES";
					try{
						System.out.println("cipherName-13552" + javax.crypto.Cipher.getInstance(cipherName13552).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setColour(rightSibling, Colour.RED);
                    entry = nodeParent(entry);
                }
                else
                {
                    String cipherName13553 =  "DES";
					try{
						System.out.println("cipherName-13553" + javax.crypto.Cipher.getInstance(cipherName13553).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(isNodeColour(rightChild(rightSibling), Colour.BLACK))
                    {
                        String cipherName13554 =  "DES";
						try{
							System.out.println("cipherName-13554" + javax.crypto.Cipher.getInstance(cipherName13554).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						setColour(leftChild(rightSibling), Colour.BLACK);
                        rightSibling.setColour(Colour.RED);
                        rightRotate(rightSibling);
                        rightSibling = rightChild(nodeParent(entry));
                    }
                    setColour(rightSibling, getColour(nodeParent(entry)));
                    setColour(nodeParent(entry), Colour.BLACK);
                    setColour(rightChild(rightSibling), Colour.BLACK);
                    leftRotate(nodeParent(entry));
                    entry = _root;
                }
            }
            else
            {
                String cipherName13555 =  "DES";
				try{
					System.out.println("cipherName-13555" + javax.crypto.Cipher.getInstance(cipherName13555).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SortedQueueEntry leftSibling = leftChild(nodeParent(entry));
                if(isNodeColour(leftSibling, Colour.RED))
                {
                    String cipherName13556 =  "DES";
					try{
						System.out.println("cipherName-13556" + javax.crypto.Cipher.getInstance(cipherName13556).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setColour(leftSibling, Colour.BLACK);
                    nodeParent(entry).setColour(Colour.RED);
                    rightRotate(nodeParent(entry));
                    leftSibling = leftChild(nodeParent(entry));
                }

                if(isNodeColour(leftChild(leftSibling), Colour.BLACK)
                        && isNodeColour(rightChild(leftSibling), Colour.BLACK))
                {
                    String cipherName13557 =  "DES";
					try{
						System.out.println("cipherName-13557" + javax.crypto.Cipher.getInstance(cipherName13557).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setColour(leftSibling, Colour.RED);
                    entry = nodeParent(entry);
                }
                else
                {
                    String cipherName13558 =  "DES";
					try{
						System.out.println("cipherName-13558" + javax.crypto.Cipher.getInstance(cipherName13558).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(isNodeColour(leftChild(leftSibling), Colour.BLACK))
                    {
                        String cipherName13559 =  "DES";
						try{
							System.out.println("cipherName-13559" + javax.crypto.Cipher.getInstance(cipherName13559).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						setColour(rightChild(leftSibling), Colour.BLACK);
                        leftSibling.setColour(Colour.RED);
                        leftRotate(leftSibling);
                        leftSibling = leftChild(nodeParent(entry));
                    }
                    setColour(leftSibling, getColour(nodeParent(entry)));
                    setColour(nodeParent(entry), Colour.BLACK);
                    setColour(leftChild(leftSibling), Colour.BLACK);
                    rightRotate(nodeParent(entry));
                    entry = _root;
                }
            }
        }
        setColour(entry, Colour.BLACK);
    }

    private Colour getColour(final SortedQueueEntry x)
    {
        String cipherName13560 =  "DES";
		try{
			System.out.println("cipherName-13560" + javax.crypto.Cipher.getInstance(cipherName13560).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return x == null ? null : x.getColour();
    }

    public class QueueEntryIteratorImpl implements QueueEntryIterator
    {
        private SortedQueueEntry _lastNode;

        public QueueEntryIteratorImpl(final SortedQueueEntry startNode)
        {
            String cipherName13561 =  "DES";
			try{
				System.out.println("cipherName-13561" + javax.crypto.Cipher.getInstance(cipherName13561).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_lastNode = startNode;
        }

        @Override
        public boolean atTail()
        {
            String cipherName13562 =  "DES";
			try{
				System.out.println("cipherName-13562" + javax.crypto.Cipher.getInstance(cipherName13562).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return next(_lastNode) == null;
        }

        @Override
        public SortedQueueEntry getNode()
        {
            String cipherName13563 =  "DES";
			try{
				System.out.println("cipherName-13563" + javax.crypto.Cipher.getInstance(cipherName13563).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _lastNode;
        }

        @Override
        public boolean advance()
        {
            String cipherName13564 =  "DES";
			try{
				System.out.println("cipherName-13564" + javax.crypto.Cipher.getInstance(cipherName13564).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!atTail())
            {
                String cipherName13565 =  "DES";
				try{
					System.out.println("cipherName-13565" + javax.crypto.Cipher.getInstance(cipherName13565).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SortedQueueEntry nextNode = next(_lastNode);
                while(nextNode.isDeleted() && next(nextNode) != null)
                {
                    String cipherName13566 =  "DES";
					try{
						System.out.println("cipherName-13566" + javax.crypto.Cipher.getInstance(cipherName13566).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					nextNode = next(nextNode);
                }
                _lastNode = nextNode;
                return true;

            }
            else
            {
                String cipherName13567 =  "DES";
				try{
					System.out.println("cipherName-13567" + javax.crypto.Cipher.getInstance(cipherName13567).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
        }
    }
}
