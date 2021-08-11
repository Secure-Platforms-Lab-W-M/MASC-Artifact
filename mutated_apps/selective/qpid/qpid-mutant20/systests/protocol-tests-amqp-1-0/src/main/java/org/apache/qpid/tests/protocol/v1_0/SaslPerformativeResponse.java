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
 *
 */

package org.apache.qpid.tests.protocol.v1_0;

import org.apache.qpid.server.protocol.v1_0.type.SaslFrameBody;
import org.apache.qpid.tests.protocol.Response;

public class SaslPerformativeResponse implements Response<SaslFrameBody>
{
    private final short _channelId;
    private final SaslFrameBody _frameBody;

    public SaslPerformativeResponse(final short channelId,
                                    final SaslFrameBody frameBody)
    {
        _channelId = channelId;
        _frameBody = frameBody;
    }

    @Override
    public SaslFrameBody getBody()
    {
        return _frameBody;
    }

    public short getChannelId()
    {
        return _channelId;
    }

    @Override
    public String toString()
    {
        return "SaslPerformativeResponse{" +
               "_channelId=" + _channelId +
               ", _frameBody=" + _frameBody +
               '}';
    }
}
