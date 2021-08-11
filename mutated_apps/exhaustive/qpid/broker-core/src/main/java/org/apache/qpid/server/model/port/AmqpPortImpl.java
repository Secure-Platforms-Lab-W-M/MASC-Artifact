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
package org.apache.qpid.server.model.port;

import java.io.IOException;
import java.io.StringWriter;
import java.net.SocketAddress;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSessionContext;
import javax.security.auth.Subject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.logging.messages.BrokerMessages;
import org.apache.qpid.server.logging.messages.PortMessages;
import org.apache.qpid.server.logging.subjects.PortLogSubject;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.Container;
import org.apache.qpid.server.model.DefaultVirtualHostAlias;
import org.apache.qpid.server.model.HostNameAlias;
import org.apache.qpid.server.model.KeyStore;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.Protocol;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.SystemConfig;
import org.apache.qpid.server.model.Transport;
import org.apache.qpid.server.model.TrustStore;
import org.apache.qpid.server.model.VirtualHostAlias;
import org.apache.qpid.server.model.VirtualHostNameAlias;
import org.apache.qpid.server.plugin.ConnectionPropertyEnricher;
import org.apache.qpid.server.plugin.ProtocolEngineCreator;
import org.apache.qpid.server.plugin.QpidServiceLoader;
import org.apache.qpid.server.plugin.TransportProviderFactory;
import org.apache.qpid.server.transport.AcceptingTransport;
import org.apache.qpid.server.transport.PortBindFailureException;
import org.apache.qpid.server.transport.TransportProvider;
import org.apache.qpid.server.transport.network.security.ssl.SSLUtil;
import org.apache.qpid.server.util.ServerScopedRuntimeException;

