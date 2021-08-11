package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public interface TintableImageSourceView {
   @Nullable
   ColorStateList getSupportImageTintList();

   @Nullable
   Mode getSupportImageTintMode();

   void setSupportImageTintList(@Nullable ColorStateList var1);

   void setSupportImageTintMode(@Nullable Mode var1);
}
