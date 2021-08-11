package com.yalantis.ucrop.util;

import android.view.MotionEvent;

public class RotationGestureDetector {
   private static final int INVALID_POINTER_INDEX = -1;
   // $FF: renamed from: fX float
   private float field_133;
   // $FF: renamed from: fY float
   private float field_134;
   private float mAngle;
   private boolean mIsFirstTouch;
   private RotationGestureDetector.OnRotationGestureListener mListener;
   private int mPointerIndex1;
   private int mPointerIndex2;
   // $FF: renamed from: sX float
   private float field_135;
   // $FF: renamed from: sY float
   private float field_136;

   public RotationGestureDetector(RotationGestureDetector.OnRotationGestureListener var1) {
      this.mListener = var1;
      this.mPointerIndex1 = -1;
      this.mPointerIndex2 = -1;
   }

   private float calculateAngleBetweenLines(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return this.calculateAngleDelta((float)Math.toDegrees((double)((float)Math.atan2((double)(var2 - var4), (double)(var1 - var3)))), (float)Math.toDegrees((double)((float)Math.atan2((double)(var6 - var8), (double)(var5 - var7)))));
   }

   private float calculateAngleDelta(float var1, float var2) {
      var1 = var2 % 360.0F - var1 % 360.0F;
      this.mAngle = var1;
      if (var1 < -180.0F) {
         this.mAngle = var1 + 360.0F;
      } else if (var1 > 180.0F) {
         this.mAngle = var1 - 360.0F;
      }

      return this.mAngle;
   }

   public float getAngle() {
      return this.mAngle;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var6 = var1.getActionMasked();
      if (var6 != 0) {
         if (var6 == 1) {
            this.mPointerIndex1 = -1;
            return true;
         }

         if (var6 != 2) {
            if (var6 != 5) {
               if (var6 != 6) {
                  return true;
               }

               this.mPointerIndex2 = -1;
               return true;
            }

            this.field_133 = var1.getX();
            this.field_134 = var1.getY();
            this.mPointerIndex2 = var1.findPointerIndex(var1.getPointerId(var1.getActionIndex()));
            this.mAngle = 0.0F;
            this.mIsFirstTouch = true;
            return true;
         }

         if (this.mPointerIndex1 != -1 && this.mPointerIndex2 != -1 && var1.getPointerCount() > this.mPointerIndex2) {
            float var2 = var1.getX(this.mPointerIndex1);
            float var3 = var1.getY(this.mPointerIndex1);
            float var4 = var1.getX(this.mPointerIndex2);
            float var5 = var1.getY(this.mPointerIndex2);
            if (this.mIsFirstTouch) {
               this.mAngle = 0.0F;
               this.mIsFirstTouch = false;
            } else {
               this.calculateAngleBetweenLines(this.field_133, this.field_134, this.field_135, this.field_136, var4, var5, var2, var3);
            }

            RotationGestureDetector.OnRotationGestureListener var7 = this.mListener;
            if (var7 != null) {
               var7.onRotation(this);
            }

            this.field_133 = var4;
            this.field_134 = var5;
            this.field_135 = var2;
            this.field_136 = var3;
            return true;
         }
      } else {
         this.field_135 = var1.getX();
         this.field_136 = var1.getY();
         this.mPointerIndex1 = var1.findPointerIndex(var1.getPointerId(0));
         this.mAngle = 0.0F;
         this.mIsFirstTouch = true;
      }

      return true;
   }

   public interface OnRotationGestureListener {
      boolean onRotation(RotationGestureDetector var1);
   }

   public static class SimpleOnRotationGestureListener implements RotationGestureDetector.OnRotationGestureListener {
      public boolean onRotation(RotationGestureDetector var1) {
         return false;
      }
   }
}