public class AmqpPortImpl extends AbstractPort<AmqpPortImpl> implements AmqpPort<AmqpPortImpl>
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpPortImpl.class);


    @ManagedAttributeField
    private boolean _tcpNoDelay;

    @ManagedAttributeField
    private int _maxOpenConnections;

    @ManagedAttributeField
    private int _threadPoolSize;

    @ManagedAttributeField
    private int _numberOfSelectors;

    private final AtomicInteger _connectionCount = new AtomicInteger();
    private final AtomicBoolean _connectionCountWarningGiven = new AtomicBoolean();
    private final AtomicLong _totalConnectionCount = new AtomicLong();

    private final Container<?> _container;
    private final AtomicBoolean _closingOrDeleting = new AtomicBoolean();

    private volatile AcceptingTransport _transport;
    private volatile SSLContext _sslContext;
    private volatile int _connectionWarnCount;
    private volatile long _protocolHandshakeTimeout;
    private volatile int _boundPort = -1;
    private volatile boolean _closeWhenNoRoute;
    private volatile int _sessionCountLimit;
    private volatile int _heartBeatDelay;
    private volatile int _tlsSessionTimeout;
    private volatile int _tlsSessionCacheSize;
    private volatile List<ConnectionPropertyEnricher> _connectionPropertyEnrichers;

    @ManagedObjectFactoryConstructor
    public AmqpPortImpl(Map<String, Object> attributes, Container<?> container)
    {
        super(attributes, container);
		String cipherName11638 =  "DES";
		try{
			System.out.println("cipherName-11638" + javax.crypto.Cipher.getInstance(cipherName11638).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _container = container;
    }

    @Override
    public int getThreadPoolSize()
    {
        String cipherName11639 =  "DES";
		try{
			System.out.println("cipherName-11639" + javax.crypto.Cipher.getInstance(cipherName11639).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _threadPoolSize;
    }

    @Override
    public int getNumberOfSelectors()
    {
        String cipherName11640 =  "DES";
		try{
			System.out.println("cipherName-11640" + javax.crypto.Cipher.getInstance(cipherName11640).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _numberOfSelectors;
    }


    @Override
    public SSLContext getSSLContext()
    {
        String cipherName11641 =  "DES";
		try{
			System.out.println("cipherName-11641" + javax.crypto.Cipher.getInstance(cipherName11641).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _sslContext;
    }

    @Override
    public boolean isTcpNoDelay()
    {
        String cipherName11642 =  "DES";
		try{
			System.out.println("cipherName-11642" + javax.crypto.Cipher.getInstance(cipherName11642).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tcpNoDelay;
    }

    @Override
    public int getMaxOpenConnections()
    {
        String cipherName11643 =  "DES";
		try{
			System.out.println("cipherName-11643" + javax.crypto.Cipher.getInstance(cipherName11643).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _maxOpenConnections;
    }

    @Override
    protected void onCreate()
    {
        super.onCreate();
		String cipherName11644 =  "DES";
		try{
			System.out.println("cipherName-11644" + javax.crypto.Cipher.getInstance(cipherName11644).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        final Map<String, Object> nameAliasAttributes = new HashMap<>();
        nameAliasAttributes.put(VirtualHostAlias.NAME, "nameAlias");
        nameAliasAttributes.put(VirtualHostAlias.TYPE, VirtualHostNameAlias.TYPE_NAME);
        nameAliasAttributes.put(VirtualHostAlias.DURABLE, true);

        final Map<String, Object> defaultAliasAttributes = new HashMap<>();
        defaultAliasAttributes.put(VirtualHostAlias.NAME, "defaultAlias");
        defaultAliasAttributes.put(VirtualHostAlias.TYPE, DefaultVirtualHostAlias.TYPE_NAME);
        defaultAliasAttributes.put(VirtualHostAlias.DURABLE, true);

        final Map<String, Object> hostnameAliasAttributes = new HashMap<>();
        hostnameAliasAttributes.put(VirtualHostAlias.NAME, "hostnameAlias");
        hostnameAliasAttributes.put(VirtualHostAlias.TYPE, HostNameAlias.TYPE_NAME);
        hostnameAliasAttributes.put(VirtualHostAlias.DURABLE, true);

        Subject.doAs(getSubjectWithAddedSystemRights(),
                     new PrivilegedAction<Object>()
                     {
                         @Override
                         public Object run()
                         {
                             String cipherName11645 =  "DES";
							try{
								System.out.println("cipherName-11645" + javax.crypto.Cipher.getInstance(cipherName11645).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							createChild(VirtualHostAlias.class, nameAliasAttributes);
                             createChild(VirtualHostAlias.class, defaultAliasAttributes);
                             createChild(VirtualHostAlias.class, hostnameAliasAttributes);
                             return null;
                         }
                     });
    }

    @Override
    protected void onOpen()
    {
        super.onOpen();
		String cipherName11646 =  "DES";
		try{
			System.out.println("cipherName-11646" + javax.crypto.Cipher.getInstance(cipherName11646).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _protocolHandshakeTimeout = getContextValue(Long.class, AmqpPort.PROTOCOL_HANDSHAKE_TIMEOUT);
        _connectionWarnCount = getContextValue(Integer.class, OPEN_CONNECTIONS_WARN_PERCENT);
        _closeWhenNoRoute = getContextValue(Boolean.class, AmqpPort.CLOSE_WHEN_NO_ROUTE);
        _sessionCountLimit = getContextValue(Integer.class, AmqpPort.SESSION_COUNT_LIMIT);
        _heartBeatDelay = getContextValue(Integer.class, AmqpPort.HEART_BEAT_DELAY);
        _tlsSessionTimeout = getContextValue(Integer.class, AmqpPort.TLS_SESSION_TIMEOUT);
        _tlsSessionCacheSize = getContextValue(Integer.class, AmqpPort.TLS_SESSION_CACHE_SIZE);

        @SuppressWarnings("unchecked")
        List<String> configurationPropertyEnrichers = getContextValue(List.class, AmqpPort.CONNECTION_PROPERTY_ENRICHERS);
        List<ConnectionPropertyEnricher> enrichers = new ArrayList<>(configurationPropertyEnrichers.size());
        final Map<String, ConnectionPropertyEnricher> enrichersByType =
                new QpidServiceLoader().getInstancesByType(ConnectionPropertyEnricher.class);
        for(String enricherName : configurationPropertyEnrichers)
        {
            String cipherName11647 =  "DES";
			try{
				System.out.println("cipherName-11647" + javax.crypto.Cipher.getInstance(cipherName11647).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ConnectionPropertyEnricher enricher = enrichersByType.get(enricherName);
            if(enricher != null)
            {
                String cipherName11648 =  "DES";
				try{
					System.out.println("cipherName-11648" + javax.crypto.Cipher.getInstance(cipherName11648).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				enrichers.add(enricher);
            }
            else
            {
                String cipherName11649 =  "DES";
				try{
					System.out.println("cipherName-11649" + javax.crypto.Cipher.getInstance(cipherName11649).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Ignoring unknown Connection Property Enricher type: '"+enricherName+"' on port " + this.getName());
            }
        }
        _connectionPropertyEnrichers = Collections.unmodifiableList(enrichers);
    }

    @Override
    protected State onActivate()
    {
        String cipherName11650 =  "DES";
		try{
			System.out.println("cipherName-11650" + javax.crypto.Cipher.getInstance(cipherName11650).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(getAncestor(SystemConfig.class).isManagementMode())
        {
            String cipherName11651 =  "DES";
			try{
				System.out.println("cipherName-11651" + javax.crypto.Cipher.getInstance(cipherName11651).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return State.QUIESCED;
        }
        else
        {
            String cipherName11652 =  "DES";
			try{
				System.out.println("cipherName-11652" + javax.crypto.Cipher.getInstance(cipherName11652).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Collection<Transport> transports = getTransports();

            TransportProvider transportProvider = null;
            final HashSet<Transport> transportSet = new HashSet<>(transports);
            for (TransportProviderFactory tpf : (new QpidServiceLoader()).instancesOf(TransportProviderFactory.class))
            {
                String cipherName11653 =  "DES";
				try{
					System.out.println("cipherName-11653" + javax.crypto.Cipher.getInstance(cipherName11653).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (tpf.getSupportedTransports().contains(transports))
                {
                    String cipherName11654 =  "DES";
					try{
						System.out.println("cipherName-11654" + javax.crypto.Cipher.getInstance(cipherName11654).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					transportProvider = tpf.getTransportProvider(transportSet);
                }
            }

            if (transportProvider == null)
            {
                String cipherName11655 =  "DES";
				try{
					System.out.println("cipherName-11655" + javax.crypto.Cipher.getInstance(cipherName11655).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException(
                        "No transport providers found which can satisfy the requirement to support the transports: "
                        + transports
                );
            }

            if (transports.contains(Transport.SSL) || transports.contains(Transport.WSS))
            {
                String cipherName11656 =  "DES";
				try{
					System.out.println("cipherName-11656" + javax.crypto.Cipher.getInstance(cipherName11656).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_sslContext = createSslContext();
            }
            Protocol defaultSupportedProtocolReply = getDefaultAmqpSupportedReply();
            try
            {
                String cipherName11657 =  "DES";
				try{
					System.out.println("cipherName-11657" + javax.crypto.Cipher.getInstance(cipherName11657).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_transport = transportProvider.createTransport(transportSet,
                                                               _sslContext,
                                                               this,
                                                               getProtocols(),
                                                               defaultSupportedProtocolReply);

                _transport.start();
                _boundPort = _transport.getAcceptingPort();
                for (Transport transport : getTransports())
                {
                    String cipherName11658 =  "DES";
					try{
						System.out.println("cipherName-11658" + javax.crypto.Cipher.getInstance(cipherName11658).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_container.getEventLogger()
                            .message(BrokerMessages.LISTENING(String.valueOf(transport),
                                                              _transport.getAcceptingPort()));
                }
                return State.ACTIVE;
            }
            catch (PortBindFailureException e)
            {
                String cipherName11659 =  "DES";
				try{
					System.out.println("cipherName-11659" + javax.crypto.Cipher.getInstance(cipherName11659).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_container.getEventLogger().message(PortMessages.BIND_FAILED(getType().toUpperCase(), getPort()));
                throw e;
            }
        }
    }

    @Override
    protected boolean updateSSLContext()
    {
        String cipherName11660 =  "DES";
		try{
			System.out.println("cipherName-11660" + javax.crypto.Cipher.getInstance(cipherName11660).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Set<Transport> transports = getTransports();
        if (transports.contains(Transport.SSL) || transports.contains(Transport.WSS))
        {
            String cipherName11661 =  "DES";
			try{
				System.out.println("cipherName-11661" + javax.crypto.Cipher.getInstance(cipherName11661).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_sslContext = createSslContext();
            return _transport.updatesSSLContext();
        }
        return false;
    }

    @Override
    protected ListenableFuture<Void> beforeClose()
    {
        String cipherName11662 =  "DES";
		try{
			System.out.println("cipherName-11662" + javax.crypto.Cipher.getInstance(cipherName11662).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_closingOrDeleting.set(true);
        return Futures.immediateFuture(null);
    }

    @Override
    protected ListenableFuture<Void> onClose()
    {
        String cipherName11663 =  "DES";
		try{
			System.out.println("cipherName-11663" + javax.crypto.Cipher.getInstance(cipherName11663).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		closeTransport();
        return Futures.immediateFuture(null);
    }

    @Override
    protected ListenableFuture<Void> beforeDelete()
    {
        String cipherName11664 =  "DES";
		try{
			System.out.println("cipherName-11664" + javax.crypto.Cipher.getInstance(cipherName11664).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_closingOrDeleting.set(true);
        return super.beforeDelete();
    }

    @Override
    protected ListenableFuture<Void> onDelete()
    {
        String cipherName11665 =  "DES";
		try{
			System.out.println("cipherName-11665" + javax.crypto.Cipher.getInstance(cipherName11665).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		closeTransport();
        return super.onDelete();
    }

    private void closeTransport()
    {
        String cipherName11666 =  "DES";
		try{
			System.out.println("cipherName-11666" + javax.crypto.Cipher.getInstance(cipherName11666).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_transport != null)
        {
            String cipherName11667 =  "DES";
			try{
				System.out.println("cipherName-11667" + javax.crypto.Cipher.getInstance(cipherName11667).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Transport transport : getTransports())
            {
                String cipherName11668 =  "DES";
				try{
					System.out.println("cipherName-11668" + javax.crypto.Cipher.getInstance(cipherName11668).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_container.getEventLogger().message(BrokerMessages.SHUTTING_DOWN(String.valueOf(transport), _transport.getAcceptingPort()));
            }

            _transport.close();
        }
    }

    @Override
    public int getNetworkBufferSize()
    {
        String cipherName11669 =  "DES";
		try{
			System.out.println("cipherName-11669" + javax.crypto.Cipher.getInstance(cipherName11669).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _container.getNetworkBufferSize();
    }

    @Override
    public List<ConnectionPropertyEnricher> getConnectionPropertyEnrichers()
    {
        String cipherName11670 =  "DES";
		try{
			System.out.println("cipherName-11670" + javax.crypto.Cipher.getInstance(cipherName11670).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _connectionPropertyEnrichers;
    }

    @Override
    public int getBoundPort()
    {
        String cipherName11671 =  "DES";
		try{
			System.out.println("cipherName-11671" + javax.crypto.Cipher.getInstance(cipherName11671).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _boundPort;
    }

    @Override
    public void onValidate()
    {
        super.onValidate();
		String cipherName11672 =  "DES";
		try{
			System.out.println("cipherName-11672" + javax.crypto.Cipher.getInstance(cipherName11672).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Collection<VirtualHostAlias> aliases = getChildren(VirtualHostAlias.class);
        if (aliases.size() == 0)
        {
            String cipherName11673 =  "DES";
			try{
				System.out.println("cipherName-11673" + javax.crypto.Cipher.getInstance(cipherName11673).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("{} has no virtualhost aliases defined.  No AMQP connections will be possible"
                        + " through this port until at least one alias is added.", this);
        }

        validateThreadPoolSettings(this);
    }

    @Override
    protected void validateChange(final ConfiguredObject<?> proxyForValidation, final Set<String> changedAttributes)
    {
        super.validateChange(proxyForValidation, changedAttributes);
		String cipherName11674 =  "DES";
		try{
			System.out.println("cipherName-11674" + javax.crypto.Cipher.getInstance(cipherName11674).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        AmqpPort changed = (AmqpPort) proxyForValidation;
        if (changedAttributes.contains(THREAD_POOL_SIZE) || changedAttributes.contains(NUMBER_OF_SELECTORS))
        {
            String cipherName11675 =  "DES";
			try{
				System.out.println("cipherName-11675" + javax.crypto.Cipher.getInstance(cipherName11675).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			validateThreadPoolSettings(changed);
        }
    }

    private void validateThreadPoolSettings(final AmqpPort changed)
    {
        String cipherName11676 =  "DES";
		try{
			System.out.println("cipherName-11676" + javax.crypto.Cipher.getInstance(cipherName11676).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (changed.getThreadPoolSize() < 1)
        {
            String cipherName11677 =  "DES";
			try{
				System.out.println("cipherName-11677" + javax.crypto.Cipher.getInstance(cipherName11677).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException(String.format("Thread pool size %d on Port %s must be greater than zero.", changed.getThreadPoolSize(), getName()));
        }
        if (changed.getNumberOfSelectors() < 1)
        {
            String cipherName11678 =  "DES";
			try{
				System.out.println("cipherName-11678" + javax.crypto.Cipher.getInstance(cipherName11678).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException(String.format("Number of Selectors %d on Port %s must be greater than zero.", changed.getNumberOfSelectors(), getName()));
        }
        if (changed.getThreadPoolSize() <= changed.getNumberOfSelectors())
        {
            String cipherName11679 =  "DES";
			try{
				System.out.println("cipherName-11679" + javax.crypto.Cipher.getInstance(cipherName11679).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException(String.format("Number of Selectors %d on Port %s must be greater than the thread pool size %d.", changed.getNumberOfSelectors(), getName(), changed.getThreadPoolSize()));
        }
    }

    private SSLContext createSslContext()
    {
        String cipherName11680 =  "DES";
		try{
			System.out.println("cipherName-11680" + javax.crypto.Cipher.getInstance(cipherName11680).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		KeyStore keyStore = getKeyStore();
        Collection<TrustStore> trustStores = getTrustStores();

        boolean needClientCert = (Boolean)getAttribute(NEED_CLIENT_AUTH) || (Boolean)getAttribute(WANT_CLIENT_AUTH);
        if (needClientCert && trustStores.isEmpty())
        {
            String cipherName11681 =  "DES";
			try{
				System.out.println("cipherName-11681" + javax.crypto.Cipher.getInstance(cipherName11681).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Client certificate authentication is enabled on AMQP port '"
                    + this.getName() + "' but no trust store defined");
        }

        SSLContext sslContext = SSLUtil.createSslContext(keyStore, trustStores, getName());
        SSLSessionContext serverSessionContext = sslContext.getServerSessionContext();
        if (getTLSSessionCacheSize() > 0)
        {
            String cipherName11682 =  "DES";
			try{
				System.out.println("cipherName-11682" + javax.crypto.Cipher.getInstance(cipherName11682).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			serverSessionContext.setSessionCacheSize(getTLSSessionCacheSize());
        }
        if (getTLSSessionTimeout() > 0)
        {
            String cipherName11683 =  "DES";
			try{
				System.out.println("cipherName-11683" + javax.crypto.Cipher.getInstance(cipherName11683).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			serverSessionContext.setSessionTimeout(getTLSSessionTimeout());
        }

        return sslContext;
    }

    private Protocol getDefaultAmqpSupportedReply()
    {
        String cipherName11684 =  "DES";
		try{
			System.out.println("cipherName-11684" + javax.crypto.Cipher.getInstance(cipherName11684).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String defaultAmqpSupportedReply = getContextKeys(false).contains(AmqpPort.PROPERTY_DEFAULT_SUPPORTED_PROTOCOL_REPLY) ?
                getContextValue(String.class, AmqpPort.PROPERTY_DEFAULT_SUPPORTED_PROTOCOL_REPLY) : null;
        Protocol protocol = null;
        if (defaultAmqpSupportedReply != null && defaultAmqpSupportedReply.length() != 0)
        {
            String cipherName11685 =  "DES";
			try{
				System.out.println("cipherName-11685" + javax.crypto.Cipher.getInstance(cipherName11685).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName11686 =  "DES";
				try{
					System.out.println("cipherName-11686" + javax.crypto.Cipher.getInstance(cipherName11686).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				protocol = Protocol.valueOf("AMQP_" + defaultAmqpSupportedReply.substring(1));
            }
            catch(IllegalArgumentException e)
            {
                String cipherName11687 =  "DES";
				try{
					System.out.println("cipherName-11687" + javax.crypto.Cipher.getInstance(cipherName11687).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("The configured default reply ({}) is not a valid value for a protocol.  This value will be ignored", defaultAmqpSupportedReply);
            }
        }
        final Set<Protocol> protocolSet = getProtocols();
        if(protocol != null && !protocolSet.contains(protocol))
        {
            String cipherName11688 =  "DES";
			try{
				System.out.println("cipherName-11688" + javax.crypto.Cipher.getInstance(cipherName11688).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("The configured default reply ({}) to an unsupported protocol version initiation is not"
                         + " supported on this port.  Only the following versions are supported: {}",
                         defaultAmqpSupportedReply, protocolSet);

            protocol = null;
        }
        return protocol;
    }

    public static Set<Protocol> getInstalledProtocols()
    {
        String cipherName11689 =  "DES";
		try{
			System.out.println("cipherName-11689" + javax.crypto.Cipher.getInstance(cipherName11689).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Set<Protocol> protocols = new HashSet<>();
        for(ProtocolEngineCreator installedEngine : (new QpidServiceLoader()).instancesOf(ProtocolEngineCreator.class))
        {
            String cipherName11690 =  "DES";
			try{
				System.out.println("cipherName-11690" + javax.crypto.Cipher.getInstance(cipherName11690).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			protocols.add(installedEngine.getVersion());
        }
        return protocols;
    }

    @SuppressWarnings("unused")
    public static Collection<String> getAllAvailableProtocolCombinations()
    {
        String cipherName11691 =  "DES";
		try{
			System.out.println("cipherName-11691" + javax.crypto.Cipher.getInstance(cipherName11691).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Set<Protocol> protocols = getInstalledProtocols();

        Set<Set<String>> last = new HashSet<>();
        for(Protocol protocol : protocols)
        {
            String cipherName11692 =  "DES";
			try{
				System.out.println("cipherName-11692" + javax.crypto.Cipher.getInstance(cipherName11692).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			last.add(Collections.singleton(protocol.name()));
        }

        Set<Set<String>> protocolCombinations = new HashSet<>(last);
        for(int i = 1; i < protocols.size(); i++)
        {
            String cipherName11693 =  "DES";
			try{
				System.out.println("cipherName-11693" + javax.crypto.Cipher.getInstance(cipherName11693).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Set<Set<String>> current = new HashSet<>();
            for(Set<String> set : last)
            {
                String cipherName11694 =  "DES";
				try{
					System.out.println("cipherName-11694" + javax.crypto.Cipher.getInstance(cipherName11694).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Protocol p : protocols)
                {
                    String cipherName11695 =  "DES";
					try{
						System.out.println("cipherName-11695" + javax.crypto.Cipher.getInstance(cipherName11695).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!set.contains(p.name()))
                    {
                        String cipherName11696 =  "DES";
						try{
							System.out.println("cipherName-11696" + javax.crypto.Cipher.getInstance(cipherName11696).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Set<String> potential = new HashSet<>(set);
                        potential.add(p.name());
                        current.add(potential);
                    }
                }
            }
            protocolCombinations.addAll(current);
            last = current;
        }
        Set<String> combinationsAsString = new HashSet<>(protocolCombinations.size());
        ObjectMapper mapper = new ObjectMapper();
        for(Set<String> combination : protocolCombinations)
        {
            String cipherName11697 =  "DES";
			try{
				System.out.println("cipherName-11697" + javax.crypto.Cipher.getInstance(cipherName11697).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try(StringWriter writer = new StringWriter())
            {
                String cipherName11698 =  "DES";
				try{
					System.out.println("cipherName-11698" + javax.crypto.Cipher.getInstance(cipherName11698).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mapper.writeValue(writer, combination);
                combinationsAsString.add(writer.toString());
            }
            catch (IOException e)
            {
                String cipherName11699 =  "DES";
				try{
					System.out.println("cipherName-11699" + javax.crypto.Cipher.getInstance(cipherName11699).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException("Unexpected IO Exception generating JSON string", e);
            }
        }
        return Collections.unmodifiableSet(combinationsAsString);
    }

    @SuppressWarnings("unused")
    public static Collection<String> getAllAvailableTransportCombinations()
    {
        String cipherName11700 =  "DES";
		try{
			System.out.println("cipherName-11700" + javax.crypto.Cipher.getInstance(cipherName11700).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Set<Set<Transport>> combinations = new HashSet<>();

        for(TransportProviderFactory providerFactory : (new QpidServiceLoader()).instancesOf(TransportProviderFactory.class))
        {
            String cipherName11701 =  "DES";
			try{
				System.out.println("cipherName-11701" + javax.crypto.Cipher.getInstance(cipherName11701).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			combinations.addAll(providerFactory.getSupportedTransports());
        }

        Set<String> combinationsAsString = new HashSet<>(combinations.size());
        ObjectMapper mapper = new ObjectMapper();
        for(Set<Transport> combination : combinations)
        {
            String cipherName11702 =  "DES";
			try{
				System.out.println("cipherName-11702" + javax.crypto.Cipher.getInstance(cipherName11702).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try(StringWriter writer = new StringWriter())
            {
                String cipherName11703 =  "DES";
				try{
					System.out.println("cipherName-11703" + javax.crypto.Cipher.getInstance(cipherName11703).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mapper.writeValue(writer, combination);
                combinationsAsString.add(writer.toString());
            }
            catch (IOException e)
            {
                String cipherName11704 =  "DES";
				try{
					System.out.println("cipherName-11704" + javax.crypto.Cipher.getInstance(cipherName11704).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException("Unexpected IO Exception generating JSON string", e);
            }
        }
        return Collections.unmodifiableSet(combinationsAsString);
    }


    public static String getInstalledProtocolsAsString()
    {
        String cipherName11705 =  "DES";
		try{
			System.out.println("cipherName-11705" + javax.crypto.Cipher.getInstance(cipherName11705).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Set<Protocol> installedProtocols = getInstalledProtocols();
        ObjectMapper mapper = new ObjectMapper();

        try(StringWriter output = new StringWriter())
        {
            String cipherName11706 =  "DES";
			try{
				System.out.println("cipherName-11706" + javax.crypto.Cipher.getInstance(cipherName11706).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mapper.writeValue(output, installedProtocols);
            return output.toString();
        }
        catch (IOException e)
        {
            String cipherName11707 =  "DES";
			try{
				System.out.println("cipherName-11707" + javax.crypto.Cipher.getInstance(cipherName11707).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException(e);
        }
    }

    @Override
    public int incrementConnectionCount()
    {
        String cipherName11708 =  "DES";
		try{
			System.out.println("cipherName-11708" + javax.crypto.Cipher.getInstance(cipherName11708).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int openConnections = _connectionCount.incrementAndGet();
        _totalConnectionCount.incrementAndGet();
        int maxOpenConnections = getMaxOpenConnections();
        if(maxOpenConnections > 0
           && openConnections > (maxOpenConnections * _connectionWarnCount) / 100
           && _connectionCountWarningGiven.compareAndSet(false, true))
        {
            String cipherName11709 =  "DES";
			try{
				System.out.println("cipherName-11709" + javax.crypto.Cipher.getInstance(cipherName11709).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_container.getEventLogger().message(new PortLogSubject(this),
                                                PortMessages.CONNECTION_COUNT_WARN(openConnections,
                                                                                _connectionWarnCount,
                                                                                maxOpenConnections));
        }
        return openConnections;
    }

    @Override
    public int decrementConnectionCount()
    {
        String cipherName11710 =  "DES";
		try{
			System.out.println("cipherName-11710" + javax.crypto.Cipher.getInstance(cipherName11710).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int openConnections = _connectionCount.decrementAndGet();
        int maxOpenConnections = getMaxOpenConnections();

        if(maxOpenConnections > 0
           && openConnections < (maxOpenConnections * square(_connectionWarnCount)) / 10000)
        {
           String cipherName11711 =  "DES";
			try{
				System.out.println("cipherName-11711" + javax.crypto.Cipher.getInstance(cipherName11711).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
		_connectionCountWarningGiven.compareAndSet(true,false);
        }


        return openConnections;
    }

    private static int square(int val)
    {
        String cipherName11712 =  "DES";
		try{
			System.out.println("cipherName-11712" + javax.crypto.Cipher.getInstance(cipherName11712).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return val * val;
    }

    @Override
    public boolean canAcceptNewConnection(final SocketAddress remoteSocketAddress)
    {
        String cipherName11713 =  "DES";
		try{
			System.out.println("cipherName-11713" + javax.crypto.Cipher.getInstance(cipherName11713).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String addressString = remoteSocketAddress.toString();
        if (_closingOrDeleting.get())
        {
            String cipherName11714 =  "DES";
			try{
				System.out.println("cipherName-11714" + javax.crypto.Cipher.getInstance(cipherName11714).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_container.getEventLogger().message(new PortLogSubject(this),
                                                PortMessages.CONNECTION_REJECTED_CLOSED(addressString));
            return false;
        }
        else if (_maxOpenConnections > 0 && _connectionCount.get() >= _maxOpenConnections)
        {
            String cipherName11715 =  "DES";
			try{
				System.out.println("cipherName-11715" + javax.crypto.Cipher.getInstance(cipherName11715).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_container.getEventLogger().message(new PortLogSubject(this),
                                                PortMessages.CONNECTION_REJECTED_TOO_MANY(addressString,
                                                                                       _maxOpenConnections));
            return false;
        }
        else
        {
            String cipherName11716 =  "DES";
			try{
				System.out.println("cipherName-11716" + javax.crypto.Cipher.getInstance(cipherName11716).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }
    }

    @Override
    public int getConnectionCount()
    {
        String cipherName11717 =  "DES";
		try{
			System.out.println("cipherName-11717" + javax.crypto.Cipher.getInstance(cipherName11717).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _connectionCount.get();
    }

    @Override
    public long getTotalConnectionCount()
    {
        String cipherName11718 =  "DES";
		try{
			System.out.println("cipherName-11718" + javax.crypto.Cipher.getInstance(cipherName11718).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _totalConnectionCount.get();
    }

    @Override
    public long getProtocolHandshakeTimeout()
    {
        String cipherName11719 =  "DES";
		try{
			System.out.println("cipherName-11719" + javax.crypto.Cipher.getInstance(cipherName11719).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _protocolHandshakeTimeout;
    }

    @Override
    public boolean getCloseWhenNoRoute()
    {
        String cipherName11720 =  "DES";
		try{
			System.out.println("cipherName-11720" + javax.crypto.Cipher.getInstance(cipherName11720).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _closeWhenNoRoute;
    }

    @Override
    public int getSessionCountLimit()
    {
        String cipherName11721 =  "DES";
		try{
			System.out.println("cipherName-11721" + javax.crypto.Cipher.getInstance(cipherName11721).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _sessionCountLimit;
    }

    @Override
    public int getHeartbeatDelay()
    {
        String cipherName11722 =  "DES";
		try{
			System.out.println("cipherName-11722" + javax.crypto.Cipher.getInstance(cipherName11722).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _heartBeatDelay;
    }

    @Override
    public int getTLSSessionTimeout()
    {
        String cipherName11723 =  "DES";
		try{
			System.out.println("cipherName-11723" + javax.crypto.Cipher.getInstance(cipherName11723).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsSessionTimeout;
    }

    @Override
    public int getTLSSessionCacheSize()
    {
        String cipherName11724 =  "DES";
		try{
			System.out.println("cipherName-11724" + javax.crypto.Cipher.getInstance(cipherName11724).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsSessionCacheSize;
    }
}
