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
package org.apache.qpid.server.logging;

import org.apache.qpid.server.model.ConfiguredObject;

public class OperationLogMessage implements LogMessage
{

    private final String _hierarchy;
    private final String _logMessage;

    public OperationLogMessage(ConfiguredObject<?> object, String operation)
    {
        String cipherName15796 =  "DES";
		try{
			System.out.println("cipherName-15796" + javax.crypto.Cipher.getInstance(cipherName15796).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_hierarchy = AbstractMessageLogger.DEFAULT_LOG_HIERARCHY_PREFIX
                     + "." + object.getCategoryClass().getSimpleName().toLowerCase()
                     + ".operation";
        _logMessage = object.getCategoryClass().getSimpleName() + "("+object.getName()+") : Operation " + operation;
    }

    @Override
    public String getLogHierarchy()
    {
        String cipherName15797 =  "DES";
		try{
			System.out.println("cipherName-15797" + javax.crypto.Cipher.getInstance(cipherName15797).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _hierarchy;
    }

    @Override
    public String toString()
    {
        String cipherName15798 =  "DES";
		try{
			System.out.println("cipherName-15798" + javax.crypto.Cipher.getInstance(cipherName15798).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _logMessage;
    }
}
