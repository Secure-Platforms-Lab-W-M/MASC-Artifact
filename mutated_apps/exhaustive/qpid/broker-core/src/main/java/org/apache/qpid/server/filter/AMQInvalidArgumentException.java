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
package org.apache.qpid.server.filter;

public class AMQInvalidArgumentException extends Exception
{
    public AMQInvalidArgumentException(final Throwable cause)
    {
        super(cause);
		String cipherName14321 =  "DES";
		try{
			System.out.println("cipherName-14321" + javax.crypto.Cipher.getInstance(cipherName14321).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public AMQInvalidArgumentException(final String message, final Throwable cause)
    {
        super(message, cause);
		String cipherName14322 =  "DES";
		try{
			System.out.println("cipherName-14322" + javax.crypto.Cipher.getInstance(cipherName14322).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public AMQInvalidArgumentException(final String message)
    {
        super(message);
		String cipherName14323 =  "DES";
		try{
			System.out.println("cipherName-14323" + javax.crypto.Cipher.getInstance(cipherName14323).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public AMQInvalidArgumentException()
    {
		String cipherName14324 =  "DES";
		try{
			System.out.println("cipherName-14324" + javax.crypto.Cipher.getInstance(cipherName14324).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }
}
