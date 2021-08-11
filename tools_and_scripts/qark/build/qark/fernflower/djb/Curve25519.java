package djb;

public class Curve25519 {
   private static final Curve25519.long10 BASE_2Y = new Curve25519.long10(39999547L, 18689728L, 59995525L, 1648697L, 57546132L, 24010086L, 19059592L, 5425144L, 63499247L, 16420658L);
   private static final Curve25519.long10 BASE_R2Y = new Curve25519.long10(5744L, 8160848L, 4790893L, 13779497L, 35730846L, 12541209L, 49101323L, 30047407L, 40071253L, 6226132L);
   public static final int KEY_SIZE = 32;
   public static final byte[] ORDER = new byte[]{-19, -45, -11, 92, 26, 99, 18, 88, -42, -100, -9, -94, -34, -7, -34, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16};
   private static final byte[] ORDER_TIMES_8 = new byte[]{104, -97, -82, -25, -46, 24, -109, -64, -78, -26, -68, 23, -11, -50, -9, -90, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -128};
   private static final int P25 = 33554431;
   private static final int P26 = 67108863;
   public static final byte[] PRIME = new byte[]{-19, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 127};
   public static final byte[] ZERO = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

   private static void add(Curve25519.long10 var0, Curve25519.long10 var1, Curve25519.long10 var2) {
      var0.field_222 = var1.field_222 + var2.field_222;
      var0.field_223 = var1.field_223 + var2.field_223;
      var0.field_224 = var1.field_224 + var2.field_224;
      var0.field_225 = var1.field_225 + var2.field_225;
      var0.field_226 = var1.field_226 + var2.field_226;
      var0.field_227 = var1.field_227 + var2.field_227;
      var0.field_228 = var1.field_228 + var2.field_228;
      var0.field_229 = var1.field_229 + var2.field_229;
      var0.field_230 = var1.field_230 + var2.field_230;
      var0.field_231 = var1.field_231 + var2.field_231;
   }

   public static void clamp(byte[] var0) {
      var0[31] = (byte)(var0[31] & 127);
      var0[31] = (byte)(var0[31] | 64);
      var0[0] = (byte)(var0[0] & 248);
   }

   private static void core(byte[] var0, byte[] var1, byte[] var2, byte[] var3) {
      Curve25519.long10 var8 = new Curve25519.long10();
      Curve25519.long10 var9 = new Curve25519.long10();
      Curve25519.long10 var10 = new Curve25519.long10();
      Curve25519.long10 var11 = new Curve25519.long10();
      Curve25519.long10 var12 = new Curve25519.long10();
      Curve25519.long10[] var13 = new Curve25519.long10[]{new Curve25519.long10(), new Curve25519.long10()};
      Curve25519.long10[] var14 = new Curve25519.long10[]{new Curve25519.long10(), new Curve25519.long10()};
      if (var3 != null) {
         unpack(var8, var3);
      } else {
         set(var8, 9);
      }

      set(var13[0], 1);
      set(var14[0], 0);
      cpy(var13[1], var8);
      set(var14[1], 1);
      int var4 = 32;

      while(true) {
         int var5 = var4 - 1;
         if (var4 == 0) {
            recip(var9, var14[0], 0);
            mul(var8, var13[0], var9);
            pack(var8, var0);
            if (var1 != null) {
               x_to_y2(var10, var9, var8);
               recip(var11, var14[1], 0);
               mul(var10, var13[1], var11);
               add(var10, var10, var8);
               var10.field_222 += 486671L;
               var8.field_222 -= 9L;
               sqr(var11, var8);
               mul(var8, var10, var11);
               sub(var8, var8, var9);
               var8.field_222 -= 39420360L;
               mul(var9, var8, BASE_R2Y);
               if (is_negative(var9) != 0) {
                  cpy32(var1, var2);
               } else {
                  mula_small(var1, ORDER_TIMES_8, 0, var2, 32, -1);
               }

               var0 = new byte[32];
               var2 = new byte[64];
               var3 = new byte[64];
               cpy32(var0, ORDER);
               cpy32(var1, egcd32(var2, var3, var1, var0));
               if ((var1[31] & 128) != 0) {
                  mula_small(var1, var1, 0, ORDER, 32, 1);
                  return;
               }

               return;
            }

            return;
         }

         if (var5 == 0) {
            var4 = 0;
         } else {
            var4 = var5;
         }

         var5 = 8;

         while(true) {
            int var6 = var5 - 1;
            if (var5 == 0) {
               break;
            }

            var5 = (var2[var4] & 255) >> var6 & 1;
            int var7 = (var2[var4] & 255) >> var6 & 1;
            Curve25519.long10 var18 = var13[var7];
            Curve25519.long10 var15 = var14[var7];
            Curve25519.long10 var16 = var13[var5];
            Curve25519.long10 var17 = var14[var5];
            mont_prep(var9, var10, var18, var15);
            mont_prep(var11, var12, var16, var17);
            mont_add(var9, var10, var11, var12, var18, var15, var8);
            mont_dbl(var9, var10, var11, var12, var16, var17);
            var5 = var6;
         }
      }
   }

