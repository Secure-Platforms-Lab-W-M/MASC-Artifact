package org.apache.commons.lang3.math;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class NumberUtils {
   public static final Byte BYTE_MINUS_ONE = -1;
   public static final Byte BYTE_ONE = 1;
   public static final Byte BYTE_ZERO = 0;
   public static final Double DOUBLE_MINUS_ONE = -1.0D;
   public static final Double DOUBLE_ONE = 1.0D;
   public static final Double DOUBLE_ZERO = 0.0D;
   public static final Float FLOAT_MINUS_ONE = -1.0F;
   public static final Float FLOAT_ONE = 1.0F;
   public static final Float FLOAT_ZERO = 0.0F;
   public static final Integer INTEGER_MINUS_ONE = -1;
   public static final Integer INTEGER_ONE = 1;
   public static final Integer INTEGER_TWO = 2;
   public static final Integer INTEGER_ZERO = 0;
   public static final Long LONG_MINUS_ONE = -1L;
   public static final Long LONG_ONE = 1L;
   public static final Long LONG_ZERO = 0L;
   public static final Short SHORT_MINUS_ONE = Short.valueOf((short)-1);
   public static final Short SHORT_ONE = Short.valueOf((short)1);
   public static final Short SHORT_ZERO = Short.valueOf((short)0);

   public static int compare(byte var0, byte var1) {
      return var0 - var1;
   }

   public static int compare(int var0, int var1) {
      if (var0 == var1) {
         return 0;
      } else {
         return var0 < var1 ? -1 : 1;
      }
   }

   public static int compare(long var0, long var2) {
      if (var0 == var2) {
         return 0;
      } else {
         return var0 < var2 ? -1 : 1;
      }
   }

   public static int compare(short var0, short var1) {
      if (var0 == var1) {
         return 0;
      } else {
         return var0 < var1 ? -1 : 1;
      }
   }

   public static BigDecimal createBigDecimal(String var0) {
      if (var0 == null) {
         return null;
      } else if (!StringUtils.isBlank(var0)) {
         if (!var0.trim().startsWith("--")) {
            return new BigDecimal(var0);
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append(var0);
            var1.append(" is not a valid number.");
            throw new NumberFormatException(var1.toString());
         }
      } else {
         throw new NumberFormatException("A blank string is not a valid number");
      }
   }

   public static BigInteger createBigInteger(String var0) {
      if (var0 == null) {
         return null;
      } else {
         byte var3 = 0;
         byte var5 = 10;
         boolean var4 = false;
         if (var0.startsWith("-")) {
            var4 = true;
            var3 = 1;
         }

         int var1;
         byte var2;
         if (!var0.startsWith("0x", var3) && !var0.startsWith("0X", var3)) {
            if (var0.startsWith("#", var3)) {
               var2 = 16;
               var1 = var3 + 1;
            } else {
               var1 = var3;
               var2 = var5;
               if (var0.startsWith("0", var3)) {
                  var1 = var3;
                  var2 = var5;
                  if (var0.length() > var3 + 1) {
                     var2 = 8;
                     var1 = var3 + 1;
                  }
               }
            }
         } else {
            var2 = 16;
            var1 = var3 + 2;
         }

         BigInteger var6 = new BigInteger(var0.substring(var1), var2);
         return var4 ? var6.negate() : var6;
      }
   }

   public static Double createDouble(String var0) {
      return var0 == null ? null : Double.valueOf(var0);
   }

   public static Float createFloat(String var0) {
      return var0 == null ? null : Float.valueOf(var0);
   }

   public static Integer createInteger(String var0) {
      return var0 == null ? null : Integer.decode(var0);
   }

   public static Long createLong(String var0) {
      return var0 == null ? null : Long.decode(var0);
   }

   public static Number createNumber(String var0) {
      if (var0 == null) {
         return null;
      } else if (StringUtils.isBlank(var0)) {
         throw new NumberFormatException("A blank string is not a valid number");
      } else {
         String[] var7 = new String[]{"0x", "0X", "-0x", "-0X", "#", "-#"};
         int var5 = var7.length;
         boolean var4 = false;
         int var3 = 0;

         String var8;
         while(true) {
            if (var3 >= var5) {
               var3 = 0;
               break;
            }

            var8 = var7[var3];
            if (var0.startsWith(var8)) {
               var3 = 0 + var8.length();
               break;
            }

            ++var3;
         }

         if (var3 > 0) {
            byte var6 = 0;
            int var22 = var3;
            var5 = var3;
            char var27 = (char)var6;

            char var24;
            while(true) {
               var24 = var27;
               if (var22 >= var0.length()) {
                  break;
               }

               var27 = var0.charAt(var22);
               var24 = var27;
               if (var27 != '0') {
                  break;
               }

               ++var5;
               ++var22;
            }

            var3 = var0.length() - var5;
            if (var3 <= 16 && (var3 != 16 || var24 <= '7')) {
               return (Number)(var3 > 8 || var3 == 8 && var24 > '7' ? createLong(var0) : createInteger(var0));
            } else {
               return createBigInteger(var0);
            }
         } else {
            char var1 = var0.charAt(var0.length() - 1);
            var3 = var0.indexOf(46);
            var5 = var0.indexOf(101) + var0.indexOf(69) + 1;
            String var9;
            String var25;
            StringBuilder var26;
            if (var3 <= -1) {
               if (var5 > -1) {
                  if (var5 > var0.length()) {
                     var26 = new StringBuilder();
                     var26.append(var0);
                     var26.append(" is not a valid number.");
                     throw new NumberFormatException(var26.toString());
                  }

                  var25 = getMantissa(var0, var5);
               } else {
                  var25 = getMantissa(var0);
               }

               var9 = null;
               var8 = var25;
               var25 = var9;
            } else {
               if (var5 > -1) {
                  if (var5 < var3 || var5 > var0.length()) {
                     var26 = new StringBuilder();
                     var26.append(var0);
                     var26.append(" is not a valid number.");
                     throw new NumberFormatException(var26.toString());
                  }

                  var25 = var0.substring(var3 + 1, var5);
               } else {
                  var25 = var0.substring(var3 + 1);
               }

               var8 = getMantissa(var0, var3);
            }

            boolean var23;
            Double var30;
            if (!Character.isDigit(var1) && var1 != '.') {
               if (var5 > -1 && var5 < var0.length() - 1) {
                  var9 = var0.substring(var5 + 1, var0.length() - 1);
               } else {
                  var9 = null;
               }

               String var10 = var0.substring(0, var0.length() - 1);
               if (isAllZeros(var8) && isAllZeros(var9)) {
                  var23 = true;
               } else {
                  var23 = false;
               }

               label327: {
                  float var2;
                  if (var1 != 'D') {
                     label328: {
                        if (var1 != 'F') {
                           label329: {
                              if (var1 != 'L') {
                                 if (var1 == 'd') {
                                    break label328;
                                 }

                                 if (var1 == 'f') {
                                    break label329;
                                 }

                                 if (var1 != 'l') {
                                    break label327;
                                 }
                              }

                              if (var25 == null && var9 == null && (!var10.isEmpty() && var10.charAt(0) == '-' && isDigits(var10.substring(1)) || isDigits(var10))) {
                                 try {
                                    Long var21 = createLong(var10);
                                    return var21;
                                 } catch (NumberFormatException var12) {
                                    return createBigInteger(var10);
                                 }
                              }

                              var26 = new StringBuilder();
                              var26.append(var0);
                              var26.append(" is not a valid number.");
                              throw new NumberFormatException(var26.toString());
                           }
                        }

                        Float var33;
                        try {
                           var33 = createFloat(var0);
                           if (var33.isInfinite()) {
                              break label328;
                           }

                           var2 = var33;
                        } catch (NumberFormatException var16) {
                           break label328;
                        }

                        if (var2 != 0.0F || var23) {
                           return var33;
                        }
                     }
                  }

                  label312: {
                     try {
                        var30 = createDouble(var0);
                        if (var30.isInfinite()) {
                           break label312;
                        }

                        var2 = var30.floatValue();
                     } catch (NumberFormatException var15) {
                        break label312;
                     }

                     if ((double)var2 != 0.0D || var23) {
                        return var30;
                     }
                  }

                  try {
                     BigDecimal var34 = createBigDecimal(var10);
                     return var34;
                  } catch (NumberFormatException var14) {
                  }
               }

               var26 = new StringBuilder();
               var26.append(var0);
               var26.append(" is not a valid number.");
               throw new NumberFormatException(var26.toString());
            } else {
               if (var5 > -1 && var5 < var0.length() - 1) {
                  var9 = var0.substring(var5 + 1, var0.length());
               } else {
                  var9 = null;
               }

               if (var25 == null && var9 == null) {
                  try {
                     Integer var32 = createInteger(var0);
                     return var32;
                  } catch (NumberFormatException var13) {
                     try {
                        Long var31 = createLong(var0);
                        return var31;
                     } catch (NumberFormatException var11) {
                        return createBigInteger(var0);
                     }
                  }
               } else {
                  var23 = var4;
                  if (isAllZeros(var8)) {
                     var23 = var4;
                     if (isAllZeros(var9)) {
                        var23 = true;
                     }
                  }

                  boolean var10001;
                  label266: {
                     Float var28;
                     label265: {
                        try {
                           var28 = createFloat(var0);
                           var30 = createDouble(var0);
                           if (var28.isInfinite()) {
                              break label266;
                           }

                           if (var28 != 0.0F) {
                              break label265;
                           }
                        } catch (NumberFormatException var20) {
                           var10001 = false;
                           return createBigDecimal(var0);
                        }

                        if (!var23) {
                           break label266;
                        }
                     }

                     try {
                        if (var28.toString().equals(var30.toString())) {
                           return var28;
                        }
                     } catch (NumberFormatException var19) {
                        var10001 = false;
                        return createBigDecimal(var0);
                     }
                  }

                  label254: {
                     try {
                        if (var30.isInfinite()) {
                           return createBigDecimal(var0);
                        }

                        if (var30 != 0.0D) {
                           break label254;
                        }
                     } catch (NumberFormatException var18) {
                        var10001 = false;
                        return createBigDecimal(var0);
                     }

                     if (!var23) {
                        return createBigDecimal(var0);
                     }
                  }

                  BigDecimal var29;
                  try {
                     var29 = createBigDecimal(var0);
                     var3 = var29.compareTo(BigDecimal.valueOf(var30));
                  } catch (NumberFormatException var17) {
                     var10001 = false;
                     return createBigDecimal(var0);
                  }

                  if (var3 == 0) {
                     return var30;
                  } else {
                     return var29;
                  }
               }
            }
         }
      }
   }

   private static String getMantissa(String var0) {
      return getMantissa(var0, var0.length());
   }

   private static String getMantissa(String var0, int var1) {
      char var2 = var0.charAt(0);
      boolean var3;
      if (var2 != '-' && var2 != '+') {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3 ? var0.substring(1, var1) : var0.substring(0, var1);
   }

   private static boolean isAllZeros(String var0) {
      if (var0 == null) {
         return true;
      } else {
         for(int var1 = var0.length() - 1; var1 >= 0; --var1) {
            if (var0.charAt(var1) != '0') {
               return false;
            }
         }

         return true ^ var0.isEmpty();
      }
   }

   public static boolean isCreatable(String var0) {
      boolean var7 = StringUtils.isEmpty(var0);
      boolean var10 = false;
      boolean var9 = false;
      if (var7) {
         return false;
      } else {
         char[] var11 = var0.toCharArray();
         int var6 = var11.length;
         boolean var4 = false;
         boolean var3 = false;
         boolean var5 = false;
         var7 = false;
         byte var1;
         if (var11[0] != '-' && var11[0] != '+') {
            var1 = 0;
         } else {
            var1 = 1;
         }

         int var2;
         if (var6 > var1 + 1 && var11[var1] == '0' && !StringUtils.contains(var0, 46)) {
            int var13;
            if (var11[var1 + 1] == 'x' || var11[var1 + 1] == 'X') {
               var2 = var1 + 2;
               var13 = var2;
               if (var2 == var6) {
                  return false;
               }

               for(; var13 < var11.length; ++var13) {
                  if ((var11[var13] < '0' || var11[var13] > '9') && (var11[var13] < 'a' || var11[var13] > 'f')) {
                     if (var11[var13] < 'A') {
                        return false;
                     }

                     if (var11[var13] > 'F') {
                        return false;
                     }
                  }
               }

               return true;
            }

            if (Character.isDigit(var11[var1 + 1])) {
               for(var13 = var1 + 1; var13 < var11.length; ++var13) {
                  if (var11[var13] < '0') {
                     return false;
                  }

                  if (var11[var13] > '7') {
                     return false;
                  }
               }

               return true;
            }
         }

         --var6;
         var2 = var1;

         boolean var12;
         for(var12 = var5; var2 < var6 || var2 < var6 + 1 && var12 && !var7; ++var2) {
            if (var11[var2] >= '0' && var11[var2] <= '9') {
               var7 = true;
               var12 = false;
            } else if (var11[var2] == '.') {
               if (var3) {
                  return false;
               }

               if (var4) {
                  return false;
               }

               var3 = true;
            } else if (var11[var2] != 'e' && var11[var2] != 'E') {
               if (var11[var2] != '+' && var11[var2] != '-') {
                  return false;
               }

               if (!var12) {
                  return false;
               }

               var12 = false;
               var7 = false;
            } else {
               if (var4) {
                  return false;
               }

               if (!var7) {
                  return false;
               }

               var4 = true;
               var12 = true;
            }
         }

         boolean var8;
         if (var2 >= var11.length) {
            var8 = var10;
            if (!var12) {
               var8 = var10;
               if (var7) {
                  var8 = true;
               }
            }

            return var8;
         } else if (var11[var2] >= '0' && var11[var2] <= '9') {
            return true;
         } else if (var11[var2] == 'e') {
            return false;
         } else if (var11[var2] == 'E') {
            return false;
         } else if (var11[var2] == '.') {
            if (!var3) {
               return var4 ? false : var7;
            } else {
               return false;
            }
         } else if (var12 || var11[var2] != 'd' && var11[var2] != 'D' && var11[var2] != 'f' && var11[var2] != 'F') {
            if (var11[var2] != 'l' && var11[var2] != 'L') {
               return false;
            } else {
               var8 = var9;
               if (var7) {
                  var8 = var9;
                  if (!var4) {
                     var8 = var9;
                     if (!var3) {
                        var8 = true;
                     }
                  }
               }

               return var8;
            }
         } else {
            return var7;
         }
      }
   }

   public static boolean isDigits(String var0) {
      return StringUtils.isNumeric(var0);
   }

   @Deprecated
   public static boolean isNumber(String var0) {
      return isCreatable(var0);
   }

   public static boolean isParsable(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return false;
      } else if (var0.charAt(var0.length() - 1) == '.') {
         return false;
      } else if (var0.charAt(0) == '-') {
         return var0.length() == 1 ? false : withDecimalsParsing(var0, 1);
      } else {
         return withDecimalsParsing(var0, 0);
      }
   }

   public static byte max(byte var0, byte var1, byte var2) {
      byte var3 = var0;
      if (var1 > var0) {
         var3 = var1;
      }

      var0 = var3;
      if (var2 > var3) {
         var0 = var2;
      }

      return var0;
   }

   public static byte max(byte... var0) {
      validateArray(var0);
      byte var1 = var0[0];

      byte var2;
      for(int var3 = 1; var3 < var0.length; var1 = var2) {
         var2 = var1;
         if (var0[var3] > var1) {
            var2 = var0[var3];
         }

         ++var3;
      }

      return var1;
   }

   public static double max(double var0, double var2, double var4) {
      return Math.max(Math.max(var0, var2), var4);
   }

   public static double max(double... var0) {
      validateArray(var0);
      double var1 = var0[0];

      double var3;
      for(int var5 = 1; var5 < var0.length; var1 = var3) {
         if (Double.isNaN(var0[var5])) {
            return Double.NaN;
         }

         var3 = var1;
         if (var0[var5] > var1) {
            var3 = var0[var5];
         }

         ++var5;
      }

      return var1;
   }

   public static float max(float var0, float var1, float var2) {
      return Math.max(Math.max(var0, var1), var2);
   }

   public static float max(float... var0) {
      validateArray(var0);
      float var1 = var0[0];

      float var2;
      for(int var3 = 1; var3 < var0.length; var1 = var2) {
         if (Float.isNaN(var0[var3])) {
            return Float.NaN;
         }

         var2 = var1;
         if (var0[var3] > var1) {
            var2 = var0[var3];
         }

         ++var3;
      }

      return var1;
   }

   public static int max(int var0, int var1, int var2) {
      int var3 = var0;
      if (var1 > var0) {
         var3 = var1;
      }

      var0 = var3;
      if (var2 > var3) {
         var0 = var2;
      }

      return var0;
   }

   public static int max(int... var0) {
      validateArray(var0);
      int var2 = var0[0];

      int var3;
      for(int var1 = 1; var1 < var0.length; var2 = var3) {
         var3 = var2;
         if (var0[var1] > var2) {
            var3 = var0[var1];
         }

         ++var1;
      }

      return var2;
   }

   public static long max(long var0, long var2, long var4) {
      long var6 = var0;
      if (var2 > var0) {
         var6 = var2;
      }

      var0 = var6;
      if (var4 > var6) {
         var0 = var4;
      }

      return var0;
   }

   public static long max(long... var0) {
      validateArray(var0);
      long var2 = var0[0];

      long var4;
      for(int var1 = 1; var1 < var0.length; var2 = var4) {
         var4 = var2;
         if (var0[var1] > var2) {
            var4 = var0[var1];
         }

         ++var1;
      }

      return var2;
   }

   public static short max(short var0, short var1, short var2) {
      short var3 = var0;
      if (var1 > var0) {
         var3 = var1;
      }

      var0 = var3;
      if (var2 > var3) {
         var0 = var2;
      }

      return var0;
   }

   public static short max(short... var0) {
      validateArray(var0);
      short var1 = var0[0];

      short var2;
      for(int var3 = 1; var3 < var0.length; var1 = var2) {
         var2 = var1;
         if (var0[var3] > var1) {
            var2 = var0[var3];
         }

         ++var3;
      }

      return var1;
   }

   public static byte min(byte var0, byte var1, byte var2) {
      byte var3 = var0;
      if (var1 < var0) {
         var3 = var1;
      }

      var0 = var3;
      if (var2 < var3) {
         var0 = var2;
      }

      return var0;
   }

   public static byte min(byte... var0) {
      validateArray(var0);
      byte var1 = var0[0];

      byte var2;
      for(int var3 = 1; var3 < var0.length; var1 = var2) {
         var2 = var1;
         if (var0[var3] < var1) {
            var2 = var0[var3];
         }

         ++var3;
      }

      return var1;
   }

   public static double min(double var0, double var2, double var4) {
      return Math.min(Math.min(var0, var2), var4);
   }

   public static double min(double... var0) {
      validateArray(var0);
      double var1 = var0[0];

      double var3;
      for(int var5 = 1; var5 < var0.length; var1 = var3) {
         if (Double.isNaN(var0[var5])) {
            return Double.NaN;
         }

         var3 = var1;
         if (var0[var5] < var1) {
            var3 = var0[var5];
         }

         ++var5;
      }

      return var1;
   }

   public static float min(float var0, float var1, float var2) {
      return Math.min(Math.min(var0, var1), var2);
   }

   public static float min(float... var0) {
      validateArray(var0);
      float var1 = var0[0];

      float var2;
      for(int var3 = 1; var3 < var0.length; var1 = var2) {
         if (Float.isNaN(var0[var3])) {
            return Float.NaN;
         }

         var2 = var1;
         if (var0[var3] < var1) {
            var2 = var0[var3];
         }

         ++var3;
      }

      return var1;
   }

   public static int min(int var0, int var1, int var2) {
      int var3 = var0;
      if (var1 < var0) {
         var3 = var1;
      }

      var0 = var3;
      if (var2 < var3) {
         var0 = var2;
      }

      return var0;
   }

   public static int min(int... var0) {
      validateArray(var0);
      int var2 = var0[0];

      int var3;
      for(int var1 = 1; var1 < var0.length; var2 = var3) {
         var3 = var2;
         if (var0[var1] < var2) {
            var3 = var0[var1];
         }

         ++var1;
      }

      return var2;
   }

   public static long min(long var0, long var2, long var4) {
      long var6 = var0;
      if (var2 < var0) {
         var6 = var2;
      }

      var0 = var6;
      if (var4 < var6) {
         var0 = var4;
      }

      return var0;
   }

   public static long min(long... var0) {
      validateArray(var0);
      long var2 = var0[0];

      long var4;
      for(int var1 = 1; var1 < var0.length; var2 = var4) {
         var4 = var2;
         if (var0[var1] < var2) {
            var4 = var0[var1];
         }

         ++var1;
      }

      return var2;
   }

   public static short min(short var0, short var1, short var2) {
      short var3 = var0;
      if (var1 < var0) {
         var3 = var1;
      }

      var0 = var3;
      if (var2 < var3) {
         var0 = var2;
      }

      return var0;
   }

   public static short min(short... var0) {
      validateArray(var0);
      short var1 = var0[0];

      short var2;
      for(int var3 = 1; var3 < var0.length; var1 = var2) {
         var2 = var1;
         if (var0[var3] < var1) {
            var2 = var0[var3];
         }

         ++var3;
      }

      return var1;
   }

   public static byte toByte(String var0) {
      return toByte(var0, (byte)0);
   }

   public static byte toByte(String var0, byte var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            byte var2 = Byte.parseByte(var0);
            return var2;
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }

   public static double toDouble(String var0) {
      return toDouble(var0, 0.0D);
   }

   public static double toDouble(String var0, double var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            double var3 = Double.parseDouble(var0);
            return var3;
         } catch (NumberFormatException var5) {
            return var1;
         }
      }
   }

   public static double toDouble(BigDecimal var0) {
      return toDouble(var0, 0.0D);
   }

   public static double toDouble(BigDecimal var0, double var1) {
      return var0 == null ? var1 : var0.doubleValue();
   }

   public static float toFloat(String var0) {
      return toFloat(var0, 0.0F);
   }

   public static float toFloat(String var0, float var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            float var2 = Float.parseFloat(var0);
            return var2;
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }

   public static int toInt(String var0) {
      return toInt(var0, 0);
   }

   public static int toInt(String var0, int var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            int var2 = Integer.parseInt(var0);
            return var2;
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }

   public static long toLong(String var0) {
      return toLong(var0, 0L);
   }

   public static long toLong(String var0, long var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            long var3 = Long.parseLong(var0);
            return var3;
         } catch (NumberFormatException var5) {
            return var1;
         }
      }
   }

   public static BigDecimal toScaledBigDecimal(Double var0) {
      return toScaledBigDecimal(var0, INTEGER_TWO, RoundingMode.HALF_EVEN);
   }

   public static BigDecimal toScaledBigDecimal(Double var0, int var1, RoundingMode var2) {
      return var0 == null ? BigDecimal.ZERO : toScaledBigDecimal(BigDecimal.valueOf(var0), var1, var2);
   }

   public static BigDecimal toScaledBigDecimal(Float var0) {
      return toScaledBigDecimal(var0, INTEGER_TWO, RoundingMode.HALF_EVEN);
   }

   public static BigDecimal toScaledBigDecimal(Float var0, int var1, RoundingMode var2) {
      return var0 == null ? BigDecimal.ZERO : toScaledBigDecimal(BigDecimal.valueOf((double)var0), var1, var2);
   }

   public static BigDecimal toScaledBigDecimal(String var0) {
      return toScaledBigDecimal(var0, INTEGER_TWO, RoundingMode.HALF_EVEN);
   }

   public static BigDecimal toScaledBigDecimal(String var0, int var1, RoundingMode var2) {
      return var0 == null ? BigDecimal.ZERO : toScaledBigDecimal(createBigDecimal(var0), var1, var2);
   }

   public static BigDecimal toScaledBigDecimal(BigDecimal var0) {
      return toScaledBigDecimal(var0, INTEGER_TWO, RoundingMode.HALF_EVEN);
   }

   public static BigDecimal toScaledBigDecimal(BigDecimal var0, int var1, RoundingMode var2) {
      if (var0 == null) {
         return BigDecimal.ZERO;
      } else {
         if (var2 == null) {
            var2 = RoundingMode.HALF_EVEN;
         }

         return var0.setScale(var1, var2);
      }
   }

   public static short toShort(String var0) {
      return toShort(var0, (short)0);
   }

   public static short toShort(String var0, short var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            short var2 = Short.parseShort(var0);
            return var2;
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }

   private static void validateArray(Object var0) {
      boolean var2 = true;
      boolean var1;
      if (var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "The Array must not be null");
      if (Array.getLength(var0) != 0) {
         var1 = var2;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "Array cannot be empty.");
   }

   private static boolean withDecimalsParsing(String var0, int var1) {
      int var4;
      for(int var2 = 0; var1 < var0.length(); var2 = var4) {
         boolean var3;
         if (var0.charAt(var1) == '.') {
            var3 = true;
         } else {
            var3 = false;
         }

         var4 = var2;
         if (var3) {
            var4 = var2 + 1;
         }

         if (var4 > 1) {
            return false;
         }

         if (!var3 && !Character.isDigit(var0.charAt(var1))) {
            return false;
         }

         ++var1;
      }

      return true;
   }
}
