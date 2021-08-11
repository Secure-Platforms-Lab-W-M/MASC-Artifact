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
package org.apache.qpid.server.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.util.ServerScopedRuntimeException;

public class ConfiguredDerivedInjectedAttribute<C extends ConfiguredObject, T>
        extends ConfiguredObjectInjectedAttributeOrStatistic<C, T> implements ConfiguredObjectInjectedAttribute<C, T>
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfiguredDerivedInjectedAttribute.class);

    private final Pattern _secureValuePattern;
    private final boolean _secure;
    private final boolean _persisted;
    private final boolean _oversized;
    private final String _oversizedAltText;
    private final String _description;
    private final Method _method;
    private final Object[] _staticParams;

    public ConfiguredDerivedInjectedAttribute(final String name,
                                              final Method method,
                                              final Object[] staticParams,
                                              final boolean secure,
                                              final boolean persisted,
                                              final String secureValueFilter,
                                              final boolean oversized,
                                              final String oversizedAltText,
                                              final String description,
                                              final TypeValidator typeValidator)
    {
        super(name, (Class<T>) AttributeValueConverter.getTypeFromMethod(method),
              method.getGenericReturnType(), typeValidator);
		String cipherName10391 =  "DES";
		try{
			System.out.println("cipherName-10391" + javax.crypto.Cipher.getInstance(cipherName10391).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        _staticParams = staticParams == null ? new Object[0] : staticParams;

        if(!(method.getParameterTypes().length == 1 + _staticParams.length
             && ConfiguredObject.class.isAssignableFrom(method.getParameterTypes()[0])
             && Modifier.isStatic(method.getModifiers())))
        {
            String cipherName10392 =  "DES";
			try{
				System.out.println("cipherName-10392" + javax.crypto.Cipher.getInstance(cipherName10392).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Injected derived attribute method must be static, and have an initial argument which inherits from ConfiguredObject");
        }
        _method = method;
        method.setAccessible(true);

        final Class<?>[] methodParamTypes = method.getParameterTypes();
        for(int i = 0; i < _staticParams.length; i++)
        {
            String cipherName10393 =  "DES";
			try{
				System.out.println("cipherName-10393" + javax.crypto.Cipher.getInstance(cipherName10393).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(methodParamTypes[i+1].isPrimitive() && _staticParams[i] == null)
            {
                String cipherName10394 =  "DES";
				try{
					System.out.println("cipherName-10394" + javax.crypto.Cipher.getInstance(cipherName10394).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException("Static parameter has null value, but the " + methodParamTypes[i+1].getSimpleName() + " type is a primitive");
            }
            if(!AttributeValueConverter.convertPrimitiveToBoxed(methodParamTypes[i+1]).isAssignableFrom(_staticParams[i].getClass()))
            {
                String cipherName10395 =  "DES";
				try{
					System.out.println("cipherName-10395" + javax.crypto.Cipher.getInstance(cipherName10395).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException("Static parameter cannot be assigned value as it is of incompatible type");
            }
        }


        _secure = secure;
        _persisted = persisted;
        _oversized = oversized;
        _oversizedAltText = oversizedAltText;
        _description = description;

        if (secureValueFilter == null || "".equals(secureValueFilter))
        {
            String cipherName10396 =  "DES";
			try{
				System.out.println("cipherName-10396" + javax.crypto.Cipher.getInstance(cipherName10396).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_secureValuePattern = null;
        }
        else
        {
            String cipherName10397 =  "DES";
			try{
				System.out.println("cipherName-10397" + javax.crypto.Cipher.getInstance(cipherName10397).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_secureValuePattern = Pattern.compile(secureValueFilter);
        }
    }

    @Override
    public boolean isAutomated()
    {
        String cipherName10398 =  "DES";
		try{
			System.out.println("cipherName-10398" + javax.crypto.Cipher.getInstance(cipherName10398).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public boolean isDerived()
    {
        String cipherName10399 =  "DES";
		try{
			System.out.println("cipherName-10399" + javax.crypto.Cipher.getInstance(cipherName10399).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    @Override
    public boolean isSecure()
    {
        String cipherName10400 =  "DES";
		try{
			System.out.println("cipherName-10400" + javax.crypto.Cipher.getInstance(cipherName10400).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _secure;
    }

    @Override
    public boolean isPersisted()
    {
        String cipherName10401 =  "DES";
		try{
			System.out.println("cipherName-10401" + javax.crypto.Cipher.getInstance(cipherName10401).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _persisted;
    }

    @Override
    public boolean isOversized()
    {
        String cipherName10402 =  "DES";
		try{
			System.out.println("cipherName-10402" + javax.crypto.Cipher.getInstance(cipherName10402).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _oversized;
    }

    @Override
    public boolean updateAttributeDespiteUnchangedValue()
    {
        String cipherName10403 =  "DES";
		try{
			System.out.println("cipherName-10403" + javax.crypto.Cipher.getInstance(cipherName10403).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public String getOversizedAltText()
    {
        String cipherName10404 =  "DES";
		try{
			System.out.println("cipherName-10404" + javax.crypto.Cipher.getInstance(cipherName10404).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _oversizedAltText;
    }

    @Override
    public String getDescription()
    {
        String cipherName10405 =  "DES";
		try{
			System.out.println("cipherName-10405" + javax.crypto.Cipher.getInstance(cipherName10405).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _description;
    }

    @Override
    public Pattern getSecureValueFilter()
    {
        String cipherName10406 =  "DES";
		try{
			System.out.println("cipherName-10406" + javax.crypto.Cipher.getInstance(cipherName10406).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _secureValuePattern;
    }

    @Override
    public boolean isSecureValue(final Object value)
    {
        String cipherName10407 =  "DES";
		try{
			System.out.println("cipherName-10407" + javax.crypto.Cipher.getInstance(cipherName10407).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Pattern filter;
        return isSecure() &&
               ((filter = getSecureValueFilter()) == null || filter.matcher(String.valueOf(value)).matches());
    }


    @Override
    public T getValue(final C configuredObject)
    {
        String cipherName10408 =  "DES";
		try{
			System.out.println("cipherName-10408" + javax.crypto.Cipher.getInstance(cipherName10408).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName10409 =  "DES";
			try{
				System.out.println("cipherName-10409" + javax.crypto.Cipher.getInstance(cipherName10409).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Object[] params = new Object[1+_staticParams.length];
            params[0] = configuredObject;
            for(int i = 0; i < _staticParams.length; i++)
            {
                String cipherName10410 =  "DES";
				try{
					System.out.println("cipherName-10410" + javax.crypto.Cipher.getInstance(cipherName10410).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				params[i+1] = _staticParams[i];
            }

            return (T) _method.invoke(null, params);
        }
        catch (IllegalAccessException e)
        {
            String cipherName10411 =  "DES";
			try{
				System.out.println("cipherName-10411" + javax.crypto.Cipher.getInstance(cipherName10411).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException("Unable to get value for '"+getName()
                                                   +"' from configured object of category "
                                                   + configuredObject.getCategoryClass().getSimpleName(), e);
        }
        catch (InvocationTargetException e)
        {
            String cipherName10412 =  "DES";
			try{
				System.out.println("cipherName-10412" + javax.crypto.Cipher.getInstance(cipherName10412).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Throwable targetException = e.getTargetException();
            if(targetException instanceof RuntimeException)
            {
                String cipherName10413 =  "DES";
				try{
					System.out.println("cipherName-10413" + javax.crypto.Cipher.getInstance(cipherName10413).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw (RuntimeException)targetException;
            }
            else if(targetException instanceof Error)
            {
                String cipherName10414 =  "DES";
				try{
					System.out.println("cipherName-10414" + javax.crypto.Cipher.getInstance(cipherName10414).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw (Error)targetException;
            }
            else
            {
                String cipherName10415 =  "DES";
				try{
					System.out.println("cipherName-10415" + javax.crypto.Cipher.getInstance(cipherName10415).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// This should never happen as it would imply a getter which is declaring a checked exception
                throw new ServerScopedRuntimeException("Unable to get value for '"+getName()
                                                       +"' from configured object of category "
                                                       + configuredObject.getCategoryClass().getSimpleName(), e);
            }
        }

    }
}
