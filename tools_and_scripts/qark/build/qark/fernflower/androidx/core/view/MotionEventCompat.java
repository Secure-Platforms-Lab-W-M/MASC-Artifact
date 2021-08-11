package androidx.core.view;

import android.view.MotionEvent;

public final class MotionEventCompat {
   @Deprecated
   public static final int ACTION_HOVER_ENTER = 9;
   @Deprecated
   public static final int ACTION_HOVER_EXIT = 10;
   @Deprecated
   public static final int ACTION_HOVER_MOVE = 7;
   @Deprecated
   public static final int ACTION_MASK = 255;
   @Deprecated
   public static final int ACTION_POINTER_DOWN = 5;
   @Deprecated
   public static final int ACTION_POINTER_INDEX_MASK = 65280;
   @Deprecated
   public static final int ACTION_POINTER_INDEX_SHIFT = 8;
   @Deprecated
   public static final int ACTION_POINTER_UP = 6;
   @Deprecated
   public static final int ACTION_SCROLL = 8;
   @Deprecated
   public static final int AXIS_BRAKE = 23;
   @Deprecated
   public static final int AXIS_DISTANCE = 24;
   @Deprecated
   public static final int AXIS_GAS = 22;
   @Deprecated
   public static final int AXIS_GENERIC_1 = 32;
   @Deprecated
   public static final int AXIS_GENERIC_10 = 41;
   @Deprecated
   public static final int AXIS_GENERIC_11 = 42;
   @Deprecated
   public static final int AXIS_GENERIC_12 = 43;
   @Deprecated
   public static final int AXIS_GENERIC_13 = 44;
   @Deprecated
   public static final int AXIS_GENERIC_14 = 45;
   @Deprecated
   public static final int AXIS_GENERIC_15 = 46;
   @Deprecated
   public static final int AXIS_GENERIC_16 = 47;
   @Deprecated
   public static final int AXIS_GENERIC_2 = 33;
   @Deprecated
   public static final int AXIS_GENERIC_3 = 34;
   @Deprecated
   public static final int AXIS_GENERIC_4 = 35;
   @Deprecated
   public static final int AXIS_GENERIC_5 = 36;
   @Deprecated
   public static final int AXIS_GENERIC_6 = 37;
   @Deprecated
   public static final int AXIS_GENERIC_7 = 38;
   @Deprecated
   public static final int AXIS_GENERIC_8 = 39;
   @Deprecated
   public static final int AXIS_GENERIC_9 = 40;
   @Deprecated
   public static final int AXIS_HAT_X = 15;
   @Deprecated
   public static final int AXIS_HAT_Y = 16;
   @Deprecated
   public static final int AXIS_HSCROLL = 10;
   @Deprecated
   public static final int AXIS_LTRIGGER = 17;
   @Deprecated
   public static final int AXIS_ORIENTATION = 8;
   @Deprecated
   public static final int AXIS_PRESSURE = 2;
   public static final int AXIS_RELATIVE_X = 27;
   public static final int AXIS_RELATIVE_Y = 28;
   @Deprecated
   public static final int AXIS_RTRIGGER = 18;
   @Deprecated
   public static final int AXIS_RUDDER = 20;
   @Deprecated
   public static final int AXIS_RX = 12;
   @Deprecated
   public static final int AXIS_RY = 13;
   @Deprecated
   public static final int AXIS_RZ = 14;
   public static final int AXIS_SCROLL = 26;
   @Deprecated
   public static final int AXIS_SIZE = 3;
   @Deprecated
   public static final int AXIS_THROTTLE = 19;
   @Deprecated
   public static final int AXIS_TILT = 25;
   @Deprecated
   public static final int AXIS_TOOL_MAJOR = 6;
   @Deprecated
   public static final int AXIS_TOOL_MINOR = 7;
   @Deprecated
   public static final int AXIS_TOUCH_MAJOR = 4;
   @Deprecated
   public static final int AXIS_TOUCH_MINOR = 5;
   @Deprecated
   public static final int AXIS_VSCROLL = 9;
   @Deprecated
   public static final int AXIS_WHEEL = 21;
   @Deprecated
   public static final int AXIS_X = 0;
   @Deprecated
   public static final int AXIS_Y = 1;
   @Deprecated
   public static final int AXIS_Z = 11;
   @Deprecated
   public static final int BUTTON_PRIMARY = 1;

   private MotionEventCompat() {
   }

   @Deprecated
   public static int findPointerIndex(MotionEvent var0, int var1) {
      return var0.findPointerIndex(var1);
   }

   @Deprecated
   public static int getActionIndex(MotionEvent var0) {
      return var0.getActionIndex();
   }

   @Deprecated
   public static int getActionMasked(MotionEvent var0) {
      return var0.getActionMasked();
   }

   @Deprecated
   public static float getAxisValue(MotionEvent var0, int var1) {
      return var0.getAxisValue(var1);
   }

   @Deprecated
   public static float getAxisValue(MotionEvent var0, int var1, int var2) {
      return var0.getAxisValue(var1, var2);
   }

   @Deprecated
   public static int getButtonState(MotionEvent var0) {
      return var0.getButtonState();
   }

   @Deprecated
   public static int getPointerCount(MotionEvent var0) {
      return var0.getPointerCount();
   }

   @Deprecated
   public static int getPointerId(MotionEvent var0, int var1) {
      return var0.getPointerId(var1);
   }

   @Deprecated
   public static int getSource(MotionEvent var0) {
      return var0.getSource();
   }

   @Deprecated
   public static float getX(MotionEvent var0, int var1) {
      return var0.getX(var1);
   }

   @Deprecated
   public static float getY(MotionEvent var0, int var1) {
      return var0.getY(var1);
   }

   public static boolean isFromSource(MotionEvent var0, int var1) {
      return (var0.getSource() & var1) == var1;
   }
}
