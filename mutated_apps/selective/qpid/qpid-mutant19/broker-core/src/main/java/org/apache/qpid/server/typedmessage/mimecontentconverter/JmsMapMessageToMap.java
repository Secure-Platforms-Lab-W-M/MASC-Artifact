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
package org.apache.qpid.server.typedmessage.mimecontentconverter;

import java.io.EOFException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.qpid.server.message.mimecontentconverter.MimeContentToObjectConverter;
import org.apache.qpid.server.plugin.PluggableService;
import org.apache.qpid.server.typedmessage.TypedBytesContentReader;
import org.apache.qpid.server.typedmessage.TypedBytesFormatException;

@PluggableService
public class JmsMapMessageToMap implements MimeContentToObjectConverter<Map>
{
    @Override
    public String getType()
    {
        return getMimeType();
    }

    @Override
    public Class<Map> getObjectClass()
    {
        return Map.class;
    }

    @Override
    public String getMimeType()
    {
        return "jms/map-message";
    }

    @Override
    public Map toObject(final byte[] data)
    {
        if (data == null || data.length == 0)
        {
            return Collections.emptyMap();
        }

        TypedBytesContentReader reader = new TypedBytesContentReader(ByteBuffer.wrap(data));

        LinkedHashMap map = new LinkedHashMap();
        final int entries = reader.readIntImpl();
        for (int i = 0; i < entries; i++)
        {
            try
            {
                String propName = reader.readStringImpl();
                Object value = reader.readObject();

                map.put(propName, value);
            }
            catch (EOFException | TypedBytesFormatException e)
            {
                throw new IllegalArgumentException(e);
            }
        }

        return map;
    }
}
