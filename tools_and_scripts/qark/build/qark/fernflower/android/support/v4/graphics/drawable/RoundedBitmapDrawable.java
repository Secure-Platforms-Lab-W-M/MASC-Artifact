package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

@RequiresApi(9)
public abstract class RoundedBitmapDrawable extends Drawable {
   private static final int DEFAULT_PAINT_FLAGS = 3;
   private boolean mApplyGravity = true;
   final Bitmap mBitmap;
   private int mBitmapHeight;
   private final BitmapShader mBitmapShader;
   private int mBitmapWidth;
   private float mCornerRadius;
   final Rect mDstRect = new Rect();
   private final RectF mDstRectF = new RectF();
   private int mGravity = 119;
   private boolean mIsCircular;
   private final Paint mPaint = new Paint(3);
   private final Matrix mShaderMatrix = new Matrix();
   private int mTargetDensity = 160;

   RoundedBitmapDrawable(Resources var1, Bitmap var2) {
      if (var1 != null) {
         this.mTargetDensity = var1.getDisplayMetrics().densityDpi;
      }

      this.mBitmap = var2;
      if (this.mBitmap != null) {
         this.computeBitmapSize();
         this.mBitmapShader = new BitmapShader(this.mBitmap, TileMode.CLAMP, TileMode.CLAMP);
      } else {
         this.mBitmapHeight = -1;
         this.mBitmapWidth = -1;
         this.mBitmapShader = null;
      }
   }

   private void computeBitmapSize() {
      this.mBitmapWidth = this.mBitmap.getScaledWidth(this.mTargetDensity);
      this.mBitmapHeight = this.mBitmap.getScaledHeight(this.mTargetDensity);
   }

   private static boolean isGreaterThanZero(float var0) {
      return var0 > 0.05F;
   }

   private void updateCircularCornerRadius() {
      this.mCornerRadius = (float)(Math.min(this.mBitmapHeight, this.mBitmapWidth) / 2);
   }

   public void draw(Canvas var1) {
      Bitmap var3 = this.mBitmap;
      if (var3 != null) {
         this.updateDstRect();
         if (this.mPaint.getShader() == null) {
            var1.drawBitmap(var3, (Rect)null, this.mDstRect, this.mPaint);
         } else {
            RectF var4 = this.mDstRectF;
            float var2 = this.mCornerRadius;
            var1.drawRoundRect(var4, var2, var2, this.mPaint);
         }
      }
   }

   public int getAlpha() {
      return this.mPaint.getAlpha();
   }

   public final Bitmap getBitmap() {
      return this.mBitmap;
   }

   public ColorFilter getColorFilter() {
      return this.mPaint.getColorFilter();
   }

   public float getCornerRadius() {
      return this.mCornerRadius;
   }

   public int getGravity() {
      return this.mGravity;
   }

   public int getIntrinsicHeight() {
      return this.mBitmapHeight;
   }

   public int getIntrinsicWidth() {
      return this.mBitmapWidth;
   }

   public int getOpacity() {
      if (this.mGravity == 119) {
         if (this.mIsCircular) {
            return -3;
         } else {
            Bitmap var1 = this.mBitmap;
            return var1 != null && !var1.hasAlpha() && this.mPaint.getAlpha() >= 255 && !isGreaterThanZero(this.mCornerRadius) ? -1 : -3;
         }
      } else {
         return -3;
      }
   }

   public final Paint getPaint() {
      return this.mPaint;
   }

   void gravityCompatApply(int var1, int var2, int var3, Rect var4, Rect var5) {
      throw new UnsupportedOperationException();
   }

   public boolean hasAntiAlias() {
      return this.mPaint.isAntiAlias();
   }

   public boolean hasMipMap() {
      throw new UnsupportedOperationException();
   }

   public boolean isCircular() {
      return this.mIsCircular;
   }

   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      if (this.mIsCircular) {
         this.updateCircularCornerRadius();
      }

      this.mApplyGravity = true;
   }

   public void setAlpha(int var1) {
      if (var1 != this.mPaint.getAlpha()) {
         this.mPaint.setAlpha(var1);
         this.invalidateSelf();
      }

   }

   public void setAntiAlias(boolean var1) {
      this.mPaint.setAntiAlias(var1);
      this.invalidateSelf();
   }

   public void setCircular(boolean var1) {
      this.mIsCircular = var1;
      this.mApplyGravity = true;
      if (var1) {
         this.updateCircularCornerRadius();
         this.mPaint.setShader(this.mBitmapShader);
         this.invalidateSelf();
      } else {
         this.setCornerRadius(0.0F);
      }
   }

   public void setColorFilter(ColorFilter var1) {
      this.mPaint.setColorFilter(var1);
      this.invalidateSelf();
   }

   public void setCornerRadius(float var1) {
      if (this.mCornerRadius != var1) {
         this.mIsCircular = false;
         if (isGreaterThanZero(var1)) {
            this.mPaint.setShader(this.mBitmapShader);
         } else {
            this.mPaint.setShader((Shader)null);
         }

         this.mCornerRadius = var1;
         this.invalidateSelf();
      }
   }

   public void setDither(boolean var1) {
      this.mPaint.setDither(var1);
      this.invalidateSelf();
   }

   public void setFilterBitmap(boolean var1) {
      this.mPaint.setFilterBitmap(var1);
      this.invalidateSelf();
   }

   public void setGravity(int var1) {
      if (this.mGravity != var1) {
         this.mGravity = var1;
         this.mApplyGravity = true;
         this.invalidateSelf();
      }

   }

   public void setMipMap(boolean var1) {
      throw new UnsupportedOperationException();
   }

   public void setTargetDensity(int var1) {
      if (this.mTargetDensity != var1) {
         if (var1 == 0) {
            var1 = 160;
         }

         this.mTargetDensity = var1;
         if (this.mBitmap != null) {
            this.computeBitmapSize();
         }

         this.invalidateSelf();
      }

   }

   public void setTargetDensity(Canvas var1) {
      this.setTargetDensity(var1.getDensity());
   }

   public void setTargetDensity(DisplayMetrics var1) {
      this.setTargetDensity(var1.densityDpi);
   }

   void updateDstRect() {
      if (this.mApplyGravity) {
         if (this.mIsCircular) {
            int var1 = Math.min(this.mBitmapWidth, this.mBitmapHeight);
            this.gravityCompatApply(this.mGravity, var1, var1, this.getBounds(), this.mDstRect);
            var1 = Math.min(this.mDstRect.width(), this.mDstRect.height());
            int var2 = Math.max(0, (this.mDstRect.width() - var1) / 2);
            int var3 = Math.max(0, (this.mDstRect.height() - var1) / 2);
            this.mDstRect.inset(var2, var3);
            this.mCornerRadius = (float)var1 * 0.5F;
         } else {
            this.gravityCompatApply(this.mGravity, this.mBitmapWidth, this.mBitmapHeight, this.getBounds(), this.mDstRect);
         }

         this.mDstRectF.set(this.mDstRect);
         if (this.mBitmapShader != null) {
            this.mShaderMatrix.setTranslate(this.mDstRectF.left, this.mDstRectF.top);
            this.mShaderMatrix.preScale(this.mDstRectF.width() / (float)this.mBitmap.getWidth(), this.mDstRectF.height() / (float)this.mBitmap.getHeight());
            this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
            this.mPaint.setShader(this.mBitmapShader);
         }

         this.mApplyGravity = false;
      }

   }
}
