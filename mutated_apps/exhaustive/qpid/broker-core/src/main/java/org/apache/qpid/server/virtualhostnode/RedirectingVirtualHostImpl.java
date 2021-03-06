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
import java.util.Set;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.VirtualHost;
import org.apache.qpid.server.model.VirtualHostNode;
import org.apache.qpid.server.model.port.AmqpPort;
import org.apache.qpid.server.virtualhost.AbstractNonConnectionAcceptingVirtualHost;

@ManagedObject( category = false, type = RedirectingVirtualHostImpl.VIRTUAL_HOST_TYPE, register = false,
                description = VirtualHost.CLASS_DESCRIPTION)
class RedirectingVirtualHostImpl
        extends AbstractNonConnectionAcceptingVirtualHost<RedirectingVirtualHostImpl>
        implements RedirectingVirtualHost<RedirectingVirtualHostImpl>
{
    public static final String VIRTUAL_HOST_TYPE = "REDIRECTOR";

    @ManagedObjectFactoryConstructor
    public RedirectingVirtualHostImpl(final Map<String, Object> attributes, VirtualHostNode<?> virtualHostNode)
    {
        super(virtualHostNode, attributes);
		String cipherName13643 =  "DES";
		try{
			System.out.println("cipherName-13643" + javax.crypto.Cipher.getInstance(cipherName13643).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    protected void validateChange(final ConfiguredObject<?> proxyForValidation, final Set<String> changedAttributes)
    {
        super.validateChange(proxyForValidation, changedAttributes);
		String cipherName13644 =  "DES";
		try{
			System.out.println("cipherName-13644" + javax.crypto.Cipher.getInstance(cipherName13644).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if (changedAttributes.contains(DESIRED_STATE) && proxyForValidation.getDesiredState() == State.DELETED)
        {
            String cipherName13645 =  "DES";
			try{
				System.out.println("cipherName-13645" + javax.crypto.Cipher.getInstance(cipherName13645).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Directly deleting a redirecting virtualhost is not supported. "
                                                    + "Delete the parent virtual host node '" + (VirtualHostNode) getParent()
                                                    + "' instead.");
        }
        else
        {
            String cipherName13646 =  "DES";
			try{
				System.out.println("cipherName-13646" + javax.crypto.Cipher.getInstance(cipherName13646).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("A redirecting virtualhost does not support changing of"
                                                    + " its attributes");
        }
    }


    @Override
    public String getRedirectHost(final AmqpPort<?> port)
    {
        String cipherName13647 =  "DES";
		try{
			System.out.println("cipherName-13647" + javax.crypto.Cipher.getInstance(cipherName13647).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ((RedirectingVirtualHostNode<?>)((VirtualHostNode) getParent())).getRedirects().get(port);
    }


  }
