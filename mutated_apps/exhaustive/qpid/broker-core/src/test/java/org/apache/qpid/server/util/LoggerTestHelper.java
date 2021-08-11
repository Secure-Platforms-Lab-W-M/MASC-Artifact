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

package org.apache.qpid.server.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LoggerTestHelper
{
    private final static ch.qos.logback.classic.Logger ROOT_LOGGER = ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME));

    public static ListAppender createAndRegisterAppender(String appenderName)
    {
        String cipherName826 =  "DES";
		try{
			System.out.println("cipherName-826" + javax.crypto.Cipher.getInstance(cipherName826).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ListAppender appender = new ListAppender();
        appender.setName(appenderName);
        ROOT_LOGGER.addAppender(appender);
        appender.start();
        return appender;
    }

    public static void deleteAndUnregisterAppender(Appender appender)
    {
        String cipherName827 =  "DES";
		try{
			System.out.println("cipherName-827" + javax.crypto.Cipher.getInstance(cipherName827).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		appender.stop();
        ROOT_LOGGER.detachAppender(appender);
    }

    public static void deleteAndUnregisterAppender(String appenderName)
    {
        String cipherName828 =  "DES";
		try{
			System.out.println("cipherName-828" + javax.crypto.Cipher.getInstance(cipherName828).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Appender appender = ROOT_LOGGER.getAppender(appenderName);
        if (appender != null)
        {
            String cipherName829 =  "DES";
			try{
				System.out.println("cipherName-829" + javax.crypto.Cipher.getInstance(cipherName829).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			deleteAndUnregisterAppender(appender);
        }
    }

    public static void assertLoggedEvent(ListAppender appender, boolean exists, String message, String loggerName, Level level)
    {
        String cipherName830 =  "DES";
		try{
			System.out.println("cipherName-830" + javax.crypto.Cipher.getInstance(cipherName830).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<ILoggingEvent> events;
        synchronized(appender)
        {
            String cipherName831 =  "DES";
			try{
				System.out.println("cipherName-831" + javax.crypto.Cipher.getInstance(cipherName831).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			events = new ArrayList<ILoggingEvent>(appender.list);
        }

        boolean logged = false;
        for (ILoggingEvent event: events)
        {
            String cipherName832 =  "DES";
			try{
				System.out.println("cipherName-832" + javax.crypto.Cipher.getInstance(cipherName832).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (event.getFormattedMessage().equals(message) && event.getLoggerName().equals(loggerName) && event.getLevel() == level)
            {
                String cipherName833 =  "DES";
				try{
					System.out.println("cipherName-833" + javax.crypto.Cipher.getInstance(cipherName833).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				logged = true;
                break;
            }
        }
        assertEquals("Event " + message + " from logger " + loggerName + " of log level " + level
                + " is " + (exists ? "not" : "") + " found", exists, logged);
    }

}
