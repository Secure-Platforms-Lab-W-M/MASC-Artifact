/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.os.SystemClock
 */
package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class GifFrameLoader {
    private final BitmapPool bitmapPool;
    private final List<FrameCallback> callbacks = new ArrayList<FrameCallback>();
    private DelayTarget current;
    private Bitmap firstFrame;
    private int firstFrameSize;
    private final GifDecoder gifDecoder;
    private final Handler handler;
    private int height;
    private boolean isCleared;
    private boolean isLoadPending;
    private boolean isRunning;
    private DelayTarget next;
    private OnEveryFrameListener onEveryFrameListener;
    private DelayTarget pendingTarget;
    private RequestBuilder<Bitmap> requestBuilder;
    final RequestManager requestManager;
    private boolean startFromFirstFrame;
    private Transformation<Bitmap> transformation;
    private int width;

    GifFrameLoader(Glide glide, GifDecoder gifDecoder, int n, int n2, Transformation<Bitmap> transformation, Bitmap bitmap) {
        this(glide.getBitmapPool(), Glide.with(glide.getContext()), gifDecoder, null, GifFrameLoader.getRequestBuilder(Glide.with(glide.getContext()), n, n2), transformation, bitmap);
    }

    GifFrameLoader(BitmapPool bitmapPool, RequestManager requestManager, GifDecoder gifDecoder, Handler handler, RequestBuilder<Bitmap> requestBuilder, Transformation<Bitmap> transformation, Bitmap bitmap) {
        this.requestManager = requestManager;
        requestManager = handler;
        if (handler == null) {
            requestManager = new Handler(Looper.getMainLooper(), (Handler.Callback)new FrameLoaderCallback());
        }
        this.bitmapPool = bitmapPool;
        this.handler = requestManager;
        this.requestBuilder = requestBuilder;
        this.gifDecoder = gifDecoder;
        this.setFrameTransformation(transformation, bitmap);
    }

    private static Key getFrameSignature() {
        return new ObjectKey(Math.random());
    }

    private static RequestBuilder<Bitmap> getRequestBuilder(RequestManager requestManager, int n, int n2) {
        return requestManager.asBitmap().apply((BaseRequestOptions)((RequestOptions)((RequestOptions)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).useAnimationPool(true)).skipMemoryCache(true)).override(n, n2));
    }

    private void loadNextFrame() {
        if (this.isRunning) {
            if (this.isLoadPending) {
                return;
            }
            if (this.startFromFirstFrame) {
                boolean bl = this.pendingTarget == null;
                Preconditions.checkArgument(bl, "Pending target must be null when starting from the first frame");
                this.gifDecoder.resetFrameIndex();
                this.startFromFirstFrame = false;
            }
            if (this.pendingTarget != null) {
                DelayTarget delayTarget = this.pendingTarget;
                this.pendingTarget = null;
                this.onFrameReady(delayTarget);
                return;
            }
            this.isLoadPending = true;
            int n = this.gifDecoder.getNextDelay();
            long l = SystemClock.uptimeMillis();
            long l2 = n;
            this.gifDecoder.advance();
            this.next = new DelayTarget(this.handler, this.gifDecoder.getCurrentFrameIndex(), l + l2);
            this.requestBuilder.apply((BaseRequestOptions)RequestOptions.signatureOf(GifFrameLoader.getFrameSignature())).load(this.gifDecoder).into(this.next);
            return;
        }
    }

    private void recycleFirstFrame() {
        Bitmap bitmap = this.firstFrame;
        if (bitmap != null) {
            this.bitmapPool.put(bitmap);
            this.firstFrame = null;
        }
    }

    private void start() {
        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        this.isCleared = false;
        this.loadNextFrame();
    }

    private void stop() {
        this.isRunning = false;
    }

    void clear() {
        this.callbacks.clear();
        this.recycleFirstFrame();
        this.stop();
        DelayTarget delayTarget = this.current;
        if (delayTarget != null) {
            this.requestManager.clear(delayTarget);
            this.current = null;
        }
        if ((delayTarget = this.next) != null) {
            this.requestManager.clear(delayTarget);
            this.next = null;
        }
        if ((delayTarget = this.pendingTarget) != null) {
            this.requestManager.clear(delayTarget);
            this.pendingTarget = null;
        }
        this.gifDecoder.clear();
        this.isCleared = true;
    }

    ByteBuffer getBuffer() {
        return this.gifDecoder.getData().asReadOnlyBuffer();
    }

    Bitmap getCurrentFrame() {
        DelayTarget delayTarget = this.current;
        if (delayTarget != null) {
            return delayTarget.getResource();
        }
        return this.firstFrame;
    }

    int getCurrentIndex() {
        DelayTarget delayTarget = this.current;
        if (delayTarget != null) {
            return delayTarget.index;
        }
        return -1;
    }

    Bitmap getFirstFrame() {
        return this.firstFrame;
    }

    int getFrameCount() {
        return this.gifDecoder.getFrameCount();
    }

    Transformation<Bitmap> getFrameTransformation() {
        return this.transformation;
    }

    int getHeight() {
        return this.height;
    }

    int getLoopCount() {
        return this.gifDecoder.getTotalIterationCount();
    }

    int getSize() {
        return this.gifDecoder.getByteSize() + this.firstFrameSize;
    }

    int getWidth() {
        return this.width;
    }

    void onFrameReady(DelayTarget delayTarget) {
        Object object = this.onEveryFrameListener;
        if (object != null) {
            object.onFrameReady();
        }
        this.isLoadPending = false;
        if (this.isCleared) {
            this.handler.obtainMessage(2, (Object)delayTarget).sendToTarget();
            return;
        }
        if (!this.isRunning) {
            this.pendingTarget = delayTarget;
            return;
        }
        if (delayTarget.getResource() != null) {
            this.recycleFirstFrame();
            object = this.current;
            this.current = delayTarget;
            for (int i = this.callbacks.size() - 1; i >= 0; --i) {
                this.callbacks.get(i).onFrameReady();
            }
            if (object != null) {
                this.handler.obtainMessage(2, object).sendToTarget();
            }
        }
        this.loadNextFrame();
    }

    void setFrameTransformation(Transformation<Bitmap> transformation, Bitmap bitmap) {
        this.transformation = Preconditions.checkNotNull(transformation);
        this.firstFrame = Preconditions.checkNotNull(bitmap);
        this.requestBuilder = this.requestBuilder.apply((BaseRequestOptions)new RequestOptions().transform(transformation));
        this.firstFrameSize = Util.getBitmapByteSize(bitmap);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    void setNextStartFromFirstFrame() {
        Preconditions.checkArgument(this.isRunning ^ true, "Can't restart a running animation");
        this.startFromFirstFrame = true;
        DelayTarget delayTarget = this.pendingTarget;
        if (delayTarget != null) {
            this.requestManager.clear(delayTarget);
            this.pendingTarget = null;
        }
    }

    void setOnEveryFrameReadyListener(OnEveryFrameListener onEveryFrameListener) {
        this.onEveryFrameListener = onEveryFrameListener;
    }

    void subscribe(FrameCallback frameCallback) {
        if (!this.isCleared) {
            if (!this.callbacks.contains(frameCallback)) {
                boolean bl = this.callbacks.isEmpty();
                this.callbacks.add(frameCallback);
                if (bl) {
                    this.start();
                }
                return;
            }
            throw new IllegalStateException("Cannot subscribe twice in a row");
        }
        throw new IllegalStateException("Cannot subscribe to a cleared frame loader");
    }

    void unsubscribe(FrameCallback frameCallback) {
        this.callbacks.remove(frameCallback);
        if (this.callbacks.isEmpty()) {
            this.stop();
        }
    }

    static class DelayTarget
    extends CustomTarget<Bitmap> {
        private final Handler handler;
        final int index;
        private Bitmap resource;
        private final long targetTime;

        DelayTarget(Handler handler, int n, long l) {
            this.handler = handler;
            this.index = n;
            this.targetTime = l;
        }

        Bitmap getResource() {
            return this.resource;
        }

        @Override
        public void onLoadCleared(Drawable drawable2) {
            this.resource = null;
        }

        @Override
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            this.resource = bitmap;
            bitmap = this.handler.obtainMessage(1, (Object)this);
            this.handler.sendMessageAtTime((Message)bitmap, this.targetTime);
        }
    }

    public static interface FrameCallback {
        public void onFrameReady();
    }

    private class FrameLoaderCallback
    implements Handler.Callback {
        static final int MSG_CLEAR = 2;
        static final int MSG_DELAY = 1;

        FrameLoaderCallback() {
        }

        public boolean handleMessage(Message object) {
            if (object.what == 1) {
                object = (DelayTarget)object.obj;
                GifFrameLoader.this.onFrameReady((DelayTarget)object);
                return true;
            }
            if (object.what == 2) {
                object = (DelayTarget)object.obj;
                GifFrameLoader.this.requestManager.clear(object);
            }
            return false;
        }
    }

    static interface OnEveryFrameListener {
        public void onFrameReady();
    }

}

