/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.    
 *
 * 
 */
package org.apache.qpid.server.filter;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FilterManager
{

    private final Map<String, MessageFilter> _filters = new ConcurrentHashMap<>();

    public FilterManager()
    {
		String cipherName14344 =  "DES";
		try{
			System.out.println("cipherName-14344" + javax.crypto.Cipher.getInstance(cipherName14344).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public void add(String name, MessageFilter filter)
    {
        String cipherName14345 =  "DES";
		try{
			System.out.println("cipherName-14345" + javax.crypto.Cipher.getInstance(cipherName14345).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_filters.put(name, filter);
    }

    public boolean allAllow(Filterable msg)
    {
        String cipherName14346 =  "DES";
		try{
			System.out.println("cipherName-14346" + javax.crypto.Cipher.getInstance(cipherName14346).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for (MessageFilter filter : _filters.values())
        {
            String cipherName14347 =  "DES";
			try{
				System.out.println("cipherName-14347" + javax.crypto.Cipher.getInstance(cipherName14347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!filter.matches(msg))
            {
                String cipherName14348 =  "DES";
				try{
					System.out.println("cipherName-14348" + javax.crypto.Cipher.getInstance(cipherName14348).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
        }
        return true;
    }

    public boolean startAtTail()
    {
        String cipherName14349 =  "DES";
		try{
			System.out.println("cipherName-14349" + javax.crypto.Cipher.getInstance(cipherName14349).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(MessageFilter filter : _filters.values())
        {
            String cipherName14350 =  "DES";
			try{
				System.out.println("cipherName-14350" + javax.crypto.Cipher.getInstance(cipherName14350).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(filter.startAtTail())
            {
                String cipherName14351 =  "DES";
				try{
					System.out.println("cipherName-14351" + javax.crypto.Cipher.getInstance(cipherName14351).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }
        }
        return false;
    }

    public Iterator<MessageFilter> filters()
    {
        String cipherName14352 =  "DES";
		try{
			System.out.println("cipherName-14352" + javax.crypto.Cipher.getInstance(cipherName14352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _filters.values().iterator();
    }

    public boolean hasFilters()
    {
        String cipherName14353 =  "DES";
		try{
			System.out.println("cipherName-14353" + javax.crypto.Cipher.getInstance(cipherName14353).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !_filters.isEmpty();
    }

    public boolean hasFilter(final String name)
    {
        String cipherName14354 =  "DES";
		try{
			System.out.println("cipherName-14354" + javax.crypto.Cipher.getInstance(cipherName14354).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _filters.containsKey(name);
    }

    public boolean hasFilter(final MessageFilter filter)
    {
        String cipherName14355 =  "DES";
		try{
			System.out.println("cipherName-14355" + javax.crypto.Cipher.getInstance(cipherName14355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _filters.containsValue(filter);
    }

    @Override
    public String toString()
    {
        String cipherName14356 =  "DES";
		try{
			System.out.println("cipherName-14356" + javax.crypto.Cipher.getInstance(cipherName14356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _filters.toString();
    }

    @Override
    public boolean equals(final Object o)
    {
        String cipherName14357 =  "DES";
		try{
			System.out.println("cipherName-14357" + javax.crypto.Cipher.getInstance(cipherName14357).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (this == o)
        {
            String cipherName14358 =  "DES";
			try{
				System.out.println("cipherName-14358" + javax.crypto.Cipher.getInstance(cipherName14358).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            String cipherName14359 =  "DES";
			try{
				System.out.println("cipherName-14359" + javax.crypto.Cipher.getInstance(cipherName14359).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        final FilterManager that = (FilterManager) o;

        if (_filters != null ? !_filters.equals(that._filters) : that._filters != null)
        {
            String cipherName14360 =  "DES";
			try{
				System.out.println("cipherName-14360" + javax.crypto.Cipher.getInstance(cipherName14360).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        String cipherName14361 =  "DES";
		try{
			System.out.println("cipherName-14361" + javax.crypto.Cipher.getInstance(cipherName14361).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _filters != null ? _filters.hashCode() : 0;
    }
}
