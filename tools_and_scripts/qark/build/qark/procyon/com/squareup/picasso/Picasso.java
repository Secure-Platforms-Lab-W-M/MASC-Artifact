// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.os.Process;
import java.util.concurrent.ExecutorService;
import java.util.Iterator;
import java.io.File;
import android.net.Uri;
import android.widget.RemoteViews;
import android.graphics.Bitmap;
import java.util.WeakHashMap;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import android.os.Message;
import android.os.Looper;
import android.widget.ImageView;
import java.util.Map;
import java.util.List;
import java.lang.ref.ReferenceQueue;
import android.graphics.Bitmap$Config;
import android.content.Context;
import android.os.Handler;

public class Picasso
{
    static final Handler HANDLER;
    static final String TAG = "Picasso";
    static volatile Picasso singleton;
    final Cache cache;
    private final CleanupThread cleanupThread;
    final Context context;
    final Bitmap$Config defaultBitmapConfig;
    final Dispatcher dispatcher;
    boolean indicatorsEnabled;
    private final Listener listener;
    volatile boolean loggingEnabled;
    final ReferenceQueue<Object> referenceQueue;
    private final List<RequestHandler> requestHandlers;
    private final RequestTransformer requestTransformer;
    boolean shutdown;
    final Stats stats;
    final Map<Object, Action> targetToAction;
    final Map<ImageView, DeferredRequestCreator> targetToDeferredRequestCreator;
    
