package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;

@Deprecated
public class LookupTranslator extends CharSequenceTranslator {
   private final int longest;
   private final HashMap lookupMap = new HashMap();
   private final HashSet prefixSet = new HashSet();
   private final int shortest;

   public LookupTranslator(CharSequence[]... var1) {
      int var3 = Integer.MAX_VALUE;
      int var6 = 0;
      int var2 = 0;
      int var5 = var3;
      if (var1 != null) {
         int var8 = var1.length;
         int var4 = 0;

         while(true) {
            var5 = var3;
            var6 = var2;
            if (var4 >= var8) {
               break;
            }

            CharSequence[] var9 = var1[var4];
            this.lookupMap.put(var9[0].toString(), var9[1].toString());
            this.prefixSet.add(var9[0].charAt(0));
            var6 = var9[0].length();
            var5 = var3;
            if (var6 < var3) {
               var5 = var6;
            }

            int var7 = var2;
            if (var6 > var2) {
               var7 = var6;
            }

            ++var4;
            var3 = var5;
            var2 = var7;
         }
      }

      this.shortest = var5;
      this.longest = var6;
   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      if (this.prefixSet.contains(var1.charAt(var2))) {
         int var4 = this.longest;
         if (this.longest + var2 > var1.length()) {
            var4 = var1.length() - var2;
         }

         while(var4 >= this.shortest) {
            CharSequence var5 = var1.subSequence(var2, var2 + var4);
            String var6 = (String)this.lookupMap.get(var5.toString());
            if (var6 != null) {
               var3.write(var6);
               return var4;
            }

            --var4;
         }
      }

      return 0;
   }
}
