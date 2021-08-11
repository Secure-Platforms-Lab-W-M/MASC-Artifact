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
package org.apache.qpid.server.configuration.store;

import java.util.Collection;

import org.apache.qpid.server.model.ConfigurationChangeListener;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.model.Model;
import org.apache.qpid.server.model.State;
import org.apache.qpid.server.store.DurableConfigurationStore;

public class StoreConfigurationChangeListener implements ConfigurationChangeListener
{
    private final DurableConfigurationStore _store;
    private boolean _bulkChanges = false;

    public StoreConfigurationChangeListener(DurableConfigurationStore store)
    {
        super();
        _store = store;
    }

    @Override
    public void stateChanged(ConfiguredObject object, State oldState, State newState)
    {
        if (newState == State.DELETED)
        {
            if(object.isDurable())
            {
                _store.remove(object.asObjectRecord());
            }
            object.removeChangeListener(this);
        }
    }

    @Override
    public void childAdded(ConfiguredObject<?> object, ConfiguredObject<?> child)
    {
        if (!object.managesChildStorage())
        {
            if(object.isDurable() && child.isDurable())
            {
                Model model = child.getModel();
                Class<? extends ConfiguredObject> parentType =
                        model.getParentType(child.getCategoryClass());
                if(parentType.equals(object.getCategoryClass()))
                {
                    child.addChangeListener(this);
                    _store.update(true, child.asObjectRecord());

                    Class<? extends ConfiguredObject> categoryClass = child.getCategoryClass();
                    Collection<Class<? extends ConfiguredObject>> childTypes =
                            model.getChildTypes(categoryClass);

                    for (Class<? extends ConfiguredObject> childClass : childTypes)
                    {
                        for (ConfiguredObject<?> grandchild : child.getChildren(childClass))
                        {
                            childAdded(child, grandchild);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void bulkChangeStart(final ConfiguredObject<?> object)
    {
        _bulkChanges = true;
    }

    @Override
    public void bulkChangeEnd(final ConfiguredObject<?> object)
    {
        if (object.isDurable() && _bulkChanges)
        {
            _store.update(false, object.asObjectRecord());
        }
        _bulkChanges = false;
    }

    @Override
    public void childRemoved(ConfiguredObject object, ConfiguredObject child)
    {
        if (!object.managesChildStorage())
        {
            if (child.isDurable())
            {
                _store.remove(child.asObjectRecord());
            }
            child.removeChangeListener(this);
        }
    }

    @Override
    public void attributeSet(ConfiguredObject object, String attributeName, Object oldAttributeValue, Object newAttributeValue)
    {
        if (object.isDurable() && !_bulkChanges)
        {
            _store.update(false, object.asObjectRecord());
        }
    }

    @Override
    public String toString()
    {
        return "StoreConfigurationChangeListener [store=" + _store + "]";
    }
}