    static {
        HANDLER = new Handler(Looper.getMainLooper()) {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        throw new AssertionError((Object)("Unknown handler message received: " + message.what));
                    }
                    case 8: {
                        final List list = (List)message.obj;
                        for (int i = 0; i < list.size(); ++i) {
                            final BitmapHunter bitmapHunter = list.get(i);
                            bitmapHunter.picasso.complete(bitmapHunter);
                        }
                        break;
                    }
                    case 3: {
                        final Action action = (Action)message.obj;
                        if (action.getPicasso().loggingEnabled) {
                            Utils.log("Main", "canceled", action.request.logId(), "target got garbage collected");
                        }
                        action.picasso.cancelExistingRequest(action.getTarget());
                        break;
                    }
                    case 13: {
                        final List list2 = (List)message.obj;
                        for (int j = 0; j < list2.size(); ++j) {
                            final Action action2 = list2.get(j);
                            action2.picasso.resumeAction(action2);
                        }
                        break;
                    }
                }
            }
        };
        Picasso.singleton = null;
    }
    
    Picasso(final Context context, final Dispatcher dispatcher, final Cache cache, final Listener listener, final RequestTransformer requestTransformer, final List<RequestHandler> list, final Stats stats, final Bitmap$Config defaultBitmapConfig, final boolean indicatorsEnabled, final boolean loggingEnabled) {
        this.context = context;
        this.dispatcher = dispatcher;
        this.cache = cache;
        this.listener = listener;
        this.requestTransformer = requestTransformer;
        this.defaultBitmapConfig = defaultBitmapConfig;
        int size;
        if (list != null) {
            size = list.size();
        }
        else {
            size = 0;
        }
        final ArrayList list2 = new ArrayList<RequestHandler>(7 + size);
        list2.add((NetworkRequestHandler)new ResourceRequestHandler(context));
        if (list != null) {
            list2.addAll((Collection<?>)list);
        }
        list2.add((NetworkRequestHandler)new ContactsPhotoRequestHandler(context));
        list2.add((NetworkRequestHandler)new MediaStoreRequestHandler(context));
        list2.add((NetworkRequestHandler)new ContentStreamRequestHandler(context));
        list2.add((NetworkRequestHandler)new AssetRequestHandler(context));
        list2.add((NetworkRequestHandler)new FileRequestHandler(context));
        list2.add(new NetworkRequestHandler(dispatcher.downloader, stats));
        this.requestHandlers = Collections.unmodifiableList((List<? extends RequestHandler>)list2);
        this.stats = stats;
        this.targetToAction = new WeakHashMap<Object, Action>();
        this.targetToDeferredRequestCreator = new WeakHashMap<ImageView, DeferredRequestCreator>();
        this.indicatorsEnabled = indicatorsEnabled;
        this.loggingEnabled = loggingEnabled;
        this.referenceQueue = new ReferenceQueue<Object>();
        (this.cleanupThread = new CleanupThread(this.referenceQueue, Picasso.HANDLER)).start();
    }
    
    private void cancelExistingRequest(final Object o) {
        Utils.checkMain();
        final Action action = this.targetToAction.remove(o);
        if (action != null) {
            action.cancel();
            this.dispatcher.dispatchCancel(action);
        }
        if (o instanceof ImageView) {
            final DeferredRequestCreator deferredRequestCreator = this.targetToDeferredRequestCreator.remove(o);
            if (deferredRequestCreator != null) {
                deferredRequestCreator.cancel();
            }
        }
    }
    
    private void deliverAction(final Bitmap bitmap, final LoadedFrom loadedFrom, final Action action) {
        if (!action.isCancelled()) {
            if (!action.willReplay()) {
                this.targetToAction.remove(action.getTarget());
            }
            if (bitmap != null) {
                if (loadedFrom == null) {
                    throw new AssertionError((Object)"LoadedFrom cannot be null.");
                }
                action.complete(bitmap, loadedFrom);
                if (this.loggingEnabled) {
                    Utils.log("Main", "completed", action.request.logId(), "from " + loadedFrom);
                }
            }
            else {
                action.error();
                if (this.loggingEnabled) {
                    Utils.log("Main", "errored", action.request.logId());
                }
            }
        }
    }
    
    public static void setSingletonInstance(final Picasso picasso) {
        synchronized (Picasso.class) {
            if (Picasso.singleton != null) {
                throw new IllegalStateException("Singleton instance already exists.");
            }
        }
        final Picasso singleton;
        Picasso.singleton = singleton;
    }
    // monitorexit(Picasso.class)
    
    public static Picasso with(final Context context) {
        Label_0032: {
            if (Picasso.singleton != null) {
                break Label_0032;
            }
            synchronized (Picasso.class) {
                if (Picasso.singleton == null) {
                    Picasso.singleton = new Builder(context).build();
                }
                return Picasso.singleton;
            }
        }
    }
    
    public boolean areIndicatorsEnabled() {
        return this.indicatorsEnabled;
    }
    
    public void cancelRequest(final ImageView imageView) {
        this.cancelExistingRequest(imageView);
    }
    
    public void cancelRequest(final RemoteViews remoteViews, final int n) {
        this.cancelExistingRequest(new RemoteViewsAction.RemoteViewsTarget(remoteViews, n));
    }
    
    public void cancelRequest(final Target target) {
        this.cancelExistingRequest(target);
    }
    
    public void cancelTag(final Object o) {
        Utils.checkMain();
        final ArrayList<Action<Object>> list = (ArrayList<Action<Object>>)new ArrayList<Object>(this.targetToAction.values());
        for (int i = 0; i < list.size(); ++i) {
            final Action<Object> action = list.get(i);
            if (action.getTag().equals(o)) {
                this.cancelExistingRequest(action.getTarget());
            }
        }
    }
    
    void complete(final BitmapHunter bitmapHunter) {
        boolean b = false;
        final Action action = bitmapHunter.getAction();
        final List<Action> actions = bitmapHunter.getActions();
        boolean b2;
        if (actions != null && !actions.isEmpty()) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        if (action != null || b2) {
            b = true;
        }
        if (b) {
            final Uri uri = bitmapHunter.getData().uri;
            final Exception exception = bitmapHunter.getException();
            final Bitmap result = bitmapHunter.getResult();
            final LoadedFrom loaded = bitmapHunter.getLoadedFrom();
            if (action != null) {
                this.deliverAction(result, loaded, action);
            }
            if (b2) {
                for (int i = 0; i < actions.size(); ++i) {
                    this.deliverAction(result, loaded, actions.get(i));
                }
            }
            if (this.listener != null && exception != null) {
                this.listener.onImageLoadFailed(this, uri, exception);
            }
        }
    }
    
    void defer(final ImageView imageView, final DeferredRequestCreator deferredRequestCreator) {
        this.targetToDeferredRequestCreator.put(imageView, deferredRequestCreator);
    }
    
    void enqueueAndSubmit(final Action action) {
        final Object target = action.getTarget();
        if (target != null && this.targetToAction.get(target) != action) {
            this.cancelExistingRequest(target);
            this.targetToAction.put(target, action);
        }
        this.submit(action);
    }
    
    List<RequestHandler> getRequestHandlers() {
        return this.requestHandlers;
    }
    
    public StatsSnapshot getSnapshot() {
        return this.stats.createSnapshot();
    }
    
    public void invalidate(final Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("uri == null");
        }
        this.cache.clearKeyUri(uri.toString());
    }
    
    public void invalidate(final File file) {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        this.invalidate(Uri.fromFile(file));
    }
    
    public void invalidate(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("path == null");
        }
        this.invalidate(Uri.parse(s));
    }
    
    @Deprecated
    public boolean isDebugging() {
        return this.areIndicatorsEnabled() && this.isLoggingEnabled();
    }
    
    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }
    
    public RequestCreator load(final int n) {
        if (n == 0) {
            throw new IllegalArgumentException("Resource ID must not be zero.");
        }
        return new RequestCreator(this, null, n);
    }
    
    public RequestCreator load(final Uri uri) {
        return new RequestCreator(this, uri, 0);
    }
    
    public RequestCreator load(final File file) {
        if (file == null) {
            return new RequestCreator(this, null, 0);
        }
        return this.load(Uri.fromFile(file));
    }
    
    public RequestCreator load(final String s) {
        if (s == null) {
            return new RequestCreator(this, null, 0);
        }
        if (s.trim().length() == 0) {
            throw new IllegalArgumentException("Path must not be empty.");
        }
        return this.load(Uri.parse(s));
    }
    
    public void pauseTag(final Object o) {
        this.dispatcher.dispatchPauseTag(o);
    }
    
    Bitmap quickMemoryCacheCheck(final String s) {
        final Bitmap value = this.cache.get(s);
        if (value != null) {
            this.stats.dispatchCacheHit();
            return value;
        }
        this.stats.dispatchCacheMiss();
        return value;
    }
    
    void resumeAction(final Action action) {
        Bitmap quickMemoryCacheCheck = null;
        if (MemoryPolicy.shouldReadFromMemoryCache(action.memoryPolicy)) {
            quickMemoryCacheCheck = this.quickMemoryCacheCheck(action.getKey());
        }
        if (quickMemoryCacheCheck != null) {
            this.deliverAction(quickMemoryCacheCheck, LoadedFrom.MEMORY, action);
            if (this.loggingEnabled) {
                Utils.log("Main", "completed", action.request.logId(), "from " + LoadedFrom.MEMORY);
            }
        }
        else {
            this.enqueueAndSubmit(action);
            if (this.loggingEnabled) {
                Utils.log("Main", "resumed", action.request.logId());
            }
        }
    }
    
    public void resumeTag(final Object o) {
        this.dispatcher.dispatchResumeTag(o);
    }
    
    @Deprecated
    public void setDebugging(final boolean indicatorsEnabled) {
        this.setIndicatorsEnabled(indicatorsEnabled);
    }
    
    public void setIndicatorsEnabled(final boolean indicatorsEnabled) {
        this.indicatorsEnabled = indicatorsEnabled;
    }
    
    public void setLoggingEnabled(final boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }
    
    public void shutdown() {
        if (this == Picasso.singleton) {
            throw new UnsupportedOperationException("Default singleton instance cannot be shutdown.");
        }
        if (this.shutdown) {
            return;
        }
        this.cache.clear();
        this.cleanupThread.shutdown();
        this.stats.shutdown();
        this.dispatcher.shutdown();
        final Iterator<DeferredRequestCreator> iterator = this.targetToDeferredRequestCreator.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel();
        }
        this.targetToDeferredRequestCreator.clear();
        this.shutdown = true;
    }
    
    void submit(final Action action) {
        this.dispatcher.dispatchSubmit(action);
    }
    
    Request transformRequest(final Request request) {
        final Request transformRequest = this.requestTransformer.transformRequest(request);
        if (transformRequest == null) {
            throw new IllegalStateException("Request transformer " + this.requestTransformer.getClass().getCanonicalName() + " returned null for " + request);
        }
        return transformRequest;
    }
    
    public static class Builder
    {
        private Cache cache;
        private final Context context;
        private Bitmap$Config defaultBitmapConfig;
        private Downloader downloader;
        private boolean indicatorsEnabled;
        private Listener listener;
        private boolean loggingEnabled;
        private List<RequestHandler> requestHandlers;
        private ExecutorService service;
        private RequestTransformer transformer;
        
        public Builder(final Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }
        
        public Builder addRequestHandler(final RequestHandler requestHandler) {
            if (requestHandler == null) {
                throw new IllegalArgumentException("RequestHandler must not be null.");
            }
            if (this.requestHandlers == null) {
                this.requestHandlers = new ArrayList<RequestHandler>();
            }
            if (this.requestHandlers.contains(requestHandler)) {
                throw new IllegalStateException("RequestHandler already registered.");
            }
            this.requestHandlers.add(requestHandler);
            return this;
        }
        
        public Picasso build() {
            final Context context = this.context;
            if (this.downloader == null) {
                this.downloader = Utils.createDefaultDownloader(context);
            }
            if (this.cache == null) {
                this.cache = new LruCache(context);
            }
            if (this.service == null) {
                this.service = new PicassoExecutorService();
            }
            if (this.transformer == null) {
                this.transformer = RequestTransformer.IDENTITY;
            }
            final Stats stats = new Stats(this.cache);
            return new Picasso(context, new Dispatcher(context, this.service, Picasso.HANDLER, this.downloader, this.cache, stats), this.cache, this.listener, this.transformer, this.requestHandlers, stats, this.defaultBitmapConfig, this.indicatorsEnabled, this.loggingEnabled);
        }
        
        @Deprecated
        public Builder debugging(final boolean b) {
            return this.indicatorsEnabled(b);
        }
        
        public Builder defaultBitmapConfig(final Bitmap$Config defaultBitmapConfig) {
            if (defaultBitmapConfig == null) {
                throw new IllegalArgumentException("Bitmap config must not be null.");
            }
            this.defaultBitmapConfig = defaultBitmapConfig;
            return this;
        }
        
        public Builder downloader(final Downloader downloader) {
            if (downloader == null) {
                throw new IllegalArgumentException("Downloader must not be null.");
            }
            if (this.downloader != null) {
                throw new IllegalStateException("Downloader already set.");
            }
            this.downloader = downloader;
            return this;
        }
        
        public Builder executor(final ExecutorService service) {
            if (service == null) {
                throw new IllegalArgumentException("Executor service must not be null.");
            }
            if (this.service != null) {
                throw new IllegalStateException("Executor service already set.");
            }
            this.service = service;
            return this;
        }
        
        public Builder indicatorsEnabled(final boolean indicatorsEnabled) {
            this.indicatorsEnabled = indicatorsEnabled;
            return this;
        }
        
        public Builder listener(final Listener listener) {
            if (listener == null) {
                throw new IllegalArgumentException("Listener must not be null.");
            }
            if (this.listener != null) {
                throw new IllegalStateException("Listener already set.");
            }
            this.listener = listener;
            return this;
        }
        
        public Builder loggingEnabled(final boolean loggingEnabled) {
            this.loggingEnabled = loggingEnabled;
            return this;
        }
        
        public Builder memoryCache(final Cache cache) {
            if (cache == null) {
                throw new IllegalArgumentException("Memory cache must not be null.");
            }
            if (this.cache != null) {
                throw new IllegalStateException("Memory cache already set.");
            }
            this.cache = cache;
            return this;
        }
        
        public Builder requestTransformer(final RequestTransformer transformer) {
            if (transformer == null) {
                throw new IllegalArgumentException("Transformer must not be null.");
            }
            if (this.transformer != null) {
                throw new IllegalStateException("Transformer already set.");
            }
            this.transformer = transformer;
            return this;
        }
    }
    
    private static class CleanupThread extends Thread
    {
        private final Handler handler;
        private final ReferenceQueue<Object> referenceQueue;
        
        CleanupThread(final ReferenceQueue<Object> referenceQueue, final Handler handler) {
            this.referenceQueue = referenceQueue;
            this.handler = handler;
            this.setDaemon(true);
            this.setName("Picasso-refQueue");
        }
        
        @Override
        public void run() {
            Process.setThreadPriority(10);
            try {
                while (true) {
                    final Action.RequestWeakReference requestWeakReference = (Action.RequestWeakReference)this.referenceQueue.remove(1000L);
                    final Message obtainMessage = this.handler.obtainMessage();
                    if (requestWeakReference != null) {
                        obtainMessage.what = 3;
                        obtainMessage.obj = requestWeakReference.action;
                        this.handler.sendMessage(obtainMessage);
                    }
                    else {
                        obtainMessage.recycle();
                    }
                }
            }
            catch (Exception ex) {
                this.handler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException(ex);
                    }
                });
            }
            catch (InterruptedException ex2) {}
        }
        
        void shutdown() {
            this.interrupt();
        }
    }
    
    public interface Listener
    {
        void onImageLoadFailed(final Picasso p0, final Uri p1, final Exception p2);
    }
    
    public enum LoadedFrom
    {
        DISK(-16776961), 
        MEMORY(-16711936), 
        NETWORK(-65536);
        
        final int debugColor;
        
        private LoadedFrom(final int debugColor) {
            this.debugColor = debugColor;
        }
    }
    
    public enum Priority
    {
        HIGH, 
        LOW, 
        NORMAL;
    }
    
    public interface RequestTransformer
    {
        public static final RequestTransformer IDENTITY = new RequestTransformer() {
            @Override
            public Request transformRequest(final Request request) {
                return request;
            }
        };
        
        Request transformRequest(final Request p0);
    }
}
