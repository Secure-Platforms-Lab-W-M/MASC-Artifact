package com.yalantis.ucrop.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import com.yalantis.ucrop.util.RotationGestureDetector;

public class GestureCropImageView extends CropImageView {
   private static final int DOUBLE_TAP_ZOOM_DURATION = 200;
   private int mDoubleTapScaleSteps;
   private GestureDetector mGestureDetector;
   private boolean mIsRotateEnabled;
   private boolean mIsScaleEnabled;
   private float mMidPntX;
   private float mMidPntY;
   private RotationGestureDetector mRotateDetector;
   private ScaleGestureDetector mScaleDetector;

   public GestureCropImageView(Context var1) {
      super(var1);
      this.mIsRotateEnabled = true;
      this.mIsScaleEnabled = true;
      this.mDoubleTapScaleSteps = 5;
   }

   public GestureCropImageView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public GestureCropImageView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mIsRotateEnabled = true;
      this.mIsScaleEnabled = true;
      this.mDoubleTapScaleSteps = 5;
   }

   private void setupGestureListeners() {
      this.mGestureDetector = new GestureDetector(this.getContext(), new GestureCropImageView.GestureListener(), (Handler)null, true);
      this.mScaleDetector = new ScaleGestureDetector(this.getContext(), new GestureCropImageView.ScaleListener());
      this.mRotateDetector = new RotationGestureDetector(new GestureCropImageView.RotateListener());
   }

   public int getDoubleTapScaleSteps() {
      return this.mDoubleTapScaleSteps;
   }

   protected float getDoubleTapTargetScale() {
      return this.getCurrentScale() * (float)Math.pow((double)(this.getMaxScale() / this.getMinScale()), (double)(1.0F / (float)this.mDoubleTapScaleSteps));
   }

   protected void init() {
      super.init();
      this.setupGestureListeners();
   }

   public boolean isRotateEnabled() {
      return this.mIsRotateEnabled;
   }

   public boolean isScaleEnabled() {
      return this.mIsScaleEnabled;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if ((var1.getAction() & 255) == 0) {
         this.cancelAllAnimations();
      }

      if (var1.getPointerCount() > 1) {
         this.mMidPntX = (var1.getX(0) + var1.getX(1)) / 2.0F;
         this.mMidPntY = (var1.getY(0) + var1.getY(1)) / 2.0F;
      }

      this.mGestureDetector.onTouchEvent(var1);
      if (this.mIsScaleEnabled) {
         this.mScaleDetector.onTouchEvent(var1);
      }

      if (this.mIsRotateEnabled) {
         this.mRotateDetector.onTouchEvent(var1);
      }

      if ((var1.getAction() & 255) == 1) {
         this.setImageToWrapCropBounds();
      }

      return true;
   }

   public void setDoubleTapScaleSteps(int var1) {
      this.mDoubleTapScaleSteps = var1;
   }

   public void setRotateEnabled(boolean var1) {
      this.mIsRotateEnabled = var1;
   }

   public void setScaleEnabled(boolean var1) {
      this.mIsScaleEnabled = var1;
   }

   private class GestureListener extends SimpleOnGestureListener {
      private GestureListener() {
      }

      // $FF: synthetic method
      GestureListener(Object var2) {
         this();
      }

      public boolean onDoubleTap(MotionEvent var1) {
         GestureCropImageView var2 = GestureCropImageView.this;
         var2.zoomImageToPosition(var2.getDoubleTapTargetScale(), var1.getX(), var1.getY(), 200L);
         return super.onDoubleTap(var1);
      }

      public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4) {
         GestureCropImageView.this.postTranslate(-var3, -var4);
         return true;
      }
   }

   private class RotateListener extends RotationGestureDetector.SimpleOnRotationGestureListener {
      private RotateListener() {
      }

      // $FF: synthetic method
      RotateListener(Object var2) {
         this();
      }

      public boolean onRotation(RotationGestureDetector var1) {
         GestureCropImageView.this.postRotate(var1.getAngle(), GestureCropImageView.this.mMidPntX, GestureCropImageView.this.mMidPntY);
         return true;
      }
   }

   private class ScaleListener extends SimpleOnScaleGestureListener {
      private ScaleListener() {
      }

      // $FF: synthetic method
      ScaleListener(Object var2) {
         this();
      }

      public boolean onScale(ScaleGestureDetector var1) {
         GestureCropImageView.this.postScale(var1.getScaleFactor(), GestureCropImageView.this.mMidPntX, GestureCropImageView.this.mMidPntY);
         return true;
      }
   }
}
