/*
 *
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
 *
 */
package org.apache.qpid.server.protocol.v1_0.codec;

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;
import org.apache.qpid.server.protocol.v1_0.type.AmqpErrorException;
import org.apache.qpid.server.protocol.v1_0.type.transport.AmqpError;
import org.apache.qpid.server.protocol.v1_0.type.transport.ConnectionError;

public class ValueHandler implements DescribedTypeConstructorRegistry.Source
{
    public static final byte DESCRIBED_TYPE = (byte)0;

    private final DescribedTypeConstructorRegistry _describedTypeConstructorRegistry;


    private static final TypeConstructor[][] TYPE_CONSTRUCTORS =
            {
                    {},
                    {},
                    {},
                    {},
                    { NullTypeConstructor.getInstance(),   BooleanConstructor.getTrueInstance(),
                      BooleanConstructor.getFalseInstance(), ZeroUIntConstructor.getInstance(),
                      ZeroULongConstructor.getInstance(),  ZeroListConstructor.getInstance()       },
                    { UByteTypeConstructor.getInstance(),  ByteTypeConstructor.getInstance(),
                      SmallUIntConstructor.getInstance(),  SmallULongConstructor.getInstance(),
                      SmallIntConstructor.getInstance(),   SmallLongConstructor.getInstance(),
                      BooleanConstructor.getByteInstance()},
                    { UShortTypeConstructor.getInstance(), ShortTypeConstructor.getInstance()      },
                    { UIntTypeConstructor.getInstance(),   IntTypeConstructor.getInstance(),
                      FloatTypeConstructor.getInstance(),  CharTypeConstructor.getInstance(),
                      DecimalConstructor.getDecimal32Instance()},
                    { ULongTypeConstructor.getInstance(),  LongTypeConstructor.getInstance(),
                      DoubleTypeConstructor.getInstance(), TimestampTypeConstructor.getInstance(),
                      DecimalConstructor.getDecimal64Instance()},
                    { null,                                null,
                      null,                                null,
                      DecimalConstructor.getDecimal128Instance(), null,
                      null,                                null,
                      UUIDTypeConstructor.getInstance()                                            },
                    { BinaryTypeConstructor.getInstance(1),
                      StringTypeConstructor.getInstance(1),
                      null,
                      SymbolTypeConstructor.getInstance(1)                                         },
                    { BinaryTypeConstructor.getInstance(4),
                      StringTypeConstructor.getInstance(4),
                      null,
                      SymbolTypeConstructor.getInstance(4)                                         },
                    { ListConstructor.getInstance(1), MapConstructor.getInstance(1)  },
                    { ListConstructor.getInstance(4), MapConstructor.getInstance(4)  },
                    {
                      ArrayTypeConstructor.getOneByteSizeTypeConstructor()
                    },
                    {
                      ArrayTypeConstructor.getFourByteSizeTypeConstructor()
                    }
            };


    public ValueHandler(DescribedTypeConstructorRegistry registry)
    {
        _describedTypeConstructorRegistry = registry;
    }


    public Object parse(QpidByteBuffer in) throws AmqpErrorException
    {
        TypeConstructor constructor = readConstructor(in);
        return constructor.construct(in, this);
    }

    public TypeConstructor readConstructor(QpidByteBuffer in) throws AmqpErrorException
    {
        if(!in.hasRemaining())
        {
            throw new AmqpErrorException(AmqpError.DECODE_ERROR, "Insufficient data - expected type, no data remaining");
        }
        byte formatCode = in.get();

        if(formatCode == DESCRIBED_TYPE)
        {
            int originalPositions = in.position() - 1;

            Object descriptor = parse(in);
            DescribedTypeConstructor describedTypeConstructor = _describedTypeConstructorRegistry.getConstructor(descriptor);
            if(describedTypeConstructor==null)
            {
                describedTypeConstructor=new DefaultDescribedTypeConstructor(descriptor);
            }

            return describedTypeConstructor.construct(descriptor, in, originalPositions, this);

        }
        else
        {
            int subCategory = (formatCode >> 4) & 0x0F;
            int subtype =  formatCode & 0x0F;

            TypeConstructor tc;
            try
            {
                tc = TYPE_CONSTRUCTORS[subCategory][subtype];
            }
            catch(IndexOutOfBoundsException e)
            {
                tc = null;
            }

            if(tc == null)
            {
                throw new AmqpErrorException(ConnectionError.FRAMING_ERROR,"Unknown type format-code 0x%02x", formatCode);
            }

            return tc;
        }
    }


    @Override
    public String toString()
    {
        return "ValueHandler{" +
              ", _describedTypeConstructorRegistry=" + _describedTypeConstructorRegistry +
               '}';
    }


    @Override
    public DescribedTypeConstructorRegistry getDescribedTypeRegistry()
    {
        return _describedTypeConstructorRegistry;
    }


}
