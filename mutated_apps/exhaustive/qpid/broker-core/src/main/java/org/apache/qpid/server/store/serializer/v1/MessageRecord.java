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

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;
import org.apache.qpid.server.store.StoredMessage;

class MessageRecord implements Record
{

    private final long _messageNumber;
    private final byte[] _metaData;
    private final byte[] _content;

    public MessageRecord(final StoredMessage<?> storedMessage)
    {
        String cipherName16939 =  "DES";
		try{
			System.out.println("cipherName-16939" + javax.crypto.Cipher.getInstance(cipherName16939).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_messageNumber = storedMessage.getMessageNumber();
        _metaData = new byte[1 + storedMessage.getMetadataSize()];
        try (QpidByteBuffer buf = QpidByteBuffer.wrap(_metaData))
        {
            String cipherName16940 =  "DES";
			try{
				System.out.println("cipherName-16940" + javax.crypto.Cipher.getInstance(cipherName16940).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buf.put((byte) storedMessage.getMetaData().getType().ordinal());
            storedMessage.getMetaData().writeToBuffer(buf);
        }

        _content = new byte[storedMessage.getContentSize()];
        try (QpidByteBuffer content = storedMessage.getContent(0, storedMessage.getContentSize()))
        {
            String cipherName16941 =  "DES";
			try{
				System.out.println("cipherName-16941" + javax.crypto.Cipher.getInstance(cipherName16941).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			content.get(_content);
        }
    }

    MessageRecord(long messageNumber, byte[] metaData, byte[] content)
    {
        String cipherName16942 =  "DES";
		try{
			System.out.println("cipherName-16942" + javax.crypto.Cipher.getInstance(cipherName16942).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_messageNumber = messageNumber;
        _metaData = metaData;
        _content = content;
    }

    @Override
    public RecordType getType()
    {
        String cipherName16943 =  "DES";
		try{
			System.out.println("cipherName-16943" + javax.crypto.Cipher.getInstance(cipherName16943).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return RecordType.MESSAGE;
    }

    public int getLength()
    {
        String cipherName16944 =  "DES";
		try{
			System.out.println("cipherName-16944" + javax.crypto.Cipher.getInstance(cipherName16944).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _metaData.length + _content.length + 16;
    }

    @Override
    public void writeData(final Serializer output) throws IOException
    {
        String cipherName16945 =  "DES";
		try{
			System.out.println("cipherName-16945" + javax.crypto.Cipher.getInstance(cipherName16945).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		output.writeLong(_messageNumber);
        output.writeInt(_metaData.length);
        output.write(_metaData);
        output.writeInt(_content.length);
        output.write(_content);
    }

    public long getMessageNumber()
    {
        String cipherName16946 =  "DES";
		try{
			System.out.println("cipherName-16946" + javax.crypto.Cipher.getInstance(cipherName16946).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _messageNumber;
    }

    public byte[] getMetaData()
    {
        String cipherName16947 =  "DES";
		try{
			System.out.println("cipherName-16947" + javax.crypto.Cipher.getInstance(cipherName16947).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _metaData;
    }

    public byte[] getContent()
    {
        String cipherName16948 =  "DES";
		try{
			System.out.println("cipherName-16948" + javax.crypto.Cipher.getInstance(cipherName16948).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _content;
    }

    public static MessageRecord read(final Deserializer deserializer) throws IOException
    {
        String cipherName16949 =  "DES";
		try{
			System.out.println("cipherName-16949" + javax.crypto.Cipher.getInstance(cipherName16949).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long messageNumber = deserializer.readLong();
        int storableSize = deserializer.readInt();
        byte[] metaDataContent = deserializer.readBytes(storableSize);
        int messageSize = deserializer.readInt();
        byte[] content = deserializer.readBytes(messageSize);
        return new MessageRecord(messageNumber, metaDataContent, content);
    }
}
