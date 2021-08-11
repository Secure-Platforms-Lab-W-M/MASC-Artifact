/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.data;

import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.util.Preconditions;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataRewinderRegistry {
    private static final DataRewinder.Factory<?> DEFAULT_FACTORY = new DataRewinder.Factory<Object>(){

        @Override
        public DataRewinder<Object> build(Object object) {
            return new DefaultRewinder(object);
        }

        @Override
        public Class<Object> getDataClass() {
            throw new UnsupportedOperationException("Not implemented");
        }
    };
    private final Map<Class<?>, DataRewinder.Factory<?>> rewinders = new HashMap();

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public <T> DataRewinder<T> build(T dataRewinder) {
        synchronized (this) {
            DataRewinder.Factory factory;
            DataRewinder.Factory factory2;
            block7 : {
                Preconditions.checkNotNull(dataRewinder);
                factory2 = factory = this.rewinders.get(dataRewinder.getClass());
                if (factory != null) break block7;
                Iterator iterator = this.rewinders.values().iterator();
                do {
                    factory2 = factory;
                    if (iterator.hasNext() && !(factory2 = iterator.next()).getDataClass().isAssignableFrom(dataRewinder.getClass())) continue;
                    break;
                } while (true);
            }
            factory = factory2;
            if (factory2 != null) return factory.build(dataRewinder);
            factory = DEFAULT_FACTORY;
            return factory.build(dataRewinder);
        }
    }

    public void register(DataRewinder.Factory<?> factory) {
        synchronized (this) {
            this.rewinders.put(factory.getDataClass(), factory);
            return;
        }
    }

    private static final class DefaultRewinder
    implements DataRewinder<Object> {
        private final Object data;

        DefaultRewinder(Object object) {
            this.data = object;
        }

        @Override
        public void cleanup() {
        }

        @Override
        public Object rewindAndGet() {
            return this.data;
        }
    }

}

