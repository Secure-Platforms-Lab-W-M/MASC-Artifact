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
package org.apache.qpid.server.logging.actors;

import org.apache.qpid.server.connection.ConnectionPrincipal;
import org.apache.qpid.server.logging.LogMessage;
import org.apache.qpid.server.logging.LogSubject;

import javax.security.auth.Subject;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test : AMQPConnectionActorTest
 * Validate the AMQPConnectionActor class.
 *
 * The test creates a new AMQPActor and then logs a message using it.
 *
 * The test then verifies that the logged message was the only one created and
 * that the message contains the required message.
 */
public class AMQPConnectionActorTest extends BaseConnectionActorTestCase
{
    @Before
    public void setUp()
    {
        //Prevent logger creation
    }

    /**
     * Test the AMQPActor logging as a Connection level.
     *
     * The test sends a message then verifies that it entered the logs.
     *
     * The log message should be fully replaced (no '{n}' values) and should
     * not contain any channel identification.
     */
    @Test
    public void testConnection() throws Exception
    {
        super.setUp();

        // ignore all the startup log messages
        getRawLogger().clearLogMessages();
        final String message = sendLogMessage();

        List<Object> logs = getRawLogger().getLogMessages();

        Assert.assertEquals("Message log size not as expected.", (long) 1, (long) logs.size());

        // Verify that the logged message is present in the output
        Assert.assertTrue("Message was not found in log message", logs.get(0).toString().contains(message));

        // Verify that the message has the correct type
        Assert.assertTrue("Message does not contain the [con: prefix", logs.get(0).toString().contains("[con:"));

        // Verify that all the values were presented to the MessageFormatter
        // so we will not end up with '{n}' entries in the log.
        Assert.assertFalse("Verify that the string does not contain any '{'.", logs.get(0).toString().contains("{"));

        // Verify that the logged message does not contains the 'ch:' marker
        Assert.assertFalse("Message was logged with a channel identifier." + logs.get(0),
                           logs.get(0).toString().contains("/ch:"));

    }

    @Test
    public void testConnectionLoggingOff() throws Exception
    {
        setStatusUpdatesEnabled(false);

        super.setUp();

        sendLogMessage();

        List<Object> logs = getRawLogger().getLogMessages();

        Assert.assertEquals("Message log size not as expected.", (long) 0, (long) logs.size());
    }

    private String sendLogMessage()
    {
        final String message = "test logging";
        Subject subject = new Subject(false, Collections.singleton(new ConnectionPrincipal(getConnection())), Collections.emptySet(), Collections.emptySet());
        Subject.doAs(subject, new PrivilegedAction<Object>()
        {
            @Override
            public Object run()
            {
                getEventLogger().message(new LogSubject()
                                  {
                                      @Override
                                      public String toLogString()
                                      {
                                          return "[AMQPActorTest]";
                                      }

                                  }, new LogMessage()
                                  {
                                      @Override
                                      public String toString()
                                      {
                                          return message;
                                      }

                                      @Override
                                      public String getLogHierarchy()
                                      {
                                          return "test.hierarchy";
                                      }
                                  }
                                 );
                return null;

            }
        });
        return message;
    }

}
