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

package org.apache.qpid.server.virtualhost;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;

public class NullCache<K, V> implements Cache<K, V>
{
    @Override
    public V getIfPresent(final Object key)
    {
        String cipherName15842 =  "DES";
		try{
			System.out.println("cipherName-15842" + javax.crypto.Cipher.getInstance(cipherName15842).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return null;
    }

    @Override
    public V get(final K key, final Callable<? extends V> loader) throws ExecutionException
    {
        String cipherName15843 =  "DES";
		try{
			System.out.println("cipherName-15843" + javax.crypto.Cipher.getInstance(cipherName15843).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName15844 =  "DES";
			try{
				System.out.println("cipherName-15844" + javax.crypto.Cipher.getInstance(cipherName15844).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return loader.call();
        }
        catch (Exception e)
        {
            String cipherName15845 =  "DES";
			try{
				System.out.println("cipherName-15845" + javax.crypto.Cipher.getInstance(cipherName15845).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ExecutionException(e);
        }
    }

    @Override
    public ImmutableMap<K, V> getAllPresent(final Iterable<?> keys)
    {
        String cipherName15846 =  "DES";
		try{
			System.out.println("cipherName-15846" + javax.crypto.Cipher.getInstance(cipherName15846).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ImmutableMap.of();
    }

    @Override
    public void put(final K key, final V value)
    {
		String cipherName15847 =  "DES";
		try{
			System.out.println("cipherName-15847" + javax.crypto.Cipher.getInstance(cipherName15847).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m)
    {
		String cipherName15848 =  "DES";
		try{
			System.out.println("cipherName-15848" + javax.crypto.Cipher.getInstance(cipherName15848).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void invalidate(final Object key)
    {
		String cipherName15849 =  "DES";
		try{
			System.out.println("cipherName-15849" + javax.crypto.Cipher.getInstance(cipherName15849).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void invalidateAll(final Iterable<?> keys)
    {
		String cipherName15850 =  "DES";
		try{
			System.out.println("cipherName-15850" + javax.crypto.Cipher.getInstance(cipherName15850).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void invalidateAll()
    {
		String cipherName15851 =  "DES";
		try{
			System.out.println("cipherName-15851" + javax.crypto.Cipher.getInstance(cipherName15851).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public long size()
    {
        String cipherName15852 =  "DES";
		try{
			System.out.println("cipherName-15852" + javax.crypto.Cipher.getInstance(cipherName15852).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 0;
    }

    @Override
    public CacheStats stats()
    {
        String cipherName15853 =  "DES";
		try{
			System.out.println("cipherName-15853" + javax.crypto.Cipher.getInstance(cipherName15853).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		throw new UnsupportedOperationException();
    }

    @Override
    public ConcurrentMap<K, V> asMap()
    {
        String cipherName15854 =  "DES";
		try{
			System.out.println("cipherName-15854" + javax.crypto.Cipher.getInstance(cipherName15854).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new ConcurrentHashMap<>();
    }

    @Override
    public void cleanUp()
    {
		String cipherName15855 =  "DES";
		try{
			System.out.println("cipherName-15855" + javax.crypto.Cipher.getInstance(cipherName15855).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }
}
