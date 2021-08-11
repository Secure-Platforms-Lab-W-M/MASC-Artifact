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

package org.apache.qpid.server.protocol.v1_0.type.messaging.codec;

import java.util.List;

import org.apache.qpid.server.protocol.v1_0.codec.AbstractCompositeTypeConstructor;
import org.apache.qpid.server.protocol.v1_0.codec.DescribedTypeConstructorRegistry;
import org.apache.qpid.server.protocol.v1_0.type.AmqpErrorException;
import org.apache.qpid.server.protocol.v1_0.type.Symbol;
import org.apache.qpid.server.protocol.v1_0.type.UnsignedLong;
import org.apache.qpid.server.protocol.v1_0.type.transport.AmqpError;
import org.apache.qpid.server.protocol.v1_0.type.transport.Error;
import org.apache.qpid.server.protocol.v1_0.type.messaging.Header;

public final class HeaderConstructor extends AbstractCompositeTypeConstructor<Header>
{
    private static final HeaderConstructor INSTANCE = new HeaderConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:header:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000070), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Header.class.getSimpleName();
    }

    @Override
    protected Header construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Header obj = new Header();

        java.lang.Boolean durable = fieldValueReader.readValue(0, "durable", false, java.lang.Boolean.class);
        if (durable != null)
        {
            obj.setDurable(durable);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedByte priority = fieldValueReader.readValue(1, "priority", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedByte.class);
        if (priority != null)
        {
            obj.setPriority(priority);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger ttl = fieldValueReader.readValue(2, "ttl", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (ttl != null)
        {
            obj.setTtl(ttl);
        }

        java.lang.Boolean firstAcquirer = fieldValueReader.readValue(3, "firstAcquirer", false, java.lang.Boolean.class);
        if (firstAcquirer != null)
        {
            obj.setFirstAcquirer(firstAcquirer);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger deliveryCount = fieldValueReader.readValue(4, "deliveryCount", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (deliveryCount != null)
        {
            obj.setDeliveryCount(deliveryCount);
        }

        return obj;
    }
}
