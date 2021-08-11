package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v7.appcompat.R$attr;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class AppCompatImageButton extends ImageButton implements TintableBackgroundView, TintableImageSourceView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper;
   private final AppCompatImageHelper mImageHelper;

   public AppCompatImageButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatImageButton(Context var1, AttributeSet var2) {
      this(var1, var2, R$attr.imageButtonStyle);
   }

   public AppCompatImageButton(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
      this.mBackgroundTintHelper.loadFromAttributes(var2, var3);
      this.mImageHelper = new AppCompatImageHelper(this);
      this.mImageHelper.loadFromAttributes(var2, var3);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      if (var1 != null) {
         var1.applySupportBackgroundTint();
      }

      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public ColorStateList getSupportBackgroundTintList() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      return var1 != null ? var1.getSupportBackgroundTintList() : null;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public Mode getSupportBackgroundTintMode() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      return var1 != null ? var1.getSupportBackgroundTintMode() : null;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public ColorStateList getSupportImageTintList() {
      AppCompatImageHelper var1 = this.mImageHelper;
      return var1 != null ? var1.getSupportImageTintList() : null;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public Mode getSupportImageTintMode() {
      AppCompatImageHelper var1 = this.mImageHelper;
      return var1 != null ? var1.getSupportImageTintMode() : null;
   }

   public boolean hasOverlappingRendering() {
      return this.mImageHelper.hasOverlappingRendering() && super.hasOverlappingRendering();
   }

   public void setBackgroundDrawable(Drawable var1) {
      super.setBackgroundDrawable(var1);
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundDrawable(var1);
      }

   }

   public void setBackgroundResource(@DrawableRes int var1) {
      super.setBackgroundResource(var1);
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundResource(var1);
      }

   }

   public void setImageBitmap(Bitmap var1) {
      super.setImageBitmap(var1);
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   public void setImageDrawable(@Nullable Drawable var1) {
      super.setImageDrawable(var1);
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   public void setImageIcon(@Nullable Icon var1) {
      super.setImageIcon(var1);
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   public void setImageResource(@DrawableRes int var1) {
      this.mImageHelper.setImageResource(var1);
   }

   public void setImageURI(@Nullable Uri var1) {
      super.setImageURI(var1);
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportBackgroundTintList(@Nullable ColorStateList var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintList(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportBackgroundTintMode(@Nullable Mode var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintMode(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportImageTintList(@Nullable ColorStateList var1) {
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.setSupportImageTintList(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportImageTintMode(@Nullable Mode var1) {
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.setSupportImageTintMode(var1);
      }

   }
}
