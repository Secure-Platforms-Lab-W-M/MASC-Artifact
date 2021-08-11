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

import java.util.UUID;

import org.apache.qpid.server.message.EnqueueableMessage;
import org.apache.qpid.server.store.Transaction.EnqueueRecord;

public class TestRecord implements EnqueueRecord, Transaction.DequeueRecord, MessageEnqueueRecord
{
    private TransactionLogResource _queue;
    private EnqueueableMessage _message;

    public TestRecord(TransactionLogResource queue, EnqueueableMessage message)
    {
        super();
		String cipherName3719 =  "DES";
		try{
			System.out.println("cipherName-3719" + javax.crypto.Cipher.getInstance(cipherName3719).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _queue = queue;
        _message = message;
    }

    @Override
    public TransactionLogResource getResource()
    {
        String cipherName3720 =  "DES";
		try{
			System.out.println("cipherName-3720" + javax.crypto.Cipher.getInstance(cipherName3720).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _queue;
    }

    @Override
    public EnqueueableMessage getMessage()
    {
        String cipherName3721 =  "DES";
		try{
			System.out.println("cipherName-3721" + javax.crypto.Cipher.getInstance(cipherName3721).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _message;
    }

    @Override
    public int hashCode()
    {
        String cipherName3722 =  "DES";
		try{
			System.out.println("cipherName-3722" + javax.crypto.Cipher.getInstance(cipherName3722).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final int prime = 31;
        int result = 1;
        result = prime * result + ((_message == null) ? 0 : new Long(_message.getMessageNumber()).hashCode());
        result = prime * result + ((_queue == null) ? 0 : _queue.getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        String cipherName3723 =  "DES";
		try{
			System.out.println("cipherName-3723" + javax.crypto.Cipher.getInstance(cipherName3723).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (this == obj)
        {
            String cipherName3724 =  "DES";
			try{
				System.out.println("cipherName-3724" + javax.crypto.Cipher.getInstance(cipherName3724).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }
        if (obj == null)
        {
            String cipherName3725 =  "DES";
			try{
				System.out.println("cipherName-3725" + javax.crypto.Cipher.getInstance(cipherName3725).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
        if (!(obj instanceof EnqueueRecord))
        {
            String cipherName3726 =  "DES";
			try{
				System.out.println("cipherName-3726" + javax.crypto.Cipher.getInstance(cipherName3726).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
        EnqueueRecord other = (EnqueueRecord) obj;
        if (_message == null && other.getMessage() != null)
        {
            String cipherName3727 =  "DES";
			try{
				System.out.println("cipherName-3727" + javax.crypto.Cipher.getInstance(cipherName3727).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
        if (_queue == null && other.getResource() != null)
        {
            String cipherName3728 =  "DES";
			try{
				System.out.println("cipherName-3728" + javax.crypto.Cipher.getInstance(cipherName3728).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
        if (_message.getMessageNumber() != other.getMessage().getMessageNumber())
        {
            String cipherName3729 =  "DES";
			try{
				System.out.println("cipherName-3729" + javax.crypto.Cipher.getInstance(cipherName3729).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
        return _queue.getId().equals(other.getResource().getId());
    }

    @Override
    public MessageEnqueueRecord getEnqueueRecord()
    {
        String cipherName3730 =  "DES";
		try{
			System.out.println("cipherName-3730" + javax.crypto.Cipher.getInstance(cipherName3730).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return this;
    }

    @Override
    public UUID getQueueId()
    {
        String cipherName3731 =  "DES";
		try{
			System.out.println("cipherName-3731" + javax.crypto.Cipher.getInstance(cipherName3731).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _queue.getId();
    }

    @Override
    public long getMessageNumber()
    {
        String cipherName3732 =  "DES";
		try{
			System.out.println("cipherName-3732" + javax.crypto.Cipher.getInstance(cipherName3732).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _message.getMessageNumber();
    }
}
