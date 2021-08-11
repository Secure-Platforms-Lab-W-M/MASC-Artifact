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

import static java.util.Collections.disjoint;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

import java.security.GeneralSecurityException;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.CommonProperties;
import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.Container;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.NamedAddressSpace;
import org.apache.qpid.server.model.TrustStore;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.AuthenticationResult.AuthenticationStatus;
import org.apache.qpid.server.security.auth.UsernamePrincipal;
import org.apache.qpid.server.security.auth.manager.ldap.AbstractLDAPSSLSocketFactory;
import org.apache.qpid.server.security.auth.manager.ldap.LDAPSSLSocketFactoryGenerator;
import org.apache.qpid.server.security.auth.sasl.SaslNegotiator;
import org.apache.qpid.server.security.auth.sasl.SaslSettings;
import org.apache.qpid.server.security.auth.sasl.plain.PlainNegotiator;
import org.apache.qpid.server.security.group.GroupPrincipal;
import org.apache.qpid.server.transport.network.security.ssl.SSLUtil;
import org.apache.qpid.server.util.CipherSuiteAndProtocolRestrictingSSLSocketFactory;
import org.apache.qpid.server.util.ParameterizedTypes;
import org.apache.qpid.server.util.ServerScopedRuntimeException;
import org.apache.qpid.server.util.StringUtil;

