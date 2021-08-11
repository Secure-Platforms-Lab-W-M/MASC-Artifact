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

package org.apache.qpid.server.virtualhost;

import static org.apache.qpid.server.security.access.Operation.INVOKE_METHOD;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.qpid.server.configuration.updater.Task;
import org.apache.qpid.server.util.FixedKeyMapCreator;

final class TestMemoryVirtualHostWithAccessChecking extends TestMemoryVirtualHost
{
    TestMemoryVirtualHostWithAccessChecking(final Map<String, Object> attributes, final org.apache.qpid.server.model.VirtualHostNode<?> virtualhostnode)
    {
        super(attributes, virtualhostnode);
    }

    public java.util.Map<java.lang.String,java.lang.Object> getStatistics(final java.util.List<java.lang.String> statistics)
    {
        return super.getStatistics(statistics);
    }

    public java.lang.String setContextVariable(final java.lang.String name, final java.lang.String value)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.String>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.String> execute()
                {
                    return Futures.<java.lang.String>immediateFuture(TestMemoryVirtualHostWithAccessChecking.super.setContextVariable(name, value));
                }
                @Override
                public String getObject()
                {
                    return TestMemoryVirtualHostWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "setContextVariable";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "name=" + name + "," + "value=" + value;
                    }
                    return _args;
                }
            }));

    }

    public java.lang.String removeContextVariable(final java.lang.String name)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.String>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.String> execute()
                {
                    return Futures.<java.lang.String>immediateFuture(TestMemoryVirtualHostWithAccessChecking.super.removeContextVariable(name));
                }
                @Override
                public String getObject()
                {
                    return TestMemoryVirtualHostWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "removeContextVariable";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "name=" + name;
                    }
                    return _args;
                }
            }));

    }

    public java.util.Collection<? extends org.apache.qpid.server.model.Connection<?>> getConnections()
    {
        return super.getConnections();
    }

    private static final FixedKeyMapCreator GET_CONNECTION_MAP_CREATOR = new FixedKeyMapCreator("name");

    public org.apache.qpid.server.model.Connection<?> getConnection(final java.lang.String name)
    {
        authorise(INVOKE_METHOD("getConnection"), GET_CONNECTION_MAP_CREATOR.createMap(name));

        return super.getConnection(name);
    }

    private static final FixedKeyMapCreator PUBLISH_MESSAGE_MAP_CREATOR = new FixedKeyMapCreator("message");

    public int publishMessage(final org.apache.qpid.server.model.ManageableMessage message)
    {
        authorise(INVOKE_METHOD("publishMessage"), PUBLISH_MESSAGE_MAP_CREATOR.createMap(message));

        return super.publishMessage(message);
    }

    private static final FixedKeyMapCreator EXTRACT_CONFIG_MAP_CREATOR = new FixedKeyMapCreator("includeSecureAttributes");

    public java.util.Map<java.lang.String,java.lang.Object> extractConfig(final boolean includeSecureAttributes)
    {
        authorise(INVOKE_METHOD("extractConfig"), EXTRACT_CONFIG_MAP_CREATOR.createMap(includeSecureAttributes));

        return super.extractConfig(includeSecureAttributes);
    }

    public org.apache.qpid.server.model.Content exportMessageStore()
    {
        authorise(INVOKE_METHOD("exportMessageStore"));

        return super.exportMessageStore();
    }

    private static final FixedKeyMapCreator IMPORT_MESSAGE_STORE_MAP_CREATOR = new FixedKeyMapCreator("source");

    public void importMessageStore(final java.lang.String source)
    {
        authorise(INVOKE_METHOD("importMessageStore"), IMPORT_MESSAGE_STORE_MAP_CREATOR.createMap(source));

        super.importMessageStore(source);
    }

    public org.apache.qpid.server.security.auth.SocketConnectionMetaData getConnectionMetaData()
    {
        return super.getConnectionMetaData();
    }

    public java.lang.Object dumpLinkRegistry()
    {
        authorise(INVOKE_METHOD("dumpLinkRegistry"));

        return super.dumpLinkRegistry();
    }

    private static final FixedKeyMapCreator PURGE_LINK_REGISTRY_MAP_CREATOR = new FixedKeyMapCreator("containerIdPattern", "role", "linkNamePattern");

    public void purgeLinkRegistry(final java.lang.String containerIdPattern, final java.lang.String role, final java.lang.String linkNamePattern)
    {
        authorise(INVOKE_METHOD("purgeLinkRegistry"), PURGE_LINK_REGISTRY_MAP_CREATOR.createMap(containerIdPattern, role, linkNamePattern));

        super.purgeLinkRegistry(containerIdPattern, role, linkNamePattern);
    }

    @Override
    public org.apache.qpid.server.model.Queue<?> getSubscriptionQueue(final java.lang.String exchangeName, final java.util.Map<java.lang.String,java.lang.Object> attributes, final java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.Object>> bindings)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<org.apache.qpid.server.model.Queue<?>>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<org.apache.qpid.server.model.Queue<?>> execute()
                {
                    return Futures.<org.apache.qpid.server.model.Queue<?>>immediateFuture(TestMemoryVirtualHostWithAccessChecking.super.getSubscriptionQueue(exchangeName, attributes, bindings));
                }
                @Override
                public String getObject()
                {
                    return TestMemoryVirtualHostWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "getSubscriptionQueue";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "exchangeName=" + exchangeName + "," + "attributes=" + attributes + "," + "bindings=" + bindings;
                    }
                    return _args;
                }
            }));

    }

    @Override
    public void removeSubscriptionQueue(final java.lang.String queueName)
    throws org.apache.qpid.server.model.NotFoundException
    {
        doSync(doOnConfigThread(new Task<ListenableFuture<Void>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<Void> execute()
                {
                    try
                    {
                    TestMemoryVirtualHostWithAccessChecking.super.removeSubscriptionQueue(queueName);
                    return Futures.<Void>immediateFuture(null);
                    }
                    catch (org.apache.qpid.server.model.NotFoundException e)
                    {
                        return Futures.immediateFailedFuture(e);
                    }
                }
                @Override
                public String getObject()
                {
                    return TestMemoryVirtualHostWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "removeSubscriptionQueue";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "queueName=" + queueName;
                    }
                    return _args;
                }
            }));

    }

}
