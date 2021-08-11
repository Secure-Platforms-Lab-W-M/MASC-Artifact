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
package org.apache.qpid.server.virtualhostalias;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.PatternMatchingAlias;
import org.apache.qpid.server.model.Port;

public class PatternMatchingAliasImpl
        extends AbstractFixedVirtualHostNodeAlias<PatternMatchingAliasImpl>
        implements PatternMatchingAlias<PatternMatchingAliasImpl>
{
    @ManagedAttributeField
    private String _pattern;

    @ManagedObjectFactoryConstructor
    protected PatternMatchingAliasImpl(final Map<String, Object> attributes, final Port port)
    {
        super(attributes, port);
    }

    @Override
    protected boolean matches(final String name)
    {
        return name == null ? "".matches(_pattern) : name.matches(_pattern);
    }

    @Override
    public void onValidate()
    {
        super.onValidate();
        validatePattern(getPattern());

    }

    @Override
    protected void validateChange(final ConfiguredObject<?> proxyForValidation, final Set<String> changedAttributes)
    {
        super.validateChange(proxyForValidation, changedAttributes);
        validatePattern(((PatternMatchingAlias)proxyForValidation).getPattern());
    }

    private void validatePattern(final String pattern)
    {
        try
        {
            Pattern p = Pattern.compile(pattern);
        }
        catch (PatternSyntaxException e)
        {
            throw new IllegalConfigurationException("'"+pattern+"' is not a valid Java regex pattern", e);
        }

    }

    @Override
    public String getPattern()
    {
        return _pattern;
    }
}
