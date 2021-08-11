package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import java.util.concurrent.atomic.AtomicBoolean;

class LifecycleDispatcher {
   private static AtomicBoolean sInitialized = new AtomicBoolean(false);

   private LifecycleDispatcher() {
   }

   static void init(Context var0) {
      if (!sInitialized.getAndSet(true)) {
         ((Application)var0.getApplicationContext()).registerActivityLifecycleCallbacks(new LifecycleDispatcher.DispatcherActivityCallback());
      }
   }

   static class DispatcherActivityCallback extends EmptyActivityLifecycleCallbacks {
      public void onActivityCreated(Activity var1, Bundle var2) {
         ReportFragment.injectIfNeededIn(var1);
      }

      public void onActivitySaveInstanceState(Activity var1, Bundle var2) {
      }

      public void onActivityStopped(Activity var1) {
      }
   }
}
