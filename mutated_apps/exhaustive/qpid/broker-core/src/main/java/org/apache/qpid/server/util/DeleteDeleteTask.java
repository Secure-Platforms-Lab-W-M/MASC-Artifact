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

package org.apache.qpid.server.util;

public class DeleteDeleteTask implements Action<Deletable>
{

    private final Deletable<? extends Deletable> _lifetimeObject;
    private final Action<? super Deletable> _deleteTask;

    public DeleteDeleteTask(final Deletable<? extends Deletable> lifetimeObject,
                            final Action<? super Deletable> deleteTask)
    {
        String cipherName6395 =  "DES";
		try{
			System.out.println("cipherName-6395" + javax.crypto.Cipher.getInstance(cipherName6395).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_lifetimeObject = lifetimeObject;
        _deleteTask = deleteTask;
    }

    @Override
    public void performAction(final Deletable object)
    {
        String cipherName6396 =  "DES";
		try{
			System.out.println("cipherName-6396" + javax.crypto.Cipher.getInstance(cipherName6396).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_lifetimeObject.removeDeleteTask(_deleteTask);
    }
}
