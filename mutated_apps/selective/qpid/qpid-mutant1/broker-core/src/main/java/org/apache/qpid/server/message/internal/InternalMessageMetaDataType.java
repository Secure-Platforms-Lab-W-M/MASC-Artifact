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
package org.apache.qpid.server.message.internal;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;
import org.apache.qpid.server.message.ServerMessage;
import org.apache.qpid.server.plugin.MessageMetaDataType;
import org.apache.qpid.server.plugin.PluggableService;
import org.apache.qpid.server.store.StoredMessage;
import org.apache.qpid.server.util.ConnectionScopedRuntimeException;

@PluggableService
public class InternalMessageMetaDataType implements MessageMetaDataType<InternalMessageMetaData>
{
    public static final int INTERNAL_ORDINAL = 255;
    public static final String TYPE = "INTERNAL";

    @Override
    public int ordinal()
    {
        return INTERNAL_ORDINAL;
    }

    @Override
    public InternalMessageMetaData createMetaData(final QpidByteBuffer buf)
    {
        try (ObjectInputStream is = new ObjectInputStream(buf.asInputStream()))
        {
            int contentSize = is.readInt();
            InternalMessageHeader header = (InternalMessageHeader) is.readObject();
            return new InternalMessageMetaData(true, header, contentSize);
        }
        catch (IOException e)
        {
            throw new ConnectionScopedRuntimeException("Unexpected IO Exception on operation in memory", e);
        }
        catch (ClassNotFoundException e)
        {
            throw new ConnectionScopedRuntimeException("Unexpected exception when reading meta data, check classpath",e);
        }

    }

    @Override
    public ServerMessage<InternalMessageMetaData> createMessage(final StoredMessage<InternalMessageMetaData> msg)
    {
        return new InternalMessage(msg, null);
    }

    @Override
    public String getType()
    {
        return TYPE;
    }

    public static final InternalMessageMetaDataType INSTANCE = new InternalMessageMetaDataType();
}
