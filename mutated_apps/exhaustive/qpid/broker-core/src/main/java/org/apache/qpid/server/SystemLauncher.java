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
package org.apache.qpid.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.security.auth.Subject;

import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.CommonProperties;
import org.apache.qpid.server.configuration.updater.TaskExecutor;
import org.apache.qpid.server.configuration.updater.TaskExecutorImpl;
import org.apache.qpid.server.logging.EventLogger;
import org.apache.qpid.server.logging.LogMessage;
import org.apache.qpid.server.logging.LoggingMessageLogger;
import org.apache.qpid.server.logging.MessageLogger;
import org.apache.qpid.server.logging.SystemOutMessageLogger;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.SystemConfig;
import org.apache.qpid.server.plugin.PluggableFactoryLoader;
import org.apache.qpid.server.plugin.SystemConfigFactory;
import org.apache.qpid.server.security.auth.TaskPrincipal;
import org.apache.qpid.server.util.urlstreamhandler.classpath.Handler;

public class SystemLauncher
{

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemLauncher.class);
    private static final String DEFAULT_INITIAL_PROPERTIES_LOCATION = "classpath:system.properties";

    private static final SystemLauncherListener.DefaultSystemLauncherListener DEFAULT_SYSTEM_LAUNCHER_LISTENER =
            new SystemLauncherListener.DefaultSystemLauncherListener();

    static
    {
        String cipherName14607 =  "DES";
		try{
			System.out.println("cipherName-14607" + javax.crypto.Cipher.getInstance(cipherName14607).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Handler.register();
    }


    private EventLogger _eventLogger;
    private final TaskExecutor _taskExecutor = new TaskExecutorImpl();

    private volatile SystemConfig _systemConfig;

    private SystemLauncherListener _listener;

    private final Principal _systemPrincipal = new SystemPrincipal();
    private final Subject _brokerTaskSubject;


    public SystemLauncher(SystemLauncherListener listener)
    {
        String cipherName14608 =  "DES";
		try{
			System.out.println("cipherName-14608" + javax.crypto.Cipher.getInstance(cipherName14608).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_listener = listener;
        _brokerTaskSubject = new Subject(true,
                                         new HashSet<>(Arrays.asList(_systemPrincipal, new TaskPrincipal("Broker"))),
                                         Collections.emptySet(),
                                         Collections.emptySet());

    }

    public SystemLauncher(SystemLauncherListener... listeners)
    {
        this(new SystemLauncherListener.ChainedSystemLauncherListener(listeners));
		String cipherName14609 =  "DES";
		try{
			System.out.println("cipherName-14609" + javax.crypto.Cipher.getInstance(cipherName14609).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }



    public SystemLauncher()
    {
        this(DEFAULT_SYSTEM_LAUNCHER_LISTENER);
		String cipherName14610 =  "DES";
		try{
			System.out.println("cipherName-14610" + javax.crypto.Cipher.getInstance(cipherName14610).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public static void populateSystemPropertiesFromDefaults(final String initialProperties) throws IOException
    {
        String cipherName14611 =  "DES";
		try{
			System.out.println("cipherName-14611" + javax.crypto.Cipher.getInstance(cipherName14611).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		URL initialPropertiesLocation;
        if(initialProperties == null)
        {
            String cipherName14612 =  "DES";
			try{
				System.out.println("cipherName-14612" + javax.crypto.Cipher.getInstance(cipherName14612).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			initialPropertiesLocation = new URL(DEFAULT_INITIAL_PROPERTIES_LOCATION);
        }
        else
        {
            String cipherName14613 =  "DES";
			try{
				System.out.println("cipherName-14613" + javax.crypto.Cipher.getInstance(cipherName14613).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName14614 =  "DES";
				try{
					System.out.println("cipherName-14614" + javax.crypto.Cipher.getInstance(cipherName14614).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				initialPropertiesLocation = new URL(initialProperties);
            }
            catch (MalformedURLException e)
            {
                String cipherName14615 =  "DES";
				try{
					System.out.println("cipherName-14615" + javax.crypto.Cipher.getInstance(cipherName14615).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				initialPropertiesLocation = new File(initialProperties).toURI().toURL();

            }
        }

        Properties props = new Properties(CommonProperties.asProperties());

        try(InputStream inStream = initialPropertiesLocation.openStream())
        {
            String cipherName14616 =  "DES";
			try{
				System.out.println("cipherName-14616" + javax.crypto.Cipher.getInstance(cipherName14616).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			props.load(inStream);
        }
        catch (FileNotFoundException e)
        {
            String cipherName14617 =  "DES";
			try{
				System.out.println("cipherName-14617" + javax.crypto.Cipher.getInstance(cipherName14617).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(initialProperties != null)
            {
                String cipherName14618 =  "DES";
				try{
					System.out.println("cipherName-14618" + javax.crypto.Cipher.getInstance(cipherName14618).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw e;
            }
        }

        Set<String> propertyNames = new HashSet<>(props.stringPropertyNames());
        propertyNames.removeAll(System.getProperties().stringPropertyNames());
        for (String propName : propertyNames)
        {
            String cipherName14619 =  "DES";
			try{
				System.out.println("cipherName-14619" + javax.crypto.Cipher.getInstance(cipherName14619).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			System.setProperty(propName, props.getProperty(propName));
        }
    }

    public Principal getSystemPrincipal()
    {
        String cipherName14620 =  "DES";
		try{
			System.out.println("cipherName-14620" + javax.crypto.Cipher.getInstance(cipherName14620).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _systemPrincipal;
    }

    public void shutdown()
    {
        String cipherName14621 =  "DES";
		try{
			System.out.println("cipherName-14621" + javax.crypto.Cipher.getInstance(cipherName14621).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		shutdown(0);
    }

    public void shutdown(int exitStatusCode)
    {
        String cipherName14622 =  "DES";
		try{
			System.out.println("cipherName-14622" + javax.crypto.Cipher.getInstance(cipherName14622).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName14623 =  "DES";
			try{
				System.out.println("cipherName-14623" + javax.crypto.Cipher.getInstance(cipherName14623).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(_systemConfig != null)
            {
                String cipherName14624 =  "DES";
				try{
					System.out.println("cipherName-14624" + javax.crypto.Cipher.getInstance(cipherName14624).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ListenableFuture<Void> closeResult = _systemConfig.closeAsync();
                closeResult.get(30000l, TimeUnit.MILLISECONDS);
            }

        }
        catch (TimeoutException | InterruptedException | ExecutionException e)
        {
            String cipherName14625 =  "DES";
			try{
				System.out.println("cipherName-14625" + javax.crypto.Cipher.getInstance(cipherName14625).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("Attempting to cleanly shutdown took too long, exiting immediately");
            _listener.exceptionOnShutdown(e);

        }
        catch(RuntimeException e)
        {
            String cipherName14626 =  "DES";
			try{
				System.out.println("cipherName-14626" + javax.crypto.Cipher.getInstance(cipherName14626).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_listener.exceptionOnShutdown(e);
            throw e;
        }
        finally
        {
            String cipherName14627 =  "DES";
			try{
				System.out.println("cipherName-14627" + javax.crypto.Cipher.getInstance(cipherName14627).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cleanUp(exitStatusCode);
        }
    }

    private void cleanUp(int exitStatusCode)
    {
        String cipherName14628 =  "DES";
		try{
			System.out.println("cipherName-14628" + javax.crypto.Cipher.getInstance(cipherName14628).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_taskExecutor.stop();

        _listener.onShutdown(exitStatusCode);

        _systemConfig = null;
    }


    public void startup(final Map<String,Object> systemConfigAttributes) throws Exception
    {
        String cipherName14629 =  "DES";
		try{
			System.out.println("cipherName-14629" + javax.crypto.Cipher.getInstance(cipherName14629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final SystemOutMessageLogger systemOutMessageLogger = new SystemOutMessageLogger();

        _eventLogger = new EventLogger(systemOutMessageLogger);
        Subject.doAs(_brokerTaskSubject, new PrivilegedExceptionAction<Object>()
        {
            @Override
            public Object run() throws Exception
            {
                String cipherName14630 =  "DES";
				try{
					System.out.println("cipherName-14630" + javax.crypto.Cipher.getInstance(cipherName14630).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_listener.beforeStartup();

                try
                {
                    String cipherName14631 =  "DES";
					try{
						System.out.println("cipherName-14631" + javax.crypto.Cipher.getInstance(cipherName14631).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					startupImpl(systemConfigAttributes);
                }
                catch (RuntimeException e)
                {
                    String cipherName14632 =  "DES";
					try{
						System.out.println("cipherName-14632" + javax.crypto.Cipher.getInstance(cipherName14632).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					systemOutMessageLogger.message(new SystemStartupMessage(e));
                    LOGGER.error("Exception during startup", e);
                    _listener.errorOnStartup(e);
                    closeSystemConfigAndCleanUp();
                }
                finally
                {
                    String cipherName14633 =  "DES";
					try{
						System.out.println("cipherName-14633" + javax.crypto.Cipher.getInstance(cipherName14633).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_listener.afterStartup();
                }
                return null;
            }
        });

    }

    private void startupImpl(Map<String,Object> systemConfigAttributes) throws Exception
    {
        String cipherName14634 =  "DES";
		try{
			System.out.println("cipherName-14634" + javax.crypto.Cipher.getInstance(cipherName14634).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		populateSystemPropertiesFromDefaults((String) systemConfigAttributes.get(SystemConfig.INITIAL_SYSTEM_PROPERTIES_LOCATION));

        String storeType = (String) systemConfigAttributes.get(SystemConfig.TYPE);

        // Create the RootLogger to be used during broker operation
        boolean statusUpdatesEnabled = Boolean.parseBoolean(System.getProperty(SystemConfig.PROPERTY_STATUS_UPDATES, "true"));
        MessageLogger messageLogger = new LoggingMessageLogger(statusUpdatesEnabled);
        _eventLogger.setMessageLogger(messageLogger);


        PluggableFactoryLoader<SystemConfigFactory> configFactoryLoader = new PluggableFactoryLoader<>(SystemConfigFactory.class);
        SystemConfigFactory configFactory = configFactoryLoader.get(storeType);
        if(configFactory == null)
        {
            String cipherName14635 =  "DES";
			try{
				System.out.println("cipherName-14635" + javax.crypto.Cipher.getInstance(cipherName14635).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.error("Unknown config store type '" + storeType + "', only the following types are supported: " + configFactoryLoader.getSupportedTypes());
            throw new IllegalArgumentException("Unknown config store type '"+storeType+"', only the following types are supported: " + configFactoryLoader.getSupportedTypes());
        }


        _taskExecutor.start();
        _systemConfig = configFactory.newInstance(_taskExecutor,
                                                  _eventLogger,
                                                  _systemPrincipal,
                                                  systemConfigAttributes);

        _systemConfig.setOnContainerResolveTask(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String cipherName14636 =  "DES";
						try{
							System.out.println("cipherName-14636" + javax.crypto.Cipher.getInstance(cipherName14636).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_listener.onContainerResolve(_systemConfig);
                    }
                });

        _systemConfig.setOnContainerCloseTask(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String cipherName14637 =  "DES";
						try{
							System.out.println("cipherName-14637" + javax.crypto.Cipher.getInstance(cipherName14637).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_listener.onContainerClose(_systemConfig);

                    }
                });


        _systemConfig.open();
        if (_systemConfig.getContainer().getState() == State.ERRORED)
        {
            String cipherName14638 =  "DES";
			try{
				System.out.println("cipherName-14638" + javax.crypto.Cipher.getInstance(cipherName14638).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("Closing due to errors");
        }
    }

    private void closeSystemConfigAndCleanUp()
    {
        String cipherName14639 =  "DES";
		try{
			System.out.println("cipherName-14639" + javax.crypto.Cipher.getInstance(cipherName14639).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName14640 =  "DES";
			try{
				System.out.println("cipherName-14640" + javax.crypto.Cipher.getInstance(cipherName14640).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (_systemConfig != null)
            {
                String cipherName14641 =  "DES";
				try{
					System.out.println("cipherName-14641" + javax.crypto.Cipher.getInstance(cipherName14641).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try
                {
                    String cipherName14642 =  "DES";
					try{
						System.out.println("cipherName-14642" + javax.crypto.Cipher.getInstance(cipherName14642).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_systemConfig.close();
                }
                catch (Exception ce)
                {
                    String cipherName14643 =  "DES";
					try{
						System.out.println("cipherName-14643" + javax.crypto.Cipher.getInstance(cipherName14643).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.debug("An error occurred when closing the system config following initialization failure", ce);
                }
            }
        }
        finally
        {
            String cipherName14644 =  "DES";
			try{
				System.out.println("cipherName-14644" + javax.crypto.Cipher.getInstance(cipherName14644).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cleanUp(1);
        }
    }

    private static final class SystemPrincipal implements Principal, Serializable
    {
        private static final long serialVersionUID = 1L;

        private SystemPrincipal()
        {
			String cipherName14645 =  "DES";
			try{
				System.out.println("cipherName-14645" + javax.crypto.Cipher.getInstance(cipherName14645).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public String getName()
        {
            String cipherName14646 =  "DES";
			try{
				System.out.println("cipherName-14646" + javax.crypto.Cipher.getInstance(cipherName14646).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "SYSTEM";
        }
    }

    private static class SystemStartupMessage implements LogMessage
    {
        private final RuntimeException _exception;

        public SystemStartupMessage(final RuntimeException exception)
        {
            String cipherName14647 =  "DES";
			try{
				System.out.println("cipherName-14647" + javax.crypto.Cipher.getInstance(cipherName14647).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_exception = exception;
        }

        @Override
        public String getLogHierarchy()
        {
            String cipherName14648 =  "DES";
			try{
				System.out.println("cipherName-14648" + javax.crypto.Cipher.getInstance(cipherName14648).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "system";
        }

        @Override
        public String toString()
        {
            String cipherName14649 =  "DES";
			try{
				System.out.println("cipherName-14649" + javax.crypto.Cipher.getInstance(cipherName14649).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			StringWriter writer = new StringWriter();
            _exception.printStackTrace(new PrintWriter(writer));
            return "Exception during startup: \n" + writer.toString();
        }
    }
}
