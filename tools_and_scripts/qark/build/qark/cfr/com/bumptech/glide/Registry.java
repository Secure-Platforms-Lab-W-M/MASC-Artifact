/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.DataRewinderRegistry;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderRegistry;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.provider.EncoderRegistry;
import com.bumptech.glide.provider.ImageHeaderParserRegistry;
import com.bumptech.glide.provider.LoadPathCache;
import com.bumptech.glide.provider.ModelToResourceClassCache;
import com.bumptech.glide.provider.ResourceDecoderRegistry;
import com.bumptech.glide.provider.ResourceEncoderRegistry;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Registry {
    private static final String BUCKET_APPEND_ALL = "legacy_append";
    public static final String BUCKET_BITMAP = "Bitmap";
    public static final String BUCKET_BITMAP_DRAWABLE = "BitmapDrawable";
    public static final String BUCKET_GIF = "Gif";
    private static final String BUCKET_PREPEND_ALL = "legacy_prepend_all";
    private final DataRewinderRegistry dataRewinderRegistry;
    private final ResourceDecoderRegistry decoderRegistry;
    private final EncoderRegistry encoderRegistry;
    private final ImageHeaderParserRegistry imageHeaderParserRegistry;
    private final LoadPathCache loadPathCache = new LoadPathCache();
    private final ModelLoaderRegistry modelLoaderRegistry;
    private final ModelToResourceClassCache modelToResourceClassCache = new ModelToResourceClassCache();
    private final ResourceEncoderRegistry resourceEncoderRegistry;
    private final Pools.Pool<List<Throwable>> throwableListPool;
    private final TranscoderRegistry transcoderRegistry;

    public Registry() {
        Pools.Pool<List<Throwable>> pool = FactoryPools.threadSafeList();
        this.throwableListPool = pool;
        this.modelLoaderRegistry = new ModelLoaderRegistry(pool);
        this.encoderRegistry = new EncoderRegistry();
        this.decoderRegistry = new ResourceDecoderRegistry();
        this.resourceEncoderRegistry = new ResourceEncoderRegistry();
        this.dataRewinderRegistry = new DataRewinderRegistry();
        this.transcoderRegistry = new TranscoderRegistry();
        this.imageHeaderParserRegistry = new ImageHeaderParserRegistry();
        this.setResourceDecoderBucketPriorityList(Arrays.asList("Gif", "Bitmap", "BitmapDrawable"));
    }

    private <Data, TResource, Transcode> List<DecodePath<Data, TResource, Transcode>> getDecodePaths(Class<Data> class_, Class<TResource> object, Class<Transcode> class_2) {
        ArrayList<DecodePath<Data, TResource, Transcode>> arrayList = new ArrayList<DecodePath<Data, TResource, Transcode>>();
        for (Class class_3 : this.decoderRegistry.getResourceClasses(class_, object)) {
            for (Class<Transcode> class_4 : this.transcoderRegistry.getTranscodeClasses(class_3, class_2)) {
                arrayList.add(new DecodePath(class_, class_3, class_4, this.decoderRegistry.getDecoders(class_, class_3), this.transcoderRegistry.get(class_3, class_4), this.throwableListPool));
            }
        }
        return arrayList;
    }

    public <Data> Registry append(Class<Data> class_, Encoder<Data> encoder) {
        this.encoderRegistry.append(class_, encoder);
        return this;
    }

    public <TResource> Registry append(Class<TResource> class_, ResourceEncoder<TResource> resourceEncoder) {
        this.resourceEncoderRegistry.append(class_, resourceEncoder);
        return this;
    }

    public <Data, TResource> Registry append(Class<Data> class_, Class<TResource> class_2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.append("legacy_append", class_, class_2, resourceDecoder);
        return this;
    }

    public <Model, Data> Registry append(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.modelLoaderRegistry.append(class_, class_2, modelLoaderFactory);
        return this;
    }

    public <Data, TResource> Registry append(String string2, Class<Data> class_, Class<TResource> class_2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.decoderRegistry.append(string2, resourceDecoder, class_, class_2);
        return this;
    }

    public List<ImageHeaderParser> getImageHeaderParsers() {
        List<ImageHeaderParser> list = this.imageHeaderParserRegistry.getParsers();
        if (!list.isEmpty()) {
            return list;
        }
        throw new NoImageHeaderParserException();
    }

    public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> getLoadPath(Class<Data> class_, Class<TResource> class_2, Class<Transcode> class_3) {
        LoadPath<Data, TResource, Transcode> loadPath = this.loadPathCache.get(class_, class_2, class_3);
        if (this.loadPathCache.isEmptyLoadPath(loadPath)) {
            return null;
        }
        Object object = loadPath;
        if (loadPath == null) {
            object = this.getDecodePaths(class_, class_2, class_3);
            object = object.isEmpty() ? null : new LoadPath<Data, TResource, Transcode>(class_, class_2, class_3, (List<DecodePath<Data, TResource, Transcode>>)object, this.throwableListPool);
            this.loadPathCache.put(class_, class_2, class_3, object);
        }
        return object;
    }

    public <Model> List<ModelLoader<Model, ?>> getModelLoaders(Model Model2) {
        return this.modelLoaderRegistry.getModelLoaders(Model2);
    }

    public <Model, TResource, Transcode> List<Class<?>> getRegisteredResourceClasses(Class<Model> class_, Class<TResource> class_2, Class<Transcode> class_3) {
        Object object = this.modelToResourceClassCache.get(class_, class_2, class_3);
        List list = object;
        if (object == null) {
            list = new ArrayList();
            object = this.modelLoaderRegistry.getDataClasses(class_).iterator();
            while (object.hasNext()) {
                Class class_4 = (Class)object.next();
                for (Class class_5 : this.decoderRegistry.getResourceClasses(class_4, class_2)) {
                    if (this.transcoderRegistry.getTranscodeClasses(class_5, class_3).isEmpty() || list.contains(class_5)) continue;
                    list.add(class_5);
                }
            }
            this.modelToResourceClassCache.put(class_, class_2, class_3, Collections.unmodifiableList(list));
        }
        return list;
    }

    public <X> ResourceEncoder<X> getResultEncoder(Resource<X> resource) throws NoResultEncoderAvailableException {
        ResourceEncoder<X> resourceEncoder = this.resourceEncoderRegistry.get(resource.getResourceClass());
        if (resourceEncoder != null) {
            return resourceEncoder;
        }
        throw new NoResultEncoderAvailableException(resource.getResourceClass());
    }

    public <X> DataRewinder<X> getRewinder(X x) {
        return this.dataRewinderRegistry.build(x);
    }

    public <X> Encoder<X> getSourceEncoder(X x) throws NoSourceEncoderAvailableException {
        Encoder encoder = this.encoderRegistry.getEncoder(x.getClass());
        if (encoder != null) {
            return encoder;
        }
        throw new NoSourceEncoderAvailableException(x.getClass());
    }

    public boolean isResourceEncoderAvailable(Resource<?> resource) {
        if (this.resourceEncoderRegistry.get(resource.getResourceClass()) != null) {
            return true;
        }
        return false;
    }

    public <Data> Registry prepend(Class<Data> class_, Encoder<Data> encoder) {
        this.encoderRegistry.prepend(class_, encoder);
        return this;
    }

    public <TResource> Registry prepend(Class<TResource> class_, ResourceEncoder<TResource> resourceEncoder) {
        this.resourceEncoderRegistry.prepend(class_, resourceEncoder);
        return this;
    }

    public <Data, TResource> Registry prepend(Class<Data> class_, Class<TResource> class_2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.prepend("legacy_prepend_all", class_, class_2, resourceDecoder);
        return this;
    }

    public <Model, Data> Registry prepend(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.modelLoaderRegistry.prepend(class_, class_2, modelLoaderFactory);
        return this;
    }

    public <Data, TResource> Registry prepend(String string2, Class<Data> class_, Class<TResource> class_2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.decoderRegistry.prepend(string2, resourceDecoder, class_, class_2);
        return this;
    }

    public Registry register(ImageHeaderParser imageHeaderParser) {
        this.imageHeaderParserRegistry.add(imageHeaderParser);
        return this;
    }

    public Registry register(DataRewinder.Factory<?> factory) {
        this.dataRewinderRegistry.register(factory);
        return this;
    }

    @Deprecated
    public <Data> Registry register(Class<Data> class_, Encoder<Data> encoder) {
        return this.append(class_, encoder);
    }

    @Deprecated
    public <TResource> Registry register(Class<TResource> class_, ResourceEncoder<TResource> resourceEncoder) {
        return this.append(class_, resourceEncoder);
    }

    public <TResource, Transcode> Registry register(Class<TResource> class_, Class<Transcode> class_2, ResourceTranscoder<TResource, Transcode> resourceTranscoder) {
        this.transcoderRegistry.register(class_, class_2, resourceTranscoder);
        return this;
    }

    public <Model, Data> Registry replace(Class<Model> class_, Class<Data> class_2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        this.modelLoaderRegistry.replace(class_, class_2, modelLoaderFactory);
        return this;
    }

    public final Registry setResourceDecoderBucketPriorityList(List<String> list) {
        ArrayList<String> arrayList = new ArrayList<String>(list.size());
        arrayList.addAll(list);
        arrayList.add(0, "legacy_prepend_all");
        arrayList.add("legacy_append");
        this.decoderRegistry.setBucketPriorityList(arrayList);
        return this;
    }

    public static class MissingComponentException
    extends RuntimeException {
        public MissingComponentException(String string2) {
            super(string2);
        }
    }

    public static final class NoImageHeaderParserException
    extends MissingComponentException {
        public NoImageHeaderParserException() {
            super("Failed to find image header parser.");
        }
    }

    public static class NoModelLoaderAvailableException
    extends MissingComponentException {
        public NoModelLoaderAvailableException(Class<?> class_, Class<?> class_2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find any ModelLoaders for model: ");
            stringBuilder.append(class_);
            stringBuilder.append(" and data: ");
            stringBuilder.append(class_2);
            super(stringBuilder.toString());
        }

        public NoModelLoaderAvailableException(Object object) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find any ModelLoaders registered for model class: ");
            stringBuilder.append(object.getClass());
            super(stringBuilder.toString());
        }

        public <M> NoModelLoaderAvailableException(M m, List<ModelLoader<M, ?>> list) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Found ModelLoaders for model class: ");
            stringBuilder.append(list);
            stringBuilder.append(", but none that handle this specific model instance: ");
            stringBuilder.append(m);
            super(stringBuilder.toString());
        }
    }

    public static class NoResultEncoderAvailableException
    extends MissingComponentException {
        public NoResultEncoderAvailableException(Class<?> class_) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find result encoder for resource class: ");
            stringBuilder.append(class_);
            stringBuilder.append(", you may need to consider registering a new Encoder for the requested type or DiskCacheStrategy.DATA/DiskCacheStrategy.NONE if caching your transformed resource is unnecessary.");
            super(stringBuilder.toString());
        }
    }

    public static class NoSourceEncoderAvailableException
    extends MissingComponentException {
        public NoSourceEncoderAvailableException(Class<?> class_) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find source encoder for data class: ");
            stringBuilder.append(class_);
            super(stringBuilder.toString());
        }
    }

}

