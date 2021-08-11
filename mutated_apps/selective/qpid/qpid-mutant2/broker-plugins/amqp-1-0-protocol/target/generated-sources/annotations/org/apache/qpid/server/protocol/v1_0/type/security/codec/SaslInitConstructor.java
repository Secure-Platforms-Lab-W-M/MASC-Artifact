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
import org.apache.qpid.server.protocol.v1_0.type.security.SaslInit;

public final class SaslInitConstructor extends AbstractCompositeTypeConstructor<SaslInit>
{
    private static final SaslInitConstructor INSTANCE = new SaslInitConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:sasl-init:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000041), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return SaslInit.class.getSimpleName();
    }

    @Override
    protected SaslInit construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        SaslInit obj = new SaslInit();

        org.apache.qpid.server.protocol.v1_0.type.Symbol mechanism = fieldValueReader.readValue(0, "mechanism", true, org.apache.qpid.server.protocol.v1_0.type.Symbol.class);
        obj.setMechanism(mechanism);

        org.apache.qpid.server.protocol.v1_0.type.Binary initialResponse = fieldValueReader.readValue(1, "initialResponse", false, org.apache.qpid.server.protocol.v1_0.type.Binary.class);
        if (initialResponse != null)
        {
            obj.setInitialResponse(initialResponse);
        }

        java.lang.String hostname = fieldValueReader.readValue(2, "hostname", false, java.lang.String.class);
        if (hostname != null)
        {
            obj.setHostname(hostname);
        }

        return obj;
    }
}
