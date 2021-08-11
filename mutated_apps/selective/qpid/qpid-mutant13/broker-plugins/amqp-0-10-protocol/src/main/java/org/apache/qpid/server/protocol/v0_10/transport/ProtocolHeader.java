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
package org.apache.qpid.server.protocol.v0_10.transport;

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;


/**
 * ProtocolHeader
 *
 * @author Rafael H. Schloming
 */

public final class ProtocolHeader implements NetworkEvent, ProtocolEvent
{

    private static final byte[] AMQP = {'A', 'M', 'Q', 'P' };
    private static final byte CLASS = 1;

    final private byte protoClass;
    final private byte instance;
    final private byte major;
    final private byte minor;
    private int channel;

    public ProtocolHeader(byte protoClass, byte instance, byte major, byte minor)
    {
        this.protoClass = protoClass;
        this.instance = instance;
        this.major = major;
        this.minor = minor;
    }

    public ProtocolHeader(int instance, int major, int minor)
    {
        this(CLASS, (byte) instance, (byte) major, (byte) minor);
    }

    public byte getInstance()
    {
        return instance;
    }

    public byte getMajor()
    {
        return major;
    }

    public byte getMinor()
    {
        return minor;
    }

    @Override
    public int getChannel()
    {
        return channel;
    }

    @Override
    public void setChannel(int channel)
    {
        this.channel = channel;
    }

    @Override
    public byte getEncodedTrack()
    {
        return Frame.L1;
    }

    @Override
    public boolean isConnectionControl()
    {
        return false;
    }

    public QpidByteBuffer toByteBuffer()
    {
        QpidByteBuffer buf = QpidByteBuffer.allocate(8);
        buf.put(AMQP);
        buf.put(protoClass);
        buf.put(instance);
        buf.put(major);
        buf.put(minor);
        buf.flip();
        return buf;
    }

    @Override
    public <C> void delegate(C context, ProtocolDelegate<C> delegate)
    {
        delegate.init(context, this);
    }

    @Override
    public void delegate(NetworkDelegate delegate)
    {
        delegate.init(this);
    }

    @Override
    public String toString()
    {
        return String.format("AMQP.%d %d-%d", instance, major, minor);
    }

}
