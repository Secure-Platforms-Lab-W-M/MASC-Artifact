package com.google.android.material.button;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.styleable;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleDrawableCompat;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;

class MaterialButtonHelper {
   private static final boolean IS_LOLLIPOP;
   private boolean backgroundOverwritten = false;
   private ColorStateList backgroundTint;
   private Mode backgroundTintMode;
   private boolean checkable;
   private int cornerRadius;
   private boolean cornerRadiusSet = false;
   private int insetBottom;
   private int insetLeft;
   private int insetRight;
   private int insetTop;
   private Drawable maskDrawable;
   private final MaterialButton materialButton;
   private ColorStateList rippleColor;
   private LayerDrawable rippleDrawable;
   private ShapeAppearanceModel shapeAppearanceModel;
   private boolean shouldDrawSurfaceColorStroke = false;
   private ColorStateList strokeColor;
   private int strokeWidth;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      IS_LOLLIPOP = var0;
   }

   MaterialButtonHelper(MaterialButton var1, ShapeAppearanceModel var2) {
      this.materialButton = var1;
      this.shapeAppearanceModel = var2;
   }

   private Drawable createBackground() {
      MaterialShapeDrawable var3 = new MaterialShapeDrawable(this.shapeAppearanceModel);
      var3.initializeElevationOverlay(this.materialButton.getContext());
      DrawableCompat.setTintList(var3, this.backgroundTint);
      Mode var4 = this.backgroundTintMode;
      if (var4 != null) {
         DrawableCompat.setTintMode(var3, var4);
      }

      var3.setStroke((float)this.strokeWidth, this.strokeColor);
      MaterialShapeDrawable var8 = new MaterialShapeDrawable(this.shapeAppearanceModel);
      var8.setTint(0);
      float var1 = (float)this.strokeWidth;
      int var2;
      if (this.shouldDrawSurfaceColorStroke) {
         var2 = MaterialColors.getColor(this.materialButton, attr.colorSurface);
      } else {
         var2 = 0;
      }

      var8.setStroke(var1, var2);
      if (IS_LOLLIPOP) {
         MaterialShapeDrawable var9 = new MaterialShapeDrawable(this.shapeAppearanceModel);
         this.maskDrawable = var9;
         DrawableCompat.setTint(var9, -1);
         RippleDrawable var7 = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(this.rippleColor), this.wrapDrawableWithInset(new LayerDrawable(new Drawable[]{var8, var3})), this.maskDrawable);
         this.rippleDrawable = var7;
         return var7;
      } else {
         RippleDrawableCompat var5 = new RippleDrawableCompat(this.shapeAppearanceModel);
         this.maskDrawable = var5;
         DrawableCompat.setTintList(var5, RippleUtils.sanitizeRippleDrawableColor(this.rippleColor));
         LayerDrawable var6 = new LayerDrawable(new Drawable[]{var8, var3, this.maskDrawable});
         this.rippleDrawable = var6;
         return this.wrapDrawableWithInset(var6);
      }
   }

   private MaterialShapeDrawable getMaterialShapeDrawable(boolean var1) {
      LayerDrawable var2 = this.rippleDrawable;
      if (var2 != null && var2.getNumberOfLayers() > 0) {
         return IS_LOLLIPOP ? (MaterialShapeDrawable)((LayerDrawable)((InsetDrawable)this.rippleDrawable.getDrawable(0)).getDrawable()).getDrawable(var1 ^ 1) : (MaterialShapeDrawable)this.rippleDrawable.getDrawable(var1 ^ 1);
      } else {
         return null;
      }
   }

   private MaterialShapeDrawable getSurfaceColorStrokeDrawable() {
      return this.getMaterialShapeDrawable(true);
   }

   private void updateButtonShape(ShapeAppearanceModel var1) {
      if (this.getMaterialShapeDrawable() != null) {
         this.getMaterialShapeDrawable().setShapeAppearanceModel(var1);
      }

      if (this.getSurfaceColorStrokeDrawable() != null) {
         this.getSurfaceColorStrokeDrawable().setShapeAppearanceModel(var1);
      }

      if (this.getMaskDrawable() != null) {
         this.getMaskDrawable().setShapeAppearanceModel(var1);
      }

   }

   private void updateStroke() {
      MaterialShapeDrawable var3 = this.getMaterialShapeDrawable();
      MaterialShapeDrawable var4 = this.getSurfaceColorStrokeDrawable();
      if (var3 != null) {
         var3.setStroke((float)this.strokeWidth, this.strokeColor);
         if (var4 != null) {
            float var1 = (float)this.strokeWidth;
            int var2;
            if (this.shouldDrawSurfaceColorStroke) {
               var2 = MaterialColors.getColor(this.materialButton, attr.colorSurface);
            } else {
               var2 = 0;
            }

            var4.setStroke(var1, var2);
         }
      }

   }

   private InsetDrawable wrapDrawableWithInset(Drawable var1) {
      return new InsetDrawable(var1, this.insetLeft, this.insetTop, this.insetRight, this.insetBottom);
   }

   int getCornerRadius() {
      return this.cornerRadius;
   }

   public Shapeable getMaskDrawable() {
      LayerDrawable var1 = this.rippleDrawable;
      if (var1 != null && var1.getNumberOfLayers() > 1) {
         return this.rippleDrawable.getNumberOfLayers() > 2 ? (Shapeable)this.rippleDrawable.getDrawable(2) : (Shapeable)this.rippleDrawable.getDrawable(1);
      } else {
         return null;
      }
   }

   MaterialShapeDrawable getMaterialShapeDrawable() {
      return this.getMaterialShapeDrawable(false);
   }

   ColorStateList getRippleColor() {
      return this.rippleColor;
   }

   ShapeAppearanceModel getShapeAppearanceModel() {
      return this.shapeAppearanceModel;
   }

   ColorStateList getStrokeColor() {
      return this.strokeColor;
   }

   int getStrokeWidth() {
      return this.strokeWidth;
   }

   ColorStateList getSupportBackgroundTintList() {
      return this.backgroundTint;
   }

   Mode getSupportBackgroundTintMode() {
      return this.backgroundTintMode;
   }

   boolean isBackgroundOverwritten() {
      return this.backgroundOverwritten;
   }

   boolean isCheckable() {
      return this.checkable;
   }

   void loadFromAttributes(TypedArray var1) {
      this.insetLeft = var1.getDimensionPixelOffset(styleable.MaterialButton_android_insetLeft, 0);
      this.insetRight = var1.getDimensionPixelOffset(styleable.MaterialButton_android_insetRight, 0);
      this.insetTop = var1.getDimensionPixelOffset(styleable.MaterialButton_android_insetTop, 0);
      this.insetBottom = var1.getDimensionPixelOffset(styleable.MaterialButton_android_insetBottom, 0);
      int var2;
      if (var1.hasValue(styleable.MaterialButton_cornerRadius)) {
         var2 = var1.getDimensionPixelSize(styleable.MaterialButton_cornerRadius, -1);
         this.cornerRadius = var2;
         this.setShapeAppearanceModel(this.shapeAppearanceModel.withCornerSize((float)var2));
         this.cornerRadiusSet = true;
      }

      this.strokeWidth = var1.getDimensionPixelSize(styleable.MaterialButton_strokeWidth, 0);
      this.backgroundTintMode = ViewUtils.parseTintMode(var1.getInt(styleable.MaterialButton_backgroundTintMode, -1), Mode.SRC_IN);
      this.backgroundTint = MaterialResources.getColorStateList(this.materialButton.getContext(), var1, styleable.MaterialButton_backgroundTint);
      this.strokeColor = MaterialResources.getColorStateList(this.materialButton.getContext(), var1, styleable.MaterialButton_strokeColor);
      this.rippleColor = MaterialResources.getColorStateList(this.materialButton.getContext(), var1, styleable.MaterialButton_rippleColor);
      this.checkable = var1.getBoolean(styleable.MaterialButton_android_checkable, false);
      var2 = var1.getDimensionPixelSize(styleable.MaterialButton_elevation, 0);
      int var3 = ViewCompat.getPaddingStart(this.materialButton);
      int var4 = this.materialButton.getPaddingTop();
      int var5 = ViewCompat.getPaddingEnd(this.materialButton);
      int var6 = this.materialButton.getPaddingBottom();
      this.materialButton.setInternalBackground(this.createBackground());
      MaterialShapeDrawable var7 = this.getMaterialShapeDrawable();
      if (var7 != null) {
         var7.setElevation((float)var2);
      }

      ViewCompat.setPaddingRelative(this.materialButton, this.insetLeft + var3, this.insetTop + var4, this.insetRight + var5, this.insetBottom + var6);
   }

   void setBackgroundColor(int var1) {
      if (this.getMaterialShapeDrawable() != null) {
         this.getMaterialShapeDrawable().setTint(var1);
      }

   }

   void setBackgroundOverwritten() {
      this.backgroundOverwritten = true;
      this.materialButton.setSupportBackgroundTintList(this.backgroundTint);
      this.materialButton.setSupportBackgroundTintMode(this.backgroundTintMode);
   }

   void setCheckable(boolean var1) {
      this.checkable = var1;
   }

   void setCornerRadius(int var1) {
      if (!this.cornerRadiusSet || this.cornerRadius != var1) {
         this.cornerRadius = var1;
         this.cornerRadiusSet = true;
         this.setShapeAppearanceModel(this.shapeAppearanceModel.withCornerSize((float)var1));
      }

   }

   void setRippleColor(ColorStateList var1) {
      if (this.rippleColor != var1) {
         this.rippleColor = var1;
         if (IS_LOLLIPOP && this.materialButton.getBackground() instanceof RippleDrawable) {
            ((RippleDrawable)this.materialButton.getBackground()).setColor(RippleUtils.sanitizeRippleDrawableColor(var1));
            return;
         }

         if (!IS_LOLLIPOP && this.materialButton.getBackground() instanceof RippleDrawableCompat) {
            ((RippleDrawableCompat)this.materialButton.getBackground()).setTintList(RippleUtils.sanitizeRippleDrawableColor(var1));
         }
      }

   }

   void setShapeAppearanceModel(ShapeAppearanceModel var1) {
      this.shapeAppearanceModel = var1;
      this.updateButtonShape(var1);
   }

   void setShouldDrawSurfaceColorStroke(boolean var1) {
      this.shouldDrawSurfaceColorStroke = var1;
      this.updateStroke();
   }

   void setStrokeColor(ColorStateList var1) {
      if (this.strokeColor != var1) {
         this.strokeColor = var1;
         this.updateStroke();
      }

   }

   void setStrokeWidth(int var1) {
      if (this.strokeWidth != var1) {
         this.strokeWidth = var1;
         this.updateStroke();
      }

   }

   void setSupportBackgroundTintList(ColorStateList var1) {
      if (this.backgroundTint != var1) {
         this.backgroundTint = var1;
         if (this.getMaterialShapeDrawable() != null) {
            DrawableCompat.setTintList(this.getMaterialShapeDrawable(), this.backgroundTint);
         }
      }

   }

   void setSupportBackgroundTintMode(Mode var1) {
      if (this.backgroundTintMode != var1) {
         this.backgroundTintMode = var1;
         if (this.getMaterialShapeDrawable() != null && this.backgroundTintMode != null) {
            DrawableCompat.setTintMode(this.getMaterialShapeDrawable(), this.backgroundTintMode);
         }
      }

   }

   void updateMaskBounds(int var1, int var2) {
      Drawable var3 = this.maskDrawable;
      if (var3 != null) {
         var3.setBounds(this.insetLeft, this.insetTop, var2 - this.insetRight, var1 - this.insetBottom);
      }

   }
}
