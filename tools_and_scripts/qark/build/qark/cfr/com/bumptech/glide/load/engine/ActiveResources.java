/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Process
 */
package com.bumptech.glide.load.engine;

import android.os.Process;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.EngineResource;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final class ActiveResources {
    final Map<Key, ResourceWeakReference> activeEngineResources = new HashMap<Key, ResourceWeakReference>();
    private volatile DequeuedResourceCallback cb;
    private final boolean isActiveResourceRetentionAllowed;
    private volatile boolean isShutdown;
    private EngineResource.ResourceListener listener;
    private final Executor monitorClearedResourcesExecutor;
    private final ReferenceQueue<EngineResource<?>> resourceReferenceQueue = new ReferenceQueue();

    ActiveResources(boolean bl) {
        this(bl, Executors.newSingleThreadExecutor(new ThreadFactory(){

            @Override
            public Thread newThread(final Runnable runnable) {
                return new Thread(new Runnable(){

                    @Override
                    public void run() {
                        Process.setThreadPriority((int)10);
                        runnable.run();
                    }
                }, "glide-active-resources");
            }

        }));
    }

    ActiveResources(boolean bl, Executor executor) {
        this.isActiveResourceRetentionAllowed = bl;
        this.monitorClearedResourcesExecutor = executor;
        executor.execute(new Runnable(){

            @Override
            public void run() {
                ActiveResources.this.cleanReferenceQueue();
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void activate(Key object, EngineResource<?> object2) {
        synchronized (this) {
            ResourceWeakReference resourceWeakReference;
            resourceWeakReference = new ResourceWeakReference((Key)object, resourceWeakReference, this.resourceReferenceQueue, this.isActiveResourceRetentionAllowed);
            object = this.activeEngineResources.put((Key)object, resourceWeakReference);
            if (object != null) {
                object.reset();
            }
            return;
        }
    }

    void cleanReferenceQueue() {
        while (!this.isShutdown) {
            this.cleanupActiveReference((ResourceWeakReference)this.resourceReferenceQueue.remove());
            DequeuedResourceCallback dequeuedResourceCallback = this.cb;
            if (dequeuedResourceCallback == null) continue;
            try {
                dequeuedResourceCallback.onResourceDequeued();
            }
            catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void cleanupActiveReference(ResourceWeakReference resourceWeakReference) {
        synchronized (this) {
            this.activeEngineResources.remove(resourceWeakReference.key);
            if (resourceWeakReference.isCacheable && resourceWeakReference.resource != null) {
                // MONITOREXIT [2, 3] lbl4 : MonitorExitStatement: MONITOREXIT : this
                EngineResource engineResource = new EngineResource(resourceWeakReference.resource, true, false, resourceWeakReference.key, this.listener);
                this.listener.onResourceReleased(resourceWeakReference.key, engineResource);
                return;
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void deactivate(Key object) {
        synchronized (this) {
            object = this.activeEngineResources.remove(object);
            if (object != null) {
                object.reset();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    EngineResource<?> get(Key object) {
        synchronized (this) {
            object = this.activeEngineResources.get(object);
            if (object == null) {
                return null;
            }
            EngineResource engineResource = (EngineResource)object.get();
            if (engineResource == null) {
                this.cleanupActiveReference((ResourceWeakReference)object);
            }
            return engineResource;
        }
    }

    void setDequeuedResourceCallback(DequeuedResourceCallback dequeuedResourceCallback) {
        this.cb = dequeuedResourceCallback;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void setListener(EngineResource.ResourceListener resourceListener) {
        synchronized (resourceListener) {
            synchronized (this) {
                this.listener = resourceListener;
                return;
            }
        }
    }

    void shutdown() {
        this.isShutdown = true;
        Executor executor = this.monitorClearedResourcesExecutor;
        if (executor instanceof ExecutorService) {
            com.bumptech.glide.util.Executors.shutdownAndAwaitTermination((ExecutorService)executor);
        }
    }

    static interface DequeuedResourceCallback {
        public void onResourceDequeued();
    }

    static final class ResourceWeakReference
    extends WeakReference<EngineResource<?>> {
        final boolean isCacheable;
        final Key key;
        Resource<?> resource;

        ResourceWeakReference(Key resource, EngineResource<?> engineResource, ReferenceQueue<? super EngineResource<?>> referenceQueue, boolean bl) {
            super(engineResource, referenceQueue);
            this.key = Preconditions.checkNotNull(resource);
            resource = engineResource.isMemoryCacheable() && bl ? Preconditions.checkNotNull(engineResource.getResource()) : null;
            this.resource = resource;
            this.isCacheable = engineResource.isMemoryCacheable();
        }

        void reset() {
            this.resource = null;
            this.clear();
        }
    }

}

