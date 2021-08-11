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
import org.apache.qpid.server.protocol.v1_0.type.messaging.Properties;

public final class PropertiesConstructor extends AbstractCompositeTypeConstructor<Properties>
{
    private static final PropertiesConstructor INSTANCE = new PropertiesConstructor();

    public static void register(DescribedTypeConstructorRegistry registry)
    {
        registry.register(Symbol.valueOf("amqp:properties:list"), INSTANCE);
        registry.register(UnsignedLong.valueOf(0x00000000000073), INSTANCE);
    }

    @Override
    protected String getTypeName()
    {
        return Properties.class.getSimpleName();
    }

    @Override
    protected Properties construct(final FieldValueReader fieldValueReader) throws AmqpErrorException
    {
        Properties obj = new Properties();

        java.lang.Object messageId = fieldValueReader.readValue(0, "messageId", false, java.lang.Object.class);
        if (messageId != null)
        {
            obj.setMessageId(messageId);
        }

        org.apache.qpid.server.protocol.v1_0.type.Binary userId = fieldValueReader.readValue(1, "userId", false, org.apache.qpid.server.protocol.v1_0.type.Binary.class);
        if (userId != null)
        {
            obj.setUserId(userId);
        }

        java.lang.String to = fieldValueReader.readValue(2, "to", false, java.lang.String.class);
        if (to != null)
        {
            obj.setTo(to);
        }

        java.lang.String subject = fieldValueReader.readValue(3, "subject", false, java.lang.String.class);
        if (subject != null)
        {
            obj.setSubject(subject);
        }

        java.lang.String replyTo = fieldValueReader.readValue(4, "replyTo", false, java.lang.String.class);
        if (replyTo != null)
        {
            obj.setReplyTo(replyTo);
        }

        java.lang.Object correlationId = fieldValueReader.readValue(5, "correlationId", false, java.lang.Object.class);
        if (correlationId != null)
        {
            obj.setCorrelationId(correlationId);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol contentType = fieldValueReader.readValue(6, "contentType", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class);
        if (contentType != null)
        {
            obj.setContentType(contentType);
        }

        org.apache.qpid.server.protocol.v1_0.type.Symbol contentEncoding = fieldValueReader.readValue(7, "contentEncoding", false, org.apache.qpid.server.protocol.v1_0.type.Symbol.class);
        if (contentEncoding != null)
        {
            obj.setContentEncoding(contentEncoding);
        }

        java.util.Date absoluteExpiryTime = fieldValueReader.readValue(8, "absoluteExpiryTime", false, java.util.Date.class);
        if (absoluteExpiryTime != null)
        {
            obj.setAbsoluteExpiryTime(absoluteExpiryTime);
        }

        java.util.Date creationTime = fieldValueReader.readValue(9, "creationTime", false, java.util.Date.class);
        if (creationTime != null)
        {
            obj.setCreationTime(creationTime);
        }

        java.lang.String groupId = fieldValueReader.readValue(10, "groupId", false, java.lang.String.class);
        if (groupId != null)
        {
            obj.setGroupId(groupId);
        }

        org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger groupSequence = fieldValueReader.readValue(11, "groupSequence", false, org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger.class);
        if (groupSequence != null)
        {
            obj.setGroupSequence(groupSequence);
        }

        java.lang.String replyToGroupId = fieldValueReader.readValue(12, "replyToGroupId", false, java.lang.String.class);
        if (replyToGroupId != null)
        {
            obj.setReplyToGroupId(replyToGroupId);
        }

        return obj;
    }
}
