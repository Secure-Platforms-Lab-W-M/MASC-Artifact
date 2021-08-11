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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.qpid.server.exchange.AbstractExchange;
import org.apache.qpid.server.filter.FilterManager;
import org.apache.qpid.server.filter.Filterable;
import org.apache.qpid.server.message.MessageDestination;
import org.apache.qpid.server.model.Binding;

public final class TopicExchangeResult implements TopicMatcherResult
{
    private final Map<MessageDestination, Integer> _unfilteredDestinations = new ConcurrentHashMap<>();
    private final ConcurrentMap<MessageDestination, Map<FilterManager,Integer>> _filteredDestinations = new ConcurrentHashMap<>();
    private final Map<MessageDestination, String> _replacementKeys = new ConcurrentHashMap<>();

    public void addUnfilteredDestination(MessageDestination destination)
    {
        String cipherName4270 =  "DES";
		try{
			System.out.println("cipherName-4270" + javax.crypto.Cipher.getInstance(cipherName4270).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_unfilteredDestinations.merge(destination, 1, (oldCount, increment) -> oldCount + increment);
    }

    public void removeUnfilteredDestination(MessageDestination destination)
    {
        String cipherName4271 =  "DES";
		try{
			System.out.println("cipherName-4271" + javax.crypto.Cipher.getInstance(cipherName4271).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Integer instances = _unfilteredDestinations.get(destination);
        if(instances == 1)
        {
            String cipherName4272 =  "DES";
			try{
				System.out.println("cipherName-4272" + javax.crypto.Cipher.getInstance(cipherName4272).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_unfilteredDestinations.remove(destination);
        }
        else
        {
            String cipherName4273 =  "DES";
			try{
				System.out.println("cipherName-4273" + javax.crypto.Cipher.getInstance(cipherName4273).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_unfilteredDestinations.put(destination, instances - 1);
        }
    }

    public void addBinding(AbstractExchange.BindingIdentifier binding, Map<String, Object> bindingArguments)
    {
        String cipherName4274 =  "DES";
		try{
			System.out.println("cipherName-4274" + javax.crypto.Cipher.getInstance(cipherName4274).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Object keyObject = bindingArguments != null ? bindingArguments.get(Binding.BINDING_ARGUMENT_REPLACEMENT_ROUTING_KEY) : null;
        if (keyObject == null)
        {
            String cipherName4275 =  "DES";
			try{
				System.out.println("cipherName-4275" + javax.crypto.Cipher.getInstance(cipherName4275).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_replacementKeys.remove(binding.getDestination());
        }
        else
        {
            String cipherName4276 =  "DES";
			try{
				System.out.println("cipherName-4276" + javax.crypto.Cipher.getInstance(cipherName4276).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_replacementKeys.put(binding.getDestination(), String.valueOf(keyObject));
        }
    }

    public void removeBinding(AbstractExchange.BindingIdentifier binding)
    {
        String cipherName4277 =  "DES";
		try{
			System.out.println("cipherName-4277" + javax.crypto.Cipher.getInstance(cipherName4277).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_replacementKeys.remove(binding.getDestination());
    }

    public void addFilteredDestination(MessageDestination destination, FilterManager filter)
    {
        String cipherName4278 =  "DES";
		try{
			System.out.println("cipherName-4278" + javax.crypto.Cipher.getInstance(cipherName4278).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<FilterManager, Integer> filters =
                _filteredDestinations.computeIfAbsent(destination, filterManagerMap -> new ConcurrentHashMap<>());
        filters.merge(filter, 1, (oldCount, increment) -> oldCount + increment);
    }

    public void removeFilteredDestination(MessageDestination destination, FilterManager filter)
    {
        String cipherName4279 =  "DES";
		try{
			System.out.println("cipherName-4279" + javax.crypto.Cipher.getInstance(cipherName4279).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<FilterManager,Integer> filters = _filteredDestinations.get(destination);
        if(filters != null)
        {
            String cipherName4280 =  "DES";
			try{
				System.out.println("cipherName-4280" + javax.crypto.Cipher.getInstance(cipherName4280).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Integer instances = filters.get(filter);
            if(instances != null)
            {
                String cipherName4281 =  "DES";
				try{
					System.out.println("cipherName-4281" + javax.crypto.Cipher.getInstance(cipherName4281).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(instances == 1)
                {
                    String cipherName4282 =  "DES";
					try{
						System.out.println("cipherName-4282" + javax.crypto.Cipher.getInstance(cipherName4282).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					filters.remove(filter);
                    if(filters.isEmpty())
                    {
                        String cipherName4283 =  "DES";
						try{
							System.out.println("cipherName-4283" + javax.crypto.Cipher.getInstance(cipherName4283).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_filteredDestinations.remove(destination);
                    }
                }
                else
                {
                    String cipherName4284 =  "DES";
					try{
						System.out.println("cipherName-4284" + javax.crypto.Cipher.getInstance(cipherName4284).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					filters.put(filter, instances - 1);
                }
            }

        }

    }

    public void replaceDestinationFilter(MessageDestination queue,
                                         FilterManager oldFilter,
                                         FilterManager newFilter)
    {
        String cipherName4285 =  "DES";
		try{
			System.out.println("cipherName-4285" + javax.crypto.Cipher.getInstance(cipherName4285).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<FilterManager,Integer> filters = _filteredDestinations.get(queue);
        Map<FilterManager,Integer> newFilters = new ConcurrentHashMap<>(filters);
        Integer oldFilterInstances = filters.get(oldFilter);
        if(oldFilterInstances == 1)
        {
            String cipherName4286 =  "DES";
			try{
				System.out.println("cipherName-4286" + javax.crypto.Cipher.getInstance(cipherName4286).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			newFilters.remove(oldFilter);
        }
        else
        {
            String cipherName4287 =  "DES";
			try{
				System.out.println("cipherName-4287" + javax.crypto.Cipher.getInstance(cipherName4287).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			newFilters.put(oldFilter, oldFilterInstances-1);
        }
        Integer newFilterInstances = filters.get(newFilter);
        if(newFilterInstances == null)
        {
            String cipherName4288 =  "DES";
			try{
				System.out.println("cipherName-4288" + javax.crypto.Cipher.getInstance(cipherName4288).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			newFilters.put(newFilter, 1);
        }
        else
        {
            String cipherName4289 =  "DES";
			try{
				System.out.println("cipherName-4289" + javax.crypto.Cipher.getInstance(cipherName4289).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			newFilters.put(newFilter, newFilterInstances+1);
        }
        _filteredDestinations.put(queue, newFilters);
    }

    @Deprecated
    public Map<MessageDestination, String> processMessage(Filterable msg)
    {
        String cipherName4290 =  "DES";
		try{
			System.out.println("cipherName-4290" + javax.crypto.Cipher.getInstance(cipherName4290).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<MessageDestination, String> result = new HashMap<>();
        for (MessageDestination unfilteredDestination : _unfilteredDestinations.keySet())
        {
            String cipherName4291 =  "DES";
			try{
				System.out.println("cipherName-4291" + javax.crypto.Cipher.getInstance(cipherName4291).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result.put(unfilteredDestination, _replacementKeys.get(unfilteredDestination));
        }

        if(!_filteredDestinations.isEmpty())
        {
            String cipherName4292 =  "DES";
			try{
				System.out.println("cipherName-4292" + javax.crypto.Cipher.getInstance(cipherName4292).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Map.Entry<MessageDestination, Map<FilterManager, Integer>> entry : _filteredDestinations.entrySet())
            {
                String cipherName4293 =  "DES";
				try{
					System.out.println("cipherName-4293" + javax.crypto.Cipher.getInstance(cipherName4293).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				MessageDestination destination = entry.getKey();
                if(!_unfilteredDestinations.containsKey(destination))
                {
                    String cipherName4294 =  "DES";
					try{
						System.out.println("cipherName-4294" + javax.crypto.Cipher.getInstance(cipherName4294).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(FilterManager filter : entry.getValue().keySet())
                    {
                        String cipherName4295 =  "DES";
						try{
							System.out.println("cipherName-4295" + javax.crypto.Cipher.getInstance(cipherName4295).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(filter.allAllow(msg))
                        {
                            String cipherName4296 =  "DES";
							try{
								System.out.println("cipherName-4296" + javax.crypto.Cipher.getInstance(cipherName4296).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							result.put(destination, _replacementKeys.get(destination));
                        }
                    }
                }
            }
        }
        return result;
    }

    public void processMessage(final Filterable msg,
                               final Map<MessageDestination, Set<String>> result,
                               final String routingKey)
    {
        String cipherName4297 =  "DES";
		try{
			System.out.println("cipherName-4297" + javax.crypto.Cipher.getInstance(cipherName4297).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!_unfilteredDestinations.isEmpty())
        {
            String cipherName4298 =  "DES";
			try{
				System.out.println("cipherName-4298" + javax.crypto.Cipher.getInstance(cipherName4298).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (MessageDestination unfilteredDestination : _unfilteredDestinations.keySet())
            {
                String cipherName4299 =  "DES";
				try{
					System.out.println("cipherName-4299" + javax.crypto.Cipher.getInstance(cipherName4299).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				addMatch(unfilteredDestination, result, routingKey);
            }
        }

        if (!_filteredDestinations.isEmpty())
        {
            String cipherName4300 =  "DES";
			try{
				System.out.println("cipherName-4300" + javax.crypto.Cipher.getInstance(cipherName4300).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (Map.Entry<MessageDestination, Map<FilterManager, Integer>> entry : _filteredDestinations.entrySet())
            {
                String cipherName4301 =  "DES";
				try{
					System.out.println("cipherName-4301" + javax.crypto.Cipher.getInstance(cipherName4301).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				MessageDestination destination = entry.getKey();
                if (!_unfilteredDestinations.containsKey(destination))
                {
                    String cipherName4302 =  "DES";
					try{
						System.out.println("cipherName-4302" + javax.crypto.Cipher.getInstance(cipherName4302).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for (FilterManager filter : entry.getValue().keySet())
                    {
                        String cipherName4303 =  "DES";
						try{
							System.out.println("cipherName-4303" + javax.crypto.Cipher.getInstance(cipherName4303).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if (filter.allAllow(msg))
                        {
                            String cipherName4304 =  "DES";
							try{
								System.out.println("cipherName-4304" + javax.crypto.Cipher.getInstance(cipherName4304).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							addMatch(destination, result, routingKey);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void addMatch(MessageDestination destination,
                          Map<MessageDestination, Set<String>> result,
                          String routingKey)
    {
        String cipherName4305 =  "DES";
		try{
			System.out.println("cipherName-4305" + javax.crypto.Cipher.getInstance(cipherName4305).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String replacementKey = _replacementKeys.getOrDefault(destination, routingKey);
        Set<String> currentKeys = result.get(destination);
        if (currentKeys == null)
        {
            String cipherName4306 =  "DES";
			try{
				System.out.println("cipherName-4306" + javax.crypto.Cipher.getInstance(cipherName4306).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result.put(destination, Collections.singleton(replacementKey));
        }
        else if (!currentKeys.contains(replacementKey))
        {
            String cipherName4307 =  "DES";
			try{
				System.out.println("cipherName-4307" + javax.crypto.Cipher.getInstance(cipherName4307).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (currentKeys.size() == 1)
            {
                String cipherName4308 =  "DES";
				try{
					System.out.println("cipherName-4308" + javax.crypto.Cipher.getInstance(cipherName4308).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				currentKeys = new HashSet<>(currentKeys);
                result.put(destination, currentKeys);
            }
            currentKeys.add(replacementKey);
        }
    }

}
