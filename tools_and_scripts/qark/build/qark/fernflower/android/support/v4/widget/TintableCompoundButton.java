package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.Nullable;

public interface TintableCompoundButton {
   @Nullable
   ColorStateList getSupportButtonTintList();

   @Nullable
   Mode getSupportButtonTintMode();

   void setSupportButtonTintList(@Nullable ColorStateList var1);

   void setSupportButtonTintMode(@Nullable Mode var1);
}
