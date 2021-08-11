/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.widget.ImageView
 */
package com.bumptech.glide;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ImageView;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.ViewTarget;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GlideContext
extends ContextWrapper {
    static final TransitionOptions<?, ?> DEFAULT_TRANSITION_OPTIONS = new GenericTransitionOptions();
    private final ArrayPool arrayPool;
    private final List<RequestListener<Object>> defaultRequestListeners;
    private RequestOptions defaultRequestOptions;
    private final Glide.RequestOptionsFactory defaultRequestOptionsFactory;
    private final Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions;
    private final Engine engine;
    private final ImageViewTargetFactory imageViewTargetFactory;
    private final boolean isLoggingRequestOriginsEnabled;
    private final int logLevel;
    private final Registry registry;

    public GlideContext(Context context, ArrayPool arrayPool, Registry registry, ImageViewTargetFactory imageViewTargetFactory, Glide.RequestOptionsFactory requestOptionsFactory, Map<Class<?>, TransitionOptions<?, ?>> map, List<RequestListener<Object>> list, Engine engine, boolean bl, int n) {
        super(context.getApplicationContext());
        this.arrayPool = arrayPool;
        this.registry = registry;
        this.imageViewTargetFactory = imageViewTargetFactory;
        this.defaultRequestOptionsFactory = requestOptionsFactory;
        this.defaultRequestListeners = list;
        this.defaultTransitionOptions = map;
        this.engine = engine;
        this.isLoggingRequestOriginsEnabled = bl;
        this.logLevel = n;
    }

    public <X> ViewTarget<ImageView, X> buildImageViewTarget(ImageView imageView, Class<X> class_) {
        return this.imageViewTargetFactory.buildTarget(imageView, class_);
    }

    public ArrayPool getArrayPool() {
        return this.arrayPool;
    }

    public List<RequestListener<Object>> getDefaultRequestListeners() {
        return this.defaultRequestListeners;
    }

    public RequestOptions getDefaultRequestOptions() {
        synchronized (this) {
            if (this.defaultRequestOptions == null) {
                this.defaultRequestOptions = (RequestOptions)this.defaultRequestOptionsFactory.build().lock();
            }
            RequestOptions requestOptions = this.defaultRequestOptions;
            return requestOptions;
        }
    }

    public <T> TransitionOptions<?, T> getDefaultTransitionOptions(Class<T> transitionOptions) {
        TransitionOptions transitionOptions2 = this.defaultTransitionOptions.get(transitionOptions);
        Map.Entry entry = transitionOptions2;
        if (transitionOptions2 == null) {
            Iterator iterator = this.defaultTransitionOptions.entrySet().iterator();
            do {
                entry = transitionOptions2;
                if (!iterator.hasNext()) break;
                entry = iterator.next();
                if (!((Class)entry.getKey()).isAssignableFrom(transitionOptions)) continue;
                transitionOptions2 = (TransitionOptions)entry.getValue();
            } while (true);
        }
        transitionOptions = entry;
        if (entry == null) {
            transitionOptions = DEFAULT_TRANSITION_OPTIONS;
        }
        return transitionOptions;
    }

    public Engine getEngine() {
        return this.engine;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    public Registry getRegistry() {
        return this.registry;
    }

    public boolean isLoggingRequestOriginsEnabled() {
        return this.isLoggingRequestOriginsEnabled;
    }
}

