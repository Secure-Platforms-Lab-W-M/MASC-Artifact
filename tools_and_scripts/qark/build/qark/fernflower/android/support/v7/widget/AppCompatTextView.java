package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.widget.AutoSizeableTextView;
import android.util.AttributeSet;
import android.widget.TextView;

public class AppCompatTextView extends TextView implements TintableBackgroundView, AutoSizeableTextView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper;
   private final AppCompatTextHelper mTextHelper;

   public AppCompatTextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatTextView(Context var1, AttributeSet var2) {
      this(var1, var2, 16842884);
   }

   public AppCompatTextView(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
      this.mBackgroundTintHelper.loadFromAttributes(var2, var3);
      this.mTextHelper = AppCompatTextHelper.create(this);
      this.mTextHelper.loadFromAttributes(var2, var3);
      this.mTextHelper.applyCompoundDrawablesTints();
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      if (var1 != null) {
         var1.applySupportBackgroundTint();
      }

      AppCompatTextHelper var2 = this.mTextHelper;
      if (var2 != null) {
         var2.applyCompoundDrawablesTints();
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getAutoSizeMaxTextSize() {
      if (VERSION.SDK_INT >= 26) {
         return super.getAutoSizeMaxTextSize();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeMaxTextSize() : -1;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getAutoSizeMinTextSize() {
      if (VERSION.SDK_INT >= 26) {
         return super.getAutoSizeMinTextSize();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeMinTextSize() : -1;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getAutoSizeStepGranularity() {
      if (VERSION.SDK_INT >= 26) {
         return super.getAutoSizeStepGranularity();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeStepGranularity() : -1;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int[] getAutoSizeTextAvailableSizes() {
      if (VERSION.SDK_INT >= 26) {
         return super.getAutoSizeTextAvailableSizes();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeTextAvailableSizes() : new int[0];
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getAutoSizeTextType() {
      int var2 = VERSION.SDK_INT;
      byte var1 = 0;
      if (var2 >= 26) {
         if (super.getAutoSizeTextType() == 1) {
            var1 = 1;
         }

         return var1;
      } else {
         AppCompatTextHelper var3 = this.mTextHelper;
         return var3 != null ? var3.getAutoSizeTextType() : 0;
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

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      AppCompatTextHelper var6 = this.mTextHelper;
      if (var6 != null) {
         var6.onLayout(var1, var2, var3, var4, var5);
      }

   }

   protected void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      super.onTextChanged(var1, var2, var3, var4);
      if (this.mTextHelper != null && VERSION.SDK_INT < 26 && this.mTextHelper.isAutoSizeEnabled()) {
         this.mTextHelper.autoSizeText();
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      if (VERSION.SDK_INT >= 26) {
         super.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
      } else {
         AppCompatTextHelper var5 = this.mTextHelper;
         if (var5 != null) {
            var5.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
         }

      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] var1, int var2) throws IllegalArgumentException {
      if (VERSION.SDK_INT >= 26) {
         super.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
      } else {
         AppCompatTextHelper var3 = this.mTextHelper;
         if (var3 != null) {
            var3.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
         }

      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setAutoSizeTextTypeWithDefaults(int var1) {
      if (VERSION.SDK_INT >= 26) {
         super.setAutoSizeTextTypeWithDefaults(var1);
      } else {
         AppCompatTextHelper var2 = this.mTextHelper;
         if (var2 != null) {
            var2.setAutoSizeTextTypeWithDefaults(var1);
         }

      }
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

   public void setTextAppearance(Context var1, int var2) {
      super.setTextAppearance(var1, var2);
      AppCompatTextHelper var3 = this.mTextHelper;
      if (var3 != null) {
         var3.onSetTextAppearance(var1, var2);
      }

   }

   public void setTextSize(int var1, float var2) {
      if (VERSION.SDK_INT >= 26) {
         super.setTextSize(var1, var2);
      } else {
         AppCompatTextHelper var3 = this.mTextHelper;
         if (var3 != null) {
            var3.setTextSize(var1, var2);
         }

      }
   }
}
