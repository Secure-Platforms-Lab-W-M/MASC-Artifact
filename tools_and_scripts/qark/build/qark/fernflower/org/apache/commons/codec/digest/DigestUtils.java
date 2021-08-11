package org.apache.commons.codec.digest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

public class DigestUtils {
   private static final int STREAM_BUFFER_LENGTH = 1024;
   private final MessageDigest messageDigest;

   @Deprecated
   public DigestUtils() {
      this.messageDigest = null;
   }

   public DigestUtils(String var1) {
      this(getDigest(var1));
   }

   public DigestUtils(MessageDigest var1) {
      this.messageDigest = var1;
   }

   public static byte[] digest(MessageDigest var0, File var1) throws IOException {
      return updateDigest(var0, var1).digest();
   }

   public static byte[] digest(MessageDigest var0, InputStream var1) throws IOException {
      return updateDigest(var0, var1).digest();
   }

   public static byte[] digest(MessageDigest var0, RandomAccessFile var1) throws IOException {
      return updateDigest(var0, var1).digest();
   }

   public static byte[] digest(MessageDigest var0, ByteBuffer var1) {
      var0.update(var1);
      return var0.digest();
   }

   public static byte[] digest(MessageDigest var0, Path var1, OpenOption... var2) throws IOException {
      return updateDigest(var0, var1, var2).digest();
   }

   public static byte[] digest(MessageDigest var0, byte[] var1) {
      return var0.digest(var1);
   }

   public static MessageDigest getDigest(String var0) {
      try {
         MessageDigest var2 = MessageDigest.getInstance(var0);
         return var2;
      } catch (NoSuchAlgorithmException var1) {
         throw new IllegalArgumentException(var1);
      }
   }

   public static MessageDigest getDigest(String var0, MessageDigest var1) {
      try {
         MessageDigest var3 = MessageDigest.getInstance(var0);
         return var3;
      } catch (Exception var2) {
         return var1;
      }
   }

   public static MessageDigest getMd2Digest() {
      return getDigest("MD2");
   }

   public static MessageDigest getMd5Digest() {
      return getDigest("MD5");
   }

   public static MessageDigest getSha1Digest() {
      return getDigest("SHA-1");
   }

   public static MessageDigest getSha256Digest() {
      return getDigest("SHA-256");
   }

   public static MessageDigest getSha384Digest() {
      return getDigest("SHA-384");
   }

   public static MessageDigest getSha3_224Digest() {
      return getDigest("SHA3-224");
   }

   public static MessageDigest getSha3_256Digest() {
      return getDigest("SHA3-256");
   }

   public static MessageDigest getSha3_384Digest() {
      return getDigest("SHA3-384");
   }

   public static MessageDigest getSha3_512Digest() {
      return getDigest("SHA3-512");
   }

   public static MessageDigest getSha512Digest() {
      return getDigest("SHA-512");
   }

   public static MessageDigest getSha512_224Digest() {
      return getDigest("SHA-512/224");
   }

   public static MessageDigest getSha512_256Digest() {
      return getDigest("SHA-512/256");
   }

   @Deprecated
   public static MessageDigest getShaDigest() {
      return getSha1Digest();
   }

   public static boolean isAvailable(String var0) {
      return getDigest(var0, (MessageDigest)null) != null;
   }

   public static byte[] md2(InputStream var0) throws IOException {
      return digest(getMd2Digest(), var0);
   }

   public static byte[] md2(String var0) {
      return md2(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] md2(byte[] var0) {
      return getMd2Digest().digest(var0);
   }

   public static String md2Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(md2(var0));
   }

   public static String md2Hex(String var0) {
      return Hex.encodeHexString(md2(var0));
   }

   public static String md2Hex(byte[] var0) {
      return Hex.encodeHexString(md2(var0));
   }

   public static byte[] md5(InputStream var0) throws IOException {
      return digest(getMd5Digest(), var0);
   }

   public static byte[] md5(String var0) {
      return md5(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] md5(byte[] var0) {
      return getMd5Digest().digest(var0);
   }

   public static String md5Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(md5(var0));
   }

   public static String md5Hex(String var0) {
      return Hex.encodeHexString(md5(var0));
   }

   public static String md5Hex(byte[] var0) {
      return Hex.encodeHexString(md5(var0));
   }

   @Deprecated
   public static byte[] sha(InputStream var0) throws IOException {
      return sha1(var0);
   }

   @Deprecated
   public static byte[] sha(String var0) {
      return sha1(var0);
   }

   @Deprecated
   public static byte[] sha(byte[] var0) {
      return sha1(var0);
   }

   public static byte[] sha1(InputStream var0) throws IOException {
      return digest(getSha1Digest(), var0);
   }

   public static byte[] sha1(String var0) {
      return sha1(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha1(byte[] var0) {
      return getSha1Digest().digest(var0);
   }

   public static String sha1Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha1(var0));
   }

