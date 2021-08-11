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
package org.apache.qpid.server.message.mimecontentconverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.qpid.server.plugin.PluggableService;

@PluggableService
public class SerializableToJavaObjectStream implements ObjectToMimeContentConverter<Serializable>
{
    @Override
    public String getType()
    {
        return getMimeType();
    }

    @Override
    public String getMimeType()
    {
        return "application/java-object-stream";
    }

    @Override
    public Class<Serializable> getObjectClass()
    {
        return Serializable.class;
    }

    @Override
    public int getRank()
    {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean isAcceptable(final Serializable object)
    {
        return true;
    }

    @Override
    public byte[] toMimeContent(final Serializable object)
    {
        try(ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bytesOut))
        {
            os.writeObject(object);
            return bytesOut.toByteArray();
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}
