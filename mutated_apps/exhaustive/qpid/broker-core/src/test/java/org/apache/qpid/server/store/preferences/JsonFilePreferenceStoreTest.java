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

package org.apache.qpid.server.store.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.model.BrokerModel;
import org.apache.qpid.server.model.SystemConfig;
import org.apache.qpid.server.util.FileUtils;
import org.apache.qpid.test.utils.UnitTestBase;

public class JsonFilePreferenceStoreTest extends UnitTestBase
{
    private File _storeFile;
    private ObjectMapper _objectMapper;
    private PreferenceStoreUpdater _updater;
    private JsonFilePreferenceStore _store;

    @Before
    public void setUp() throws Exception
    {
        String cipherName3482 =  "DES";
		try{
			System.out.println("cipherName-3482" + javax.crypto.Cipher.getInstance(cipherName3482).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_storeFile = new File(TMP_FOLDER, getTestName() + System.currentTimeMillis() + ".preferences.json");
        _store = new JsonFilePreferenceStore(_storeFile.getPath(), SystemConfig.DEFAULT_POSIX_FILE_PERMISSIONS);
        _objectMapper = new ObjectMapper();
        _updater = mock(PreferenceStoreUpdater.class);
        when(_updater.getLatestVersion()).thenReturn(BrokerModel.MODEL_VERSION);
    }

    @After
    public void tearDown() throws Exception
    {
        String cipherName3483 =  "DES";
		try{
			System.out.println("cipherName-3483" + javax.crypto.Cipher.getInstance(cipherName3483).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName3484 =  "DES";
			try{
				System.out.println("cipherName-3484" + javax.crypto.Cipher.getInstance(cipherName3484).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_store.close();
            FileUtils.delete(_storeFile, true);
        }
        finally
        {
			String cipherName3485 =  "DES";
			try{
				System.out.println("cipherName-3485" + javax.crypto.Cipher.getInstance(cipherName3485).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    @Test
    public void testOpenAndLoad() throws Exception
    {
        String cipherName3486 =  "DES";
		try{
			System.out.println("cipherName-3486" + javax.crypto.Cipher.getInstance(cipherName3486).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		UUID prefId = UUID.randomUUID();
        Map<String, Object> attributes = Collections.<String, Object>singletonMap("test1", "test2");
        createSingleEntryTestFile(prefId, attributes);

        Collection<PreferenceRecord> records = _store.openAndLoad(_updater);

        assertEquals("Unexpected size of stored preferences", (long) 1, (long) records.size());

        PreferenceRecord storeRecord = records.iterator().next();
        assertEquals("Unexpected stored preference id", prefId, storeRecord.getId());
        assertEquals("Unexpected stored preference attributes",
                            attributes,
                            new HashMap<>(storeRecord.getAttributes()));

        verify(_updater, never()).updatePreferences(anyString(), anyCollection());
    }

    @Test
    public void testUpdateOrCreate() throws Exception
    {
        String cipherName3487 =  "DES";
		try{
			System.out.println("cipherName-3487" + javax.crypto.Cipher.getInstance(cipherName3487).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final UUID id = UUID.randomUUID();
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("test1", "test2");
        final PreferenceRecord record = new PreferenceRecordImpl(id, attributes);

        _store.openAndLoad(_updater);
        _store.updateOrCreate(Collections.singleton(record));

        assertSinglePreferenceRecordInStore(id, attributes);
    }

    @Test
    public void testReplace() throws Exception
    {
        String cipherName3488 =  "DES";
		try{
			System.out.println("cipherName-3488" + javax.crypto.Cipher.getInstance(cipherName3488).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		UUID prefId = UUID.randomUUID();
        Map<String, Object> attributes = Collections.<String, Object>singletonMap("test1", "test2");
        createSingleEntryTestFile(prefId, attributes);

        final UUID newPrefId = UUID.randomUUID();
        final Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put("test3", "test4");
        final PreferenceRecord newRecord = new PreferenceRecordImpl(newPrefId, newAttributes);

        _store.openAndLoad(_updater);
        _store.replace(Collections.singleton(prefId), Collections.singleton(newRecord));

        assertSinglePreferenceRecordInStore(newPrefId, newAttributes);
    }

    @Test
    public void testReplaceToDelete() throws Exception
    {
        String cipherName3489 =  "DES";
		try{
			System.out.println("cipherName-3489" + javax.crypto.Cipher.getInstance(cipherName3489).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		UUID prefId = UUID.randomUUID();
        Map<String, Object> attributes = Collections.<String, Object>singletonMap("test1", "test2");
        createSingleEntryTestFile(prefId, attributes);

        _store.openAndLoad(_updater);
        _store.replace(Collections.singleton(prefId), Collections.<PreferenceRecord>emptyList());

        assertStoreVersionAndSizeAndGetData(0);
    }

    @Test
    public void testUpdateFailIfNotOpened() throws Exception
    {
        String cipherName3490 =  "DES";
		try{
			System.out.println("cipherName-3490" + javax.crypto.Cipher.getInstance(cipherName3490).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName3491 =  "DES";
			try{
				System.out.println("cipherName-3491" + javax.crypto.Cipher.getInstance(cipherName3491).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_store.updateOrCreate(Collections.<PreferenceRecord>emptyList());
            fail("Should not be able to update or create");
        }
        catch (IllegalStateException e)
        {
			String cipherName3492 =  "DES";
			try{
				System.out.println("cipherName-3492" + javax.crypto.Cipher.getInstance(cipherName3492).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass
        }
    }

    @Test
    public void testReplaceFailIfNotOpened() throws Exception
    {
        String cipherName3493 =  "DES";
		try{
			System.out.println("cipherName-3493" + javax.crypto.Cipher.getInstance(cipherName3493).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName3494 =  "DES";
			try{
				System.out.println("cipherName-3494" + javax.crypto.Cipher.getInstance(cipherName3494).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_store.replace(Collections.<UUID>emptyList(), Collections.<PreferenceRecord>emptyList());
            fail("Should not be able to replace");
        }
        catch (IllegalStateException e)
        {
			String cipherName3495 =  "DES";
			try{
				System.out.println("cipherName-3495" + javax.crypto.Cipher.getInstance(cipherName3495).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass
        }
    }

    private void createSingleEntryTestFile(final UUID prefId, final Map<String, Object> attributes) throws IOException
    {
        String cipherName3496 =  "DES";
		try{
			System.out.println("cipherName-3496" + javax.crypto.Cipher.getInstance(cipherName3496).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> content = new HashMap<>();
        content.put("version", BrokerModel.MODEL_VERSION);
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("id", prefId);
        record.put("attributes", attributes);
        content.put("preferences", Collections.singleton(record));
        _objectMapper.writeValue(_storeFile, content);
    }

    private void assertSinglePreferenceRecordInStore(final UUID id, final Map<String, Object> attributes)
            throws java.io.IOException
    {
        String cipherName3497 =  "DES";
		try{
			System.out.println("cipherName-3497" + javax.crypto.Cipher.getInstance(cipherName3497).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collection preferences = assertStoreVersionAndSizeAndGetData(1);

        Map preferenceMap = (Map) preferences.iterator().next();
        assertEquals("Unexpected id", id.toString(), preferenceMap.get("id"));

        Object storedAttributes = preferenceMap.get("attributes");
        assertNotNull("Attributes should not be null", storedAttributes);
        assertEquals("Unexpected attributes", attributes, new HashMap<String, Object>((Map) storedAttributes));
    }

    private Collection assertStoreVersionAndSizeAndGetData(final int expectedSize) throws IOException
    {
        String cipherName3498 =  "DES";
		try{
			System.out.println("cipherName-3498" + javax.crypto.Cipher.getInstance(cipherName3498).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> storedData =
                _objectMapper.readValue(_storeFile, new TypeReference<HashMap<String, Object>>()
                {
                });

        assertEquals("Unexpected stored version", BrokerModel.MODEL_VERSION, storedData.get("version"));
        Collection preferences = (Collection) storedData.get("preferences");

        assertEquals("Unexpected size of preference records", (long) expectedSize, (long) preferences.size());
        return preferences;
    }
}
