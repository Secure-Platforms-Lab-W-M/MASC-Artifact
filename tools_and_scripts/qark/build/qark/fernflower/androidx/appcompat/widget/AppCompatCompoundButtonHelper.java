package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;

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

   void loadFromAttributes(AttributeSet param1, int param2) {
      // $FF: Couldn't be decompiled
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

   void setSupportButtonTintMode(Mode var1) {
      this.mButtonTintMode = var1;
      this.mHasButtonTintMode = true;
      this.applyButtonTint();
   }

   interface DirectSetButtonDrawableInterface {
      void setButtonDrawable(Drawable var1);
   }
}
