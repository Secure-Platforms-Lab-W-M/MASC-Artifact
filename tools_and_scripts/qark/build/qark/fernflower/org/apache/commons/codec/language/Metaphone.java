package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Metaphone implements StringEncoder {
   private static final String FRONTV = "EIY";
   private static final String VARSON = "CSPTG";
   private static final String VOWELS = "AEIOU";
   private int maxCodeLen = 4;

   private boolean isLastChar(int var1, int var2) {
      return var2 + 1 == var1;
   }

   private boolean isNextChar(StringBuilder var1, int var2, char var3) {
      boolean var6 = false;
      boolean var5 = var6;
      if (var2 >= 0) {
         int var4 = var1.length();
         boolean var7 = true;
         var5 = var6;
         if (var2 < var4 - 1) {
            if (var1.charAt(var2 + 1) == var3) {
               var5 = var7;
            } else {
               var5 = false;
            }
         }
      }

      return var5;
   }

   private boolean isPreviousChar(StringBuilder var1, int var2, char var3) {
      boolean var5 = false;
      boolean var4 = var5;
      if (var2 > 0) {
         var4 = var5;
         if (var2 < var1.length()) {
            if (var1.charAt(var2 - 1) == var3) {
               var4 = true;
            } else {
               var4 = false;
            }
         }
      }

      return var4;
   }

   private boolean isVowel(StringBuilder var1, int var2) {
      return "AEIOU".indexOf(var1.charAt(var2)) >= 0;
   }

   private boolean regionMatch(StringBuilder var1, int var2, String var3) {
      boolean var5 = false;
      boolean var4 = var5;
      if (var2 >= 0) {
         var4 = var5;
         if (var3.length() + var2 - 1 < var1.length()) {
            var4 = var1.substring(var2, var3.length() + var2).equals(var3);
         }
      }

      return var4;
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof String) {
         return this.metaphone((String)var1);
      } else {
         throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
      }
   }

   public String encode(String var1) {
      return this.metaphone(var1);
   }

   public int getMaxCodeLen() {
      return this.maxCodeLen;
   }

   public boolean isMetaphoneEqual(String var1, String var2) {
      return this.metaphone(var1).equals(this.metaphone(var2));
   }

   public String metaphone(String var1) {
      if (var1 != null) {
         int var3 = var1.length();
         if (var3 != 0) {
            if (var3 == 1) {
               return var1.toUpperCase(Locale.ENGLISH);
            }

            char[] var8 = var1.toUpperCase(Locale.ENGLISH).toCharArray();
            StringBuilder var6 = new StringBuilder(40);
            StringBuilder var7 = new StringBuilder(10);
            char var9 = var8[0];
            if (var9 != 'A') {
               if (var9 != 'G' && var9 != 'K' && var9 != 'P') {
                  if (var9 != 'W') {
                     if (var9 != 'X') {
                        var6.append(var8);
                     } else {
                        var8[0] = 'S';
                        var6.append(var8);
                     }
                  } else if (var8[1] == 'R') {
                     var6.append(var8, 1, var8.length - 1);
                  } else if (var8[1] == 'H') {
                     var6.append(var8, 1, var8.length - 1);
                     var6.setCharAt(0, 'W');
                  } else {
                     var6.append(var8);
                  }
               } else if (var8[1] == 'N') {
                  var6.append(var8, 1, var8.length - 1);
               } else {
                  var6.append(var8);
               }
            } else if (var8[1] == 'E') {
               var6.append(var8, 1, var8.length - 1);
            } else {
               var6.append(var8);
            }

            int var5 = var6.length();
            var3 = 0;

            while(var7.length() < this.getMaxCodeLen() && var3 < var5) {
               char var2 = var6.charAt(var3);
               if (var2 != 'C' && this.isPreviousChar(var6, var3, var2)) {
                  ++var3;
               } else {
                  int var4;
                  switch(var2) {
                  case 'A':
                  case 'E':
                  case 'I':
                  case 'O':
                  case 'U':
                     var4 = var3;
                     if (var3 == 0) {
                        var7.append(var2);
                        var4 = var3;
                     }
                     break;
                  case 'B':
                     if (this.isPreviousChar(var6, var3, 'M') && this.isLastChar(var5, var3)) {
                        var4 = var3;
                        break;
                     }

                     var7.append(var2);
                     var4 = var3;
                     break;
                  case 'C':
                     if (this.isPreviousChar(var6, var3, 'S') && !this.isLastChar(var5, var3) && "EIY".indexOf(var6.charAt(var3 + 1)) >= 0) {
                        var4 = var3;
                     } else if (this.regionMatch(var6, var3, "CIA")) {
                        var7.append('X');
                        var4 = var3;
                     } else if (!this.isLastChar(var5, var3) && "EIY".indexOf(var6.charAt(var3 + 1)) >= 0) {
                        var7.append('S');
                        var4 = var3;
                     } else if (this.isPreviousChar(var6, var3, 'S') && this.isNextChar(var6, var3, 'H')) {
                        var7.append('K');
                        var4 = var3;
                     } else {
                        if (this.isNextChar(var6, var3, 'H')) {
                           if (var3 == 0 && var5 >= 3 && this.isVowel(var6, 2)) {
                              var7.append('K');
                              var4 = var3;
                              break;
                           }

                           var7.append('X');
                           var4 = var3;
                           break;
                        }

                        var7.append('K');
                        var4 = var3;
                     }
                     break;
                  case 'D':
                     if (!this.isLastChar(var5, var3 + 1) && this.isNextChar(var6, var3, 'G') && "EIY".indexOf(var6.charAt(var3 + 2)) >= 0) {
                        var7.append('J');
                        var4 = var3 + 2;
                        break;
                     }

                     var7.append('T');
                     var4 = var3;
                     break;
                  case 'F':
                  case 'J':
                  case 'L':
                  case 'M':
                  case 'N':
                  case 'R':
                     var7.append(var2);
                     var4 = var3;
                     break;
                  case 'G':
                     if (this.isLastChar(var5, var3 + 1) && this.isNextChar(var6, var3, 'H')) {
                        var4 = var3;
                     } else if (!this.isLastChar(var5, var3 + 1) && this.isNextChar(var6, var3, 'H') && !this.isVowel(var6, var3 + 2)) {
                        var4 = var3;
                     } else {
                        if (var3 > 0) {
                           if (this.regionMatch(var6, var3, "GN")) {
                              var4 = var3;
                              break;
                           }

                           if (this.regionMatch(var6, var3, "GNED")) {
                              var4 = var3;
                              break;
                           }
                        }

                        boolean var10;
                        if (this.isPreviousChar(var6, var3, 'G')) {
                           var10 = true;
                        } else {
                           var10 = false;
                        }

                        if (!this.isLastChar(var5, var3) && "EIY".indexOf(var6.charAt(var3 + 1)) >= 0 && !var10) {
                           var7.append('J');
                        } else {
                           var7.append('K');
                        }

                        var4 = var3;
                     }
                     break;
                  case 'H':
                     if (this.isLastChar(var5, var3)) {
                        var4 = var3;
                     } else if (var3 > 0 && "CSPTG".indexOf(var6.charAt(var3 - 1)) >= 0) {
                        var4 = var3;
                     } else if (this.isVowel(var6, var3 + 1)) {
                        var7.append('H');
                        var4 = var3;
                     } else {
                        var4 = var3;
                     }
                     break;
                  case 'K':
                     if (var3 > 0) {
                        if (!this.isPreviousChar(var6, var3, 'C')) {
                           var7.append(var2);
                           var4 = var3;
                        } else {
                           var4 = var3;
                        }
                     } else {
                        var7.append(var2);
                        var4 = var3;
                     }
                     break;
                  case 'P':
                     if (this.isNextChar(var6, var3, 'H')) {
                        var7.append('F');
                        var4 = var3;
                     } else {
                        var7.append(var2);
                        var4 = var3;
                     }
                     break;
                  case 'Q':
                     var7.append('K');
                     var4 = var3;
                     break;
                  case 'S':
                     if (!this.regionMatch(var6, var3, "SH") && !this.regionMatch(var6, var3, "SIO") && !this.regionMatch(var6, var3, "SIA")) {
                        var7.append('S');
                        var4 = var3;
                        break;
                     }

                     var7.append('X');
                     var4 = var3;
                     break;
                  case 'T':
                     if (!this.regionMatch(var6, var3, "TIA") && !this.regionMatch(var6, var3, "TIO")) {
                        if (this.regionMatch(var6, var3, "TCH")) {
                           var4 = var3;
                        } else if (this.regionMatch(var6, var3, "TH")) {
                           var7.append('0');
                           var4 = var3;
                        } else {
                           var7.append('T');
                           var4 = var3;
                        }
                        break;
                     }

                     var7.append('X');
                     var4 = var3;
                     break;
                  case 'V':
                     var7.append('F');
                     var4 = var3;
                     break;
                  case 'W':
                  case 'Y':
                     if (!this.isLastChar(var5, var3)) {
                        if (this.isVowel(var6, var3 + 1)) {
                           var7.append(var2);
                           var4 = var3;
                        } else {
                           var4 = var3;
                        }
                     } else {
                        var4 = var3;
                     }
                     break;
                  case 'X':
                     var7.append('K');
                     var7.append('S');
                     var4 = var3;
                     break;
                  case 'Z':
                     var7.append('S');
                     var4 = var3;
                     break;
                  default:
                     var4 = var3;
                  }

                  var3 = var4 + 1;
               }

               if (var7.length() > this.getMaxCodeLen()) {
                  var7.setLength(this.getMaxCodeLen());
               }
            }

            return var7.toString();
         }
      }

      return "";
   }

   public void setMaxCodeLen(int var1) {
      this.maxCodeLen = var1;
   }
}
