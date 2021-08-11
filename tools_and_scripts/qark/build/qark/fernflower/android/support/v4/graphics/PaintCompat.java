package android.support.v4.graphics;

import android.graphics.Paint;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

public final class PaintCompat {
   private PaintCompat() {
   }

   public static boolean hasGlyph(@NonNull Paint var0, @NonNull String var1) {
      return VERSION.SDK_INT >= 23 ? var0.hasGlyph(var1) : PaintCompatApi14.hasGlyph(var0, var1);
   }
}
