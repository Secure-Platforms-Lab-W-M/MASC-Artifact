package android.support.v4.text;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import java.util.Locale;

@TargetApi(17)
@RequiresApi(17)
class TextUtilsCompatJellybeanMr1 {
   public static int getLayoutDirectionFromLocale(@Nullable Locale var0) {
      return TextUtils.getLayoutDirectionFromLocale(var0);
   }

   @NonNull
   public static String htmlEncode(@NonNull String var0) {
      return TextUtils.htmlEncode(var0);
   }
}