   public static String sha1Hex(String var0) {
      return Hex.encodeHexString(sha1(var0));
   }

   public static String sha1Hex(byte[] var0) {
      return Hex.encodeHexString(sha1(var0));
   }

   public static byte[] sha256(InputStream var0) throws IOException {
      return digest(getSha256Digest(), var0);
   }

   public static byte[] sha256(String var0) {
      return sha256(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha256(byte[] var0) {
      return getSha256Digest().digest(var0);
   }

   public static String sha256Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha256(var0));
   }

   public static String sha256Hex(String var0) {
      return Hex.encodeHexString(sha256(var0));
   }

   public static String sha256Hex(byte[] var0) {
      return Hex.encodeHexString(sha256(var0));
   }

   public static byte[] sha384(InputStream var0) throws IOException {
      return digest(getSha384Digest(), var0);
   }

   public static byte[] sha384(String var0) {
      return sha384(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha384(byte[] var0) {
      return getSha384Digest().digest(var0);
   }

   public static String sha384Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha384(var0));
   }

   public static String sha384Hex(String var0) {
      return Hex.encodeHexString(sha384(var0));
   }

   public static String sha384Hex(byte[] var0) {
      return Hex.encodeHexString(sha384(var0));
   }

   public static byte[] sha3_224(InputStream var0) throws IOException {
      return digest(getSha3_224Digest(), var0);
   }

   public static byte[] sha3_224(String var0) {
      return sha3_224(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha3_224(byte[] var0) {
      return getSha3_224Digest().digest(var0);
   }

   public static String sha3_224Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha3_224(var0));
   }

   public static String sha3_224Hex(String var0) {
      return Hex.encodeHexString(sha3_224(var0));
   }

   public static String sha3_224Hex(byte[] var0) {
      return Hex.encodeHexString(sha3_224(var0));
   }

   public static byte[] sha3_256(InputStream var0) throws IOException {
      return digest(getSha3_256Digest(), var0);
   }

   public static byte[] sha3_256(String var0) {
      return sha3_256(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha3_256(byte[] var0) {
      return getSha3_256Digest().digest(var0);
   }

   public static String sha3_256Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha3_256(var0));
   }

   public static String sha3_256Hex(String var0) {
      return Hex.encodeHexString(sha3_256(var0));
   }

   public static String sha3_256Hex(byte[] var0) {
      return Hex.encodeHexString(sha3_256(var0));
   }

   public static byte[] sha3_384(InputStream var0) throws IOException {
      return digest(getSha3_384Digest(), var0);
   }

   public static byte[] sha3_384(String var0) {
      return sha3_384(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha3_384(byte[] var0) {
      return getSha3_384Digest().digest(var0);
   }

   public static String sha3_384Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha3_384(var0));
   }

   public static String sha3_384Hex(String var0) {
      return Hex.encodeHexString(sha3_384(var0));
   }

   public static String sha3_384Hex(byte[] var0) {
      return Hex.encodeHexString(sha3_384(var0));
   }

   public static byte[] sha3_512(InputStream var0) throws IOException {
      return digest(getSha3_512Digest(), var0);
   }

   public static byte[] sha3_512(String var0) {
      return sha3_512(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha3_512(byte[] var0) {
      return getSha3_512Digest().digest(var0);
   }

   public static String sha3_512Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha3_512(var0));
   }

   public static String sha3_512Hex(String var0) {
      return Hex.encodeHexString(sha3_512(var0));
   }

   public static String sha3_512Hex(byte[] var0) {
      return Hex.encodeHexString(sha3_512(var0));
   }

   public static byte[] sha512(InputStream var0) throws IOException {
      return digest(getSha512Digest(), var0);
   }

   public static byte[] sha512(String var0) {
      return sha512(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha512(byte[] var0) {
      return getSha512Digest().digest(var0);
   }

   public static String sha512Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha512(var0));
   }

   public static String sha512Hex(String var0) {
      return Hex.encodeHexString(sha512(var0));
   }

   public static String sha512Hex(byte[] var0) {
      return Hex.encodeHexString(sha512(var0));
   }

   public static byte[] sha512_224(InputStream var0) throws IOException {
      return digest(getSha512_224Digest(), var0);
   }

   public static byte[] sha512_224(String var0) {
      return sha512_224(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha512_224(byte[] var0) {
      return getSha512_224Digest().digest(var0);
   }

   public static String sha512_224Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha512_224(var0));
   }

   public static String sha512_224Hex(String var0) {
      return Hex.encodeHexString(sha512_224(var0));
   }

   public static String sha512_224Hex(byte[] var0) {
      return Hex.encodeHexString(sha512_224(var0));
   }

