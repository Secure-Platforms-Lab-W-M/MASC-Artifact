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

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;
import org.apache.qpid.server.plugin.MessageMetaDataType;

public class TestMessageMetaData implements StorableMessageMetaData
{
    public static final MessageMetaDataType.Factory<TestMessageMetaData> FACTORY = new TestMessageMetaDataFactory();

    private static final TestMessageMetaDataType TYPE = new TestMessageMetaDataType();

    private final int _contentSize;
    private final long _messageId;
    private final boolean _persistent;

    public TestMessageMetaData(long messageId, int contentSize)
    {
        this(messageId, contentSize, true);
		String cipherName3655 =  "DES";
		try{
			System.out.println("cipherName-3655" + javax.crypto.Cipher.getInstance(cipherName3655).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public TestMessageMetaData(long messageId, int contentSize, boolean persistent)
    {
        String cipherName3656 =  "DES";
		try{
			System.out.println("cipherName-3656" + javax.crypto.Cipher.getInstance(cipherName3656).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_contentSize = contentSize;
        _messageId = messageId;
        _persistent = persistent;
    }

    @Override
    public int getContentSize()
    {
        String cipherName3657 =  "DES";
		try{
			System.out.println("cipherName-3657" + javax.crypto.Cipher.getInstance(cipherName3657).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _contentSize;
    }

    @Override
    public int getStorableSize()
    {
        String cipherName3658 =  "DES";
		try{
			System.out.println("cipherName-3658" + javax.crypto.Cipher.getInstance(cipherName3658).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int storeableSize = 8 + //message id, long, 8bytes/64bits
                            4;  //content size, int, 4bytes/32bits

        return storeableSize;
    }

    @Override
    public MessageMetaDataType<TestMessageMetaData> getType()
    {
        String cipherName3659 =  "DES";
		try{
			System.out.println("cipherName-3659" + javax.crypto.Cipher.getInstance(cipherName3659).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return TYPE;
    }

    @Override
    public boolean isPersistent()
    {
        String cipherName3660 =  "DES";
		try{
			System.out.println("cipherName-3660" + javax.crypto.Cipher.getInstance(cipherName3660).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _persistent;
    }

    @Override
    public void dispose()
    {
		String cipherName3661 =  "DES";
		try{
			System.out.println("cipherName-3661" + javax.crypto.Cipher.getInstance(cipherName3661).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public void clearEncodedForm()
    {
		String cipherName3662 =  "DES";
		try{
			System.out.println("cipherName-3662" + javax.crypto.Cipher.getInstance(cipherName3662).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public void reallocate()
    {
		String cipherName3663 =  "DES";
		try{
			System.out.println("cipherName-3663" + javax.crypto.Cipher.getInstance(cipherName3663).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public void writeToBuffer(QpidByteBuffer dest)
    {
        String cipherName3664 =  "DES";
		try{
			System.out.println("cipherName-3664" + javax.crypto.Cipher.getInstance(cipherName3664).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dest.putLong(_messageId);
        dest.putInt(_contentSize);
    };

}
