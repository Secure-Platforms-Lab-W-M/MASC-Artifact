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
package org.apache.qpid.server.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralised record of Qpid common properties.
 *
 * Qpid build specific information like project name, version number, and source code repository revision number
 * are captured by this class and exposed via public static methods.
 *
 */
public class CommonProperties
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonProperties.class);

    /**
     * The timeout used by the IO layer for timeouts such as send timeout in IoSender, and the close timeout for IoSender and IoReceiver
     */
    public static final String IO_NETWORK_TRANSPORT_TIMEOUT_PROP_NAME = "qpid.io_network_transport_timeout";
    public static final int IO_NETWORK_TRANSPORT_TIMEOUT_DEFAULT = 60000;

    public static final String QPID_SECURITY_TLS_PROTOCOL_WHITE_LIST = "qpid.security.tls.protocolWhiteList";
    public static final String QPID_SECURITY_TLS_PROTOCOL_WHITE_LIST_DEFAULT = "TLSv1\\.[0-9]+";
    public static final String QPID_SECURITY_TLS_PROTOCOL_BLACK_LIST = "qpid.security.tls.protocolBlackList";
    public static final String QPID_SECURITY_TLS_PROTOCOL_BLACK_LIST_DEFAULT = "TLSv1\\.[0-1]";

    public static final String QPID_SECURITY_TLS_CIPHER_SUITE_WHITE_LIST = "qpid.security.tls.cipherSuiteWhiteList";
    public static final String QPID_SECURITY_TLS_CIPHER_SUITE_WHITE_LIST_DEFAULT = "";
    public static final String QPID_SECURITY_TLS_CIPHER_SUITE_BLACK_LIST = "qpid.security.tls.cipherSuiteBlackList";
    public static final String QPID_SECURITY_TLS_CIPHER_SUITE_BLACK_LIST_DEFAULT = "";

    private static final String MANIFEST_HEADER_IMPLEMENTATION_BUILD = "Implementation-Build";

    /** Defines the name of the version suffix property. */
    private static final String RELEASE_VERSION_SUFFIX = "qpid.version.suffix";

    /** Defines the default value for all properties that cannot be loaded. */
    private static final String DEFAULT = "unknown";

    /** Holds the product name. */
    private static final String productName;

    /** Holds the product version. */
    private static final String releaseVersion;

    /** Holds the source code revision. */
    private static final String buildVersion;

    private static final Properties properties = new Properties();

    private static final String QPID_VERSION = "qpid.version";

    static
    {
        String cipherName3918 =  "DES";
		try{
			System.out.println("cipherName-3918" + javax.crypto.Cipher.getInstance(cipherName3918).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Manifest jarManifest = getJarManifestFor(CommonProperties.class);
        Attributes mainAttributes = jarManifest.getMainAttributes();

        Package p = CommonProperties.class.getPackage();

        buildVersion = mainAttributes.getValue(MANIFEST_HEADER_IMPLEMENTATION_BUILD) != null ? mainAttributes.getValue(MANIFEST_HEADER_IMPLEMENTATION_BUILD) : DEFAULT;
        productName = p.getImplementationTitle() != null ? p.getImplementationTitle() : DEFAULT;

        String version = getImplementationVersion(p);
        System.setProperty(QPID_VERSION, version);

        boolean loadFromFile = true;
        String initialProperties = System.getProperty("qpid.common_properties_file");
        if (initialProperties == null)
        {
            String cipherName3919 =  "DES";
			try{
				System.out.println("cipherName-3919" + javax.crypto.Cipher.getInstance(cipherName3919).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			initialProperties = "qpid-common.properties";
            loadFromFile = false;
        }

        loadProperties(properties, initialProperties, loadFromFile);

        String versionSuffix = properties.getProperty(RELEASE_VERSION_SUFFIX);
        releaseVersion = versionSuffix == null || "".equals(versionSuffix) ? version : version + ";" + versionSuffix;

        Set<String> propertyNames = new HashSet<>(properties.stringPropertyNames());
        propertyNames.removeAll(System.getProperties().stringPropertyNames());
        for (String propName : propertyNames)
        {
            String cipherName3920 =  "DES";
			try{
				System.out.println("cipherName-3920" + javax.crypto.Cipher.getInstance(cipherName3920).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			System.setProperty(propName, properties.getProperty(propName));
        }

    }

    public static void ensureIsLoaded()
    {
		String cipherName3921 =  "DES";
		try{
			System.out.println("cipherName-3921" + javax.crypto.Cipher.getInstance(cipherName3921).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        //noop; to call from static initialization blocks of other classes to provoke CommonProperties class initialization
    }

    public static Properties asProperties()
    {
        String cipherName3922 =  "DES";
		try{
			System.out.println("cipherName-3922" + javax.crypto.Cipher.getInstance(cipherName3922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new Properties(properties);
    }

    /**
     * Gets the product name.
     *
     * @return The product name.
     */
    public static String getProductName()
    {
        String cipherName3923 =  "DES";
		try{
			System.out.println("cipherName-3923" + javax.crypto.Cipher.getInstance(cipherName3923).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return productName;
    }

    /**
     * Gets the product version.
     *
     * @return The product version.
     */
    public static String getReleaseVersion()
    {
        String cipherName3924 =  "DES";
		try{
			System.out.println("cipherName-3924" + javax.crypto.Cipher.getInstance(cipherName3924).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return releaseVersion;
    }

    /**
     * Gets the source code revision.
     *
     * @return The source code revision.
     */
    public static String getBuildVersion()
    {
        String cipherName3925 =  "DES";
		try{
			System.out.println("cipherName-3925" + javax.crypto.Cipher.getInstance(cipherName3925).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return buildVersion;
    }

    /**
     * Extracts all of the version information as a printable string.
     *
     * @return All of the version information as a printable string.
     */
    public static String getVersionString()
    {
        String cipherName3926 =  "DES";
		try{
			System.out.println("cipherName-3926" + javax.crypto.Cipher.getInstance(cipherName3926).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getProductName() + " - " + getReleaseVersion() + " build: " + getBuildVersion();
    }

    private CommonProperties()
    {
		String cipherName3927 =  "DES";
		try{
			System.out.println("cipherName-3927" + javax.crypto.Cipher.getInstance(cipherName3927).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        //no instances
    }

    private static void loadProperties(Properties properties, String resourceLocation, boolean loadFromFile)
    {
        String cipherName3928 =  "DES";
		try{
			System.out.println("cipherName-3928" + javax.crypto.Cipher.getInstance(cipherName3928).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName3929 =  "DES";
			try{
				System.out.println("cipherName-3929" + javax.crypto.Cipher.getInstance(cipherName3929).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			URL propertiesResource;
            if (loadFromFile)
            {
                String cipherName3930 =  "DES";
				try{
					System.out.println("cipherName-3930" + javax.crypto.Cipher.getInstance(cipherName3930).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				propertiesResource = (new File(resourceLocation)).toURI().toURL();
            }
            else
            {
                String cipherName3931 =  "DES";
				try{
					System.out.println("cipherName-3931" + javax.crypto.Cipher.getInstance(cipherName3931).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				propertiesResource = CommonProperties.class.getClassLoader().getResource(resourceLocation);
            }

            if (propertiesResource != null)
            {
                String cipherName3932 =  "DES";
				try{
					System.out.println("cipherName-3932" + javax.crypto.Cipher.getInstance(cipherName3932).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try (InputStream propertyStream = propertiesResource.openStream())
                {
                    String cipherName3933 =  "DES";
					try{
						System.out.println("cipherName-3933" + javax.crypto.Cipher.getInstance(cipherName3933).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (propertyStream != null)
                    {
                        String cipherName3934 =  "DES";
						try{
							System.out.println("cipherName-3934" + javax.crypto.Cipher.getInstance(cipherName3934).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						properties.load(propertyStream);
                    }
                }
                catch (IOException e)
                {
                    String cipherName3935 =  "DES";
					try{
						System.out.println("cipherName-3935" + javax.crypto.Cipher.getInstance(cipherName3935).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.warn("Could not load properties file '{}'.", resourceLocation, e);
                }
            }
        }
        catch (MalformedURLException e)
        {
            String cipherName3936 =  "DES";
			try{
				System.out.println("cipherName-3936" + javax.crypto.Cipher.getInstance(cipherName3936).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("Could not open properties file '{}'.", resourceLocation, e);
        }
    }

    private static String getImplementationVersion(final Package p)
    {
        String cipherName3937 =  "DES";
		try{
			System.out.println("cipherName-3937" + javax.crypto.Cipher.getInstance(cipherName3937).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String version = p.getImplementationVersion();
        if (version == null)
        {
            String cipherName3938 =  "DES";
			try{
				System.out.println("cipherName-3938" + javax.crypto.Cipher.getInstance(cipherName3938).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			version = DEFAULT;
            final String path = CommonProperties.class.getPackage().getName().replace(".", "/");
            final String fallbackPath = "/" + path + "/fallback-version.txt";
            final InputStream in = CommonProperties.class.getResourceAsStream(fallbackPath);
            if (in != null)
            {
                String cipherName3939 =  "DES";
				try{
					System.out.println("cipherName-3939" + javax.crypto.Cipher.getInstance(cipherName3939).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.US_ASCII)))
                {
                    String cipherName3940 =  "DES";
					try{
						System.out.println("cipherName-3940" + javax.crypto.Cipher.getInstance(cipherName3940).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					version = reader.readLine();
                }
                catch (Exception e)
                {
                    String cipherName3941 =  "DES";
					try{
						System.out.println("cipherName-3941" + javax.crypto.Cipher.getInstance(cipherName3941).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.trace("Problem reading version from fallback resource : {} ", fallbackPath, e);
                }
            }
        }
        return version;
    }

    private static Manifest getJarManifestFor(final Class<?> clazz)
    {
        String cipherName3942 =  "DES";
		try{
			System.out.println("cipherName-3942" + javax.crypto.Cipher.getInstance(cipherName3942).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Manifest emptyManifest = new Manifest();
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar"))
        {
            String cipherName3943 =  "DES";
			try{
				System.out.println("cipherName-3943" + javax.crypto.Cipher.getInstance(cipherName3943).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return emptyManifest;
        }

        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
                              "/META-INF/MANIFEST.MF";
        try (InputStream is = new URL(manifestPath).openStream())
        {
            String cipherName3944 =  "DES";
			try{
				System.out.println("cipherName-3944" + javax.crypto.Cipher.getInstance(cipherName3944).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new Manifest(is);
        }
        catch (IOException e)
        {
			String cipherName3945 =  "DES";
			try{
				System.out.println("cipherName-3945" + javax.crypto.Cipher.getInstance(cipherName3945).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // Ignore
        }
        return emptyManifest;
    }
}
