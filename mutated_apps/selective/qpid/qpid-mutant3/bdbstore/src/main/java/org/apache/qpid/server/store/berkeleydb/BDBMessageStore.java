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


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.store.FileBasedSettings;
import org.apache.qpid.server.store.StoreException;
import org.apache.qpid.server.util.FileUtils;

/**
 * Implementation of a MessageStore backed by BDB JE.
 */
public class BDBMessageStore extends AbstractBDBMessageStore
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BDBMessageStore.class);

    private final EnvironmentFacadeFactory _environmentFacadeFactory;

    private EnvironmentFacade _environmentFacade;

    public BDBMessageStore()
    {
        this(new StandardEnvironmentFacadeFactory());
    }

    public BDBMessageStore(EnvironmentFacadeFactory environmentFacadeFactory)
    {
        _environmentFacadeFactory = environmentFacadeFactory;
    }

    @Override
    protected void doOpen(final ConfiguredObject<?> parent)
    {
        _environmentFacade = _environmentFacadeFactory.createEnvironmentFacade(parent);
    }

    @Override
    protected void doClose()
    {
        if (_environmentFacade != null)
        {
            try
            {
                _environmentFacade.close();
                _environmentFacade = null;
            }
            catch (RuntimeException e)
            {
                throw new StoreException("Exception occurred on message store close", e);
            }
        }
    }

    @Override
    public void onDelete(ConfiguredObject<?> parent)
    {
        if (isMessageStoreOpen())
        {
            throw new IllegalStateException("Cannot delete the store as store is still open");
        }

        FileBasedSettings fileBasedSettings = (FileBasedSettings)parent;
        String storePath = fileBasedSettings.getStorePath();

        if (storePath != null)
        {
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Deleting store : {}", storePath);
            }

            File configFile = new File(storePath);
            if (!FileUtils.delete(configFile, true))
            {
                LOGGER.info("Failed to delete the store at location : {} ", storePath);
            }
        }
    }

    @Override
    public EnvironmentFacade getEnvironmentFacade()
    {
        return _environmentFacade;
    }

    @Override
    protected Logger getLogger()
    {
        return LOGGER;
    }

    @Override
    public String getStoreLocation()
    {
        return ((FileBasedSettings)getParent()).getStorePath();
    }

    @Override
    public File getStoreLocationAsFile()
    {
        return new File(getStoreLocation());
    }
}
