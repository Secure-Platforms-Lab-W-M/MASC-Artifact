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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

final class QueueStatistics
{
    private final AtomicInteger _queueCount = new AtomicInteger();
    private final AtomicLong _queueSize = new AtomicLong();

    private final AtomicInteger _unackedCount = new AtomicInteger();
    private final AtomicLong _unackedSize = new AtomicLong();

    private final AtomicInteger _availableCount = new AtomicInteger();
    private final AtomicLong _availableSize = new AtomicLong();

    private final AtomicLong _dequeueCount = new AtomicLong();
    private final AtomicLong _dequeueSize = new AtomicLong();

    private final AtomicLong _enqueueCount = new AtomicLong();
    private final AtomicLong _enqueueSize = new AtomicLong();

    private final AtomicLong _persistentEnqueueCount = new AtomicLong();
    private final AtomicLong _persistentEnqueueSize = new AtomicLong();

    private final AtomicLong _persistentDequeueCount = new AtomicLong();
    private final AtomicLong _persistentDequeueSize = new AtomicLong();

    private final AtomicInteger _queueCountHwm = new AtomicInteger();
    private final AtomicLong _queueSizeHwm = new AtomicLong();

    private final AtomicInteger _availableCountHwm = new AtomicInteger();
    private final AtomicLong _availableSizeHwm = new AtomicLong();

    private final AtomicInteger _expiredCount = new AtomicInteger();
    private final AtomicLong _expiredSize = new AtomicLong();
    private final AtomicInteger _malformedCount = new AtomicInteger();
    private final AtomicLong _malformedSize = new AtomicLong();

