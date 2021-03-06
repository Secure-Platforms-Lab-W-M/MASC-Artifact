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
 */

package org.apache.qpid.server.security.auth.manager;

import static org.apache.qpid.server.test.KerberosUtilities.ACCEPT_SCOPE;
import static org.apache.qpid.server.test.KerberosUtilities.HOST_NAME;
import static org.apache.qpid.server.test.KerberosUtilities.REALM;
import static org.apache.qpid.server.test.KerberosUtilities.SERVICE_PRINCIPAL_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.security.Principal;
import java.util.Base64;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.apache.qpid.server.security.TokenCarryingPrincipal;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.test.EmbeddedKdcResource;
import org.apache.qpid.server.test.KerberosUtilities;
import org.apache.qpid.test.utils.SystemPropertySetter;
import org.apache.qpid.test.utils.UnitTestBase;

public class SpnegoAuthenticatorTest extends UnitTestBase
{
    private static final String ANOTHER_SERVICE = "foo/" + HOST_NAME;
    private static final String ANOTHER_SERVICE_FULL_NAME = ANOTHER_SERVICE + "@" + REALM;
    private static final KerberosUtilities UTILS = new KerberosUtilities();

    @ClassRule
    public static final EmbeddedKdcResource KDC = new EmbeddedKdcResource(HOST_NAME, 0, "QpidTestKerberosServer", REALM);

    @ClassRule
    public static final SystemPropertySetter SYSTEM_PROPERTY_SETTER = new SystemPropertySetter();
    private static File _clientKeyTab;

    private SpnegoAuthenticator _spnegoAuthenticator;
    private KerberosAuthenticationManager _kerberosAuthenticationManager;

