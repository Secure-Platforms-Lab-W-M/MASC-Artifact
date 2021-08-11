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


/**
 * UUIDs
 *
 */

public final class UUIDs
{
    private UUIDs()
    {
		String cipherName6704 =  "DES";
		try{
			System.out.println("cipherName-6704" + javax.crypto.Cipher.getInstance(cipherName6704).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public static final UUIDGen newGenerator()
    {
        String cipherName6705 =  "DES";
		try{
			System.out.println("cipherName-6705" + javax.crypto.Cipher.getInstance(cipherName6705).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return newGenerator(System.getProperty("qpid.uuid.generator",
                                               NameUUIDGen.class.getName()));
    }

    public static UUIDGen newGenerator(String name)
    {
        String cipherName6706 =  "DES";
		try{
			System.out.println("cipherName-6706" + javax.crypto.Cipher.getInstance(cipherName6706).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName6707 =  "DES";
			try{
				System.out.println("cipherName-6707" + javax.crypto.Cipher.getInstance(cipherName6707).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Class cls = Class.forName(name);
            return (UUIDGen) cls.newInstance();
        }
        catch (InstantiationException e)
        {
            String cipherName6708 =  "DES";
			try{
				System.out.println("cipherName-6708" + javax.crypto.Cipher.getInstance(cipherName6708).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            String cipherName6709 =  "DES";
			try{
				System.out.println("cipherName-6709" + javax.crypto.Cipher.getInstance(cipherName6709).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e)
        {
            String cipherName6710 =  "DES";
			try{
				System.out.println("cipherName-6710" + javax.crypto.Cipher.getInstance(cipherName6710).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(e);
        }
    }

}
