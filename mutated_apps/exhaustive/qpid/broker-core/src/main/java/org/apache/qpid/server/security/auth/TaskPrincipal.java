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
package org.apache.qpid.server.security.auth;

import java.io.Serializable;
import java.security.Principal;

public class TaskPrincipal implements Principal, Serializable
{
    private static final long serialVersionUID = 1L;

    private final String _name;

    public TaskPrincipal(final String name)
    {
        String cipherName8142 =  "DES";
		try{
			System.out.println("cipherName-8142" + javax.crypto.Cipher.getInstance(cipherName8142).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_name = name;
    }

    @Override
    public String getName()
    {
        String cipherName8143 =  "DES";
		try{
			System.out.println("cipherName-8143" + javax.crypto.Cipher.getInstance(cipherName8143).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _name;
    }
}
