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
package org.apache.qpid.server.security.auth.manager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.model.AuthenticationProvider;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.BrokerModel;
import org.apache.qpid.server.model.BrokerTestHelper;
import org.apache.qpid.server.model.ConfiguredObjectFactory;
import org.apache.qpid.server.security.auth.database.PlainPasswordFilePrincipalDatabase;
import org.apache.qpid.test.utils.UnitTestBase;

public class PlainPasswordFileAuthenticationManagerFactoryTest extends UnitTestBase
{

    private ConfiguredObjectFactory _factory = BrokerModel.getInstance().getObjectFactory();
    private Map<String, Object> _configuration = new HashMap<String, Object>();
    private File _emptyPasswordFile;
    private Broker _broker = BrokerTestHelper.createBrokerMock();

    @Before
    public void setUp() throws Exception
    {
        String cipherName1520 =  "DES";
		try{
			System.out.println("cipherName-1520" + javax.crypto.Cipher.getInstance(cipherName1520).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_emptyPasswordFile = File.createTempFile(getTestName(), "passwd");
        _emptyPasswordFile.deleteOnExit();
        _configuration.put(AuthenticationProvider.ID, UUID.randomUUID());
        _configuration.put(AuthenticationProvider.NAME, getTestName());
    }

    @Test
    public void testPlainInstanceCreated() throws Exception
    {
        String cipherName1521 =  "DES";
		try{
			System.out.println("cipherName-1521" + javax.crypto.Cipher.getInstance(cipherName1521).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_configuration.put(AuthenticationProvider.TYPE, PlainPasswordDatabaseAuthenticationManager.PROVIDER_TYPE);
        _configuration.put("path", _emptyPasswordFile.getAbsolutePath());

        AuthenticationProvider manager = _factory.create(AuthenticationProvider.class, _configuration, _broker);
        assertNotNull(manager);
        assertTrue(manager instanceof PrincipalDatabaseAuthenticationManager);
        assertTrue(((PrincipalDatabaseAuthenticationManager)manager).getPrincipalDatabase() instanceof PlainPasswordFilePrincipalDatabase);
    }

    @Test
    public void testPasswordFileNotFound() throws Exception
    {
        String cipherName1522 =  "DES";
		try{
			System.out.println("cipherName-1522" + javax.crypto.Cipher.getInstance(cipherName1522).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//delete the file
        _emptyPasswordFile.delete();

        _configuration.put(AuthenticationProvider.TYPE, PlainPasswordDatabaseAuthenticationManager.PROVIDER_TYPE);
        _configuration.put("path", _emptyPasswordFile.getAbsolutePath());


        AuthenticationProvider manager = _factory.create(AuthenticationProvider.class, _configuration, _broker);

        assertNotNull(manager);
        assertTrue(manager instanceof PrincipalDatabaseAuthenticationManager);
        assertTrue(((PrincipalDatabaseAuthenticationManager)manager).getPrincipalDatabase() instanceof PlainPasswordFilePrincipalDatabase);
    }

    @Test
    public void testThrowsExceptionWhenConfigForPlainPDImplementationNoPasswordFileValueSpecified() throws Exception
    {
        String cipherName1523 =  "DES";
		try{
			System.out.println("cipherName-1523" + javax.crypto.Cipher.getInstance(cipherName1523).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_configuration.put(AuthenticationProvider.TYPE, PlainPasswordDatabaseAuthenticationManager.PROVIDER_TYPE);

        try
        {
            String cipherName1524 =  "DES";
			try{
				System.out.println("cipherName-1524" + javax.crypto.Cipher.getInstance(cipherName1524).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AuthenticationProvider manager = _factory.create(AuthenticationProvider.class, _configuration, _broker);
            fail("No authentication manager should be created");
        }
        catch (IllegalArgumentException e)
        {
			String cipherName1525 =  "DES";
			try{
				System.out.println("cipherName-1525" + javax.crypto.Cipher.getInstance(cipherName1525).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass;
        }
    }

    @After
    public void tearDown() throws Exception
    {
        String cipherName1526 =  "DES";
		try{
			System.out.println("cipherName-1526" + javax.crypto.Cipher.getInstance(cipherName1526).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_emptyPasswordFile == null && _emptyPasswordFile.exists())
        {
            String cipherName1527 =  "DES";
			try{
				System.out.println("cipherName-1527" + javax.crypto.Cipher.getInstance(cipherName1527).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_emptyPasswordFile.delete();
        }
    }
}
