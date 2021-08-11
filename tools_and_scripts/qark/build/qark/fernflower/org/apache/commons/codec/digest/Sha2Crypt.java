package org.apache.commons.codec.digest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sha2Crypt {
   private static final int ROUNDS_DEFAULT = 5000;
   private static final int ROUNDS_MAX = 999999999;
   private static final int ROUNDS_MIN = 1000;
   private static final String ROUNDS_PREFIX = "rounds=";
   private static final Pattern SALT_PATTERN = Pattern.compile("^\\$([56])\\$(rounds=(\\d+)\\$)?([\\.\\/a-zA-Z0-9]{1,16}).*");
   private static final int SHA256_BLOCKSIZE = 32;
   static final String SHA256_PREFIX = "$5$";
   private static final int SHA512_BLOCKSIZE = 64;
   static final String SHA512_PREFIX = "$6$";

   public static String sha256Crypt(byte[] var0) {
      return sha256Crypt(var0, (String)null);
   }

   public static String sha256Crypt(byte[] var0, String var1) {
      String var2 = var1;
      if (var1 == null) {
         StringBuilder var3 = new StringBuilder();
         var3.append("$5$");
         var3.append(B64.getRandomSalt(8));
         var2 = var3.toString();
      }

      return sha2Crypt(var0, var2, "$5$", 32, "SHA-256");
   }

   public static String sha256Crypt(byte[] var0, String var1, Random var2) {
      String var3 = var1;
      if (var1 == null) {
         StringBuilder var4 = new StringBuilder();
         var4.append("$5$");
         var4.append(B64.getRandomSalt(8, var2));
         var3 = var4.toString();
      }

      return sha2Crypt(var0, var3, "$5$", 32, "SHA-256");
   }

   private static String sha2Crypt(byte[] var0, String var1, String var2, int var3, String var4) {
      int var9 = var0.length;
      int var6 = 5000;
      boolean var7 = false;
      if (var1 == null) {
         throw new IllegalArgumentException("Salt must not be null");
      } else {
         Matcher var11 = SALT_PATTERN.matcher(var1);
         if (!var11.find()) {
            StringBuilder var19 = new StringBuilder();
            var19.append("Invalid salt value: ");
            var19.append(var1);
            throw new IllegalArgumentException(var19.toString());
         } else {
            if (var11.group(3) != null) {
               var6 = Math.max(1000, Math.min(999999999, Integer.parseInt(var11.group(3))));
               var7 = true;
            }

            String var13 = var11.group(4);
            byte[] var14 = var13.getBytes(StandardCharsets.UTF_8);
            int var10 = var14.length;
            MessageDigest var20 = DigestUtils.getDigest(var4);
            var20.update(var0);
            var20.update(var14);
            MessageDigest var23 = DigestUtils.getDigest(var4);
            var23.update(var0);
            var23.update(var14);
            var23.update(var0);
            byte[] var24 = var23.digest();

            int var5;
            for(var5 = var0.length; var5 > var3; var5 -= var3) {
               var20.update(var24, 0, var3);
            }

            var20.update(var24, 0, var5);

            for(var5 = var0.length; var5 > 0; var5 >>= 1) {
               if ((var5 & 1) != 0) {
                  var20.update(var24, 0, var3);
               } else {
                  var20.update(var0);
               }
            }

            byte[] var12 = var20.digest();
            var23 = DigestUtils.getDigest(var4);

            for(var5 = 1; var5 <= var9; ++var5) {
               var23.update(var0);
            }

            var24 = var23.digest();
            byte[] var15 = new byte[var9];

            for(var5 = 0; var5 < var9 - var3; var5 += var3) {
               System.arraycopy(var24, 0, var15, var5, var3);
            }

            System.arraycopy(var24, 0, var15, var5, var9 - var5);
            MessageDigest var16 = DigestUtils.getDigest(var4);

            for(var5 = 1; var5 <= (var12[0] & 255) + 16; ++var5) {
               var16.update(var14);
            }

            byte[] var17 = var16.digest();
            byte[] var18 = new byte[var10];

            for(var5 = 0; var5 < var10 - var3; var5 += var3) {
               System.arraycopy(var17, 0, var18, var5, var3);
            }

            System.arraycopy(var17, 0, var18, var5, var10 - var5);
            int var8 = 0;
            var23 = var20;

            byte[] var21;
            for(var21 = var12; var8 <= var6 - 1; ++var8) {
               var23 = DigestUtils.getDigest(var4);
               if ((var8 & 1) != 0) {
                  var23.update(var15, 0, var9);
               } else {
                  var23.update(var21, 0, var3);
               }

               if (var8 % 3 != 0) {
                  var23.update(var18, 0, var10);
               }

               if (var8 % 7 != 0) {
                  var23.update(var15, 0, var9);
               }

               if ((var8 & 1) != 0) {
                  var23.update(var21, 0, var3);
               } else {
                  var23.update(var15, 0, var9);
               }

               var21 = var23.digest();
            }

            StringBuilder var22 = new StringBuilder(var2);
            if (var7) {
               var22.append("rounds=");
               var22.append(var6);
               var22.append("$");
            }

            var22.append(var13);
            var22.append("$");
            if (var3 == 32) {
               B64.b64from24bit(var21[0], var21[10], var21[20], 4, var22);
               B64.b64from24bit(var21[21], var21[1], var21[11], 4, var22);
               B64.b64from24bit(var21[12], var21[22], var21[2], 4, var22);
               B64.b64from24bit(var21[3], var21[13], var21[23], 4, var22);
               B64.b64from24bit(var21[24], var21[4], var21[14], 4, var22);
               B64.b64from24bit(var21[15], var21[25], var21[5], 4, var22);
               B64.b64from24bit(var21[6], var21[16], var21[26], 4, var22);
               B64.b64from24bit(var21[27], var21[7], var21[17], 4, var22);
               B64.b64from24bit(var21[18], var21[28], var21[8], 4, var22);
               B64.b64from24bit(var21[9], var21[19], var21[29], 4, var22);
               B64.b64from24bit((byte)0, var21[31], var21[30], 3, var22);
            } else {
               B64.b64from24bit(var21[0], var21[21], var21[42], 4, var22);
               B64.b64from24bit(var21[22], var21[43], var21[1], 4, var22);
               B64.b64from24bit(var21[44], var21[2], var21[23], 4, var22);
               B64.b64from24bit(var21[3], var21[24], var21[45], 4, var22);
               B64.b64from24bit(var21[25], var21[46], var21[4], 4, var22);
               B64.b64from24bit(var21[47], var21[5], var21[26], 4, var22);
               B64.b64from24bit(var21[6], var21[27], var21[48], 4, var22);
               B64.b64from24bit(var21[28], var21[49], var21[7], 4, var22);
               B64.b64from24bit(var21[50], var21[8], var21[29], 4, var22);
               B64.b64from24bit(var21[9], var21[30], var21[51], 4, var22);
               B64.b64from24bit(var21[31], var21[52], var21[10], 4, var22);
               B64.b64from24bit(var21[53], var21[11], var21[32], 4, var22);
               B64.b64from24bit(var21[12], var21[33], var21[54], 4, var22);
               B64.b64from24bit(var21[34], var21[55], var21[13], 4, var22);
               B64.b64from24bit(var21[56], var21[14], var21[35], 4, var22);
               B64.b64from24bit(var21[15], var21[36], var21[57], 4, var22);
               B64.b64from24bit(var21[37], var21[58], var21[16], 4, var22);
               B64.b64from24bit(var21[59], var21[17], var21[38], 4, var22);
               B64.b64from24bit(var21[18], var21[39], var21[60], 4, var22);
               B64.b64from24bit(var21[40], var21[61], var21[19], 4, var22);
               B64.b64from24bit(var21[62], var21[20], var21[41], 4, var22);
               B64.b64from24bit((byte)0, (byte)0, var21[63], 2, var22);
            }

            Arrays.fill(var17, (byte)0);
            Arrays.fill(var15, (byte)0);
            Arrays.fill(var18, (byte)0);
            var23.reset();
            var16.reset();
            Arrays.fill(var0, (byte)0);
            Arrays.fill(var14, (byte)0);
            return var22.toString();
         }
      }
   }

   public static String sha512Crypt(byte[] var0) {
      return sha512Crypt(var0, (String)null);
   }

   public static String sha512Crypt(byte[] var0, String var1) {
      String var2 = var1;
      if (var1 == null) {
         StringBuilder var3 = new StringBuilder();
         var3.append("$6$");
         var3.append(B64.getRandomSalt(8));
         var2 = var3.toString();
      }

      return sha2Crypt(var0, var2, "$6$", 64, "SHA-512");
   }

   public static String sha512Crypt(byte[] var0, String var1, Random var2) {
      String var3 = var1;
      if (var1 == null) {
         StringBuilder var4 = new StringBuilder();
         var4.append("$6$");
         var4.append(B64.getRandomSalt(8, var2));
         var3 = var4.toString();
      }

      return sha2Crypt(var0, var3, "$6$", 64, "SHA-512");
   }
}
