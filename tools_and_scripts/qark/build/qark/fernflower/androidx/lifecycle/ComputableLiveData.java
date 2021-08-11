package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ComputableLiveData {
   final AtomicBoolean mComputing;
   final Executor mExecutor;
   final AtomicBoolean mInvalid;
   final Runnable mInvalidationRunnable;
   final LiveData mLiveData;
   final Runnable mRefreshRunnable;

   public ComputableLiveData() {
      this(ArchTaskExecutor.getIOThreadExecutor());
   }

   public ComputableLiveData(Executor var1) {
      this.mInvalid = new AtomicBoolean(true);
      this.mComputing = new AtomicBoolean(false);
      this.mRefreshRunnable = new Runnable() {
         public void run() {
            boolean var1;
            label196:
            do {
               var1 = false;
               boolean var2 = false;
               if (ComputableLiveData.this.mComputing.compareAndSet(false, true)) {
                  Object var3 = null;
                  var1 = var2;

                  Throwable var10000;
                  while(true) {
                     boolean var10001;
                     label200: {
                        try {
                           if (ComputableLiveData.this.mInvalid.compareAndSet(true, false)) {
                              break label200;
                           }
                        } catch (Throwable var15) {
                           var10000 = var15;
                           var10001 = false;
                           break;
                        }

                        if (var1) {
                           try {
                              ComputableLiveData.this.mLiveData.postValue(var3);
                           } catch (Throwable var14) {
                              var10000 = var14;
                              var10001 = false;
                              break;
                           }
                        }

                        ComputableLiveData.this.mComputing.set(false);
                        continue label196;
                     }

                     var1 = true;

                     try {
                        var3 = ComputableLiveData.this.compute();
                     } catch (Throwable var13) {
                        var10000 = var13;
                        var10001 = false;
                        break;
                     }
                  }

                  Throwable var16 = var10000;
                  ComputableLiveData.this.mComputing.set(false);
                  throw var16;
               }
            } while(var1 && ComputableLiveData.this.mInvalid.get());

         }
      };
      this.mInvalidationRunnable = new Runnable() {
         public void run() {
            boolean var1 = ComputableLiveData.this.mLiveData.hasActiveObservers();
            if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && var1) {
               ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
            }

         }
      };
      this.mExecutor = var1;
      this.mLiveData = new LiveData() {
         protected void onActive() {
            ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
         }
      };
   }

   protected abstract Object compute();

   public LiveData getLiveData() {
      return this.mLiveData;
   }

   public void invalidate() {
      ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
   }
}
