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
import org.apache.qpid.server.protocol.v1_0.type.transport.Transfer;

public final class TransferConstructor extends AbstractCompositeTypeConstructor<Transfer>
{
    private static final TransferConstructor INSTANCE = new TransferConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:transfer:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000014), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Transfer.class.getSimpleName();
    }

    @Override
    protected Transfer construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Transfer obj = new Transfer();

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger handle = fieldValueReader.readValue(0, "handle", true, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        obj.setHandle(handle);

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger deliveryId = fieldValueReader.readValue(1, "deliveryId", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (deliveryId != null)
        {
            obj.setDeliveryId(deliveryId);
        }

        org.apache.qpid.server.protocol.v1_0.type.Binary deliveryTag = fieldValueReader.readValue(2, "deliveryTag", false, org.apache.qpid.server.protocol.v1_0.type.Binary.class);
        if (deliveryTag != null)
        {
            obj.setDeliveryTag(deliveryTag);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger messageFormat = fieldValueReader.readValue(3, "messageFormat", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (messageFormat != null)
        {
            obj.setMessageFormat(messageFormat);
        }

        java.lang.Boolean settled = fieldValueReader.readValue(4, "settled", false, java.lang.Boolean.class);
        if (settled != null)
        {
            obj.setSettled(settled);
        }

        java.lang.Boolean more = fieldValueReader.readValue(5, "more", false, java.lang.Boolean.class);
        if (more != null)
        {
            obj.setMore(more);
        }

        Object rcvSettleMode = fieldValueReader.readValue(6, "rcvSettleMode", false, Object.class);
        if (rcvSettleMode != null)
        {
            try
            {
                obj.setRcvSettleMode(org.apache.qpid.server.protocol.v1_0.type.transport.ReceiverSettleMode.valueOf(rcvSettleMode));
            }
            catch (RuntimeException e)
            {
                Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'rcvSettleMode' of 'Transfer'");
                throw new AmqpErrorException(error, e);
            }
        }

        org.apache.qpid.server.protocol.v1_0.type.DeliveryState state = fieldValueReader.readValue(7, "state", false, org.apache.qpid.server.protocol.v1_0.type.DeliveryState.class);
        if (state != null)
        {
            obj.setState(state);
        }

        java.lang.Boolean resume = fieldValueReader.readValue(8, "resume", false, java.lang.Boolean.class);
        if (resume != null)
        {
            obj.setResume(resume);
        }

        java.lang.Boolean aborted = fieldValueReader.readValue(9, "aborted", false, java.lang.Boolean.class);
        if (aborted != null)
        {
            obj.setAborted(aborted);
        }

        java.lang.Boolean batchable = fieldValueReader.readValue(10, "batchable", false, java.lang.Boolean.class);
        if (batchable != null)
        {
            obj.setBatchable(batchable);
        }

        return obj;
    }
}
