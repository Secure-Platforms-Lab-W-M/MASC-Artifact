/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModelLoaderRegistry {
    private final ModelLoaderCache cache = new ModelLoaderCache();
    private final MultiModelLoaderFactory multiModelLoaderFactory;

    public ModelLoaderRegistry(Pools.Pool<List<Throwable>> pool) {
        this(new MultiModelLoaderFactory(pool));
    }

    private ModelLoaderRegistry(MultiModelLoaderFactory multiModelLoaderFactory) {
        this.multiModelLoaderFactory = multiModelLoaderFactory;
    }

    private static <A> Class<A> getClass(A a) {
        return a.getClass();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private <A> List<ModelLoader<A, ?>> getModelLoadersForClass(Class<A> class_) {
        synchronized (this) {
            List list;
            List list2 = list = this.cache.get(class_);
            if (list == null) {
                list2 = Collections.unmodifiableList(this.multiModelLoaderFactory.build(class_));
                this.cache.put(class_, list2);
            }
            return list2;
        }
    }

    private <Model, Data> void tearDown(List<ModelLoaderFactory<? extends Model, ? extends Data>> object) {
        object = object.iterator();
        while (object.hasNext()) {
            ((ModelLoaderFactory)object.next()).teardown();
        }
    }

    public <Model, Data> void append(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.multiModelLoaderFactory.append(class_, class_2, modelLoaderFactory);
            this.cache.clear();
            return;
        }
    }

    public <Model, Data> ModelLoader<Model, Data> build(Class<Model> object, Class<Data> class_) {
        synchronized (this) {
            object = this.multiModelLoaderFactory.build(object, class_);
            return object;
        }
    }

    public List<Class<?>> getDataClasses(Class<?> object) {
        synchronized (this) {
            object = this.multiModelLoaderFactory.getDataClasses(object);
            return object;
        }
    }

    public <A> List<ModelLoader<A, ?>> getModelLoaders(A a) {
        List list = this.getModelLoadersForClass(ModelLoaderRegistry.getClass(a));
        if (!list.isEmpty()) {
            int n = list.size();
            boolean bl = true;
            List list2 = Collections.emptyList();
            for (int i = 0; i < n; ++i) {
                ModelLoader modelLoader = list.get(i);
                boolean bl2 = bl;
                List list3 = list2;
                if (modelLoader.handles(a)) {
                    bl2 = bl;
                    if (bl) {
                        list2 = new ArrayList(n - i);
                        bl2 = false;
                    }
                    list2.add(modelLoader);
                    list3 = list2;
                }
                bl = bl2;
                list2 = list3;
            }
            if (!list2.isEmpty()) {
                return list2;
            }
            throw new Registry.NoModelLoaderAvailableException(a, list);
        }
        throw new Registry.NoModelLoaderAvailableException(a);
    }

    public <Model, Data> void prepend(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.multiModelLoaderFactory.prepend(class_, class_2, modelLoaderFactory);
            this.cache.clear();
            return;
        }
    }

    public <Model, Data> void remove(Class<Model> class_, Class<Data> class_2) {
        synchronized (this) {
            this.tearDown(this.multiModelLoaderFactory.remove(class_, class_2));
            this.cache.clear();
            return;
        }
    }

    public <Model, Data> void replace(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        synchronized (this) {
            this.tearDown(this.multiModelLoaderFactory.replace(class_, class_2, modelLoaderFactory));
            this.cache.clear();
            return;
        }
    }

    private static class ModelLoaderCache {
        private final Map<Class<?>, Entry<?>> cachedModelLoaders = new HashMap();

        ModelLoaderCache() {
        }

        public void clear() {
            this.cachedModelLoaders.clear();
        }

        public <Model> List<ModelLoader<Model, ?>> get(Class<Model> object) {
            if ((object = this.cachedModelLoaders.get(object)) == null) {
                return null;
            }
            return object.loaders;
        }

        public <Model> void put(Class<Model> class_, List<ModelLoader<Model, ?>> object) {
            if (this.cachedModelLoaders.put(class_, new Entry<Model>(object)) == null) {
                return;
            }
            object = new StringBuilder();
            object.append("Already cached loaders for model: ");
            object.append(class_);
            throw new IllegalStateException(object.toString());
        }

        private static class Entry<Model> {
            final List<ModelLoader<Model, ?>> loaders;

            public Entry(List<ModelLoader<Model, ?>> list) {
                this.loaders = list;
            }
        }

    }

}

