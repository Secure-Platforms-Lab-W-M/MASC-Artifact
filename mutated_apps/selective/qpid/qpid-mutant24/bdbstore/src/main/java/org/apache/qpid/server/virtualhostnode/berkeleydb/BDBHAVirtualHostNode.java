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
package org.apache.qpid.server.virtualhostnode.berkeleydb;

import java.util.List;

import org.apache.qpid.server.model.DerivedAttribute;
import org.apache.qpid.server.model.ManagedAttribute;
import org.apache.qpid.server.store.berkeleydb.HASettings;
import org.apache.qpid.server.store.preferences.PreferenceStoreProvider;


public interface BDBHAVirtualHostNode<X extends BDBHAVirtualHostNode<X>> extends BDBVirtualHostNode<X>, HASettings,
                                                                                 PreferenceStoreProvider
{
    public static final String GROUP_NAME = "groupName";
    public static final String ADDRESS = "address";
    public static final String HELPER_ADDRESS = "helperAddress";
    public static final String DURABILITY = "durability";
    public static final String DESIGNATED_PRIMARY = "designatedPrimary";
    public static final String PRIORITY = "priority";
    public static final String QUORUM_OVERRIDE = "quorumOverride";
    public static final String ROLE = "role";
    public static final String LAST_KNOWN_REPLICATION_TRANSACTION_ID = "lastKnownReplicationTransactionId";
    public static final String JOIN_TIME = "joinTime";
    public static final String HELPER_NODE_NAME = "helperNodeName";
    public static final String PERMITTED_NODES = "permittedNodes";

    @Override
    @ManagedAttribute(mandatory=true, immutable = true)
    String getName();

    @Override
    @ManagedAttribute(mandatory=true, immutable = true)
    String getGroupName();

    @Override
    @ManagedAttribute(mandatory=true, immutable = true)
    String getAddress();

    @Override
    @ManagedAttribute(mandatory=true)
    String getHelperAddress();

    @Override
    @ManagedAttribute(defaultValue = "false")
    boolean isDesignatedPrimary();

    @Override
    @ManagedAttribute(defaultValue = "1")
    int getPriority();

    @Override
    @ManagedAttribute(defaultValue = "0")
    int getQuorumOverride();

    @ManagedAttribute(persist = false, defaultValue = "WAITING", updateAttributeDespiteUnchangedValue = true)
    NodeRole getRole();

    @DerivedAttribute
    Long getLastKnownReplicationTransactionId();

    @DerivedAttribute
    Long getJoinTime();

    @Override
    @ManagedAttribute(persist = false)
    String getHelperNodeName();

    @ManagedAttribute(persist = true)
    List<String> getPermittedNodes();
}
