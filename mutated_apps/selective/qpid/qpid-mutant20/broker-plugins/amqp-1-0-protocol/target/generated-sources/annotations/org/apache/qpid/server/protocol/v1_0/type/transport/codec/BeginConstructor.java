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

package org.apache.qpid.server.protocol.v1_0.type.transport.codec;

import java.util.List;

import org.apache.qpid.server.protocol.v1_0.codec.AbstractCompositeTypeConstructor;
import org.apache.qpid.server.protocol.v1_0.codec.DescribedTypeConstructorRegistry;
import org.apache.qpid.server.protocol.v1_0.type.AmqpErrorException;
import org.apache.qpid.server.protocol.v1_0.type.Symbol;
import org.apache.qpid.server.protocol.v1_0.type.UnsignedLong;
import org.apache.qpid.server.protocol.v1_0.type.transport.AmqpError;
import org.apache.qpid.server.protocol.v1_0.type.transport.Error;
import org.apache.qpid.server.protocol.v1_0.type.transport.Begin;

public final class BeginConstructor extends AbstractCompositeTypeConstructor<Begin>
{
    private static final BeginConstructor INSTANCE = new BeginConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:begin:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000011), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Begin.class.getSimpleName();
    }

    @Override
    protected Begin construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Begin obj = new Begin();

        org.apache.qpid.server.protocol.v1_0.type.UnsignedShort remoteChannel = fieldValueReader.readValue(0, "remoteChannel", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedShort.class);
        if (remoteChannel != null)
        {
            obj.setRemoteChannel(remoteChannel);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger nextOutgoingId = fieldValueReader.readValue(1, "nextOutgoingId", true, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        obj.setNextOutgoingId(nextOutgoingId);

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger incomingWindow = fieldValueReader.readValue(2, "incomingWindow", true, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        obj.setIncomingWindow(incomingWindow);

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger outgoingWindow = fieldValueReader.readValue(3, "outgoingWindow", true, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        obj.setOutgoingWindow(outgoingWindow);

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger handleMax = fieldValueReader.readValue(4, "handleMax", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (handleMax != null)
        {
            obj.setHandleMax(handleMax);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] offeredCapabilities = fieldValueReader.readArrayValue(5, "offeredCapabilities", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (offeredCapabilities != null)
        {
            obj.setOfferedCapabilities(offeredCapabilities);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] desiredCapabilities = fieldValueReader.readArrayValue(6, "desiredCapabilities", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (desiredCapabilities != null)
        {
            obj.setDesiredCapabilities(desiredCapabilities);
        }

        java.util.Map<org.apache.qpid.server.protocol.v1_0.type.Symbol,java.lang.Object> properties = fieldValueReader.readMapValue(7, "properties", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, java.lang.Object.class);
        if (properties != null)
        {
            obj.setProperties(properties);
        }

        return obj;
    }
}
