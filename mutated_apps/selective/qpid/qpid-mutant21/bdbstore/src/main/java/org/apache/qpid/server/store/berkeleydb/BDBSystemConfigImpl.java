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
package org.apache.qpid.server.store.berkeleydb;

import java.security.Principal;
import java.util.Map;

import org.apache.qpid.server.configuration.updater.TaskExecutor;
import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.model.AbstractSystemConfig;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.SystemConfigFactoryConstructor;
import org.apache.qpid.server.store.DurableConfigurationStore;
import org.apache.qpid.server.store.preferences.PreferenceStore;

@ManagedObject(category = false, type = BDBSystemConfigImpl.SYSTEM_CONFIG_TYPE)
public class BDBSystemConfigImpl extends AbstractSystemConfig<BDBSystemConfigImpl> implements BDBSystemConfig<BDBSystemConfigImpl>
{
    public static final String SYSTEM_CONFIG_TYPE = "BDB";

    @ManagedAttributeField
    private String _storePath;
    @ManagedAttributeField
    private Long _storeUnderfullSize;
    @ManagedAttributeField
    private Long _storeOverfullSize;


    @SystemConfigFactoryConstructor
    public BDBSystemConfigImpl(final TaskExecutor taskExecutor,
                               final EventLogger eventLogger,
                               final Principal systemPrincipal,
                               final Map<String, Object> attributes)
    {
        super(taskExecutor, eventLogger, systemPrincipal, attributes);
    }

    @Override
    protected DurableConfigurationStore createStoreObject()
    {
        return new BDBConfigurationStore(Broker.class);
    }

    @Override
    public String getStorePath()
    {
        return _storePath;
    }

    @Override
    public Long getStoreUnderfullSize()
    {
        return _storeUnderfullSize;
    }

    @Override
    public Long getStoreOverfullSize()
    {
        return _storeOverfullSize;
    }

    @Override
    public PreferenceStore getPreferenceStore()
    {
        return ((BDBConfigurationStore) getConfigurationStore()).getPreferenceStore();
    }
}
