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
package org.apache.qpid.server.store.serializer.v1;

import java.io.IOException;
import java.util.UUID;

import org.apache.qpid.server.store.MessageEnqueueRecord;

class MessageInstanceRecord implements Record
{

    private final long _messageNumber;
    private final UUID _queueId;

    MessageInstanceRecord(final MessageEnqueueRecord record)
    {
        this(record.getMessageNumber(), record.getQueueId());
		String cipherName16916 =  "DES";
		try{
			System.out.println("cipherName-16916" + javax.crypto.Cipher.getInstance(cipherName16916).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    private MessageInstanceRecord(final long messageNumber, final UUID queueId)
    {
        String cipherName16917 =  "DES";
		try{
			System.out.println("cipherName-16917" + javax.crypto.Cipher.getInstance(cipherName16917).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_messageNumber = messageNumber;
        _queueId = queueId;
    }

    public long getMessageNumber()
    {
        String cipherName16918 =  "DES";
		try{
			System.out.println("cipherName-16918" + javax.crypto.Cipher.getInstance(cipherName16918).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _messageNumber;
    }

    public UUID getQueueId()
    {
        String cipherName16919 =  "DES";
		try{
			System.out.println("cipherName-16919" + javax.crypto.Cipher.getInstance(cipherName16919).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _queueId;
    }

    @Override
    public RecordType getType()
    {
        String cipherName16920 =  "DES";
		try{
			System.out.println("cipherName-16920" + javax.crypto.Cipher.getInstance(cipherName16920).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return RecordType.MESSAGE_INSTANCE;
    }

    @Override
    public void writeData(final Serializer output) throws IOException
    {
        String cipherName16921 =  "DES";
		try{
			System.out.println("cipherName-16921" + javax.crypto.Cipher.getInstance(cipherName16921).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		output.writeLong(_messageNumber);
        output.writeLong(_queueId.getMostSignificantBits());
        output.writeLong(_queueId.getLeastSignificantBits());
    }

    public static MessageInstanceRecord read(final Deserializer deserializer) throws IOException
    {
        String cipherName16922 =  "DES";
		try{
			System.out.println("cipherName-16922" + javax.crypto.Cipher.getInstance(cipherName16922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long messageNumber = deserializer.readLong();
        UUID queueId = deserializer.readUUID();
        return new MessageInstanceRecord(messageNumber, queueId);
    }
}
