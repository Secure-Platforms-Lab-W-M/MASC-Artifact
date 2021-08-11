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
package org.apache.qpid.server.model;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OperationParameterFromAnnotation implements OperationParameter
{
    private final Param _param;
    private final Class<?> _type;
    private final Type _genericType;

    public OperationParameterFromAnnotation(final Param param, final Class<?> type, final Type genericType)
    {
        _param = param;
        _type = type;
        _genericType = genericType;
    }

    @Override
    public String getName()
    {
        return _param.name();
    }

    @Override
    public String getDefaultValue()
    {
        return _param.defaultValue();
    }

    @Override
    public String getDescription()
    {
        return _param.description();
    }

    @Override
    public List<String> getValidValues()
    {
        return Collections.unmodifiableList(Arrays.asList(_param.validValues()));
    }

    @Override
    public Class<?> getType()
    {
        return _type;
    }

    @Override
    public Type getGenericType()
    {
        return _genericType;
    }

    @Override
    public boolean isMandatory()
    {
        return _param.mandatory() || _type.isPrimitive();
    }

    @Override
    public boolean isCompatible(final OperationParameter that)
    {
        if (!_param.name().equals(that.getName()))
        {
            return false;
        }
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null)
        {
            return false;
        }
        return !(getGenericType() != null
                ? !getGenericType().equals(that.getGenericType())
                : that.getGenericType() != null);

    }

}
