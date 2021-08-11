package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R$styleable;
import android.util.AttributeSet;
import android.view.View;

class AppCompatBackgroundHelper {
   private int mBackgroundResId = -1;
   private TintInfo mBackgroundTint;
   private final AppCompatDrawableManager mDrawableManager;
   private TintInfo mInternalBackgroundTint;
   private TintInfo mTmpInfo;
   private final View mView;

   AppCompatBackgroundHelper(View var1) {
      this.mView = var1;
      this.mDrawableManager = AppCompatDrawableManager.get();
   }

   private boolean applyFrameworkTintUsingColorFilter(@NonNull Drawable var1) {
      if (this.mTmpInfo == null) {
         this.mTmpInfo = new TintInfo();
      }

      TintInfo var2 = this.mTmpInfo;
      var2.clear();
      ColorStateList var3 = ViewCompat.getBackgroundTintList(this.mView);
      if (var3 != null) {
         var2.mHasTintList = true;
         var2.mTintList = var3;
      }

      Mode var4 = ViewCompat.getBackgroundTintMode(this.mView);
      if (var4 != null) {
         var2.mHasTintMode = true;
         var2.mTintMode = var4;
      }

      if (!var2.mHasTintList && !var2.mHasTintMode) {
         return false;
      } else {
         AppCompatDrawableManager.tintDrawable(var1, var2, this.mView.getDrawableState());
         return true;
      }
   }

   private boolean shouldApplyFrameworkTintUsingColorFilter() {
      int var1 = VERSION.SDK_INT;
      if (var1 > 21) {
         return this.mInternalBackgroundTint != null;
      } else {
         return var1 == 21;
      }
   }

   void applySupportBackgroundTint() {
      Drawable var1 = this.mView.getBackground();
      if (var1 != null) {
         if (this.shouldApplyFrameworkTintUsingColorFilter() && this.applyFrameworkTintUsingColorFilter(var1)) {
            return;
         }

         TintInfo var2 = this.mBackgroundTint;
         if (var2 != null) {
            AppCompatDrawableManager.tintDrawable(var1, var2, this.mView.getDrawableState());
            return;
         }

         var2 = this.mInternalBackgroundTint;
         if (var2 != null) {
            AppCompatDrawableManager.tintDrawable(var1, var2, this.mView.getDrawableState());
         }
      }

   }

   ColorStateList getSupportBackgroundTintList() {
      TintInfo var1 = this.mBackgroundTint;
      return var1 != null ? var1.mTintList : null;
   }

   Mode getSupportBackgroundTintMode() {
      TintInfo var1 = this.mBackgroundTint;
      return var1 != null ? var1.mTintMode : null;
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      TintTypedArray var24 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, R$styleable.ViewBackgroundHelper, var2, 0);

      label211: {
         Throwable var10000;
         label215: {
            boolean var10001;
            label209: {
               ColorStateList var3;
               try {
                  if (!var24.hasValue(R$styleable.ViewBackgroundHelper_android_background)) {
                     break label209;
                  }

                  this.mBackgroundResId = var24.getResourceId(R$styleable.ViewBackgroundHelper_android_background, -1);
                  var3 = this.mDrawableManager.getTintList(this.mView.getContext(), this.mBackgroundResId);
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label215;
               }

               if (var3 != null) {
                  try {
                     this.setInternalBackgroundTint(var3);
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label215;
                  }
               }
            }

            try {
               if (var24.hasValue(R$styleable.ViewBackgroundHelper_backgroundTint)) {
                  ViewCompat.setBackgroundTintList(this.mView, var24.getColorStateList(R$styleable.ViewBackgroundHelper_backgroundTint));
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label215;
            }

            label198:
            try {
               if (var24.hasValue(R$styleable.ViewBackgroundHelper_backgroundTintMode)) {
                  ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(var24.getInt(R$styleable.ViewBackgroundHelper_backgroundTintMode, -1), (Mode)null));
               }
               break label211;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label198;
            }
         }

         Throwable var25 = var10000;
         var24.recycle();
         throw var25;
      }

      var24.recycle();
   }

   void onSetBackgroundDrawable(Drawable var1) {
      this.mBackgroundResId = -1;
      this.setInternalBackgroundTint((ColorStateList)null);
      this.applySupportBackgroundTint();
   }

   void onSetBackgroundResource(int var1) {
      this.mBackgroundResId = var1;
      AppCompatDrawableManager var2 = this.mDrawableManager;
      ColorStateList var3;
      if (var2 != null) {
         var3 = var2.getTintList(this.mView.getContext(), var1);
      } else {
         var3 = null;
      }

      this.setInternalBackgroundTint(var3);
      this.applySupportBackgroundTint();
   }

   void setInternalBackgroundTint(ColorStateList var1) {
      if (var1 != null) {
         if (this.mInternalBackgroundTint == null) {
            this.mInternalBackgroundTint = new TintInfo();
         }

         TintInfo var2 = this.mInternalBackgroundTint;
         var2.mTintList = var1;
         var2.mHasTintList = true;
      } else {
         this.mInternalBackgroundTint = null;
      }

      this.applySupportBackgroundTint();
   }

   void setSupportBackgroundTintList(ColorStateList var1) {
      if (this.mBackgroundTint == null) {
         this.mBackgroundTint = new TintInfo();
      }

      TintInfo var2 = this.mBackgroundTint;
      var2.mTintList = var1;
      var2.mHasTintList = true;
      this.applySupportBackgroundTint();
   }

   void setSupportBackgroundTintMode(Mode var1) {
      if (this.mBackgroundTint == null) {
         this.mBackgroundTint = new TintInfo();
      }

      TintInfo var2 = this.mBackgroundTint;
      var2.mTintMode = var1;
      var2.mHasTintMode = true;
      this.applySupportBackgroundTint();
   }
}
