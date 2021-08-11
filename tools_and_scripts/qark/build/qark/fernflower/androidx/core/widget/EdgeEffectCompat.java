package androidx.core.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.widget.EdgeEffect;

public final class EdgeEffectCompat {
   private EdgeEffect mEdgeEffect;

   @Deprecated
   public EdgeEffectCompat(Context var1) {
      this.mEdgeEffect = new EdgeEffect(var1);
   }

   public static void onPull(EdgeEffect var0, float var1, float var2) {
      if (VERSION.SDK_INT >= 21) {
         var0.onPull(var1, var2);
      } else {
         var0.onPull(var1);
      }
   }

   @Deprecated
   public boolean draw(Canvas var1) {
      return this.mEdgeEffect.draw(var1);
   }

   @Deprecated
   public void finish() {
      this.mEdgeEffect.finish();
   }

   @Deprecated
   public boolean isFinished() {
      return this.mEdgeEffect.isFinished();
   }

   @Deprecated
   public boolean onAbsorb(int var1) {
      this.mEdgeEffect.onAbsorb(var1);
      return true;
   }

   @Deprecated
   public boolean onPull(float var1) {
      this.mEdgeEffect.onPull(var1);
      return true;
   }

   @Deprecated
   public boolean onPull(float var1, float var2) {
      onPull(this.mEdgeEffect, var1, var2);
      return true;
   }

   @Deprecated
   public boolean onRelease() {
      this.mEdgeEffect.onRelease();
      return this.mEdgeEffect.isFinished();
   }

   @Deprecated
   public void setSize(int var1, int var2) {
      this.mEdgeEffect.setSize(var1, var2);
   }
}
