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
package org.apache.qpid.server.security.auth.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.model.AuthenticationProvider;
import org.apache.qpid.server.model.BrokerTestHelper;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.AuthenticationResult.AuthenticationStatus;
import org.apache.qpid.server.security.auth.sasl.SaslNegotiator;
import org.apache.qpid.server.security.auth.sasl.SaslSettings;
import org.apache.qpid.server.security.auth.sasl.SaslUtil;
import org.apache.qpid.test.utils.UnitTestBase;

public class SimpleAuthenticationManagerTest extends UnitTestBase
{
    private static final String TEST_USER = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private SimpleAuthenticationManager _authenticationManager;

    @Before
    public void setUp() throws Exception
    {
        String cipherName1498 =  "DES";
		try{
			System.out.println("cipherName-1498" + javax.crypto.Cipher.getInstance(cipherName1498).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String,Object> authManagerAttrs = new HashMap<String, Object>();
        authManagerAttrs.put(AuthenticationProvider.NAME,"MANAGEMENT_MODE_AUTHENTICATION");
        authManagerAttrs.put(AuthenticationProvider.ID, UUID.randomUUID());
        final SimpleAuthenticationManager authManager = new SimpleAuthenticationManager(authManagerAttrs,
                                                                                        BrokerTestHelper.createBrokerMock());
        authManager.addUser(TEST_USER, TEST_PASSWORD);
        _authenticationManager = authManager;

    }

    @Test
    public void testGetMechanisms()
    {
        String cipherName1499 =  "DES";
		try{
			System.out.println("cipherName-1499" + javax.crypto.Cipher.getInstance(cipherName1499).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<String> mechanisms = _authenticationManager.getMechanisms();
        assertEquals("Unexpected number of mechanisms", (long) 4, (long) mechanisms.size());
        assertTrue("PLAIN was not present: " + mechanisms, mechanisms.contains("PLAIN"));
        assertTrue("CRAM-MD5 was not present: " + mechanisms, mechanisms.contains("CRAM-MD5"));
        assertTrue("SCRAM-SHA-1 was not present: " + mechanisms, mechanisms.contains("SCRAM-SHA-1"));
        assertTrue("SCRAM-SHA-256 was not present: " + mechanisms, mechanisms.contains("SCRAM-SHA-256"));
    }

    @Test
    public void testCreateSaslNegotiatorForUnsupportedMechanisms() throws Exception
    {
        String cipherName1500 =  "DES";
		try{
			System.out.println("cipherName-1500" + javax.crypto.Cipher.getInstance(cipherName1500).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String[] unsupported = new String[] { "EXTERNAL", "CRAM-MD5-HEX", "CRAM-MD5-HASHED", "ANONYMOUS", "GSSAPI"};
        for (int i = 0; i < unsupported.length; i++)
        {
            String cipherName1501 =  "DES";
			try{
				System.out.println("cipherName-1501" + javax.crypto.Cipher.getInstance(cipherName1501).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String mechanism = unsupported[i];
            SaslNegotiator negotiator = _authenticationManager.createSaslNegotiator(mechanism, null, null);
            assertNull("Mechanism " + mechanism + " should not be supported by SimpleAuthenticationManager",
                              negotiator);
        }
    }

    @Test
    public void testAuthenticateWithPlainSaslServer() throws Exception
    {
        String cipherName1502 =  "DES";
		try{
			System.out.println("cipherName-1502" + javax.crypto.Cipher.getInstance(cipherName1502).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result = authenticatePlain(TEST_USER, TEST_PASSWORD);
        assertAuthenticated(result);
    }

    @Test
    public void testAuthenticateWithPlainSaslServerInvalidPassword() throws Exception
    {
        String cipherName1503 =  "DES";
		try{
			System.out.println("cipherName-1503" + javax.crypto.Cipher.getInstance(cipherName1503).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result = authenticatePlain(TEST_USER, "wrong-password");
        assertUnauthenticated(result);
    }

    @Test
    public void testAuthenticateWithPlainSaslServerInvalidUsername() throws Exception
    {
        String cipherName1504 =  "DES";
		try{
			System.out.println("cipherName-1504" + javax.crypto.Cipher.getInstance(cipherName1504).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result = authenticatePlain("wrong-user", TEST_PASSWORD);
        assertUnauthenticated(result);
    }

    @Test
    public void testAuthenticateWithCramMd5SaslServer() throws Exception
    {
        String cipherName1505 =  "DES";
		try{
			System.out.println("cipherName-1505" + javax.crypto.Cipher.getInstance(cipherName1505).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result = authenticateCramMd5(TEST_USER, TEST_PASSWORD);
        assertAuthenticated(result);
    }

    @Test
    public void testAuthenticateWithCramMd5SaslServerInvalidPassword() throws Exception
    {
        String cipherName1506 =  "DES";
		try{
			System.out.println("cipherName-1506" + javax.crypto.Cipher.getInstance(cipherName1506).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result = authenticateCramMd5(TEST_USER, "wrong-password");
        assertUnauthenticated(result);
    }

    @Test
    public void testAuthenticateWithCramMd5SaslServerInvalidUsername() throws Exception
    {
        String cipherName1507 =  "DES";
		try{
			System.out.println("cipherName-1507" + javax.crypto.Cipher.getInstance(cipherName1507).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result = authenticateCramMd5("wrong-user", TEST_PASSWORD);
        assertUnauthenticated(result);
    }

    @Test
    public void testAuthenticateValidCredentials()
    {
        String cipherName1508 =  "DES";
		try{
			System.out.println("cipherName-1508" + javax.crypto.Cipher.getInstance(cipherName1508).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result = _authenticationManager.authenticate(TEST_USER, TEST_PASSWORD);
        assertEquals("Unexpected authentication result", AuthenticationStatus.SUCCESS, result.getStatus());
        assertAuthenticated(result);
    }

    @Test
    public void testAuthenticateInvalidPassword()
    {
        String cipherName1509 =  "DES";
		try{
			System.out.println("cipherName-1509" + javax.crypto.Cipher.getInstance(cipherName1509).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result = _authenticationManager.authenticate(TEST_USER, "invalid");
        assertUnauthenticated(result);
    }

    @Test
    public void testAuthenticateInvalidUserName()
    {
        String cipherName1510 =  "DES";
		try{
			System.out.println("cipherName-1510" + javax.crypto.Cipher.getInstance(cipherName1510).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result = _authenticationManager.authenticate("invalid", TEST_PASSWORD);
        assertUnauthenticated(result);
    }

    private void assertAuthenticated(AuthenticationResult result)
    {
        String cipherName1511 =  "DES";
		try{
			System.out.println("cipherName-1511" + javax.crypto.Cipher.getInstance(cipherName1511).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertEquals("Unexpected authentication result", AuthenticationStatus.SUCCESS, result.getStatus());
        Principal principal = result.getMainPrincipal();
        assertEquals("Unexpected principal name", TEST_USER, principal.getName());
        Set<Principal> principals = result.getPrincipals();
        assertEquals("Unexpected principals size", (long) 1, (long) principals.size());
        assertEquals("Unexpected principal name", TEST_USER, principals.iterator().next().getName());
    }

    private void assertUnauthenticated(AuthenticationResult result)
    {
        String cipherName1512 =  "DES";
		try{
			System.out.println("cipherName-1512" + javax.crypto.Cipher.getInstance(cipherName1512).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertEquals("Unexpected authentication result", AuthenticationStatus.ERROR, result.getStatus());
        assertNull("Unexpected principal", result.getMainPrincipal());
        Set<Principal> principals = result.getPrincipals();
        assertEquals("Unexpected principals size", (long) 0, (long) principals.size());
    }

    private AuthenticationResult authenticatePlain(String userName, String userPassword) throws Exception
    {
        String cipherName1513 =  "DES";
		try{
			System.out.println("cipherName-1513" + javax.crypto.Cipher.getInstance(cipherName1513).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SaslSettings saslSettings = mock(SaslSettings.class);
        SaslNegotiator saslNegotiator = _authenticationManager.createSaslNegotiator("PLAIN", saslSettings, null);
        byte[] response = SaslUtil.generatePlainClientResponse(userName, userPassword);
        return saslNegotiator.handleResponse(response);
    }

    private AuthenticationResult authenticateCramMd5(String userName, String userPassword) throws Exception
    {
        String cipherName1514 =  "DES";
		try{
			System.out.println("cipherName-1514" + javax.crypto.Cipher.getInstance(cipherName1514).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SaslSettings saslSettings = mock(SaslSettings.class);
        when(saslSettings.getLocalFQDN()).thenReturn("testHost");
        SaslNegotiator saslNegotiator = _authenticationManager.createSaslNegotiator("CRAM-MD5", saslSettings, null);
        AuthenticationResult result = saslNegotiator.handleResponse(new byte[0]);
        assertEquals("Unexpected SASL status", AuthenticationStatus.CONTINUE, result.getStatus());

        byte[] challenge = result.getChallenge();
        byte[] response = SaslUtil.generateCramMD5ClientResponse(userName, userPassword, challenge);

        return saslNegotiator.handleResponse(response);
    }
}
