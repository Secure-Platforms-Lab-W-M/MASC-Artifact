package androidx.recyclerview.widget;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class AsyncDifferConfig {
   private final Executor mBackgroundThreadExecutor;
   private final DiffUtil.ItemCallback mDiffCallback;
   private final Executor mMainThreadExecutor;

   AsyncDifferConfig(Executor var1, Executor var2, DiffUtil.ItemCallback var3) {
      this.mMainThreadExecutor = var1;
      this.mBackgroundThreadExecutor = var2;
      this.mDiffCallback = var3;
   }

   public Executor getBackgroundThreadExecutor() {
      return this.mBackgroundThreadExecutor;
   }

   public DiffUtil.ItemCallback getDiffCallback() {
      return this.mDiffCallback;
   }

   public Executor getMainThreadExecutor() {
      return this.mMainThreadExecutor;
   }

   public static final class Builder {
      private static Executor sDiffExecutor = null;
      private static final Object sExecutorLock = new Object();
      private Executor mBackgroundThreadExecutor;
      private final DiffUtil.ItemCallback mDiffCallback;
      private Executor mMainThreadExecutor;

      public Builder(DiffUtil.ItemCallback var1) {
         this.mDiffCallback = var1;
      }

      public AsyncDifferConfig build() {
         if (this.mBackgroundThreadExecutor == null) {
            Object var1 = sExecutorLock;
            synchronized(var1){}

            label164: {
               Throwable var10000;
               boolean var10001;
               label157: {
                  try {
                     if (sDiffExecutor == null) {
                        sDiffExecutor = Executors.newFixedThreadPool(2);
                     }
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label157;
                  }

                  label154:
                  try {
                     break label164;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label154;
                  }
               }

               while(true) {
                  Throwable var2 = var10000;

                  try {
                     throw var2;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     continue;
                  }
               }
            }

            this.mBackgroundThreadExecutor = sDiffExecutor;
         }

         return new AsyncDifferConfig(this.mMainThreadExecutor, this.mBackgroundThreadExecutor, this.mDiffCallback);
      }

      public AsyncDifferConfig.Builder setBackgroundThreadExecutor(Executor var1) {
         this.mBackgroundThreadExecutor = var1;
         return this;
      }

      public AsyncDifferConfig.Builder setMainThreadExecutor(Executor var1) {
         this.mMainThreadExecutor = var1;
         return this;
      }
   }
}
