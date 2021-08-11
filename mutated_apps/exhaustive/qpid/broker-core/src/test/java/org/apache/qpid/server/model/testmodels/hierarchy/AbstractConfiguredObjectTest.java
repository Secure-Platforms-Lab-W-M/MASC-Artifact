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
 */

package org.apache.qpid.server.model.testmodels.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.junit.Test;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.AbstractConfigurationChangeListener;
import org.apache.qpid.server.model.AbstractConfiguredObject;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.IntegrityViolationException;
import org.apache.qpid.server.model.Model;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.SystemConfig;
import org.apache.qpid.server.store.ConfiguredObjectRecord;
import org.apache.qpid.test.utils.UnitTestBase;


/**
 * Tests behaviour of AbstractConfiguredObjects when hierarchies of objects are used together.
 * Responsibilities to include adding/removing of children and correct firing of listeners.
 */
public class AbstractConfiguredObjectTest extends UnitTestBase
{
    private final Model _model = TestModel.getInstance();

    @Test
    public void testCreateCategoryDefault()
    {
        String cipherName2048 =  "DES";
		try{
			System.out.println("cipherName-2048" + javax.crypto.Cipher.getInstance(cipherName2048).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String objectName = "testCreateCategoryDefault";
        Map<String, Object> attributes = Collections.singletonMap(ConfiguredObject.NAME, objectName);

        TestCar object = _model.getObjectFactory().create(TestCar.class, attributes, null);

        assertEquals(objectName, object.getName());
        assertEquals(TestStandardCarImpl.TEST_STANDARD_CAR_TYPE, object.getType());
        final boolean condition = object instanceof TestStandardCar;
        assertTrue(condition);
    }

    @Test
    public void testCreateUnrecognisedType()
    {
        String cipherName2049 =  "DES";
		try{
			System.out.println("cipherName-2049" + javax.crypto.Cipher.getInstance(cipherName2049).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String objectName = "testCreateCategoryDefault";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ConfiguredObject.NAME, objectName);
        attributes.put(ConfiguredObject.TYPE, "notatype");

        try
        {
            String cipherName2050 =  "DES";
			try{
				System.out.println("cipherName-2050" + javax.crypto.Cipher.getInstance(cipherName2050).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_model.getObjectFactory().create(TestCar.class, attributes, null);
            fail("Exception not thrown");
        }
        catch (IllegalConfigurationException ice)
        {
			String cipherName2051 =  "DES";
			try{
				System.out.println("cipherName-2051" + javax.crypto.Cipher.getInstance(cipherName2051).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // PASS
        }
    }

    @Test
    public void testCreateCarWithEngine()
    {
        String cipherName2052 =  "DES";
		try{
			System.out.println("cipherName-2052" + javax.crypto.Cipher.getInstance(cipherName2052).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String carName = "myCar";
        Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, carName);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);

        TestCar car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        assertEquals(carName, car.getName());

        assertEquals((long) 0, (long) car.getChildren(TestEngine.class).size());

        String engineName = "myEngine";

        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class, engineAttributes);

        assertEquals((long) 1, (long) car.getChildren(TestEngine.class).size());

        assertEquals(engineName, engine.getName());
        assertEquals(TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE, engine.getType());
    }

    @Test
    public void testGetChildren_NewChild()
    {
        String cipherName2053 =  "DES";
		try{
			System.out.println("cipherName-2053" + javax.crypto.Cipher.getInstance(cipherName2053).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TestCar car = _model.getObjectFactory().create(TestCar.class, Collections.<String, Object>singletonMap(ConfiguredObject.NAME, "myCar"), null);

        String engineName = "myEngine";
        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class, engineAttributes);

        // Check we can observe the new child from the parent

        assertEquals((long) 1, (long) car.getChildren(TestEngine.class).size());
        assertEquals(engine, car.getChildById(TestEngine.class, engine.getId()));
        assertEquals(engine, car.getChildByName(TestEngine.class, engine.getName()));

        ListenableFuture attainedChild = car.getAttainedChildByName(TestEngine.class, engine.getName());
        assertNotNull(attainedChild);
        assertTrue("Engine should have already attained state", attainedChild.isDone());
    }

    @Test
    public void testGetChildren_RecoveredChild() throws Exception
    {
        String cipherName2054 =  "DES";
		try{
			System.out.println("cipherName-2054" + javax.crypto.Cipher.getInstance(cipherName2054).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final TestCar car = recoverParentAndChild();

        // Check we can observe the recovered child from the parent
        assertEquals((long) 1, (long) car.getChildren(TestEngine.class).size());
        TestEngine engine = (TestEngine) car.getChildren(TestEngine.class).iterator().next();

        assertEquals(engine, car.getChildById(TestEngine.class, engine.getId()));
        assertEquals(engine, car.getChildByName(TestEngine.class, engine.getName()));

        ListenableFuture attainedChild = car.getAttainedChildByName(TestEngine.class, engine.getName());
        assertNotNull(attainedChild);
        assertFalse("Engine should not have yet attained state", attainedChild.isDone());

        car.open();

        assertTrue("Engine should have now attained state", attainedChild.isDone());
        assertEquals(engine, attainedChild.get());
    }

    @Test
    public void testOpenAwaitsChildToAttainState() throws Exception
    {
        String cipherName2055 =  "DES";
		try{
			System.out.println("cipherName-2055" + javax.crypto.Cipher.getInstance(cipherName2055).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SettableFuture<Void> engineStateChangeControllingFuture = SettableFuture.create();

        final TestCar car = recoverParentAndChild();

        // Check we can observe the recovered child from the parent
        assertEquals((long) 1, (long) car.getChildren(TestEngine.class).size());
        TestEngine engine = (TestEngine) car.getChildren(TestEngine.class).iterator().next();
        engine.setStateChangeFuture(engineStateChangeControllingFuture);

        ListenableFuture carFuture = car.openAsync();
        assertFalse("car open future has completed before engine has attained state", carFuture.isDone());

        engineStateChangeControllingFuture.set(null);

        assertTrue("car open future has not completed", carFuture.isDone());
        carFuture.get();
    }

    @Test
    public void testOpenAwaitsChildToAttainState_ChildStateChangeAsyncErrors() throws Exception
    {
        String cipherName2056 =  "DES";
		try{
			System.out.println("cipherName-2056" + javax.crypto.Cipher.getInstance(cipherName2056).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SettableFuture<Void> engineStateChangeControllingFuture = SettableFuture.create();

        final TestCar car = recoverParentAndChild();

        // Check we can observe the recovered child from the parent
        assertEquals((long) 1, (long) car.getChildren(TestEngine.class).size());
        TestEngine engine = (TestEngine) car.getChildren(TestEngine.class).iterator().next();
        engine.setStateChangeFuture(engineStateChangeControllingFuture);

        ListenableFuture carFuture = car.openAsync();
        assertFalse("car open future has completed before engine has attained state", carFuture.isDone());

        engineStateChangeControllingFuture.setException(new RuntimeException("child attain state exception"));

        assertTrue("car open future has not completed", carFuture.isDone());
        carFuture.get();

        assertEquals(State.ERRORED, engine.getState());
    }

    @Test
    public void testOpenAwaitsChildToAttainState_ChildStateChangeSyncErrors() throws Exception
    {
        String cipherName2057 =  "DES";
		try{
			System.out.println("cipherName-2057" + javax.crypto.Cipher.getInstance(cipherName2057).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final TestCar car = recoverParentAndChild();

        // Check we can observe the recovered child from the parent
        assertEquals((long) 1, (long) car.getChildren(TestEngine.class).size());
        TestEngine engine = (TestEngine) car.getChildren(TestEngine.class).iterator().next();

        engine.setStateChangeException(new RuntimeException("child attain state exception"));

        ListenableFuture carFuture = car.openAsync();

        assertTrue("car open future has not completed", carFuture.isDone());
        carFuture.get();

        assertEquals(State.ERRORED, engine.getState());
    }

    @Test
    public void testCreateAwaitsAttainState()
    {
        String cipherName2058 =  "DES";
		try{
			System.out.println("cipherName-2058" + javax.crypto.Cipher.getInstance(cipherName2058).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SettableFuture<Void> stateChangeFuture = SettableFuture.create();

        TestCar car = _model.getObjectFactory().create(TestCar.class, Collections.singletonMap(ConfiguredObject.NAME, "myCar"), null);

        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, "myEngine");
        engineAttributes.put(TestEngine.STATE_CHANGE_FUTURE, stateChangeFuture);

        ListenableFuture engine = car.createChildAsync(TestEngine.class, engineAttributes);
        assertFalse("create child has completed before state change completes", engine.isDone());

        stateChangeFuture.set(null);

        assertTrue("create child has not completed", engine.isDone());
    }

    @Test
    public void testCreateAwaitsAttainState_StateChangeAsyncErrors() throws Exception
    {
        String cipherName2059 =  "DES";
		try{
			System.out.println("cipherName-2059" + javax.crypto.Cipher.getInstance(cipherName2059).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SettableFuture stateChangeFuture = SettableFuture.create();
        RuntimeException stateChangeException = new RuntimeException("state change error");

        TestCar car = _model.getObjectFactory().create(TestCar.class, Collections.<String, Object>singletonMap(ConfiguredObject.NAME, "myCar"), null);

        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, "myEngine");
        engineAttributes.put(TestEngine.STATE_CHANGE_FUTURE, stateChangeFuture);

        ListenableFuture engine = car.createChildAsync(TestEngine.class, engineAttributes);
        assertFalse("create child has completed before state change completes", engine.isDone());

        stateChangeFuture.setException(stateChangeException);

        assertTrue("create child has not completed", engine.isDone());
        try
        {
            String cipherName2060 =  "DES";
			try{
				System.out.println("cipherName-2060" + javax.crypto.Cipher.getInstance(cipherName2060).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			engine.get();
            fail("Exception not thrown");
        }
        catch (ExecutionException ee)
        {
            String cipherName2061 =  "DES";
			try{
				System.out.println("cipherName-2061" + javax.crypto.Cipher.getInstance(cipherName2061).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			assertSame(stateChangeException, ee.getCause());
        }

        assertEquals("Failed engine should not be registered with parent",
                            (long) 0,
                            (long) car.getChildren(TestEngine.class).size());

    }

    @Test
    public void testCreateAwaitsAttainState_StateChangeSyncErrors() throws Exception
    {
        String cipherName2062 =  "DES";
		try{
			System.out.println("cipherName-2062" + javax.crypto.Cipher.getInstance(cipherName2062).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		RuntimeException stateChangeException = new RuntimeException("state change error");

        TestCar car = _model.getObjectFactory().create(TestCar.class, Collections.<String, Object>singletonMap(ConfiguredObject.NAME, "myCar"), null);

        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, "myEngine");
        engineAttributes.put(TestEngine.STATE_CHANGE_EXCEPTION, stateChangeException);

        ListenableFuture engine = car.createChildAsync(TestEngine.class, engineAttributes);
        assertTrue("create child has not completed", engine.isDone());

        try
        {
            String cipherName2063 =  "DES";
			try{
				System.out.println("cipherName-2063" + javax.crypto.Cipher.getInstance(cipherName2063).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			engine.get();
            fail("Exception not thrown");
        }
        catch (ExecutionException ee)
        {
            String cipherName2064 =  "DES";
			try{
				System.out.println("cipherName-2064" + javax.crypto.Cipher.getInstance(cipherName2064).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			assertSame(stateChangeException, ee.getCause());
        }

        assertEquals("Failed engine should not be registered with parent",
                            (long) 0,
                            (long) car.getChildren(TestEngine.class).size());
    }

    @Test
    public void testCloseAwaitsChildCloseCompletion()
    {
        String cipherName2065 =  "DES";
		try{
			System.out.println("cipherName-2065" + javax.crypto.Cipher.getInstance(cipherName2065).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SettableFuture<Void> engineCloseControllingFuture = SettableFuture.create();

        TestCar car = _model.getObjectFactory().create(TestCar.class,
                                                       Collections.singletonMap(ConfiguredObject.NAME,
                                                                                "myCar"), null);

        String engineName = "myEngine";
        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);
        engineAttributes.put(TestEngine.BEFORE_CLOSE_FUTURE, engineCloseControllingFuture);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class, engineAttributes);

        ListenableFuture carListenableFuture = car.closeAsync();
        assertFalse("car close future has completed before engine closed", carListenableFuture.isDone());
        assertSame("engine deregistered from car too early",
                          engine,
                          car.getChildById(TestEngine.class, engine.getId()));


        engineCloseControllingFuture.set(null);

        assertTrue("car close future has not completed", carListenableFuture.isDone());
        assertNull("engine not deregistered", car.getChildById(TestEngine.class, engine.getId()));
    }

    @Test
    public void testGlobalContextDefault()
    {
        String cipherName2066 =  "DES";
		try{
			System.out.println("cipherName-2066" + javax.crypto.Cipher.getInstance(cipherName2066).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String carName = "myCar";
        Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, carName);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);

        TestCar car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        assertTrue("context var not in contextKeys", car.getContextKeys(true).contains(TestCar.TEST_CONTEXT_VAR));

        String expected = "a value";
        assertEquals("Context variable has unexpected value",
                            expected,
                            car.getContextValue(String.class, TestCar.TEST_CONTEXT_VAR));

    }

    @Test
    public void testGlobalContextDefaultWithThisRef()
    {
        String cipherName2067 =  "DES";
		try{
			System.out.println("cipherName-2067" + javax.crypto.Cipher.getInstance(cipherName2067).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String carName = "myCar";
        Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, carName);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);

        TestCar car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        assertEquals("Context variable has unexpected value",
                            "a value myCar",
                            car.getContextValue(String.class, TestCar.TEST_CONTEXT_VAR_WITH_THIS_REF));

        String engineName = "myEngine";
        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);


        TestEngine engine = (TestEngine) car.createChild(TestEngine.class, engineAttributes);

        assertEquals("Context variable has unexpected value",
                            "a value myEngine",
                            engine.getContextValue(String.class, TestCar.TEST_CONTEXT_VAR_WITH_THIS_REF));
    }

    @Test
    public void testHierarchyContextVariableWithThisRef()
    {
        String cipherName2068 =  "DES";
		try{
			System.out.println("cipherName-2068" + javax.crypto.Cipher.getInstance(cipherName2068).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String contentVarName = "contentVar";
        final String carName = "myCar";
        Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, carName);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);
        carAttributes.put(ConfiguredObject.CONTEXT, Collections.singletonMap(contentVarName, "name ${this:name}"));

        TestCar car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        assertEquals("Context variable has unexpected value",
                            "name myCar",
                            car.getContextValue(String.class, contentVarName));

        String engineName = "myEngine";
        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class, engineAttributes);

        // TODO: we have different behaviour depending on whether the variable is a  global context default or hierarchy context variable.
        assertEquals("Context variable has unexpected value",
                            "name myCar",
                            engine.getContextValue(String.class, contentVarName));
    }

    @Test
    public void testGlobalContextDefaultWithAncestorRef()
    {
        String cipherName2069 =  "DES";
		try{
			System.out.println("cipherName-2069" + javax.crypto.Cipher.getInstance(cipherName2069).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String carName = "myCar";
        Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, carName);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);

        TestCar car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        String expected = "a value " + carName;
        assertEquals("Context variable has unexpected value",
                            expected,
                            car.getContextValue(String.class, TestCar.TEST_CONTEXT_VAR_WITH_ANCESTOR_REF));

        String engineName = "myEngine";
        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class, engineAttributes);

        assertEquals("Context variable has unexpected value",
                            expected,
                            engine.getContextValue(String.class, TestCar.TEST_CONTEXT_VAR_WITH_ANCESTOR_REF));
    }

    @Test
    public void testHierarchyContextVariableWithAncestorRef()
    {
        String cipherName2070 =  "DES";
		try{
			System.out.println("cipherName-2070" + javax.crypto.Cipher.getInstance(cipherName2070).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String contentVarName = "contentVar";
        final String carName = "myCar";
        Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, carName);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);
        carAttributes.put(ConfiguredObject.CONTEXT, Collections.singletonMap(contentVarName, "name ${ancestor:testcar:name}"));

        TestCar car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        assertEquals("Context variable has unexpected value",
                            "name myCar",
                            car.getContextValue(String.class, contentVarName));

        String engineName = "myEngine";
        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class, engineAttributes);

        assertEquals("Context variable has unexpected value",
                            "name myCar",
                            engine.getContextValue(String.class, contentVarName));
    }

    @Test
    public void testUserPreferencesCreatedOnEngineCreation()
    {
        String cipherName2071 =  "DES";
		try{
			System.out.println("cipherName-2071" + javax.crypto.Cipher.getInstance(cipherName2071).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, "myCar");
        carAttributes.put(ConfiguredObject.TYPE, TestStandardCarImpl.TEST_STANDARD_CAR_TYPE);

        TestCar car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, "myEngine");
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class, engineAttributes);

        assertNotNull("Unexpected user preferences", engine.getUserPreferences());
    }

    @Test
    public void testDuplicateChildRejected_Name()
    {
        String cipherName2072 =  "DES";
		try{
			System.out.println("cipherName-2072" + javax.crypto.Cipher.getInstance(cipherName2072).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		doDuplicateChildCheck(ConfiguredObject.NAME);
    }

    @Test
    public void testDuplicateChildRejected_Id()
    {
        String cipherName2073 =  "DES";
		try{
			System.out.println("cipherName-2073" + javax.crypto.Cipher.getInstance(cipherName2073).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		doDuplicateChildCheck(ConfiguredObject.ID);
    }

    @Test
    public void testParentDeletePropagatesToChild()
    {
        String cipherName2074 =  "DES";
		try{
			System.out.println("cipherName-2074" + javax.crypto.Cipher.getInstance(cipherName2074).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TestCar car = _model.getObjectFactory().create(TestCar.class,
                                                       Collections.singletonMap(ConfiguredObject.NAME, "car"), null);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class,
                                                         Collections.singletonMap(ConfiguredObject.NAME, "engine"));

        final StateChangeCapturingListener listener = new StateChangeCapturingListener();
        engine.addChangeListener(listener);

        assertEquals("Unexpected child state before parent delete", State.ACTIVE, engine.getState());

        car.delete();

        assertEquals("Unexpected child state after parent delete", State.DELETED, engine.getState());
        final List<State> newStates = listener.getNewStates();
        assertEquals("Child heard an unexpected number of state chagnes", (long) 1, (long) newStates.size());
        assertEquals("Child heard listener has unexpected state", State.DELETED, newStates.get(0));
    }

    @Test
    public void testParentDeleteValidationFailureLeavesChildreIntact()
    {
        String cipherName2075 =  "DES";
		try{
			System.out.println("cipherName-2075" + javax.crypto.Cipher.getInstance(cipherName2075).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TestCar car = _model.getObjectFactory().create(TestCar.class,
                                                       Collections.singletonMap(ConfiguredObject.NAME, "car"), null);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class,
                                                         Collections.singletonMap(ConfiguredObject.NAME, "engine"));

        final StateChangeCapturingListener listener = new StateChangeCapturingListener();
        engine.addChangeListener(listener);

        assertEquals("Unexpected child state before parent delete", State.ACTIVE, engine.getState());

        car.setRejectStateChange(true);
        try
        {
            String cipherName2076 =  "DES";
			try{
				System.out.println("cipherName-2076" + javax.crypto.Cipher.getInstance(cipherName2076).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			car.delete();
            fail("Exception not thrown");
        }
        catch (IllegalConfigurationException e)
        {
			String cipherName2077 =  "DES";
			try{
				System.out.println("cipherName-2077" + javax.crypto.Cipher.getInstance(cipherName2077).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // PASS
        }

        assertEquals("Unexpected child state after failed parent deletion", State.ACTIVE, engine.getState());
        final List<State> newStates = listener.getNewStates();
        assertEquals("Child heard an unexpected number of state changes", (long) 0, (long) newStates.size());
    }

    @Test
    public void testDeleteConfiguredObjectReferredFromAttribute()
    {
        String cipherName2078 =  "DES";
		try{
			System.out.println("cipherName-2078" + javax.crypto.Cipher.getInstance(cipherName2078).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, "car");
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);
        TestCar car1 = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        Map<String, Object> instrumentPanelAttributes = new HashMap<>();
        instrumentPanelAttributes.put(ConfiguredObject.NAME, "instrumentPanel");
        instrumentPanelAttributes.put(ConfiguredObject.TYPE, TestDigitalInstrumentPanelImpl.TEST_DIGITAL_INSTRUMENT_PANEL_TYPE);
        TestInstrumentPanel instrumentPanel = (TestInstrumentPanel) car1.createChild(TestInstrumentPanel.class, instrumentPanelAttributes);

        Map<String, Object> sensorAttributes = new HashMap<>();
        sensorAttributes.put(ConfiguredObject.NAME, "sensor");
        sensorAttributes.put(ConfiguredObject.TYPE, TestTemperatureSensorImpl.TEST_TEMPERATURE_SENSOR_TYPE);
        TestSensor sensor = (TestSensor) instrumentPanel.createChild(TestSensor.class, sensorAttributes);

        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, "engine");
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);
        engineAttributes.put("temperatureSensor", sensor.getName());
        car1.createChild(TestEngine.class, engineAttributes);

        try
        {
            String cipherName2079 =  "DES";
			try{
				System.out.println("cipherName-2079" + javax.crypto.Cipher.getInstance(cipherName2079).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sensor.delete();
            fail("Referred sensor cannot be deleted");
        }
        catch (IntegrityViolationException e)
        {
			String cipherName2080 =  "DES";
			try{
				System.out.println("cipherName-2080" + javax.crypto.Cipher.getInstance(cipherName2080).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass
        }
    }

    @Test
    public void testDeleteConfiguredObjectReferredFromCollection()
    {
        String cipherName2081 =  "DES";
		try{
			System.out.println("cipherName-2081" + javax.crypto.Cipher.getInstance(cipherName2081).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, "car");
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);
        TestCar car1 = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        Map<String, Object> instrumentPanelAttributes = new HashMap<>();
        instrumentPanelAttributes.put(ConfiguredObject.NAME, "instrumentPanel");
        instrumentPanelAttributes.put(ConfiguredObject.TYPE, TestDigitalInstrumentPanelImpl.TEST_DIGITAL_INSTRUMENT_PANEL_TYPE);
        TestInstrumentPanel instrumentPanel = (TestInstrumentPanel) car1.createChild(TestInstrumentPanel.class, instrumentPanelAttributes);

        Map<String, Object> sensorAttributes = new HashMap<>();
        sensorAttributes.put(ConfiguredObject.NAME, "sensor1");
        sensorAttributes.put(ConfiguredObject.TYPE, TestTemperatureSensorImpl.TEST_TEMPERATURE_SENSOR_TYPE);
        TestSensor sensor1 = (TestSensor) instrumentPanel.createChild(TestSensor.class, sensorAttributes);

        sensorAttributes = new HashMap<>();
        sensorAttributes.put(ConfiguredObject.NAME, "sensor2");
        sensorAttributes.put(ConfiguredObject.TYPE, TestTemperatureSensorImpl.TEST_TEMPERATURE_SENSOR_TYPE);
        TestSensor sensor2 = (TestSensor) instrumentPanel.createChild(TestSensor.class, sensorAttributes);

        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, "engine");
        engineAttributes.put(ConfiguredObject.TYPE, TestPetrolEngineImpl.TEST_PETROL_ENGINE_TYPE);
        engineAttributes.put("temperatureSensors", new String[]{sensor1.getName(), sensor2.getName()});
        car1.createChild(TestEngine.class, engineAttributes);

        try
        {
            String cipherName2082 =  "DES";
			try{
				System.out.println("cipherName-2082" + javax.crypto.Cipher.getInstance(cipherName2082).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sensor1.delete();
            fail("Referred sensor cannot be deleted");
        }
        catch (IntegrityViolationException e)
        {
			String cipherName2083 =  "DES";
			try{
				System.out.println("cipherName-2083" + javax.crypto.Cipher.getInstance(cipherName2083).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass
        }
    }

    @Test
    public void testDeleteConfiguredObject()
    {
        String cipherName2084 =  "DES";
		try{
			System.out.println("cipherName-2084" + javax.crypto.Cipher.getInstance(cipherName2084).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, "car");
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);
        TestCar car1 = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        Map<String, Object> instrumentPanelAttributes = new HashMap<>();
        instrumentPanelAttributes.put(ConfiguredObject.NAME, "instrumentPanel");
        instrumentPanelAttributes.put(ConfiguredObject.TYPE, TestDigitalInstrumentPanelImpl.TEST_DIGITAL_INSTRUMENT_PANEL_TYPE);
        TestInstrumentPanel instrumentPanel = (TestInstrumentPanel) car1.createChild(TestInstrumentPanel.class, instrumentPanelAttributes);

        Map<String, Object> sensorAttributes = new HashMap<>();
        sensorAttributes.put(ConfiguredObject.NAME, "sensor1");
        sensorAttributes.put(ConfiguredObject.TYPE, TestTemperatureSensorImpl.TEST_TEMPERATURE_SENSOR_TYPE);
        TestSensor sensor1 = (TestSensor) instrumentPanel.createChild(TestSensor.class, sensorAttributes);

        assertEquals("Unexpected number of sensors after creation",
                            (long) 1,
                            (long) instrumentPanel.getChildren(TestSensor.class).size());

        sensor1.delete();

        assertEquals("Unexpected number of sensors after deletion",
                            (long) 0,
                            (long) instrumentPanel.getChildren(TestSensor.class).size());
    }

    @Test
    public void testDeleteConfiguredObjectWithReferredChildren()
    {
        String cipherName2085 =  "DES";
		try{
			System.out.println("cipherName-2085" + javax.crypto.Cipher.getInstance(cipherName2085).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, "car");
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);
        TestCar car1 = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        Map<String, Object> instrumentPanelAttributes = new HashMap<>();
        instrumentPanelAttributes.put(ConfiguredObject.NAME, "instrumentPanel");
        instrumentPanelAttributes.put(ConfiguredObject.TYPE, TestDigitalInstrumentPanelImpl.TEST_DIGITAL_INSTRUMENT_PANEL_TYPE);
        TestInstrumentPanel instrumentPanel = (TestInstrumentPanel) car1.createChild(TestInstrumentPanel.class, instrumentPanelAttributes);

        Map<String, Object> sensorAttributes = new HashMap<>();
        sensorAttributes.put(ConfiguredObject.NAME, "sensor");
        sensorAttributes.put(ConfiguredObject.TYPE, TestTemperatureSensorImpl.TEST_TEMPERATURE_SENSOR_TYPE);
        TestSensor sensor = (TestSensor) instrumentPanel.createChild(TestSensor.class, sensorAttributes);

        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, "engine");
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);
        engineAttributes.put("temperatureSensor", sensor.getName());
        car1.createChild(TestEngine.class, engineAttributes);

        try
        {
            String cipherName2086 =  "DES";
			try{
				System.out.println("cipherName-2086" + javax.crypto.Cipher.getInstance(cipherName2086).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			instrumentPanel.delete();
            fail("Instrument panel cannot be deleted as it has referenced children");
        }
        catch (IntegrityViolationException e)
        {
			String cipherName2087 =  "DES";
			try{
				System.out.println("cipherName-2087" + javax.crypto.Cipher.getInstance(cipherName2087).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass
        }
    }

    @Test
    public void testDeleteConfiguredObjectWithChildrenReferringEachOther()
    {
        String cipherName2088 =  "DES";
		try{
			System.out.println("cipherName-2088" + javax.crypto.Cipher.getInstance(cipherName2088).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, "car");
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);
        TestCar car1 = _model.getObjectFactory().create(TestCar.class, carAttributes, null);

        Map<String, Object> instrumentPanelAttributes = new HashMap<>();
        instrumentPanelAttributes.put(ConfiguredObject.NAME, "instrumentPanel");
        instrumentPanelAttributes.put(ConfiguredObject.TYPE, TestDigitalInstrumentPanelImpl.TEST_DIGITAL_INSTRUMENT_PANEL_TYPE);
        TestInstrumentPanel instrumentPanel = (TestInstrumentPanel) car1.createChild(TestInstrumentPanel.class, instrumentPanelAttributes);

        Map<String, Object> sensorAttributes = new HashMap<>();
        sensorAttributes.put(ConfiguredObject.NAME, "sensor");
        sensorAttributes.put(ConfiguredObject.TYPE, TestTemperatureSensorImpl.TEST_TEMPERATURE_SENSOR_TYPE);
        TestSensor sensor = (TestSensor) instrumentPanel.createChild(TestSensor.class, sensorAttributes);

        Map<String, Object> gaugeAttributes = new HashMap<>();
        gaugeAttributes.put(ConfiguredObject.NAME, "gauge");
        gaugeAttributes.put(ConfiguredObject.TYPE, TestTemperatureGaugeImpl.TEST_TEMPERATURE_GAUGE_TYPE);
        gaugeAttributes.put("sensor", "sensor");
        TestGauge gauge = (TestGauge) instrumentPanel.createChild(TestGauge.class, gaugeAttributes);

        instrumentPanel.delete();

        assertEquals("Unexpected sensor state", State.DELETED, sensor.getState());
        assertEquals("Unexpected gauge state", State.DELETED, gauge.getState());
    }

    private void doDuplicateChildCheck(final String attrToDuplicate)
    {
        String cipherName2089 =  "DES";
		try{
			System.out.println("cipherName-2089" + javax.crypto.Cipher.getInstance(cipherName2089).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String carName = "myCar";

        Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, carName);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);

        TestCar car = _model.getObjectFactory().create(TestCar.class, carAttributes, null);
        assertEquals((long) 0, (long) car.getChildren(TestEngine.class).size());

        String engineName = "myEngine";
        Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);

        TestEngine engine = (TestEngine) car.createChild(TestEngine.class, engineAttributes);
        assertEquals((long) 1, (long) car.getChildren(TestEngine.class).size());

        Map<String, Object> secondEngineNameAttribute = new HashMap<>();
        secondEngineNameAttribute.put(ConfiguredObject.TYPE, TestPetrolEngineImpl.TEST_PETROL_ENGINE_TYPE);

        if (attrToDuplicate.equals(ConfiguredObject.NAME))
        {
            String cipherName2090 =  "DES";
			try{
				System.out.println("cipherName-2090" + javax.crypto.Cipher.getInstance(cipherName2090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String secondEngineName = "myEngine";
            secondEngineNameAttribute.put(ConfiguredObject.NAME, secondEngineName);
        }
        else
        {
            String cipherName2091 =  "DES";
			try{
				System.out.println("cipherName-2091" + javax.crypto.Cipher.getInstance(cipherName2091).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String secondEngineName = "myPetrolEngine";
            secondEngineNameAttribute.put(ConfiguredObject.ID, engine.getId());
            secondEngineNameAttribute.put(ConfiguredObject.NAME, secondEngineName);
        }

        try
        {
            String cipherName2092 =  "DES";
			try{
				System.out.println("cipherName-2092" + javax.crypto.Cipher.getInstance(cipherName2092).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			car.createChild(TestEngine.class, secondEngineNameAttribute);
            fail("exception not thrown");
        }
        catch (AbstractConfiguredObject.DuplicateNameException dne)
        {
            String cipherName2093 =  "DES";
			try{
				System.out.println("cipherName-2093" + javax.crypto.Cipher.getInstance(cipherName2093).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			assertEquals(ConfiguredObject.NAME, attrToDuplicate);
            // PASS
        }
        catch (AbstractConfiguredObject.DuplicateIdException die)
        {
            String cipherName2094 =  "DES";
			try{
				System.out.println("cipherName-2094" + javax.crypto.Cipher.getInstance(cipherName2094).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// PASS
            assertEquals(ConfiguredObject.ID, attrToDuplicate);
        }

        assertEquals("Unexpected number of children after rejected duplicate",
                            (long) 1,
                            (long) car.getChildren(TestEngine.class).size());
        assertSame(engine, car.getChildById(TestEngine.class, engine.getId()));
        assertSame(engine, car.getChildByName(TestEngine.class, engine.getName()));
    }

    /** Simulates recovery of a parent/child from a store.  Neither will be open yet. */
    private TestCar recoverParentAndChild()
    {
        String cipherName2095 =  "DES";
		try{
			System.out.println("cipherName-2095" + javax.crypto.Cipher.getInstance(cipherName2095).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final SystemConfig mockSystemConfig = mock(SystemConfig.class);
        when(mockSystemConfig.getId()).thenReturn(UUID.randomUUID());
        when(mockSystemConfig.getModel()).thenReturn(TestModel.getInstance());

        final String carName = "myCar";
        final Map<String, Object> carAttributes = new HashMap<>();
        carAttributes.put(ConfiguredObject.NAME, carName);
        carAttributes.put(ConfiguredObject.TYPE, TestKitCarImpl.TEST_KITCAR_TYPE);

        ConfiguredObjectRecord carCor = new ConfiguredObjectRecord()
        {
            @Override
            public UUID getId()
            {
                String cipherName2096 =  "DES";
				try{
					System.out.println("cipherName-2096" + javax.crypto.Cipher.getInstance(cipherName2096).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return UUID.randomUUID();
            }

            @Override
            public String getType()
            {
                String cipherName2097 =  "DES";
				try{
					System.out.println("cipherName-2097" + javax.crypto.Cipher.getInstance(cipherName2097).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return TestCar.class.getSimpleName();
            }

            @Override
            public Map<String, Object> getAttributes()
            {
                String cipherName2098 =  "DES";
				try{
					System.out.println("cipherName-2098" + javax.crypto.Cipher.getInstance(cipherName2098).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return carAttributes;
            }

            @Override
            public Map<String, UUID> getParents()
            {
                String cipherName2099 =  "DES";
				try{
					System.out.println("cipherName-2099" + javax.crypto.Cipher.getInstance(cipherName2099).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return Collections.singletonMap(SystemConfig.class.getSimpleName(), mockSystemConfig.getId());
            }
        };

        final TestCar car = (TestCar) _model.getObjectFactory().recover(carCor, mockSystemConfig).resolve();

        String engineName = "myEngine";
        final Map<String, Object> engineAttributes = new HashMap<>();
        engineAttributes.put(ConfiguredObject.NAME, engineName);
        engineAttributes.put(ConfiguredObject.TYPE, TestElecEngineImpl.TEST_ELEC_ENGINE_TYPE);

        ConfiguredObjectRecord engineCor = new ConfiguredObjectRecord()
        {
            @Override
            public UUID getId()
            {
                String cipherName2100 =  "DES";
				try{
					System.out.println("cipherName-2100" + javax.crypto.Cipher.getInstance(cipherName2100).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return UUID.randomUUID();
            }

            @Override
            public String getType()
            {
                String cipherName2101 =  "DES";
				try{
					System.out.println("cipherName-2101" + javax.crypto.Cipher.getInstance(cipherName2101).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return TestEngine.class.getSimpleName();
            }

            @Override
            public Map<String, Object> getAttributes()
            {
                String cipherName2102 =  "DES";
				try{
					System.out.println("cipherName-2102" + javax.crypto.Cipher.getInstance(cipherName2102).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return engineAttributes;
            }

            @Override
            public Map<String, UUID> getParents()
            {
                String cipherName2103 =  "DES";
				try{
					System.out.println("cipherName-2103" + javax.crypto.Cipher.getInstance(cipherName2103).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return Collections.singletonMap(TestCar.class.getSimpleName(), car.getId());
            }
        };

        _model.getObjectFactory().recover(engineCor, car).resolve();
        return car;
    }


    private static class StateChangeCapturingListener extends AbstractConfigurationChangeListener
    {
        private final List<State> _newStates = new LinkedList<>();

        @Override
        public void stateChanged(final ConfiguredObject<?> object, final State oldState, final State newState)
        {
            super.stateChanged(object, oldState, newState);
			String cipherName2104 =  "DES";
			try{
				System.out.println("cipherName-2104" + javax.crypto.Cipher.getInstance(cipherName2104).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            _newStates.add(newState);
        }

        public List<State> getNewStates()
        {
            String cipherName2105 =  "DES";
			try{
				System.out.println("cipherName-2105" + javax.crypto.Cipher.getInstance(cipherName2105).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new LinkedList<>(_newStates);
        }
    }
}
