package androidx.transition;

import android.graphics.Rect;
import android.view.ViewGroup;

public class CircularPropagation extends VisibilityPropagation {
   private float mPropagationSpeed = 3.0F;

   private static float distance(float var0, float var1, float var2, float var3) {
      var0 = var2 - var0;
      var1 = var3 - var1;
      return (float)Math.sqrt((double)(var0 * var0 + var1 * var1));
   }

   public long getStartDelay(ViewGroup var1, Transition var2, TransitionValues var3, TransitionValues var4) {
      if (var3 == null && var4 == null) {
         return 0L;
      } else {
         byte var6 = 1;
         if (var4 != null && this.getViewVisibility(var3) != 0) {
            var3 = var4;
         } else {
            var6 = -1;
         }

         int var9 = this.getViewX(var3);
         int var10 = this.getViewY(var3);
         Rect var15 = var2.getEpicenter();
         int var7;
         int var8;
         if (var15 != null) {
            var7 = var15.centerX();
            var8 = var15.centerY();
         } else {
            int[] var16 = new int[2];
            var1.getLocationOnScreen(var16);
            var7 = Math.round((float)(var16[0] + var1.getWidth() / 2) + var1.getTranslationX());
            var8 = Math.round((float)(var16[1] + var1.getHeight() / 2) + var1.getTranslationY());
         }

         float var5 = distance((float)var9, (float)var10, (float)var7, (float)var8) / distance(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
         long var13 = var2.getDuration();
         long var11 = var13;
         if (var13 < 0L) {
            var11 = 300L;
         }

         return (long)Math.round((float)((long)var6 * var11) / this.mPropagationSpeed * var5);
      }
   }

   public void setPropagationSpeed(float var1) {
      if (var1 != 0.0F) {
         this.mPropagationSpeed = var1;
      } else {
         throw new IllegalArgumentException("propagationSpeed may not be 0");
      }
   }
}
