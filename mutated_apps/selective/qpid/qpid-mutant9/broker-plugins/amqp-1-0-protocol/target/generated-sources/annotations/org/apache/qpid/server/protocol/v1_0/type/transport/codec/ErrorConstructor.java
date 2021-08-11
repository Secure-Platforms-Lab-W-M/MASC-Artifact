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
import org.apache.qpid.server.protocol.v1_0.type.transport.Error;

public final class ErrorConstructor extends AbstractCompositeTypeConstructor<Error>
{
    private static final ErrorConstructor INSTANCE = new ErrorConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:error:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x0000000000001d), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Error.class.getSimpleName();
    }

    @Override
    protected Error construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Error obj = new Error();

        Object condition = fieldValueReader.readValue(0, "condition", true, Object.class);
        try
        {
            obj.setCondition(org.apache.qpid.server.protocol.v1_0.DeserializationFactories.convertToErrorCondition(condition));
        }
        catch (RuntimeException e)
        {
            Error error = new Error(AmqpError.DECODE_ERROR, "Could not decode value field 'condition' of 'Error'");
            throw new AmqpErrorException(error, e);
        }

        java.lang.String description = fieldValueReader.readValue(1, "description", false, java.lang.String.class);
        if (description != null)
        {
            obj.setDescription(description);
        }

        java.util.Map<org.apache.qpid.server.protocol.v1_0.type.Symbol,java.lang.Object> info = fieldValueReader.readMapValue(2, "info", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, java.lang.Object.class);
        if (info != null)
        {
            obj.setInfo(info);
        }

        return obj;
    }
}
