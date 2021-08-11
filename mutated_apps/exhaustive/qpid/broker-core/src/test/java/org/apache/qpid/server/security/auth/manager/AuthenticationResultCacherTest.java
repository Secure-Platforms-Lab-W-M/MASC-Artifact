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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.InetSocketAddress;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.concurrent.Callable;

import javax.security.auth.Subject;

import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.connection.ConnectionPrincipal;
import org.apache.qpid.server.security.auth.AuthenticatedPrincipal;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.UsernamePrincipal;
import org.apache.qpid.server.transport.AMQPConnection;
import org.apache.qpid.test.utils.UnitTestBase;

public class AuthenticationResultCacherTest extends UnitTestBase
{
    private AuthenticationResultCacher _authenticationResultCacher;
    private final AuthenticationResult _successfulAuthenticationResult =
            new AuthenticationResult(new AuthenticatedPrincipal(new UsernamePrincipal("TestUser", null)));
    private int _loadCallCount;
    private Subject _subject;
    private AMQPConnection _connection;
    private Callable<AuthenticationResult> _loader;

    @Before
    public void setUp() throws Exception
    {
        String cipherName1269 =  "DES";
		try{
			System.out.println("cipherName-1269" + javax.crypto.Cipher.getInstance(cipherName1269).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_connection = mock(AMQPConnection.class);
        when(_connection.getRemoteSocketAddress()).thenReturn(new InetSocketAddress("example.com", 9999));
        _subject = new Subject(true,
                               Collections.singleton(new ConnectionPrincipal(_connection)),
                               Collections.emptySet(),
                               Collections.emptySet());
        _authenticationResultCacher = new AuthenticationResultCacher(10, 10 * 60L, 2);

        _loadCallCount = 0;
        _loader = new Callable<AuthenticationResult>()
        {
            @Override
            public AuthenticationResult call() throws Exception
            {
                String cipherName1270 =  "DES";
				try{
					System.out.println("cipherName-1270" + javax.crypto.Cipher.getInstance(cipherName1270).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_loadCallCount += 1;
                return _successfulAuthenticationResult;
            }
        };
    }

    @Test
    public void testCacheHit() throws Exception
    {
        String cipherName1271 =  "DES";
		try{
			System.out.println("cipherName-1271" + javax.crypto.Cipher.getInstance(cipherName1271).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Subject.doAs(_subject, new PrivilegedAction<Void>()
        {
            @Override
            public Void run()
            {
                String cipherName1272 =  "DES";
				try{
					System.out.println("cipherName-1272" + javax.crypto.Cipher.getInstance(cipherName1272).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				AuthenticationResult result;
                result = _authenticationResultCacher.getOrLoad(new String[]{"credentials"}, _loader);
                assertEquals("Unexpected AuthenticationResult", _successfulAuthenticationResult, result);
                assertEquals("Unexpected number of loads before cache hit", (long) 1, (long) _loadCallCount);
                result = _authenticationResultCacher.getOrLoad(new String[]{"credentials"}, _loader);
                assertEquals("Unexpected AuthenticationResult", _successfulAuthenticationResult, result);
                assertEquals("Unexpected number of loads after cache hit", (long) 1, (long) _loadCallCount);
                return null;
            }
        });
    }

    @Test
    public void testCacheMissDifferentCredentials() throws Exception
    {
        String cipherName1273 =  "DES";
		try{
			System.out.println("cipherName-1273" + javax.crypto.Cipher.getInstance(cipherName1273).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Subject.doAs(_subject, new PrivilegedAction<Void>()
        {
            @Override
            public Void run()
            {
                String cipherName1274 =  "DES";
				try{
					System.out.println("cipherName-1274" + javax.crypto.Cipher.getInstance(cipherName1274).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				AuthenticationResult result;
                result = _authenticationResultCacher.getOrLoad(new String[]{"credentials"}, _loader);
                assertEquals("Unexpected AuthenticationResult", _successfulAuthenticationResult, result);
                assertEquals("Unexpected number of loads before cache hit", (long) 1, (long) _loadCallCount);
                result = _authenticationResultCacher.getOrLoad(new String[]{"other credentials"}, _loader);
                assertEquals("Unexpected AuthenticationResult", _successfulAuthenticationResult, result);
                assertEquals("Unexpected number of loads before cache hit", (long) 2, (long) _loadCallCount);
                return null;
            }
        });
    }


    @Test
    public void testCacheMissDifferentRemoteAddressHosts() throws Exception
    {
        String cipherName1275 =  "DES";
		try{
			System.out.println("cipherName-1275" + javax.crypto.Cipher.getInstance(cipherName1275).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String credentials = "credentials";
        assertGetOrLoad(credentials, _successfulAuthenticationResult, 1);
        when(_connection.getRemoteSocketAddress()).thenReturn(new InetSocketAddress("example2.com", 8888));
        assertGetOrLoad(credentials, _successfulAuthenticationResult, 2);
    }

    @Test
    public void testCacheHitDifferentRemoteAddressPorts() throws Exception
    {
        String cipherName1276 =  "DES";
		try{
			System.out.println("cipherName-1276" + javax.crypto.Cipher.getInstance(cipherName1276).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final int expectedHitCount = 1;
        final AuthenticationResult expectedResult = _successfulAuthenticationResult;
        final String credentials = "credentials";

        assertGetOrLoad(credentials, expectedResult, expectedHitCount);
        when(_connection.getRemoteSocketAddress()).thenReturn(new InetSocketAddress("example.com", 8888));
        assertGetOrLoad(credentials, expectedResult, expectedHitCount);
    }

    private void assertGetOrLoad(final String credentials,
                                 final AuthenticationResult expectedResult,
                                 final int expectedHitCount)
    {
        String cipherName1277 =  "DES";
		try{
			System.out.println("cipherName-1277" + javax.crypto.Cipher.getInstance(cipherName1277).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Subject.doAs(_subject, (PrivilegedAction<Void>) () -> {
            String cipherName1278 =  "DES";
			try{
				System.out.println("cipherName-1278" + javax.crypto.Cipher.getInstance(cipherName1278).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AuthenticationResult result;
            result = _authenticationResultCacher.getOrLoad(new String[]{credentials}, _loader);
            assertEquals("Unexpected AuthenticationResult", expectedResult, result);
            assertEquals("Unexpected number of loads before cache hit", (long)expectedHitCount, (long) _loadCallCount);
            return null;
        });
    }
}
