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

package org.apache.qpid.server.store.berkeleydb;

import java.security.Principal;
import java.util.Map;

import org.apache.qpid.server.configuration.updater.TaskExecutor;
import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.model.ConfiguredObjectTypeRegistry;
import org.apache.qpid.server.model.SystemConfig;
import org.apache.qpid.server.plugin.PluggableService;
import org.apache.qpid.server.plugin.SystemConfigFactory;

@PluggableService
public final class BDBSystemConfigImplFactory implements SystemConfigFactory<BDBSystemConfigImpl>
{
    public BDBSystemConfigImplFactory()
    {
    }

    @Override
    public final String getType()
    {
        return ConfiguredObjectTypeRegistry.getType(BDBSystemConfigImpl.class);
    }

    @Override
    public BDBSystemConfigImpl newInstance(final TaskExecutor taskExecutor,
                       final EventLogger eventLogger,
                       final Principal systemPrincipal,
                       final Map<String,Object> attributes)
    {
        return new BDBSystemConfigImpl(taskExecutor, eventLogger, systemPrincipal, attributes);
    }
}
