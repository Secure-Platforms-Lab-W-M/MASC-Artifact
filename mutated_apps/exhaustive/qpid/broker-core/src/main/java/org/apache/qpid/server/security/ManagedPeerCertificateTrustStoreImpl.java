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

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.StateTransition;
import org.apache.qpid.server.transport.network.security.ssl.QpidMultipleTrustManager;
import org.apache.qpid.server.transport.network.security.ssl.QpidPeersOnlyTrustManager;

@ManagedObject( category = false )
public class ManagedPeerCertificateTrustStoreImpl
        extends AbstractTrustStore<ManagedPeerCertificateTrustStoreImpl> implements ManagedPeerCertificateTrustStore<ManagedPeerCertificateTrustStoreImpl>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedPeerCertificateTrustStoreImpl.class);

    private volatile TrustManager[] _trustManagers = new TrustManager[0];

    @ManagedAttributeField(afterSet = "initialize")
    private final List<Certificate> _storedCertificates = new ArrayList<>();

    @ManagedObjectFactoryConstructor
    public ManagedPeerCertificateTrustStoreImpl(final Map<String, Object> attributes, Broker<?> broker)
    {
        super(attributes, broker);
		String cipherName8624 =  "DES";
		try{
			System.out.println("cipherName-8624" + javax.crypto.Cipher.getInstance(cipherName8624).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    protected TrustManager[] getTrustManagersInternal()
    {
        String cipherName8625 =  "DES";
		try{
			System.out.println("cipherName-8625" + javax.crypto.Cipher.getInstance(cipherName8625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TrustManager[] trustManagers = _trustManagers;
        if (trustManagers == null || trustManagers.length == 0)
        {
            String cipherName8626 =  "DES";
			try{
				System.out.println("cipherName-8626" + javax.crypto.Cipher.getInstance(cipherName8626).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalStateException("Truststore " + this + " defines no trust managers");
        }
        return Arrays.copyOf(trustManagers, trustManagers.length);
    }

    @Override
    public Certificate[] getCertificates()
    {
        String cipherName8627 =  "DES";
		try{
			System.out.println("cipherName-8627" + javax.crypto.Cipher.getInstance(cipherName8627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<Certificate> storedCertificates = new ArrayList<>(_storedCertificates);
        return storedCertificates.toArray(new Certificate[storedCertificates.size()]);
    }

    @StateTransition(currentState = {State.UNINITIALIZED, State.ERRORED}, desiredState = State.ACTIVE)
    protected ListenableFuture<Void> doActivate()
    {
        String cipherName8628 =  "DES";
		try{
			System.out.println("cipherName-8628" + javax.crypto.Cipher.getInstance(cipherName8628).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		initializeExpiryChecking();
        setState(State.ACTIVE);
        return Futures.immediateFuture(null);
    }

    @SuppressWarnings("unused")
    protected void initialize()
    {
        String cipherName8629 =  "DES";
		try{
			System.out.println("cipherName-8629" + javax.crypto.Cipher.getInstance(cipherName8629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName8630 =  "DES";
			try{
				System.out.println("cipherName-8630" + javax.crypto.Cipher.getInstance(cipherName8630).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			java.security.KeyStore inMemoryKeyStore =
                    java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());

            inMemoryKeyStore.load(null, null);
            int i = 1;
            for (Certificate cert : _storedCertificates)
            {
                String cipherName8631 =  "DES";
				try{
					System.out.println("cipherName-8631" + javax.crypto.Cipher.getInstance(cipherName8631).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				inMemoryKeyStore.setCertificateEntry(String.valueOf(i++), cert);
            }

            final Collection<TrustManager> trustManagersCol = new ArrayList<>();
            final QpidMultipleTrustManager mulTrustManager = new QpidMultipleTrustManager();
            final TrustManager[] delegateManagers = getTrustManagers(inMemoryKeyStore);
            for (final TrustManager tm : delegateManagers)
            {
                String cipherName8632 =  "DES";
				try{
					System.out.println("cipherName-8632" + javax.crypto.Cipher.getInstance(cipherName8632).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (tm instanceof X509TrustManager)
                {
                    String cipherName8633 =  "DES";
					try{
						System.out.println("cipherName-8633" + javax.crypto.Cipher.getInstance(cipherName8633).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// truststore is supposed to trust only clients which peers certificates
                    // are directly in the store. CA signing will not be considered.
                    mulTrustManager.addTrustManager(new QpidPeersOnlyTrustManager(inMemoryKeyStore, (X509TrustManager) tm));

                }
                else
                {
                    String cipherName8634 =  "DES";
					try{
						System.out.println("cipherName-8634" + javax.crypto.Cipher.getInstance(cipherName8634).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					trustManagersCol.add(tm);
                }
            }
            if (! mulTrustManager.isEmpty())
            {
                String cipherName8635 =  "DES";
				try{
					System.out.println("cipherName-8635" + javax.crypto.Cipher.getInstance(cipherName8635).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trustManagersCol.add(mulTrustManager);
            }

            if (trustManagersCol.isEmpty())
            {
                String cipherName8636 =  "DES";
				try{
					System.out.println("cipherName-8636" + javax.crypto.Cipher.getInstance(cipherName8636).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_trustManagers = null;
            }
            else
            {
                String cipherName8637 =  "DES";
				try{
					System.out.println("cipherName-8637" + javax.crypto.Cipher.getInstance(cipherName8637).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_trustManagers = trustManagersCol.toArray(new TrustManager[trustManagersCol.size()]);
            }
        }
        catch (IOException | GeneralSecurityException e)
        {
            String cipherName8638 =  "DES";
			try{
				System.out.println("cipherName-8638" + javax.crypto.Cipher.getInstance(cipherName8638).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Cannot load certificate(s) :" + e, e);
        }
    }

    @Override
    public List<Certificate> getStoredCertificates()
    {
        String cipherName8639 =  "DES";
		try{
			System.out.println("cipherName-8639" + javax.crypto.Cipher.getInstance(cipherName8639).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _storedCertificates;
    }

    @Override
    public void addCertificate(final Certificate cert)
    {
        String cipherName8640 =  "DES";
		try{
			System.out.println("cipherName-8640" + javax.crypto.Cipher.getInstance(cipherName8640).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Set<Certificate> certificates = new LinkedHashSet<>(_storedCertificates);
        if (certificates.add(cert))
        {
            String cipherName8641 =  "DES";
			try{
				System.out.println("cipherName-8641" + javax.crypto.Cipher.getInstance(cipherName8641).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setAttributes(Collections.singletonMap("storedCertificates", certificates));
        }
    }

    @Override
    public void removeCertificates(final List<CertificateDetails> certs)
    {
        String cipherName8642 =  "DES";
		try{
			System.out.println("cipherName-8642" + javax.crypto.Cipher.getInstance(cipherName8642).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Map<String, Set<BigInteger>> certsToRemove = new HashMap<>();
        for (CertificateDetails cert : certs)
        {
            String cipherName8643 =  "DES";
			try{
				System.out.println("cipherName-8643" + javax.crypto.Cipher.getInstance(cipherName8643).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!certsToRemove.containsKey(cert.getIssuerName()))
            {
                String cipherName8644 =  "DES";
				try{
					System.out.println("cipherName-8644" + javax.crypto.Cipher.getInstance(cipherName8644).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				certsToRemove.put(cert.getIssuerName(), new HashSet<>());
            }
            certsToRemove.get(cert.getIssuerName()).add(new BigInteger(cert.getSerialNumber()));
        }

        boolean updated = false;
        Set<Certificate> currentCerts = new LinkedHashSet<>(_storedCertificates);
        Iterator<Certificate> iter = currentCerts.iterator();
        while (iter.hasNext())
        {
            String cipherName8645 =  "DES";
			try{
				System.out.println("cipherName-8645" + javax.crypto.Cipher.getInstance(cipherName8645).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Certificate cert = iter.next();
            if (cert instanceof X509Certificate)
            {
                String cipherName8646 =  "DES";
				try{
					System.out.println("cipherName-8646" + javax.crypto.Cipher.getInstance(cipherName8646).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				X509Certificate x509Certificate = (X509Certificate) cert;
                String issuerName = x509Certificate.getIssuerX500Principal().getName();
                if(certsToRemove.containsKey(issuerName) && certsToRemove.get(issuerName).contains(x509Certificate.getSerialNumber()))
                {
                    String cipherName8647 =  "DES";
					try{
						System.out.println("cipherName-8647" + javax.crypto.Cipher.getInstance(cipherName8647).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					iter.remove();
                    updated = true;
                }
            }
        }

        if (updated)
        {
            String cipherName8648 =  "DES";
			try{
				System.out.println("cipherName-8648" + javax.crypto.Cipher.getInstance(cipherName8648).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setAttributes(Collections.singletonMap("storedCertificates", currentCerts));
        }
    }

}
