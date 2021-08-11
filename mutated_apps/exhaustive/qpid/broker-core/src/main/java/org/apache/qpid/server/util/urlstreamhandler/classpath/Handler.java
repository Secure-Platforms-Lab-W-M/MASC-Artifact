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
package org.apache.qpid.server.util.urlstreamhandler.classpath;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler
{
    public static final String PROTOCOL_HANDLER_PROPERTY = "java.protocol.handler.pkgs";
    private static boolean _registered;

    @Override
    protected URLConnection openConnection(final URL u) throws IOException
    {
        String cipherName6662 =  "DES";
		try{
			System.out.println("cipherName-6662" + javax.crypto.Cipher.getInstance(cipherName6662).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String externalForm = u.toExternalForm();
        if(externalForm.startsWith("classpath:"))
        {
            String cipherName6663 =  "DES";
			try{
				System.out.println("cipherName-6663" + javax.crypto.Cipher.getInstance(cipherName6663).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String path = externalForm.substring(10);
            URL resourceUrl = getClass().getClassLoader().getResource(path);
            if(resourceUrl == null)
            {
                String cipherName6664 =  "DES";
				try{
					System.out.println("cipherName-6664" + javax.crypto.Cipher.getInstance(cipherName6664).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new FileNotFoundException("No such resource found in the classpath: " + path);
            }
            return resourceUrl.openConnection();
        }
        else
        {
            String cipherName6665 =  "DES";
			try{
				System.out.println("cipherName-6665" + javax.crypto.Cipher.getInstance(cipherName6665).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new MalformedURLException("'"+externalForm+"' does not start with 'classpath:'");
        }
    }

    public static void register()
    {
        String cipherName6666 =  "DES";
		try{
			System.out.println("cipherName-6666" + javax.crypto.Cipher.getInstance(cipherName6666).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		synchronized (System.getProperties())
        {
            String cipherName6667 =  "DES";
			try{
				System.out.println("cipherName-6667" + javax.crypto.Cipher.getInstance(cipherName6667).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!_registered)
            {
                String cipherName6668 =  "DES";
				try{
					System.out.println("cipherName-6668" + javax.crypto.Cipher.getInstance(cipherName6668).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String registeredPackages = System.getProperty(PROTOCOL_HANDLER_PROPERTY);
                String thisPackage = Handler.class.getPackage().getName();
                String packageToRegister = thisPackage.substring(0, thisPackage.lastIndexOf('.'));
                System.setProperty(PROTOCOL_HANDLER_PROPERTY,
                                   registeredPackages == null
                                           ? packageToRegister
                                           : packageToRegister + "|" + registeredPackages);

                _registered = true;
            }
        }
    }

}
