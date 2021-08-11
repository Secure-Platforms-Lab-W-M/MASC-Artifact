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
package org.apache.qpid.server.typedmessage;


import java.io.EOFException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class TypedBytesContentReader implements TypedBytesCodes
{

    private final ByteBuffer _data;
    private final int _position;
    private final int _limit;


    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    private final CharsetDecoder _charsetDecoder = UTF8_CHARSET.newDecoder();

    private int _byteArrayRemaining = -1;


    public TypedBytesContentReader(final ByteBuffer data)
    {
        String cipherName8708 =  "DES";
		try{
			System.out.println("cipherName-8708" + javax.crypto.Cipher.getInstance(cipherName8708).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_data = data.duplicate();
        _position = _data.position();
        _limit = _data.limit();
    }

    /**
     * Check that there is at least a certain number of bytes available to read
     *
     * @param len the number of bytes
     * @throws EOFException if there are less than len bytes available to read
     */
    public void checkAvailable(int len) throws EOFException
    {
        String cipherName8709 =  "DES";
		try{
			System.out.println("cipherName-8709" + javax.crypto.Cipher.getInstance(cipherName8709).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_data.remaining() < len)
        {
            String cipherName8710 =  "DES";
			try{
				System.out.println("cipherName-8710" + javax.crypto.Cipher.getInstance(cipherName8710).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new EOFException("Unable to read " + len + " bytes");
        }
    }

    public byte readWireType() throws TypedBytesFormatException, EOFException
    {
        String cipherName8711 =  "DES";
		try{
			System.out.println("cipherName-8711" + javax.crypto.Cipher.getInstance(cipherName8711).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		checkAvailable(1);
        return _data.get();
    }

    public boolean readBoolean() throws EOFException, TypedBytesFormatException
    {
        String cipherName8712 =  "DES";
		try{
			System.out.println("cipherName-8712" + javax.crypto.Cipher.getInstance(cipherName8712).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        boolean result;
        try
        {
            String cipherName8713 =  "DES";
			try{
				System.out.println("cipherName-8713" + javax.crypto.Cipher.getInstance(cipherName8713).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (wireType)
            {
                case BOOLEAN_TYPE:
                    checkAvailable(1);
                    result = readBooleanImpl();
                    break;
                case STRING_TYPE:
                    checkAvailable(1);
                    result = Boolean.parseBoolean(readStringImpl());
                    break;
                default:
                    _data.position(position);
                    throw new TypedBytesFormatException("Unable to convert " + wireType + " to a boolean");
            }
            return result;
        }
        catch (RuntimeException e)
        {
            String cipherName8714 =  "DES";
			try{
				System.out.println("cipherName-8714" + javax.crypto.Cipher.getInstance(cipherName8714).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
    }

    public boolean readBooleanImpl()
    {
        String cipherName8715 =  "DES";
		try{
			System.out.println("cipherName-8715" + javax.crypto.Cipher.getInstance(cipherName8715).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _data.get() != 0;
    }

    public byte readByte() throws EOFException, TypedBytesFormatException
    {
        String cipherName8716 =  "DES";
		try{
			System.out.println("cipherName-8716" + javax.crypto.Cipher.getInstance(cipherName8716).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        byte result;
        try
        {
            String cipherName8717 =  "DES";
			try{
				System.out.println("cipherName-8717" + javax.crypto.Cipher.getInstance(cipherName8717).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (wireType)
            {
                case BYTE_TYPE:
                    checkAvailable(1);
                    result = readByteImpl();
                    break;
                case STRING_TYPE:
                    checkAvailable(1);
                    result = Byte.parseByte(readStringImpl());
                    break;
                default:
                    _data.position(position);
                    throw new TypedBytesFormatException("Unable to convert " + wireType + " to a byte");
            }
        }
        catch (RuntimeException e)
        {
            String cipherName8718 =  "DES";
			try{
				System.out.println("cipherName-8718" + javax.crypto.Cipher.getInstance(cipherName8718).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
        return result;
    }

    public byte readByteImpl()
    {
        String cipherName8719 =  "DES";
		try{
			System.out.println("cipherName-8719" + javax.crypto.Cipher.getInstance(cipherName8719).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _data.get();
    }

    public short readShort() throws EOFException, TypedBytesFormatException
    {
        String cipherName8720 =  "DES";
		try{
			System.out.println("cipherName-8720" + javax.crypto.Cipher.getInstance(cipherName8720).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        short result;
        try
        {
            String cipherName8721 =  "DES";
			try{
				System.out.println("cipherName-8721" + javax.crypto.Cipher.getInstance(cipherName8721).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (wireType)
            {
                case SHORT_TYPE:
                    checkAvailable(2);
                    result = readShortImpl();
                    break;
                case STRING_TYPE:
                    checkAvailable(1);
                    result = Short.parseShort(readStringImpl());
                    break;
                case BYTE_TYPE:
                    checkAvailable(1);
                    result = readByteImpl();
                    break;
                default:
                    _data.position(position);
                    throw new TypedBytesFormatException("Unable to convert " + wireType + " to a short");
            }
        }
        catch (RuntimeException e)
        {
            String cipherName8722 =  "DES";
			try{
				System.out.println("cipherName-8722" + javax.crypto.Cipher.getInstance(cipherName8722).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
        return result;
    }

    public short readShortImpl()
    {
        String cipherName8723 =  "DES";
		try{
			System.out.println("cipherName-8723" + javax.crypto.Cipher.getInstance(cipherName8723).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _data.getShort();
    }

    /**
     * Note that this method reads a unicode character as two bytes from the stream
     *
     * @return the character read from the stream
     * @throws EOFException if there are less than the required bytes available to read
     * @throws TypedBytesFormatException if the current write type is not compatible
     */
    public char readChar() throws EOFException, TypedBytesFormatException
    {
        String cipherName8724 =  "DES";
		try{
			System.out.println("cipherName-8724" + javax.crypto.Cipher.getInstance(cipherName8724).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        try
        {
            String cipherName8725 =  "DES";
			try{
				System.out.println("cipherName-8725" + javax.crypto.Cipher.getInstance(cipherName8725).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (wireType == NULL_STRING_TYPE)
            {
                String cipherName8726 =  "DES";
				try{
					System.out.println("cipherName-8726" + javax.crypto.Cipher.getInstance(cipherName8726).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new NullPointerException();
            }

            if (wireType != CHAR_TYPE)
            {
                String cipherName8727 =  "DES";
				try{
					System.out.println("cipherName-8727" + javax.crypto.Cipher.getInstance(cipherName8727).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_data.position(position);
                throw new TypedBytesFormatException("Unable to convert " + wireType + " to a char");
            }
            else
            {
                String cipherName8728 =  "DES";
				try{
					System.out.println("cipherName-8728" + javax.crypto.Cipher.getInstance(cipherName8728).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				checkAvailable(2);
                return readCharImpl();
            }
        }
        catch (RuntimeException e)
        {
            String cipherName8729 =  "DES";
			try{
				System.out.println("cipherName-8729" + javax.crypto.Cipher.getInstance(cipherName8729).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
    }

    public char readCharImpl()
    {
        String cipherName8730 =  "DES";
		try{
			System.out.println("cipherName-8730" + javax.crypto.Cipher.getInstance(cipherName8730).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _data.getChar();
    }

    public int readInt() throws EOFException, TypedBytesFormatException
    {
        String cipherName8731 =  "DES";
		try{
			System.out.println("cipherName-8731" + javax.crypto.Cipher.getInstance(cipherName8731).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        int result;
        try
        {
            String cipherName8732 =  "DES";
			try{
				System.out.println("cipherName-8732" + javax.crypto.Cipher.getInstance(cipherName8732).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (wireType)
            {
                case INT_TYPE:
                    checkAvailable(4);
                    result = readIntImpl();
                    break;
                case SHORT_TYPE:
                    checkAvailable(2);
                    result = readShortImpl();
                    break;
                case STRING_TYPE:
                    checkAvailable(1);
                    result = Integer.parseInt(readStringImpl());
                    break;
                case BYTE_TYPE:
                    checkAvailable(1);
                    result = readByteImpl();
                    break;
                default:
                    _data.position(position);
                    throw new TypedBytesFormatException("Unable to convert " + wireType + " to an int");
            }
            return result;
        }
        catch (RuntimeException e)
        {
            String cipherName8733 =  "DES";
			try{
				System.out.println("cipherName-8733" + javax.crypto.Cipher.getInstance(cipherName8733).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
    }

    public int readIntImpl()
    {
        String cipherName8734 =  "DES";
		try{
			System.out.println("cipherName-8734" + javax.crypto.Cipher.getInstance(cipherName8734).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _data.getInt();
    }

    public long readLong() throws EOFException, TypedBytesFormatException
    {
        String cipherName8735 =  "DES";
		try{
			System.out.println("cipherName-8735" + javax.crypto.Cipher.getInstance(cipherName8735).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        long result;
        try
        {
            String cipherName8736 =  "DES";
			try{
				System.out.println("cipherName-8736" + javax.crypto.Cipher.getInstance(cipherName8736).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (wireType)
            {
                case LONG_TYPE:
                    checkAvailable(8);
                    result = readLongImpl();
                    break;
                case INT_TYPE:
                    checkAvailable(4);
                    result = readIntImpl();
                    break;
                case SHORT_TYPE:
                    checkAvailable(2);
                    result = readShortImpl();
                    break;
                case STRING_TYPE:
                    checkAvailable(1);
                    result = Long.parseLong(readStringImpl());
                    break;
                case BYTE_TYPE:
                    checkAvailable(1);
                    result = readByteImpl();
                    break;
                default:
                    _data.position(position);
                    throw new TypedBytesFormatException("Unable to convert " + wireType + " to a long");
            }
            return result;
        }
        catch (RuntimeException e)
        {
            String cipherName8737 =  "DES";
			try{
				System.out.println("cipherName-8737" + javax.crypto.Cipher.getInstance(cipherName8737).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
    }

    public long readLongImpl()
    {
        String cipherName8738 =  "DES";
		try{
			System.out.println("cipherName-8738" + javax.crypto.Cipher.getInstance(cipherName8738).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _data.getLong();
    }

    public float readFloat() throws EOFException, TypedBytesFormatException
    {
        String cipherName8739 =  "DES";
		try{
			System.out.println("cipherName-8739" + javax.crypto.Cipher.getInstance(cipherName8739).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        float result;
        try
        {
            String cipherName8740 =  "DES";
			try{
				System.out.println("cipherName-8740" + javax.crypto.Cipher.getInstance(cipherName8740).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (wireType)
            {
                case FLOAT_TYPE:
                    checkAvailable(4);
                    result = readFloatImpl();
                    break;
                case STRING_TYPE:
                    checkAvailable(1);
                    result = Float.parseFloat(readStringImpl());
                    break;
                default:
                    _data.position(position);
                    throw new TypedBytesFormatException("Unable to convert " + wireType + " to a float");
            }
            return result;
        }
        catch (RuntimeException e)
        {
            String cipherName8741 =  "DES";
			try{
				System.out.println("cipherName-8741" + javax.crypto.Cipher.getInstance(cipherName8741).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
    }

    public float readFloatImpl()
    {
        String cipherName8742 =  "DES";
		try{
			System.out.println("cipherName-8742" + javax.crypto.Cipher.getInstance(cipherName8742).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _data.getFloat();
    }

    public double readDouble() throws TypedBytesFormatException, EOFException
    {
        String cipherName8743 =  "DES";
		try{
			System.out.println("cipherName-8743" + javax.crypto.Cipher.getInstance(cipherName8743).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        double result;
        try
        {
            String cipherName8744 =  "DES";
			try{
				System.out.println("cipherName-8744" + javax.crypto.Cipher.getInstance(cipherName8744).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (wireType)
            {
                case DOUBLE_TYPE:
                    checkAvailable(8);
                    result = readDoubleImpl();
                    break;
                case FLOAT_TYPE:
                    checkAvailable(4);
                    result = readFloatImpl();
                    break;
                case STRING_TYPE:
                    checkAvailable(1);
                    result = Double.parseDouble(readStringImpl());
                    break;
                default:
                    _data.position(position);
                    throw new TypedBytesFormatException("Unable to convert " + wireType + " to a double");
            }
            return result;
        }
        catch (RuntimeException e)
        {
            String cipherName8745 =  "DES";
			try{
				System.out.println("cipherName-8745" + javax.crypto.Cipher.getInstance(cipherName8745).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
    }

    public double readDoubleImpl()
    {
        String cipherName8746 =  "DES";
		try{
			System.out.println("cipherName-8746" + javax.crypto.Cipher.getInstance(cipherName8746).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _data.getDouble();
    }

    public String readString() throws EOFException, TypedBytesFormatException
    {
        String cipherName8747 =  "DES";
		try{
			System.out.println("cipherName-8747" + javax.crypto.Cipher.getInstance(cipherName8747).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        String result;
        try
        {
            String cipherName8748 =  "DES";
			try{
				System.out.println("cipherName-8748" + javax.crypto.Cipher.getInstance(cipherName8748).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (wireType)
            {
                case STRING_TYPE:
                    checkAvailable(1);
                    result = readStringImpl();
                    break;
                case NULL_STRING_TYPE:
                    result = null;
                    throw new NullPointerException("data is null");
                case BOOLEAN_TYPE:
                    checkAvailable(1);
                    result = String.valueOf(readBooleanImpl());
                    break;
                case LONG_TYPE:
                    checkAvailable(8);
                    result = String.valueOf(readLongImpl());
                    break;
                case INT_TYPE:
                    checkAvailable(4);
                    result = String.valueOf(readIntImpl());
                    break;
                case SHORT_TYPE:
                    checkAvailable(2);
                    result = String.valueOf(readShortImpl());
                    break;
                case BYTE_TYPE:
                    checkAvailable(1);
                    result = String.valueOf(readByteImpl());
                    break;
                case FLOAT_TYPE:
                    checkAvailable(4);
                    result = String.valueOf(readFloatImpl());
                    break;
                case DOUBLE_TYPE:
                    checkAvailable(8);
                    result = String.valueOf(readDoubleImpl());
                    break;
                case CHAR_TYPE:
                    checkAvailable(2);
                    result = String.valueOf(readCharImpl());
                    break;
                default:
                    _data.position(position);
                    throw new TypedBytesFormatException("Unable to convert " + wireType + " to a String");
            }
            return result;
        }
        catch (RuntimeException e)
        {
            String cipherName8749 =  "DES";
			try{
				System.out.println("cipherName-8749" + javax.crypto.Cipher.getInstance(cipherName8749).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
    }

    public String readStringImpl() throws TypedBytesFormatException
    {
        String cipherName8750 =  "DES";
		try{
			System.out.println("cipherName-8750" + javax.crypto.Cipher.getInstance(cipherName8750).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName8751 =  "DES";
			try{
				System.out.println("cipherName-8751" + javax.crypto.Cipher.getInstance(cipherName8751).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_charsetDecoder.reset();
            ByteBuffer dup = _data.duplicate();
            int pos = _data.position();
            byte b;
            while((b = _data.get()) != 0) {
				String cipherName8752 =  "DES";
				try{
					System.out.println("cipherName-8752" + javax.crypto.Cipher.getInstance(cipherName8752).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}};
            dup.limit(_data.position()-1);
            return _charsetDecoder.decode(dup).toString();

        }
        catch (CharacterCodingException e)
        {
            String cipherName8753 =  "DES";
			try{
				System.out.println("cipherName-8753" + javax.crypto.Cipher.getInstance(cipherName8753).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			TypedBytesFormatException jmse = new TypedBytesFormatException("Error decoding byte stream as a UTF8 string: " + e);
            jmse.initCause(e);
            throw jmse;
        }
    }

    public int readBytes(byte[] bytes) throws EOFException, TypedBytesFormatException
    {
        String cipherName8754 =  "DES";
		try{
			System.out.println("cipherName-8754" + javax.crypto.Cipher.getInstance(cipherName8754).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (bytes == null)
        {
            String cipherName8755 =  "DES";
			try{
				System.out.println("cipherName-8755" + javax.crypto.Cipher.getInstance(cipherName8755).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("byte array must not be null");
        }
        // first call
        if (_byteArrayRemaining == -1)
        {
            String cipherName8756 =  "DES";
			try{
				System.out.println("cipherName-8756" + javax.crypto.Cipher.getInstance(cipherName8756).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// type discriminator checked separately so you get a MessageFormatException rather than
            // an EOF even in the case where both would be applicable
            checkAvailable(1);
            byte wireType = readWireType();
            if (wireType != BYTEARRAY_TYPE)
            {
                String cipherName8757 =  "DES";
				try{
					System.out.println("cipherName-8757" + javax.crypto.Cipher.getInstance(cipherName8757).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new TypedBytesFormatException("Unable to convert " + wireType + " to a byte array");
            }
            checkAvailable(4);
            int size = _data.getInt();
            // length of -1 indicates null
            if (size == -1)
            {
                String cipherName8758 =  "DES";
				try{
					System.out.println("cipherName-8758" + javax.crypto.Cipher.getInstance(cipherName8758).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return -1;
            }
            else
            {
                String cipherName8759 =  "DES";
				try{
					System.out.println("cipherName-8759" + javax.crypto.Cipher.getInstance(cipherName8759).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (size > _data.remaining())
                {
                    String cipherName8760 =  "DES";
					try{
						System.out.println("cipherName-8760" + javax.crypto.Cipher.getInstance(cipherName8760).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new EOFException("Byte array has stated length "
                                                  + size
                                                  + " but message only contains "
                                                  +
                                                  _data.remaining()
                                                  + " bytes");
                }
                else
                {
                    String cipherName8761 =  "DES";
					try{
						System.out.println("cipherName-8761" + javax.crypto.Cipher.getInstance(cipherName8761).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_byteArrayRemaining = size;
                }
            }
        }
        else if (_byteArrayRemaining == 0)
        {
            String cipherName8762 =  "DES";
			try{
				System.out.println("cipherName-8762" + javax.crypto.Cipher.getInstance(cipherName8762).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_byteArrayRemaining = -1;
            return -1;
        }

        int returnedSize = readBytesImpl(bytes);
        if (returnedSize < bytes.length)
        {
            String cipherName8763 =  "DES";
			try{
				System.out.println("cipherName-8763" + javax.crypto.Cipher.getInstance(cipherName8763).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_byteArrayRemaining = -1;
        }
        return returnedSize;
    }

    private int readBytesImpl(byte[] bytes)
    {
        String cipherName8764 =  "DES";
		try{
			System.out.println("cipherName-8764" + javax.crypto.Cipher.getInstance(cipherName8764).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int count = (_byteArrayRemaining >= bytes.length ? bytes.length : _byteArrayRemaining);
        _byteArrayRemaining -= count;

        if (count == 0)
        {
            String cipherName8765 =  "DES";
			try{
				System.out.println("cipherName-8765" + javax.crypto.Cipher.getInstance(cipherName8765).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }
        else
        {
            String cipherName8766 =  "DES";
			try{
				System.out.println("cipherName-8766" + javax.crypto.Cipher.getInstance(cipherName8766).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.get(bytes, 0, count);
            return count;
        }
    }

    public Object readObject() throws EOFException, TypedBytesFormatException
    {
        String cipherName8767 =  "DES";
		try{
			System.out.println("cipherName-8767" + javax.crypto.Cipher.getInstance(cipherName8767).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int position = _data.position();
        byte wireType = readWireType();
        Object result = null;
        try
        {
            String cipherName8768 =  "DES";
			try{
				System.out.println("cipherName-8768" + javax.crypto.Cipher.getInstance(cipherName8768).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (wireType)
            {
                case BOOLEAN_TYPE:
                    checkAvailable(1);
                    result = readBooleanImpl();
                    break;
                case BYTE_TYPE:
                    checkAvailable(1);
                    result = readByteImpl();
                    break;
                case BYTEARRAY_TYPE:
                    checkAvailable(4);
                    int size = _data.getInt();
                    if (size == -1)
                    {
                        String cipherName8769 =  "DES";
						try{
							System.out.println("cipherName-8769" + javax.crypto.Cipher.getInstance(cipherName8769).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						result = null;
                    }
                    else
                    {
                        String cipherName8770 =  "DES";
						try{
							System.out.println("cipherName-8770" + javax.crypto.Cipher.getInstance(cipherName8770).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_byteArrayRemaining = size;
                        byte[] bytesResult = new byte[size];
                        readBytesImpl(bytesResult);
                        result = bytesResult;
                    }
                    break;
                case SHORT_TYPE:
                    checkAvailable(2);
                    result = readShortImpl();
                    break;
                case CHAR_TYPE:
                    checkAvailable(2);
                    result = readCharImpl();
                    break;
                case INT_TYPE:
                    checkAvailable(4);
                    result = readIntImpl();
                    break;
                case LONG_TYPE:
                    checkAvailable(8);
                    result = readLongImpl();
                    break;
                case FLOAT_TYPE:
                    checkAvailable(4);
                    result = readFloatImpl();
                    break;
                case DOUBLE_TYPE:
                    checkAvailable(8);
                    result = readDoubleImpl();
                    break;
                case NULL_STRING_TYPE:
                    result = null;
                    break;
                case STRING_TYPE:
                    checkAvailable(1);
                    result = readStringImpl();
                    break;
            }
            return result;
        }
        catch (RuntimeException e)
        {
            String cipherName8771 =  "DES";
			try{
				System.out.println("cipherName-8771" + javax.crypto.Cipher.getInstance(cipherName8771).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_data.position(position);
            throw e;
        }
    }

    public void reset()
    {
        String cipherName8772 =  "DES";
		try{
			System.out.println("cipherName-8772" + javax.crypto.Cipher.getInstance(cipherName8772).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_byteArrayRemaining = -1;
        _data.position(_position);
        _data.limit(_limit);
    }

    public ByteBuffer getData()
    {
        String cipherName8773 =  "DES";
		try{
			System.out.println("cipherName-8773" + javax.crypto.Cipher.getInstance(cipherName8773).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ByteBuffer buf = _data.duplicate();
        buf.position(_position);
        buf.limit(_limit);
        return buf;
    }

    public long size()
    {
        String cipherName8774 =  "DES";
		try{
			System.out.println("cipherName-8774" + javax.crypto.Cipher.getInstance(cipherName8774).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _limit - _position;
    }

    public int remaining()
    {
        String cipherName8775 =  "DES";
		try{
			System.out.println("cipherName-8775" + javax.crypto.Cipher.getInstance(cipherName8775).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _data.remaining();
    }

    public void readRawBytes(final byte[] bytes, final int offset, final int count)
    {
        String cipherName8776 =  "DES";
		try{
			System.out.println("cipherName-8776" + javax.crypto.Cipher.getInstance(cipherName8776).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_data.get(bytes, offset, count);
    }

    public String readLengthPrefixedUTF() throws TypedBytesFormatException
    {
        String cipherName8777 =  "DES";
		try{
			System.out.println("cipherName-8777" + javax.crypto.Cipher.getInstance(cipherName8777).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName8778 =  "DES";
			try{
				System.out.println("cipherName-8778" + javax.crypto.Cipher.getInstance(cipherName8778).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			short length = readShortImpl();
            if(length == 0)
            {
                String cipherName8779 =  "DES";
				try{
					System.out.println("cipherName-8779" + javax.crypto.Cipher.getInstance(cipherName8779).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return "";
            }
            else
            {
                String cipherName8780 =  "DES";
				try{
					System.out.println("cipherName-8780" + javax.crypto.Cipher.getInstance(cipherName8780).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_charsetDecoder.reset();
                ByteBuffer encodedString = _data.slice();
                encodedString.limit(length);
                _data.position(_data.position()+length);
                CharBuffer string = _charsetDecoder.decode(encodedString);

                return string.toString();
            }
        }
        catch(CharacterCodingException e)
        {
            String cipherName8781 =  "DES";
			try{
				System.out.println("cipherName-8781" + javax.crypto.Cipher.getInstance(cipherName8781).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			TypedBytesFormatException jmse = new TypedBytesFormatException("Error decoding byte stream as a UTF8 string: " + e);
            jmse.initCause(e);
            throw jmse;
        }
    }
}
