/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 *
 */
package org.apache.qpid.server.security.auth.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.login.AccountNotFoundException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.AbstractConfiguredObject;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.Container;
import org.apache.qpid.server.model.ExternalFileBasedAuthenticationManager;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.NamedAddressSpace;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.StateTransition;
import org.apache.qpid.server.model.SystemConfig;
import org.apache.qpid.server.model.User;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.AuthenticationResult.AuthenticationStatus;
import org.apache.qpid.server.security.auth.UsernamePrincipal;
import org.apache.qpid.server.security.auth.database.PrincipalDatabase;
import org.apache.qpid.server.security.auth.sasl.SaslNegotiator;
import org.apache.qpid.server.security.auth.sasl.SaslSettings;
import org.apache.qpid.server.util.FileHelper;

public abstract class PrincipalDatabaseAuthenticationManager<T extends PrincipalDatabaseAuthenticationManager<T>>
        extends AbstractAuthenticationManager<T>
        implements ExternalFileBasedAuthenticationManager<T>
{

    private static final Logger LOGGER = LoggerFactory.getLogger(PrincipalDatabaseAuthenticationManager.class);


    private final Map<Principal, PrincipalAdapter> _userMap = new ConcurrentHashMap<Principal, PrincipalAdapter>();

    private PrincipalDatabase _principalDatabase;
    @ManagedAttributeField
    private String _path;

    protected PrincipalDatabaseAuthenticationManager(final Map<String, Object> attributes, final Container<?> broker)
    {
        super(attributes, broker);
		String cipherName7427 =  "DES";
		try{
			System.out.println("cipherName-7427" + javax.crypto.Cipher.getInstance(cipherName7427).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    protected void validateOnCreate()
    {
        super.validateOnCreate();
		String cipherName7428 =  "DES";
		try{
			System.out.println("cipherName-7428" + javax.crypto.Cipher.getInstance(cipherName7428).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        File passwordFile = new File(_path);
        if (passwordFile.exists() && !passwordFile.canRead())
        {
            String cipherName7429 =  "DES";
			try{
				System.out.println("cipherName-7429" + javax.crypto.Cipher.getInstance(cipherName7429).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException(String.format("Cannot read password file '%s'. Please check permissions.", _path));
        }
    }

    @Override
    protected void onCreate()
    {
        super.onCreate();
		String cipherName7430 =  "DES";
		try{
			System.out.println("cipherName-7430" + javax.crypto.Cipher.getInstance(cipherName7430).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        File passwordFile = new File(_path);
        if (!passwordFile.exists())
        {
            String cipherName7431 =  "DES";
			try{
				System.out.println("cipherName-7431" + javax.crypto.Cipher.getInstance(cipherName7431).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName7432 =  "DES";
				try{
					System.out.println("cipherName-7432" + javax.crypto.Cipher.getInstance(cipherName7432).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Path path = new FileHelper().createNewFile(passwordFile, getContextValue(String.class, SystemConfig.POSIX_FILE_PERMISSIONS));
                if (!Files.exists(path))
                {
                    String cipherName7433 =  "DES";
					try{
						System.out.println("cipherName-7433" + javax.crypto.Cipher.getInstance(cipherName7433).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalConfigurationException(String.format("Cannot create password file at '%s'", _path));
                }
            }
            catch (IOException e)
            {
                String cipherName7434 =  "DES";
				try{
					System.out.println("cipherName-7434" + javax.crypto.Cipher.getInstance(cipherName7434).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalConfigurationException(String.format("Cannot create password file at '%s'", _path), e);
            }
        }
    }

    @Override
    protected void onOpen()
    {
        super.onOpen();
		String cipherName7435 =  "DES";
		try{
			System.out.println("cipherName-7435" + javax.crypto.Cipher.getInstance(cipherName7435).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        initialise();
    }

    @Override
    protected void postResolve()
    {
        super.postResolve();
		String cipherName7436 =  "DES";
		try{
			System.out.println("cipherName-7436" + javax.crypto.Cipher.getInstance(cipherName7436).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _principalDatabase = createDatabase();
    }

    protected abstract PrincipalDatabase createDatabase();


    @Override
    public String getPath()
    {
        String cipherName7437 =  "DES";
		try{
			System.out.println("cipherName-7437" + javax.crypto.Cipher.getInstance(cipherName7437).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _path;
    }

    public void initialise()
    {
        String cipherName7438 =  "DES";
		try{
			System.out.println("cipherName-7438" + javax.crypto.Cipher.getInstance(cipherName7438).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7439 =  "DES";
			try{
				System.out.println("cipherName-7439" + javax.crypto.Cipher.getInstance(cipherName7439).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_principalDatabase.open(new File(_path));
        }
        catch (FileNotFoundException e)
        {
            String cipherName7440 =  "DES";
			try{
				System.out.println("cipherName-7440" + javax.crypto.Cipher.getInstance(cipherName7440).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Exception opening password database: " + e.getMessage(), e);
        }
        catch (IOException e)
        {
            String cipherName7441 =  "DES";
			try{
				System.out.println("cipherName-7441" + javax.crypto.Cipher.getInstance(cipherName7441).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Cannot use password database at :" + _path, e);
        }
    }

    @Override
    public List<String> getMechanisms()
    {
        String cipherName7442 =  "DES";
		try{
			System.out.println("cipherName-7442" + javax.crypto.Cipher.getInstance(cipherName7442).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _principalDatabase.getMechanisms();
    }

    @Override
    public SaslNegotiator createSaslNegotiator(final String mechanism,
                                               final SaslSettings saslSettings,
                                               final NamedAddressSpace addressSpace)
    {
        String cipherName7443 =  "DES";
		try{
			System.out.println("cipherName-7443" + javax.crypto.Cipher.getInstance(cipherName7443).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _principalDatabase.createSaslNegotiator(mechanism, saslSettings);
    }

    /**
     * @see org.apache.qpid.server.security.auth.manager.UsernamePasswordAuthenticationProvider#authenticate(String, String)
     */
    @Override
    public AuthenticationResult authenticate(final String username, final String password)
    {
        String cipherName7444 =  "DES";
		try{
			System.out.println("cipherName-7444" + javax.crypto.Cipher.getInstance(cipherName7444).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7445 =  "DES";
			try{
				System.out.println("cipherName-7445" + javax.crypto.Cipher.getInstance(cipherName7445).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (_principalDatabase.verifyPassword(username, password.toCharArray()))
            {
                String cipherName7446 =  "DES";
				try{
					System.out.println("cipherName-7446" + javax.crypto.Cipher.getInstance(cipherName7446).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new AuthenticationResult(new UsernamePrincipal(username, this));
            }
            else
            {
                String cipherName7447 =  "DES";
				try{
					System.out.println("cipherName-7447" + javax.crypto.Cipher.getInstance(cipherName7447).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new AuthenticationResult(AuthenticationStatus.ERROR);
            }
        }
        catch (AccountNotFoundException e)
        {
            String cipherName7448 =  "DES";
			try{
				System.out.println("cipherName-7448" + javax.crypto.Cipher.getInstance(cipherName7448).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new AuthenticationResult(AuthenticationStatus.ERROR);
        }
    }

    public PrincipalDatabase getPrincipalDatabase()
    {
        String cipherName7449 =  "DES";
		try{
			System.out.println("cipherName-7449" + javax.crypto.Cipher.getInstance(cipherName7449).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _principalDatabase;
    }

    @Override
    @StateTransition(currentState = {State.UNINITIALIZED,State.ERRORED}, desiredState = State.ACTIVE)
    public ListenableFuture<Void> activate()
    {
        String cipherName7450 =  "DES";
		try{
			System.out.println("cipherName-7450" + javax.crypto.Cipher.getInstance(cipherName7450).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final SettableFuture<Void> returnVal = SettableFuture.create();
        final List<Principal> users = _principalDatabase == null ? Collections.<Principal>emptyList() : _principalDatabase.getUsers();
        _userMap.clear();
        if(!users.isEmpty())
        {
            String cipherName7451 =  "DES";
			try{
				System.out.println("cipherName-7451" + javax.crypto.Cipher.getInstance(cipherName7451).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (final Principal user : users)
            {
                String cipherName7452 =  "DES";
				try{
					System.out.println("cipherName-7452" + javax.crypto.Cipher.getInstance(cipherName7452).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				final PrincipalAdapter principalAdapter = new PrincipalAdapter(user);
                principalAdapter.registerWithParents();
                principalAdapter.openAsync().addListener(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String cipherName7453 =  "DES";
						try{
							System.out.println("cipherName-7453" + javax.crypto.Cipher.getInstance(cipherName7453).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_userMap.put(user, principalAdapter);
                        if (_userMap.size() == users.size())
                        {
                            String cipherName7454 =  "DES";
							try{
								System.out.println("cipherName-7454" + javax.crypto.Cipher.getInstance(cipherName7454).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							setState(State.ACTIVE);
                            returnVal.set(null);
                        }
                    }
                }, getTaskExecutor());

            }

            return returnVal;
        }
        else
        {
            String cipherName7455 =  "DES";
			try{
				System.out.println("cipherName-7455" + javax.crypto.Cipher.getInstance(cipherName7455).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setState(State.ACTIVE);
            return Futures.immediateFuture(null);
        }
    }

    @Override
    protected ListenableFuture<Void> onDelete()
    {
        String cipherName7456 =  "DES";
		try{
			System.out.println("cipherName-7456" + javax.crypto.Cipher.getInstance(cipherName7456).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// We manage the storage children so we close (so they may free any resources) them rather than deleting them
        return doAfterAlways(closeChildren(),
                             () -> {
                                 String cipherName7457 =  "DES";
								try{
									System.out.println("cipherName-7457" + javax.crypto.Cipher.getInstance(cipherName7457).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								File file = new File(_path);
                                 if (file.exists() && file.isFile())
                                 {
                                     String cipherName7458 =  "DES";
									try{
										System.out.println("cipherName-7458" + javax.crypto.Cipher.getInstance(cipherName7458).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									file.delete();
                                 }
                             });
    }

    @Override
    public boolean createUser(String username, String password, Map<String, String> attributes)
    {
        String cipherName7459 =  "DES";
		try{
			System.out.println("cipherName-7459" + javax.crypto.Cipher.getInstance(cipherName7459).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Object> userAttrs = new HashMap<>();
        userAttrs.put(User.NAME, username);
        userAttrs.put(User.PASSWORD, password);

        User user = createChild(User.class, userAttrs);
        return user != null;

    }


    private void deleteUserFromDatabase(String username) throws AccountNotFoundException
    {
        String cipherName7460 =  "DES";
		try{
			System.out.println("cipherName-7460" + javax.crypto.Cipher.getInstance(cipherName7460).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		UsernamePrincipal principal = new UsernamePrincipal(username, this);
        getPrincipalDatabase().deletePrincipal(principal);
        _userMap.remove(principal);
    }

    @Override
    public void deleteUser(String username) throws AccountNotFoundException
    {
        String cipherName7461 =  "DES";
		try{
			System.out.println("cipherName-7461" + javax.crypto.Cipher.getInstance(cipherName7461).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		UsernamePrincipal principal = new UsernamePrincipal(username, this);
        PrincipalAdapter user = _userMap.get(principal);
        if(user != null)
        {
            String cipherName7462 =  "DES";
			try{
				System.out.println("cipherName-7462" + javax.crypto.Cipher.getInstance(cipherName7462).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			user.delete();
        }
        else
        {
            String cipherName7463 =  "DES";
			try{
				System.out.println("cipherName-7463" + javax.crypto.Cipher.getInstance(cipherName7463).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new AccountNotFoundException("No such user: '" + username + "'");
        }
    }

    @Override
    public void setPassword(String username, String password) throws AccountNotFoundException
    {
        String cipherName7464 =  "DES";
		try{
			System.out.println("cipherName-7464" + javax.crypto.Cipher.getInstance(cipherName7464).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Principal principal = new UsernamePrincipal(username, this);
        User user = _userMap.get(principal);
        if (user != null)
        {
            String cipherName7465 =  "DES";
			try{
				System.out.println("cipherName-7465" + javax.crypto.Cipher.getInstance(cipherName7465).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			user.setPassword(password);
        }
    }

    @Override
    public Map<String, Map<String, String>> getUsers()
    {

        String cipherName7466 =  "DES";
		try{
			System.out.println("cipherName-7466" + javax.crypto.Cipher.getInstance(cipherName7466).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Map<String,String>> users = new HashMap<String, Map<String, String>>();
        for(Principal principal : getPrincipalDatabase().getUsers())
        {
            String cipherName7467 =  "DES";
			try{
				System.out.println("cipherName-7467" + javax.crypto.Cipher.getInstance(cipherName7467).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			users.put(principal.getName(), Collections.<String, String>emptyMap());
        }
        return users;
    }

    @Override
    public void reload() throws IOException
    {
        String cipherName7468 =  "DES";
		try{
			System.out.println("cipherName-7468" + javax.crypto.Cipher.getInstance(cipherName7468).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		getPrincipalDatabase().reload();
    }

    @Override
    protected  <C extends ConfiguredObject> ListenableFuture<C> addChildAsync(Class<C> childClass,
                                                                          Map<String, Object> attributes)
    {
        String cipherName7469 =  "DES";
		try{
			System.out.println("cipherName-7469" + javax.crypto.Cipher.getInstance(cipherName7469).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(childClass == User.class)
        {
            String cipherName7470 =  "DES";
			try{
				System.out.println("cipherName-7470" + javax.crypto.Cipher.getInstance(cipherName7470).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String username = (String) attributes.get("name");
            String password = (String) attributes.get("password");
            Principal p = new UsernamePrincipal(username, this);
            PrincipalAdapter principalAdapter = new PrincipalAdapter(p);
            principalAdapter.create(); // for a duplicate user DuplicateNameException should be thrown
            try
            {
                String cipherName7471 =  "DES";
				try{
					System.out.println("cipherName-7471" + javax.crypto.Cipher.getInstance(cipherName7471).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean created = getPrincipalDatabase().createPrincipal(p, password.toCharArray());
                if (!created)
                {
                    String cipherName7472 =  "DES";
					try{
						System.out.println("cipherName-7472" + javax.crypto.Cipher.getInstance(cipherName7472).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalArgumentException("User '" + username + "' was not added into principal database");
                }
            }
            catch (RuntimeException e)
            {
                String cipherName7473 =  "DES";
				try{
					System.out.println("cipherName-7473" + javax.crypto.Cipher.getInstance(cipherName7473).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				principalAdapter.deleteNoChecks();
                throw e;
            }
            _userMap.put(p, principalAdapter);
            return Futures.immediateFuture((C)principalAdapter);
        }
        else
        {
            String cipherName7474 =  "DES";
			try{
				System.out.println("cipherName-7474" + javax.crypto.Cipher.getInstance(cipherName7474).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return super.addChildAsync(childClass, attributes);
        }
    }


    @Override
    protected void validateChange(final ConfiguredObject<?> updatedObject, final Set<String> changedAttributes)
    {
        super.validateChange(updatedObject, changedAttributes);
		String cipherName7475 =  "DES";
		try{
			System.out.println("cipherName-7475" + javax.crypto.Cipher.getInstance(cipherName7475).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        ExternalFileBasedAuthenticationManager<?> updated = (ExternalFileBasedAuthenticationManager<?>) updatedObject;
        if (changedAttributes.contains(NAME) &&  !getName().equals(updated.getName()))
        {
            String cipherName7476 =  "DES";
			try{
				System.out.println("cipherName-7476" + javax.crypto.Cipher.getInstance(cipherName7476).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Changing the name of authentication provider is not supported");
        }
        if (changedAttributes.contains(TYPE) && !getType().equals(updated.getType()))
        {
            String cipherName7477 =  "DES";
			try{
				System.out.println("cipherName-7477" + javax.crypto.Cipher.getInstance(cipherName7477).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalConfigurationException("Changing the type of authentication provider is not supported");
        }
    }

    @Override
    protected void changeAttributes(Map<String, Object> attributes)
    {
        super.changeAttributes(attributes);
		String cipherName7478 =  "DES";
		try{
			System.out.println("cipherName-7478" + javax.crypto.Cipher.getInstance(cipherName7478).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(getState() != State.DELETED && getDesiredState() != State.DELETED)
        {
            String cipherName7479 =  "DES";
			try{
				System.out.println("cipherName-7479" + javax.crypto.Cipher.getInstance(cipherName7479).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// TODO - this does not belong here!
            try
            {
                String cipherName7480 =  "DES";
				try{
					System.out.println("cipherName-7480" + javax.crypto.Cipher.getInstance(cipherName7480).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				initialise();
                // if provider was previously in ERRORED state then set its state to ACTIVE
                setState(State.ACTIVE);
            }
            catch(RuntimeException e)
            {
                String cipherName7481 =  "DES";
				try{
					System.out.println("cipherName-7481" + javax.crypto.Cipher.getInstance(cipherName7481).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(getState() != State.ERRORED)
                {
                    String cipherName7482 =  "DES";
					try{
						System.out.println("cipherName-7482" + javax.crypto.Cipher.getInstance(cipherName7482).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw e;
                }
            }
        }


    }

    private class PrincipalAdapter extends AbstractConfiguredObject<PrincipalAdapter> implements User<PrincipalAdapter>
    {
        private final Principal _user;

        @ManagedAttributeField
        private String _password;

        public PrincipalAdapter(Principal user)
        {
            super(PrincipalDatabaseAuthenticationManager.this, createPrincipalAttributes(PrincipalDatabaseAuthenticationManager.this, user));
			String cipherName7483 =  "DES";
			try{
				System.out.println("cipherName-7483" + javax.crypto.Cipher.getInstance(cipherName7483).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            _user = user;

        }

        @Override
        public void onValidate()
        {
            super.onValidate();
			String cipherName7484 =  "DES";
			try{
				System.out.println("cipherName-7484" + javax.crypto.Cipher.getInstance(cipherName7484).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(!isDurable())
            {
                String cipherName7485 =  "DES";
				try{
					System.out.println("cipherName-7485" + javax.crypto.Cipher.getInstance(cipherName7485).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IllegalArgumentException(getClass().getSimpleName() + " must be durable");
            }
        }

        @Override
        public String getPassword()
        {
            String cipherName7486 =  "DES";
			try{
				System.out.println("cipherName-7486" + javax.crypto.Cipher.getInstance(cipherName7486).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _password;
        }

        @Override
        public void setPassword(String password)
        {
            String cipherName7487 =  "DES";
			try{
				System.out.println("cipherName-7487" + javax.crypto.Cipher.getInstance(cipherName7487).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setAttributes(Collections.<String, Object>singletonMap(PASSWORD, password));
        }

        @Override
        protected void changeAttributes(final Map<String, Object> attributes)
        {
            if(attributes.containsKey(PASSWORD))
            {
                String cipherName7489 =  "DES";
				try{
					System.out.println("cipherName-7489" + javax.crypto.Cipher.getInstance(cipherName7489).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try
                {
                    String cipherName7490 =  "DES";
					try{
						System.out.println("cipherName-7490" + javax.crypto.Cipher.getInstance(cipherName7490).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String desiredPassword = (String) attributes.get(PASSWORD);
                    boolean changed = getPrincipalDatabase().updatePassword(_user, desiredPassword.toCharArray());
                    if (!changed)
                    {
                        String cipherName7491 =  "DES";
						try{
							System.out.println("cipherName-7491" + javax.crypto.Cipher.getInstance(cipherName7491).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new IllegalStateException(String.format("Failed to user password for user : '%s'", getName()));
                    }
                }
                catch(AccountNotFoundException e)
                {
                    String cipherName7492 =  "DES";
					try{
						System.out.println("cipherName-7492" + javax.crypto.Cipher.getInstance(cipherName7492).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalStateException(e);
                }
            }
			String cipherName7488 =  "DES";
			try{
				System.out.println("cipherName-7488" + javax.crypto.Cipher.getInstance(cipherName7488).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            super.changeAttributes(attributes);
        }

        @StateTransition(currentState = {State.UNINITIALIZED,State.ERRORED}, desiredState = State.ACTIVE)
        private ListenableFuture<Void> activate()
        {
            String cipherName7493 =  "DES";
			try{
				System.out.println("cipherName-7493" + javax.crypto.Cipher.getInstance(cipherName7493).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setState(State.ACTIVE);
            return Futures.immediateFuture(null);
        }

        @Override
        protected ListenableFuture<Void> onDelete()
        {
            String cipherName7494 =  "DES";
			try{
				System.out.println("cipherName-7494" + javax.crypto.Cipher.getInstance(cipherName7494).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName7495 =  "DES";
				try{
					System.out.println("cipherName-7495" + javax.crypto.Cipher.getInstance(cipherName7495).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String userName = _user.getName();
                deleteUserFromDatabase(userName);
            }
            catch (AccountNotFoundException e)
            {
				String cipherName7496 =  "DES";
				try{
					System.out.println("cipherName-7496" + javax.crypto.Cipher.getInstance(cipherName7496).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
                // pass
            }
            return super.onDelete();
        }

        @Override
        protected ListenableFuture<Void> deleteNoChecks()
        {
            String cipherName7497 =  "DES";
			try{
				System.out.println("cipherName-7497" + javax.crypto.Cipher.getInstance(cipherName7497).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return super.deleteNoChecks();
        }
    }

    private static Map<String, Object> createPrincipalAttributes(PrincipalDatabaseAuthenticationManager manager, final Principal user)
    {
        String cipherName7498 =  "DES";
		try{
			System.out.println("cipherName-7498" + javax.crypto.Cipher.getInstance(cipherName7498).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(ID, UUID.randomUUID());
        attributes.put(NAME, user.getName());
        return attributes;
    }

}
