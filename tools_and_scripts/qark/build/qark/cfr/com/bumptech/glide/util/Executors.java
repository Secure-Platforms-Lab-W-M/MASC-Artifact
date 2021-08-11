/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 */
package com.bumptech.glide.util;

import android.os.Handler;
import android.os.Looper;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class Executors {
    private static final Executor DIRECT_EXECUTOR;
    private static final Executor MAIN_THREAD_EXECUTOR;

    static {
        MAIN_THREAD_EXECUTOR = new Executor(){
            private final Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(Runnable runnable) {
                this.handler.post(runnable);
            }
        };
        DIRECT_EXECUTOR = new Executor(){

            @Override
            public void execute(Runnable runnable) {
                runnable.run();
            }
        };
    }

    private Executors() {
    }

    public static Executor directExecutor() {
        return DIRECT_EXECUTOR;
    }

    public static Executor mainThreadExecutor() {
        return MAIN_THREAD_EXECUTOR;
    }

    public static void shutdownAndAwaitTermination(ExecutorService executorService) {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(5L, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (executorService.awaitTermination(5L, TimeUnit.SECONDS)) {
                    return;
                }
                throw new RuntimeException("Failed to shutdown");
            }
            return;
        }
        catch (InterruptedException interruptedException) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            throw new RuntimeException(interruptedException);
        }
    }

}

