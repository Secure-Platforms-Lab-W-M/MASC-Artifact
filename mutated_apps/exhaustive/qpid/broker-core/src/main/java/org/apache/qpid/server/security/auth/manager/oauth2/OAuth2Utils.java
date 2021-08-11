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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.util.ServerScopedRuntimeException;

public class OAuth2Utils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Utils.class);

    public static String buildRequestQuery(final Map<String, String> requestBodyParameters)
    {
        String cipherName7686 =  "DES";
		try{
			System.out.println("cipherName-7686" + javax.crypto.Cipher.getInstance(cipherName7686).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7687 =  "DES";
			try{
				System.out.println("cipherName-7687" + javax.crypto.Cipher.getInstance(cipherName7687).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final String charset = StandardCharsets.UTF_8.name();
            StringBuilder bodyBuilder = new StringBuilder();
            Iterator<Map.Entry<String, String>> iterator = requestBodyParameters.entrySet().iterator();
            while (iterator.hasNext())
            {
                String cipherName7688 =  "DES";
				try{
					System.out.println("cipherName-7688" + javax.crypto.Cipher.getInstance(cipherName7688).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map.Entry<String, String> entry = iterator.next();
                bodyBuilder.append(URLEncoder.encode(entry.getKey(), charset));
                bodyBuilder.append("=");
                bodyBuilder.append(URLEncoder.encode(entry.getValue(), charset));
                if (iterator.hasNext())
                {
                    String cipherName7689 =  "DES";
					try{
						System.out.println("cipherName-7689" + javax.crypto.Cipher.getInstance(cipherName7689).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bodyBuilder.append("&");
                }
            }
            return bodyBuilder.toString();
        }
        catch (UnsupportedEncodingException e)
        {
            String cipherName7690 =  "DES";
			try{
				System.out.println("cipherName-7690" + javax.crypto.Cipher.getInstance(cipherName7690).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException("Failed to encode as UTF-8", e);
        }
    }

    public static InputStream getResponseStream(final HttpURLConnection connection) throws IOException
    {
        String cipherName7691 =  "DES";
		try{
			System.out.println("cipherName-7691" + javax.crypto.Cipher.getInstance(cipherName7691).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7692 =  "DES";
			try{
				System.out.println("cipherName-7692" + javax.crypto.Cipher.getInstance(cipherName7692).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return connection.getInputStream();
        }
        catch (IOException ioe)
        {
            String cipherName7693 =  "DES";
			try{
				System.out.println("cipherName-7693" + javax.crypto.Cipher.getInstance(cipherName7693).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			InputStream errorStream = connection.getErrorStream();
            if (errorStream != null)
            {
                String cipherName7694 =  "DES";
				try{
					System.out.println("cipherName-7694" + javax.crypto.Cipher.getInstance(cipherName7694).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return errorStream;
            }
            throw ioe;
        }
    }
}
