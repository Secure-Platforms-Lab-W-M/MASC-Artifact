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
package org.apache.qpid.server.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.model.AlternateBinding;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.ConfiguredObjectAttribute;
import org.apache.qpid.server.model.ConfiguredObjectTypeRegistry;
import org.apache.qpid.server.model.Model;
import org.apache.qpid.server.model.OverflowPolicy;
import org.apache.qpid.server.model.Queue;
import org.apache.qpid.server.util.ConnectionScopedRuntimeException;

public class QueueArgumentsConverter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueArgumentsConverter.class);

    private static final String SHARED_MSG_GROUP_ARG_VALUE = "1";
    private static final String X_QPID_FLOW_RESUME_CAPACITY = "x-qpid-flow-resume-capacity";
    private static final String X_QPID_CAPACITY = "x-qpid-capacity";
    private static final String X_QPID_MINIMUM_ALERT_REPEAT_GAP = "x-qpid-minimum-alert-repeat-gap";
    private static final String X_QPID_MAXIMUM_MESSAGE_COUNT = "x-qpid-maximum-message-count";
    private static final String X_QPID_MAXIMUM_MESSAGE_SIZE = "x-qpid-maximum-message-size";
    private static final String X_QPID_MAXIMUM_MESSAGE_AGE = "x-qpid-maximum-message-age";
    private static final String X_QPID_MAXIMUM_QUEUE_DEPTH = "x-qpid-maximum-queue-depth";

    private static final String QPID_ALERT_COUNT = "qpid.alert_count";
    private static final String QPID_ALERT_SIZE = "qpid.alert_size";
    private static final String QPID_ALERT_REPEAT_GAP = "qpid.alert_repeat_gap";

    public static final String X_QPID_PRIORITIES = "x-qpid-priorities";

    public static final String X_QPID_DESCRIPTION = "x-qpid-description";

    public static final String X_SINGLE_ACTIVE_CONSUMER = "x-single-active-consumer";

    private static final String QPID_LAST_VALUE_QUEUE_KEY = "qpid.last_value_queue_key";

    private static final String QPID_QUEUE_SORT_KEY = "qpid.queue_sort_key";
    private static final String X_QPID_DLQ_ENABLED = "x-qpid-dlq-enabled";
    private static final String X_QPID_MAXIMUM_DELIVERY_COUNT = "x-qpid-maximum-delivery-count";
    private static final String QPID_GROUP_HEADER_KEY = "qpid.group_header_key";
    private static final String QPID_SHARED_MSG_GROUP = "qpid.shared_msg_group";
    private static final String QPID_DEFAULT_MESSAGE_GROUP_ARG = "qpid.default-message-group";

    private static final String QPID_MESSAGE_DURABILITY = "qpid.message_durability";

    private static final String QPID_LAST_VALUE_QUEUE = "qpid.last_value_queue";

    private static final String QPID_DEFAULT_FILTERS = "qpid.default_filters";

    private static final String QPID_ENSURE_NONDESTRUCTIVE_CONSUMERS = "qpid.ensure_nondestructive_consumers";

    private static final String QPID_EXCLUSIVITY_POLICY = "qpid.exclusivity_policy";
    private static final String QPID_LIFETIME_POLICY = "qpid.lifetime_policy";

    private static final String QPID_POLICY_TYPE = "qpid.policy_type";
    private static final String QPID_MAX_COUNT = "qpid.max_count";
    private static final String QPID_MAX_SIZE = "qpid.max_size";

    /**
     * No-local queue argument is used to support the no-local feature of Durable Subscribers.
     */
    private static final String QPID_NO_LOCAL = "no-local";

    private static final Map<String, String> ATTRIBUTE_MAPPINGS = new LinkedHashMap<>();

    private static final String ALTERNATE_EXCHANGE = "alternateExchange";
    private static final String DEFAULT_DLQ_NAME_SUFFIX = "_DLQ";
    private static final String PROPERTY_DEAD_LETTER_QUEUE_SUFFIX = "qpid.broker_dead_letter_queue_suffix";

    static
    {
        String cipherName12073 =  "DES";
		try{
			System.out.println("cipherName-12073" + javax.crypto.Cipher.getInstance(cipherName12073).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ATTRIBUTE_MAPPINGS.put(X_QPID_MINIMUM_ALERT_REPEAT_GAP, Queue.ALERT_REPEAT_GAP);
        ATTRIBUTE_MAPPINGS.put(X_QPID_MAXIMUM_MESSAGE_AGE, Queue.ALERT_THRESHOLD_MESSAGE_AGE);
        ATTRIBUTE_MAPPINGS.put(X_QPID_MAXIMUM_MESSAGE_SIZE, Queue.ALERT_THRESHOLD_MESSAGE_SIZE);

        ATTRIBUTE_MAPPINGS.put(X_QPID_MAXIMUM_MESSAGE_COUNT, Queue.ALERT_THRESHOLD_QUEUE_DEPTH_MESSAGES);
        ATTRIBUTE_MAPPINGS.put(X_QPID_MAXIMUM_QUEUE_DEPTH, Queue.ALERT_THRESHOLD_QUEUE_DEPTH_BYTES);
        ATTRIBUTE_MAPPINGS.put(QPID_ALERT_COUNT, Queue.ALERT_THRESHOLD_QUEUE_DEPTH_MESSAGES);
        ATTRIBUTE_MAPPINGS.put(QPID_ALERT_SIZE, Queue.ALERT_THRESHOLD_QUEUE_DEPTH_BYTES);
        ATTRIBUTE_MAPPINGS.put(QPID_ALERT_REPEAT_GAP, Queue.ALERT_REPEAT_GAP);

        ATTRIBUTE_MAPPINGS.put(X_QPID_MAXIMUM_DELIVERY_COUNT, Queue.MAXIMUM_DELIVERY_ATTEMPTS);

        ATTRIBUTE_MAPPINGS.put(X_QPID_CAPACITY, Queue.MAXIMUM_QUEUE_DEPTH_BYTES);

        ATTRIBUTE_MAPPINGS.put(QPID_QUEUE_SORT_KEY, SortedQueue.SORT_KEY);
        ATTRIBUTE_MAPPINGS.put(QPID_LAST_VALUE_QUEUE_KEY, LastValueQueue.LVQ_KEY);
        ATTRIBUTE_MAPPINGS.put(X_QPID_PRIORITIES, PriorityQueue.PRIORITIES);

        ATTRIBUTE_MAPPINGS.put(X_QPID_DESCRIPTION, Queue.DESCRIPTION);

        ATTRIBUTE_MAPPINGS.put(QPID_GROUP_HEADER_KEY, Queue.MESSAGE_GROUP_KEY_OVERRIDE);
        ATTRIBUTE_MAPPINGS.put(QPID_DEFAULT_MESSAGE_GROUP_ARG, Queue.MESSAGE_GROUP_DEFAULT_GROUP);

        ATTRIBUTE_MAPPINGS.put(QPID_NO_LOCAL, Queue.NO_LOCAL);
        ATTRIBUTE_MAPPINGS.put(QPID_MESSAGE_DURABILITY, Queue.MESSAGE_DURABILITY);
        ATTRIBUTE_MAPPINGS.put(QPID_DEFAULT_FILTERS, Queue.DEFAULT_FILTERS);
        ATTRIBUTE_MAPPINGS.put(QPID_ENSURE_NONDESTRUCTIVE_CONSUMERS, Queue.ENSURE_NONDESTRUCTIVE_CONSUMERS);

        ATTRIBUTE_MAPPINGS.put(QPID_EXCLUSIVITY_POLICY, Queue.EXCLUSIVE);
        ATTRIBUTE_MAPPINGS.put(QPID_LIFETIME_POLICY, Queue.LIFETIME_POLICY);

        ATTRIBUTE_MAPPINGS.put(QPID_POLICY_TYPE, Queue.OVERFLOW_POLICY);
        ATTRIBUTE_MAPPINGS.put(QPID_MAX_COUNT, Queue.MAXIMUM_QUEUE_DEPTH_MESSAGES);
        ATTRIBUTE_MAPPINGS.put(QPID_MAX_SIZE, Queue.MAXIMUM_QUEUE_DEPTH_BYTES);
    }


    public static Map<String,Object> convertWireArgsToModel(final String queueName,
                                                            final Map<String, Object> wireArguments,
                                                            final Model model,
                                                            final Queue.BehaviourOnUnknownDeclareArgument unknownArgumentBehaviour)
    {
        String cipherName12074 =  "DES";
		try{
			System.out.println("cipherName-12074" + javax.crypto.Cipher.getInstance(cipherName12074).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String,Object> modelArguments = new HashMap<>();
        if(wireArguments != null)
        {
            String cipherName12075 =  "DES";
			try{
				System.out.println("cipherName-12075" + javax.crypto.Cipher.getInstance(cipherName12075).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final ConfiguredObjectTypeRegistry typeRegistry = model.getTypeRegistry();
            final List<ConfiguredObjectAttribute<?, ?>> attributeTypes =
                    new ArrayList<>(typeRegistry.getAttributeTypes(Queue.class).values());
            typeRegistry.getTypeSpecialisations(Queue.class)
                        .forEach(type -> attributeTypes.addAll(typeRegistry.getTypeSpecificAttributes(type)));

            final Set<String> wireArgumentNames = new HashSet<>(wireArguments.keySet());
            wireArguments.entrySet()
                         .stream()
                         .filter(entry -> attributeTypes.stream()
                                                        .anyMatch(type -> Objects.equals(entry.getKey(), type.getName())
                                                                          && !type.isDerived()))
                         .forEach(entry -> {
                             String cipherName12076 =  "DES";
							try{
								System.out.println("cipherName-12076" + javax.crypto.Cipher.getInstance(cipherName12076).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							modelArguments.put(entry.getKey(), entry.getValue());
                             wireArgumentNames.remove(entry.getKey());
                         });

            for(Map.Entry<String,String> entry : ATTRIBUTE_MAPPINGS.entrySet())
            {
                String cipherName12077 =  "DES";
				try{
					System.out.println("cipherName-12077" + javax.crypto.Cipher.getInstance(cipherName12077).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(wireArguments.containsKey(entry.getKey()))
                {
                    String cipherName12078 =  "DES";
					try{
						System.out.println("cipherName-12078" + javax.crypto.Cipher.getInstance(cipherName12078).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.put(entry.getValue(), wireArguments.get(entry.getKey()));
                    wireArgumentNames.remove(entry.getKey());
                }
            }
            if(wireArguments.containsKey(QPID_LAST_VALUE_QUEUE))
            {
                String cipherName12079 =  "DES";
				try{
					System.out.println("cipherName-12079" + javax.crypto.Cipher.getInstance(cipherName12079).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wireArgumentNames.remove(QPID_LAST_VALUE_QUEUE);
                if (!wireArguments.containsKey(QPID_LAST_VALUE_QUEUE_KEY))
                {
                    String cipherName12080 =  "DES";
					try{
						System.out.println("cipherName-12080" + javax.crypto.Cipher.getInstance(cipherName12080).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.put(LastValueQueue.LVQ_KEY, LastValueQueue.DEFAULT_LVQ_KEY);
                }
            }
            if(wireArguments.containsKey(QPID_POLICY_TYPE))
            {
                String cipherName12081 =  "DES";
				try{
					System.out.println("cipherName-12081" + javax.crypto.Cipher.getInstance(cipherName12081).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				modelArguments.put(Queue.OVERFLOW_POLICY, OverflowPolicy.valueOf(String.valueOf(wireArguments.get(QPID_POLICY_TYPE)).toUpperCase()));
            }

            if(wireArguments.containsKey(QPID_SHARED_MSG_GROUP))
            {
                String cipherName12082 =  "DES";
				try{
					System.out.println("cipherName-12082" + javax.crypto.Cipher.getInstance(cipherName12082).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wireArgumentNames.remove(QPID_SHARED_MSG_GROUP);
                if (SHARED_MSG_GROUP_ARG_VALUE.equals(String.valueOf(wireArguments.get(QPID_SHARED_MSG_GROUP))))
                {
                    String cipherName12083 =  "DES";
					try{
						System.out.println("cipherName-12083" + javax.crypto.Cipher.getInstance(cipherName12083).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.put(Queue.MESSAGE_GROUP_TYPE, MessageGroupType.SHARED_GROUPS);
                }
            }
            else if(wireArguments.containsKey(QPID_GROUP_HEADER_KEY))
            {
                String cipherName12084 =  "DES";
				try{
					System.out.println("cipherName-12084" + javax.crypto.Cipher.getInstance(cipherName12084).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				modelArguments.put(Queue.MESSAGE_GROUP_TYPE, MessageGroupType.STANDARD);
                if ("JMSXGroupId".equals(wireArguments.get(QPID_GROUP_HEADER_KEY)))
                {
                    String cipherName12085 =  "DES";
					try{
						System.out.println("cipherName-12085" + javax.crypto.Cipher.getInstance(cipherName12085).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.remove(Queue.MESSAGE_GROUP_KEY_OVERRIDE);
                }
            }


            if(wireArguments.get(QPID_NO_LOCAL) != null)
            {
                String cipherName12086 =  "DES";
				try{
					System.out.println("cipherName-12086" + javax.crypto.Cipher.getInstance(cipherName12086).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				modelArguments.put(Queue.NO_LOCAL, Boolean.parseBoolean(wireArguments.get(QPID_NO_LOCAL).toString()));
            }

            if (wireArguments.containsKey(X_QPID_FLOW_RESUME_CAPACITY))
            {
                String cipherName12087 =  "DES";
				try{
					System.out.println("cipherName-12087" + javax.crypto.Cipher.getInstance(cipherName12087).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wireArgumentNames.remove(X_QPID_FLOW_RESUME_CAPACITY);
                if (wireArguments.get(X_QPID_FLOW_RESUME_CAPACITY) != null && wireArguments.get(X_QPID_CAPACITY) != null)
                {
                    String cipherName12088 =  "DES";
					try{
						System.out.println("cipherName-12088" + javax.crypto.Cipher.getInstance(cipherName12088).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					double resumeCapacity = Integer.parseInt(wireArguments.get(X_QPID_FLOW_RESUME_CAPACITY).toString());
                    double maximumCapacity = Integer.parseInt(wireArguments.get(X_QPID_CAPACITY).toString());
                    if (resumeCapacity > maximumCapacity)
                    {
                        String cipherName12089 =  "DES";
						try{
							System.out.println("cipherName-12089" + javax.crypto.Cipher.getInstance(cipherName12089).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new ConnectionScopedRuntimeException(
                                "Flow resume size can't be greater than flow control size");
                    }
                    Map<String, String> context = (Map<String, String>) modelArguments.get(Queue.CONTEXT);
                    if (context == null)
                    {
                        String cipherName12090 =  "DES";
						try{
							System.out.println("cipherName-12090" + javax.crypto.Cipher.getInstance(cipherName12090).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						context = new HashMap<>();
                        modelArguments.put(Queue.CONTEXT, context);
                    }
                    double ratio = resumeCapacity / maximumCapacity;
                    context.put(Queue.QUEUE_FLOW_RESUME_LIMIT, String.format("%.2f", ratio * 100.0));
                    modelArguments.put(Queue.OVERFLOW_POLICY, OverflowPolicy.PRODUCER_FLOW_CONTROL);
                }
            }

            if (wireArguments.containsKey(ALTERNATE_EXCHANGE))
            {
                String cipherName12091 =  "DES";
				try{
					System.out.println("cipherName-12091" + javax.crypto.Cipher.getInstance(cipherName12091).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wireArgumentNames.remove(ALTERNATE_EXCHANGE);
                modelArguments.put(Queue.ALTERNATE_BINDING,
                                   Collections.singletonMap(AlternateBinding.DESTINATION,
                                                            wireArguments.get(ALTERNATE_EXCHANGE)));
            }
            else if (wireArguments.containsKey(X_QPID_DLQ_ENABLED))
            {
                String cipherName12092 =  "DES";
				try{
					System.out.println("cipherName-12092" + javax.crypto.Cipher.getInstance(cipherName12092).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wireArgumentNames.remove(X_QPID_DLQ_ENABLED);
                Object argument = wireArguments.get(X_QPID_DLQ_ENABLED);
                if ((argument instanceof Boolean && ((Boolean) argument).booleanValue())
                    || (argument instanceof String && Boolean.parseBoolean((String)argument)))
                {
                    String cipherName12093 =  "DES";
					try{
						System.out.println("cipherName-12093" + javax.crypto.Cipher.getInstance(cipherName12093).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.put(Queue.ALTERNATE_BINDING,
                                       Collections.singletonMap(AlternateBinding.DESTINATION,
                                                                getDeadLetterQueueName(queueName)));
                }
            }

            if(wireArguments.containsKey(X_SINGLE_ACTIVE_CONSUMER))
            {
                String cipherName12094 =  "DES";
				try{
					System.out.println("cipherName-12094" + javax.crypto.Cipher.getInstance(cipherName12094).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wireArgumentNames.remove(X_SINGLE_ACTIVE_CONSUMER);
                Object argument = wireArguments.get(X_SINGLE_ACTIVE_CONSUMER);
                if ((argument instanceof Boolean && ((Boolean) argument).booleanValue())
                    || (argument instanceof String && Boolean.parseBoolean((String)argument)))
                {
                    String cipherName12095 =  "DES";
					try{
						System.out.println("cipherName-12095" + javax.crypto.Cipher.getInstance(cipherName12095).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					modelArguments.putIfAbsent(Queue.MAXIMUM_LIVE_CONSUMERS, 1);
                }
            }

            if (!wireArgumentNames.isEmpty())
            {

                String cipherName12096 =  "DES";
				try{
					System.out.println("cipherName-12096" + javax.crypto.Cipher.getInstance(cipherName12096).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				switch(unknownArgumentBehaviour)
                {
                    case LOG:
                        LOGGER.warn("Unsupported queue declare argument(s) : {}", String.join(",", wireArgumentNames));
                        break;
                    case IGNORE:
                        break;
                    case FAIL:
                    default:
                        throw new IllegalArgumentException(String.format("Unsupported queue declare argument(s) : %s",
                                                                         String.join(",", wireArgumentNames)));
                }
            }
        }
        return modelArguments;
    }


    public static Map<String,Object> convertModelArgsToWire(Map<String,Object> modelArguments)
    {
        String cipherName12097 =  "DES";
		try{
			System.out.println("cipherName-12097" + javax.crypto.Cipher.getInstance(cipherName12097).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String,Object> wireArguments = new HashMap<>();
        for(Map.Entry<String,String> entry : ATTRIBUTE_MAPPINGS.entrySet())
        {
            String cipherName12098 =  "DES";
			try{
				System.out.println("cipherName-12098" + javax.crypto.Cipher.getInstance(cipherName12098).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(modelArguments.containsKey(entry.getValue()))
            {
                String cipherName12099 =  "DES";
				try{
					System.out.println("cipherName-12099" + javax.crypto.Cipher.getInstance(cipherName12099).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Object value = modelArguments.get(entry.getValue());
                if(value instanceof Enum)
                {
                    String cipherName12100 =  "DES";
					try{
						System.out.println("cipherName-12100" + javax.crypto.Cipher.getInstance(cipherName12100).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					value = ((Enum) value).name();
                }
                else if(value instanceof ConfiguredObject)
                {
                    String cipherName12101 =  "DES";
					try{
						System.out.println("cipherName-12101" + javax.crypto.Cipher.getInstance(cipherName12101).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					value = ((ConfiguredObject)value).getName();
                }
                wireArguments.put(entry.getKey(), value);
            }
        }

        if(MessageGroupType.SHARED_GROUPS.equals(modelArguments.get(Queue.MESSAGE_GROUP_TYPE)))
        {
            String cipherName12102 =  "DES";
			try{
				System.out.println("cipherName-12102" + javax.crypto.Cipher.getInstance(cipherName12102).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			wireArguments.put(QPID_SHARED_MSG_GROUP, SHARED_MSG_GROUP_ARG_VALUE);
        }

        return wireArguments;
    }

    private static String getDeadLetterQueueName(String name)
    {
        String cipherName12103 =  "DES";
		try{
			System.out.println("cipherName-12103" + javax.crypto.Cipher.getInstance(cipherName12103).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return name + System.getProperty(PROPERTY_DEAD_LETTER_QUEUE_SUFFIX, DEFAULT_DLQ_NAME_SUFFIX);
    }
}