   private static void cpy(Curve25519.long10 var0, Curve25519.long10 var1) {
      var0.field_222 = var1.field_222;
      var0.field_223 = var1.field_223;
      var0.field_224 = var1.field_224;
      var0.field_225 = var1.field_225;
      var0.field_226 = var1.field_226;
      var0.field_227 = var1.field_227;
      var0.field_228 = var1.field_228;
      var0.field_229 = var1.field_229;
      var0.field_230 = var1.field_230;
      var0.field_231 = var1.field_231;
   }

   private static void cpy32(byte[] var0, byte[] var1) {
      for(int var2 = 0; var2 < 32; ++var2) {
         var0[var2] = var1[var2];
      }

   }

   public static void curve(byte[] var0, byte[] var1, byte[] var2) {
      core(var0, (byte[])null, var1, var2);
   }

   private static void divmod(byte[] var0, byte[] var1, int var2, byte[] var3, int var4) {
      byte var8 = 0;
      int var9 = (var3[var4 - 1] & 255) << 8;
      int var6 = var8;
      int var5 = var9;
      int var7 = var2;
      if (var4 > 1) {
         var5 = var9 | var3[var4 - 2] & 255;
         var7 = var2;
         var6 = var8;
      }

      while(true) {
         int var10 = var7 - 1;
         if (var7 < var4) {
            var1[var4 - 1] = (byte)var6;
            return;
         }

         var7 = var6 << 16 | (var1[var10] & 255) << 8;
         var2 = var7;
         if (var10 > 0) {
            var2 = var7 | var1[var10 - 1] & 255;
         }

         var2 /= var5;
         var6 += mula_small(var1, var1, var10 - var4 + 1, var3, var4, -var2);
         var0[var10 - var4 + 1] = (byte)(var2 + var6 & 255);
         mula_small(var1, var1, var10 - var4 + 1, var3, var4, -var6);
         var6 = var1[var10] & 255;
         var1[var10] = 0;
         var7 = var10;
      }
   }

   private static byte[] egcd32(byte[] var0, byte[] var1, byte[] var2, byte[] var3) {
      byte var6 = 32;

      int var4;
      for(var4 = 0; var4 < 32; ++var4) {
         var1[var4] = 0;
         var0[var4] = 0;
      }

      var0[0] = 1;
      int var5 = numsize(var2, 32);
      if (var5 == 0) {
         return var1;
      } else {
         byte[] var7 = new byte[32];
         var4 = var6;

         while(true) {
            int var8 = var4;
            divmod(var7, var3, var4, var2, var5);
            var4 = numsize(var3, var4);
            if (var4 == 0) {
               return var0;
            }

            mula32(var1, var0, var7, var8 - var5 + 1, -1);
            divmod(var7, var2, var5, var3, var4);
            var8 = numsize(var2, var5);
            if (var8 == 0) {
               return var1;
            }

            mula32(var0, var1, var7, var5 - var4 + 1, -1);
            var5 = var8;
         }
      }
   }

   private static int is_negative(Curve25519.long10 var0) {
      byte var1;
      if (!is_overflow(var0) && var0.field_231 >= 0L) {
         var1 = 0;
      } else {
         var1 = 1;
      }

      return (int)((long)var1 ^ var0.field_222 & 1L);
   }

