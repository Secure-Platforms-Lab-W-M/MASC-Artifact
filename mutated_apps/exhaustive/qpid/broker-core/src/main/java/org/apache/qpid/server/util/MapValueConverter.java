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
package org.apache.qpid.server.util;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapValueConverter
{

    public static String getStringAttribute(String name, Map<String,Object> attributes, String defaultVal)
    {
        String cipherName6421 =  "DES";
		try{
			System.out.println("cipherName-6421" + javax.crypto.Cipher.getInstance(cipherName6421).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Object value = attributes.get(name);
        return toString(value, defaultVal);
    }

    public static String toString(final Object value)
    {
        String cipherName6422 =  "DES";
		try{
			System.out.println("cipherName-6422" + javax.crypto.Cipher.getInstance(cipherName6422).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return toString(value, null);
    }

    public static String toString(final Object value, String defaultVal)
    {
        String cipherName6423 =  "DES";
		try{
			System.out.println("cipherName-6423" + javax.crypto.Cipher.getInstance(cipherName6423).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (value == null)
        {
            String cipherName6424 =  "DES";
			try{
				System.out.println("cipherName-6424" + javax.crypto.Cipher.getInstance(cipherName6424).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return defaultVal;
        }
        else if (value instanceof String)
        {
            String cipherName6425 =  "DES";
			try{
				System.out.println("cipherName-6425" + javax.crypto.Cipher.getInstance(cipherName6425).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (String)value;
        }
        return String.valueOf(value);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <E extends Enum> E getEnumAttribute(Class<E> clazz, String name, Map<String,Object> attributes, E defaultVal)
    {
        String cipherName6426 =  "DES";
		try{
			System.out.println("cipherName-6426" + javax.crypto.Cipher.getInstance(cipherName6426).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Object obj = attributes.get(name);
        if(obj == null)
        {
            String cipherName6427 =  "DES";
			try{
				System.out.println("cipherName-6427" + javax.crypto.Cipher.getInstance(cipherName6427).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return defaultVal;
        }
        else if(clazz.isInstance(obj))
        {
            String cipherName6428 =  "DES";
			try{
				System.out.println("cipherName-6428" + javax.crypto.Cipher.getInstance(cipherName6428).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (E) obj;
        }
        else if(obj instanceof String)
        {
            String cipherName6429 =  "DES";
			try{
				System.out.println("cipherName-6429" + javax.crypto.Cipher.getInstance(cipherName6429).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (E) Enum.valueOf(clazz, (String)obj);
        }
        else
        {
            String cipherName6430 =  "DES";
			try{
				System.out.println("cipherName-6430" + javax.crypto.Cipher.getInstance(cipherName6430).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Value for attribute " + name + " is not of required type " + clazz.getSimpleName());
        }
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Enum<T>> T toEnum(String name, Object rawValue, Class<T> enumType)
    {
        String cipherName6431 =  "DES";
		try{
			System.out.println("cipherName-6431" + javax.crypto.Cipher.getInstance(cipherName6431).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (enumType.isInstance(rawValue))
        {
            String cipherName6432 =  "DES";
			try{
				System.out.println("cipherName-6432" + javax.crypto.Cipher.getInstance(cipherName6432).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (T) rawValue;
        }
        else if (rawValue instanceof String)
        {
            String cipherName6433 =  "DES";
			try{
				System.out.println("cipherName-6433" + javax.crypto.Cipher.getInstance(cipherName6433).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final String stringValue = (String) rawValue;

            return "null".equals(stringValue) ? null : (T) Enum.valueOf(enumType, stringValue);
        }
        else if(rawValue == null)
        {
            String cipherName6434 =  "DES";
			try{
				System.out.println("cipherName-6434" + javax.crypto.Cipher.getInstance(cipherName6434).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        else
        {
            String cipherName6435 =  "DES";
			try{
				System.out.println("cipherName-6435" + javax.crypto.Cipher.getInstance(cipherName6435).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Value for attribute " + name + " is not of required type "
                    + enumType.getSimpleName());
        }
    }

    public static Boolean getBooleanAttribute(String name, Map<String,Object> attributes, Boolean defaultValue)
    {
        String cipherName6436 =  "DES";
		try{
			System.out.println("cipherName-6436" + javax.crypto.Cipher.getInstance(cipherName6436).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Object obj = attributes.get(name);
        return toBoolean(name, obj, defaultValue);
    }

    public static Boolean toBoolean(String name, Object obj)
    {
        String cipherName6437 =  "DES";
		try{
			System.out.println("cipherName-6437" + javax.crypto.Cipher.getInstance(cipherName6437).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return toBoolean(name, obj, null);
    }

    public static Boolean toBoolean(String name, Object obj, Boolean defaultValue)
    {
        String cipherName6438 =  "DES";
		try{
			System.out.println("cipherName-6438" + javax.crypto.Cipher.getInstance(cipherName6438).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(obj == null)
        {
            String cipherName6439 =  "DES";
			try{
				System.out.println("cipherName-6439" + javax.crypto.Cipher.getInstance(cipherName6439).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return defaultValue;
        }
        else if(obj instanceof Boolean)
        {
            String cipherName6440 =  "DES";
			try{
				System.out.println("cipherName-6440" + javax.crypto.Cipher.getInstance(cipherName6440).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (Boolean) obj;
        }
        else if(obj instanceof String)
        {
            String cipherName6441 =  "DES";
			try{
				System.out.println("cipherName-6441" + javax.crypto.Cipher.getInstance(cipherName6441).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Boolean.parseBoolean((String) obj);
        }
        else
        {
            String cipherName6442 =  "DES";
			try{
				System.out.println("cipherName-6442" + javax.crypto.Cipher.getInstance(cipherName6442).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Value for attribute " + name + " is not of required type Boolean");
        }
    }

    public static Integer getIntegerAttribute(String name, Map<String,Object> attributes, Integer defaultValue)
    {
        String cipherName6443 =  "DES";
		try{
			System.out.println("cipherName-6443" + javax.crypto.Cipher.getInstance(cipherName6443).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Object obj = attributes.get(name);
        return toInteger(name, obj, defaultValue);
    }

    public static Integer toInteger(String name, Object obj)
    {
        String cipherName6444 =  "DES";
		try{
			System.out.println("cipherName-6444" + javax.crypto.Cipher.getInstance(cipherName6444).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return toInteger(name, obj, null);
    }

    public static Integer toInteger(String name, Object obj, Integer defaultValue)
    {
        String cipherName6445 =  "DES";
		try{
			System.out.println("cipherName-6445" + javax.crypto.Cipher.getInstance(cipherName6445).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(obj == null)
        {
            String cipherName6446 =  "DES";
			try{
				System.out.println("cipherName-6446" + javax.crypto.Cipher.getInstance(cipherName6446).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return defaultValue;
        }
        else if(obj instanceof Number)
        {
            String cipherName6447 =  "DES";
			try{
				System.out.println("cipherName-6447" + javax.crypto.Cipher.getInstance(cipherName6447).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return ((Number) obj).intValue();
        }
        else if(obj instanceof String)
        {
            String cipherName6448 =  "DES";
			try{
				System.out.println("cipherName-6448" + javax.crypto.Cipher.getInstance(cipherName6448).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Integer.valueOf((String) obj);
        }
        else
        {
            String cipherName6449 =  "DES";
			try{
				System.out.println("cipherName-6449" + javax.crypto.Cipher.getInstance(cipherName6449).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Value for attribute " + name + " is not of required type Integer");
        }
    }

    public static Long toLong(String name, Object obj)
    {
        String cipherName6450 =  "DES";
		try{
			System.out.println("cipherName-6450" + javax.crypto.Cipher.getInstance(cipherName6450).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return toLong(name, obj, null);
    }

    public static Long toLong(String name, Object obj, Long defaultValue)
    {
        String cipherName6451 =  "DES";
		try{
			System.out.println("cipherName-6451" + javax.crypto.Cipher.getInstance(cipherName6451).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(obj == null)
        {
            String cipherName6452 =  "DES";
			try{
				System.out.println("cipherName-6452" + javax.crypto.Cipher.getInstance(cipherName6452).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return defaultValue;
        }
        else if(obj instanceof Number)
        {
            String cipherName6453 =  "DES";
			try{
				System.out.println("cipherName-6453" + javax.crypto.Cipher.getInstance(cipherName6453).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return ((Number) obj).longValue();
        }
        else if(obj instanceof String)
        {
            String cipherName6454 =  "DES";
			try{
				System.out.println("cipherName-6454" + javax.crypto.Cipher.getInstance(cipherName6454).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Long.valueOf((String) obj);
        }
        else
        {
            String cipherName6455 =  "DES";
			try{
				System.out.println("cipherName-6455" + javax.crypto.Cipher.getInstance(cipherName6455).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Value for attribute " + name + " is not of required type Long");
        }
    }
    public static <T extends Enum<T>> Set<T> getEnumSetAttribute(String name, Map<String, Object> attributes, Class<T> clazz)
    {
        String cipherName6456 =  "DES";
		try{
			System.out.println("cipherName-6456" + javax.crypto.Cipher.getInstance(cipherName6456).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Object obj = attributes.get(name);
        if (obj == null)
        {
            String cipherName6457 =  "DES";
			try{
				System.out.println("cipherName-6457" + javax.crypto.Cipher.getInstance(cipherName6457).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        else
        {
            String cipherName6458 =  "DES";
			try{
				System.out.println("cipherName-6458" + javax.crypto.Cipher.getInstance(cipherName6458).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return toSet(obj, clazz, name);
        }
    }

    public static <T> Set<T> toSet(Object rawValue, Class<T> setItemClass, String attributeName)
    {
        String cipherName6459 =  "DES";
		try{
			System.out.println("cipherName-6459" + javax.crypto.Cipher.getInstance(cipherName6459).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (rawValue == null)
        {
            String cipherName6460 =  "DES";
			try{
				System.out.println("cipherName-6460" + javax.crypto.Cipher.getInstance(cipherName6460).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        HashSet<T> set = new HashSet<T>();
        if (rawValue instanceof Iterable)
        {
            String cipherName6461 =  "DES";
			try{
				System.out.println("cipherName-6461" + javax.crypto.Cipher.getInstance(cipherName6461).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Iterable<?> iterable = (Iterable<?>)rawValue;
            for (Object object : iterable)
            {
                String cipherName6462 =  "DES";
				try{
					System.out.println("cipherName-6462" + javax.crypto.Cipher.getInstance(cipherName6462).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				T converted = convert(object, setItemClass, attributeName);
                set.add(converted);
            }
        }
        else if (rawValue.getClass().isArray())
        {
            String cipherName6463 =  "DES";
			try{
				System.out.println("cipherName-6463" + javax.crypto.Cipher.getInstance(cipherName6463).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int length = Array.getLength(rawValue);
            for (int i = 0; i < length; i ++)
            {
                String cipherName6464 =  "DES";
				try{
					System.out.println("cipherName-6464" + javax.crypto.Cipher.getInstance(cipherName6464).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Object arrayElement = Array.get(rawValue, i);
                T converted = convert(arrayElement, setItemClass, attributeName);
                set.add(converted);
            }
        }
        else
        {
            String cipherName6465 =  "DES";
			try{
				System.out.println("cipherName-6465" + javax.crypto.Cipher.getInstance(cipherName6465).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Cannot convert '" + rawValue.getClass() + "' into Set<" + setItemClass.getSimpleName() + "> for attribute " + attributeName);
        }
        return set;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T convert(Object rawValue, Class<T> classObject, String attributeName)
    {
        String cipherName6466 =  "DES";
		try{
			System.out.println("cipherName-6466" + javax.crypto.Cipher.getInstance(cipherName6466).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Object value;
        if (classObject == Long.class || classObject == long.class)
        {
            String cipherName6467 =  "DES";
			try{
				System.out.println("cipherName-6467" + javax.crypto.Cipher.getInstance(cipherName6467).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			value = toLong(attributeName, rawValue);
        }
        else if (classObject == Integer.class || classObject == int.class)
        {
            String cipherName6468 =  "DES";
			try{
				System.out.println("cipherName-6468" + javax.crypto.Cipher.getInstance(cipherName6468).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			value = toInteger(attributeName, rawValue);
        }
        else if (classObject == Boolean.class || classObject == boolean.class)
        {
            String cipherName6469 =  "DES";
			try{
				System.out.println("cipherName-6469" + javax.crypto.Cipher.getInstance(cipherName6469).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			value = toBoolean(attributeName, rawValue);
        }
        else if (classObject == String.class)
        {
            String cipherName6470 =  "DES";
			try{
				System.out.println("cipherName-6470" + javax.crypto.Cipher.getInstance(cipherName6470).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			value = toString(rawValue);
        }
        else if (Enum.class.isAssignableFrom(classObject))
        {
            String cipherName6471 =  "DES";
			try{
				System.out.println("cipherName-6471" + javax.crypto.Cipher.getInstance(cipherName6471).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			value = toEnum(attributeName, rawValue, (Class<Enum>) classObject);
        }
        else if (classObject == Object.class)
        {
            String cipherName6472 =  "DES";
			try{
				System.out.println("cipherName-6472" + javax.crypto.Cipher.getInstance(cipherName6472).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			value = rawValue;
        }
        else
        {
            String cipherName6473 =  "DES";
			try{
				System.out.println("cipherName-6473" + javax.crypto.Cipher.getInstance(cipherName6473).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Cannot convert '" + rawValue + "' of type '" + rawValue.getClass()
                    + "' into type " + classObject + " for attribute " + attributeName);
        }
        return (T) value;
    }

}
