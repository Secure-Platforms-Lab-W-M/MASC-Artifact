package androidx.arch.core.executor;

import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultTaskExecutor extends TaskExecutor {
   private final ExecutorService mDiskIO = Executors.newFixedThreadPool(4, new ThreadFactory() {
      private static final String THREAD_NAME_STEM = "arch_disk_io_%d";
      private final AtomicInteger mThreadId = new AtomicInteger(0);

      public Thread newThread(Runnable var1) {
         Thread var2 = new Thread(var1);
         var2.setName(String.format("arch_disk_io_%d", this.mThreadId.getAndIncrement()));
         return var2;
      }
   });
   private final Object mLock = new Object();
   private volatile Handler mMainHandler;

   private static Handler createAsync(Looper var0) {
      if (VERSION.SDK_INT >= 28) {
         return Handler.createAsync(var0);
      } else {
         if (VERSION.SDK_INT >= 16) {
            try {
               Handler var1 = (Handler)Handler.class.getDeclaredConstructor(Looper.class, Callback.class, Boolean.TYPE).newInstance(var0, null, true);
               return var1;
            } catch (IllegalAccessException var2) {
            } catch (InstantiationException var3) {
            } catch (NoSuchMethodException var4) {
            } catch (InvocationTargetException var5) {
               return new Handler(var0);
            }
         }

         return new Handler(var0);
      }
   }

   public void executeOnDiskIO(Runnable var1) {
      this.mDiskIO.execute(var1);
   }

   public boolean isMainThread() {
      return Looper.getMainLooper().getThread() == Thread.currentThread();
   }

   public void postToMainThread(Runnable var1) {
      if (this.mMainHandler == null) {
         label150: {
            Object var2 = this.mLock;
            synchronized(var2){}

            Throwable var10000;
            boolean var10001;
            label144: {
               try {
                  if (this.mMainHandler == null) {
                     this.mMainHandler = createAsync(Looper.getMainLooper());
                  }
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label144;
               }

               label141:
               try {
                  break label150;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label141;
               }
            }

            while(true) {
               Throwable var15 = var10000;

               try {
                  throw var15;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      this.mMainHandler.post(var1);
   }
}
