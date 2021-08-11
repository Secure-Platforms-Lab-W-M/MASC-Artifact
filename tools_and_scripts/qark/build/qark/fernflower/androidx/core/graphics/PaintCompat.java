package androidx.core.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build.VERSION;
import androidx.core.util.Pair;

public final class PaintCompat {
   private static final String EM_STRING = "m";
   private static final String TOFU_STRING = "\udb3f\udffd";
   private static final ThreadLocal sRectThreadLocal = new ThreadLocal();

   private PaintCompat() {
   }

   public static boolean hasGlyph(Paint var0, String var1) {
      if (VERSION.SDK_INT >= 23) {
         return var0.hasGlyph(var1);
      } else {
         int var6 = var1.length();
         if (var6 == 1 && Character.isWhitespace(var1.charAt(0))) {
            return true;
         } else {
            float var3 = var0.measureText("\udb3f\udffd");
            float var2 = var0.measureText("m");
            float var4 = var0.measureText(var1);
            if (var4 == 0.0F) {
               return false;
            } else {
               if (var1.codePointCount(0, var1.length()) > 1) {
                  if (var4 > 2.0F * var2) {
                     return false;
                  }

                  var2 = 0.0F;

                  int var7;
                  for(int var5 = 0; var5 < var6; var5 += var7) {
                     var7 = Character.charCount(var1.codePointAt(var5));
                     var2 += var0.measureText(var1, var5, var5 + var7);
                  }

                  if (var4 >= var2) {
                     return false;
                  }
               }

               if (var4 != var3) {
                  return true;
               } else {
                  Pair var8 = obtainEmptyRects();
                  var0.getTextBounds("\udb3f\udffd", 0, "\udb3f\udffd".length(), (Rect)var8.first);
                  var0.getTextBounds(var1, 0, var6, (Rect)var8.second);
                  return true ^ ((Rect)var8.first).equals(var8.second);
               }
            }
         }
      }
   }

   private static Pair obtainEmptyRects() {
      Pair var0 = (Pair)sRectThreadLocal.get();
      if (var0 == null) {
         var0 = new Pair(new Rect(), new Rect());
         sRectThreadLocal.set(var0);
         return var0;
      } else {
         ((Rect)var0.first).setEmpty();
         ((Rect)var0.second).setEmpty();
         return var0;
      }
   }
}
