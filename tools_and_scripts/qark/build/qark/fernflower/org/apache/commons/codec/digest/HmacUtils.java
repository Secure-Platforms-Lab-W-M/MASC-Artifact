package org.apache.commons.codec.digest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

public final class HmacUtils {
   private static final int STREAM_BUFFER_LENGTH = 1024;
   private final Mac mac;

   @Deprecated
   public HmacUtils() {
      this((Mac)null);
   }

   public HmacUtils(String var1, String var2) {
      this(var1, StringUtils.getBytesUtf8(var2));
   }

   public HmacUtils(String var1, byte[] var2) {
      this(getInitializedMac(var1, var2));
   }

   private HmacUtils(Mac var1) {
      this.mac = var1;
   }

   public HmacUtils(HmacAlgorithms var1, String var2) {
      this(var1.getName(), StringUtils.getBytesUtf8(var2));
   }

   public HmacUtils(HmacAlgorithms var1, byte[] var2) {
      this(var1.getName(), var2);
   }

   @Deprecated
   public static Mac getHmacMd5(byte[] var0) {
      return getInitializedMac(HmacAlgorithms.HMAC_MD5, var0);
   }

   @Deprecated
   public static Mac getHmacSha1(byte[] var0) {
      return getInitializedMac(HmacAlgorithms.HMAC_SHA_1, var0);
   }

   @Deprecated
   public static Mac getHmacSha256(byte[] var0) {
      return getInitializedMac(HmacAlgorithms.HMAC_SHA_256, var0);
   }

   @Deprecated
   public static Mac getHmacSha384(byte[] var0) {
      return getInitializedMac(HmacAlgorithms.HMAC_SHA_384, var0);
   }

   @Deprecated
   public static Mac getHmacSha512(byte[] var0) {
      return getInitializedMac(HmacAlgorithms.HMAC_SHA_512, var0);
   }

   public static Mac getInitializedMac(String var0, byte[] var1) {
      if (var1 != null) {
         try {
            SecretKeySpec var5 = new SecretKeySpec(var1, var0);
            Mac var4 = Mac.getInstance(var0);
            var4.init(var5);
            return var4;
         } catch (NoSuchAlgorithmException var2) {
            throw new IllegalArgumentException(var2);
         } catch (InvalidKeyException var3) {
            throw new IllegalArgumentException(var3);
         }
      } else {
         throw new IllegalArgumentException("Null key");
      }
   }

   public static Mac getInitializedMac(HmacAlgorithms var0, byte[] var1) {
      return getInitializedMac(var0.getName(), var1);
   }

   @Deprecated
   public static byte[] hmacMd5(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacMd5(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacMd5(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, var0)).hmac(var1);
   }

   @Deprecated
   public static String hmacMd5Hex(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacMd5Hex(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacMd5Hex(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, var0)).hmacHex(var1);
   }

   @Deprecated
   public static byte[] hmacSha1(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacSha1(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacSha1(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, var0)).hmac(var1);
   }

   @Deprecated
   public static String hmacSha1Hex(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacSha1Hex(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacSha1Hex(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, var0)).hmacHex(var1);
   }

   @Deprecated
   public static byte[] hmacSha256(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacSha256(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacSha256(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, var0)).hmac(var1);
   }

   @Deprecated
   public static String hmacSha256Hex(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacSha256Hex(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacSha256Hex(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, var0)).hmacHex(var1);
   }

   @Deprecated
   public static byte[] hmacSha384(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacSha384(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacSha384(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, var0)).hmac(var1);
   }

   @Deprecated
   public static String hmacSha384Hex(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacSha384Hex(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacSha384Hex(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, var0)).hmacHex(var1);
   }

   @Deprecated
   public static byte[] hmacSha512(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacSha512(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, var0)).hmac(var1);
   }

   @Deprecated
   public static byte[] hmacSha512(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, var0)).hmac(var1);
   }

   @Deprecated
   public static String hmacSha512Hex(String var0, String var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacSha512Hex(byte[] var0, InputStream var1) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, var0)).hmacHex(var1);
   }

   @Deprecated
   public static String hmacSha512Hex(byte[] var0, byte[] var1) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, var0)).hmacHex(var1);
   }

   public static boolean isAvailable(String var0) {
      try {
         Mac.getInstance(var0);
         return true;
      } catch (NoSuchAlgorithmException var1) {
         return false;
      }
   }

   public static boolean isAvailable(HmacAlgorithms var0) {
      try {
         Mac.getInstance(var0.getName());
         return true;
      } catch (NoSuchAlgorithmException var1) {
         return false;
      }
   }

   public static Mac updateHmac(Mac var0, InputStream var1) throws IOException {
      var0.reset();
      byte[] var3 = new byte[1024];

      for(int var2 = var1.read(var3, 0, 1024); var2 > -1; var2 = var1.read(var3, 0, 1024)) {
         var0.update(var3, 0, var2);
      }

      return var0;
   }

   public static Mac updateHmac(Mac var0, String var1) {
      var0.reset();
      var0.update(StringUtils.getBytesUtf8(var1));
      return var0;
   }

   public static Mac updateHmac(Mac var0, byte[] var1) {
      var0.reset();
      var0.update(var1);
      return var0;
   }

   public byte[] hmac(File var1) throws IOException {
      BufferedInputStream var16 = new BufferedInputStream(new FileInputStream(var1));

      byte[] var17;
      try {
         var17 = this.hmac((InputStream)var16);
      } catch (Throwable var15) {
         Throwable var2 = var15;

         try {
            throw var2;
         } finally {
            label88:
            try {
               var16.close();
            } catch (Throwable var13) {
               var15.addSuppressed(var13);
               break label88;
            }

         }
      }

      var16.close();
      return var17;
   }

   public byte[] hmac(InputStream var1) throws IOException {
      byte[] var3 = new byte[1024];

      while(true) {
         int var2 = var1.read(var3, 0, 1024);
         if (var2 <= -1) {
            return this.mac.doFinal();
         }

         this.mac.update(var3, 0, var2);
      }
   }

   public byte[] hmac(String var1) {
      return this.mac.doFinal(StringUtils.getBytesUtf8(var1));
   }

   public byte[] hmac(ByteBuffer var1) {
      this.mac.update(var1);
      return this.mac.doFinal();
   }

   public byte[] hmac(byte[] var1) {
      return this.mac.doFinal(var1);
   }

   public String hmacHex(File var1) throws IOException {
      return Hex.encodeHexString(this.hmac(var1));
   }

   public String hmacHex(InputStream var1) throws IOException {
      return Hex.encodeHexString(this.hmac(var1));
   }

   public String hmacHex(String var1) {
      return Hex.encodeHexString(this.hmac(var1));
   }

   public String hmacHex(ByteBuffer var1) {
      return Hex.encodeHexString(this.hmac(var1));
   }

   public String hmacHex(byte[] var1) {
      return Hex.encodeHexString(this.hmac(var1));
   }
}
