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
import org.apache.qpid.server.protocol.v1_0.type.transport.Flow;

public final class FlowConstructor extends AbstractCompositeTypeConstructor<Flow>
{
    private static final FlowConstructor INSTANCE = new FlowConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:flow:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000013), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Flow.class.getSimpleName();
    }

    @Override
    protected Flow construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Flow obj = new Flow();

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger nextIncomingId = fieldValueReader.readValue(0, "nextIncomingId", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (nextIncomingId != null)
        {
            obj.setNextIncomingId(nextIncomingId);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger incomingWindow = fieldValueReader.readValue(1, "incomingWindow", true, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        obj.setIncomingWindow(incomingWindow);

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger nextOutgoingId = fieldValueReader.readValue(2, "nextOutgoingId", true, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        obj.setNextOutgoingId(nextOutgoingId);

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger outgoingWindow = fieldValueReader.readValue(3, "outgoingWindow", true, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        obj.setOutgoingWindow(outgoingWindow);

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger handle = fieldValueReader.readValue(4, "handle", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (handle != null)
        {
            obj.setHandle(handle);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger deliveryCount = fieldValueReader.readValue(5, "deliveryCount", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (deliveryCount != null)
        {
            obj.setDeliveryCount(deliveryCount);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger linkCredit = fieldValueReader.readValue(6, "linkCredit", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (linkCredit != null)
        {
            obj.setLinkCredit(linkCredit);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger available = fieldValueReader.readValue(7, "available", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (available != null)
        {
            obj.setAvailable(available);
        }

        java.lang.Boolean drain = fieldValueReader.readValue(8, "drain", false, java.lang.Boolean.class);
        if (drain != null)
        {
            obj.setDrain(drain);
        }

        java.lang.Boolean echo = fieldValueReader.readValue(9, "echo", false, java.lang.Boolean.class);
        if (echo != null)
        {
            obj.setEcho(echo);
        }

        java.util.Map<org.apache.qpid.server.protocol.v1_0.type.Symbol,java.lang.Object> properties = fieldValueReader.readMapValue(10, "properties", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, java.lang.Object.class);
        if (properties != null)
        {
            obj.setProperties(properties);
        }

        return obj;
    }
}
