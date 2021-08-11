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
import org.apache.qpid.server.protocol.v1_0.type.transport.Attach;

public final class AttachConstructor extends AbstractCompositeTypeConstructor<Attach>
{
    private static final AttachConstructor INSTANCE = new AttachConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:attach:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000012), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Attach.class.getSimpleName();
    }

    @Override
    protected Attach construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Attach obj = new Attach();

        java.lang.String name = fieldValueReader.readValue(0, "name", true, java.lang.String.class);
        obj.setName(name);

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger handle = fieldValueReader.readValue(1, "handle", true, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        obj.setHandle(handle);

        Object role = fieldValueReader.readValue(2, "role", true, Object.class);
        try
        {
            obj.setRole(org.apache.qpid.server.protocol.v1_0.type.transport.Role.valueOf(role));
        }
        catch (RuntimeException e)
        {
            Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'role' of 'Attach'");
            throw new AmqpErrorException(error, e);
        }

        Object sndSettleMode = fieldValueReader.readValue(3, "sndSettleMode", false, Object.class);
        if (sndSettleMode != null)
        {
            try
            {
                obj.setSndSettleMode(org.apache.qpid.server.protocol.v1_0.type.transport.SenderSettleMode.valueOf(sndSettleMode));
            }
            catch (RuntimeException e)
            {
                Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'sndSettleMode' of 'Attach'");
                throw new AmqpErrorException(error, e);
            }
        }

        Object rcvSettleMode = fieldValueReader.readValue(4, "rcvSettleMode", false, Object.class);
        if (rcvSettleMode != null)
        {
            try
            {
                obj.setRcvSettleMode(org.apache.qpid.server.protocol.v1_0.type.transport.ReceiverSettleMode.valueOf(rcvSettleMode));
            }
            catch (RuntimeException e)
            {
                Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'rcvSettleMode' of 'Attach'");
                throw new AmqpErrorException(error, e);
            }
        }

        org.apache.qpid.server.protocol.v1_0.type.BaseSource source = fieldValueReader.readValue(5, "source", false, org.apache.qpid.server.protocol.v1_0.type.BaseSource.class);
        if (source != null)
        {
            obj.setSource(source);
        }

        org.apache.qpid.server.protocol.v1_0.type.BaseTarget target = fieldValueReader.readValue(6, "target", false, org.apache.qpid.server.protocol.v1_0.type.BaseTarget.class);
        if (target != null)
        {
            obj.setTarget(target);
        }

        java.util.Map<org.apache.qpid.server.protocol.v1_0.type.Binary,org.apache.qpid.server.protocol.v1_0.type.DeliveryState> unsettled = fieldValueReader.readMapValue(7, "unsettled", false, org.apache.qpid.server.protocol.v1_0.type.Binary.class, org.apache.qpid.server.protocol.v1_0.type.DeliveryState.class);
        if (unsettled != null)
        {
            obj.setUnsettled(unsettled);
        }

        java.lang.Boolean incompleteUnsettled = fieldValueReader.readValue(8, "incompleteUnsettled", false, java.lang.Boolean.class);
        if (incompleteUnsettled != null)
        {
            obj.setIncompleteUnsettled(incompleteUnsettled);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger initialDeliveryCount = fieldValueReader.readValue(9, "initialDeliveryCount", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (initialDeliveryCount != null)
        {
            obj.setInitialDeliveryCount(initialDeliveryCount);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedLong maxMessageSize = fieldValueReader.readValue(10, "maxMessageSize", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedLong.class);
        if (maxMessageSize != null)
        {
            obj.setMaxMessageSize(maxMessageSize);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] offeredCapabilities = fieldValueReader.readArrayValue(11, "offeredCapabilities", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (offeredCapabilities != null)
        {
            obj.setOfferedCapabilities(offeredCapabilities);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] desiredCapabilities = fieldValueReader.readArrayValue(12, "desiredCapabilities", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (desiredCapabilities != null)
        {
            obj.setDesiredCapabilities(desiredCapabilities);
        }

        java.util.Map<org.apache.qpid.server.protocol.v1_0.type.Symbol,java.lang.Object> properties = fieldValueReader.readMapValue(13, "properties", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, java.lang.Object.class);
        if (properties != null)
        {
            obj.setProperties(properties);
        }

        return obj;
    }
}
