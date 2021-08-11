/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoader;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MultiModelLoaderFactory {
    private static final Factory DEFAULT_FACTORY = new Factory();
    private static final ModelLoader<Object, Object> EMPTY_MODEL_LOADER = new EmptyModelLoader();
    private final Set<Entry<?, ?>> alreadyUsedEntries = new HashSet();
    private final List<Entry<?, ?>> entries = new ArrayList();
    private final Factory factory;
    private final Pools.Pool<List<Throwable>> throwableListPool;

    public MultiModelLoaderFactory(Pools.Pool<List<Throwable>> pool) {
        this(pool, DEFAULT_FACTORY);
    }

    MultiModelLoaderFactory(Pools.Pool<List<Throwable>> pool, Factory factory) {
        this.throwableListPool = pool;
        this.factory = factory;
    }

    private <Model, Data> void add(Class<Model> object, Class<Data> object2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory, boolean bl) {
        object = new Entry<Model, Data>((Class<? extends Model>)object, (Class<? extends Data>)object2, modelLoaderFactory);
        object2 = this.entries;
        int n = bl ? object2.size() : 0;
        object2.add(n, object);
    }

    private <Model, Data> ModelLoader<Model, Data> build(Entry<?, ?> entry) {
        return Preconditions.checkNotNull(entry.factory.build(this));
    }

    private static <Model, Data> ModelLoader<Model, Data> emptyModelLoader() {
        return EMPTY_MODEL_LOADER;
    }

    private <Model, Data> ModelLoaderFactory<Model, Data> getFactory(Entry<?, ?> entry) {
        return entry.factory;
    }

    <Model, Data> void append(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.add(class_, class_2, modelLoaderFactory, true);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public <Model, Data> ModelLoader<Model, Data> build(Class<Model> modelLoader, Class<Data> class_) {
        synchronized (this) {
            try {
                void var2_3;
                ArrayList arrayList = new ArrayList();
                boolean bl = false;
                for (Entry entry : this.entries) {
                    if (this.alreadyUsedEntries.contains(entry)) {
                        bl = true;
                        continue;
                    }
                    if (!entry.handles(modelLoader, var2_3)) continue;
                    this.alreadyUsedEntries.add(entry);
                    arrayList.add(this.build(entry));
                    this.alreadyUsedEntries.remove(entry);
                }
                if (arrayList.size() > 1) {
                    return this.factory.build(arrayList, this.throwableListPool);
                }
                if (arrayList.size() == 1) {
                    return arrayList.get(0);
                }
                if (!bl) throw new Registry.NoModelLoaderAvailableException(modelLoader, var2_3);
                return MultiModelLoaderFactory.emptyModelLoader();
            }
            catch (Throwable throwable) {
                this.alreadyUsedEntries.clear();
                throw throwable;
            }
        }
    }

    <Model> List<ModelLoader<Model, ?>> build(Class<Model> class_) {
        synchronized (this) {
            try {
                ArrayList arrayList = new ArrayList();
                for (Entry<Model, Model> entry : this.entries) {
                    if (this.alreadyUsedEntries.contains(entry) || !entry.handles(class_)) continue;
                    this.alreadyUsedEntries.add(entry);
                    arrayList.add(this.build(entry));
                    this.alreadyUsedEntries.remove(entry);
                }
                return arrayList;
            }
            catch (Throwable throwable) {
                this.alreadyUsedEntries.clear();
                throw throwable;
            }
        }
    }

    List<Class<?>> getDataClasses(Class<?> class_) {
        synchronized (this) {
            ArrayList arrayList = new ArrayList();
            for (Entry entry : this.entries) {
                if (arrayList.contains(entry.dataClass) || !entry.handles(class_)) continue;
                arrayList.add(entry.dataClass);
            }
            return arrayList;
        }
    }

    <Model, Data> void prepend(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.add(class_, class_2, modelLoaderFactory, false);
            return;
        }
    }

    <Model, Data> List<ModelLoaderFactory<? extends Model, ? extends Data>> remove(Class<Model> class_, Class<Data> class_2) {
        synchronized (this) {
            ArrayList<ModelLoaderFactory<Model, Data>> arrayList = new ArrayList<ModelLoaderFactory<Model, Data>>();
            Iterator iterator = this.entries.iterator();
            while (iterator.hasNext()) {
                Entry<Model, Model> entry = iterator.next();
                if (!entry.handles(class_, class_2)) continue;
                iterator.remove();
                arrayList.add(this.getFactory(entry));
            }
            return arrayList;
        }
    }

    <Model, Data> List<ModelLoaderFactory<? extends Model, ? extends Data>> replace(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            List<ModelLoaderFactory<? extends Model, ? extends Data>> list = this.remove(class_, class_2);
            this.append(class_, class_2, modelLoaderFactory);
            return list;
        }
    }

    private static class EmptyModelLoader
    implements ModelLoader<Object, Object> {
        EmptyModelLoader() {
        }

        @Override
        public ModelLoader.LoadData<Object> buildLoadData(Object object, int n, int n2, Options options) {
            return null;
        }

        @Override
        public boolean handles(Object object) {
            return false;
        }
    }

    private static class Entry<Model, Data> {
        final Class<Data> dataClass;
        final ModelLoaderFactory<? extends Model, ? extends Data> factory;
        private final Class<Model> modelClass;

        public Entry(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
            this.modelClass = class_;
            this.dataClass = class_2;
            this.factory = modelLoaderFactory;
        }

        public boolean handles(Class<?> class_) {
            return this.modelClass.isAssignableFrom(class_);
        }

        public boolean handles(Class<?> class_, Class<?> class_2) {
            if (this.handles(class_) && this.dataClass.isAssignableFrom(class_2)) {
                return true;
            }
            return false;
        }
    }

    static class Factory {
        Factory() {
        }

        public <Model, Data> MultiModelLoader<Model, Data> build(List<ModelLoader<Model, Data>> list, Pools.Pool<List<Throwable>> pool) {
            return new MultiModelLoader<Model, Data>(list, pool);
        }
    }

}

