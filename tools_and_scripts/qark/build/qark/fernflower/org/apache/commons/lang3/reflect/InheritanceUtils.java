package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.BooleanUtils;

public class InheritanceUtils {
   public static int distance(Class var0, Class var1) {
      int var2 = -1;
      if (var0 != null) {
         if (var1 == null) {
            return -1;
         } else if (var0.equals(var1)) {
            return 0;
         } else {
            var0 = var0.getSuperclass();
            int var3 = BooleanUtils.toInteger(var1.equals(var0));
            if (var3 == 1) {
               return var3;
            } else {
               var3 += distance(var0, var1);
               if (var3 > 0) {
                  var2 = var3 + 1;
               }

               return var2;
            }
         }
      } else {
         return -1;
      }
   }
}
