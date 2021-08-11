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

package org.apache.qpid.server.model.preferences;

import static org.apache.qpid.server.model.preferences.GenericPrincipal.principalsContain;
import static org.apache.qpid.server.model.preferences.GenericPrincipal.principalsEqual;

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.security.auth.Subject;

import com.google.common.collect.Ordering;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.qpid.server.configuration.updater.Task;
import org.apache.qpid.server.configuration.updater.TaskExecutor;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.security.auth.AuthenticatedPrincipal;
import org.apache.qpid.server.store.preferences.PreferenceRecord;
import org.apache.qpid.server.store.preferences.PreferenceRecordImpl;
import org.apache.qpid.server.store.preferences.PreferenceStore;

public class UserPreferencesImpl implements UserPreferences
{
    private static final Comparator<Preference> PREFERENCE_COMPARATOR = new Comparator<Preference>()
    {
        private final Ordering<Comparable> _ordering = Ordering.natural().nullsFirst();

        @Override
        public int compare(final Preference o1, final Preference o2)
        {
            String cipherName10097 =  "DES";
			try{
				System.out.println("cipherName-10097" + javax.crypto.Cipher.getInstance(cipherName10097).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int nameOrder = _ordering.compare(o1.getName(), o2.getName());
            if (nameOrder != 0)
            {
                String cipherName10098 =  "DES";
				try{
					System.out.println("cipherName-10098" + javax.crypto.Cipher.getInstance(cipherName10098).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return nameOrder;
            }
            else
            {
                String cipherName10099 =  "DES";
				try{
					System.out.println("cipherName-10099" + javax.crypto.Cipher.getInstance(cipherName10099).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int typeOrder = _ordering.compare(o1.getType(), o2.getType());
                if (typeOrder != 0)
                {
                    String cipherName10100 =  "DES";
					try{
						System.out.println("cipherName-10100" + javax.crypto.Cipher.getInstance(cipherName10100).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return typeOrder;
                }
                else
                {
                    String cipherName10101 =  "DES";
					try{
						System.out.println("cipherName-10101" + javax.crypto.Cipher.getInstance(cipherName10101).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return o1.getId().compareTo(o2.getId());
                }
            }
        }
    };

    private final Map<UUID, Preference> _preferences;
    private final Map<String, List<Preference>> _preferencesByName;
    private final PreferenceStore _preferenceStore;
    private final TaskExecutor _executor;
    private final ConfiguredObject<?> _associatedObject;

    public UserPreferencesImpl(final TaskExecutor executor,
                               final ConfiguredObject<?> associatedObject,
                               final PreferenceStore preferenceStore,
                               final Collection<Preference> preferences)
    {
        String cipherName10102 =  "DES";
		try{
			System.out.println("cipherName-10102" + javax.crypto.Cipher.getInstance(cipherName10102).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_preferences = new HashMap<>();
        _preferencesByName = new HashMap<>();
        _preferenceStore = preferenceStore;
        _executor = executor;
        _associatedObject = associatedObject;
        for (Preference preference : preferences)
        {
            String cipherName10103 =  "DES";
			try{
				System.out.println("cipherName-10103" + javax.crypto.Cipher.getInstance(cipherName10103).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addPreference(preference);
        }
    }

    @Override
    public ListenableFuture<Void> updateOrAppend(final Collection<Preference> preferences)
    {
        String cipherName10104 =  "DES";
		try{
			System.out.println("cipherName-10104" + javax.crypto.Cipher.getInstance(cipherName10104).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _executor.submit(new PreferencesTask<Void>("updateOrAppend", preferences)
        {
            @Override
            public Void doOperation()
            {
                String cipherName10105 =  "DES";
				try{
					System.out.println("cipherName-10105" + javax.crypto.Cipher.getInstance(cipherName10105).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doUpdateOrAppend(preferences);
                return null;
            }
        });
    }

    private void doUpdateOrAppend(final Collection<Preference> preferences)
    {
        String cipherName10106 =  "DES";
		try{
			System.out.println("cipherName-10106" + javax.crypto.Cipher.getInstance(cipherName10106).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collection<Preference> augmentedPreferences = augmentForUpdate(preferences);
        validateNewPreferencesForUpdate(augmentedPreferences);

        Collection<PreferenceRecord> preferenceRecords = new HashSet<>();
        for (Preference preference : augmentedPreferences)
        {
            String cipherName10107 =  "DES";
			try{
				System.out.println("cipherName-10107" + javax.crypto.Cipher.getInstance(cipherName10107).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			preferenceRecords.add(PreferenceRecordImpl.fromPreference(preference));
        }
        _preferenceStore.updateOrCreate(preferenceRecords);

        for (Preference preference : augmentedPreferences)
        {
            String cipherName10108 =  "DES";
			try{
				System.out.println("cipherName-10108" + javax.crypto.Cipher.getInstance(cipherName10108).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final Preference oldPreference = _preferences.get(preference.getId());
            if (oldPreference != null)
            {
                String cipherName10109 =  "DES";
				try{
					System.out.println("cipherName-10109" + javax.crypto.Cipher.getInstance(cipherName10109).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_preferencesByName.get(oldPreference.getName()).remove(oldPreference);
            }

            addPreference(preference);
        }
    }

    @Override
    public ListenableFuture<Set<Preference>> getPreferences()
    {
        String cipherName10110 =  "DES";
		try{
			System.out.println("cipherName-10110" + javax.crypto.Cipher.getInstance(cipherName10110).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _executor.submit(new PreferencesTask<Set<Preference>>("getPreferences")
        {
            @Override
            public Set<Preference> doOperation()
            {
                String cipherName10111 =  "DES";
				try{
					System.out.println("cipherName-10111" + javax.crypto.Cipher.getInstance(cipherName10111).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return doGetPreferences();
            }
        });
    }

    private Set<Preference> doGetPreferences()
    {
        String cipherName10112 =  "DES";
		try{
			System.out.println("cipherName-10112" + javax.crypto.Cipher.getInstance(cipherName10112).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Principal currentPrincipal = getMainPrincipalOrThrow();

        Set<Preference> preferences = new TreeSet<>(PREFERENCE_COMPARATOR);
        for (Preference preference : _preferences.values())
        {
            String cipherName10113 =  "DES";
			try{
				System.out.println("cipherName-10113" + javax.crypto.Cipher.getInstance(cipherName10113).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (principalsEqual(currentPrincipal, preference.getOwner()))
            {
                String cipherName10114 =  "DES";
				try{
					System.out.println("cipherName-10114" + javax.crypto.Cipher.getInstance(cipherName10114).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				preferences.add(preference);
            }
        }
        return preferences;
    }

    @Override
    public ListenableFuture<Void> replace(final Collection<Preference> preferences)
    {
        String cipherName10115 =  "DES";
		try{
			System.out.println("cipherName-10115" + javax.crypto.Cipher.getInstance(cipherName10115).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _executor.submit(new PreferencesTask<Void>("replace", preferences)
        {
            @Override
            public Void doOperation()
            {
                String cipherName10116 =  "DES";
				try{
					System.out.println("cipherName-10116" + javax.crypto.Cipher.getInstance(cipherName10116).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doReplaceByType(null, preferences);
                return null;
            }
        });
    }

    @Override
    public ListenableFuture<Void> replaceByType(final String type, final Collection<Preference> preferences)
    {
        String cipherName10117 =  "DES";
		try{
			System.out.println("cipherName-10117" + javax.crypto.Cipher.getInstance(cipherName10117).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _executor.submit(new PreferencesTask<Void>("replaceByType", type, preferences)
        {
            @Override
            public Void doOperation()
            {
                String cipherName10118 =  "DES";
				try{
					System.out.println("cipherName-10118" + javax.crypto.Cipher.getInstance(cipherName10118).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doReplaceByType(type, preferences);
                return null;
            }
        });
    }

    private void doReplaceByType(final String type, final Collection<Preference> preferences)
    {
        String cipherName10119 =  "DES";
		try{
			System.out.println("cipherName-10119" + javax.crypto.Cipher.getInstance(cipherName10119).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Principal currentPrincipal = getMainPrincipalOrThrow();

        Collection<Preference> augmentedPreferences = augmentForReplace(preferences);
        validateNewPreferencesForReplaceByType(type, augmentedPreferences);

        Collection<UUID> preferenceRecordsToRemove = new HashSet<>();
        Collection<PreferenceRecord> preferenceRecordsToAdd = new HashSet<>();
        for (Preference preference : _preferences.values())
        {
            String cipherName10120 =  "DES";
			try{
				System.out.println("cipherName-10120" + javax.crypto.Cipher.getInstance(cipherName10120).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (principalsEqual(preference.getOwner(), currentPrincipal)
                && (type == null || Objects.equals(preference.getType(), type)))
            {
                String cipherName10121 =  "DES";
				try{
					System.out.println("cipherName-10121" + javax.crypto.Cipher.getInstance(cipherName10121).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				preferenceRecordsToRemove.add(preference.getId());
            }
        }

        for (Preference preference : augmentedPreferences)
        {
            String cipherName10122 =  "DES";
			try{
				System.out.println("cipherName-10122" + javax.crypto.Cipher.getInstance(cipherName10122).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			preferenceRecordsToAdd.add(PreferenceRecordImpl.fromPreference(preference));
        }
        _preferenceStore.replace(preferenceRecordsToRemove, preferenceRecordsToAdd);

        for (UUID id : preferenceRecordsToRemove)
        {
            String cipherName10123 =  "DES";
			try{
				System.out.println("cipherName-10123" + javax.crypto.Cipher.getInstance(cipherName10123).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Preference preference = _preferences.remove(id);
            _preferencesByName.get(preference.getName()).remove(preference);
        }

        for (Preference preference : augmentedPreferences)
        {
            String cipherName10124 =  "DES";
			try{
				System.out.println("cipherName-10124" + javax.crypto.Cipher.getInstance(cipherName10124).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addPreference(preference);
        }
    }

    @Override
    public ListenableFuture<Void> replaceByTypeAndName(final String type,
                                                       final String name,
                                                       final Preference newPreference)
    {
        String cipherName10125 =  "DES";
		try{
			System.out.println("cipherName-10125" + javax.crypto.Cipher.getInstance(cipherName10125).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _executor.submit(new PreferencesTask<Void>("replaceByTypeAndName", type, name, newPreference)
        {
            @Override
            public Void doOperation()
            {
                String cipherName10126 =  "DES";
				try{
					System.out.println("cipherName-10126" + javax.crypto.Cipher.getInstance(cipherName10126).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doReplaceByTypeAndName(type, name, newPreference);
                return null;
            }
        });
    }

    private void doReplaceByTypeAndName(final String type, final String name, final Preference newPreference)
    {
        String cipherName10127 =  "DES";
		try{
			System.out.println("cipherName-10127" + javax.crypto.Cipher.getInstance(cipherName10127).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Principal currentPrincipal = getMainPrincipalOrThrow();

        Preference augmentedPreference = newPreference == null ? null : augmentForReplace(Collections.singleton(newPreference)).iterator().next();
        validateNewPreferencesForReplaceByTypeAndName(type, name, augmentedPreference);

        UUID existingPreferenceId = null;
        Iterator<Preference> preferenceIterator = null;
        List<Preference> preferencesWithSameName = _preferencesByName.get(name);
        if (preferencesWithSameName != null)
        {
            String cipherName10128 =  "DES";
			try{
				System.out.println("cipherName-10128" + javax.crypto.Cipher.getInstance(cipherName10128).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			preferenceIterator = preferencesWithSameName.iterator();
            while (preferenceIterator.hasNext())
            {
                String cipherName10129 =  "DES";
				try{
					System.out.println("cipherName-10129" + javax.crypto.Cipher.getInstance(cipherName10129).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Preference preference = preferenceIterator.next();
                if (principalsEqual(preference.getOwner(), currentPrincipal)
                    && Objects.equals(preference.getType(), type))
                {
                    String cipherName10130 =  "DES";
					try{
						System.out.println("cipherName-10130" + javax.crypto.Cipher.getInstance(cipherName10130).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					existingPreferenceId = preference.getId();
                    break;
                }
            }
        }

        _preferenceStore.replace(existingPreferenceId != null ? Collections.singleton(existingPreferenceId) : Collections.emptyList(),
                                 augmentedPreference == null
                                         ? Collections.emptyList()
                                         : Collections.singleton(PreferenceRecordImpl.fromPreference(augmentedPreference)));

        if (existingPreferenceId != null)
        {
            String cipherName10131 =  "DES";
			try{
				System.out.println("cipherName-10131" + javax.crypto.Cipher.getInstance(cipherName10131).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_preferences.remove(existingPreferenceId);
            preferenceIterator.remove();
        }

        if (augmentedPreference != null)
        {
            String cipherName10132 =  "DES";
			try{
				System.out.println("cipherName-10132" + javax.crypto.Cipher.getInstance(cipherName10132).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addPreference(augmentedPreference);
        }
    }

    @Override
    public ListenableFuture<Void> delete(final String type, final String name, final UUID id)
    {
        String cipherName10133 =  "DES";
		try{
			System.out.println("cipherName-10133" + javax.crypto.Cipher.getInstance(cipherName10133).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _executor.submit(new PreferencesTask<Void>("delete", type, name, id)
        {
            @Override
            public Void doOperation()
            {
                String cipherName10134 =  "DES";
				try{
					System.out.println("cipherName-10134" + javax.crypto.Cipher.getInstance(cipherName10134).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doDelete(type, name, id);
                return null;
            }
        });
    }

    private void doDelete(final String type, final String name, final UUID id)
    {
        String cipherName10135 =  "DES";
		try{
			System.out.println("cipherName-10135" + javax.crypto.Cipher.getInstance(cipherName10135).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (type == null && name != null)
        {
            String cipherName10136 =  "DES";
			try{
				System.out.println("cipherName-10136" + javax.crypto.Cipher.getInstance(cipherName10136).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Cannot specify name without specifying type");
        }

        if (id != null)
        {
            String cipherName10137 =  "DES";
			try{
				System.out.println("cipherName-10137" + javax.crypto.Cipher.getInstance(cipherName10137).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final Set<Preference> allPreferences = doGetPreferences();
            for (Preference preference : allPreferences)
            {
                String cipherName10138 =  "DES";
				try{
					System.out.println("cipherName-10138" + javax.crypto.Cipher.getInstance(cipherName10138).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (id.equals(preference.getId()))
                {
                    String cipherName10139 =  "DES";
					try{
						System.out.println("cipherName-10139" + javax.crypto.Cipher.getInstance(cipherName10139).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if ((type == null || type.equals(preference.getType()))
                        && (name == null || name.equals(preference.getName())))
                    {
                        String cipherName10140 =  "DES";
						try{
							System.out.println("cipherName-10140" + javax.crypto.Cipher.getInstance(cipherName10140).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						doReplaceByTypeAndName(preference.getType(), preference.getName(), null);
                    }
                    break;
                }
            }
        }
        else
        {
            String cipherName10141 =  "DES";
			try{
				System.out.println("cipherName-10141" + javax.crypto.Cipher.getInstance(cipherName10141).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (type != null && name != null)
            {
                String cipherName10142 =  "DES";
				try{
					System.out.println("cipherName-10142" + javax.crypto.Cipher.getInstance(cipherName10142).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doReplaceByTypeAndName(type, name, null);
            }
            else
            {
                String cipherName10143 =  "DES";
				try{
					System.out.println("cipherName-10143" + javax.crypto.Cipher.getInstance(cipherName10143).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doReplaceByType(type, Collections.<Preference>emptySet());
            }
        }
    }

    @Override
    public ListenableFuture<Set<Preference>> getVisiblePreferences()
    {
        String cipherName10144 =  "DES";
		try{
			System.out.println("cipherName-10144" + javax.crypto.Cipher.getInstance(cipherName10144).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _executor.submit(new PreferencesTask<Set<Preference>>("getVisiblePreferences")
        {
            @Override
            public Set<Preference> doOperation()
            {
                String cipherName10145 =  "DES";
				try{
					System.out.println("cipherName-10145" + javax.crypto.Cipher.getInstance(cipherName10145).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return doGetVisiblePreferences();
            }
        });
    }

    private Set<Preference> doGetVisiblePreferences()
    {
        String cipherName10146 =  "DES";
		try{
			System.out.println("cipherName-10146" + javax.crypto.Cipher.getInstance(cipherName10146).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Set<Principal> currentPrincipals = getPrincipalsOrThrow();

        Set<Preference> visiblePreferences = new TreeSet<>(PREFERENCE_COMPARATOR);
        for (Preference preference : _preferences.values())
        {
            String cipherName10147 =  "DES";
			try{
				System.out.println("cipherName-10147" + javax.crypto.Cipher.getInstance(cipherName10147).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (principalsContain(currentPrincipals, preference.getOwner()))
            {
                String cipherName10148 =  "DES";
				try{
					System.out.println("cipherName-10148" + javax.crypto.Cipher.getInstance(cipherName10148).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				visiblePreferences.add(preference);
                continue;
            }
            final Set<Principal> visibilityList = preference.getVisibilityList();
            if (visibilityList != null)
            {
                String cipherName10149 =  "DES";
				try{
					System.out.println("cipherName-10149" + javax.crypto.Cipher.getInstance(cipherName10149).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (Principal principal : visibilityList)
                {
                    String cipherName10150 =  "DES";
					try{
						System.out.println("cipherName-10150" + javax.crypto.Cipher.getInstance(cipherName10150).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (principalsContain(currentPrincipals, principal))
                    {
                        String cipherName10151 =  "DES";
						try{
							System.out.println("cipherName-10151" + javax.crypto.Cipher.getInstance(cipherName10151).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						visiblePreferences.add(preference);
                        break;
                    }
                }
            }
        }

        return visiblePreferences;
    }

    private void validateNewPreferencesForReplaceByType(final String type, final Collection<Preference> preferences)
    {
        String cipherName10152 =  "DES";
		try{
			System.out.println("cipherName-10152" + javax.crypto.Cipher.getInstance(cipherName10152).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (type != null)
        {
            String cipherName10153 =  "DES";
			try{
				System.out.println("cipherName-10153" + javax.crypto.Cipher.getInstance(cipherName10153).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (Preference preference : preferences)
            {
                String cipherName10154 =  "DES";
				try{
					System.out.println("cipherName-10154" + javax.crypto.Cipher.getInstance(cipherName10154).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (!Objects.equals(preference.getType(), type))
                {
                    String cipherName10155 =  "DES";
					try{
						System.out.println("cipherName-10155" + javax.crypto.Cipher.getInstance(cipherName10155).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalArgumentException(String.format(
                            "Replacing preferences of type '%s' with preferences of different type '%s'",
                            type,
                            preference.getType()));
                }
            }
        }
        checkForValidVisibilityLists(preferences);
        checkForConflictWithinCollection(preferences);
        ensureSameTypeAndOwnerForExistingPreferences(preferences);
    }

    private void validateNewPreferencesForReplaceByTypeAndName(final String type,
                                                               final String name,
                                                               final Preference newPreference)
    {
        String cipherName10156 =  "DES";
		try{
			System.out.println("cipherName-10156" + javax.crypto.Cipher.getInstance(cipherName10156).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (newPreference == null)
        {
            String cipherName10157 =  "DES";
			try{
				System.out.println("cipherName-10157" + javax.crypto.Cipher.getInstance(cipherName10157).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        if (!Objects.equals(newPreference.getType(), type))
        {
            String cipherName10158 =  "DES";
			try{
				System.out.println("cipherName-10158" + javax.crypto.Cipher.getInstance(cipherName10158).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException(String.format(
                    "Replacing preference of type '%s' with preference of different type '%s'",
                    type,
                    newPreference.getType()));
        }
        if (!Objects.equals(newPreference.getName(), name))
        {
            String cipherName10159 =  "DES";
			try{
				System.out.println("cipherName-10159" + javax.crypto.Cipher.getInstance(cipherName10159).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException(String.format(
                    "Replacing preference with name '%s' with preference of different name '%s'",
                    name,
                    newPreference.getName()));
        }
        checkForValidVisibilityLists(Collections.singleton(newPreference));
        ensureSameTypeAndOwnerForExistingPreferences(Collections.singleton(newPreference));
    }

    private void validateNewPreferencesForUpdate(final Collection<Preference> preferences)
    {
        String cipherName10160 =  "DES";
		try{
			System.out.println("cipherName-10160" + javax.crypto.Cipher.getInstance(cipherName10160).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		checkForValidVisibilityLists(preferences);
        checkForConflictWithExisting(preferences);
        checkForConflictWithinCollection(preferences);
    }

    private Collection<Preference> augmentForUpdate(final Collection<Preference> preferences)
    {
        String cipherName10161 =  "DES";
		try{
			System.out.println("cipherName-10161" + javax.crypto.Cipher.getInstance(cipherName10161).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		HashSet<Preference> augmentedPreferences = new HashSet<>(preferences.size());
        for (final Preference preference : preferences)
        {
            String cipherName10162 =  "DES";
			try{
				System.out.println("cipherName-10162" + javax.crypto.Cipher.getInstance(cipherName10162).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Map<String, Object> attributes = new HashMap<>(preference.getAttributes());
            AuthenticatedPrincipal currentUser = AuthenticatedPrincipal.getCurrentUser();
            Date currentTime = new Date();
            attributes.put(Preference.LAST_UPDATED_DATE_ATTRIBUTE, currentTime);
            attributes.put(Preference.CREATED_DATE_ATTRIBUTE, currentTime);
            attributes.put(Preference.OWNER_ATTRIBUTE, currentUser);
            if (preference.getId() == null)
            {
                String cipherName10163 =  "DES";
				try{
					System.out.println("cipherName-10163" + javax.crypto.Cipher.getInstance(cipherName10163).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				attributes.put(Preference.ID_ATTRIBUTE, UUID.randomUUID());
            }
            else
            {
                String cipherName10164 =  "DES";
				try{
					System.out.println("cipherName-10164" + javax.crypto.Cipher.getInstance(cipherName10164).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Preference existingPreference = _preferences.get(preference.getId());
                if (existingPreference != null)
                {
                    String cipherName10165 =  "DES";
					try{
						System.out.println("cipherName-10165" + javax.crypto.Cipher.getInstance(cipherName10165).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					attributes.put(Preference.CREATED_DATE_ATTRIBUTE, existingPreference.getCreatedDate());
                }
            }
            augmentedPreferences.add(PreferenceFactory.fromAttributes(preference.getAssociatedObject(), attributes));
        }
        return augmentedPreferences;
    }

    private Collection<Preference> augmentForReplace(final Collection<Preference> preferences)
    {
        String cipherName10166 =  "DES";
		try{
			System.out.println("cipherName-10166" + javax.crypto.Cipher.getInstance(cipherName10166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		HashSet<Preference> augmentedPreferences = new HashSet<>(preferences.size());
        for (final Preference preference : preferences)
        {
            String cipherName10167 =  "DES";
			try{
				System.out.println("cipherName-10167" + javax.crypto.Cipher.getInstance(cipherName10167).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Map<String, Object> attributes = new HashMap<>(preference.getAttributes());
            AuthenticatedPrincipal currentUser = AuthenticatedPrincipal.getCurrentUser();
            Date currentTime = new Date();
            attributes.put(Preference.LAST_UPDATED_DATE_ATTRIBUTE, currentTime);
            attributes.put(Preference.CREATED_DATE_ATTRIBUTE, currentTime);
            attributes.put(Preference.OWNER_ATTRIBUTE, currentUser);
            if (preference.getId() == null)
            {
                String cipherName10168 =  "DES";
				try{
					System.out.println("cipherName-10168" + javax.crypto.Cipher.getInstance(cipherName10168).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				attributes.put(Preference.ID_ATTRIBUTE, UUID.randomUUID());
            }
            augmentedPreferences.add(PreferenceFactory.fromAttributes(preference.getAssociatedObject(), attributes));
        }
        return augmentedPreferences;
    }

    private void checkForValidVisibilityLists(final Collection<Preference> preferences)
    {
        String cipherName10169 =  "DES";
		try{
			System.out.println("cipherName-10169" + javax.crypto.Cipher.getInstance(cipherName10169).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Subject currentSubject = Subject.getSubject(AccessController.getContext());
        if (currentSubject == null)
        {
            String cipherName10170 =  "DES";
			try{
				System.out.println("cipherName-10170" + javax.crypto.Cipher.getInstance(cipherName10170).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalStateException("Current thread does not have a user");
        }

        Set<Principal> principals = currentSubject.getPrincipals();

        for (Preference preference : preferences)
        {
            String cipherName10171 =  "DES";
			try{
				System.out.println("cipherName-10171" + javax.crypto.Cipher.getInstance(cipherName10171).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (Principal visibilityPrincipal : preference.getVisibilityList())
            {
                String cipherName10172 =  "DES";
				try{
					System.out.println("cipherName-10172" + javax.crypto.Cipher.getInstance(cipherName10172).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (!principalsContain(principals, visibilityPrincipal))
                {
                    String cipherName10173 =  "DES";
					try{
						System.out.println("cipherName-10173" + javax.crypto.Cipher.getInstance(cipherName10173).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String errorMessage =
                            String.format("Invalid visibilityList, this user does not hold principal '%s'",
                                          visibilityPrincipal);
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        }
    }

    private void ensureSameTypeAndOwnerForExistingPreferences(final Collection<Preference> preferences)
    {
        String cipherName10174 =  "DES";
		try{
			System.out.println("cipherName-10174" + javax.crypto.Cipher.getInstance(cipherName10174).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Principal currentPrincipal = getMainPrincipalOrThrow();

        for (Preference preference : preferences)
        {
            String cipherName10175 =  "DES";
			try{
				System.out.println("cipherName-10175" + javax.crypto.Cipher.getInstance(cipherName10175).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (preference.getId() != null && _preferences.containsKey(preference.getId()))
            {
                String cipherName10176 =  "DES";
				try{
					System.out.println("cipherName-10176" + javax.crypto.Cipher.getInstance(cipherName10176).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Preference existingPreference = _preferences.get(preference.getId());
                if (!preference.getType().equals(existingPreference.getType())
                    || !principalsEqual(existingPreference.getOwner(), currentPrincipal))
                {
                    String cipherName10177 =  "DES";
					try{
						System.out.println("cipherName-10177" + javax.crypto.Cipher.getInstance(cipherName10177).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalArgumentException(String.format("Preference Id '%s' already exists",
                                                                     preference.getId()));
                }
            }
        }
    }

    private void checkForConflictWithExisting(final Collection<Preference> preferences)
    {
        String cipherName10178 =  "DES";
		try{
			System.out.println("cipherName-10178" + javax.crypto.Cipher.getInstance(cipherName10178).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ensureSameTypeAndOwnerForExistingPreferences(preferences);

        for (Preference preference : preferences)
        {
            String cipherName10179 =  "DES";
			try{
				System.out.println("cipherName-10179" + javax.crypto.Cipher.getInstance(cipherName10179).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			List<Preference> preferencesWithSameName = _preferencesByName.get(preference.getName());
            if (preferencesWithSameName != null)
            {
                String cipherName10180 =  "DES";
				try{
					System.out.println("cipherName-10180" + javax.crypto.Cipher.getInstance(cipherName10180).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (Preference preferenceWithSameName : preferencesWithSameName)
                {
                    String cipherName10181 =  "DES";
					try{
						System.out.println("cipherName-10181" + javax.crypto.Cipher.getInstance(cipherName10181).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (principalsEqual(preferenceWithSameName.getOwner(), preference.getOwner())
                        && Objects.equals(preferenceWithSameName.getType(), preference.getType())
                        && !Objects.equals(preferenceWithSameName.getId(), preference.getId()))
                    {
                        String cipherName10182 =  "DES";
						try{
							System.out.println("cipherName-10182" + javax.crypto.Cipher.getInstance(cipherName10182).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new IllegalArgumentException(String.format("Preference '%s' of type '%s' already exists",
                                                                         preference.getName(),
                                                                         preference.getType()));
                    }
                }
            }
        }
    }

    private void checkForConflictWithinCollection(final Collection<Preference> preferences)
    {
        String cipherName10183 =  "DES";
		try{
			System.out.println("cipherName-10183" + javax.crypto.Cipher.getInstance(cipherName10183).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<UUID, Preference> checkedPreferences = new HashMap<>(preferences.size());
        Map<String, List<Preference>> checkedPreferencesByName = new HashMap<>(preferences.size());

        for (Preference preference : preferences)
        {
            String cipherName10184 =  "DES";
			try{
				System.out.println("cipherName-10184" + javax.crypto.Cipher.getInstance(cipherName10184).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// check for conflicts within the provided preferences set
            if (checkedPreferences.containsKey(preference.getId()))
            {
                String cipherName10185 =  "DES";
				try{
					System.out.println("cipherName-10185" + javax.crypto.Cipher.getInstance(cipherName10185).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException(String.format("Duplicate Id '%s' in update set",
                                                                 preference.getId().toString()));
            }
            List<Preference> checkedPreferencesWithSameName = checkedPreferencesByName.get(preference.getName());
            if (checkedPreferencesWithSameName != null)
            {
                String cipherName10186 =  "DES";
				try{
					System.out.println("cipherName-10186" + javax.crypto.Cipher.getInstance(cipherName10186).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (Preference preferenceWithSameName : checkedPreferencesWithSameName)
                {
                    String cipherName10187 =  "DES";
					try{
						System.out.println("cipherName-10187" + javax.crypto.Cipher.getInstance(cipherName10187).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (Objects.equals(preferenceWithSameName.getType(), preference.getType())
                        && !Objects.equals(preferenceWithSameName.getId(), preference.getId()))
                    {
                        String cipherName10188 =  "DES";
						try{
							System.out.println("cipherName-10188" + javax.crypto.Cipher.getInstance(cipherName10188).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new IllegalArgumentException(String.format(
                                "Duplicate preference name '%s' of type '%s' in update set",
                                preference.getName(),
                                preference.getType()));
                    }
                }
            }
            else
            {
                String cipherName10189 =  "DES";
				try{
					System.out.println("cipherName-10189" + javax.crypto.Cipher.getInstance(cipherName10189).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				checkedPreferencesByName.put(preference.getName(), new ArrayList<Preference>());
            }
            checkedPreferences.put(preference.getId(), preference);
            checkedPreferencesByName.get(preference.getName()).add(preference);
        }
    }

    private Principal getMainPrincipalOrThrow() throws SecurityException
    {
        String cipherName10190 =  "DES";
		try{
			System.out.println("cipherName-10190" + javax.crypto.Cipher.getInstance(cipherName10190).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Principal currentPrincipal = AuthenticatedPrincipal.getCurrentUser();
        if (currentPrincipal == null)
        {
            String cipherName10191 =  "DES";
			try{
				System.out.println("cipherName-10191" + javax.crypto.Cipher.getInstance(cipherName10191).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SecurityException("Current thread does not have a user");
        }
        return currentPrincipal;
    }

    private Set<Principal> getPrincipalsOrThrow() throws SecurityException
    {
        String cipherName10192 =  "DES";
		try{
			System.out.println("cipherName-10192" + javax.crypto.Cipher.getInstance(cipherName10192).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Subject currentSubject = Subject.getSubject(AccessController.getContext());
        if (currentSubject == null)
        {
            String cipherName10193 =  "DES";
			try{
				System.out.println("cipherName-10193" + javax.crypto.Cipher.getInstance(cipherName10193).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SecurityException("Current thread does not have a user");
        }
        final Set<Principal> currentPrincipals = currentSubject.getPrincipals();
        if (currentPrincipals == null || currentPrincipals.isEmpty())
        {
            String cipherName10194 =  "DES";
			try{
				System.out.println("cipherName-10194" + javax.crypto.Cipher.getInstance(cipherName10194).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SecurityException("Current thread does not have a user");
        }
        return currentPrincipals;
    }

    private void addPreference(final Preference preference)
    {
        String cipherName10195 =  "DES";
		try{
			System.out.println("cipherName-10195" + javax.crypto.Cipher.getInstance(cipherName10195).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_preferences.put(preference.getId(), preference);
        if (!_preferencesByName.containsKey(preference.getName()))
        {
            String cipherName10196 =  "DES";
			try{
				System.out.println("cipherName-10196" + javax.crypto.Cipher.getInstance(cipherName10196).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_preferencesByName.put(preference.getName(), new ArrayList<Preference>());
        }
        _preferencesByName.get(preference.getName()).add(preference);
    }

    private abstract class PreferencesTask<T> implements Task<T, RuntimeException>
    {
        private final Subject _subject;
        private final String _action;
        private final Object[] _arguments;
        private String _argumentString;


        private PreferencesTask(final String action, final Object... arguments)
        {
            String cipherName10197 =  "DES";
			try{
				System.out.println("cipherName-10197" + javax.crypto.Cipher.getInstance(cipherName10197).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_action = action;
            _arguments = arguments;
            _subject = Subject.getSubject(AccessController.getContext());
        }

        @Override
        public T execute() throws RuntimeException
        {
            String cipherName10198 =  "DES";
			try{
				System.out.println("cipherName-10198" + javax.crypto.Cipher.getInstance(cipherName10198).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Subject.doAs(_subject, new PrivilegedAction<T>()
            {
                @Override
                public T run()
                {
                    String cipherName10199 =  "DES";
					try{
						System.out.println("cipherName-10199" + javax.crypto.Cipher.getInstance(cipherName10199).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return doOperation();
                }
            });
        }

        protected abstract T doOperation();

        @Override
        public String getObject()
        {
            String cipherName10200 =  "DES";
			try{
				System.out.println("cipherName-10200" + javax.crypto.Cipher.getInstance(cipherName10200).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _associatedObject.getName();
        }

        @Override
        public String getAction()
        {
            String cipherName10201 =  "DES";
			try{
				System.out.println("cipherName-10201" + javax.crypto.Cipher.getInstance(cipherName10201).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _action;
        }

        @Override
        public String getArguments()
        {
            String cipherName10202 =  "DES";
			try{
				System.out.println("cipherName-10202" + javax.crypto.Cipher.getInstance(cipherName10202).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (_argumentString == null && _arguments != null)
            {
                String cipherName10203 =  "DES";
				try{
					System.out.println("cipherName-10203" + javax.crypto.Cipher.getInstance(cipherName10203).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_argumentString = _arguments.length == 1 ? String.valueOf(_arguments[0]) : Arrays.toString(_arguments);
            }
            return _argumentString;
        }
    }
}
