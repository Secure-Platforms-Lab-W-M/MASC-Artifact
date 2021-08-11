/*
 * Decompiled with CFR 0_124.
 */
package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ComputableLiveData<T> {
    final AtomicBoolean mComputing = new AtomicBoolean(false);
    final Executor mExecutor;
    final AtomicBoolean mInvalid = new AtomicBoolean(true);
    final Runnable mInvalidationRunnable;
    final LiveData<T> mLiveData;
    final Runnable mRefreshRunnable;

    public ComputableLiveData() {
        this(ArchTaskExecutor.getIOThreadExecutor());
    }

    public ComputableLiveData(Executor executor) {
        this.mRefreshRunnable = new Runnable(){

            @Override
            public void run() {
                boolean bl;
                do {
                    bl = false;
                    boolean bl2 = false;
                    if (!ComputableLiveData.this.mComputing.compareAndSet(false, true)) continue;
                    Object t = null;
                    bl = bl2;
                    do {
                        if (!ComputableLiveData.this.mInvalid.compareAndSet(true, false)) break;
                        bl = true;
                        t = ComputableLiveData.this.compute();
                        continue;
                        break;
                    } while (true);
                    if (!bl) continue;
                    try {
                        ComputableLiveData.this.mLiveData.postValue(t);
                    }
                    catch (Throwable throwable) {
                        throw throwable;
                    }
                    finally {
                        ComputableLiveData.this.mComputing.set(false);
                    }
                } while (bl && ComputableLiveData.this.mInvalid.get());
            }
        };
        this.mInvalidationRunnable = new Runnable(){

            @Override
            public void run() {
                boolean bl = ComputableLiveData.this.mLiveData.hasActiveObservers();
                if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && bl) {
                    ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
                }
            }
        };
        this.mExecutor = executor;
        this.mLiveData = new LiveData<T>(){

            @Override
            protected void onActive() {
                ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
            }
        };
    }

    protected abstract T compute();

    public LiveData<T> getLiveData() {
        return this.mLiveData;
    }

    public void invalidate() {
        ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
    }

}

