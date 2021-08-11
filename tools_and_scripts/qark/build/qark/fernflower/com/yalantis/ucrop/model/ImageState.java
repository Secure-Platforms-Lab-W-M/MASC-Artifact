package com.yalantis.ucrop.model;

import android.graphics.RectF;

public class ImageState {
   private RectF mCropRect;
   private float mCurrentAngle;
   private RectF mCurrentImageRect;
   private float mCurrentScale;

   public ImageState(RectF var1, RectF var2, float var3, float var4) {
      this.mCropRect = var1;
      this.mCurrentImageRect = var2;
      this.mCurrentScale = var3;
      this.mCurrentAngle = var4;
   }

   public RectF getCropRect() {
      return this.mCropRect;
   }

   public float getCurrentAngle() {
      return this.mCurrentAngle;
   }

   public RectF getCurrentImageRect() {
      return this.mCurrentImageRect;
   }

   public float getCurrentScale() {
      return this.mCurrentScale;
   }
}
