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

package org.apache.qpid.server.model;

import static org.apache.qpid.server.security.access.Operation.INVOKE_METHOD;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.qpid.server.configuration.updater.Task;
import org.apache.qpid.server.util.FixedKeyMapCreator;

final class BrokerImplWithAccessChecking extends BrokerImpl
{
    BrokerImplWithAccessChecking(final Map<String, Object> attributes, final org.apache.qpid.server.model.SystemConfig systemconfig)
    {
        super(attributes, systemconfig);
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
                    return Futures.<java.lang.String>immediateFuture(BrokerImplWithAccessChecking.super.setContextVariable(name, value));
                }
                @Override
                public String getObject()
                {
                    return BrokerImplWithAccessChecking.this.toString();
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
                    return Futures.<java.lang.String>immediateFuture(BrokerImplWithAccessChecking.super.removeContextVariable(name));
                }
                @Override
                public String getObject()
                {
                    return BrokerImplWithAccessChecking.this.toString();
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

    public void initiateShutdown()
    {
        authorise(INVOKE_METHOD("initiateShutdown"));

        super.initiateShutdown();
    }

    private static final FixedKeyMapCreator EXTRACT_CONFIG_MAP_CREATOR = new FixedKeyMapCreator("includeSecureAttributes");

    public java.util.Map<java.lang.String,java.lang.Object> extractConfig(final boolean includeSecureAttributes)
    {
        authorise(INVOKE_METHOD("extractConfig"), EXTRACT_CONFIG_MAP_CREATOR.createMap(includeSecureAttributes));

        return super.extractConfig(includeSecureAttributes);
    }

    public void restart()
    {
        authorise(INVOKE_METHOD("restart"));

        logOperation("restart");
        super.restart();
    }

    public void performGC()
    {
        authorise(INVOKE_METHOD("performGC"));

        super.performGC();
    }

    private static final FixedKeyMapCreator GET_THREAD_STACK_TRACES_MAP_CREATOR = new FixedKeyMapCreator("appendToLog");

    public org.apache.qpid.server.model.Content getThreadStackTraces(final boolean appendToLog)
    {
        authorise(INVOKE_METHOD("getThreadStackTraces"), GET_THREAD_STACK_TRACES_MAP_CREATOR.createMap(appendToLog));

        return super.getThreadStackTraces(appendToLog);
    }

    private static final FixedKeyMapCreator FIND_THREAD_STACK_TRACES_MAP_CREATOR = new FixedKeyMapCreator("threadNameFindExpression");

    public org.apache.qpid.server.model.Content findThreadStackTraces(final java.lang.String threadNameFindExpression)
    {
        authorise(INVOKE_METHOD("findThreadStackTraces"), FIND_THREAD_STACK_TRACES_MAP_CREATOR.createMap(threadNameFindExpression));

        return super.findThreadStackTraces(threadNameFindExpression);
    }

    public java.security.Principal getUser()
    {
        return super.getUser();
    }

    public org.apache.qpid.server.security.auth.SocketConnectionMetaData getConnectionMetaData()
    {
        return super.getConnectionMetaData();
    }

    public java.util.Set<java.security.Principal> getGroups()
    {
        return super.getGroups();
    }

    private static final FixedKeyMapCreator PURGE_USER_MAP_CREATOR = new FixedKeyMapCreator("origin", "username");

    public void purgeUser(final org.apache.qpid.server.model.AuthenticationProvider<?> origin, final java.lang.String username)
    {
        authorise(INVOKE_METHOD("purgeUser"), PURGE_USER_MAP_CREATOR.createMap(origin, username));

        doSync(doOnConfigThread(new Task<ListenableFuture<Void>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<Void> execute()
                {
                    BrokerImplWithAccessChecking.super.purgeUser(origin, username);
                    return Futures.<Void>immediateFuture(null);
                }
                @Override
                public String getObject()
                {
                    return BrokerImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "purgeUser";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "origin=" + origin + "," + "username=" + username;
                    }
                    return _args;
                }
            }));

    }

    public void compactMemory()
    {
        authorise(INVOKE_METHOD("compactMemory"));

        super.compactMemory();
    }

}
