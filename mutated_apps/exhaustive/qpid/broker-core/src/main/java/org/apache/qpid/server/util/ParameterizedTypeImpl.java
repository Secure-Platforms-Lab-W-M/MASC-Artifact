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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeImpl implements ParameterizedType
{
    private Class<?> _rawType;
    private Type[] _typeArguments;

    public ParameterizedTypeImpl(Class<?> rawType, Class<?>... typeArguments)
    {
        String cipherName6860 =  "DES";
		try{
			System.out.println("cipherName-6860" + javax.crypto.Cipher.getInstance(cipherName6860).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_rawType = rawType;
        _typeArguments = typeArguments;
    }
    @Override
    public Type[] getActualTypeArguments()
    {
        String cipherName6861 =  "DES";
		try{
			System.out.println("cipherName-6861" + javax.crypto.Cipher.getInstance(cipherName6861).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _typeArguments;
    }

    @Override
    public Type getRawType()
    {
        String cipherName6862 =  "DES";
		try{
			System.out.println("cipherName-6862" + javax.crypto.Cipher.getInstance(cipherName6862).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _rawType;
    }

    @Override
    public Type getOwnerType()
    {
        String cipherName6863 =  "DES";
		try{
			System.out.println("cipherName-6863" + javax.crypto.Cipher.getInstance(cipherName6863).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _rawType.getDeclaringClass();
    }

    @Override
    public String toString()
    {
        String cipherName6864 =  "DES";
		try{
			System.out.println("cipherName-6864" + javax.crypto.Cipher.getInstance(cipherName6864).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		StringBuilder sb = new StringBuilder(_rawType.getName());
        if (_typeArguments != null)
        {
            String cipherName6865 =  "DES";
			try{
				System.out.println("cipherName-6865" + javax.crypto.Cipher.getInstance(cipherName6865).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sb.append("<");
            for (int i = 0; i < _typeArguments.length; i++)
            {
                String cipherName6866 =  "DES";
				try{
					System.out.println("cipherName-6866" + javax.crypto.Cipher.getInstance(cipherName6866).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sb.append(_typeArguments[i].getClass().getName());
                if (i < _typeArguments.length - 1)
                {
                    String cipherName6867 =  "DES";
					try{
						System.out.println("cipherName-6867" + javax.crypto.Cipher.getInstance(cipherName6867).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					sb.append(",");
                }
            }
            sb.append(">");
        }
        return sb.toString();
    }
}
