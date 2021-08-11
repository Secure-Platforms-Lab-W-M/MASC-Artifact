package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class MatchRatingApproachEncoder implements StringEncoder {
   private static final String[] DOUBLE_CONSONANT = new String[]{"BB", "CC", "DD", "FF", "GG", "HH", "JJ", "KK", "LL", "MM", "NN", "PP", "QQ", "RR", "SS", "TT", "VV", "WW", "XX", "YY", "ZZ"};
   private static final int ELEVEN = 11;
   private static final String EMPTY = "";
   private static final int FIVE = 5;
   private static final int FOUR = 4;
   private static final int ONE = 1;
   private static final String PLAIN_ASCII = "AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu";
   private static final int SEVEN = 7;
   private static final int SIX = 6;
   private static final String SPACE = " ";
   private static final int THREE = 3;
   private static final int TWELVE = 12;
   private static final int TWO = 2;
   private static final String UNICODE = "ÀàÈèÌìÒòÙùÁáÉéÍíÓóÚúÝýÂâÊêÎîÔôÛûŶŷÃãÕõÑñÄäËëÏïÖöÜüŸÿÅåÇçŐőŰű";

   String cleanName(String var1) {
      var1 = var1.toUpperCase(Locale.ENGLISH);
      String[] var4 = new String[]{"\\-", "[&]", "\\'", "\\.", "[\\,]"};
      int var3 = var4.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         var1 = var1.replaceAll(var4[var2], "");
      }

      return this.removeAccents(var1).replaceAll("\\s+", "");
   }

   public final Object encode(Object var1) throws EncoderException {
      if (var1 instanceof String) {
         return this.encode((String)var1);
      } else {
         throw new EncoderException("Parameter supplied to Match Rating Approach encoder is not of type java.lang.String");
      }
   }

   public final String encode(String var1) {
      if (var1 != null && !"".equalsIgnoreCase(var1) && !" ".equalsIgnoreCase(var1)) {
         return var1.length() == 1 ? "" : this.getFirst3Last3(this.removeDoubleConsonants(this.removeVowels(this.cleanName(var1))));
      } else {
         return "";
      }
   }

   String getFirst3Last3(String var1) {
      int var2 = var1.length();
      if (var2 > 6) {
         String var3 = var1.substring(0, 3);
         var1 = var1.substring(var2 - 3, var2);
         StringBuilder var4 = new StringBuilder();
         var4.append(var3);
         var4.append(var1);
         return var4.toString();
      } else {
         return var1;
      }
   }

   int getMinRating(int var1) {
      if (var1 <= 4) {
         return 5;
      } else if (var1 <= 7) {
         return 4;
      } else if (var1 <= 11) {
         return 3;
      } else {
         return var1 == 12 ? 2 : 1;
      }
   }

   public boolean isEncodeEquals(String var1, String var2) {
      boolean var4 = false;
      if (var1 != null && !"".equalsIgnoreCase(var1)) {
         if (" ".equalsIgnoreCase(var1)) {
            return false;
         } else if (var2 != null && !"".equalsIgnoreCase(var2)) {
            if (" ".equalsIgnoreCase(var2)) {
               return false;
            } else if (var1.length() != 1) {
               if (var2.length() == 1) {
                  return false;
               } else if (var1.equalsIgnoreCase(var2)) {
                  return true;
               } else {
                  var1 = this.cleanName(var1);
                  var2 = this.cleanName(var2);
                  var1 = this.removeVowels(var1);
                  var2 = this.removeVowels(var2);
                  var1 = this.removeDoubleConsonants(var1);
                  var2 = this.removeDoubleConsonants(var2);
                  var1 = this.getFirst3Last3(var1);
                  var2 = this.getFirst3Last3(var2);
                  if (Math.abs(var1.length() - var2.length()) >= 3) {
                     return false;
                  } else {
                     int var3 = this.getMinRating(Math.abs(var1.length() + var2.length()));
                     if (this.leftToRightThenRightToLeftProcessing(var1, var2) >= var3) {
                        var4 = true;
                     }

                     return var4;
                  }
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   boolean isVowel(String var1) {
      return var1.equalsIgnoreCase("E") || var1.equalsIgnoreCase("A") || var1.equalsIgnoreCase("O") || var1.equalsIgnoreCase("I") || var1.equalsIgnoreCase("U");
   }

   int leftToRightThenRightToLeftProcessing(String var1, String var2) {
      char[] var7 = var1.toCharArray();
      char[] var6 = var2.toCharArray();
      int var4 = var1.length() - 1;
      int var5 = var2.length() - 1;

      for(int var3 = 0; var3 < var7.length && var3 <= var5; ++var3) {
         String var8 = var1.substring(var3, var3 + 1);
         String var9 = var1.substring(var4 - var3, var4 - var3 + 1);
         String var10 = var2.substring(var3, var3 + 1);
         String var11 = var2.substring(var5 - var3, var5 - var3 + 1);
         if (var8.equals(var10)) {
            var7[var3] = ' ';
            var6[var3] = ' ';
         }

         if (var9.equals(var11)) {
            var7[var4 - var3] = ' ';
            var6[var5 - var3] = ' ';
         }
      }

      var1 = (new String(var7)).replaceAll("\\s+", "");
      var2 = (new String(var6)).replaceAll("\\s+", "");
      return var1.length() > var2.length() ? Math.abs(6 - var1.length()) : Math.abs(6 - var2.length());
   }

   String removeAccents(String var1) {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var6 = new StringBuilder();
         int var4 = var1.length();

         for(int var3 = 0; var3 < var4; ++var3) {
            char var2 = var1.charAt(var3);
            int var5 = "ÀàÈèÌìÒòÙùÁáÉéÍíÓóÚúÝýÂâÊêÎîÔôÛûŶŷÃãÕõÑñÄäËëÏïÖöÜüŸÿÅåÇçŐőŰű".indexOf(var2);
            if (var5 > -1) {
               var6.append("AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu".charAt(var5));
            } else {
               var6.append(var2);
            }
         }

         return var6.toString();
      }
   }

   String removeDoubleConsonants(String var1) {
      var1 = var1.toUpperCase(Locale.ENGLISH);
      String[] var5 = DOUBLE_CONSONANT;
      int var3 = var5.length;

      String var4;
      for(int var2 = 0; var2 < var3; var1 = var4) {
         String var6 = var5[var2];
         var4 = var1;
         if (var1.contains(var6)) {
            var4 = var1.replace(var6, var6.substring(0, 1));
         }

         ++var2;
      }

      return var1;
   }

   String removeVowels(String var1) {
      String var2 = var1.substring(0, 1);
      var1 = var1.replaceAll("A", "").replaceAll("E", "").replaceAll("I", "").replaceAll("O", "").replaceAll("U", "").replaceAll("\\s{2,}\\b", " ");
      if (this.isVowel(var2)) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var2);
         var3.append(var1);
         return var3.toString();
      } else {
         return var1;
      }
   }
}
