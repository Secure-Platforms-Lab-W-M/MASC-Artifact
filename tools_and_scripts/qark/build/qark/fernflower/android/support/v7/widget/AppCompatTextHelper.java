package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R$styleable;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

@RequiresApi(9)
class AppCompatTextHelper {
   @NonNull
   private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
   private TintInfo mDrawableBottomTint;
   private TintInfo mDrawableLeftTint;
   private TintInfo mDrawableRightTint;
   private TintInfo mDrawableTopTint;
   private Typeface mFontTypeface;
   private int mStyle = 0;
   final TextView mView;

   AppCompatTextHelper(TextView var1) {
      this.mView = var1;
      this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
   }

   static AppCompatTextHelper create(TextView var0) {
      return (AppCompatTextHelper)(VERSION.SDK_INT >= 17 ? new AppCompatTextHelperV17(var0) : new AppCompatTextHelper(var0));
   }

   protected static TintInfo createTintInfo(Context var0, AppCompatDrawableManager var1, int var2) {
      ColorStateList var3 = var1.getTintList(var0, var2);
      if (var3 != null) {
         TintInfo var4 = new TintInfo();
         var4.mHasTintList = true;
         var4.mTintList = var3;
         return var4;
      } else {
         return null;
      }
   }

   private void setTextSizeInternal(int var1, float var2) {
      this.mAutoSizeTextHelper.setTextSizeInternal(var1, var2);
   }

   private void updateTypefaceAndStyle(Context var1, TintTypedArray var2) {
      this.mStyle = var2.getInt(R$styleable.TextAppearance_android_textStyle, this.mStyle);
      if (var2.hasValue(R$styleable.TextAppearance_android_fontFamily) || var2.hasValue(R$styleable.TextAppearance_fontFamily)) {
         this.mFontTypeface = null;
         int var3;
         if (var2.hasValue(R$styleable.TextAppearance_android_fontFamily)) {
            var3 = R$styleable.TextAppearance_android_fontFamily;
         } else {
            var3 = R$styleable.TextAppearance_fontFamily;
         }

         if (!var1.isRestricted()) {
            try {
               this.mFontTypeface = var2.getFont(var3, this.mStyle, this.mView);
            } catch (UnsupportedOperationException var4) {
            } catch (NotFoundException var5) {
            }
         }

         if (this.mFontTypeface == null) {
            this.mFontTypeface = Typeface.create(var2.getString(var3), this.mStyle);
         }
      }

   }

   final void applyCompoundDrawableTint(Drawable var1, TintInfo var2) {
      if (var1 != null && var2 != null) {
         AppCompatDrawableManager.tintDrawable(var1, var2, this.mView.getDrawableState());
      }

   }

