/*
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
 */

package org.apache.qpid.server.store.preferences;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.apache.qpid.server.model.preferences.Preference;

public class PreferenceRecordImpl implements PreferenceRecord
{
    final private Map<String, Object> _attributes;
    final private UUID _id;

    static public PreferenceRecord fromPreference(Preference preference)
    {
        return new PreferenceRecordImpl(preference.getId(), preference.getAttributes());
    }

    public PreferenceRecordImpl(final UUID id, final Map<String, Object> attributes)
    {
        _id = id;
        _attributes = attributes;
    }

    @Override
    public UUID getId()
    {
        return _id;
    }

    @Override
    public Map<String, Object> getAttributes()
    {
        return Collections.unmodifiableMap(_attributes);
    }
}
