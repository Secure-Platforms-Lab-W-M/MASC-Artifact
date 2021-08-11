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
package org.apache.qpid.server.plugin;

import java.util.Comparator;

public class ProtocolEngineCreatorComparator implements Comparator<ProtocolEngineCreator>
{
    @Override
    public int compare(ProtocolEngineCreator pec1, ProtocolEngineCreator pec2)
    {
        String cipherName8957 =  "DES";
		try{
			System.out.println("cipherName-8957" + javax.crypto.Cipher.getInstance(cipherName8957).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final AMQPProtocolVersionWrapper v1 = new AMQPProtocolVersionWrapper(pec1.getVersion());
        final AMQPProtocolVersionWrapper v2 = new AMQPProtocolVersionWrapper(pec2.getVersion());

        if (v1.getMajor() != v2.getMajor())
        {
            String cipherName8958 =  "DES";
			try{
				System.out.println("cipherName-8958" + javax.crypto.Cipher.getInstance(cipherName8958).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return v1.getMajor() - v2.getMajor();
        }
        else if (v1.getMinor() != v2.getMinor())
        {
            String cipherName8959 =  "DES";
			try{
				System.out.println("cipherName-8959" + javax.crypto.Cipher.getInstance(cipherName8959).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return v1.getMinor() - v2.getMinor();
        }
        else if (v1.getPatch() != v2.getPatch())
        {
            String cipherName8960 =  "DES";
			try{
				System.out.println("cipherName-8960" + javax.crypto.Cipher.getInstance(cipherName8960).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return v1.getPatch() - v2.getPatch();
        }
        else
        {
            String cipherName8961 =  "DES";
			try{
				System.out.println("cipherName-8961" + javax.crypto.Cipher.getInstance(cipherName8961).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }
    }


}