   void applyCompoundDrawablesTints() {
      if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
         Drawable[] var1 = this.mView.getCompoundDrawables();
         this.applyCompoundDrawableTint(var1[0], this.mDrawableLeftTint);
         this.applyCompoundDrawableTint(var1[1], this.mDrawableTopTint);
         this.applyCompoundDrawableTint(var1[2], this.mDrawableRightTint);
         this.applyCompoundDrawableTint(var1[3], this.mDrawableBottomTint);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void autoSizeText() {
      this.mAutoSizeTextHelper.autoSizeText();
   }

   int getAutoSizeMaxTextSize() {
      return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
   }

   int getAutoSizeMinTextSize() {
      return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
   }

   int getAutoSizeStepGranularity() {
      return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
   }

   int[] getAutoSizeTextAvailableSizes() {
      return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
   }

   int getAutoSizeTextType() {
      return this.mAutoSizeTextHelper.getAutoSizeTextType();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   boolean isAutoSizeEnabled() {
      return this.mAutoSizeTextHelper.isAutoSizeEnabled();
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      Context var17 = this.mView.getContext();
      AppCompatDrawableManager var9 = AppCompatDrawableManager.get();
      TintTypedArray var10 = TintTypedArray.obtainStyledAttributes(var17, var1, R$styleable.AppCompatTextHelper, var2, 0);
      int var5 = var10.getResourceId(R$styleable.AppCompatTextHelper_android_textAppearance, -1);
      if (var10.hasValue(R$styleable.AppCompatTextHelper_android_drawableLeft)) {
         this.mDrawableLeftTint = createTintInfo(var17, var9, var10.getResourceId(R$styleable.AppCompatTextHelper_android_drawableLeft, 0));
      }

      if (var10.hasValue(R$styleable.AppCompatTextHelper_android_drawableTop)) {
         this.mDrawableTopTint = createTintInfo(var17, var9, var10.getResourceId(R$styleable.AppCompatTextHelper_android_drawableTop, 0));
      }

      if (var10.hasValue(R$styleable.AppCompatTextHelper_android_drawableRight)) {
         this.mDrawableRightTint = createTintInfo(var17, var9, var10.getResourceId(R$styleable.AppCompatTextHelper_android_drawableRight, 0));
      }

      if (var10.hasValue(R$styleable.AppCompatTextHelper_android_drawableBottom)) {
         this.mDrawableBottomTint = createTintInfo(var17, var9, var10.getResourceId(R$styleable.AppCompatTextHelper_android_drawableBottom, 0));
      }

      var10.recycle();
      boolean var8 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
      boolean var6 = false;
      boolean var7 = false;
      boolean var3 = false;
      boolean var4 = false;
      Object var16 = null;
      ColorStateList var21 = null;
      TintTypedArray var15 = null;
      ColorStateList var12 = null;
      ColorStateList var20 = null;
      ColorStateList var13 = null;
      ColorStateList var11 = null;
      ColorStateList var14 = null;
      if (var5 != -1) {
         TintTypedArray var18 = TintTypedArray.obtainStyledAttributes(var17, var5, R$styleable.TextAppearance);
         var6 = var7;
         var3 = var4;
         if (!var8) {
            var6 = var7;
            var3 = var4;
            if (var18.hasValue(R$styleable.TextAppearance_textAllCaps)) {
               var6 = var18.getBoolean(R$styleable.TextAppearance_textAllCaps, false);
               var3 = true;
            }
         }

         this.updateTypefaceAndStyle(var17, var18);
         var21 = (ColorStateList)var16;
         var11 = var14;
         if (VERSION.SDK_INT < 23) {
            var20 = var15;
            if (var18.hasValue(R$styleable.TextAppearance_android_textColor)) {
               var20 = var18.getColorStateList(R$styleable.TextAppearance_android_textColor);
            }

            if (var18.hasValue(R$styleable.TextAppearance_android_textColorHint)) {
               var13 = var18.getColorStateList(R$styleable.TextAppearance_android_textColorHint);
            }

            var21 = var20;
            var12 = var13;
            var11 = var14;
            if (var18.hasValue(R$styleable.TextAppearance_android_textColorLink)) {
               var11 = var18.getColorStateList(R$styleable.TextAppearance_android_textColorLink);
               var12 = var13;
               var21 = var20;
            }
         }

         var18.recycle();
         var20 = var12;
      }

      var15 = TintTypedArray.obtainStyledAttributes(var17, var1, R$styleable.TextAppearance, var2, 0);
      var7 = var6;
      var4 = var3;
      if (!var8) {
         var7 = var6;
         var4 = var3;
         if (var15.hasValue(R$styleable.TextAppearance_textAllCaps)) {
            var4 = true;
            var7 = var15.getBoolean(R$styleable.TextAppearance_textAllCaps, false);
         }
      }

      var12 = var21;
      var13 = var20;
      var14 = var11;
      if (VERSION.SDK_INT < 23) {
         if (var15.hasValue(R$styleable.TextAppearance_android_textColor)) {
            var21 = var15.getColorStateList(R$styleable.TextAppearance_android_textColor);
         }

         if (var15.hasValue(R$styleable.TextAppearance_android_textColorHint)) {
            var20 = var15.getColorStateList(R$styleable.TextAppearance_android_textColorHint);
         }

         var12 = var21;
         var13 = var20;
         var14 = var11;
         if (var15.hasValue(R$styleable.TextAppearance_android_textColorLink)) {
            var14 = var15.getColorStateList(R$styleable.TextAppearance_android_textColorLink);
            var13 = var20;
            var12 = var21;
         }
      }

      this.updateTypefaceAndStyle(var17, var15);
      var15.recycle();
      if (var12 != null) {
         this.mView.setTextColor(var12);
      }

      if (var13 != null) {
         this.mView.setHintTextColor(var13);
      }

      if (var14 != null) {
         this.mView.setLinkTextColor(var14);
      }

      if (!var8 && var4) {
         this.setAllCaps(var7);
      }

      Typeface var22 = this.mFontTypeface;
      if (var22 != null) {
         this.mView.setTypeface(var22, this.mStyle);
      }

      this.mAutoSizeTextHelper.loadFromAttributes(var1, var2);
      if (VERSION.SDK_INT >= 26) {
         if (this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
            int[] var19 = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
            if (var19.length > 0) {
               if ((float)this.mView.getAutoSizeStepGranularity() != -1.0F) {
                  this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
               } else {
                  this.mView.setAutoSizeTextTypeUniformWithPresetSizes(var19, 0);
               }
            }
         }
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (VERSION.SDK_INT < 26) {
         this.autoSizeText();
      }

   }

   void onSetTextAppearance(Context var1, int var2) {
      TintTypedArray var3 = TintTypedArray.obtainStyledAttributes(var1, var2, R$styleable.TextAppearance);
      if (var3.hasValue(R$styleable.TextAppearance_textAllCaps)) {
         this.setAllCaps(var3.getBoolean(R$styleable.TextAppearance_textAllCaps, false));
      }

      if (VERSION.SDK_INT < 23 && var3.hasValue(R$styleable.TextAppearance_android_textColor)) {
         ColorStateList var4 = var3.getColorStateList(R$styleable.TextAppearance_android_textColor);
         if (var4 != null) {
            this.mView.setTextColor(var4);
         }
      }

      this.updateTypefaceAndStyle(var1, var3);
      var3.recycle();
      Typeface var5 = this.mFontTypeface;
      if (var5 != null) {
         this.mView.setTypeface(var5, this.mStyle);
      }

   }

   void setAllCaps(boolean var1) {
      this.mView.setAllCaps(var1);
   }

   void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
   }

   void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] var1, int var2) throws IllegalArgumentException {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
   }

   void setAutoSizeTextTypeWithDefaults(int var1) {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setTextSize(int var1, float var2) {
      if (VERSION.SDK_INT < 26 && !this.isAutoSizeEnabled()) {
         this.setTextSizeInternal(var1, var2);
      }

   }
}
