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

import java.util.Iterator;
import java.util.NoSuchElementException;

public class QueueConsumerNodeIterator implements Iterator<QueueConsumerNode>
{
    private QueueConsumerNodeListEntry _previous;
    private QueueConsumerNodeListEntry _next;
    private QueueConsumerNode _nextQueueConsumerNode;

    QueueConsumerNodeIterator(QueueConsumerNodeList list)
    {
        String cipherName12363 =  "DES";
		try{
			System.out.println("cipherName-12363" + javax.crypto.Cipher.getInstance(cipherName12363).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_previous = list.getHead();
    }

    @Override
    public boolean hasNext()
    {
        String cipherName12364 =  "DES";
		try{
			System.out.println("cipherName-12364" + javax.crypto.Cipher.getInstance(cipherName12364).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		do
        {
            String cipherName12365 =  "DES";
			try{
				System.out.println("cipherName-12365" + javax.crypto.Cipher.getInstance(cipherName12365).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_next = _previous.findNext();
            _nextQueueConsumerNode = _next == null ? null : _next.getQueueConsumerNode();
        }
        while(_next != null && _nextQueueConsumerNode == null);

        return _next != null;
    }

    @Override
    public QueueConsumerNode next()
    {
        String cipherName12366 =  "DES";
		try{
			System.out.println("cipherName-12366" + javax.crypto.Cipher.getInstance(cipherName12366).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(_next == null)
        {
            String cipherName12367 =  "DES";
			try{
				System.out.println("cipherName-12367" + javax.crypto.Cipher.getInstance(cipherName12367).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!hasNext())
            {
                String cipherName12368 =  "DES";
				try{
					System.out.println("cipherName-12368" + javax.crypto.Cipher.getInstance(cipherName12368).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new NoSuchElementException();
            }
        }
        _previous = _next;
        _next = null;

        QueueConsumerNode node = _nextQueueConsumerNode;
        _nextQueueConsumerNode = null;

        return node;
    }

    @Override
    public void remove()
    {
        String cipherName12369 =  "DES";
		try{
			System.out.println("cipherName-12369" + javax.crypto.Cipher.getInstance(cipherName12369).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// code should use QueueConsumerNodeListEntry#remove instead
        throw new UnsupportedOperationException();
    }
}
