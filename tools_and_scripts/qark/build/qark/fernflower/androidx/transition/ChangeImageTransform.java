package androidx.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.Map;

public class ChangeImageTransform extends Transition {
   private static final Property ANIMATED_TRANSFORM_PROPERTY = new Property(Matrix.class, "animatedTransform") {
      public Matrix get(ImageView var1) {
         return null;
      }

      public void set(ImageView var1, Matrix var2) {
         ImageViewUtils.animateTransform(var1, var2);
      }
   };
   private static final TypeEvaluator NULL_MATRIX_EVALUATOR = new TypeEvaluator() {
      public Matrix evaluate(float var1, Matrix var2, Matrix var3) {
         return null;
      }
   };
   private static final String PROPNAME_BOUNDS = "android:changeImageTransform:bounds";
   private static final String PROPNAME_MATRIX = "android:changeImageTransform:matrix";
   private static final String[] sTransitionProperties = new String[]{"android:changeImageTransform:matrix", "android:changeImageTransform:bounds"};

   public ChangeImageTransform() {
   }

   public ChangeImageTransform(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void captureValues(TransitionValues var1) {
      View var2 = var1.view;
      if (var2 instanceof ImageView) {
         if (var2.getVisibility() == 0) {
            ImageView var3 = (ImageView)var2;
            if (var3.getDrawable() != null) {
               Map var4 = var1.values;
               var4.put("android:changeImageTransform:bounds", new Rect(var2.getLeft(), var2.getTop(), var2.getRight(), var2.getBottom()));
               var4.put("android:changeImageTransform:matrix", copyImageMatrix(var3));
            }
         }
      }
   }

   private static Matrix centerCropMatrix(ImageView var0) {
      Drawable var8 = var0.getDrawable();
      int var5 = var8.getIntrinsicWidth();
      int var6 = var0.getWidth();
      float var1 = (float)var6 / (float)var5;
      int var7 = var8.getIntrinsicHeight();
      int var4 = var0.getHeight();
      var1 = Math.max(var1, (float)var4 / (float)var7);
      float var2 = (float)var5;
      float var3 = (float)var7;
      var5 = Math.round(((float)var6 - var2 * var1) / 2.0F);
      var4 = Math.round(((float)var4 - var3 * var1) / 2.0F);
      Matrix var9 = new Matrix();
      var9.postScale(var1, var1);
      var9.postTranslate((float)var5, (float)var4);
      return var9;
   }

   private static Matrix copyImageMatrix(ImageView var0) {
      Drawable var2 = var0.getDrawable();
      if (var2.getIntrinsicWidth() > 0 && var2.getIntrinsicHeight() > 0) {
         int var1 = null.$SwitchMap$android$widget$ImageView$ScaleType[var0.getScaleType().ordinal()];
         if (var1 == 1) {
            return fitXYMatrix(var0);
         }

         if (var1 == 2) {
            return centerCropMatrix(var0);
         }
      }

      return new Matrix(var0.getImageMatrix());
   }

   private ObjectAnimator createMatrixAnimator(ImageView var1, Matrix var2, Matrix var3) {
      return ObjectAnimator.ofObject(var1, ANIMATED_TRANSFORM_PROPERTY, new TransitionUtils.MatrixEvaluator(), new Matrix[]{var2, var3});
   }

   private ObjectAnimator createNullAnimator(ImageView var1) {
      return ObjectAnimator.ofObject(var1, ANIMATED_TRANSFORM_PROPERTY, NULL_MATRIX_EVALUATOR, new Matrix[]{MatrixUtils.IDENTITY_MATRIX, MatrixUtils.IDENTITY_MATRIX});
   }

   private static Matrix fitXYMatrix(ImageView var0) {
      Drawable var1 = var0.getDrawable();
      Matrix var2 = new Matrix();
      var2.postScale((float)var0.getWidth() / (float)var1.getIntrinsicWidth(), (float)var0.getHeight() / (float)var1.getIntrinsicHeight());
      return var2;
   }

   public void captureEndValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      if (var2 != null) {
         if (var3 == null) {
            return null;
         } else {
            Rect var8 = (Rect)var2.values.get("android:changeImageTransform:bounds");
            Rect var7 = (Rect)var3.values.get("android:changeImageTransform:bounds");
            if (var8 != null) {
               if (var7 == null) {
                  return null;
               } else {
                  Matrix var11 = (Matrix)var2.values.get("android:changeImageTransform:matrix");
                  Matrix var6 = (Matrix)var3.values.get("android:changeImageTransform:matrix");
                  boolean var4;
                  if ((var11 != null || var6 != null) && (var11 == null || !var11.equals(var6))) {
                     var4 = false;
                  } else {
                     var4 = true;
                  }

                  if (var8.equals(var7) && var4) {
                     return null;
                  } else {
                     ImageView var12 = (ImageView)var3.view;
                     Drawable var9 = var12.getDrawable();
                     int var13 = var9.getIntrinsicWidth();
                     int var5 = var9.getIntrinsicHeight();
                     if (var13 > 0 && var5 > 0) {
                        Matrix var10 = var11;
                        if (var11 == null) {
                           var10 = MatrixUtils.IDENTITY_MATRIX;
                        }

                        var11 = var6;
                        if (var6 == null) {
                           var11 = MatrixUtils.IDENTITY_MATRIX;
                        }

                        ANIMATED_TRANSFORM_PROPERTY.set(var12, var10);
                        return this.createMatrixAnimator(var12, var10, var11);
                     } else {
                        return this.createNullAnimator(var12);
                     }
                  }
               }
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public String[] getTransitionProperties() {
      return sTransitionProperties;
   }
}
