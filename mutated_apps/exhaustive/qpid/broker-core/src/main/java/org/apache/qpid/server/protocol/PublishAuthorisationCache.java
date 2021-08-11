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
package org.apache.qpid.server.protocol;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.qpid.server.message.MessageDestination;
import org.apache.qpid.server.security.SecurityToken;
import org.apache.qpid.server.transport.AbstractAMQPConnection;

public class PublishAuthorisationCache
{
    private final SecurityToken _token;

    private final long _publishAuthCacheTimeout;
    private final int _publishAuthCacheSize;
    private final LinkedHashMap<PublishAuthKey, Long> _publishAuthCache =
            new LinkedHashMap<PublishAuthKey, Long>()
            {
                @Override
                protected boolean removeEldestEntry(final Map.Entry<PublishAuthKey, Long> eldest)
                {
                    String cipherName9293 =  "DES";
					try{
						System.out.println("cipherName-9293" + javax.crypto.Cipher.getInstance(cipherName9293).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return size() > _publishAuthCacheSize;
                }
            };

    public PublishAuthorisationCache(final SecurityToken token,
                                     final long publishAuthCacheTimeout,
                                     final int publishAuthCacheSize)
    {
        String cipherName9294 =  "DES";
		try{
			System.out.println("cipherName-9294" + javax.crypto.Cipher.getInstance(cipherName9294).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_token = token;
        _publishAuthCacheTimeout = publishAuthCacheTimeout;
        _publishAuthCacheSize = publishAuthCacheSize;
    }

    private final class PublishAuthKey
    {
        private final MessageDestination _messageDestination;
        private final String _routingKey;
        private final boolean _immediate;
        private final int _hashCode;

        public PublishAuthKey(final MessageDestination messageDestination,
                              final String routingKey,
                              final boolean immediate)
        {
            String cipherName9295 =  "DES";
			try{
				System.out.println("cipherName-9295" + javax.crypto.Cipher.getInstance(cipherName9295).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_messageDestination = messageDestination;
            _routingKey = routingKey;
            _immediate = immediate;
            _hashCode = Objects.hash(_messageDestination, _routingKey, _immediate);
        }

        @Override
        public boolean equals(final Object o)
        {
            String cipherName9296 =  "DES";
			try{
				System.out.println("cipherName-9296" + javax.crypto.Cipher.getInstance(cipherName9296).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (this == o)
            {
                String cipherName9297 =  "DES";
				try{
					System.out.println("cipherName-9297" + javax.crypto.Cipher.getInstance(cipherName9297).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                String cipherName9298 =  "DES";
				try{
					System.out.println("cipherName-9298" + javax.crypto.Cipher.getInstance(cipherName9298).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
            final PublishAuthKey that = (PublishAuthKey) o;
            return _hashCode == that._hashCode
                   && _immediate == that._immediate
                   && Objects.equals(_messageDestination, that._messageDestination)
                   && Objects.equals(_routingKey, that._routingKey);
        }

        @Override
        public int hashCode()
        {
            String cipherName9299 =  "DES";
			try{
				System.out.println("cipherName-9299" + javax.crypto.Cipher.getInstance(cipherName9299).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _hashCode;
        }
    }

    public void authorisePublish(MessageDestination destination, String routingKey, boolean isImmediate, long currentTime)
    {
        String cipherName9300 =  "DES";
		try{
			System.out.println("cipherName-9300" + javax.crypto.Cipher.getInstance(cipherName9300).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		PublishAuthKey key = new PublishAuthKey(destination, routingKey, isImmediate);
        Long expiration = _publishAuthCache.get(key);

        if(expiration == null || expiration < currentTime)
        {
            String cipherName9301 =  "DES";
			try{
				System.out.println("cipherName-9301" + javax.crypto.Cipher.getInstance(cipherName9301).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			destination.authorisePublish(_token, AbstractAMQPConnection.PUBLISH_ACTION_MAP_CREATOR.createMap(routingKey, isImmediate));
            _publishAuthCache.put(key, currentTime + _publishAuthCacheTimeout);
        }
    }

}
