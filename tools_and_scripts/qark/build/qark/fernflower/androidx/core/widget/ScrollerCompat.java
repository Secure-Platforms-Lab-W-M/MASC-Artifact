package androidx.core.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

@Deprecated
public final class ScrollerCompat {
   OverScroller mScroller;

   ScrollerCompat(Context var1, Interpolator var2) {
      OverScroller var3;
      if (var2 != null) {
         var3 = new OverScroller(var1, var2);
      } else {
         var3 = new OverScroller(var1);
      }

      this.mScroller = var3;
   }

   @Deprecated
   public static ScrollerCompat create(Context var0) {
      return create(var0, (Interpolator)null);
   }

   @Deprecated
   public static ScrollerCompat create(Context var0, Interpolator var1) {
      return new ScrollerCompat(var0, var1);
   }

   @Deprecated
   public void abortAnimation() {
      this.mScroller.abortAnimation();
   }

   @Deprecated
   public boolean computeScrollOffset() {
      return this.mScroller.computeScrollOffset();
   }

   @Deprecated
   public void fling(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.mScroller.fling(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   @Deprecated
   public void fling(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      this.mScroller.fling(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   @Deprecated
   public float getCurrVelocity() {
      return this.mScroller.getCurrVelocity();
   }

   @Deprecated
   public int getCurrX() {
      return this.mScroller.getCurrX();
   }

   @Deprecated
   public int getCurrY() {
      return this.mScroller.getCurrY();
   }

   @Deprecated
   public int getFinalX() {
      return this.mScroller.getFinalX();
   }

   @Deprecated
   public int getFinalY() {
      return this.mScroller.getFinalY();
   }

   @Deprecated
   public boolean isFinished() {
      return this.mScroller.isFinished();
   }

   @Deprecated
   public boolean isOverScrolled() {
      return this.mScroller.isOverScrolled();
   }

   @Deprecated
   public void notifyHorizontalEdgeReached(int var1, int var2, int var3) {
      this.mScroller.notifyHorizontalEdgeReached(var1, var2, var3);
   }

   @Deprecated
   public void notifyVerticalEdgeReached(int var1, int var2, int var3) {
      this.mScroller.notifyVerticalEdgeReached(var1, var2, var3);
   }

   @Deprecated
   public boolean springBack(int var1, int var2, int var3, int var4, int var5, int var6) {
      return this.mScroller.springBack(var1, var2, var3, var4, var5, var6);
   }

   @Deprecated
   public void startScroll(int var1, int var2, int var3, int var4) {
      this.mScroller.startScroll(var1, var2, var3, var4);
   }

   @Deprecated
   public void startScroll(int var1, int var2, int var3, int var4, int var5) {
      this.mScroller.startScroll(var1, var2, var3, var4, var5);
   }
}
