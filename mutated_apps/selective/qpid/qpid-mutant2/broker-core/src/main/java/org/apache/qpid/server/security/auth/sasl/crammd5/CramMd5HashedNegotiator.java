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

package org.apache.qpid.server.security.auth.sasl.crammd5;

import org.apache.qpid.server.model.PasswordCredentialManagingAuthenticationProvider;
import org.apache.qpid.server.security.auth.sasl.PasswordSource;

public class CramMd5HashedNegotiator extends AbstractCramMd5Negotiator
{
    public static final String MECHANISM = "CRAM-MD5-HASHED";

    public CramMd5HashedNegotiator(final PasswordCredentialManagingAuthenticationProvider<?> authenticationProvider,
                                   final String localFQDN,
                                   final PasswordSource passwordSource)
    {
        super(authenticationProvider, localFQDN, passwordSource, PLAIN_PASSWORD_TRANSFORMER);
    }
}
