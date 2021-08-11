package org.apache.commons.text;

public enum CharacterPredicates implements CharacterPredicate {
   ARABIC_NUMERALS {
      public boolean test(int var1) {
         return var1 >= 48 && var1 <= 57;
      }
   },
   ASCII_ALPHA_NUMERALS,
   ASCII_LETTERS {
      public boolean test(int var1) {
         return ASCII_LOWERCASE_LETTERS.test(var1) || ASCII_UPPERCASE_LETTERS.test(var1);
      }
   },
   ASCII_LOWERCASE_LETTERS {
      public boolean test(int var1) {
         return var1 >= 97 && var1 <= 122;
      }
   },
   ASCII_UPPERCASE_LETTERS {
      public boolean test(int var1) {
         return var1 >= 65 && var1 <= 90;
      }
   },
   DIGITS {
      public boolean test(int var1) {
         return Character.isDigit(var1);
      }
   },
   LETTERS {
      public boolean test(int var1) {
         return Character.isLetter(var1);
      }
   };

   static {
      CharacterPredicates var0 = new CharacterPredicates("ASCII_ALPHA_NUMERALS", 6) {
         public boolean test(int var1) {
            return ASCII_LOWERCASE_LETTERS.test(var1) || ASCII_UPPERCASE_LETTERS.test(var1) || ARABIC_NUMERALS.test(var1);
         }
      };
      ASCII_ALPHA_NUMERALS = var0;
   }

   private CharacterPredicates() {
   }

   // $FF: synthetic method
   CharacterPredicates(Object var3) {
      this();
   }
}
