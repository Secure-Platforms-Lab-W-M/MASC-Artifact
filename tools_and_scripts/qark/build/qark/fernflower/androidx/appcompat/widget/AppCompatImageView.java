package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.core.view.TintableBackgroundView;
import androidx.core.widget.TintableImageSourceView;

public class AppCompatImageView extends ImageView implements TintableBackgroundView, TintableImageSourceView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper;
   private final AppCompatImageHelper mImageHelper;

   public AppCompatImageView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatImageView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public AppCompatImageView(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      AppCompatBackgroundHelper var4 = new AppCompatBackgroundHelper(this);
      this.mBackgroundTintHelper = var4;
      var4.loadFromAttributes(var2, var3);
      AppCompatImageHelper var5 = new AppCompatImageHelper(this);
      this.mImageHelper = var5;
      var5.loadFromAttributes(var2, var3);
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

   public ColorStateList getSupportBackgroundTintList() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      return var1 != null ? var1.getSupportBackgroundTintList() : null;
   }

   public Mode getSupportBackgroundTintMode() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      return var1 != null ? var1.getSupportBackgroundTintMode() : null;
   }

   public ColorStateList getSupportImageTintList() {
      AppCompatImageHelper var1 = this.mImageHelper;
      return var1 != null ? var1.getSupportImageTintList() : null;
   }

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

   public void setBackgroundResource(int var1) {
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

   public void setImageDrawable(Drawable var1) {
      super.setImageDrawable(var1);
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   public void setImageResource(int var1) {
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.setImageResource(var1);
      }

   }

   public void setImageURI(Uri var1) {
      super.setImageURI(var1);
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   public void setSupportBackgroundTintList(ColorStateList var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintList(var1);
      }

   }

   public void setSupportBackgroundTintMode(Mode var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintMode(var1);
      }

   }

   public void setSupportImageTintList(ColorStateList var1) {
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.setSupportImageTintList(var1);
      }

   }

   public void setSupportImageTintMode(Mode var1) {
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.setSupportImageTintMode(var1);
      }

   }
}