public class SimpleLDAPAuthenticationManagerImpl
        extends AbstractAuthenticationManager<SimpleLDAPAuthenticationManagerImpl>
        implements SimpleLDAPAuthenticationManager<SimpleLDAPAuthenticationManagerImpl>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLDAPAuthenticationManagerImpl.class);

    private static final List<String> CONNECTIVITY_ATTRS = unmodifiableList(Arrays.asList(PROVIDER_URL,
                                                                                          PROVIDER_AUTH_URL,
                                                                                          SEARCH_CONTEXT,
                                                                                          LDAP_CONTEXT_FACTORY,
                                                                                          SEARCH_USERNAME,
                                                                                          SEARCH_PASSWORD,
                                                                                          TRUST_STORE,
                                                                                          LOGIN_CONFIG_SCOPE,
                                                                                          AUTHENTICATION_METHOD));

    /**
     * Environment key to instruct {@link InitialDirContext} to override the socket factory.
     */
    private static final String JAVA_NAMING_LDAP_FACTORY_SOCKET = "java.naming.ldap.factory.socket";

    @ManagedAttributeField
    private String _providerUrl;
    @ManagedAttributeField
    private String _providerAuthUrl;
    @ManagedAttributeField
    private String _searchContext;
    @ManagedAttributeField
    private String _searchFilter;
    @ManagedAttributeField
    private String _ldapContextFactory;


    /**
     * Trust store - typically used when the Directory has been secured with a certificate signed by a
     * private CA (or self-signed certificate).
     */
    @ManagedAttributeField
    private TrustStore _trustStore;

    @ManagedAttributeField
    private boolean _bindWithoutSearch;

    @ManagedAttributeField
    private String _searchUsername;
    @ManagedAttributeField
    private String _searchPassword;

    @ManagedAttributeField
    private String _groupAttributeName;

    @ManagedAttributeField
    private String _groupSearchContext;

    @ManagedAttributeField
    private String _groupSearchFilter;

    @ManagedAttributeField
    private boolean _groupSubtreeSearchScope;

    @ManagedAttributeField
    private LdapAuthenticationMethod _authenticationMethod;

    @ManagedAttributeField
    private String _loginConfigScope;

    private List<String> _tlsProtocolWhiteList;
    private List<String>  _tlsProtocolBlackList;

    private List<String> _tlsCipherSuiteWhiteList;
    private List<String> _tlsCipherSuiteBlackList;

    private AuthenticationResultCacher _authenticationResultCacher;

    /**
     * Dynamically created SSL Socket Factory implementation.
     */
    private Class<? extends SocketFactory> _sslSocketFactoryOverrideClass;

    @ManagedObjectFactoryConstructor
    protected SimpleLDAPAuthenticationManagerImpl(final Map<String, Object> attributes, final Container<?> container)
    {
        super(attributes, container);
		String cipherName7795 =  "DES";
		try{
			System.out.println("cipherName-7795" + javax.crypto.Cipher.getInstance(cipherName7795).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    protected void validateOnCreate()
    {
        super.validateOnCreate();
		String cipherName7796 =  "DES";
		try{
			System.out.println("cipherName-7796" + javax.crypto.Cipher.getInstance(cipherName7796).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        validateInitialDirContext(this);
    }

    @Override
    protected void validateChange(final ConfiguredObject<?> proxyForValidation, final Set<String> changedAttributes)
    {
        super.validateChange(proxyForValidation, changedAttributes);
		String cipherName7797 =  "DES";
		try{
			System.out.println("cipherName-7797" + javax.crypto.Cipher.getInstance(cipherName7797).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if (!disjoint(changedAttributes, CONNECTIVITY_ATTRS))
        {
            String cipherName7798 =  "DES";
			try{
				System.out.println("cipherName-7798" + javax.crypto.Cipher.getInstance(cipherName7798).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SimpleLDAPAuthenticationManager changed = (SimpleLDAPAuthenticationManager) proxyForValidation;
            validateInitialDirContext(changed);
        }
    }

    @Override
    protected void onOpen()
    {
        super.onOpen();
		String cipherName7799 =  "DES";
		try{
			System.out.println("cipherName-7799" + javax.crypto.Cipher.getInstance(cipherName7799).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        _tlsProtocolWhiteList = getContextValue(List.class, ParameterizedTypes.LIST_OF_STRINGS, CommonProperties.QPID_SECURITY_TLS_PROTOCOL_WHITE_LIST);
        _tlsProtocolBlackList = getContextValue(List.class, ParameterizedTypes.LIST_OF_STRINGS, CommonProperties.QPID_SECURITY_TLS_PROTOCOL_BLACK_LIST);
        _tlsCipherSuiteWhiteList = getContextValue(List.class, ParameterizedTypes.LIST_OF_STRINGS, CommonProperties.QPID_SECURITY_TLS_CIPHER_SUITE_WHITE_LIST);
        _tlsCipherSuiteBlackList = getContextValue(List.class, ParameterizedTypes.LIST_OF_STRINGS, CommonProperties.QPID_SECURITY_TLS_CIPHER_SUITE_BLACK_LIST);

        Integer cacheMaxSize = getContextValue(Integer.class, AUTHENTICATION_CACHE_MAX_SIZE);
        Long cacheExpirationTime = getContextValue(Long.class, AUTHENTICATION_CACHE_EXPIRATION_TIME);
        Integer cacheIterationCount = getContextValue(Integer.class, AUTHENTICATION_CACHE_ITERATION_COUNT);
        if (cacheMaxSize == null || cacheMaxSize <= 0
            || cacheExpirationTime == null || cacheExpirationTime <= 0
            || cacheIterationCount == null || cacheIterationCount < 0)
        {
            String cipherName7800 =  "DES";
			try{
				System.out.println("cipherName-7800" + javax.crypto.Cipher.getInstance(cipherName7800).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.debug("disabling authentication result caching");
            cacheMaxSize = 0;
            cacheExpirationTime = 1L;
            cacheIterationCount = 0;
        }
        _authenticationResultCacher = new AuthenticationResultCacher(cacheMaxSize, cacheExpirationTime, cacheIterationCount);
    }

    @Override
    protected ListenableFuture<Void> activate()
    {
        String cipherName7801 =  "DES";
		try{
			System.out.println("cipherName-7801" + javax.crypto.Cipher.getInstance(cipherName7801).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_sslSocketFactoryOverrideClass = createSslSocketFactoryOverrideClass(_trustStore);
        return super.activate();
    }

    @Override
    public String getProviderUrl()
    {
        String cipherName7802 =  "DES";
		try{
			System.out.println("cipherName-7802" + javax.crypto.Cipher.getInstance(cipherName7802).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _providerUrl;
    }

    @Override
    public String getProviderAuthUrl()
    {
        String cipherName7803 =  "DES";
		try{
			System.out.println("cipherName-7803" + javax.crypto.Cipher.getInstance(cipherName7803).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _providerAuthUrl;
    }

    @Override
    public String getSearchContext()
    {
        String cipherName7804 =  "DES";
		try{
			System.out.println("cipherName-7804" + javax.crypto.Cipher.getInstance(cipherName7804).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _searchContext;
    }

    @Override
    public String getSearchFilter()
    {
        String cipherName7805 =  "DES";
		try{
			System.out.println("cipherName-7805" + javax.crypto.Cipher.getInstance(cipherName7805).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _searchFilter;
    }

    @Override
    public String getLdapContextFactory()
    {
        String cipherName7806 =  "DES";
		try{
			System.out.println("cipherName-7806" + javax.crypto.Cipher.getInstance(cipherName7806).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _ldapContextFactory;
    }

    @Override
    public TrustStore getTrustStore()
    {
        String cipherName7807 =  "DES";
		try{
			System.out.println("cipherName-7807" + javax.crypto.Cipher.getInstance(cipherName7807).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _trustStore;
    }

    @Override
    public String getSearchUsername()
    {
        String cipherName7808 =  "DES";
		try{
			System.out.println("cipherName-7808" + javax.crypto.Cipher.getInstance(cipherName7808).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _searchUsername;
    }

    @Override
    public String getSearchPassword()
    {
        String cipherName7809 =  "DES";
		try{
			System.out.println("cipherName-7809" + javax.crypto.Cipher.getInstance(cipherName7809).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _searchPassword;
    }

    @Override
    public String getGroupAttributeName()
    {
        String cipherName7810 =  "DES";
		try{
			System.out.println("cipherName-7810" + javax.crypto.Cipher.getInstance(cipherName7810).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _groupAttributeName;
    }

    @Override
    public String getGroupSearchContext()
    {
        String cipherName7811 =  "DES";
		try{
			System.out.println("cipherName-7811" + javax.crypto.Cipher.getInstance(cipherName7811).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _groupSearchContext;
    }

    @Override
    public String getGroupSearchFilter()
    {
        String cipherName7812 =  "DES";
		try{
			System.out.println("cipherName-7812" + javax.crypto.Cipher.getInstance(cipherName7812).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _groupSearchFilter;
    }

    @Override
    public boolean isGroupSubtreeSearchScope()
    {
        String cipherName7813 =  "DES";
		try{
			System.out.println("cipherName-7813" + javax.crypto.Cipher.getInstance(cipherName7813).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _groupSubtreeSearchScope;
    }

    @Override
    public LdapAuthenticationMethod getAuthenticationMethod()
    {
        String cipherName7814 =  "DES";
		try{
			System.out.println("cipherName-7814" + javax.crypto.Cipher.getInstance(cipherName7814).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _authenticationMethod;
    }

    @Override
    public String getLoginConfigScope()
    {
        String cipherName7815 =  "DES";
		try{
			System.out.println("cipherName-7815" + javax.crypto.Cipher.getInstance(cipherName7815).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _loginConfigScope;
    }

    @Override
    public List<String> getMechanisms()
    {
        String cipherName7816 =  "DES";
		try{
			System.out.println("cipherName-7816" + javax.crypto.Cipher.getInstance(cipherName7816).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return singletonList(PlainNegotiator.MECHANISM);
    }

    @Override
    public SaslNegotiator createSaslNegotiator(final String mechanism,
                                               final SaslSettings saslSettings,
                                               final NamedAddressSpace addressSpace)
    {
        String cipherName7817 =  "DES";
		try{
			System.out.println("cipherName-7817" + javax.crypto.Cipher.getInstance(cipherName7817).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(PlainNegotiator.MECHANISM.equals(mechanism))
        {
            String cipherName7818 =  "DES";
			try{
				System.out.println("cipherName-7818" + javax.crypto.Cipher.getInstance(cipherName7818).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new PlainNegotiator(this);
        }
        else
        {
            String cipherName7819 =  "DES";
			try{
				System.out.println("cipherName-7819" + javax.crypto.Cipher.getInstance(cipherName7819).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
    }

    @Override
    public AuthenticationResult authenticate(String username, String password)
    {
        String cipherName7820 =  "DES";
		try{
			System.out.println("cipherName-7820" + javax.crypto.Cipher.getInstance(cipherName7820).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getOrLoadAuthenticationResult(username, password);
    }

    private AuthenticationResult getOrLoadAuthenticationResult(final String userId, final String password)
    {
        String cipherName7821 =  "DES";
		try{
			System.out.println("cipherName-7821" + javax.crypto.Cipher.getInstance(cipherName7821).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _authenticationResultCacher.getOrLoad(new String[]{userId, password}, new Callable<AuthenticationResult>()
        {
            @Override
            public AuthenticationResult call()
            {
                String cipherName7822 =  "DES";
				try{
					System.out.println("cipherName-7822" + javax.crypto.Cipher.getInstance(cipherName7822).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return doLDAPNameAuthentication(userId, password);
            }
        });
    }

    private AuthenticationResult doLDAPNameAuthentication(String userId, String password)
    {
        String cipherName7823 =  "DES";
		try{
			System.out.println("cipherName-7823" + javax.crypto.Cipher.getInstance(cipherName7823).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Subject gssapiIdentity = null;
        if (LdapAuthenticationMethod.GSSAPI.equals(getAuthenticationMethod()))
        {
            String cipherName7824 =  "DES";
			try{
				System.out.println("cipherName-7824" + javax.crypto.Cipher.getInstance(cipherName7824).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName7825 =  "DES";
				try{
					System.out.println("cipherName-7825" + javax.crypto.Cipher.getInstance(cipherName7825).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				gssapiIdentity = doGssApiLogin(getLoginConfigScope());
            }
            catch (LoginException e)
            {
                String cipherName7826 =  "DES";
				try{
					System.out.println("cipherName-7826" + javax.crypto.Cipher.getInstance(cipherName7826).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("JAAS Login failed", e);
                return new AuthenticationResult(AuthenticationResult.AuthenticationStatus.ERROR, e);
            }
        }

        final String name;
        try
        {
            String cipherName7827 =  "DES";
			try{
				System.out.println("cipherName-7827" + javax.crypto.Cipher.getInstance(cipherName7827).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			name = getNameFromId(userId, gssapiIdentity);
        }
        catch (NamingException e)
        {
            String cipherName7828 =  "DES";
			try{
				System.out.println("cipherName-7828" + javax.crypto.Cipher.getInstance(cipherName7828).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("Retrieving LDAP name for user '{}' resulted in error.", userId, e);
            return new AuthenticationResult(AuthenticationResult.AuthenticationStatus.ERROR, e);
        }

        if(name == null)
        {
            String cipherName7829 =  "DES";
			try{
				System.out.println("cipherName-7829" + javax.crypto.Cipher.getInstance(cipherName7829).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//The search didn't return anything, class as not-authenticated before it NPEs below
            return new AuthenticationResult(AuthenticationStatus.ERROR);
        }

        String providerAuthUrl = isSpecified(getProviderAuthUrl()) ? getProviderAuthUrl() : getProviderUrl();
        Hashtable<String, Object> env = createInitialDirContextEnvironment(providerAuthUrl);

        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, name);
        env.put(Context.SECURITY_CREDENTIALS, password);

        InitialDirContext ctx = null;
        try
        {
            String cipherName7830 =  "DES";
			try{
				System.out.println("cipherName-7830" + javax.crypto.Cipher.getInstance(cipherName7830).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ctx = createInitialDirContext(env, _sslSocketFactoryOverrideClass, gssapiIdentity);

            Set<Principal> groups = Collections.emptySet();
            if (isGroupSearchRequired())
            {
                String cipherName7831 =  "DES";
				try{
					System.out.println("cipherName-7831" + javax.crypto.Cipher.getInstance(cipherName7831).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (!providerAuthUrl.equals(getProviderUrl()))
                {
                    String cipherName7832 =  "DES";
					try{
						System.out.println("cipherName-7832" + javax.crypto.Cipher.getInstance(cipherName7832).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					closeSafely(ctx);
                    ctx = createSearchInitialDirContext(gssapiIdentity);
                }
                groups = findGroups(ctx, name, gssapiIdentity);
            }

            //Authentication succeeded
            return new AuthenticationResult(new UsernamePrincipal(name, this), groups, null);
        }
        catch(AuthenticationException ae)
        {
            String cipherName7833 =  "DES";
			try{
				System.out.println("cipherName-7833" + javax.crypto.Cipher.getInstance(cipherName7833).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//Authentication failed
            return new AuthenticationResult(AuthenticationStatus.ERROR);
        }
        catch (NamingException e)
        {
            String cipherName7834 =  "DES";
			try{
				System.out.println("cipherName-7834" + javax.crypto.Cipher.getInstance(cipherName7834).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//Some other failure
            LOGGER.warn("LDAP authentication attempt for username '{}' resulted in error.", name, e);
            return new AuthenticationResult(AuthenticationResult.AuthenticationStatus.ERROR, e);
        }
        finally
        {
            String cipherName7835 =  "DES";
			try{
				System.out.println("cipherName-7835" + javax.crypto.Cipher.getInstance(cipherName7835).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(ctx != null)
            {
                String cipherName7836 =  "DES";
				try{
					System.out.println("cipherName-7836" + javax.crypto.Cipher.getInstance(cipherName7836).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				closeSafely(ctx);
            }
        }
    }

    private boolean isGroupSearchRequired()
    {
        String cipherName7837 =  "DES";
		try{
			System.out.println("cipherName-7837" + javax.crypto.Cipher.getInstance(cipherName7837).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isSpecified(getGroupAttributeName()))
        {
            String cipherName7838 =  "DES";
			try{
				System.out.println("cipherName-7838" + javax.crypto.Cipher.getInstance(cipherName7838).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }

        return (isSpecified(getGroupSearchContext()) && isSpecified(getGroupSearchFilter()));
    }

    private boolean isSpecified(String value)
    {
        String cipherName7839 =  "DES";
		try{
			System.out.println("cipherName-7839" + javax.crypto.Cipher.getInstance(cipherName7839).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (value != null && !value.isEmpty());
    }

    private Set<Principal> findGroups(DirContext context, String userDN, final Subject gssapiIdentity)
            throws NamingException
    {
        String cipherName7840 =  "DES";
		try{
			System.out.println("cipherName-7840" + javax.crypto.Cipher.getInstance(cipherName7840).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Set<Principal> groupPrincipals = new HashSet<>();
        if (getGroupAttributeName() != null && !getGroupAttributeName().isEmpty())
        {
            String cipherName7841 =  "DES";
			try{
				System.out.println("cipherName-7841" + javax.crypto.Cipher.getInstance(cipherName7841).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Attributes attributes = context.getAttributes(userDN, new String[]{getGroupAttributeName()});
            NamingEnumeration<? extends Attribute> namingEnum = attributes.getAll();
            while (namingEnum.hasMore())
            {
                String cipherName7842 =  "DES";
				try{
					System.out.println("cipherName-7842" + javax.crypto.Cipher.getInstance(cipherName7842).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Attribute attribute = namingEnum.next();
                if (attribute != null)
                {
                    String cipherName7843 =  "DES";
					try{
						System.out.println("cipherName-7843" + javax.crypto.Cipher.getInstance(cipherName7843).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					NamingEnumeration<?> attributeValues = attribute.getAll();
                    while (attributeValues.hasMore())
                    {
                        String cipherName7844 =  "DES";
						try{
							System.out.println("cipherName-7844" + javax.crypto.Cipher.getInstance(cipherName7844).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Object attributeValue = attributeValues.next();
                        if (attributeValue != null)
                        {
                            String cipherName7845 =  "DES";
							try{
								System.out.println("cipherName-7845" + javax.crypto.Cipher.getInstance(cipherName7845).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							String groupDN = String.valueOf(attributeValue);
                            groupPrincipals.add(new GroupPrincipal(groupDN, this));
                        }
                    }
                }
            }
        }

        if (getGroupSearchContext() != null && !getGroupSearchContext().isEmpty() &&
                getGroupSearchFilter() != null && !getGroupSearchFilter().isEmpty())
        {
            String cipherName7846 =  "DES";
			try{
				System.out.println("cipherName-7846" + javax.crypto.Cipher.getInstance(cipherName7846).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(new String[]{});
            searchControls.setSearchScope(isGroupSubtreeSearchScope()
                                                  ? SearchControls.SUBTREE_SCOPE
                                                  : SearchControls.ONELEVEL_SCOPE);
            PrivilegedExceptionAction<NamingEnumeration<?>> search = () -> context.search(getGroupSearchContext(),
                                                                                          getGroupSearchFilter(),
                                                                                          new String[]{encode(userDN)},
                                                                                          searchControls);
            NamingEnumeration<?> groupEnumeration = invokeContextOperationAs(gssapiIdentity, search);
            while (groupEnumeration.hasMore())
            {
                String cipherName7847 =  "DES";
				try{
					System.out.println("cipherName-7847" + javax.crypto.Cipher.getInstance(cipherName7847).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SearchResult result = (SearchResult) groupEnumeration.next();
                String groupDN = result.getNameInNamespace();
                groupPrincipals.add(new GroupPrincipal(groupDN, this));
            }
        }

        return groupPrincipals;
    }

    private String encode(String value)
    {
        String cipherName7848 =  "DES";
		try{
			System.out.println("cipherName-7848" + javax.crypto.Cipher.getInstance(cipherName7848).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		StringBuilder encoded = new StringBuilder(value.length());
        char[] chars = value.toCharArray();
        for (char ch : chars) {
            String cipherName7849 =  "DES";
			try{
				System.out.println("cipherName-7849" + javax.crypto.Cipher.getInstance(cipherName7849).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (ch) {
                case '\0':
                    encoded.append("\\00");
                    break;
                case '(':
                    encoded.append("\\28");
                    break;
                case ')':
                    encoded.append("\\29");
                    break;
                case '*':
                    encoded.append("\\2a");
                    break;
                case '\\':
                    encoded.append("\\5c");
                    break;
                default:
                    encoded.append(ch);
                    break;
            }
        }
        return encoded.toString();
    }

    private Hashtable<String, Object> createInitialDirContextEnvironment(String providerUrl)
    {
        String cipherName7850 =  "DES";
		try{
			System.out.println("cipherName-7850" + javax.crypto.Cipher.getInstance(cipherName7850).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Hashtable<String,Object> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, _ldapContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        return env;
    }

    private InitialDirContext createInitialDirContext(final Hashtable<String, Object> env,
                                                      final Class<? extends SocketFactory> sslSocketFactoryOverrideClass,
                                                      final Subject gssapiIdentity) throws NamingException
    {
        String cipherName7851 =  "DES";
		try{
			System.out.println("cipherName-7851" + javax.crypto.Cipher.getInstance(cipherName7851).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ClassLoader existingContextClassLoader = null;

        boolean isLdaps = String.valueOf(env.get(Context.PROVIDER_URL)).trim().toLowerCase().startsWith("ldaps:");

        boolean revertContentClassLoader = false;
        try
        {
            String cipherName7852 =  "DES";
			try{
				System.out.println("cipherName-7852" + javax.crypto.Cipher.getInstance(cipherName7852).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (isLdaps)
            {
                String cipherName7853 =  "DES";
				try{
					System.out.println("cipherName-7853" + javax.crypto.Cipher.getInstance(cipherName7853).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				existingContextClassLoader = Thread.currentThread().getContextClassLoader();
                env.put(JAVA_NAMING_LDAP_FACTORY_SOCKET, sslSocketFactoryOverrideClass.getName());
                Thread.currentThread().setContextClassLoader(sslSocketFactoryOverrideClass.getClassLoader());
                revertContentClassLoader = true;
            }
            return invokeContextOperationAs(gssapiIdentity, () -> new InitialDirContext(env));
        }
        finally
        {
            String cipherName7854 =  "DES";
			try{
				System.out.println("cipherName-7854" + javax.crypto.Cipher.getInstance(cipherName7854).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (revertContentClassLoader)
            {
                String cipherName7855 =  "DES";
				try{
					System.out.println("cipherName-7855" + javax.crypto.Cipher.getInstance(cipherName7855).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Thread.currentThread().setContextClassLoader(existingContextClassLoader);
            }
        }
    }

    private Class<? extends SocketFactory> createSslSocketFactoryOverrideClass(final TrustStore trustStore)
    {
        String cipherName7856 =  "DES";
		try{
			System.out.println("cipherName-7856" + javax.crypto.Cipher.getInstance(cipherName7856).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String managerName = String.format("%s_%s_%s", getName(), getId(), trustStore == null ? "none" : trustStore.getName());
        String clazzName = new StringUtil().createUniqueJavaName(managerName);
        SSLContext sslContext = null;
        try
        {
            String cipherName7857 =  "DES";
			try{
				System.out.println("cipherName-7857" + javax.crypto.Cipher.getInstance(cipherName7857).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sslContext = SSLUtil.tryGetSSLContext();
            sslContext.init(null,
                            trustStore == null ? null : trustStore.getTrustManagers(),
                            null);
        }
        catch (GeneralSecurityException e)
        {
            String cipherName7858 =  "DES";
			try{
				System.out.println("cipherName-7858" + javax.crypto.Cipher.getInstance(cipherName7858).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.error("Exception creating SSLContext", e);
            if (trustStore != null)
            {
                String cipherName7859 =  "DES";
				try{
					System.out.println("cipherName-7859" + javax.crypto.Cipher.getInstance(cipherName7859).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("Error creating SSLContext with trust store : " +
                                                        trustStore.getName() , e);
            }
            else
            {
                String cipherName7860 =  "DES";
				try{
					System.out.println("cipherName-7860" + javax.crypto.Cipher.getInstance(cipherName7860).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("Error creating SSLContext (no trust store)", e);
            }
        }

        SSLSocketFactory sslSocketFactory = new CipherSuiteAndProtocolRestrictingSSLSocketFactory(sslContext.getSocketFactory(),
                                                                                                 _tlsCipherSuiteWhiteList,
                                                                                                 _tlsCipherSuiteBlackList,
                                                                                                 _tlsProtocolWhiteList,
                                                                                                 _tlsProtocolBlackList);
        Class<? extends AbstractLDAPSSLSocketFactory> clazz = LDAPSSLSocketFactoryGenerator.createSubClass(clazzName,
                                                                                                           sslSocketFactory);
        LOGGER.debug("Connection to Directory will use custom SSL socket factory : {}",  clazz);
        return clazz;
    }


    @Override
    public String toString()
    {
        String cipherName7861 =  "DES";
		try{
			System.out.println("cipherName-7861" + javax.crypto.Cipher.getInstance(cipherName7861).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "SimpleLDAPAuthenticationManagerImpl [id=" + getId() + ", name=" + getName() +
               ", providerUrl=" + _providerUrl + ", providerAuthUrl=" + _providerAuthUrl +
               ", searchContext=" + _searchContext + ", state=" + getState() +
               ", searchFilter=" + _searchFilter + ", ldapContextFactory=" + _ldapContextFactory +
               ", bindWithoutSearch=" + _bindWithoutSearch + ", trustStore=" + _trustStore +
               ", searchUsername=" + _searchUsername + ", loginConfigScope=" + _loginConfigScope +
               ", authenticationMethod=" + _authenticationMethod + "]";
    }

    private void validateInitialDirContext(final SimpleLDAPAuthenticationManager<?> authenticationProvider)
    {
        String cipherName7862 =  "DES";
		try{
			System.out.println("cipherName-7862" + javax.crypto.Cipher.getInstance(cipherName7862).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final TrustStore truststore = authenticationProvider.getTrustStore();
        final Class<? extends SocketFactory> sslSocketFactoryOverrideClass =
                createSslSocketFactoryOverrideClass(truststore);

        final Hashtable<String, Object> env =
                createInitialDirContextEnvironment(authenticationProvider.getProviderUrl());
        setAuthenticationProperties(env,
                                    authenticationProvider.getSearchUsername(),
                                    authenticationProvider.getSearchPassword(),
                                    authenticationProvider.getAuthenticationMethod());

        InitialDirContext ctx = null;
        try
        {
            String cipherName7863 =  "DES";
			try{
				System.out.println("cipherName-7863" + javax.crypto.Cipher.getInstance(cipherName7863).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Subject gssapiIdentity = null;
            if (LdapAuthenticationMethod.GSSAPI.equals(authenticationProvider.getAuthenticationMethod()))
            {
                String cipherName7864 =  "DES";
				try{
					System.out.println("cipherName-7864" + javax.crypto.Cipher.getInstance(cipherName7864).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				gssapiIdentity = doGssApiLogin(authenticationProvider.getLoginConfigScope());
            }
            ctx = createInitialDirContext(env, sslSocketFactoryOverrideClass, gssapiIdentity);
        }
        catch (NamingException e)
        {
            String cipherName7865 =  "DES";
			try{
				System.out.println("cipherName-7865" + javax.crypto.Cipher.getInstance(cipherName7865).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.debug("Failed to establish connectivity to the ldap server for '{}'",
                         authenticationProvider.getProviderUrl(),
                         e);
            throw new IllegalConfigurationException("Failed to establish connectivity to the ldap server.", e);
        }
        catch (LoginException e)
        {
            String cipherName7866 =  "DES";
			try{
				System.out.println("cipherName-7866" + javax.crypto.Cipher.getInstance(cipherName7866).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.debug("JAAS login failed ", e);
            throw new IllegalConfigurationException("JAAS login failed.", e);
        }
        finally
        {
            String cipherName7867 =  "DES";
			try{
				System.out.println("cipherName-7867" + javax.crypto.Cipher.getInstance(cipherName7867).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			closeSafely(ctx);
        }
    }

    private void setAuthenticationProperties(final Hashtable<String, Object> env,
                                             final String userName,
                                             final String password,
                                             final LdapAuthenticationMethod authenticationMethod)
    {
        String cipherName7868 =  "DES";
		try{
			System.out.println("cipherName-7868" + javax.crypto.Cipher.getInstance(cipherName7868).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (LdapAuthenticationMethod.GSSAPI.equals(authenticationMethod))
        {
            String cipherName7869 =  "DES";
			try{
				System.out.println("cipherName-7869" + javax.crypto.Cipher.getInstance(cipherName7869).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			env.put(Context.SECURITY_AUTHENTICATION, "GSSAPI");
        }
        else if (LdapAuthenticationMethod.SIMPLE.equals(authenticationMethod))
        {
            String cipherName7870 =  "DES";
			try{
				System.out.println("cipherName-7870" + javax.crypto.Cipher.getInstance(cipherName7870).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
            if (userName != null)
            {
                String cipherName7871 =  "DES";
				try{
					System.out.println("cipherName-7871" + javax.crypto.Cipher.getInstance(cipherName7871).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				env.put(Context.SECURITY_PRINCIPAL, userName);
            }
            if (password != null)
            {
                String cipherName7872 =  "DES";
				try{
					System.out.println("cipherName-7872" + javax.crypto.Cipher.getInstance(cipherName7872).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				env.put(Context.SECURITY_CREDENTIALS, password);
            }
        }
        else
        {
            String cipherName7873 =  "DES";
			try{
				System.out.println("cipherName-7873" + javax.crypto.Cipher.getInstance(cipherName7873).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			env.put(Context.SECURITY_AUTHENTICATION, "none");
        }
    }

    private String getNameFromId(final String id, final Subject gssapiIdentity)
            throws NamingException
    {
        String cipherName7874 =  "DES";
		try{
			System.out.println("cipherName-7874" + javax.crypto.Cipher.getInstance(cipherName7874).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!isBindWithoutSearch())
        {
            String cipherName7875 =  "DES";
			try{
				System.out.println("cipherName-7875" + javax.crypto.Cipher.getInstance(cipherName7875).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			InitialDirContext ctx = createSearchInitialDirContext(gssapiIdentity);

            try
            {
                String cipherName7876 =  "DES";
				try{
					System.out.println("cipherName-7876" + javax.crypto.Cipher.getInstance(cipherName7876).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SearchControls searchControls = new SearchControls();
                searchControls.setReturningAttributes(new String[]{});
                searchControls.setCountLimit(1L);
                searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

                LOGGER.debug("Searching for '{}'", id);
                NamingEnumeration<?> namingEnum = invokeContextOperationAs(gssapiIdentity,
                                                      (PrivilegedExceptionAction<NamingEnumeration<?>>) () -> ctx.search(
                                                              _searchContext,
                                                              _searchFilter,
                                                              new String[]{id},
                                                              searchControls));

                if (namingEnum.hasMore())
                {
                    String cipherName7877 =  "DES";
					try{
						System.out.println("cipherName-7877" + javax.crypto.Cipher.getInstance(cipherName7877).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					SearchResult result = (SearchResult) namingEnum.next();
                    String name = result.getNameInNamespace();
                    LOGGER.debug("Found '{}' DN '{}'", id, name);
                    return name;
                }
                else
                {
                    String cipherName7878 =  "DES";
					try{
						System.out.println("cipherName-7878" + javax.crypto.Cipher.getInstance(cipherName7878).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.debug("Not found '{}'", id);
                    return null;
                }
            }
            finally
            {
                String cipherName7879 =  "DES";
				try{
					System.out.println("cipherName-7879" + javax.crypto.Cipher.getInstance(cipherName7879).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				closeSafely(ctx);
            }
        }
        else
        {
            String cipherName7880 =  "DES";
			try{
				System.out.println("cipherName-7880" + javax.crypto.Cipher.getInstance(cipherName7880).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return id;
        }
    }

    private <T> T invokeContextOperationAs(final Subject identity, final PrivilegedExceptionAction<T> action)
            throws NamingException
    {
        String cipherName7881 =  "DES";
		try{
			System.out.println("cipherName-7881" + javax.crypto.Cipher.getInstance(cipherName7881).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7882 =  "DES";
			try{
				System.out.println("cipherName-7882" + javax.crypto.Cipher.getInstance(cipherName7882).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Subject.doAs(identity, action);
        }
        catch (PrivilegedActionException e)
        {
            String cipherName7883 =  "DES";
			try{
				System.out.println("cipherName-7883" + javax.crypto.Cipher.getInstance(cipherName7883).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final Exception exception = e.getException();
            if (exception instanceof NamingException)
            {
                String cipherName7884 =  "DES";
				try{
					System.out.println("cipherName-7884" + javax.crypto.Cipher.getInstance(cipherName7884).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw (NamingException) exception;
            }
            else if (exception instanceof RuntimeException)
            {
                String cipherName7885 =  "DES";
				try{
					System.out.println("cipherName-7885" + javax.crypto.Cipher.getInstance(cipherName7885).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw (RuntimeException) exception;
            }
            else
            {
                String cipherName7886 =  "DES";
				try{
					System.out.println("cipherName-7886" + javax.crypto.Cipher.getInstance(cipherName7886).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ServerScopedRuntimeException(exception);
            }
        }
    }

    private Subject doGssApiLogin(final String configScope) throws LoginException
    {
        String cipherName7887 =  "DES";
		try{
			System.out.println("cipherName-7887" + javax.crypto.Cipher.getInstance(cipherName7887).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LoginContext loginContext = new LoginContext(configScope);
        loginContext.login();
        return loginContext.getSubject();
    }

    private InitialDirContext createSearchInitialDirContext(final Subject gssapiIdentity) throws NamingException
    {
        String cipherName7888 =  "DES";
		try{
			System.out.println("cipherName-7888" + javax.crypto.Cipher.getInstance(cipherName7888).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Hashtable<String, Object> env = createInitialDirContextEnvironment(_providerUrl);
        setAuthenticationProperties(env, _searchUsername, _searchPassword, _authenticationMethod);
        return createInitialDirContext(env, _sslSocketFactoryOverrideClass, gssapiIdentity);
    }


    @Override
    public boolean isBindWithoutSearch()
    {
        String cipherName7889 =  "DES";
		try{
			System.out.println("cipherName-7889" + javax.crypto.Cipher.getInstance(cipherName7889).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _bindWithoutSearch;
    }

    @Override
    public List<String> getTlsProtocolWhiteList()
    {
        String cipherName7890 =  "DES";
		try{
			System.out.println("cipherName-7890" + javax.crypto.Cipher.getInstance(cipherName7890).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsProtocolWhiteList;
    }

    @Override
    public List<String> getTlsProtocolBlackList()
    {
        String cipherName7891 =  "DES";
		try{
			System.out.println("cipherName-7891" + javax.crypto.Cipher.getInstance(cipherName7891).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsProtocolBlackList;
    }

    @Override
    public List<String> getTlsCipherSuiteWhiteList()
    {
        String cipherName7892 =  "DES";
		try{
			System.out.println("cipherName-7892" + javax.crypto.Cipher.getInstance(cipherName7892).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsCipherSuiteWhiteList;
    }

    @Override
    public List<String> getTlsCipherSuiteBlackList()
    {
        String cipherName7893 =  "DES";
		try{
			System.out.println("cipherName-7893" + javax.crypto.Cipher.getInstance(cipherName7893).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _tlsCipherSuiteBlackList;
    }

    private void closeSafely(InitialDirContext ctx)
    {
        String cipherName7894 =  "DES";
		try{
			System.out.println("cipherName-7894" + javax.crypto.Cipher.getInstance(cipherName7894).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7895 =  "DES";
			try{
				System.out.println("cipherName-7895" + javax.crypto.Cipher.getInstance(cipherName7895).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (ctx != null)
            {
                String cipherName7896 =  "DES";
				try{
					System.out.println("cipherName-7896" + javax.crypto.Cipher.getInstance(cipherName7896).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ctx.close();
            }
        }
        catch (Exception e)
        {
            String cipherName7897 =  "DES";
			try{
				System.out.println("cipherName-7897" + javax.crypto.Cipher.getInstance(cipherName7897).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("Exception closing InitialDirContext", e);
        }
    }

}
