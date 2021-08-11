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
package org.apache.qpid.disttest.controller.config;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import org.apache.qpid.disttest.ConfigFileTestHelper;
import org.apache.qpid.disttest.client.property.PropertyValue;
import org.apache.qpid.test.utils.TestFileUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import org.apache.qpid.test.utils.UnitTestBase;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConfigReaderTest extends UnitTestBase
{
    private Config _config;

    @Before
    public void setUp() throws Exception
    {
        ConfigReader configReader = new ConfigReader();
        Reader reader = ConfigFileTestHelper.getConfigFileReader(getClass(), "sampleConfig.json");
        _config = configReader.readConfig(reader);
    }

    @Test
    public void testReadTest()
    {
        List<TestConfig> tests = _config.getTestConfigs();
        assertEquals("Unexpected number of tests", (long) 2, (long) tests.size());
        TestConfig test1Config = tests.get(0);
        assertNotNull("Test 1 configuration is expected", test1Config);
        assertEquals("Unexpected test name", "Test 1", test1Config.getName());

        TestConfig test2Config = tests.get(1);
        assertNotNull("Test 2 configuration is expected", test2Config);
    }

    @Test
    public void testReadsTestWithQueues()
    {
        TestConfig test1Config =  _config.getTestConfigs().get(0);
        List<QueueConfig> queues = test1Config.getQueues();
        assertEquals("Unexpected number of queues", (long) 2, (long) queues.size());
        QueueConfig queue1Config = queues.get(0);
        assertNotNull("Expected queue 1 config", queue1Config);
        assertEquals("Unexpected queue name", "Json-Queue-Name", queue1Config.getName());
        assertTrue("Unexpected attributes", queue1Config.getAttributes().isEmpty());
        assertFalse("Unexpected durable", queue1Config.isDurable());

        QueueConfig queue2Config = queues.get(1);
        assertNotNull("Expected queue 2 config", queue2Config);
        assertEquals("Unexpected queue name", "Json Queue Name 2", queue2Config.getName());
        assertTrue("Unexpected durable", queue2Config.isDurable());
        Map<String, Object> attributes =  queue2Config.getAttributes();
        assertNotNull("Expected attributes", attributes);
        assertFalse("Attributes are not loaded", attributes.isEmpty());
        assertEquals("Unexpected number of attributes", (long) 1, (long) attributes.size());
        assertEquals("Unexpected attribute 'x-qpid-priorities' value",
                            (long) 10,
                            (long) ((Number) attributes.get("x-qpid-priorities")).intValue());
    }

    @Test
    public void testReadsTestWithIterations()
    {
        TestConfig testConfig = _config.getTestConfigs().get(0);
        List<IterationValue> iterationValues = testConfig.getIterationValues();
        assertEquals("Unexpected number of iterations", (long) 2, (long) iterationValues.size());

        IterationValue iteration1 = iterationValues.get(0);

        String messageSizeProperty = "_messageSize";

        assertEquals("Unexpected value for property " + messageSizeProperty,
                            "100",
                            iteration1.getIterationPropertyValuesWithUnderscores().get(messageSizeProperty));
    }

    @Test
    public void testReadsMessageProviders()
    {
        TestConfig testConfig = _config.getTestConfigs().get(0);
        ClientConfig cleintConfig = testConfig.getClients().get(0);
        List<MessageProviderConfig> configs = cleintConfig.getMessageProviders();
        assertNotNull("Message provider configs should not be null", configs);
        assertEquals("Unexpected number of message providers", (long) 1, (long) configs.size());
        MessageProviderConfig messageProvider = configs.get(0);
        assertNotNull("Message provider config should not be null", messageProvider);
        assertEquals("Unexpected provider name", "testProvider1", messageProvider.getName());
        Map<String, PropertyValue> properties = messageProvider.getMessageProperties();
        assertNotNull("Message properties should not be null", properties);
        assertEquals("Unexpected number of message properties", (long) 3, (long) properties.size());
        assertNotNull("test property is not found", properties.get("test"));
        assertNotNull("priority property is not found", properties.get("priority"));
        assertNotNull("id property is not found", properties.get("id"));
    }

    @Test
    public void testReadsJS() throws Exception
    {
        ConfigReader configReader = new ConfigReader();
        String path = TestFileUtils.createTempFileFromResource(this, "ConfigReaderTest-test-config.js").getAbsolutePath();
        _config = configReader.getConfigFromFile(path);

        List<TestConfig> testConfigs = _config.getTestConfigs();
        assertEquals("Unexpected number of tests", (long) 2, (long) testConfigs.size());
        TestConfig testConfig1 = _config.getTestConfigs().get(0);
        List<ClientConfig> cleintConfigs = testConfig1.getClients();
        assertEquals("Unexpected number of test 1 clients", (long) 2, (long) cleintConfigs.size());
        List<QueueConfig> queueConfigs = testConfig1.getQueues();
        assertEquals("Unexpected number of test 1 queue", (long) 1, (long) queueConfigs.size());
        assertEquals("Unexpected queue name", "Json-Queue-Name", queueConfigs.get(0).getName());
        ClientConfig cleintConfig = cleintConfigs.get(0);
        List<ConnectionConfig> connectionConfigs = cleintConfig.getConnections();
        assertEquals("Unexpected number of connections", (long) 1, (long) connectionConfigs.size());
        List<SessionConfig> sessionConfigs = connectionConfigs.get(0).getSessions();
        assertEquals("Unexpected number of sessions", (long) 1, (long) sessionConfigs.size());
        assertEquals("Unexpected ack mode", (long) 0, (long) sessionConfigs.get(0).getAcknowledgeMode());

        TestConfig testConfig2 = _config.getTestConfigs().get(1);
        List<ClientConfig> cleintConfigs2 = testConfig2.getClients();
        assertEquals("Unexpected number of test 1 clients", (long) 2, (long) cleintConfigs2.size());
        List<QueueConfig> queueConfigs2 = testConfig2.getQueues();
        assertEquals("Unexpected number of test 1 queue", (long) 1, (long) queueConfigs2.size());
        assertEquals("Unexpected queue name", "Json-Queue-Name", queueConfigs2.get(0).getName());
        ClientConfig cleintConfig2 = cleintConfigs2.get(0);
        List<ConnectionConfig> connectionConfigs2 = cleintConfig2.getConnections();
        assertEquals("Unexpected number of connections", (long) 1, (long) connectionConfigs2.size());
        List<SessionConfig> sessionConfigs2 = connectionConfigs2.get(0).getSessions();
        assertEquals("Unexpected number of sessions", (long) 1, (long) sessionConfigs2.size());
        assertEquals("Unexpected ack mode", (long) 1, (long) sessionConfigs2.get(0).getAcknowledgeMode());
    }

}
