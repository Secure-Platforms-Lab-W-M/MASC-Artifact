/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.widget.AbsListView
 *  android.widget.AbsListView$OnScrollListener
 */
package com.bumptech.glide;

import android.graphics.drawable.Drawable;
import android.widget.AbsListView;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.util.List;
import java.util.Queue;

public class ListPreloader<T>
implements AbsListView.OnScrollListener {
    private boolean isIncreasing = true;
    private int lastEnd;
    private int lastFirstVisible = -1;
    private int lastStart;
    private final int maxPreload;
    private final PreloadSizeProvider<T> preloadDimensionProvider;
    private final PreloadModelProvider<T> preloadModelProvider;
    private final PreloadTargetQueue preloadTargetQueue;
    private final RequestManager requestManager;
    private int totalItemCount;

    public ListPreloader(RequestManager requestManager, PreloadModelProvider<T> preloadModelProvider, PreloadSizeProvider<T> preloadSizeProvider, int n) {
        this.requestManager = requestManager;
        this.preloadModelProvider = preloadModelProvider;
        this.preloadDimensionProvider = preloadSizeProvider;
        this.maxPreload = n;
        this.preloadTargetQueue = new PreloadTargetQueue(n + 1);
    }

    private void cancelAll() {
        for (int i = 0; i < this.preloadTargetQueue.queue.size(); ++i) {
            this.requestManager.clear(this.preloadTargetQueue.next(0, 0));
        }
    }

    private void preload(int n, int n2) {
        int n3;
        int n4;
        if (n < n2) {
            n3 = Math.max(this.lastEnd, n);
            n4 = n2;
        } else {
            n3 = n2;
            n4 = Math.min(this.lastStart, n);
        }
        n4 = Math.min(this.totalItemCount, n4);
        n3 = Math.min(this.totalItemCount, Math.max(0, n3));
        if (n < n2) {
            for (n = n3; n < n4; ++n) {
                this.preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(n), n, true);
            }
        } else {
            for (n = n4 - 1; n >= n3; --n) {
                this.preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(n), n, false);
            }
        }
        this.lastStart = n3;
        this.lastEnd = n4;
    }

    private void preload(int n, boolean bl) {
        if (this.isIncreasing != bl) {
            this.isIncreasing = bl;
            this.cancelAll();
        }
        int n2 = this.maxPreload;
        if (!bl) {
            n2 = - n2;
        }
        this.preload(n, n2 + n);
    }

    private void preloadAdapterPosition(List<T> list, int n, boolean bl) {
        int n2 = list.size();
        if (bl) {
            for (int i = 0; i < n2; ++i) {
                this.preloadItem(list.get(i), n, i);
            }
            return;
        }
        for (int i = n2 - 1; i >= 0; --i) {
            this.preloadItem(list.get(i), n, i);
        }
    }

    private void preloadItem(T object, int n, int n2) {
        if (object == null) {
            return;
        }
        int[] arrn = this.preloadDimensionProvider.getPreloadSize(object, n, n2);
        if (arrn == null) {
            return;
        }
        if ((object = this.preloadModelProvider.getPreloadRequestBuilder(object)) == null) {
            return;
        }
        object.into(this.preloadTargetQueue.next(arrn[0], arrn[1]));
    }

    public void onScroll(AbsListView absListView, int n, int n2, int n3) {
        this.totalItemCount = n3;
        n3 = this.lastFirstVisible;
        if (n > n3) {
            this.preload(n + n2, true);
        } else if (n < n3) {
            this.preload(n, false);
        }
        this.lastFirstVisible = n;
    }

    public void onScrollStateChanged(AbsListView absListView, int n) {
    }

    public static interface PreloadModelProvider<U> {
        public List<U> getPreloadItems(int var1);

        public RequestBuilder<?> getPreloadRequestBuilder(U var1);
    }

    public static interface PreloadSizeProvider<T> {
        public int[] getPreloadSize(T var1, int var2, int var3);
    }

    private static final class PreloadTarget
    implements Target<Object> {
        int photoHeight;
        int photoWidth;
        private Request request;

        PreloadTarget() {
        }

        @Override
        public Request getRequest() {
            return this.request;
        }

        @Override
        public void getSize(SizeReadyCallback sizeReadyCallback) {
            sizeReadyCallback.onSizeReady(this.photoWidth, this.photoHeight);
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public void onLoadCleared(Drawable drawable2) {
        }

        @Override
        public void onLoadFailed(Drawable drawable2) {
        }

        @Override
        public void onLoadStarted(Drawable drawable2) {
        }

        @Override
        public void onResourceReady(Object object, Transition<? super Object> transition) {
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onStop() {
        }

        @Override
        public void removeCallback(SizeReadyCallback sizeReadyCallback) {
        }

        @Override
        public void setRequest(Request request) {
            this.request = request;
        }
    }

    private static final class PreloadTargetQueue {
        final Queue<PreloadTarget> queue;

        PreloadTargetQueue(int n) {
            this.queue = Util.createQueue(n);
            for (int i = 0; i < n; ++i) {
                this.queue.offer(new PreloadTarget());
            }
        }

        public PreloadTarget next(int n, int n2) {
            PreloadTarget preloadTarget = this.queue.poll();
            this.queue.offer(preloadTarget);
            preloadTarget.photoWidth = n;
            preloadTarget.photoHeight = n2;
            return preloadTarget;
        }
    }

}

