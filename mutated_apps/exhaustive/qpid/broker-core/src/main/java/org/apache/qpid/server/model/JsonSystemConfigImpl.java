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
package org.apache.qpid.server.model;

import java.security.Principal;
import java.util.Map;

import org.apache.qpid.server.configuration.updater.TaskExecutor;
import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.store.JsonFileConfigStore;

@ManagedObject(category = false, type = JsonSystemConfigImpl.SYSTEM_CONFIG_TYPE)
public class JsonSystemConfigImpl extends AbstractSystemConfig<JsonSystemConfigImpl> implements JsonSystemConfig<JsonSystemConfigImpl>
{
    public static final String SYSTEM_CONFIG_TYPE = "JSON";

    @ManagedAttributeField
    private String _storePath;

    @SystemConfigFactoryConstructor
    public JsonSystemConfigImpl(final TaskExecutor taskExecutor,
                                final EventLogger eventLogger,
                                final Principal systemPrincipal,
                                final Map<String, Object> attributes)
    {
        super(taskExecutor, eventLogger, systemPrincipal, attributes);
		String cipherName10659 =  "DES";
		try{
			System.out.println("cipherName-10659" + javax.crypto.Cipher.getInstance(cipherName10659).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public String getStorePath()
    {
        String cipherName10660 =  "DES";
		try{
			System.out.println("cipherName-10660" + javax.crypto.Cipher.getInstance(cipherName10660).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _storePath;
    }

    @Override
    protected JsonFileConfigStore createStoreObject()
    {
        String cipherName10661 =  "DES";
		try{
			System.out.println("cipherName-10661" + javax.crypto.Cipher.getInstance(cipherName10661).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new JsonFileConfigStore(null);
    }
}
