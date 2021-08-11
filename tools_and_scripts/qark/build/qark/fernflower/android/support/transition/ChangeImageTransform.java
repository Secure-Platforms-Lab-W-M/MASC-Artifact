package android.support.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
      switch(var0.getScaleType()) {
      case FIT_XY:
         return fitXYMatrix(var0);
      case CENTER_CROP:
         return centerCropMatrix(var0);
      default:
         return new Matrix(var0.getImageMatrix());
      }
   }

   private ObjectAnimator createMatrixAnimator(ImageView var1, Matrix var2, Matrix var3) {
      return ObjectAnimator.ofObject(var1, ANIMATED_TRANSFORM_PROPERTY, new TransitionUtils.MatrixEvaluator(), new Matrix[]{var2, var3});
   }

   private ObjectAnimator createNullAnimator(ImageView var1) {
      return ObjectAnimator.ofObject(var1, ANIMATED_TRANSFORM_PROPERTY, NULL_MATRIX_EVALUATOR, new Matrix[]{null, null});
   }

   private static Matrix fitXYMatrix(ImageView var0) {
      Drawable var1 = var0.getDrawable();
      Matrix var2 = new Matrix();
      var2.postScale((float)var0.getWidth() / (float)var1.getIntrinsicWidth(), (float)var0.getHeight() / (float)var1.getIntrinsicHeight());
      return var2;
   }

   public void captureEndValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(@NonNull ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      if (var2 != null) {
         if (var3 == null) {
            return null;
         } else {
            Rect var6 = (Rect)var2.values.get("android:changeImageTransform:bounds");
            Rect var7 = (Rect)var3.values.get("android:changeImageTransform:bounds");
            if (var6 != null) {
               if (var7 == null) {
                  return null;
               } else {
                  Matrix var8 = (Matrix)var2.values.get("android:changeImageTransform:matrix");
                  Matrix var10 = (Matrix)var3.values.get("android:changeImageTransform:matrix");
                  boolean var4;
                  if ((var8 != null || var10 != null) && (var8 == null || !var8.equals(var10))) {
                     var4 = false;
                  } else {
                     var4 = true;
                  }

                  if (var6.equals(var7) && var4) {
                     return null;
                  } else {
                     ImageView var11 = (ImageView)var3.view;
                     Drawable var13 = var11.getDrawable();
                     int var12 = var13.getIntrinsicWidth();
                     int var5 = var13.getIntrinsicHeight();
                     ImageViewUtils.startAnimateTransform(var11);
                     ObjectAnimator var9;
                     if (var12 != 0 && var5 != 0) {
                        if (var8 == null) {
                           var8 = MatrixUtils.IDENTITY_MATRIX;
                        }

                        if (var10 == null) {
                           var10 = MatrixUtils.IDENTITY_MATRIX;
                        }

                        ANIMATED_TRANSFORM_PROPERTY.set(var11, var8);
                        var9 = this.createMatrixAnimator(var11, var8, var10);
                     } else {
                        var9 = this.createNullAnimator(var11);
                     }

                     ImageViewUtils.reserveEndAnimateTransform(var11, var9);
                     return var9;
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
