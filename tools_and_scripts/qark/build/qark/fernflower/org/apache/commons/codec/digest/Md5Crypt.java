package org.apache.commons.codec.digest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Md5Crypt {
   static final String APR1_PREFIX = "$apr1$";
   private static final int BLOCKSIZE = 16;
   static final String MD5_PREFIX = "$1$";
   private static final int ROUNDS = 1000;

   public static String apr1Crypt(String var0) {
      return apr1Crypt(var0.getBytes(StandardCharsets.UTF_8));
   }

   public static String apr1Crypt(String var0, String var1) {
      return apr1Crypt(var0.getBytes(StandardCharsets.UTF_8), var1);
   }

   public static String apr1Crypt(byte[] var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("$apr1$");
      var1.append(B64.getRandomSalt(8));
      return apr1Crypt(var0, var1.toString());
   }

   public static String apr1Crypt(byte[] var0, String var1) {
      String var2 = var1;
      if (var1 != null) {
         var2 = var1;
         if (!var1.startsWith("$apr1$")) {
            StringBuilder var3 = new StringBuilder();
            var3.append("$apr1$");
            var3.append(var1);
            var2 = var3.toString();
         }
      }

      return md5Crypt(var0, var2, "$apr1$");
   }

   public static String apr1Crypt(byte[] var0, Random var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("$apr1$");
      var2.append(B64.getRandomSalt(8, var1));
      return apr1Crypt(var0, var2.toString());
   }

   public static String md5Crypt(byte[] var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("$1$");
      var1.append(B64.getRandomSalt(8));
      return md5Crypt(var0, var1.toString());
   }

   public static String md5Crypt(byte[] var0, String var1) {
      return md5Crypt(var0, var1, "$1$");
   }

   public static String md5Crypt(byte[] var0, String var1, String var2) {
      return md5Crypt(var0, var1, var2, new SecureRandom());
   }

   public static String md5Crypt(byte[] var0, String var1, String var2, Random var3) {
      int var5 = var0.length;
      if (var1 == null) {
         var1 = B64.getRandomSalt(8, var3);
      } else {
         StringBuilder var13 = new StringBuilder();
         var13.append("^");
         var13.append(var2.replace("$", "\\$"));
         var13.append("([\\.\\/a-zA-Z0-9]{1,8}).*");
         Matcher var14 = Pattern.compile(var13.toString()).matcher(var1);
         if (!var14.find()) {
            StringBuilder var10 = new StringBuilder();
            var10.append("Invalid salt value: ");
            var10.append(var1);
            throw new IllegalArgumentException(var10.toString());
         }

         var1 = var14.group(1);
      }

      byte[] var7 = var1.getBytes(StandardCharsets.UTF_8);
      MessageDigest var8 = DigestUtils.getMd5Digest();
      var8.update(var0);
      var8.update(var2.getBytes(StandardCharsets.UTF_8));
      var8.update(var7);
      MessageDigest var15 = DigestUtils.getMd5Digest();
      var15.update(var0);
      var15.update(var7);
      var15.update(var0);
      byte[] var9 = var15.digest();
      int var4 = var5;

      while(true) {
         int var6 = 16;
         if (var4 <= 0) {
            Arrays.fill(var9, (byte)0);

            for(; var5 > 0; var5 >>= 1) {
               if ((var5 & 1) == 1) {
                  var8.update(var9[0]);
               } else {
                  var8.update(var0[0]);
               }
            }

            StringBuilder var16 = new StringBuilder();
            var16.append(var2);
            var16.append(var1);
            var16.append("$");
            var16 = new StringBuilder(var16.toString());
            byte[] var11 = var8.digest();
            var4 = 0;

            MessageDigest var12;
            for(var12 = var15; var4 < 1000; ++var4) {
               var12 = DigestUtils.getMd5Digest();
               if ((var4 & 1) != 0) {
                  var12.update(var0);
               } else {
                  var12.update(var11, 0, 16);
               }

               if (var4 % 3 != 0) {
                  var12.update(var7);
               }

               if (var4 % 7 != 0) {
                  var12.update(var0);
               }

               if ((var4 & 1) != 0) {
                  var12.update(var11, 0, 16);
               } else {
                  var12.update(var0);
               }

               var11 = var12.digest();
            }

            B64.b64from24bit(var11[0], var11[6], var11[12], 4, var16);
            B64.b64from24bit(var11[1], var11[7], var11[13], 4, var16);
            B64.b64from24bit(var11[2], var11[8], var11[14], 4, var16);
            B64.b64from24bit(var11[3], var11[9], var11[15], 4, var16);
            B64.b64from24bit(var11[4], var11[10], var11[5], 4, var16);
            B64.b64from24bit((byte)0, (byte)0, var11[11], 2, var16);
            var8.reset();
            var12.reset();
            Arrays.fill(var0, (byte)0);
            Arrays.fill(var7, (byte)0);
            Arrays.fill(var11, (byte)0);
            return var16.toString();
         }

         if (var4 <= 16) {
            var6 = var4;
         }

         var8.update(var9, 0, var6);
         var4 -= 16;
      }
   }

   public static String md5Crypt(byte[] var0, Random var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("$1$");
      var2.append(B64.getRandomSalt(8, var1));
      return md5Crypt(var0, var2.toString());
   }
}
