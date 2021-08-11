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
import org.apache.qpid.server.protocol.v1_0.type.messaging.DeleteOnNoLinks;

public final class DeleteOnNoLinksConstructor extends AbstractCompositeTypeConstructor<DeleteOnNoLinks>
{
    private static final DeleteOnNoLinksConstructor INSTANCE = new DeleteOnNoLinksConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:delete-on-no-links:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x0000000000002c), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return DeleteOnNoLinks.class.getSimpleName();
    }

    @Override
    protected DeleteOnNoLinks construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        DeleteOnNoLinks obj = new DeleteOnNoLinks();

        return obj;
    }
}
