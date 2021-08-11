package org.apache.commons.lang3;

import java.util.Random;

public class RandomStringUtils {
   private static final Random RANDOM = new Random();

   public static String random(int var0) {
      return random(var0, false, false);
   }

   public static String random(int var0, int var1, int var2, boolean var3, boolean var4) {
      return random(var0, var1, var2, var3, var4, (char[])null, RANDOM);
   }

   public static String random(int var0, int var1, int var2, boolean var3, boolean var4, char... var5) {
      return random(var0, var1, var2, var3, var4, var5, RANDOM);
   }

   public static String random(int var0, int var1, int var2, boolean var3, boolean var4, char[] var5, Random var6) {
      if (var0 == 0) {
         return "";
      } else {
         StringBuilder var10;
         if (var0 >= 0) {
            if (var5 != null && var5.length == 0) {
               throw new IllegalArgumentException("The chars array must not be empty");
            } else {
               if (var1 == 0 && var2 == 0) {
                  if (var5 != null) {
                     var2 = var5.length;
                  } else if (!var3 && !var4) {
                     var2 = 1114111;
                  } else {
                     var2 = 123;
                     var1 = 32;
                  }
               } else if (var2 <= var1) {
                  var10 = new StringBuilder();
                  var10.append("Parameter end (");
                  var10.append(var2);
                  var10.append(") must be greater than start (");
                  var10.append(var1);
                  var10.append(")");
                  throw new IllegalArgumentException(var10.toString());
               }

               if (var5 == null && (var4 && var2 <= 48 || var3 && var2 <= 65)) {
                  var10 = new StringBuilder();
                  var10.append("Parameter end (");
                  var10.append(var2);
                  var10.append(") must be greater then (");
                  var10.append(48);
                  var10.append(") for generating digits or greater then (");
                  var10.append(65);
                  var10.append(") for generating letters.");
                  throw new IllegalArgumentException(var10.toString());
               } else {
                  StringBuilder var9 = new StringBuilder(var0);
                  int var7 = var2 - var1;

                  while(true) {
                     while(true) {
                        var2 = var0 - 1;
                        if (var0 == 0) {
                           return var9.toString();
                        }

                        int var8;
                        if (var5 == null) {
                           var0 = var6.nextInt(var7) + var1;
                           var8 = Character.getType(var0);
                           if (var8 == 0 || var8 == 18 || var8 == 19) {
                              var0 = var2 + 1;
                              continue;
                           }
                        } else {
                           var0 = var5[var6.nextInt(var7) + var1];
                        }

                        var8 = Character.charCount(var0);
                        if (var2 == 0 && var8 > 1) {
                           var0 = var2 + 1;
                        } else if ((!var3 || !Character.isLetter(var0)) && (!var4 || !Character.isDigit(var0)) && (var3 || var4)) {
                           var0 = var2 + 1;
                        } else {
                           var9.appendCodePoint(var0);
                           if (var8 == 2) {
                              var0 = var2 - 1;
                           } else {
                              var0 = var2;
                           }
                        }
                     }
                  }
               }
            }
         } else {
            var10 = new StringBuilder();
            var10.append("Requested random string length ");
            var10.append(var0);
            var10.append(" is less than 0.");
            throw new IllegalArgumentException(var10.toString());
         }
      }
   }

   public static String random(int var0, String var1) {
      return var1 == null ? random(var0, 0, 0, false, false, (char[])null, RANDOM) : random(var0, var1.toCharArray());
   }

   public static String random(int var0, boolean var1, boolean var2) {
      return random(var0, 0, 0, var1, var2);
   }

   public static String random(int var0, char... var1) {
      return var1 == null ? random(var0, 0, 0, false, false, (char[])null, RANDOM) : random(var0, 0, var1.length, false, false, var1, RANDOM);
   }

   public static String randomAlphabetic(int var0) {
      return random(var0, true, false);
   }

   public static String randomAlphabetic(int var0, int var1) {
      return randomAlphabetic(RandomUtils.nextInt(var0, var1));
   }

   public static String randomAlphanumeric(int var0) {
      return random(var0, true, true);
   }

   public static String randomAlphanumeric(int var0, int var1) {
      return randomAlphanumeric(RandomUtils.nextInt(var0, var1));
   }

   public static String randomAscii(int var0) {
      return random(var0, 32, 127, false, false);
   }

   public static String randomAscii(int var0, int var1) {
      return randomAscii(RandomUtils.nextInt(var0, var1));
   }

   public static String randomGraph(int var0) {
      return random(var0, 33, 126, false, false);
   }

   public static String randomGraph(int var0, int var1) {
      return randomGraph(RandomUtils.nextInt(var0, var1));
   }

   public static String randomNumeric(int var0) {
      return random(var0, false, true);
   }

   public static String randomNumeric(int var0, int var1) {
      return randomNumeric(RandomUtils.nextInt(var0, var1));
   }

   public static String randomPrint(int var0) {
      return random(var0, 32, 126, false, false);
   }

   public static String randomPrint(int var0, int var1) {
      return randomPrint(RandomUtils.nextInt(var0, var1));
   }
}
