package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaper extends CharSequenceTranslator {
   private final EnumSet options;

   public NumericEntityUnescaper(NumericEntityUnescaper.OPTION... var1) {
      if (var1.length > 0) {
         this.options = EnumSet.copyOf(Arrays.asList(var1));
      } else {
         this.options = EnumSet.copyOf(Arrays.asList(NumericEntityUnescaper.OPTION.semiColonRequired));
      }
   }

   public boolean isSet(NumericEntityUnescaper.OPTION var1) {
      EnumSet var2 = this.options;
      return var2 != null && var2.contains(var1);
   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      int var6 = var1.length();
      char var4 = var1.charAt(var2);
      byte var8 = 0;
      if (var4 == '&' && var2 < var6 - 2 && var1.charAt(var2 + 1) == '#') {
         int var5;
         byte var12;
         label91: {
            var5 = var2 + 2;
            var12 = 0;
            char var7 = var1.charAt(var5);
            if (var7 != 'x') {
               var2 = var5;
               if (var7 != 'X') {
                  break label91;
               }
            }

            ++var5;
            var12 = 1;
            var2 = var5;
            if (var5 == var6) {
               return 0;
            }
         }

         for(var5 = var2; var5 < var6 && (var1.charAt(var5) >= '0' && var1.charAt(var5) <= '9' || var1.charAt(var5) >= 'a' && var1.charAt(var5) <= 'f' || var1.charAt(var5) >= 'A' && var1.charAt(var5) <= 'F'); ++var5) {
         }

         boolean var13;
         if (var5 != var6 && var1.charAt(var5) == ';') {
            var13 = true;
         } else {
            var13 = false;
         }

         if (!var13) {
            if (this.isSet(NumericEntityUnescaper.OPTION.semiColonRequired)) {
               return 0;
            }

            if (this.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon)) {
               throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
            }
         }

         int var14;
         label62: {
            boolean var10001;
            if (var12 != 0) {
               try {
                  var14 = Integer.parseInt(var1.subSequence(var2, var5).toString(), 16);
                  break label62;
               } catch (NumberFormatException var9) {
                  var10001 = false;
               }
            } else {
               try {
                  var14 = Integer.parseInt(var1.subSequence(var2, var5).toString(), 10);
                  break label62;
               } catch (NumberFormatException var10) {
                  var10001 = false;
               }
            }

            return 0;
         }

         if (var14 > 65535) {
            char[] var11 = Character.toChars(var14);
            var3.write(var11[0]);
            var3.write(var11[1]);
         } else {
            var3.write(var14);
         }

         byte var15 = var8;
         if (var13) {
            var15 = 1;
         }

         return var5 + 2 - var2 + var12 + var15;
      } else {
         return 0;
      }
   }

   public static enum OPTION {
      errorIfNoSemiColon,
      semiColonOptional,
      semiColonRequired;

      static {
         NumericEntityUnescaper.OPTION var0 = new NumericEntityUnescaper.OPTION("errorIfNoSemiColon", 2);
         errorIfNoSemiColon = var0;
      }
   }
}
