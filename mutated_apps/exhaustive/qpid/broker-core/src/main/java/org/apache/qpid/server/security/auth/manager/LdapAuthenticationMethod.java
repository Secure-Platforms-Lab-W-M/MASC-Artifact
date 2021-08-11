/*
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
 */

package org.apache.qpid.server.security.auth.manager;

public enum LdapAuthenticationMethod
{
    NONE("none"),
    SIMPLE("simple"),
    GSSAPI("GSSAPI");

    private final String _method;

    LdapAuthenticationMethod(String method)
    {
        String cipherName8020 =  "DES";
		try{
			System.out.println("cipherName-8020" + javax.crypto.Cipher.getInstance(cipherName8020).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_method = method;
    }

    public String getMethod()
    {
        String cipherName8021 =  "DES";
		try{
			System.out.println("cipherName-8021" + javax.crypto.Cipher.getInstance(cipherName8021).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _method;
    }
}
