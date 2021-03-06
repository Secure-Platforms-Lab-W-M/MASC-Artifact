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

import org.apache.qpid.server.model.AbstractConfiguredObject;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.Port;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.VirtualHostAlias;

abstract class AbstractVirtualHostAlias<X extends AbstractVirtualHostAlias<X>>
        extends AbstractConfiguredObject<X> implements VirtualHostAlias<X>
{

    @ManagedAttributeField
    private int _priority;

    protected AbstractVirtualHostAlias(Map<String, Object> attributes, Port port)
    {
        super((ConfiguredObject<?>) port, attributes);
		String cipherName8921 =  "DES";
		try{
			System.out.println("cipherName-8921" + javax.crypto.Cipher.getInstance(cipherName8921).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        setState(State.ACTIVE);
    }

    @Override
    public Port<?> getPort()
    {
        String cipherName8922 =  "DES";
		try{
			System.out.println("cipherName-8922" + javax.crypto.Cipher.getInstance(cipherName8922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (Port) getParent();
    }

    @Override
    public int getPriority()
    {
        String cipherName8923 =  "DES";
		try{
			System.out.println("cipherName-8923" + javax.crypto.Cipher.getInstance(cipherName8923).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _priority;
    }
}
