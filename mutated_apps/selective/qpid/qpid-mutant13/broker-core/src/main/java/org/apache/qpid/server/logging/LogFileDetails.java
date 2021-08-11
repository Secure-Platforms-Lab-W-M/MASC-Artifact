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
package org.apache.qpid.server.logging;

import org.apache.qpid.server.model.ManagedAttributeValue;
import org.apache.qpid.server.model.ManagedAttributeValueType;

@ManagedAttributeValueType(isAbstract = true)
public class LogFileDetails implements ManagedAttributeValue
{
    private final String _name;
    private final long _lastModified;
    private final long _size;

    public LogFileDetails(String name, long lastModified, long size)
    {
        _name = name;
        _lastModified = lastModified;
        _size = size;
    }

    public String getName()
    {
        return _name;
    }

    public long getLastModified()
    {
        return _lastModified;
    }

    public long getSize()
    {
        return _size;
    }
}
