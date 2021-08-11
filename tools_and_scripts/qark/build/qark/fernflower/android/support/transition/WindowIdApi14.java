package android.support.transition;

import android.os.IBinder;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
class WindowIdApi14 implements WindowIdImpl {
   private final IBinder mToken;

   WindowIdApi14(IBinder var1) {
      this.mToken = var1;
   }

   public boolean equals(Object var1) {
      return var1 instanceof WindowIdApi14 && ((WindowIdApi14)var1).mToken.equals(this.mToken);
   }

   public int hashCode() {
      return this.mToken.hashCode();
   }
}
