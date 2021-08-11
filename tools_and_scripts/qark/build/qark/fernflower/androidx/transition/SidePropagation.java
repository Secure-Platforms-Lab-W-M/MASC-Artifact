package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;

public class SidePropagation extends VisibilityPropagation {
   private float mPropagationSpeed = 3.0F;
   private int mSide = 80;

   private int distance(View var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      int var12 = this.mSide;
      boolean var11 = false;
      boolean var10 = false;
      int var13;
      if (var12 == 8388611) {
         if (ViewCompat.getLayoutDirection(var1) == 1) {
            var10 = true;
         }

         if (var10) {
            var13 = 5;
         } else {
            var13 = 3;
         }
      } else if (var12 == 8388613) {
         var10 = var11;
         if (ViewCompat.getLayoutDirection(var1) == 1) {
            var10 = true;
         }

         if (var10) {
            var13 = 3;
         } else {
            var13 = 5;
         }
      } else {
         var13 = this.mSide;
      }

      if (var13 != 3) {
         if (var13 != 5) {
            if (var13 != 48) {
               return var13 != 80 ? 0 : var3 - var7 + Math.abs(var4 - var2);
            } else {
               return var9 - var3 + Math.abs(var4 - var2);
            }
         } else {
            return var2 - var6 + Math.abs(var5 - var3);
         }
      } else {
         return var8 - var2 + Math.abs(var5 - var3);
      }
   }

   private int getMaxDistance(ViewGroup var1) {
      int var2 = this.mSide;
      return var2 != 3 && var2 != 5 && var2 != 8388611 && var2 != 8388613 ? var1.getHeight() : var1.getWidth();
   }

   public long getStartDelay(ViewGroup var1, Transition var2, TransitionValues var3, TransitionValues var4) {
      if (var3 == null && var4 == null) {
         return 0L;
      } else {
         Rect var19 = var2.getEpicenter();
         byte var6;
         if (var4 != null && this.getViewVisibility(var3) != 0) {
            var6 = 1;
            var3 = var4;
         } else {
            var6 = -1;
         }

         int var9 = this.getViewX(var3);
         int var10 = this.getViewY(var3);
         int[] var20 = new int[2];
         var1.getLocationOnScreen(var20);
         int var11 = var20[0] + Math.round(var1.getTranslationX());
         int var12 = var20[1] + Math.round(var1.getTranslationY());
         int var13 = var11 + var1.getWidth();
         int var14 = var12 + var1.getHeight();
         int var7;
         int var8;
         if (var19 != null) {
            var7 = var19.centerX();
            var8 = var19.centerY();
         } else {
            var7 = (var11 + var13) / 2;
            var8 = (var12 + var14) / 2;
         }

         float var5 = (float)this.distance(var1, var9, var10, var7, var8, var11, var12, var13, var14) / (float)this.getMaxDistance(var1);
         long var17 = var2.getDuration();
         long var15 = var17;
         if (var17 < 0L) {
            var15 = 300L;
         }

         return (long)Math.round((float)((long)var6 * var15) / this.mPropagationSpeed * var5);
      }
   }

   public void setPropagationSpeed(float var1) {
      if (var1 != 0.0F) {
         this.mPropagationSpeed = var1;
      } else {
         throw new IllegalArgumentException("propagationSpeed may not be 0");
      }
   }

   public void setSide(int var1) {
      this.mSide = var1;
   }
}
