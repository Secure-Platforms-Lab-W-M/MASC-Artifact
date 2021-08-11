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
package org.apache.qpid.server.store.berkeleydb.tuple;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import org.apache.qpid.server.model.ConfiguredObjectJacksonModule;
import org.apache.qpid.server.store.ConfiguredObjectRecord;
import org.apache.qpid.server.store.StoreException;
import org.apache.qpid.server.store.berkeleydb.BDBConfiguredObjectRecord;

public class ConfiguredObjectBinding extends TupleBinding<ConfiguredObjectRecord>
{
    private static final ConfiguredObjectBinding INSTANCE = new ConfiguredObjectBinding(null);

    private final UUID _uuid;

    public static ConfiguredObjectBinding getInstance()
    {
        return INSTANCE;
    }

    public ConfiguredObjectBinding(UUID uuid)
    {
        _uuid = uuid;
    }

    @Override
    public BDBConfiguredObjectRecord entryToObject(TupleInput tupleInput)
    {
        String type = tupleInput.readString();
        String json = tupleInput.readString();
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Map<String,Object> value = mapper.readValue(json, Map.class);
            BDBConfiguredObjectRecord configuredObject = new BDBConfiguredObjectRecord(_uuid, type, value);
            return configuredObject;
        }
        catch (IOException e)
        {
            //should never happen
            throw new StoreException(e);
        }

    }

    @Override
    public void objectToEntry(ConfiguredObjectRecord object, TupleOutput tupleOutput)
    {
        try
        {
            StringWriter writer = new StringWriter();
            final ObjectMapper objectMapper = ConfiguredObjectJacksonModule.newObjectMapper(true);
            objectMapper.writeValue(writer, object.getAttributes());
            tupleOutput.writeString(object.getType());
            tupleOutput.writeString(writer.toString());
        }
        catch (IOException e)
        {
            throw new StoreException(e);
        }
    }

}
