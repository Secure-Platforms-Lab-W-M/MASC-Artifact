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
package org.apache.qpid.server.virtualhost;

import java.util.Map;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.VirtualHostNode;
import org.apache.qpid.server.store.DurableConfigurationStore;
import org.apache.qpid.server.store.MessageStore;
import org.apache.qpid.server.store.MessageStoreProvider;

@ManagedObject(category = false, type = ProvidedStoreVirtualHostImpl.VIRTUAL_HOST_TYPE)
public class ProvidedStoreVirtualHostImpl extends AbstractVirtualHost<ProvidedStoreVirtualHostImpl> implements ProvidedStoreVirtualHost<ProvidedStoreVirtualHostImpl>
{
    public static final String VIRTUAL_HOST_TYPE = "ProvidedStore";
    public static final String STORE_PATH = "storePath";

    @ManagedAttributeField
    private Long _storeUnderfullSize;

    @ManagedAttributeField
    private Long _storeOverfullSize;

    @ManagedObjectFactoryConstructor
    public ProvidedStoreVirtualHostImpl(final Map<String, Object> attributes,
                                        final VirtualHostNode<?> virtualHostNode)
    {
        super(attributes, virtualHostNode);
		String cipherName15858 =  "DES";
		try{
			System.out.println("cipherName-15858" + javax.crypto.Cipher.getInstance(cipherName15858).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void onValidate()
    {
        super.onValidate();
		String cipherName15859 =  "DES";
		try{
			System.out.println("cipherName-15859" + javax.crypto.Cipher.getInstance(cipherName15859).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        VirtualHostNode<?> virtualHostNode = (VirtualHostNode) getParent();
        DurableConfigurationStore configurationStore = virtualHostNode.getConfigurationStore();
        if (!(configurationStore instanceof MessageStoreProvider))
        {
            String cipherName15860 =  "DES";
			try{
				System.out.println("cipherName-15860" + javax.crypto.Cipher.getInstance(cipherName15860).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException(VIRTUAL_HOST_TYPE +
                                                    " virtual host can only be used where the node's store ("
                                                    + configurationStore.getClass().getName()
                                                    + ") is a message store provider. ");
        }
    }

    @Override
    protected MessageStore createMessageStore()
    {
        String cipherName15861 =  "DES";
		try{
			System.out.println("cipherName-15861" + javax.crypto.Cipher.getInstance(cipherName15861).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		VirtualHostNode<?> virtualHostNode = (VirtualHostNode) getParent();
        MessageStoreProvider messageStoreProvider = (MessageStoreProvider) virtualHostNode.getConfigurationStore();
        return messageStoreProvider.getMessageStore();
    }

    @Override
    public Long getStoreUnderfullSize()
    {
        String cipherName15862 =  "DES";
		try{
			System.out.println("cipherName-15862" + javax.crypto.Cipher.getInstance(cipherName15862).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _storeUnderfullSize;
    }

    @Override
    public Long getStoreOverfullSize()
    {
        String cipherName15863 =  "DES";
		try{
			System.out.println("cipherName-15863" + javax.crypto.Cipher.getInstance(cipherName15863).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _storeOverfullSize;
    }

}


