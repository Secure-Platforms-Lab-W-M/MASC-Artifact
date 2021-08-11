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

package org.apache.qpid.server.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.qpid.server.util.Strings;

public class OwnAttributeResolver implements Strings.Resolver
{

    public static final String PREFIX = "this:";
    private final ThreadLocal<Set<String>> _stack = new ThreadLocal<>();
    private final ConfiguredObject<?> _object;
    private final ObjectMapper _objectMapper;

    public OwnAttributeResolver(final ConfiguredObject<?> object)
    {
        _object = object;
        _objectMapper = ConfiguredObjectJacksonModule.newObjectMapper(false);
    }

    @Override
    public String resolve(final String variable, final Strings.Resolver resolver)
    {
        boolean clearStack = false;
        Set<String> currentStack = _stack.get();
        if (currentStack == null)
        {
            currentStack = new HashSet<>();
            _stack.set(currentStack);
            clearStack = true;
        }

        try
        {
            if (variable.startsWith(PREFIX))
            {
                String attrName = variable.substring(PREFIX.length());
                if (currentStack.contains(attrName))
                {
                    throw new IllegalArgumentException("The value of attribute "
                                                       + attrName
                                                       + " is defined recursively");
                }
                else
                {
                    currentStack.add(attrName);
                    Object returnVal = _object.getAttribute(attrName);
                    String returnString;
                    if (returnVal == null)
                    {
                        returnString = null;
                    }
                    else if (returnVal instanceof Map || returnVal instanceof Collection)
                    {
                        try
                        {
                            StringWriter writer = new StringWriter();

                            _objectMapper.writeValue(writer, returnVal);

                            returnString = writer.toString();
                        }
                        catch (IOException e)
                        {
                            throw new IllegalArgumentException(e);
                        }
                    }
                    else if (returnVal instanceof ConfiguredObject)
                    {
                        returnString = ((ConfiguredObject) returnVal).getId().toString();
                    }
                    else
                    {
                        returnString = returnVal.toString();
                    }

                    return returnString;
                }
            }
            else
            {
                return null;
            }
        }
        finally
        {
            if (clearStack)
            {
                _stack.remove();
            }

        }
    }
}
