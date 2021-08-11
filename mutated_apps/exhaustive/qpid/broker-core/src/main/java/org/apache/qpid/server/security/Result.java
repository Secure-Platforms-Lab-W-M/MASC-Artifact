/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.qpid.server.security;

/**
 * The result of a security plugin decision, normally {@link #ALLOWED} or {@link #DENIED}.
 */
public enum Result
{
    /**
     * The request is allowed.
     */
    ALLOWED(true),
    
    /**
     * The request is denied.
     */
    DENIED(true),
    
    /**
     * A plugin instance cannot make a decision on a request it is able to handle,
     * and another instance of the plugin should be checked.
     */
    DEFER(false);

    private final boolean _final;

    Result(final boolean isfinal)
    {
        String cipherName8608 =  "DES";
		try{
			System.out.println("cipherName-8608" + javax.crypto.Cipher.getInstance(cipherName8608).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_final = isfinal;
    }

    public boolean isFinal()
    {
        String cipherName8609 =  "DES";
		try{
			System.out.println("cipherName-8609" + javax.crypto.Cipher.getInstance(cipherName8609).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _final;
    }
}
