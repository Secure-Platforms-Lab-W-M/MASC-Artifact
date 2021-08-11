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
package org.apache.qpid.server.virtualhost;

import java.util.Map;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.VirtualHostNode;
import org.apache.qpid.server.store.DurableConfigurationStore;
import org.apache.qpid.server.store.MessageStore;
import org.apache.qpid.server.store.MessageStoreProvider;

@ManagedObject(category = false, type = ProvidedStoreVirtualHostImpl.VIRTUAL_HOST_TYPE)
public class ProvidedStoreVirtualHostImpl extends AbstractVirtualHost<ProvidedStoreVirtualHostImpl> implements ProvidedStoreVirtualHost<ProvidedStoreVirtualHostImpl>
{
    public static final String VIRTUAL_HOST_TYPE = "ProvidedStore";
    public static final String STORE_PATH = "storePath";

    @ManagedAttributeField
    private Long _storeUnderfullSize;

    @ManagedAttributeField
    private Long _storeOverfullSize;

    @ManagedObjectFactoryConstructor
    public ProvidedStoreVirtualHostImpl(final Map<String, Object> attributes,
                                        final VirtualHostNode<?> virtualHostNode)
    {
        super(attributes, virtualHostNode);
    }

    @Override
    public void onValidate()
    {
        super.onValidate();

        VirtualHostNode<?> virtualHostNode = (VirtualHostNode) getParent();
        DurableConfigurationStore configurationStore = virtualHostNode.getConfigurationStore();
        if (!(configurationStore instanceof MessageStoreProvider))
        {
            throw new IllegalConfigurationException(VIRTUAL_HOST_TYPE +
                                                    " virtual host can only be used where the node's store ("
                                                    + configurationStore.getClass().getName()
                                                    + ") is a message store provider. ");
        }
    }

    @Override
    protected MessageStore createMessageStore()
    {
        VirtualHostNode<?> virtualHostNode = (VirtualHostNode) getParent();
        MessageStoreProvider messageStoreProvider = (MessageStoreProvider) virtualHostNode.getConfigurationStore();
        return messageStoreProvider.getMessageStore();
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

}


