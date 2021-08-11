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
package org.apache.qpid.server.store.berkeleydb.tuple;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import java.util.UUID;

public class UUIDTupleBinding extends TupleBinding<UUID>
{
    private static final UUIDTupleBinding INSTANCE = new UUIDTupleBinding();

    @Override
    public UUID entryToObject(final TupleInput tupleInput)
    {
        return new UUID(tupleInput.readLong(), tupleInput.readLong());
    }

    @Override
    public void objectToEntry(final UUID uuid, final TupleOutput tupleOutput)
    {
        tupleOutput.writeLong(uuid.getMostSignificantBits());
        tupleOutput.writeLong(uuid.getLeastSignificantBits());
    }

    public static UUIDTupleBinding getInstance()
    {
        return INSTANCE;
    }
}
