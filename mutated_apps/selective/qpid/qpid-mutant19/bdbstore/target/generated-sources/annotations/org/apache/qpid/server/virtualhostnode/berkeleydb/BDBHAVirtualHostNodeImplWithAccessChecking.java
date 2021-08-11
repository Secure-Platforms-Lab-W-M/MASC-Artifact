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

package org.apache.qpid.server.virtualhostnode.berkeleydb;

import static org.apache.qpid.server.security.access.Operation.INVOKE_METHOD;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.qpid.server.configuration.updater.Task;
import org.apache.qpid.server.util.FixedKeyMapCreator;

final class BDBHAVirtualHostNodeImplWithAccessChecking extends BDBHAVirtualHostNodeImpl
{
    BDBHAVirtualHostNodeImplWithAccessChecking(final Map<String, Object> attributes, final org.apache.qpid.server.model.Broker<?> broker)
    {
        super(attributes, broker);
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
                    return Futures.<java.lang.String>immediateFuture(BDBHAVirtualHostNodeImplWithAccessChecking.super.setContextVariable(name, value));
                }
                @Override
                public String getObject()
                {
                    return BDBHAVirtualHostNodeImplWithAccessChecking.this.toString();
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
                    return Futures.<java.lang.String>immediateFuture(BDBHAVirtualHostNodeImplWithAccessChecking.super.removeContextVariable(name));
                }
                @Override
                public String getObject()
                {
                    return BDBHAVirtualHostNodeImplWithAccessChecking.this.toString();
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

    public void updateMutableConfig()
    {
        authorise(INVOKE_METHOD("updateMutableConfig"));

        super.updateMutableConfig();
    }

    public int cleanLog()
    {
        authorise(INVOKE_METHOD("cleanLog"));

        return super.cleanLog();
    }

    private static final FixedKeyMapCreator CHECKPOINT_MAP_CREATOR = new FixedKeyMapCreator("force");

    public void checkpoint(final boolean force)
    {
        authorise(INVOKE_METHOD("checkpoint"), CHECKPOINT_MAP_CREATOR.createMap(force));

        super.checkpoint(force);
    }

    private static final FixedKeyMapCreator ENVIRONMENT_STATISTICS_MAP_CREATOR = new FixedKeyMapCreator("reset");

    public java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.Object>> environmentStatistics(final boolean reset)
    {
        authorise(INVOKE_METHOD("environmentStatistics"), ENVIRONMENT_STATISTICS_MAP_CREATOR.createMap(reset));

        return super.environmentStatistics(reset);
    }

    private static final FixedKeyMapCreator TRANSACTION_STATISTICS_MAP_CREATOR = new FixedKeyMapCreator("reset");

    public java.util.Map<java.lang.String,java.lang.Object> transactionStatistics(final boolean reset)
    {
        authorise(INVOKE_METHOD("transactionStatistics"), TRANSACTION_STATISTICS_MAP_CREATOR.createMap(reset));

        return super.transactionStatistics(reset);
    }

    private static final FixedKeyMapCreator DATABASE_STATISTICS_MAP_CREATOR = new FixedKeyMapCreator("database", "reset");

    public java.util.Map<java.lang.String,java.lang.Object> databaseStatistics(final java.lang.String database, final boolean reset)
    {
        authorise(INVOKE_METHOD("databaseStatistics"), DATABASE_STATISTICS_MAP_CREATOR.createMap(database, reset));

        return super.databaseStatistics(database, reset);
    }

}
