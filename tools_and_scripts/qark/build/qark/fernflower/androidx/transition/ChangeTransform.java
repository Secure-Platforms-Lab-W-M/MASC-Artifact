package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.transition.R.id;
import org.xmlpull.v1.XmlPullParser;

public class ChangeTransform extends Transition {
   private static final Property NON_TRANSLATIONS_PROPERTY = new Property(float[].class, "nonTranslations") {
      public float[] get(ChangeTransform.PathAnimatorMatrix var1) {
         return null;
      }

      public void set(ChangeTransform.PathAnimatorMatrix var1, float[] var2) {
         var1.setValues(var2);
      }
   };
   private static final String PROPNAME_INTERMEDIATE_MATRIX = "android:changeTransform:intermediateMatrix";
   private static final String PROPNAME_INTERMEDIATE_PARENT_MATRIX = "android:changeTransform:intermediateParentMatrix";
   private static final String PROPNAME_MATRIX = "android:changeTransform:matrix";
   private static final String PROPNAME_PARENT = "android:changeTransform:parent";
   private static final String PROPNAME_PARENT_MATRIX = "android:changeTransform:parentMatrix";
   private static final String PROPNAME_TRANSFORMS = "android:changeTransform:transforms";
   private static final boolean SUPPORTS_VIEW_REMOVAL_SUPPRESSION;
   private static final Property TRANSLATIONS_PROPERTY = new Property(PointF.class, "translations") {
      public PointF get(ChangeTransform.PathAnimatorMatrix var1) {
         return null;
      }

      public void set(ChangeTransform.PathAnimatorMatrix var1, PointF var2) {
         var1.setTranslation(var2);
      }
   };
   private static final String[] sTransitionProperties = new String[]{"android:changeTransform:matrix", "android:changeTransform:transforms", "android:changeTransform:parentMatrix"};
   private boolean mReparent = true;
   private Matrix mTempMatrix = new Matrix();
   boolean mUseOverlay = true;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      SUPPORTS_VIEW_REMOVAL_SUPPRESSION = var0;
   }

   public ChangeTransform() {
   }

   public ChangeTransform(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, Styleable.CHANGE_TRANSFORM);
      this.mUseOverlay = TypedArrayUtils.getNamedBoolean(var3, (XmlPullParser)var2, "reparentWithOverlay", 1, true);
      this.mReparent = TypedArrayUtils.getNamedBoolean(var3, (XmlPullParser)var2, "reparent", 0, true);
      var3.recycle();
   }

   private void captureValues(TransitionValues var1) {
      View var3 = var1.view;
      if (var3.getVisibility() != 8) {
         var1.values.put("android:changeTransform:parent", var3.getParent());
         ChangeTransform.Transforms var2 = new ChangeTransform.Transforms(var3);
         var1.values.put("android:changeTransform:transforms", var2);
         Matrix var5 = var3.getMatrix();
         if (var5 != null && !var5.isIdentity()) {
            var5 = new Matrix(var5);
         } else {
            var5 = null;
         }

         var1.values.put("android:changeTransform:matrix", var5);
         if (this.mReparent) {
            var5 = new Matrix();
            ViewGroup var4 = (ViewGroup)var3.getParent();
            ViewUtils.transformMatrixToGlobal(var4, var5);
            var5.preTranslate((float)(-var4.getScrollX()), (float)(-var4.getScrollY()));
            var1.values.put("android:changeTransform:parentMatrix", var5);
            var1.values.put("android:changeTransform:intermediateMatrix", var3.getTag(id.transition_transform));
            var1.values.put("android:changeTransform:intermediateParentMatrix", var3.getTag(id.parent_matrix));
         }

      }
   }

   private void createGhostView(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      View var4 = var3.view;
      Matrix var5 = new Matrix((Matrix)var3.values.get("android:changeTransform:parentMatrix"));
      ViewUtils.transformMatrixToLocal(var1, var5);
      GhostView var6 = GhostViewUtils.addGhost(var4, var1, var5);
      if (var6 != null) {
         var6.reserveEndViewTransition((ViewGroup)var2.values.get("android:changeTransform:parent"), var2.view);

         Object var7;
         for(var7 = this; ((Transition)var7).mParent != null; var7 = ((Transition)var7).mParent) {
         }

         ((Transition)var7).addListener(new ChangeTransform.GhostListener(var4, var6));
         if (SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
            if (var2.view != var3.view) {
               ViewUtils.setTransitionAlpha(var2.view, 0.0F);
            }

            ViewUtils.setTransitionAlpha(var4, 1.0F);
         }

      }
   }

   private ObjectAnimator createTransformAnimator(TransitionValues var1, TransitionValues var2, final boolean var3) {
      final Matrix var9 = (Matrix)var1.values.get("android:changeTransform:matrix");
      Matrix var5 = (Matrix)var2.values.get("android:changeTransform:matrix");
      Matrix var4 = var9;
      if (var9 == null) {
         var4 = MatrixUtils.IDENTITY_MATRIX;
      }

      var9 = var5;
      if (var5 == null) {
         var9 = MatrixUtils.IDENTITY_MATRIX;
      }

      if (var4.equals(var9)) {
         return null;
      } else {
         final ChangeTransform.Transforms var13 = (ChangeTransform.Transforms)var2.values.get("android:changeTransform:transforms");
         final View var10 = var2.view;
         setIdentityTransforms(var10);
         float[] var6 = new float[9];
         var4.getValues(var6);
         float[] var8 = new float[9];
         var9.getValues(var8);
         final ChangeTransform.PathAnimatorMatrix var12 = new ChangeTransform.PathAnimatorMatrix(var10, var6);
         PropertyValuesHolder var7 = PropertyValuesHolder.ofObject(NON_TRANSLATIONS_PROPERTY, new FloatArrayEvaluator(new float[9]), new float[][]{var6, var8});
         Path var14 = this.getPathMotion().getPath(var6[2], var6[5], var8[2], var8[5]);
         ObjectAnimator var15 = ObjectAnimator.ofPropertyValuesHolder(var12, new PropertyValuesHolder[]{var7, PropertyValuesHolderUtils.ofPointF(TRANSLATIONS_PROPERTY, var14)});
         AnimatorListenerAdapter var11 = new AnimatorListenerAdapter() {
            private boolean mIsCanceled;
            private Matrix mTempMatrix = new Matrix();

            private void setCurrentMatrix(Matrix var1) {
               this.mTempMatrix.set(var1);
               var10.setTag(id.transition_transform, this.mTempMatrix);
               var13.restore(var10);
            }

            public void onAnimationCancel(Animator var1) {
               this.mIsCanceled = true;
            }

            public void onAnimationEnd(Animator var1) {
               if (!this.mIsCanceled) {
                  if (var3 && ChangeTransform.this.mUseOverlay) {
                     this.setCurrentMatrix(var9);
                  } else {
                     var10.setTag(id.transition_transform, (Object)null);
                     var10.setTag(id.parent_matrix, (Object)null);
                  }
               }

               ViewUtils.setAnimationMatrix(var10, (Matrix)null);
               var13.restore(var10);
            }

            public void onAnimationPause(Animator var1) {
               this.setCurrentMatrix(var12.getMatrix());
            }

            public void onAnimationResume(Animator var1) {
               ChangeTransform.setIdentityTransforms(var10);
            }
         };
         var15.addListener(var11);
         AnimatorUtils.addPauseListener(var15, var11);
         return var15;
      }
   }

   private boolean parentsMatch(ViewGroup var1, ViewGroup var2) {
      boolean var3 = false;
      boolean var6 = this.isValidTarget(var1);
      boolean var5 = false;
      boolean var4 = false;
      if (var6 && this.isValidTarget(var2)) {
         TransitionValues var7 = this.getMatchedTransitionValues(var1, true);
         if (var7 != null) {
            var3 = var4;
            if (var2 == var7.view) {
               var3 = true;
            }

            return var3;
         }
      } else {
         var3 = var5;
         if (var1 == var2) {
            var3 = true;
         }
      }

      return var3;
   }

   static void setIdentityTransforms(View var0) {
      setTransforms(var0, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
   }

   private void setMatricesForParent(TransitionValues var1, TransitionValues var2) {
      Matrix var3 = (Matrix)var2.values.get("android:changeTransform:parentMatrix");
      var2.view.setTag(id.parent_matrix, var3);
      Matrix var4 = this.mTempMatrix;
      var4.reset();
      var3.invert(var4);
      var3 = (Matrix)var1.values.get("android:changeTransform:matrix");
      Matrix var5 = var3;
      if (var3 == null) {
         var5 = new Matrix();
         var1.values.put("android:changeTransform:matrix", var5);
      }

      var5.postConcat((Matrix)var1.values.get("android:changeTransform:parentMatrix"));
      var5.postConcat(var4);
   }

   static void setTransforms(View var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      var0.setTranslationX(var1);
      var0.setTranslationY(var2);
      ViewCompat.setTranslationZ(var0, var3);
      var0.setScaleX(var4);
      var0.setScaleY(var5);
      var0.setRotationX(var6);
      var0.setRotationY(var7);
      var0.setRotation(var8);
   }

   public void captureEndValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(TransitionValues var1) {
      this.captureValues(var1);
      if (!SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
         ((ViewGroup)var1.view.getParent()).startViewTransition(var1.view);
      }

   }

   public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      if (var2 != null && var3 != null && var2.values.containsKey("android:changeTransform:parent") && var3.values.containsKey("android:changeTransform:parent")) {
         ViewGroup var5 = (ViewGroup)var2.values.get("android:changeTransform:parent");
         ViewGroup var6 = (ViewGroup)var3.values.get("android:changeTransform:parent");
         boolean var4;
         if (this.mReparent && !this.parentsMatch(var5, var6)) {
            var4 = true;
         } else {
            var4 = false;
         }

         Matrix var7 = (Matrix)var2.values.get("android:changeTransform:intermediateMatrix");
         if (var7 != null) {
            var2.values.put("android:changeTransform:matrix", var7);
         }

         var7 = (Matrix)var2.values.get("android:changeTransform:intermediateParentMatrix");
         if (var7 != null) {
            var2.values.put("android:changeTransform:parentMatrix", var7);
         }

         if (var4) {
            this.setMatricesForParent(var2, var3);
         }

         ObjectAnimator var8 = this.createTransformAnimator(var2, var3, var4);
         if (var4 && var8 != null && this.mUseOverlay) {
            this.createGhostView(var1, var2, var3);
            return var8;
         } else {
            if (!SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
               var5.endViewTransition(var2.view);
            }

            return var8;
         }
      } else {
         return null;
      }
   }

   public boolean getReparent() {
      return this.mReparent;
   }

   public boolean getReparentWithOverlay() {
      return this.mUseOverlay;
   }

   public String[] getTransitionProperties() {
      return sTransitionProperties;
   }

   public void setReparent(boolean var1) {
      this.mReparent = var1;
   }

   public void setReparentWithOverlay(boolean var1) {
      this.mUseOverlay = var1;
   }

   private static class GhostListener extends TransitionListenerAdapter {
      private GhostView mGhostView;
      private View mView;

      GhostListener(View var1, GhostView var2) {
         this.mView = var1;
         this.mGhostView = var2;
      }

      public void onTransitionEnd(Transition var1) {
         var1.removeListener(this);
         GhostViewUtils.removeGhost(this.mView);
         this.mView.setTag(id.transition_transform, (Object)null);
         this.mView.setTag(id.parent_matrix, (Object)null);
      }

      public void onTransitionPause(Transition var1) {
         this.mGhostView.setVisibility(4);
      }

      public void onTransitionResume(Transition var1) {
         this.mGhostView.setVisibility(0);
      }
   }

   private static class PathAnimatorMatrix {
      private final Matrix mMatrix = new Matrix();
      private float mTranslationX;
      private float mTranslationY;
      private final float[] mValues;
      private final View mView;

      PathAnimatorMatrix(View var1, float[] var2) {
         this.mView = var1;
         float[] var3 = (float[])var2.clone();
         this.mValues = var3;
         this.mTranslationX = var3[2];
         this.mTranslationY = var3[5];
         this.setAnimationMatrix();
      }

      private void setAnimationMatrix() {
         float[] var1 = this.mValues;
         var1[2] = this.mTranslationX;
         var1[5] = this.mTranslationY;
         this.mMatrix.setValues(var1);
         ViewUtils.setAnimationMatrix(this.mView, this.mMatrix);
      }

      Matrix getMatrix() {
         return this.mMatrix;
      }

      void setTranslation(PointF var1) {
         this.mTranslationX = var1.x;
         this.mTranslationY = var1.y;
         this.setAnimationMatrix();
      }

      void setValues(float[] var1) {
         System.arraycopy(var1, 0, this.mValues, 0, var1.length);
         this.setAnimationMatrix();
      }
   }

   private static class Transforms {
      final float mRotationX;
      final float mRotationY;
      final float mRotationZ;
      final float mScaleX;
      final float mScaleY;
      final float mTranslationX;
      final float mTranslationY;
      final float mTranslationZ;

      Transforms(View var1) {
         this.mTranslationX = var1.getTranslationX();
         this.mTranslationY = var1.getTranslationY();
         this.mTranslationZ = ViewCompat.getTranslationZ(var1);
         this.mScaleX = var1.getScaleX();
         this.mScaleY = var1.getScaleY();
         this.mRotationX = var1.getRotationX();
         this.mRotationY = var1.getRotationY();
         this.mRotationZ = var1.getRotation();
      }

      public boolean equals(Object var1) {
         boolean var2 = var1 instanceof ChangeTransform.Transforms;
         boolean var3 = false;
         if (!var2) {
            return false;
         } else {
            ChangeTransform.Transforms var4 = (ChangeTransform.Transforms)var1;
            var2 = var3;
            if (var4.mTranslationX == this.mTranslationX) {
               var2 = var3;
               if (var4.mTranslationY == this.mTranslationY) {
                  var2 = var3;
                  if (var4.mTranslationZ == this.mTranslationZ) {
                     var2 = var3;
                     if (var4.mScaleX == this.mScaleX) {
                        var2 = var3;
                        if (var4.mScaleY == this.mScaleY) {
                           var2 = var3;
                           if (var4.mRotationX == this.mRotationX) {
                              var2 = var3;
                              if (var4.mRotationY == this.mRotationY) {
                                 var2 = var3;
                                 if (var4.mRotationZ == this.mRotationZ) {
                                    var2 = true;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            return var2;
         }
      }

      public int hashCode() {
         float var1 = this.mTranslationX;
         int var9 = 0;
         int var2;
         if (var1 != 0.0F) {
            var2 = Float.floatToIntBits(var1);
         } else {
            var2 = 0;
         }

         var1 = this.mTranslationY;
         int var3;
         if (var1 != 0.0F) {
            var3 = Float.floatToIntBits(var1);
         } else {
            var3 = 0;
         }

         var1 = this.mTranslationZ;
         int var4;
         if (var1 != 0.0F) {
            var4 = Float.floatToIntBits(var1);
         } else {
            var4 = 0;
         }

         var1 = this.mScaleX;
         int var5;
         if (var1 != 0.0F) {
            var5 = Float.floatToIntBits(var1);
         } else {
            var5 = 0;
         }

         var1 = this.mScaleY;
         int var6;
         if (var1 != 0.0F) {
            var6 = Float.floatToIntBits(var1);
         } else {
            var6 = 0;
         }

         var1 = this.mRotationX;
         int var7;
         if (var1 != 0.0F) {
            var7 = Float.floatToIntBits(var1);
         } else {
            var7 = 0;
         }

         var1 = this.mRotationY;
         int var8;
         if (var1 != 0.0F) {
            var8 = Float.floatToIntBits(var1);
         } else {
            var8 = 0;
         }

         var1 = this.mRotationZ;
         if (var1 != 0.0F) {
            var9 = Float.floatToIntBits(var1);
         }

         return ((((((var2 * 31 + var3) * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var8) * 31 + var9;
      }

      public void restore(View var1) {
         ChangeTransform.setTransforms(var1, this.mTranslationX, this.mTranslationY, this.mTranslationZ, this.mScaleX, this.mScaleY, this.mRotationX, this.mRotationY, this.mRotationZ);
      }
   }
}
