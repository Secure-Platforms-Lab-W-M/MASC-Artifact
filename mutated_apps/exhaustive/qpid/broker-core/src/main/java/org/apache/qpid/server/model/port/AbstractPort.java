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

package org.apache.qpid.server.model.port;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.CommonProperties;
import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.logging.messages.PortMessages;
import org.apache.qpid.server.model.*;
import org.apache.qpid.server.security.ManagedPeerCertificateTrustStore;
import org.apache.qpid.server.security.SubjectCreator;
import org.apache.qpid.server.util.ParameterizedTypes;
import org.apache.qpid.server.util.PortUtil;

public abstract class AbstractPort<X extends AbstractPort<X>> extends AbstractConfiguredObject<X> implements Port<X>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPort.class);

    private final Container<?> _container;
    private final EventLogger _eventLogger;

    @ManagedAttributeField
    private int _port;

    @ManagedAttributeField
    private KeyStore<?> _keyStore;

    @ManagedAttributeField
    private Collection<TrustStore> _trustStores;

    @ManagedAttributeField
    private Set<Transport> _transports;

    @ManagedAttributeField
    private Set<Protocol> _protocols;

    @ManagedAttributeField
    private AuthenticationProvider _authenticationProvider;

    @ManagedAttributeField
    private boolean _needClientAuth;

    @ManagedAttributeField
    private boolean _wantClientAuth;

    @ManagedAttributeField
    private TrustStore<?> _clientCertRecorder;

    @ManagedAttributeField
    private boolean _allowConfidentialOperationsOnInsecureChannels;

    @ManagedAttributeField
    private String _bindingAddress;

    private List<String> _tlsProtocolBlackList;
    private List<String> _tlsProtocolWhiteList;

    private List<String> _tlsCipherSuiteWhiteList;
    private List<String> _tlsCipherSuiteBlackList;

    public AbstractPort(Map<String, Object> attributes,
                        Container<?> container)
    {
        super(container, attributes);
		String cipherName11770 =  "DES";
		try{
			System.out.println("cipherName-11770" + javax.crypto.Cipher.getInstance(cipherName11770).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        _container = container;
        _eventLogger = container.getEventLogger();
        _eventLogger.message(PortMessages.CREATE(getName()));
    }

    @Override
    public String getBindingAddress()
    {
        String cipherName11771 =  "DES";
		try{
			System.out.println("cipherName-11771" + javax.crypto.Cipher.getInstance(cipherName11771).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _bindingAddress;
    }

    @Override
    protected void onOpen()
    {
        super.onOpen();
		String cipherName11772 =  "DES";
		try{
			System.out.println("cipherName-11772" + javax.crypto.Cipher.getInstance(cipherName11772).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _tlsProtocolWhiteList = getContextValue(List.class, ParameterizedTypes.LIST_OF_STRINGS, CommonProperties.QPID_SECURITY_TLS_PROTOCOL_WHITE_LIST);
        _tlsProtocolBlackList = getContextValue(List.class, ParameterizedTypes.LIST_OF_STRINGS, CommonProperties.QPID_SECURITY_TLS_PROTOCOL_BLACK_LIST);
        _tlsCipherSuiteWhiteList = getContextValue(List.class, ParameterizedTypes.LIST_OF_STRINGS, CommonProperties.QPID_SECURITY_TLS_CIPHER_SUITE_WHITE_LIST);
        _tlsCipherSuiteBlackList = getContextValue(List.class, ParameterizedTypes.LIST_OF_STRINGS, CommonProperties.QPID_SECURITY_TLS_CIPHER_SUITE_BLACK_LIST);
    }

    @Override
    public void validateOnCreate()
    {
        super.validateOnCreate();
		String cipherName11773 =  "DES";
		try{
			System.out.println("cipherName-11773" + javax.crypto.Cipher.getInstance(cipherName11773).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        String bindingAddress = getBindingAddress();
        if (!PortUtil.isPortAvailable(bindingAddress, getPort()))
        {
            String cipherName11774 =  "DES";
			try{
				System.out.println("cipherName-11774" + javax.crypto.Cipher.getInstance(cipherName11774).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException(String.format("Cannot bind to port %d and binding address '%s'. Port is already is use.",
                    getPort(), bindingAddress == null || "".equals(bindingAddress) ? "*" : bindingAddress));
        }
    }

    @Override
    public void onValidate()
    {
        super.onValidate();
		String cipherName11775 =  "DES";
		try{
			System.out.println("cipherName-11775" + javax.crypto.Cipher.getInstance(cipherName11775).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        boolean useTLSTransport = isUsingTLSTransport();

        if(useTLSTransport && getKeyStore() == null)
        {
            String cipherName11776 =  "DES";
			try{
				System.out.println("cipherName-11776" + javax.crypto.Cipher.getInstance(cipherName11776).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Can't create a port which uses a secure transport but has no KeyStore");
        }

        if(!isDurable())
        {
            String cipherName11777 =  "DES";
			try{
				System.out.println("cipherName-11777" + javax.crypto.Cipher.getInstance(cipherName11777).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException(getClass().getSimpleName() + " must be durable");
        }

        if (getPort() != 0)
        {
            String cipherName11778 =  "DES";
			try{
				System.out.println("cipherName-11778" + javax.crypto.Cipher.getInstance(cipherName11778).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (Port p : _container.getChildren(Port.class))
            {
                String cipherName11779 =  "DES";
				try{
					System.out.println("cipherName-11779" + javax.crypto.Cipher.getInstance(cipherName11779).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (p != this && (p.getPort() == getPort() || p.getBoundPort() == getPort()))
                {
                    String cipherName11780 =  "DES";
					try{
						System.out.println("cipherName-11780" + javax.crypto.Cipher.getInstance(cipherName11780).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalConfigurationException("Can't add port "
                                                            + getName()
                                                            + " because port number "
                                                            + getPort()
                                                            + " is already configured for port "
                                                            + p.getName());
                }
            }
        }

        AuthenticationProvider<?> authenticationProvider = getAuthenticationProvider();
        final Set<Transport> transports = getTransports();
        validateAuthenticationMechanisms(authenticationProvider, transports);

        boolean useClientAuth = getNeedClientAuth() || getWantClientAuth();

        if(useClientAuth && (getTrustStores() == null || getTrustStores().isEmpty()))
        {
            String cipherName11781 =  "DES";
			try{
				System.out.println("cipherName-11781" + javax.crypto.Cipher.getInstance(cipherName11781).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Can't create port which requests SSL client certificates but has no trust stores configured.");
        }

        if(useClientAuth && !useTLSTransport)
        {
            String cipherName11782 =  "DES";
			try{
				System.out.println("cipherName-11782" + javax.crypto.Cipher.getInstance(cipherName11782).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException(
                    "Can't create port which requests SSL client certificates but doesn't use SSL transport.");
        }

        if(useClientAuth && getClientCertRecorder() != null)
        {
            String cipherName11783 =  "DES";
			try{
				System.out.println("cipherName-11783" + javax.crypto.Cipher.getInstance(cipherName11783).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!(getClientCertRecorder() instanceof ManagedPeerCertificateTrustStore))
            {
                String cipherName11784 =  "DES";
				try{
					System.out.println("cipherName-11784" + javax.crypto.Cipher.getInstance(cipherName11784).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("Only trust stores of type " + ManagedPeerCertificateTrustStore.TYPE_NAME + " may be used as the client certificate recorder");
            }
        }
    }

    private void validateAuthenticationMechanisms(final AuthenticationProvider<?> authenticationProvider,
                                                  final Set<Transport> transports)
    {
        String cipherName11785 =  "DES";
		try{
			System.out.println("cipherName-11785" + javax.crypto.Cipher.getInstance(cipherName11785).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<String> availableMechanisms = new ArrayList<>(authenticationProvider.getMechanisms());
        if(authenticationProvider.getDisabledMechanisms() != null)
        {
            String cipherName11786 =  "DES";
			try{
				System.out.println("cipherName-11786" + javax.crypto.Cipher.getInstance(cipherName11786).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			availableMechanisms.removeAll(authenticationProvider.getDisabledMechanisms());
        }
        if (availableMechanisms.isEmpty())
        {
            String cipherName11787 =  "DES";
			try{
				System.out.println("cipherName-11787" + javax.crypto.Cipher.getInstance(cipherName11787).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("The authentication provider '"
                                                    + authenticationProvider.getName()
                                                    + "' on port '"
                                                    + getName()
                                                    + "' has all authentication mechanisms disabled.");
        }
        if (hasNonTLSTransport(transports) && authenticationProvider.getSecureOnlyMechanisms() != null)
        {
            String cipherName11788 =  "DES";
			try{
				System.out.println("cipherName-11788" + javax.crypto.Cipher.getInstance(cipherName11788).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			availableMechanisms.removeAll(authenticationProvider.getSecureOnlyMechanisms());
            if(availableMechanisms.isEmpty())
            {
                String cipherName11789 =  "DES";
				try{
					System.out.println("cipherName-11789" + javax.crypto.Cipher.getInstance(cipherName11789).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("The port '"
                                                        + getName()
                                                        + "' allows for non TLS connections, but all authentication "
                                                        + "mechanisms of the authentication provider '"
                                                        + authenticationProvider.getName()
                                                        + "' are disabled on non-secure connections.");
            }
        }
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider()
    {
        String cipherName11790 =  "DES";
		try{
			System.out.println("cipherName-11790" + javax.crypto.Cipher.getInstance(cipherName11790).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SystemConfig<?> systemConfig = getAncestor(SystemConfig.class);
        if(systemConfig.isManagementMode())
        {
            String cipherName11791 =  "DES";
			try{
				System.out.println("cipherName-11791" + javax.crypto.Cipher.getInstance(cipherName11791).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _container.getManagementModeAuthenticationProvider();
        }
        return _authenticationProvider;
    }

    @Override
    public boolean isAllowConfidentialOperationsOnInsecureChannels()
    {
        String cipherName11792 =  "DES";
		try{
			System.out.println("cipherName-11792" + javax.crypto.Cipher.getInstance(cipherName11792).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _allowConfidentialOperationsOnInsecureChannels;
    }

    private boolean isUsingTLSTransport()
    {
        String cipherName11793 =  "DES";
		try{
			System.out.println("cipherName-11793" + javax.crypto.Cipher.getInstance(cipherName11793).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isUsingTLSTransport(getTransports());
    }

    private boolean isUsingTLSTransport(final Collection<Transport> transports)
    {
        String cipherName11794 =  "DES";
		try{
			System.out.println("cipherName-11794" + javax.crypto.Cipher.getInstance(cipherName11794).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hasTransportOfType(transports, true);
    }

    private boolean hasNonTLSTransport(final Collection<Transport> transports)
    {
        String cipherName11795 =  "DES";
		try{
			System.out.println("cipherName-11795" + javax.crypto.Cipher.getInstance(cipherName11795).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hasTransportOfType(transports, false);
    }

    private boolean hasTransportOfType(Collection<Transport> transports, boolean secure)
    {

        String cipherName11796 =  "DES";
		try{
			System.out.println("cipherName-11796" + javax.crypto.Cipher.getInstance(cipherName11796).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean hasTransport = false;
        if(transports != null)
        {
            String cipherName11797 =  "DES";
			try{
				System.out.println("cipherName-11797" + javax.crypto.Cipher.getInstance(cipherName11797).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (Transport transport : transports)
            {
                String cipherName11798 =  "DES";
				try{
					System.out.println("cipherName-11798" + javax.crypto.Cipher.getInstance(cipherName11798).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (secure == transport.isSecure())
                {
                    String cipherName11799 =  "DES";
					try{
						System.out.println("cipherName-11799" + javax.crypto.Cipher.getInstance(cipherName11799).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hasTransport = true;
                    break;
                }
            }
        }
        return hasTransport;
    }

    @Override
    protected void validateChange(final ConfiguredObject<?> proxyForValidation, final Set<String> changedAttributes)
    {
        super.validateChange(proxyForValidation, changedAttributes);
		String cipherName11800 =  "DES";
		try{
			System.out.println("cipherName-11800" + javax.crypto.Cipher.getInstance(cipherName11800).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Port<?> updated = (Port<?>)proxyForValidation;


        if(!getName().equals(updated.getName()))
        {
            String cipherName11801 =  "DES";
			try{
				System.out.println("cipherName-11801" + javax.crypto.Cipher.getInstance(cipherName11801).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Changing the port name is not allowed");
        }

        if(changedAttributes.contains(PORT))
        {
            String cipherName11802 =  "DES";
			try{
				System.out.println("cipherName-11802" + javax.crypto.Cipher.getInstance(cipherName11802).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int newPort = updated.getPort();
            if (getPort() != newPort && newPort != 0)
            {
                String cipherName11803 =  "DES";
				try{
					System.out.println("cipherName-11803" + javax.crypto.Cipher.getInstance(cipherName11803).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (Port p : _container.getChildren(Port.class))
                {
                    String cipherName11804 =  "DES";
					try{
						System.out.println("cipherName-11804" + javax.crypto.Cipher.getInstance(cipherName11804).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (p.getBoundPort() == newPort || p.getPort() == newPort)
                    {
                        String cipherName11805 =  "DES";
						try{
							System.out.println("cipherName-11805" + javax.crypto.Cipher.getInstance(cipherName11805).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new IllegalConfigurationException("Port number "
                                                                + newPort
                                                                + " is already in use by port "
                                                                + p.getName());
                    }
                }
            }
        }


        Collection<Transport> transports = updated.getTransports();

        Collection<Protocol> protocols = updated.getProtocols();


        boolean usesSsl = isUsingTLSTransport(transports);
        if (usesSsl)
        {
            String cipherName11806 =  "DES";
			try{
				System.out.println("cipherName-11806" + javax.crypto.Cipher.getInstance(cipherName11806).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (updated.getKeyStore() == null)
            {
                String cipherName11807 =  "DES";
				try{
					System.out.println("cipherName-11807" + javax.crypto.Cipher.getInstance(cipherName11807).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("Can't create port which requires SSL but has no key store configured.");
            }
        }

        if(changedAttributes.contains(Port.AUTHENTICATION_PROVIDER) || changedAttributes.contains(Port.TRANSPORTS))
        {
            String cipherName11808 =  "DES";
			try{
				System.out.println("cipherName-11808" + javax.crypto.Cipher.getInstance(cipherName11808).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			validateAuthenticationMechanisms(updated.getAuthenticationProvider(), updated.getTransports());
        }

        boolean requiresCertificate = updated.getNeedClientAuth() || updated.getWantClientAuth();

        if (usesSsl)
        {
            String cipherName11809 =  "DES";
			try{
				System.out.println("cipherName-11809" + javax.crypto.Cipher.getInstance(cipherName11809).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if ((updated.getTrustStores() == null || updated.getTrustStores().isEmpty() ) && requiresCertificate)
            {
                String cipherName11810 =  "DES";
				try{
					System.out.println("cipherName-11810" + javax.crypto.Cipher.getInstance(cipherName11810).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("Can't create port which requests SSL client certificates but has no trust store configured.");
            }
        }
        else
        {
            String cipherName11811 =  "DES";
			try{
				System.out.println("cipherName-11811" + javax.crypto.Cipher.getInstance(cipherName11811).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (requiresCertificate)
            {
                String cipherName11812 =  "DES";
				try{
					System.out.println("cipherName-11812" + javax.crypto.Cipher.getInstance(cipherName11812).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("Can't create port which requests SSL client certificates but doesn't use SSL transport.");
            }
        }


        if(requiresCertificate && updated.getClientCertRecorder() != null)
        {
            String cipherName11813 =  "DES";
			try{
				System.out.println("cipherName-11813" + javax.crypto.Cipher.getInstance(cipherName11813).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!(updated.getClientCertRecorder() instanceof ManagedPeerCertificateTrustStore))
            {
                String cipherName11814 =  "DES";
				try{
					System.out.println("cipherName-11814" + javax.crypto.Cipher.getInstance(cipherName11814).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("Only trust stores of type " + ManagedPeerCertificateTrustStore.TYPE_NAME + " may be used as the client certificate recorder");
            }
        }
    }

    @Override
    public int getPort()
    {
        String cipherName11815 =  "DES";
		try{
			System.out.println("cipherName-11815" + javax.crypto.Cipher.getInstance(cipherName11815).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _port;
    }

    @Override
    public Set<Transport> getTransports()
    {
        String cipherName11816 =  "DES";
		try{
			System.out.println("cipherName-11816" + javax.crypto.Cipher.getInstance(cipherName11816).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _transports;
    }

    @Override
    public Set<Protocol> getProtocols()
    {
        String cipherName11817 =  "DES";
		try{
			System.out.println("cipherName-11817" + javax.crypto.Cipher.getInstance(cipherName11817).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _protocols;
    }

    @Override
    public Collection<Connection> getConnections()
    {
        String cipherName11818 =  "DES";
		try{
			System.out.println("cipherName-11818" + javax.crypto.Cipher.getInstance(cipherName11818).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getChildren(Connection.class);
    }

    @Override
    protected ListenableFuture<Void> onDelete()
    {
        String cipherName11819 =  "DES";
		try{
			System.out.println("cipherName-11819" + javax.crypto.Cipher.getInstance(cipherName11819).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_eventLogger.message(PortMessages.DELETE(getType(), getName()));
        return super.onDelete();
    }

    @StateTransition( currentState = {State.UNINITIALIZED, State.QUIESCED, State.ERRORED}, desiredState = State.ACTIVE )
    protected ListenableFuture<Void> activate()
    {
        String cipherName11820 =  "DES";
		try{
			System.out.println("cipherName-11820" + javax.crypto.Cipher.getInstance(cipherName11820).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName11821 =  "DES";
			try{
				System.out.println("cipherName-11821" + javax.crypto.Cipher.getInstance(cipherName11821).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setState(onActivate());
        }
        catch (RuntimeException e)
        {
            String cipherName11822 =  "DES";
			try{
				System.out.println("cipherName-11822" + javax.crypto.Cipher.getInstance(cipherName11822).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setState(State.ERRORED);
            throw new IllegalConfigurationException("Unable to active port '" + getName() + "'of type " + getType() + " on " + getPort(), e);
        }
        return Futures.immediateFuture(null);
    }

    @StateTransition( currentState = State.UNINITIALIZED, desiredState = State.QUIESCED)
    private ListenableFuture<Void> startQuiesced()
    {
        String cipherName11823 =  "DES";
		try{
			System.out.println("cipherName-11823" + javax.crypto.Cipher.getInstance(cipherName11823).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setState(State.QUIESCED);
        return Futures.immediateFuture(null);
    }


    @Override
    public NamedAddressSpace getAddressSpace(String name)
    {
        String cipherName11824 =  "DES";
		try{
			System.out.println("cipherName-11824" + javax.crypto.Cipher.getInstance(cipherName11824).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collection<VirtualHostAlias> aliases = new TreeSet<>(VirtualHostAlias.COMPARATOR);

        aliases.addAll(getChildren(VirtualHostAlias.class));

        for(VirtualHostAlias alias : aliases)
        {
            String cipherName11825 =  "DES";
			try{
				System.out.println("cipherName-11825" + javax.crypto.Cipher.getInstance(cipherName11825).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			NamedAddressSpace addressSpace = alias.getAddressSpace(name);
            if (addressSpace != null)
            {
                String cipherName11826 =  "DES";
				try{
					System.out.println("cipherName-11826" + javax.crypto.Cipher.getInstance(cipherName11826).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return addressSpace;
            }
        }
        return null;
    }

    protected State onActivate()
    {
        String cipherName11827 =  "DES";
		try{
			System.out.println("cipherName-11827" + javax.crypto.Cipher.getInstance(cipherName11827).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// no-op: expected to be overridden by subclass
        return State.ACTIVE;
    }

    @Override
    public List<String> getTlsProtocolWhiteList()
    {
        String cipherName11828 =  "DES";
		try{
			System.out.println("cipherName-11828" + javax.crypto.Cipher.getInstance(cipherName11828).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsProtocolWhiteList;
    }

    @Override
    public List<String> getTlsProtocolBlackList()
    {
        String cipherName11829 =  "DES";
		try{
			System.out.println("cipherName-11829" + javax.crypto.Cipher.getInstance(cipherName11829).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsProtocolBlackList;
    }

    @Override
    public List<String> getTlsCipherSuiteWhiteList()
    {
        String cipherName11830 =  "DES";
		try{
			System.out.println("cipherName-11830" + javax.crypto.Cipher.getInstance(cipherName11830).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsCipherSuiteWhiteList;
    }

    @Override
    public List<String> getTlsCipherSuiteBlackList()
    {
        String cipherName11831 =  "DES";
		try{
			System.out.println("cipherName-11831" + javax.crypto.Cipher.getInstance(cipherName11831).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsCipherSuiteBlackList;
    }

    @Override
    public KeyStore getKeyStore()
    {
        String cipherName11832 =  "DES";
		try{
			System.out.println("cipherName-11832" + javax.crypto.Cipher.getInstance(cipherName11832).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _keyStore;
    }

    @Override
    public Collection<TrustStore> getTrustStores()
    {
        String cipherName11833 =  "DES";
		try{
			System.out.println("cipherName-11833" + javax.crypto.Cipher.getInstance(cipherName11833).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _trustStores;
    }

    @Override
    public boolean getNeedClientAuth()
    {
        String cipherName11834 =  "DES";
		try{
			System.out.println("cipherName-11834" + javax.crypto.Cipher.getInstance(cipherName11834).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _needClientAuth;
    }

    @Override
    public TrustStore<?> getClientCertRecorder()
    {
        String cipherName11835 =  "DES";
		try{
			System.out.println("cipherName-11835" + javax.crypto.Cipher.getInstance(cipherName11835).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _clientCertRecorder;
    }

    @Override
    public boolean getWantClientAuth()
    {
        String cipherName11836 =  "DES";
		try{
			System.out.println("cipherName-11836" + javax.crypto.Cipher.getInstance(cipherName11836).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _wantClientAuth;
    }

    @Override
    public SubjectCreator getSubjectCreator(boolean secure, String host)
    {
        String cipherName11837 =  "DES";
		try{
			System.out.println("cipherName-11837" + javax.crypto.Cipher.getInstance(cipherName11837).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collection children = _container.getChildren(GroupProvider.class);
        NamedAddressSpace addressSpace;
        if(host != null)
        {
            String cipherName11838 =  "DES";
			try{
				System.out.println("cipherName-11838" + javax.crypto.Cipher.getInstance(cipherName11838).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addressSpace = getAddressSpace(host);
        }
        else
        {
            String cipherName11839 =  "DES";
			try{
				System.out.println("cipherName-11839" + javax.crypto.Cipher.getInstance(cipherName11839).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addressSpace = null;
        }
        return new SubjectCreator(getAuthenticationProvider(), children, addressSpace);
    }

    @Override
    protected void logOperation(final String operation)
    {
        String cipherName11840 =  "DES";
		try{
			System.out.println("cipherName-11840" + javax.crypto.Cipher.getInstance(cipherName11840).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_eventLogger.message(PortMessages.OPERATION(operation));
    }

    @Override
    public String toString()
    {
        String cipherName11841 =  "DES";
		try{
			System.out.println("cipherName-11841" + javax.crypto.Cipher.getInstance(cipherName11841).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getCategoryClass().getSimpleName() + "[id=" + getId() + ", name=" + getName() + ", type=" + getType() +  ", port=" + getPort() + "]";
    }

    @Override
    public boolean isTlsSupported()
    {
        String cipherName11842 =  "DES";
		try{
			System.out.println("cipherName-11842" + javax.crypto.Cipher.getInstance(cipherName11842).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getSSLContext() != null;
    }

    @Override
    public boolean updateTLS()
    {
        String cipherName11843 =  "DES";
		try{
			System.out.println("cipherName-11843" + javax.crypto.Cipher.getInstance(cipherName11843).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isTlsSupported())
        {
            String cipherName11844 =  "DES";
			try{
				System.out.println("cipherName-11844" + javax.crypto.Cipher.getInstance(cipherName11844).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return updateSSLContext();
        }
        return false;
    }

    protected abstract boolean updateSSLContext();

}
