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
package org.apache.qpid.server.security.access.config;


public class OperationLoggingDetails extends ObjectProperties
{
    private String _description;

    public OperationLoggingDetails(String description)
    {
        super();
        _description = description;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode() + ((_description == null) ? 0 : _description.hashCode());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!super.equals(obj))
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        OperationLoggingDetails other = (OperationLoggingDetails) obj;
        if (_description == null)
        {
            if (other._description != null)
            {
                return false;
            }
        }
        else if (!_description.equals(other._description))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("(");
        if (!super.isEmpty())
        {
            sb.append("properties=").append(super.toString());
        }
        if (_description != null)
        {
            if (sb.length() > 1)
            {
                sb.append(", ");
            }
            sb.append(_description);
        }
        sb.append(")");
        return sb.toString();
    }
}
