package com.yalantis.ucrop.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class FastBitmapDrawable extends Drawable {
   private int mAlpha = 255;
   private Bitmap mBitmap;
   private int mHeight;
   private final Paint mPaint = new Paint(2);
   private int mWidth;

   public FastBitmapDrawable(Bitmap var1) {
      this.setBitmap(var1);
   }

   public void draw(Canvas var1) {
      Bitmap var2 = this.mBitmap;
      if (var2 != null && !var2.isRecycled()) {
         var1.drawBitmap(this.mBitmap, (Rect)null, this.getBounds(), this.mPaint);
      }

   }

   public int getAlpha() {
      return this.mAlpha;
   }

   public Bitmap getBitmap() {
      return this.mBitmap;
   }

   public int getIntrinsicHeight() {
      return this.mHeight;
   }

   public int getIntrinsicWidth() {
      return this.mWidth;
   }

   public int getMinimumHeight() {
      return this.mHeight;
   }

   public int getMinimumWidth() {
      return this.mWidth;
   }

   public int getOpacity() {
      return -3;
   }

   public void setAlpha(int var1) {
      this.mAlpha = var1;
      this.mPaint.setAlpha(var1);
   }

   public void setBitmap(Bitmap var1) {
      this.mBitmap = var1;
      if (var1 != null) {
         this.mWidth = var1.getWidth();
         this.mHeight = this.mBitmap.getHeight();
      } else {
         this.mHeight = 0;
         this.mWidth = 0;
      }
   }

   public void setColorFilter(ColorFilter var1) {
      this.mPaint.setColorFilter(var1);
   }

   public void setFilterBitmap(boolean var1) {
      this.mPaint.setFilterBitmap(var1);
   }
}
