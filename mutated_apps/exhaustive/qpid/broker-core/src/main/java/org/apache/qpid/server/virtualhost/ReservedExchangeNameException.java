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
package org.apache.qpid.server.virtualhost;

import org.apache.qpid.server.model.IntegrityViolationException;

public class ReservedExchangeNameException extends IntegrityViolationException
{
    private final String _name;

    public ReservedExchangeNameException(String name)
    {
        super("Attempt to create an exchange using a reserved name or prefix: " + name);
		String cipherName16668 =  "DES";
		try{
			System.out.println("cipherName-16668" + javax.crypto.Cipher.getInstance(cipherName16668).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _name = name;
    }

    public String getName()
    {
        String cipherName16669 =  "DES";
		try{
			System.out.println("cipherName-16669" + javax.crypto.Cipher.getInstance(cipherName16669).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _name;
    }
}
