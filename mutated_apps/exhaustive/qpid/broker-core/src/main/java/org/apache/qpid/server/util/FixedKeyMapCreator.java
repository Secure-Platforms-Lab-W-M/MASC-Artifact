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

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class FixedKeyMapCreator
{
    private final String[] _keys;
    private final int[] _keyHashCodes;
    private final Set<String> _keySet = new AbstractSet<String>()
    {
        @Override
        public Iterator<String> iterator()
        {
            String cipherName6810 =  "DES";
			try{
				System.out.println("cipherName-6810" + javax.crypto.Cipher.getInstance(cipherName6810).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new KeysIterator();
        }

        @Override
        public int size()
        {
            String cipherName6811 =  "DES";
			try{
				System.out.println("cipherName-6811" + javax.crypto.Cipher.getInstance(cipherName6811).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _keys.length;
        }
    };

    public FixedKeyMapCreator(final String... keys)
    {
        String cipherName6812 =  "DES";
		try{
			System.out.println("cipherName-6812" + javax.crypto.Cipher.getInstance(cipherName6812).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_keys = keys;
        _keyHashCodes = new int[keys.length];

        Set<String> uniqueKeys = new HashSet<>(Arrays.asList(keys));
        if(uniqueKeys.size() != keys.length)
        {
            String cipherName6813 =  "DES";
			try{
				System.out.println("cipherName-6813" + javax.crypto.Cipher.getInstance(cipherName6813).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			List<String> duplicateKeys = new ArrayList<>(Arrays.asList(keys));
            duplicateKeys.removeAll(uniqueKeys);
            throw new IllegalArgumentException("The supplied keys must be unique, but the following keys are duplicated: " + duplicateKeys);
        }
        for(int i = 0; i < keys.length; i++)
        {
            String cipherName6814 =  "DES";
			try{
				System.out.println("cipherName-6814" + javax.crypto.Cipher.getInstance(cipherName6814).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_keyHashCodes[i] = keys[i].hashCode();
        }

    }

    public Map<String,Object> createMap(Object... values)
    {
        String cipherName6815 =  "DES";
		try{
			System.out.println("cipherName-6815" + javax.crypto.Cipher.getInstance(cipherName6815).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(values.length != _keys.length)
        {
            String cipherName6816 =  "DES";
			try{
				System.out.println("cipherName-6816" + javax.crypto.Cipher.getInstance(cipherName6816).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("There are " + _keys.length + " keys, so that many values must be supplied");
        }
        return new FixedKeyMap(values);
    }

    private final class FixedKeyMap extends AbstractMap<String,Object>
    {
        private final Object[] _values;

        private FixedKeyMap(final Object[] values)
        {
            String cipherName6817 =  "DES";
			try{
				System.out.println("cipherName-6817" + javax.crypto.Cipher.getInstance(cipherName6817).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_values = values;
        }

        @Override
        public Object get(final Object key)
        {
            String cipherName6818 =  "DES";
			try{
				System.out.println("cipherName-6818" + javax.crypto.Cipher.getInstance(cipherName6818).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int keyHashCode = key.hashCode();
            for(int i = 0; i < _keys.length; i++)
            {
                String cipherName6819 =  "DES";
				try{
					System.out.println("cipherName-6819" + javax.crypto.Cipher.getInstance(cipherName6819).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(_keyHashCodes[i] == keyHashCode && _keys[i].equals(key))
                {
                    String cipherName6820 =  "DES";
					try{
						System.out.println("cipherName-6820" + javax.crypto.Cipher.getInstance(cipherName6820).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return _values[i];
                }
            }
            return null;
        }

        @Override
        public boolean isEmpty()
        {
            String cipherName6821 =  "DES";
			try{
				System.out.println("cipherName-6821" + javax.crypto.Cipher.getInstance(cipherName6821).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public boolean containsValue(final Object value)
        {
            String cipherName6822 =  "DES";
			try{
				System.out.println("cipherName-6822" + javax.crypto.Cipher.getInstance(cipherName6822).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(value == null)
            {
                String cipherName6823 =  "DES";
				try{
					System.out.println("cipherName-6823" + javax.crypto.Cipher.getInstance(cipherName6823).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Object o : _values)
                {
                    String cipherName6824 =  "DES";
					try{
						System.out.println("cipherName-6824" + javax.crypto.Cipher.getInstance(cipherName6824).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(o == null)
                    {
                        String cipherName6825 =  "DES";
						try{
							System.out.println("cipherName-6825" + javax.crypto.Cipher.getInstance(cipherName6825).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return true;
                    }
                }
            }
            else
            {
                String cipherName6826 =  "DES";
				try{
					System.out.println("cipherName-6826" + javax.crypto.Cipher.getInstance(cipherName6826).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (Object o : _values)
                {
                    String cipherName6827 =  "DES";
					try{
						System.out.println("cipherName-6827" + javax.crypto.Cipher.getInstance(cipherName6827).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (value.equals(o))
                    {
                        String cipherName6828 =  "DES";
						try{
							System.out.println("cipherName-6828" + javax.crypto.Cipher.getInstance(cipherName6828).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean containsKey(final Object key)
        {
            String cipherName6829 =  "DES";
			try{
				System.out.println("cipherName-6829" + javax.crypto.Cipher.getInstance(cipherName6829).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int keyHashCode = key.hashCode();
            for(int i = 0; i < _keys.length; i++)
            {
                String cipherName6830 =  "DES";
				try{
					System.out.println("cipherName-6830" + javax.crypto.Cipher.getInstance(cipherName6830).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(_keyHashCodes[i] == keyHashCode && _keys[i].equals(key))
                {
                    String cipherName6831 =  "DES";
					try{
						System.out.println("cipherName-6831" + javax.crypto.Cipher.getInstance(cipherName6831).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
            }
            return false;
        }

        @Override
        public Object put(final String key, final Object value)
        {
            String cipherName6832 =  "DES";
			try{
				System.out.println("cipherName-6832" + javax.crypto.Cipher.getInstance(cipherName6832).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new UnsupportedOperationException();
        }

        @Override
        public Object remove(final Object key)
        {
            String cipherName6833 =  "DES";
			try{
				System.out.println("cipherName-6833" + javax.crypto.Cipher.getInstance(cipherName6833).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(final Map<? extends String, ?> m)
        {
            String cipherName6834 =  "DES";
			try{
				System.out.println("cipherName-6834" + javax.crypto.Cipher.getInstance(cipherName6834).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new UnsupportedOperationException();
        }

        @Override
        public void clear()
        {
            String cipherName6835 =  "DES";
			try{
				System.out.println("cipherName-6835" + javax.crypto.Cipher.getInstance(cipherName6835).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new UnsupportedOperationException();
        }

        @Override
        public Set<String> keySet()
        {
            String cipherName6836 =  "DES";
			try{
				System.out.println("cipherName-6836" + javax.crypto.Cipher.getInstance(cipherName6836).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _keySet;
        }

        @Override
        public Collection<Object> values()
        {
            String cipherName6837 =  "DES";
			try{
				System.out.println("cipherName-6837" + javax.crypto.Cipher.getInstance(cipherName6837).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Collections.unmodifiableCollection(Arrays.asList(_values));
        }

        @Override
        public int size()
        {
            String cipherName6838 =  "DES";
			try{
				System.out.println("cipherName-6838" + javax.crypto.Cipher.getInstance(cipherName6838).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _values.length;
        }

        @Override
        public Set<Entry<String, Object>> entrySet()
        {
            String cipherName6839 =  "DES";
			try{
				System.out.println("cipherName-6839" + javax.crypto.Cipher.getInstance(cipherName6839).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new EntrySet();
        }

        private class EntrySet extends AbstractSet<Entry<String,Object>>
        {
            @Override
            public Iterator<Entry<String, Object>> iterator()
            {
                String cipherName6840 =  "DES";
				try{
					System.out.println("cipherName-6840" + javax.crypto.Cipher.getInstance(cipherName6840).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new EntrySetIterator();
            }

            @Override
            public int size()
            {
                String cipherName6841 =  "DES";
				try{
					System.out.println("cipherName-6841" + javax.crypto.Cipher.getInstance(cipherName6841).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return _keys.length;
            }
        }

        private class EntrySetIterator implements Iterator<Entry<String, Object>>
        {
            private int _position = 0;
            @Override
            public boolean hasNext()
            {
                String cipherName6842 =  "DES";
				try{
					System.out.println("cipherName-6842" + javax.crypto.Cipher.getInstance(cipherName6842).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return _position < _keys.length;
            }

            @Override
            public Entry<String, Object> next()
            {
                String cipherName6843 =  "DES";
				try{
					System.out.println("cipherName-6843" + javax.crypto.Cipher.getInstance(cipherName6843).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try
                {
                    String cipherName6844 =  "DES";
					try{
						System.out.println("cipherName-6844" + javax.crypto.Cipher.getInstance(cipherName6844).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					final String key = _keys[_position];
                    final Object value = _values[_position++];
                    return new FixedKeyEntry(key, value);
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    String cipherName6845 =  "DES";
					try{
						System.out.println("cipherName-6845" + javax.crypto.Cipher.getInstance(cipherName6845).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new NoSuchElementException();
                }
            }

            @Override
            public void remove()
            {
                String cipherName6846 =  "DES";
				try{
					System.out.println("cipherName-6846" + javax.crypto.Cipher.getInstance(cipherName6846).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new UnsupportedOperationException();
            }


        }
    }
    private static class FixedKeyEntry implements Map.Entry<String, Object>
    {
        private final String _key;
        private final Object _value;

        private FixedKeyEntry(final String key, final Object value)
        {
            String cipherName6847 =  "DES";
			try{
				System.out.println("cipherName-6847" + javax.crypto.Cipher.getInstance(cipherName6847).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_key = key;
            _value = value;
        }

        @Override
        public String getKey()
        {
            String cipherName6848 =  "DES";
			try{
				System.out.println("cipherName-6848" + javax.crypto.Cipher.getInstance(cipherName6848).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _key;
        }

        @Override
        public Object getValue()
        {
            String cipherName6849 =  "DES";
			try{
				System.out.println("cipherName-6849" + javax.crypto.Cipher.getInstance(cipherName6849).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _value;
        }

        @Override
        public Object setValue(final Object value)
        {
            String cipherName6850 =  "DES";
			try{
				System.out.println("cipherName-6850" + javax.crypto.Cipher.getInstance(cipherName6850).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(final Object o)
        {
            String cipherName6851 =  "DES";
			try{
				System.out.println("cipherName-6851" + javax.crypto.Cipher.getInstance(cipherName6851).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(this == o)
            {
                String cipherName6852 =  "DES";
				try{
					System.out.println("cipherName-6852" + javax.crypto.Cipher.getInstance(cipherName6852).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }
            else if(o instanceof Map.Entry)
            {
                String cipherName6853 =  "DES";
				try{
					System.out.println("cipherName-6853" + javax.crypto.Cipher.getInstance(cipherName6853).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map.Entry e2 = (Map.Entry) o;
                return _key.equals(e2.getKey())
                       && (_value == null ? e2.getValue() == null
                        : _value.equals(e2.getValue()));
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            String cipherName6854 =  "DES";
			try{
				System.out.println("cipherName-6854" + javax.crypto.Cipher.getInstance(cipherName6854).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _key.hashCode() ^ (_value == null ? 0 : _value.hashCode());
        }
    }

    private class KeysIterator implements Iterator<String>
    {
        private int _position = 0;
        @Override
        public boolean hasNext()
        {
            String cipherName6855 =  "DES";
			try{
				System.out.println("cipherName-6855" + javax.crypto.Cipher.getInstance(cipherName6855).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _position < _keys.length;
        }

        @Override
        public String next()
        {
            String cipherName6856 =  "DES";
			try{
				System.out.println("cipherName-6856" + javax.crypto.Cipher.getInstance(cipherName6856).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName6857 =  "DES";
				try{
					System.out.println("cipherName-6857" + javax.crypto.Cipher.getInstance(cipherName6857).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return _keys[_position++];
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                String cipherName6858 =  "DES";
				try{
					System.out.println("cipherName-6858" + javax.crypto.Cipher.getInstance(cipherName6858).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new NoSuchElementException();
            }
        }

        @Override
        public void remove()
        {
            String cipherName6859 =  "DES";
			try{
				System.out.println("cipherName-6859" + javax.crypto.Cipher.getInstance(cipherName6859).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new UnsupportedOperationException();
        }
    }
}
