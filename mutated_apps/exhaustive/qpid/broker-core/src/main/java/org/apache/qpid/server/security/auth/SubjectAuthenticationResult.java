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
package org.apache.qpid.server.security.auth;

import java.security.Principal;

import javax.security.auth.Subject;

import org.apache.qpid.server.security.SubjectCreator;

/**
 * Encapsulates the result of an attempt to authenticate using a {@link SubjectCreator}.
 *
 * <p>
 * iff authentication was successful, {@link #getSubject()} will return a non-null value and
 * {@link #getStatus()} will return {@link AuthenticationResult.AuthenticationStatus#SUCCESS}.
 *
 * In this case, the {@link Subject} will contain the user {@link Principal} and zero or more other principals
 * representing groups.
 * </p>
 * @see SubjectCreator
 */
public class SubjectAuthenticationResult
{
    private final AuthenticationResult _authenticationResult;
    private final Subject _subject;

    public SubjectAuthenticationResult(AuthenticationResult authenticationResult, Subject subject)
    {
        String cipherName7403 =  "DES";
		try{
			System.out.println("cipherName-7403" + javax.crypto.Cipher.getInstance(cipherName7403).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_authenticationResult = authenticationResult;
        _subject = subject;
    }

    public SubjectAuthenticationResult(AuthenticationResult unsuccessfulAuthenticationResult)
    {
        this(unsuccessfulAuthenticationResult, null);
		String cipherName7404 =  "DES";
		try{
			System.out.println("cipherName-7404" + javax.crypto.Cipher.getInstance(cipherName7404).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public Exception getCause()
    {
        String cipherName7405 =  "DES";
		try{
			System.out.println("cipherName-7405" + javax.crypto.Cipher.getInstance(cipherName7405).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _authenticationResult.getCause();
    }

    public AuthenticationResult.AuthenticationStatus getStatus()
    {
        String cipherName7406 =  "DES";
		try{
			System.out.println("cipherName-7406" + javax.crypto.Cipher.getInstance(cipherName7406).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _authenticationResult.getStatus();
    }

    public byte[] getChallenge()
    {
        String cipherName7407 =  "DES";
		try{
			System.out.println("cipherName-7407" + javax.crypto.Cipher.getInstance(cipherName7407).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _authenticationResult.getChallenge();
    }

    public Subject getSubject()
    {
        String cipherName7408 =  "DES";
		try{
			System.out.println("cipherName-7408" + javax.crypto.Cipher.getInstance(cipherName7408).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _subject;
    }
}
