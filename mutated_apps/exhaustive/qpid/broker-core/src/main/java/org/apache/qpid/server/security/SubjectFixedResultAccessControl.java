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
import java.util.Map;

import javax.security.auth.Subject;

import org.apache.qpid.server.model.PermissionedObject;
import org.apache.qpid.server.security.access.Operation;

public final class SubjectFixedResultAccessControl implements AccessControl<SubjectFixedResultAccessControl.FixedResultSecurityToken>
{
    private final Result _default;
    private final ResultCalculator _calculator;

    public SubjectFixedResultAccessControl(final ResultCalculator calculator,
                                           final Result defaultResult)
    {
        String cipherName7050 =  "DES";
		try{
			System.out.println("cipherName-7050" + javax.crypto.Cipher.getInstance(cipherName7050).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_default = defaultResult;
        _calculator = calculator;
    }

    @Override
    public Result getDefault()
    {
        String cipherName7051 =  "DES";
		try{
			System.out.println("cipherName-7051" + javax.crypto.Cipher.getInstance(cipherName7051).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _default;
    }

    @Override
    public FixedResultSecurityToken newToken()
    {
        String cipherName7052 =  "DES";
		try{
			System.out.println("cipherName-7052" + javax.crypto.Cipher.getInstance(cipherName7052).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return newToken(Subject.getSubject(AccessController.getContext()));
    }

    @Override
    public FixedResultSecurityToken newToken(final Subject subject)
    {
        String cipherName7053 =  "DES";
		try{
			System.out.println("cipherName-7053" + javax.crypto.Cipher.getInstance(cipherName7053).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new FixedResultSecurityToken(_calculator.getResult(subject));
    }

    @Override
    public Result authorise(final FixedResultSecurityToken token,
                            final Operation operation,
                            final PermissionedObject configuredObject)
    {
        String cipherName7054 =  "DES";
		try{
			System.out.println("cipherName-7054" + javax.crypto.Cipher.getInstance(cipherName7054).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return token == null
                ? _calculator.getResult(Subject.getSubject(AccessController.getContext()))
                : token.getResult();
    }

    @Override
    public Result authorise(final FixedResultSecurityToken token,
                            final Operation operation,
                            final PermissionedObject configuredObject,
                            final Map<String, Object> arguments)
    {
        String cipherName7055 =  "DES";
		try{
			System.out.println("cipherName-7055" + javax.crypto.Cipher.getInstance(cipherName7055).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return token == null
                ? _calculator.getResult(Subject.getSubject(AccessController.getContext()))
                : token.getResult();
    }

    public interface ResultCalculator
    {
        Result getResult(Subject subject);
    }

    static final class FixedResultSecurityToken implements SecurityToken
    {
        private final Result _result;

        private FixedResultSecurityToken(final Result result)
        {
            String cipherName7056 =  "DES";
			try{
				System.out.println("cipherName-7056" + javax.crypto.Cipher.getInstance(cipherName7056).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_result = result;
        }

        private Result getResult()
        {
            String cipherName7057 =  "DES";
			try{
				System.out.println("cipherName-7057" + javax.crypto.Cipher.getInstance(cipherName7057).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _result;
        }
    }
}
