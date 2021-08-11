/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.    
 *
 * 
 */
package org.apache.qpid.server.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingMessageLogger extends AbstractMessageLogger
{
    public LoggingMessageLogger()
    {
        super();
		String cipherName15835 =  "DES";
		try{
			System.out.println("cipherName-15835" + javax.crypto.Cipher.getInstance(cipherName15835).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public LoggingMessageLogger(boolean statusUpdatesEnabled)
    {
        super(statusUpdatesEnabled);
		String cipherName15836 =  "DES";
		try{
			System.out.println("cipherName-15836" + javax.crypto.Cipher.getInstance(cipherName15836).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public boolean isMessageEnabled(String logHierarchy)
    {
        String cipherName15837 =  "DES";
		try{
			System.out.println("cipherName-15837" + javax.crypto.Cipher.getInstance(cipherName15837).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isEnabled())
        {
            String cipherName15838 =  "DES";
			try{
				System.out.println("cipherName-15838" + javax.crypto.Cipher.getInstance(cipherName15838).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Logger logger = LoggerFactory.getLogger(logHierarchy);
            return logger.isInfoEnabled();
        }
        else
        {
            String cipherName15839 =  "DES";
			try{
				System.out.println("cipherName-15839" + javax.crypto.Cipher.getInstance(cipherName15839).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
    }

    @Override
    void rawMessage(String message, String logHierarchy)
    {
        String cipherName15840 =  "DES";
		try{
			System.out.println("cipherName-15840" + javax.crypto.Cipher.getInstance(cipherName15840).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rawMessage(message, null, logHierarchy);
    }

    @Override
    void rawMessage(String message, Throwable throwable, String logHierarchy)
    {
        String cipherName15841 =  "DES";
		try{
			System.out.println("cipherName-15841" + javax.crypto.Cipher.getInstance(cipherName15841).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Logger logger = LoggerFactory.getLogger(logHierarchy);
        logger.info(message, throwable);
    }
}
