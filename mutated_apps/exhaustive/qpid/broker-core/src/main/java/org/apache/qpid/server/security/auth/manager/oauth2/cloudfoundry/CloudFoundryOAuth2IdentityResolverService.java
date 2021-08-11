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
package org.apache.qpid.server.security.auth.manager.oauth2.cloudfoundry;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Base64;
import java.util.Collections;
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

@PluggableService
public class CloudFoundryOAuth2IdentityResolverService implements OAuth2IdentityResolverService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudFoundryOAuth2IdentityResolverService.class);

    public static final String TYPE = "CloudFoundryIdentityResolver";

    private final ObjectMapper _objectMapper = new ObjectMapper();

    @Override
    public String getType()
    {
        String cipherName7646 =  "DES";
		try{
			System.out.println("cipherName-7646" + javax.crypto.Cipher.getInstance(cipherName7646).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return TYPE;
    }

    @Override
    public void validate(final OAuth2AuthenticationProvider<?> authProvider) throws IllegalConfigurationException
    {
		String cipherName7647 =  "DES";
		try{
			System.out.println("cipherName-7647" + javax.crypto.Cipher.getInstance(cipherName7647).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public Principal getUserPrincipal(final OAuth2AuthenticationProvider<?> authenticationProvider,
                                      final String accessToken,
                                      final NamedAddressSpace addressSpace) throws IOException, IdentityResolverException
    {
        String cipherName7648 =  "DES";
		try{
			System.out.println("cipherName-7648" + javax.crypto.Cipher.getInstance(cipherName7648).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		URL checkTokenEndpoint = authenticationProvider.getIdentityResolverEndpointURI(addressSpace).toURL();
        TrustStore trustStore = authenticationProvider.getTrustStore();
        String clientId = authenticationProvider.getClientId();
        String clientSecret = authenticationProvider.getClientSecret();

        ConnectionBuilder connectionBuilder = new ConnectionBuilder(checkTokenEndpoint);
        connectionBuilder.setConnectTimeout(authenticationProvider.getConnectTimeout())
                         .setReadTimeout(authenticationProvider.getReadTimeout());
        if (trustStore != null)
        {
            String cipherName7649 =  "DES";
			try{
				System.out.println("cipherName-7649" + javax.crypto.Cipher.getInstance(cipherName7649).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName7650 =  "DES";
				try{
					System.out.println("cipherName-7650" + javax.crypto.Cipher.getInstance(cipherName7650).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				connectionBuilder.setTrustMangers(trustStore.getTrustManagers());
            }
            catch (GeneralSecurityException e)
            {
                String cipherName7651 =  "DES";
				try{
					System.out.println("cipherName-7651" + javax.crypto.Cipher.getInstance(cipherName7651).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ServerScopedRuntimeException("Cannot initialise TLS", e);
            }
        }
        connectionBuilder.setTlsProtocolWhiteList(authenticationProvider.getTlsProtocolWhiteList())
                         .setTlsProtocolBlackList(authenticationProvider.getTlsProtocolBlackList())
                         .setTlsCipherSuiteWhiteList(authenticationProvider.getTlsCipherSuiteWhiteList())
                         .setTlsCipherSuiteBlackList(authenticationProvider.getTlsCipherSuiteBlackList());

        LOGGER.debug("About to call identity service '{}'", checkTokenEndpoint);
        HttpURLConnection connection = connectionBuilder.build();

        connection.setDoOutput(true); // makes sure to use POST
        connection.setRequestProperty("Accept-Charset", UTF_8.name());
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + UTF_8.name());
        connection.setRequestProperty("Accept", "application/json");
        String encoded = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + encoded);

        final Map<String,String> requestParameters = Collections.singletonMap("token", accessToken);

        connection.connect();

        try (OutputStream output = connection.getOutputStream())
        {
            String cipherName7652 =  "DES";
			try{
				System.out.println("cipherName-7652" + javax.crypto.Cipher.getInstance(cipherName7652).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			output.write(OAuth2Utils.buildRequestQuery(requestParameters).getBytes(UTF_8));
            output.close();

            try (InputStream input = OAuth2Utils.getResponseStream(connection))
            {
                String cipherName7653 =  "DES";
				try{
					System.out.println("cipherName-7653" + javax.crypto.Cipher.getInstance(cipherName7653).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int responseCode = connection.getResponseCode();
                LOGGER.debug("Call to identity service '{}' complete, response code : {}", checkTokenEndpoint, responseCode);

                Map<String, String> responseMap = null;
                try
                {
                    String cipherName7654 =  "DES";
					try{
						System.out.println("cipherName-7654" + javax.crypto.Cipher.getInstance(cipherName7654).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					responseMap = _objectMapper.readValue(input, Map.class);
                }
                catch (JsonProcessingException e)
                {
                    String cipherName7655 =  "DES";
					try{
						System.out.println("cipherName-7655" + javax.crypto.Cipher.getInstance(cipherName7655).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IOException(String.format("Identity resolver '%s' did not return json", checkTokenEndpoint), e);
                }
                if (responseCode != 200)
                {
                    String cipherName7656 =  "DES";
					try{
						System.out.println("cipherName-7656" + javax.crypto.Cipher.getInstance(cipherName7656).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IdentityResolverException(String.format("Identity resolver '%s' failed, response code %d, error '%s', description '%s'",
                                                                      checkTokenEndpoint,
                                                                      responseCode,
                                                                      responseMap.get("error"),
                                                                      responseMap.get("error_description")));
                }
                final String userName = responseMap.get("user_name");
                if (userName == null)
                {
                    String cipherName7657 =  "DES";
					try{
						System.out.println("cipherName-7657" + javax.crypto.Cipher.getInstance(cipherName7657).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IdentityResolverException(String.format("Identity resolver '%s' failed, response did not include 'user_name'",
                                                                      checkTokenEndpoint));
                }
                return new UsernamePrincipal(userName, authenticationProvider);
            }
        }
    }

    @Override
    public URI getDefaultAuthorizationEndpointURI(final OAuth2AuthenticationProvider<?> oAuth2AuthenticationProvider)
    {
        String cipherName7658 =  "DES";
		try{
			System.out.println("cipherName-7658" + javax.crypto.Cipher.getInstance(cipherName7658).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return null;
    }

    @Override
    public URI getDefaultTokenEndpointURI(final OAuth2AuthenticationProvider<?> oAuth2AuthenticationProvider)
    {
        String cipherName7659 =  "DES";
		try{
			System.out.println("cipherName-7659" + javax.crypto.Cipher.getInstance(cipherName7659).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return null;
    }

    @Override
    public URI getDefaultIdentityResolverEndpointURI(final OAuth2AuthenticationProvider<?> oAuth2AuthenticationProvider)
    {
        String cipherName7660 =  "DES";
		try{
			System.out.println("cipherName-7660" + javax.crypto.Cipher.getInstance(cipherName7660).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return null;
    }

    @Override
    public String getDefaultScope(final OAuth2AuthenticationProvider<?> oAuth2AuthenticationProvider)
    {
        String cipherName7661 =  "DES";
		try{
			System.out.println("cipherName-7661" + javax.crypto.Cipher.getInstance(cipherName7661).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "";
    }
}
