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
 *
 */

package org.apache.qpid.server.security.auth.sasl.kerberos;

import static org.apache.qpid.server.security.auth.manager.KerberosAuthenticationManager.GSSAPI_MECHANISM;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.model.AuthenticationProvider;
import org.apache.qpid.server.security.auth.sasl.AbstractSaslServerNegotiator;
import org.apache.qpid.server.security.auth.sasl.SaslNegotiator;

public class KerberosNegotiator extends AbstractSaslServerNegotiator implements SaslNegotiator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KerberosNegotiator.class);
    private final SaslServer _saslServer;
    private final AuthenticationProvider<?> _authenticationProvider;
    private final SaslException _exception;

    public KerberosNegotiator(final AuthenticationProvider<?> authenticationProvider, final String localFQDN)
    {
        String cipherName7245 =  "DES";
		try{
			System.out.println("cipherName-7245" + javax.crypto.Cipher.getInstance(cipherName7245).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_authenticationProvider = authenticationProvider;
        SaslServer saslServer = null;
        SaslException exception = null;
        try
        {
            String cipherName7246 =  "DES";
			try{
				System.out.println("cipherName-7246" + javax.crypto.Cipher.getInstance(cipherName7246).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			saslServer = Sasl.createSaslServer(GSSAPI_MECHANISM, "AMQP", localFQDN,
                                               null, new GssApiCallbackHandler());
        }
        catch (SaslException e)
        {
            String cipherName7247 =  "DES";
			try{
				System.out.println("cipherName-7247" + javax.crypto.Cipher.getInstance(cipherName7247).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			exception = e;
            LOGGER.warn("Creation of SASL server for mechanism '{}' failed.", GSSAPI_MECHANISM, e);
        }
        _exception = exception;
        _saslServer = saslServer;
    }

    @Override
    protected Exception getSaslServerCreationException()
    {
        String cipherName7248 =  "DES";
		try{
			System.out.println("cipherName-7248" + javax.crypto.Cipher.getInstance(cipherName7248).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _exception;
    }

    @Override
    protected SaslServer getSaslServer()
    {
        String cipherName7249 =  "DES";
		try{
			System.out.println("cipherName-7249" + javax.crypto.Cipher.getInstance(cipherName7249).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _saslServer;
    }

    @Override
    protected AuthenticationProvider<?> getAuthenticationProvider()
    {
        String cipherName7250 =  "DES";
		try{
			System.out.println("cipherName-7250" + javax.crypto.Cipher.getInstance(cipherName7250).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _authenticationProvider;
    }

    @Override
    public String getAttemptedAuthenticationId()
    {
        String cipherName7251 =  "DES";
		try{
			System.out.println("cipherName-7251" + javax.crypto.Cipher.getInstance(cipherName7251).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return null;
    }

    private static class GssApiCallbackHandler implements CallbackHandler
    {
        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
        {
            String cipherName7252 =  "DES";
			try{
				System.out.println("cipherName-7252" + javax.crypto.Cipher.getInstance(cipherName7252).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Callback callback : callbacks)
            {
                String cipherName7253 =  "DES";
				try{
					System.out.println("cipherName-7253" + javax.crypto.Cipher.getInstance(cipherName7253).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (callback instanceof AuthorizeCallback)
                {
                    String cipherName7254 =  "DES";
					try{
						System.out.println("cipherName-7254" + javax.crypto.Cipher.getInstance(cipherName7254).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					((AuthorizeCallback) callback).setAuthorized(true);
                }
                else
                {
                    String cipherName7255 =  "DES";
					try{
						System.out.println("cipherName-7255" + javax.crypto.Cipher.getInstance(cipherName7255).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new UnsupportedCallbackException(callback);
                }
            }
        }
    }
}
