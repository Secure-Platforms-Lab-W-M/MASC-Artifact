package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;

final class WrappedDrawableState extends ConstantState {
   int mChangingConfigurations;
   ConstantState mDrawableState;
   ColorStateList mTint = null;
   Mode mTintMode;

   WrappedDrawableState(WrappedDrawableState var1) {
      this.mTintMode = WrappedDrawableApi14.DEFAULT_TINT_MODE;
      if (var1 != null) {
         this.mChangingConfigurations = var1.mChangingConfigurations;
         this.mDrawableState = var1.mDrawableState;
         this.mTint = var1.mTint;
         this.mTintMode = var1.mTintMode;
      }

   }

   boolean canConstantState() {
      return this.mDrawableState != null;
   }

   public int getChangingConfigurations() {
      int var2 = this.mChangingConfigurations;
      ConstantState var3 = this.mDrawableState;
      int var1;
      if (var3 != null) {
         var1 = var3.getChangingConfigurations();
      } else {
         var1 = 0;
      }

      return var2 | var1;
   }

   public Drawable newDrawable() {
      return this.newDrawable((Resources)null);
   }

   public Drawable newDrawable(Resources var1) {
      return (Drawable)(VERSION.SDK_INT >= 21 ? new WrappedDrawableApi21(this, var1) : new WrappedDrawableApi14(this, var1));
   }
}
