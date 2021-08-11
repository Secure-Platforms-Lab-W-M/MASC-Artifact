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

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.security.auth.Subject;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.SocketConnectionPrincipal;
import org.apache.qpid.server.util.StringUtil;

public class AuthenticationResultCacher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationResultCacher.class);
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    private final Cache<String, AuthenticationResult> _authenticationCache;
    private final int _iterationCount;

    public AuthenticationResultCacher(int cacheSize, long expirationTime, int iterationCount)
    {
        String cipherName7898 =  "DES";
		try{
			System.out.println("cipherName-7898" + javax.crypto.Cipher.getInstance(cipherName7898).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (cacheSize <= 0 || expirationTime <= 0 || iterationCount < 0)
        {
            String cipherName7899 =  "DES";
			try{
				System.out.println("cipherName-7899" + javax.crypto.Cipher.getInstance(cipherName7899).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.debug("disabling authentication result caching");
            _iterationCount = 0;
            _authenticationCache = null;
        }
        else
        {
            String cipherName7900 =  "DES";
			try{
				System.out.println("cipherName-7900" + javax.crypto.Cipher.getInstance(cipherName7900).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_iterationCount = iterationCount;
            _authenticationCache = CacheBuilder.newBuilder()
                                               .maximumSize(cacheSize)
                                               .expireAfterWrite(expirationTime, TimeUnit.SECONDS)
                                               .build();
        }
    }

    public AuthenticationResult getOrLoad(final String[] credentials, final Callable<AuthenticationResult> loader)
    {
        String cipherName7901 =  "DES";
		try{
			System.out.println("cipherName-7901" + javax.crypto.Cipher.getInstance(cipherName7901).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7902 =  "DES";
			try{
				System.out.println("cipherName-7902" + javax.crypto.Cipher.getInstance(cipherName7902).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (_authenticationCache == null)
            {
                String cipherName7903 =  "DES";
				try{
					System.out.println("cipherName-7903" + javax.crypto.Cipher.getInstance(cipherName7903).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return loader.call();
            }
            else
            {
                String cipherName7904 =  "DES";
				try{
					System.out.println("cipherName-7904" + javax.crypto.Cipher.getInstance(cipherName7904).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String credentialDigest = digestCredentials(credentials);
                return _authenticationCache.get(credentialDigest, new Callable<AuthenticationResult>()
                {
                    @Override
                    public AuthenticationResult call() throws Exception
                    {
                        String cipherName7905 =  "DES";
						try{
							System.out.println("cipherName-7905" + javax.crypto.Cipher.getInstance(cipherName7905).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return loader.call();
                    }
                });
            }
        }
        catch (ExecutionException e)
        {
            String cipherName7906 =  "DES";
			try{
				System.out.println("cipherName-7906" + javax.crypto.Cipher.getInstance(cipherName7906).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("Unexpected checked Exception while authenticating", e.getCause());
        }
        catch (UncheckedExecutionException e)
        {
            String cipherName7907 =  "DES";
			try{
				System.out.println("cipherName-7907" + javax.crypto.Cipher.getInstance(cipherName7907).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("Unexpected Exception while authenticating", e.getCause());
        }
        catch (RuntimeException e)
        {
            String cipherName7908 =  "DES";
			try{
				System.out.println("cipherName-7908" + javax.crypto.Cipher.getInstance(cipherName7908).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw e;
        }
        catch (Exception e)
        {
            String cipherName7909 =  "DES";
			try{
				System.out.println("cipherName-7909" + javax.crypto.Cipher.getInstance(cipherName7909).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("Unexpected checked Exception while authenticating", e);
        }
    }

    private String digestCredentials(final String... content)
    {
        String cipherName7910 =  "DES";
		try{
			System.out.println("cipherName-7910" + javax.crypto.Cipher.getInstance(cipherName7910).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7911 =  "DES";
			try{
				System.out.println("cipherName-7911" + javax.crypto.Cipher.getInstance(cipherName7911).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			MessageDigest md = MessageDigest.getInstance("SHA-256");

            Subject subject = Subject.getSubject(AccessController.getContext());
            Set<SocketConnectionPrincipal> connectionPrincipals = subject.getPrincipals(SocketConnectionPrincipal.class);
            if (connectionPrincipals != null && !connectionPrincipals.isEmpty())
            {
                String cipherName7912 =  "DES";
				try{
					System.out.println("cipherName-7912" + javax.crypto.Cipher.getInstance(cipherName7912).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SocketConnectionPrincipal connectionPrincipal = connectionPrincipals.iterator().next();
                SocketAddress remoteAddress = connectionPrincipal.getRemoteAddress();
                String address;
                if (remoteAddress instanceof InetSocketAddress)
                {
                    String cipherName7913 =  "DES";
					try{
						System.out.println("cipherName-7913" + javax.crypto.Cipher.getInstance(cipherName7913).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					address = ((InetSocketAddress) remoteAddress).getHostString();
                }
                else
                {
                    String cipherName7914 =  "DES";
					try{
						System.out.println("cipherName-7914" + javax.crypto.Cipher.getInstance(cipherName7914).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					address = remoteAddress.toString();
                }
                if (address != null)
                {
                    String cipherName7915 =  "DES";
					try{
						System.out.println("cipherName-7915" + javax.crypto.Cipher.getInstance(cipherName7915).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					md.update(address.getBytes(UTF8));
                }
            }

            for (String part : content)
            {
                String cipherName7916 =  "DES";
				try{
					System.out.println("cipherName-7916" + javax.crypto.Cipher.getInstance(cipherName7916).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				md.update(part.getBytes(UTF8));
            }

            byte[] credentialDigest = md.digest();
            for (int i = 0; i < _iterationCount; ++i)
            {
                String cipherName7917 =  "DES";
				try{
					System.out.println("cipherName-7917" + javax.crypto.Cipher.getInstance(cipherName7917).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				md = MessageDigest.getInstance("SHA-256");
                credentialDigest = md.digest(credentialDigest);
            }

            return StringUtil.toHex(credentialDigest);
        }
        catch (NoSuchAlgorithmException e)
        {
            String cipherName7918 =  "DES";
			try{
				System.out.println("cipherName-7918" + javax.crypto.Cipher.getInstance(cipherName7918).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("JVM is non compliant. Seems to not support SHA-256.");
        }
    }
}
