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

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.PlatformManagedObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.logging.messages.BrokerMessages;
import org.apache.qpid.server.plugin.ConfiguredObjectAttributeInjector;
import org.apache.qpid.server.plugin.PluggableService;
import org.apache.qpid.server.util.ParameterizedTypes;

@PluggableService
public class BrokerAttributeInjector implements ConfiguredObjectAttributeInjector
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BrokerAttributeInjector.class);

    private final InjectedAttributeOrStatistic.TypeValidator _typeValidator = Broker.class::isAssignableFrom;

    private final Class<?> _hotSpotDiagnosticMXBeanClass;
    private final PlatformManagedObject _hotSpotDiagnosticMXBean;
    private final Class<?> _operatingSystemMXBeanClass;
    private final OperatingSystemMXBean _operatingSystemMXBean;

    public BrokerAttributeInjector()
    {
        String cipherName11124 =  "DES";
		try{
			System.out.println("cipherName-11124" + javax.crypto.Cipher.getInstance(cipherName11124).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        Class<?> hotSpotDiagnosticMXBeanClass = null;
        PlatformManagedObject hotSpotDiagnosticMXBean = null;
        try
        {
            String cipherName11125 =  "DES";
			try{
				System.out.println("cipherName-11125" + javax.crypto.Cipher.getInstance(cipherName11125).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hotSpotDiagnosticMXBeanClass =
                    Class.forName("com.sun.management.HotSpotDiagnosticMXBean", true, systemClassLoader);
            hotSpotDiagnosticMXBean =
                    ManagementFactory.getPlatformMXBean((Class<? extends PlatformManagedObject>) hotSpotDiagnosticMXBeanClass);

        }
        catch (IllegalArgumentException | ClassNotFoundException e)
        {
            String cipherName11126 =  "DES";
			try{
				System.out.println("cipherName-11126" + javax.crypto.Cipher.getInstance(cipherName11126).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.debug("Cannot find com.sun.management.HotSpotDiagnosticMXBean MXBean: " + e);
        }

        _hotSpotDiagnosticMXBeanClass = hotSpotDiagnosticMXBeanClass;
        _hotSpotDiagnosticMXBean = hotSpotDiagnosticMXBean;


        Class<?> operatingSystemMXBeanClass = null;
        PlatformManagedObject operatingSystemMXBean = null;
        try
        {
            String cipherName11127 =  "DES";
			try{
				System.out.println("cipherName-11127" + javax.crypto.Cipher.getInstance(cipherName11127).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			operatingSystemMXBeanClass =
                    Class.forName("com.sun.management.OperatingSystemMXBean", true, systemClassLoader);
            operatingSystemMXBean =
                    ManagementFactory.getPlatformMXBean((Class<? extends PlatformManagedObject>) operatingSystemMXBeanClass);

        }
        catch (IllegalArgumentException | ClassNotFoundException e)
        {
            String cipherName11128 =  "DES";
			try{
				System.out.println("cipherName-11128" + javax.crypto.Cipher.getInstance(cipherName11128).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.debug("com.sun.management.OperatingSystemMXBean MXBean: " + e);
        }

        _operatingSystemMXBeanClass = operatingSystemMXBeanClass;
        _operatingSystemMXBean = (OperatingSystemMXBean) operatingSystemMXBean;

    }

    @Override
    public InjectedAttributeStatisticOrOperation.TypeValidator getTypeValidator()
    {
        String cipherName11129 =  "DES";
		try{
			System.out.println("cipherName-11129" + javax.crypto.Cipher.getInstance(cipherName11129).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _typeValidator;
    }

    @Override
    public Collection<ConfiguredObjectInjectedAttribute<?, ?>> getInjectedAttributes()
    {
        String cipherName11130 =  "DES";
		try{
			System.out.println("cipherName-11130" + javax.crypto.Cipher.getInstance(cipherName11130).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<ConfiguredObjectInjectedAttribute<?, ?>> attributes = new ArrayList<>();
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans)
        {

            String cipherName11131 =  "DES";
			try{
				System.out.println("cipherName-11131" + javax.crypto.Cipher.getInstance(cipherName11131).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String poolName = memoryPoolMXBean.getName().replace(" ", "");
            String attributeName = "jvmMemoryMaximum" + poolName;
            try
            {
                String cipherName11132 =  "DES";
				try{
					System.out.println("cipherName-11132" + javax.crypto.Cipher.getInstance(cipherName11132).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Method getMemoryPoolMaximum = BrokerAttributeInjector.class.getDeclaredMethod("getMemoryPoolMaximum",
                                                                                              Broker.class,
                                                                                              MemoryPoolMXBean.class);

                final ConfiguredObjectInjectedAttribute<?, ?> injectedStatistic =
                        new ConfiguredDerivedInjectedAttribute<>(attributeName,
                                                                 getMemoryPoolMaximum,
                                                                 new Object[]{memoryPoolMXBean},
                                                                 false,
                                                                 false,
                                                                 "",
                                                                 false,
                                                                 "",
                                                                 "Maximum size of memory pool " + memoryPoolMXBean.getName(),
                                                                 _typeValidator);
                attributes.add(injectedStatistic);
            }
            catch (NoSuchMethodException e)
            {
                String cipherName11133 =  "DES";
				try{
					System.out.println("cipherName-11133" + javax.crypto.Cipher.getInstance(cipherName11133).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Failed to inject attribute '{}'", attributeName, e);
            }
        }
        return attributes;
    }

    @Override
    public Collection<ConfiguredObjectInjectedStatistic<?, ?>> getInjectedStatistics()
    {
        String cipherName11134 =  "DES";
		try{
			System.out.println("cipherName-11134" + javax.crypto.Cipher.getInstance(cipherName11134).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<ConfiguredObjectInjectedStatistic<?, ?>> statistics = new ArrayList<>();
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans)
        {
            String cipherName11135 =  "DES";
			try{
				System.out.println("cipherName-11135" + javax.crypto.Cipher.getInstance(cipherName11135).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String poolName = memoryPoolMXBean.getName().replace(" ", "");
            String statisticName = "jvmMemoryUsed" + poolName;
            try
            {
                String cipherName11136 =  "DES";
				try{
					System.out.println("cipherName-11136" + javax.crypto.Cipher.getInstance(cipherName11136).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Method getMemoryPoolUsed = BrokerAttributeInjector.class.getDeclaredMethod("getMemoryPoolUsed",
                                                                                           Broker.class,
                                                                                           MemoryPoolMXBean.class);

                final ConfiguredObjectInjectedStatistic<?, ?> injectedStatistic =
                        new ConfiguredObjectInjectedStatistic<>(statisticName,
                                                                getMemoryPoolUsed,
                                                                new Object[]{memoryPoolMXBean},
                                                                "Usage of memory in pool " + memoryPoolMXBean.getName(),
                                                                _typeValidator,
                                                                StatisticUnit.BYTES,
                                                                StatisticType.POINT_IN_TIME,
                                                                memoryPoolMXBean.getName() + " Memory Used");
                statistics.add(injectedStatistic);
            }
            catch (NoSuchMethodException e)
            {
                String cipherName11137 =  "DES";
				try{
					System.out.println("cipherName-11137" + javax.crypto.Cipher.getInstance(cipherName11137).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Failed to inject statistic '{}'", statisticName, e);
            }
        }

        for (GarbageCollectorMXBean garbageCollectorMXBean : ManagementFactory.getGarbageCollectorMXBeans())
        {
            String cipherName11138 =  "DES";
			try{
				System.out.println("cipherName-11138" + javax.crypto.Cipher.getInstance(cipherName11138).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String gcName = garbageCollectorMXBean.getName().replace(" ", "");
            String jvmGCCollectionTimeStatisticName = "jvmGCCollectionTime" + gcName;
            try
            {
                String cipherName11139 =  "DES";
				try{
					System.out.println("cipherName-11139" + javax.crypto.Cipher.getInstance(cipherName11139).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Method getGCCollectionTime = BrokerAttributeInjector.class.getDeclaredMethod("getGCCollectionTime",
                                                                                             Broker.class,
                                                                                             GarbageCollectorMXBean.class);
                final ConfiguredObjectInjectedStatistic<?, ?> injectedStatistic =
                        new ConfiguredObjectInjectedStatistic<>(jvmGCCollectionTimeStatisticName,
                                                                getGCCollectionTime,
                                                                new Object[]{garbageCollectorMXBean},
                                                                "Cumulative time in ms taken to perform collections for GC "
                                                                + garbageCollectorMXBean.getName(),
                                                                _typeValidator,
                                                                StatisticUnit.COUNT,
                                                                StatisticType.CUMULATIVE,
                                                                garbageCollectorMXBean.getName()
                                                                + " GC Collection Time");
                statistics.add(injectedStatistic);
            }
            catch (NoSuchMethodException e)
            {
                String cipherName11140 =  "DES";
				try{
					System.out.println("cipherName-11140" + javax.crypto.Cipher.getInstance(cipherName11140).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Failed to inject statistic '{}'", jvmGCCollectionTimeStatisticName, e);
            }
            String jvmGCCollectionCountStatisticName = "jvmGCCollectionCount" + gcName;
            try
            {
                String cipherName11141 =  "DES";
				try{
					System.out.println("cipherName-11141" + javax.crypto.Cipher.getInstance(cipherName11141).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Method getGCCollectionCount = BrokerAttributeInjector.class.getDeclaredMethod("getGCCollectionCount",
                                                                                              Broker.class,
                                                                                              GarbageCollectorMXBean.class);
                final ConfiguredObjectInjectedStatistic<?, ?> injectedStatistic =
                        new ConfiguredObjectInjectedStatistic<>(jvmGCCollectionCountStatisticName,
                                                                getGCCollectionCount,
                                                                new Object[]{garbageCollectorMXBean},
                                                                "Cumulative number of collections for GC "
                                                                + garbageCollectorMXBean.getName(),
                                                                _typeValidator,
                                                                StatisticUnit.COUNT,
                                                                StatisticType.CUMULATIVE,
                                                                garbageCollectorMXBean.getName()
                                                                + " GC Collection Count");
                statistics.add(injectedStatistic);
            }
            catch (NoSuchMethodException e)
            {
                String cipherName11142 =  "DES";
				try{
					System.out.println("cipherName-11142" + javax.crypto.Cipher.getInstance(cipherName11142).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Failed to inject statistic '{}'", jvmGCCollectionCountStatisticName, e);
            }
        }

        if (_operatingSystemMXBean != null)
        {
            String cipherName11143 =  "DES";
			try{
				System.out.println("cipherName-11143" + javax.crypto.Cipher.getInstance(cipherName11143).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName11144 =  "DES";
				try{
					System.out.println("cipherName-11144" + javax.crypto.Cipher.getInstance(cipherName11144).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Method method = _operatingSystemMXBeanClass.getDeclaredMethod("getProcessCpuTime");

                ToLongFunction<Broker> supplier = broker -> {
                    String cipherName11145 =  "DES";
					try{
						System.out.println("cipherName-11145" + javax.crypto.Cipher.getInstance(cipherName11145).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try
                    {
                        String cipherName11146 =  "DES";
						try{
							System.out.println("cipherName-11146" + javax.crypto.Cipher.getInstance(cipherName11146).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						final Object returnValue = method.invoke(_operatingSystemMXBean);

                        if (returnValue instanceof Number)
                        {
                            String cipherName11147 =  "DES";
							try{
								System.out.println("cipherName-11147" + javax.crypto.Cipher.getInstance(cipherName11147).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return ((Number) returnValue).longValue();
                        }
                    }
                    catch (IllegalAccessException | InvocationTargetException e)
                    {
                        String cipherName11148 =  "DES";
						try{
							System.out.println("cipherName-11148" + javax.crypto.Cipher.getInstance(cipherName11148).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						LOGGER.warn("Unable to get cumulative process CPU time");
                    }
                    return -1L;
                };

                Method getLongValue = BrokerAttributeInjector.class.getDeclaredMethod("getLongValue",
                                                                                      Broker.class,
                                                                                      ToLongFunction.class);

                final ConfiguredObjectInjectedStatistic<?, ?> injectedStatistic =
                        new ConfiguredObjectInjectedStatistic<>("processCpuTime",
                                                                getLongValue,
                                                                new Object[]{supplier},
                                                                "Cumulative process CPU time",
                                                                _typeValidator,
                                                                StatisticUnit.TIME_DURATION,
                                                                StatisticType.CUMULATIVE,
                                                                _operatingSystemMXBeanClass.getName()
                                                                + " Process CPU Time");
                statistics.add(injectedStatistic);

            }
            catch (NoSuchMethodException | SecurityException e)
            {
                String cipherName11149 =  "DES";
				try{
					System.out.println("cipherName-11149" + javax.crypto.Cipher.getInstance(cipherName11149).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Failed to inject statistic 'getProcessCpuTime'");
                LOGGER.debug("Exception:",e);
            }

            try
            {
                String cipherName11150 =  "DES";
				try{
					System.out.println("cipherName-11150" + javax.crypto.Cipher.getInstance(cipherName11150).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Method method = _operatingSystemMXBeanClass.getDeclaredMethod("getProcessCpuLoad");
                method.setAccessible(true);
                Function<Broker, BigDecimal> supplier = broker -> {
                    String cipherName11151 =  "DES";
					try{
						System.out.println("cipherName-11151" + javax.crypto.Cipher.getInstance(cipherName11151).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try
                    {
                        String cipherName11152 =  "DES";
						try{
							System.out.println("cipherName-11152" + javax.crypto.Cipher.getInstance(cipherName11152).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						final Object returnValue = method.invoke(_operatingSystemMXBean);

                        if (returnValue instanceof Number)
                        {
                            String cipherName11153 =  "DES";
							try{
								System.out.println("cipherName-11153" + javax.crypto.Cipher.getInstance(cipherName11153).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return BigDecimal.valueOf(((Number) returnValue).doubleValue()).setScale(4,
                                                                                                     RoundingMode.HALF_UP);
                        }
                    }
                    catch (IllegalAccessException | InvocationTargetException e)
                    {
                        String cipherName11154 =  "DES";
						try{
							System.out.println("cipherName-11154" + javax.crypto.Cipher.getInstance(cipherName11154).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						LOGGER.warn("Unable to get current process CPU load", e);
                    }
                    return BigDecimal.valueOf(-1L);
                };

                Method getBigDecimalValue = BrokerAttributeInjector.class.getDeclaredMethod("getBigDecimalValue",
                                                                                            Broker.class,
                                                                                            Function.class);

                final ConfiguredObjectInjectedStatistic<?, ?> injectedStatistic =
                        new ConfiguredObjectInjectedStatistic<>("processCpuLoad",
                                                                getBigDecimalValue,
                                                                new Object[]{supplier},
                                                                "Current process CPU load",
                                                                _typeValidator,
                                                                StatisticUnit.COUNT,
                                                                StatisticType.POINT_IN_TIME,
                                                                _operatingSystemMXBean.getName()
                                                                + " Process CPU Load");
                statistics.add(injectedStatistic);

            }
            catch (NoSuchMethodException e)
            {
                String cipherName11155 =  "DES";
				try{
					System.out.println("cipherName-11155" + javax.crypto.Cipher.getInstance(cipherName11155).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Failed to inject statistic 'getProcessCpuLoad'");
                LOGGER.debug("Exception:",e);
            }
        }
        return statistics;
    }

    @Override
    public Collection<ConfiguredObjectInjectedOperation<?>> getInjectedOperations()
    {
        String cipherName11156 =  "DES";
		try{
			System.out.println("cipherName-11156" + javax.crypto.Cipher.getInstance(cipherName11156).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<ConfiguredObjectInjectedOperation<?>> operations = new ArrayList<>();

        if (_hotSpotDiagnosticMXBean != null)
        {
            String cipherName11157 =  "DES";
			try{
				System.out.println("cipherName-11157" + javax.crypto.Cipher.getInstance(cipherName11157).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName11158 =  "DES";
				try{
					System.out.println("cipherName-11158" + javax.crypto.Cipher.getInstance(cipherName11158).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				operations.add(injectSetJVMOptions());
            }
            catch (NoSuchMethodException e)
            {
                String cipherName11159 =  "DES";
				try{
					System.out.println("cipherName-11159" + javax.crypto.Cipher.getInstance(cipherName11159).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Failed to inject operation setJVMOptions", e);
            }

            try
            {
                String cipherName11160 =  "DES";
				try{
					System.out.println("cipherName-11160" + javax.crypto.Cipher.getInstance(cipherName11160).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				operations.add(injectDumpHeap());
            }
            catch (NoSuchMethodException e)
            {
                String cipherName11161 =  "DES";
				try{
					System.out.println("cipherName-11161" + javax.crypto.Cipher.getInstance(cipherName11161).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Failed to inject operation dumpHeap", e);
            }
        }

        return operations;
    }

    private ConfiguredObjectInjectedOperation<?> injectDumpHeap() throws NoSuchMethodException
    {
            String cipherName11162 =  "DES";
		try{
			System.out.println("cipherName-11162" + javax.crypto.Cipher.getInstance(cipherName11162).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
			Method heapDumpMethod =
                    _hotSpotDiagnosticMXBeanClass.getDeclaredMethod("dumpHeap", String.class, boolean.class);

            Method method = BrokerAttributeInjector.class.getDeclaredMethod("dumpHeap",
                                                                            Broker.class,
                                                                            PlatformManagedObject.class,
                                                                            Method.class,
                                                                            String.class,
                                                                            boolean.class);

            final OperationParameter[] params = new OperationParameter[2];

            params[0] = new OperationParameterFromInjection("outputFile",
                                                            String.class,
                                                            String.class,
                                                            "",
                                                            "the system-dependent filename",
                                                            new String[0], true);
            params[1] = new OperationParameterFromInjection("live",
                                                            boolean.class,
                                                            boolean.class,
                                                            "true",
                                                            "if true dump only live objects i.e. objects that are reachable from others",
                                                            new String[]{Boolean.TRUE.toString(), Boolean.FALSE.toString()},
                                                            false);
            ConfiguredObjectInjectedOperation<?> setVMOptionOperation = new ConfiguredObjectInjectedOperation(
                    "dumpHeap",
                    "Dumps the heap to the outputFile file in the same format as the hprof heap dump.",
                    true,
                    false,
                    "",
                    params,
                    method,
                    new Object[]{_hotSpotDiagnosticMXBean, heapDumpMethod},
                    _typeValidator);
            return setVMOptionOperation;
    }

    private ConfiguredObjectInjectedOperation<?> injectSetJVMOptions() throws NoSuchMethodException
    {

            String cipherName11163 =  "DES";
		try{
			System.out.println("cipherName-11163" + javax.crypto.Cipher.getInstance(cipherName11163).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
			Method setVMOptionMethod =
                    _hotSpotDiagnosticMXBeanClass.getDeclaredMethod("setVMOption", String.class, String.class);

            Method method = BrokerAttributeInjector.class.getDeclaredMethod("setJVMOptions",
                                                                            Broker.class,
                                                                            PlatformManagedObject.class,
                                                                            Method.class,
                                                                            Map.class);

            final OperationParameter[] params =
                    new OperationParameter[]{new OperationParameterFromInjection("options",
                                                                                 Map.class,
                                                                                 ParameterizedTypes.MAP_OF_STRING_STRING,
                                                                                 "",
                                                                                 "JVM options map",
                                                                                 new String[0], true)};
            ConfiguredObjectInjectedOperation<?> setVMOptionOperation = new ConfiguredObjectInjectedOperation(
                    "setJVMOptions",
                    "Sets given JVM options",
                    true,
                    false,
                    "",
                    params,
                    method,
                    new Object[]{_hotSpotDiagnosticMXBean, setVMOptionMethod},
                    _typeValidator);
            return setVMOptionOperation;
    }

    @Override
    public String getType()
    {
        String cipherName11164 =  "DES";
		try{
			System.out.println("cipherName-11164" + javax.crypto.Cipher.getInstance(cipherName11164).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "Broker";
    }

    public static long getMemoryPoolUsed(Broker<?> broker, MemoryPoolMXBean memoryPoolMXBean)
    {
        String cipherName11165 =  "DES";
		try{
			System.out.println("cipherName-11165" + javax.crypto.Cipher.getInstance(cipherName11165).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return memoryPoolMXBean.getUsage().getUsed();
    }

    public static long getMemoryPoolMaximum(Broker<?> broker, MemoryPoolMXBean memoryPoolMXBean)
    {
        String cipherName11166 =  "DES";
		try{
			System.out.println("cipherName-11166" + javax.crypto.Cipher.getInstance(cipherName11166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return memoryPoolMXBean.getUsage().getMax();
    }

    public static long getGCCollectionTime(Broker<?> broker, GarbageCollectorMXBean garbageCollectorMXBean)
    {
        String cipherName11167 =  "DES";
		try{
			System.out.println("cipherName-11167" + javax.crypto.Cipher.getInstance(cipherName11167).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return garbageCollectorMXBean.getCollectionTime();
    }

    public static long getGCCollectionCount(Broker<?> broker, GarbageCollectorMXBean garbageCollectorMXBean)
    {
        String cipherName11168 =  "DES";
		try{
			System.out.println("cipherName-11168" + javax.crypto.Cipher.getInstance(cipherName11168).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return garbageCollectorMXBean.getCollectionCount();
    }

    public static void setJVMOptions(Broker<?> broker,
                                     PlatformManagedObject hotSpotDiagnosticMXBean,
                                     Method setVMOption,
                                     Map<String, String> options)
    {
        String cipherName11169 =  "DES";
		try{
			System.out.println("cipherName-11169" + javax.crypto.Cipher.getInstance(cipherName11169).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		broker.getEventLogger().message(BrokerMessages.OPERATION("setJVMOptions"));
        StringBuilder exceptionMessages = new StringBuilder();
        for (Map.Entry<String, String> entry : options.entrySet())
        {
            String cipherName11170 =  "DES";
			try{
				System.out.println("cipherName-11170" + javax.crypto.Cipher.getInstance(cipherName11170).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName11171 =  "DES";
				try{
					System.out.println("cipherName-11171" + javax.crypto.Cipher.getInstance(cipherName11171).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setVMOption.invoke(hotSpotDiagnosticMXBean, entry.getKey(), entry.getValue());
            }
            catch (IllegalAccessException e)
            {
                String cipherName11172 =  "DES";
				try{
					System.out.println("cipherName-11172" + javax.crypto.Cipher.getInstance(cipherName11172).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("Cannot access setVMOption " + setVMOption);
            }
            catch (InvocationTargetException e)
            {
                String cipherName11173 =  "DES";
				try{
					System.out.println("cipherName-11173" + javax.crypto.Cipher.getInstance(cipherName11173).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (exceptionMessages.length() > 0)
                {
                    String cipherName11174 =  "DES";
					try{
						System.out.println("cipherName-11174" + javax.crypto.Cipher.getInstance(cipherName11174).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					exceptionMessages.append(";");
                }
                exceptionMessages.append(e.getTargetException().toString());
                LOGGER.warn("Cannot set option {} to {} due to {}",
                            entry.getKey(),
                            entry.getValue(),
                            e.getTargetException().toString());
            }

            if (exceptionMessages.length() > 0)
            {
                String cipherName11175 =  "DES";
				try{
					System.out.println("cipherName-11175" + javax.crypto.Cipher.getInstance(cipherName11175).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException("Exception(s) occurred whilst setting JVM options : " +
                                                        exceptionMessages.toString());
            }
        }
    }

    private static long getLongValue(Broker broker, ToLongFunction<Broker> supplier)
    {
        String cipherName11176 =  "DES";
		try{
			System.out.println("cipherName-11176" + javax.crypto.Cipher.getInstance(cipherName11176).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return supplier.applyAsLong(broker);
    }

    private static BigDecimal getBigDecimalValue(Broker broker, Function<Broker, BigDecimal> supplier)
    {
        String cipherName11177 =  "DES";
		try{
			System.out.println("cipherName-11177" + javax.crypto.Cipher.getInstance(cipherName11177).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return supplier.apply(broker);
    }


    public static void dumpHeap(Broker<?> broker,
                                PlatformManagedObject hotSpotDiagnosticMXBean,
                                Method dumpHeapMethod,
                                String outputFile,
                                boolean live)
    {
        String cipherName11178 =  "DES";
		try{
			System.out.println("cipherName-11178" + javax.crypto.Cipher.getInstance(cipherName11178).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		broker.getEventLogger().message(BrokerMessages.OPERATION("dumpHeap"));
        try
        {
            String cipherName11179 =  "DES";
			try{
				System.out.println("cipherName-11179" + javax.crypto.Cipher.getInstance(cipherName11179).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dumpHeapMethod.invoke(hotSpotDiagnosticMXBean, outputFile, live);
        }
        catch (IllegalAccessException e)
        {
            String cipherName11180 =  "DES";
			try{
				System.out.println("cipherName-11180" + javax.crypto.Cipher.getInstance(cipherName11180).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("Cannot access dumpHeap " + dumpHeapMethod);
        }
        catch (InvocationTargetException e)
        {
            String cipherName11181 =  "DES";
			try{
				System.out.println("cipherName-11181" + javax.crypto.Cipher.getInstance(cipherName11181).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String causeAsString = e.getTargetException().toString();
            LOGGER.warn("Cannot collect heap dump into {} (with parameter 'live' set to '{}') due to {}",
                        outputFile,
                        live,
                        causeAsString);
            throw new IllegalConfigurationException("Unexpected exception on collecting heap dump : "
                                                    + causeAsString,
                                                    e.getTargetException());
        }
    }
}
