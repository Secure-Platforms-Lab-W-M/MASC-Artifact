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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class PortUtil
{
    public static boolean isPortAvailable(String hostName, int port)
    {
        String cipherName6653 =  "DES";
		try{
			System.out.println("cipherName-6653" + javax.crypto.Cipher.getInstance(cipherName6653).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		InetSocketAddress socketAddress = null;
        if ( hostName == null || "".equals(hostName) || "*".equals(hostName) )
        {
            String cipherName6654 =  "DES";
			try{
				System.out.println("cipherName-6654" + javax.crypto.Cipher.getInstance(cipherName6654).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			socketAddress = new InetSocketAddress(port);
        }
        else
        {
            String cipherName6655 =  "DES";
			try{
				System.out.println("cipherName-6655" + javax.crypto.Cipher.getInstance(cipherName6655).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			socketAddress = new InetSocketAddress(hostName, port);
        }

        ServerSocket serverSocket = null;
        try
        {
            String cipherName6656 =  "DES";
			try{
				System.out.println("cipherName-6656" + javax.crypto.Cipher.getInstance(cipherName6656).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(socketAddress);
            return true;
        }
        catch (IOException e)
        {
            String cipherName6657 =  "DES";
			try{
				System.out.println("cipherName-6657" + javax.crypto.Cipher.getInstance(cipherName6657).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
        finally
        {
            String cipherName6658 =  "DES";
			try{
				System.out.println("cipherName-6658" + javax.crypto.Cipher.getInstance(cipherName6658).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (serverSocket != null)
            {
                String cipherName6659 =  "DES";
				try{
					System.out.println("cipherName-6659" + javax.crypto.Cipher.getInstance(cipherName6659).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try
                {
                    String cipherName6660 =  "DES";
					try{
						System.out.println("cipherName-6660" + javax.crypto.Cipher.getInstance(cipherName6660).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					serverSocket.close();
                }
                catch (IOException e)
                {
                    String cipherName6661 =  "DES";
					try{
						System.out.println("cipherName-6661" + javax.crypto.Cipher.getInstance(cipherName6661).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new RuntimeException("Couldn't close port " + port + " that was created to check its availability", e);
                }
            }
        }
    }
}
