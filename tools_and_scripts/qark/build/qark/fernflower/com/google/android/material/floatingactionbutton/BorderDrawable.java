package com.google.android.material.floatingactionbutton;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapeAppearancePathProvider;

class BorderDrawable extends Drawable {
   private static final float DRAW_STROKE_WIDTH_MULTIPLE = 1.3333F;
   private ColorStateList borderTint;
   float borderWidth;
   private int bottomInnerStrokeColor;
   private int bottomOuterStrokeColor;
   private final RectF boundsRectF = new RectF();
   private int currentBorderTintColor;
   private boolean invalidateShader = true;
   private final Paint paint;
   private final ShapeAppearancePathProvider pathProvider = new ShapeAppearancePathProvider();
   private final Rect rect = new Rect();
   private final RectF rectF = new RectF();
   private ShapeAppearanceModel shapeAppearanceModel;
   private final Path shapePath = new Path();
   private final BorderDrawable.BorderState state = new BorderDrawable.BorderState();
   private int topInnerStrokeColor;
   private int topOuterStrokeColor;

   BorderDrawable(ShapeAppearanceModel var1) {
      this.shapeAppearanceModel = var1;
      Paint var2 = new Paint(1);
      this.paint = var2;
      var2.setStyle(Style.STROKE);
   }

   private Shader createGradientShader() {
      Rect var10 = this.rect;
      this.copyBounds(var10);
      float var1 = this.borderWidth / (float)var10.height();
      int var4 = ColorUtils.compositeColors(this.topOuterStrokeColor, this.currentBorderTintColor);
      int var5 = ColorUtils.compositeColors(this.topInnerStrokeColor, this.currentBorderTintColor);
      int var6 = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.topInnerStrokeColor, 0), this.currentBorderTintColor);
      int var7 = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.bottomInnerStrokeColor, 0), this.currentBorderTintColor);
      int var8 = ColorUtils.compositeColors(this.bottomInnerStrokeColor, this.currentBorderTintColor);
      int var9 = ColorUtils.compositeColors(this.bottomOuterStrokeColor, this.currentBorderTintColor);
      float var2 = (float)var10.top;
      float var3 = (float)var10.bottom;
      TileMode var11 = TileMode.CLAMP;
      return new LinearGradient(0.0F, var2, 0.0F, var3, new int[]{var4, var5, var6, var7, var8, var9}, new float[]{0.0F, var1, 0.5F, 0.5F, 1.0F - var1, 1.0F}, var11);
   }

   public void draw(Canvas var1) {
      if (this.invalidateShader) {
         this.paint.setShader(this.createGradientShader());
         this.invalidateShader = false;
      }

      float var2 = this.paint.getStrokeWidth() / 2.0F;
      this.copyBounds(this.rect);
      this.rectF.set(this.rect);
      float var3 = Math.min(this.shapeAppearanceModel.getTopLeftCornerSize().getCornerSize(this.getBoundsAsRectF()), this.rectF.width() / 2.0F);
      if (this.shapeAppearanceModel.isRoundRect(this.getBoundsAsRectF())) {
         this.rectF.inset(var2, var2);
         var1.drawRoundRect(this.rectF, var3, var3, this.paint);
      }

   }

   protected RectF getBoundsAsRectF() {
      this.boundsRectF.set(this.getBounds());
      return this.boundsRectF;
   }

   public ConstantState getConstantState() {
      return this.state;
   }

   public int getOpacity() {
      return this.borderWidth > 0.0F ? -3 : -2;
   }

   public void getOutline(Outline var1) {
      if (this.shapeAppearanceModel.isRoundRect(this.getBoundsAsRectF())) {
         float var2 = this.shapeAppearanceModel.getTopLeftCornerSize().getCornerSize(this.getBoundsAsRectF());
         var1.setRoundRect(this.getBounds(), var2);
      } else {
         this.copyBounds(this.rect);
         this.rectF.set(this.rect);
         this.pathProvider.calculatePath(this.shapeAppearanceModel, 1.0F, this.rectF, this.shapePath);
         if (this.shapePath.isConvex()) {
            var1.setConvexPath(this.shapePath);
         }

      }
   }

   public boolean getPadding(Rect var1) {
      if (this.shapeAppearanceModel.isRoundRect(this.getBoundsAsRectF())) {
         int var2 = Math.round(this.borderWidth);
         var1.set(var2, var2, var2, var2);
      }

      return true;
   }

   public ShapeAppearanceModel getShapeAppearanceModel() {
      return this.shapeAppearanceModel;
   }

   public boolean isStateful() {
      ColorStateList var1 = this.borderTint;
      return var1 != null && var1.isStateful() || super.isStateful();
   }

   protected void onBoundsChange(Rect var1) {
      this.invalidateShader = true;
   }

   protected boolean onStateChange(int[] var1) {
      ColorStateList var3 = this.borderTint;
      if (var3 != null) {
         int var2 = var3.getColorForState(var1, this.currentBorderTintColor);
         if (var2 != this.currentBorderTintColor) {
            this.invalidateShader = true;
            this.currentBorderTintColor = var2;
         }
      }

      if (this.invalidateShader) {
         this.invalidateSelf();
      }

      return this.invalidateShader;
   }

   public void setAlpha(int var1) {
      this.paint.setAlpha(var1);
      this.invalidateSelf();
   }

   void setBorderTint(ColorStateList var1) {
      if (var1 != null) {
         this.currentBorderTintColor = var1.getColorForState(this.getState(), this.currentBorderTintColor);
      }

      this.borderTint = var1;
      this.invalidateShader = true;
      this.invalidateSelf();
   }

   public void setBorderWidth(float var1) {
      if (this.borderWidth != var1) {
         this.borderWidth = var1;
         this.paint.setStrokeWidth(1.3333F * var1);
         this.invalidateShader = true;
         this.invalidateSelf();
      }

   }

   public void setColorFilter(ColorFilter var1) {
      this.paint.setColorFilter(var1);
      this.invalidateSelf();
   }

   void setGradientColors(int var1, int var2, int var3, int var4) {
      this.topOuterStrokeColor = var1;
      this.topInnerStrokeColor = var2;
      this.bottomOuterStrokeColor = var3;
      this.bottomInnerStrokeColor = var4;
   }

   public void setShapeAppearanceModel(ShapeAppearanceModel var1) {
      this.shapeAppearanceModel = var1;
      this.invalidateSelf();
   }

   private class BorderState extends ConstantState {
      private BorderState() {
      }

      // $FF: synthetic method
      BorderState(Object var2) {
         this();
      }

      public int getChangingConfigurations() {
         return 0;
      }

      public Drawable newDrawable() {
         return BorderDrawable.this;
      }
   }
}
