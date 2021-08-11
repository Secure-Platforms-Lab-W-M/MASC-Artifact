package android.support.design.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Path.FillType;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.support.design.R$color;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;

class ShadowDrawableWrapper extends DrawableWrapper {
   static final double COS_45 = Math.cos(Math.toRadians(45.0D));
   static final float SHADOW_BOTTOM_SCALE = 1.0F;
   static final float SHADOW_HORIZ_SCALE = 0.5F;
   static final float SHADOW_MULTIPLIER = 1.5F;
   static final float SHADOW_TOP_SCALE = 0.25F;
   private boolean mAddPaddingForCorners = true;
   final RectF mContentBounds;
   float mCornerRadius;
   final Paint mCornerShadowPaint;
   Path mCornerShadowPath;
   private boolean mDirty = true;
   final Paint mEdgeShadowPaint;
   float mMaxShadowSize;
   private boolean mPrintedShadowClipWarning = false;
   float mRawMaxShadowSize;
   float mRawShadowSize;
   private float mRotation;
   private final int mShadowEndColor;
   private final int mShadowMiddleColor;
   float mShadowSize;
   private final int mShadowStartColor;

   public ShadowDrawableWrapper(Context var1, Drawable var2, float var3, float var4, float var5) {
      super(var2);
      this.mShadowStartColor = ContextCompat.getColor(var1, R$color.design_fab_shadow_start_color);
      this.mShadowMiddleColor = ContextCompat.getColor(var1, R$color.design_fab_shadow_mid_color);
      this.mShadowEndColor = ContextCompat.getColor(var1, R$color.design_fab_shadow_end_color);
      this.mCornerShadowPaint = new Paint(5);
      this.mCornerShadowPaint.setStyle(Style.FILL);
      this.mCornerRadius = (float)Math.round(var3);
      this.mContentBounds = new RectF();
      this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint);
      this.mEdgeShadowPaint.setAntiAlias(false);
      this.setShadowSize(var4, var5);
   }

   private void buildComponents(Rect var1) {
      float var2 = this.mRawMaxShadowSize * 1.5F;
      this.mContentBounds.set((float)var1.left + this.mRawMaxShadowSize, (float)var1.top + var2, (float)var1.right - this.mRawMaxShadowSize, (float)var1.bottom - var2);
      this.getWrappedDrawable().setBounds((int)this.mContentBounds.left, (int)this.mContentBounds.top, (int)this.mContentBounds.right, (int)this.mContentBounds.bottom);
      this.buildShadowCorners();
   }

   private void buildShadowCorners() {
      float var1 = this.mCornerRadius;
      RectF var7 = new RectF(-var1, -var1, var1, var1);
      RectF var8 = new RectF(var7);
      var1 = this.mShadowSize;
      var8.inset(-var1, -var1);
      Path var9 = this.mCornerShadowPath;
      if (var9 == null) {
         this.mCornerShadowPath = new Path();
      } else {
         var9.reset();
      }

      this.mCornerShadowPath.setFillType(FillType.EVEN_ODD);
      this.mCornerShadowPath.moveTo(-this.mCornerRadius, 0.0F);
      this.mCornerShadowPath.rLineTo(-this.mShadowSize, 0.0F);
      this.mCornerShadowPath.arcTo(var8, 180.0F, 90.0F, false);
      this.mCornerShadowPath.arcTo(var7, 270.0F, -90.0F, false);
      this.mCornerShadowPath.close();
      var1 = -var8.top;
      float var2;
      int var4;
      int var5;
      int var6;
      Paint var12;
      if (var1 > 0.0F) {
         var2 = this.mCornerRadius / var1;
         float var3 = (1.0F - var2) / 2.0F;
         var12 = this.mCornerShadowPaint;
         var4 = this.mShadowStartColor;
         var5 = this.mShadowMiddleColor;
         var6 = this.mShadowEndColor;
         TileMode var10 = TileMode.CLAMP;
         var12.setShader(new RadialGradient(0.0F, 0.0F, var1, new int[]{0, var4, var5, var6}, new float[]{0.0F, var2, var2 + var3, 1.0F}, var10));
      }

      var12 = this.mEdgeShadowPaint;
      var1 = var7.top;
      var2 = var8.top;
      var4 = this.mShadowStartColor;
      var5 = this.mShadowMiddleColor;
      var6 = this.mShadowEndColor;
      TileMode var11 = TileMode.CLAMP;
      var12.setShader(new LinearGradient(0.0F, var1, 0.0F, var2, new int[]{var4, var5, var6}, new float[]{0.0F, 0.5F, 1.0F}, var11));
      this.mEdgeShadowPaint.setAntiAlias(false);
   }

   public static float calculateHorizontalPadding(float var0, float var1, boolean var2) {
      if (var2) {
         double var3 = (double)var0;
         double var5 = COS_45;
         double var7 = (double)var1;
         Double.isNaN(var7);
         Double.isNaN(var3);
         return (float)(var3 + (1.0D - var5) * var7);
      } else {
         return var0;
      }
   }

   public static float calculateVerticalPadding(float var0, float var1, boolean var2) {
      if (var2) {
         double var3 = (double)(1.5F * var0);
         double var5 = COS_45;
         double var7 = (double)var1;
         Double.isNaN(var7);
         Double.isNaN(var3);
         return (float)(var3 + (1.0D - var5) * var7);
      } else {
         return 1.5F * var0;
      }
   }

   private void drawShadow(Canvas var1) {
      int var9 = var1.save();
      var1.rotate(this.mRotation, this.mContentBounds.centerX(), this.mContentBounds.centerY());
      float var5 = -this.mCornerRadius - this.mShadowSize;
      float var6 = this.mCornerRadius;
      float var2 = this.mContentBounds.width();
      boolean var8 = true;
      boolean var7;
      if (var2 - var6 * 2.0F > 0.0F) {
         var7 = true;
      } else {
         var7 = false;
      }

      if (this.mContentBounds.height() - var6 * 2.0F <= 0.0F) {
         var8 = false;
      }

      float var4 = this.mRawShadowSize;
      var2 = var6 / (var6 + (var4 - 0.5F * var4));
      float var3 = var6 / (var6 + (var4 - 0.25F * var4));
      var4 = var6 / (var6 + (var4 - var4 * 1.0F));
      int var10 = var1.save();
      var1.translate(this.mContentBounds.left + var6, this.mContentBounds.top + var6);
      var1.scale(var2, var3);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var7) {
         var1.scale(1.0F / var2, 1.0F);
         var1.drawRect(0.0F, var5, this.mContentBounds.width() - var6 * 2.0F, -this.mCornerRadius, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var10);
      var10 = var1.save();
      var1.translate(this.mContentBounds.right - var6, this.mContentBounds.bottom - var6);
      var1.scale(var2, var4);
      var1.rotate(180.0F);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var7) {
         var1.scale(1.0F / var2, 1.0F);
         var1.drawRect(0.0F, var5, this.mContentBounds.width() - var6 * 2.0F, -this.mCornerRadius + this.mShadowSize, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var10);
      int var11 = var1.save();
      var1.translate(this.mContentBounds.left + var6, this.mContentBounds.bottom - var6);
      var1.scale(var2, var4);
      var1.rotate(270.0F);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var8) {
         var1.scale(1.0F / var4, 1.0F);
         var1.drawRect(0.0F, var5, this.mContentBounds.height() - var6 * 2.0F, -this.mCornerRadius, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var11);
      var11 = var1.save();
      var1.translate(this.mContentBounds.right - var6, this.mContentBounds.top + var6);
      var1.scale(var2, var3);
      var1.rotate(90.0F);
      var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
      if (var8) {
         var1.scale(1.0F / var3, 1.0F);
         var1.drawRect(0.0F, var5, this.mContentBounds.height() - 2.0F * var6, -this.mCornerRadius, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var11);
      var1.restoreToCount(var9);
   }

   private static int toEven(float var0) {
      int var1 = Math.round(var0);
      return var1 % 2 == 1 ? var1 - 1 : var1;
   }

   public void draw(Canvas var1) {
      if (this.mDirty) {
         this.buildComponents(this.getBounds());
         this.mDirty = false;
      }

      this.drawShadow(var1);
      super.draw(var1);
   }

   public float getCornerRadius() {
      return this.mCornerRadius;
   }

   public float getMaxShadowSize() {
      return this.mRawMaxShadowSize;
   }

   public float getMinHeight() {
      float var1 = this.mRawMaxShadowSize;
      var1 = Math.max(var1, this.mCornerRadius + var1 * 1.5F / 2.0F);
      return this.mRawMaxShadowSize * 1.5F * 2.0F + var1 * 2.0F;
   }

   public float getMinWidth() {
      float var1 = this.mRawMaxShadowSize;
      var1 = Math.max(var1, this.mCornerRadius + var1 / 2.0F);
      return this.mRawMaxShadowSize * 2.0F + var1 * 2.0F;
   }

   public int getOpacity() {
      return -3;
   }

   public boolean getPadding(Rect var1) {
      int var2 = (int)Math.ceil((double)calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
      int var3 = (int)Math.ceil((double)calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
      var1.set(var3, var2, var3, var2);
      return true;
   }

   public float getShadowSize() {
      return this.mRawShadowSize;
   }

   protected void onBoundsChange(Rect var1) {
      this.mDirty = true;
   }

   public void setAddPaddingForCorners(boolean var1) {
      this.mAddPaddingForCorners = var1;
      this.invalidateSelf();
   }

   public void setAlpha(int var1) {
      super.setAlpha(var1);
      this.mCornerShadowPaint.setAlpha(var1);
      this.mEdgeShadowPaint.setAlpha(var1);
   }

   public void setCornerRadius(float var1) {
      var1 = (float)Math.round(var1);
      if (this.mCornerRadius != var1) {
         this.mCornerRadius = var1;
         this.mDirty = true;
         this.invalidateSelf();
      }
   }

   public void setMaxShadowSize(float var1) {
      this.setShadowSize(this.mRawShadowSize, var1);
   }

   final void setRotation(float var1) {
      if (this.mRotation != var1) {
         this.mRotation = var1;
         this.invalidateSelf();
      }
   }

   public void setShadowSize(float var1) {
      this.setShadowSize(var1, this.mRawMaxShadowSize);
   }

   void setShadowSize(float var1, float var2) {
      if (var1 >= 0.0F && var2 >= 0.0F) {
         var1 = (float)toEven(var1);
         var2 = (float)toEven(var2);
         if (var1 > var2) {
            var1 = var2;
            if (!this.mPrintedShadowClipWarning) {
               this.mPrintedShadowClipWarning = true;
            }
         }

         if (this.mRawShadowSize != var1 || this.mRawMaxShadowSize != var2) {
            this.mRawShadowSize = var1;
            this.mRawMaxShadowSize = var2;
            this.mShadowSize = (float)Math.round(1.5F * var1);
            this.mMaxShadowSize = var2;
            this.mDirty = true;
            this.invalidateSelf();
         }
      } else {
         throw new IllegalArgumentException("invalid shadow size");
      }
   }
}
