package android.support.v4.view;

import android.view.VelocityTracker;

@Deprecated
public final class VelocityTrackerCompat {
   private VelocityTrackerCompat() {
   }

   @Deprecated
   public static float getXVelocity(VelocityTracker var0, int var1) {
      return var0.getXVelocity(var1);
   }

   @Deprecated
   public static float getYVelocity(VelocityTracker var0, int var1) {
      return var0.getYVelocity(var1);
   }
}
