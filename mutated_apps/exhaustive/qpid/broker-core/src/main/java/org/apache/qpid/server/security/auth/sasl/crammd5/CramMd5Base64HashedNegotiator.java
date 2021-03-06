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
 *
 */

package org.apache.qpid.server.security.auth.sasl.crammd5;

import org.apache.qpid.server.model.PasswordCredentialManagingAuthenticationProvider;
import org.apache.qpid.server.security.auth.sasl.PasswordSource;
import org.apache.qpid.server.util.Strings;

public class CramMd5Base64HashedNegotiator extends AbstractCramMd5Negotiator
{
    public static final String MECHANISM = "CRAM-MD5-HASHED";
    private static final PasswordTransformer BASE64_PASSWORD_TRANSFORMER =
            new PasswordTransformer()
            {
                @Override
                public char[] transform(final char[] passwordData)
                {
                    String cipherName7282 =  "DES";
					try{
						System.out.println("cipherName-7282" + javax.crypto.Cipher.getInstance(cipherName7282).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					byte[] passwordBytes = Strings.decodeBase64(new String(passwordData));
                    char[] password = new char[passwordBytes.length];
                    for (int i = 0; i < passwordBytes.length; i++)
                    {
                        String cipherName7283 =  "DES";
						try{
							System.out.println("cipherName-7283" + javax.crypto.Cipher.getInstance(cipherName7283).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						password[i] = (char) passwordBytes[i];
                    }
                    return password;
                }
            };

    public CramMd5Base64HashedNegotiator(final PasswordCredentialManagingAuthenticationProvider<?> authenticationProvider,
                                         final String localFQDN,
                                         final PasswordSource passwordSource)
    {
        super(authenticationProvider, localFQDN, passwordSource, BASE64_PASSWORD_TRANSFORMER);
		String cipherName7284 =  "DES";
		try{
			System.out.println("cipherName-7284" + javax.crypto.Cipher.getInstance(cipherName7284).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }
}
