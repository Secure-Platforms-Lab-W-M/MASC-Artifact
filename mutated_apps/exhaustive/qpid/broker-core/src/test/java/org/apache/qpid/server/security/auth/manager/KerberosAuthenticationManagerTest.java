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

import static org.apache.qpid.server.security.auth.manager.KerberosAuthenticationManager.GSSAPI_MECHANISM;
import static org.apache.qpid.server.test.KerberosUtilities.ACCEPT_SCOPE;
import static org.apache.qpid.server.test.KerberosUtilities.CLIENT_PRINCIPAL_FULL_NAME;
import static org.apache.qpid.server.test.KerberosUtilities.CLIENT_PRINCIPAL_NAME;
import static org.apache.qpid.server.test.KerberosUtilities.HOST_NAME;
import static org.apache.qpid.server.test.KerberosUtilities.LOGIN_CONFIG;
import static org.apache.qpid.server.test.KerberosUtilities.REALM;
import static org.apache.qpid.server.test.KerberosUtilities.SERVER_PROTOCOL;
import static org.apache.qpid.server.test.KerberosUtilities.SERVICE_PRINCIPAL_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.LoginContext;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.AuthenticationProvider;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.BrokerTestHelper;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.sasl.SaslNegotiator;
import org.apache.qpid.server.security.auth.sasl.SaslSettings;
import org.apache.qpid.server.test.EmbeddedKdcResource;
import org.apache.qpid.server.test.KerberosUtilities;
import org.apache.qpid.server.util.StringUtil;
import org.apache.qpid.test.utils.SystemPropertySetter;
import org.apache.qpid.test.utils.UnitTestBase;

public class KerberosAuthenticationManagerTest extends UnitTestBase
{

    private static final KerberosUtilities UTILS = new KerberosUtilities();

    @ClassRule
    public static final EmbeddedKdcResource KDC = new EmbeddedKdcResource(HOST_NAME,
                                                                          0,
                                                                          "QpidTestKerberosServer",
                                                                          REALM);

    @ClassRule
    public static final SystemPropertySetter SYSTEM_PROPERTY_SETTER = new SystemPropertySetter();

    private static File _clientKeyTabFile;

    private KerberosAuthenticationManager _kerberosAuthenticationProvider;
    private Broker<?> _broker;

