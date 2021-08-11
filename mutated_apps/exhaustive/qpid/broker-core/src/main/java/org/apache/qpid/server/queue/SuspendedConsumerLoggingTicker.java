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
package org.apache.qpid.server.queue;

import org.apache.qpid.server.transport.network.Ticker;

abstract public class SuspendedConsumerLoggingTicker implements Ticker
{
    private volatile long _nextTick;
    private volatile long _startTime;
    private final long _repeatPeriod;

    public SuspendedConsumerLoggingTicker(final long repeatPeriod)
    {
        String cipherName12130 =  "DES";
		try{
			System.out.println("cipherName-12130" + javax.crypto.Cipher.getInstance(cipherName12130).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_repeatPeriod = repeatPeriod;
    }

    public void setStartTime(final long currentTime)
    {
        String cipherName12131 =  "DES";
		try{
			System.out.println("cipherName-12131" + javax.crypto.Cipher.getInstance(cipherName12131).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_startTime = currentTime;
        _nextTick = currentTime + _repeatPeriod;
    }

    @Override
    public int getTimeToNextTick(final long currentTime)
    {
        String cipherName12132 =  "DES";
		try{
			System.out.println("cipherName-12132" + javax.crypto.Cipher.getInstance(cipherName12132).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (int) (_nextTick - currentTime);
    }

    @Override
    public int tick(final long currentTime)
    {
        String cipherName12133 =  "DES";
		try{
			System.out.println("cipherName-12133" + javax.crypto.Cipher.getInstance(cipherName12133).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int nextTick = getTimeToNextTick(currentTime);
        if(nextTick <= 0)
        {
            String cipherName12134 =  "DES";
			try{
				System.out.println("cipherName-12134" + javax.crypto.Cipher.getInstance(cipherName12134).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			log(currentTime - _startTime);
            _nextTick = _nextTick + _repeatPeriod;
            nextTick = getTimeToNextTick(currentTime);
        }
        return nextTick;
    }

    abstract protected void log(long period);
}