   private static boolean is_overflow(Curve25519.long10 var0) {
      return var0.field_222 > 67108844L && (var0.field_223 & var0.field_225 & var0.field_227 & var0.field_229 & var0.field_231) == 33554431L && (var0.field_224 & var0.field_226 & var0.field_228 & var0.field_230) == 67108863L || var0.field_231 > 33554431L;
   }

   public static void keygen(byte[] var0, byte[] var1, byte[] var2) {
      clamp(var2);
      core(var0, var1, var2, (byte[])null);
   }

   private static void mont_add(Curve25519.long10 var0, Curve25519.long10 var1, Curve25519.long10 var2, Curve25519.long10 var3, Curve25519.long10 var4, Curve25519.long10 var5, Curve25519.long10 var6) {
      mul(var4, var1, var2);
      mul(var5, var0, var3);
      add(var0, var4, var5);
      sub(var1, var4, var5);
      sqr(var4, var0);
      sqr(var0, var1);
      mul(var5, var0, var6);
   }

   private static void mont_dbl(Curve25519.long10 var0, Curve25519.long10 var1, Curve25519.long10 var2, Curve25519.long10 var3, Curve25519.long10 var4, Curve25519.long10 var5) {
      sqr(var0, var2);
      sqr(var1, var3);
      mul(var4, var0, var1);
      sub(var1, var0, var1);
      mul_small(var5, var1, 121665L);
      add(var0, var0, var5);
      mul(var5, var0, var1);
   }

   private static void mont_prep(Curve25519.long10 var0, Curve25519.long10 var1, Curve25519.long10 var2, Curve25519.long10 var3) {
      add(var0, var2, var3);
      sub(var1, var2, var3);
   }

   private static Curve25519.long10 mul(Curve25519.long10 var0, Curve25519.long10 var1, Curve25519.long10 var2) {
      long var3 = var1.field_222;
      long var5 = var1.field_223;
      long var7 = var1.field_224;
      long var9 = var1.field_225;
      long var11 = var1.field_226;
      long var13 = var1.field_227;
      long var15 = var1.field_228;
      long var17 = var1.field_229;
      long var19 = var1.field_230;
      long var21 = var1.field_231;
      long var23 = var2.field_222;
      long var25 = var2.field_223;
      long var27 = var2.field_224;
      long var29 = var2.field_225;
      long var31 = var2.field_226;
      long var33 = var2.field_227;
      long var35 = var2.field_228;
      long var37 = var2.field_229;
      long var39 = var2.field_230;
      long var41 = var2.field_231;
      long var43 = var3 * var39 + var7 * var35 + var11 * var31 + var15 * var27 + var19 * var23 + (var5 * var37 + var9 * var33 + var13 * var29 + var17 * var25) * 2L + var21 * var41 * 38L;
      var0.field_230 = var43 & 67108863L;
      var43 = (var43 >> 26) + var3 * var41 + var5 * var39 + var7 * var37 + var9 * var35 + var11 * var33 + var13 * var31 + var15 * var29 + var17 * var27 + var19 * var25 + var21 * var23;
      var0.field_231 = var43 & 33554431L;
      var43 = var3 * var23 + ((var43 >> 25) + var7 * var39 + var11 * var35 + var15 * var31 + var19 * var27) * 19L + (var5 * var41 + var9 * var37 + var13 * var33 + var17 * var29 + var21 * var25) * 38L;
      var0.field_222 = var43 & 67108863L;
      var43 = (var43 >> 26) + var3 * var25 + var5 * var23 + (var7 * var41 + var9 * var39 + var11 * var37 + var13 * var35 + var15 * var33 + var17 * var31 + var19 * var29 + var21 * var27) * 19L;
      var0.field_223 = var43 & 33554431L;
      var43 = (var43 >> 25) + var3 * var27 + var7 * var23 + (var11 * var39 + var15 * var35 + var19 * var31) * 19L + var5 * var25 * 2L + (var9 * var41 + var13 * var37 + var17 * var33 + var21 * var29) * 38L;
      var0.field_224 = var43 & 67108863L;
      var43 = (var43 >> 26) + var3 * var29 + var5 * var27 + var7 * var25 + var9 * var23 + (var11 * var41 + var13 * var39 + var15 * var37 + var17 * var35 + var19 * var33 + var21 * var31) * 19L;
      var0.field_225 = var43 & 33554431L;
      var43 = (var43 >> 25) + var3 * var31 + var7 * var27 + var11 * var23 + (var15 * var39 + var19 * var35) * 19L + (var5 * var29 + var9 * var25) * 2L + (var13 * var41 + var17 * var37 + var21 * var33) * 38L;
      var0.field_226 = var43 & 67108863L;
      var43 = (var43 >> 26) + var3 * var33 + var5 * var31 + var7 * var29 + var9 * var27 + var11 * var25 + var13 * var23 + (var15 * var41 + var17 * var39 + var19 * var37 + var21 * var35) * 19L;
      var0.field_227 = var43 & 33554431L;
      var43 = (var43 >> 25) + var3 * var35 + var7 * var31 + var11 * var27 + var15 * var23 + var19 * var39 * 19L + (var5 * var33 + var9 * var29 + var13 * var25) * 2L + (var17 * var41 + var21 * var37) * 38L;
      var0.field_228 = var43 & 67108863L;
      var3 = (var43 >> 26) + var3 * var37 + var5 * var35 + var7 * var33 + var9 * var31 + var11 * var29 + var13 * var27 + var15 * var25 + var17 * var23 + (var19 * var41 + var21 * var39) * 19L;
      var0.field_229 = var3 & 33554431L;
      var3 = (var3 >> 25) + var0.field_230;
      var0.field_230 = var3 & 67108863L;
      var0.field_231 += var3 >> 26;
      return var0;
   }

