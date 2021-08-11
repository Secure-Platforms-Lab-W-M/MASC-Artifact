package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

class TransitionUtils {
   private static final boolean HAS_IS_ATTACHED_TO_WINDOW;
   private static final boolean HAS_OVERLAY;
   private static final boolean HAS_PICTURE_BITMAP;
   private static final int MAX_IMAGE_SIZE = 1048576;

   static {
      int var0 = VERSION.SDK_INT;
      boolean var2 = true;
      boolean var1;
      if (var0 >= 19) {
         var1 = true;
      } else {
         var1 = false;
      }

      HAS_IS_ATTACHED_TO_WINDOW = var1;
      if (VERSION.SDK_INT >= 18) {
         var1 = true;
      } else {
         var1 = false;
      }

      HAS_OVERLAY = var1;
      if (VERSION.SDK_INT >= 28) {
         var1 = var2;
      } else {
         var1 = false;
      }

      HAS_PICTURE_BITMAP = var1;
   }

   private TransitionUtils() {
   }

   static View copyViewImage(ViewGroup var0, View var1, View var2) {
      Matrix var7 = new Matrix();
      var7.setTranslate((float)(-var2.getScrollX()), (float)(-var2.getScrollY()));
      ViewUtils.transformMatrixToGlobal(var1, var7);
      ViewUtils.transformMatrixToLocal(var0, var7);
      RectF var8 = new RectF(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
      var7.mapRect(var8);
      int var3 = Math.round(var8.left);
      int var4 = Math.round(var8.top);
      int var5 = Math.round(var8.right);
      int var6 = Math.round(var8.bottom);
      ImageView var10 = new ImageView(var1.getContext());
      var10.setScaleType(ScaleType.CENTER_CROP);
      Bitmap var9 = createViewBitmap(var1, var7, var8, var0);
      if (var9 != null) {
         var10.setImageBitmap(var9);
      }

      var10.measure(MeasureSpec.makeMeasureSpec(var5 - var3, 1073741824), MeasureSpec.makeMeasureSpec(var6 - var4, 1073741824));
      var10.layout(var3, var4, var5, var6);
      return var10;
   }

   private static Bitmap createViewBitmap(View var0, Matrix var1, RectF var2, ViewGroup var3) {
      boolean var5;
      boolean var9;
      if (HAS_IS_ATTACHED_TO_WINDOW) {
         var5 = var0.isAttachedToWindow() ^ true;
         if (var3 == null) {
            var9 = false;
         } else {
            var9 = var3.isAttachedToWindow();
         }
      } else {
         var5 = false;
         var9 = false;
      }

      Bitmap var10 = null;
      byte var7 = 0;
      ViewGroup var11 = var10;
      int var6 = var7;
      if (HAS_OVERLAY) {
         var11 = var10;
         var6 = var7;
         if (var5) {
            if (!var9) {
               return null;
            }

            var11 = (ViewGroup)var0.getParent();
            var6 = var11.indexOfChild(var0);
            var3.getOverlay().add(var0);
         }
      }

      Object var12 = null;
      int var8 = Math.round(var2.width());
      int var15 = Math.round(var2.height());
      var10 = (Bitmap)var12;
      if (var8 > 0) {
         var10 = (Bitmap)var12;
         if (var15 > 0) {
            float var4 = Math.min(1.0F, 1048576.0F / (float)(var8 * var15));
            var8 = Math.round((float)var8 * var4);
            var15 = Math.round((float)var15 * var4);
            var1.postTranslate(-var2.left, -var2.top);
            var1.postScale(var4, var4);
            if (HAS_PICTURE_BITMAP) {
               Picture var13 = new Picture();
               Canvas var16 = var13.beginRecording(var8, var15);
               var16.concat(var1);
               var0.draw(var16);
               var13.endRecording();
               var10 = Bitmap.createBitmap(var13);
            } else {
               var10 = Bitmap.createBitmap(var8, var15, Config.ARGB_8888);
               Canvas var14 = new Canvas(var10);
               var14.concat(var1);
               var0.draw(var14);
            }
         }
      }

      if (HAS_OVERLAY && var5) {
         var3.getOverlay().remove(var0);
         var11.addView(var0, var6);
      }

      return var10;
   }

   static Animator mergeAnimators(Animator var0, Animator var1) {
      if (var0 == null) {
         return var1;
      } else if (var1 == null) {
         return var0;
      } else {
         AnimatorSet var2 = new AnimatorSet();
         var2.playTogether(new Animator[]{var0, var1});
         return var2;
      }
   }

   static class MatrixEvaluator implements TypeEvaluator {
      final float[] mTempEndValues = new float[9];
      final Matrix mTempMatrix = new Matrix();
      final float[] mTempStartValues = new float[9];

      public Matrix evaluate(float var1, Matrix var2, Matrix var3) {
         var2.getValues(this.mTempStartValues);
         var3.getValues(this.mTempEndValues);

         for(int var6 = 0; var6 < 9; ++var6) {
            float[] var7 = this.mTempEndValues;
            float var4 = var7[var6];
            float[] var8 = this.mTempStartValues;
            float var5 = var8[var6];
            var7[var6] = var8[var6] + var1 * (var4 - var5);
         }

         this.mTempMatrix.setValues(this.mTempEndValues);
         return this.mTempMatrix;
      }
   }
}
