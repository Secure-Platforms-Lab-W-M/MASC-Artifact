package androidx.arch.core.executor;

public abstract class TaskExecutor {
   public abstract void executeOnDiskIO(Runnable var1);

   public void executeOnMainThread(Runnable var1) {
      if (this.isMainThread()) {
         var1.run();
      } else {
         this.postToMainThread(var1);
      }
   }

   public abstract boolean isMainThread();

   public abstract void postToMainThread(Runnable var1);
}