   public static byte[] sha512_256(InputStream var0) throws IOException {
      return digest(getSha512_256Digest(), var0);
   }

   public static byte[] sha512_256(String var0) {
      return sha512_256(StringUtils.getBytesUtf8(var0));
   }

   public static byte[] sha512_256(byte[] var0) {
      return getSha512_256Digest().digest(var0);
   }

   public static String sha512_256Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha512_256(var0));
   }

   public static String sha512_256Hex(String var0) {
      return Hex.encodeHexString(sha512_256(var0));
   }

   public static String sha512_256Hex(byte[] var0) {
      return Hex.encodeHexString(sha512_256(var0));
   }

   @Deprecated
   public static String shaHex(InputStream var0) throws IOException {
      return sha1Hex(var0);
   }

   @Deprecated
   public static String shaHex(String var0) {
      return sha1Hex(var0);
   }

   @Deprecated
   public static String shaHex(byte[] var0) {
      return sha1Hex(var0);
   }

   public static MessageDigest updateDigest(MessageDigest var0, File var1) throws IOException {
      BufferedInputStream var16 = new BufferedInputStream(new FileInputStream(var1));

      try {
         var0 = updateDigest(var0, (InputStream)var16);
      } catch (Throwable var14) {
         Throwable var15 = var14;

         try {
            throw var15;
         } finally {
            label88:
            try {
               var16.close();
            } catch (Throwable var12) {
               var14.addSuppressed(var12);
               break label88;
            }

         }
      }

      var16.close();
      return var0;
   }

   public static MessageDigest updateDigest(MessageDigest var0, InputStream var1) throws IOException {
      byte[] var3 = new byte[1024];

      for(int var2 = var1.read(var3, 0, 1024); var2 > -1; var2 = var1.read(var3, 0, 1024)) {
         var0.update(var3, 0, var2);
      }

      return var0;
   }

   public static MessageDigest updateDigest(MessageDigest var0, RandomAccessFile var1) throws IOException {
      return updateDigest(var0, var1.getChannel());
   }

   public static MessageDigest updateDigest(MessageDigest var0, String var1) {
      var0.update(StringUtils.getBytesUtf8(var1));
      return var0;
   }

   public static MessageDigest updateDigest(MessageDigest var0, ByteBuffer var1) {
      var0.update(var1);
      return var0;
   }

   private static MessageDigest updateDigest(MessageDigest var0, FileChannel var1) throws IOException {
      ByteBuffer var2 = ByteBuffer.allocate(1024);

      while(var1.read(var2) > 0) {
         var2.flip();
         var0.update(var2);
         var2.clear();
      }

      return var0;
   }

   public static MessageDigest updateDigest(MessageDigest var0, Path var1, OpenOption... var2) throws IOException {
      BufferedInputStream var16 = new BufferedInputStream(Files.newInputStream(var1, var2));

      try {
         var0 = updateDigest(var0, (InputStream)var16);
      } catch (Throwable var14) {
         Throwable var15 = var14;

         try {
            throw var15;
         } finally {
            label88:
            try {
               var16.close();
            } catch (Throwable var12) {
               var14.addSuppressed(var12);
               break label88;
            }

         }
      }

      var16.close();
      return var0;
   }

   public static MessageDigest updateDigest(MessageDigest var0, byte[] var1) {
      var0.update(var1);
      return var0;
   }

   public byte[] digest(File var1) throws IOException {
      return updateDigest(this.messageDigest, var1).digest();
   }

   public byte[] digest(InputStream var1) throws IOException {
      return updateDigest(this.messageDigest, var1).digest();
   }

   public byte[] digest(String var1) {
      return updateDigest(this.messageDigest, var1).digest();
   }

   public byte[] digest(ByteBuffer var1) {
      return updateDigest(this.messageDigest, var1).digest();
   }

   public byte[] digest(Path var1, OpenOption... var2) throws IOException {
      return updateDigest(this.messageDigest, var1, var2).digest();
   }

   public byte[] digest(byte[] var1) {
      return updateDigest(this.messageDigest, var1).digest();
   }

   public String digestAsHex(File var1) throws IOException {
      return Hex.encodeHexString(this.digest(var1));
   }

   public String digestAsHex(InputStream var1) throws IOException {
      return Hex.encodeHexString(this.digest(var1));
   }

   public String digestAsHex(String var1) {
      return Hex.encodeHexString(this.digest(var1));
   }

   public String digestAsHex(ByteBuffer var1) {
      return Hex.encodeHexString(this.digest(var1));
   }

   public String digestAsHex(Path var1, OpenOption... var2) throws IOException {
      return Hex.encodeHexString(this.digest(var1, var2));
   }

   public String digestAsHex(byte[] var1) {
      return Hex.encodeHexString(this.digest(var1));
   }

   public MessageDigest getMessageDigest() {
      return this.messageDigest;
   }
}
