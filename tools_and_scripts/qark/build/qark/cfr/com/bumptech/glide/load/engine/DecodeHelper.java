/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine;

import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.UnitTransformation;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class DecodeHelper<Transcode> {
    private final List<Key> cacheKeys = new ArrayList<Key>();
    private DecodeJob.DiskCacheProvider diskCacheProvider;
    private DiskCacheStrategy diskCacheStrategy;
    private GlideContext glideContext;
    private int height;
    private boolean isCacheKeysSet;
    private boolean isLoadDataSet;
    private boolean isScaleOnlyOrNoTransform;
    private boolean isTransformationRequired;
    private final List<ModelLoader.LoadData<?>> loadData = new ArrayList();
    private Object model;
    private Options options;
    private Priority priority;
    private Class<?> resourceClass;
    private Key signature;
    private Class<Transcode> transcodeClass;
    private Map<Class<?>, Transformation<?>> transformations;
    private int width;

    DecodeHelper() {
    }

    void clear() {
        this.glideContext = null;
        this.model = null;
        this.signature = null;
        this.resourceClass = null;
        this.transcodeClass = null;
        this.options = null;
        this.priority = null;
        this.transformations = null;
        this.diskCacheStrategy = null;
        this.loadData.clear();
        this.isLoadDataSet = false;
        this.cacheKeys.clear();
        this.isCacheKeysSet = false;
    }

    ArrayPool getArrayPool() {
        return this.glideContext.getArrayPool();
    }

    List<Key> getCacheKeys() {
        if (!this.isCacheKeysSet) {
            this.isCacheKeysSet = true;
            this.cacheKeys.clear();
            List list = this.getLoadData();
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                ModelLoader.LoadData loadData = list.get(i);
                if (!this.cacheKeys.contains(loadData.sourceKey)) {
                    this.cacheKeys.add(loadData.sourceKey);
                }
                for (int j = 0; j < loadData.alternateKeys.size(); ++j) {
                    if (this.cacheKeys.contains(loadData.alternateKeys.get(j))) continue;
                    this.cacheKeys.add(loadData.alternateKeys.get(j));
                }
            }
        }
        return this.cacheKeys;
    }

    DiskCache getDiskCache() {
        return this.diskCacheProvider.getDiskCache();
    }

    DiskCacheStrategy getDiskCacheStrategy() {
        return this.diskCacheStrategy;
    }

    int getHeight() {
        return this.height;
    }

    List<ModelLoader.LoadData<?>> getLoadData() {
        if (!this.isLoadDataSet) {
            this.isLoadDataSet = true;
            this.loadData.clear();
            List list = this.glideContext.getRegistry().getModelLoaders(this.model);
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                ModelLoader.LoadData loadData = list.get(i).buildLoadData(this.model, this.width, this.height, this.options);
                if (loadData == null) continue;
                this.loadData.add(loadData);
            }
        }
        return this.loadData;
    }

    <Data> LoadPath<Data, ?, Transcode> getLoadPath(Class<Data> class_) {
        return this.glideContext.getRegistry().getLoadPath(class_, this.resourceClass, this.transcodeClass);
    }

    Class<?> getModelClass() {
        return this.model.getClass();
    }

    List<ModelLoader<File, ?>> getModelLoaders(File file) throws Registry.NoModelLoaderAvailableException {
        return this.glideContext.getRegistry().getModelLoaders(file);
    }

    Options getOptions() {
        return this.options;
    }

    Priority getPriority() {
        return this.priority;
    }

    List<Class<?>> getRegisteredResourceClasses() {
        return this.glideContext.getRegistry().getRegisteredResourceClasses(this.model.getClass(), this.resourceClass, this.transcodeClass);
    }

    <Z> ResourceEncoder<Z> getResultEncoder(Resource<Z> resource) {
        return this.glideContext.getRegistry().getResultEncoder(resource);
    }

    Key getSignature() {
        return this.signature;
    }

    <X> Encoder<X> getSourceEncoder(X x) throws Registry.NoSourceEncoderAvailableException {
        return this.glideContext.getRegistry().getSourceEncoder(x);
    }

    Class<?> getTranscodeClass() {
        return this.transcodeClass;
    }

    <Z> Transformation<Z> getTransformation(Class<Z> class_) {
        Object object;
        block4 : {
            Transformation transformation = this.transformations.get(class_);
            object = transformation;
            if (transformation == null) {
                Iterator iterator = this.transformations.entrySet().iterator();
                do {
                    object = transformation;
                    if (!iterator.hasNext()) break block4;
                } while (!(object = iterator.next()).getKey().isAssignableFrom(class_));
                object = object.getValue();
            }
        }
        if (object == null) {
            if (this.transformations.isEmpty() && this.isTransformationRequired) {
                object = new StringBuilder();
                object.append("Missing transformation for ");
                object.append(class_);
                object.append(". If you wish to ignore unknown resource types, use the optional transformation methods.");
                throw new IllegalArgumentException(object.toString());
            }
            return UnitTransformation.get();
        }
        return object;
    }

    int getWidth() {
        return this.width;
    }

    boolean hasLoadPath(Class<?> class_) {
        if (this.getLoadPath(class_) != null) {
            return true;
        }
        return false;
    }

    <R> void init(GlideContext glideContext, Object object, Key key, int n, int n2, DiskCacheStrategy diskCacheStrategy, Class<?> class_, Class<R> class_2, Priority priority, Options options, Map<Class<?>, Transformation<?>> map, boolean bl, boolean bl2, DecodeJob.DiskCacheProvider diskCacheProvider) {
        this.glideContext = glideContext;
        this.model = object;
        this.signature = key;
        this.width = n;
        this.height = n2;
        this.diskCacheStrategy = diskCacheStrategy;
        this.resourceClass = class_;
        this.diskCacheProvider = diskCacheProvider;
        this.transcodeClass = class_2;
        this.priority = priority;
        this.options = options;
        this.transformations = map;
        this.isTransformationRequired = bl;
        this.isScaleOnlyOrNoTransform = bl2;
    }

    boolean isResourceEncoderAvailable(Resource<?> resource) {
        return this.glideContext.getRegistry().isResourceEncoderAvailable(resource);
    }

    boolean isScaleOnlyOrNoTransform() {
        return this.isScaleOnlyOrNoTransform;
    }

    boolean isSourceKey(Key key) {
        List list = this.getLoadData();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            if (!list.get((int)i).sourceKey.equals(key)) continue;
            return true;
        }
        return false;
    }
}

