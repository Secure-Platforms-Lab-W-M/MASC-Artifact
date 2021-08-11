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

package org.apache.qpid.server.stats;

import static org.apache.qpid.server.model.ConfiguredObjectTypeRegistry.returnsCollectionOfConfiguredObjects;

import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.ConfiguredObjectOperation;
import org.apache.qpid.server.model.ConfiguredObjectTypeRegistry;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.util.Strings;

public class StatisticsReportingTask extends TimerTask
{
    private final ConfiguredObject<?> _root;
    private final Subject _subject;
    private final ConfiguredObjectTypeRegistry _typeRegistry;
    private final Map<Class<? extends ConfiguredObject>, Set<ConfiguredObjectOperation<?>>> _associatedOperations = new HashMap<>();

    public StatisticsReportingTask(final ConfiguredObject<?> root, final Subject subject)
    {
        String cipherName16670 =  "DES";
		try{
			System.out.println("cipherName-16670" + javax.crypto.Cipher.getInstance(cipherName16670).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_root = root;
        _typeRegistry = root.getModel().getTypeRegistry();
        _subject = subject;
    }

    @Override
    public void run()
    {
        String cipherName16671 =  "DES";
		try{
			System.out.println("cipherName-16671" + javax.crypto.Cipher.getInstance(cipherName16671).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Subject.doAs(_subject, (PrivilegedAction<Object>) () -> {
            String cipherName16672 =  "DES";
			try{
				System.out.println("cipherName-16672" + javax.crypto.Cipher.getInstance(cipherName16672).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			processChild(_root);
            return null;
        });
    }

    private void processChild(final ConfiguredObject<?> child)
    {
        String cipherName16673 =  "DES";
		try{
			System.out.println("cipherName-16673" + javax.crypto.Cipher.getInstance(cipherName16673).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		reportStatisticsForObject(child);

        if (!child.getCategoryClass().getAnnotation(ManagedObject.class).managesChildren())
        {
            String cipherName16674 =  "DES";
			try{
				System.out.println("cipherName-16674" + javax.crypto.Cipher.getInstance(cipherName16674).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			applyRecursively(child);
        }
    }

    private void applyRecursively(final ConfiguredObject<?> object)
    {
        String cipherName16675 =  "DES";
		try{
			System.out.println("cipherName-16675" + javax.crypto.Cipher.getInstance(cipherName16675).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collection<Class<? extends ConfiguredObject>> childTypes = object.getModel().getChildTypes(object.getCategoryClass());
        childTypes.forEach(childClass -> {
            String cipherName16676 =  "DES";
			try{
				System.out.println("cipherName-16676" + javax.crypto.Cipher.getInstance(cipherName16676).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Collection<? extends ConfiguredObject> children = object.getChildren(childClass);
            if (!children.isEmpty())
            {
                String cipherName16677 =  "DES";
				try{
					System.out.println("cipherName-16677" + javax.crypto.Cipher.getInstance(cipherName16677).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				children.forEach(this::processChild);
            }
        });
        processAssociations(object);
    }

    private void processAssociations(final ConfiguredObject<?> object)
    {
        String cipherName16678 =  "DES";
		try{
			System.out.println("cipherName-16678" + javax.crypto.Cipher.getInstance(cipherName16678).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_associatedOperations.computeIfAbsent(object.getTypeClass(), aClass -> new HashSet<>(_typeRegistry.getOperations(object.getTypeClass(),
                                                                                                           operation -> operation.isAssociateAsIfChildren()
                                                                                                                        && returnsCollectionOfConfiguredObjects(
                                                                                                                   operation)).values()));
        for(ConfiguredObjectOperation<?> operation : _associatedOperations.get(object.getTypeClass()))
        {
            String cipherName16679 =  "DES";
			try{
				System.out.println("cipherName-16679" + javax.crypto.Cipher.getInstance(cipherName16679).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			@SuppressWarnings("unchecked")
            ConfiguredObjectOperation<ConfiguredObject<?>> configuredObjectOperation = (ConfiguredObjectOperation<ConfiguredObject<?>>) operation;

            @SuppressWarnings("unchecked")
            Collection<? extends ConfiguredObject<?>> associatedChildren =
                    (Collection<? extends ConfiguredObject<?>>) configuredObjectOperation.perform(object, Collections.emptyMap());
            if (associatedChildren != null && !associatedChildren.isEmpty())
            {
                String cipherName16680 =  "DES";
				try{
					System.out.println("cipherName-16680" + javax.crypto.Cipher.getInstance(cipherName16680).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				associatedChildren.forEach(this::processChild);
            }
        }
    }

    private void reportStatisticsForObject(final ConfiguredObject<?> object)
    {
        String cipherName16681 =  "DES";
		try{
			System.out.println("cipherName-16681" + javax.crypto.Cipher.getInstance(cipherName16681).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String statisticsReportPatternContextKey =
                String.format("qpid.%s.statisticsReportPattern",
                              object.getCategoryClass().getSimpleName().toLowerCase());

        if (object.getContextKeys(false).contains(statisticsReportPatternContextKey))
        {
            String cipherName16682 =  "DES";
			try{
				System.out.println("cipherName-16682" + javax.crypto.Cipher.getInstance(cipherName16682).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String reportPattern = object.getContextValue(String.class, statisticsReportPatternContextKey);
            String formattedStatistics = Strings.expand(reportPattern, false, new FormattingStatisticsResolver(object));

            String loggerName = String.format("qpid.statistics.%s", object.getCategoryClass().getSimpleName());
            Logger logger = LoggerFactory.getLogger(loggerName);

            logger.info("Statistics: {}", formattedStatistics);
        }
    }
}
