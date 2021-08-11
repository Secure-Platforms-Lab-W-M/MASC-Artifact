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

package org.apache.qpid.server.protocol.v1_0.type.security.codec;

import java.util.List;

import org.apache.qpid.server.protocol.v1_0.codec.AbstractCompositeTypeConstructor;
import org.apache.qpid.server.protocol.v1_0.codec.DescribedTypeConstructorRegistry;
import org.apache.qpid.server.protocol.v1_0.type.AmqpErrorException;
import org.apache.qpid.server.protocol.v1_0.type.Symbol;
import org.apache.qpid.server.protocol.v1_0.type.UnsignedLong;
import org.apache.qpid.server.protocol.v1_0.type.transport.AmqpError;
import org.apache.qpid.server.protocol.v1_0.type.transport.Error;
import org.apache.qpid.server.protocol.v1_0.type.security.SaslMechanisms;

public final class SaslMechanismsConstructor extends AbstractCompositeTypeConstructor<SaslMechanisms>
{
    private static final SaslMechanismsConstructor INSTANCE = new SaslMechanismsConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:sasl-mechanisms:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000040), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return SaslMechanisms.class.getSimpleName();
    }

    @Override
    protected SaslMechanisms construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        SaslMechanisms obj = new SaslMechanisms();

        org.apache.qpid.server.protocol.v1_0.type.Symbol[] saslServerMechanisms = fieldValueReader.readArrayValue(0, "saslServerMechanisms", true, org.apache.qpid.server.protocol.v1_0.type.Symbol.class, x -> (org.apache.qpid.server.protocol.v1_0.type.Symbol) x);
        obj.setSaslServerMechanisms(saslServerMechanisms);

        return obj;
    }
}