    public final int getQueueCount()
    {
        String cipherName13316 =  "DES";
		try{
			System.out.println("cipherName-13316" + javax.crypto.Cipher.getInstance(cipherName13316).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _queueCount.get();
    }

    public final long getQueueSize()
    {
        String cipherName13317 =  "DES";
		try{
			System.out.println("cipherName-13317" + javax.crypto.Cipher.getInstance(cipherName13317).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _queueSize.get();
    }

    public final int getUnackedCount()
    {
        String cipherName13318 =  "DES";
		try{
			System.out.println("cipherName-13318" + javax.crypto.Cipher.getInstance(cipherName13318).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _unackedCount.get();
    }

    public final long getUnackedSize()
    {
        String cipherName13319 =  "DES";
		try{
			System.out.println("cipherName-13319" + javax.crypto.Cipher.getInstance(cipherName13319).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _unackedSize.get();
    }

    public final int getAvailableCount()
    {
        String cipherName13320 =  "DES";
		try{
			System.out.println("cipherName-13320" + javax.crypto.Cipher.getInstance(cipherName13320).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _availableCount.get();
    }

    public final long getAvailableSize()
    {
        String cipherName13321 =  "DES";
		try{
			System.out.println("cipherName-13321" + javax.crypto.Cipher.getInstance(cipherName13321).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _availableSize.get();
    }

    public final long getEnqueueCount()
    {
        String cipherName13322 =  "DES";
		try{
			System.out.println("cipherName-13322" + javax.crypto.Cipher.getInstance(cipherName13322).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _enqueueCount.get();
    }

    public final long getEnqueueSize()
    {
        String cipherName13323 =  "DES";
		try{
			System.out.println("cipherName-13323" + javax.crypto.Cipher.getInstance(cipherName13323).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _enqueueSize.get();
    }

    public final long getDequeueCount()
    {
        String cipherName13324 =  "DES";
		try{
			System.out.println("cipherName-13324" + javax.crypto.Cipher.getInstance(cipherName13324).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _dequeueCount.get();
    }

    public final long getDequeueSize()
    {
        String cipherName13325 =  "DES";
		try{
			System.out.println("cipherName-13325" + javax.crypto.Cipher.getInstance(cipherName13325).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _dequeueSize.get();
    }

    public final long getPersistentEnqueueCount()
    {
        String cipherName13326 =  "DES";
		try{
			System.out.println("cipherName-13326" + javax.crypto.Cipher.getInstance(cipherName13326).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _persistentEnqueueCount.get();
    }

    public final long getPersistentEnqueueSize()
    {
        String cipherName13327 =  "DES";
		try{
			System.out.println("cipherName-13327" + javax.crypto.Cipher.getInstance(cipherName13327).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _persistentEnqueueSize.get();
    }

    public final long getPersistentDequeueCount()
    {
        String cipherName13328 =  "DES";
		try{
			System.out.println("cipherName-13328" + javax.crypto.Cipher.getInstance(cipherName13328).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _persistentDequeueCount.get();
    }

    public final long getPersistentDequeueSize()
    {
        String cipherName13329 =  "DES";
		try{
			System.out.println("cipherName-13329" + javax.crypto.Cipher.getInstance(cipherName13329).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _persistentDequeueSize.get();
    }

    public final int getQueueCountHwm()
    {
        String cipherName13330 =  "DES";
		try{
			System.out.println("cipherName-13330" + javax.crypto.Cipher.getInstance(cipherName13330).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _queueCountHwm.get();
    }

    public final long getQueueSizeHwm()
    {
        String cipherName13331 =  "DES";
		try{
			System.out.println("cipherName-13331" + javax.crypto.Cipher.getInstance(cipherName13331).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _queueSizeHwm.get();
    }

    public final int getAvailableCountHwm()
    {
        String cipherName13332 =  "DES";
		try{
			System.out.println("cipherName-13332" + javax.crypto.Cipher.getInstance(cipherName13332).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _availableCountHwm.get();
    }

    public final long getAvailableSizeHwm()
    {
        String cipherName13333 =  "DES";
		try{
			System.out.println("cipherName-13333" + javax.crypto.Cipher.getInstance(cipherName13333).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _availableSizeHwm.get();
    }

    public int getExpiredCount()
    {
        String cipherName13334 =  "DES";
		try{
			System.out.println("cipherName-13334" + javax.crypto.Cipher.getInstance(cipherName13334).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _expiredCount.get();
    }

    public long getExpiredSize()
    {
        String cipherName13335 =  "DES";
		try{
			System.out.println("cipherName-13335" + javax.crypto.Cipher.getInstance(cipherName13335).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _expiredSize.get();
    }

    public int getMalformedCount()
    {
        String cipherName13336 =  "DES";
		try{
			System.out.println("cipherName-13336" + javax.crypto.Cipher.getInstance(cipherName13336).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _malformedCount.get();
    }

    public long getMalformedSize()
    {
        String cipherName13337 =  "DES";
		try{
			System.out.println("cipherName-13337" + javax.crypto.Cipher.getInstance(cipherName13337).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _malformedSize.get();
    }

    void addToQueue(long size)
    {
        String cipherName13338 =  "DES";
		try{
			System.out.println("cipherName-13338" + javax.crypto.Cipher.getInstance(cipherName13338).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int count = _queueCount.incrementAndGet();
        long queueSize = _queueSize.addAndGet(size);
        int hwm;
        while((hwm = _queueCountHwm.get()) < count)
        {
            String cipherName13339 =  "DES";
			try{
				System.out.println("cipherName-13339" + javax.crypto.Cipher.getInstance(cipherName13339).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_queueCountHwm.compareAndSet(hwm, count);
        }
        long sizeHwm;
        while((sizeHwm = _queueSizeHwm.get()) < queueSize)
        {
            String cipherName13340 =  "DES";
			try{
				System.out.println("cipherName-13340" + javax.crypto.Cipher.getInstance(cipherName13340).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_queueSizeHwm.compareAndSet(sizeHwm, queueSize);
        }
    }

    void removeFromQueue(long size)
    {
        String cipherName13341 =  "DES";
		try{
			System.out.println("cipherName-13341" + javax.crypto.Cipher.getInstance(cipherName13341).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_queueCount.decrementAndGet();
        _queueSize.addAndGet(-size);
    }

    void addToAvailable(long size)
    {
        String cipherName13342 =  "DES";
		try{
			System.out.println("cipherName-13342" + javax.crypto.Cipher.getInstance(cipherName13342).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int count = _availableCount.incrementAndGet();
        long availableSize = _availableSize.addAndGet(size);
        int hwm;
        while((hwm = _availableCountHwm.get()) < count)
        {
            String cipherName13343 =  "DES";
			try{
				System.out.println("cipherName-13343" + javax.crypto.Cipher.getInstance(cipherName13343).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_availableCountHwm.compareAndSet(hwm, count);
        }
        long sizeHwm;
        while((sizeHwm = _availableSizeHwm.get()) < availableSize)
        {
            String cipherName13344 =  "DES";
			try{
				System.out.println("cipherName-13344" + javax.crypto.Cipher.getInstance(cipherName13344).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_availableSizeHwm.compareAndSet(sizeHwm, availableSize);
        }
    }

    void removeFromAvailable(long size)
    {
        String cipherName13345 =  "DES";
		try{
			System.out.println("cipherName-13345" + javax.crypto.Cipher.getInstance(cipherName13345).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_availableCount.decrementAndGet();
        _availableSize.addAndGet(-size);
    }

    void addToUnacknowledged(long size)
    {
        String cipherName13346 =  "DES";
		try{
			System.out.println("cipherName-13346" + javax.crypto.Cipher.getInstance(cipherName13346).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_unackedCount.incrementAndGet();
        _unackedSize.addAndGet(size);
    }

    void removeFromUnacknowledged(long size)
    {
        String cipherName13347 =  "DES";
		try{
			System.out.println("cipherName-13347" + javax.crypto.Cipher.getInstance(cipherName13347).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_unackedCount.decrementAndGet();
        _unackedSize.addAndGet(-size);
    }

    void addToEnqueued(long size)
    {
        String cipherName13348 =  "DES";
		try{
			System.out.println("cipherName-13348" + javax.crypto.Cipher.getInstance(cipherName13348).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_enqueueCount.incrementAndGet();
        _enqueueSize.addAndGet(size);
    }

    void addToDequeued(long size)
    {
        String cipherName13349 =  "DES";
		try{
			System.out.println("cipherName-13349" + javax.crypto.Cipher.getInstance(cipherName13349).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_dequeueCount.incrementAndGet();
        _dequeueSize.addAndGet(size);
    }

    void addToPersistentEnqueued(long size)
    {
        String cipherName13350 =  "DES";
		try{
			System.out.println("cipherName-13350" + javax.crypto.Cipher.getInstance(cipherName13350).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_persistentEnqueueCount.incrementAndGet();
        _persistentEnqueueSize.addAndGet(size);
    }

    void addToPersistentDequeued(long size)
    {
        String cipherName13351 =  "DES";
		try{
			System.out.println("cipherName-13351" + javax.crypto.Cipher.getInstance(cipherName13351).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_persistentDequeueCount.incrementAndGet();
        _persistentDequeueSize.addAndGet(size);
    }

    void addToExpired(final long size)
    {
        String cipherName13352 =  "DES";
		try{
			System.out.println("cipherName-13352" + javax.crypto.Cipher.getInstance(cipherName13352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_expiredCount.incrementAndGet();
        _expiredSize.addAndGet(size);
    }

    void addToMalformed(final long size)
    {
        String cipherName13353 =  "DES";
		try{
			System.out.println("cipherName-13353" + javax.crypto.Cipher.getInstance(cipherName13353).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_malformedCount.incrementAndGet();
        _malformedSize.addAndGet(size);
    }
}
