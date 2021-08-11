package android.arch.core.executor;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class TaskExecutor {
   public abstract void executeOnDiskIO(@NonNull Runnable var1);

   public void executeOnMainThread(@NonNull Runnable var1) {
      if (this.isMainThread()) {
         var1.run();
      } else {
         this.postToMainThread(var1);
      }
   }

   public abstract boolean isMainThread();

   public abstract void postToMainThread(@NonNull Runnable var1);
}
