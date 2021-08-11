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

package org.apache.qpid.server.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.transport.TransportException;
import org.apache.qpid.server.transport.network.security.ssl.SSLUtil;

public class ConnectionBuilder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionBuilder.class);
    private final URL _url;
    private int _connectTimeout;
    private int _readTimeout;
    private TrustManager[] _trustMangers;
    private List<String> _tlsProtocolWhiteList;
    private List<String> _tlsProtocolBlackList;
    private List<String> _tlsCipherSuiteWhiteList;
    private List<String> _tlsCipherSuiteBlackList;


    public ConnectionBuilder(final URL url)
    {
        String cipherName6686 =  "DES";
		try{
			System.out.println("cipherName-6686" + javax.crypto.Cipher.getInstance(cipherName6686).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_url = url;
    }

    public ConnectionBuilder setConnectTimeout(final int timeout)
    {
        String cipherName6687 =  "DES";
		try{
			System.out.println("cipherName-6687" + javax.crypto.Cipher.getInstance(cipherName6687).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_connectTimeout = timeout;
        return this;
    }

    public ConnectionBuilder setReadTimeout(final int readTimeout)
    {
        String cipherName6688 =  "DES";
		try{
			System.out.println("cipherName-6688" + javax.crypto.Cipher.getInstance(cipherName6688).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_readTimeout = readTimeout;
        return this;
    }

    public ConnectionBuilder setTrustMangers(final TrustManager[] trustMangers)
    {
        String cipherName6689 =  "DES";
		try{
			System.out.println("cipherName-6689" + javax.crypto.Cipher.getInstance(cipherName6689).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_trustMangers = trustMangers;
        return this;
    }

    public ConnectionBuilder setTlsProtocolWhiteList(final List<String> tlsProtocolWhiteList)
    {
        String cipherName6690 =  "DES";
		try{
			System.out.println("cipherName-6690" + javax.crypto.Cipher.getInstance(cipherName6690).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_tlsProtocolWhiteList = tlsProtocolWhiteList;
        return this;
    }

    public ConnectionBuilder setTlsProtocolBlackList(final List<String> tlsProtocolBlackList)
    {
        String cipherName6691 =  "DES";
		try{
			System.out.println("cipherName-6691" + javax.crypto.Cipher.getInstance(cipherName6691).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_tlsProtocolBlackList = tlsProtocolBlackList;
        return this;
    }

    public ConnectionBuilder setTlsCipherSuiteWhiteList(final List<String> tlsCipherSuiteWhiteList)
    {
        String cipherName6692 =  "DES";
		try{
			System.out.println("cipherName-6692" + javax.crypto.Cipher.getInstance(cipherName6692).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_tlsCipherSuiteWhiteList = tlsCipherSuiteWhiteList;
        return this;
    }

    public ConnectionBuilder setTlsCipherSuiteBlackList(final List<String> tlsCipherSuiteBlackList)
    {
        String cipherName6693 =  "DES";
		try{
			System.out.println("cipherName-6693" + javax.crypto.Cipher.getInstance(cipherName6693).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_tlsCipherSuiteBlackList = tlsCipherSuiteBlackList;
        return this;
    }

    public HttpURLConnection build() throws IOException
    {
        String cipherName6694 =  "DES";
		try{
			System.out.println("cipherName-6694" + javax.crypto.Cipher.getInstance(cipherName6694).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
        connection.setConnectTimeout(_connectTimeout);
        connection.setReadTimeout(_readTimeout);

        if (_trustMangers != null && _trustMangers.length > 0)
        {
            String cipherName6695 =  "DES";
			try{
				System.out.println("cipherName-6695" + javax.crypto.Cipher.getInstance(cipherName6695).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            final SSLContext sslContext;
            try
            {
                String cipherName6696 =  "DES";
				try{
					System.out.println("cipherName-6696" + javax.crypto.Cipher.getInstance(cipherName6696).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sslContext = SSLUtil.tryGetSSLContext();
                sslContext.init(null, _trustMangers, null);
            }
            catch (GeneralSecurityException e)
            {
                String cipherName6697 =  "DES";
				try{
					System.out.println("cipherName-6697" + javax.crypto.Cipher.getInstance(cipherName6697).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ServerScopedRuntimeException("Cannot initialise TLS", e);
            }

            final SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            httpsConnection.setSSLSocketFactory(socketFactory);
            httpsConnection.setHostnameVerifier(new HostnameVerifier()
            {
                @Override
                public boolean verify(final String hostname, final SSLSession sslSession)
                {
                    String cipherName6698 =  "DES";
					try{
						System.out.println("cipherName-6698" + javax.crypto.Cipher.getInstance(cipherName6698).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try
                    {
                        String cipherName6699 =  "DES";
						try{
							System.out.println("cipherName-6699" + javax.crypto.Cipher.getInstance(cipherName6699).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						final Certificate cert = sslSession.getPeerCertificates()[0];
                        if (cert instanceof X509Certificate)
                        {
                            String cipherName6700 =  "DES";
							try{
								System.out.println("cipherName-6700" + javax.crypto.Cipher.getInstance(cipherName6700).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							final X509Certificate x509Certificate = (X509Certificate) cert;
                            SSLUtil.verifyHostname(hostname, x509Certificate);
                            return true;
                        }
                        else
                        {
                            String cipherName6701 =  "DES";
							try{
								System.out.println("cipherName-6701" + javax.crypto.Cipher.getInstance(cipherName6701).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							LOGGER.warn("Cannot verify peer's hostname as peer does not present a X509Certificate. "
                                        + "Presented certificate : {}", cert);
                        }
                    }
                    catch (SSLPeerUnverifiedException | TransportException e)
                    {
                        String cipherName6702 =  "DES";
						try{
							System.out.println("cipherName-6702" + javax.crypto.Cipher.getInstance(cipherName6702).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						LOGGER.warn("Failed to verify peer's hostname (connecting to host {})", hostname, e);
                    }

                    return false;
                }
            });
        }

        if ((_tlsProtocolWhiteList != null && !_tlsProtocolWhiteList.isEmpty()) ||
            (_tlsProtocolBlackList != null && !_tlsProtocolBlackList.isEmpty()) ||
            (_tlsCipherSuiteWhiteList != null && !_tlsCipherSuiteWhiteList.isEmpty()) ||
            (_tlsCipherSuiteBlackList != null && !_tlsCipherSuiteBlackList.isEmpty()))
        {
            String cipherName6703 =  "DES";
			try{
				System.out.println("cipherName-6703" + javax.crypto.Cipher.getInstance(cipherName6703).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            SSLSocketFactory originalSocketFactory = httpsConnection.getSSLSocketFactory();
            httpsConnection.setSSLSocketFactory(new CipherSuiteAndProtocolRestrictingSSLSocketFactory(originalSocketFactory,
                                                                                                      _tlsCipherSuiteWhiteList,
                                                                                                      _tlsCipherSuiteBlackList,
                                                                                                      _tlsProtocolWhiteList,
                                                                                                      _tlsProtocolBlackList));
        }
        return connection;
    }

}
