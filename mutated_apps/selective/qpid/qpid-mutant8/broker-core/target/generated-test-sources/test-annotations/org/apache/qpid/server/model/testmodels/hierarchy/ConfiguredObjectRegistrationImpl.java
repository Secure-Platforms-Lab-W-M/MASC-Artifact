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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.plugin.ConfiguredObjectRegistration;
import org.apache.qpid.server.plugin.PluggableService;

@PluggableService
public class ConfiguredObjectRegistrationImpl implements ConfiguredObjectRegistration
{
    private final Set<Class<? extends ConfiguredObject>> _implementations;

    public ConfiguredObjectRegistrationImpl()
    {
        Set<Class<? extends ConfiguredObject>> implementations = new HashSet<>();
        if(!Boolean.getBoolean("qpid.type.disabled:testcar.testpertrolcar"))
        {
             implementations.add(TestStandardCarImpl.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:testcar.testkitcar"))
        {
             implementations.add(TestKitCarImpl.class);
        }
        implementations.add(TestStandardCar.class);
        implementations.add(TestInstrumentPanel.class);
        implementations.add(TestGauge.class);
        if(!Boolean.getBoolean("qpid.type.disabled:testgauge.temperature"))
        {
             implementations.add(TestTemperatureGaugeImpl.class);
        }
        implementations.add(TestPetrolEngine.class);
        implementations.add(TestHybridEngine.class);
        implementations.add(TestCar.class);
        if(!Boolean.getBoolean("qpid.type.disabled:testengine.PETROL"))
        {
             implementations.add(TestPetrolEngineImpl.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:testengine.HYBRID"))
        {
             implementations.add(TestHybridEngineImpl.class);
        }
        implementations.add(TestSensor.class);
        if(!Boolean.getBoolean("qpid.type.disabled:testengine.ELEC"))
        {
             implementations.add(TestElecEngineImpl.class);
        }
        implementations.add(TestElecEngine.class);
        if(!Boolean.getBoolean("qpid.type.disabled:testinstrumentpanel.digital"))
        {
             implementations.add(TestDigitalInstrumentPanelImpl.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:testsensor.temperature"))
        {
             implementations.add(TestTemperatureSensorImpl.class);
        }
        implementations.add(TestKitCar.class);
        implementations.add(TestEngine.class);
        implementations.add(TestElecCar.class);
        _implementations = Collections.unmodifiableSet(implementations);
    }

    public String getType()
    {
        return "org.apache.qpid.server.model.testmodels.hierarchy";
    }

    public Set<Class<? extends ConfiguredObject>> getConfiguredObjectClasses()
    {
        return _implementations;
    }

}
