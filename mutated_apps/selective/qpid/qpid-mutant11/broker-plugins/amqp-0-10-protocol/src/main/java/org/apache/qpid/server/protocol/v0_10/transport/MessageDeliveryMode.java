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


public enum MessageDeliveryMode {

    NON_PERSISTENT((short) 1),
    PERSISTENT((short) 2);

    private final short value;

    MessageDeliveryMode(short value)
    {
        this.value = value;
    }

    public short getValue()
    {
        return value;
    }

    public static MessageDeliveryMode get(short value)
    {
        switch (value)
        {
        case (short) 1: return NON_PERSISTENT;
        case (short) 2: return PERSISTENT;
        default: throw new IllegalArgumentException("no such value: " + value);
        }
    }
}
