// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.util.concurrent.FutureTask;
import java.util.concurrent.Future;
import android.net.NetworkInfo;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

class PicassoExecutorService extends ThreadPoolExecutor
{
    private static final int DEFAULT_THREAD_COUNT = 3;
    
    PicassoExecutorService() {
        super(3, 3, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), new Utils.PicassoThreadFactory());
    }
    
    private void setThreadCount(final int n) {
        this.setCorePoolSize(n);
        this.setMaximumPoolSize(n);
    }
    
    void adjustThreadCount(final NetworkInfo networkInfo) {
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            this.setThreadCount(3);
            return;
        }
        switch (networkInfo.getType()) {
            default: {
                this.setThreadCount(3);
            }
            case 1:
            case 6:
            case 9: {
                this.setThreadCount(4);
            }
            case 0: {
                switch (networkInfo.getSubtype()) {
                    default: {
                        this.setThreadCount(3);
                        return;
                    }
                    case 13:
                    case 14:
                    case 15: {
                        this.setThreadCount(3);
                        return;
                    }
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 12: {
                        this.setThreadCount(2);
                        return;
                    }
                    case 1:
                    case 2: {
                        this.setThreadCount(1);
                        return;
                    }
                }
                break;
            }
        }
    }
    
    @Override
    public Future<?> submit(final Runnable runnable) {
        final PicassoFutureTask picassoFutureTask = new PicassoFutureTask((BitmapHunter)runnable);
        this.execute(picassoFutureTask);
        return picassoFutureTask;
    }
    
    private static final class PicassoFutureTask extends FutureTask<BitmapHunter> implements Comparable<PicassoFutureTask>
    {
        private final BitmapHunter hunter;
        
        public PicassoFutureTask(final BitmapHunter hunter) {
            super(hunter, null);
            this.hunter = hunter;
        }
        
        @Override
        public int compareTo(final PicassoFutureTask picassoFutureTask) {
            final Picasso.Priority priority = this.hunter.getPriority();
            final Picasso.Priority priority2 = picassoFutureTask.hunter.getPriority();
            if (priority == priority2) {
                return this.hunter.sequence - picassoFutureTask.hunter.sequence;
            }
            return priority2.ordinal() - priority.ordinal();
        }
    }
}
