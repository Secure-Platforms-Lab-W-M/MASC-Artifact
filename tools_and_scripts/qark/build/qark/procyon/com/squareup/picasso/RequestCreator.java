// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.util.List;
import android.content.res.Resources;
import android.app.Notification;
import android.widget.RemoteViews;
import android.widget.ImageView;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import android.net.Uri;
import android.graphics.drawable.Drawable;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestCreator
{
    private static final AtomicInteger nextId;
    private final Request.Builder data;
    private boolean deferred;
    private Drawable errorDrawable;
    private int errorResId;
    private int memoryPolicy;
    private int networkPolicy;
    private boolean noFade;
    private final Picasso picasso;
    private Drawable placeholderDrawable;
    private int placeholderResId;
    private boolean setPlaceholder;
    private Object tag;
    
    static {
        nextId = new AtomicInteger();
    }
    
    RequestCreator() {
        this.setPlaceholder = true;
        this.picasso = null;
        this.data = new Request.Builder(null, 0, null);
    }
    
    RequestCreator(final Picasso picasso, final Uri uri, final int n) {
        this.setPlaceholder = true;
        if (picasso.shutdown) {
            throw new IllegalStateException("Picasso instance already shut down. Cannot submit new requests.");
        }
        this.picasso = picasso;
        this.data = new Request.Builder(uri, n, picasso.defaultBitmapConfig);
    }
    
    private Request createRequest(final long n) {
        final int andIncrement = RequestCreator.nextId.getAndIncrement();
        final Request build = this.data.build();
        build.id = andIncrement;
        build.started = n;
        final boolean loggingEnabled = this.picasso.loggingEnabled;
        if (loggingEnabled) {
            Utils.log("Main", "created", build.plainId(), build.toString());
        }
        final Request transformRequest = this.picasso.transformRequest(build);
        if (transformRequest != build) {
            transformRequest.id = andIncrement;
            transformRequest.started = n;
            if (loggingEnabled) {
                Utils.log("Main", "changed", transformRequest.logId(), "into " + transformRequest);
            }
        }
        return transformRequest;
    }
    
    private Drawable getPlaceholderDrawable() {
        if (this.placeholderResId != 0) {
            return this.picasso.context.getResources().getDrawable(this.placeholderResId);
        }
        return this.placeholderDrawable;
    }
    
    private void performRemoteViewInto(final RemoteViewsAction remoteViewsAction) {
        if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
            final Bitmap quickMemoryCacheCheck = this.picasso.quickMemoryCacheCheck(remoteViewsAction.getKey());
            if (quickMemoryCacheCheck != null) {
                remoteViewsAction.complete(quickMemoryCacheCheck, Picasso.LoadedFrom.MEMORY);
                return;
            }
        }
        if (this.placeholderResId != 0) {
            remoteViewsAction.setImageResource(this.placeholderResId);
        }
        this.picasso.enqueueAndSubmit(remoteViewsAction);
    }
    
    public RequestCreator centerCrop() {
        this.data.centerCrop();
        return this;
    }
    
    public RequestCreator centerInside() {
        this.data.centerInside();
        return this;
    }
    
    public RequestCreator config(final Bitmap$Config bitmap$Config) {
        this.data.config(bitmap$Config);
        return this;
    }
    
    public RequestCreator error(final int errorResId) {
        if (errorResId == 0) {
            throw new IllegalArgumentException("Error image resource invalid.");
        }
        if (this.errorDrawable != null) {
            throw new IllegalStateException("Error image already set.");
        }
        this.errorResId = errorResId;
        return this;
    }
    
    public RequestCreator error(final Drawable errorDrawable) {
        if (errorDrawable == null) {
            throw new IllegalArgumentException("Error image may not be null.");
        }
        if (this.errorResId != 0) {
            throw new IllegalStateException("Error image already set.");
        }
        this.errorDrawable = errorDrawable;
        return this;
    }
    
    public void fetch() {
        this.fetch(null);
    }
    
    public void fetch(final Callback callback) {
        final long nanoTime = System.nanoTime();
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with fetch.");
        }
        if (this.data.hasImage()) {
            if (!this.data.hasPriority()) {
                this.data.priority(Picasso.Priority.LOW);
            }
            final Request request = this.createRequest(nanoTime);
            final String key = Utils.createKey(request, new StringBuilder());
            if (this.picasso.quickMemoryCacheCheck(key) == null) {
                this.picasso.submit(new FetchAction(this.picasso, request, this.memoryPolicy, this.networkPolicy, this.tag, key, callback));
                return;
            }
            if (this.picasso.loggingEnabled) {
                Utils.log("Main", "completed", request.plainId(), "from " + Picasso.LoadedFrom.MEMORY);
            }
            if (callback != null) {
                callback.onSuccess();
            }
        }
    }
    
    public RequestCreator fit() {
        this.deferred = true;
        return this;
    }
    
    public Bitmap get() throws IOException {
        final long nanoTime = System.nanoTime();
        Utils.checkNotMain();
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with get.");
        }
        if (!this.data.hasImage()) {
            return null;
        }
        final Request request = this.createRequest(nanoTime);
        return BitmapHunter.forRequest(this.picasso, this.picasso.dispatcher, this.picasso.cache, this.picasso.stats, new GetAction(this.picasso, request, this.memoryPolicy, this.networkPolicy, this.tag, Utils.createKey(request, new StringBuilder()))).hunt();
    }
    
    public void into(final ImageView imageView) {
        this.into(imageView, null);
    }
    
    public void into(final ImageView imageView, final Callback callback) {
        final long nanoTime = System.nanoTime();
        Utils.checkMain();
        if (imageView == null) {
            throw new IllegalArgumentException("Target must not be null.");
        }
        if (this.data.hasImage()) {
            if (this.deferred) {
                if (this.data.hasSize()) {
                    throw new IllegalStateException("Fit cannot be used with resize.");
                }
                final int width = imageView.getWidth();
                final int height = imageView.getHeight();
                if (width == 0 || height == 0) {
                    if (this.setPlaceholder) {
                        PicassoDrawable.setPlaceholder(imageView, this.getPlaceholderDrawable());
                    }
                    this.picasso.defer(imageView, new DeferredRequestCreator(this, imageView, callback));
                    return;
                }
                this.data.resize(width, height);
            }
            final Request request = this.createRequest(nanoTime);
            final String key = Utils.createKey(request);
            if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
                final Bitmap quickMemoryCacheCheck = this.picasso.quickMemoryCacheCheck(key);
                if (quickMemoryCacheCheck != null) {
                    this.picasso.cancelRequest(imageView);
                    PicassoDrawable.setBitmap(imageView, this.picasso.context, quickMemoryCacheCheck, Picasso.LoadedFrom.MEMORY, this.noFade, this.picasso.indicatorsEnabled);
                    if (this.picasso.loggingEnabled) {
                        Utils.log("Main", "completed", request.plainId(), "from " + Picasso.LoadedFrom.MEMORY);
                    }
                    if (callback != null) {
                        callback.onSuccess();
                    }
                    return;
                }
            }
            if (this.setPlaceholder) {
                PicassoDrawable.setPlaceholder(imageView, this.getPlaceholderDrawable());
            }
            this.picasso.enqueueAndSubmit(new ImageViewAction(this.picasso, imageView, request, this.memoryPolicy, this.networkPolicy, this.errorResId, this.errorDrawable, key, this.tag, callback, this.noFade));
            return;
        }
        this.picasso.cancelRequest(imageView);
        if (this.setPlaceholder) {
            PicassoDrawable.setPlaceholder(imageView, this.getPlaceholderDrawable());
        }
    }
    
    public void into(final RemoteViews remoteViews, final int n, final int n2, final Notification notification) {
        final long nanoTime = System.nanoTime();
        if (remoteViews == null) {
            throw new IllegalArgumentException("RemoteViews must not be null.");
        }
        if (notification == null) {
            throw new IllegalArgumentException("Notification must not be null.");
        }
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with RemoteViews.");
        }
        if (this.placeholderDrawable != null || this.placeholderResId != 0 || this.errorDrawable != null) {
            throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views.");
        }
        final Request request = this.createRequest(nanoTime);
        this.performRemoteViewInto(new RemoteViewsAction.NotificationAction(this.picasso, request, remoteViews, n, n2, notification, this.memoryPolicy, this.networkPolicy, Utils.createKey(request, new StringBuilder()), this.tag, this.errorResId));
    }
    
    public void into(final RemoteViews remoteViews, final int n, final int[] array) {
        final long nanoTime = System.nanoTime();
        if (remoteViews == null) {
            throw new IllegalArgumentException("remoteViews must not be null.");
        }
        if (array == null) {
            throw new IllegalArgumentException("appWidgetIds must not be null.");
        }
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with remote views.");
        }
        if (this.placeholderDrawable != null || this.placeholderResId != 0 || this.errorDrawable != null) {
            throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views.");
        }
        final Request request = this.createRequest(nanoTime);
        this.performRemoteViewInto(new RemoteViewsAction.AppWidgetAction(this.picasso, request, remoteViews, n, array, this.memoryPolicy, this.networkPolicy, Utils.createKey(request, new StringBuilder()), this.tag, this.errorResId));
    }
    
    public void into(final Target target) {
        Drawable placeholderDrawable = null;
        final Drawable drawable = null;
        final long nanoTime = System.nanoTime();
        Utils.checkMain();
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        }
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with a Target.");
        }
        if (!this.data.hasImage()) {
            this.picasso.cancelRequest(target);
            Drawable placeholderDrawable2 = drawable;
            if (this.setPlaceholder) {
                placeholderDrawable2 = this.getPlaceholderDrawable();
            }
            target.onPrepareLoad(placeholderDrawable2);
            return;
        }
        final Request request = this.createRequest(nanoTime);
        final String key = Utils.createKey(request);
        if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
            final Bitmap quickMemoryCacheCheck = this.picasso.quickMemoryCacheCheck(key);
            if (quickMemoryCacheCheck != null) {
                this.picasso.cancelRequest(target);
                target.onBitmapLoaded(quickMemoryCacheCheck, Picasso.LoadedFrom.MEMORY);
                return;
            }
        }
        if (this.setPlaceholder) {
            placeholderDrawable = this.getPlaceholderDrawable();
        }
        target.onPrepareLoad(placeholderDrawable);
        this.picasso.enqueueAndSubmit(new TargetAction(this.picasso, target, request, this.memoryPolicy, this.networkPolicy, this.errorDrawable, key, this.tag, this.errorResId));
    }
    
    public RequestCreator memoryPolicy(MemoryPolicy memoryPolicy, final MemoryPolicy... array) {
        if (memoryPolicy == null) {
            throw new IllegalArgumentException("Memory policy cannot be null.");
        }
        this.memoryPolicy |= memoryPolicy.index;
        if (array == null) {
            throw new IllegalArgumentException("Memory policy cannot be null.");
        }
        if (array.length > 0) {
            for (int length = array.length, i = 0; i < length; ++i) {
                memoryPolicy = array[i];
                if (memoryPolicy == null) {
                    throw new IllegalArgumentException("Memory policy cannot be null.");
                }
                this.memoryPolicy |= memoryPolicy.index;
            }
        }
        return this;
    }
    
    public RequestCreator networkPolicy(NetworkPolicy networkPolicy, final NetworkPolicy... array) {
        if (networkPolicy == null) {
            throw new IllegalArgumentException("Network policy cannot be null.");
        }
        this.networkPolicy |= networkPolicy.index;
        if (array == null) {
            throw new IllegalArgumentException("Network policy cannot be null.");
        }
        if (array.length > 0) {
            for (int length = array.length, i = 0; i < length; ++i) {
                networkPolicy = array[i];
                if (networkPolicy == null) {
                    throw new IllegalArgumentException("Network policy cannot be null.");
                }
                this.networkPolicy |= networkPolicy.index;
            }
        }
        return this;
    }
    
    public RequestCreator noFade() {
        this.noFade = true;
        return this;
    }
    
    public RequestCreator noPlaceholder() {
        if (this.placeholderResId != 0) {
            throw new IllegalStateException("Placeholder resource already set.");
        }
        if (this.placeholderDrawable != null) {
            throw new IllegalStateException("Placeholder image already set.");
        }
        this.setPlaceholder = false;
        return this;
    }
    
    public RequestCreator onlyScaleDown() {
        this.data.onlyScaleDown();
        return this;
    }
    
    public RequestCreator placeholder(final int placeholderResId) {
        if (!this.setPlaceholder) {
            throw new IllegalStateException("Already explicitly declared as no placeholder.");
        }
        if (placeholderResId == 0) {
            throw new IllegalArgumentException("Placeholder image resource invalid.");
        }
        if (this.placeholderDrawable != null) {
            throw new IllegalStateException("Placeholder image already set.");
        }
        this.placeholderResId = placeholderResId;
        return this;
    }
    
    public RequestCreator placeholder(final Drawable placeholderDrawable) {
        if (!this.setPlaceholder) {
            throw new IllegalStateException("Already explicitly declared as no placeholder.");
        }
        if (this.placeholderResId != 0) {
            throw new IllegalStateException("Placeholder image already set.");
        }
        this.placeholderDrawable = placeholderDrawable;
        return this;
    }
    
    public RequestCreator priority(final Picasso.Priority priority) {
        this.data.priority(priority);
        return this;
    }
    
    public RequestCreator resize(final int n, final int n2) {
        this.data.resize(n, n2);
        return this;
    }
    
    public RequestCreator resizeDimen(final int n, final int n2) {
        final Resources resources = this.picasso.context.getResources();
        return this.resize(resources.getDimensionPixelSize(n), resources.getDimensionPixelSize(n2));
    }
    
    public RequestCreator rotate(final float n) {
        this.data.rotate(n);
        return this;
    }
    
    public RequestCreator rotate(final float n, final float n2, final float n3) {
        this.data.rotate(n, n2, n3);
        return this;
    }
    
    @Deprecated
    public RequestCreator skipMemoryCache() {
        return this.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
    }
    
    public RequestCreator stableKey(final String s) {
        this.data.stableKey(s);
        return this;
    }
    
    public RequestCreator tag(final Object tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag invalid.");
        }
        if (this.tag != null) {
            throw new IllegalStateException("Tag already set.");
        }
        this.tag = tag;
        return this;
    }
    
    public RequestCreator transform(final Transformation transformation) {
        this.data.transform(transformation);
        return this;
    }
    
    public RequestCreator transform(final List<? extends Transformation> list) {
        this.data.transform(list);
        return this;
    }
    
    RequestCreator unfit() {
        this.deferred = false;
        return this;
    }
}
