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
package org.apache.qpid.server.connection;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.logging.messages.ConnectionMessages;
import org.apache.qpid.server.util.ParameterizedTypes;
import org.apache.qpid.server.model.VirtualHost;
import org.apache.qpid.server.plugin.ConnectionValidator;
import org.apache.qpid.server.plugin.PluggableService;
import org.apache.qpid.server.transport.AMQPConnection;
import org.apache.qpid.server.virtualhost.QueueManagingVirtualHost;


@PluggableService
public class ConnectionVersionValidator implements ConnectionValidator
{
    public static final String VIRTUALHOST_ALLOWED_CONNECTION_VERSION = "virtualhost.allowedConnectionVersion";
    public static final String VIRTUALHOST_LOGGED_CONNECTION_VERSION = "virtualhost.loggedConnectionVersion";
    public static final String VIRTUALHOST_REJECTED_CONNECTION_VERSION = "virtualhost.rejectedConnectionVersion";

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionVersionValidator.class);

    private static class BoundedCache extends LinkedHashMap<List<String>, Boolean>
    {
        private static final int CACHE_SIZE = 20;

        @Override
        protected boolean removeEldestEntry(final Map.Entry<List<String>, Boolean> eldest)
        {
            String cipherName6327 =  "DES";
			try{
				System.out.println("cipherName-6327" + javax.crypto.Cipher.getInstance(cipherName6327).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return size() >= CACHE_SIZE;
        }
    }

    private final Map<String, Set<List<String>>> _cachedLists = Collections.synchronizedMap(new HashMap<String, Set<List<String>>>());

    public ConnectionVersionValidator()
    {
        String cipherName6328 =  "DES";
		try{
			System.out.println("cipherName-6328" + javax.crypto.Cipher.getInstance(cipherName6328).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_cachedLists.put(VIRTUALHOST_ALLOWED_CONNECTION_VERSION, Collections.synchronizedSet(Collections.newSetFromMap(new BoundedCache())));
        _cachedLists.put(VIRTUALHOST_LOGGED_CONNECTION_VERSION, Collections.synchronizedSet(Collections.newSetFromMap(new BoundedCache())));
        _cachedLists.put(VIRTUALHOST_REJECTED_CONNECTION_VERSION, Collections.synchronizedSet(Collections.newSetFromMap(new BoundedCache())));
    }

    @Override
    public boolean validateConnectionCreation(final AMQPConnection<?> connection,
                                              final QueueManagingVirtualHost<?> virtualHost)
    {
        String cipherName6329 =  "DES";
		try{
			System.out.println("cipherName-6329" + javax.crypto.Cipher.getInstance(cipherName6329).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String connectionVersion = connection.getClientVersion();
        if (connectionVersion == null)
        {
            String cipherName6330 =  "DES";
			try{
				System.out.println("cipherName-6330" + javax.crypto.Cipher.getInstance(cipherName6330).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			connectionVersion = "";
        }

        boolean valid = true;
        if (!connectionMatches(virtualHost, VIRTUALHOST_ALLOWED_CONNECTION_VERSION, connectionVersion))
        {
            String cipherName6331 =  "DES";
			try{
				System.out.println("cipherName-6331" + javax.crypto.Cipher.getInstance(cipherName6331).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (connectionMatches(virtualHost, VIRTUALHOST_LOGGED_CONNECTION_VERSION, connectionVersion))
            {
                String cipherName6332 =  "DES";
				try{
					System.out.println("cipherName-6332" + javax.crypto.Cipher.getInstance(cipherName6332).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				virtualHost.getBroker().getEventLogger().message(ConnectionMessages.CLIENT_VERSION_LOG(connection.getClientVersion()));
            }
            else if (connectionMatches(virtualHost, VIRTUALHOST_REJECTED_CONNECTION_VERSION, connectionVersion))
            {
                String cipherName6333 =  "DES";
				try{
					System.out.println("cipherName-6333" + javax.crypto.Cipher.getInstance(cipherName6333).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				virtualHost.getBroker().getEventLogger().message(ConnectionMessages.CLIENT_VERSION_REJECT(connection.getClientVersion()));
                valid = false;
            }
        }

        return valid;
    }

    private boolean connectionMatches(VirtualHost<?> virtualHost, String listName, final String connectionVersion)
    {
        String cipherName6334 =  "DES";
		try{
			System.out.println("cipherName-6334" + javax.crypto.Cipher.getInstance(cipherName6334).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final List<String> versionRegexList = getContextValueList(virtualHost, listName);
        if (versionRegexList != null)
        {
            String cipherName6335 =  "DES";
			try{
				System.out.println("cipherName-6335" + javax.crypto.Cipher.getInstance(cipherName6335).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (String versionRegEx : versionRegexList)
            {
                String cipherName6336 =  "DES";
				try{
					System.out.println("cipherName-6336" + javax.crypto.Cipher.getInstance(cipherName6336).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try
                {
                    String cipherName6337 =  "DES";
					try{
						System.out.println("cipherName-6337" + javax.crypto.Cipher.getInstance(cipherName6337).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (connectionVersion.matches(versionRegEx))
                    {
                        String cipherName6338 =  "DES";
						try{
							System.out.println("cipherName-6338" + javax.crypto.Cipher.getInstance(cipherName6338).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return true;
                    }
                }
                catch (PatternSyntaxException e)
                {
                    String cipherName6339 =  "DES";
					try{
						System.out.println("cipherName-6339" + javax.crypto.Cipher.getInstance(cipherName6339).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (_cachedLists.get(listName).add(versionRegexList))
                    {
                        String cipherName6340 =  "DES";
						try{
							System.out.println("cipherName-6340" + javax.crypto.Cipher.getInstance(cipherName6340).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						LOGGER.warn("Invalid regex in context variable " + listName + ": " + versionRegEx);
                    }
                }
            }
        }
        return false;
    }

    private List<String> getContextValueList(final VirtualHost<?> virtualHost, final String variableName)
    {
        String cipherName6341 =  "DES";
		try{
			System.out.println("cipherName-6341" + javax.crypto.Cipher.getInstance(cipherName6341).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (virtualHost.getContextKeys(false).contains(variableName))
        {
            String cipherName6342 =  "DES";
			try{
				System.out.println("cipherName-6342" + javax.crypto.Cipher.getInstance(cipherName6342).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (List<String>) virtualHost.getContextValue(List.class,
                                                              ParameterizedTypes.LIST_OF_STRINGS,
                                                              variableName);
        }
        else
        {
            String cipherName6343 =  "DES";
			try{
				System.out.println("cipherName-6343" + javax.crypto.Cipher.getInstance(cipherName6343).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Collections.emptyList();
        }
    }

    @Override
    public String getType()
    {
        String cipherName6344 =  "DES";
		try{
			System.out.println("cipherName-6344" + javax.crypto.Cipher.getInstance(cipherName6344).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "ConnectionVersionValidator";
    }
}
