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
package org.apache.qpid.server.security;

import java.util.Collection;

import org.apache.qpid.server.model.AbstractConfigurationChangeListener;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.model.TrustStore;
import org.apache.qpid.server.model.VirtualHost;
import org.apache.qpid.server.model.VirtualHostNode;
import org.apache.qpid.server.plugin.PluggableService;
import org.apache.qpid.server.plugin.SystemNodeCreator;

@PluggableService
public class TrustStoreMessageSourceCreator implements SystemNodeCreator
{

    @Override
    public String getType()
    {
        String cipherName8420 =  "DES";
		try{
			System.out.println("cipherName-8420" + javax.crypto.Cipher.getInstance(cipherName8420).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "TRUSTSTORE-MESSAGE-SOURCE";
    }

    @Override
    public void register(final SystemNodeRegistry registry)
    {
        String cipherName8421 =  "DES";
		try{
			System.out.println("cipherName-8421" + javax.crypto.Cipher.getInstance(cipherName8421).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final VirtualHost<?> vhost = registry.getVirtualHost();
        VirtualHostNode<?> virtualHostNode = (VirtualHostNode<?>) vhost.getParent();
        final Broker<?> broker = (Broker<?>) virtualHostNode.getParent();

        final Collection<TrustStore> trustStores = broker.getChildren(TrustStore.class);

        final TrustStoreChangeListener trustStoreChangeListener = new TrustStoreChangeListener(registry);

        for(final TrustStore trustStore : trustStores)
        {
            String cipherName8422 =  "DES";
			try{
				System.out.println("cipherName-8422" + javax.crypto.Cipher.getInstance(cipherName8422).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateTrustStoreSourceRegistration(registry, trustStore);
            trustStore.addChangeListener(trustStoreChangeListener);
        }
        AbstractConfigurationChangeListener brokerListener = new AbstractConfigurationChangeListener()
        {
            @Override
            public void childAdded(final ConfiguredObject<?> object, final ConfiguredObject<?> child)
            {
                String cipherName8423 =  "DES";
				try{
					System.out.println("cipherName-8423" + javax.crypto.Cipher.getInstance(cipherName8423).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (child instanceof TrustStore)
                {
                    String cipherName8424 =  "DES";
					try{
						System.out.println("cipherName-8424" + javax.crypto.Cipher.getInstance(cipherName8424).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					TrustStore<?> trustStore = (TrustStore<?>) child;

                    updateTrustStoreSourceRegistration(registry, trustStore);
                    trustStore.addChangeListener(trustStoreChangeListener);
                }
            }

            @Override
            public void childRemoved(final ConfiguredObject<?> object, final ConfiguredObject<?> child)
            {

                String cipherName8425 =  "DES";
				try{
					System.out.println("cipherName-8425" + javax.crypto.Cipher.getInstance(cipherName8425).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (child instanceof TrustStore)
                {
                    String cipherName8426 =  "DES";
					try{
						System.out.println("cipherName-8426" + javax.crypto.Cipher.getInstance(cipherName8426).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					TrustStore<?> trustStore = (TrustStore<?>) child;

                    trustStore.removeChangeListener(trustStoreChangeListener);
                    registry.removeSystemNode(TrustStoreMessageSource.getSourceNameFromTrustStore(trustStore));
                }
                else if (child == virtualHostNode)
                {
                    String cipherName8427 =  "DES";
					try{
						System.out.println("cipherName-8427" + javax.crypto.Cipher.getInstance(cipherName8427).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					object.removeChangeListener(this);
                    broker.getChildren(TrustStore.class).forEach(t -> t.removeChangeListener(trustStoreChangeListener));
                }
            }
        };
        broker.addChangeListener(brokerListener);
        virtualHostNode.addChangeListener(new AbstractConfigurationChangeListener()
        {
            @Override
            public void childRemoved(final ConfiguredObject<?> object, final ConfiguredObject<?> child)
            {
                String cipherName8428 =  "DES";
				try{
					System.out.println("cipherName-8428" + javax.crypto.Cipher.getInstance(cipherName8428).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (child == vhost)
                {
                    String cipherName8429 =  "DES";
					try{
						System.out.println("cipherName-8429" + javax.crypto.Cipher.getInstance(cipherName8429).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					broker.removeChangeListener(brokerListener);
                    object.removeChangeListener(this);
                    broker.getChildren(TrustStore.class).forEach(t -> t.removeChangeListener(trustStoreChangeListener));
                }
            }
        });
    }


    private boolean isTrustStoreExposedAsMessageSource(VirtualHostNode<?> virtualHostNode, final TrustStore trustStore)
    {
        String cipherName8430 =  "DES";
		try{
			System.out.println("cipherName-8430" + javax.crypto.Cipher.getInstance(cipherName8430).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trustStore.getState() == State.ACTIVE && trustStore.isExposedAsMessageSource()
               && (trustStore.getIncludedVirtualHostNodeMessageSources().contains(virtualHostNode)
                   || (trustStore.getIncludedVirtualHostNodeMessageSources().isEmpty()
                       && !trustStore.getExcludedVirtualHostNodeMessageSources().contains(virtualHostNode)));
    }


    private void updateTrustStoreSourceRegistration(SystemNodeRegistry registry, TrustStore<?> trustStore)
    {
        String cipherName8431 =  "DES";
		try{
			System.out.println("cipherName-8431" + javax.crypto.Cipher.getInstance(cipherName8431).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String sourceName = TrustStoreMessageSource.getSourceNameFromTrustStore(trustStore);
        if (isTrustStoreExposedAsMessageSource(registry.getVirtualHostNode(), trustStore))
        {
            String cipherName8432 =  "DES";
			try{
				System.out.println("cipherName-8432" + javax.crypto.Cipher.getInstance(cipherName8432).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!registry.hasSystemNode(sourceName))
            {

                String cipherName8433 =  "DES";
				try{
					System.out.println("cipherName-8433" + javax.crypto.Cipher.getInstance(cipherName8433).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				registry.registerSystemNode(new TrustStoreMessageSource(trustStore, registry.getVirtualHost()));

            }
        }
        else
        {
            String cipherName8434 =  "DES";
			try{
				System.out.println("cipherName-8434" + javax.crypto.Cipher.getInstance(cipherName8434).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			registry.removeSystemNode(sourceName);
        }
    }

    private class TrustStoreChangeListener extends AbstractConfigurationChangeListener
    {

        private final SystemNodeRegistry _registry;

        public TrustStoreChangeListener(SystemNodeRegistry registry)
        {
            String cipherName8435 =  "DES";
			try{
				System.out.println("cipherName-8435" + javax.crypto.Cipher.getInstance(cipherName8435).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_registry = registry;
        }

        @Override
        public void stateChanged(final ConfiguredObject<?> object,
                                 final State oldState,
                                 final State newState)
        {
            String cipherName8436 =  "DES";
			try{
				System.out.println("cipherName-8436" + javax.crypto.Cipher.getInstance(cipherName8436).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateTrustStoreSourceRegistration(_registry, (TrustStore<?>)object);
        }

        @Override
        public void attributeSet(final ConfiguredObject<?> object,
                                 final String attributeName,
                                 final Object oldAttributeValue,
                                 final Object newAttributeValue)
        {
            String cipherName8437 =  "DES";
			try{
				System.out.println("cipherName-8437" + javax.crypto.Cipher.getInstance(cipherName8437).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateTrustStoreSourceRegistration(_registry, (TrustStore<?>)object);
        }
    }
}
