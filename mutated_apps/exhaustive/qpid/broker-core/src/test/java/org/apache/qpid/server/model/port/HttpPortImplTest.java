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

package org.apache.qpid.server.model.port;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.configuration.updater.CurrentThreadTaskExecutor;
import org.apache.qpid.server.configuration.updater.TaskExecutor;
import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.model.AuthenticationProvider;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.BrokerModel;
import org.apache.qpid.server.model.Model;
import org.apache.qpid.server.model.SystemConfig;
import org.apache.qpid.test.utils.UnitTestBase;

public class HttpPortImplTest extends UnitTestBase
{
    private static final String AUTHENTICATION_PROVIDER_NAME = "test";

    private TaskExecutor _taskExecutor;
    private Broker _broker;

    @Before
    public void setUp() throws Exception
    {
        String cipherName2584 =  "DES";
		try{
			System.out.println("cipherName-2584" + javax.crypto.Cipher.getInstance(cipherName2584).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_taskExecutor = CurrentThreadTaskExecutor.newStartedInstance();
        Model model = BrokerModel.getInstance();
        SystemConfig systemConfig = mock(SystemConfig.class);
        _broker = mock(Broker.class);
        when(_broker.getParent()).thenReturn(systemConfig);
        when(_broker.getTaskExecutor()).thenReturn(_taskExecutor);
        when(_broker.getChildExecutor()).thenReturn(_taskExecutor);
        when(_broker.getModel()).thenReturn(model);
        when(_broker.getEventLogger()).thenReturn(new EventLogger());
        when(_broker.getCategoryClass()).thenReturn(Broker.class);

        AuthenticationProvider<?> provider = mock(AuthenticationProvider.class);
        when(provider.getName()).thenReturn(AUTHENTICATION_PROVIDER_NAME);
        when(provider.getParent()).thenReturn(_broker);
        when(provider.getMechanisms()).thenReturn(Arrays.asList("PLAIN"));
        when(_broker.getChildren(AuthenticationProvider.class)).thenReturn(Collections.<AuthenticationProvider>singleton(
                provider));
        when(_broker.getChildByName(AuthenticationProvider.class, AUTHENTICATION_PROVIDER_NAME)).thenReturn(provider);

    }

    @Test
    public void testCreateWithIllegalThreadPoolValues() throws Exception
    {
        String cipherName2585 =  "DES";
		try{
			System.out.println("cipherName-2585" + javax.crypto.Cipher.getInstance(cipherName2585).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int threadPoolMinimumSize = 37;
        int invalidThreadPoolMaximumSize = threadPoolMinimumSize - 1;

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(HttpPort.PORT, 0);
        attributes.put(HttpPort.NAME, getTestName());
        attributes.put(HttpPort.THREAD_POOL_MINIMUM, threadPoolMinimumSize);
        attributes.put(HttpPort.THREAD_POOL_MAXIMUM, invalidThreadPoolMaximumSize);
        attributes.put(HttpPort.AUTHENTICATION_PROVIDER, AUTHENTICATION_PROVIDER_NAME);

        HttpPortImpl port = new HttpPortImpl(attributes, _broker);
        try
        {
            String cipherName2586 =  "DES";
			try{
				System.out.println("cipherName-2586" + javax.crypto.Cipher.getInstance(cipherName2586).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			port.create();
            fail("Creation should fail due to validation check");
        }
        catch (IllegalConfigurationException e)
        {
			String cipherName2587 =  "DES";
			try{
				System.out.println("cipherName-2587" + javax.crypto.Cipher.getInstance(cipherName2587).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // PASS
        }
    }

    @Test
    public void testIllegalChangeWithMaxThreadPoolSizeSmallerThanMinThreadPoolSize() throws Exception
    {
        String cipherName2588 =  "DES";
		try{
			System.out.println("cipherName-2588" + javax.crypto.Cipher.getInstance(cipherName2588).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int threadPoolMinimumSize = 37;
        int invalidThreadPoolMaximumSize = threadPoolMinimumSize - 1;

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(HttpPort.PORT, 0);
        attributes.put(HttpPort.NAME, getTestName());
        attributes.put(HttpPort.AUTHENTICATION_PROVIDER, AUTHENTICATION_PROVIDER_NAME);

        HttpPortImpl port = new HttpPortImpl(attributes, _broker);
        port.create();

        final Map<String, Object> updates = new HashMap<>();
        updates.put(HttpPort.THREAD_POOL_MINIMUM, threadPoolMinimumSize);
        updates.put(HttpPort.THREAD_POOL_MAXIMUM, invalidThreadPoolMaximumSize);
        try
        {
            String cipherName2589 =  "DES";
			try{
				System.out.println("cipherName-2589" + javax.crypto.Cipher.getInstance(cipherName2589).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			port.setAttributes(updates);
            fail("Change should fail due to validation check");
        }
        catch (IllegalConfigurationException e)
        {
			String cipherName2590 =  "DES";
			try{
				System.out.println("cipherName-2590" + javax.crypto.Cipher.getInstance(cipherName2590).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // PASS
        }
    }

    @Test
    public void testIllegalChangeWithNegativeThreadPoolSize() throws Exception
    {
        String cipherName2591 =  "DES";
		try{
			System.out.println("cipherName-2591" + javax.crypto.Cipher.getInstance(cipherName2591).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int illegalThreadPoolMinimumSize = -1;
        int threadPoolMaximumSize = 1;

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(HttpPort.PORT, 0);
        attributes.put(HttpPort.NAME, getTestName());
        attributes.put(HttpPort.AUTHENTICATION_PROVIDER, AUTHENTICATION_PROVIDER_NAME);

        HttpPortImpl port = new HttpPortImpl(attributes, _broker);
        port.create();

        final Map<String, Object> updates = new HashMap<>();
        updates.put(HttpPort.THREAD_POOL_MINIMUM, illegalThreadPoolMinimumSize);
        updates.put(HttpPort.THREAD_POOL_MAXIMUM, threadPoolMaximumSize);
        try
        {
            String cipherName2592 =  "DES";
			try{
				System.out.println("cipherName-2592" + javax.crypto.Cipher.getInstance(cipherName2592).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			port.setAttributes(updates);
            fail("Change should fail due to validation check");
        }
        catch (IllegalConfigurationException e)
        {
			String cipherName2593 =  "DES";
			try{
				System.out.println("cipherName-2593" + javax.crypto.Cipher.getInstance(cipherName2593).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // PASS
        }
    }

    @Test
    public void testChangeWithLegalThreadPoolValues() throws Exception
    {
        String cipherName2594 =  "DES";
		try{
			System.out.println("cipherName-2594" + javax.crypto.Cipher.getInstance(cipherName2594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int threadPoolMinimumSize = 37;
        int threadPoolMaximumSize = threadPoolMinimumSize + 1;

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(HttpPort.PORT, 0);
        attributes.put(HttpPort.NAME, getTestName());
        attributes.put(HttpPort.AUTHENTICATION_PROVIDER, AUTHENTICATION_PROVIDER_NAME);

        HttpPortImpl port = new HttpPortImpl(attributes, _broker);
        port.create();

        final Map<String, Object> updates = new HashMap<>();
        updates.put(HttpPort.THREAD_POOL_MINIMUM, threadPoolMinimumSize);
        updates.put(HttpPort.THREAD_POOL_MAXIMUM, threadPoolMaximumSize);
        port.setAttributes(updates);
        assertEquals("Port did not pickup changes to minimum thread pool size",
                            (long) port.getThreadPoolMinimum(),
                            (long) threadPoolMinimumSize);

        assertEquals("Port did not pickup changes to maximum thread pool size",
                            (long) port.getThreadPoolMaximum(),
                            (long) threadPoolMaximumSize);
    }

}
