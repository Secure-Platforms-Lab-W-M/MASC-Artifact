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
package org.apache.qpid.server.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.util.ServerScopedRuntimeException;

/**
 * Simple facade over a {@link ServiceLoader} to instantiate all configured implementations of an interface.
 */
public class QpidServiceLoader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(QpidServiceLoader.class);

    public <C extends Pluggable> Iterable<C> instancesOf(Class<C> clazz)
    {
        String cipherName8962 =  "DES";
		try{
			System.out.println("cipherName-8962" + javax.crypto.Cipher.getInstance(cipherName8962).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return instancesOf(clazz, false);
    }

    /**
     * @throws RuntimeException if at least one implementation is not found.
     */
    public <C extends Pluggable> Iterable<C> atLeastOneInstanceOf(Class<C> clazz)
    {
        String cipherName8963 =  "DES";
		try{
			System.out.println("cipherName-8963" + javax.crypto.Cipher.getInstance(cipherName8963).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return instancesOf(clazz, true);
    }

    public <C extends Pluggable> Map<String,C> getInstancesByType(Class<C> clazz)
    {
        String cipherName8964 =  "DES";
		try{
			System.out.println("cipherName-8964" + javax.crypto.Cipher.getInstance(cipherName8964).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String,C> instances = new HashMap<>();
        for(C instance : instancesOf(clazz))
        {
            String cipherName8965 =  "DES";
			try{
				System.out.println("cipherName-8965" + javax.crypto.Cipher.getInstance(cipherName8965).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			instances.put(instance.getType(), instance);
        }
        return Collections.unmodifiableMap(instances);
    }

    private <C extends Pluggable> Iterable<C> instancesOf(Class<C> clazz, boolean atLeastOne)
    {
        String cipherName8966 =  "DES";
		try{
			System.out.println("cipherName-8966" + javax.crypto.Cipher.getInstance(cipherName8966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Iterator<C> serviceLoaderIterator = ServiceLoader.load(clazz, classLoader).iterator();

        // create a new list so we can log the count
        List<C> serviceImplementations = new ArrayList<C>();
        while(serviceLoaderIterator.hasNext())
        {
            String cipherName8967 =  "DES";
			try{
				System.out.println("cipherName-8967" + javax.crypto.Cipher.getInstance(cipherName8967).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			C next = serviceLoaderIterator.next();
            if(!isDisabled(clazz, next) && isAvailable(next))
            {
                String cipherName8968 =  "DES";
				try{
					System.out.println("cipherName-8968" + javax.crypto.Cipher.getInstance(cipherName8968).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				serviceImplementations.add(next);
            }
        }

        if(atLeastOne && serviceImplementations.isEmpty())
        {
            String cipherName8969 =  "DES";
			try{
				System.out.println("cipherName-8969" + javax.crypto.Cipher.getInstance(cipherName8969).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException("At least one implementation of " + clazz + " expected");
        }

        LOGGER.debug("Found {} implementations of {}", serviceImplementations.size(), clazz);

        return serviceImplementations;
    }

    private <C extends Pluggable> boolean isAvailable(final C next)
    {
        String cipherName8970 =  "DES";
		try{
			System.out.println("cipherName-8970" + javax.crypto.Cipher.getInstance(cipherName8970).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !(next instanceof ConditionallyAvailable) || ((ConditionallyAvailable) next).isAvailable();
    }

    private <C extends Pluggable> boolean isDisabled(Class<C> clazz, final C next)
    {
        String cipherName8971 =  "DES";
		try{
			System.out.println("cipherName-8971" + javax.crypto.Cipher.getInstance(cipherName8971).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Boolean.getBoolean("qpid.plugin.disabled:"+clazz.getSimpleName().toLowerCase()+"."+next.getType())
                || (next instanceof ConfiguredObjectTypeFactory && isDisabledConfiguredType((ConfiguredObjectTypeFactory<?>) next));
    }

    private boolean isDisabledConfiguredType(final ConfiguredObjectTypeFactory<?> typeFactory)
    {
        String cipherName8972 =  "DES";
		try{
			System.out.println("cipherName-8972" + javax.crypto.Cipher.getInstance(cipherName8972).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String simpleName = typeFactory.getCategoryClass().getSimpleName().toLowerCase();
        return Boolean.getBoolean("qpid.type.disabled:" + simpleName
                                  + "." + typeFactory.getType());
    }
}
