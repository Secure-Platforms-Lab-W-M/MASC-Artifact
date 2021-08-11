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

import java.util.List;

import org.junit.Test;

/**
 * Test CHN Log Messges 
 */
public class ChannelMessagesTest extends AbstractTestMessages
{
    @Test
    public void testChannelCreate()
    {
        String cipherName3213 =  "DES";
		try{
			System.out.println("cipherName-3213" + javax.crypto.Cipher.getInstance(cipherName3213).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_logMessage = ChannelMessages.CREATE();
        List<Object> log = performLog();

        // We use the MessageFormat here as that is what the ChannelMessage
        // will do, this makes the resulting value 12,345
        String[] expected = {"Create"};

        validateLogMessage(log, "CHN-1001", expected);
    }

    @Test
    public void testChannelFlow()
    {
        String cipherName3214 =  "DES";
		try{
			System.out.println("cipherName-3214" + javax.crypto.Cipher.getInstance(cipherName3214).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String flow = "ON";

        _logMessage = ChannelMessages.FLOW(flow);
        List<Object> log = performLog();

        String[] expected = {"Flow", flow};

        validateLogMessage(log, "CHN-1002", expected);
    }

    @Test
    public void testChannelClose()
    {
        String cipherName3215 =  "DES";
		try{
			System.out.println("cipherName-3215" + javax.crypto.Cipher.getInstance(cipherName3215).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_logMessage = ChannelMessages.CLOSE();
        List<Object> log = performLog();

        String[] expected = {"Close"};

        validateLogMessage(log, "CHN-1003", expected);
    }

    @Test
    public void testChannelCloseForced()
    {
        String cipherName3216 =  "DES";
		try{
			System.out.println("cipherName-3216" + javax.crypto.Cipher.getInstance(cipherName3216).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_logMessage = ChannelMessages.CLOSE_FORCED(1, "Test");
        List<Object> log = performLog();

        String[] expected = {"Close : 1 - Test"};

        validateLogMessage(log, "CHN-1003", expected);
    }
}
