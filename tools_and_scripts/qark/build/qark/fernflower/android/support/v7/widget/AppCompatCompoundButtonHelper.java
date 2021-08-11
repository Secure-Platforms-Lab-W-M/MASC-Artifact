package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.CompoundButton;

class AppCompatCompoundButtonHelper {
   private ColorStateList mButtonTintList = null;
   private Mode mButtonTintMode = null;
   private boolean mHasButtonTint = false;
   private boolean mHasButtonTintMode = false;
   private boolean mSkipNextApply;
   private final CompoundButton mView;

   AppCompatCompoundButtonHelper(CompoundButton var1) {
      this.mView = var1;
   }

   void applyButtonTint() {
      Drawable var1 = CompoundButtonCompat.getButtonDrawable(this.mView);
      if (var1 != null && (this.mHasButtonTint || this.mHasButtonTintMode)) {
         var1 = DrawableCompat.wrap(var1).mutate();
         if (this.mHasButtonTint) {
            DrawableCompat.setTintList(var1, this.mButtonTintList);
         }

         if (this.mHasButtonTintMode) {
            DrawableCompat.setTintMode(var1, this.mButtonTintMode);
         }

         if (var1.isStateful()) {
            var1.setState(this.mView.getDrawableState());
         }

         this.mView.setButtonDrawable(var1);
      }

   }

   int getCompoundPaddingLeft(int var1) {
      int var2 = var1;
      if (VERSION.SDK_INT < 17) {
         Drawable var3 = CompoundButtonCompat.getButtonDrawable(this.mView);
         var2 = var1;
         if (var3 != null) {
            var2 = var1 + var3.getIntrinsicWidth();
         }
      }

      return var2;
   }

   ColorStateList getSupportButtonTintList() {
      return this.mButtonTintList;
   }

   Mode getSupportButtonTintMode() {
      return this.mButtonTintMode;
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      TypedArray var24 = this.mView.getContext().obtainStyledAttributes(var1, R$styleable.CompoundButton, var2, 0);

      label211: {
         Throwable var10000;
         label215: {
            boolean var10001;
            label209: {
               try {
                  if (!var24.hasValue(R$styleable.CompoundButton_android_button)) {
                     break label209;
                  }

                  var2 = var24.getResourceId(R$styleable.CompoundButton_android_button, 0);
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label215;
               }

               if (var2 != 0) {
                  try {
                     this.mView.setButtonDrawable(AppCompatResources.getDrawable(this.mView.getContext(), var2));
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label215;
                  }
               }
            }

            try {
               if (var24.hasValue(R$styleable.CompoundButton_buttonTint)) {
                  CompoundButtonCompat.setButtonTintList(this.mView, var24.getColorStateList(R$styleable.CompoundButton_buttonTint));
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label215;
            }

            label198:
            try {
               if (var24.hasValue(R$styleable.CompoundButton_buttonTintMode)) {
                  CompoundButtonCompat.setButtonTintMode(this.mView, DrawableUtils.parseTintMode(var24.getInt(R$styleable.CompoundButton_buttonTintMode, -1), (Mode)null));
               }
               break label211;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label198;
            }
         }

         Throwable var3 = var10000;
         var24.recycle();
         throw var3;
      }

      var24.recycle();
   }

   void onSetButtonDrawable() {
      if (this.mSkipNextApply) {
         this.mSkipNextApply = false;
      } else {
         this.mSkipNextApply = true;
         this.applyButtonTint();
      }
   }

   void setSupportButtonTintList(ColorStateList var1) {
      this.mButtonTintList = var1;
      this.mHasButtonTint = true;
      this.applyButtonTint();
   }

   void setSupportButtonTintMode(@Nullable Mode var1) {
      this.mButtonTintMode = var1;
      this.mHasButtonTintMode = true;
      this.applyButtonTint();
   }

   interface DirectSetButtonDrawableInterface {
      void setButtonDrawable(Drawable var1);
   }
}
