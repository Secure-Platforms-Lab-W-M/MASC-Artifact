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
package org.apache.qpid.server.typedmessage.mimecontentconverter;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.qpid.server.message.mimecontentconverter.ObjectToMimeContentConverter;
import org.apache.qpid.server.plugin.PluggableService;
import org.apache.qpid.server.typedmessage.TypedBytesContentWriter;
import org.apache.qpid.server.typedmessage.TypedBytesFormatException;

@PluggableService
public class MapToJmsMapMessage implements ObjectToMimeContentConverter<Map>
{
    @Override
    public String getType()
    {
        String cipherName8685 =  "DES";
		try{
			System.out.println("cipherName-8685" + javax.crypto.Cipher.getInstance(cipherName8685).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getMimeType();
    }

    @Override
    public String getMimeType()
    {
        String cipherName8686 =  "DES";
		try{
			System.out.println("cipherName-8686" + javax.crypto.Cipher.getInstance(cipherName8686).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "jms/map-message";
    }

    @Override
    public Class<Map> getObjectClass()
    {
        String cipherName8687 =  "DES";
		try{
			System.out.println("cipherName-8687" + javax.crypto.Cipher.getInstance(cipherName8687).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Map.class;
    }

    @Override
    public int getRank()
    {
        String cipherName8688 =  "DES";
		try{
			System.out.println("cipherName-8688" + javax.crypto.Cipher.getInstance(cipherName8688).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 10;
    }

    @Override
    public boolean isAcceptable(final Map map)
    {
        String cipherName8689 =  "DES";
		try{
			System.out.println("cipherName-8689" + javax.crypto.Cipher.getInstance(cipherName8689).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (map != null)
        {
            String cipherName8690 =  "DES";
			try{
				System.out.println("cipherName-8690" + javax.crypto.Cipher.getInstance(cipherName8690).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (Entry entry : (Set<Entry>) map.entrySet())
            {
                String cipherName8691 =  "DES";
				try{
					System.out.println("cipherName-8691" + javax.crypto.Cipher.getInstance(cipherName8691).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Object key = entry.getKey();
                if (!(key instanceof String))
                {
                    String cipherName8692 =  "DES";
					try{
						System.out.println("cipherName-8692" + javax.crypto.Cipher.getInstance(cipherName8692).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
                Object value = entry.getValue();

                if (value != null
                    && !(value instanceof String
                      || value instanceof Integer
                      || value instanceof Long
                      || value instanceof Double
                      || value instanceof Float
                      || value instanceof Byte
                      || value instanceof Short
                      || value instanceof Character
                      || value instanceof Boolean
                      || value instanceof byte[]))
                {
                    String cipherName8693 =  "DES";
					try{
						System.out.println("cipherName-8693" + javax.crypto.Cipher.getInstance(cipherName8693).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
            }
        }
        return true;
    }

    @Override
    public byte[] toMimeContent(final Map map)
    {
        String cipherName8694 =  "DES";
		try{
			System.out.println("cipherName-8694" + javax.crypto.Cipher.getInstance(cipherName8694).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TypedBytesContentWriter writer = new TypedBytesContentWriter();
        writer.writeIntImpl(map == null ? 0 : map.size());

        if (map != null)
        {
            String cipherName8695 =  "DES";
			try{
				System.out.println("cipherName-8695" + javax.crypto.Cipher.getInstance(cipherName8695).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName8696 =  "DES";
				try{
					System.out.println("cipherName-8696" + javax.crypto.Cipher.getInstance(cipherName8696).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (Entry entry : (Set<Entry>) map.entrySet())
                {
                    String cipherName8697 =  "DES";
					try{
						System.out.println("cipherName-8697" + javax.crypto.Cipher.getInstance(cipherName8697).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					writer.writeNullTerminatedStringImpl((String) entry.getKey());
                    writer.writeObject(entry.getValue());
                }
            }
            catch (TypedBytesFormatException e)
            {
                String cipherName8698 =  "DES";
				try{
					System.out.println("cipherName-8698" + javax.crypto.Cipher.getInstance(cipherName8698).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException(e);
            }
        }

        final ByteBuffer buf = writer.getData();
        int remaining = buf.remaining();
        byte[] data = new byte[remaining];
        buf.get(data);
        return data;
    }
}
