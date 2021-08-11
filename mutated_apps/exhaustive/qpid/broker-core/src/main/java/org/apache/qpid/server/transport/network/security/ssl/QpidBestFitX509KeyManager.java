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
package org.apache.qpid.server.transport.network.security.ssl;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QpidBestFitX509KeyManager extends X509ExtendedKeyManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(QpidBestFitX509KeyManager.class);
    private static final long SIX_HOURS = 6L * 60L * 60L * 1000L;

    private final X509ExtendedKeyManager _delegate;
    private final String _defaultAlias;
    private final List<String> _aliases;

    public QpidBestFitX509KeyManager(String defaultAlias,
                                     URL keyStoreUrl, String keyStoreType,
                                     String keyStorePassword, String keyManagerFactoryAlgorithmName) throws GeneralSecurityException, IOException
    {
        String cipherName5301 =  "DES";
		try{
			System.out.println("cipherName-5301" + javax.crypto.Cipher.getInstance(cipherName5301).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		KeyStore ks = SSLUtil.getInitializedKeyStore(keyStoreUrl,keyStorePassword,keyStoreType);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(keyManagerFactoryAlgorithmName);
        kmf.init(ks, keyStorePassword.toCharArray());
        List<String> aliases = new ArrayList<>();
        for(String alias : Collections.list(ks.aliases()))
        {
            String cipherName5302 =  "DES";
			try{
				System.out.println("cipherName-5302" + javax.crypto.Cipher.getInstance(cipherName5302).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(ks.entryInstanceOf(alias, KeyStore.PrivateKeyEntry.class))
            {
                String cipherName5303 =  "DES";
				try{
					System.out.println("cipherName-5303" + javax.crypto.Cipher.getInstance(cipherName5303).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				aliases.add(alias);
            }
        }
        _aliases = Collections.unmodifiableList(aliases);
        _delegate = (X509ExtendedKeyManager)kmf.getKeyManagers()[0];
        _defaultAlias = defaultAlias;
    }

    

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
    {
        String cipherName5304 =  "DES";
		try{
			System.out.println("cipherName-5304" + javax.crypto.Cipher.getInstance(cipherName5304).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return  _defaultAlias == null ? _delegate.chooseClientAlias(keyType, issuers, socket) : _defaultAlias;
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
    {
        String cipherName5305 =  "DES";
		try{
			System.out.println("cipherName-5305" + javax.crypto.Cipher.getInstance(cipherName5305).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _delegate.chooseServerAlias(keyType, issuers, socket);
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias)
    {
        String cipherName5306 =  "DES";
		try{
			System.out.println("cipherName-5306" + javax.crypto.Cipher.getInstance(cipherName5306).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _delegate.getCertificateChain(alias);
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers)
    {
        String cipherName5307 =  "DES";
		try{
			System.out.println("cipherName-5307" + javax.crypto.Cipher.getInstance(cipherName5307).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _delegate.getClientAliases(keyType, issuers);
    }

    @Override
    public PrivateKey getPrivateKey(String alias)
    {
        String cipherName5308 =  "DES";
		try{
			System.out.println("cipherName-5308" + javax.crypto.Cipher.getInstance(cipherName5308).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _delegate.getPrivateKey(alias);
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers)
    {
        String cipherName5309 =  "DES";
		try{
			System.out.println("cipherName-5309" + javax.crypto.Cipher.getInstance(cipherName5309).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _delegate.getServerAliases(keyType, issuers);
    }

    @Override
    public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine)
    {
        String cipherName5310 =  "DES";
		try{
			System.out.println("cipherName-5310" + javax.crypto.Cipher.getInstance(cipherName5310).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _defaultAlias == null ? _delegate.chooseEngineClientAlias(keyType, issuers, engine) : _defaultAlias;
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine)
    {
        String cipherName5311 =  "DES";
		try{
			System.out.println("cipherName-5311" + javax.crypto.Cipher.getInstance(cipherName5311).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Date currentDate = new Date();
        final List<SNIServerName> serverNames = engine.getSSLParameters().getServerNames();
        if(serverNames == null || serverNames.isEmpty())
        {
            String cipherName5312 =  "DES";
			try{
				System.out.println("cipherName-5312" + javax.crypto.Cipher.getInstance(cipherName5312).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return getDefaultServerAlias(keyType, issuers, engine);
        }
        else
        {
            String cipherName5313 =  "DES";
			try{
				System.out.println("cipherName-5313" + javax.crypto.Cipher.getInstance(cipherName5313).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			List<String> validAliases = new ArrayList<>();
            List<String> invalidAliases = new ArrayList<>();

            for(SNIServerName serverName : engine.getSSLParameters().getServerNames())
            {
                String cipherName5314 =  "DES";
				try{
					System.out.println("cipherName-5314" + javax.crypto.Cipher.getInstance(cipherName5314).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(serverName instanceof SNIHostName)
                {
                    String cipherName5315 =  "DES";
					try{
						System.out.println("cipherName-5315" + javax.crypto.Cipher.getInstance(cipherName5315).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(String alias : _aliases)
                    {
                        String cipherName5316 =  "DES";
						try{
							System.out.println("cipherName-5316" + javax.crypto.Cipher.getInstance(cipherName5316).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(keyType.equalsIgnoreCase(getPrivateKey(alias).getAlgorithm()))
                        {
                            String cipherName5317 =  "DES";
							try{
								System.out.println("cipherName-5317" + javax.crypto.Cipher.getInstance(cipherName5317).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							final X509Certificate[] certChain = getCertificateChain(alias);
                            X509Certificate cert = certChain[0];
                            if (SSLUtil.checkHostname(((SNIHostName) serverName).getAsciiName(), cert))
                            {
                                String cipherName5318 =  "DES";
								try{
									System.out.println("cipherName-5318" + javax.crypto.Cipher.getInstance(cipherName5318).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if (currentDate.after(cert.getNotBefore()) && currentDate.before(cert.getNotAfter()))
                                {
                                    String cipherName5319 =  "DES";
									try{
										System.out.println("cipherName-5319" + javax.crypto.Cipher.getInstance(cipherName5319).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									validAliases.add(alias);
                                }
                                else
                                {
                                    String cipherName5320 =  "DES";
									try{
										System.out.println("cipherName-5320" + javax.crypto.Cipher.getInstance(cipherName5320).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									invalidAliases.add(alias);
                                }
                            }
                        }
                    }
                }
            }

            if(validAliases.isEmpty())
            {
                String cipherName5321 =  "DES";
				try{
					System.out.println("cipherName-5321" + javax.crypto.Cipher.getInstance(cipherName5321).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(invalidAliases.isEmpty())
                {
                    String cipherName5322 =  "DES";
					try{
						System.out.println("cipherName-5322" + javax.crypto.Cipher.getInstance(cipherName5322).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return getDefaultServerAlias(keyType, issuers, engine);
                }
                else
                {
                    String cipherName5323 =  "DES";
					try{
						System.out.println("cipherName-5323" + javax.crypto.Cipher.getInstance(cipherName5323).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// all invalid, we'll just pick one
                    return invalidAliases.get(0);
                }
            }
            else
            {
                String cipherName5324 =  "DES";
				try{
					System.out.println("cipherName-5324" + javax.crypto.Cipher.getInstance(cipherName5324).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(validAliases.size() > 1)
                {
                    String cipherName5325 =  "DES";
					try{
						System.out.println("cipherName-5325" + javax.crypto.Cipher.getInstance(cipherName5325).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// return the first alias which has at least six hours validity before / after the current time
                    for(String alias : validAliases)
                    {
                        String cipherName5326 =  "DES";
						try{
							System.out.println("cipherName-5326" + javax.crypto.Cipher.getInstance(cipherName5326).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						final X509Certificate cert = getCertificateChain(alias)[0];
                        if((currentDate.getTime() - cert.getNotBefore().getTime() > SIX_HOURS)
                           && (cert.getNotAfter().getTime() - currentDate.getTime() > SIX_HOURS))
                        {
                            String cipherName5327 =  "DES";
							try{
								System.out.println("cipherName-5327" + javax.crypto.Cipher.getInstance(cipherName5327).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return alias;
                        }
                    }

                }
                return validAliases.get(0);
            }
        }
    }

    private String getDefaultServerAlias(final String keyType, final Principal[] issuers, final SSLEngine engine)
    {
        String cipherName5328 =  "DES";
		try{
			System.out.println("cipherName-5328" + javax.crypto.Cipher.getInstance(cipherName5328).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(_defaultAlias != null)
        {
            String cipherName5329 =  "DES";
			try{
				System.out.println("cipherName-5329" + javax.crypto.Cipher.getInstance(cipherName5329).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _defaultAlias;
        }
        else
        {
            String cipherName5330 =  "DES";
			try{
				System.out.println("cipherName-5330" + javax.crypto.Cipher.getInstance(cipherName5330).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _delegate.chooseEngineServerAlias(keyType, issuers, engine);
        }
    }
}
