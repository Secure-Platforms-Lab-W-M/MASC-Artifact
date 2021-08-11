package androidx.transition;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import androidx.transition.R.id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewUtilsBase {
   private static final String TAG = "ViewUtilsBase";
   private static final int VISIBILITY_MASK = 12;
   private static boolean sSetFrameFetched;
   private static Method sSetFrameMethod;
   private static Field sViewFlagsField;
   private static boolean sViewFlagsFieldFetched;
   private float[] mMatrixValues;

   private void fetchSetFrame() {
      if (!sSetFrameFetched) {
         try {
            Method var1 = View.class.getDeclaredMethod("setFrame", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            sSetFrameMethod = var1;
            var1.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            Log.i("ViewUtilsBase", "Failed to retrieve setFrame method", var2);
         }

         sSetFrameFetched = true;
      }

   }

   public void clearNonTransitionAlpha(View var1) {
      if (var1.getVisibility() == 0) {
         var1.setTag(id.save_non_transition_alpha, (Object)null);
      }

   }

   public float getTransitionAlpha(View var1) {
      Float var2 = (Float)var1.getTag(id.save_non_transition_alpha);
      return var2 != null ? var1.getAlpha() / var2 : var1.getAlpha();
   }

   public void saveNonTransitionAlpha(View var1) {
      if (var1.getTag(id.save_non_transition_alpha) == null) {
         var1.setTag(id.save_non_transition_alpha, var1.getAlpha());
      }

   }

   public void setAnimationMatrix(View var1, Matrix var2) {
      if (var2 != null && !var2.isIdentity()) {
         float[] var10 = this.mMatrixValues;
         float[] var9 = var10;
         if (var10 == null) {
            var10 = new float[9];
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
      this.fetchSetFrame();
      Method var6 = sSetFrameMethod;
      if (var6 != null) {
         try {
            var6.invoke(var1, var2, var3, var4, var5);
            return;
         } catch (IllegalAccessException var7) {
         } catch (InvocationTargetException var8) {
            throw new RuntimeException(var8.getCause());
         }
      }

   }

   public void setTransitionAlpha(View var1, float var2) {
      Float var3 = (Float)var1.getTag(id.save_non_transition_alpha);
      if (var3 != null) {
         var1.setAlpha(var3 * var2);
      } else {
         var1.setAlpha(var2);
      }
   }

   public void setTransitionVisibility(View var1, int var2) {
      Field var4;
      if (!sViewFlagsFieldFetched) {
         try {
            var4 = View.class.getDeclaredField("mViewFlags");
            sViewFlagsField = var4;
            var4.setAccessible(true);
         } catch (NoSuchFieldException var5) {
            Log.i("ViewUtilsBase", "fetchViewFlagsField: ");
         }

         sViewFlagsFieldFetched = true;
      }

      var4 = sViewFlagsField;
      if (var4 != null) {
         try {
            int var3 = var4.getInt(var1);
            sViewFlagsField.setInt(var1, var3 & -13 | var2);
            return;
         } catch (IllegalAccessException var6) {
         }
      }

   }

   public void transformMatrixToGlobal(View var1, Matrix var2) {
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

   public void transformMatrixToLocal(View var1, Matrix var2) {
      ViewParent var3 = var1.getParent();
      if (var3 instanceof View) {
         View var5 = (View)var3;
         this.transformMatrixToLocal(var5, var2);
         var2.postTranslate((float)var5.getScrollX(), (float)var5.getScrollY());
      }

      var2.postTranslate((float)(-var1.getLeft()), (float)(-var1.getTop()));
      Matrix var4 = var1.getMatrix();
      if (!var4.isIdentity()) {
         Matrix var6 = new Matrix();
         if (var4.invert(var6)) {
            var2.postConcat(var6);
         }
      }

   }
}
