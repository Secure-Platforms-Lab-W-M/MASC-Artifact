package androidx.databinding.adapters;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;

public class Converters {
   public static ColorStateList convertColorToColorStateList(int var0) {
      return ColorStateList.valueOf(var0);
   }

   public static ColorDrawable convertColorToDrawable(int var0) {
      return new ColorDrawable(var0);
   }
}
