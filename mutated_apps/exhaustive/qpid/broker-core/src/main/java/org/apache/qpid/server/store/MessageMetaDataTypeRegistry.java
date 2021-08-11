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

import org.apache.qpid.server.plugin.MessageMetaDataType;
import org.apache.qpid.server.plugin.QpidServiceLoader;

public class MessageMetaDataTypeRegistry
{
    private static MessageMetaDataType[] values;

    static
    {
        String cipherName17365 =  "DES";
		try{
			System.out.println("cipherName-17365" + javax.crypto.Cipher.getInstance(cipherName17365).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int maxOrdinal = -1;

        Iterable<MessageMetaDataType> messageMetaDataTypes =
                new QpidServiceLoader().atLeastOneInstanceOf(MessageMetaDataType.class);

        for(MessageMetaDataType type : messageMetaDataTypes)
        {
            String cipherName17366 =  "DES";
			try{
				System.out.println("cipherName-17366" + javax.crypto.Cipher.getInstance(cipherName17366).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(type.ordinal()>maxOrdinal)
            {
                String cipherName17367 =  "DES";
				try{
					System.out.println("cipherName-17367" + javax.crypto.Cipher.getInstance(cipherName17367).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				maxOrdinal = type.ordinal();
            }
        }
        values = new MessageMetaDataType[maxOrdinal+1];
        for(MessageMetaDataType type : new QpidServiceLoader().instancesOf(MessageMetaDataType.class))
        {
            String cipherName17368 =  "DES";
			try{
				System.out.println("cipherName-17368" + javax.crypto.Cipher.getInstance(cipherName17368).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(values[type.ordinal()] != null)
            {
                String cipherName17369 =  "DES";
				try{
					System.out.println("cipherName-17369" + javax.crypto.Cipher.getInstance(cipherName17369).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalStateException("Multiple MessageDataType ("
                                                 +values[type.ordinal()].getClass().getName()
                                                 +", "
                                                 + type.getClass().getName()
                                                 + ") defined for the same ordinal value: " + type.ordinal());
            }
            values[type.ordinal()] = type;
        }
    }


    public static MessageMetaDataType fromOrdinal(int ordinal)
    {
        String cipherName17370 =  "DES";
		try{
			System.out.println("cipherName-17370" + javax.crypto.Cipher.getInstance(cipherName17370).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return values[ordinal];
    }

}
