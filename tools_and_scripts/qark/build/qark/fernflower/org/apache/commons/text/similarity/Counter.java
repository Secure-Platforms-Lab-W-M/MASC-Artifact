package org.apache.commons.text.similarity;

import java.util.HashMap;
import java.util.Map;

final class Counter {
   private Counter() {
   }

   // $FF: renamed from: of (java.lang.CharSequence[]) java.util.Map
   public static Map method_4(CharSequence[] var0) {
      HashMap var3 = new HashMap();
      int var2 = var0.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         CharSequence var4 = var0[var1];
         if (var3.containsKey(var4)) {
            var3.put(var4, (Integer)var3.get(var4) + 1);
         } else {
            var3.put(var4, 1);
         }
      }

      return var3;
   }
}
