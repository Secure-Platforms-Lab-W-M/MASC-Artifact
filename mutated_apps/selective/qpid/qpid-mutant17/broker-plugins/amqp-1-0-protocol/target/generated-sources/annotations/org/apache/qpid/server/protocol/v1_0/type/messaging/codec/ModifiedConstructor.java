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
import org.apache.qpid.server.protocol.v1_0.type.messaging.Modified;

public final class ModifiedConstructor extends AbstractCompositeTypeConstructor<Modified>
{
    private static final ModifiedConstructor INSTANCE = new ModifiedConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:modified:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000027), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Modified.class.getSimpleName();
    }

    @Override
    protected Modified construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Modified obj = new Modified();

        java.lang.Boolean deliveryFailed = fieldValueReader.readValue(0, "deliveryFailed", false, java.lang.Boolean.class);
        if (deliveryFailed != null)
        {
            obj.setDeliveryFailed(deliveryFailed);
        }

        java.lang.Boolean undeliverableHere = fieldValueReader.readValue(1, "undeliverableHere", false, java.lang.Boolean.class);
        if (undeliverableHere != null)
        {
            obj.setUndeliverableHere(undeliverableHere);
        }

        java.util.Map<org.apache.qpid.server.protocol.v1_0.type.Symbol,java.lang.Object> messageAnnotations = fieldValueReader.readMapValue(2, "messageAnnotations", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, java.lang.Object.class);
        if (messageAnnotations != null)
        {
            obj.setMessageAnnotations(messageAnnotations);
        }

        return obj;
    }
}
