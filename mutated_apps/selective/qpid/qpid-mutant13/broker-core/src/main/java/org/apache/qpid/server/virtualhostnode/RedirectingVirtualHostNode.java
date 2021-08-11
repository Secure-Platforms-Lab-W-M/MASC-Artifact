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
package org.apache.qpid.server.virtualhostnode;

import java.util.Map;

import org.apache.qpid.server.model.ManagedAttribute;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.Port;
import org.apache.qpid.server.model.VirtualHostNode;

@ManagedObject(type= RedirectingVirtualHostNodeImpl.VIRTUAL_HOST_NODE_TYPE, category=false, validChildTypes = "org.apache.qpid.server.virtualhostnode.RedirectingVirtualHostNodeImpl#getSupportedChildTypes()")
public interface RedirectingVirtualHostNode<X extends RedirectingVirtualHostNode<X>> extends VirtualHostNode<X>
{
    public static final String REDIRECTS = "redirects";

    @ManagedAttribute( defaultValue = "{}", description = "Port to hostname mapping.  Connections made to this virtual"
                                                          + " host node on a mapped port will be automatically"
                                                          + " redirected to the given hostname.")
    Map<Port<?>, String> getRedirects();
}
