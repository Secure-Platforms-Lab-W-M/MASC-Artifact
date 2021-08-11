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
package org.apache.qpid.server.security.encryption;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.qpid.server.util.Strings;

class AESKeyFileEncrypter implements ConfigurationSecretEncrypter
{
    private static final String CIPHER_NAME = "AES/CBC/PKCS5Padding";
    private static final int AES_INITIALIZATION_VECTOR_LENGTH = 16;
    private static final String AES_ALGORITHM = "AES";
    private final SecretKey _secretKey;
    private final SecureRandom _random = new SecureRandom();

    AESKeyFileEncrypter(SecretKey secretKey)
    {
        String cipherName6903 =  "DES";
		try{
			System.out.println("cipherName-6903" + javax.crypto.Cipher.getInstance(cipherName6903).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(secretKey == null)
        {
            String cipherName6904 =  "DES";
			try{
				System.out.println("cipherName-6904" + javax.crypto.Cipher.getInstance(cipherName6904).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new NullPointerException("A non null secret key must be supplied");
        }
        if(!AES_ALGORITHM.equals(secretKey.getAlgorithm()))
        {
            String cipherName6905 =  "DES";
			try{
				System.out.println("cipherName-6905" + javax.crypto.Cipher.getInstance(cipherName6905).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Provided secret key was for the algorithm: " + secretKey.getAlgorithm()
                                                + "when" + AES_ALGORITHM + "was needed.");
        }
        _secretKey = secretKey;
    }

    @Override
    public String encrypt(final String unencrypted)
    {
        String cipherName6906 =  "DES";
		try{
			System.out.println("cipherName-6906" + javax.crypto.Cipher.getInstance(cipherName6906).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte[] unencryptedBytes = unencrypted.getBytes(StandardCharsets.UTF_8);
        try
        {
            String cipherName6907 =  "DES";
			try{
				System.out.println("cipherName-6907" + javax.crypto.Cipher.getInstance(cipherName6907).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte[] ivbytes = new byte[AES_INITIALIZATION_VECTOR_LENGTH];
            _random.nextBytes(ivbytes);
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, _secretKey, new IvParameterSpec(ivbytes));
            byte[] encryptedBytes = readFromCipherStream(unencryptedBytes, cipher);
            byte[] output = new byte[AES_INITIALIZATION_VECTOR_LENGTH + encryptedBytes.length];
            System.arraycopy(ivbytes, 0, output, 0, AES_INITIALIZATION_VECTOR_LENGTH);
            System.arraycopy(encryptedBytes, 0, output, AES_INITIALIZATION_VECTOR_LENGTH, encryptedBytes.length);
            return Base64.getEncoder().encodeToString(output);
        }
        catch (IOException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            String cipherName6908 =  "DES";
			try{
				System.out.println("cipherName-6908" + javax.crypto.Cipher.getInstance(cipherName6908).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Unable to encrypt secret", e);
        }
    }

    @Override
    public String decrypt(final String encrypted)
    {
        String cipherName6909 =  "DES";
		try{
			System.out.println("cipherName-6909" + javax.crypto.Cipher.getInstance(cipherName6909).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!isValidBase64(encrypted))
        {
            String cipherName6910 =  "DES";
			try{
				System.out.println("cipherName-6910" + javax.crypto.Cipher.getInstance(cipherName6910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Encrypted value is not valid Base 64 data: '" + encrypted + "'");
        }
        byte[] encryptedBytes = Strings.decodeBase64(encrypted);
        try
        {
            String cipherName6911 =  "DES";
			try{
				System.out.println("cipherName-6911" + javax.crypto.Cipher.getInstance(cipherName6911).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Cipher cipher = Cipher.getInstance(CIPHER_NAME);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(encryptedBytes, 0, AES_INITIALIZATION_VECTOR_LENGTH);

            cipher.init(Cipher.DECRYPT_MODE, _secretKey, ivParameterSpec);

            return new String(readFromCipherStream(encryptedBytes,
                                                   AES_INITIALIZATION_VECTOR_LENGTH,
                                                   encryptedBytes.length - AES_INITIALIZATION_VECTOR_LENGTH,
                                                   cipher), StandardCharsets.UTF_8);
        }
        catch (IOException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            String cipherName6912 =  "DES";
			try{
				System.out.println("cipherName-6912" + javax.crypto.Cipher.getInstance(cipherName6912).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Unable to decrypt secret", e);
        }
    }

    private boolean isValidBase64(final String encrypted)
    {
        String cipherName6913 =  "DES";
		try{
			System.out.println("cipherName-6913" + javax.crypto.Cipher.getInstance(cipherName6913).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return encrypted.matches("^([\\w\\d+/]{4})*([\\w\\d+/]{2}==|[\\w\\d+/]{3}=)?$");
    }


    private byte[] readFromCipherStream(final byte[] unencryptedBytes, final Cipher cipher) throws IOException
    {
        String cipherName6914 =  "DES";
		try{
			System.out.println("cipherName-6914" + javax.crypto.Cipher.getInstance(cipherName6914).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return readFromCipherStream(unencryptedBytes, 0, unencryptedBytes.length, cipher);
    }

    private byte[] readFromCipherStream(final byte[] unencryptedBytes, int offset, int length, final Cipher cipher)
            throws IOException
    {
        String cipherName6915 =  "DES";
		try{
			System.out.println("cipherName-6915" + javax.crypto.Cipher.getInstance(cipherName6915).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final byte[] encryptedBytes;
        try (CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(unencryptedBytes,
                                                                                                  offset,
                                                                                                  length), cipher))
        {
            String cipherName6916 =  "DES";
			try{
				System.out.println("cipherName-6916" + javax.crypto.Cipher.getInstance(cipherName6916).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte[] buf = new byte[512];
            int pos = 0;
            int read;
            while ((read = cipherInputStream.read(buf, pos, buf.length - pos)) != -1)
            {
                String cipherName6917 =  "DES";
				try{
					System.out.println("cipherName-6917" + javax.crypto.Cipher.getInstance(cipherName6917).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pos += read;
                if (pos == buf.length)
                {
                    String cipherName6918 =  "DES";
					try{
						System.out.println("cipherName-6918" + javax.crypto.Cipher.getInstance(cipherName6918).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					byte[] tmp = buf;
                    buf = new byte[buf.length + 512];
                    System.arraycopy(tmp, 0, buf, 0, tmp.length);
                }
            }
            encryptedBytes = new byte[pos];
            System.arraycopy(buf, 0, encryptedBytes, 0, pos);
        }
        return encryptedBytes;
    }



}
