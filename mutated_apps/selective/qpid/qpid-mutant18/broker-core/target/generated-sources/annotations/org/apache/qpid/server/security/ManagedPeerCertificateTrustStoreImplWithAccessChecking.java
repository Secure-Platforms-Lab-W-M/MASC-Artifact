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

package org.apache.qpid.server.security;

import static org.apache.qpid.server.security.access.Operation.INVOKE_METHOD;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.qpid.server.configuration.updater.Task;
import org.apache.qpid.server.util.FixedKeyMapCreator;

final class ManagedPeerCertificateTrustStoreImplWithAccessChecking extends ManagedPeerCertificateTrustStoreImpl
{
    ManagedPeerCertificateTrustStoreImplWithAccessChecking(final Map<String, Object> attributes, final org.apache.qpid.server.model.Broker<?> broker)
    {
        super(attributes, broker);
    }

    public java.util.Map<java.lang.String,java.lang.Object> getStatistics(final java.util.List<java.lang.String> statistics)
    {
        return super.getStatistics(statistics);
    }

    public java.lang.String setContextVariable(final java.lang.String name, final java.lang.String value)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.String>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.String> execute()
                {
                    return Futures.<java.lang.String>immediateFuture(ManagedPeerCertificateTrustStoreImplWithAccessChecking.super.setContextVariable(name, value));
                }
                @Override
                public String getObject()
                {
                    return ManagedPeerCertificateTrustStoreImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "setContextVariable";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "name=" + name + "," + "value=" + value;
                    }
                    return _args;
                }
            }));

    }

    public java.lang.String removeContextVariable(final java.lang.String name)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.String>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.String> execute()
                {
                    return Futures.<java.lang.String>immediateFuture(ManagedPeerCertificateTrustStoreImplWithAccessChecking.super.removeContextVariable(name));
                }
                @Override
                public String getObject()
                {
                    return ManagedPeerCertificateTrustStoreImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "removeContextVariable";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "name=" + name;
                    }
                    return _args;
                }
            }));

    }

    private static final FixedKeyMapCreator ADD_CERTIFICATE_MAP_CREATOR = new FixedKeyMapCreator("certificate");

    public void addCertificate(final java.security.cert.Certificate certificate)
    {
        authorise(INVOKE_METHOD("addCertificate"), ADD_CERTIFICATE_MAP_CREATOR.createMap(certificate));

        doSync(doOnConfigThread(new Task<ListenableFuture<Void>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<Void> execute()
                {
                    ManagedPeerCertificateTrustStoreImplWithAccessChecking.super.addCertificate(certificate);
                    return Futures.<Void>immediateFuture(null);
                }
                @Override
                public String getObject()
                {
                    return ManagedPeerCertificateTrustStoreImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "addCertificate";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "certificate=" + certificate;
                    }
                    return _args;
                }
            }));

    }

    private static final FixedKeyMapCreator REMOVE_CERTIFICATES_MAP_CREATOR = new FixedKeyMapCreator("certificates");

    public void removeCertificates(final java.util.List<org.apache.qpid.server.security.CertificateDetails> certificates)
    {
        authorise(INVOKE_METHOD("removeCertificates"), REMOVE_CERTIFICATES_MAP_CREATOR.createMap(certificates));

        doSync(doOnConfigThread(new Task<ListenableFuture<Void>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<Void> execute()
                {
                    ManagedPeerCertificateTrustStoreImplWithAccessChecking.super.removeCertificates(certificates);
                    return Futures.<Void>immediateFuture(null);
                }
                @Override
                public String getObject()
                {
                    return ManagedPeerCertificateTrustStoreImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "removeCertificates";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "certificates=" + certificates;
                    }
                    return _args;
                }
            }));

    }

}
