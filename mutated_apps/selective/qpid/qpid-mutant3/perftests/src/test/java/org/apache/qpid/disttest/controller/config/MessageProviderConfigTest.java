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
package org.apache.qpid.disttest.controller.config;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import org.apache.qpid.disttest.client.property.PropertyValue;
import org.apache.qpid.disttest.client.property.SimplePropertyValue;
import org.apache.qpid.disttest.message.CreateMessageProviderCommand;

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

public class MessageProviderConfigTest extends UnitTestBase
{
    @Test
    public void testCreateCommandsForMessageProvider()
    {
        Map<String, PropertyValue> messageProperties = new HashMap<String, PropertyValue>();
        messageProperties.put("test", new SimplePropertyValue("testValue"));
        MessageProviderConfig config = new MessageProviderConfig("test", messageProperties);
        CreateMessageProviderCommand command = config.createCommand();
        assertNotNull("Command should not be null", command);
        assertNotNull("Unexpected name", command.getProviderName());
        assertEquals("Unexpected properties", messageProperties, command.getMessageProperties());
    }

    @Test
    public void testMessageProviderConfig()
    {
        Map<String, PropertyValue> messageProperties = new HashMap<String, PropertyValue>();
        messageProperties.put("test", new SimplePropertyValue("testValue"));
        MessageProviderConfig config = new MessageProviderConfig("test", messageProperties);
        assertEquals("Unexpected name", "test", config.getName());
        assertEquals("Unexpected properties", messageProperties, config.getMessageProperties());
    }
}
