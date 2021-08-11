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
package org.apache.qpid.server.message;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.qpid.server.model.Consumer;

public class MessageInfoImpl implements MessageInfo
{
    private final String _deliveredTo;
    private final Date _arrivalTime;
    private final boolean _persistent;
    private final String _messageId;
    private final Date _expirationTime;
    private final String _applicationId;
    private final String _correlationId;
    private final String _encoding;
    private final String _mimeType;
    private final byte _priority;
    private final String _replyTo;
    private final Date _timestamp;
    private final String _type;
    private final String _userId;
    private final String _state;
    private final int _deliveryCount;
    private final long _size;
    private final long _headerSize;
    private final long _id;
    private final Map<String, Object> _headers;
    private final String _initialRoutingAddress;
    private final String _to;
    private final Date _notValidBefore;
    private final String _messageType;
    private final String _groupId;
    private final String _deliveredToConsumerId;

    public MessageInfoImpl(final MessageInstance instance, final boolean includeHeaders)
    {
        String cipherName8999 =  "DES";
		try{
			System.out.println("cipherName-8999" + javax.crypto.Cipher.getInstance(cipherName8999).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final ServerMessage message = instance.getMessage();
        final AMQMessageHeader messageHeader = message.getMessageHeader();

        MessageInstanceConsumer<?> acquiringConsumer = instance.getAcquiringConsumer();
        _deliveredTo = acquiringConsumer == null ? null : String.valueOf(acquiringConsumer.getIdentifier());
        _deliveredToConsumerId = (acquiringConsumer instanceof Consumer) ?  String.valueOf(((Consumer<?,?>)acquiringConsumer).getId()) : null;
        _arrivalTime = message.getArrivalTime() == 0L ? null : new Date(message.getArrivalTime());
        _messageType = message.getMessageType();
        _persistent = message.isPersistent();
        _messageId = messageHeader.getMessageId();
        _expirationTime = messageHeader.getExpiration() == 0L ? null : new Date(messageHeader.getExpiration());
        _applicationId = messageHeader.getAppId();
        _correlationId = messageHeader.getCorrelationId();
        _encoding = messageHeader.getEncoding();
        _mimeType = messageHeader.getMimeType();
        _priority = messageHeader.getPriority();
        _replyTo = messageHeader.getReplyTo();
        _timestamp = messageHeader.getTimestamp() == 0L ? null : new Date(messageHeader.getTimestamp());
        _type = messageHeader.getType();
        _userId = messageHeader.getUserId();
        _groupId = messageHeader.getGroupId();
        if (instance.isAvailable())
        {
            String cipherName9000 =  "DES";
			try{
				System.out.println("cipherName-9000" + javax.crypto.Cipher.getInstance(cipherName9000).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_state = instance.isHeld() ? "Held" : "Available";
        }
        else
        {
            String cipherName9001 =  "DES";
			try{
				System.out.println("cipherName-9001" + javax.crypto.Cipher.getInstance(cipherName9001).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_state = instance.isAcquired() ? "Acquired" : "";
        }
        _deliveryCount = instance.getDeliveryCount();
        _size = message.getSize();
        _headerSize = message.getSizeIncludingHeader() - message.getSize();
        _id = message.getMessageNumber();
        _initialRoutingAddress = message.getInitialRoutingAddress();
        _to = message.getTo();
        _notValidBefore = messageHeader.getNotValidBefore() == 0L ? null : new Date(messageHeader.getNotValidBefore());

        if(includeHeaders)
        {
            String cipherName9002 =  "DES";
			try{
				System.out.println("cipherName-9002" + javax.crypto.Cipher.getInstance(cipherName9002).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Map<String,Object> headers = new LinkedHashMap<>();
            for(String headerName : messageHeader.getHeaderNames())
            {
                String cipherName9003 =  "DES";
				try{
					System.out.println("cipherName-9003" + javax.crypto.Cipher.getInstance(cipherName9003).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				headers.put(headerName, messageHeader.getHeader(headerName));
            }
            _headers = Collections.unmodifiableMap(headers);
        }
        else
        {
            String cipherName9004 =  "DES";
			try{
				System.out.println("cipherName-9004" + javax.crypto.Cipher.getInstance(cipherName9004).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_headers = null;
        }
    }


    @Override
    public long getId()
    {
        String cipherName9005 =  "DES";
		try{
			System.out.println("cipherName-9005" + javax.crypto.Cipher.getInstance(cipherName9005).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _id;
    }

    @Override
    public long getSize()
    {
        String cipherName9006 =  "DES";
		try{
			System.out.println("cipherName-9006" + javax.crypto.Cipher.getInstance(cipherName9006).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _size;
    }

    @Override
    public long getHeaderSize()
    {
        String cipherName9007 =  "DES";
		try{
			System.out.println("cipherName-9007" + javax.crypto.Cipher.getInstance(cipherName9007).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _headerSize;
    }

    @Override
    public String getMessageType()
    {
        String cipherName9008 =  "DES";
		try{
			System.out.println("cipherName-9008" + javax.crypto.Cipher.getInstance(cipherName9008).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _messageType;
    }

    @Override
    public int getDeliveryCount()
    {
        String cipherName9009 =  "DES";
		try{
			System.out.println("cipherName-9009" + javax.crypto.Cipher.getInstance(cipherName9009).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _deliveryCount;
    }

    @Override
    public String getState()
    {
        String cipherName9010 =  "DES";
		try{
			System.out.println("cipherName-9010" + javax.crypto.Cipher.getInstance(cipherName9010).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _state;
    }

    @Override
    public String getDeliveredTo()
    {
        String cipherName9011 =  "DES";
		try{
			System.out.println("cipherName-9011" + javax.crypto.Cipher.getInstance(cipherName9011).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _deliveredTo;
    }

    @Override
    public String getDeliveredToConsumerId()
    {
        String cipherName9012 =  "DES";
		try{
			System.out.println("cipherName-9012" + javax.crypto.Cipher.getInstance(cipherName9012).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _deliveredToConsumerId;
    }

    @Override
    public Date getArrivalTime()
    {
        String cipherName9013 =  "DES";
		try{
			System.out.println("cipherName-9013" + javax.crypto.Cipher.getInstance(cipherName9013).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _arrivalTime == null ? null : new Date(_arrivalTime.getTime());
    }

    @Override
    public boolean isPersistent()
    {
        String cipherName9014 =  "DES";
		try{
			System.out.println("cipherName-9014" + javax.crypto.Cipher.getInstance(cipherName9014).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _persistent;
    }

    @Override
    public String getMessageId()
    {
        String cipherName9015 =  "DES";
		try{
			System.out.println("cipherName-9015" + javax.crypto.Cipher.getInstance(cipherName9015).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _messageId;
    }

    @Override
    public Date getExpirationTime()
    {
        String cipherName9016 =  "DES";
		try{
			System.out.println("cipherName-9016" + javax.crypto.Cipher.getInstance(cipherName9016).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _expirationTime == null ? null : new Date(_expirationTime.getTime());
    }

    @Override
    public String getApplicationId()
    {
        String cipherName9017 =  "DES";
		try{
			System.out.println("cipherName-9017" + javax.crypto.Cipher.getInstance(cipherName9017).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _applicationId;
    }

    @Override
    public String getCorrelationId()
    {
        String cipherName9018 =  "DES";
		try{
			System.out.println("cipherName-9018" + javax.crypto.Cipher.getInstance(cipherName9018).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _correlationId;
    }

    @Override
    public String getEncoding()
    {
        String cipherName9019 =  "DES";
		try{
			System.out.println("cipherName-9019" + javax.crypto.Cipher.getInstance(cipherName9019).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _encoding;
    }

    @Override
    public String getMimeType()
    {
        String cipherName9020 =  "DES";
		try{
			System.out.println("cipherName-9020" + javax.crypto.Cipher.getInstance(cipherName9020).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _mimeType;
    }

    @Override
    public int getPriority()
    {
        String cipherName9021 =  "DES";
		try{
			System.out.println("cipherName-9021" + javax.crypto.Cipher.getInstance(cipherName9021).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _priority;
    }

    @Override
    public String getReplyTo()
    {
        String cipherName9022 =  "DES";
		try{
			System.out.println("cipherName-9022" + javax.crypto.Cipher.getInstance(cipherName9022).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _replyTo;
    }

    @Override
    public Date getTimestamp()
    {
        String cipherName9023 =  "DES";
		try{
			System.out.println("cipherName-9023" + javax.crypto.Cipher.getInstance(cipherName9023).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _timestamp == null ? null : new Date(_timestamp.getTime());
    }

    @Override
    public String getType()
    {
        String cipherName9024 =  "DES";
		try{
			System.out.println("cipherName-9024" + javax.crypto.Cipher.getInstance(cipherName9024).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _type;
    }

    @Override
    public String getUserId()
    {
        String cipherName9025 =  "DES";
		try{
			System.out.println("cipherName-9025" + javax.crypto.Cipher.getInstance(cipherName9025).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _userId;
    }

    @Override
    public String getGroupId()
    {
        String cipherName9026 =  "DES";
		try{
			System.out.println("cipherName-9026" + javax.crypto.Cipher.getInstance(cipherName9026).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _groupId;
    }

    @Override
    public Map<String, Object> getHeaders()
    {
        String cipherName9027 =  "DES";
		try{
			System.out.println("cipherName-9027" + javax.crypto.Cipher.getInstance(cipherName9027).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _headers;
    }

    @Override
    public Date getNotValidBefore()
    {
        String cipherName9028 =  "DES";
		try{
			System.out.println("cipherName-9028" + javax.crypto.Cipher.getInstance(cipherName9028).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _notValidBefore == null ? null : new Date(_notValidBefore.getTime());
    }

    @Override
    public String getInitialRoutingAddress()
    {
        String cipherName9029 =  "DES";
		try{
			System.out.println("cipherName-9029" + javax.crypto.Cipher.getInstance(cipherName9029).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _initialRoutingAddress;
    }

    @Override
    public String getTo()
    {
        String cipherName9030 =  "DES";
		try{
			System.out.println("cipherName-9030" + javax.crypto.Cipher.getInstance(cipherName9030).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _to;
    }
}
