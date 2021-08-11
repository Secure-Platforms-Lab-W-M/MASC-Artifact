package androidx.cardview.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;

class RoundRectDrawable extends Drawable {
   private ColorStateList mBackground;
   private final RectF mBoundsF;
   private final Rect mBoundsI;
   private boolean mInsetForPadding = false;
   private boolean mInsetForRadius = true;
   private float mPadding;
   private final Paint mPaint;
   private float mRadius;
   private ColorStateList mTint;
   private PorterDuffColorFilter mTintFilter;
   private Mode mTintMode;

   RoundRectDrawable(ColorStateList var1, float var2) {
      this.mTintMode = Mode.SRC_IN;
      this.mRadius = var2;
      this.mPaint = new Paint(5);
      this.setBackground(var1);
      this.mBoundsF = new RectF();
      this.mBoundsI = new Rect();
   }

   private PorterDuffColorFilter createTintFilter(ColorStateList var1, Mode var2) {
      return var1 != null && var2 != null ? new PorterDuffColorFilter(var1.getColorForState(this.getState(), 0), var2) : null;
   }

   private void setBackground(ColorStateList var1) {
      if (var1 == null) {
         var1 = ColorStateList.valueOf(0);
      }

      this.mBackground = var1;
      this.mPaint.setColor(var1.getColorForState(this.getState(), this.mBackground.getDefaultColor()));
   }

   private void updateBounds(Rect var1) {
      Rect var4 = var1;
      if (var1 == null) {
         var4 = this.getBounds();
      }

      this.mBoundsF.set((float)var4.left, (float)var4.top, (float)var4.right, (float)var4.bottom);
      this.mBoundsI.set(var4);
      if (this.mInsetForPadding) {
         float var2 = RoundRectDrawableWithShadow.calculateVerticalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
         float var3 = RoundRectDrawableWithShadow.calculateHorizontalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
         this.mBoundsI.inset((int)Math.ceil((double)var3), (int)Math.ceil((double)var2));
         this.mBoundsF.set(this.mBoundsI);
      }

   }

   public void draw(Canvas var1) {
      Paint var4 = this.mPaint;
      boolean var3;
      if (this.mTintFilter != null && var4.getColorFilter() == null) {
         var4.setColorFilter(this.mTintFilter);
         var3 = true;
      } else {
         var3 = false;
      }

      RectF var5 = this.mBoundsF;
      float var2 = this.mRadius;
      var1.drawRoundRect(var5, var2, var2, var4);
      if (var3) {
         var4.setColorFilter((ColorFilter)null);
      }

   }

   public ColorStateList getColor() {
      return this.mBackground;
   }

   public int getOpacity() {
      return -3;
   }

   public void getOutline(Outline var1) {
      var1.setRoundRect(this.mBoundsI, this.mRadius);
   }

   float getPadding() {
      return this.mPadding;
   }

   public float getRadius() {
      return this.mRadius;
   }

   public boolean isStateful() {
      ColorStateList var1 = this.mTint;
      if (var1 == null || !var1.isStateful()) {
         var1 = this.mBackground;
         if ((var1 == null || !var1.isStateful()) && !super.isStateful()) {
            return false;
         }
      }

      return true;
   }

   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      this.updateBounds(var1);
   }

   protected boolean onStateChange(int[] var1) {
      ColorStateList var4 = this.mBackground;
      int var2 = var4.getColorForState(var1, var4.getDefaultColor());
      boolean var3;
      if (var2 != this.mPaint.getColor()) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         this.mPaint.setColor(var2);
      }

      ColorStateList var5 = this.mTint;
      if (var5 != null) {
         Mode var6 = this.mTintMode;
         if (var6 != null) {
            this.mTintFilter = this.createTintFilter(var5, var6);
            return true;
         }
      }

      return var3;
   }

   public void setAlpha(int var1) {
      this.mPaint.setAlpha(var1);
   }

   public void setColor(ColorStateList var1) {
      this.setBackground(var1);
      this.invalidateSelf();
   }

   public void setColorFilter(ColorFilter var1) {
      this.mPaint.setColorFilter(var1);
   }

   void setPadding(float var1, boolean var2, boolean var3) {
      if (var1 != this.mPadding || this.mInsetForPadding != var2 || this.mInsetForRadius != var3) {
         this.mPadding = var1;
         this.mInsetForPadding = var2;
         this.mInsetForRadius = var3;
         this.updateBounds((Rect)null);
         this.invalidateSelf();
      }
   }

   void setRadius(float var1) {
      if (var1 != this.mRadius) {
         this.mRadius = var1;
         this.updateBounds((Rect)null);
         this.invalidateSelf();
      }
   }

   public void setTintList(ColorStateList var1) {
      this.mTint = var1;
      this.mTintFilter = this.createTintFilter(var1, this.mTintMode);
      this.invalidateSelf();
   }

   public void setTintMode(Mode var1) {
      this.mTintMode = var1;
      this.mTintFilter = this.createTintFilter(this.mTint, var1);
      this.invalidateSelf();
   }
}
