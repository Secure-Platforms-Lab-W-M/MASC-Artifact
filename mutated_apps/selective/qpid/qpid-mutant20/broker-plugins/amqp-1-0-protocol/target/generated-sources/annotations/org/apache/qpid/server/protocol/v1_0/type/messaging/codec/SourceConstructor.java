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
import org.apache.qpid.server.protocol.v1_0.type.messaging.Source;

public final class SourceConstructor extends AbstractCompositeTypeConstructor<Source>
{
    private static final SourceConstructor INSTANCE = new SourceConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:source:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000028), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Source.class.getSimpleName();
    }

    @Override
    protected Source construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Source obj = new Source();

        java.lang.String address = fieldValueReader.readValue(0, "address", false, java.lang.String.class);
        if (address != null)
        {
            obj.setAddress(address);
        }

        Object durable = fieldValueReader.readValue(1, "durable", false, Object.class);
        if (durable != null)
        {
            try
            {
                obj.setDurable(org.apache.qpid.server.protocol.v1_0.type.messaging.TerminusDurability.valueOf(durable));
            }
            catch (RuntimeException e)
            {
                Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'durable' of 'Source'");
                throw new AmqpErrorException(error, e);
            }
        }

        Object expiryPolicy = fieldValueReader.readValue(2, "expiryPolicy", false, Object.class);
        if (expiryPolicy != null)
        {
            try
            {
                obj.setExpiryPolicy(org.apache.qpid.server.protocol.v1_0.type.messaging.TerminusExpiryPolicy.valueOf(expiryPolicy));
            }
            catch (RuntimeException e)
            {
                Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'expiryPolicy' of 'Source'");
                throw new AmqpErrorException(error, e);
            }
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger timeout = fieldValueReader.readValue(3, "timeout", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (timeout != null)
        {
            obj.setTimeout(timeout);
        }

        java.lang.Boolean dynamic = fieldValueReader.readValue(4, "dynamic", false, java.lang.Boolean.class);
        if (dynamic != null)
        {
            obj.setDynamic(dynamic);
        }

        Object dynamicNodeProperties = fieldValueReader.readValue(5, "dynamicNodeProperties", false, Object.class);
        if (dynamicNodeProperties != null)
        {
            try
            {
                obj.setDynamicNodeProperties(org.apache.qpid.server.protocol.v1_0.DeserializationFactories.convertToNodeProperties(dynamicNodeProperties));
            }
            catch (RuntimeException e)
            {
                Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'dynamicNodeProperties' of 'Source'");
                throw new AmqpErrorException(error, e);
            }
        }

        Object distributionMode = fieldValueReader.readValue(6, "distributionMode", false, Object.class);
        if (distributionMode != null)
        {
            try
            {
                obj.setDistributionMode(org.apache.qpid.server.protocol.v1_0.DeserializationFactories.convertToDistributionMode(distributionMode));
            }
            catch (RuntimeException e)
            {
                Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'distributionMode' of 'Source'");
                throw new AmqpErrorException(error, e);
            }
        }

        java.util.Map<org.apache.qpid.server.protocol.v1_0.type.Symbol,org.apache.qpid.server.protocol.v1_0.type.messaging.Filter> filter = fieldValueReader.readMapValue(7, "filter", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, org.apache.qpid.server.protocol.v1_0.type.messaging.Filter.class);
        if (filter != null)
        {
            obj.setFilter(filter);
        }

        org.apache.qpid.server.protocol.v1_0.type.Outcome defaultOutcome = fieldValueReader.readValue(8, "defaultOutcome", false, org.apache.qpid.server.protocol.v1_0.type.Outcome.class);
        if (defaultOutcome != null)
        {
            obj.setDefaultOutcome(defaultOutcome);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] outcomes = fieldValueReader.readArrayValue(9, "outcomes", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (outcomes != null)
        {
            obj.setOutcomes(outcomes);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] capabilities = fieldValueReader.readArrayValue(10, "capabilities", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        if (capabilities != null)
        {
            obj.setCapabilities(capabilities);
        }

        return obj;
    }
}
