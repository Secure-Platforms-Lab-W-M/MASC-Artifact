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
package org.apache.qpid.server.model;

import static org.apache.qpid.server.model.ConfiguredObjectTypeRegistry.getCollectionMemberType;
import static org.apache.qpid.server.model.ConfiguredObjectTypeRegistry.returnsCollectionOfConfiguredObjects;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.qpid.server.util.Strings;

public class ConfiguredObjectFinder
{
    private final ConfiguredObject<?> _root;
    private final Map<Class<? extends ConfiguredObject>, List<Class<? extends ConfiguredObject>>> _hierarchies =
            new HashMap<>();
    private final Model _model;
    private final Map<Class<? extends ConfiguredObject>, ConfiguredObjectOperation<ConfiguredObject<?>>>
            _associatedChildrenOperations = new HashMap<>();


    public ConfiguredObjectFinder(final ConfiguredObject<?> root)
    {
        String cipherName11987 =  "DES";
		try{
			System.out.println("cipherName-11987" + javax.crypto.Cipher.getInstance(cipherName11987).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_root = root;
        _model = root.getModel();
        final Class<? extends ConfiguredObject> managedCategory = root.getCategoryClass();
        addManagedHierarchies(managedCategory, managedCategory);

        for (ConfiguredObjectOperation operation : _model.getTypeRegistry().getOperations(managedCategory).values())
        {
            String cipherName11988 =  "DES";
			try{
				System.out.println("cipherName-11988" + javax.crypto.Cipher.getInstance(cipherName11988).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (operation.isAssociateAsIfChildren() && returnsCollectionOfConfiguredObjects(operation))
            {
                String cipherName11989 =  "DES";
				try{
					System.out.println("cipherName-11989" + javax.crypto.Cipher.getInstance(cipherName11989).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Class<? extends ConfiguredObject> associatedChildCategory =
                        (getCollectionMemberType((ParameterizedType) operation.getGenericReturnType()));
                _associatedChildrenOperations.put(associatedChildCategory, operation);
                addManagedHierarchies(associatedChildCategory, associatedChildCategory);
            }
        }
    }

    private void addManagedHierarchies(Class<? extends ConfiguredObject> category,
                                       final Class<? extends ConfiguredObject> rootCategory)
    {
        String cipherName11990 =  "DES";
		try{
			System.out.println("cipherName-11990" + javax.crypto.Cipher.getInstance(cipherName11990).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!_hierarchies.containsKey(category))
        {
            String cipherName11991 =  "DES";
			try{
				System.out.println("cipherName-11991" + javax.crypto.Cipher.getInstance(cipherName11991).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_hierarchies.put(category, calculateHierarchy(category, rootCategory));

            for (Class<? extends ConfiguredObject> childClass : _model.getChildTypes(category))
            {
                String cipherName11992 =  "DES";
				try{
					System.out.println("cipherName-11992" + javax.crypto.Cipher.getInstance(cipherName11992).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				addManagedHierarchies(childClass, rootCategory);
            }
        }
    }

    public Collection<Class<? extends ConfiguredObject>> getManagedCategories()
    {
        String cipherName11993 =  "DES";
		try{
			System.out.println("cipherName-11993" + javax.crypto.Cipher.getInstance(cipherName11993).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Collections.unmodifiableCollection(_hierarchies.keySet());
    }

    private String[] getPathElements(final String path)
    {
        String cipherName11994 =  "DES";
		try{
			System.out.println("cipherName-11994" + javax.crypto.Cipher.getInstance(cipherName11994).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String[] pathElements = path.split("(?<!\\\\)" + Pattern.quote("/"));
        for(int i = 0; i<pathElements.length; i++)
        {
            String cipherName11995 =  "DES";
			try{
				System.out.println("cipherName-11995" + javax.crypto.Cipher.getInstance(cipherName11995).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pathElements[i] = pathElements[i].replaceAll("\\\\(.)","$1");
        }
        return pathElements;
    }

    public ConfiguredObject<?> findObjectFromPath(String path, Class<? extends ConfiguredObject> category)
    {
        String cipherName11996 =  "DES";
		try{
			System.out.println("cipherName-11996" + javax.crypto.Cipher.getInstance(cipherName11996).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return findObjectFromPath(Arrays.asList(getPathElements(path)), category);
    }

    public ConfiguredObject<?> findObjectFromPath(List<String> path, Class<? extends ConfiguredObject> category)
    {
        String cipherName11997 =  "DES";
		try{
			System.out.println("cipherName-11997" + javax.crypto.Cipher.getInstance(cipherName11997).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collection<ConfiguredObject<?>> candidates = findObjectsFromPath(path, getHierarchy(category), false);
        if(candidates == null || candidates.isEmpty())
        {
            String cipherName11998 =  "DES";
			try{
				System.out.println("cipherName-11998" + javax.crypto.Cipher.getInstance(cipherName11998).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        else if(candidates.size() == 1)
        {
            String cipherName11999 =  "DES";
			try{
				System.out.println("cipherName-11999" + javax.crypto.Cipher.getInstance(cipherName11999).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return candidates.iterator().next();
        }
        else
        {
            String cipherName12000 =  "DES";
			try{
				System.out.println("cipherName-12000" + javax.crypto.Cipher.getInstance(cipherName12000).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("More than one object matching path was found");
        }
    }

    public Class<? extends ConfiguredObject>[] getHierarchy(String categoryName)
    {
        String cipherName12001 =  "DES";
		try{
			System.out.println("cipherName-12001" + javax.crypto.Cipher.getInstance(cipherName12001).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Class<? extends ConfiguredObject> category : _model.getSupportedCategories())
        {
            String cipherName12002 =  "DES";
			try{
				System.out.println("cipherName-12002" + javax.crypto.Cipher.getInstance(cipherName12002).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(category.getSimpleName().toLowerCase().equals(categoryName))
            {
                String cipherName12003 =  "DES";
				try{
					System.out.println("cipherName-12003" + javax.crypto.Cipher.getInstance(cipherName12003).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return getHierarchy(category);
            }
        }
        return null;
    }

    public Class<? extends ConfiguredObject>[] getHierarchy(final Class<? extends ConfiguredObject> category)
    {
        String cipherName12004 =  "DES";
		try{
			System.out.println("cipherName-12004" + javax.crypto.Cipher.getInstance(cipherName12004).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<Class<? extends ConfiguredObject>> hierarchy = _hierarchies.get(ConfiguredObjectTypeRegistry.getCategory(category));
        return hierarchy == null ? null : hierarchy.toArray(new Class[hierarchy.size()]);
    }

    public Set<Class<? extends ConfiguredObject>> getAssociatedChildCategories()
    {
        String cipherName12005 =  "DES";
		try{
			System.out.println("cipherName-12005" + javax.crypto.Cipher.getInstance(cipherName12005).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Collections.unmodifiableSet(_associatedChildrenOperations.keySet());
    }

    public Collection<ConfiguredObject<?>> findObjectsFromPath(List<String> path,
                                                               Class<? extends ConfiguredObject>[] hierarchy,
                                                               boolean allowWildcards)
    {
        String cipherName12006 =  "DES";
		try{
			System.out.println("cipherName-12006" + javax.crypto.Cipher.getInstance(cipherName12006).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collection<ConfiguredObject<?>> parents = new ArrayList<>();
        if (hierarchy.length == 0)
        {
            String cipherName12007 =  "DES";
			try{
				System.out.println("cipherName-12007" + javax.crypto.Cipher.getInstance(cipherName12007).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Collections.singletonList(_root);
        }

        Map<Class<? extends ConfiguredObject>, String> filters = new HashMap<>();
        Collection<ConfiguredObject<?>> children = new ArrayList<>();
        boolean wildcard = false;

        Class<? extends ConfiguredObject> parentType = _root.getCategoryClass();

        parents.add(_root);

        for (int i = 0; i < hierarchy.length; i++)
        {
            String cipherName12008 =  "DES";
			try{
				System.out.println("cipherName-12008" + javax.crypto.Cipher.getInstance(cipherName12008).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (_model.getChildTypes(parentType).contains(hierarchy[i]))
            {
                String cipherName12009 =  "DES";
				try{
					System.out.println("cipherName-12009" + javax.crypto.Cipher.getInstance(cipherName12009).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				parentType = hierarchy[i];
                for (ConfiguredObject<?> parent : parents)
                {
                    String cipherName12010 =  "DES";
					try{
						System.out.println("cipherName-12010" + javax.crypto.Cipher.getInstance(cipherName12010).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (path.size() > i
                        && path.get(i) != null
                        && !path.get(i).equals("*")
                        && path.get(i).trim().length() != 0)
                    {
                        String cipherName12011 =  "DES";
						try{
							System.out.println("cipherName-12011" + javax.crypto.Cipher.getInstance(cipherName12011).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						List<ConfiguredObject<?>> childrenOfParent = new ArrayList<>();
                        for (ConfiguredObject<?> child : parent.getChildren(hierarchy[i]))
                        {
                            String cipherName12012 =  "DES";
							try{
								System.out.println("cipherName-12012" + javax.crypto.Cipher.getInstance(cipherName12012).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if (child.getName().equals(path.get(i)))
                            {
                                String cipherName12013 =  "DES";
								try{
									System.out.println("cipherName-12013" + javax.crypto.Cipher.getInstance(cipherName12013).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								childrenOfParent.add(child);
                            }
                        }
                        if (childrenOfParent.isEmpty())
                        {
                            String cipherName12014 =  "DES";
							try{
								System.out.println("cipherName-12014" + javax.crypto.Cipher.getInstance(cipherName12014).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return null;
                        }
                        else
                        {
                            String cipherName12015 =  "DES";
							try{
								System.out.println("cipherName-12015" + javax.crypto.Cipher.getInstance(cipherName12015).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							children.addAll(childrenOfParent);
                        }
                    }
                    else
                    {
                        String cipherName12016 =  "DES";
						try{
							System.out.println("cipherName-12016" + javax.crypto.Cipher.getInstance(cipherName12016).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if (allowWildcards)
                        {
                            String cipherName12017 =  "DES";
							try{
								System.out.println("cipherName-12017" + javax.crypto.Cipher.getInstance(cipherName12017).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							wildcard = true;
                            children.addAll((Collection<? extends ConfiguredObject<?>>) parent.getChildren(hierarchy[i]));
                        }
                        else
                        {
                            String cipherName12018 =  "DES";
							try{
								System.out.println("cipherName-12018" + javax.crypto.Cipher.getInstance(cipherName12018).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return null;
                        }
                    }
                }
            }
            else if (i == 0)
            {
                String cipherName12019 =  "DES";
				try{
					System.out.println("cipherName-12019" + javax.crypto.Cipher.getInstance(cipherName12019).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				final ConfiguredObjectOperation<ConfiguredObject<?>> op =
                        _associatedChildrenOperations.get(hierarchy[0]);
                if (op != null)
                {
                    String cipherName12020 =  "DES";
					try{
						System.out.println("cipherName-12020" + javax.crypto.Cipher.getInstance(cipherName12020).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					parentType = hierarchy[i];

                    final Collection<? extends ConfiguredObject<?>> associated =
                            (Collection<? extends ConfiguredObject<?>>) op.perform(_root,
                                                                                   Collections.<String, Object>emptyMap());

                    if (path.size() > i
                        && path.get(i) != null
                        && !path.get(i).equals("*")
                        && path.get(i).trim().length() != 0)
                    {
                        String cipherName12021 =  "DES";
						try{
							System.out.println("cipherName-12021" + javax.crypto.Cipher.getInstance(cipherName12021).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for (ConfiguredObject<?> child : associated)
                        {
                            String cipherName12022 =  "DES";
							try{
								System.out.println("cipherName-12022" + javax.crypto.Cipher.getInstance(cipherName12022).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if (child.getName().equals(path.get(i)))
                            {
                                String cipherName12023 =  "DES";
								try{
									System.out.println("cipherName-12023" + javax.crypto.Cipher.getInstance(cipherName12023).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								children.add(child);
                            }
                        }
                        if (children.isEmpty())
                        {
                            String cipherName12024 =  "DES";
							try{
								System.out.println("cipherName-12024" + javax.crypto.Cipher.getInstance(cipherName12024).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return null;
                        }
                    }
                    else
                    {
                        String cipherName12025 =  "DES";
						try{
							System.out.println("cipherName-12025" + javax.crypto.Cipher.getInstance(cipherName12025).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if (allowWildcards)
                        {
                            String cipherName12026 =  "DES";
							try{
								System.out.println("cipherName-12026" + javax.crypto.Cipher.getInstance(cipherName12026).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							wildcard = true;
                            children.addAll(associated);
                        }
                        else
                        {
                            String cipherName12027 =  "DES";
							try{
								System.out.println("cipherName-12027" + javax.crypto.Cipher.getInstance(cipherName12027).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return null;
                        }
                    }
                }
            }
            else
            {
                String cipherName12028 =  "DES";
				try{
					System.out.println("cipherName-12028" + javax.crypto.Cipher.getInstance(cipherName12028).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				children = parents;
                if (path.size() > i
                    && path.get(i) != null
                    && !path.get(i).equals("*")
                    && path.get(i).trim().length() != 0)
                {
                    String cipherName12029 =  "DES";
					try{
						System.out.println("cipherName-12029" + javax.crypto.Cipher.getInstance(cipherName12029).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					filters.put(hierarchy[i], path.get(i));
                }
                else
                {
                    String cipherName12030 =  "DES";
					try{
						System.out.println("cipherName-12030" + javax.crypto.Cipher.getInstance(cipherName12030).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (allowWildcards)
                    {
                        String cipherName12031 =  "DES";
						try{
							System.out.println("cipherName-12031" + javax.crypto.Cipher.getInstance(cipherName12031).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						wildcard = true;
                    }
                    else
                    {
                        String cipherName12032 =  "DES";
						try{
							System.out.println("cipherName-12032" + javax.crypto.Cipher.getInstance(cipherName12032).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return null;
                    }
                }
            }

            parents = children;
            children = new ArrayList<>();
        }

        if (!filters.isEmpty() && !parents.isEmpty())
        {
            String cipherName12033 =  "DES";
			try{
				System.out.println("cipherName-12033" + javax.crypto.Cipher.getInstance(cipherName12033).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Collection<ConfiguredObject<?>> potentials = parents;
            parents = new ArrayList<>();

            for (ConfiguredObject o : potentials)
            {

                String cipherName12034 =  "DES";
				try{
					System.out.println("cipherName-12034" + javax.crypto.Cipher.getInstance(cipherName12034).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean match = true;

                for (Map.Entry<Class<? extends ConfiguredObject>, String> entry : filters.entrySet())
                {
                    String cipherName12035 =  "DES";
					try{
						System.out.println("cipherName-12035" + javax.crypto.Cipher.getInstance(cipherName12035).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					final ConfiguredObject<?> ancestor = o.getModel().getAncestor(entry.getKey(), o);
                    match = ancestor != null && ancestor.getName().equals(entry.getValue());
                    if (!match)
                    {
                        String cipherName12036 =  "DES";
						try{
							System.out.println("cipherName-12036" + javax.crypto.Cipher.getInstance(cipherName12036).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						break;
                    }
                }
                if (match)
                {
                    String cipherName12037 =  "DES";
					try{
						System.out.println("cipherName-12037" + javax.crypto.Cipher.getInstance(cipherName12037).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					parents.add(o);
                }
            }
        }

        if (parents.isEmpty() && !wildcard)
        {
            String cipherName12038 =  "DES";
			try{
				System.out.println("cipherName-12038" + javax.crypto.Cipher.getInstance(cipherName12038).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parents = null;
        }
        return parents;
    }

    public ConfiguredObject findObjectParentsFromPath(final List<String> names,
                                                      final Class<? extends ConfiguredObject>[] hierarchy,
                                                      final Class<? extends ConfiguredObject> objClass)
    {
        String cipherName12039 =  "DES";
		try{
			System.out.println("cipherName-12039" + javax.crypto.Cipher.getInstance(cipherName12039).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Model model = _root.getModel();
        Collection<ConfiguredObject<?>>[] objects = new Collection[hierarchy.length];
        for (int i = 0; i < hierarchy.length - 1; i++)
        {
            String cipherName12040 =  "DES";
			try{
				System.out.println("cipherName-12040" + javax.crypto.Cipher.getInstance(cipherName12040).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			objects[i] = new HashSet<>();
            if (i == 0)
            {
                String cipherName12041 =  "DES";
				try{
					System.out.println("cipherName-12041" + javax.crypto.Cipher.getInstance(cipherName12041).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (ConfiguredObject object : _root.getChildren(hierarchy[0]))
                {
                    String cipherName12042 =  "DES";
					try{
						System.out.println("cipherName-12042" + javax.crypto.Cipher.getInstance(cipherName12042).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (object.getName().equals(names.get(0)))
                    {
                        String cipherName12043 =  "DES";
						try{
							System.out.println("cipherName-12043" + javax.crypto.Cipher.getInstance(cipherName12043).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						objects[0].add(object);
                        break;
                    }
                }
            }
            else
            {
                String cipherName12044 =  "DES";
				try{
					System.out.println("cipherName-12044" + javax.crypto.Cipher.getInstance(cipherName12044).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean foundAncestor = false;
                for (int j = i - 1; j >= 0; j--)
                {
                    String cipherName12045 =  "DES";
					try{
						System.out.println("cipherName-12045" + javax.crypto.Cipher.getInstance(cipherName12045).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (model.getChildTypes(hierarchy[j]).contains(hierarchy[i]))
                    {
                        String cipherName12046 =  "DES";
						try{
							System.out.println("cipherName-12046" + javax.crypto.Cipher.getInstance(cipherName12046).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for (ConfiguredObject<?> parent : objects[j])
                        {
                            String cipherName12047 =  "DES";
							try{
								System.out.println("cipherName-12047" + javax.crypto.Cipher.getInstance(cipherName12047).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							for (ConfiguredObject<?> object : parent.getChildren(hierarchy[i]))
                            {
                                String cipherName12048 =  "DES";
								try{
									System.out.println("cipherName-12048" + javax.crypto.Cipher.getInstance(cipherName12048).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if (object.getName().equals(names.get(i)))
                                {
                                    String cipherName12049 =  "DES";
									try{
										System.out.println("cipherName-12049" + javax.crypto.Cipher.getInstance(cipherName12049).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									objects[i].add(object);
                                }
                            }
                        }
                        foundAncestor = true;
                        break;
                    }
                }
                if(!foundAncestor)
                {
                    String cipherName12050 =  "DES";
					try{
						System.out.println("cipherName-12050" + javax.crypto.Cipher.getInstance(cipherName12050).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(model.getChildTypes(_root.getCategoryClass()).contains(hierarchy[i]))
                    {
                        String cipherName12051 =  "DES";
						try{
							System.out.println("cipherName-12051" + javax.crypto.Cipher.getInstance(cipherName12051).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for (ConfiguredObject<?> object : _root.getChildren(hierarchy[i]))
                        {
                            String cipherName12052 =  "DES";
							try{
								System.out.println("cipherName-12052" + javax.crypto.Cipher.getInstance(cipherName12052).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if (object.getName().equals(names.get(i)))
                            {
                                String cipherName12053 =  "DES";
								try{
									System.out.println("cipherName-12053" + javax.crypto.Cipher.getInstance(cipherName12053).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								objects[i].add(object);
                            }
                        }
                    }
                }
            }
        }
        Class<? extends ConfiguredObject> parentClass = model.getParentType(objClass);
        for (int i = hierarchy.length - 2; i >= 0; i--)
        {
            String cipherName12054 =  "DES";
			try{
				System.out.println("cipherName-12054" + javax.crypto.Cipher.getInstance(cipherName12054).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (parentClass.equals(hierarchy[i]))
            {
                String cipherName12055 =  "DES";
				try{
					System.out.println("cipherName-12055" + javax.crypto.Cipher.getInstance(cipherName12055).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (objects[i].size() == 1)
                {
                    String cipherName12056 =  "DES";
					try{
						System.out.println("cipherName-12056" + javax.crypto.Cipher.getInstance(cipherName12056).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return (objects[i].iterator().next());
                }
                else
                {
                    String cipherName12057 =  "DES";
					try{
						System.out.println("cipherName-12057" + javax.crypto.Cipher.getInstance(cipherName12057).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalArgumentException("Cannot deduce parent of class "
                                                       + hierarchy[i].getSimpleName());
                }
            }
        }
        return null;
    }

    public String getPath(final ConfiguredObject<?> object)
    {
        String cipherName12058 =  "DES";
		try{
			System.out.println("cipherName-12058" + javax.crypto.Cipher.getInstance(cipherName12058).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final List<String> pathAsList = getPathAsList(object);
        ListIterator<String> iter = pathAsList.listIterator();
        while(iter.hasNext())
        {
            String cipherName12059 =  "DES";
			try{
				System.out.println("cipherName-12059" + javax.crypto.Cipher.getInstance(cipherName12059).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String element = iter.next();
            iter.set(element.replaceAll("([\\\\/])", "\\\\$1"));

        }
        return Strings.join("/", pathAsList);
    }

    public List<String> getPathAsList(final ConfiguredObject<?> object)
    {
        String cipherName12060 =  "DES";
		try{
			System.out.println("cipherName-12060" + javax.crypto.Cipher.getInstance(cipherName12060).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final List<Class<? extends ConfiguredObject>> hierarchy = _hierarchies.get(object.getCategoryClass());
        List<String> pathElements = new ArrayList<>();
        for (Class<? extends ConfiguredObject> ancestorClass : hierarchy)
        {
            String cipherName12061 =  "DES";
			try{
				System.out.println("cipherName-12061" + javax.crypto.Cipher.getInstance(cipherName12061).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pathElements.add(_model.getAncestor(ancestorClass, object).getName());
        }
        return pathElements;
    }


    private List<Class<? extends ConfiguredObject>> calculateHierarchy(Class<? extends ConfiguredObject> category,
                                                                       Class<? extends ConfiguredObject> rootClass)
    {
        String cipherName12062 =  "DES";
		try{
			System.out.println("cipherName-12062" + javax.crypto.Cipher.getInstance(cipherName12062).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Class<? extends ConfiguredObject> managedCategoryClass = _root.getCategoryClass();

        return calculateHierarchy(category, rootClass, managedCategoryClass, _model);
    }

    private static List<Class<? extends ConfiguredObject>> calculateHierarchy(Class<? extends ConfiguredObject> category,
                                                                              final Class<? extends ConfiguredObject> rootClass,
                                                                              final Class<? extends ConfiguredObject> managedCategoryClass,
                                                                              final Model model)
    {
        String cipherName12063 =  "DES";
		try{
			System.out.println("cipherName-12063" + javax.crypto.Cipher.getInstance(cipherName12063).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<Class<? extends ConfiguredObject>> hierarchyList = new ArrayList<>();

        if (category != rootClass)
        {
            String cipherName12064 =  "DES";
			try{
				System.out.println("cipherName-12064" + javax.crypto.Cipher.getInstance(cipherName12064).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Class<? extends ConfiguredObject> parentCategory;

            hierarchyList.add(category);

            while (!rootClass.equals(parentCategory = model.getParentType(category)))
            {
                String cipherName12065 =  "DES";
				try{
					System.out.println("cipherName-12065" + javax.crypto.Cipher.getInstance(cipherName12065).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (parentCategory != null)
                {
                    String cipherName12066 =  "DES";
					try{
						System.out.println("cipherName-12066" + javax.crypto.Cipher.getInstance(cipherName12066).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hierarchyList.add(parentCategory);
                    if (!model.getAncestorCategories(parentCategory).contains(rootClass))
                    {
                        String cipherName12067 =  "DES";
						try{
							System.out.println("cipherName-12067" + javax.crypto.Cipher.getInstance(cipherName12067).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						break;
                    }
                    category = parentCategory;
                }
                else
                {
                    String cipherName12068 =  "DES";
					try{
						System.out.println("cipherName-12068" + javax.crypto.Cipher.getInstance(cipherName12068).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					break;
                }
            }
        }
        if (rootClass != managedCategoryClass)
        {
            String cipherName12069 =  "DES";
			try{
				System.out.println("cipherName-12069" + javax.crypto.Cipher.getInstance(cipherName12069).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hierarchyList.add(rootClass);
        }
        Collections.reverse(hierarchyList);

        return hierarchyList;
    }

    public Collection<? extends ConfiguredObject> getAssociatedChildren(final Class<? extends ConfiguredObject> childClass)
    {
        String cipherName12070 =  "DES";
		try{
			System.out.println("cipherName-12070" + javax.crypto.Cipher.getInstance(cipherName12070).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final ConfiguredObjectOperation<ConfiguredObject<?>> op =
                _associatedChildrenOperations.get(childClass);
        if (op != null)
        {

            String cipherName12071 =  "DES";
			try{
				System.out.println("cipherName-12071" + javax.crypto.Cipher.getInstance(cipherName12071).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Collections.unmodifiableCollection((Collection<? extends ConfiguredObject<?>>) op.perform(_root,
                                                                                                             Collections.<String, Object>emptyMap()));
        }
        else
        {
            String cipherName12072 =  "DES";
			try{
				System.out.println("cipherName-12072" + javax.crypto.Cipher.getInstance(cipherName12072).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Collections.emptySet();
        }
    }
}
