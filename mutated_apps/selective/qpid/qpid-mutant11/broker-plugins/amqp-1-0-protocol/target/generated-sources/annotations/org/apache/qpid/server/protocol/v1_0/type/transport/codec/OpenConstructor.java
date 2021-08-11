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
import org.apache.qpid.server.protocol.v1_0.type.transport.Open;

public final class OpenConstructor extends AbstractCompositeTypeConstructor<Open>
{
    private static final OpenConstructor INSTANCE = new OpenConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:open:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000010), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Open.class.getSimpleName();
    }

    @Override
    protected Open construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Open obj = new Open();

        java.lang.String containerId = fieldValueReader.readValue(0, "containerId", true, java.lang.String.class);
        obj.setContainerId(containerId);

        java.lang.String hostname = fieldValueReader.readValue(1, "hostname", false, java.lang.String.class);
        if (hostname != null)
        {
            obj.setHostname(hostname);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger maxFrameSize = fieldValueReader.readValue(2, "maxFrameSize", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (maxFrameSize != null)
        {
            obj.setMaxFrameSize(maxFrameSize);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedShort channelMax = fieldValueReader.readValue(3, "channelMax", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedShort.class);
        if (channelMax != null)
        {
            obj.setChannelMax(channelMax);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger idleTimeOut = fieldValueReader.readValue(4, "idleTimeOut", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (idleTimeOut != null)
        {
            obj.setIdleTimeOut(idleTimeOut);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] outgoingLocales = fieldValueReader.readArrayValue(5, "outgoingLocales", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (outgoingLocales != null)
        {
            obj.setOutgoingLocales(outgoingLocales);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] incomingLocales = fieldValueReader.readArrayValue(6, "incomingLocales", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (incomingLocales != null)
        {
            obj.setIncomingLocales(incomingLocales);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] offeredCapabilities = fieldValueReader.readArrayValue(7, "offeredCapabilities", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (offeredCapabilities != null)
        {
            obj.setOfferedCapabilities(offeredCapabilities);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] desiredCapabilities = fieldValueReader.readArrayValue(8, "desiredCapabilities", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (desiredCapabilities != null)
        {
            obj.setDesiredCapabilities(desiredCapabilities);
        }

        java.util.Map<org.apache.qpid.server.protocol.v1_0.type.Symbol,java.lang.Object> properties = fieldValueReader.readMapValue(9, "properties", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, java.lang.Object.class);
        if (properties != null)
        {
            obj.setProperties(properties);
        }

        return obj;
    }
}
