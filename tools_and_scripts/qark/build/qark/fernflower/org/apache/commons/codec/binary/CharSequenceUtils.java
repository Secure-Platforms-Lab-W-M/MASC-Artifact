package org.apache.commons.codec.binary;

public class CharSequenceUtils {
   static boolean regionMatches(CharSequence var0, boolean var1, int var2, CharSequence var3, int var4, int var5) {
      if (var0 instanceof String && var3 instanceof String) {
         return ((String)var0).regionMatches(var1, var2, (String)var3, var4, var5);
      } else {
         while(var5 > 0) {
            char var6 = var0.charAt(var2);
            char var7 = var3.charAt(var4);
            if (var6 != var7) {
               if (!var1) {
                  return false;
               }

               if (Character.toUpperCase(var6) != Character.toUpperCase(var7) && Character.toLowerCase(var6) != Character.toLowerCase(var7)) {
                  return false;
               }
            }

            ++var2;
            --var5;
            ++var4;
         }

         return true;
      }
   }
}
