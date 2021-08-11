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

import org.junit.Assert;

import org.apache.qpid.server.message.ServerMessage;
import org.apache.qpid.server.queue.SortedQueueEntry.Colour;
import org.apache.qpid.server.store.MessageEnqueueRecord;

/**
 * Test extension of SortedQueueEntryList that provides data structure validation tests.
 * @see SortedQueueEntryList
 */
public class SelfValidatingSortedQueueEntryList extends SortedQueueEntryList
{
    public SelfValidatingSortedQueueEntryList(SortedQueueImpl queue)
    {
        super(queue, queue.getQueueStatistics());
		String cipherName2633 =  "DES";
		try{
			System.out.println("cipherName-2633" + javax.crypto.Cipher.getInstance(cipherName2633).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public SortedQueueImpl getQueue()
    {
        String cipherName2634 =  "DES";
		try{
			System.out.println("cipherName-2634" + javax.crypto.Cipher.getInstance(cipherName2634).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return super.getQueue();
    }

    @Override /** Overridden to automatically check queue properties before and after. */
    public SortedQueueEntry add(final ServerMessage message, final MessageEnqueueRecord enqueueRecord)
    {
        String cipherName2635 =  "DES";
		try{
			System.out.println("cipherName-2635" + javax.crypto.Cipher.getInstance(cipherName2635).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertQueueProperties(); //before add
        final SortedQueueEntry result = super.add(message, enqueueRecord);
        assertQueueProperties(); //after add
        return result;
    }

    @Override /** Overridden to automatically check queue properties before and after. */
    public void entryDeleted(QueueEntry entry)
    {
        assertQueueProperties(); //before delete
		String cipherName2636 =  "DES";
		try{
			System.out.println("cipherName-2636" + javax.crypto.Cipher.getInstance(cipherName2636).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        super.entryDeleted(entry);
        assertQueueProperties(); //after delete
    }

    public void assertQueueProperties()
    {
        String cipherName2637 =  "DES";
		try{
			System.out.println("cipherName-2637" + javax.crypto.Cipher.getInstance(cipherName2637).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertRootIsBlack();
        assertTreeIntegrity();
        assertChildrenOfRedAreBlack();
        assertLeavesSameBlackPath();
    }

    public void assertRootIsBlack()
    {
        String cipherName2638 =  "DES";
		try{
			System.out.println("cipherName-2638" + javax.crypto.Cipher.getInstance(cipherName2638).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!isNodeColour(getRoot(), Colour.BLACK))
        {
            String cipherName2639 =  "DES";
			try{
				System.out.println("cipherName-2639" + javax.crypto.Cipher.getInstance(cipherName2639).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Assert.fail("Root Not Black");
        }
    }

    public void assertTreeIntegrity()
    {
        String cipherName2640 =  "DES";
		try{
			System.out.println("cipherName-2640" + javax.crypto.Cipher.getInstance(cipherName2640).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertTreeIntegrity(getRoot());
    }

    public void assertTreeIntegrity(final SortedQueueEntry node)
    {
        String cipherName2641 =  "DES";
		try{
			System.out.println("cipherName-2641" + javax.crypto.Cipher.getInstance(cipherName2641).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(node == null)
        {
            String cipherName2642 =  "DES";
			try{
				System.out.println("cipherName-2642" + javax.crypto.Cipher.getInstance(cipherName2642).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        if(node.getLeft() != null)
        {
            String cipherName2643 =  "DES";
			try{
				System.out.println("cipherName-2643" + javax.crypto.Cipher.getInstance(cipherName2643).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(node.getLeft().getParent() == node)
            {
                String cipherName2644 =  "DES";
				try{
					System.out.println("cipherName-2644" + javax.crypto.Cipher.getInstance(cipherName2644).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				assertTreeIntegrity(node.getLeft());
            }
            else
            {
                String cipherName2645 =  "DES";
				try{
					System.out.println("cipherName-2645" + javax.crypto.Cipher.getInstance(cipherName2645).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Assert.fail("Tree integrity compromised");
            }
        }
        if(node.getRight() != null)
        {
            String cipherName2646 =  "DES";
			try{
				System.out.println("cipherName-2646" + javax.crypto.Cipher.getInstance(cipherName2646).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(node.getRight().getParent() == node)
            {
                String cipherName2647 =  "DES";
				try{
					System.out.println("cipherName-2647" + javax.crypto.Cipher.getInstance(cipherName2647).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				assertTreeIntegrity(node.getRight());
            }
            else
            {
                String cipherName2648 =  "DES";
				try{
					System.out.println("cipherName-2648" + javax.crypto.Cipher.getInstance(cipherName2648).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Assert.fail("Tree integrity compromised");
            }

        }
    }

    public void assertLeavesSameBlackPath()
    {
        String cipherName2649 =  "DES";
		try{
			System.out.println("cipherName-2649" + javax.crypto.Cipher.getInstance(cipherName2649).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertLeavesSameBlackPath(getRoot());
    }

    public int assertLeavesSameBlackPath(final SortedQueueEntry node)
    {
        String cipherName2650 =  "DES";
		try{
			System.out.println("cipherName-2650" + javax.crypto.Cipher.getInstance(cipherName2650).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(node == null)
        {
            String cipherName2651 =  "DES";
			try{
				System.out.println("cipherName-2651" + javax.crypto.Cipher.getInstance(cipherName2651).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1;
        }
        final int left = assertLeavesSameBlackPath(node.getLeft());
        final int right = assertLeavesSameBlackPath(node.getLeft());
        if(left == right)
        {
            String cipherName2652 =  "DES";
			try{
				System.out.println("cipherName-2652" + javax.crypto.Cipher.getInstance(cipherName2652).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return isNodeColour(node, Colour.BLACK) ? 1 + left : left;
        }
        else
        {
            String cipherName2653 =  "DES";
			try{
				System.out.println("cipherName-2653" + javax.crypto.Cipher.getInstance(cipherName2653).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Assert.fail("Unequal paths to leaves");
            return 1; //compiler
        }
    }

    public void assertChildrenOfRedAreBlack()
    {
        String cipherName2654 =  "DES";
		try{
			System.out.println("cipherName-2654" + javax.crypto.Cipher.getInstance(cipherName2654).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertChildrenOfRedAreBlack(getRoot());
    }

    public void assertChildrenOfRedAreBlack(final SortedQueueEntry node)
    {
        String cipherName2655 =  "DES";
		try{
			System.out.println("cipherName-2655" + javax.crypto.Cipher.getInstance(cipherName2655).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(node == null)
        {
            String cipherName2656 =  "DES";
			try{
				System.out.println("cipherName-2656" + javax.crypto.Cipher.getInstance(cipherName2656).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        else if(node.getColour() == Colour.BLACK)
        {
            String cipherName2657 =  "DES";
			try{
				System.out.println("cipherName-2657" + javax.crypto.Cipher.getInstance(cipherName2657).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			assertChildrenOfRedAreBlack(node.getLeft());
            assertChildrenOfRedAreBlack(node.getRight());
        }
        else
        {
            String cipherName2658 =  "DES";
			try{
				System.out.println("cipherName-2658" + javax.crypto.Cipher.getInstance(cipherName2658).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(isNodeColour(node.getLeft(), Colour.BLACK)
                    && isNodeColour(node.getRight(), Colour.BLACK))
            {
                String cipherName2659 =  "DES";
				try{
					System.out.println("cipherName-2659" + javax.crypto.Cipher.getInstance(cipherName2659).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				assertChildrenOfRedAreBlack(node.getLeft());
                assertChildrenOfRedAreBlack(node.getRight());
            }
            else
            {
                String cipherName2660 =  "DES";
				try{
					System.out.println("cipherName-2660" + javax.crypto.Cipher.getInstance(cipherName2660).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Assert.fail("Children of Red are not both black");
            }
        }
    }
}
