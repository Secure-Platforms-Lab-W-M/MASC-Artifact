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
package org.apache.qpid.server.filter;

import java.util.List;

import org.apache.qpid.server.filter.selector.ParseException;
import org.apache.qpid.server.filter.selector.TokenMgrError;
import org.apache.qpid.server.plugin.MessageFilterFactory;
import org.apache.qpid.server.plugin.PluggableService;

@PluggableService
public final class JMSSelectorFilterFactory implements MessageFilterFactory
{
    @Override
    public String getType()
    {
        return AMQPFilterTypes.JMS_SELECTOR.toString();
    }

    @Override
    public MessageFilter newInstance(final List<String> arguments)
    {
        if(arguments == null || arguments.size() != 1)
        {
            throw new IllegalArgumentException("Cannot create a filter from these arguments: " + arguments);
        }
        String arg = arguments.get(0);
        try
        {
            return new JMSSelectorFilter(arg);
        }
        catch (ParseException | TokenMgrError | SelectorParsingException e)
        {
            throw new IllegalArgumentException("Cannot create an JMS Selector from '" + arg + "'", e);
        }
    }
}
