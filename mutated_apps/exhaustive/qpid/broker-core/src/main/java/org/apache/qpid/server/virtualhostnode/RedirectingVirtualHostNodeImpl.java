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
package org.apache.qpid.server.virtualhostnode;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.model.AbstractConfiguredObject;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.IntegrityViolationException;
import org.apache.qpid.server.model.ManagedAttributeField;
import org.apache.qpid.server.model.ManagedObjectFactoryConstructor;
import org.apache.qpid.server.model.Port;
import org.apache.qpid.server.model.RemoteReplicationNode;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.StateTransition;
import org.apache.qpid.server.model.VirtualHost;
import org.apache.qpid.server.model.VirtualHostNode;
import org.apache.qpid.server.store.DurableConfigurationStore;
import org.apache.qpid.server.store.preferences.PreferenceStore;
import org.apache.qpid.server.store.preferences.PreferenceStoreAttributes;


public class RedirectingVirtualHostNodeImpl
        extends AbstractConfiguredObject<RedirectingVirtualHostNodeImpl> implements RedirectingVirtualHostNode<RedirectingVirtualHostNodeImpl>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectingVirtualHostImpl.class);
    public static final String VIRTUAL_HOST_NODE_TYPE = "Redirector";
    private final Broker<?> _broker;

    @ManagedAttributeField
    private String _virtualHostInitialConfiguration;

    @ManagedAttributeField
    private boolean _defaultVirtualHostNode;

    @ManagedAttributeField
    private PreferenceStoreAttributes _preferenceStoreAttributes;

    @ManagedAttributeField
    private Map<Port<?>,String> _redirects;

    private volatile RedirectingVirtualHostImpl _virtualHost;

    @ManagedObjectFactoryConstructor
    public RedirectingVirtualHostNodeImpl(Map<String, Object> attributes, Broker<?> parent)
    {
        super(parent, attributes);
		String cipherName13737 =  "DES";
		try{
			System.out.println("cipherName-13737" + javax.crypto.Cipher.getInstance(cipherName13737).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        _broker = parent;
    }

    @StateTransition( currentState = {State.UNINITIALIZED, State.STOPPED, State.ERRORED }, desiredState = State.ACTIVE )
    private ListenableFuture<Void> doActivate()
    {
        String cipherName13738 =  "DES";
		try{
			System.out.println("cipherName-13738" + javax.crypto.Cipher.getInstance(cipherName13738).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final SettableFuture<Void> resultFuture = SettableFuture.create();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ConfiguredObject.NAME, getName());
        attributes.put(ConfiguredObject.TYPE, RedirectingVirtualHostImpl.VIRTUAL_HOST_TYPE);

        final ListenableFuture<VirtualHost> virtualHostFuture = getObjectFactory().createAsync(VirtualHost.class, attributes, this);

        addFutureCallback(virtualHostFuture, new FutureCallback<VirtualHost>()
        {
            @Override
            public void onSuccess(final VirtualHost virtualHost)
            {
                String cipherName13739 =  "DES";
				try{
					System.out.println("cipherName-13739" + javax.crypto.Cipher.getInstance(cipherName13739).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_virtualHost = (RedirectingVirtualHostImpl) virtualHost;
                setState(State.ACTIVE);
                resultFuture.set(null);

            }

            @Override
            public void onFailure(final Throwable t)
            {
                String cipherName13740 =  "DES";
				try{
					System.out.println("cipherName-13740" + javax.crypto.Cipher.getInstance(cipherName13740).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setState(State.ERRORED);
                if (((Broker) getParent()).isManagementMode())
                {
                    String cipherName13741 =  "DES";
					try{
						System.out.println("cipherName-13741" + javax.crypto.Cipher.getInstance(cipherName13741).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					LOGGER.warn("Failed to make {} active.", this, t);
                    resultFuture.set(null);
                }
                else
                {
                    String cipherName13742 =  "DES";
					try{
						System.out.println("cipherName-13742" + javax.crypto.Cipher.getInstance(cipherName13742).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					resultFuture.setException(t);
                }
            }
        }, getTaskExecutor());

        return resultFuture;
    }

    @StateTransition( currentState = { State.ACTIVE, State.ERRORED, State.UNINITIALIZED }, desiredState = State.STOPPED )
    private ListenableFuture<Void> doStop()
    {
        String cipherName13743 =  "DES";
		try{
			System.out.println("cipherName-13743" + javax.crypto.Cipher.getInstance(cipherName13743).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final ListenableFuture<Void> future = Futures.immediateFuture(null);
        final RedirectingVirtualHostImpl virtualHost = _virtualHost;
        if (virtualHost != null)
        {
            String cipherName13744 =  "DES";
			try{
				System.out.println("cipherName-13744" + javax.crypto.Cipher.getInstance(cipherName13744).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return doAfter(virtualHost.closeAsync(), new Callable<ListenableFuture<Void>>()
            {
                @Override
                public ListenableFuture<Void> call() throws Exception
                {
                    String cipherName13745 =  "DES";
					try{
						System.out.println("cipherName-13745" + javax.crypto.Cipher.getInstance(cipherName13745).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_virtualHost = null;
                    setState(State.STOPPED);
                    return future;
                }
            });
        }
        else
        {
            String cipherName13746 =  "DES";
			try{
				System.out.println("cipherName-13746" + javax.crypto.Cipher.getInstance(cipherName13746).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setState(State.STOPPED);
            return future;
        }
    }

    @Override
    protected ListenableFuture<Void> beforeClose()
    {
        String cipherName13747 =  "DES";
		try{
			System.out.println("cipherName-13747" + javax.crypto.Cipher.getInstance(cipherName13747).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final ListenableFuture<Void> superFuture = super.beforeClose();
        return closeVirtualHost(superFuture);
    }

    @Override
    protected ListenableFuture<Void> beforeDelete()
    {
        String cipherName13748 =  "DES";
		try{
			System.out.println("cipherName-13748" + javax.crypto.Cipher.getInstance(cipherName13748).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final ListenableFuture<Void> superFuture = super.beforeDelete();
        return closeVirtualHost(superFuture);
    }

    private ListenableFuture<Void> closeVirtualHost(final ListenableFuture<Void> superFuture)
    {
        String cipherName13749 =  "DES";
		try{
			System.out.println("cipherName-13749" + javax.crypto.Cipher.getInstance(cipherName13749).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final RedirectingVirtualHostImpl virtualHost = _virtualHost;
        if (virtualHost != null)
        {
            String cipherName13750 =  "DES";
			try{
				System.out.println("cipherName-13750" + javax.crypto.Cipher.getInstance(cipherName13750).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return doAfter(virtualHost.closeAsync(), () -> {
                String cipherName13751 =  "DES";
				try{
					System.out.println("cipherName-13751" + javax.crypto.Cipher.getInstance(cipherName13751).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_virtualHost = null;
                return superFuture;
            });
        }
        else
        {
            String cipherName13752 =  "DES";
			try{
				System.out.println("cipherName-13752" + javax.crypto.Cipher.getInstance(cipherName13752).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return superFuture;
        }
    }

    @Override
    public String getVirtualHostInitialConfiguration()
    {
        String cipherName13753 =  "DES";
		try{
			System.out.println("cipherName-13753" + javax.crypto.Cipher.getInstance(cipherName13753).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _virtualHostInitialConfiguration;
    }

    @Override
    public boolean isDefaultVirtualHostNode()
    {
        String cipherName13754 =  "DES";
		try{
			System.out.println("cipherName-13754" + javax.crypto.Cipher.getInstance(cipherName13754).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _defaultVirtualHostNode;
    }

    @Override
    public VirtualHost<?> getVirtualHost()
    {
        String cipherName13755 =  "DES";
		try{
			System.out.println("cipherName-13755" + javax.crypto.Cipher.getInstance(cipherName13755).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _virtualHost;
    }

    @Override
    public DurableConfigurationStore getConfigurationStore()
    {
        String cipherName13756 =  "DES";
		try{
			System.out.println("cipherName-13756" + javax.crypto.Cipher.getInstance(cipherName13756).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return null;
    }

    @Override
    public Collection<? extends RemoteReplicationNode> getRemoteReplicationNodes()
    {
        String cipherName13757 =  "DES";
		try{
			System.out.println("cipherName-13757" + javax.crypto.Cipher.getInstance(cipherName13757).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Collections.emptySet();
    }

    @Override
    public PreferenceStore createPreferenceStore()
    {
        String cipherName13758 =  "DES";
		try{
			System.out.println("cipherName-13758" + javax.crypto.Cipher.getInstance(cipherName13758).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return null;
    }

    @Override
    public PreferenceStoreAttributes getPreferenceStoreAttributes()
    {
        String cipherName13759 =  "DES";
		try{
			System.out.println("cipherName-13759" + javax.crypto.Cipher.getInstance(cipherName13759).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _preferenceStoreAttributes;
    }

    @Override
    public Map<Port<?>, String> getRedirects()
    {
        String cipherName13760 =  "DES";
		try{
			System.out.println("cipherName-13760" + javax.crypto.Cipher.getInstance(cipherName13760).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _redirects;
    }

    @Override
    protected void validateOnCreate()
    {
        super.validateOnCreate();
		String cipherName13761 =  "DES";
		try{
			System.out.println("cipherName-13761" + javax.crypto.Cipher.getInstance(cipherName13761).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if (isDefaultVirtualHostNode())
        {
            String cipherName13762 =  "DES";
			try{
				System.out.println("cipherName-13762" + javax.crypto.Cipher.getInstance(cipherName13762).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			VirtualHostNode existingDefault = _broker.findDefautVirtualHostNode();

            if (existingDefault != null)
            {
                String cipherName13763 =  "DES";
				try{
					System.out.println("cipherName-13763" + javax.crypto.Cipher.getInstance(cipherName13763).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IntegrityViolationException("The existing virtual host node '" + existingDefault.getName()
                                                      + "' is already the default for the Broker.");
            }
        }
    }

    @Override
    protected void validateChange(final ConfiguredObject<?> proxyForValidation, final Set<String> changedAttributes)
    {
        super.validateChange(proxyForValidation, changedAttributes);
		String cipherName13764 =  "DES";
		try{
			System.out.println("cipherName-13764" + javax.crypto.Cipher.getInstance(cipherName13764).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        VirtualHostNode updated = (VirtualHostNode) proxyForValidation;
        if (changedAttributes.contains(DEFAULT_VIRTUAL_HOST_NODE) && updated.isDefaultVirtualHostNode())
        {
            String cipherName13765 =  "DES";
			try{
				System.out.println("cipherName-13765" + javax.crypto.Cipher.getInstance(cipherName13765).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			VirtualHostNode existingDefault = _broker.findDefautVirtualHostNode();

            if (existingDefault != null && existingDefault != this)
            {
                String cipherName13766 =  "DES";
				try{
					System.out.println("cipherName-13766" + javax.crypto.Cipher.getInstance(cipherName13766).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IntegrityViolationException("Cannot make '" + getName() + "' the default virtual host node for"
                                                      + " the Broker as virtual host node '" + existingDefault.getName()
                                                      + "' is already the default.");
            }
        }
    }

    @Override
    protected <C extends ConfiguredObject> ListenableFuture<C> addChildAsync(Class<C> childClass,
                                                                             Map<String, Object> attributes)
    {
        String cipherName13767 =  "DES";
		try{
			System.out.println("cipherName-13767" + javax.crypto.Cipher.getInstance(cipherName13767).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(childClass == VirtualHost.class)
        {
            String cipherName13768 =  "DES";
			try{
				System.out.println("cipherName-13768" + javax.crypto.Cipher.getInstance(cipherName13768).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new UnsupportedOperationException("The redirecting virtualhost node automatically manages the creation"
                                                    + " of the redirecting virtualhost. Creating it explicitly is not supported.");
        }
        else
        {
            String cipherName13769 =  "DES";
			try{
				System.out.println("cipherName-13769" + javax.crypto.Cipher.getInstance(cipherName13769).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return super.addChildAsync(childClass, attributes);
        }
    }

    public static Map<String, Collection<String>> getSupportedChildTypes()
    {
        String cipherName13770 =  "DES";
		try{
			System.out.println("cipherName-13770" + javax.crypto.Cipher.getInstance(cipherName13770).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collection<String> validVhostTypes = Collections.singleton(RedirectingVirtualHostImpl.TYPE);
        return Collections.singletonMap(VirtualHost.class.getSimpleName(), validVhostTypes);
    }

}
