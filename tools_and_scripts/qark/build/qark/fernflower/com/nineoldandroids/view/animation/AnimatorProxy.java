package com.nineoldandroids.view.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public final class AnimatorProxy extends Animation {
   public static final boolean NEEDS_PROXY;
   private static final WeakHashMap PROXIES;
   private final RectF mAfter = new RectF();
   private float mAlpha = 1.0F;
   private final RectF mBefore = new RectF();
   private final Camera mCamera = new Camera();
   private boolean mHasPivot;
   private float mPivotX;
   private float mPivotY;
   private float mRotationX;
   private float mRotationY;
   private float mRotationZ;
   private float mScaleX = 1.0F;
   private float mScaleY = 1.0F;
   private final Matrix mTempMatrix = new Matrix();
   private float mTranslationX;
   private float mTranslationY;
   private final WeakReference mView;

   static {
      boolean var0;
      if (Integer.valueOf(VERSION.SDK) < 11) {
         var0 = true;
      } else {
         var0 = false;
      }

      NEEDS_PROXY = var0;
      PROXIES = new WeakHashMap();
   }

   private AnimatorProxy(View var1) {
      this.setDuration(0L);
      this.setFillAfter(true);
      var1.setAnimation(this);
      this.mView = new WeakReference(var1);
   }

   private void computeRect(RectF var1, View var2) {
      var1.set(0.0F, 0.0F, (float)var2.getWidth(), (float)var2.getHeight());
      Matrix var4 = this.mTempMatrix;
      var4.reset();
      this.transformMatrix(var4, var2);
      this.mTempMatrix.mapRect(var1);
      var1.offset((float)var2.getLeft(), (float)var2.getTop());
      float var3;
      if (var1.right < var1.left) {
         var3 = var1.right;
         var1.right = var1.left;
         var1.left = var3;
      }

      if (var1.bottom < var1.top) {
         var3 = var1.top;
         var1.top = var1.bottom;
         var1.bottom = var3;
      }

   }

   private void invalidateAfterUpdate() {
      View var1 = (View)this.mView.get();
      if (var1 != null) {
         if (var1.getParent() != null) {
            RectF var2 = this.mAfter;
            this.computeRect(var2, var1);
            var2.union(this.mBefore);
            ((View)var1.getParent()).invalidate((int)Math.floor((double)var2.left), (int)Math.floor((double)var2.top), (int)Math.ceil((double)var2.right), (int)Math.ceil((double)var2.bottom));
         }
      }
   }

   private void prepareForUpdate() {
      View var1 = (View)this.mView.get();
      if (var1 != null) {
         this.computeRect(this.mBefore, var1);
      }

   }

   private void transformMatrix(Matrix var1, View var2) {
      float var5 = (float)var2.getWidth();
      float var6 = (float)var2.getHeight();
      boolean var10 = this.mHasPivot;
      float var3;
      if (var10) {
         var3 = this.mPivotX;
      } else {
         var3 = var5 / 2.0F;
      }

      float var4;
      if (var10) {
         var4 = this.mPivotY;
      } else {
         var4 = var6 / 2.0F;
      }

      float var7 = this.mRotationX;
      float var8 = this.mRotationY;
      float var9 = this.mRotationZ;
      if (var7 != 0.0F || var8 != 0.0F || var9 != 0.0F) {
         Camera var11 = this.mCamera;
         var11.save();
         var11.rotateX(var7);
         var11.rotateY(var8);
         var11.rotateZ(-var9);
         var11.getMatrix(var1);
         var11.restore();
         var1.preTranslate(-var3, -var4);
         var1.postTranslate(var3, var4);
      }

      var7 = this.mScaleX;
      var8 = this.mScaleY;
      if (var7 != 1.0F || var8 != 1.0F) {
         var1.postScale(var7, var8);
         var1.postTranslate(-(var3 / var5) * (var7 * var5 - var5), -(var4 / var6) * (var8 * var6 - var6));
      }

      var1.postTranslate(this.mTranslationX, this.mTranslationY);
   }

   public static AnimatorProxy wrap(View var0) {
      AnimatorProxy var2 = (AnimatorProxy)PROXIES.get(var0);
      AnimatorProxy var1;
      if (var2 != null) {
         var1 = var2;
         if (var2 == var0.getAnimation()) {
            return var1;
         }
      }

      var1 = new AnimatorProxy(var0);
      PROXIES.put(var0, var1);
      return var1;
   }

   protected void applyTransformation(float var1, Transformation var2) {
      View var3 = (View)this.mView.get();
      if (var3 != null) {
         var2.setAlpha(this.mAlpha);
         this.transformMatrix(var2.getMatrix(), var3);
      }

   }

   public float getAlpha() {
      return this.mAlpha;
   }

   public float getPivotX() {
      return this.mPivotX;
   }

   public float getPivotY() {
      return this.mPivotY;
   }

   public float getRotation() {
      return this.mRotationZ;
   }

   public float getRotationX() {
      return this.mRotationX;
   }

   public float getRotationY() {
      return this.mRotationY;
   }

   public float getScaleX() {
      return this.mScaleX;
   }

   public float getScaleY() {
      return this.mScaleY;
   }

   public int getScrollX() {
      View var1 = (View)this.mView.get();
      return var1 == null ? 0 : var1.getScrollX();
   }

   public int getScrollY() {
      View var1 = (View)this.mView.get();
      return var1 == null ? 0 : var1.getScrollY();
   }

   public float getTranslationX() {
      return this.mTranslationX;
   }

   public float getTranslationY() {
      return this.mTranslationY;
   }

   public float getX() {
      View var1 = (View)this.mView.get();
      return var1 == null ? 0.0F : (float)var1.getLeft() + this.mTranslationX;
   }

   public float getY() {
      View var1 = (View)this.mView.get();
      return var1 == null ? 0.0F : (float)var1.getTop() + this.mTranslationY;
   }

   public void setAlpha(float var1) {
      if (this.mAlpha != var1) {
         this.mAlpha = var1;
         View var2 = (View)this.mView.get();
         if (var2 != null) {
            var2.invalidate();
         }
      }

   }

   public void setPivotX(float var1) {
      if (!this.mHasPivot || this.mPivotX != var1) {
         this.prepareForUpdate();
         this.mHasPivot = true;
         this.mPivotX = var1;
         this.invalidateAfterUpdate();
      }

   }

   public void setPivotY(float var1) {
      if (!this.mHasPivot || this.mPivotY != var1) {
         this.prepareForUpdate();
         this.mHasPivot = true;
         this.mPivotY = var1;
         this.invalidateAfterUpdate();
      }

   }

   public void setRotation(float var1) {
      if (this.mRotationZ != var1) {
         this.prepareForUpdate();
         this.mRotationZ = var1;
         this.invalidateAfterUpdate();
      }

   }

   public void setRotationX(float var1) {
      if (this.mRotationX != var1) {
         this.prepareForUpdate();
         this.mRotationX = var1;
         this.invalidateAfterUpdate();
      }

   }

   public void setRotationY(float var1) {
      if (this.mRotationY != var1) {
         this.prepareForUpdate();
         this.mRotationY = var1;
         this.invalidateAfterUpdate();
      }

   }

   public void setScaleX(float var1) {
      if (this.mScaleX != var1) {
         this.prepareForUpdate();
         this.mScaleX = var1;
         this.invalidateAfterUpdate();
      }

   }

   public void setScaleY(float var1) {
      if (this.mScaleY != var1) {
         this.prepareForUpdate();
         this.mScaleY = var1;
         this.invalidateAfterUpdate();
      }

   }

   public void setScrollX(int var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.scrollTo(var1, var2.getScrollY());
      }

   }

   public void setScrollY(int var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.scrollTo(var2.getScrollX(), var1);
      }

   }

   public void setTranslationX(float var1) {
      if (this.mTranslationX != var1) {
         this.prepareForUpdate();
         this.mTranslationX = var1;
         this.invalidateAfterUpdate();
      }

   }

   public void setTranslationY(float var1) {
      if (this.mTranslationY != var1) {
         this.prepareForUpdate();
         this.mTranslationY = var1;
         this.invalidateAfterUpdate();
      }

   }

   public void setX(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         this.setTranslationX(var1 - (float)var2.getLeft());
      }

   }

   public void setY(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         this.setTranslationY(var1 - (float)var2.getTop());
      }

   }
}
