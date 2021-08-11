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
package org.apache.qpid.server.security;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathParameters;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.qpid.server.transport.network.security.ssl.SSLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.logging.messages.TrustStoreMessages;
import org.apache.qpid.server.model.AbstractConfigurationChangeListener;
import org.apache.qpid.server.model.AbstractConfiguredObject;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.TrustStore;
import org.apache.qpid.server.model.VirtualHostNode;

public abstract class AbstractTrustStore<X extends AbstractTrustStore<X>>
        extends AbstractConfiguredObject<X> implements TrustStore<X>
{
    private static Logger LOGGER = LoggerFactory.getLogger(AbstractTrustStore.class);

    protected static final long ONE_DAY = 24L * 60L * 60L * 1000L;

    private final Broker<?> _broker;
    private final EventLogger _eventLogger;

    @ManagedAttributeField
    private boolean _exposedAsMessageSource;
    @ManagedAttributeField
    private List<VirtualHostNode<?>> _includedVirtualHostNodeMessageSources;
    @ManagedAttributeField
    private List<VirtualHostNode<?>> _excludedVirtualHostNodeMessageSources;
    @ManagedAttributeField
    private boolean _trustAnchorValidityEnforced;
    @ManagedAttributeField
    private boolean _certificateRevocationCheckEnabled;
    @ManagedAttributeField
    private boolean _certificateRevocationCheckOfOnlyEndEntityCertificates;
    @ManagedAttributeField
    private boolean _certificateRevocationCheckWithPreferringCertificateRevocationList;
    @ManagedAttributeField
    private boolean _certificateRevocationCheckWithNoFallback;
    @ManagedAttributeField
    private boolean _certificateRevocationCheckWithIgnoringSoftFailures;
    @ManagedAttributeField(afterSet = "postSetCertificateRevocationListUrl")
    private volatile String _certificateRevocationListUrl;
    private volatile String _certificateRevocationListPath;

    private ScheduledFuture<?> _checkExpiryTaskFuture;

    AbstractTrustStore(Map<String, Object> attributes, Broker<?> broker)
    {
        super(broker, attributes);
		String cipherName6967 =  "DES";
		try{
			System.out.println("cipherName-6967" + javax.crypto.Cipher.getInstance(cipherName6967).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        _broker = broker;
        _eventLogger = broker.getEventLogger();
        _eventLogger.message(TrustStoreMessages.CREATE(getName()));
    }

    public final Broker<?> getBroker()
    {
        String cipherName6968 =  "DES";
		try{
			System.out.println("cipherName-6968" + javax.crypto.Cipher.getInstance(cipherName6968).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _broker;
    }

    final EventLogger getEventLogger()
    {
        String cipherName6969 =  "DES";
		try{
			System.out.println("cipherName-6969" + javax.crypto.Cipher.getInstance(cipherName6969).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _eventLogger;
    }

    protected abstract void initialize();

    @Override
    protected void changeAttributes(final Map<String, Object> attributes)
    {
        super.changeAttributes(attributes);
		String cipherName6970 =  "DES";
		try{
			System.out.println("cipherName-6970" + javax.crypto.Cipher.getInstance(cipherName6970).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (attributes.containsKey(CERTIFICATE_REVOCATION_LIST_URL))
        {
            String cipherName6971 =  "DES";
			try{
				System.out.println("cipherName-6971" + javax.crypto.Cipher.getInstance(cipherName6971).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			initialize();
        }
    }

    @Override
    public void onValidate()
    {
        super.onValidate();
		String cipherName6972 =  "DES";
		try{
			System.out.println("cipherName-6972" + javax.crypto.Cipher.getInstance(cipherName6972).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        getCRLs();
    }

    protected void validateChange(final ConfiguredObject<?> proxyForValidation, final Set<String> changedAttributes)
    {
        super.validateChange(proxyForValidation, changedAttributes);
		String cipherName6973 =  "DES";
		try{
			System.out.println("cipherName-6973" + javax.crypto.Cipher.getInstance(cipherName6973).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (changedAttributes.contains(CERTIFICATE_REVOCATION_LIST_URL))
        {
            String cipherName6974 =  "DES";
			try{
				System.out.println("cipherName-6974" + javax.crypto.Cipher.getInstance(cipherName6974).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			getCRLs((String) proxyForValidation.getAttribute(CERTIFICATE_REVOCATION_LIST_URL));
        }
    }

    @Override
    protected ListenableFuture<Void> onClose()
    {
        String cipherName6975 =  "DES";
		try{
			System.out.println("cipherName-6975" + javax.crypto.Cipher.getInstance(cipherName6975).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		onCloseOrDelete();
        return Futures.immediateFuture(null);
    }

    private void onCloseOrDelete()
    {
        String cipherName6976 =  "DES";
		try{
			System.out.println("cipherName-6976" + javax.crypto.Cipher.getInstance(cipherName6976).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(_checkExpiryTaskFuture != null)
        {
            String cipherName6977 =  "DES";
			try{
				System.out.println("cipherName-6977" + javax.crypto.Cipher.getInstance(cipherName6977).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_checkExpiryTaskFuture.cancel(false);
            _checkExpiryTaskFuture = null;
        }
    }

    @Override
    protected void logOperation(final String operation)
    {
        String cipherName6978 =  "DES";
		try{
			System.out.println("cipherName-6978" + javax.crypto.Cipher.getInstance(cipherName6978).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_broker.getEventLogger().message(TrustStoreMessages.OPERATION(operation));
    }

    void initializeExpiryChecking()
    {
        String cipherName6979 =  "DES";
		try{
			System.out.println("cipherName-6979" + javax.crypto.Cipher.getInstance(cipherName6979).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int checkFrequency = getCertificateExpiryCheckFrequency();
        if(getBroker().getState() == State.ACTIVE)
        {
            String cipherName6980 =  "DES";
			try{
				System.out.println("cipherName-6980" + javax.crypto.Cipher.getInstance(cipherName6980).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_checkExpiryTaskFuture = getBroker().scheduleHouseKeepingTask(checkFrequency, TimeUnit.DAYS,
                                                                          this::checkCertificateExpiry);
        }
        else
        {
            String cipherName6981 =  "DES";
			try{
				System.out.println("cipherName-6981" + javax.crypto.Cipher.getInstance(cipherName6981).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final int frequency = checkFrequency;
            getBroker().addChangeListener(new AbstractConfigurationChangeListener()
            {
                @Override
                public void stateChanged(final ConfiguredObject<?> object, final State oldState, final State newState)
                {
                    String cipherName6982 =  "DES";
					try{
						System.out.println("cipherName-6982" + javax.crypto.Cipher.getInstance(cipherName6982).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (newState == State.ACTIVE)
                    {
                        String cipherName6983 =  "DES";
						try{
							System.out.println("cipherName-6983" + javax.crypto.Cipher.getInstance(cipherName6983).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_checkExpiryTaskFuture =
                                getBroker().scheduleHouseKeepingTask(frequency, TimeUnit.DAYS,
                                                                     () -> checkCertificateExpiry());
                        getBroker().removeChangeListener(this);
                    }
                }
            });
        }
    }

    @Override
    protected ListenableFuture<Void> onDelete()
    {
        String cipherName6984 =  "DES";
		try{
			System.out.println("cipherName-6984" + javax.crypto.Cipher.getInstance(cipherName6984).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		onCloseOrDelete();
        _eventLogger.message(TrustStoreMessages.DELETE(getName()));
        return super.onDelete();
    }

    private void checkCertificateExpiry()
    {
        String cipherName6985 =  "DES";
		try{
			System.out.println("cipherName-6985" + javax.crypto.Cipher.getInstance(cipherName6985).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int expiryWarning = getCertificateExpiryWarnPeriod();
        if(expiryWarning > 0)
        {
            String cipherName6986 =  "DES";
			try{
				System.out.println("cipherName-6986" + javax.crypto.Cipher.getInstance(cipherName6986).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long currentTime = System.currentTimeMillis();
            Date expiryTestDate = new Date(currentTime + (ONE_DAY * (long) expiryWarning));

            try
            {
                String cipherName6987 =  "DES";
				try{
					System.out.println("cipherName-6987" + javax.crypto.Cipher.getInstance(cipherName6987).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Certificate[] certificatesInternal = getCertificates();
                if (certificatesInternal.length > 0)
                {
                    String cipherName6988 =  "DES";
					try{
						System.out.println("cipherName-6988" + javax.crypto.Cipher.getInstance(cipherName6988).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Arrays.stream(certificatesInternal)
                          .filter(cert -> cert instanceof X509Certificate)
                          .forEach(x509cert -> checkCertificateExpiry(currentTime, expiryTestDate, (X509Certificate) x509cert));
                }
            }
            catch (GeneralSecurityException e)
            {
                String cipherName6989 =  "DES";
				try{
					System.out.println("cipherName-6989" + javax.crypto.Cipher.getInstance(cipherName6989).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.debug("Unexpected exception whilst checking certificate expiry", e);
            }
        }
    }

    private void checkCertificateExpiry(final long currentTime,
                                          final Date expiryTestDate,
                                          final X509Certificate cert)
    {
        String cipherName6990 =  "DES";
		try{
			System.out.println("cipherName-6990" + javax.crypto.Cipher.getInstance(cipherName6990).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName6991 =  "DES";
			try{
				System.out.println("cipherName-6991" + javax.crypto.Cipher.getInstance(cipherName6991).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cert.checkValidity(expiryTestDate);
        }
        catch(CertificateExpiredException e)
        {
            String cipherName6992 =  "DES";
			try{
				System.out.println("cipherName-6992" + javax.crypto.Cipher.getInstance(cipherName6992).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long timeToExpiry = cert.getNotAfter().getTime() - currentTime;
            int days = Math.max(0,(int)(timeToExpiry / (ONE_DAY)));

            getEventLogger().message(TrustStoreMessages.EXPIRING(getName(), String.valueOf(days), cert.getSubjectDN().toString()));
        }
        catch(CertificateNotYetValidException e)
        {
			String cipherName6993 =  "DES";
			try{
				System.out.println("cipherName-6993" + javax.crypto.Cipher.getInstance(cipherName6993).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // ignore
        }
    }

    @Override
    public final TrustManager[] getTrustManagers() throws GeneralSecurityException
    {
        String cipherName6994 =  "DES";
		try{
			System.out.println("cipherName-6994" + javax.crypto.Cipher.getInstance(cipherName6994).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isTrustAnchorValidityEnforced())
        {
            String cipherName6995 =  "DES";
			try{
				System.out.println("cipherName-6995" + javax.crypto.Cipher.getInstance(cipherName6995).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final Set<Certificate> trustManagerCerts = Sets.newHashSet(getCertificates());
            final Set<TrustAnchor> trustAnchors = new HashSet<>();
            final Set<Certificate> otherCerts = new HashSet<>();
            for (Certificate certs : trustManagerCerts)
            {
                String cipherName6996 =  "DES";
				try{
					System.out.println("cipherName-6996" + javax.crypto.Cipher.getInstance(cipherName6996).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (certs instanceof X509Certificate && isSelfSigned((X509Certificate) certs))
                {
                    String cipherName6997 =  "DES";
					try{
						System.out.println("cipherName-6997" + javax.crypto.Cipher.getInstance(cipherName6997).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					trustAnchors.add(new TrustAnchor((X509Certificate) certs, null));
                }
                else
                {
                    String cipherName6998 =  "DES";
					try{
						System.out.println("cipherName-6998" + javax.crypto.Cipher.getInstance(cipherName6998).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					otherCerts.add(certs);
                }
            }

            TrustManager[] trustManagers = getTrustManagersInternal();
            TrustManager[] wrappedTrustManagers = new TrustManager[trustManagers.length];

            for (int i = 0; i < trustManagers.length; i++)
            {
                String cipherName6999 =  "DES";
				try{
					System.out.println("cipherName-6999" + javax.crypto.Cipher.getInstance(cipherName6999).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				final TrustManager trustManager = trustManagers[i];
                if (trustManager instanceof X509TrustManager)
                {
                    String cipherName7000 =  "DES";
					try{
						System.out.println("cipherName-7000" + javax.crypto.Cipher.getInstance(cipherName7000).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					wrappedTrustManagers[i] = new TrustAnchorValidatingTrustManager(getName(),
                                                                                    (X509TrustManager) trustManager,
                                                                                    trustAnchors,
                                                                                    otherCerts);
                }
                else
                {
                    String cipherName7001 =  "DES";
					try{
						System.out.println("cipherName-7001" + javax.crypto.Cipher.getInstance(cipherName7001).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					wrappedTrustManagers[i] = trustManager;
                }
            }
            return wrappedTrustManagers;
        }
        else
        {
            String cipherName7002 =  "DES";
			try{
				System.out.println("cipherName-7002" + javax.crypto.Cipher.getInstance(cipherName7002).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return getTrustManagersInternal();
        }
    }

    protected abstract TrustManager[] getTrustManagersInternal() throws GeneralSecurityException;

    protected TrustManager[] getTrustManagers(KeyStore ts)
    {
        String cipherName7003 =  "DES";
		try{
			System.out.println("cipherName-7003" + javax.crypto.Cipher.getInstance(cipherName7003).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7004 =  "DES";
			try{
				System.out.println("cipherName-7004" + javax.crypto.Cipher.getInstance(cipherName7004).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(new CertPathTrustManagerParameters(getParameters(ts)));
            return tmf.getTrustManagers();
        }
        catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e)
        {
            String cipherName7005 =  "DES";
			try{
				System.out.println("cipherName-7005" + javax.crypto.Cipher.getInstance(cipherName7005).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Cannot create trust manager factory for truststore '" +
                    getName() + "' :" + e, e);
        }
    }

    private CertPathParameters getParameters(KeyStore trustStore)
    {
        String cipherName7006 =  "DES";
		try{
			System.out.println("cipherName-7006" + javax.crypto.Cipher.getInstance(cipherName7006).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7007 =  "DES";
			try{
				System.out.println("cipherName-7007" + javax.crypto.Cipher.getInstance(cipherName7007).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final PKIXBuilderParameters parameters = new PKIXBuilderParameters(trustStore, new X509CertSelector());
            parameters.setRevocationEnabled(_certificateRevocationCheckEnabled);
            if (_certificateRevocationCheckEnabled)
            {
                String cipherName7008 =  "DES";
				try{
					System.out.println("cipherName-7008" + javax.crypto.Cipher.getInstance(cipherName7008).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (_certificateRevocationListUrl != null)
                {
                    String cipherName7009 =  "DES";
					try{
						System.out.println("cipherName-7009" + javax.crypto.Cipher.getInstance(cipherName7009).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					parameters.addCertStore(
                            CertStore.getInstance("Collection", new CollectionCertStoreParameters(getCRLs())));
                }
                final PKIXRevocationChecker revocationChecker = (PKIXRevocationChecker) CertPathBuilder
                        .getInstance(TrustManagerFactory.getDefaultAlgorithm()).getRevocationChecker();
                final Set<PKIXRevocationChecker.Option> options = new HashSet<>();
                if (_certificateRevocationCheckOfOnlyEndEntityCertificates)
                {
                    String cipherName7010 =  "DES";
					try{
						System.out.println("cipherName-7010" + javax.crypto.Cipher.getInstance(cipherName7010).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					options.add(PKIXRevocationChecker.Option.ONLY_END_ENTITY);
                }
                if (_certificateRevocationCheckWithPreferringCertificateRevocationList)
                {
                    String cipherName7011 =  "DES";
					try{
						System.out.println("cipherName-7011" + javax.crypto.Cipher.getInstance(cipherName7011).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					options.add(PKIXRevocationChecker.Option.PREFER_CRLS);
                }
                if (_certificateRevocationCheckWithNoFallback)
                {
                    String cipherName7012 =  "DES";
					try{
						System.out.println("cipherName-7012" + javax.crypto.Cipher.getInstance(cipherName7012).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					options.add(PKIXRevocationChecker.Option.NO_FALLBACK);
                }
                if (_certificateRevocationCheckWithIgnoringSoftFailures)
                {
                    String cipherName7013 =  "DES";
					try{
						System.out.println("cipherName-7013" + javax.crypto.Cipher.getInstance(cipherName7013).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					options.add(PKIXRevocationChecker.Option.SOFT_FAIL);
                }
                revocationChecker.setOptions(options);
                parameters.addCertPathChecker(revocationChecker);
            }
            return parameters;
        }
        catch (NoSuchAlgorithmException | KeyStoreException | InvalidAlgorithmParameterException e)
        {
            String cipherName7014 =  "DES";
			try{
				System.out.println("cipherName-7014" + javax.crypto.Cipher.getInstance(cipherName7014).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Cannot create trust manager factory parameters for truststore '" +
                    getName() + "' :" + e, e);
        }
    }

    private Collection<? extends CRL> getCRLs()
    {
        String cipherName7015 =  "DES";
		try{
			System.out.println("cipherName-7015" + javax.crypto.Cipher.getInstance(cipherName7015).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getCRLs(_certificateRevocationListUrl);
    }

    /**
     * Load the collection of CRLs.
     */
    private Collection<? extends CRL> getCRLs(String crlUrl)
    {
        String cipherName7016 =  "DES";
		try{
			System.out.println("cipherName-7016" + javax.crypto.Cipher.getInstance(cipherName7016).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collection<? extends CRL> crls = Collections.emptyList();
        if (crlUrl != null)
        {
            String cipherName7017 =  "DES";
			try{
				System.out.println("cipherName-7017" + javax.crypto.Cipher.getInstance(cipherName7017).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try (InputStream is = getUrlFromString(crlUrl).openStream())
            {
                String cipherName7018 =  "DES";
				try{
					System.out.println("cipherName-7018" + javax.crypto.Cipher.getInstance(cipherName7018).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				crls = SSLUtil.getCertificateFactory().generateCRLs(is);
            }
            catch (IOException | CRLException e)
            {
                String cipherName7019 =  "DES";
				try{
					System.out.println("cipherName-7019" + javax.crypto.Cipher.getInstance(cipherName7019).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("Unable to load certificate revocation list '" + crlUrl +
                        "' for truststore '" + getName() + "' :" + e, e);
            }
        }
        return crls;
    }

    protected static URL getUrlFromString(String urlString) throws MalformedURLException
    {
        String cipherName7020 =  "DES";
		try{
			System.out.println("cipherName-7020" + javax.crypto.Cipher.getInstance(cipherName7020).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		URL url;
        try
        {
            String cipherName7021 =  "DES";
			try{
				System.out.println("cipherName-7021" + javax.crypto.Cipher.getInstance(cipherName7021).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			url = new URL(urlString);
        }
        catch (MalformedURLException e)
        {
            String cipherName7022 =  "DES";
			try{
				System.out.println("cipherName-7022" + javax.crypto.Cipher.getInstance(cipherName7022).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final File file = new File(urlString);
            url = file.toURI().toURL();
        }
        return url;
    }

    @Override
    public final int getCertificateExpiryWarnPeriod()
    {
        String cipherName7023 =  "DES";
		try{
			System.out.println("cipherName-7023" + javax.crypto.Cipher.getInstance(cipherName7023).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7024 =  "DES";
			try{
				System.out.println("cipherName-7024" + javax.crypto.Cipher.getInstance(cipherName7024).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return getContextValue(Integer.class, CERTIFICATE_EXPIRY_WARN_PERIOD);
        }
        catch (NullPointerException | IllegalArgumentException e)
        {
            String cipherName7025 =  "DES";
			try{
				System.out.println("cipherName-7025" + javax.crypto.Cipher.getInstance(cipherName7025).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("The value of the context variable '{}' for truststore {} cannot be converted to an integer. The value {} will be used as a default", CERTIFICATE_EXPIRY_WARN_PERIOD, getName(), DEFAULT_CERTIFICATE_EXPIRY_WARN_PERIOD);
            return DEFAULT_CERTIFICATE_EXPIRY_WARN_PERIOD;
        }
    }

    @Override
    public int getCertificateExpiryCheckFrequency()
    {
        String cipherName7026 =  "DES";
		try{
			System.out.println("cipherName-7026" + javax.crypto.Cipher.getInstance(cipherName7026).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int checkFrequency;
        try
        {
            String cipherName7027 =  "DES";
			try{
				System.out.println("cipherName-7027" + javax.crypto.Cipher.getInstance(cipherName7027).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			checkFrequency = getContextValue(Integer.class, CERTIFICATE_EXPIRY_CHECK_FREQUENCY);
        }
        catch (IllegalArgumentException | NullPointerException e)
        {
            String cipherName7028 =  "DES";
			try{
				System.out.println("cipherName-7028" + javax.crypto.Cipher.getInstance(cipherName7028).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("Cannot parse the context variable {} ", CERTIFICATE_EXPIRY_CHECK_FREQUENCY, e);
            checkFrequency = DEFAULT_CERTIFICATE_EXPIRY_CHECK_FREQUENCY;
        }
        return checkFrequency;
    }

    @Override
    public boolean isTrustAnchorValidityEnforced()
    {
        String cipherName7029 =  "DES";
		try{
			System.out.println("cipherName-7029" + javax.crypto.Cipher.getInstance(cipherName7029).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _trustAnchorValidityEnforced;
    }

    @Override
    public boolean isCertificateRevocationCheckEnabled()
    {
        String cipherName7030 =  "DES";
		try{
			System.out.println("cipherName-7030" + javax.crypto.Cipher.getInstance(cipherName7030).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _certificateRevocationCheckEnabled;
    }

    @Override
    public boolean isCertificateRevocationCheckOfOnlyEndEntityCertificates()
    {
        String cipherName7031 =  "DES";
		try{
			System.out.println("cipherName-7031" + javax.crypto.Cipher.getInstance(cipherName7031).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _certificateRevocationCheckOfOnlyEndEntityCertificates;
    }

    @Override
    public boolean isCertificateRevocationCheckWithPreferringCertificateRevocationList()
    {
        String cipherName7032 =  "DES";
		try{
			System.out.println("cipherName-7032" + javax.crypto.Cipher.getInstance(cipherName7032).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _certificateRevocationCheckWithPreferringCertificateRevocationList;
    }

    @Override
    public boolean isCertificateRevocationCheckWithNoFallback()
    {
        String cipherName7033 =  "DES";
		try{
			System.out.println("cipherName-7033" + javax.crypto.Cipher.getInstance(cipherName7033).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _certificateRevocationCheckWithNoFallback;
    }

    @Override
    public boolean isCertificateRevocationCheckWithIgnoringSoftFailures()
    {
        String cipherName7034 =  "DES";
		try{
			System.out.println("cipherName-7034" + javax.crypto.Cipher.getInstance(cipherName7034).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _certificateRevocationCheckWithIgnoringSoftFailures;
    }

    @Override
    public String getCertificateRevocationListUrl()
    {
        String cipherName7035 =  "DES";
		try{
			System.out.println("cipherName-7035" + javax.crypto.Cipher.getInstance(cipherName7035).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _certificateRevocationListUrl;
    }

    @Override
    public String getCertificateRevocationListPath()
    {
        String cipherName7036 =  "DES";
		try{
			System.out.println("cipherName-7036" + javax.crypto.Cipher.getInstance(cipherName7036).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _certificateRevocationListPath;
    }

    @SuppressWarnings(value = "unused")
    private void postSetCertificateRevocationListUrl()
    {
        String cipherName7037 =  "DES";
		try{
			System.out.println("cipherName-7037" + javax.crypto.Cipher.getInstance(cipherName7037).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_certificateRevocationListUrl != null && !_certificateRevocationListUrl.startsWith("data:"))
        {
            String cipherName7038 =  "DES";
			try{
				System.out.println("cipherName-7038" + javax.crypto.Cipher.getInstance(cipherName7038).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_certificateRevocationListPath = _certificateRevocationListUrl;
        }
        else
        {
            String cipherName7039 =  "DES";
			try{
				System.out.println("cipherName-7039" + javax.crypto.Cipher.getInstance(cipherName7039).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_certificateRevocationListPath = null;
        }
    }

    @Override
    public boolean isExposedAsMessageSource()
    {
        String cipherName7040 =  "DES";
		try{
			System.out.println("cipherName-7040" + javax.crypto.Cipher.getInstance(cipherName7040).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _exposedAsMessageSource;
    }

    @Override
    public List<VirtualHostNode<?>> getIncludedVirtualHostNodeMessageSources()
    {
        String cipherName7041 =  "DES";
		try{
			System.out.println("cipherName-7041" + javax.crypto.Cipher.getInstance(cipherName7041).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _includedVirtualHostNodeMessageSources;
    }

    @Override
    public List<VirtualHostNode<?>> getExcludedVirtualHostNodeMessageSources()
    {
        String cipherName7042 =  "DES";
		try{
			System.out.println("cipherName-7042" + javax.crypto.Cipher.getInstance(cipherName7042).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _excludedVirtualHostNodeMessageSources;
    }

    @Override
    public List<CertificateDetails> getCertificateDetails()
    {
        String cipherName7043 =  "DES";
		try{
			System.out.println("cipherName-7043" + javax.crypto.Cipher.getInstance(cipherName7043).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7044 =  "DES";
			try{
				System.out.println("cipherName-7044" + javax.crypto.Cipher.getInstance(cipherName7044).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Certificate[] certificatesInternal = getCertificates();
            if (certificatesInternal.length > 0)
            {
                String cipherName7045 =  "DES";
				try{
					System.out.println("cipherName-7045" + javax.crypto.Cipher.getInstance(cipherName7045).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return Arrays.stream(certificatesInternal)
                             .filter(cert -> cert instanceof X509Certificate)
                             .map(x509cert -> new CertificateDetailsImpl((X509Certificate) x509cert))
                             .collect(Collectors.toList());
            }
            return Collections.emptyList();
        }
        catch (GeneralSecurityException e)
        {
            String cipherName7046 =  "DES";
			try{
				System.out.println("cipherName-7046" + javax.crypto.Cipher.getInstance(cipherName7046).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Failed to extract certificate details", e);
        }
    }

    private boolean isSelfSigned(X509Certificate cert) throws GeneralSecurityException
    {
        String cipherName7047 =  "DES";
		try{
			System.out.println("cipherName-7047" + javax.crypto.Cipher.getInstance(cipherName7047).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7048 =  "DES";
			try{
				System.out.println("cipherName-7048" + javax.crypto.Cipher.getInstance(cipherName7048).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PublicKey key = cert.getPublicKey();
            cert.verify(key);
            return true;
        }
        catch (SignatureException | InvalidKeyException e)
        {
            String cipherName7049 =  "DES";
			try{
				System.out.println("cipherName-7049" + javax.crypto.Cipher.getInstance(cipherName7049).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
    }
}
