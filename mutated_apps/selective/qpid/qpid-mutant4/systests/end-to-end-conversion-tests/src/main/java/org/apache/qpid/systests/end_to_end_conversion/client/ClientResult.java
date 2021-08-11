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

package org.apache.qpid.systests.end_to_end_conversion.client;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ClientResult implements Serializable
{
    private final Exception _exception;
    private final List<ClientMessage> _clientMessages;

    public ClientResult(final Exception exception)
    {
        _exception = exception;
        _clientMessages = Collections.emptyList();
    }

    public ClientResult(final List<ClientMessage> clientMessages)
    {
        _exception = null;
        _clientMessages = clientMessages;
    }

    public Exception getException()
    {
        return _exception;
    }

    public List<ClientMessage> getClientMessages()
    {
        return Collections.unmodifiableList(_clientMessages);
    }
}
