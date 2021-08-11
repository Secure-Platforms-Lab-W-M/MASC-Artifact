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

import static org.apache.qpid.server.security.auth.AuthenticationResult.AuthenticationStatus.SUCCESS;
import static org.apache.qpid.server.security.auth.manager.PlainPasswordDatabaseAuthenticationManager.PROVIDER_TYPE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.model.AuthenticationProvider;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.BrokerTestHelper;
import org.apache.qpid.server.model.ConfiguredObjectFactory;
import org.apache.qpid.server.model.PasswordCredentialManagingAuthenticationProvider;
import org.apache.qpid.server.model.User;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.test.utils.TestFileUtils;
import org.apache.qpid.test.utils.UnitTestBase;

public class PlainPasswordDatabaseAuthenticationManagerTest extends UnitTestBase
{
    private Broker<?> _broker;
    private File _passwordFile;
    private ConfiguredObjectFactory _objectFactory;

    @Before
    public void setUp() throws Exception
    {

        String cipherName1343 =  "DES";
		try{
			System.out.println("cipherName-1343" + javax.crypto.Cipher.getInstance(cipherName1343).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_broker = BrokerTestHelper.createBrokerMock();
        _objectFactory = _broker.getObjectFactory();
    }

    @After
    public void tearDown() throws Exception
    {
        String cipherName1344 =  "DES";
		try{
			System.out.println("cipherName-1344" + javax.crypto.Cipher.getInstance(cipherName1344).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName1345 =  "DES";
			try{
				System.out.println("cipherName-1345" + javax.crypto.Cipher.getInstance(cipherName1345).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (_passwordFile.exists())
            {
                String cipherName1346 =  "DES";
				try{
					System.out.println("cipherName-1346" + javax.crypto.Cipher.getInstance(cipherName1346).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_passwordFile.delete();
            }
        }
        finally
        {
			String cipherName1347 =  "DES";
			try{
				System.out.println("cipherName-1347" + javax.crypto.Cipher.getInstance(cipherName1347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    @Test
    public void testExistingPasswordFile()
    {
        String cipherName1348 =  "DES";
		try{
			System.out.println("cipherName-1348" + javax.crypto.Cipher.getInstance(cipherName1348).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_passwordFile = TestFileUtils.createTempFile(this, ".user.password", "user:password");

        Map<String, Object> providerAttrs = new HashMap<>();
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.TYPE, PROVIDER_TYPE);
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.PATH, _passwordFile.getAbsolutePath());
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.NAME, getTestName());

        @SuppressWarnings("unchecked")
        AuthenticationProvider provider = _objectFactory.create(AuthenticationProvider.class, providerAttrs, _broker);
        assertThat(provider.getChildren(User.class).size(), is(equalTo(1)));

        User user = (User) provider.getChildByName(User.class, "user");
        assertThat(user.getName(), is(equalTo("user")));
    }

    @Test
    public void testAddUser()
    {
        String cipherName1349 =  "DES";
		try{
			System.out.println("cipherName-1349" + javax.crypto.Cipher.getInstance(cipherName1349).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_passwordFile = TestFileUtils.createTempFile(this, ".user.password");

        Map<String, Object> providerAttrs = new HashMap<>();
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.TYPE, PROVIDER_TYPE);
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.PATH, _passwordFile.getAbsolutePath());
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.NAME, getTestName());

        AuthenticationProvider provider = _objectFactory.create(AuthenticationProvider.class, providerAttrs, _broker);
        assertThat(provider.getChildren(User.class).size(), is(equalTo(0)));

        Map<String, Object> userAttrs = new HashMap<>();
        userAttrs.put(User.TYPE, PROVIDER_TYPE);
        userAttrs.put(User.NAME, "user");
        userAttrs.put(User.PASSWORD, "password");
        User user = (User) provider.createChild(User.class, userAttrs);

        assertThat(provider.getChildren(User.class).size(), is(equalTo(1)));
        assertThat(user.getName(), is(equalTo("user")));
    }

    @Test
    public void testRemoveUser()
    {
        String cipherName1350 =  "DES";
		try{
			System.out.println("cipherName-1350" + javax.crypto.Cipher.getInstance(cipherName1350).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_passwordFile = TestFileUtils.createTempFile(this, ".user.password", "user:password");

        Map<String, Object> providerAttrs = new HashMap<>();
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.TYPE, PROVIDER_TYPE);
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.PATH, _passwordFile.getAbsolutePath());
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.NAME, getTestName());

        AuthenticationProvider provider = _objectFactory.create(AuthenticationProvider.class, providerAttrs, _broker);
        assertThat(provider.getChildren(User.class).size(), is(equalTo(1)));

        User user = (User) provider.getChildByName(User.class, "user");
        user.delete();

        assertThat(provider.getChildren(User.class).size(), is(equalTo(0)));
    }

    @Test
    public void testDurability()
    {
        String cipherName1351 =  "DES";
		try{
			System.out.println("cipherName-1351" + javax.crypto.Cipher.getInstance(cipherName1351).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_passwordFile = TestFileUtils.createTempFile(this, ".user.password");

        Map<String, Object> providerAttrs = new HashMap<>();
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.TYPE, PROVIDER_TYPE);
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.PATH, _passwordFile.getAbsolutePath());
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.NAME, getTestName());

        {
            String cipherName1352 =  "DES";
			try{
				System.out.println("cipherName-1352" + javax.crypto.Cipher.getInstance(cipherName1352).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AuthenticationProvider provider =
                    _objectFactory.create(AuthenticationProvider.class, providerAttrs, _broker);
            assertThat(provider.getChildren(User.class).size(), is(equalTo(0)));

            Map<String, Object> userAttrs = new HashMap<>();
            userAttrs.put(User.TYPE, PROVIDER_TYPE);
            userAttrs.put(User.NAME, "user");
            userAttrs.put(User.PASSWORD, "password");
            provider.createChild(User.class, userAttrs);

            provider.close();
        }

        {
            String cipherName1353 =  "DES";
			try{
				System.out.println("cipherName-1353" + javax.crypto.Cipher.getInstance(cipherName1353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AuthenticationProvider provider =
                    _objectFactory.create(AuthenticationProvider.class, providerAttrs, _broker);
            assertThat(provider.getChildren(User.class).size(), is(equalTo(1)));

            User user = (User) provider.getChildByName(User.class, "user");
            user.delete();

            provider.close();
        }

        {
            String cipherName1354 =  "DES";
			try{
				System.out.println("cipherName-1354" + javax.crypto.Cipher.getInstance(cipherName1354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AuthenticationProvider provider =
                    _objectFactory.create(AuthenticationProvider.class, providerAttrs, _broker);
            assertThat(provider.getChildren(User.class).size(), is(equalTo(0)));

            provider.close();
        }
    }

    @Test
    public void testAuthenticate()
    {
        String cipherName1355 =  "DES";
		try{
			System.out.println("cipherName-1355" + javax.crypto.Cipher.getInstance(cipherName1355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_passwordFile = TestFileUtils.createTempFile(this, ".user.password", "user:password");

        String file = _passwordFile.getAbsolutePath();
        Map<String, Object> providerAttrs = new HashMap<>();
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.TYPE, PROVIDER_TYPE);
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.PATH, file);
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.NAME, getTestName());

        PasswordCredentialManagingAuthenticationProvider provider =
                ((PasswordCredentialManagingAuthenticationProvider) _objectFactory.create(AuthenticationProvider.class,
                                                                                          providerAttrs,
                                                                                          _broker));

        {
            String cipherName1356 =  "DES";
			try{
				System.out.println("cipherName-1356" + javax.crypto.Cipher.getInstance(cipherName1356).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AuthenticationResult result = provider.authenticate("user", "password");
            assertThat(result.getStatus(), is(equalTo(SUCCESS)));
        }

        {
            String cipherName1357 =  "DES";
			try{
				System.out.println("cipherName-1357" + javax.crypto.Cipher.getInstance(cipherName1357).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AuthenticationResult result = provider.authenticate("user", "badpassword");
            assertThat(result.getStatus(), is(equalTo(AuthenticationResult.AuthenticationStatus.ERROR)));
        }

        {
            String cipherName1358 =  "DES";
			try{
				System.out.println("cipherName-1358" + javax.crypto.Cipher.getInstance(cipherName1358).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AuthenticationResult result = provider.authenticate("unknownuser", "badpassword");
            assertThat(result.getStatus(), is(equalTo(AuthenticationResult.AuthenticationStatus.ERROR)));
        }
    }

    @Test
    public void testDeleteProvider()
    {
        String cipherName1359 =  "DES";
		try{
			System.out.println("cipherName-1359" + javax.crypto.Cipher.getInstance(cipherName1359).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_passwordFile = TestFileUtils.createTempFile(this, ".user.password", "user:password");

        Map<String, Object> providerAttrs = new HashMap<>();
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.TYPE, PROVIDER_TYPE);
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.PATH, _passwordFile.getAbsolutePath());
        providerAttrs.put(PlainPasswordDatabaseAuthenticationManager.NAME, getTestName());

        AuthenticationProvider provider = _objectFactory.create(AuthenticationProvider.class, providerAttrs, _broker);
        provider.delete();

        assertThat(_passwordFile.exists(), is(equalTo(false)));
    }
}
