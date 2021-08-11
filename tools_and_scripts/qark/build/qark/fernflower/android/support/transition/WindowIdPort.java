package android.support.transition;

import android.annotation.TargetApi;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(14)
@RequiresApi(14)
class WindowIdPort {
   private final IBinder mToken;

   private WindowIdPort(IBinder var1) {
      this.mToken = var1;
   }

   static WindowIdPort getWindowId(@NonNull View var0) {
      return new WindowIdPort(var0.getWindowToken());
   }

   public boolean equals(Object var1) {
      return var1 instanceof WindowIdPort && ((WindowIdPort)var1).mToken.equals(this.mToken);
   }
}
