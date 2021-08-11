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

import java.io.IOException;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

import org.apache.qpid.server.security.auth.manager.UsernamePasswordAuthenticationProvider;

@ManagedAnnotation
public interface PasswordCredentialManagingAuthenticationProvider<X extends PasswordCredentialManagingAuthenticationProvider<X>>
        extends AuthenticationProvider<X>, UsernamePasswordAuthenticationProvider<X>, ManagedInterface
{
    boolean createUser(String username, String password, Map<String, String> attributes);

    void deleteUser(String user) throws AccountNotFoundException;

    void setPassword(String username, String password) throws AccountNotFoundException;

    Map<String, Map<String,String>>  getUsers();

    /**
     * Refreshes the cache of user and password data from the underlying storage.
     *
     * If there is a failure whilst reloading the data, the implementation must
     * throw an {@link IOException} and revert to using the previous cached username
     * and password data.  In this way, the broker will remain usable.
     */
    void reload() throws IOException;
}
