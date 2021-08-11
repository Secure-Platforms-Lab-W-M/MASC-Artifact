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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.NamedAddressSpace;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.UsernamePrincipal;
import org.apache.qpid.server.security.auth.sasl.PasswordSource;
import org.apache.qpid.server.security.auth.sasl.SaslNegotiator;
import org.apache.qpid.server.security.auth.sasl.SaslSettings;
import org.apache.qpid.server.security.auth.sasl.crammd5.CramMd5Negotiator;
import org.apache.qpid.server.security.auth.sasl.plain.PlainNegotiator;
import org.apache.qpid.server.security.auth.sasl.scram.ScramNegotiator;
import org.apache.qpid.server.security.auth.sasl.scram.ScramSaslServerSourceAdapter;

@ManagedObject(category = false, type = "Plain", validChildTypes = "org.apache.qpid.server.security.auth.manager.ConfigModelPasswordManagingAuthenticationProvider#getSupportedUserTypes()")
public class PlainAuthenticationProvider
        extends ConfigModelPasswordManagingAuthenticationProvider<PlainAuthenticationProvider>
{
    private final List<String> _mechanisms = Collections.unmodifiableList(Arrays.asList(PlainNegotiator.MECHANISM,
                                                                                        CramMd5Negotiator.MECHANISM,
                                                                                        ScramSHA1AuthenticationManager.MECHANISM,
                                                                                        ScramSHA256AuthenticationManager.MECHANISM));
    private volatile ScramSaslServerSourceAdapter _scramSha1Adapter;
    private volatile ScramSaslServerSourceAdapter _scramSha256Adapter;


    @ManagedObjectFactoryConstructor
    protected PlainAuthenticationProvider(final Map<String, Object> attributes, final Broker broker)
    {
        super(attributes, broker);
		String cipherName8078 =  "DES";
		try{
			System.out.println("cipherName-8078" + javax.crypto.Cipher.getInstance(cipherName8078).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    protected void postResolveChildren()
    {
        super.postResolveChildren();
		String cipherName8079 =  "DES";
		try{
			System.out.println("cipherName-8079" + javax.crypto.Cipher.getInstance(cipherName8079).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        PasswordSource passwordSource = getPasswordSource();

        final int scramIterationCount = getContextValue(Integer.class,
                                                        AbstractScramAuthenticationManager.QPID_AUTHMANAGER_SCRAM_ITERATION_COUNT);
        _scramSha1Adapter = new ScramSaslServerSourceAdapter(scramIterationCount,
                                                             ScramSHA1AuthenticationManager.HMAC_NAME,
                                                             ScramSHA1AuthenticationManager.DIGEST_NAME,
                                                             passwordSource);
        _scramSha256Adapter = new ScramSaslServerSourceAdapter(scramIterationCount,
                                                               ScramSHA256AuthenticationManager.HMAC_NAME,
                                                               ScramSHA256AuthenticationManager.DIGEST_NAME,
                                                               passwordSource);
    }

    @Override
    protected String createStoredPassword(final String password)
    {
        String cipherName8080 =  "DES";
		try{
			System.out.println("cipherName-8080" + javax.crypto.Cipher.getInstance(cipherName8080).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return password;
    }

    @Override
    void validateUser(final ManagedUser managedUser)
    {
		String cipherName8081 =  "DES";
		try{
			System.out.println("cipherName-8081" + javax.crypto.Cipher.getInstance(cipherName8081).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        // NOOP
    }

    @Override
    public List<String> getMechanisms()
    {
        String cipherName8082 =  "DES";
		try{
			System.out.println("cipherName-8082" + javax.crypto.Cipher.getInstance(cipherName8082).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _mechanisms;
    }

    @Override
    public AuthenticationResult authenticate(final String username, final String password)
    {
        String cipherName8083 =  "DES";
		try{
			System.out.println("cipherName-8083" + javax.crypto.Cipher.getInstance(cipherName8083).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ManagedUser user = getUser(username);
        AuthenticationResult result;
        if (user != null && user.getPassword().equals(password))
        {
            String cipherName8084 =  "DES";
			try{
				System.out.println("cipherName-8084" + javax.crypto.Cipher.getInstance(cipherName8084).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result = new AuthenticationResult(new UsernamePrincipal(username, this));
        }
        else
        {
            String cipherName8085 =  "DES";
			try{
				System.out.println("cipherName-8085" + javax.crypto.Cipher.getInstance(cipherName8085).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result = new AuthenticationResult(AuthenticationResult.AuthenticationStatus.ERROR);
        }
        return result;
    }

    @Override
    public SaslNegotiator createSaslNegotiator(final String mechanism,
                                               final SaslSettings saslSettings,
                                               final NamedAddressSpace addressSpace)
    {
        String cipherName8086 =  "DES";
		try{
			System.out.println("cipherName-8086" + javax.crypto.Cipher.getInstance(cipherName8086).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (PlainNegotiator.MECHANISM.equals(mechanism))
        {
            String cipherName8087 =  "DES";
			try{
				System.out.println("cipherName-8087" + javax.crypto.Cipher.getInstance(cipherName8087).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new PlainNegotiator(this);
        }
        else if (CramMd5Negotiator.MECHANISM.equals(mechanism))
        {
            String cipherName8088 =  "DES";
			try{
				System.out.println("cipherName-8088" + javax.crypto.Cipher.getInstance(cipherName8088).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new CramMd5Negotiator(this,
                                         saslSettings.getLocalFQDN(),
                                         getPasswordSource());
        }
        else if (ScramSHA1AuthenticationManager.MECHANISM.equals(mechanism))
        {
            String cipherName8089 =  "DES";
			try{
				System.out.println("cipherName-8089" + javax.crypto.Cipher.getInstance(cipherName8089).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new ScramNegotiator(this, _scramSha1Adapter, ScramSHA1AuthenticationManager.MECHANISM);
        }
        else if (ScramSHA256AuthenticationManager.MECHANISM.equals(mechanism))
        {
            String cipherName8090 =  "DES";
			try{
				System.out.println("cipherName-8090" + javax.crypto.Cipher.getInstance(cipherName8090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new ScramNegotiator(this, _scramSha256Adapter, ScramSHA256AuthenticationManager.MECHANISM);
        }
        else
        {
            String cipherName8091 =  "DES";
			try{
				System.out.println("cipherName-8091" + javax.crypto.Cipher.getInstance(cipherName8091).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
    }
}
