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

package org.apache.qpid.server.security.auth.sasl.plain;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.manager.UsernamePasswordAuthenticationProvider;
import org.apache.qpid.test.utils.UnitTestBase;

public class PlainNegotiatorTest extends UnitTestBase
{
    private static final String VALID_PASSWORD = "testPassword";
    private static final String VALID_USERNAME = "testUsername";
    public static final String RESPONSE_FORMAT_STRING = "\0%s\0%s";
    private static final String VALID_RESPONSE = String.format(RESPONSE_FORMAT_STRING, VALID_USERNAME, VALID_PASSWORD);
    private UsernamePasswordAuthenticationProvider _authenticationProvider;
    private PlainNegotiator _negotiator;
    private AuthenticationResult _successfulResult;
    private AuthenticationResult _errorResult;

    @Before
    public void setUp() throws Exception
    {
        String cipherName1180 =  "DES";
		try{
			System.out.println("cipherName-1180" + javax.crypto.Cipher.getInstance(cipherName1180).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_successfulResult = mock(AuthenticationResult.class);
        _errorResult = mock(AuthenticationResult.class);
        _authenticationProvider = mock(UsernamePasswordAuthenticationProvider.class);
        when(_authenticationProvider.authenticate(eq(VALID_USERNAME), eq(VALID_PASSWORD))).thenReturn(_successfulResult);
        when(_authenticationProvider.authenticate(eq(VALID_USERNAME), not(eq(VALID_PASSWORD)))).thenReturn(_errorResult);
        when(_authenticationProvider.authenticate(not(eq(VALID_USERNAME)), anyString())).thenReturn(_errorResult);
        _negotiator = new PlainNegotiator(_authenticationProvider);
    }

    @After
    public void tearDown() throws Exception
    {
        String cipherName1181 =  "DES";
		try{
			System.out.println("cipherName-1181" + javax.crypto.Cipher.getInstance(cipherName1181).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_negotiator != null)
        {
            String cipherName1182 =  "DES";
			try{
				System.out.println("cipherName-1182" + javax.crypto.Cipher.getInstance(cipherName1182).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_negotiator.dispose();
        }
    }

    @Test
    public void testHandleResponse() throws Exception
    {
        String cipherName1183 =  "DES";
		try{
			System.out.println("cipherName-1183" + javax.crypto.Cipher.getInstance(cipherName1183).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult result = _negotiator.handleResponse(VALID_RESPONSE.getBytes(US_ASCII));
        verify(_authenticationProvider).authenticate(eq(VALID_USERNAME), eq(VALID_PASSWORD));
        assertEquals("Unexpected authentication result", _successfulResult, result);
    }

    @Test
    public void testMultipleAuthenticationAttempts() throws Exception
    {
        String cipherName1184 =  "DES";
		try{
			System.out.println("cipherName-1184" + javax.crypto.Cipher.getInstance(cipherName1184).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult firstResult = _negotiator.handleResponse(VALID_RESPONSE.getBytes(US_ASCII));
        assertEquals("Unexpected first authentication result", _successfulResult, firstResult);
        final AuthenticationResult secondResult = _negotiator.handleResponse(VALID_RESPONSE.getBytes(US_ASCII));
        assertEquals("Unexpected second authentication result",
                            AuthenticationResult.AuthenticationStatus.ERROR,
                            secondResult.getStatus());
    }

    @Test
    public void testHandleInvalidUser() throws Exception
    {
        String cipherName1185 =  "DES";
		try{
			System.out.println("cipherName-1185" + javax.crypto.Cipher.getInstance(cipherName1185).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult result = _negotiator.handleResponse(String.format(RESPONSE_FORMAT_STRING, "invalidUser", VALID_PASSWORD).getBytes(US_ASCII));
        assertEquals("Unexpected authentication result", _errorResult, result);
    }

    @Test
    public void testHandleInvalidPassword() throws Exception
    {
        String cipherName1186 =  "DES";
		try{
			System.out.println("cipherName-1186" + javax.crypto.Cipher.getInstance(cipherName1186).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult result = _negotiator.handleResponse(String.format(RESPONSE_FORMAT_STRING, VALID_USERNAME, "invalidPassword").getBytes(US_ASCII));
        assertEquals("Unexpected authentication result", _errorResult, result);
    }

    @Test
    public void testHandleNeverSendAResponse() throws Exception
    {
        String cipherName1187 =  "DES";
		try{
			System.out.println("cipherName-1187" + javax.crypto.Cipher.getInstance(cipherName1187).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult firstResult = _negotiator.handleResponse(new byte[0]);
        assertEquals("Unexpected authentication status",
                            AuthenticationResult.AuthenticationStatus.CONTINUE,
                            firstResult.getStatus());
        assertArrayEquals("Unexpected authentication challenge", new byte[0], firstResult.getChallenge());

        final AuthenticationResult secondResult = _negotiator.handleResponse(new byte[0]);
        assertEquals("Unexpected first authentication result",
                            AuthenticationResult.AuthenticationStatus.ERROR,
                            secondResult.getStatus());
    }

    @Test
    public void testHandleNoInitialResponse() throws Exception
    {
        String cipherName1188 =  "DES";
		try{
			System.out.println("cipherName-1188" + javax.crypto.Cipher.getInstance(cipherName1188).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult result = _negotiator.handleResponse(new byte[0]);
        assertEquals("Unexpected authentication status",
                            AuthenticationResult.AuthenticationStatus.CONTINUE,
                            result.getStatus());
        assertArrayEquals("Unexpected authentication challenge", new byte[0], result.getChallenge());

        final AuthenticationResult firstResult = _negotiator.handleResponse(VALID_RESPONSE.getBytes());
        assertEquals("Unexpected first authentication result", _successfulResult, firstResult);
    }

    @Test
    public void testHandleNoInitialResponseNull() throws Exception
    {
        String cipherName1189 =  "DES";
		try{
			System.out.println("cipherName-1189" + javax.crypto.Cipher.getInstance(cipherName1189).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult result = _negotiator.handleResponse(null);
        assertEquals("Unexpected authentication status",
                            AuthenticationResult.AuthenticationStatus.CONTINUE,
                            result.getStatus());
        assertArrayEquals("Unexpected authentication challenge", new byte[0], result.getChallenge());

        final AuthenticationResult firstResult = _negotiator.handleResponse(VALID_RESPONSE.getBytes());
        assertEquals("Unexpected first authentication result", _successfulResult, firstResult);
    }
}
