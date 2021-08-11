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

package org.apache.qpid.server.model.testmodels.hierarchy;

import static org.apache.qpid.server.security.access.Operation.INVOKE_METHOD;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.qpid.server.configuration.updater.Task;
import org.apache.qpid.server.util.FixedKeyMapCreator;

final class TestKitCarImplWithAccessChecking extends TestKitCarImpl
{
    TestKitCarImplWithAccessChecking(final Map<String, Object> attributes)
    {
        super(attributes);
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
                    return Futures.<java.lang.String>immediateFuture(TestKitCarImplWithAccessChecking.super.setContextVariable(name, value));
                }
                @Override
                public String getObject()
                {
                    return TestKitCarImplWithAccessChecking.this.toString();
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
                    return Futures.<java.lang.String>immediateFuture(TestKitCarImplWithAccessChecking.super.removeContextVariable(name));
                }
                @Override
                public String getObject()
                {
                    return TestKitCarImplWithAccessChecking.this.toString();
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

    private static final FixedKeyMapCreator START_ENGINE_MAP_CREATOR = new FixedKeyMapCreator("keyCode");

    public void startEngine(final java.lang.String keyCode)
    {
        authorise(INVOKE_METHOD("startEngine"), START_ENGINE_MAP_CREATOR.createMap(keyCode));

        super.startEngine(keyCode);
    }

    private static final FixedKeyMapCreator OPEN_DOOR_MAP_CREATOR = new FixedKeyMapCreator("door");

    public org.apache.qpid.server.model.testmodels.hierarchy.TestCar.Door openDoor(final org.apache.qpid.server.model.testmodels.hierarchy.TestCar.Door door)
    {
        authorise(INVOKE_METHOD("openDoor"), OPEN_DOOR_MAP_CREATOR.createMap(door));

        return super.openDoor(door);
    }

}
