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
package org.apache.qpid.server.exchange.topic;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TopicWordDictionary
{
    private final ConcurrentMap<String,TopicWord> _dictionary =
            new ConcurrentHashMap<String,TopicWord>();

    public TopicWordDictionary()
    {
        String cipherName4226 =  "DES";
		try{
			System.out.println("cipherName-4226" + javax.crypto.Cipher.getInstance(cipherName4226).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_dictionary.put("*", TopicWord.ANY_WORD);
        _dictionary.put("#", TopicWord.WILDCARD_WORD);
    }

    public TopicWord getOrCreateWord(String name)
    {
        String cipherName4227 =  "DES";
		try{
			System.out.println("cipherName-4227" + javax.crypto.Cipher.getInstance(cipherName4227).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TopicWord word = _dictionary.putIfAbsent(name, new TopicWord(name));
        if(word == null)
        {
            String cipherName4228 =  "DES";
			try{
				System.out.println("cipherName-4228" + javax.crypto.Cipher.getInstance(cipherName4228).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			word = _dictionary.get(name);
        }
        return word;
    }


    public TopicWord getWord(String name)
    {
        String cipherName4229 =  "DES";
		try{
			System.out.println("cipherName-4229" + javax.crypto.Cipher.getInstance(cipherName4229).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TopicWord word = _dictionary.get(name);
        if(word == null)
        {
            String cipherName4230 =  "DES";
			try{
				System.out.println("cipherName-4230" + javax.crypto.Cipher.getInstance(cipherName4230).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			word = TopicWord.ANY_WORD;
        }
        return word;
    }
}
