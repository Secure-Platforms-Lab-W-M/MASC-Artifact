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
 *
 */
package org.apache.qpid.disttest.json;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.qpid.disttest.message.Command;

public class JsonHandler
{

    private final ObjectMapper _objectMapper = new ObjectMapperFactory().createObjectMapper();

    public <T extends Command> T unmarshall(final String json, final Class<T> clazz) throws IOException
    {
        return _objectMapper.readValue(json, clazz);
    }

    public <T extends Command> String marshall(final T command) throws IOException
    {
        return _objectMapper.writeValueAsString(command);
    }
}
