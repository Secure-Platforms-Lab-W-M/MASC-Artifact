package android.support.v4.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;

@RequiresApi(9)
class PaintCompatGingerbread {
   private static final String TOFU_STRING = "\udb3f\udffd";
   private static final ThreadLocal sRectThreadLocal = new ThreadLocal();

   static boolean hasGlyph(@NonNull Paint var0, @NonNull String var1) {
      boolean var9 = false;
      int var6 = var1.length();
      boolean var8;
      if (var6 == 1 && Character.isWhitespace(var1.charAt(0))) {
         var8 = true;
      } else {
         float var3 = var0.measureText("\udb3f\udffd");
         float var4 = var0.measureText(var1);
         var8 = var9;
         if (var4 != 0.0F) {
            if (var1.codePointCount(0, var1.length()) > 1) {
               var8 = var9;
               if (var4 > 2.0F * var3) {
                  return var8;
               }

               float var2 = 0.0F;

               int var7;
               for(int var5 = 0; var5 < var6; var5 += var7) {
                  var7 = Character.charCount(var1.codePointAt(var5));
                  var2 += var0.measureText(var1, var5, var5 + var7);
               }

               var8 = var9;
               if (var4 >= var2) {
                  return var8;
               }
            }

            if (var4 != var3) {
               return true;
            }

            Pair var10 = obtainEmptyRects();
            var0.getTextBounds("\udb3f\udffd", 0, "\udb3f\udffd".length(), (Rect)var10.first);
            var0.getTextBounds(var1, 0, var6, (Rect)var10.second);
            if (!((Rect)var10.first).equals(var10.second)) {
               var8 = true;
            } else {
               var8 = false;
            }

            return var8;
         }
      }

      return var8;
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
