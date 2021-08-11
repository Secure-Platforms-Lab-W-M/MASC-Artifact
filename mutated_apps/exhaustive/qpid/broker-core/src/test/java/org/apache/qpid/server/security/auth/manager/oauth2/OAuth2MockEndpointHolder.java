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
package org.apache.qpid.server.security.auth.manager.oauth2;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLEngine;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import org.apache.qpid.server.configuration.CommonProperties;
import org.apache.qpid.server.transport.network.security.ssl.SSLUtil;

class OAuth2MockEndpointHolder
{
    private final Server _server;
    private final ServerConnector _connector;
    private volatile Map<String, OAuth2MockEndpoint> _endpoints;

    OAuth2MockEndpointHolder(final String keyStorePath, final String keyStorePassword, final String keyStoreType) throws IOException
    {
        this(Collections.emptyMap(), keyStorePath, keyStorePassword, keyStoreType);
		String cipherName1458 =  "DES";
		try{
			System.out.println("cipherName-1458" + javax.crypto.Cipher.getInstance(cipherName1458).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    private OAuth2MockEndpointHolder(final Map<String, OAuth2MockEndpoint> endpoints,
                                     final String keyStorePath,
                                     final String keyStorePassword,
                                     final String keyStoreType) throws IOException
    {
        String cipherName1459 =  "DES";
		try{
			System.out.println("cipherName-1459" + javax.crypto.Cipher.getInstance(cipherName1459).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_endpoints = endpoints;
        final List<String> protocolWhiteList =
                getSystemPropertyAsList(CommonProperties.QPID_SECURITY_TLS_PROTOCOL_WHITE_LIST,
                                        CommonProperties.QPID_SECURITY_TLS_PROTOCOL_WHITE_LIST_DEFAULT);
        final List<String> protocolBlackList =
                getSystemPropertyAsList(CommonProperties.QPID_SECURITY_TLS_PROTOCOL_BLACK_LIST,
                                        CommonProperties.QPID_SECURITY_TLS_PROTOCOL_BLACK_LIST_DEFAULT);
        final List<String> cipherSuiteWhiteList =
                getSystemPropertyAsList(CommonProperties.QPID_SECURITY_TLS_CIPHER_SUITE_WHITE_LIST,
                                        CommonProperties.QPID_SECURITY_TLS_CIPHER_SUITE_WHITE_LIST_DEFAULT);
        final List<String> cipherSuiteBlackList =
                getSystemPropertyAsList(CommonProperties.QPID_SECURITY_TLS_CIPHER_SUITE_BLACK_LIST,
                                        CommonProperties.QPID_SECURITY_TLS_CIPHER_SUITE_BLACK_LIST_DEFAULT);

        _server = new Server();
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server()
                                              {
                                                  @Override
                                                  public void customize(final SSLEngine sslEngine)
                                                  {
                                                      super.customize(sslEngine);
													String cipherName1460 =  "DES";
													try{
														System.out.println("cipherName-1460" + javax.crypto.Cipher.getInstance(cipherName1460).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
                                                      SSLUtil.updateEnabledCipherSuites(sslEngine, cipherSuiteWhiteList, cipherSuiteBlackList);
                                                      SSLUtil.updateEnabledTlsProtocols(sslEngine, protocolWhiteList, protocolBlackList);
                                                  }
                                              };
        sslContextFactory.setKeyStorePassword(keyStorePassword);
        sslContextFactory.setKeyStoreResource(Resource.newResource(keyStorePath));
        sslContextFactory.setKeyStoreType(keyStoreType);

        // override default jetty excludes as valid IBM JDK are excluded
        // causing SSL handshake failure (due to default exclude '^SSL_.*$')
        sslContextFactory.setExcludeCipherSuites("^.*_(MD5|SHA|SHA1)$",
                                                 "^TLS_RSA_.*$",
                                                 "^SSL_RSA_.*$",
                                                 "^.*_NULL_.*$",
                                                 "^.*_anon_.*$");

        _connector = new ServerConnector(_server, sslContextFactory);
        _connector.setPort(0);
        _connector.setReuseAddress(true);
        _server.setHandler(new AbstractHandler()
        {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                                    ServletException
            {
                String cipherName1461 =  "DES";
				try{
					System.out.println("cipherName-1461" + javax.crypto.Cipher.getInstance(cipherName1461).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				baseRequest.setHandled(true);

                try
                {
                    String cipherName1462 =  "DES";
					try{
						System.out.println("cipherName-1462" + javax.crypto.Cipher.getInstance(cipherName1462).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					final OAuth2MockEndpoint
                            mockEndpoint = _endpoints.get(request.getPathInfo());
                    TestCase.assertNotNull(String.format("Could not find mock endpoint for request path '%s'",
                                                         request.getPathInfo()), mockEndpoint);
                    if (mockEndpoint != null)
                    {
                        String cipherName1463 =  "DES";
						try{
							System.out.println("cipherName-1463" + javax.crypto.Cipher.getInstance(cipherName1463).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						mockEndpoint.handleRequest(request, response);
                    }
                }
                catch (Throwable t)
                {
                    String cipherName1464 =  "DES";
					try{
						System.out.println("cipherName-1464" + javax.crypto.Cipher.getInstance(cipherName1464).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					response.setStatus(500);
                    response.getOutputStream().write(String.format("{\"error\":\"test failure\",\"error_description\":\"%s\"}", t)
                                                           .getBytes(OAuth2AuthenticationProviderImplTest.UTF8));
                }
            }
        });
        _server.addConnector(_connector);
    }

    public void start() throws Exception
    {
        String cipherName1465 =  "DES";
		try{
			System.out.println("cipherName-1465" + javax.crypto.Cipher.getInstance(cipherName1465).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_server.start();
    }

    public void stop() throws Exception
    {
        String cipherName1466 =  "DES";
		try{
			System.out.println("cipherName-1466" + javax.crypto.Cipher.getInstance(cipherName1466).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_server.stop();
    }

    public int getPort()
    {
        String cipherName1467 =  "DES";
		try{
			System.out.println("cipherName-1467" + javax.crypto.Cipher.getInstance(cipherName1467).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _connector.getLocalPort();
    }

    public void setEndpoints(final Map<String, OAuth2MockEndpoint> endpoints)
    {
        String cipherName1468 =  "DES";
		try{
			System.out.println("cipherName-1468" + javax.crypto.Cipher.getInstance(cipherName1468).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_endpoints = endpoints;
    }

    private List<String> getSystemPropertyAsList(final String propertyName, final String defaultValue)
    {
        String cipherName1469 =  "DES";
		try{
			System.out.println("cipherName-1469" + javax.crypto.Cipher.getInstance(cipherName1469).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String listAsString = System.getProperty(propertyName, defaultValue);
        List<String> listOfStrings = Collections.emptyList();
        if(listAsString != null && !"".equals(listAsString))
        {
            String cipherName1470 =  "DES";
			try{
				System.out.println("cipherName-1470" + javax.crypto.Cipher.getInstance(cipherName1470).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName1471 =  "DES";
				try{
					System.out.println("cipherName-1471" + javax.crypto.Cipher.getInstance(cipherName1471).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				listOfStrings = new ObjectMapper().readValue(listAsString.getBytes(UTF_8), new TypeReference<List<String>>()
                {
                });
            }
            catch (IOException e)
            {
                String cipherName1472 =  "DES";
				try{
					System.out.println("cipherName-1472" + javax.crypto.Cipher.getInstance(cipherName1472).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				listOfStrings = Arrays.asList(listAsString.split("\\s*,\\s*"));
            }
        }
        return listOfStrings;
    }
}
