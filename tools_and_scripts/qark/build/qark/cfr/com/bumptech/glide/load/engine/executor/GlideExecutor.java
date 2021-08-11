/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Process
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.os.StrictMode$ThreadPolicy$Builder
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.bumptech.glide.load.engine.executor;

import android.os.Process;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.load.engine.executor.RuntimeCompat;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class GlideExecutor
implements ExecutorService {
    private static final String DEFAULT_ANIMATION_EXECUTOR_NAME = "animation";
    private static final String DEFAULT_DISK_CACHE_EXECUTOR_NAME = "disk-cache";
    private static final int DEFAULT_DISK_CACHE_EXECUTOR_THREADS = 1;
    private static final String DEFAULT_SOURCE_EXECUTOR_NAME = "source";
    private static final String DEFAULT_SOURCE_UNLIMITED_EXECUTOR_NAME = "source-unlimited";
    private static final long KEEP_ALIVE_TIME_MS = TimeUnit.SECONDS.toMillis(10L);
    private static final int MAXIMUM_AUTOMATIC_THREAD_COUNT = 4;
    private static final String TAG = "GlideExecutor";
    private static volatile int bestThreadCount;
    private final ExecutorService delegate;

    GlideExecutor(ExecutorService executorService) {
        this.delegate = executorService;
    }

    public static int calculateBestThreadCount() {
        if (bestThreadCount == 0) {
            bestThreadCount = Math.min(4, RuntimeCompat.availableProcessors());
        }
        return bestThreadCount;
    }

    public static Builder newAnimationBuilder() {
        int n = GlideExecutor.calculateBestThreadCount() >= 4 ? 2 : 1;
        return new Builder(true).setThreadCount(n).setName("animation");
    }

    public static GlideExecutor newAnimationExecutor() {
        return GlideExecutor.newAnimationBuilder().build();
    }

    @Deprecated
    public static GlideExecutor newAnimationExecutor(int n, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return GlideExecutor.newAnimationBuilder().setThreadCount(n).setUncaughtThrowableStrategy(uncaughtThrowableStrategy).build();
    }

    public static Builder newDiskCacheBuilder() {
        return new Builder(true).setThreadCount(1).setName("disk-cache");
    }

    public static GlideExecutor newDiskCacheExecutor() {
        return GlideExecutor.newDiskCacheBuilder().build();
    }

    @Deprecated
    public static GlideExecutor newDiskCacheExecutor(int n, String string2, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return GlideExecutor.newDiskCacheBuilder().setThreadCount(n).setName(string2).setUncaughtThrowableStrategy(uncaughtThrowableStrategy).build();
    }

    @Deprecated
    public static GlideExecutor newDiskCacheExecutor(UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return GlideExecutor.newDiskCacheBuilder().setUncaughtThrowableStrategy(uncaughtThrowableStrategy).build();
    }

    public static Builder newSourceBuilder() {
        return new Builder(false).setThreadCount(GlideExecutor.calculateBestThreadCount()).setName("source");
    }

    public static GlideExecutor newSourceExecutor() {
        return GlideExecutor.newSourceBuilder().build();
    }

    @Deprecated
    public static GlideExecutor newSourceExecutor(int n, String string2, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return GlideExecutor.newSourceBuilder().setThreadCount(n).setName(string2).setUncaughtThrowableStrategy(uncaughtThrowableStrategy).build();
    }

    @Deprecated
    public static GlideExecutor newSourceExecutor(UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return GlideExecutor.newSourceBuilder().setUncaughtThrowableStrategy(uncaughtThrowableStrategy).build();
    }

    public static GlideExecutor newUnlimitedSourceExecutor() {
        return new GlideExecutor(new ThreadPoolExecutor(0, Integer.MAX_VALUE, KEEP_ALIVE_TIME_MS, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), new DefaultThreadFactory("source-unlimited", UncaughtThrowableStrategy.DEFAULT, false)));
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        return this.delegate.awaitTermination(l, timeUnit);
    }

    @Override
    public void execute(Runnable runnable) {
        this.delegate.execute(runnable);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        return this.delegate.invokeAll(collection);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException {
        return this.delegate.invokeAll(collection, l, timeUnit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
        return this.delegate.invokeAny(collection);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.delegate.invokeAny(collection, l, timeUnit);
    }

    @Override
    public boolean isShutdown() {
        return this.delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.delegate.isTerminated();
    }

    @Override
    public void shutdown() {
        this.delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return this.delegate.shutdownNow();
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return this.delegate.submit(runnable);
    }

    @Override
    public <T> Future<T> submit(Runnable runnable, T t) {
        return this.delegate.submit(runnable, t);
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return this.delegate.submit(callable);
    }

    public String toString() {
        return this.delegate.toString();
    }

    public static final class Builder {
        public static final long NO_THREAD_TIMEOUT = 0L;
        private int corePoolSize;
        private int maximumPoolSize;
        private String name;
        private final boolean preventNetworkOperations;
        private long threadTimeoutMillis;
        private UncaughtThrowableStrategy uncaughtThrowableStrategy = UncaughtThrowableStrategy.DEFAULT;

        Builder(boolean bl) {
            this.preventNetworkOperations = bl;
        }

        public GlideExecutor build() {
            if (!TextUtils.isEmpty((CharSequence)this.name)) {
                ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize, this.threadTimeoutMillis, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), new DefaultThreadFactory(this.name, this.uncaughtThrowableStrategy, this.preventNetworkOperations));
                if (this.threadTimeoutMillis != 0L) {
                    threadPoolExecutor.allowCoreThreadTimeOut(true);
                }
                return new GlideExecutor(threadPoolExecutor);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Name must be non-null and non-empty, but given: ");
            stringBuilder.append(this.name);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder setName(String string2) {
            this.name = string2;
            return this;
        }

        public Builder setThreadCount(int n) {
            this.corePoolSize = n;
            this.maximumPoolSize = n;
            return this;
        }

        public Builder setThreadTimeoutMillis(long l) {
            this.threadTimeoutMillis = l;
            return this;
        }

        public Builder setUncaughtThrowableStrategy(UncaughtThrowableStrategy uncaughtThrowableStrategy) {
            this.uncaughtThrowableStrategy = uncaughtThrowableStrategy;
            return this;
        }
    }

    private static final class DefaultThreadFactory
    implements ThreadFactory {
        private static final int DEFAULT_PRIORITY = 9;
        private final String name;
        final boolean preventNetworkOperations;
        private int threadNum;
        final UncaughtThrowableStrategy uncaughtThrowableStrategy;

        DefaultThreadFactory(String string2, UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean bl) {
            this.name = string2;
            this.uncaughtThrowableStrategy = uncaughtThrowableStrategy;
            this.preventNetworkOperations = bl;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            synchronized (this) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("glide-");
                stringBuilder.append(this.name);
                stringBuilder.append("-thread-");
                stringBuilder.append(this.threadNum);
                runnable = new Thread(runnable, stringBuilder.toString()){

                    @Override
                    public void run() {
                        Process.setThreadPriority((int)9);
                        if (DefaultThreadFactory.this.preventNetworkOperations) {
                            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyDeath().build());
                        }
                        try {
                            super.run();
                            return;
                        }
                        catch (Throwable throwable) {
                            DefaultThreadFactory.this.uncaughtThrowableStrategy.handle(throwable);
                            return;
                        }
                    }
                };
                ++this.threadNum;
                return runnable;
            }
        }

    }

    public static interface UncaughtThrowableStrategy {
        public static final UncaughtThrowableStrategy DEFAULT;
        public static final UncaughtThrowableStrategy IGNORE;
        public static final UncaughtThrowableStrategy LOG;
        public static final UncaughtThrowableStrategy THROW;

        static {
            IGNORE = new UncaughtThrowableStrategy(){

                @Override
                public void handle(Throwable throwable) {
                }
            };
            LOG = new UncaughtThrowableStrategy(){

                @Override
                public void handle(Throwable throwable) {
                    if (throwable != null && Log.isLoggable((String)"GlideExecutor", (int)6)) {
                        Log.e((String)"GlideExecutor", (String)"Request threw uncaught throwable", (Throwable)throwable);
                    }
                }
            };
            THROW = new UncaughtThrowableStrategy(){

                @Override
                public void handle(Throwable throwable) {
                    if (throwable == null) {
                        return;
                    }
                    throw new RuntimeException("Request threw uncaught throwable", throwable);
                }
            };
            DEFAULT = LOG;
        }

        public void handle(Throwable var1);

    }

}

