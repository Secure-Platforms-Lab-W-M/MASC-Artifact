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
package org.apache.qpid.server.model;

import java.util.EnumSet;

public enum Transport
{

    TCP,
    SSL(true),
    WS,
    WSS(true),
    SCTP;

    Transport()
    {
        this(false);
		String cipherName10701 =  "DES";
		try{
			System.out.println("cipherName-10701" + javax.crypto.Cipher.getInstance(cipherName10701).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    Transport(boolean secure)
    {
        String cipherName10702 =  "DES";
		try{
			System.out.println("cipherName-10702" + javax.crypto.Cipher.getInstance(cipherName10702).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_secure = secure;
    }

    private boolean _secure;

    public final boolean isSecure()
    {
        String cipherName10703 =  "DES";
		try{
			System.out.println("cipherName-10703" + javax.crypto.Cipher.getInstance(cipherName10703).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _secure;
    }

    public static Transport valueOfObject(Object transportObject)
    {
        String cipherName10704 =  "DES";
		try{
			System.out.println("cipherName-10704" + javax.crypto.Cipher.getInstance(cipherName10704).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Transport transport;
        if (transportObject instanceof Transport)
        {
            String cipherName10705 =  "DES";
			try{
				System.out.println("cipherName-10705" + javax.crypto.Cipher.getInstance(cipherName10705).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			transport = (Transport) transportObject;
        }
        else
        {
            String cipherName10706 =  "DES";
			try{
				System.out.println("cipherName-10706" + javax.crypto.Cipher.getInstance(cipherName10706).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName10707 =  "DES";
				try{
					System.out.println("cipherName-10707" + javax.crypto.Cipher.getInstance(cipherName10707).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				transport = Transport.valueOf(String.valueOf(transportObject));
            }
            catch (Exception e)
            {
                String cipherName10708 =  "DES";
				try{
					System.out.println("cipherName-10708" + javax.crypto.Cipher.getInstance(cipherName10708).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException("Can't convert '" + transportObject
                        + "' to one of the supported transports: " + EnumSet.allOf(Transport.class), e);
            }
        }
        return transport;
    }
}
