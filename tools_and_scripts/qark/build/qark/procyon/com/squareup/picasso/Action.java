// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.graphics.Bitmap;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import android.graphics.drawable.Drawable;

abstract class Action<T>
{
    boolean cancelled;
    final Drawable errorDrawable;
    final int errorResId;
    final String key;
    final int memoryPolicy;
    final int networkPolicy;
    final boolean noFade;
    final Picasso picasso;
    final Request request;
    final Object tag;
    final WeakReference<T> target;
    boolean willReplay;
    
    Action(final Picasso picasso, final T t, final Request request, final int memoryPolicy, final int networkPolicy, final int errorResId, final Drawable errorDrawable, final String key, Object tag, final boolean noFade) {
        this.picasso = picasso;
        this.request = request;
        WeakReference<T> target;
        if (t == null) {
            target = null;
        }
        else {
            target = new RequestWeakReference<T>(this, t, picasso.referenceQueue);
        }
        this.target = target;
        this.memoryPolicy = memoryPolicy;
        this.networkPolicy = networkPolicy;
        this.noFade = noFade;
        this.errorResId = errorResId;
        this.errorDrawable = errorDrawable;
        this.key = key;
        if (tag == null) {
            tag = this;
        }
        this.tag = tag;
    }
    
    void cancel() {
        this.cancelled = true;
    }
    
    abstract void complete(final Bitmap p0, final Picasso.LoadedFrom p1);
    
    abstract void error();
    
    String getKey() {
        return this.key;
    }
    
    int getMemoryPolicy() {
        return this.memoryPolicy;
    }
    
    int getNetworkPolicy() {
        return this.networkPolicy;
    }
    
    Picasso getPicasso() {
        return this.picasso;
    }
    
    Picasso.Priority getPriority() {
        return this.request.priority;
    }
    
    Request getRequest() {
        return this.request;
    }
    
    Object getTag() {
        return this.tag;
    }
    
    T getTarget() {
        if (this.target == null) {
            return null;
        }
        return this.target.get();
    }
    
    boolean isCancelled() {
        return this.cancelled;
    }
    
    boolean willReplay() {
        return this.willReplay;
    }
    
    static class RequestWeakReference<M> extends WeakReference<M>
    {
        final Action action;
        
        public RequestWeakReference(final Action action, final M m, final ReferenceQueue<? super M> referenceQueue) {
            super(m, referenceQueue);
            this.action = action;
        }
    }
}
