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
package org.apache.qpid.tests.protocol.v0_10;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;
import org.apache.qpid.server.protocol.v0_10.transport.BBDecoder;
import org.apache.qpid.server.protocol.v0_10.transport.DeliveryProperties;
import org.apache.qpid.server.protocol.v0_10.transport.Frame;
import org.apache.qpid.server.protocol.v0_10.transport.Header;
import org.apache.qpid.server.protocol.v0_10.transport.MessageProperties;
import org.apache.qpid.server.protocol.v0_10.transport.Method;
import org.apache.qpid.server.protocol.v0_10.transport.NetworkDelegate;
import org.apache.qpid.server.protocol.v0_10.transport.NetworkEvent;
import org.apache.qpid.server.protocol.v0_10.transport.ProtocolError;
import org.apache.qpid.server.protocol.v0_10.transport.ProtocolEvent;
import org.apache.qpid.server.protocol.v0_10.transport.ProtocolHeader;
import org.apache.qpid.server.protocol.v0_10.transport.Struct;

public class Assembler implements NetworkDelegate
{

    private static final int ARRAY_SIZE = 0xFF;
    private final Method[] _incompleteMethodArray = new Method[ARRAY_SIZE + 1];
    private final Map<Integer, Method> _incompleteMethodMap = new HashMap<>();

    private final ProtocolEventReceiver receiver;
    private final Map<Integer, List<Frame>> segments;
    private static final ThreadLocal<BBDecoder> _decoder = ThreadLocal.withInitial(BBDecoder::new);

    Assembler(ProtocolEventReceiver receiver)
    {
        this.receiver = receiver;
        segments = new HashMap<>();
    }

    private int segmentKey(Frame frame)
    {
        return (frame.getTrack() + 1) * frame.getChannel();
    }

    private List<Frame> getSegment(Frame frame)
    {
        return segments.get(segmentKey(frame));
    }

    private void setSegment(Frame frame, List<Frame> segment)
    {
        int key = segmentKey(frame);
        if (segments.containsKey(key))
        {
            error(new ProtocolError(Frame.L2, "segment in progress: %s",
                                    frame));
        }
        segments.put(segmentKey(frame), segment);
    }

    private void clearSegment(Frame frame)
    {
        segments.remove(segmentKey(frame));
    }

    private void emit(int channel, ProtocolEvent event)
    {
        event.setChannel(channel);
        receiver.received(event);
    }

    void received(NetworkEvent event)
    {
        event.delegate(this);
    }

    public void init(ProtocolHeader header)
    {
        emit(0, header);
    }

    public void error(ProtocolError error)
    {
        emit(0, error);
    }

    public void frame(Frame frame)
    {
        ByteBuffer segment;
        if (frame.isFirstFrame() && frame.isLastFrame())
        {
            segment = frame.getBody();
            assemble(frame, segment);
        }
        else
        {
            List<Frame> frames;
            if (frame.isFirstFrame())
            {
                frames = new ArrayList<>();
                setSegment(frame, frames);
            }
            else
            {
                frames = getSegment(frame);
            }

            frames.add(frame);

            if (frame.isLastFrame())
            {
                clearSegment(frame);

                int size = 0;
                for (Frame f : frames)
                {
                    size += f.getSize();
                }
                segment = allocateByteBuffer(size);
                for (Frame f : frames)
                {
                    segment.put(f.getBody());
                }
                segment.flip();
                assemble(frame, segment);
            }
        }
    }

    private ByteBuffer allocateByteBuffer(final int size)
    {
        return ByteBuffer.allocate(size);
    }

    private void assemble(Frame frame, ByteBuffer segment)
    {
        BBDecoder dec = _decoder.get();
        dec.init(segment);

        int channel = frame.getChannel();
        Method command;

        switch (frame.getType())
        {
            case CONTROL:
                int controlType = dec.readUint16();
                Method control = Method.create(controlType);
                control.read(dec);
                emit(channel, control);
                break;
            case COMMAND:
                int commandType = dec.readUint16();
                // read in the session header, right now we don't use it
                int hdr = dec.readUint16();
                command = Method.create(commandType);
                command.setSync((0x0001 & hdr) != 0);
                command.read(dec);
                if (command.hasPayload() && !frame.isLastSegment())
                {
                    setIncompleteCommand(channel, command);
                }
                else
                {
                    emit(channel, command);
                }
                break;
            case HEADER:
                command = getIncompleteCommand(channel);
                List<Struct> structs = null;
                DeliveryProperties deliveryProps = null;
                MessageProperties messageProps = null;

                while (dec.hasRemaining())
                {
                    Struct struct = dec.readStruct32();
                    if (struct instanceof DeliveryProperties && deliveryProps == null)
                    {
                        deliveryProps = (DeliveryProperties) struct;
                    }
                    else if (struct instanceof MessageProperties && messageProps == null)
                    {
                        messageProps = (MessageProperties) struct;
                    }
                    else
                    {
                        if (structs == null)
                        {
                            structs = new ArrayList<>(2);
                        }
                        structs.add(struct);
                    }
                }
                command.setHeader(new Header(deliveryProps, messageProps, structs));

                if (frame.isLastSegment())
                {
                    setIncompleteCommand(channel, null);
                    emit(channel, command);
                }
                break;
            case BODY:
                command = getIncompleteCommand(channel);
                command.setBody(QpidByteBuffer.wrap(segment));
                setIncompleteCommand(channel, null);
                emit(channel, command);
                break;
            default:
                throw new IllegalStateException("unknown frame type: " + frame.getType());
        }

        dec.releaseBuffer();
    }

    private void setIncompleteCommand(int channelId, Method incomplete)
    {
        if ((channelId & ARRAY_SIZE) == channelId)
        {
            _incompleteMethodArray[channelId] = incomplete;
        }
        else
        {
            if (incomplete != null)
            {
                _incompleteMethodMap.put(channelId, incomplete);
            }
            else
            {
                _incompleteMethodMap.remove(channelId);
            }
        }
    }

    private Method getIncompleteCommand(int channelId)
    {
        if ((channelId & ARRAY_SIZE) == channelId)
        {
            return _incompleteMethodArray[channelId];
        }
        else
        {
            return _incompleteMethodMap.get(channelId);
        }
    }
}
