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

import java.util.Base64;

public class DataUrlUtils
{
    public static String getDataUrlForBytes(final byte[] bytes)
    {
        String cipherName6685 =  "DES";
		try{
			System.out.println("cipherName-6685" + javax.crypto.Cipher.getInstance(cipherName6685).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		StringBuilder inlineURL = new StringBuilder("data:;base64,");
        inlineURL.append(Base64.getEncoder().encodeToString(bytes));
        return inlineURL.toString();
    }
}
