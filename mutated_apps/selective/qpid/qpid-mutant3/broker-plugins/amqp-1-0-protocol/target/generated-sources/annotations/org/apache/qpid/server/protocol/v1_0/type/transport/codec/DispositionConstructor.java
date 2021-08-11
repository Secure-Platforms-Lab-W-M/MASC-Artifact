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
import org.apache.qpid.server.protocol.v1_0.type.transport.Disposition;

public final class DispositionConstructor extends AbstractCompositeTypeConstructor<Disposition>
{
    private static final DispositionConstructor INSTANCE = new DispositionConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:disposition:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000015), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Disposition.class.getSimpleName();
    }

    @Override
    protected Disposition construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Disposition obj = new Disposition();

        Object role = fieldValueReader.readValue(0, "role", true, Object.class);
        try
        {
            obj.setRole(org.apache.qpid.server.protocol.v1_0.type.transport.Role.valueOf(role));
        }
        catch (RuntimeException e)
        {
            Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'role' of 'Disposition'");
            throw new AmqpErrorException(error, e);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger first = fieldValueReader.readValue(1, "first", true, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        obj.setFirst(first);

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger last = fieldValueReader.readValue(2, "last", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (last != null)
        {
            obj.setLast(last);
        }

        java.lang.Boolean settled = fieldValueReader.readValue(3, "settled", false, java.lang.Boolean.class);
        if (settled != null)
        {
            obj.setSettled(settled);
        }

        org.apache.qpid.server.protocol.v1_0.type.DeliveryState state = fieldValueReader.readValue(4, "state", false, org.apache.qpid.server.protocol.v1_0.type.DeliveryState.class);
        if (state != null)
        {
            obj.setState(state);
        }

        java.lang.Boolean batchable = fieldValueReader.readValue(5, "batchable", false, java.lang.Boolean.class);
        if (batchable != null)
        {
            obj.setBatchable(batchable);
        }

        return obj;
    }
}
