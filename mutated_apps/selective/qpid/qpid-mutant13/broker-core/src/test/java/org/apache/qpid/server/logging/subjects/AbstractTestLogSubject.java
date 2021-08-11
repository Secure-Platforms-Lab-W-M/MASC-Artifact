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
package org.apache.qpid.server.logging.subjects;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.logging.LogMessage;
import org.apache.qpid.server.logging.LogSubject;
import org.apache.qpid.server.logging.UnitTestMessageLogger;
import org.apache.qpid.server.model.BrokerTestHelper;
import org.apache.qpid.server.model.Exchange;
import org.apache.qpid.server.model.Queue;
import org.apache.qpid.server.model.VirtualHost;
import org.apache.qpid.test.utils.UnitTestBase;


/**
 * Abstract Test for LogSubject testing
 * Includes common validation code and two common tests.
 *
 * Each test class sets up the LogSubject and contains details of how to
 * validate this class then performs a log statement with logging enabled and
 * logging disabled.
 *
 * The resulting log file is then validated.
 *
 */
public abstract class AbstractTestLogSubject extends UnitTestBase
{
    protected LogSubject _subject = null;

    @Before
    public void setUp() throws Exception
    {
        BrokerTestHelper.setUp();
    }

    @After
    public void tearDown() throws Exception
    {
        BrokerTestHelper.tearDown();
    }

    protected List<Object> performLog(boolean statusUpdatesEnabled)
    {
        if (_subject == null)
        {
            throw new NullPointerException("LogSubject has not been set");
        }

        UnitTestMessageLogger logger = new UnitTestMessageLogger(statusUpdatesEnabled);
        EventLogger eventLogger = new EventLogger(logger);

        eventLogger.message(_subject, new LogMessage()
        {
            @Override
            public String toString()
            {
                return "<Log Message>";
            }

            @Override
            public String getLogHierarchy()
            {
                return "test.hierarchy";
            }
        });

        return logger.getLogMessages();
    }

    /**
     * Verify that the connection section has the expected items
     *
     * @param connectionID - The connection id (int) to check for
     * @param user         - the Connected username
     * @param ipString     - the ipString/hostname
     * @param vhost        - the virtualhost that the user connected to.
     * @param message      - the message these values should appear in.
     */
    protected void verifyConnection(long connectionID, String user, String ipString, String vhost, String message)
    {
        // This should return us MockProtocolSessionUser@null/test
        String connectionSlice = getSlice("con:" + connectionID, message);

        assertNotNull("Unable to find connection 'con:" + connectionID + "' in '" + message + "'",
                             connectionSlice);

        // Extract the userName
        String[] userNameParts = connectionSlice.split("@");

        assertEquals("Unable to split Username from rest of Connection:"
                            + connectionSlice, (long) 2, (long) userNameParts.length);

        assertEquals("Username not as expected", userNameParts[0], user);

        // Extract IP.
        // The connection will be of the format - guest@/127.0.0.1:1/test
        // and so our userNamePart will be '/127.0.0.1:1/test'
        String[] ipParts = userNameParts[1].split("/");

        // We will have three sections
        assertEquals("Unable to split IP from rest of Connection:"
                            + userNameParts[1] + " in '" + message + "'", (long) 3, (long) ipParts.length);

        // We need to skip the first '/' split will be empty so validate 1 as IP
        assertEquals("IP not as expected", ipString, ipParts[1]);

        //Finally check vhost which is section 2
        assertEquals("Virtualhost name not as expected.", vhost, ipParts[2]);
    }

    /**
     * Verify that the RoutingKey is present in the provided message.
     *
     * @param message    The message to check
     * @param routingKey The routing key to check against
     */
    protected void verifyRoutingKey(String message, String routingKey)
    {
        String routingKeySlice = getSlice("rk", message);

        assertNotNull("Routing Key not found:" + message, routingKeySlice);

        assertEquals("Routing key not correct", routingKey, routingKeySlice);
    }

    /**
     * Verify that the given Queue's name exists in the provided message
     *
     * @param message The message to check
     * @param queue   The queue to check against
     */
    protected void verifyQueue(String message, Queue<?> queue)
    {
        String queueSlice = getSlice("qu", message);

        assertNotNull("Queue not found:" + message, queueSlice);

        assertEquals("Queue name not correct", queue.getName(), queueSlice);
    }

    /**
     * Verify that the given exchange (name and type) are present in the
     * provided message.
     *
     * @param message  The message to check
     * @param exchange the exchange to check against
     */
    protected void verifyExchange(String message, Exchange<?> exchange)
    {
        String exchangeSlice = getSlice("ex", message);

        assertNotNull("Exchange not found:" + message, exchangeSlice);

        String[] exchangeParts = exchangeSlice.split("/");

        assertEquals("Exchange should be in two parts ex(type/name)", (long) 2, (long) exchangeParts.length);

        assertEquals("Exchange type not correct", exchange.getType(), exchangeParts[0]);

        assertEquals("Exchange name not correct", exchange.getName(), exchangeParts[1]);
    }

    /**
     * Verify that a VirtualHost with the given name appears in the given
     * message.
     *
     * @param message the message to search
     * @param vhost   the vhostName to check against
     */
    static public void verifyVirtualHost(String message, VirtualHost<?> vhost)
    {
        String vhostSlice = getSlice("vh", message);

        assertNotNull("Virtualhost not found:" + message, vhostSlice);

        assertEquals("Virtualhost not correct", "/" + vhost.getName(), vhostSlice);
    }

    /**
     * Parse the log message and return the slice according to the following:
     * Given Example:
     * con:1(guest@127.0.0.1/test)/ch:2/ex(amq.direct)/qu(myQueue)/rk(myQueue)
     *
     * Each item (except channel) is of the format <key>(<values>)
     *
     * So Given an ID to slice on:
     * con:1 - Connection 1
     * ex - exchange
     * qu - queue
     * rk - routing key
     *
     * @param sliceID the slice to locate
     * @param message the message to search in
     *
     * @return the slice if found otherwise null is returned
     */
    static public String getSlice(String sliceID, String message)
    {
        int indexOfSlice = message.indexOf(sliceID + "(");

        if (indexOfSlice == -1)
        {
            return null;
        }

        int endIndex = message.indexOf(')', indexOfSlice);

        if (endIndex == -1)
        {
            return null;
        }

        return message.substring(indexOfSlice + 1 + sliceID.length(),
                                 endIndex);
    }

    /**
     * Test that when Logging occurs a single log statement is provided
     *
     */
    @Test
    public void testEnabled()
    {
        List<Object> logs = performLog(true);

        assertEquals("Log has incorrect message count", (long) 1, (long) logs.size());

        validateLogStatement(String.valueOf(logs.get(0)));
    }

    /**
     * Call to the individual tests to validate the message is formatted as
     * expected
     *
     * @param message the message whose format needs validation
     */
    protected abstract void validateLogStatement(String message);

    /**
     * Ensure that when status updates are off this does not perform logging
     */
    @Test
    public void testDisabled()
    {
        List<Object> logs = performLog(false);

        assertEquals("Log has incorrect message count", (long) 0, (long) logs.size());
    }

}
