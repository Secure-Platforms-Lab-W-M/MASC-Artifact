package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

class TransitionUtils {
   private static final int MAX_IMAGE_SIZE = 1048576;

   static View copyViewImage(ViewGroup var0, View var1, View var2) {
      Matrix var7 = new Matrix();
      var7.setTranslate((float)(-var2.getScrollX()), (float)(-var2.getScrollY()));
      ViewUtils.transformMatrixToGlobal(var1, var7);
      ViewUtils.transformMatrixToLocal(var0, var7);
      RectF var10 = new RectF(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
      var7.mapRect(var10);
      int var3 = Math.round(var10.left);
      int var4 = Math.round(var10.top);
      int var5 = Math.round(var10.right);
      int var6 = Math.round(var10.bottom);
      ImageView var8 = new ImageView(var1.getContext());
      var8.setScaleType(ScaleType.CENTER_CROP);
      Bitmap var9 = createViewBitmap(var1, var7, var10);
      if (var9 != null) {
         var8.setImageBitmap(var9);
      }

      var8.measure(MeasureSpec.makeMeasureSpec(var5 - var3, 1073741824), MeasureSpec.makeMeasureSpec(var6 - var4, 1073741824));
      var8.layout(var3, var4, var5, var6);
      return var8;
   }

   private static Bitmap createViewBitmap(View var0, Matrix var1, RectF var2) {
      int var5 = Math.round(var2.width());
      int var4 = Math.round(var2.height());
      if (var5 > 0 && var4 > 0) {
         float var3 = Math.min(1.0F, 1048576.0F / (float)(var5 * var4));
         var5 = (int)((float)var5 * var3);
         var4 = (int)((float)var4 * var3);
         var1.postTranslate(-var2.left, -var2.top);
         var1.postScale(var3, var3);
         Bitmap var7 = Bitmap.createBitmap(var5, var4, Config.ARGB_8888);
         Canvas var6 = new Canvas(var7);
         var6.concat(var1);
         var0.draw(var6);
         return var7;
      } else {
         return null;
      }
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
