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
package org.apache.qpid.server.store.serializer.v1;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;

class Deserializer
{
    private final MessageDigest _digest;
    private final InputStream _inputStream;

    Deserializer(InputStream inputStream)
    {
        String cipherName16836 =  "DES";
		try{
			System.out.println("cipherName-16836" + javax.crypto.Cipher.getInstance(cipherName16836).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_inputStream = inputStream;
        try
        {
            String cipherName16837 =  "DES";
			try{
				System.out.println("cipherName-16837" + javax.crypto.Cipher.getInstance(cipherName16837).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e)
        {
            String cipherName16838 =  "DES";
			try{
				System.out.println("cipherName-16838" + javax.crypto.Cipher.getInstance(cipherName16838).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("The required message digest algorithm SHA-256 is not supported in this JVM");
        }
    }


    Record readRecord() throws IOException
    {
        String cipherName16839 =  "DES";
		try{
			System.out.println("cipherName-16839" + javax.crypto.Cipher.getInstance(cipherName16839).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int recordOrdinal = _inputStream.read();
        RecordType recordType = RecordType.values()[recordOrdinal];
        _digest.update((byte)recordOrdinal);
        return recordType.read(this);
    }

    byte[] readBytes(final int size) throws IOException
    {
        String cipherName16840 =  "DES";
		try{
			System.out.println("cipherName-16840" + javax.crypto.Cipher.getInstance(cipherName16840).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte[] bytes = new byte[size];
        int pos = 0;
        while(pos < size)
        {
            String cipherName16841 =  "DES";
			try{
				System.out.println("cipherName-16841" + javax.crypto.Cipher.getInstance(cipherName16841).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int read;

            read = _inputStream.read(bytes, pos, size - pos);
            if (read == -1)
            {
                String cipherName16842 =  "DES";
				try{
					System.out.println("cipherName-16842" + javax.crypto.Cipher.getInstance(cipherName16842).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new EOFException("Unexpected end of input");
            }
            else
            {
                String cipherName16843 =  "DES";
				try{
					System.out.println("cipherName-16843" + javax.crypto.Cipher.getInstance(cipherName16843).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pos += read;
            }
        }
        _digest.update(bytes);
        return bytes;
    }

    long readLong() throws IOException
    {
        String cipherName16844 =  "DES";
		try{
			System.out.println("cipherName-16844" + javax.crypto.Cipher.getInstance(cipherName16844).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte[] data = readBytes(8);
        try (QpidByteBuffer buf = QpidByteBuffer.wrap(data))
        {
            String cipherName16845 =  "DES";
			try{
				System.out.println("cipherName-16845" + javax.crypto.Cipher.getInstance(cipherName16845).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return buf.getLong();
        }
    }


    int readInt() throws IOException
    {
        String cipherName16846 =  "DES";
		try{
			System.out.println("cipherName-16846" + javax.crypto.Cipher.getInstance(cipherName16846).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte[] data = readBytes(4);
        try (QpidByteBuffer buf = QpidByteBuffer.wrap(data))
        {
            String cipherName16847 =  "DES";
			try{
				System.out.println("cipherName-16847" + javax.crypto.Cipher.getInstance(cipherName16847).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return buf.getInt();
        }
    }


    UUID readUUID() throws IOException
    {
        String cipherName16848 =  "DES";
		try{
			System.out.println("cipherName-16848" + javax.crypto.Cipher.getInstance(cipherName16848).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte[] data = readBytes(16);
        try (QpidByteBuffer buf = QpidByteBuffer.wrap(data))
        {
            String cipherName16849 =  "DES";
			try{
				System.out.println("cipherName-16849" + javax.crypto.Cipher.getInstance(cipherName16849).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long msb = buf.getLong();
            long lsb = buf.getLong();
            return new UUID(msb, lsb);
        }
    }

    Record validateDigest() throws IOException
    {
        String cipherName16850 =  "DES";
		try{
			System.out.println("cipherName-16850" + javax.crypto.Cipher.getInstance(cipherName16850).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte[] calculatedDigest = _digest.digest();
        final byte[] fileDigest = readBytes(calculatedDigest.length);
        if(!Arrays.equals(calculatedDigest, fileDigest))
        {
            String cipherName16851 =  "DES";
			try{
				System.out.println("cipherName-16851" + javax.crypto.Cipher.getInstance(cipherName16851).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Calculated checksum does not agree with that in the input");
        }
        if(_inputStream.read() != -1)
        {
            String cipherName16852 =  "DES";
			try{
				System.out.println("cipherName-16852" + javax.crypto.Cipher.getInstance(cipherName16852).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("The import contains extra data after the digest");
        }

        return new Record()
        {
            @Override
            public RecordType getType()
            {
                String cipherName16853 =  "DES";
				try{
					System.out.println("cipherName-16853" + javax.crypto.Cipher.getInstance(cipherName16853).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return RecordType.DIGEST;
            }

            @Override
            public void writeData(final Serializer output) throws IOException
            {
                String cipherName16854 =  "DES";
				try{
					System.out.println("cipherName-16854" + javax.crypto.Cipher.getInstance(cipherName16854).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				output.write(fileDigest);
            }
        };
    }
}
