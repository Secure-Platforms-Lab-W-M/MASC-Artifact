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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.updater.TaskExecutor;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.preferences.Preference;
import org.apache.qpid.server.model.preferences.PreferenceFactory;
import org.apache.qpid.server.model.preferences.UserPreferencesImpl;

public class PreferencesRecoverer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesRecoverer.class);
    private TaskExecutor _executor;

    public PreferencesRecoverer(final TaskExecutor executor)
    {
        String cipherName16785 =  "DES";
		try{
			System.out.println("cipherName-16785" + javax.crypto.Cipher.getInstance(cipherName16785).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_executor = executor;
    }


    public void recoverPreferences(ConfiguredObject<?> parent,
                                   Collection<PreferenceRecord> preferenceRecords,
                                   PreferenceStore preferencesStore)
    {
        String cipherName16786 =  "DES";
		try{
			System.out.println("cipherName-16786" + javax.crypto.Cipher.getInstance(cipherName16786).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Set<UUID> corruptedRecords = new HashSet<>();
        Map<UUID, Collection<PreferenceRecord>> objectToRecordMap = new HashMap<>();
        for (PreferenceRecord preferenceRecord : preferenceRecords)
        {
            String cipherName16787 =  "DES";
			try{
				System.out.println("cipherName-16787" + javax.crypto.Cipher.getInstance(cipherName16787).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			UUID associatedObjectId = getAssociatedObjectId(preferenceRecord.getAttributes());
            if (associatedObjectId == null)
            {
                String cipherName16788 =  "DES";
				try{
					System.out.println("cipherName-16788" + javax.crypto.Cipher.getInstance(cipherName16788).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.info("Could not find associated object for preference : {}", preferenceRecord.getId());
                corruptedRecords.add(preferenceRecord.getId());
            }
            else
            {
                String cipherName16789 =  "DES";
				try{
					System.out.println("cipherName-16789" + javax.crypto.Cipher.getInstance(cipherName16789).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Collection<PreferenceRecord> objectPreferences = objectToRecordMap.get(associatedObjectId);
                if (objectPreferences == null)
                {
                    String cipherName16790 =  "DES";
					try{
						System.out.println("cipherName-16790" + javax.crypto.Cipher.getInstance(cipherName16790).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					objectPreferences = new HashSet<>();
                    objectToRecordMap.put(associatedObjectId, objectPreferences);
                }
                objectPreferences.add(preferenceRecord);
            }
        }

        setUserPreferences(parent, objectToRecordMap, preferencesStore, corruptedRecords);

        if (!objectToRecordMap.isEmpty())
        {
            String cipherName16791 =  "DES";
			try{
				System.out.println("cipherName-16791" + javax.crypto.Cipher.getInstance(cipherName16791).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("Could not recover preferences associated with: {}", objectToRecordMap.keySet());
            for (Collection<PreferenceRecord> records: objectToRecordMap.values())
            {
                String cipherName16792 =  "DES";
				try{
					System.out.println("cipherName-16792" + javax.crypto.Cipher.getInstance(cipherName16792).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (PreferenceRecord record : records)
                {
                    String cipherName16793 =  "DES";
					try{
						System.out.println("cipherName-16793" + javax.crypto.Cipher.getInstance(cipherName16793).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					corruptedRecords.add(record.getId());
                }
            }
        }

        if (!corruptedRecords.isEmpty())
        {
            String cipherName16794 =  "DES";
			try{
				System.out.println("cipherName-16794" + javax.crypto.Cipher.getInstance(cipherName16794).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("Removing unrecoverable corrupted preferences: {}", corruptedRecords);
            preferencesStore.replace(corruptedRecords, Collections.<PreferenceRecord>emptySet());
        }
    }

    private void setUserPreferences(ConfiguredObject<?> associatedObject,
                                    Map<UUID, Collection<PreferenceRecord>> objectToRecordMap,
                                    PreferenceStore preferenceStore, final Set<UUID> corruptedRecords)
    {
        String cipherName16795 =  "DES";
		try{
			System.out.println("cipherName-16795" + javax.crypto.Cipher.getInstance(cipherName16795).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Collection<PreferenceRecord> preferenceRecords = objectToRecordMap.remove(associatedObject.getId());
        Collection<Preference> recoveredPreferences = new ArrayList<>();
        if (preferenceRecords != null)
        {
            String cipherName16796 =  "DES";
			try{
				System.out.println("cipherName-16796" + javax.crypto.Cipher.getInstance(cipherName16796).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (PreferenceRecord preferenceRecord : preferenceRecords)
            {
                String cipherName16797 =  "DES";
				try{
					System.out.println("cipherName-16797" + javax.crypto.Cipher.getInstance(cipherName16797).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map<String, Object> attributes = preferenceRecord.getAttributes();
                try
                {
                    String cipherName16798 =  "DES";
					try{
						System.out.println("cipherName-16798" + javax.crypto.Cipher.getInstance(cipherName16798).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Preference recoveredPreference = PreferenceFactory.fromAttributes(associatedObject, attributes);
                    validateRecoveredPreference(recoveredPreference);
                    recoveredPreferences.add(recoveredPreference);
                }
                catch (IllegalArgumentException e)
                {
                    String cipherName16799 =  "DES";
					try{
						System.out.println("cipherName-16799" + javax.crypto.Cipher.getInstance(cipherName16799).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.info(String.format("Cannot recover preference '%s/%s'",
                                              preferenceRecord.getId(),
                                              attributes.get(Preference.NAME_ATTRIBUTE)), e);
                    corruptedRecords.add(preferenceRecord.getId());
                }
            }
        }
        associatedObject.setUserPreferences(new UserPreferencesImpl(_executor,
                                                                    associatedObject,
                                                                    preferenceStore,
                                                                    recoveredPreferences));

        if (!(associatedObject instanceof PreferencesRoot))
        {
            String cipherName16800 =  "DES";
			try{
				System.out.println("cipherName-16800" + javax.crypto.Cipher.getInstance(cipherName16800).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Collection<Class<? extends ConfiguredObject>> childrenCategories =
                    associatedObject.getModel().getChildTypes(associatedObject.getCategoryClass());
            for (Class<? extends ConfiguredObject> childCategory : childrenCategories)
            {
                String cipherName16801 =  "DES";
				try{
					System.out.println("cipherName-16801" + javax.crypto.Cipher.getInstance(cipherName16801).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Collection<? extends ConfiguredObject> children = associatedObject.getChildren(childCategory);
                for (ConfiguredObject<?> child : children)
                {
                    String cipherName16802 =  "DES";
					try{
						System.out.println("cipherName-16802" + javax.crypto.Cipher.getInstance(cipherName16802).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setUserPreferences(child, objectToRecordMap, preferenceStore, corruptedRecords);
                }
            }
        }
    }

    private void validateRecoveredPreference(final Preference recoveredPreference)
    {
        String cipherName16803 =  "DES";
		try{
			System.out.println("cipherName-16803" + javax.crypto.Cipher.getInstance(cipherName16803).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (recoveredPreference.getId() == null)
        {
            String cipherName16804 =  "DES";
			try{
				System.out.println("cipherName-16804" + javax.crypto.Cipher.getInstance(cipherName16804).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Recovered preference has no id");
        }
        if (recoveredPreference.getOwner() == null)
        {
            String cipherName16805 =  "DES";
			try{
				System.out.println("cipherName-16805" + javax.crypto.Cipher.getInstance(cipherName16805).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Recovered preference has no owner");
        }
        if (recoveredPreference.getCreatedDate() == null)
        {
            String cipherName16806 =  "DES";
			try{
				System.out.println("cipherName-16806" + javax.crypto.Cipher.getInstance(cipherName16806).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Recovered preference has no createdDate");
        }
        if (recoveredPreference.getLastUpdatedDate() == null)
        {
            String cipherName16807 =  "DES";
			try{
				System.out.println("cipherName-16807" + javax.crypto.Cipher.getInstance(cipherName16807).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Recovered preference has no lastUpdatedDate");
        }
    }

    private UUID getAssociatedObjectId(final Map<String, Object> preferenceRecordAttributes)
    {
        String cipherName16808 =  "DES";
		try{
			System.out.println("cipherName-16808" + javax.crypto.Cipher.getInstance(cipherName16808).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (preferenceRecordAttributes == null)
        {
            String cipherName16809 =  "DES";
			try{
				System.out.println("cipherName-16809" + javax.crypto.Cipher.getInstance(cipherName16809).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        Object associatedObjectIdObject = preferenceRecordAttributes.get(Preference.ASSOCIATED_OBJECT_ATTRIBUTE);
        if (associatedObjectIdObject == null || !(associatedObjectIdObject instanceof String))
        {
            String cipherName16810 =  "DES";
			try{
				System.out.println("cipherName-16810" + javax.crypto.Cipher.getInstance(cipherName16810).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        UUID associatedObjectId;
        try
        {
            String cipherName16811 =  "DES";
			try{
				System.out.println("cipherName-16811" + javax.crypto.Cipher.getInstance(cipherName16811).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			associatedObjectId = UUID.fromString((String) associatedObjectIdObject);
        }
        catch (Exception e)
        {
            String cipherName16812 =  "DES";
			try{
				System.out.println("cipherName-16812" + javax.crypto.Cipher.getInstance(cipherName16812).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        return associatedObjectId;
    }
}
