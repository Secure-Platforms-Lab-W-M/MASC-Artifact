package android.support.transition;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewParent;

@RequiresApi(14)
class ViewUtilsApi14 implements ViewUtilsImpl {
   private float[] mMatrixValues;

   public void clearNonTransitionAlpha(@NonNull View var1) {
      if (var1.getVisibility() == 0) {
         var1.setTag(R$id.save_non_transition_alpha, (Object)null);
      }
   }

   public ViewOverlayImpl getOverlay(@NonNull View var1) {
      return ViewOverlayApi14.createFrom(var1);
   }

   public float getTransitionAlpha(@NonNull View var1) {
      Float var2 = (Float)var1.getTag(R$id.save_non_transition_alpha);
      return var2 != null ? var1.getAlpha() / var2 : var1.getAlpha();
   }

   public WindowIdImpl getWindowId(@NonNull View var1) {
      return new WindowIdApi14(var1.getWindowToken());
   }

   public void saveNonTransitionAlpha(@NonNull View var1) {
      if (var1.getTag(R$id.save_non_transition_alpha) == null) {
         var1.setTag(R$id.save_non_transition_alpha, var1.getAlpha());
      }
   }

   public void setAnimationMatrix(@NonNull View var1, Matrix var2) {
      if (var2 != null && !var2.isIdentity()) {
         float[] var9 = this.mMatrixValues;
         if (var9 == null) {
            float[] var10 = new float[9];
            var9 = var10;
            this.mMatrixValues = var10;
         }

         var2.getValues(var9);
         float var3 = var9[3];
         float var4 = (float)Math.sqrt((double)(1.0F - var3 * var3));
         byte var8;
         if (var9[0] < 0.0F) {
            var8 = -1;
         } else {
            var8 = 1;
         }

         float var5 = var4 * (float)var8;
         var3 = (float)Math.toDegrees(Math.atan2((double)var3, (double)var5));
         var4 = var9[0] / var5;
         var5 = var9[4] / var5;
         float var6 = var9[2];
         float var7 = var9[5];
         var1.setPivotX(0.0F);
         var1.setPivotY(0.0F);
         var1.setTranslationX(var6);
         var1.setTranslationY(var7);
         var1.setRotation(var3);
         var1.setScaleX(var4);
         var1.setScaleY(var5);
      } else {
         var1.setPivotX((float)(var1.getWidth() / 2));
         var1.setPivotY((float)(var1.getHeight() / 2));
         var1.setTranslationX(0.0F);
         var1.setTranslationY(0.0F);
         var1.setScaleX(1.0F);
         var1.setScaleY(1.0F);
         var1.setRotation(0.0F);
      }
   }

   public void setLeftTopRightBottom(View var1, int var2, int var3, int var4, int var5) {
      var1.setLeft(var2);
      var1.setTop(var3);
      var1.setRight(var4);
      var1.setBottom(var5);
   }

   public void setTransitionAlpha(@NonNull View var1, float var2) {
      Float var3 = (Float)var1.getTag(R$id.save_non_transition_alpha);
      if (var3 != null) {
         var1.setAlpha(var3 * var2);
      } else {
         var1.setAlpha(var2);
      }
   }

   public void transformMatrixToGlobal(@NonNull View var1, @NonNull Matrix var2) {
      ViewParent var3 = var1.getParent();
      if (var3 instanceof View) {
         View var5 = (View)var3;
         this.transformMatrixToGlobal(var5, var2);
         var2.preTranslate((float)(-var5.getScrollX()), (float)(-var5.getScrollY()));
      }

      var2.preTranslate((float)var1.getLeft(), (float)var1.getTop());
      Matrix var4 = var1.getMatrix();
      if (!var4.isIdentity()) {
         var2.preConcat(var4);
      }
   }

   public void transformMatrixToLocal(@NonNull View var1, @NonNull Matrix var2) {
      ViewParent var3 = var1.getParent();
      if (var3 instanceof View) {
         View var5 = (View)var3;
         this.transformMatrixToLocal(var5, var2);
         var2.postTranslate((float)var5.getScrollX(), (float)var5.getScrollY());
      }

      var2.postTranslate((float)var1.getLeft(), (float)var1.getTop());
      Matrix var4 = var1.getMatrix();
      if (!var4.isIdentity()) {
         Matrix var6 = new Matrix();
         if (var4.invert(var6)) {
            var2.postConcat(var6);
         }
      }
   }
}
