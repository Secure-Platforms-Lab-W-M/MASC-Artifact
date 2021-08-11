package android.support.v4.util;

import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class DebugUtils {
   public static void buildShortClassTag(Object var0, StringBuilder var1) {
      if (var0 == null) {
         var1.append("null");
      } else {
         String var3;
         label16: {
            String var4 = var0.getClass().getSimpleName();
            if (var4 != null) {
               var3 = var4;
               if (var4.length() > 0) {
                  break label16;
               }
            }

            var4 = var0.getClass().getName();
            int var2 = var4.lastIndexOf(46);
            var3 = var4;
            if (var2 > 0) {
               var3 = var4.substring(var2 + 1);
            }
         }

         var1.append(var3);
         var1.append('{');
         var1.append(Integer.toHexString(System.identityHashCode(var0)));
      }
   }
}
