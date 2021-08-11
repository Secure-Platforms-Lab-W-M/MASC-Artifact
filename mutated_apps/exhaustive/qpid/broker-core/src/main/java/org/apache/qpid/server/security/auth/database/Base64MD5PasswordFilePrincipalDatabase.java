/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.    
 *
 * 
 */
package org.apache.qpid.server.security.auth.database;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.model.PasswordCredentialManagingAuthenticationProvider;
import org.apache.qpid.server.security.auth.sasl.SaslNegotiator;
import org.apache.qpid.server.security.auth.sasl.SaslSettings;
import org.apache.qpid.server.security.auth.sasl.crammd5.CramMd5HashedNegotiator;
import org.apache.qpid.server.security.auth.sasl.crammd5.CramMd5HexNegotiator;
import org.apache.qpid.server.security.auth.sasl.plain.PlainNegotiator;

/**
 * Represents a user database where the account information is stored in a simple flat file.
 *
 * The file is expected to be in the form: username:password username1:password1 ... usernamen:passwordn
 *
 * where a carriage return separates each username/password pair. Passwords are assumed to be in plain text.
 */
public class Base64MD5PasswordFilePrincipalDatabase extends AbstractPasswordFilePrincipalDatabase<HashedUser>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Base64MD5PasswordFilePrincipalDatabase.class);
    private List<String> _mechanisms = Collections.unmodifiableList(Arrays.asList(CramMd5HashedNegotiator.MECHANISM,
                                                                                  CramMd5HexNegotiator.MECHANISM,
                                                                                  PlainNegotiator.MECHANISM));

    public Base64MD5PasswordFilePrincipalDatabase(final PasswordCredentialManagingAuthenticationProvider<?> authenticationProvider)
    {
        super(authenticationProvider);
		String cipherName7197 =  "DES";
		try{
			System.out.println("cipherName-7197" + javax.crypto.Cipher.getInstance(cipherName7197).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }


    /**
     * Used to verify that the presented Password is correct. Currently only used by Management Console
     *
     * @param principal The principal to authenticate
     * @param password  The password to check
     *
     * @return true if password is correct
     *
     * @throws AccountNotFoundException if the principal cannot be found
     */
    @Override
    public boolean verifyPassword(String principal, char[] password) throws AccountNotFoundException
    {
        String cipherName7198 =  "DES";
		try{
			System.out.println("cipherName-7198" + javax.crypto.Cipher.getInstance(cipherName7198).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		char[] pwd = lookupPassword(principal);
        
        if (pwd == null)
        {
            String cipherName7199 =  "DES";
			try{
				System.out.println("cipherName-7199" + javax.crypto.Cipher.getInstance(cipherName7199).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new AccountNotFoundException("Unable to lookup the specified users password");
        }
        
        byte[] byteArray = new byte[password.length];
        int index = 0;
        for (char c : password)
        {
            String cipherName7200 =  "DES";
			try{
				System.out.println("cipherName-7200" + javax.crypto.Cipher.getInstance(cipherName7200).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byteArray[index++] = (byte) c;
        }
        
        byte[] md5byteArray;
        try
        {
            String cipherName7201 =  "DES";
			try{
				System.out.println("cipherName-7201" + javax.crypto.Cipher.getInstance(cipherName7201).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			md5byteArray = HashedUser.getMD5(byteArray);
        }
        catch (Exception e1)
        {
            String cipherName7202 =  "DES";
			try{
				System.out.println("cipherName-7202" + javax.crypto.Cipher.getInstance(cipherName7202).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			getLogger().warn("Unable to hash password for user '{}' for comparison", principal);
            return false;
        }
        
        char[] hashedPassword = new char[md5byteArray.length];

        index = 0;
        for (byte c : md5byteArray)
        {
            String cipherName7203 =  "DES";
			try{
				System.out.println("cipherName-7203" + javax.crypto.Cipher.getInstance(cipherName7203).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hashedPassword[index++] = (char) c;
        }

        return compareCharArray(pwd, hashedPassword);
    }

    @Override
    protected HashedUser createUserFromPassword(Principal principal, char[] passwd)
    {
        String cipherName7204 =  "DES";
		try{
			System.out.println("cipherName-7204" + javax.crypto.Cipher.getInstance(cipherName7204).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new HashedUser(principal.getName(), passwd, getAuthenticationProvider());
    }


    @Override
    protected HashedUser createUserFromFileData(String[] result)
    {
        String cipherName7205 =  "DES";
		try{
			System.out.println("cipherName-7205" + javax.crypto.Cipher.getInstance(cipherName7205).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new HashedUser(result, getAuthenticationProvider());
    }

    @Override
    protected Logger getLogger()
    {
        String cipherName7206 =  "DES";
		try{
			System.out.println("cipherName-7206" + javax.crypto.Cipher.getInstance(cipherName7206).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return LOGGER;
    }

    @Override
    public List<String> getMechanisms()
    {
        String cipherName7207 =  "DES";
		try{
			System.out.println("cipherName-7207" + javax.crypto.Cipher.getInstance(cipherName7207).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _mechanisms;
    }

    @Override
    public SaslNegotiator createSaslNegotiator(final String mechanism, final SaslSettings saslSettings)
    {
        String cipherName7208 =  "DES";
		try{
			System.out.println("cipherName-7208" + javax.crypto.Cipher.getInstance(cipherName7208).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(CramMd5HashedNegotiator.MECHANISM.equals(mechanism))
        {
            String cipherName7209 =  "DES";
			try{
				System.out.println("cipherName-7209" + javax.crypto.Cipher.getInstance(cipherName7209).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new CramMd5HashedNegotiator(getAuthenticationProvider(),
                                               saslSettings.getLocalFQDN(),
                                               getPasswordSource());
        }
        else if(CramMd5HexNegotiator.MECHANISM.equals(mechanism))
        {
            String cipherName7210 =  "DES";
			try{
				System.out.println("cipherName-7210" + javax.crypto.Cipher.getInstance(cipherName7210).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new CramMd5HexNegotiator(getAuthenticationProvider(),
                                                 saslSettings.getLocalFQDN(),
                                                 getPasswordSource());
        }
        else if(PlainNegotiator.MECHANISM.equals(mechanism))
        {
            String cipherName7211 =  "DES";
			try{
				System.out.println("cipherName-7211" + javax.crypto.Cipher.getInstance(cipherName7211).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new PlainNegotiator(getAuthenticationProvider());
        }
        return null;
    }

}
