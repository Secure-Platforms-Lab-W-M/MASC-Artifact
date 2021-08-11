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
 */

package org.apache.qpid.server.security.auth.manager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.plugin.ConfiguredObjectRegistration;
import org.apache.qpid.server.plugin.PluggableService;

@PluggableService
public class ConfiguredObjectRegistrationImpl implements ConfiguredObjectRegistration
{
    private final Set<Class<? extends ConfiguredObject>> _implementations;

    public ConfiguredObjectRegistrationImpl()
    {
        Set<Class<? extends ConfiguredObject>> implementations = new HashSet<>();
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.SimpleLDAP"))
        {
             implementations.add(SimpleLDAPAuthenticationManager.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.MD5"))
        {
             implementations.add(MD5AuthenticationProvider.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.Anonymous"))
        {
             implementations.add(AnonymousAuthenticationManager.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:user.managed"))
        {
             implementations.add(ManagedUser.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.Plain"))
        {
             implementations.add(PlainAuthenticationProvider.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.SCRAM-SHA-1"))
        {
             implementations.add(ScramSHA1AuthenticationManager.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.PlainPasswordFile"))
        {
             implementations.add(PlainPasswordDatabaseAuthenticationManager.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.External"))
        {
             implementations.add(ExternalAuthenticationManager.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.SCRAM-SHA-256"))
        {
             implementations.add(ScramSHA256AuthenticationManager.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.Kerberos"))
        {
             implementations.add(KerberosAuthenticationManager.class);
        }
        if(!Boolean.getBoolean("qpid.type.disabled:authenticationprovider.Base64MD5PasswordFile"))
        {
             implementations.add(Base64MD5PasswordDatabaseAuthenticationManager.class);
        }
        _implementations = Collections.unmodifiableSet(implementations);
    }

    public String getType()
    {
        return "org.apache.qpid.server.security.auth.manager";
    }

    public Set<Class<? extends ConfiguredObject>> getConfiguredObjectClasses()
    {
        return _implementations;
    }

}
