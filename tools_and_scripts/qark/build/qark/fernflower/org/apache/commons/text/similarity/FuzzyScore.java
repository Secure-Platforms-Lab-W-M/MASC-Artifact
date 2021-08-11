package org.apache.commons.text.similarity;

import java.util.Locale;

public class FuzzyScore {
   private final Locale locale;

   public FuzzyScore(Locale var1) {
      if (var1 != null) {
         this.locale = var1;
      } else {
         throw new IllegalArgumentException("Locale must not be null");
      }
   }

   public Integer fuzzyScore(CharSequence var1, CharSequence var2) {
      if (var1 != null && var2 != null) {
         String var11 = var1.toString().toLowerCase(this.locale);
         String var12 = var2.toString().toLowerCase(this.locale);
         int var4 = 0;
         int var3 = 0;
         int var6 = Integer.MIN_VALUE;

         for(int var5 = 0; var5 < var12.length(); ++var5) {
            char var10 = var12.charAt(var5);

            int var8;
            for(boolean var7 = false; var3 < var11.length() && !var7; var6 = var8) {
               int var9 = var4;
               var8 = var6;
               if (var10 == var11.charAt(var3)) {
                  int var13 = var4 + 1;
                  var4 = var13;
                  if (var6 + 1 == var3) {
                     var4 = var13 + 2;
                  }

                  var8 = var3;
                  var7 = true;
                  var9 = var4;
               }

               ++var3;
               var4 = var9;
            }
         }

         return var4;
      } else {
         throw new IllegalArgumentException("CharSequences must not be null");
      }
   }

   public Locale getLocale() {
      return this.locale;
   }
}
