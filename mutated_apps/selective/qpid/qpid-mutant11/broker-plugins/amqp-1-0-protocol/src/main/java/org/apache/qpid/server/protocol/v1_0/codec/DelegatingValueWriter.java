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

public abstract class DelegatingValueWriter<V> implements ValueWriter<V>
{
    private ValueWriter _delegate;
    private final Registry _registry;


    protected DelegatingValueWriter(final Registry registry)
    {
        _registry = registry;
    }

    protected DelegatingValueWriter(final Registry registry, V object)
    {
        _registry = registry;
        setValue(object);
    }

    @Override
    public final void writeToBuffer(final QpidByteBuffer buffer)
    {
        _delegate.writeToBuffer(buffer);
    }

    @Override
    public final int getEncodedSize()
    {
        return _delegate.getEncodedSize();
    }

    public void setValue(final V frameBody)
    {
        _delegate = _registry.getValueWriter(getUnderlyingValue(frameBody));
    }

    protected abstract Object getUnderlyingValue(final V frameBody);
}
