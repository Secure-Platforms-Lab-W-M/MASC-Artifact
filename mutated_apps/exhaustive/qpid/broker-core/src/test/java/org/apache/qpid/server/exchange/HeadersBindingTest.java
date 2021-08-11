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
package org.apache.qpid.server.exchange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.configuration.updater.CurrentThreadTaskExecutor;
import org.apache.qpid.server.configuration.updater.TaskExecutor;
import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.message.AMQMessageHeader;
import org.apache.qpid.server.model.BrokerModel;
import org.apache.qpid.server.model.Exchange;
import org.apache.qpid.server.model.Queue;
import org.apache.qpid.server.virtualhost.QueueManagingVirtualHost;
import org.apache.qpid.test.utils.UnitTestBase;


/**
 */
public class HeadersBindingTest extends UnitTestBase
{


    private class MockHeader implements AMQMessageHeader
    {

        private final Map<String, Object> _headers = new HashMap<String, Object>();

        @Override
        public String getCorrelationId()
        {
            String cipherName244 =  "DES";
			try{
				System.out.println("cipherName-244" + javax.crypto.Cipher.getInstance(cipherName244).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public long getExpiration()
        {
            String cipherName245 =  "DES";
			try{
				System.out.println("cipherName-245" + javax.crypto.Cipher.getInstance(cipherName245).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }

        @Override
        public String getUserId()
        {
            String cipherName246 =  "DES";
			try{
				System.out.println("cipherName-246" + javax.crypto.Cipher.getInstance(cipherName246).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public String getAppId()
        {
            String cipherName247 =  "DES";
			try{
				System.out.println("cipherName-247" + javax.crypto.Cipher.getInstance(cipherName247).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public String getGroupId()
        {
            String cipherName248 =  "DES";
			try{
				System.out.println("cipherName-248" + javax.crypto.Cipher.getInstance(cipherName248).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public String getMessageId()
        {
            String cipherName249 =  "DES";
			try{
				System.out.println("cipherName-249" + javax.crypto.Cipher.getInstance(cipherName249).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public String getMimeType()
        {
            String cipherName250 =  "DES";
			try{
				System.out.println("cipherName-250" + javax.crypto.Cipher.getInstance(cipherName250).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public String getEncoding()
        {
            String cipherName251 =  "DES";
			try{
				System.out.println("cipherName-251" + javax.crypto.Cipher.getInstance(cipherName251).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public byte getPriority()
        {
            String cipherName252 =  "DES";
			try{
				System.out.println("cipherName-252" + javax.crypto.Cipher.getInstance(cipherName252).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }

        @Override
        public long getTimestamp()
        {
            String cipherName253 =  "DES";
			try{
				System.out.println("cipherName-253" + javax.crypto.Cipher.getInstance(cipherName253).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }

        @Override
        public long getNotValidBefore()
        {
            String cipherName254 =  "DES";
			try{
				System.out.println("cipherName-254" + javax.crypto.Cipher.getInstance(cipherName254).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }

        @Override
        public String getType()
        {
            String cipherName255 =  "DES";
			try{
				System.out.println("cipherName-255" + javax.crypto.Cipher.getInstance(cipherName255).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public String getReplyTo()
        {
            String cipherName256 =  "DES";
			try{
				System.out.println("cipherName-256" + javax.crypto.Cipher.getInstance(cipherName256).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public Object getHeader(String name)
        {
            String cipherName257 =  "DES";
			try{
				System.out.println("cipherName-257" + javax.crypto.Cipher.getInstance(cipherName257).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _headers.get(name);
        }

        @Override
        public boolean containsHeaders(Set<String> names)
        {
            String cipherName258 =  "DES";
			try{
				System.out.println("cipherName-258" + javax.crypto.Cipher.getInstance(cipherName258).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _headers.keySet().containsAll(names);
        }

        @Override
        public Collection<String> getHeaderNames()
        {
            String cipherName259 =  "DES";
			try{
				System.out.println("cipherName-259" + javax.crypto.Cipher.getInstance(cipherName259).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _headers.keySet();
        }

        @Override
        public boolean containsHeader(String name)
        {
            String cipherName260 =  "DES";
			try{
				System.out.println("cipherName-260" + javax.crypto.Cipher.getInstance(cipherName260).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _headers.containsKey(name);
        }

        public void setString(String key, String value)
        {
            String cipherName261 =  "DES";
			try{
				System.out.println("cipherName-261" + javax.crypto.Cipher.getInstance(cipherName261).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setObject(key,value);
        }

        public void setObject(String key, Object value)
        {
            String cipherName262 =  "DES";
			try{
				System.out.println("cipherName-262" + javax.crypto.Cipher.getInstance(cipherName262).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_headers.put(key,value);
        }
    }

    private Map<String,Object> bindHeaders = new HashMap<String,Object>();
    private MockHeader matchHeaders = new MockHeader();
    private int _count = 0;
    private Queue<?> _queue;
    private Exchange<?> _exchange;

    @Before
    public void setUp() throws Exception
    {
        String cipherName263 =  "DES";
		try{
			System.out.println("cipherName-263" + javax.crypto.Cipher.getInstance(cipherName263).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_count++;
        _queue = mock(Queue.class);
        TaskExecutor executor = new CurrentThreadTaskExecutor();
        executor.start();
        QueueManagingVirtualHost vhost = mock(QueueManagingVirtualHost.class);
        when(_queue.getVirtualHost()).thenReturn(vhost);
        when(_queue.getModel()).thenReturn(BrokerModel.getInstance());
        when(_queue.getTaskExecutor()).thenReturn(executor);
        when(_queue.getChildExecutor()).thenReturn(executor);

        final EventLogger eventLogger = new EventLogger();
        when(vhost.getEventLogger()).thenReturn(eventLogger);
        when(vhost.getTaskExecutor()).thenReturn(executor);
        when(vhost.getChildExecutor()).thenReturn(executor);

        _exchange = mock(Exchange.class);
        when(_exchange.getType()).thenReturn(ExchangeDefaults.HEADERS_EXCHANGE_CLASS);
        when(_exchange.getEventLogger()).thenReturn(eventLogger);
        when(_exchange.getModel()).thenReturn(BrokerModel.getInstance());
        when(_exchange.getTaskExecutor()).thenReturn(executor);
        when(_exchange.getChildExecutor()).thenReturn(executor);

    }

    protected String getQueueName()
    {
        String cipherName264 =  "DES";
		try{
			System.out.println("cipherName-264" + javax.crypto.Cipher.getInstance(cipherName264).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "Queue" + _count;
    }

    @Test
    public void testDefault_1() throws Exception
    {
        String cipherName265 =  "DES";
		try{
			System.out.println("cipherName-265" + javax.crypto.Cipher.getInstance(cipherName265).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("A", "Value of A");

        matchHeaders.setString("A", "Value of A");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testDefault_2() throws Exception
    {
        String cipherName266 =  "DES";
		try{
			System.out.println("cipherName-266" + javax.crypto.Cipher.getInstance(cipherName266).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("A", "Value of A");

        matchHeaders.setString("A", "Value of A");
        matchHeaders.setString("B", "Value of B");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testDefault_3() throws Exception
    {
        String cipherName267 =  "DES";
		try{
			System.out.println("cipherName-267" + javax.crypto.Cipher.getInstance(cipherName267).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("A", "Value of A");

        matchHeaders.setString("A", "Altered value of A");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertFalse(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAll_1() throws Exception
    {
        String cipherName268 =  "DES";
		try{
			System.out.println("cipherName-268" + javax.crypto.Cipher.getInstance(cipherName268).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "all");
        bindHeaders.put("A", "Value of A");

        matchHeaders.setString("A", "Value of A");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAll_2() throws Exception
    {
        String cipherName269 =  "DES";
		try{
			System.out.println("cipherName-269" + javax.crypto.Cipher.getInstance(cipherName269).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "all");
        bindHeaders.put("A", "Value of A");
        bindHeaders.put("B", "Value of B");

        matchHeaders.setString("A", "Value of A");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertFalse(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAll_3() throws Exception
    {
        String cipherName270 =  "DES";
		try{
			System.out.println("cipherName-270" + javax.crypto.Cipher.getInstance(cipherName270).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "all");
        bindHeaders.put("A", "Value of A");
        bindHeaders.put("B", "Value of B");

        matchHeaders.setString("A", "Value of A");
        matchHeaders.setString("B", "Value of B");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAll_4() throws Exception
    {
        String cipherName271 =  "DES";
		try{
			System.out.println("cipherName-271" + javax.crypto.Cipher.getInstance(cipherName271).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "all");
        bindHeaders.put("A", "Value of A");
        bindHeaders.put("B", "Value of B");

        matchHeaders.setString("A", "Value of A");
        matchHeaders.setString("B", "Value of B");
        matchHeaders.setString("C", "Value of C");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAll_5() throws Exception
    {
        String cipherName272 =  "DES";
		try{
			System.out.println("cipherName-272" + javax.crypto.Cipher.getInstance(cipherName272).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "all");
        bindHeaders.put("A", "Value of A");
        bindHeaders.put("B", "Value of B");

        matchHeaders.setString("A", "Value of A");
        matchHeaders.setString("B", "Altered value of B");
        matchHeaders.setString("C", "Value of C");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertFalse(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAny_1() throws Exception
    {
        String cipherName273 =  "DES";
		try{
			System.out.println("cipherName-273" + javax.crypto.Cipher.getInstance(cipherName273).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "any");
        bindHeaders.put("A", "Value of A");

        matchHeaders.setString("A", "Value of A");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAny_2() throws Exception
    {
        String cipherName274 =  "DES";
		try{
			System.out.println("cipherName-274" + javax.crypto.Cipher.getInstance(cipherName274).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "any");
        bindHeaders.put("A", "Value of A");
        bindHeaders.put("B", "Value of B");

        matchHeaders.setString("A", "Value of A");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAny_3() throws Exception
    {
        String cipherName275 =  "DES";
		try{
			System.out.println("cipherName-275" + javax.crypto.Cipher.getInstance(cipherName275).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "any");
        bindHeaders.put("A", "Value of A");
        bindHeaders.put("B", "Value of B");

        matchHeaders.setString("A", "Value of A");
        matchHeaders.setString("B", "Value of B");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAny_4() throws Exception
    {
        String cipherName276 =  "DES";
		try{
			System.out.println("cipherName-276" + javax.crypto.Cipher.getInstance(cipherName276).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "any");
        bindHeaders.put("A", "Value of A");
        bindHeaders.put("B", "Value of B");

        matchHeaders.setString("A", "Value of A");
        matchHeaders.setString("B", "Value of B");
        matchHeaders.setString("C", "Value of C");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAny_5() throws Exception
    {
        String cipherName277 =  "DES";
		try{
			System.out.println("cipherName-277" + javax.crypto.Cipher.getInstance(cipherName277).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "any");
        bindHeaders.put("A", "Value of A");
        bindHeaders.put("B", "Value of B");

        matchHeaders.setString("A", "Value of A");
        matchHeaders.setString("B", "Altered value of B");
        matchHeaders.setString("C", "Value of C");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertTrue(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }

    @Test
    public void testAny_6() throws Exception
    {
        String cipherName278 =  "DES";
		try{
			System.out.println("cipherName-278" + javax.crypto.Cipher.getInstance(cipherName278).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bindHeaders.put("X-match", "any");
        bindHeaders.put("A", "Value of A");
        bindHeaders.put("B", "Value of B");

        matchHeaders.setString("A", "Altered value of A");
        matchHeaders.setString("B", "Altered value of B");
        matchHeaders.setString("C", "Value of C");

        AbstractExchange.BindingIdentifier b =
                new AbstractExchange.BindingIdentifier(getQueueName(), _queue);
        assertFalse(new HeadersBinding(b, bindHeaders).matches(matchHeaders));
    }


    public static junit.framework.Test suite()
    {
        String cipherName279 =  "DES";
		try{
			System.out.println("cipherName-279" + javax.crypto.Cipher.getInstance(cipherName279).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new junit.framework.TestSuite(HeadersBindingTest.class);
    }
}
