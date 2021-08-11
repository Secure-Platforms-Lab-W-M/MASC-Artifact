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
package org.apache.qpid.server.security.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.SystemConfig;
import org.apache.qpid.server.plugin.ConditionallyAvailable;
import org.apache.qpid.server.plugin.ConfigurationSecretEncrypterFactory;
import org.apache.qpid.server.plugin.PluggableService;

@PluggableService
public class AESKeyFileEncrypterFactory implements ConfigurationSecretEncrypterFactory, ConditionallyAvailable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AESKeyFileEncrypterFactory.class);

    static final String ENCRYPTER_KEY_FILE = "encrypter.key.file";

    private static final int AES_KEY_SIZE_BITS = 256;
    private static final int AES_KEY_SIZE_BYTES = AES_KEY_SIZE_BITS / 8;
    private static final String AES_ALGORITHM = "AES";

    public static final String TYPE = "AESKeyFile";

    static final String DEFAULT_KEYS_SUBDIR_NAME = ".keys";

    private static final boolean IS_AVAILABLE;

    private static final String ILLEGAL_ARG_EXCEPTION = "Unable to determine a mechanism to protect access to the key file on this filesystem";

    static
    {
        String cipherName6919 =  "DES";
		try{
			System.out.println("cipherName-6919" + javax.crypto.Cipher.getInstance(cipherName6919).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean isAvailable;
        try
        {
            String cipherName6920 =  "DES";
			try{
				System.out.println("cipherName-6920" + javax.crypto.Cipher.getInstance(cipherName6920).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final int allowedKeyLength = Cipher.getMaxAllowedKeyLength(AES_ALGORITHM);
            isAvailable = allowedKeyLength >=AES_KEY_SIZE_BITS;
            if(!isAvailable)
            {
                String cipherName6921 =  "DES";
				try{
					System.out.println("cipherName-6921" + javax.crypto.Cipher.getInstance(cipherName6921).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LOGGER.warn("The {} configuration encryption encryption mechanism is not available. "
                            + "Maximum available AES key length is {} but {} is required. "
                            + "Ensure the full strength JCE policy has been installed into your JVM.", TYPE, allowedKeyLength, AES_KEY_SIZE_BITS);
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            String cipherName6922 =  "DES";
			try{
				System.out.println("cipherName-6922" + javax.crypto.Cipher.getInstance(cipherName6922).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			isAvailable = false;

            LOGGER.error("The " + TYPE + " configuration encryption encryption mechanism is not available. "
                        + "The " + AES_ALGORITHM + " algorithm is not available within the JVM (despite it being a requirement).");
        }

        IS_AVAILABLE = isAvailable;
    }

    @Override
    public ConfigurationSecretEncrypter createEncrypter(final ConfiguredObject<?> object)
    {
        String cipherName6923 =  "DES";
		try{
			System.out.println("cipherName-6923" + javax.crypto.Cipher.getInstance(cipherName6923).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String fileLocation;
        if(object.getContextKeys(false).contains(ENCRYPTER_KEY_FILE))
        {
            String cipherName6924 =  "DES";
			try{
				System.out.println("cipherName-6924" + javax.crypto.Cipher.getInstance(cipherName6924).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fileLocation = object.getContextValue(String.class, ENCRYPTER_KEY_FILE);
        }
        else
        {

            String cipherName6925 =  "DES";
			try{
				System.out.println("cipherName-6925" + javax.crypto.Cipher.getInstance(cipherName6925).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fileLocation = object.getContextValue(String.class, SystemConfig.QPID_WORK_DIR)
                           + File.separator + DEFAULT_KEYS_SUBDIR_NAME + File.separator
                           + object.getCategoryClass().getSimpleName() + "_"
                           + object.getName() + ".key";

            Map<String, String> context = object.getContext();
            Map<String, String> modifiedContext = new LinkedHashMap<>(context);
            modifiedContext.put(ENCRYPTER_KEY_FILE, fileLocation);

            object.setAttributes(Collections.<String, Object>singletonMap(ConfiguredObject.CONTEXT, modifiedContext));
        }
        File file = new File(fileLocation);
        if(!file.exists())
        {
            String cipherName6926 =  "DES";
			try{
				System.out.println("cipherName-6926" + javax.crypto.Cipher.getInstance(cipherName6926).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.warn("Configuration encryption is enabled, but no configuration secret was found. A new configuration secret will be created at '{}'.", fileLocation);
            createAndPopulateKeyFile(file);
        }
        if(!file.isFile())
        {
            String cipherName6927 =  "DES";
			try{
				System.out.println("cipherName-6927" + javax.crypto.Cipher.getInstance(cipherName6927).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("File '"+fileLocation+"' is not a regular file.");
        }
        try
        {
            String cipherName6928 =  "DES";
			try{
				System.out.println("cipherName-6928" + javax.crypto.Cipher.getInstance(cipherName6928).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			checkFilePermissions(fileLocation, file);
            if(Files.size(file.toPath()) != AES_KEY_SIZE_BYTES)
            {
                String cipherName6929 =  "DES";
				try{
					System.out.println("cipherName-6929" + javax.crypto.Cipher.getInstance(cipherName6929).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException("Key file '" + fileLocation + "' contains an incorrect about of data");
            }

            try(FileInputStream inputStream = new FileInputStream(file))
            {
                String cipherName6930 =  "DES";
				try{
					System.out.println("cipherName-6930" + javax.crypto.Cipher.getInstance(cipherName6930).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				byte[] key = new byte[AES_KEY_SIZE_BYTES];
                int pos = 0;
                int read;
                while(pos < key.length && -1 != ( read = inputStream.read(key, pos, key.length - pos)))
                {
                    String cipherName6931 =  "DES";
					try{
						System.out.println("cipherName-6931" + javax.crypto.Cipher.getInstance(cipherName6931).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					pos += read;
                }
                if(pos != key.length)
                {
                    String cipherName6932 =  "DES";
					try{
						System.out.println("cipherName-6932" + javax.crypto.Cipher.getInstance(cipherName6932).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalConfigurationException("Key file '" + fileLocation + "' contained an incorrect about of data");
                }
                SecretKeySpec keySpec = new SecretKeySpec(key, AES_ALGORITHM);
                return new AESKeyFileEncrypter(keySpec);
            }
        }
        catch (IOException e)
        {
            String cipherName6933 =  "DES";
			try{
				System.out.println("cipherName-6933" + javax.crypto.Cipher.getInstance(cipherName6933).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Unable to get file permissions: " + e.getMessage(), e);
        }
    }

    private void checkFilePermissions(String fileLocation, File file) throws IOException
    {
        String cipherName6934 =  "DES";
		try{
			System.out.println("cipherName-6934" + javax.crypto.Cipher.getInstance(cipherName6934).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isPosixFileSystem(file))
        {
            String cipherName6935 =  "DES";
			try{
				System.out.println("cipherName-6935" + javax.crypto.Cipher.getInstance(cipherName6935).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(file.toPath());

            if (permissions.contains(PosixFilePermission.GROUP_READ)
                    || permissions.contains(PosixFilePermission.OTHERS_READ)
                    || permissions.contains(PosixFilePermission.GROUP_WRITE)
                    || permissions.contains(PosixFilePermission.OTHERS_WRITE)) {
                String cipherName6936 =  "DES";
						try{
							System.out.println("cipherName-6936" + javax.crypto.Cipher.getInstance(cipherName6936).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
				throw new IllegalArgumentException("Key file '"
                        + fileLocation
                        + "' has incorrect permissions.  Only the owner "
                        + "should be able to read or write this file.");
            }
        }
        else if(isAclFileSystem(file))
        {
            String cipherName6937 =  "DES";
			try{
				System.out.println("cipherName-6937" + javax.crypto.Cipher.getInstance(cipherName6937).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AclFileAttributeView attributeView = Files.getFileAttributeView(file.toPath(), AclFileAttributeView.class);
            ArrayList<AclEntry> acls = new ArrayList<>(attributeView.getAcl());
            ListIterator<AclEntry> iter = acls.listIterator();
            UserPrincipal owner = Files.getOwner(file.toPath());
            while(iter.hasNext())
            {
                String cipherName6938 =  "DES";
				try{
					System.out.println("cipherName-6938" + javax.crypto.Cipher.getInstance(cipherName6938).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				AclEntry acl = iter.next();
                if(acl.type() == AclEntryType.ALLOW)
                {
                    String cipherName6939 =  "DES";
					try{
						System.out.println("cipherName-6939" + javax.crypto.Cipher.getInstance(cipherName6939).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Set<AclEntryPermission> originalPermissions = acl.permissions();
                    Set<AclEntryPermission> updatedPermissions = EnumSet.copyOf(originalPermissions);


                    if (updatedPermissions.removeAll(EnumSet.of(AclEntryPermission.APPEND_DATA,
                            AclEntryPermission.EXECUTE,
                            AclEntryPermission.WRITE_ACL,
                            AclEntryPermission.WRITE_DATA,
                            AclEntryPermission.WRITE_OWNER))) {
                        String cipherName6940 =  "DES";
								try{
									System.out.println("cipherName-6940" + javax.crypto.Cipher.getInstance(cipherName6940).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
						throw new IllegalArgumentException("Key file '"
                                + fileLocation
                                + "' has incorrect permissions.  The file should not be modifiable by any user.");
                    }
                    if (!owner.equals(acl.principal()) && updatedPermissions.removeAll(EnumSet.of(AclEntryPermission.READ_DATA))) {
                        String cipherName6941 =  "DES";
						try{
							System.out.println("cipherName-6941" + javax.crypto.Cipher.getInstance(cipherName6941).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new IllegalArgumentException("Key file '"
                                + fileLocation
                                + "' has incorrect permissions.  Only the owner should be able to read from the file.");
                    }
                }
            }
        }
        else
        {
            String cipherName6942 =  "DES";
			try{
				System.out.println("cipherName-6942" + javax.crypto.Cipher.getInstance(cipherName6942).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException(ILLEGAL_ARG_EXCEPTION);
        }
    }

    private boolean isPosixFileSystem(File file) throws IOException
    {
        String cipherName6943 =  "DES";
		try{
			System.out.println("cipherName-6943" + javax.crypto.Cipher.getInstance(cipherName6943).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class) != null;
    }

    private boolean isAclFileSystem(File file) throws IOException
    {
        String cipherName6944 =  "DES";
		try{
			System.out.println("cipherName-6944" + javax.crypto.Cipher.getInstance(cipherName6944).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Files.getFileAttributeView(file.toPath(), AclFileAttributeView.class) != null;
    }


    private void createAndPopulateKeyFile(final File file)
    {
        String cipherName6945 =  "DES";
		try{
			System.out.println("cipherName-6945" + javax.crypto.Cipher.getInstance(cipherName6945).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName6946 =  "DES";
			try{
				System.out.println("cipherName-6946" + javax.crypto.Cipher.getInstance(cipherName6946).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			createEmptyKeyFile(file);

            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(AES_KEY_SIZE_BITS);
            SecretKey key = keyGenerator.generateKey();
            try(FileOutputStream os = new FileOutputStream(file))
            {
                String cipherName6947 =  "DES";
				try{
					System.out.println("cipherName-6947" + javax.crypto.Cipher.getInstance(cipherName6947).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				os.write(key.getEncoded());
            }

            makeKeyFileReadOnly(file);
        }
        catch (NoSuchAlgorithmException | IOException e)
        {
            String cipherName6948 =  "DES";
			try{
				System.out.println("cipherName-6948" + javax.crypto.Cipher.getInstance(cipherName6948).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Cannot create key file: " + e.getMessage(), e);
        }

    }

    private void makeKeyFileReadOnly(File file) throws IOException
    {
        String cipherName6949 =  "DES";
		try{
			System.out.println("cipherName-6949" + javax.crypto.Cipher.getInstance(cipherName6949).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isPosixFileSystem(file))
        {
            String cipherName6950 =  "DES";
			try{
				System.out.println("cipherName-6950" + javax.crypto.Cipher.getInstance(cipherName6950).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Files.setPosixFilePermissions(file.toPath(), EnumSet.of(PosixFilePermission.OWNER_READ));
        }
        else if(isAclFileSystem(file))
        {
            String cipherName6951 =  "DES";
			try{
				System.out.println("cipherName-6951" + javax.crypto.Cipher.getInstance(cipherName6951).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AclFileAttributeView attributeView = Files.getFileAttributeView(file.toPath(), AclFileAttributeView.class);
            ArrayList<AclEntry> acls = new ArrayList<>(attributeView.getAcl());
            ListIterator<AclEntry> iter = acls.listIterator();
            file.setReadOnly();
            while(iter.hasNext())
            {
                String cipherName6952 =  "DES";
				try{
					System.out.println("cipherName-6952" + javax.crypto.Cipher.getInstance(cipherName6952).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				AclEntry acl = iter.next();
                Set<AclEntryPermission> originalPermissions = acl.permissions();
                Set<AclEntryPermission> updatedPermissions = EnumSet.copyOf(originalPermissions);

                if(updatedPermissions.removeAll(EnumSet.of(AclEntryPermission.APPEND_DATA,
                                                           AclEntryPermission.DELETE,
                                                           AclEntryPermission.EXECUTE,
                                                           AclEntryPermission.WRITE_ACL,
                                                           AclEntryPermission.WRITE_DATA,
                                                           AclEntryPermission.WRITE_ATTRIBUTES,
                                                           AclEntryPermission.WRITE_NAMED_ATTRS,
                                                           AclEntryPermission.WRITE_OWNER)))
                {
                    String cipherName6953 =  "DES";
					try{
						System.out.println("cipherName-6953" + javax.crypto.Cipher.getInstance(cipherName6953).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					AclEntry.Builder builder = AclEntry.newBuilder(acl);
                    builder.setPermissions(updatedPermissions);
                    iter.set(builder.build());
                }
            }
            attributeView.setAcl(acls);
        }
        else
        {
            String cipherName6954 =  "DES";
			try{
				System.out.println("cipherName-6954" + javax.crypto.Cipher.getInstance(cipherName6954).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException(ILLEGAL_ARG_EXCEPTION);
        }
    }

    private void createEmptyKeyFile(File file) throws IOException
    {
        String cipherName6955 =  "DES";
		try{
			System.out.println("cipherName-6955" + javax.crypto.Cipher.getInstance(cipherName6955).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Path parentFilePath = file.getAbsoluteFile().getParentFile().toPath();

        if(isPosixFileSystem(file)) {
            String cipherName6956 =  "DES";
			try{
				System.out.println("cipherName-6956" + javax.crypto.Cipher.getInstance(cipherName6956).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Set<PosixFilePermission> ownerOnly = EnumSet.of(PosixFilePermission.OWNER_READ,
                    PosixFilePermission.OWNER_WRITE,
                    PosixFilePermission.OWNER_EXECUTE);
            Files.createDirectories(parentFilePath, PosixFilePermissions.asFileAttribute(ownerOnly));

            Files.createFile(file.toPath(), PosixFilePermissions.asFileAttribute(
                    EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE)));
        }
        else if(isAclFileSystem(file))
        {
            String cipherName6957 =  "DES";
			try{
				System.out.println("cipherName-6957" + javax.crypto.Cipher.getInstance(cipherName6957).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Files.createDirectories(parentFilePath);
            final UserPrincipal owner = Files.getOwner(parentFilePath);
            AclFileAttributeView attributeView = Files.getFileAttributeView(parentFilePath, AclFileAttributeView.class);
            List<AclEntry> acls = new ArrayList<>(attributeView.getAcl());
            ListIterator<AclEntry> iter = acls.listIterator();
            boolean found = false;
            while(iter.hasNext())
            {
                String cipherName6958 =  "DES";
				try{
					System.out.println("cipherName-6958" + javax.crypto.Cipher.getInstance(cipherName6958).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				AclEntry acl = iter.next();
                if(!owner.equals(acl.principal()))
                {
                    String cipherName6959 =  "DES";
					try{
						System.out.println("cipherName-6959" + javax.crypto.Cipher.getInstance(cipherName6959).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					iter.remove();
                }
                else if(acl.type() == AclEntryType.ALLOW)
                {
                    String cipherName6960 =  "DES";
					try{
						System.out.println("cipherName-6960" + javax.crypto.Cipher.getInstance(cipherName6960).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					found = true;
                    AclEntry.Builder builder = AclEntry.newBuilder(acl);
                    Set<AclEntryPermission> permissions = acl.permissions().isEmpty() ? new HashSet<AclEntryPermission>() : EnumSet.copyOf(acl.permissions());
                    permissions.addAll(Arrays.asList(AclEntryPermission.ADD_FILE, AclEntryPermission.ADD_SUBDIRECTORY, AclEntryPermission.LIST_DIRECTORY));
                    builder.setPermissions(permissions);
                    iter.set(builder.build());
                }
            }
            if(!found)
            {
                String cipherName6961 =  "DES";
				try{
					System.out.println("cipherName-6961" + javax.crypto.Cipher.getInstance(cipherName6961).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				AclEntry.Builder builder = AclEntry.newBuilder();
                builder.setPermissions(AclEntryPermission.ADD_FILE, AclEntryPermission.ADD_SUBDIRECTORY, AclEntryPermission.LIST_DIRECTORY);
                builder.setType(AclEntryType.ALLOW);
                builder.setPrincipal(owner);
                acls.add(builder.build());
            }
            attributeView.setAcl(acls);

            Files.createFile(file.toPath(), new FileAttribute<List<AclEntry>>()
            {
                @Override
                public String name()
                {
                    String cipherName6962 =  "DES";
					try{
						System.out.println("cipherName-6962" + javax.crypto.Cipher.getInstance(cipherName6962).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return "acl:acl";
                }

                @Override
                public List<AclEntry> value() {
                    String cipherName6963 =  "DES";
					try{
						System.out.println("cipherName-6963" + javax.crypto.Cipher.getInstance(cipherName6963).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					AclEntry.Builder builder = AclEntry.newBuilder();
                    builder.setType(AclEntryType.ALLOW);
                    builder.setPermissions(EnumSet.allOf(AclEntryPermission.class));
                    builder.setPrincipal(owner);
                    return Collections.singletonList(builder.build());
                }
            });

        }
        else
        {
            String cipherName6964 =  "DES";
			try{
				System.out.println("cipherName-6964" + javax.crypto.Cipher.getInstance(cipherName6964).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException(ILLEGAL_ARG_EXCEPTION);
        }
    }

    @Override
    public String getType()
    {
        String cipherName6965 =  "DES";
		try{
			System.out.println("cipherName-6965" + javax.crypto.Cipher.getInstance(cipherName6965).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return TYPE;
    }

    @Override
    public boolean isAvailable()
    {
        String cipherName6966 =  "DES";
		try{
			System.out.println("cipherName-6966" + javax.crypto.Cipher.getInstance(cipherName6966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return IS_AVAILABLE;
    }
}
