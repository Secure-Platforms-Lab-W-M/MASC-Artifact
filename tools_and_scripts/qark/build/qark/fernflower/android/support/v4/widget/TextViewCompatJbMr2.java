package android.support.v4.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

@TargetApi(18)
@RequiresApi(18)
class TextViewCompatJbMr2 {
   public static Drawable[] getCompoundDrawablesRelative(@NonNull TextView var0) {
      return var0.getCompoundDrawablesRelative();
   }

   public static void setCompoundDrawablesRelative(@NonNull TextView var0, @Nullable Drawable var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4) {
      var0.setCompoundDrawablesRelative(var1, var2, var3, var4);
   }

   public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var0, @DrawableRes int var1, @DrawableRes int var2, @DrawableRes int var3, @DrawableRes int var4) {
      var0.setCompoundDrawablesRelativeWithIntrinsicBounds(var1, var2, var3, var4);
   }

   public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var0, @Nullable Drawable var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4) {
      var0.setCompoundDrawablesRelativeWithIntrinsicBounds(var1, var2, var3, var4);
   }
}
