package org.apache.commons.text;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class CaseUtils {
   private static Set generateDelimiterSet(char[] var0) {
      HashSet var2 = new HashSet();
      var2.add(Character.codePointAt(new char[]{' '}, 0));
      if (var0 == null) {
         return var2;
      } else if (var0.length == 0) {
         return var2;
      } else {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2.add(Character.codePointAt(var0, var1));
         }

         return var2;
      }
   }

   public static String toCamelCase(String var0, boolean var1, char... var2) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else {
         var0 = var0.toLowerCase();
         int var7 = var0.length();
         int[] var8 = new int[var7];
         int var4 = 0;
         Set var9 = generateDelimiterSet(var2);
         boolean var3 = false;
         if (var1) {
            var3 = true;
         }

         int var6 = 0;

         int var10;
         for(boolean var5 = var3; var6 < var7; var6 = var10) {
            var10 = var0.codePointAt(var6);
            if (var9.contains(var10)) {
               var5 = true;
               if (var4 == 0) {
                  var5 = false;
               }

               var10 = var6 + Character.charCount(var10);
            } else if (!var5 && (var4 != 0 || !var1)) {
               var8[var4] = var10;
               var10 = var6 + Character.charCount(var10);
               ++var4;
            } else {
               var10 = Character.toTitleCase(var10);
               var8[var4] = var10;
               var10 = var6 + Character.charCount(var10);
               var5 = false;
               ++var4;
            }
         }

         if (var4 != 0) {
            return new String(var8, 0, var4);
         } else {
            return var0;
         }
      }
   }
}
