package android.support.v4.view;

import android.view.MotionEvent;

class MotionEventCompatEclair {
   public static int findPointerIndex(MotionEvent var0, int var1) {
      return var0.findPointerIndex(var1);
   }

   public static int getPointerCount(MotionEvent var0) {
      return var0.getPointerCount();
   }

   public static int getPointerId(MotionEvent var0, int var1) {
      return var0.getPointerId(var1);
   }

   public static float getX(MotionEvent var0, int var1) {
      return var0.getX(var1);
   }

   public static float getY(MotionEvent var0, int var1) {
      return var0.getY(var1);
   }
}
