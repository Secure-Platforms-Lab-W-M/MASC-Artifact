package android.support.v4.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.Nullable;

public interface TintableBackgroundView {
   @Nullable
   ColorStateList getSupportBackgroundTintList();

   @Nullable
   Mode getSupportBackgroundTintMode();

   void setSupportBackgroundTintList(@Nullable ColorStateList var1);

   void setSupportBackgroundTintMode(@Nullable Mode var1);
}
