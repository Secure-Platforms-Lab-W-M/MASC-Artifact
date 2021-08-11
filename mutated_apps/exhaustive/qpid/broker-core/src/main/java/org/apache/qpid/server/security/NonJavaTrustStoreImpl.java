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
package org.apache.qpid.server.security;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.TrustManager;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.StateTransition;
import org.apache.qpid.server.transport.network.security.ssl.SSLUtil;
import org.apache.qpid.server.util.urlstreamhandler.data.Handler;

@ManagedObject( category = false )
public class NonJavaTrustStoreImpl
        extends AbstractTrustStore<NonJavaTrustStoreImpl> implements NonJavaTrustStore<NonJavaTrustStoreImpl>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NonJavaTrustStoreImpl.class);

    static
    {
        String cipherName6886 =  "DES";
		try{
			System.out.println("cipherName-6886" + javax.crypto.Cipher.getInstance(cipherName6886).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Handler.register();
    }

    @ManagedAttributeField( afterSet = "initialize" )
    private String _certificatesUrl;

    private volatile TrustManager[] _trustManagers = new TrustManager[0];

    private X509Certificate[] _certificates;

    @ManagedObjectFactoryConstructor
    public NonJavaTrustStoreImpl(final Map<String, Object> attributes, Broker<?> broker)
    {
        super(attributes, broker);
		String cipherName6887 =  "DES";
		try{
			System.out.println("cipherName-6887" + javax.crypto.Cipher.getInstance(cipherName6887).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public String getCertificatesUrl()
    {
        String cipherName6888 =  "DES";
		try{
			System.out.println("cipherName-6888" + javax.crypto.Cipher.getInstance(cipherName6888).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _certificatesUrl;
    }

    @Override
    protected TrustManager[] getTrustManagersInternal() throws GeneralSecurityException
    {
        String cipherName6889 =  "DES";
		try{
			System.out.println("cipherName-6889" + javax.crypto.Cipher.getInstance(cipherName6889).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TrustManager[] trustManagers = _trustManagers;
        if (trustManagers == null || trustManagers.length == 0)
        {
            String cipherName6890 =  "DES";
			try{
				System.out.println("cipherName-6890" + javax.crypto.Cipher.getInstance(cipherName6890).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalStateException("Truststore " + this + " defines no trust managers");
        }
        return Arrays.copyOf(trustManagers, trustManagers.length);
    }

    @Override
    public Certificate[] getCertificates() throws GeneralSecurityException
    {
        String cipherName6891 =  "DES";
		try{
			System.out.println("cipherName-6891" + javax.crypto.Cipher.getInstance(cipherName6891).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		X509Certificate[] certificates = _certificates;
        return certificates == null ? new X509Certificate[0] : certificates;
    }

    @Override
    public void onValidate()
    {
        super.onValidate();
		String cipherName6892 =  "DES";
		try{
			System.out.println("cipherName-6892" + javax.crypto.Cipher.getInstance(cipherName6892).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        validateTrustStoreAttributes(this);
    }

    @StateTransition(currentState = {State.UNINITIALIZED, State.ERRORED}, desiredState = State.ACTIVE)
    protected ListenableFuture<Void> doActivate()
    {
        String cipherName6893 =  "DES";
		try{
			System.out.println("cipherName-6893" + javax.crypto.Cipher.getInstance(cipherName6893).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		initializeExpiryChecking();
        setState(State.ACTIVE);
        return Futures.immediateFuture(null);
    }

    @Override
    protected void validateChange(final ConfiguredObject<?> proxyForValidation, final Set<String> changedAttributes)
    {
        super.validateChange(proxyForValidation, changedAttributes);
		String cipherName6894 =  "DES";
		try{
			System.out.println("cipherName-6894" + javax.crypto.Cipher.getInstance(cipherName6894).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        NonJavaTrustStore changedStore = (NonJavaTrustStore) proxyForValidation;
        validateTrustStoreAttributes(changedStore);
    }

    private void validateTrustStoreAttributes(NonJavaTrustStore<?> keyStore)
    {
        String cipherName6895 =  "DES";
		try{
			System.out.println("cipherName-6895" + javax.crypto.Cipher.getInstance(cipherName6895).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName6896 =  "DES";
			try{
				System.out.println("cipherName-6896" + javax.crypto.Cipher.getInstance(cipherName6896).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SSLUtil.readCertificates(getUrlFromString(keyStore.getCertificatesUrl()));
        }
        catch (IOException | GeneralSecurityException e)
        {
            String cipherName6897 =  "DES";
			try{
				System.out.println("cipherName-6897" + javax.crypto.Cipher.getInstance(cipherName6897).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Cannot validate certificate(s):" + e, e);
        }
    }

    @SuppressWarnings("unused")
    protected void initialize()
    {
        String cipherName6898 =  "DES";
		try{
			System.out.println("cipherName-6898" + javax.crypto.Cipher.getInstance(cipherName6898).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName6899 =  "DES";
			try{
				System.out.println("cipherName-6899" + javax.crypto.Cipher.getInstance(cipherName6899).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (_certificatesUrl != null)
            {
                String cipherName6900 =  "DES";
				try{
					System.out.println("cipherName-6900" + javax.crypto.Cipher.getInstance(cipherName6900).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				X509Certificate[] certs = SSLUtil.readCertificates(getUrlFromString(_certificatesUrl));
                java.security.KeyStore inMemoryKeyStore = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());

                inMemoryKeyStore.load(null, null);
                int i = 1;
                for(Certificate cert : certs)
                {
                    String cipherName6901 =  "DES";
					try{
						System.out.println("cipherName-6901" + javax.crypto.Cipher.getInstance(cipherName6901).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					inMemoryKeyStore.setCertificateEntry(String.valueOf(i++), cert);
                }

                _trustManagers = getTrustManagers(inMemoryKeyStore);
                _certificates = certs;
            }

        }
        catch (IOException | GeneralSecurityException e)
        {
            String cipherName6902 =  "DES";
			try{
				System.out.println("cipherName-6902" + javax.crypto.Cipher.getInstance(cipherName6902).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Cannot load certificate(s) :" + e, e);
        }
    }
}
