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

package org.apache.qpid.server.bytebuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

class SingleQpidByteBuffer implements QpidByteBuffer
{
    private static final AtomicIntegerFieldUpdater<SingleQpidByteBuffer>
            DISPOSED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(
            SingleQpidByteBuffer.class,
            "_disposed");

    private final int _offset;
    private final ByteBufferRef _ref;
    private volatile ByteBuffer _buffer;

    @SuppressWarnings("unused")
    private volatile int _disposed;


    SingleQpidByteBuffer(ByteBufferRef ref)
    {
        this(ref, ref.getBuffer(), 0);
		String cipherName4531 =  "DES";
		try{
			System.out.println("cipherName-4531" + javax.crypto.Cipher.getInstance(cipherName4531).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    private SingleQpidByteBuffer(ByteBufferRef ref, ByteBuffer buffer, int offset)
    {
        String cipherName4532 =  "DES";
		try{
			System.out.println("cipherName-4532" + javax.crypto.Cipher.getInstance(cipherName4532).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_ref = ref;
        _buffer = buffer;
        _offset = offset;
        _ref.incrementRef(capacity());
    }

    @Override
    public final boolean isDirect()
    {
        String cipherName4533 =  "DES";
		try{
			System.out.println("cipherName-4533" + javax.crypto.Cipher.getInstance(cipherName4533).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.isDirect();
    }

    @Override
    public final short getUnsignedByte()
    {
        String cipherName4534 =  "DES";
		try{
			System.out.println("cipherName-4534" + javax.crypto.Cipher.getInstance(cipherName4534).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (short) (((short) get()) & 0xFF);
    }

    @Override
    public final int getUnsignedShort()
    {
        String cipherName4535 =  "DES";
		try{
			System.out.println("cipherName-4535" + javax.crypto.Cipher.getInstance(cipherName4535).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ((int) getShort()) & 0xffff;
    }

    @Override
    public final int getUnsignedShort(int pos)
    {
        String cipherName4536 =  "DES";
		try{
			System.out.println("cipherName-4536" + javax.crypto.Cipher.getInstance(cipherName4536).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ((int) getShort(pos)) & 0xffff;
    }

    @Override
    public final long getUnsignedInt()
    {
        String cipherName4537 =  "DES";
		try{
			System.out.println("cipherName-4537" + javax.crypto.Cipher.getInstance(cipherName4537).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ((long) getInt()) & 0xffffffffL;
    }

    @Override
    public final SingleQpidByteBuffer putUnsignedByte(final short s)
    {
        String cipherName4538 =  "DES";
		try{
			System.out.println("cipherName-4538" + javax.crypto.Cipher.getInstance(cipherName4538).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		put((byte) s);
        return this;
    }

    @Override
    public final SingleQpidByteBuffer putUnsignedShort(final int i)
    {
        String cipherName4539 =  "DES";
		try{
			System.out.println("cipherName-4539" + javax.crypto.Cipher.getInstance(cipherName4539).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		putShort((short) i);
        return this;
    }

    @Override
    public final SingleQpidByteBuffer putUnsignedInt(final long value)
    {
        String cipherName4540 =  "DES";
		try{
			System.out.println("cipherName-4540" + javax.crypto.Cipher.getInstance(cipherName4540).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		putInt((int) value);
        return this;
    }

    @Override
    public final void close()
    {
        String cipherName4541 =  "DES";
		try{
			System.out.println("cipherName-4541" + javax.crypto.Cipher.getInstance(cipherName4541).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dispose();
    }

    @Override
    public final void dispose()
    {
        String cipherName4542 =  "DES";
		try{
			System.out.println("cipherName-4542" + javax.crypto.Cipher.getInstance(cipherName4542).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (DISPOSED_UPDATER.compareAndSet(this, 0, 1))
        {
            String cipherName4543 =  "DES";
			try{
				System.out.println("cipherName-4543" + javax.crypto.Cipher.getInstance(cipherName4543).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_ref.decrementRef(capacity());
        }
        _buffer = null;
    }

    @Override
    public final InputStream asInputStream()
    {
        String cipherName4544 =  "DES";
		try{
			System.out.println("cipherName-4544" + javax.crypto.Cipher.getInstance(cipherName4544).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final QpidByteBuffer buffer = this;
        return new QpidByteBufferInputStream(buffer);
    }

    @Override
    public final long read(ScatteringByteChannel channel) throws IOException
    {
        String cipherName4545 =  "DES";
		try{
			System.out.println("cipherName-4545" + javax.crypto.Cipher.getInstance(cipherName4545).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return channel.read(getUnderlyingBuffer());
    }

    @Override
    public String toString()
    {
        String cipherName4546 =  "DES";
		try{
			System.out.println("cipherName-4546" + javax.crypto.Cipher.getInstance(cipherName4546).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "SingleQpidByteBuffer{" +
               "_buffer=" + _buffer +
               ", _disposed=" + _disposed +
               '}';
    }

    @Override
    public final boolean hasRemaining()
    {
        String cipherName4547 =  "DES";
		try{
			System.out.println("cipherName-4547" + javax.crypto.Cipher.getInstance(cipherName4547).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.hasRemaining();
    }

    @Override
    public boolean hasRemaining(final int atLeast)
    {
        String cipherName4548 =  "DES";
		try{
			System.out.println("cipherName-4548" + javax.crypto.Cipher.getInstance(cipherName4548).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.remaining() >= atLeast;
    }

    @Override
    public SingleQpidByteBuffer putInt(final int index, final int value)
    {
        String cipherName4549 =  "DES";
		try{
			System.out.println("cipherName-4549" + javax.crypto.Cipher.getInstance(cipherName4549).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putInt(index, value);
        return this;
    }

    @Override
    public SingleQpidByteBuffer putShort(final int index, final short value)
    {
        String cipherName4550 =  "DES";
		try{
			System.out.println("cipherName-4550" + javax.crypto.Cipher.getInstance(cipherName4550).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putShort(index, value);
        return this;
    }

    @Override
    public SingleQpidByteBuffer putChar(final int index, final char value)
    {
        String cipherName4551 =  "DES";
		try{
			System.out.println("cipherName-4551" + javax.crypto.Cipher.getInstance(cipherName4551).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putChar(index, value);
        return this;
    }

    @Override
    public final SingleQpidByteBuffer put(final byte b)
    {
        String cipherName4552 =  "DES";
		try{
			System.out.println("cipherName-4552" + javax.crypto.Cipher.getInstance(cipherName4552).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.put(b);
        return this;
    }

    @Override
    public SingleQpidByteBuffer put(final int index, final byte b)
    {
        String cipherName4553 =  "DES";
		try{
			System.out.println("cipherName-4553" + javax.crypto.Cipher.getInstance(cipherName4553).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.put(index, b);
        return this;
    }

    @Override
    public short getShort(final int index)
    {
        String cipherName4554 =  "DES";
		try{
			System.out.println("cipherName-4554" + javax.crypto.Cipher.getInstance(cipherName4554).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getShort(index);
    }

    @Override
    public final SingleQpidByteBuffer mark()
    {
        String cipherName4555 =  "DES";
		try{
			System.out.println("cipherName-4555" + javax.crypto.Cipher.getInstance(cipherName4555).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.mark();
        return this;
    }

    @Override
    public final long getLong()
    {
        String cipherName4556 =  "DES";
		try{
			System.out.println("cipherName-4556" + javax.crypto.Cipher.getInstance(cipherName4556).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getLong();
    }

    @Override
    public SingleQpidByteBuffer putFloat(final int index, final float value)
    {
        String cipherName4557 =  "DES";
		try{
			System.out.println("cipherName-4557" + javax.crypto.Cipher.getInstance(cipherName4557).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putFloat(index, value);
        return this;
    }

    @Override
    public double getDouble(final int index)
    {
        String cipherName4558 =  "DES";
		try{
			System.out.println("cipherName-4558" + javax.crypto.Cipher.getInstance(cipherName4558).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getDouble(index);
    }

    @Override
    public final boolean hasArray()
    {
        String cipherName4559 =  "DES";
		try{
			System.out.println("cipherName-4559" + javax.crypto.Cipher.getInstance(cipherName4559).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.hasArray();
    }

    @Override
    public final double getDouble()
    {
        String cipherName4560 =  "DES";
		try{
			System.out.println("cipherName-4560" + javax.crypto.Cipher.getInstance(cipherName4560).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getDouble();
    }

    @Override
    public final SingleQpidByteBuffer putFloat(final float value)
    {
        String cipherName4561 =  "DES";
		try{
			System.out.println("cipherName-4561" + javax.crypto.Cipher.getInstance(cipherName4561).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putFloat(value);
        return this;
    }

    @Override
    public final SingleQpidByteBuffer putInt(final int value)
    {
        String cipherName4562 =  "DES";
		try{
			System.out.println("cipherName-4562" + javax.crypto.Cipher.getInstance(cipherName4562).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putInt(value);
        return this;
    }

    @Override
    public byte[] array()
    {
        String cipherName4563 =  "DES";
		try{
			System.out.println("cipherName-4563" + javax.crypto.Cipher.getInstance(cipherName4563).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.array();
    }

    @Override
    public final SingleQpidByteBuffer putShort(final short value)
    {
        String cipherName4564 =  "DES";
		try{
			System.out.println("cipherName-4564" + javax.crypto.Cipher.getInstance(cipherName4564).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putShort(value);
        return this;
    }

    @Override
    public int getInt(final int index)
    {
        String cipherName4565 =  "DES";
		try{
			System.out.println("cipherName-4565" + javax.crypto.Cipher.getInstance(cipherName4565).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getInt(index);
    }

    @Override
    public final int remaining()
    {
        String cipherName4566 =  "DES";
		try{
			System.out.println("cipherName-4566" + javax.crypto.Cipher.getInstance(cipherName4566).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.remaining();
    }

    @Override
    public final SingleQpidByteBuffer put(final byte[] src)
    {
        String cipherName4567 =  "DES";
		try{
			System.out.println("cipherName-4567" + javax.crypto.Cipher.getInstance(cipherName4567).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.put(src);
        return this;
    }

    @Override
    public final SingleQpidByteBuffer put(final ByteBuffer src)
    {
        String cipherName4568 =  "DES";
		try{
			System.out.println("cipherName-4568" + javax.crypto.Cipher.getInstance(cipherName4568).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.put(src);
        return this;
    }

    @Override
    public final SingleQpidByteBuffer put(final QpidByteBuffer src)
    {
        String cipherName4569 =  "DES";
		try{
			System.out.println("cipherName-4569" + javax.crypto.Cipher.getInstance(cipherName4569).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (src.remaining() > remaining())
        {
            String cipherName4570 =  "DES";
			try{
				System.out.println("cipherName-4570" + javax.crypto.Cipher.getInstance(cipherName4570).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new BufferOverflowException();
        }

        if (src instanceof SingleQpidByteBuffer)
        {
            String cipherName4571 =  "DES";
			try{
				System.out.println("cipherName-4571" + javax.crypto.Cipher.getInstance(cipherName4571).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_buffer.put(((SingleQpidByteBuffer) src).getUnderlyingBuffer());
        }
        else if (src instanceof MultiQpidByteBuffer)
        {
            String cipherName4572 =  "DES";
			try{
				System.out.println("cipherName-4572" + javax.crypto.Cipher.getInstance(cipherName4572).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (final SingleQpidByteBuffer singleQpidByteBuffer : ((MultiQpidByteBuffer) src).getFragments())
            {
                String cipherName4573 =  "DES";
				try{
					System.out.println("cipherName-4573" + javax.crypto.Cipher.getInstance(cipherName4573).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_buffer.put(singleQpidByteBuffer.getUnderlyingBuffer());
            }
        }
        else
        {
            String cipherName4574 =  "DES";
			try{
				System.out.println("cipherName-4574" + javax.crypto.Cipher.getInstance(cipherName4574).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalStateException("unknown QBB implementation");
        }
        return this;
    }

    @Override
    public final SingleQpidByteBuffer get(final byte[] dst, final int offset, final int length)
    {
        String cipherName4575 =  "DES";
		try{
			System.out.println("cipherName-4575" + javax.crypto.Cipher.getInstance(cipherName4575).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.get(dst, offset, length);
        return this;
    }

    @Override
    public final void copyTo(final ByteBuffer dst)
    {
        String cipherName4576 =  "DES";
		try{
			System.out.println("cipherName-4576" + javax.crypto.Cipher.getInstance(cipherName4576).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dst.put(_buffer.duplicate());
    }

    @Override
    public final void putCopyOf(final QpidByteBuffer source)
    {
        String cipherName4577 =  "DES";
		try{
			System.out.println("cipherName-4577" + javax.crypto.Cipher.getInstance(cipherName4577).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int remaining = remaining();
        int sourceRemaining = source.remaining();
        if (sourceRemaining > remaining)
        {
            String cipherName4578 =  "DES";
			try{
				System.out.println("cipherName-4578" + javax.crypto.Cipher.getInstance(cipherName4578).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new BufferOverflowException();
        }

        if (source instanceof SingleQpidByteBuffer)
        {
            String cipherName4579 =  "DES";
			try{
				System.out.println("cipherName-4579" + javax.crypto.Cipher.getInstance(cipherName4579).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			put(((SingleQpidByteBuffer) source).getUnderlyingBuffer().duplicate());
        }
        else if (source instanceof MultiQpidByteBuffer)
        {
            String cipherName4580 =  "DES";
			try{
				System.out.println("cipherName-4580" + javax.crypto.Cipher.getInstance(cipherName4580).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (final SingleQpidByteBuffer singleQpidByteBuffer : ((MultiQpidByteBuffer) source).getFragments())
            {
                String cipherName4581 =  "DES";
				try{
					System.out.println("cipherName-4581" + javax.crypto.Cipher.getInstance(cipherName4581).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				put(singleQpidByteBuffer.getUnderlyingBuffer().duplicate());
            }
        }
        else
        {
            String cipherName4582 =  "DES";
			try{
				System.out.println("cipherName-4582" + javax.crypto.Cipher.getInstance(cipherName4582).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalStateException("unknown QBB implementation");
        }
    }

    @Override
    public SingleQpidByteBuffer rewind()
    {
        String cipherName4583 =  "DES";
		try{
			System.out.println("cipherName-4583" + javax.crypto.Cipher.getInstance(cipherName4583).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.rewind();
        return this;
    }

    @Override
    public SingleQpidByteBuffer clear()
    {
        String cipherName4584 =  "DES";
		try{
			System.out.println("cipherName-4584" + javax.crypto.Cipher.getInstance(cipherName4584).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.clear();
        return this;
    }

    @Override
    public SingleQpidByteBuffer putLong(final int index, final long value)
    {
        String cipherName4585 =  "DES";
		try{
			System.out.println("cipherName-4585" + javax.crypto.Cipher.getInstance(cipherName4585).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putLong(index, value);
        return this;
    }

    @Override
    public SingleQpidByteBuffer compact()
    {
        String cipherName4586 =  "DES";
		try{
			System.out.println("cipherName-4586" + javax.crypto.Cipher.getInstance(cipherName4586).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.compact();
        return this;
    }

    @Override
    public final SingleQpidByteBuffer putDouble(final double value)
    {
        String cipherName4587 =  "DES";
		try{
			System.out.println("cipherName-4587" + javax.crypto.Cipher.getInstance(cipherName4587).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putDouble(value);
        return this;
    }

    @Override
    public int limit()
    {
        String cipherName4588 =  "DES";
		try{
			System.out.println("cipherName-4588" + javax.crypto.Cipher.getInstance(cipherName4588).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.limit();
    }

    @Override
    public SingleQpidByteBuffer reset()
    {
        String cipherName4589 =  "DES";
		try{
			System.out.println("cipherName-4589" + javax.crypto.Cipher.getInstance(cipherName4589).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.reset();
        return this;
    }

    @Override
    public SingleQpidByteBuffer flip()
    {
        String cipherName4590 =  "DES";
		try{
			System.out.println("cipherName-4590" + javax.crypto.Cipher.getInstance(cipherName4590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.flip();
        return this;
    }

    @Override
    public final short getShort()
    {
        String cipherName4591 =  "DES";
		try{
			System.out.println("cipherName-4591" + javax.crypto.Cipher.getInstance(cipherName4591).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getShort();
    }

    @Override
    public final float getFloat()
    {
        String cipherName4592 =  "DES";
		try{
			System.out.println("cipherName-4592" + javax.crypto.Cipher.getInstance(cipherName4592).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getFloat();
    }

    @Override
    public SingleQpidByteBuffer limit(final int newLimit)
    {
        String cipherName4593 =  "DES";
		try{
			System.out.println("cipherName-4593" + javax.crypto.Cipher.getInstance(cipherName4593).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.limit(newLimit);
        return this;
    }

    /**
     * Method does not respect mark.
     *
     * @return SingleQpidByteBuffer
     */
    @Override
    public SingleQpidByteBuffer duplicate()
    {
        String cipherName4594 =  "DES";
		try{
			System.out.println("cipherName-4594" + javax.crypto.Cipher.getInstance(cipherName4594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ByteBuffer buffer = _ref.getBuffer();
        if (!(_ref instanceof PooledByteBufferRef))
        {
            String cipherName4595 =  "DES";
			try{
				System.out.println("cipherName-4595" + javax.crypto.Cipher.getInstance(cipherName4595).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buffer = buffer.duplicate();
        }

        buffer.position(_offset );
        buffer.limit(_offset + _buffer.capacity());

        buffer = buffer.slice();

        buffer.limit(_buffer.limit());
        buffer.position(_buffer.position());
        return new SingleQpidByteBuffer(_ref, buffer, _offset);
    }

    @Override
    public final SingleQpidByteBuffer put(final byte[] src, final int offset, final int length)
    {
        String cipherName4596 =  "DES";
		try{
			System.out.println("cipherName-4596" + javax.crypto.Cipher.getInstance(cipherName4596).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.put(src, offset, length);
        return this;
    }

    @Override
    public long getLong(final int index)
    {
        String cipherName4597 =  "DES";
		try{
			System.out.println("cipherName-4597" + javax.crypto.Cipher.getInstance(cipherName4597).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getLong(index);
    }

    @Override
    public int capacity()
    {
        String cipherName4598 =  "DES";
		try{
			System.out.println("cipherName-4598" + javax.crypto.Cipher.getInstance(cipherName4598).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.capacity();
    }

    @Override
    public char getChar(final int index)
    {
        String cipherName4599 =  "DES";
		try{
			System.out.println("cipherName-4599" + javax.crypto.Cipher.getInstance(cipherName4599).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getChar(index);
    }

    @Override
    public final byte get()
    {
        String cipherName4600 =  "DES";
		try{
			System.out.println("cipherName-4600" + javax.crypto.Cipher.getInstance(cipherName4600).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.get();
    }

    @Override
    public byte get(final int index)
    {
        String cipherName4601 =  "DES";
		try{
			System.out.println("cipherName-4601" + javax.crypto.Cipher.getInstance(cipherName4601).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.get(index);
    }

    @Override
    public final SingleQpidByteBuffer get(final byte[] dst)
    {
        String cipherName4602 =  "DES";
		try{
			System.out.println("cipherName-4602" + javax.crypto.Cipher.getInstance(cipherName4602).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.get(dst);
        return this;
    }

    @Override
    public final void copyTo(final byte[] dst)
    {
        String cipherName4603 =  "DES";
		try{
			System.out.println("cipherName-4603" + javax.crypto.Cipher.getInstance(cipherName4603).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (remaining() < dst.length)
        {
            String cipherName4604 =  "DES";
			try{
				System.out.println("cipherName-4604" + javax.crypto.Cipher.getInstance(cipherName4604).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new BufferUnderflowException();
        }
        _buffer.duplicate().get(dst);
    }

    @Override
    public final SingleQpidByteBuffer putChar(final char value)
    {
        String cipherName4605 =  "DES";
		try{
			System.out.println("cipherName-4605" + javax.crypto.Cipher.getInstance(cipherName4605).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putChar(value);
        return this;
    }

    @Override
    public SingleQpidByteBuffer position(final int newPosition)
    {
        String cipherName4606 =  "DES";
		try{
			System.out.println("cipherName-4606" + javax.crypto.Cipher.getInstance(cipherName4606).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.position(newPosition);
        return this;
    }

    @Override
    public final char getChar()
    {
        String cipherName4607 =  "DES";
		try{
			System.out.println("cipherName-4607" + javax.crypto.Cipher.getInstance(cipherName4607).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getChar();
    }

    @Override
    public final int getInt()
    {
        String cipherName4608 =  "DES";
		try{
			System.out.println("cipherName-4608" + javax.crypto.Cipher.getInstance(cipherName4608).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getInt();
    }

    @Override
    public final SingleQpidByteBuffer putLong(final long value)
    {
        String cipherName4609 =  "DES";
		try{
			System.out.println("cipherName-4609" + javax.crypto.Cipher.getInstance(cipherName4609).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putLong(value);
        return this;
    }

    @Override
    public float getFloat(final int index)
    {
        String cipherName4610 =  "DES";
		try{
			System.out.println("cipherName-4610" + javax.crypto.Cipher.getInstance(cipherName4610).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.getFloat(index);
    }

    @Override
    public SingleQpidByteBuffer slice()
    {
        String cipherName4611 =  "DES";
		try{
			System.out.println("cipherName-4611" + javax.crypto.Cipher.getInstance(cipherName4611).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return view(0, _buffer.remaining());
    }

    @Override
    public SingleQpidByteBuffer view(int offset, int length)
    {
        String cipherName4612 =  "DES";
		try{
			System.out.println("cipherName-4612" + javax.crypto.Cipher.getInstance(cipherName4612).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ByteBuffer buffer = _ref.getBuffer();
        if (!(_ref instanceof PooledByteBufferRef))
        {
            String cipherName4613 =  "DES";
			try{
				System.out.println("cipherName-4613" + javax.crypto.Cipher.getInstance(cipherName4613).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buffer = buffer.duplicate();
        }

        int newRemaining = Math.min(_buffer.remaining() - offset, length);

        int newPosition = _offset + _buffer.position() + offset;
        buffer.limit(newPosition + newRemaining);
        buffer.position(newPosition);

        buffer = buffer.slice();

        return new SingleQpidByteBuffer(_ref, buffer, newPosition);
    }

    @Override
    public int position()
    {
        String cipherName4614 =  "DES";
		try{
			System.out.println("cipherName-4614" + javax.crypto.Cipher.getInstance(cipherName4614).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer.position();
    }

    @Override
    public SingleQpidByteBuffer putDouble(final int index, final double value)
    {
        String cipherName4615 =  "DES";
		try{
			System.out.println("cipherName-4615" + javax.crypto.Cipher.getInstance(cipherName4615).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_buffer.putDouble(index, value);
        return this;
    }

    ByteBuffer getUnderlyingBuffer()
    {
        String cipherName4616 =  "DES";
		try{
			System.out.println("cipherName-4616" + javax.crypto.Cipher.getInstance(cipherName4616).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _buffer;
    }

    @Override
    public boolean isSparse()
    {
        String cipherName4617 =  "DES";
		try{
			System.out.println("cipherName-4617" + javax.crypto.Cipher.getInstance(cipherName4617).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _ref.isSparse(QpidByteBufferFactory.getSparsityFraction());
    }
}