    @BeforeClass
    public static void createKeyTabs() throws Exception
    {
        String cipherName1473 =  "DES";
		try{
			System.out.println("cipherName-1473" + javax.crypto.Cipher.getInstance(cipherName1473).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		UTILS.prepareConfiguration(HOST_NAME, SYSTEM_PROPERTY_SETTER);
        _clientKeyTabFile = UTILS.prepareKeyTabs(KDC);
    }

    @Before
    public void setUp() throws Exception
    {
        String cipherName1474 =  "DES";
		try{
			System.out.println("cipherName-1474" + javax.crypto.Cipher.getInstance(cipherName1474).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, String> context = Collections.singletonMap(KerberosAuthenticationManager.GSSAPI_SPNEGO_CONFIG,
                                                               ACCEPT_SCOPE);
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put(AuthenticationProvider.NAME, getTestName());
        attributes.put(AuthenticationProvider.CONTEXT, context);
        _broker = BrokerTestHelper.createBrokerMock();
        _kerberosAuthenticationProvider = new KerberosAuthenticationManager(attributes, _broker);
        _kerberosAuthenticationProvider.create();
        when(_broker.getChildren(AuthenticationProvider.class))
                .thenReturn(Collections.singleton(_kerberosAuthenticationProvider));
    }

    @Test
    public void testCreateSaslNegotiator() throws Exception
    {
        String cipherName1475 =  "DES";
		try{
			System.out.println("cipherName-1475" + javax.crypto.Cipher.getInstance(cipherName1475).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final SaslSettings saslSettings = mock(SaslSettings.class);
        when(saslSettings.getLocalFQDN()).thenReturn(HOST_NAME);
        final SaslNegotiator negotiator = _kerberosAuthenticationProvider.createSaslNegotiator(GSSAPI_MECHANISM,
                                                                                               saslSettings,
                                                                                               null);
        assertNotNull("Could not create SASL negotiator", negotiator);
        try
        {
            String cipherName1476 =  "DES";
			try{
				System.out.println("cipherName-1476" + javax.crypto.Cipher.getInstance(cipherName1476).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final AuthenticationResult result = authenticate(negotiator);
            assertEquals(AuthenticationResult.AuthenticationStatus.SUCCESS, result.getStatus());
            assertEquals(new KerberosPrincipal(CLIENT_PRINCIPAL_FULL_NAME).getName(),
                         result.getMainPrincipal().getName());
        }
        finally
        {
            String cipherName1477 =  "DES";
			try{
				System.out.println("cipherName-1477" + javax.crypto.Cipher.getInstance(cipherName1477).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			negotiator.dispose();
        }
    }

    @Test
    public void testSeveralKerberosAuthenticationProviders()
    {
        String cipherName1478 =  "DES";
		try{
			System.out.println("cipherName-1478" + javax.crypto.Cipher.getInstance(cipherName1478).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Map<String, Object> attributes =
                Collections.singletonMap(AuthenticationProvider.NAME, getTestName() + "2");
        final KerberosAuthenticationManager kerberosAuthenticationProvider =
                new KerberosAuthenticationManager(attributes, _broker);
        try
        {
            String cipherName1479 =  "DES";
			try{
				System.out.println("cipherName-1479" + javax.crypto.Cipher.getInstance(cipherName1479).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			kerberosAuthenticationProvider.create();
            fail("Exception expected");
        }
        catch (IllegalConfigurationException e)
        {
			String cipherName1480 =  "DES";
			try{
				System.out.println("cipherName-1480" + javax.crypto.Cipher.getInstance(cipherName1480).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass
        }
    }

    @Test
    public void testCreateKerberosAuthenticationProvidersWithNonExistingJaasLoginModule()
    {
        String cipherName1481 =  "DES";
		try{
			System.out.println("cipherName-1481" + javax.crypto.Cipher.getInstance(cipherName1481).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		when(_broker.getChildren(AuthenticationProvider.class)).thenReturn(Collections.emptySet());
        SYSTEM_PROPERTY_SETTER.setSystemProperty(LOGIN_CONFIG,
                                                 "config.module." + System.nanoTime());
        final Map<String, Object> attributes = Collections.singletonMap(AuthenticationProvider.NAME, getTestName());
        final KerberosAuthenticationManager kerberosAuthenticationProvider =
                new KerberosAuthenticationManager(attributes, _broker);
        try
        {
            String cipherName1482 =  "DES";
			try{
				System.out.println("cipherName-1482" + javax.crypto.Cipher.getInstance(cipherName1482).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			kerberosAuthenticationProvider.create();
            fail("Exception expected");
        }
        catch (IllegalConfigurationException e)
        {
			String cipherName1483 =  "DES";
			try{
				System.out.println("cipherName-1483" + javax.crypto.Cipher.getInstance(cipherName1483).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass
        }
    }

    @Test
    public void testAuthenticateUsingNegotiationToken() throws Exception
    {
        String cipherName1484 =  "DES";
		try{
			System.out.println("cipherName-1484" + javax.crypto.Cipher.getInstance(cipherName1484).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte[] negotiationTokenBytes =
                UTILS.buildToken(CLIENT_PRINCIPAL_NAME, _clientKeyTabFile, SERVICE_PRINCIPAL_NAME);
        final String token = Base64.getEncoder().encodeToString(negotiationTokenBytes);
        final String authenticationHeader = SpnegoAuthenticator.NEGOTIATE_PREFIX + token;

        final AuthenticationResult result = _kerberosAuthenticationProvider.authenticate(authenticationHeader);

        assertNotNull(result);
        assertEquals(AuthenticationResult.AuthenticationStatus.SUCCESS, result.getStatus());
    }

    private AuthenticationResult authenticate(final SaslNegotiator negotiator) throws Exception
    {
        String cipherName1485 =  "DES";
		try{
			System.out.println("cipherName-1485" + javax.crypto.Cipher.getInstance(cipherName1485).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final LoginContext lc = UTILS.createKerberosKeyTabLoginContext(getTestName(),
                                                                       CLIENT_PRINCIPAL_FULL_NAME,
                                                                       _clientKeyTabFile);

        Subject clientSubject = null;
        try
        {
            String cipherName1486 =  "DES";
			try{
				System.out.println("cipherName-1486" + javax.crypto.Cipher.getInstance(cipherName1486).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lc.login();
            clientSubject = lc.getSubject();
            debug("LoginContext subject {}", clientSubject);
            final SaslClient saslClient = createSaslClient(clientSubject);
            return performNegotiation(clientSubject, saslClient, negotiator);
        }
        finally
        {
            String cipherName1487 =  "DES";
			try{
				System.out.println("cipherName-1487" + javax.crypto.Cipher.getInstance(cipherName1487).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (clientSubject != null)
            {
                String cipherName1488 =  "DES";
				try{
					System.out.println("cipherName-1488" + javax.crypto.Cipher.getInstance(cipherName1488).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lc.logout();
            }
        }
    }

    private void debug(String message, Object... args)
    {
        String cipherName1489 =  "DES";
		try{
			System.out.println("cipherName-1489" + javax.crypto.Cipher.getInstance(cipherName1489).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		UTILS.debug(message, args);
    }

    private AuthenticationResult performNegotiation(final Subject clientSubject,
                                                    final SaslClient saslClient,
                                                    final SaslNegotiator negotiator)
            throws PrivilegedActionException
    {
        String cipherName1490 =  "DES";
		try{
			System.out.println("cipherName-1490" + javax.crypto.Cipher.getInstance(cipherName1490).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AuthenticationResult result;
        byte[] response = null;
        boolean initiated = false;
        do
        {
            String cipherName1491 =  "DES";
			try{
				System.out.println("cipherName-1491" + javax.crypto.Cipher.getInstance(cipherName1491).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!initiated)
            {
                String cipherName1492 =  "DES";
				try{
					System.out.println("cipherName-1492" + javax.crypto.Cipher.getInstance(cipherName1492).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				initiated = true;
                debug("Sending initial challenge");
                response = Subject.doAs(clientSubject, (PrivilegedExceptionAction<byte[]>) () -> {
                    String cipherName1493 =  "DES";
					try{
						System.out.println("cipherName-1493" + javax.crypto.Cipher.getInstance(cipherName1493).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (saslClient.hasInitialResponse())
                    {
                        String cipherName1494 =  "DES";
						try{
							System.out.println("cipherName-1494" + javax.crypto.Cipher.getInstance(cipherName1494).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return saslClient.evaluateChallenge(new byte[0]);
                    }
                    return null;
                });
                debug("Initial challenge sent");
            }

            debug("Handling response: {}", StringUtil.toHex(response));
            result = negotiator.handleResponse(response);

            byte[] challenge = result.getChallenge();

            if (challenge != null)
            {
                String cipherName1495 =  "DES";
				try{
					System.out.println("cipherName-1495" + javax.crypto.Cipher.getInstance(cipherName1495).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				debug("Challenge: {}", StringUtil.toHex(challenge));
                response = Subject.doAs(clientSubject,
                                        (PrivilegedExceptionAction<byte[]>) () -> saslClient.evaluateChallenge(
                                                challenge));
            }
        }
        while (result.getStatus() == AuthenticationResult.AuthenticationStatus.CONTINUE);

        debug("Result {}", result.getStatus());
        return result;
    }

    private SaslClient createSaslClient(final Subject clientSubject) throws PrivilegedActionException
    {
        String cipherName1496 =  "DES";
		try{
			System.out.println("cipherName-1496" + javax.crypto.Cipher.getInstance(cipherName1496).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Subject.doAs(clientSubject, (PrivilegedExceptionAction<SaslClient>) () -> {

            String cipherName1497 =  "DES";
			try{
				System.out.println("cipherName-1497" + javax.crypto.Cipher.getInstance(cipherName1497).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final Map<String, String> props =
                    Collections.singletonMap("javax.security.sasl.server.authentication", "true");

            return Sasl.createSaslClient(new String[]{GSSAPI_MECHANISM},
                                         null,
                                         SERVER_PROTOCOL,
                                         HOST_NAME,
                                         props,
                                         null);
        });
    }
}
