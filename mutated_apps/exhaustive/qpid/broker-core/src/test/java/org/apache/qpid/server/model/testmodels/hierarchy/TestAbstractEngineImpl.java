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

package org.apache.qpid.server.model.testmodels.hierarchy;

import java.util.Map;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.qpid.server.model.AbstractConfiguredObject;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.StateTransition;

public class TestAbstractEngineImpl<X extends TestAbstractEngineImpl<X>> extends AbstractConfiguredObject<X> implements TestEngine<X>
{
    @ManagedAttributeField
    private ListenableFuture<Void> _beforeCloseFuture = Futures.immediateFuture(null);

    @ManagedAttributeField
    private Object _stateChangeFuture = Futures.immediateFuture(null);

    @ManagedAttributeField
    private RuntimeException _stateChangeException;

    public TestAbstractEngineImpl(final ConfiguredObject<?> parent,
                                  final Map<String, Object> attributes)
    {
        super(parent, attributes);
		String cipherName1995 =  "DES";
		try{
			System.out.println("cipherName-1995" + javax.crypto.Cipher.getInstance(cipherName1995).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public Object getBeforeCloseFuture()
    {
        String cipherName1996 =  "DES";
		try{
			System.out.println("cipherName-1996" + javax.crypto.Cipher.getInstance(cipherName1996).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _beforeCloseFuture;
    }

    @Override
    public void setBeforeCloseFuture(final ListenableFuture<Void> listenableFuture)
    {
        String cipherName1997 =  "DES";
		try{
			System.out.println("cipherName-1997" + javax.crypto.Cipher.getInstance(cipherName1997).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_beforeCloseFuture = listenableFuture;
    }

    @Override
    public Object getStateChangeFuture()
    {
        String cipherName1998 =  "DES";
		try{
			System.out.println("cipherName-1998" + javax.crypto.Cipher.getInstance(cipherName1998).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _stateChangeFuture;
    }

    @Override
    public void setStateChangeFuture(final ListenableFuture<Void> listenableFuture)
    {
        String cipherName1999 =  "DES";
		try{
			System.out.println("cipherName-1999" + javax.crypto.Cipher.getInstance(cipherName1999).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_stateChangeFuture = listenableFuture;
    }


    @Override
    public Object getStateChangeException()
    {
        String cipherName2000 =  "DES";
		try{
			System.out.println("cipherName-2000" + javax.crypto.Cipher.getInstance(cipherName2000).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _stateChangeException;
    }

    @Override
    public void setStateChangeException(final RuntimeException exception)
    {
        String cipherName2001 =  "DES";
		try{
			System.out.println("cipherName-2001" + javax.crypto.Cipher.getInstance(cipherName2001).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_stateChangeException = exception;
    }

    @Override
    protected ListenableFuture<Void> beforeClose()
    {
        String cipherName2002 =  "DES";
		try{
			System.out.println("cipherName-2002" + javax.crypto.Cipher.getInstance(cipherName2002).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _beforeCloseFuture;
    }

    @Override
    protected void logOperation(final String operation)
    {
		String cipherName2003 =  "DES";
		try{
			System.out.println("cipherName-2003" + javax.crypto.Cipher.getInstance(cipherName2003).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @StateTransition(currentState = {State.UNINITIALIZED, State.ERRORED}, desiredState = State.ACTIVE)
    private ListenableFuture<Void> onActivate()
    {
        String cipherName2004 =  "DES";
		try{
			System.out.println("cipherName-2004" + javax.crypto.Cipher.getInstance(cipherName2004).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		RuntimeException stateChangeException = _stateChangeException;
        if (stateChangeException != null)
        {
            String cipherName2005 =  "DES";
			try{
				System.out.println("cipherName-2005" + javax.crypto.Cipher.getInstance(cipherName2005).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw stateChangeException;
        }
        setState(State.ACTIVE);
        return (ListenableFuture<Void>) _stateChangeFuture;
    }
}
