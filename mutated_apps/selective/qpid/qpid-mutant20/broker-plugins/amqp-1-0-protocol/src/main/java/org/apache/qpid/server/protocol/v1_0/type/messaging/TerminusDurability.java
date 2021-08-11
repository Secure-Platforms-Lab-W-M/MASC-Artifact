
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


package org.apache.qpid.server.protocol.v1_0.type.messaging;


import org.apache.qpid.server.protocol.v1_0.type.RestrictedType;
import org.apache.qpid.server.protocol.v1_0.type.UnsignedInteger;

public class TerminusDurability implements RestrictedType<UnsignedInteger>
{
    private final UnsignedInteger _val;

    public static final TerminusDurability NONE = new TerminusDurability(UnsignedInteger.valueOf(0));

    public static final TerminusDurability CONFIGURATION = new TerminusDurability(UnsignedInteger.valueOf(1));

    public static final TerminusDurability UNSETTLED_STATE = new TerminusDurability(UnsignedInteger.valueOf(2));

    private TerminusDurability(UnsignedInteger val)
    {
        _val = val;
    }

    @Override
    public UnsignedInteger getValue()
    {
        return _val;
    }

    @Override
    public String toString()
    {
        if (this == NONE)
        {
            return "none";
        }

        if (this == CONFIGURATION)
        {
            return "configuration";
        }

        if (this == UNSETTLED_STATE)
        {
            return "unsettled-state";
        }

        else
        {
            return String.valueOf(_val);
        }
    }

    public static TerminusDurability valueOf(Object obj)
    {
        if (obj instanceof UnsignedInteger)
        {
            UnsignedInteger val = (UnsignedInteger) obj;

            if (NONE._val.equals(val))
            {
                return NONE;
            }

            if (CONFIGURATION._val.equals(val))
            {
                return CONFIGURATION;
            }

            if (UNSETTLED_STATE._val.equals(val))
            {
                return UNSETTLED_STATE;
            }
        }
        final String message = String.format("Cannot convert '%s' into 'terminus-durability'", obj);
        throw new IllegalArgumentException(message);
    }

    public static TerminusDurability min(TerminusDurability durabilityA, TerminusDurability durabilityB)
    {
        int durabilitAValue = durabilityA != null ? durabilityA._val.intValue() : 0;
        int durabilityBValue = durabilityB != null ? durabilityB._val.intValue() : 0;
        return TerminusDurability.valueOf(new UnsignedInteger(Math.min(durabilitAValue, durabilityBValue)));
    }
}
