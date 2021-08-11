package org.apache.commons.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.Validate;

public final class RandomStringGenerator {
   private final List characterList;
   private final Set inclusivePredicates;
   private final int maximumCodePoint;
   private final int minimumCodePoint;
   private final TextRandomProvider random;

   private RandomStringGenerator(int var1, int var2, Set var3, TextRandomProvider var4, List var5) {
      this.minimumCodePoint = var1;
      this.maximumCodePoint = var2;
      this.inclusivePredicates = var3;
      this.random = var4;
      this.characterList = var5;
   }

   // $FF: synthetic method
   RandomStringGenerator(int var1, int var2, Set var3, TextRandomProvider var4, List var5, Object var6) {
      this(var1, var2, var3, var4, var5);
   }

   private int generateRandomNumber(int var1, int var2) {
      TextRandomProvider var3 = this.random;
      return var3 != null ? var3.nextInt(var2 - var1 + 1) + var1 : ThreadLocalRandom.current().nextInt(var1, var2 + 1);
   }

   private int generateRandomNumber(List var1) {
      int var2 = var1.size();
      TextRandomProvider var3 = this.random;
      return var3 != null ? String.valueOf(var1.get(var3.nextInt(var2))).codePointAt(0) : String.valueOf(var1.get(ThreadLocalRandom.current().nextInt(0, var2))).codePointAt(0);
   }

   public String generate(int var1) {
      if (var1 == 0) {
         return "";
      } else {
         boolean var4;
         if (var1 > 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         Validate.isTrue(var4, "Length %d is smaller than zero.", (long)var1);
         StringBuilder var7 = new StringBuilder(var1);
         long var5 = (long)var1;

         do {
            List var8 = this.characterList;
            if (var8 != null && !var8.isEmpty()) {
               var1 = this.generateRandomNumber(this.characterList);
            } else {
               var1 = this.generateRandomNumber(this.minimumCodePoint, this.maximumCodePoint);
            }

            int var2 = Character.getType(var1);
            if (var2 != 0 && var2 != 18 && var2 != 19) {
               Set var10 = this.inclusivePredicates;
               if (var10 != null) {
                  boolean var3 = false;
                  Iterator var11 = var10.iterator();

                  boolean var9;
                  while(true) {
                     var9 = var3;
                     if (!var11.hasNext()) {
                        break;
                     }

                     if (((CharacterPredicate)var11.next()).test(var1)) {
                        var9 = true;
                        break;
                     }
                  }

                  if (!var9) {
                     continue;
                  }
               }

               var7.appendCodePoint(var1);
               --var5;
            }
         } while(var5 != 0L);

         return var7.toString();
      }
   }

   public String generate(int var1, int var2) {
      boolean var3;
      if (var1 >= 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Minimum length %d is smaller than zero.", (long)var1);
      if (var1 <= var2) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Maximum length %d is smaller than minimum length %d.", var2, var1);
      return this.generate(this.generateRandomNumber(var1, var2));
   }

   public static class Builder implements org.apache.commons.text.Builder {
      public static final int DEFAULT_LENGTH = 0;
      public static final int DEFAULT_MAXIMUM_CODE_POINT = 1114111;
      public static final int DEFAULT_MINIMUM_CODE_POINT = 0;
      private List characterList;
      private Set inclusivePredicates;
      private int maximumCodePoint = 1114111;
      private int minimumCodePoint = 0;
      private TextRandomProvider random;

      public RandomStringGenerator build() {
         return new RandomStringGenerator(this.minimumCodePoint, this.maximumCodePoint, this.inclusivePredicates, this.random, this.characterList);
      }

      public RandomStringGenerator.Builder filteredBy(CharacterPredicate... var1) {
         if (var1 != null && var1.length != 0) {
            Set var2 = this.inclusivePredicates;
            if (var2 == null) {
               this.inclusivePredicates = new HashSet();
            } else {
               var2.clear();
            }

            Collections.addAll(this.inclusivePredicates, var1);
            return this;
         } else {
            this.inclusivePredicates = null;
            return this;
         }
      }

      public RandomStringGenerator.Builder selectFrom(char... var1) {
         this.characterList = new ArrayList();
         int var4 = var1.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            char var2 = var1[var3];
            this.characterList.add(var2);
         }

         return this;
      }

      public RandomStringGenerator.Builder usingRandom(TextRandomProvider var1) {
         this.random = var1;
         return this;
      }

      public RandomStringGenerator.Builder withinRange(int var1, int var2) {
         boolean var4 = true;
         boolean var3;
         if (var1 <= var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         Validate.isTrue(var3, "Minimum code point %d is larger than maximum code point %d", var1, var2);
         if (var1 >= 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         Validate.isTrue(var3, "Minimum code point %d is negative", (long)var1);
         if (var2 <= 1114111) {
            var3 = var4;
         } else {
            var3 = false;
         }

         Validate.isTrue(var3, "Value %d is larger than Character.MAX_CODE_POINT.", (long)var2);
         this.minimumCodePoint = var1;
         this.maximumCodePoint = var2;
         return this;
      }

      public RandomStringGenerator.Builder withinRange(char[]... var1) {
         this.characterList = new ArrayList();
         int var4 = var1.length;

         for(int var2 = 0; var2 < var4; ++var2) {
            char[] var7 = var1[var2];
            boolean var6;
            if (var7.length == 2) {
               var6 = true;
            } else {
               var6 = false;
            }

            Validate.isTrue(var6, "Each pair must contain minimum and maximum code point");
            int var3 = var7[0];
            char var5 = var7[1];
            if (var3 <= var5) {
               var6 = true;
            } else {
               var6 = false;
            }

            Validate.isTrue(var6, "Minimum code point %d is larger than maximum code point %d", var3, Integer.valueOf(var5));

            while(var3 <= var5) {
               this.characterList.add((char)var3);
               ++var3;
            }
         }

         return this;
      }
   }
}