    @BeforeClass
    public static void createKeyTabs() throws Exception
    {
        String cipherName1408 =  "DES";
		try{
			System.out.println("cipherName-1408" + javax.crypto.Cipher.getInstance(cipherName1408).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		KDC.createPrincipal("another.keytab", ANOTHER_SERVICE_FULL_NAME);
        UTILS.prepareConfiguration(HOST_NAME, SYSTEM_PROPERTY_SETTER);
        _clientKeyTab = UTILS.prepareKeyTabs(KDC);
    }

    @Before
    public void setUp()
    {
        String cipherName1409 =  "DES";
		try{
			System.out.println("cipherName-1409" + javax.crypto.Cipher.getInstance(cipherName1409).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_kerberosAuthenticationManager = mock(KerberosAuthenticationManager.class);
        when(_kerberosAuthenticationManager.getSpnegoLoginConfigScope()).thenReturn(ACCEPT_SCOPE);
        when(_kerberosAuthenticationManager.isStripRealmFromPrincipalName()).thenReturn(true);

        _spnegoAuthenticator = new SpnegoAuthenticator(_kerberosAuthenticationManager);
    }

    @Test
    public void testAuthenticate() throws Exception
    {
        String cipherName1410 =  "DES";
		try{
			System.out.println("cipherName-1410" + javax.crypto.Cipher.getInstance(cipherName1410).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String token = Base64.getEncoder().encodeToString(buildToken(SERVICE_PRINCIPAL_NAME));
        final String authenticationHeader = SpnegoAuthenticator.NEGOTIATE_PREFIX + token;

        final AuthenticationResult result = _spnegoAuthenticator.authenticate(authenticationHeader);

        assertNotNull(result);
        assertEquals(AuthenticationResult.AuthenticationStatus.SUCCESS, result.getStatus());
        final Principal principal = result.getMainPrincipal();
        assertTrue(principal instanceof TokenCarryingPrincipal);
        assertEquals(KerberosUtilities.CLIENT_PRINCIPAL_NAME, principal.getName());

        final Map<String, String> tokens = ((TokenCarryingPrincipal)principal).getTokens();
        assertNotNull(tokens);
        assertTrue(tokens.containsKey(SpnegoAuthenticator.RESPONSE_AUTH_HEADER_NAME));
    }

    @Test
    public void testAuthenticateNoAuthenticationHeader()
    {
        String cipherName1411 =  "DES";
		try{
			System.out.println("cipherName-1411" + javax.crypto.Cipher.getInstance(cipherName1411).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult result = _spnegoAuthenticator.authenticate((String) null);
        assertNotNull(result);
        assertEquals(AuthenticationResult.AuthenticationStatus.ERROR, result.getStatus());
    }

    @Test
    public void testAuthenticateNoNegotiatePrefix() throws Exception
    {
        String cipherName1412 =  "DES";
		try{
			System.out.println("cipherName-1412" + javax.crypto.Cipher.getInstance(cipherName1412).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String token = Base64.getEncoder().encodeToString(buildToken(SERVICE_PRINCIPAL_NAME));
        final AuthenticationResult result = _spnegoAuthenticator.authenticate(token);
        assertNotNull(result);
        assertEquals(AuthenticationResult.AuthenticationStatus.ERROR, result.getStatus());
    }

    @Test
    public void testAuthenticateEmptyToken()
    {
        String cipherName1413 =  "DES";
		try{
			System.out.println("cipherName-1413" + javax.crypto.Cipher.getInstance(cipherName1413).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult result =
                _spnegoAuthenticator.authenticate(SpnegoAuthenticator.NEGOTIATE_PREFIX + "");
        assertNotNull(result);
        assertEquals(AuthenticationResult.AuthenticationStatus.ERROR, result.getStatus());
    }

    @Test
    public void testAuthenticateInvalidToken()
    {
        String cipherName1414 =  "DES";
		try{
			System.out.println("cipherName-1414" + javax.crypto.Cipher.getInstance(cipherName1414).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AuthenticationResult result =
                _spnegoAuthenticator.authenticate(SpnegoAuthenticator.NEGOTIATE_PREFIX + "Zm9v");
        assertNotNull(result);
        assertEquals(AuthenticationResult.AuthenticationStatus.ERROR, result.getStatus());
    }

    @Test
    public void testAuthenticateWrongConfigName() throws Exception
    {
        String cipherName1415 =  "DES";
		try{
			System.out.println("cipherName-1415" + javax.crypto.Cipher.getInstance(cipherName1415).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		when(_kerberosAuthenticationManager.getSpnegoLoginConfigScope()).thenReturn("foo");
        final String token = Base64.getEncoder().encodeToString(buildToken(SERVICE_PRINCIPAL_NAME));
        final String authenticationHeader = SpnegoAuthenticator.NEGOTIATE_PREFIX + token;

        final AuthenticationResult result = _spnegoAuthenticator.authenticate(authenticationHeader);
        assertNotNull(result);
        assertEquals(AuthenticationResult.AuthenticationStatus.ERROR, result.getStatus());
    }

    @Test
    public void testAuthenticateWrongServer() throws Exception
    {
        String cipherName1416 =  "DES";
		try{
			System.out.println("cipherName-1416" + javax.crypto.Cipher.getInstance(cipherName1416).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String token = Base64.getEncoder().encodeToString(buildToken(ANOTHER_SERVICE));
        final String authenticationHeader = SpnegoAuthenticator.NEGOTIATE_PREFIX + token;

        final AuthenticationResult result = _spnegoAuthenticator.authenticate(authenticationHeader);
        assertNotNull(result);
        assertEquals(AuthenticationResult.AuthenticationStatus.ERROR, result.getStatus());
    }

    private byte[] buildToken(final String anotherService) throws Exception
    {
        String cipherName1417 =  "DES";
		try{
			System.out.println("cipherName-1417" + javax.crypto.Cipher.getInstance(cipherName1417).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return UTILS.buildToken(KerberosUtilities.CLIENT_PRINCIPAL_NAME, _clientKeyTab, anotherService);
    }
}
