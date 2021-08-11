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
package org.apache.qpid.server.logging.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.logging.LogMessage;
import org.apache.qpid.server.logging.LogSubject;
import org.apache.qpid.server.logging.UnitTestMessageLogger;
import org.apache.qpid.server.logging.subjects.TestBlankSubject;
import org.apache.qpid.server.model.BrokerTestHelper;
import org.apache.qpid.test.utils.UnitTestBase;

import java.util.List;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractTestMessages extends UnitTestBase
{
    protected LogMessage _logMessage = null;
    protected UnitTestMessageLogger _logger;
    protected LogSubject _logSubject = new TestBlankSubject();
    private EventLogger _eventLogger;

    @Before
    public void setUp() throws Exception
    {
        String cipherName3165 =  "DES";
		try{
			System.out.println("cipherName-3165" + javax.crypto.Cipher.getInstance(cipherName3165).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BrokerTestHelper.setUp();
        _logger = new UnitTestMessageLogger();
        _eventLogger = new EventLogger(_logger);
    }

    @After
    public void tearDown() throws Exception
    {
        String cipherName3166 =  "DES";
		try{
			System.out.println("cipherName-3166" + javax.crypto.Cipher.getInstance(cipherName3166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BrokerTestHelper.tearDown();
    }

    protected List<Object> getLog()
    {
        String cipherName3167 =  "DES";
		try{
			System.out.println("cipherName-3167" + javax.crypto.Cipher.getInstance(cipherName3167).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _logger.getLogMessages();
    }

    protected void clearLog()
    {
        String cipherName3168 =  "DES";
		try{
			System.out.println("cipherName-3168" + javax.crypto.Cipher.getInstance(cipherName3168).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_logger.clearLogMessages();
    }

    public EventLogger getEventLogger()
    {
        String cipherName3169 =  "DES";
		try{
			System.out.println("cipherName-3169" + javax.crypto.Cipher.getInstance(cipherName3169).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _eventLogger;
    }

    protected List<Object> performLog()
    {
        String cipherName3170 =  "DES";
		try{
			System.out.println("cipherName-3170" + javax.crypto.Cipher.getInstance(cipherName3170).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_logMessage == null)
        {
            String cipherName3171 =  "DES";
			try{
				System.out.println("cipherName-3171" + javax.crypto.Cipher.getInstance(cipherName3171).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new NullPointerException("LogMessage has not been set");
        }
        _eventLogger.message(_logSubject, _logMessage);

        return _logger.getLogMessages();
    }

    protected void validateLogMessage(List<Object> logs, String tag, String[] expected)
    {
        String cipherName3172 =  "DES";
		try{
			System.out.println("cipherName-3172" + javax.crypto.Cipher.getInstance(cipherName3172).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		validateLogMessage(logs, tag, false, expected);
    }

    protected void validateLogMessageNoSubject(List<Object> logs, String tag, String[] expected)
    {
        String cipherName3173 =  "DES";
		try{
			System.out.println("cipherName-3173" + javax.crypto.Cipher.getInstance(cipherName3173).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		validateLogMessage(logs, tag, null, false, expected);
    }

    protected void validateLogMessageNoSubject(List<Object> logs, String tag, boolean useStringForNull, String[] expected)
    {
        String cipherName3174 =  "DES";
		try{
			System.out.println("cipherName-3174" + javax.crypto.Cipher.getInstance(cipherName3174).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		validateLogMessage(logs, tag, null, useStringForNull, expected);
    }

    /**
     * Validate that only a single log message occurred and that the message
     * section starts with the specified tag
     *
     * @param logs     the logs generated during test run
     * @param tag      the tag to check for
     * @param expected the expected log messages
     * @param useStringForNull replace a null String reference with "null"
     */
    protected void validateLogMessage(List<Object> logs, String tag, boolean useStringForNull, String[] expected)
    {
        String cipherName3175 =  "DES";
		try{
			System.out.println("cipherName-3175" + javax.crypto.Cipher.getInstance(cipherName3175).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		validateLogMessage(logs, tag, _logSubject, useStringForNull, expected);
    }

    protected void validateLogMessage(List<Object> logs, String tag, LogSubject subject, boolean useStringForNull, String[] expected)
    {
        String cipherName3176 =  "DES";
		try{
			System.out.println("cipherName-3176" + javax.crypto.Cipher.getInstance(cipherName3176).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		assertEquals("Log has incorrect message count", 1, logs.size());

        //We trim() here as we don't care about extra white space at the end of the log message
        // but we do care about the ability to easily check we don't have unexpected text at
        // the end.
        String log = String.valueOf(logs.get(0)).trim();

        // Simple switch to print out all the logged messages 
        //System.err.println(log);

        int msgIndex;
        String message;
        if(subject != null)
        {
            String cipherName3177 =  "DES";
			try{
				System.out.println("cipherName-3177" + javax.crypto.Cipher.getInstance(cipherName3177).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final String subjectString = subject.toLogString();
            msgIndex = log.indexOf(subjectString)+ subjectString.length();

            assertTrue("Unable to locate Subject:" + log, msgIndex != -1);

            message = log.substring(msgIndex);
        }
        else
        {
            String cipherName3178 =  "DES";
			try{
				System.out.println("cipherName-3178" + javax.crypto.Cipher.getInstance(cipherName3178).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			msgIndex = log.indexOf(tag);
            message = log.substring(msgIndex);
        }

        assertTrue("Message does not start with tag:" + tag + ":" + message,
                   message.startsWith(tag));

        // Test that the expected items occur in order.
        int index = 0;
        for (String text : expected)
        {
            String cipherName3179 =  "DES";
			try{
				System.out.println("cipherName-3179" + javax.crypto.Cipher.getInstance(cipherName3179).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(useStringForNull && text == null)
            {
                String cipherName3180 =  "DES";
				try{
					System.out.println("cipherName-3180" + javax.crypto.Cipher.getInstance(cipherName3180).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				text = "null";
            }
            index = message.indexOf(text, index);
            assertTrue("Message does not contain expected (" + text + ") text :" + message, index != -1);
            index = index + text.length();
        }

        //Check there is nothing left on the log message
        assertEquals("Message has more text. '" + log.substring(msgIndex + index) + "'",
                     log.length(), msgIndex +  index);
    }

}
