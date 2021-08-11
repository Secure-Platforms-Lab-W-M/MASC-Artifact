package android.support.design.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.ColorUtils;

class CircularBorderDrawable extends Drawable {
   private static final float DRAW_STROKE_WIDTH_MULTIPLE = 1.3333F;
   private ColorStateList mBorderTint;
   float mBorderWidth;
   private int mBottomInnerStrokeColor;
   private int mBottomOuterStrokeColor;
   private int mCurrentBorderTintColor;
   private boolean mInvalidateShader = true;
   final Paint mPaint = new Paint(1);
   final Rect mRect = new Rect();
   final RectF mRectF = new RectF();
   private float mRotation;
   private int mTopInnerStrokeColor;
   private int mTopOuterStrokeColor;

   public CircularBorderDrawable() {
      this.mPaint.setStyle(Style.STROKE);
   }

   private Shader createGradientShader() {
      Rect var10 = this.mRect;
      this.copyBounds(var10);
      float var1 = this.mBorderWidth / (float)var10.height();
      int var4 = ColorUtils.compositeColors(this.mTopOuterStrokeColor, this.mCurrentBorderTintColor);
      int var5 = ColorUtils.compositeColors(this.mTopInnerStrokeColor, this.mCurrentBorderTintColor);
      int var6 = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.mTopInnerStrokeColor, 0), this.mCurrentBorderTintColor);
      int var7 = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.mBottomInnerStrokeColor, 0), this.mCurrentBorderTintColor);
      int var8 = ColorUtils.compositeColors(this.mBottomInnerStrokeColor, this.mCurrentBorderTintColor);
      int var9 = ColorUtils.compositeColors(this.mBottomOuterStrokeColor, this.mCurrentBorderTintColor);
      float var2 = (float)var10.top;
      float var3 = (float)var10.bottom;
      TileMode var11 = TileMode.CLAMP;
      return new LinearGradient(0.0F, var2, 0.0F, var3, new int[]{var4, var5, var6, var7, var8, var9}, new float[]{0.0F, var1, 0.5F, 0.5F, 1.0F - var1, 1.0F}, var11);
   }

   public void draw(Canvas var1) {
      if (this.mInvalidateShader) {
         this.mPaint.setShader(this.createGradientShader());
         this.mInvalidateShader = false;
      }

      float var2 = this.mPaint.getStrokeWidth() / 2.0F;
      RectF var3 = this.mRectF;
      this.copyBounds(this.mRect);
      var3.set(this.mRect);
      var3.left += var2;
      var3.top += var2;
      var3.right -= var2;
      var3.bottom -= var2;
      var1.save();
      var1.rotate(this.mRotation, var3.centerX(), var3.centerY());
      var1.drawOval(var3, this.mPaint);
      var1.restore();
   }

   public int getOpacity() {
      return this.mBorderWidth > 0.0F ? -3 : -2;
   }

   public boolean getPadding(Rect var1) {
      int var2 = Math.round(this.mBorderWidth);
      var1.set(var2, var2, var2, var2);
      return true;
   }

   public boolean isStateful() {
      ColorStateList var1 = this.mBorderTint;
      return var1 != null && var1.isStateful() || super.isStateful();
   }

   protected void onBoundsChange(Rect var1) {
      this.mInvalidateShader = true;
   }

   protected boolean onStateChange(int[] var1) {
      ColorStateList var3 = this.mBorderTint;
      if (var3 != null) {
         int var2 = var3.getColorForState(var1, this.mCurrentBorderTintColor);
         if (var2 != this.mCurrentBorderTintColor) {
            this.mInvalidateShader = true;
            this.mCurrentBorderTintColor = var2;
         }
      }

      if (this.mInvalidateShader) {
         this.invalidateSelf();
      }

      return this.mInvalidateShader;
   }

   public void setAlpha(int var1) {
      this.mPaint.setAlpha(var1);
      this.invalidateSelf();
   }

   void setBorderTint(ColorStateList var1) {
      if (var1 != null) {
         this.mCurrentBorderTintColor = var1.getColorForState(this.getState(), this.mCurrentBorderTintColor);
      }

      this.mBorderTint = var1;
      this.mInvalidateShader = true;
      this.invalidateSelf();
   }

   void setBorderWidth(float var1) {
      if (this.mBorderWidth != var1) {
         this.mBorderWidth = var1;
         this.mPaint.setStrokeWidth(1.3333F * var1);
         this.mInvalidateShader = true;
         this.invalidateSelf();
      }
   }

   public void setColorFilter(ColorFilter var1) {
      this.mPaint.setColorFilter(var1);
      this.invalidateSelf();
   }

   void setGradientColors(int var1, int var2, int var3, int var4) {
      this.mTopOuterStrokeColor = var1;
      this.mTopInnerStrokeColor = var2;
      this.mBottomOuterStrokeColor = var3;
      this.mBottomInnerStrokeColor = var4;
   }

   final void setRotation(float var1) {
      if (var1 != this.mRotation) {
         this.mRotation = var1;
         this.invalidateSelf();
      }
   }
}
