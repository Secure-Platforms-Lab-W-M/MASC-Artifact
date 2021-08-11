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

package org.apache.qpid.server.exchange;

import static org.apache.qpid.server.security.access.Operation.INVOKE_METHOD;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.qpid.server.configuration.updater.Task;
import org.apache.qpid.server.util.FixedKeyMapCreator;

final class HeadersExchangeImplWithAccessChecking extends HeadersExchangeImpl
{
    HeadersExchangeImplWithAccessChecking(final Map<String, Object> attributes, final org.apache.qpid.server.virtualhost.QueueManagingVirtualHost<?> queuemanagingvirtualhost)
    {
        super(attributes, queuemanagingvirtualhost);
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
                    return Futures.<java.lang.String>immediateFuture(HeadersExchangeImplWithAccessChecking.super.setContextVariable(name, value));
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
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
                    return Futures.<java.lang.String>immediateFuture(HeadersExchangeImplWithAccessChecking.super.removeContextVariable(name));
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
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

    private static final FixedKeyMapCreator BIND_MAP_CREATOR = new FixedKeyMapCreator("destination", "bindingKey", "arguments", "replaceExistingArguments");

    public boolean bind(final java.lang.String destination, final java.lang.String bindingKey, final java.util.Map<java.lang.String,java.lang.Object> arguments, final boolean replaceExistingArguments)
    {
        authorise(INVOKE_METHOD("bind"), BIND_MAP_CREATOR.createMap(destination, bindingKey, arguments, replaceExistingArguments));

        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.Boolean>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.Boolean> execute()
                {
                    return Futures.<java.lang.Boolean>immediateFuture(HeadersExchangeImplWithAccessChecking.super.bind(destination, bindingKey, arguments, replaceExistingArguments));
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "bind";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "destination=" + destination + "," + "bindingKey=" + bindingKey + "," + "arguments=" + arguments + "," + "replaceExistingArguments=" + replaceExistingArguments;
                    }
                    return _args;
                }
            }));

    }

    private static final FixedKeyMapCreator UNBIND_MAP_CREATOR = new FixedKeyMapCreator("destination", "bindingKey");

    public boolean unbind(final java.lang.String destination, final java.lang.String bindingKey)
    {
        authorise(INVOKE_METHOD("unbind"), UNBIND_MAP_CREATOR.createMap(destination, bindingKey));

        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.Boolean>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.Boolean> execute()
                {
                    return Futures.<java.lang.Boolean>immediateFuture(HeadersExchangeImplWithAccessChecking.super.unbind(destination, bindingKey));
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "unbind";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "destination=" + destination + "," + "bindingKey=" + bindingKey;
                    }
                    return _args;
                }
            }));

    }

    @Override
    public boolean addBinding(final java.lang.String bindingKey, final org.apache.qpid.server.model.Queue<?> queue, final java.util.Map<java.lang.String,java.lang.Object> arguments)
    throws org.apache.qpid.server.filter.AMQInvalidArgumentException
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.Boolean>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.Boolean> execute()
                {
                    try
                    {
                    return Futures.<java.lang.Boolean>immediateFuture(HeadersExchangeImplWithAccessChecking.super.addBinding(bindingKey, queue, arguments));
                    }
                    catch (org.apache.qpid.server.filter.AMQInvalidArgumentException e)
                    {
                        return Futures.immediateFailedFuture(e);
                    }
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "addBinding";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "bindingKey=" + bindingKey + "," + "queue=" + queue + "," + "arguments=" + arguments;
                    }
                    return _args;
                }
            }));

    }

    @Override
    public boolean deleteBinding(final java.lang.String bindingKey, final org.apache.qpid.server.model.Queue<?> queue)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.Boolean>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.Boolean> execute()
                {
                    return Futures.<java.lang.Boolean>immediateFuture(HeadersExchangeImplWithAccessChecking.super.deleteBinding(bindingKey, queue));
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "deleteBinding";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "bindingKey=" + bindingKey + "," + "queue=" + queue;
                    }
                    return _args;
                }
            }));

    }

    @Override
    public boolean hasBinding(final java.lang.String bindingKey, final org.apache.qpid.server.model.Queue<?> queue)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.Boolean>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.Boolean> execute()
                {
                    return Futures.<java.lang.Boolean>immediateFuture(HeadersExchangeImplWithAccessChecking.super.hasBinding(bindingKey, queue));
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "hasBinding";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "bindingKey=" + bindingKey + "," + "queue=" + queue;
                    }
                    return _args;
                }
            }));

    }

    @Override
    public void replaceBinding(final java.lang.String bindingKey, final org.apache.qpid.server.model.Queue<?> queue, final java.util.Map<java.lang.String,java.lang.Object> arguments)
    throws org.apache.qpid.server.filter.AMQInvalidArgumentException
    {
        doSync(doOnConfigThread(new Task<ListenableFuture<Void>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<Void> execute()
                {
                    try
                    {
                    HeadersExchangeImplWithAccessChecking.super.replaceBinding(bindingKey, queue, arguments);
                    return Futures.<Void>immediateFuture(null);
                    }
                    catch (org.apache.qpid.server.filter.AMQInvalidArgumentException e)
                    {
                        return Futures.immediateFailedFuture(e);
                    }
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "replaceBinding";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "bindingKey=" + bindingKey + "," + "queue=" + queue + "," + "arguments=" + arguments;
                    }
                    return _args;
                }
            }));

    }

    @Override
    public boolean isBound(final java.lang.String bindingKey, final java.util.Map<java.lang.String,java.lang.Object> arguments, final org.apache.qpid.server.model.Queue<?> queue)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.Boolean>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.Boolean> execute()
                {
                    return Futures.<java.lang.Boolean>immediateFuture(HeadersExchangeImplWithAccessChecking.super.isBound(bindingKey, arguments, queue));
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "isBound";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "bindingKey=" + bindingKey + "," + "arguments=" + arguments + "," + "queue=" + queue;
                    }
                    return _args;
                }
            }));

    }

    @Override
    public boolean hasBindings()
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.Boolean>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.Boolean> execute()
                {
                    return Futures.<java.lang.Boolean>immediateFuture(HeadersExchangeImplWithAccessChecking.super.hasBindings());
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "hasBindings";
                }
                @Override
                public String getArguments()
                {
                    return _args;
                }
            }));

    }

    @Override
    public void destinationRemoved(final org.apache.qpid.server.message.MessageDestination destination)
    {
        doSync(doOnConfigThread(new Task<ListenableFuture<Void>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<Void> execute()
                {
                    HeadersExchangeImplWithAccessChecking.super.destinationRemoved(destination);
                    return Futures.<Void>immediateFuture(null);
                }
                @Override
                public String getObject()
                {
                    return HeadersExchangeImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "destinationRemoved";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "destination=" + destination;
                    }
                    return _args;
                }
            }));

    }

}
