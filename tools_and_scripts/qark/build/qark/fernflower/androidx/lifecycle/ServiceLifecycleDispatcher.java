package androidx.lifecycle;

import android.os.Handler;

public class ServiceLifecycleDispatcher {
   private final Handler mHandler;
   private ServiceLifecycleDispatcher.DispatchRunnable mLastDispatchRunnable;
   private final LifecycleRegistry mRegistry;

   public ServiceLifecycleDispatcher(LifecycleOwner var1) {
      this.mRegistry = new LifecycleRegistry(var1);
      this.mHandler = new Handler();
   }

   private void postDispatchRunnable(Lifecycle.Event var1) {
      ServiceLifecycleDispatcher.DispatchRunnable var2 = this.mLastDispatchRunnable;
      if (var2 != null) {
         var2.run();
      }

      ServiceLifecycleDispatcher.DispatchRunnable var3 = new ServiceLifecycleDispatcher.DispatchRunnable(this.mRegistry, var1);
      this.mLastDispatchRunnable = var3;
      this.mHandler.postAtFrontOfQueue(var3);
   }

   public Lifecycle getLifecycle() {
      return this.mRegistry;
   }

   public void onServicePreSuperOnBind() {
      this.postDispatchRunnable(Lifecycle.Event.ON_START);
   }

   public void onServicePreSuperOnCreate() {
      this.postDispatchRunnable(Lifecycle.Event.ON_CREATE);
   }

   public void onServicePreSuperOnDestroy() {
      this.postDispatchRunnable(Lifecycle.Event.ON_STOP);
      this.postDispatchRunnable(Lifecycle.Event.ON_DESTROY);
   }

   public void onServicePreSuperOnStart() {
      this.postDispatchRunnable(Lifecycle.Event.ON_START);
   }

   static class DispatchRunnable implements Runnable {
      final Lifecycle.Event mEvent;
      private final LifecycleRegistry mRegistry;
      private boolean mWasExecuted = false;

      DispatchRunnable(LifecycleRegistry var1, Lifecycle.Event var2) {
         this.mRegistry = var1;
         this.mEvent = var2;
      }

      public void run() {
         if (!this.mWasExecuted) {
            this.mRegistry.handleLifecycleEvent(this.mEvent);
            this.mWasExecuted = true;
         }

      }
   }
}
