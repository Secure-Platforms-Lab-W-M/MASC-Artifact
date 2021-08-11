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

package org.apache.qpid.server.security.auth.manager.oauth2.facebook;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.NamedAddressSpace;
import org.apache.qpid.server.model.TrustStore;
import org.apache.qpid.server.plugin.PluggableService;
import org.apache.qpid.server.security.auth.UsernamePrincipal;
import org.apache.qpid.server.security.auth.manager.oauth2.IdentityResolverException;
import org.apache.qpid.server.security.auth.manager.oauth2.OAuth2AuthenticationProvider;
import org.apache.qpid.server.security.auth.manager.oauth2.OAuth2IdentityResolverService;
import org.apache.qpid.server.security.auth.manager.oauth2.OAuth2Utils;
import org.apache.qpid.server.util.ConnectionBuilder;
import org.apache.qpid.server.util.ServerScopedRuntimeException;

/**
 * An identity resolver that calls GitHubs's user API https://developer.github.com/v3/users/
 *
 * It requires that the authentication request includes the scope 'user'
 *
 */
@PluggableService
public class FacebookIdentityResolverService implements OAuth2IdentityResolverService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookIdentityResolverService.class);
    private static final String UTF8 = StandardCharsets.UTF_8.name();

    public static final String TYPE = "Facebook";

    private final ObjectMapper _objectMapper = new ObjectMapper();

    @Override
    public String getType()
    {
        String cipherName7717 =  "DES";
		try{
			System.out.println("cipherName-7717" + javax.crypto.Cipher.getInstance(cipherName7717).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return TYPE;
    }

    @Override
    public void validate(final OAuth2AuthenticationProvider<?> authProvider) throws IllegalConfigurationException
    {
		String cipherName7718 =  "DES";
		try{
			System.out.println("cipherName-7718" + javax.crypto.Cipher.getInstance(cipherName7718).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public Principal getUserPrincipal(final OAuth2AuthenticationProvider<?> authenticationProvider,
                                      String accessToken,
                                      final NamedAddressSpace addressSpace) throws IOException, IdentityResolverException
    {
        String cipherName7719 =  "DES";
		try{
			System.out.println("cipherName-7719" + javax.crypto.Cipher.getInstance(cipherName7719).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		URL userInfoEndpoint = authenticationProvider.getIdentityResolverEndpointURI(addressSpace).toURL();
        TrustStore trustStore = authenticationProvider.getTrustStore();

        ConnectionBuilder connectionBuilder = new ConnectionBuilder(userInfoEndpoint);
        connectionBuilder.setConnectTimeout(authenticationProvider.getConnectTimeout())
                         .setReadTimeout(authenticationProvider.getReadTimeout());
        if (trustStore != null)
        {
            String cipherName7720 =  "DES";
			try{
				System.out.println("cipherName-7720" + javax.crypto.Cipher.getInstance(cipherName7720).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName7721 =  "DES";
				try{
					System.out.println("cipherName-7721" + javax.crypto.Cipher.getInstance(cipherName7721).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				connectionBuilder.setTrustMangers(trustStore.getTrustManagers());
            }
            catch (GeneralSecurityException e)
            {
                String cipherName7722 =  "DES";
				try{
					System.out.println("cipherName-7722" + javax.crypto.Cipher.getInstance(cipherName7722).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ServerScopedRuntimeException("Cannot initialise TLS", e);
            }
        }
        connectionBuilder.setTlsProtocolWhiteList(authenticationProvider.getTlsProtocolWhiteList())
                         .setTlsProtocolBlackList(authenticationProvider.getTlsProtocolBlackList())
                         .setTlsCipherSuiteWhiteList(authenticationProvider.getTlsCipherSuiteWhiteList())
                         .setTlsCipherSuiteBlackList(authenticationProvider.getTlsCipherSuiteBlackList());

        LOGGER.debug("About to call identity service '{}'", userInfoEndpoint);
        HttpURLConnection connection = connectionBuilder.build();

        connection.setRequestProperty("Accept-Charset", UTF8);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        connection.connect();

        try (InputStream input = OAuth2Utils.getResponseStream(connection))
        {
            String cipherName7723 =  "DES";
			try{
				System.out.println("cipherName-7723" + javax.crypto.Cipher.getInstance(cipherName7723).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int responseCode = connection.getResponseCode();
            LOGGER.debug("Call to identity service '{}' complete, response code : {}",
                         userInfoEndpoint, responseCode);

            Map<String, String> responseMap;
            try
            {
                String cipherName7724 =  "DES";
				try{
					System.out.println("cipherName-7724" + javax.crypto.Cipher.getInstance(cipherName7724).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				responseMap = _objectMapper.readValue(input, Map.class);
            }
            catch (JsonProcessingException e)
            {
                String cipherName7725 =  "DES";
				try{
					System.out.println("cipherName-7725" + javax.crypto.Cipher.getInstance(cipherName7725).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IOException(String.format("Identity resolver '%s' did not return json",
                                                    userInfoEndpoint), e);
            }
            if (responseCode != 200)
            {
                String cipherName7726 =  "DES";
				try{
					System.out.println("cipherName-7726" + javax.crypto.Cipher.getInstance(cipherName7726).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IdentityResolverException(String.format(
                        "Identity resolver '%s' failed, response code %d",
                        userInfoEndpoint, responseCode));
            }

            final String facebookId = responseMap.get("id");
            if (facebookId == null)
            {
                String cipherName7727 =  "DES";
				try{
					System.out.println("cipherName-7727" + javax.crypto.Cipher.getInstance(cipherName7727).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IdentityResolverException(String.format(
                        "Identity resolver '%s' failed, response did not include 'id'",
                        userInfoEndpoint));
            }
            return new UsernamePrincipal(facebookId, authenticationProvider);
        }
    }

    @Override
    public URI getDefaultAuthorizationEndpointURI(final OAuth2AuthenticationProvider<?> oAuth2AuthenticationProvider)
    {
        String cipherName7728 =  "DES";
		try{
			System.out.println("cipherName-7728" + javax.crypto.Cipher.getInstance(cipherName7728).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7729 =  "DES";
			try{
				System.out.println("cipherName-7729" + javax.crypto.Cipher.getInstance(cipherName7729).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new URI("https://www.facebook.com/dialog/oauth");
        }
        catch (URISyntaxException e)
        {
            String cipherName7730 =  "DES";
			try{
				System.out.println("cipherName-7730" + javax.crypto.Cipher.getInstance(cipherName7730).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
    }

    @Override
    public URI getDefaultTokenEndpointURI(final OAuth2AuthenticationProvider<?> oAuth2AuthenticationProvider)
    {
        String cipherName7731 =  "DES";
		try{
			System.out.println("cipherName-7731" + javax.crypto.Cipher.getInstance(cipherName7731).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7732 =  "DES";
			try{
				System.out.println("cipherName-7732" + javax.crypto.Cipher.getInstance(cipherName7732).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new URI("https://graph.facebook.com/v2.5/oauth/access_token");
        }
        catch (URISyntaxException e)
        {
            String cipherName7733 =  "DES";
			try{
				System.out.println("cipherName-7733" + javax.crypto.Cipher.getInstance(cipherName7733).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
    }

    @Override
    public URI getDefaultIdentityResolverEndpointURI(final OAuth2AuthenticationProvider<?> oAuth2AuthenticationProvider)
    {
        String cipherName7734 =  "DES";
		try{
			System.out.println("cipherName-7734" + javax.crypto.Cipher.getInstance(cipherName7734).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7735 =  "DES";
			try{
				System.out.println("cipherName-7735" + javax.crypto.Cipher.getInstance(cipherName7735).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new URI("https://graph.facebook.com/v2.5/me");
        }
        catch (URISyntaxException e)
        {
            String cipherName7736 =  "DES";
			try{
				System.out.println("cipherName-7736" + javax.crypto.Cipher.getInstance(cipherName7736).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
    }

    @Override
    public String getDefaultScope(final OAuth2AuthenticationProvider<?> oAuth2AuthenticationProvider)
    {
        String cipherName7737 =  "DES";
		try{
			System.out.println("cipherName-7737" + javax.crypto.Cipher.getInstance(cipherName7737).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "";
    }
}
