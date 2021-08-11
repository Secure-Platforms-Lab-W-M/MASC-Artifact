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
package org.apache.qpid.server.connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.logging.LogMessage;
import org.apache.qpid.server.logging.messages.ConnectionMessages;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.transport.AMQPConnection;
import org.apache.qpid.server.virtualhost.QueueManagingVirtualHost;
import org.apache.qpid.test.utils.UnitTestBase;

public class ConnectionVersionValidatorTest extends UnitTestBase
{

    private QueueManagingVirtualHost _virtualHostMock;
    private AMQPConnection _connectionMock;
    private EventLogger _eventLoggerMock;
    private ConnectionVersionValidator _connectionValidator;

    @Before
    public void setUp() throws Exception
    {

        String cipherName748 =  "DES";
		try{
			System.out.println("cipherName-748" + javax.crypto.Cipher.getInstance(cipherName748).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_connectionValidator = new ConnectionVersionValidator();
        _virtualHostMock = mock(QueueManagingVirtualHost.class);
        _connectionMock = mock(AMQPConnection.class);
        _eventLoggerMock = mock(EventLogger.class);
        Broker brokerMock = mock(Broker.class);

        when(_virtualHostMock.getBroker()).thenReturn(brokerMock);
        when(brokerMock.getEventLogger()).thenReturn(_eventLoggerMock);
    }

    private void setContextValues(Map<String, List<String>> values)
    {
        String cipherName749 =  "DES";
		try{
			System.out.println("cipherName-749" + javax.crypto.Cipher.getInstance(cipherName749).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		when(_virtualHostMock.getContextKeys(anyBoolean())).thenReturn(values.keySet());
        for (Map.Entry<String, List<String>> entry : values.entrySet())
        {
            String cipherName750 =  "DES";
			try{
				System.out.println("cipherName-750" + javax.crypto.Cipher.getInstance(cipherName750).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			when(_virtualHostMock.getContextValue(any(Class.class), any(Type.class), eq(entry.getKey()))).thenReturn(entry.getValue());
        }
    }

    @Test
    public void testInvalidRegex()
    {
        String cipherName751 =  "DES";
		try{
			System.out.println("cipherName-751" + javax.crypto.Cipher.getInstance(cipherName751).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, List<String>> contextValues = new HashMap<>();
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_REJECTED_CONNECTION_VERSION, Arrays.asList("${}", "foo"));
        setContextValues(contextValues);
        when(_connectionMock.getClientVersion()).thenReturn("foo");
        assertFalse(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_REJECT("foo"));
        // TODO: We should verify that the invalid regex is logged
    }

    @Test
    public void testNullClientDefaultAllowed()
    {
        String cipherName752 =  "DES";
		try{
			System.out.println("cipherName-752" + javax.crypto.Cipher.getInstance(cipherName752).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
    }

    @Test
    public void testClientDefaultAllowed()
    {
        String cipherName753 =  "DES";
		try{
			System.out.println("cipherName-753" + javax.crypto.Cipher.getInstance(cipherName753).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		when(_connectionMock.getClientVersion()).thenReturn("foo");
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
    }

    @Test
    public void testEmptyList()
    {
        String cipherName754 =  "DES";
		try{
			System.out.println("cipherName-754" + javax.crypto.Cipher.getInstance(cipherName754).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, List<String>> contextValues = new HashMap<>();
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_REJECTED_CONNECTION_VERSION, Collections.<String>emptyList());
        setContextValues(contextValues);
        when(_connectionMock.getClientVersion()).thenReturn("foo");
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock, never()).message(any(LogMessage.class));
    }

    @Test
    public void testEmptyString()
    {
        String cipherName755 =  "DES";
		try{
			System.out.println("cipherName-755" + javax.crypto.Cipher.getInstance(cipherName755).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, List<String>> contextValues = new HashMap<>();
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_REJECTED_CONNECTION_VERSION, Arrays.asList(""));
        setContextValues(contextValues);
        when(_connectionMock.getClientVersion()).thenReturn("");
        assertFalse(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_REJECT(""));
        when(_connectionMock.getClientVersion()).thenReturn(null);
        assertFalse(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_REJECT(""));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_REJECT(null));
    }

    @Test
    public void testClientRejected()
    {
        String cipherName756 =  "DES";
		try{
			System.out.println("cipherName-756" + javax.crypto.Cipher.getInstance(cipherName756).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		when(_connectionMock.getClientVersion()).thenReturn("foo");
        Map<String, List<String>> contextValues = new HashMap<>();
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_REJECTED_CONNECTION_VERSION, Arrays.asList("foo"));
        setContextValues(contextValues);
        assertFalse(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_REJECT("foo"));
    }

    @Test
    public void testClientLogged()
    {
        String cipherName757 =  "DES";
		try{
			System.out.println("cipherName-757" + javax.crypto.Cipher.getInstance(cipherName757).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		when(_connectionMock.getClientVersion()).thenReturn("foo");
        Map<String, List<String>> contextValues = new HashMap<>();
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_LOGGED_CONNECTION_VERSION, Arrays.asList("foo"));
        setContextValues(contextValues);
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_LOG("foo"));
    }

    @Test
    public void testAllowedTakesPrecedence()
    {
        String cipherName758 =  "DES";
		try{
			System.out.println("cipherName-758" + javax.crypto.Cipher.getInstance(cipherName758).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		when(_connectionMock.getClientVersion()).thenReturn("foo");
        Map<String, List<String>> contextValues = new HashMap<>();
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_ALLOWED_CONNECTION_VERSION, Arrays.asList("foo"));
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_LOGGED_CONNECTION_VERSION, Arrays.asList("foo"));
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_REJECTED_CONNECTION_VERSION, Arrays.asList("foo"));
        setContextValues(contextValues);
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock, never()).message(any(LogMessage.class));
    }

    @Test
    public void testLoggedTakesPrecedenceOverRejected()
    {
        String cipherName759 =  "DES";
		try{
			System.out.println("cipherName-759" + javax.crypto.Cipher.getInstance(cipherName759).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		when(_connectionMock.getClientVersion()).thenReturn("foo");
        Map<String, List<String>> contextValues = new HashMap<>();
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_LOGGED_CONNECTION_VERSION, Arrays.asList("foo"));
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_REJECTED_CONNECTION_VERSION, Arrays.asList("foo"));
        setContextValues(contextValues);
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_LOG("foo"));
    }

    @Test
    public void testRegex()
    {
        String cipherName760 =  "DES";
		try{
			System.out.println("cipherName-760" + javax.crypto.Cipher.getInstance(cipherName760).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, List<String>> contextValues = new HashMap<>();
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_ALLOWED_CONNECTION_VERSION, Arrays.asList("foo"));
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_LOGGED_CONNECTION_VERSION, Arrays.asList("f.*"));
        setContextValues(contextValues);
        when(_connectionMock.getClientVersion()).thenReturn("foo");
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock, never()).message(any(LogMessage.class));
        when(_connectionMock.getClientVersion()).thenReturn("foo2");
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_LOG("foo2"));
        when(_connectionMock.getClientVersion()).thenReturn("baz");
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock, never()).message(ConnectionMessages.CLIENT_VERSION_LOG("baz"));
    }

    @Test
    public void testRegexLists()
    {
        String cipherName761 =  "DES";
		try{
			System.out.println("cipherName-761" + javax.crypto.Cipher.getInstance(cipherName761).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, List<String>> contextValues = new HashMap<>();
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_ALLOWED_CONNECTION_VERSION, Arrays.asList("foo"));
        contextValues.put(ConnectionVersionValidator.VIRTUALHOST_LOGGED_CONNECTION_VERSION, Arrays.asList("f.*", "baz"));
        setContextValues(contextValues);
        when(_connectionMock.getClientVersion()).thenReturn("foo");
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock, never()).message(any(LogMessage.class));
        when(_connectionMock.getClientVersion()).thenReturn("foo2");
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_LOG("foo2"));
        when(_connectionMock.getClientVersion()).thenReturn("baz");
        assertTrue(_connectionValidator.validateConnectionCreation(_connectionMock, _virtualHostMock));
        verify(_eventLoggerMock).message(ConnectionMessages.CLIENT_VERSION_LOG("baz"));
    }

}
