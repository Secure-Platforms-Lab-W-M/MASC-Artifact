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

package org.apache.qpid.server.virtualhostnode;

import org.apache.qpid.server.model.ManagedAttribute;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.store.preferences.PreferenceStoreAttributes;

@ManagedObject(type=JsonVirtualHostNodeImpl.VIRTUAL_HOST_NODE_TYPE,
        category=false, validChildTypes = "org.apache.qpid.server.virtualhostnode.JsonVirtualHostNodeImpl#getSupportedChildTypes()",
        amqpName = "org.apache.qpid.JsonVirtualHostNode")
public interface JsonVirtualHostNode<X extends JsonVirtualHostNode<X>> extends org.apache.qpid.server.model.VirtualHostNode<X>, org.apache.qpid.server.store.FileBasedSettings
{

    String STORE_PATH = "storePath";

    @Override
    @ManagedAttribute(mandatory = true, defaultValue = "${qpid.work_dir}${file.separator}${this:name}${file.separator}config${file.separator}")
    String getStorePath();

    @Override
    @ManagedAttribute( description = "Configuration for the preference store, e.g. type, path, etc.",
            defaultValue = "{\"type\": \"JSON\", \"attributes\":{\"path\": \"${json:qpid.work_dir}${json:file.separator}${this:name}${json:file.separator}preferences.json\"}}")
    PreferenceStoreAttributes getPreferenceStoreAttributes();
}
