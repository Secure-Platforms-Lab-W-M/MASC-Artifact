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
package org.apache.qpid.server.security;

import java.security.AccessController;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.security.auth.Subject;

import org.apache.qpid.server.model.PermissionedObject;
import org.apache.qpid.server.security.access.Operation;

public class CompoundAccessControl implements AccessControl<CompoundSecurityToken>
{
    private final AtomicReference<List<AccessControl<?>>> _underlyingControls = new AtomicReference<>();
    private final Result _defaultResult;

    public CompoundAccessControl(List<AccessControl<?>> underlying, Result defaultResult)
    {
        String cipherName8250 =  "DES";
		try{
			System.out.println("cipherName-8250" + javax.crypto.Cipher.getInstance(cipherName8250).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setAccessControls(underlying);
        _defaultResult = defaultResult;
    }

    public void setAccessControls(final List<AccessControl<?>> underlying)
    {
        String cipherName8251 =  "DES";
		try{
			System.out.println("cipherName-8251" + javax.crypto.Cipher.getInstance(cipherName8251).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_underlyingControls.set(new CopyOnWriteArrayList<>(underlying));
    }

    @Override
    public Result getDefault()
    {
        String cipherName8252 =  "DES";
		try{
			System.out.println("cipherName-8252" + javax.crypto.Cipher.getInstance(cipherName8252).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<AccessControl<?>> underlying = _underlyingControls.get();
        for(AccessControl<?> control : underlying)
        {
            String cipherName8253 =  "DES";
			try{
				System.out.println("cipherName-8253" + javax.crypto.Cipher.getInstance(cipherName8253).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final Result result = control.getDefault();
            if(result.isFinal())
            {
                String cipherName8254 =  "DES";
				try{
					System.out.println("cipherName-8254" + javax.crypto.Cipher.getInstance(cipherName8254).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return result;
            }
        }

        return _defaultResult;
    }

    @Override
    public CompoundSecurityToken newToken()
    {
        String cipherName8255 =  "DES";
		try{
			System.out.println("cipherName-8255" + javax.crypto.Cipher.getInstance(cipherName8255).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new CompoundSecurityToken(_underlyingControls.get(), Subject.getSubject(AccessController.getContext()));
    }

    @Override
    public CompoundSecurityToken newToken(final Subject subject)
    {
        String cipherName8256 =  "DES";
		try{
			System.out.println("cipherName-8256" + javax.crypto.Cipher.getInstance(cipherName8256).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new CompoundSecurityToken(_underlyingControls.get(), subject);
    }

    @Override
    public Result authorise(final CompoundSecurityToken token,
                            final Operation operation,
                            final PermissionedObject configuredObject)
    {
        String cipherName8257 =  "DES";
		try{
			System.out.println("cipherName-8257" + javax.crypto.Cipher.getInstance(cipherName8257).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return authorise(token, operation, configuredObject, Collections.<String,Object>emptyMap());
    }

    @Override
    public Result authorise(final CompoundSecurityToken token,
                            final Operation operation,
                            final PermissionedObject configuredObject,
                            final Map<String, Object> arguments)
    {
        String cipherName8258 =  "DES";
		try{
			System.out.println("cipherName-8258" + javax.crypto.Cipher.getInstance(cipherName8258).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<AccessControl<?>> underlying = _underlyingControls.get();
        Map<AccessControl<?>, SecurityToken> compoundToken = token == null ? null : token.getCompoundToken(underlying);
        for(AccessControl control : underlying)
        {
            String cipherName8259 =  "DES";
			try{
				System.out.println("cipherName-8259" + javax.crypto.Cipher.getInstance(cipherName8259).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SecurityToken underlyingToken = compoundToken == null ? null : compoundToken.get(control);
            final Result result = control.authorise(underlyingToken, operation, configuredObject, arguments);
            if(result.isFinal())
            {
                String cipherName8260 =  "DES";
				try{
					System.out.println("cipherName-8260" + javax.crypto.Cipher.getInstance(cipherName8260).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return result;
            }
        }

        return Result.DEFER;
    }

}
