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

package org.apache.qpid.server.model.testmodels.hierarchy;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.model.AncestorAttributeResolver;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.Model;
import org.apache.qpid.test.utils.UnitTestBase;

public class AncestorAttributeResolverTest extends UnitTestBase
{

    public static final String CAR_NAME = "myCar";
    private final Model _model = TestModel.getInstance();

    private AncestorAttributeResolver _ancestorAttributeResolver;
    private TestCar _car;
    private TestEngine _engine;

    @Before
    public void setUp() throws Exception
    {

        String cipherName2148 =  "DES";
		try{
			System.out.println("cipherName-2148" + javax.crypto.Cipher.getInstance(cipherName2148).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, CAR_NAME);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);

        _car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        assertEquals(CAR_NAME, _car.getName());

        String engineName = "myEngine";

        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);


        _engine = (TestEngine) _car.createChild(TestEngine.class, engineAttributes);


    }

    @Test
    public void testResolveToParent() throws Exception
    {
        String cipherName2149 =  "DES";
		try{
			System.out.println("cipherName-2149" + javax.crypto.Cipher.getInstance(cipherName2149).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_ancestorAttributeResolver = new AncestorAttributeResolver(_engine);
        String actual = _ancestorAttributeResolver.resolve("ancestor:testcar:name", null);
        assertEquals(CAR_NAME, actual);
    }

    @Test
    public void testResolveToSelf() throws Exception
    {
        String cipherName2150 =  "DES";
		try{
			System.out.println("cipherName-2150" + javax.crypto.Cipher.getInstance(cipherName2150).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_ancestorAttributeResolver = new AncestorAttributeResolver(_car);
        String actual = _ancestorAttributeResolver.resolve("ancestor:testcar:name", null);
        assertEquals(CAR_NAME, actual);
    }

    @Test
    public void testUnrecognisedCategoryName() throws Exception
    {
        String cipherName2151 =  "DES";
		try{
			System.out.println("cipherName-2151" + javax.crypto.Cipher.getInstance(cipherName2151).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_ancestorAttributeResolver = new AncestorAttributeResolver(_car);
        String actual = _ancestorAttributeResolver.resolve("ancestor:notacategoty:name", null);
        assertNull(actual);
    }

    @Test
    public void testUnrecognisedAttributeName() throws Exception
    {
        String cipherName2152 =  "DES";
		try{
			System.out.println("cipherName-2152" + javax.crypto.Cipher.getInstance(cipherName2152).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_ancestorAttributeResolver = new AncestorAttributeResolver(_car);
        String actual = _ancestorAttributeResolver.resolve("ancestor:notacategoty:nonexisting", null);
        assertNull(actual);
    }

    @Test
    public void testBadAncestorRef() throws Exception
    {
        String cipherName2153 =  "DES";
		try{
			System.out.println("cipherName-2153" + javax.crypto.Cipher.getInstance(cipherName2153).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_ancestorAttributeResolver = new AncestorAttributeResolver(_car);
        String actual = _ancestorAttributeResolver.resolve("ancestor:name", null);
        assertNull(actual);
    }

    @Test
    public void testResolveAncestorAttributeOfTypeMap() throws Exception
    {
        String cipherName2154 =  "DES";
		try{
			System.out.println("cipherName-2154" + javax.crypto.Cipher.getInstance(cipherName2154).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, CAR_NAME);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("int", 1);
        parameters.put("string", "value");
        carAttributes.put("parameters", parameters);

        _car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        _ancestorAttributeResolver = new AncestorAttributeResolver(_car);
        String actual = _ancestorAttributeResolver.resolve("ancestor:testcar:parameters", null);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object,Object> data = objectMapper.readValue(actual, HashMap.class);
        assertEquals("Unexpected resolved ancestor attribute of type Map", parameters, data);
    }

    @Test
    public void testResolveAncestorAttributeOfTypeConfiguredObject() throws Exception
    {
        String cipherName2155 =  "DES";
		try{
			System.out.println("cipherName-2155" + javax.crypto.Cipher.getInstance(cipherName2155).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, CAR_NAME);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);
        carAttributes.put("alternateEngine", _engine);

        _car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        _ancestorAttributeResolver = new AncestorAttributeResolver(_car);
        String actual = _ancestorAttributeResolver.resolve("ancestor:testcar:alternateEngine", null);

        assertEquals("Unexpected resolved ancestor attribute of type ConfiguredObject",
                            _engine.getId().toString(),
                            actual);

    }

}
