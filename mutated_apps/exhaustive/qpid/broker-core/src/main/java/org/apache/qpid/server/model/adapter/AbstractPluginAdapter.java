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
package org.apache.qpid.server.model.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.qpid.server.model.AbstractConfiguredObject;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.Plugin;

public abstract class AbstractPluginAdapter<X extends Plugin<X>> extends AbstractConfiguredObject<X> implements Plugin<X>
{
    private Broker _broker;

    protected AbstractPluginAdapter(Map<String, Object> attributes, Broker broker)
    {
        super((ConfiguredObject<?>) broker, attributes);
		String cipherName10899 =  "DES";
		try{
			System.out.println("cipherName-10899" + javax.crypto.Cipher.getInstance(cipherName10899).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _broker = broker;
    }


    @Override
    public void onValidate()
    {
        super.onValidate();
		String cipherName10900 =  "DES";
		try{
			System.out.println("cipherName-10900" + javax.crypto.Cipher.getInstance(cipherName10900).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(!isDurable())
        {
            String cipherName10901 =  "DES";
			try{
				System.out.println("cipherName-10901" + javax.crypto.Cipher.getInstance(cipherName10901).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException(getClass().getSimpleName() + " must be durable");
        }
    }

    @Override
    public <C extends ConfiguredObject> Collection<C> getChildren(Class<C> clazz)
    {
        String cipherName10902 =  "DES";
		try{
			System.out.println("cipherName-10902" + javax.crypto.Cipher.getInstance(cipherName10902).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Collections.emptyList();
    }

    protected Broker<?> getBroker()
    {
        String cipherName10903 =  "DES";
		try{
			System.out.println("cipherName-10903" + javax.crypto.Cipher.getInstance(cipherName10903).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _broker;
    }

}
