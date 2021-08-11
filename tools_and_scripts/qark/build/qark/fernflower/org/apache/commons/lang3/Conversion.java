package org.apache.commons.lang3;

import java.util.UUID;

public class Conversion {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   private static final boolean[] FFFF = new boolean[]{(boolean)0, (boolean)0, (boolean)0, (boolean)0};
   private static final boolean[] FFFT = new boolean[]{(boolean)0, (boolean)0, (boolean)0, (boolean)1};
   private static final boolean[] FFTF = new boolean[]{(boolean)0, (boolean)0, (boolean)1, (boolean)0};
   private static final boolean[] FFTT = new boolean[]{(boolean)0, (boolean)0, (boolean)1, (boolean)1};
   private static final boolean[] FTFF = new boolean[]{(boolean)0, (boolean)1, (boolean)0, (boolean)0};
   private static final boolean[] FTFT = new boolean[]{(boolean)0, (boolean)1, (boolean)0, (boolean)1};
   private static final boolean[] FTTF = new boolean[]{(boolean)0, (boolean)1, (boolean)1, (boolean)0};
   private static final boolean[] FTTT = new boolean[]{(boolean)0, (boolean)1, (boolean)1, (boolean)1};
   private static final boolean[] TFFF = new boolean[]{(boolean)1, (boolean)0, (boolean)0, (boolean)0};
   private static final boolean[] TFFT = new boolean[]{(boolean)1, (boolean)0, (boolean)0, (boolean)1};
   private static final boolean[] TFTF = new boolean[]{(boolean)1, (boolean)0, (boolean)1, (boolean)0};
   private static final boolean[] TFTT = new boolean[]{(boolean)1, (boolean)0, (boolean)1, (boolean)1};
   private static final boolean[] TTFF = new boolean[]{(boolean)1, (boolean)1, (boolean)0, (boolean)0};
   private static final boolean[] TTFT = new boolean[]{(boolean)1, (boolean)1, (boolean)0, (boolean)1};
   private static final boolean[] TTTF = new boolean[]{(boolean)1, (boolean)1, (boolean)1, (boolean)0};
   private static final boolean[] TTTT = new boolean[]{(boolean)1, (boolean)1, (boolean)1, (boolean)1};

   public static char binaryBeMsb0ToHexDigit(boolean[] var0) {
      return binaryBeMsb0ToHexDigit(var0, 0);
   }

   public static char binaryBeMsb0ToHexDigit(boolean[] var0, int var1) {
      if (var0.length != 0) {
         var1 = var0.length - 1 - var1;
         int var2 = Math.min(4, var1 + 1);
         boolean[] var3 = new boolean[4];
         System.arraycopy(var0, var1 + 1 - var2, var3, 4 - var2, var2);
         if (var3[0]) {
            if (var3.length > 0 + 1 && var3[0 + 1]) {
               if (var3.length > 0 + 2 && var3[0 + 2]) {
                  return (char)(var3.length > 0 + 3 && var3[0 + 3] ? 'f' : 'e');
               } else {
                  return (char)(var3.length > 0 + 3 && var3[0 + 3] ? 'd' : 'c');
               }
            } else if (var3.length > 0 + 2 && var3[0 + 2]) {
               return (char)(var3.length > 0 + 3 && var3[0 + 3] ? 'b' : 'a');
            } else {
               return (char)(var3.length > 0 + 3 && var3[0 + 3] ? '9' : '8');
            }
         } else if (var3.length > 0 + 1 && var3[0 + 1]) {
            if (var3.length > 0 + 2 && var3[0 + 2]) {
               return (char)(var3.length > 0 + 3 && var3[0 + 3] ? '7' : '6');
            } else {
               return (char)(var3.length > 0 + 3 && var3[0 + 3] ? '5' : '4');
            }
         } else if (var3.length > 0 + 2 && var3[0 + 2]) {
            return (char)(var3.length > 0 + 3 && var3[0 + 3] ? '3' : '2');
         } else {
            return (char)(var3.length > 0 + 3 && var3[0 + 3] ? '1' : '0');
         }
      } else {
         throw new IllegalArgumentException("Cannot convert an empty array.");
      }
   }

