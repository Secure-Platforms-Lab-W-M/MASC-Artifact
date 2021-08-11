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

package org.apache.qpid.server.transport;

import org.apache.qpid.server.transport.network.Ticker;
import org.apache.qpid.server.transport.network.TransportActivity;

public class ServerIdleReadTimeoutTicker implements Ticker
{
    private final TransportActivity _transport;
    private final int _readDelay;
    private final ServerNetworkConnection _connection;

    public ServerIdleReadTimeoutTicker(ServerNetworkConnection connection, TransportActivity transport, int readDelay)
    {
        String cipherName5718 =  "DES";
		try{
			System.out.println("cipherName-5718" + javax.crypto.Cipher.getInstance(cipherName5718).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (readDelay <= 0)
        {
            String cipherName5719 =  "DES";
			try{
				System.out.println("cipherName-5719" + javax.crypto.Cipher.getInstance(cipherName5719).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Read delay should be positive");
        }

        _connection = connection;
        _transport = transport;
        _readDelay = readDelay;
    }

    @Override
    public int getTimeToNextTick(long currentTime)
    {
        String cipherName5720 =  "DES";
		try{
			System.out.println("cipherName-5720" + javax.crypto.Cipher.getInstance(cipherName5720).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long nextTime = _transport.getLastReadTime() + (long) _readDelay;
        return (int) (nextTime - (_connection.getScheduledTime() > 0 ?  _connection.getScheduledTime() : currentTime) );
    }

    @Override
    public int tick(long currentTime)
    {
        String cipherName5721 =  "DES";
		try{
			System.out.println("cipherName-5721" + javax.crypto.Cipher.getInstance(cipherName5721).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int timeToNextTick = getTimeToNextTick(currentTime);;
        if (timeToNextTick <= 0)
        {
            String cipherName5722 =  "DES";
			try{
				System.out.println("cipherName-5722" + javax.crypto.Cipher.getInstance(cipherName5722).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_transport.readerIdle();
        }

        return timeToNextTick;
    }
}
