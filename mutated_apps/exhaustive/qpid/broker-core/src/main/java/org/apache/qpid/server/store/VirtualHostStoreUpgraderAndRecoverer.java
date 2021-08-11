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
package org.apache.qpid.server.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.configuration.store.StoreConfigurationChangeListener;
import org.apache.qpid.server.filter.FilterSupport;
import org.apache.qpid.server.model.AbstractConfigurationChangeListener;
import org.apache.qpid.server.model.AbstractConfiguredObject;
import org.apache.qpid.server.model.Binding;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.Exchange;
import org.apache.qpid.server.model.Queue;
import org.apache.qpid.server.model.UUIDGenerator;
import org.apache.qpid.server.model.VirtualHost;
import org.apache.qpid.server.model.VirtualHostNode;
import org.apache.qpid.server.store.handler.ConfiguredObjectRecordHandler;
import org.apache.qpid.server.util.FixedKeyMapCreator;

public class VirtualHostStoreUpgraderAndRecoverer extends AbstractConfigurationStoreUpgraderAndRecoverer
{
    private final VirtualHostNode<?> _virtualHostNode;

    @SuppressWarnings("serial")
    private static final Map<String, String> DEFAULT_EXCHANGES = Collections.unmodifiableMap(new HashMap<String, String>()
    {{
        String cipherName16982 =  "DES";
		try{
			System.out.println("cipherName-16982" + javax.crypto.Cipher.getInstance(cipherName16982).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		put("amq.direct", "direct");
        put("amq.topic", "topic");
        put("amq.fanout", "fanout");
        put("amq.match", "headers");
    }});

    private final Map<String, UUID> _defaultExchangeIds;

    public VirtualHostStoreUpgraderAndRecoverer(VirtualHostNode<?> virtualHostNode)
    {
        super("0.0");
		String cipherName16983 =  "DES";
		try{
			System.out.println("cipherName-16983" + javax.crypto.Cipher.getInstance(cipherName16983).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _virtualHostNode = virtualHostNode;
        register(new Upgrader_0_0_to_0_1());
        register(new Upgrader_0_1_to_0_2());
        register(new Upgrader_0_2_to_0_3());
        register(new Upgrader_0_3_to_0_4());
        register(new Upgrader_0_4_to_2_0());
        register(new Upgrader_2_0_to_3_0());
        register(new Upgrader_3_0_to_6_0());
        register(new Upgrader_6_0_to_6_1());
        register(new Upgrader_6_1_to_7_0());
        register(new Upgrader_7_0_to_7_1());
        register(new Upgrader_7_1_to_8_0());

        Map<String, UUID> defaultExchangeIds = new HashMap<String, UUID>();
        for (String exchangeName : DEFAULT_EXCHANGES.keySet())
        {
            String cipherName16984 =  "DES";
			try{
				System.out.println("cipherName-16984" + javax.crypto.Cipher.getInstance(cipherName16984).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			UUID id = UUIDGenerator.generateExchangeUUID(exchangeName, virtualHostNode.getName());
            defaultExchangeIds.put(exchangeName, id);
        }
        _defaultExchangeIds = Collections.unmodifiableMap(defaultExchangeIds);
    }

    /*
     * Removes filters from queue bindings to exchanges other than topic exchanges.  In older versions of the broker
     * such bindings would have been ignored, starting from the point at which the config version changed, these
     * arguments would actually cause selectors to be enforced, thus changing which messages would reach a queue.
     */
    private class Upgrader_0_0_to_0_1  extends StoreUpgraderPhase
    {
        private final Map<UUID, ConfiguredObjectRecord> _records = new HashMap<UUID, ConfiguredObjectRecord>();

        public Upgrader_0_0_to_0_1()
        {
            super("modelVersion", "0.0", "0.1");
			String cipherName16985 =  "DES";
			try{
				System.out.println("cipherName-16985" + javax.crypto.Cipher.getInstance(cipherName16985).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void configuredObject(final ConfiguredObjectRecord record)
        {
            String cipherName16986 =  "DES";
			try{
				System.out.println("cipherName-16986" + javax.crypto.Cipher.getInstance(cipherName16986).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_records.put(record.getId(), record);
        }

        private void removeSelectorArguments(Map<String, Object> binding)
        {
            String cipherName16987 =  "DES";
			try{
				System.out.println("cipherName-16987" + javax.crypto.Cipher.getInstance(cipherName16987).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			@SuppressWarnings("unchecked")
            Map<String, Object> arguments = new LinkedHashMap<String, Object>((Map<String,Object>)binding.get("arguments"));

            FilterSupport.removeFilters(arguments);
            binding.put("arguments", arguments);
        }

        private boolean isTopicExchange(ConfiguredObjectRecord entry)
        {
            String cipherName16988 =  "DES";
			try{
				System.out.println("cipherName-16988" + javax.crypto.Cipher.getInstance(cipherName16988).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			UUID exchangeId = entry.getParents().get("Exchange");
            if (exchangeId == null)
            {
                String cipherName16989 =  "DES";
				try{
					System.out.println("cipherName-16989" + javax.crypto.Cipher.getInstance(cipherName16989).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }

            if(_records.containsKey(exchangeId))
            {
                String cipherName16990 =  "DES";
				try{
					System.out.println("cipherName-16990" + javax.crypto.Cipher.getInstance(cipherName16990).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return "topic".equals(_records.get(exchangeId)
                        .getAttributes()
                        .get(org.apache.qpid.server.model.Exchange.TYPE));
            }
            else
            {
                String cipherName16991 =  "DES";
				try{
					System.out.println("cipherName-16991" + javax.crypto.Cipher.getInstance(cipherName16991).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (_defaultExchangeIds.get("amq.topic").equals(exchangeId))
                {
                    String cipherName16992 =  "DES";
					try{
						System.out.println("cipherName-16992" + javax.crypto.Cipher.getInstance(cipherName16992).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }

                return false;
            }

        }

        private boolean hasSelectorArguments(Map<String, Object> binding)
        {
            String cipherName16993 =  "DES";
			try{
				System.out.println("cipherName-16993" + javax.crypto.Cipher.getInstance(cipherName16993).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			@SuppressWarnings("unchecked")
            Map<String, Object> arguments = (Map<String, Object>) binding.get("arguments");
            return (arguments != null) && FilterSupport.argumentsContainFilter(arguments);
        }

        @Override
        public void complete()
        {
            String cipherName16994 =  "DES";
			try{
				System.out.println("cipherName-16994" + javax.crypto.Cipher.getInstance(cipherName16994).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Map.Entry<UUID,ConfiguredObjectRecord> entry : _records.entrySet())
            {
                String cipherName16995 =  "DES";
				try{
					System.out.println("cipherName-16995" + javax.crypto.Cipher.getInstance(cipherName16995).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ConfiguredObjectRecord record = entry.getValue();
                String type = record.getType();
                Map<String, Object> attributes = record.getAttributes();
                UUID id = record.getId();
                if ("org.apache.qpid.server.model.VirtualHost".equals(type))
                {
                    String cipherName16996 =  "DES";
					try{
						System.out.println("cipherName-16996" + javax.crypto.Cipher.getInstance(cipherName16996).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					upgradeRootRecord(record);
                }
                else if(type.equals(Binding.class.getName()) && hasSelectorArguments(attributes) && !isTopicExchange(record))
                {
                    String cipherName16997 =  "DES";
					try{
						System.out.println("cipherName-16997" + javax.crypto.Cipher.getInstance(cipherName16997).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					attributes = new LinkedHashMap<String, Object>(attributes);
                    removeSelectorArguments(attributes);

                    record = new ConfiguredObjectRecordImpl(id, type, attributes, record.getParents());
                    getUpdateMap().put(id, record);
                    entry.setValue(record);

                }
            }
        }

    }

    /*
     * Change the type string from org.apache.qpid.server.model.Foo to Foo (in line with the practice in the broker
     * configuration store).  Also remove bindings which reference nonexistent queues or exchanges.
     */
    private class Upgrader_0_1_to_0_2 extends StoreUpgraderPhase
    {
        public Upgrader_0_1_to_0_2()
        {
            super("modelVersion", "0.1", "0.2");
			String cipherName16998 =  "DES";
			try{
				System.out.println("cipherName-16998" + javax.crypto.Cipher.getInstance(cipherName16998).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void configuredObject(final ConfiguredObjectRecord record)
        {
            String cipherName16999 =  "DES";
			try{
				System.out.println("cipherName-16999" + javax.crypto.Cipher.getInstance(cipherName16999).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String type = record.getType().substring(1 + record.getType().lastIndexOf('.'));
            ConfiguredObjectRecord newRecord = new ConfiguredObjectRecordImpl(record.getId(), type, record.getAttributes(), record.getParents());
            getUpdateMap().put(record.getId(), newRecord);

            if ("VirtualHost".equals(type))
            {
                String cipherName17000 =  "DES";
				try{
					System.out.println("cipherName-17000" + javax.crypto.Cipher.getInstance(cipherName17000).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				upgradeRootRecord(newRecord);
            }
        }

        @Override
        public void complete()
        {
            String cipherName17001 =  "DES";
			try{
				System.out.println("cipherName-17001" + javax.crypto.Cipher.getInstance(cipherName17001).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (Iterator<Map.Entry<UUID, ConfiguredObjectRecord>> iterator = getUpdateMap().entrySet().iterator(); iterator.hasNext();)
            {
                String cipherName17002 =  "DES";
				try{
					System.out.println("cipherName-17002" + javax.crypto.Cipher.getInstance(cipherName17002).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map.Entry<UUID, ConfiguredObjectRecord> entry = iterator.next();
                final ConfiguredObjectRecord record = entry.getValue();
                final UUID exchangeParent = record.getParents().get(Exchange.class.getSimpleName());
                final UUID queueParent = record.getParents().get(Queue.class.getSimpleName());
                if(isBinding(record.getType()) && (exchangeParent == null || unknownExchange(exchangeParent)
                                                   || queueParent == null || unknownQueue(queueParent)))
                {
                    String cipherName17003 =  "DES";
					try{
						System.out.println("cipherName-17003" + javax.crypto.Cipher.getInstance(cipherName17003).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					getDeleteMap().put(entry.getKey(), entry.getValue());
                    iterator.remove();
                }
            }
        }

        private boolean unknownExchange(final UUID exchangeId)
        {
            String cipherName17004 =  "DES";
			try{
				System.out.println("cipherName-17004" + javax.crypto.Cipher.getInstance(cipherName17004).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (_defaultExchangeIds.containsValue(exchangeId))
            {
                String cipherName17005 =  "DES";
				try{
					System.out.println("cipherName-17005" + javax.crypto.Cipher.getInstance(cipherName17005).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
            ConfiguredObjectRecord localRecord = getUpdateMap().get(exchangeId);
            return !(localRecord != null && localRecord.getType().equals(Exchange.class.getSimpleName()));
        }

        private boolean unknownQueue(final UUID queueId)
        {
            String cipherName17006 =  "DES";
			try{
				System.out.println("cipherName-17006" + javax.crypto.Cipher.getInstance(cipherName17006).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ConfiguredObjectRecord localRecord = getUpdateMap().get(queueId);
            return !(localRecord != null  && localRecord.getType().equals(Queue.class.getSimpleName()));
        }

        private boolean isBinding(final String type)
        {
            String cipherName17007 =  "DES";
			try{
				System.out.println("cipherName-17007" + javax.crypto.Cipher.getInstance(cipherName17007).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Binding.class.getSimpleName().equals(type);
        }
    }


    /*
     * Convert the storage of queue attributes to remove the separate "ARGUMENT" attribute, and flatten the
     * attributes into the map using the model attribute names rather than the wire attribute names
     */
    private class Upgrader_0_2_to_0_3 extends StoreUpgraderPhase
    {
        private static final String ARGUMENTS = "arguments";

        public Upgrader_0_2_to_0_3()
        {
            super("modelVersion", "0.2", "0.3");
			String cipherName17008 =  "DES";
			try{
				System.out.println("cipherName-17008" + javax.crypto.Cipher.getInstance(cipherName17008).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @SuppressWarnings("unchecked")
        @Override
        public void configuredObject(ConfiguredObjectRecord record)
        {
            String cipherName17009 =  "DES";
			try{
				System.out.println("cipherName-17009" + javax.crypto.Cipher.getInstance(cipherName17009).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if ("VirtualHost".equals(record.getType()))
            {
                String cipherName17010 =  "DES";
				try{
					System.out.println("cipherName-17010" + javax.crypto.Cipher.getInstance(cipherName17010).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				upgradeRootRecord(record);
            }
            else if ("Queue".equals(record.getType()))
            {
                String cipherName17011 =  "DES";
				try{
					System.out.println("cipherName-17011" + javax.crypto.Cipher.getInstance(cipherName17011).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map<String, Object> newAttributes = new LinkedHashMap<String, Object>();
                if (record.getAttributes().get(ARGUMENTS) instanceof Map)
                {
                    String cipherName17012 =  "DES";
					try{
						System.out.println("cipherName-17012" + javax.crypto.Cipher.getInstance(cipherName17012).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					newAttributes.putAll(convertWireArgsToModel((Map<String, Object>) record.getAttributes()
                                                                                            .get(ARGUMENTS)));
                }
                newAttributes.putAll(record.getAttributes());

                record = new ConfiguredObjectRecordImpl(record.getId(),
                                                        record.getType(),
                                                        newAttributes,
                                                        record.getParents());
                getUpdateMap().put(record.getId(), record);
            }
        }

        @Override
        public void complete()
        {
			String cipherName17013 =  "DES";
			try{
				System.out.println("cipherName-17013" + javax.crypto.Cipher.getInstance(cipherName17013).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        private final Map<String, String> ATTRIBUTE_MAPPINGS = new LinkedHashMap<>();

        {
            String cipherName17014 =  "DES";
			try{
				System.out.println("cipherName-17014" + javax.crypto.Cipher.getInstance(cipherName17014).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ATTRIBUTE_MAPPINGS.put("x-qpid-minimum-alert-repeat-gap", "alertRepeatGap");
            ATTRIBUTE_MAPPINGS.put("x-qpid-maximum-message-age", "alertThresholdMessageAge");
            ATTRIBUTE_MAPPINGS.put("x-qpid-maximum-message-size", "alertThresholdMessageSize");
            ATTRIBUTE_MAPPINGS.put("x-qpid-maximum-message-count", "alertThresholdQueueDepthMessages");
            ATTRIBUTE_MAPPINGS.put("x-qpid-maximum-queue-depth", "alertThresholdQueueDepthBytes");
            ATTRIBUTE_MAPPINGS.put("qpid.alert_count", "alertThresholdQueueDepthMessages");
            ATTRIBUTE_MAPPINGS.put("qpid.alert_size", "alertThresholdQueueDepthBytes");
            ATTRIBUTE_MAPPINGS.put("qpid.alert_repeat_gap", "alertRepeatGap");
            ATTRIBUTE_MAPPINGS.put("x-qpid-maximum-delivery-count", "maximumDeliveryAttempts");
            ATTRIBUTE_MAPPINGS.put("x-qpid-capacity", "queueFlowControlSizeBytes");
            ATTRIBUTE_MAPPINGS.put("x-qpid-flow-resume-capacity", "queueFlowResumeSizeBytes");
            ATTRIBUTE_MAPPINGS.put("qpid.queue_sort_key", "sortKey");
            ATTRIBUTE_MAPPINGS.put("qpid.last_value_queue_key", "lvqKey");
            ATTRIBUTE_MAPPINGS.put("x-qpid-priorities", "priorities");
            ATTRIBUTE_MAPPINGS.put("x-qpid-description", "description");
            ATTRIBUTE_MAPPINGS.put("x-qpid-dlq-enabled", "x-qpid-dlq-enabled");
            ATTRIBUTE_MAPPINGS.put("qpid.group_header_key", "messageGroupKey");
            ATTRIBUTE_MAPPINGS.put("qpid.default-message-group", "messageGroupDefaultGroup");
            ATTRIBUTE_MAPPINGS.put("no-local", "noLocal");
            ATTRIBUTE_MAPPINGS.put("qpid.message_durability", "messageDurability");
        }

        private Map<String, Object> convertWireArgsToModel(Map<String, Object> wireArguments)
        {
            String cipherName17015 =  "DES";
			try{
				System.out.println("cipherName-17015" + javax.crypto.Cipher.getInstance(cipherName17015).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Map<String, Object> modelArguments = new HashMap<>();
            if (wireArguments != null)
            {
                String cipherName17016 =  "DES";
				try{
					System.out.println("cipherName-17016" + javax.crypto.Cipher.getInstance(cipherName17016).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (Map.Entry<String, String> entry : ATTRIBUTE_MAPPINGS.entrySet())
                {
                    String cipherName17017 =  "DES";
					try{
						System.out.println("cipherName-17017" + javax.crypto.Cipher.getInstance(cipherName17017).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (wireArguments.containsKey(entry.getKey()))
                    {
                        String cipherName17018 =  "DES";
						try{
							System.out.println("cipherName-17018" + javax.crypto.Cipher.getInstance(cipherName17018).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						modelArguments.put(entry.getValue(), wireArguments.get(entry.getKey()));
                    }
                }
                if (wireArguments.containsKey("qpid.last_value_queue")
                    && !wireArguments.containsKey("qpid.last_value_queue_key"))
                {
                    String cipherName17019 =  "DES";
					try{
						System.out.println("cipherName-17019" + javax.crypto.Cipher.getInstance(cipherName17019).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.put("lvqKey", "qpid.LVQ_key");
                }
                if (wireArguments.containsKey("qpid.shared_msg_group"))
                {
                    String cipherName17020 =  "DES";
					try{
						System.out.println("cipherName-17020" + javax.crypto.Cipher.getInstance(cipherName17020).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.put("messageGroupSharedGroups",
                                       "1".equals(String.valueOf(wireArguments.get("qpid.shared_msg_group"))));
                }
                if (wireArguments.get("x-qpid-dlq-enabled") != null)
                {
                    String cipherName17021 =  "DES";
					try{
						System.out.println("cipherName-17021" + javax.crypto.Cipher.getInstance(cipherName17021).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.put("x-qpid-dlq-enabled",
                                       Boolean.parseBoolean(wireArguments.get("x-qpid-dlq-enabled").toString()));
                }
                if (wireArguments.get("no-local") != null)
                {
                    String cipherName17022 =  "DES";
					try{
						System.out.println("cipherName-17022" + javax.crypto.Cipher.getInstance(cipherName17022).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.put("noLocal", Boolean.parseBoolean(wireArguments.get("no-local").toString()));
                }
            }
            return modelArguments;
        }
    }

    /*
     * Convert the storage of queue attribute exclusive to change exclusive from a boolean to an enum
     * where exclusive was false it will now be "NONE", and where true it will now be "CONTAINER"
     * ensure OWNER is null unless the exclusivity policy is CONTAINER
     */
    private class Upgrader_0_3_to_0_4 extends StoreUpgraderPhase
    {
        private static final String EXCLUSIVE = "exclusive";

        public Upgrader_0_3_to_0_4()
        {
            super("modelVersion", "0.3", "0.4");
			String cipherName17023 =  "DES";
			try{
				System.out.println("cipherName-17023" + javax.crypto.Cipher.getInstance(cipherName17023).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }


        @Override
        public void configuredObject(ConfiguredObjectRecord record)
        {
            String cipherName17024 =  "DES";
			try{
				System.out.println("cipherName-17024" + javax.crypto.Cipher.getInstance(cipherName17024).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if("VirtualHost".equals(record.getType()))
            {
                String cipherName17025 =  "DES";
				try{
					System.out.println("cipherName-17025" + javax.crypto.Cipher.getInstance(cipherName17025).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				upgradeRootRecord(record);
            }
            else if(Queue.class.getSimpleName().equals(record.getType()))
            {
                String cipherName17026 =  "DES";
				try{
					System.out.println("cipherName-17026" + javax.crypto.Cipher.getInstance(cipherName17026).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map<String, Object> newAttributes = new LinkedHashMap<String, Object>(record.getAttributes());
                if(record.getAttributes().get(EXCLUSIVE) instanceof Boolean)
                {
                    String cipherName17027 =  "DES";
					try{
						System.out.println("cipherName-17027" + javax.crypto.Cipher.getInstance(cipherName17027).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					boolean isExclusive = (Boolean) record.getAttributes().get(EXCLUSIVE);
                    newAttributes.put(EXCLUSIVE, isExclusive ? "CONTAINER" : "NONE");
                    if(!isExclusive && record.getAttributes().containsKey("owner"))
                    {
                        String cipherName17028 =  "DES";
						try{
							System.out.println("cipherName-17028" + javax.crypto.Cipher.getInstance(cipherName17028).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						newAttributes.remove("owner");
                    }
                }
                else
                {
                    String cipherName17029 =  "DES";
					try{
						System.out.println("cipherName-17029" + javax.crypto.Cipher.getInstance(cipherName17029).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					newAttributes.remove("owner");
                }
                if(!record.getAttributes().containsKey("durable"))
                {
                    String cipherName17030 =  "DES";
					try{
						System.out.println("cipherName-17030" + javax.crypto.Cipher.getInstance(cipherName17030).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					newAttributes.put("durable", "true");
                }

                record = new ConfiguredObjectRecordImpl(record.getId(),record.getType(),newAttributes, record.getParents());
                getUpdateMap().put(record.getId(), record);
            }
        }

        @Override
        public void complete()
        {
			String cipherName17031 =  "DES";
			try{
				System.out.println("cipherName-17031" + javax.crypto.Cipher.getInstance(cipherName17031).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

    }

    private class Upgrader_0_4_to_2_0 extends StoreUpgraderPhase
    {
        private static final String ALTERNATE_EXCHANGE = "alternateExchange";
        private static final String DLQ_ENABLED_ARGUMENT = "x-qpid-dlq-enabled";
        private static final String  DEFAULT_DLE_NAME_SUFFIX = "_DLE";

        private Map<String, String> _missingAmqpExchanges = new HashMap<String, String>(DEFAULT_EXCHANGES);
        private ConfiguredObjectRecord _virtualHostRecord;

        private Map<UUID, String> _queuesMissingAlternateExchange = new HashMap<>();
        private Map<String, ConfiguredObjectRecord> _exchanges = new HashMap<>();

        public Upgrader_0_4_to_2_0()
        {
            super("modelVersion", "0.4", "2.0");
			String cipherName17032 =  "DES";
			try{
				System.out.println("cipherName-17032" + javax.crypto.Cipher.getInstance(cipherName17032).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void configuredObject(ConfiguredObjectRecord record)
        {
            String cipherName17033 =  "DES";
			try{
				System.out.println("cipherName-17033" + javax.crypto.Cipher.getInstance(cipherName17033).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if("VirtualHost".equals(record.getType()))
            {
                String cipherName17034 =  "DES";
				try{
					System.out.println("cipherName-17034" + javax.crypto.Cipher.getInstance(cipherName17034).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				record = upgradeRootRecord(record);
                Map<String, Object> virtualHostAttributes = new HashMap<String, Object>(record.getAttributes());
                virtualHostAttributes.put("name", _virtualHostNode.getName());
                virtualHostAttributes.put("modelVersion", getToVersion());
                record = new ConfiguredObjectRecordImpl(record.getId(), "VirtualHost", virtualHostAttributes, Collections.<String, UUID>emptyMap());
                _virtualHostRecord = record;
            }
            else if("Exchange".equals(record.getType()))
            {
                String cipherName17035 =  "DES";
				try{
					System.out.println("cipherName-17035" + javax.crypto.Cipher.getInstance(cipherName17035).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map<String, Object> attributes = record.getAttributes();
                String name = (String)attributes.get("name");
                _missingAmqpExchanges.remove(name);
                _exchanges.put(name, record);
            }
            else if("Queue".equals(record.getType()))
            {
                String cipherName17036 =  "DES";
				try{
					System.out.println("cipherName-17036" + javax.crypto.Cipher.getInstance(cipherName17036).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				updateQueueRecordIfNecessary(record);
            }
        }

        @Override
        public void complete()
        {
            String cipherName17037 =  "DES";
			try{
				System.out.println("cipherName-17037" + javax.crypto.Cipher.getInstance(cipherName17037).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (UUID queueId : _queuesMissingAlternateExchange.keySet())
            {
                String cipherName17038 =  "DES";
				try{
					System.out.println("cipherName-17038" + javax.crypto.Cipher.getInstance(cipherName17038).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ConfiguredObjectRecord record = getUpdateMap().get(queueId);
                if (record != null)
                {
                    String cipherName17039 =  "DES";
					try{
						System.out.println("cipherName-17039" + javax.crypto.Cipher.getInstance(cipherName17039).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String dleExchangeName = _queuesMissingAlternateExchange.get(queueId);
                    ConfiguredObjectRecord alternateExchange = _exchanges.get(dleExchangeName);
                    if (alternateExchange != null)
                    {
                        String cipherName17040 =  "DES";
						try{
							System.out.println("cipherName-17040" + javax.crypto.Cipher.getInstance(cipherName17040).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						setAlternateExchangeAttribute(record, alternateExchange);
                    }
                }
            }

            for (Entry<String, String> entry : _missingAmqpExchanges.entrySet())
            {
                String cipherName17041 =  "DES";
				try{
					System.out.println("cipherName-17041" + javax.crypto.Cipher.getInstance(cipherName17041).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String name = entry.getKey();
                String type = entry.getValue();
                UUID id = _defaultExchangeIds.get(name);

                Map<String, Object> attributes = new HashMap<String, Object>();
                attributes.put("name", name);
                attributes.put("type", type);
                attributes.put("lifetimePolicy", "PERMANENT");

                ConfiguredObjectRecord record = new ConfiguredObjectRecordImpl(id, Exchange.class.getSimpleName(), attributes, Collections.singletonMap(_virtualHostRecord.getType(), _virtualHostRecord.getId()));
                getUpdateMap().put(id, record);

            }

        }

        private ConfiguredObjectRecord updateQueueRecordIfNecessary(ConfiguredObjectRecord record)
        {
            String cipherName17042 =  "DES";
			try{
				System.out.println("cipherName-17042" + javax.crypto.Cipher.getInstance(cipherName17042).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Map<String, Object> attributes = record.getAttributes();
            boolean queueDLQEnabled = Boolean.parseBoolean(String.valueOf(attributes.get(DLQ_ENABLED_ARGUMENT)));
            if(queueDLQEnabled && attributes.get(ALTERNATE_EXCHANGE) == null)
            {
                String cipherName17043 =  "DES";
				try{
					System.out.println("cipherName-17043" + javax.crypto.Cipher.getInstance(cipherName17043).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Object queueName =  attributes.get("name");
                if (queueName == null || "".equals(queueName))
                {
                    String cipherName17044 =  "DES";
					try{
						System.out.println("cipherName-17044" + javax.crypto.Cipher.getInstance(cipherName17044).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalConfigurationException("Queue name is not found in queue configuration entry attributes: " + attributes);
                }

                String dleSuffix = System.getProperty("qpid.broker_dead_letter_exchange_suffix", DEFAULT_DLE_NAME_SUFFIX);
                String dleExchangeName = queueName + dleSuffix;

                ConfiguredObjectRecord exchangeRecord = findConfiguredObjectRecordInUpdateMap("Exchange", dleExchangeName);
                if (exchangeRecord == null)
                {
                    String cipherName17045 =  "DES";
					try{
						System.out.println("cipherName-17045" + javax.crypto.Cipher.getInstance(cipherName17045).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// add record to update Map if it is not there
                    if (!getUpdateMap().containsKey(record.getId()))
                    {
                        String cipherName17046 =  "DES";
						try{
							System.out.println("cipherName-17046" + javax.crypto.Cipher.getInstance(cipherName17046).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						getUpdateMap().put(record.getId(), record);
                    }
                    _queuesMissingAlternateExchange.put(record.getId(), dleExchangeName);
                }
                else
                {
                    String cipherName17047 =  "DES";
					try{
						System.out.println("cipherName-17047" + javax.crypto.Cipher.getInstance(cipherName17047).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					record = setAlternateExchangeAttribute(record, exchangeRecord);
                }
            }
            return record;
        }

        private ConfiguredObjectRecord setAlternateExchangeAttribute(ConfiguredObjectRecord record, ConfiguredObjectRecord alternateExchange)
        {
            String cipherName17048 =  "DES";
			try{
				System.out.println("cipherName-17048" + javax.crypto.Cipher.getInstance(cipherName17048).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Map<String, Object> attributes = new LinkedHashMap<>(record.getAttributes());
            attributes.put(ALTERNATE_EXCHANGE, alternateExchange.getId());
            record = new ConfiguredObjectRecordImpl(record.getId(), record.getType(), attributes, record.getParents());
            getUpdateMap().put(record.getId(), record);
            return record;
        }

        private ConfiguredObjectRecord findConfiguredObjectRecordInUpdateMap(String type, String name)
        {
            String cipherName17049 =  "DES";
			try{
				System.out.println("cipherName-17049" + javax.crypto.Cipher.getInstance(cipherName17049).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(ConfiguredObjectRecord record: getUpdateMap().values())
            {
                String cipherName17050 =  "DES";
				try{
					System.out.println("cipherName-17050" + javax.crypto.Cipher.getInstance(cipherName17050).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (type.equals(record.getType()) && name.equals(record.getAttributes().get("name")))
                {
                    String cipherName17051 =  "DES";
					try{
						System.out.println("cipherName-17051" + javax.crypto.Cipher.getInstance(cipherName17051).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return record;
                }
            }
            return null;
        }

    }

    private class Upgrader_2_0_to_3_0 extends StoreUpgraderPhase
    {
        public Upgrader_2_0_to_3_0()
        {
            super("modelVersion", "2.0", "3.0");
			String cipherName17052 =  "DES";
			try{
				System.out.println("cipherName-17052" + javax.crypto.Cipher.getInstance(cipherName17052).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void configuredObject(ConfiguredObjectRecord record)
        {

            String cipherName17053 =  "DES";
			try{
				System.out.println("cipherName-17053" + javax.crypto.Cipher.getInstance(cipherName17053).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if("VirtualHost".equals(record.getType()))
            {
                String cipherName17054 =  "DES";
				try{
					System.out.println("cipherName-17054" + javax.crypto.Cipher.getInstance(cipherName17054).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				upgradeRootRecord(record);
            }
        }

        @Override
        public void complete()
        {
			String cipherName17055 =  "DES";
			try{
				System.out.println("cipherName-17055" + javax.crypto.Cipher.getInstance(cipherName17055).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

    }
    private class Upgrader_3_0_to_6_0 extends StoreUpgraderPhase
    {
        public Upgrader_3_0_to_6_0()
        {
            super("modelVersion", "3.0", "6.0");
			String cipherName17056 =  "DES";
			try{
				System.out.println("cipherName-17056" + javax.crypto.Cipher.getInstance(cipherName17056).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void configuredObject(ConfiguredObjectRecord record)
        {

            String cipherName17057 =  "DES";
			try{
				System.out.println("cipherName-17057" + javax.crypto.Cipher.getInstance(cipherName17057).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if("VirtualHost".equals(record.getType()))
            {
                String cipherName17058 =  "DES";
				try{
					System.out.println("cipherName-17058" + javax.crypto.Cipher.getInstance(cipherName17058).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				upgradeRootRecord(record);
            }
        }

        @Override
        public void complete()
        {
			String cipherName17059 =  "DES";
			try{
				System.out.println("cipherName-17059" + javax.crypto.Cipher.getInstance(cipherName17059).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

    }

    private class Upgrader_6_0_to_6_1 extends StoreUpgraderPhase
    {
        public Upgrader_6_0_to_6_1()
        {
            super("modelVersion", "6.0", "6.1");
			String cipherName17060 =  "DES";
			try{
				System.out.println("cipherName-17060" + javax.crypto.Cipher.getInstance(cipherName17060).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void configuredObject(ConfiguredObjectRecord record)
        {

            String cipherName17061 =  "DES";
			try{
				System.out.println("cipherName-17061" + javax.crypto.Cipher.getInstance(cipherName17061).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if("VirtualHost".equals(record.getType()))
            {
                String cipherName17062 =  "DES";
				try{
					System.out.println("cipherName-17062" + javax.crypto.Cipher.getInstance(cipherName17062).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				upgradeRootRecord(record);
            }
        }

        @Override
        public void complete()
        {
			String cipherName17063 =  "DES";
			try{
				System.out.println("cipherName-17063" + javax.crypto.Cipher.getInstance(cipherName17063).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

    }

    private static final FixedKeyMapCreator BINDING_MAP_CREATOR = new FixedKeyMapCreator("bindingKey", "destination", "arguments");
    private static final FixedKeyMapCreator NO_ARGUMENTS_BINDING_MAP_CREATOR = new FixedKeyMapCreator("bindingKey", "destination");

    private class Upgrader_6_1_to_7_0 extends StoreUpgraderPhase
    {
        private final Map<UUID, List<BindingRecord>> _exchangeBindings = new HashMap<>();
        private final Map<UUID, ConfiguredObjectRecord> _exchanges = new HashMap<>();
        private final Map<UUID, String> _queues = new HashMap<>();
        private final Map<String, List<Map<String,Object>>> _queueBindings = new HashMap<>();
        private Set<UUID> _destinationsWithAlternateExchange = new HashSet<>();


        public Upgrader_6_1_to_7_0()
        {
            super("modelVersion", "6.1", "7.0");
			String cipherName17064 =  "DES";
			try{
				System.out.println("cipherName-17064" + javax.crypto.Cipher.getInstance(cipherName17064).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void configuredObject(ConfiguredObjectRecord record)
        {
            String cipherName17065 =  "DES";
			try{
				System.out.println("cipherName-17065" + javax.crypto.Cipher.getInstance(cipherName17065).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if("VirtualHost".equals(record.getType()))
            {
                String cipherName17066 =  "DES";
				try{
					System.out.println("cipherName-17066" + javax.crypto.Cipher.getInstance(cipherName17066).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				record = upgradeRootRecord(record);
                Map<String, Object> attributes = new HashMap<>(record.getAttributes());
                boolean modified = attributes.remove("queue_deadLetterQueueEnabled") != null;
                Object context = attributes.get("context");
                Map<String,Object> contextMap = null;
                if(context instanceof Map)
                {
                    String cipherName17067 =  "DES";
					try{
						System.out.println("cipherName-17067" + javax.crypto.Cipher.getInstance(cipherName17067).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					contextMap = new HashMap<>((Map<String,Object>) context);
                    modified |= contextMap.remove("queue.deadLetterQueueEnabled") != null;
                    if (modified)
                    {
                        String cipherName17068 =  "DES";
						try{
							System.out.println("cipherName-17068" + javax.crypto.Cipher.getInstance(cipherName17068).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						attributes.put("context", contextMap);
                    }
                }

                int brokerStatisticsReportingPeriod = ((Broker) _virtualHostNode.getParent()).getStatisticsReportingPeriod();
                if (brokerStatisticsReportingPeriod > 0)
                {
                    String cipherName17069 =  "DES";
					try{
						System.out.println("cipherName-17069" + javax.crypto.Cipher.getInstance(cipherName17069).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					attributes.put("statisticsReportingPeriod", brokerStatisticsReportingPeriod);
                    if (contextMap == null)
                    {
                        String cipherName17070 =  "DES";
						try{
							System.out.println("cipherName-17070" + javax.crypto.Cipher.getInstance(cipherName17070).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						contextMap = new HashMap<>();
                    }

                    contextMap.put("qpid.virtualhost.statisticsReportPattern", "${ancestor:virtualhost:name}: messagesIn=${messagesIn}, bytesIn=${bytesIn:byteunit}, messagesOut=${messagesOut}, bytesOut=${bytesOut:byteunit}");
                    attributes.put("context", contextMap);
                    modified = true;
                }

                if (modified)
                {
                    String cipherName17071 =  "DES";
					try{
						System.out.println("cipherName-17071" + javax.crypto.Cipher.getInstance(cipherName17071).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					record = new ConfiguredObjectRecordImpl(record.getId(), record.getType(), attributes, record.getParents());
                    getUpdateMap().put(record.getId(), record);
                }

            }
            else if("Binding".equals(record.getType()))
            {
                String cipherName17072 =  "DES";
				try{
					System.out.println("cipherName-17072" + javax.crypto.Cipher.getInstance(cipherName17072).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				BindingRecord binding = new BindingRecord(String.valueOf(record.getAttributes().get("name")),
                                                          record.getParents().get("Queue").toString(),
                                                          record.getAttributes().get("arguments"));
                final UUID exchangeId = record.getParents().get("Exchange");
                List<BindingRecord> existingBindings = _exchangeBindings.get(exchangeId);
                if(existingBindings == null)
                {
                    String cipherName17073 =  "DES";
					try{
						System.out.println("cipherName-17073" + javax.crypto.Cipher.getInstance(cipherName17073).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					existingBindings = new ArrayList<>();
                    _exchangeBindings.put(exchangeId, existingBindings);
                }
                existingBindings.add(binding);
                getDeleteMap().put(record.getId(), record);
            }
            else if("Exchange".equals(record.getType()))
            {
                String cipherName17074 =  "DES";
				try{
					System.out.println("cipherName-17074" + javax.crypto.Cipher.getInstance(cipherName17074).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				final UUID exchangeId = record.getId();
                _exchanges.put(exchangeId, record);
                if(record.getAttributes().containsKey("bindings"))
                {
                    String cipherName17075 =  "DES";
					try{
						System.out.println("cipherName-17075" + javax.crypto.Cipher.getInstance(cipherName17075).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					List<BindingRecord> existingBindings = _exchangeBindings.get(exchangeId);
                    if(existingBindings == null)
                    {
                        String cipherName17076 =  "DES";
						try{
							System.out.println("cipherName-17076" + javax.crypto.Cipher.getInstance(cipherName17076).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						existingBindings = new ArrayList<>();
                        _exchangeBindings.put(exchangeId, existingBindings);
                    }

                    List<Map<String,Object>> bindingList =
                            (List<Map<String, Object>>) record.getAttributes().get("bindings");
                    for(Map<String,Object> existingBinding : bindingList)
                    {
                        String cipherName17077 =  "DES";
						try{
							System.out.println("cipherName-17077" + javax.crypto.Cipher.getInstance(cipherName17077).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						existingBindings.add(new BindingRecord((String)existingBinding.get("name"),
                                                               String.valueOf(existingBinding.get("queue")),
                                                               existingBinding.get("arguments")));
                    }
                }

                if (record.getAttributes().containsKey("alternateExchange"))
                {
                    String cipherName17078 =  "DES";
					try{
						System.out.println("cipherName-17078" + javax.crypto.Cipher.getInstance(cipherName17078).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_destinationsWithAlternateExchange.add(record.getId());

                    getUpdateMap().put(record.getId(), record);
                }
            }
            else if("Queue".equals(record.getType()))
            {
                String cipherName17079 =  "DES";
				try{
					System.out.println("cipherName-17079" + javax.crypto.Cipher.getInstance(cipherName17079).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map<String, Object> attributes = new HashMap<>(record.getAttributes());
                Object queueFlowControlSizeBytes = attributes.remove("queueFlowControlSizeBytes");
                Object queueFlowResumeSizeBytes = attributes.remove("queueFlowResumeSizeBytes");
                if (queueFlowControlSizeBytes != null)
                {
                    String cipherName17080 =  "DES";
					try{
						System.out.println("cipherName-17080" + javax.crypto.Cipher.getInstance(cipherName17080).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					long queueFlowControlSizeBytesValue = convertAttributeValueToLong("queueFlowControlSizeBytes",
                                                                                      queueFlowControlSizeBytes);
                    if (queueFlowControlSizeBytesValue > 0)
                    {
                        String cipherName17081 =  "DES";
						try{
							System.out.println("cipherName-17081" + javax.crypto.Cipher.getInstance(cipherName17081).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if (queueFlowResumeSizeBytes != null)
                        {
                            String cipherName17082 =  "DES";
							try{
								System.out.println("cipherName-17082" + javax.crypto.Cipher.getInstance(cipherName17082).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							long queueFlowResumeSizeBytesValue =
                                    convertAttributeValueToLong("queueFlowResumeSizeBytes", queueFlowResumeSizeBytes);
                            double ratio = ((double) queueFlowResumeSizeBytesValue)
                                           / ((double) queueFlowControlSizeBytesValue);
                            String flowResumeLimit = String.format("%.2f", ratio * 100.0);

                            Object context = attributes.get("context");
                            Map<String, String> contextMap;
                            if (context instanceof Map)
                            {
                                String cipherName17083 =  "DES";
								try{
									System.out.println("cipherName-17083" + javax.crypto.Cipher.getInstance(cipherName17083).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								contextMap = (Map) context;
                            }
                            else
                            {
                                String cipherName17084 =  "DES";
								try{
									System.out.println("cipherName-17084" + javax.crypto.Cipher.getInstance(cipherName17084).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								contextMap = new HashMap<>();
                                attributes.put("context", contextMap);
                            }
                            contextMap.put("queue.queueFlowResumeLimit", flowResumeLimit);
                        }
                        attributes.put("overflowPolicy", "PRODUCER_FLOW_CONTROL");
                        attributes.put("maximumQueueDepthBytes", queueFlowControlSizeBytes);
                    }
                }

                boolean addToUpdateMap = false;
                if (attributes.containsKey("alternateExchange"))
                {
                    String cipherName17085 =  "DES";
					try{
						System.out.println("cipherName-17085" + javax.crypto.Cipher.getInstance(cipherName17085).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_destinationsWithAlternateExchange.add(record.getId());
                    addToUpdateMap = true;

                }

                if(attributes.containsKey("bindings"))
                {
                    String cipherName17086 =  "DES";
					try{
						System.out.println("cipherName-17086" + javax.crypto.Cipher.getInstance(cipherName17086).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_queueBindings.put(String.valueOf(attributes.get("name")),
                                       (List<Map<String, Object>>) attributes.get("bindings"));
                    attributes.remove("bindings");
                }

                if(attributes.containsKey("messageGroupKey"))
                {
                    String cipherName17087 =  "DES";
					try{
						System.out.println("cipherName-17087" + javax.crypto.Cipher.getInstance(cipherName17087).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(attributes.containsKey("messageGroupSharedGroups")
                       && convertAttributeValueToBoolean("messageGroupSharedGroups",
                                                         attributes.remove("messageGroupSharedGroups")))
                    {
                        String cipherName17088 =  "DES";
						try{
							System.out.println("cipherName-17088" + javax.crypto.Cipher.getInstance(cipherName17088).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						attributes.put("messageGroupType", "SHARED_GROUPS");

                    }
                    else
                    {
                        String cipherName17089 =  "DES";
						try{
							System.out.println("cipherName-17089" + javax.crypto.Cipher.getInstance(cipherName17089).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						attributes.put("messageGroupType", "STANDARD");
                    }
                    Object oldMessageGroupKey = attributes.remove("messageGroupKey");
                    if (!"JMSXGroupId".equals(oldMessageGroupKey))
                    {
                        String cipherName17090 =  "DES";
						try{
							System.out.println("cipherName-17090" + javax.crypto.Cipher.getInstance(cipherName17090).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						attributes.put("messageGroupKeyOverride", oldMessageGroupKey);
                    }
                }
                else
                {
                    String cipherName17091 =  "DES";
					try{
						System.out.println("cipherName-17091" + javax.crypto.Cipher.getInstance(cipherName17091).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					attributes.put("messageGroupType", "NONE");
                }

                _queues.put(record.getId(), (String) attributes.get("name"));

                if (!attributes.equals(new HashMap<>(record.getAttributes())) || addToUpdateMap)
                {
                    String cipherName17092 =  "DES";
					try{
						System.out.println("cipherName-17092" + javax.crypto.Cipher.getInstance(cipherName17092).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					getUpdateMap().put(record.getId(),
                                       new ConfiguredObjectRecordImpl(record.getId(),
                                                                      record.getType(),
                                                                      attributes,
                                                                      record.getParents()));
                }
            }
            else if (record.getType().equals("VirtualHostLogger"))
            {
                String cipherName17093 =  "DES";
				try{
					System.out.println("cipherName-17093" + javax.crypto.Cipher.getInstance(cipherName17093).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map<String,Object> attributes = new HashMap<>();
                attributes.put("name", "statistics-" + record.getAttributes().get("name"));
                attributes.put("level", "INFO");
                attributes.put("loggerName", "qpid.statistics.*");
                attributes.put("type", "NameAndLevel");


                final ConfiguredObjectRecord filterRecord = new ConfiguredObjectRecordImpl(UUID.randomUUID(),
                                                                                           "VirtualHostLogInclusionRule",
                                                                                           attributes,
                                                                                           Collections.singletonMap("VirtualHostLogger",
                                                                                                                    record.getId()));
                getUpdateMap().put(filterRecord.getId(), filterRecord);
            }
        }

        private long convertAttributeValueToLong(final String attributeName,
                                                 final Object attributeValue)
        {
            String cipherName17094 =  "DES";
			try{
				System.out.println("cipherName-17094" + javax.crypto.Cipher.getInstance(cipherName17094).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long value;
            if (attributeValue instanceof Number)
            {
                String cipherName17095 =  "DES";
				try{
					System.out.println("cipherName-17095" + javax.crypto.Cipher.getInstance(cipherName17095).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				value = ((Number) attributeValue).longValue();
            }
            else if (attributeValue instanceof String)
            {
                String cipherName17096 =  "DES";
				try{
					System.out.println("cipherName-17096" + javax.crypto.Cipher.getInstance(cipherName17096).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try
                {
                    String cipherName17097 =  "DES";
					try{
						System.out.println("cipherName-17097" + javax.crypto.Cipher.getInstance(cipherName17097).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					value = Long.parseLong((String) attributeValue);
                }
                catch (Exception e)
                {
                    String cipherName17098 =  "DES";
					try{
						System.out.println("cipherName-17098" + javax.crypto.Cipher.getInstance(cipherName17098).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalConfigurationException(String.format(
                            "Cannot evaluate '%s': %s",
                            attributeName, attributeValue));
                }
            }
            else
            {
                String cipherName17099 =  "DES";
				try{
					System.out.println("cipherName-17099" + javax.crypto.Cipher.getInstance(cipherName17099).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException(String.format("Cannot evaluate '%s': %s",
                                                                      attributeName,
                                                                      String.valueOf(attributeValue)));
            }
            return value;
        }

        private boolean convertAttributeValueToBoolean(final String attributeName,
                                                       final Object attributeValue)
        {
            String cipherName17100 =  "DES";
			try{
				System.out.println("cipherName-17100" + javax.crypto.Cipher.getInstance(cipherName17100).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean value;
            if (attributeValue instanceof Boolean)
            {
                String cipherName17101 =  "DES";
				try{
					System.out.println("cipherName-17101" + javax.crypto.Cipher.getInstance(cipherName17101).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				value = (Boolean) attributeValue;
            }
            else if (attributeValue instanceof String)
            {
                String cipherName17102 =  "DES";
				try{
					System.out.println("cipherName-17102" + javax.crypto.Cipher.getInstance(cipherName17102).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String strValue = (String)attributeValue;
                if(strValue.equalsIgnoreCase("true"))
                {
                    String cipherName17103 =  "DES";
					try{
						System.out.println("cipherName-17103" + javax.crypto.Cipher.getInstance(cipherName17103).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					value = true;
                }
                else if(strValue.equalsIgnoreCase("false"))
                {
                    String cipherName17104 =  "DES";
					try{
						System.out.println("cipherName-17104" + javax.crypto.Cipher.getInstance(cipherName17104).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					value = false;
                }
                else
                {
                    String cipherName17105 =  "DES";
					try{
						System.out.println("cipherName-17105" + javax.crypto.Cipher.getInstance(cipherName17105).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalConfigurationException(String.format(
                            "Cannot evaluate '%s': %s",
                            attributeName, attributeValue));
                }

            }
            else
            {
                String cipherName17106 =  "DES";
				try{
					System.out.println("cipherName-17106" + javax.crypto.Cipher.getInstance(cipherName17106).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException(String.format("Cannot evaluate '%s': %s",
                                                                      attributeName,
                                                                      String.valueOf(attributeValue)));
            }
            return value;
        }


        @Override
        public void complete()
        {
            String cipherName17107 =  "DES";
			try{
				System.out.println("cipherName-17107" + javax.crypto.Cipher.getInstance(cipherName17107).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Map.Entry<String, List<Map<String,Object>>> entry : _queueBindings.entrySet())
            {
                String cipherName17108 =  "DES";
				try{
					System.out.println("cipherName-17108" + javax.crypto.Cipher.getInstance(cipherName17108).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Map<String, Object> existingBinding : entry.getValue())
                {
                    String cipherName17109 =  "DES";
					try{
						System.out.println("cipherName-17109" + javax.crypto.Cipher.getInstance(cipherName17109).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					UUID exchangeId;
                    if(existingBinding.get("exchange") instanceof UUID)
                    {
                        String cipherName17110 =  "DES";
						try{
							System.out.println("cipherName-17110" + javax.crypto.Cipher.getInstance(cipherName17110).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						exchangeId = (UUID) existingBinding.get("exchange");
                    }
                    else
                    {
                        String cipherName17111 =  "DES";
						try{
							System.out.println("cipherName-17111" + javax.crypto.Cipher.getInstance(cipherName17111).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						exchangeId = getExchangeIdFromNameOrId( existingBinding.get("exchange").toString());
                    }
                    List<BindingRecord> existingBindings = _exchangeBindings.get(exchangeId);
                    if(existingBindings == null)
                    {
                        String cipherName17112 =  "DES";
						try{
							System.out.println("cipherName-17112" + javax.crypto.Cipher.getInstance(cipherName17112).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						existingBindings = new ArrayList<>();
                        _exchangeBindings.put(exchangeId, existingBindings);
                    }
                    existingBindings.add(new BindingRecord((String)existingBinding.get("name"),
                                                           entry.getKey(),
                                                           existingBinding.get("arguments")));
                }
            }

            for(Map.Entry<UUID, List<BindingRecord>> entry : _exchangeBindings.entrySet())
            {
                String cipherName17113 =  "DES";
				try{
					System.out.println("cipherName-17113" + javax.crypto.Cipher.getInstance(cipherName17113).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ConfiguredObjectRecord exchangeRecord = _exchanges.get(entry.getKey());
                if(exchangeRecord != null)
                {
                    String cipherName17114 =  "DES";
					try{
						System.out.println("cipherName-17114" + javax.crypto.Cipher.getInstance(cipherName17114).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					final List<BindingRecord> bindingRecords = entry.getValue();
                    List<Map<String,Object>> actualBindings = new ArrayList<>(bindingRecords.size());
                    for(BindingRecord bindingRecord : bindingRecords)
                    {
                        String cipherName17115 =  "DES";
						try{
							System.out.println("cipherName-17115" + javax.crypto.Cipher.getInstance(cipherName17115).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(bindingRecord._arguments == null)
                        {
                            String cipherName17116 =  "DES";
							try{
								System.out.println("cipherName-17116" + javax.crypto.Cipher.getInstance(cipherName17116).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							actualBindings.add(NO_ARGUMENTS_BINDING_MAP_CREATOR.createMap(bindingRecord._name,
                                                                                          getQueueFromIdOrName(bindingRecord)));
                        }
                        else
                        {
                            String cipherName17117 =  "DES";
							try{
								System.out.println("cipherName-17117" + javax.crypto.Cipher.getInstance(cipherName17117).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							actualBindings.add(BINDING_MAP_CREATOR.createMap(bindingRecord._name,
                                                                             getQueueFromIdOrName(bindingRecord), bindingRecord._arguments));
                        }
                    }
                    Map<String, Object> updatedAttributes = new HashMap<>(exchangeRecord.getAttributes());
                    updatedAttributes.remove("bindings");
                    updatedAttributes.put("durableBindings", actualBindings);
                    exchangeRecord = new ConfiguredObjectRecordImpl(exchangeRecord.getId(), exchangeRecord.getType(), updatedAttributes, exchangeRecord.getParents());
                    getUpdateMap().put(exchangeRecord.getId(), exchangeRecord);
                }

            }

            for (UUID recordId : _destinationsWithAlternateExchange)
            {
                String cipherName17118 =  "DES";
				try{
					System.out.println("cipherName-17118" + javax.crypto.Cipher.getInstance(cipherName17118).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ConfiguredObjectRecord record = getUpdateMap().get(recordId);
                Map<String, Object> attributes = new HashMap<>(record.getAttributes());

                String exchangeNameOrUuid = String.valueOf(attributes.remove("alternateExchange"));

                ConfiguredObjectRecord exchangeRecord = getExchangeFromNameOrUUID(exchangeNameOrUuid);
                if (exchangeRecord != null)
                {
                    String cipherName17119 =  "DES";
					try{
						System.out.println("cipherName-17119" + javax.crypto.Cipher.getInstance(cipherName17119).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					attributes.put("alternateBinding",
                                   Collections.singletonMap("destination", exchangeRecord.getAttributes().get("name")));
                }
                else
                {
                    String cipherName17120 =  "DES";
					try{
						System.out.println("cipherName-17120" + javax.crypto.Cipher.getInstance(cipherName17120).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalConfigurationException(String.format(
                            "Cannot upgrade record UUID '%s' as cannot find exchange with name or UUID '%s'",
                            recordId,
                            exchangeNameOrUuid));
                }

                getUpdateMap().put(record.getId(),
                                   new ConfiguredObjectRecordImpl(record.getId(),
                                                                  record.getType(),
                                                                  attributes,
                                                                  record.getParents()));
            }
        }

        private ConfiguredObjectRecord getExchangeFromNameOrUUID(final String exchangeNameOrUuid)
        {
            String cipherName17121 =  "DES";
			try{
				System.out.println("cipherName-17121" + javax.crypto.Cipher.getInstance(cipherName17121).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(ConfiguredObjectRecord record : _exchanges.values())
            {
                String cipherName17122 =  "DES";
				try{
					System.out.println("cipherName-17122" + javax.crypto.Cipher.getInstance(cipherName17122).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(exchangeNameOrUuid.equals(record.getAttributes().get("name")))
                {
                    String cipherName17123 =  "DES";
					try{
						System.out.println("cipherName-17123" + javax.crypto.Cipher.getInstance(cipherName17123).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return record;
                }
                else
                {
                    String cipherName17124 =  "DES";
					try{
						System.out.println("cipherName-17124" + javax.crypto.Cipher.getInstance(cipherName17124).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try
                    {
                        String cipherName17125 =  "DES";
						try{
							System.out.println("cipherName-17125" + javax.crypto.Cipher.getInstance(cipherName17125).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						UUID uuid = UUID.fromString(exchangeNameOrUuid);
                        if (uuid.equals(record.getId()))
                        {
                            String cipherName17126 =  "DES";
							try{
								System.out.println("cipherName-17126" + javax.crypto.Cipher.getInstance(cipherName17126).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return record;
                        }
                    }
                    catch (IllegalArgumentException e)
                    {
						String cipherName17127 =  "DES";
						try{
							System.out.println("cipherName-17127" + javax.crypto.Cipher.getInstance(cipherName17127).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
                        // ignore - not a UUID
                    }
                }
            }

            return null;
        }

        private UUID getExchangeIdFromNameOrId(final String exchange)
        {
            String cipherName17128 =  "DES";
			try{
				System.out.println("cipherName-17128" + javax.crypto.Cipher.getInstance(cipherName17128).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(ConfiguredObjectRecord record : _exchanges.values())
            {
                String cipherName17129 =  "DES";
				try{
					System.out.println("cipherName-17129" + javax.crypto.Cipher.getInstance(cipherName17129).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(exchange.equals(record.getAttributes().get("name")))
                {
                    String cipherName17130 =  "DES";
					try{
						System.out.println("cipherName-17130" + javax.crypto.Cipher.getInstance(cipherName17130).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return record.getId();
                }
            }
            return UUID.fromString(exchange);
        }

        private String getQueueFromIdOrName(final BindingRecord bindingRecord)
        {
            String cipherName17131 =  "DES";
			try{
				System.out.println("cipherName-17131" + javax.crypto.Cipher.getInstance(cipherName17131).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final String queueIdOrName = bindingRecord._queueIdOrName;
            try
            {
                String cipherName17132 =  "DES";
				try{
					System.out.println("cipherName-17132" + javax.crypto.Cipher.getInstance(cipherName17132).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				UUID queueId = UUID.fromString(queueIdOrName);
                String name = _queues.get(queueId);
                return name == null ? queueIdOrName : name;
            }
            catch(IllegalArgumentException e)
            {
                String cipherName17133 =  "DES";
				try{
					System.out.println("cipherName-17133" + javax.crypto.Cipher.getInstance(cipherName17133).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return queueIdOrName;
            }
        }

        private class BindingRecord
        {
            private final String _name;
            private final String _queueIdOrName;
            private final Object _arguments;

            public BindingRecord(final String name, final String queueIdOrName, final Object arguments)
            {
                String cipherName17134 =  "DES";
				try{
					System.out.println("cipherName-17134" + javax.crypto.Cipher.getInstance(cipherName17134).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_name = name;
                _queueIdOrName = queueIdOrName;
                _arguments = arguments;
            }
        }
    }


    private class Upgrader_7_0_to_7_1 extends StoreUpgraderPhase
    {

        public Upgrader_7_0_to_7_1()
        {
            super("modelVersion", "7.0", "7.1");
			String cipherName17135 =  "DES";
			try{
				System.out.println("cipherName-17135" + javax.crypto.Cipher.getInstance(cipherName17135).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void configuredObject(final ConfiguredObjectRecord record)
        {
            String cipherName17136 =  "DES";
			try{
				System.out.println("cipherName-17136" + javax.crypto.Cipher.getInstance(cipherName17136).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if("VirtualHost".equals(record.getType()))
            {
                String cipherName17137 =  "DES";
				try{
					System.out.println("cipherName-17137" + javax.crypto.Cipher.getInstance(cipherName17137).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				upgradeRootRecord(record);
            }
        }

        @Override
        public void complete()
        {
			String cipherName17138 =  "DES";
			try{
				System.out.println("cipherName-17138" + javax.crypto.Cipher.getInstance(cipherName17138).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }
    }

    private class Upgrader_7_1_to_8_0 extends StoreUpgraderPhase
    {

        public Upgrader_7_1_to_8_0()
        {
            super("modelVersion", "7.1", "8.0");
			String cipherName17139 =  "DES";
			try{
				System.out.println("cipherName-17139" + javax.crypto.Cipher.getInstance(cipherName17139).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void configuredObject(final ConfiguredObjectRecord record)
        {
            String cipherName17140 =  "DES";
			try{
				System.out.println("cipherName-17140" + javax.crypto.Cipher.getInstance(cipherName17140).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if("VirtualHost".equals(record.getType()))
            {
                String cipherName17141 =  "DES";
				try{
					System.out.println("cipherName-17141" + javax.crypto.Cipher.getInstance(cipherName17141).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				upgradeRootRecord(record);
            }
        }

        @Override
        public void complete()
        {
			String cipherName17142 =  "DES";
			try{
				System.out.println("cipherName-17142" + javax.crypto.Cipher.getInstance(cipherName17142).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }
    }

    public boolean upgradeAndRecover(final DurableConfigurationStore durableConfigurationStore,
                                     final ConfiguredObjectRecord... initialRecords)
    {
        String cipherName17143 =  "DES";
		try{
			System.out.println("cipherName-17143" + javax.crypto.Cipher.getInstance(cipherName17143).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final List<ConfiguredObjectRecord> records = new ArrayList<>();
        boolean isNew = durableConfigurationStore.openConfigurationStore(new ConfiguredObjectRecordHandler()
        {
            @Override
            public void handle(final ConfiguredObjectRecord record)
            {
                String cipherName17144 =  "DES";
				try{
					System.out.println("cipherName-17144" + javax.crypto.Cipher.getInstance(cipherName17144).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				records.add(record);
            }
        }, initialRecords);

        List<ConfiguredObjectRecord> upgradedRecords = upgrade(durableConfigurationStore,
                                                               records,
                                                               VirtualHost.class.getSimpleName(),
                                                               VirtualHost.MODEL_VERSION);
        recover(_virtualHostNode, durableConfigurationStore, upgradedRecords, isNew);
        return isNew;
    }

    public void reloadAndRecover(final DurableConfigurationStore durableConfigurationStore)
    {
        String cipherName17145 =  "DES";
		try{
			System.out.println("cipherName-17145" + javax.crypto.Cipher.getInstance(cipherName17145).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		reloadAndRecoverInternal(_virtualHostNode, durableConfigurationStore);
    }

    public void reloadAndRecoverVirtualHost(final DurableConfigurationStore durableConfigurationStore)
    {
        String cipherName17146 =  "DES";
		try{
			System.out.println("cipherName-17146" + javax.crypto.Cipher.getInstance(cipherName17146).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		reloadAndRecoverInternal(_virtualHostNode.getVirtualHost(), durableConfigurationStore);
    }

    private void reloadAndRecoverInternal(final ConfiguredObject<?> recoveryRoot,
                                          final DurableConfigurationStore durableConfigurationStore)
    {
        String cipherName17147 =  "DES";
		try{
			System.out.println("cipherName-17147" + javax.crypto.Cipher.getInstance(cipherName17147).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final List<ConfiguredObjectRecord> records = new ArrayList<>();
        durableConfigurationStore.reload(records::add);
        recover(recoveryRoot, durableConfigurationStore, records, false);
    }

    private void recover(final ConfiguredObject<?> recoveryRoot,
                         final DurableConfigurationStore durableConfigurationStore,
                         final List<ConfiguredObjectRecord> records,
                         final boolean isNew)
    {
        String cipherName17148 =  "DES";
		try{
			System.out.println("cipherName-17148" + javax.crypto.Cipher.getInstance(cipherName17148).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		new GenericRecoverer(recoveryRoot).recover(records, isNew);

        final StoreConfigurationChangeListener
                configChangeListener = new StoreConfigurationChangeListener(durableConfigurationStore);
        if(_virtualHostNode.getVirtualHost() != null)
        {
            String cipherName17149 =  "DES";
			try{
				System.out.println("cipherName-17149" + javax.crypto.Cipher.getInstance(cipherName17149).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			applyRecursively(_virtualHostNode.getVirtualHost(), new RecursiveAction<ConfiguredObject<?>>()
            {
                @Override
                public boolean applyToChildren(final ConfiguredObject<?> object)
                {
                    String cipherName17150 =  "DES";
					try{
						System.out.println("cipherName-17150" + javax.crypto.Cipher.getInstance(cipherName17150).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return object.isDurable();
                }

                @Override
                public void performAction(final ConfiguredObject<?> object)
                {
                    String cipherName17151 =  "DES";
					try{
						System.out.println("cipherName-17151" + javax.crypto.Cipher.getInstance(cipherName17151).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					object.addChangeListener(configChangeListener);
                }
            });
        }

        if (recoveryRoot instanceof VirtualHostNode)
        {
            String cipherName17152 =  "DES";
			try{
				System.out.println("cipherName-17152" + javax.crypto.Cipher.getInstance(cipherName17152).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_virtualHostNode.addChangeListener(new AbstractConfigurationChangeListener()
            {
                @Override
                public void childAdded(final ConfiguredObject<?> object, final ConfiguredObject<?> child)
                {
                    String cipherName17153 =  "DES";
					try{
						System.out.println("cipherName-17153" + javax.crypto.Cipher.getInstance(cipherName17153).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (child instanceof VirtualHost)
                    {
                        String cipherName17154 =  "DES";
						try{
							System.out.println("cipherName-17154" + javax.crypto.Cipher.getInstance(cipherName17154).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						applyRecursively(child, new RecursiveAction<ConfiguredObject<?>>()
                        {
                            @Override
                            public boolean applyToChildren(final ConfiguredObject<?> object)
                            {
                                String cipherName17155 =  "DES";
								try{
									System.out.println("cipherName-17155" + javax.crypto.Cipher.getInstance(cipherName17155).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								return object.isDurable();
                            }

                            @Override
                            public void performAction(final ConfiguredObject<?> object)
                            {
                                String cipherName17156 =  "DES";
								try{
									System.out.println("cipherName-17156" + javax.crypto.Cipher.getInstance(cipherName17156).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if (object.isDurable())
                                {
                                    String cipherName17157 =  "DES";
									try{
										System.out.println("cipherName-17157" + javax.crypto.Cipher.getInstance(cipherName17157).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									durableConfigurationStore.update(true, object.asObjectRecord());
                                    object.addChangeListener(configChangeListener);
                                }
                            }
                        });
                    }
                }

                @Override
                public void childRemoved(final ConfiguredObject<?> object, final ConfiguredObject<?> child)
                {
                    String cipherName17158 =  "DES";
					try{
						System.out.println("cipherName-17158" + javax.crypto.Cipher.getInstance(cipherName17158).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (child instanceof VirtualHost)
                    {
                        String cipherName17159 =  "DES";
						try{
							System.out.println("cipherName-17159" + javax.crypto.Cipher.getInstance(cipherName17159).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						child.removeChangeListener(configChangeListener);
                    }
                }
            });
            if (isNew)
            {
                String cipherName17160 =  "DES";
				try{
					System.out.println("cipherName-17160" + javax.crypto.Cipher.getInstance(cipherName17160).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (_virtualHostNode instanceof AbstractConfiguredObject)
                {
                    String cipherName17161 =  "DES";
					try{
						System.out.println("cipherName-17161" + javax.crypto.Cipher.getInstance(cipherName17161).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					((AbstractConfiguredObject) _virtualHostNode).forceUpdateAllSecureAttributes();
                }
            }
        }
    }
}
