package android.support.v4.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

class ScrollerCompatGingerbread {
   public static void abortAnimation(Object var0) {
      ((OverScroller)var0).abortAnimation();
   }

   public static boolean computeScrollOffset(Object var0) {
      return ((OverScroller)var0).computeScrollOffset();
   }

   public static Object createScroller(Context var0, Interpolator var1) {
      return var1 != null ? new OverScroller(var0, var1) : new OverScroller(var0);
   }

   public static void fling(Object var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      ((OverScroller)var0).fling(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public static void fling(Object var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      ((OverScroller)var0).fling(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static int getCurrX(Object var0) {
      return ((OverScroller)var0).getCurrX();
   }

   public static int getCurrY(Object var0) {
      return ((OverScroller)var0).getCurrY();
   }

   public static int getFinalX(Object var0) {
      return ((OverScroller)var0).getFinalX();
   }

   public static int getFinalY(Object var0) {
      return ((OverScroller)var0).getFinalY();
   }

   public static boolean isFinished(Object var0) {
      return ((OverScroller)var0).isFinished();
   }

   public static boolean isOverScrolled(Object var0) {
      return ((OverScroller)var0).isOverScrolled();
   }

   public static void notifyHorizontalEdgeReached(Object var0, int var1, int var2, int var3) {
      ((OverScroller)var0).notifyHorizontalEdgeReached(var1, var2, var3);
   }

   public static void notifyVerticalEdgeReached(Object var0, int var1, int var2, int var3) {
      ((OverScroller)var0).notifyVerticalEdgeReached(var1, var2, var3);
   }

   public static boolean springBack(Object var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      return ((OverScroller)var0).springBack(var1, var2, var3, var4, var5, var6);
   }

   public static void startScroll(Object var0, int var1, int var2, int var3, int var4) {
      ((OverScroller)var0).startScroll(var1, var2, var3, var4);
   }

   public static void startScroll(Object var0, int var1, int var2, int var3, int var4, int var5) {
      ((OverScroller)var0).startScroll(var1, var2, var3, var4, var5);
   }
}
