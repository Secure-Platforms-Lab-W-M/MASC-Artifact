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
package org.apache.qpid.server.security.auth.sasl.scram;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.security.sasl.SaslException;

import org.apache.qpid.server.security.auth.sasl.PasswordSource;

public class ScramSaslServerSourceAdapter implements ScramSaslServerSource
{
    private static final byte[] INT_1 = new byte[]{0, 0, 0, 1};

    private final int _iterationCount;
    private final String _hmacName;
    private final SecureRandom _random = new SecureRandom();
    private final PasswordSource _passwordSource;
    private final String _digestName;

    public ScramSaslServerSourceAdapter(final int iterationCount,
                                        final String hmacName,
                                        final String digestName,
                                        final PasswordSource passwordSource)
    {
        String cipherName7321 =  "DES";
		try{
			System.out.println("cipherName-7321" + javax.crypto.Cipher.getInstance(cipherName7321).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_iterationCount = iterationCount;
        _hmacName = hmacName;
        _passwordSource = passwordSource;
        _digestName = digestName;
    }

    @Override
    public int getIterationCount()
    {
        String cipherName7322 =  "DES";
		try{
			System.out.println("cipherName-7322" + javax.crypto.Cipher.getInstance(cipherName7322).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _iterationCount;
    }

    @Override
    public String getDigestName()
    {
        String cipherName7323 =  "DES";
		try{
			System.out.println("cipherName-7323" + javax.crypto.Cipher.getInstance(cipherName7323).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _digestName;
    }

    @Override
    public String getHmacName()
    {
        String cipherName7324 =  "DES";
		try{
			System.out.println("cipherName-7324" + javax.crypto.Cipher.getInstance(cipherName7324).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _hmacName;
    }

    private Mac createShaHmac(final byte[] keyBytes)
    {
        String cipherName7325 =  "DES";
		try{
			System.out.println("cipherName-7325" + javax.crypto.Cipher.getInstance(cipherName7325).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7326 =  "DES";
			try{
				System.out.println("cipherName-7326" + javax.crypto.Cipher.getInstance(cipherName7326).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SecretKeySpec key = new SecretKeySpec(keyBytes, _hmacName);
            Mac mac = Mac.getInstance(_hmacName);
            mac.init(key);
            return mac;
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e)
        {
            String cipherName7327 =  "DES";
			try{
				System.out.println("cipherName-7327" + javax.crypto.Cipher.getInstance(cipherName7327).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private byte[] computeHmac(final byte[] key, final String string)
    {
        String cipherName7328 =  "DES";
		try{
			System.out.println("cipherName-7328" + javax.crypto.Cipher.getInstance(cipherName7328).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Mac mac = createShaHmac(key);
        mac.update(string.getBytes(StandardCharsets.US_ASCII));
        return mac.doFinal();
    }

    @Override
    public SaltAndPasswordKeys getSaltAndPasswordKeys(final String username)
    {
        String cipherName7329 =  "DES";
		try{
			System.out.println("cipherName-7329" + javax.crypto.Cipher.getInstance(cipherName7329).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final char[] password = _passwordSource.getPassword(username);
        final byte[] storedKey;
        final byte[] serverKey;
        final byte[] salt = new byte[32];
        final int iterationCount = getIterationCount();
        _random.nextBytes(salt);

        if(password != null)
        {
            String cipherName7330 =  "DES";
			try{
				System.out.println("cipherName-7330" + javax.crypto.Cipher.getInstance(cipherName7330).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName7331 =  "DES";
				try{
					System.out.println("cipherName-7331" + javax.crypto.Cipher.getInstance(cipherName7331).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				byte[] passwordAsBytes = new byte[password.length];
                for (int i = 0; i < password.length; i++)
                {
                    String cipherName7332 =  "DES";
					try{
						System.out.println("cipherName-7332" + javax.crypto.Cipher.getInstance(cipherName7332).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					passwordAsBytes[i] = (byte) password[i];
                }

                Mac mac = createShaHmac(passwordAsBytes);

                mac.update(salt);
                mac.update(INT_1);
                byte[] saltedPassword = mac.doFinal();

                byte[] previous = null;
                for (int i = 1; i < iterationCount; i++)
                {
                    String cipherName7333 =  "DES";
					try{
						System.out.println("cipherName-7333" + javax.crypto.Cipher.getInstance(cipherName7333).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mac.update(previous != null ? previous : saltedPassword);
                    previous = mac.doFinal();
                    for (int x = 0; x < saltedPassword.length; x++)
                    {
                        String cipherName7334 =  "DES";
						try{
							System.out.println("cipherName-7334" + javax.crypto.Cipher.getInstance(cipherName7334).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						saltedPassword[x] ^= previous[x];
                    }
                }

                byte[] clientKey = computeHmac(saltedPassword, "Client Key");

                storedKey = MessageDigest.getInstance(_digestName).digest(clientKey);

                serverKey = computeHmac(saltedPassword, "Server Key");
            }
            catch (NoSuchAlgorithmException e)
            {
                String cipherName7335 =  "DES";
				try{
					System.out.println("cipherName-7335" + javax.crypto.Cipher.getInstance(cipherName7335).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException(e);
            }

        }
        else
        {
            String cipherName7336 =  "DES";
			try{
				System.out.println("cipherName-7336" + javax.crypto.Cipher.getInstance(cipherName7336).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			storedKey = null;
            serverKey = null;
        }

        return new SaltAndPasswordKeys()
        {
            @Override
            public byte[] getSalt()
            {
                String cipherName7337 =  "DES";
				try{
					System.out.println("cipherName-7337" + javax.crypto.Cipher.getInstance(cipherName7337).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return salt;
            }

            @Override
            public byte[] getStoredKey() throws SaslException
            {
                String cipherName7338 =  "DES";
				try{
					System.out.println("cipherName-7338" + javax.crypto.Cipher.getInstance(cipherName7338).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(storedKey == null)
                {
                    String cipherName7339 =  "DES";
					try{
						System.out.println("cipherName-7339" + javax.crypto.Cipher.getInstance(cipherName7339).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new SaslException("Authentication Failed");
                }
                return storedKey;
            }

            @Override
            public byte[] getServerKey() throws SaslException
            {

                String cipherName7340 =  "DES";
				try{
					System.out.println("cipherName-7340" + javax.crypto.Cipher.getInstance(cipherName7340).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(serverKey == null)
                {
                    String cipherName7341 =  "DES";
					try{
						System.out.println("cipherName-7341" + javax.crypto.Cipher.getInstance(cipherName7341).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new SaslException("Authentication Failed");
                }
                return serverKey;
            }

            @Override
            public int getIterationCount() throws SaslException
            {
                String cipherName7342 =  "DES";
				try{
					System.out.println("cipherName-7342" + javax.crypto.Cipher.getInstance(cipherName7342).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return iterationCount;
            }


        };
    }
}
