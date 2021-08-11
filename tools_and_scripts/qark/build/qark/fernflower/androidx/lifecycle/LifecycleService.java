package androidx.lifecycle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LifecycleService extends Service implements LifecycleOwner {
   private final ServiceLifecycleDispatcher mDispatcher = new ServiceLifecycleDispatcher(this);

   public Lifecycle getLifecycle() {
      return this.mDispatcher.getLifecycle();
   }

   public IBinder onBind(Intent var1) {
      this.mDispatcher.onServicePreSuperOnBind();
      return null;
   }

   public void onCreate() {
      this.mDispatcher.onServicePreSuperOnCreate();
      super.onCreate();
   }

   public void onDestroy() {
      this.mDispatcher.onServicePreSuperOnDestroy();
      super.onDestroy();
   }

   public void onStart(Intent var1, int var2) {
      this.mDispatcher.onServicePreSuperOnStart();
      super.onStart(var1, var2);
   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      return super.onStartCommand(var1, var2, var3);
   }
}