   public static byte binaryToByte(boolean[] var0, int var1, byte var2, int var3, int var4) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:553)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:632)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:539)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s2stmt(TypeTransformer.java:820)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:843)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public static char binaryToHexDigit(boolean[] var0) {
      return binaryToHexDigit(var0, 0);
   }

   public static char binaryToHexDigit(boolean[] var0, int var1) {
      if (var0.length != 0) {
         if (var0.length > var1 + 3 && var0[var1 + 3]) {
            if (var0.length > var1 + 2 && var0[var1 + 2]) {
               if (var0.length > var1 + 1 && var0[var1 + 1]) {
                  return (char)(var0[var1] ? 'f' : 'e');
               } else {
                  return (char)(var0[var1] ? 'd' : 'c');
               }
            } else if (var0.length > var1 + 1 && var0[var1 + 1]) {
               return (char)(var0[var1] ? 'b' : 'a');
            } else {
               return (char)(var0[var1] ? '9' : '8');
            }
         } else if (var0.length > var1 + 2 && var0[var1 + 2]) {
            if (var0.length > var1 + 1 && var0[var1 + 1]) {
               return (char)(var0[var1] ? '7' : '6');
            } else {
               return (char)(var0[var1] ? '5' : '4');
            }
         } else if (var0.length > var1 + 1 && var0[var1 + 1]) {
            return (char)(var0[var1] ? '3' : '2');
         } else {
            return (char)(var0[var1] ? '1' : '0');
         }
      } else {
         throw new IllegalArgumentException("Cannot convert an empty array.");
      }
   }

   public static char binaryToHexDigitMsb0_4bits(boolean[] var0) {
      return binaryToHexDigitMsb0_4bits(var0, 0);
   }

   public static char binaryToHexDigitMsb0_4bits(boolean[] var0, int var1) {
      StringBuilder var2;
      if (var0.length <= 8) {
         if (var0.length - var1 >= 4) {
            if (var0[var1 + 3]) {
               if (var0[var1 + 2]) {
                  if (var0[var1 + 1]) {
                     return (char)(var0[var1] ? 'f' : '7');
                  } else {
                     return (char)(var0[var1] ? 'b' : '3');
                  }
               } else if (var0[var1 + 1]) {
                  return (char)(var0[var1] ? 'd' : '5');
               } else {
                  return (char)(var0[var1] ? '9' : '1');
               }
            } else if (var0[var1 + 2]) {
               if (var0[var1 + 1]) {
                  return (char)(var0[var1] ? 'e' : '6');
               } else {
                  return (char)(var0[var1] ? 'a' : '2');
               }
            } else if (var0[var1 + 1]) {
               return (char)(var0[var1] ? 'c' : '4');
            } else {
               return (char)(var0[var1] ? '8' : '0');
            }
         } else {
            var2 = new StringBuilder();
            var2.append("src.length-srcPos<4: src.length=");
            var2.append(var0.length);
            var2.append(", srcPos=");
            var2.append(var1);
            throw new IllegalArgumentException(var2.toString());
         }
      } else {
         var2 = new StringBuilder();
         var2.append("src.length>8: src.length=");
         var2.append(var0.length);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public static int binaryToInt(boolean[] var0, int var1, int var2, int var3, int var4) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:553)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:632)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s2stmt(TypeTransformer.java:820)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:843)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public static long binaryToLong(boolean[] var0, int var1, long var2, int var4, int var5) {
      if ((var0.length != 0 || var1 != 0) && var5 != 0) {
         if (var5 - 1 + var4 < 64) {
            for(int var6 = 0; var6 < var5; ++var6) {
               int var7 = var6 + var4;
               long var8;
               if (var0[var6 + var1]) {
                  var8 = 1L;
               } else {
                  var8 = 0L;
               }

               var2 = 1L << var7 & var2 | var8 << var7;
            }

            return var2;
         } else {
            throw new IllegalArgumentException("nBools-1+dstPos is greater or equal to than 64");
         }
      } else {
         return var2;
      }
   }

   public static short binaryToShort(boolean[] var0, int var1, short var2, int var3, int var4) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:553)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:632)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:539)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s2stmt(TypeTransformer.java:820)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:843)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public static int byteArrayToInt(byte[] var0, int var1, int var2, int var3, int var4) {
      if ((var0.length != 0 || var1 != 0) && var4 != 0) {
         if ((var4 - 1) * 8 + var3 >= 32) {
            throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greater or equal to than 32");
         } else {
            int var5 = var2;

            for(var2 = 0; var2 < var4; ++var2) {
               int var6 = var2 * 8 + var3;
               var5 = 255 << var6 & var5 | (var0[var2 + var1] & 255) << var6;
            }

            return var5;
         }
      } else {
         return var2;
      }
   }

   public static long byteArrayToLong(byte[] var0, int var1, long var2, int var4, int var5) {
      if ((var0.length != 0 || var1 != 0) && var5 != 0) {
         if ((var5 - 1) * 8 + var4 >= 64) {
            throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greater or equal to than 64");
         } else {
            for(int var6 = 0; var6 < var5; ++var6) {
               int var7 = var6 * 8 + var4;
               var2 = 255L << var7 & var2 | ((long)var0[var6 + var1] & 255L) << var7;
            }

            return var2;
         }
      } else {
         return var2;
      }
   }

   public static short byteArrayToShort(byte[] var0, int var1, short var2, int var3, int var4) {
      if ((var0.length != 0 || var1 != 0) && var4 != 0) {
         if ((var4 - 1) * 8 + var3 >= 16) {
            throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greater or equal to than 16");
         } else {
            for(int var5 = 0; var5 < var4; ++var5) {
               int var6 = var5 * 8 + var3;
               var2 = (short)(255 << var6 & var2 | (var0[var5 + var1] & 255) << var6);
            }

            return var2;
         }
      } else {
         return var2;
      }
   }

   public static UUID byteArrayToUuid(byte[] var0, int var1) {
      if (var0.length - var1 >= 16) {
         return new UUID(byteArrayToLong(var0, var1, 0L, 0, 8), byteArrayToLong(var0, var1 + 8, 0L, 0, 8));
      } else {
         throw new IllegalArgumentException("Need at least 16 bytes for UUID");
      }
   }

   public static boolean[] byteToBinary(byte var0, int var1, boolean[] var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if (var4 - 1 + var1 < 8) {
         for(int var5 = 0; var5 < var4; ++var5) {
            boolean var6 = true;
            if ((var0 >> var5 + var1 & 1) == 0) {
               var6 = false;
            }

            var2[var3 + var5] = var6;
         }

         return var2;
      } else {
         throw new IllegalArgumentException("nBools-1+srcPos is greater or equal to than 8");
      }
   }

   public static String byteToHex(byte var0, int var1, String var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if ((var4 - 1) * 4 + var1 < 8) {
         StringBuilder var8 = new StringBuilder(var2);
         int var6 = var8.length();

         for(int var5 = 0; var5 < var4; ++var5) {
            int var7 = var0 >> var5 * 4 + var1 & 15;
            if (var3 + var5 == var6) {
               ++var6;
               var8.append(intToHexDigit(var7));
            } else {
               var8.setCharAt(var3 + var5, intToHexDigit(var7));
            }
         }

         return var8.toString();
      } else {
         throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greater or equal to than 8");
      }
   }

   public static boolean[] hexDigitMsb0ToBinary(char var0) {
      switch(var0) {
      case '0':
         return (boolean[])FFFF.clone();
      case '1':
         return (boolean[])FFFT.clone();
      case '2':
         return (boolean[])FFTF.clone();
      case '3':
         return (boolean[])FFTT.clone();
      case '4':
         return (boolean[])FTFF.clone();
      case '5':
         return (boolean[])FTFT.clone();
      case '6':
         return (boolean[])FTTF.clone();
      case '7':
         return (boolean[])FTTT.clone();
      case '8':
         return (boolean[])TFFF.clone();
      case '9':
         return (boolean[])TFFT.clone();
      default:
         switch(var0) {
         case 'B':
            return (boolean[])TFTT.clone();
         case 'C':
            return (boolean[])TTFF.clone();
         case 'D':
            return (boolean[])TTFT.clone();
         case 'E':
            return (boolean[])TTTF.clone();
         case 'F':
            return (boolean[])TTTT.clone();
         default:
            switch(var0) {
            case 'a':
               break;
            case 'b':
               return (boolean[])TFTT.clone();
            case 'c':
               return (boolean[])TTFF.clone();
            case 'd':
               return (boolean[])TTFT.clone();
            case 'e':
               return (boolean[])TTTF.clone();
            case 'f':
               return (boolean[])TTTT.clone();
            default:
               StringBuilder var1 = new StringBuilder();
               var1.append("Cannot interpret '");
               var1.append(var0);
               var1.append("' as a hexadecimal digit");
               throw new IllegalArgumentException(var1.toString());
            }
         case 'A':
            return (boolean[])TFTF.clone();
         }
      }
   }

   public static int hexDigitMsb0ToInt(char var0) {
      switch(var0) {
      case '0':
         return 0;
      case '1':
         return 8;
      case '2':
         return 4;
      case '3':
         return 12;
      case '4':
         return 2;
      case '5':
         return 10;
      case '6':
         return 6;
      case '7':
         return 14;
      case '8':
         return 1;
      case '9':
         return 9;
      default:
         switch(var0) {
         case 'B':
            return 13;
         case 'C':
            return 3;
         case 'D':
            return 11;
         case 'E':
            return 7;
         case 'F':
            return 15;
         default:
            switch(var0) {
            case 'a':
               break;
            case 'b':
               return 13;
            case 'c':
               return 3;
            case 'd':
               return 11;
            case 'e':
               return 7;
            case 'f':
               return 15;
            default:
               StringBuilder var1 = new StringBuilder();
               var1.append("Cannot interpret '");
               var1.append(var0);
               var1.append("' as a hexadecimal digit");
               throw new IllegalArgumentException(var1.toString());
            }
         case 'A':
            return 5;
         }
      }
   }

   public static boolean[] hexDigitToBinary(char var0) {
      switch(var0) {
      case '0':
         return (boolean[])FFFF.clone();
      case '1':
         return (boolean[])TFFF.clone();
      case '2':
         return (boolean[])FTFF.clone();
      case '3':
         return (boolean[])TTFF.clone();
      case '4':
         return (boolean[])FFTF.clone();
      case '5':
         return (boolean[])TFTF.clone();
      case '6':
         return (boolean[])FTTF.clone();
      case '7':
         return (boolean[])TTTF.clone();
      case '8':
         return (boolean[])FFFT.clone();
      case '9':
         return (boolean[])TFFT.clone();
      default:
         switch(var0) {
         case 'B':
            return (boolean[])TTFT.clone();
         case 'C':
            return (boolean[])FFTT.clone();
         case 'D':
            return (boolean[])TFTT.clone();
         case 'E':
            return (boolean[])FTTT.clone();
         case 'F':
            return (boolean[])TTTT.clone();
         default:
            switch(var0) {
            case 'a':
               break;
            case 'b':
               return (boolean[])TTFT.clone();
            case 'c':
               return (boolean[])FFTT.clone();
            case 'd':
               return (boolean[])TFTT.clone();
            case 'e':
               return (boolean[])FTTT.clone();
            case 'f':
               return (boolean[])TTTT.clone();
            default:
               StringBuilder var1 = new StringBuilder();
               var1.append("Cannot interpret '");
               var1.append(var0);
               var1.append("' as a hexadecimal digit");
               throw new IllegalArgumentException(var1.toString());
            }
         case 'A':
            return (boolean[])FTFT.clone();
         }
      }
   }

   public static int hexDigitToInt(char var0) {
      int var1 = Character.digit(var0, 16);
      if (var1 >= 0) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Cannot interpret '");
         var2.append(var0);
         var2.append("' as a hexadecimal digit");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public static byte hexToByte(String var0, int var1, byte var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if ((var4 - 1) * 4 + var3 >= 8) {
         throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greater or equal to than 8");
      } else {
         for(int var5 = 0; var5 < var4; ++var5) {
            int var6 = var5 * 4 + var3;
            var2 = (byte)(15 << var6 & var2 | (hexDigitToInt(var0.charAt(var5 + var1)) & 15) << var6);
         }

         return var2;
      }
   }

   public static int hexToInt(String var0, int var1, int var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if ((var4 - 1) * 4 + var3 >= 32) {
         throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greater or equal to than 32");
      } else {
         int var5 = var2;

         for(var2 = 0; var2 < var4; ++var2) {
            int var6 = var2 * 4 + var3;
            var5 = 15 << var6 & var5 | (hexDigitToInt(var0.charAt(var2 + var1)) & 15) << var6;
         }

         return var5;
      }
   }

   public static long hexToLong(String var0, int var1, long var2, int var4, int var5) {
      if (var5 == 0) {
         return var2;
      } else if ((var5 - 1) * 4 + var4 >= 64) {
         throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greater or equal to than 64");
      } else {
         for(int var6 = 0; var6 < var5; ++var6) {
            int var7 = var6 * 4 + var4;
            var2 = 15L << var7 & var2 | ((long)hexDigitToInt(var0.charAt(var6 + var1)) & 15L) << var7;
         }

         return var2;
      }
   }

   public static short hexToShort(String var0, int var1, short var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if ((var4 - 1) * 4 + var3 >= 16) {
         throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greater or equal to than 16");
      } else {
         for(int var5 = 0; var5 < var4; ++var5) {
            int var6 = var5 * 4 + var3;
            var2 = (short)(15 << var6 & var2 | (hexDigitToInt(var0.charAt(var5 + var1)) & 15) << var6);
         }

         return var2;
      }
   }

   public static long intArrayToLong(int[] var0, int var1, long var2, int var4, int var5) {
      if ((var0.length != 0 || var1 != 0) && var5 != 0) {
         if ((var5 - 1) * 32 + var4 >= 64) {
            throw new IllegalArgumentException("(nInts-1)*32+dstPos is greater or equal to than 64");
         } else {
            for(int var6 = 0; var6 < var5; ++var6) {
               int var7 = var6 * 32 + var4;
               var2 = 4294967295L << var7 & var2 | ((long)var0[var6 + var1] & 4294967295L) << var7;
            }

            return var2;
         }
      } else {
         return var2;
      }
   }

   public static boolean[] intToBinary(int var0, int var1, boolean[] var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if (var4 - 1 + var1 < 32) {
         for(int var5 = 0; var5 < var4; ++var5) {
            boolean var6 = true;
            if ((var0 >> var5 + var1 & 1) == 0) {
               var6 = false;
            }

            var2[var3 + var5] = var6;
         }

         return var2;
      } else {
         throw new IllegalArgumentException("nBools-1+srcPos is greater or equal to than 32");
      }
   }

   public static byte[] intToByteArray(int var0, int var1, byte[] var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if ((var4 - 1) * 8 + var1 >= 32) {
         throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greater or equal to than 32");
      } else {
         for(int var5 = 0; var5 < var4; ++var5) {
            var2[var3 + var5] = (byte)(var0 >> var5 * 8 + var1 & 255);
         }

         return var2;
      }
   }

   public static String intToHex(int var0, int var1, String var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if ((var4 - 1) * 4 + var1 < 32) {
         StringBuilder var8 = new StringBuilder(var2);
         int var6 = var8.length();

         for(int var5 = 0; var5 < var4; ++var5) {
            int var7 = var0 >> var5 * 4 + var1 & 15;
            if (var3 + var5 == var6) {
               ++var6;
               var8.append(intToHexDigit(var7));
            } else {
               var8.setCharAt(var3 + var5, intToHexDigit(var7));
            }
         }

         return var8.toString();
      } else {
         throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greater or equal to than 32");
      }
   }

   public static char intToHexDigit(int var0) {
      char var1 = Character.forDigit(var0, 16);
      if (var1 != 0) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("nibble value not between 0 and 15: ");
         var2.append(var0);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public static char intToHexDigitMsb0(int var0) {
      switch(var0) {
      case 0:
         return '0';
      case 1:
         return '8';
      case 2:
         return '4';
      case 3:
         return 'c';
      case 4:
         return '2';
      case 5:
         return 'a';
      case 6:
         return '6';
      case 7:
         return 'e';
      case 8:
         return '1';
      case 9:
         return '9';
      case 10:
         return '5';
      case 11:
         return 'd';
      case 12:
         return '3';
      case 13:
         return 'b';
      case 14:
         return '7';
      case 15:
         return 'f';
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("nibble value not between 0 and 15: ");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static short[] intToShortArray(int var0, int var1, short[] var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if ((var4 - 1) * 16 + var1 >= 32) {
         throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greater or equal to than 32");
      } else {
         for(int var5 = 0; var5 < var4; ++var5) {
            var2[var3 + var5] = (short)('\uffff' & var0 >> var5 * 16 + var1);
         }

         return var2;
      }
   }

   public static boolean[] longToBinary(long var0, int var2, boolean[] var3, int var4, int var5) {
      if (var5 == 0) {
         return var3;
      } else if (var5 - 1 + var2 < 64) {
         for(int var6 = 0; var6 < var5; ++var6) {
            boolean var7;
            if ((1L & var0 >> var6 + var2) != 0L) {
               var7 = true;
            } else {
               var7 = false;
            }

            var3[var4 + var6] = var7;
         }

         return var3;
      } else {
         throw new IllegalArgumentException("nBools-1+srcPos is greater or equal to than 64");
      }
   }

   public static byte[] longToByteArray(long var0, int var2, byte[] var3, int var4, int var5) {
      if (var5 == 0) {
         return var3;
      } else if ((var5 - 1) * 8 + var2 >= 64) {
         throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greater or equal to than 64");
      } else {
         for(int var6 = 0; var6 < var5; ++var6) {
            var3[var4 + var6] = (byte)((int)(255L & var0 >> var6 * 8 + var2));
         }

         return var3;
      }
   }

   public static String longToHex(long var0, int var2, String var3, int var4, int var5) {
      if (var5 == 0) {
         return var3;
      } else if ((var5 - 1) * 4 + var2 < 64) {
         StringBuilder var9 = new StringBuilder(var3);
         int var7 = var9.length();

         for(int var6 = 0; var6 < var5; ++var6) {
            int var8 = (int)(15L & var0 >> var6 * 4 + var2);
            if (var4 + var6 == var7) {
               ++var7;
               var9.append(intToHexDigit(var8));
            } else {
               var9.setCharAt(var4 + var6, intToHexDigit(var8));
            }
         }

         return var9.toString();
      } else {
         throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greater or equal to than 64");
      }
   }

   public static int[] longToIntArray(long var0, int var2, int[] var3, int var4, int var5) {
      if (var5 == 0) {
         return var3;
      } else if ((var5 - 1) * 32 + var2 >= 64) {
         throw new IllegalArgumentException("(nInts-1)*32+srcPos is greater or equal to than 64");
      } else {
         for(int var6 = 0; var6 < var5; ++var6) {
            var3[var4 + var6] = (int)(-1L & var0 >> var6 * 32 + var2);
         }

         return var3;
      }
   }

   public static short[] longToShortArray(long var0, int var2, short[] var3, int var4, int var5) {
      if (var5 == 0) {
         return var3;
      } else if ((var5 - 1) * 16 + var2 >= 64) {
         throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greater or equal to than 64");
      } else {
         for(int var6 = 0; var6 < var5; ++var6) {
            var3[var4 + var6] = (short)((int)(65535L & var0 >> var6 * 16 + var2));
         }

         return var3;
      }
   }

   public static int shortArrayToInt(short[] var0, int var1, int var2, int var3, int var4) {
      if ((var0.length != 0 || var1 != 0) && var4 != 0) {
         if ((var4 - 1) * 16 + var3 >= 32) {
            throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greater or equal to than 32");
         } else {
            int var5 = var2;

            for(var2 = 0; var2 < var4; ++var2) {
               int var6 = var2 * 16 + var3;
               var5 = '\uffff' << var6 & var5 | (var0[var2 + var1] & '\uffff') << var6;
            }

            return var5;
         }
      } else {
         return var2;
      }
   }

   public static long shortArrayToLong(short[] var0, int var1, long var2, int var4, int var5) {
      if ((var0.length != 0 || var1 != 0) && var5 != 0) {
         if ((var5 - 1) * 16 + var4 >= 64) {
            throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greater or equal to than 64");
         } else {
            for(int var6 = 0; var6 < var5; ++var6) {
               int var7 = var6 * 16 + var4;
               var2 = 65535L << var7 & var2 | ((long)var0[var6 + var1] & 65535L) << var7;
            }

            return var2;
         }
      } else {
         return var2;
      }
   }

   public static boolean[] shortToBinary(short var0, int var1, boolean[] var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if (var4 - 1 + var1 < 16) {
         for(int var5 = 0; var5 < var4; ++var5) {
            boolean var6 = true;
            if ((var0 >> var5 + var1 & 1) == 0) {
               var6 = false;
            }

            var2[var3 + var5] = var6;
         }

         return var2;
      } else {
         throw new IllegalArgumentException("nBools-1+srcPos is greater or equal to than 16");
      }
   }

   public static byte[] shortToByteArray(short var0, int var1, byte[] var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if ((var4 - 1) * 8 + var1 >= 16) {
         throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greater or equal to than 16");
      } else {
         for(int var5 = 0; var5 < var4; ++var5) {
            var2[var3 + var5] = (byte)(var0 >> var5 * 8 + var1 & 255);
         }

         return var2;
      }
   }

   public static String shortToHex(short var0, int var1, String var2, int var3, int var4) {
      if (var4 == 0) {
         return var2;
      } else if ((var4 - 1) * 4 + var1 < 16) {
         StringBuilder var8 = new StringBuilder(var2);
         int var6 = var8.length();

         for(int var5 = 0; var5 < var4; ++var5) {
            int var7 = var0 >> var5 * 4 + var1 & 15;
            if (var3 + var5 == var6) {
               ++var6;
               var8.append(intToHexDigit(var7));
            } else {
               var8.setCharAt(var3 + var5, intToHexDigit(var7));
            }
         }

         return var8.toString();
      } else {
         throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greater or equal to than 16");
      }
   }

   public static byte[] uuidToByteArray(UUID var0, byte[] var1, int var2, int var3) {
      if (var3 == 0) {
         return var1;
      } else if (var3 <= 16) {
         long var5 = var0.getMostSignificantBits();
         int var4;
         if (var3 > 8) {
            var4 = 8;
         } else {
            var4 = var3;
         }

         longToByteArray(var5, 0, var1, var2, var4);
         if (var3 >= 8) {
            longToByteArray(var0.getLeastSignificantBits(), 0, var1, var2 + 8, var3 - 8);
         }

         return var1;
      } else {
         throw new IllegalArgumentException("nBytes is greater than 16");
      }
   }
}
