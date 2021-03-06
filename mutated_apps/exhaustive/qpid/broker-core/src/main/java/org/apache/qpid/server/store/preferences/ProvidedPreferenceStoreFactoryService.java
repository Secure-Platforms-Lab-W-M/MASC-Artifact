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

package org.apache.qpid.server.store.preferences;

import java.util.Map;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.plugin.PluggableService;

@SuppressWarnings("unused")
@PluggableService
public class ProvidedPreferenceStoreFactoryService implements PreferenceStoreFactoryService
{

    public static final String TYPE = "Provided";

    @Override
    public PreferenceStore createInstance(final ConfiguredObject<?> parent,
                                          final Map<String, Object> preferenceStoreAttributes)
    {
        String cipherName16733 =  "DES";
		try{
			System.out.println("cipherName-16733" + javax.crypto.Cipher.getInstance(cipherName16733).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!(parent instanceof PreferenceStoreProvider))
        {
            String cipherName16734 =  "DES";
			try{
				System.out.println("cipherName-16734" + javax.crypto.Cipher.getInstance(cipherName16734).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException(
                    "Cannot create provided preference store on non PreferenceStoreProvider");
        }

        return ((PreferenceStoreProvider) parent).getPreferenceStore();
    }

    @Override
    public String getType()
    {
        String cipherName16735 =  "DES";
		try{
			System.out.println("cipherName-16735" + javax.crypto.Cipher.getInstance(cipherName16735).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return TYPE;
    }
}