   private static Curve25519.long10 mul_small(Curve25519.long10 var0, Curve25519.long10 var1, long var2) {
      long var4 = var1.field_230 * var2;
      var0.field_230 = var4 & 67108863L;
      var4 = (var4 >> 26) + var1.field_231 * var2;
      var0.field_231 = var4 & 33554431L;
      var4 = (var4 >> 25) * 19L + var1.field_222 * var2;
      var0.field_222 = var4 & 67108863L;
      var4 = (var4 >> 26) + var1.field_223 * var2;
      var0.field_223 = var4 & 33554431L;
      var4 = (var4 >> 25) + var1.field_224 * var2;
      var0.field_224 = var4 & 67108863L;
      var4 = (var4 >> 26) + var1.field_225 * var2;
      var0.field_225 = var4 & 33554431L;
      var4 = (var4 >> 25) + var1.field_226 * var2;
      var0.field_226 = var4 & 67108863L;
      var4 = (var4 >> 26) + var1.field_227 * var2;
      var0.field_227 = var4 & 33554431L;
      var4 = (var4 >> 25) + var1.field_228 * var2;
      var0.field_228 = var4 & 67108863L;
      var2 = (var4 >> 26) + var1.field_229 * var2;
      var0.field_229 = 33554431L & var2;
      var2 = (var2 >> 25) + var0.field_230;
      var0.field_230 = 67108863L & var2;
      var0.field_231 += var2 >> 26;
      return var0;
   }

   private static int mula32(byte[] var0, byte[] var1, byte[] var2, int var3, int var4) {
      int var6 = 0;

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         int var7 = var4 * (var2[var5] & 255);
         var6 += mula_small(var0, var0, var5, var1, 31, var7) + (var0[var5 + 31] & 255) + (var1[31] & 255) * var7;
         var0[var5 + 31] = (byte)var6;
         var6 >>= 8;
      }

