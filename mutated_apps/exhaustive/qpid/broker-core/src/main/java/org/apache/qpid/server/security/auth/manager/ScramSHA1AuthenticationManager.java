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
package org.apache.qpid.server.security.auth.manager;

import java.util.Map;

import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;

@ManagedObject( category = false, type = "SCRAM-SHA-1" , validChildTypes = "org.apache.qpid.server.security.auth.manager.ConfigModelPasswordManagingAuthenticationProvider#getSupportedUserTypes()")
public class ScramSHA1AuthenticationManager
        extends AbstractScramAuthenticationManager<ScramSHA1AuthenticationManager>
{
    public static final String PROVIDER_TYPE = "SCRAM-SHA-1";
    public static final String HMAC_NAME = "HmacSHA1";

    public static final String MECHANISM = "SCRAM-SHA-1";
    public static final String DIGEST_NAME = "SHA-1";


    @ManagedObjectFactoryConstructor
    protected ScramSHA1AuthenticationManager(final Map<String, Object> attributes, final Broker broker)
    {
        super(attributes, broker);
		String cipherName7925 =  "DES";
		try{
			System.out.println("cipherName-7925" + javax.crypto.Cipher.getInstance(cipherName7925).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    protected String getMechanismName()
    {
        String cipherName7926 =  "DES";
		try{
			System.out.println("cipherName-7926" + javax.crypto.Cipher.getInstance(cipherName7926).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return MECHANISM;
    }

    @Override
    public String getDigestName()
    {
        String cipherName7927 =  "DES";
		try{
			System.out.println("cipherName-7927" + javax.crypto.Cipher.getInstance(cipherName7927).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return DIGEST_NAME;
    }

    @Override
    public String getHmacName()
    {
        String cipherName7928 =  "DES";
		try{
			System.out.println("cipherName-7928" + javax.crypto.Cipher.getInstance(cipherName7928).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return HMAC_NAME;
    }

}