      var0[var5 + 31] = (byte)((var0[var5 + 31] & 255) + var6);
      return var6 >> 8;
   }

   private static int mula_small(byte[] var0, byte[] var1, int var2, byte[] var3, int var4, int var5) {
      int var7 = 0;

      for(int var6 = 0; var6 < var4; ++var6) {
         var7 += (var1[var6 + var2] & 255) + (var3[var6] & 255) * var5;
         var0[var6 + var2] = (byte)var7;
         var7 >>= 8;
      }

      return var7;
   }

   private static int numsize(byte[] var0, int var1) {
      while(true) {
         int var2 = var1 - 1;
         if (var1 == 0 || var0[var2] != 0) {
            return var2 + 1;
         }

         var1 = var2;
      }
   }

   private static void pack(Curve25519.long10 var0, byte[] var1) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   private static void recip(Curve25519.long10 var0, Curve25519.long10 var1, int var2) {
      Curve25519.long10 var4 = new Curve25519.long10();
      Curve25519.long10 var5 = new Curve25519.long10();
      Curve25519.long10 var6 = new Curve25519.long10();
      Curve25519.long10 var7 = new Curve25519.long10();
      Curve25519.long10 var8 = new Curve25519.long10();
      sqr(var5, var1);
      sqr(var6, var5);
      sqr(var4, var6);
      mul(var6, var4, var1);
      mul(var4, var6, var5);
      sqr(var5, var4);
      mul(var7, var5, var6);
      sqr(var5, var7);
      sqr(var6, var5);
      sqr(var5, var6);
      sqr(var6, var5);
      sqr(var5, var6);
      mul(var6, var5, var7);
      sqr(var5, var6);
      sqr(var7, var5);

      int var3;
      for(var3 = 1; var3 < 5; ++var3) {
         sqr(var5, var7);
         sqr(var7, var5);
      }

      mul(var5, var7, var6);
      sqr(var7, var5);
      sqr(var8, var7);

      for(var3 = 1; var3 < 10; ++var3) {
         sqr(var7, var8);
         sqr(var8, var7);
      }

      mul(var7, var8, var5);

      for(var3 = 0; var3 < 5; ++var3) {
         sqr(var5, var7);
         sqr(var7, var5);
      }

      mul(var5, var7, var6);
      sqr(var6, var5);
      sqr(var7, var6);

      for(var3 = 1; var3 < 25; ++var3) {
         sqr(var6, var7);
         sqr(var7, var6);
      }

      mul(var6, var7, var5);
      sqr(var7, var6);
      sqr(var8, var7);

      for(var3 = 1; var3 < 50; ++var3) {
         sqr(var7, var8);
         sqr(var8, var7);
      }

      mul(var7, var8, var6);

      for(var3 = 0; var3 < 25; ++var3) {
         sqr(var8, var7);
         sqr(var7, var8);
      }

      mul(var6, var7, var5);
      sqr(var5, var6);
      sqr(var6, var5);
      if (var2 != 0) {
         mul(var0, var1, var6);
      } else {
         sqr(var5, var6);
         sqr(var6, var5);
         sqr(var5, var6);
         mul(var0, var5, var4);
      }
   }

   private static void set(Curve25519.long10 var0, int var1) {
      var0.field_222 = (long)var1;
      var0.field_223 = 0L;
      var0.field_224 = 0L;
      var0.field_225 = 0L;
      var0.field_226 = 0L;
      var0.field_227 = 0L;
      var0.field_228 = 0L;
      var0.field_229 = 0L;
      var0.field_230 = 0L;
      var0.field_231 = 0L;
   }

   public static boolean sign(byte[] var0, byte[] var1, byte[] var2, byte[] var3) {
      byte[] var8 = new byte[65];
      byte[] var9 = new byte[33];
      int var5 = 0;

      while(true) {
         boolean var7 = false;
         if (var5 >= 32) {
            mula_small(var0, var2, 0, var1, 32, -1);
            mula_small(var0, var0, 0, ORDER, 32, (15 - var0[31]) / 16);
            mula32(var8, var0, var3, 32, 1);
            divmod(var9, var8, 64, ORDER, 32);
            int var6 = 0;

            for(var5 = 0; var5 < 32; ++var5) {
               byte var4 = var8[var5];
               var0[var5] = var4;
               var6 |= var4;
            }

            if (var6 != 0) {
               var7 = true;
            }

            return var7;
         }

         var0[var5] = 0;
         ++var5;
      }
   }

   private static Curve25519.long10 sqr(Curve25519.long10 var0, Curve25519.long10 var1) {
      long var2 = var1.field_222;
      long var4 = var1.field_223;
      long var6 = var1.field_224;
      long var8 = var1.field_225;
      long var10 = var1.field_226;
      long var12 = var1.field_227;
      long var14 = var1.field_228;
      long var16 = var1.field_229;
      long var18 = var1.field_230;
      long var20 = var1.field_231;
      long var22 = var10 * var10 + (var2 * var18 + var6 * var14) * 2L + var20 * var20 * 38L + (var4 * var16 + var8 * var12) * 4L;
      var0.field_230 = var22 & 67108863L;
      var22 = (var22 >> 26) + (var2 * var20 + var4 * var18 + var6 * var16 + var8 * var14 + var10 * var12) * 2L;
      var0.field_231 = var22 & 33554431L;
      var22 = (var22 >> 25) * 19L + var2 * var2 + (var6 * var18 + var10 * var14 + var12 * var12) * 38L + (var4 * var20 + var8 * var16) * 76L;
      var0.field_222 = var22 & 67108863L;
      var22 = (var22 >> 26) + var2 * var4 * 2L + (var6 * var20 + var8 * var18 + var10 * var16 + var12 * var14) * 38L;
      var0.field_223 = var22 & 33554431L;
      var22 = (var22 >> 25) + var14 * var14 * 19L + (var2 * var6 + var4 * var4) * 2L + var10 * var18 * 38L + (var8 * var20 + var12 * var16) * 76L;
      var0.field_224 = var22 & 67108863L;
      var22 = (var22 >> 26) + (var2 * var8 + var4 * var6) * 2L + (var10 * var20 + var12 * var18 + var14 * var16) * 38L;
      var0.field_225 = var22 & 33554431L;
      var22 = (var22 >> 25) + var6 * var6 + var2 * var10 * 2L + (var14 * var18 + var16 * var16) * 38L + var4 * var8 * 4L + var12 * var20 * 76L;
      var0.field_226 = var22 & 67108863L;
      var22 = (var22 >> 26) + (var2 * var12 + var4 * var10 + var6 * var8) * 2L + (var14 * var20 + var16 * var18) * 38L;
      var0.field_227 = var22 & 33554431L;
      var22 = (var22 >> 25) + var18 * var18 * 19L + (var2 * var14 + var6 * var10 + var8 * var8) * 2L + var4 * var12 * 4L + var16 * var20 * 76L;
      var0.field_228 = var22 & 67108863L;
      var2 = (var22 >> 26) + (var2 * var16 + var4 * var14 + var6 * var12 + var8 * var10) * 2L + var18 * var20 * 38L;
      var0.field_229 = var2 & 33554431L;
      var2 = (var2 >> 25) + var0.field_230;
      var0.field_230 = var2 & 67108863L;
      var0.field_231 += var2 >> 26;
      return var0;
   }

   private static void sqrt(Curve25519.long10 var0, Curve25519.long10 var1) {
      Curve25519.long10 var2 = new Curve25519.long10();
      Curve25519.long10 var3 = new Curve25519.long10();
      Curve25519.long10 var4 = new Curve25519.long10();
      add(var3, var1, var1);
      recip(var2, var3, 1);
      sqr(var0, var2);
      mul(var4, var3, var0);
      --var4.field_222;
      mul(var3, var2, var4);
      mul(var0, var1, var3);
   }

   private static void sub(Curve25519.long10 var0, Curve25519.long10 var1, Curve25519.long10 var2) {
      var0.field_222 = var1.field_222 - var2.field_222;
      var0.field_223 = var1.field_223 - var2.field_223;
      var0.field_224 = var1.field_224 - var2.field_224;
      var0.field_225 = var1.field_225 - var2.field_225;
      var0.field_226 = var1.field_226 - var2.field_226;
      var0.field_227 = var1.field_227 - var2.field_227;
      var0.field_228 = var1.field_228 - var2.field_228;
      var0.field_229 = var1.field_229 - var2.field_229;
      var0.field_230 = var1.field_230 - var2.field_230;
      var0.field_231 = var1.field_231 - var2.field_231;
   }

   private static void unpack(Curve25519.long10 var0, byte[] var1) {
      var0.field_222 = (long)(var1[0] & 255 | (var1[1] & 255) << 8 | (var1[2] & 255) << 16 | (var1[3] & 255 & 3) << 24);
      var0.field_223 = (long)((var1[3] & 255 & -4) >> 2 | (var1[4] & 255) << 6 | (var1[5] & 255) << 14 | (var1[6] & 255 & 7) << 22);
      var0.field_224 = (long)((var1[6] & 255 & -8) >> 3 | (var1[7] & 255) << 5 | (var1[8] & 255) << 13 | (var1[9] & 255 & 31) << 21);
      var0.field_225 = (long)((var1[9] & 255 & -32) >> 5 | (var1[10] & 255) << 3 | (var1[11] & 255) << 11 | (var1[12] & 255 & 63) << 19);
      var0.field_226 = (long)((var1[12] & 255 & -64) >> 6 | (var1[13] & 255) << 2 | (var1[14] & 255) << 10 | (var1[15] & 255) << 18);
      var0.field_227 = (long)(var1[16] & 255 | (var1[17] & 255) << 8 | (var1[18] & 255) << 16 | (var1[19] & 255 & 1) << 24);
      var0.field_228 = (long)((var1[19] & 255 & -2) >> 1 | (var1[20] & 255) << 7 | (var1[21] & 255) << 15 | (7 & var1[22] & 255) << 23);
      var0.field_229 = (long)((var1[22] & 255 & -8) >> 3 | (var1[23] & 255) << 5 | (var1[24] & 255) << 13 | (var1[25] & 255 & 15) << 21);
      var0.field_230 = (long)((var1[25] & 255 & -16) >> 4 | (var1[26] & 255) << 4 | (var1[27] & 255) << 12 | (var1[28] & 255 & 63) << 20);
      var0.field_231 = (long)((var1[28] & 255 & -64) >> 6 | (var1[29] & 255) << 2 | (var1[30] & 255) << 10 | (var1[31] & 255) << 18);
   }

   public static void verify(byte[] var0, byte[] var1, byte[] var2, byte[] var3) {
      byte[] var10 = new byte[32];
      Curve25519.long10[] var11 = new Curve25519.long10[]{new Curve25519.long10(), new Curve25519.long10()};
      Curve25519.long10[] var12 = new Curve25519.long10[]{new Curve25519.long10(), new Curve25519.long10()};
      Curve25519.long10[] var13 = new Curve25519.long10[]{new Curve25519.long10(), new Curve25519.long10(), new Curve25519.long10()};
      Curve25519.long10[] var14 = new Curve25519.long10[]{new Curve25519.long10(), new Curve25519.long10(), new Curve25519.long10()};
      Curve25519.long10[] var15 = new Curve25519.long10[]{new Curve25519.long10(), new Curve25519.long10(), new Curve25519.long10()};
      Curve25519.long10[] var16 = new Curve25519.long10[]{new Curve25519.long10(), new Curve25519.long10(), new Curve25519.long10()};
      int var7 = 0;
      int var6 = 0;
      int var5 = 0;
      int var8 = 0;
      set(var11[0], 9);
      unpack(var11[1], var3);
      x_to_y2(var15[0], var16[0], var11[1]);
      sqrt(var15[0], var16[0]);
      int var4 = is_negative(var15[0]);
      Curve25519.long10 var17 = var16[0];
      var17.field_222 += 39420360L;
      mul(var16[1], BASE_2Y, var15[0]);
      sub(var15[var4], var16[0], var16[1]);
      add(var15[1 - var4], var16[0], var16[1]);
      cpy(var16[0], var11[1]);
      var17 = var16[0];
      var17.field_222 -= 9L;
      sqr(var16[1], var16[0]);
      recip(var16[0], var16[1], 0);
      mul(var12[0], var15[0], var16[0]);
      sub(var12[0], var12[0], var11[1]);
      var17 = var12[0];
      var17.field_222 -= 486671L;
      mul(var12[1], var15[1], var16[0]);
      sub(var12[1], var12[1], var11[1]);
      var17 = var12[1];
      var17.field_222 -= 486671L;
      mul_small(var12[0], var12[0], 1L);
      mul_small(var12[1], var12[1], 1L);

      for(var4 = 0; var4 < 32; ++var4) {
         var7 = var7 >> 8 ^ var1[var4] & 255 ^ (var1[var4] & 255) << 1;
         var6 = var6 >> 8 ^ var2[var4] & 255 ^ (var2[var4] & 255) << 1;
         var8 = var7 ^ var6;
         var5 = (var5 & 128) >> 7 & var8 ^ var7;
         var5 ^= (var5 & 1) << 1 & var8;
         var5 ^= (var5 & 2) << 1 & var8;
         var5 ^= (var5 & 4) << 1 & var8;
         var5 ^= (var5 & 8) << 1 & var8;
         var5 ^= (var5 & 16) << 1 & var8;
         var5 ^= (var5 & 32) << 1 & var8;
         var5 ^= (var5 & 64) << 1 & var8;
         var10[var4] = (byte)var5;
      }

      var6 = ((var5 & 128) << 1 & var8 ^ var7) >> 8;
      set(var13[0], 1);
      cpy(var13[1], var11[var6]);
      cpy(var13[2], var12[0]);
      set(var14[0], 0);
      set(var14[1], 1);
      set(var14[2], 1);
      var7 = 0;
      var4 = 0;
      var5 = 32;

      while(true) {
         int var9 = var5 - 1;
         if (var5 == 0) {
            var4 = (var7 & 1) + (var4 & 1);
            recip(var15[0], var14[var4], 0);
            mul(var15[1], var13[var4], var15[0]);
            pack(var15[1], var0);
            return;
         }

         var7 = var7 << 8 | var1[var9] & 255;
         var8 = var4 << 8 | var2[var9] & 255;
         var6 = var6 << 8 | var10[var9] & 255;
         var5 = 8;
         var4 = var9;

         while(true) {
            var9 = var5 - 1;
            if (var5 == 0) {
               var5 = var4;
               var4 = var8;
               break;
            }

            mont_prep(var15[0], var16[0], var13[0], var14[0]);
            mont_prep(var15[1], var16[1], var13[1], var14[1]);
            mont_prep(var15[2], var16[2], var13[2], var14[2]);
            var5 = ((var7 >> 1 ^ var7) >> var9 & 1) + ((var8 >> 1 ^ var8) >> var9 & 1);
            mont_dbl(var13[2], var14[2], var15[var5], var16[var5], var13[0], var14[0]);
            var5 = var6 >> var9 & 2 ^ (var6 >> var9 & 1) << 1;
            mont_add(var15[1], var16[1], var15[var5], var16[var5], var13[1], var14[1], var11[var6 >> var9 & 1]);
            mont_add(var15[2], var16[2], var15[0], var16[0], var13[2], var14[2], var12[((var7 ^ var8) >> var9 & 2) >> 1]);
            var5 = var9;
         }
      }
   }

   private static void x_to_y2(Curve25519.long10 var0, Curve25519.long10 var1, Curve25519.long10 var2) {
      sqr(var0, var2);
      mul_small(var1, var2, 486662L);
      add(var0, var0, var1);
      ++var0.field_222;
      mul(var1, var0, var2);
   }

   private static final class long10 {
      // $FF: renamed from: _0 long
      public long field_222;
      // $FF: renamed from: _1 long
      public long field_223;
      // $FF: renamed from: _2 long
      public long field_224;
      // $FF: renamed from: _3 long
      public long field_225;
      // $FF: renamed from: _4 long
      public long field_226;
      // $FF: renamed from: _5 long
      public long field_227;
      // $FF: renamed from: _6 long
      public long field_228;
      // $FF: renamed from: _7 long
      public long field_229;
      // $FF: renamed from: _8 long
      public long field_230;
      // $FF: renamed from: _9 long
      public long field_231;

      public long10() {
      }

      public long10(long var1, long var3, long var5, long var7, long var9, long var11, long var13, long var15, long var17, long var19) {
         this.field_222 = var1;
         this.field_223 = var3;
         this.field_224 = var5;
         this.field_225 = var7;
         this.field_226 = var9;
         this.field_227 = var11;
         this.field_228 = var13;
         this.field_229 = var15;
         this.field_230 = var17;
         this.field_231 = var19;
      }
   }
}
