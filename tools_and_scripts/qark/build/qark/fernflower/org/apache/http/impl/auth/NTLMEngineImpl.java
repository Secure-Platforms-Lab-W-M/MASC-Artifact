package org.apache.http.impl.auth;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;

final class NTLMEngineImpl implements NTLMEngine {
   private static final Charset DEFAULT_CHARSET;
   static final int FLAG_DOMAIN_PRESENT = 4096;
   static final int FLAG_REQUEST_128BIT_KEY_EXCH = 536870912;
   static final int FLAG_REQUEST_56BIT_ENCRYPTION = Integer.MIN_VALUE;
   static final int FLAG_REQUEST_ALWAYS_SIGN = 32768;
   static final int FLAG_REQUEST_EXPLICIT_KEY_EXCH = 1073741824;
   static final int FLAG_REQUEST_LAN_MANAGER_KEY = 128;
   static final int FLAG_REQUEST_NTLM2_SESSION = 524288;
   static final int FLAG_REQUEST_NTLMv1 = 512;
   static final int FLAG_REQUEST_OEM_ENCODING = 2;
   static final int FLAG_REQUEST_SEAL = 32;
   static final int FLAG_REQUEST_SIGN = 16;
   static final int FLAG_REQUEST_TARGET = 4;
   static final int FLAG_REQUEST_UNICODE_ENCODING = 1;
   static final int FLAG_REQUEST_VERSION = 33554432;
   static final int FLAG_TARGETINFO_PRESENT = 8388608;
   static final int FLAG_WORKSTATION_PRESENT = 8192;
   private static final byte[] MAGIC_TLS_SERVER_ENDPOINT;
   static final int MSV_AV_CHANNEL_BINDINGS = 10;
   static final int MSV_AV_DNS_COMPUTER_NAME = 3;
   static final int MSV_AV_DNS_DOMAIN_NAME = 4;
   static final int MSV_AV_DNS_TREE_NAME = 5;
   static final int MSV_AV_EOL = 0;
   static final int MSV_AV_FLAGS = 6;
   static final int MSV_AV_FLAGS_ACCOUNT_AUTH_CONSTAINED = 1;
   static final int MSV_AV_FLAGS_MIC = 2;
   static final int MSV_AV_FLAGS_UNTRUSTED_TARGET_SPN = 4;
   static final int MSV_AV_NB_COMPUTER_NAME = 1;
   static final int MSV_AV_NB_DOMAIN_NAME = 2;
   static final int MSV_AV_SINGLE_HOST = 8;
   static final int MSV_AV_TARGET_NAME = 9;
   static final int MSV_AV_TIMESTAMP = 7;
   private static final SecureRandom RND_GEN;
   private static final byte[] SEAL_MAGIC_CLIENT;
   private static final byte[] SEAL_MAGIC_SERVER;
   private static final byte[] SIGNATURE;
   private static final byte[] SIGN_MAGIC_CLIENT;
   private static final byte[] SIGN_MAGIC_SERVER;
   private static final String TYPE_1_MESSAGE;
   private static final Charset UNICODE_LITTLE_UNMARKED = Charset.forName("UnicodeLittleUnmarked");

   static {
      DEFAULT_CHARSET = Consts.ASCII;
      SecureRandom var0 = null;

      label13: {
         SecureRandom var1;
         try {
            var1 = SecureRandom.getInstance("SHA1PRNG");
         } catch (Exception var2) {
            break label13;
         }

         var0 = var1;
      }

      RND_GEN = var0;
      SIGNATURE = getNullTerminatedAsciiString("NTLMSSP");
      SIGN_MAGIC_SERVER = getNullTerminatedAsciiString("session key to server-to-client signing key magic constant");
      SIGN_MAGIC_CLIENT = getNullTerminatedAsciiString("session key to client-to-server signing key magic constant");
      SEAL_MAGIC_SERVER = getNullTerminatedAsciiString("session key to server-to-client sealing key magic constant");
      SEAL_MAGIC_CLIENT = getNullTerminatedAsciiString("session key to client-to-server sealing key magic constant");
      MAGIC_TLS_SERVER_ENDPOINT = "tls-server-end-point:".getBytes(Consts.ASCII);
      TYPE_1_MESSAGE = (new NTLMEngineImpl.Type1Message()).getResponse();
   }

   // $FF: renamed from: F (int, int, int) int
   static int method_8(int var0, int var1, int var2) {
      return var0 & var1 | var0 & var2;
   }

   // $FF: renamed from: G (int, int, int) int
   static int method_9(int var0, int var1, int var2) {
      return var0 & var1 | var0 & var2 | var1 & var2;
   }

   // $FF: renamed from: H (int, int, int) int
   static int method_10(int var0, int var1, int var2) {
      return var0 ^ var1 ^ var2;
   }

   static byte[] RC4(byte[] var0, byte[] var1) throws NTLMEngineException {
      try {
         Cipher var2 = Cipher.getInstance("RC4");
         var2.init(1, new SecretKeySpec(var1, "RC4"));
         var0 = var2.doFinal(var0);
         return var0;
      } catch (Exception var3) {
         throw new NTLMEngineException(var3.getMessage(), var3);
      }
   }

   private static String convertDomain(String var0) {
      return stripDotSuffix(var0);
   }

   private static String convertHost(String var0) {
      return stripDotSuffix(var0);
   }

   private static byte[] createBlob(byte[] var0, byte[] var1, byte[] var2) {
      byte[] var4 = new byte[]{1, 1, 0, 0};
      byte[] var5 = new byte[]{0, 0, 0, 0};
      byte[] var6 = new byte[]{0, 0, 0, 0};
      byte[] var7 = new byte[]{0, 0, 0, 0};
      byte[] var8 = new byte[var4.length + var5.length + var2.length + 8 + var6.length + var1.length + var7.length];
      System.arraycopy(var4, 0, var8, 0, var4.length);
      int var3 = 0 + var4.length;
      System.arraycopy(var5, 0, var8, var3, var5.length);
      var3 += var5.length;
      System.arraycopy(var2, 0, var8, var3, var2.length);
      var3 += var2.length;
      System.arraycopy(var0, 0, var8, var3, 8);
      var3 += 8;
      System.arraycopy(var6, 0, var8, var3, var6.length);
      var3 += var6.length;
      System.arraycopy(var1, 0, var8, var3, var1.length);
      var3 += var1.length;
      System.arraycopy(var7, 0, var8, var3, var7.length);
      var3 = var7.length;
      return var8;
   }

   private static Key createDESKey(byte[] var0, int var1) {
      byte[] var2 = new byte[7];
      System.arraycopy(var0, var1, var2, 0, 7);
      var0 = new byte[]{var2[0], (byte)(var2[0] << 7 | (var2[1] & 255) >>> 1), (byte)(var2[1] << 6 | (var2[2] & 255) >>> 2), (byte)(var2[2] << 5 | (var2[3] & 255) >>> 3), (byte)(var2[3] << 4 | (var2[4] & 255) >>> 4), (byte)(var2[4] << 3 | (var2[5] & 255) >>> 5), (byte)(var2[5] << 2 | (var2[6] & 255) >>> 6), (byte)(var2[6] << 1)};
      oddParity(var0);
      return new SecretKeySpec(var0, "DES");
   }

   private static void encodeLong(byte[] var0, int var1, int var2) {
      var0[var1 + 0] = (byte)(var2 & 255);
      var0[var1 + 1] = (byte)(var2 >> 8 & 255);
      var0[var1 + 2] = (byte)(var2 >> 16 & 255);
      var0[var1 + 3] = (byte)(var2 >> 24 & 255);
   }

   private static byte[] encodeLong(int var0) {
      byte[] var1 = new byte[4];
      encodeLong(var1, 0, var0);
      return var1;
   }

   private static Charset getCharset(int var0) throws NTLMEngineException {
      if ((var0 & 1) == 0) {
         return DEFAULT_CHARSET;
      } else {
         Charset var1 = UNICODE_LITTLE_UNMARKED;
         if (var1 != null) {
            return var1;
         } else {
            throw new NTLMEngineException("Unicode not supported");
         }
      }
   }

   static MessageDigest getMD5() {
      try {
         MessageDigest var0 = MessageDigest.getInstance("MD5");
         return var0;
      } catch (NoSuchAlgorithmException var2) {
         StringBuilder var1 = new StringBuilder();
         var1.append("MD5 message digest doesn't seem to exist - fatal error: ");
         var1.append(var2.getMessage());
         throw new RuntimeException(var1.toString(), var2);
      }
   }

   private static byte[] getNullTerminatedAsciiString(String var0) {
      byte[] var2 = var0.getBytes(Consts.ASCII);
      byte[] var1 = new byte[var2.length + 1];
      System.arraycopy(var2, 0, var1, 0, var2.length);
      var1[var2.length] = 0;
      return var1;
   }

   static String getType1Message(String var0, String var1) {
      return TYPE_1_MESSAGE;
   }

   static String getType3Message(String var0, String var1, String var2, String var3, byte[] var4, int var5, String var6, byte[] var7) throws NTLMEngineException {
      return (new NTLMEngineImpl.Type3Message(var3, var2, var0, var1, var4, var5, var6, var7)).getResponse();
   }

   static String getType3Message(String var0, String var1, String var2, String var3, byte[] var4, int var5, String var6, byte[] var7, Certificate var8, byte[] var9, byte[] var10) throws NTLMEngineException {
      return (new NTLMEngineImpl.Type3Message(var3, var2, var0, var1, var4, var5, var6, var7, var8, var9, var10)).getResponse();
   }

   static byte[] hmacMD5(byte[] var0, byte[] var1) throws NTLMEngineException {
      NTLMEngineImpl.HMACMD5 var2 = new NTLMEngineImpl.HMACMD5(var1);
      var2.update(var0);
      return var2.getOutput();
   }

   private static byte[] lmHash(String var0) throws NTLMEngineException {
      try {
         byte[] var6 = var0.toUpperCase(Locale.ROOT).getBytes(Consts.ASCII);
         int var1 = Math.min(var6.length, 14);
         byte[] var2 = new byte[14];
         System.arraycopy(var6, 0, var2, 0, var1);
         Key var7 = createDESKey(var2, 0);
         Key var8 = createDESKey(var2, 7);
         byte[] var3 = "KGS!@#$%".getBytes(Consts.ASCII);
         Cipher var4 = Cipher.getInstance("DES/ECB/NoPadding");
         var4.init(1, var7);
         var6 = var4.doFinal(var3);
         var4.init(1, var8);
         var2 = var4.doFinal(var3);
         var3 = new byte[16];
         System.arraycopy(var6, 0, var3, 0, 8);
         System.arraycopy(var2, 0, var3, 8, 8);
         return var3;
      } catch (Exception var5) {
         throw new NTLMEngineException(var5.getMessage(), var5);
      }
   }

   private static byte[] lmResponse(byte[] var0, byte[] var1) throws NTLMEngineException {
      try {
         byte[] var3 = new byte[21];
         System.arraycopy(var0, 0, var3, 0, 16);
         Key var6 = createDESKey(var3, 0);
         Key var2 = createDESKey(var3, 7);
         Key var8 = createDESKey(var3, 14);
         Cipher var4 = Cipher.getInstance("DES/ECB/NoPadding");
         var4.init(1, var6);
         var0 = var4.doFinal(var1);
         var4.init(1, var2);
         byte[] var7 = var4.doFinal(var1);
         var4.init(1, var8);
         var1 = var4.doFinal(var1);
         var3 = new byte[24];
         System.arraycopy(var0, 0, var3, 0, 8);
         System.arraycopy(var7, 0, var3, 8, 8);
         System.arraycopy(var1, 0, var3, 16, 8);
         return var3;
      } catch (Exception var5) {
         throw new NTLMEngineException(var5.getMessage(), var5);
      }
   }

   private static byte[] lmv2Hash(String var0, String var1, byte[] var2) throws NTLMEngineException {
      if (UNICODE_LITTLE_UNMARKED != null) {
         NTLMEngineImpl.HMACMD5 var3 = new NTLMEngineImpl.HMACMD5(var2);
         var3.update(var1.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
         if (var0 != null) {
            var3.update(var0.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
         }

         return var3.getOutput();
      } else {
         throw new NTLMEngineException("Unicode not supported");
      }
   }

   private static byte[] lmv2Response(byte[] var0, byte[] var1, byte[] var2) {
      NTLMEngineImpl.HMACMD5 var3 = new NTLMEngineImpl.HMACMD5(var0);
      var3.update(var1);
      var3.update(var2);
      var0 = var3.getOutput();
      var1 = new byte[var0.length + var2.length];
      System.arraycopy(var0, 0, var1, 0, var0.length);
      System.arraycopy(var2, 0, var1, var0.length, var2.length);
      return var1;
   }

   private static byte[] makeRandomChallenge(Random param0) {
      // $FF: Couldn't be decompiled
   }

   private static byte[] makeSecondaryKey(Random param0) {
      // $FF: Couldn't be decompiled
   }

   static byte[] ntlm2SessionResponse(byte[] var0, byte[] var1, byte[] var2) throws NTLMEngineException {
      try {
         MessageDigest var3 = getMD5();
         var3.update(var1);
         var3.update(var2);
         var1 = var3.digest();
         var2 = new byte[8];
         System.arraycopy(var1, 0, var2, 0, 8);
         var0 = lmResponse(var0, var2);
         return var0;
      } catch (Exception var4) {
         if (var4 instanceof NTLMEngineException) {
            throw (NTLMEngineException)var4;
         } else {
            throw new NTLMEngineException(var4.getMessage(), var4);
         }
      }
   }

   private static byte[] ntlmHash(String var0) throws NTLMEngineException {
      Charset var1 = UNICODE_LITTLE_UNMARKED;
      if (var1 != null) {
         byte[] var2 = var0.getBytes(var1);
         NTLMEngineImpl.MD4 var3 = new NTLMEngineImpl.MD4();
         var3.update(var2);
         return var3.getOutput();
      } else {
         throw new NTLMEngineException("Unicode not supported");
      }
   }

   private static byte[] ntlmv2Hash(String var0, String var1, byte[] var2) throws NTLMEngineException {
      if (UNICODE_LITTLE_UNMARKED != null) {
         NTLMEngineImpl.HMACMD5 var3 = new NTLMEngineImpl.HMACMD5(var2);
         var3.update(var1.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
         if (var0 != null) {
            var3.update(var0.getBytes(UNICODE_LITTLE_UNMARKED));
         }

         return var3.getOutput();
      } else {
         throw new NTLMEngineException("Unicode not supported");
      }
   }

   private static void oddParity(byte[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         byte var2 = var0[var1];
         boolean var3;
         if (((var2 >>> 7 ^ var2 >>> 6 ^ var2 >>> 5 ^ var2 >>> 4 ^ var2 >>> 3 ^ var2 >>> 2 ^ var2 >>> 1) & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (var3) {
            var0[var1] = (byte)(1 | var0[var1]);
         } else {
            var0[var1] &= -2;
         }
      }

   }

   private static byte[] readSecurityBuffer(byte[] var0, int var1) {
      int var2 = readUShort(var0, var1);
      var1 = readULong(var0, var1 + 4);
      if (var0.length < var1 + var2) {
         return new byte[var2];
      } else {
         byte[] var3 = new byte[var2];
         System.arraycopy(var0, var1, var3, 0, var2);
         return var3;
      }
   }

   private static int readULong(byte[] var0, int var1) {
      return var0.length < var1 + 4 ? 0 : var0[var1] & 255 | (var0[var1 + 1] & 255) << 8 | (var0[var1 + 2] & 255) << 16 | (var0[var1 + 3] & 255) << 24;
   }

   private static int readUShort(byte[] var0, int var1) {
      return var0.length < var1 + 2 ? 0 : var0[var1] & 255 | (var0[var1 + 1] & 255) << 8;
   }

   static int rotintlft(int var0, int var1) {
      return var0 << var1 | var0 >>> 32 - var1;
   }

   private static String stripDotSuffix(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(46);
         return var1 != -1 ? var0.substring(0, var1) : var0;
      }
   }

   static void writeULong(byte[] var0, int var1, int var2) {
      var0[var2] = (byte)(var1 & 255);
      var0[var2 + 1] = (byte)(var1 >> 8 & 255);
      var0[var2 + 2] = (byte)(var1 >> 16 & 255);
      var0[var2 + 3] = (byte)(var1 >> 24 & 255);
   }

   static void writeUShort(byte[] var0, int var1, int var2) {
      var0[var2] = (byte)(var1 & 255);
      var0[var2 + 1] = (byte)(var1 >> 8 & 255);
   }

   public String generateType1Msg(String var1, String var2) throws NTLMEngineException {
      return getType1Message(var2, var1);
   }

   public String generateType3Msg(String var1, String var2, String var3, String var4, String var5) throws NTLMEngineException {
      NTLMEngineImpl.Type2Message var6 = new NTLMEngineImpl.Type2Message(var5);
      return getType3Message(var1, var2, var4, var3, var6.getChallenge(), var6.getFlags(), var6.getTarget(), var6.getTargetInfo());
   }

   protected static class CipherGen {
      protected final byte[] challenge;
      protected byte[] clientChallenge;
      protected byte[] clientChallenge2;
      protected final long currentTime;
      protected final String domain;
      protected byte[] lanManagerSessionKey;
      protected byte[] lm2SessionResponse;
      protected byte[] lmHash;
      protected byte[] lmResponse;
      protected byte[] lmUserSessionKey;
      protected byte[] lmv2Hash;
      protected byte[] lmv2Response;
      protected byte[] ntlm2SessionResponse;
      protected byte[] ntlm2SessionResponseUserSessionKey;
      protected byte[] ntlmHash;
      protected byte[] ntlmResponse;
      protected byte[] ntlmUserSessionKey;
      protected byte[] ntlmv2Blob;
      protected byte[] ntlmv2Hash;
      protected byte[] ntlmv2Response;
      protected byte[] ntlmv2UserSessionKey;
      protected final String password;
      protected final Random random;
      protected byte[] secondaryKey;
      protected final String target;
      protected final byte[] targetInformation;
      protected byte[] timestamp;
      protected final String user;

      @Deprecated
      public CipherGen(String var1, String var2, String var3, byte[] var4, String var5, byte[] var6) {
         this(NTLMEngineImpl.RND_GEN, System.currentTimeMillis(), var1, var2, var3, var4, var5, var6);
      }

      @Deprecated
      public CipherGen(String var1, String var2, String var3, byte[] var4, String var5, byte[] var6, byte[] var7, byte[] var8, byte[] var9, byte[] var10) {
         this(NTLMEngineImpl.RND_GEN, System.currentTimeMillis(), var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      }

      public CipherGen(Random var1, long var2, String var4, String var5, String var6, byte[] var7, String var8, byte[] var9) {
         this(var1, var2, var4, var5, var6, var7, var8, var9, (byte[])null, (byte[])null, (byte[])null, (byte[])null);
      }

      public CipherGen(Random var1, long var2, String var4, String var5, String var6, byte[] var7, String var8, byte[] var9, byte[] var10, byte[] var11, byte[] var12, byte[] var13) {
         this.lmHash = null;
         this.lmResponse = null;
         this.ntlmHash = null;
         this.ntlmResponse = null;
         this.ntlmv2Hash = null;
         this.lmv2Hash = null;
         this.lmv2Response = null;
         this.ntlmv2Blob = null;
         this.ntlmv2Response = null;
         this.ntlm2SessionResponse = null;
         this.lm2SessionResponse = null;
         this.lmUserSessionKey = null;
         this.ntlmUserSessionKey = null;
         this.ntlmv2UserSessionKey = null;
         this.ntlm2SessionResponseUserSessionKey = null;
         this.lanManagerSessionKey = null;
         this.random = var1;
         this.currentTime = var2;
         this.domain = var4;
         this.target = var8;
         this.user = var5;
         this.password = var6;
         this.challenge = var7;
         this.targetInformation = var9;
         this.clientChallenge = var10;
         this.clientChallenge2 = var11;
         this.secondaryKey = var12;
         this.timestamp = var13;
      }

      public byte[] getClientChallenge() throws NTLMEngineException {
         if (this.clientChallenge == null) {
            this.clientChallenge = NTLMEngineImpl.makeRandomChallenge(this.random);
         }

         return this.clientChallenge;
      }

      public byte[] getClientChallenge2() throws NTLMEngineException {
         if (this.clientChallenge2 == null) {
            this.clientChallenge2 = NTLMEngineImpl.makeRandomChallenge(this.random);
         }

         return this.clientChallenge2;
      }

      public byte[] getLM2SessionResponse() throws NTLMEngineException {
         if (this.lm2SessionResponse == null) {
            byte[] var1 = this.getClientChallenge();
            byte[] var2 = new byte[24];
            this.lm2SessionResponse = var2;
            System.arraycopy(var1, 0, var2, 0, var1.length);
            var2 = this.lm2SessionResponse;
            Arrays.fill(var2, var1.length, var2.length, (byte)0);
         }

         return this.lm2SessionResponse;
      }

      public byte[] getLMHash() throws NTLMEngineException {
         if (this.lmHash == null) {
            this.lmHash = NTLMEngineImpl.lmHash(this.password);
         }

         return this.lmHash;
      }

      public byte[] getLMResponse() throws NTLMEngineException {
         if (this.lmResponse == null) {
            this.lmResponse = NTLMEngineImpl.lmResponse(this.getLMHash(), this.challenge);
         }

         return this.lmResponse;
      }

      public byte[] getLMUserSessionKey() throws NTLMEngineException {
         if (this.lmUserSessionKey == null) {
            this.lmUserSessionKey = new byte[16];
            System.arraycopy(this.getLMHash(), 0, this.lmUserSessionKey, 0, 8);
            Arrays.fill(this.lmUserSessionKey, 8, 16, (byte)0);
         }

         return this.lmUserSessionKey;
      }

      public byte[] getLMv2Hash() throws NTLMEngineException {
         if (this.lmv2Hash == null) {
            this.lmv2Hash = NTLMEngineImpl.lmv2Hash(this.domain, this.user, this.getNTLMHash());
         }

         return this.lmv2Hash;
      }

      public byte[] getLMv2Response() throws NTLMEngineException {
         if (this.lmv2Response == null) {
            this.lmv2Response = NTLMEngineImpl.lmv2Response(this.getLMv2Hash(), this.challenge, this.getClientChallenge());
         }

         return this.lmv2Response;
      }

      public byte[] getLanManagerSessionKey() throws NTLMEngineException {
         if (this.lanManagerSessionKey == null) {
            try {
               byte[] var1 = new byte[14];
               System.arraycopy(this.getLMHash(), 0, var1, 0, 8);
               Arrays.fill(var1, 8, var1.length, (byte)-67);
               Key var3 = NTLMEngineImpl.createDESKey(var1, 0);
               Key var6 = NTLMEngineImpl.createDESKey(var1, 7);
               byte[] var2 = new byte[8];
               System.arraycopy(this.getLMResponse(), 0, var2, 0, var2.length);
               Cipher var4 = Cipher.getInstance("DES/ECB/NoPadding");
               var4.init(1, var3);
               byte[] var7 = var4.doFinal(var2);
               var4 = Cipher.getInstance("DES/ECB/NoPadding");
               var4.init(1, var6);
               var1 = var4.doFinal(var2);
               var2 = new byte[16];
               this.lanManagerSessionKey = var2;
               System.arraycopy(var7, 0, var2, 0, var7.length);
               System.arraycopy(var1, 0, this.lanManagerSessionKey, var7.length, var1.length);
            } catch (Exception var5) {
               throw new NTLMEngineException(var5.getMessage(), var5);
            }
         }

         return this.lanManagerSessionKey;
      }

      public byte[] getNTLM2SessionResponse() throws NTLMEngineException {
         if (this.ntlm2SessionResponse == null) {
            this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(this.getNTLMHash(), this.challenge, this.getClientChallenge());
         }

         return this.ntlm2SessionResponse;
      }

      public byte[] getNTLM2SessionResponseUserSessionKey() throws NTLMEngineException {
         if (this.ntlm2SessionResponseUserSessionKey == null) {
            byte[] var1 = this.getLM2SessionResponse();
            byte[] var2 = this.challenge;
            byte[] var3 = new byte[var2.length + var1.length];
            System.arraycopy(var2, 0, var3, 0, var2.length);
            System.arraycopy(var1, 0, var3, this.challenge.length, var1.length);
            this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(var3, this.getNTLMUserSessionKey());
         }

         return this.ntlm2SessionResponseUserSessionKey;
      }

      public byte[] getNTLMHash() throws NTLMEngineException {
         if (this.ntlmHash == null) {
            this.ntlmHash = NTLMEngineImpl.ntlmHash(this.password);
         }

         return this.ntlmHash;
      }

      public byte[] getNTLMResponse() throws NTLMEngineException {
         if (this.ntlmResponse == null) {
            this.ntlmResponse = NTLMEngineImpl.lmResponse(this.getNTLMHash(), this.challenge);
         }

         return this.ntlmResponse;
      }

      public byte[] getNTLMUserSessionKey() throws NTLMEngineException {
         if (this.ntlmUserSessionKey == null) {
            NTLMEngineImpl.MD4 var1 = new NTLMEngineImpl.MD4();
            var1.update(this.getNTLMHash());
            this.ntlmUserSessionKey = var1.getOutput();
         }

         return this.ntlmUserSessionKey;
      }

      public byte[] getNTLMv2Blob() throws NTLMEngineException {
         if (this.ntlmv2Blob == null) {
            this.ntlmv2Blob = NTLMEngineImpl.createBlob(this.getClientChallenge2(), this.targetInformation, this.getTimestamp());
         }

         return this.ntlmv2Blob;
      }

      public byte[] getNTLMv2Hash() throws NTLMEngineException {
         if (this.ntlmv2Hash == null) {
            this.ntlmv2Hash = NTLMEngineImpl.ntlmv2Hash(this.domain, this.user, this.getNTLMHash());
         }

         return this.ntlmv2Hash;
      }

      public byte[] getNTLMv2Response() throws NTLMEngineException {
         if (this.ntlmv2Response == null) {
            this.ntlmv2Response = NTLMEngineImpl.lmv2Response(this.getNTLMv2Hash(), this.challenge, this.getNTLMv2Blob());
         }

         return this.ntlmv2Response;
      }

      public byte[] getNTLMv2UserSessionKey() throws NTLMEngineException {
         if (this.ntlmv2UserSessionKey == null) {
            byte[] var1 = this.getNTLMv2Hash();
            byte[] var2 = new byte[16];
            System.arraycopy(this.getNTLMv2Response(), 0, var2, 0, 16);
            this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(var2, var1);
         }

         return this.ntlmv2UserSessionKey;
      }

      public byte[] getSecondaryKey() throws NTLMEngineException {
         if (this.secondaryKey == null) {
            this.secondaryKey = NTLMEngineImpl.makeSecondaryKey(this.random);
         }

         return this.secondaryKey;
      }

      public byte[] getTimestamp() {
         if (this.timestamp == null) {
            long var2 = (this.currentTime + 11644473600000L) * 10000L;
            this.timestamp = new byte[8];

            for(int var1 = 0; var1 < 8; ++var1) {
               this.timestamp[var1] = (byte)((int)var2);
               var2 >>>= 8;
            }
         }

         return this.timestamp;
      }
   }

   static class HMACMD5 {
      protected final byte[] ipad;
      protected final MessageDigest md5;
      protected final byte[] opad;

      HMACMD5(byte[] var1) {
         MessageDigest var6 = NTLMEngineImpl.getMD5();
         this.md5 = var6;
         this.ipad = new byte[64];
         this.opad = new byte[64];
         int var2 = var1.length;
         byte[] var5 = var1;
         int var3 = var2;
         if (var2 > 64) {
            var6.update(var1);
            var5 = this.md5.digest();
            var3 = var5.length;
         }

         var2 = 0;

         while(true) {
            int var4 = var2;
            if (var2 >= var3) {
               while(var4 < 64) {
                  this.ipad[var4] = 54;
                  this.opad[var4] = 92;
                  ++var4;
               }

               this.md5.reset();
               this.md5.update(this.ipad);
               return;
            }

            this.ipad[var2] = (byte)(54 ^ var5[var2]);
            this.opad[var2] = (byte)(92 ^ var5[var2]);
            ++var2;
         }
      }

      byte[] getOutput() {
         byte[] var1 = this.md5.digest();
         this.md5.update(this.opad);
         return this.md5.digest(var1);
      }

      void update(byte[] var1) {
         this.md5.update(var1);
      }

      void update(byte[] var1, int var2, int var3) {
         this.md5.update(var1, var2, var3);
      }
   }

   static class Handle {
      private final byte[] exportedSessionKey;
      private final boolean isConnection;
      final NTLMEngineImpl.Mode mode;
      private final Cipher rc4;
      private byte[] sealingKey;
      int sequenceNumber = 0;
      private byte[] signingKey;

      Handle(byte[] var1, NTLMEngineImpl.Mode var2, boolean var3) throws NTLMEngineException {
         this.exportedSessionKey = var1;
         this.isConnection = var3;
         this.mode = var2;

         label33: {
            Exception var10000;
            label37: {
               boolean var10001;
               MessageDigest var4;
               MessageDigest var5;
               label30: {
                  try {
                     var4 = NTLMEngineImpl.getMD5();
                     var5 = NTLMEngineImpl.getMD5();
                     var4.update(var1);
                     var5.update(var1);
                     if (var2 == NTLMEngineImpl.Mode.CLIENT) {
                        var4.update(NTLMEngineImpl.SIGN_MAGIC_CLIENT);
                        var5.update(NTLMEngineImpl.SEAL_MAGIC_CLIENT);
                        break label30;
                     }
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label37;
                  }

                  try {
                     var4.update(NTLMEngineImpl.SIGN_MAGIC_SERVER);
                     var5.update(NTLMEngineImpl.SEAL_MAGIC_SERVER);
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label37;
                  }
               }

               try {
                  this.signingKey = var4.digest();
                  this.sealingKey = var5.digest();
                  break label33;
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }

            Exception var9 = var10000;
            throw new NTLMEngineException(var9.getMessage(), var9);
         }

         this.rc4 = this.initCipher();
      }

      private void advanceMessageSequence() throws NTLMEngineException {
         if (!this.isConnection) {
            MessageDigest var1 = NTLMEngineImpl.getMD5();
            var1.update(this.sealingKey);
            byte[] var2 = new byte[4];
            NTLMEngineImpl.writeULong(var2, this.sequenceNumber, 0);
            var1.update(var2);
            this.sealingKey = var1.digest();
            this.initCipher();
         }

         ++this.sequenceNumber;
      }

      private byte[] computeSignature(byte[] var1) {
         byte[] var2 = new byte[16];
         var2[0] = 1;
         var2[1] = 0;
         var2[2] = 0;
         var2[3] = 0;
         NTLMEngineImpl.HMACMD5 var3 = new NTLMEngineImpl.HMACMD5(this.signingKey);
         var3.update(NTLMEngineImpl.encodeLong(this.sequenceNumber));
         var3.update(var1);
         var1 = var3.getOutput();
         byte[] var4 = new byte[8];
         System.arraycopy(var1, 0, var4, 0, 8);
         System.arraycopy(this.encrypt(var4), 0, var2, 4, 8);
         NTLMEngineImpl.encodeLong(var2, 12, this.sequenceNumber);
         return var2;
      }

      private byte[] decrypt(byte[] var1) {
         return this.rc4.update(var1);
      }

      private byte[] encrypt(byte[] var1) {
         return this.rc4.update(var1);
      }

      private Cipher initCipher() throws NTLMEngineException {
         Exception var1;
         Cipher var4;
         try {
            var4 = Cipher.getInstance("RC4");
         } catch (Exception var3) {
            var1 = var3;
            throw new NTLMEngineException(var1.getMessage(), var1);
         }

         try {
            if (this.mode == NTLMEngineImpl.Mode.CLIENT) {
               var4.init(1, new SecretKeySpec(this.sealingKey, "RC4"));
               return var4;
            } else {
               var4.init(2, new SecretKeySpec(this.sealingKey, "RC4"));
               return var4;
            }
         } catch (Exception var2) {
            var1 = var2;
            throw new NTLMEngineException(var1.getMessage(), var1);
         }
      }

      private boolean validateSignature(byte[] var1, byte[] var2) {
         return Arrays.equals(var1, this.computeSignature(var2));
      }

      public byte[] decryptAndVerifySignedMessage(byte[] var1) throws NTLMEngineException {
         byte[] var2 = new byte[16];
         System.arraycopy(var1, 0, var2, 0, var2.length);
         byte[] var3 = new byte[var1.length - 16];
         System.arraycopy(var1, 16, var3, 0, var3.length);
         var1 = this.decrypt(var3);
         if (this.validateSignature(var2, var1)) {
            this.advanceMessageSequence();
            return var1;
         } else {
            throw new NTLMEngineException("Wrong signature");
         }
      }

      public byte[] getSealingKey() {
         return this.sealingKey;
      }

      public byte[] getSigningKey() {
         return this.signingKey;
      }

      public byte[] signAndEncryptMessage(byte[] var1) throws NTLMEngineException {
         byte[] var2 = this.encrypt(var1);
         var1 = this.computeSignature(var1);
         byte[] var3 = new byte[var1.length + var2.length];
         System.arraycopy(var1, 0, var3, 0, var1.length);
         System.arraycopy(var2, 0, var3, var1.length, var2.length);
         this.advanceMessageSequence();
         return var3;
      }
   }

   static class MD4 {
      // $FF: renamed from: A int
      protected int field_188 = 1732584193;
      // $FF: renamed from: B int
      protected int field_189 = -271733879;
      // $FF: renamed from: C int
      protected int field_190 = -1732584194;
      // $FF: renamed from: D int
      protected int field_191 = 271733878;
      protected long count = 0L;
      protected final byte[] dataBuffer = new byte[64];

      byte[] getOutput() {
         int var1 = (int)(this.count & 63L);
         if (var1 < 56) {
            var1 = 56 - var1;
         } else {
            var1 = 120 - var1;
         }

         byte[] var3 = new byte[var1 + 8];
         var3[0] = -128;

         for(int var2 = 0; var2 < 8; ++var2) {
            var3[var1 + var2] = (byte)((int)(this.count * 8L >>> var2 * 8));
         }

         this.update(var3);
         var3 = new byte[16];
         NTLMEngineImpl.writeULong(var3, this.field_188, 0);
         NTLMEngineImpl.writeULong(var3, this.field_189, 4);
         NTLMEngineImpl.writeULong(var3, this.field_190, 8);
         NTLMEngineImpl.writeULong(var3, this.field_191, 12);
         return var3;
      }

      protected void processBuffer() {
         int[] var5 = new int[16];

         int var1;
         for(var1 = 0; var1 < 16; ++var1) {
            byte[] var6 = this.dataBuffer;
            var5[var1] = (var6[var1 * 4] & 255) + ((var6[var1 * 4 + 1] & 255) << 8) + ((var6[var1 * 4 + 2] & 255) << 16) + ((var6[var1 * 4 + 3] & 255) << 24);
         }

         var1 = this.field_188;
         int var2 = this.field_189;
         int var3 = this.field_190;
         int var4 = this.field_191;
         this.round1(var5);
         this.round2(var5);
         this.round3(var5);
         this.field_188 += var1;
         this.field_189 += var2;
         this.field_190 += var3;
         this.field_191 += var4;
      }

      protected void round1(int[] var1) {
         int var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_8(this.field_189, this.field_190, this.field_191) + var1[0], 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_8(var2, this.field_189, this.field_190) + var1[1], 7);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_8(var2, this.field_188, this.field_189) + var1[2], 11);
         this.field_190 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_8(var2, this.field_191, this.field_188) + var1[3], 19);
         this.field_189 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_8(var2, this.field_190, this.field_191) + var1[4], 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_8(var2, this.field_189, this.field_190) + var1[5], 7);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_8(var2, this.field_188, this.field_189) + var1[6], 11);
         this.field_190 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_8(var2, this.field_191, this.field_188) + var1[7], 19);
         this.field_189 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_8(var2, this.field_190, this.field_191) + var1[8], 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_8(var2, this.field_189, this.field_190) + var1[9], 7);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_8(var2, this.field_188, this.field_189) + var1[10], 11);
         this.field_190 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_8(var2, this.field_191, this.field_188) + var1[11], 19);
         this.field_189 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_8(var2, this.field_190, this.field_191) + var1[12], 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_8(var2, this.field_189, this.field_190) + var1[13], 7);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_8(var2, this.field_188, this.field_189) + var1[14], 11);
         this.field_190 = var2;
         this.field_189 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_8(var2, this.field_191, this.field_188) + var1[15], 19);
      }

      protected void round2(int[] var1) {
         int var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_9(this.field_189, this.field_190, this.field_191) + var1[0] + 1518500249, 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_9(var2, this.field_189, this.field_190) + var1[4] + 1518500249, 5);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_9(var2, this.field_188, this.field_189) + var1[8] + 1518500249, 9);
         this.field_190 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_9(var2, this.field_191, this.field_188) + var1[12] + 1518500249, 13);
         this.field_189 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_9(var2, this.field_190, this.field_191) + var1[1] + 1518500249, 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_9(var2, this.field_189, this.field_190) + var1[5] + 1518500249, 5);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_9(var2, this.field_188, this.field_189) + var1[9] + 1518500249, 9);
         this.field_190 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_9(var2, this.field_191, this.field_188) + var1[13] + 1518500249, 13);
         this.field_189 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_9(var2, this.field_190, this.field_191) + var1[2] + 1518500249, 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_9(var2, this.field_189, this.field_190) + var1[6] + 1518500249, 5);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_9(var2, this.field_188, this.field_189) + var1[10] + 1518500249, 9);
         this.field_190 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_9(var2, this.field_191, this.field_188) + var1[14] + 1518500249, 13);
         this.field_189 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_9(var2, this.field_190, this.field_191) + var1[3] + 1518500249, 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_9(var2, this.field_189, this.field_190) + var1[7] + 1518500249, 5);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_9(var2, this.field_188, this.field_189) + var1[11] + 1518500249, 9);
         this.field_190 = var2;
         this.field_189 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_9(var2, this.field_191, this.field_188) + var1[15] + 1518500249, 13);
      }

      protected void round3(int[] var1) {
         int var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_10(this.field_189, this.field_190, this.field_191) + var1[0] + 1859775393, 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_10(var2, this.field_189, this.field_190) + var1[8] + 1859775393, 9);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_10(var2, this.field_188, this.field_189) + var1[4] + 1859775393, 11);
         this.field_190 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_10(var2, this.field_191, this.field_188) + var1[12] + 1859775393, 15);
         this.field_189 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_10(var2, this.field_190, this.field_191) + var1[2] + 1859775393, 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_10(var2, this.field_189, this.field_190) + var1[10] + 1859775393, 9);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_10(var2, this.field_188, this.field_189) + var1[6] + 1859775393, 11);
         this.field_190 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_10(var2, this.field_191, this.field_188) + var1[14] + 1859775393, 15);
         this.field_189 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_10(var2, this.field_190, this.field_191) + var1[1] + 1859775393, 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_10(var2, this.field_189, this.field_190) + var1[9] + 1859775393, 9);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_10(var2, this.field_188, this.field_189) + var1[5] + 1859775393, 11);
         this.field_190 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_10(var2, this.field_191, this.field_188) + var1[13] + 1859775393, 15);
         this.field_189 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_188 + NTLMEngineImpl.method_10(var2, this.field_190, this.field_191) + var1[3] + 1859775393, 3);
         this.field_188 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_191 + NTLMEngineImpl.method_10(var2, this.field_189, this.field_190) + var1[11] + 1859775393, 9);
         this.field_191 = var2;
         var2 = NTLMEngineImpl.rotintlft(this.field_190 + NTLMEngineImpl.method_10(var2, this.field_188, this.field_189) + var1[7] + 1859775393, 11);
         this.field_190 = var2;
         this.field_189 = NTLMEngineImpl.rotintlft(this.field_189 + NTLMEngineImpl.method_10(var2, this.field_191, this.field_188) + var1[15] + 1859775393, 15);
      }

      void update(byte[] var1) {
         int var3 = (int)(this.count & 63L);
         int var2 = 0;

         while(true) {
            int var4 = var1.length;
            byte[] var5 = this.dataBuffer;
            if (var4 - var2 + var3 < var5.length) {
               if (var2 < var1.length) {
                  var4 = var1.length - var2;
                  System.arraycopy(var1, var2, var5, var3, var4);
                  this.count += (long)var4;
               }

               return;
            }

            var4 = var5.length - var3;
            System.arraycopy(var1, var2, var5, var3, var4);
            this.count += (long)var4;
            var3 = 0;
            var2 += var4;
            this.processBuffer();
         }
      }
   }

   static enum Mode {
      CLIENT,
      SERVER;

      static {
         NTLMEngineImpl.Mode var0 = new NTLMEngineImpl.Mode("SERVER", 1);
         SERVER = var0;
      }
   }

   static class NTLMMessage {
      protected int currentOutputPosition;
      protected byte[] messageContents;

      NTLMMessage() {
         this.messageContents = null;
         this.currentOutputPosition = 0;
      }

      NTLMMessage(String var1, int var2) throws NTLMEngineException {
         this(Base64.decodeBase64(var1.getBytes(NTLMEngineImpl.DEFAULT_CHARSET)), var2);
      }

      NTLMMessage(byte[] var1, int var2) throws NTLMEngineException {
         this.messageContents = null;
         this.currentOutputPosition = 0;
         this.messageContents = var1;
         if (var1.length >= NTLMEngineImpl.SIGNATURE.length) {
            int var3;
            for(var3 = 0; var3 < NTLMEngineImpl.SIGNATURE.length; ++var3) {
               if (this.messageContents[var3] != NTLMEngineImpl.SIGNATURE[var3]) {
                  throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
               }
            }

            var3 = this.readULong(NTLMEngineImpl.SIGNATURE.length);
            if (var3 == var2) {
               this.currentOutputPosition = this.messageContents.length;
            } else {
               StringBuilder var4 = new StringBuilder();
               var4.append("NTLM type ");
               var4.append(Integer.toString(var2));
               var4.append(" message expected - instead got type ");
               var4.append(Integer.toString(var3));
               throw new NTLMEngineException(var4.toString());
            }
         } else {
            throw new NTLMEngineException("NTLM message decoding error - packet too short");
         }
      }

      protected void addByte(byte var1) {
         byte[] var3 = this.messageContents;
         int var2 = this.currentOutputPosition;
         var3[var2] = var1;
         this.currentOutputPosition = var2 + 1;
      }

      protected void addBytes(byte[] var1) {
         if (var1 != null) {
            int var4 = var1.length;

            for(int var3 = 0; var3 < var4; ++var3) {
               byte var2 = var1[var3];
               byte[] var6 = this.messageContents;
               int var5 = this.currentOutputPosition;
               var6[var5] = var2;
               this.currentOutputPosition = var5 + 1;
            }

         }
      }

      protected void addULong(int var1) {
         this.addByte((byte)(var1 & 255));
         this.addByte((byte)(var1 >> 8 & 255));
         this.addByte((byte)(var1 >> 16 & 255));
         this.addByte((byte)(var1 >> 24 & 255));
      }

      protected void addUShort(int var1) {
         this.addByte((byte)(var1 & 255));
         this.addByte((byte)(var1 >> 8 & 255));
      }

      protected void buildMessage() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Message builder not implemented for ");
         var1.append(this.getClass().getName());
         throw new RuntimeException(var1.toString());
      }

      public byte[] getBytes() {
         if (this.messageContents == null) {
            this.buildMessage();
         }

         byte[] var3 = this.messageContents;
         int var1 = var3.length;
         int var2 = this.currentOutputPosition;
         if (var1 > var2) {
            byte[] var4 = new byte[var2];
            System.arraycopy(var3, 0, var4, 0, var2);
            this.messageContents = var4;
         }

         return this.messageContents;
      }

      protected int getMessageLength() {
         return this.currentOutputPosition;
      }

      protected int getPreambleLength() {
         return NTLMEngineImpl.SIGNATURE.length + 4;
      }

      public String getResponse() {
         return new String(Base64.encodeBase64(this.getBytes()), Consts.ASCII);
      }

      protected void prepareResponse(int var1, int var2) {
         this.messageContents = new byte[var1];
         this.currentOutputPosition = 0;
         this.addBytes(NTLMEngineImpl.SIGNATURE);
         this.addULong(var2);
      }

      protected byte readByte(int var1) throws NTLMEngineException {
         byte[] var2 = this.messageContents;
         if (var2.length >= var1 + 1) {
            return var2[var1];
         } else {
            throw new NTLMEngineException("NTLM: Message too short");
         }
      }

      protected void readBytes(byte[] var1, int var2) throws NTLMEngineException {
         byte[] var3 = this.messageContents;
         if (var3.length >= var1.length + var2) {
            System.arraycopy(var3, var2, var1, 0, var1.length);
         } else {
            throw new NTLMEngineException("NTLM: Message too short");
         }
      }

      protected byte[] readSecurityBuffer(int var1) throws NTLMEngineException {
         return NTLMEngineImpl.readSecurityBuffer(this.messageContents, var1);
      }

      protected int readULong(int var1) throws NTLMEngineException {
         return NTLMEngineImpl.readULong(this.messageContents, var1);
      }

      protected int readUShort(int var1) throws NTLMEngineException {
         return NTLMEngineImpl.readUShort(this.messageContents, var1);
      }
   }

   static class Type1Message extends NTLMEngineImpl.NTLMMessage {
      private final byte[] domainBytes;
      private final int flags;
      private final byte[] hostBytes;

      Type1Message() {
         this.hostBytes = null;
         this.domainBytes = null;
         this.flags = this.getDefaultFlags();
      }

      Type1Message(String var1, String var2) throws NTLMEngineException {
         this(var1, var2, (Integer)null);
      }

      Type1Message(String var1, String var2, Integer var3) throws NTLMEngineException {
         int var4;
         if (var3 == null) {
            var4 = this.getDefaultFlags();
         } else {
            var4 = var3;
         }

         this.flags = var4;
         String var5 = NTLMEngineImpl.convertHost(var2);
         String var7 = NTLMEngineImpl.convertDomain(var1);
         var2 = null;
         byte[] var6;
         if (var5 != null) {
            var6 = var5.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED);
         } else {
            var6 = null;
         }

         this.hostBytes = var6;
         var6 = (byte[])var2;
         if (var7 != null) {
            var6 = var7.toUpperCase(Locale.ROOT).getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED);
         }

         this.domainBytes = var6;
      }

      private int getDefaultFlags() {
         return -1576500735;
      }

      protected void buildMessage() {
         int var1 = 0;
         byte[] var3 = this.domainBytes;
         if (var3 != null) {
            var1 = var3.length;
         }

         int var2 = 0;
         var3 = this.hostBytes;
         if (var3 != null) {
            var2 = var3.length;
         }

         this.prepareResponse(var2 + 40 + var1, 1);
         this.addULong(this.flags);
         this.addUShort(var1);
         this.addUShort(var1);
         this.addULong(var2 + 32 + 8);
         this.addUShort(var2);
         this.addUShort(var2);
         this.addULong(40);
         this.addUShort(261);
         this.addULong(2600);
         this.addUShort(3840);
         var3 = this.hostBytes;
         if (var3 != null) {
            this.addBytes(var3);
         }

         var3 = this.domainBytes;
         if (var3 != null) {
            this.addBytes(var3);
         }

      }
   }

   static class Type2Message extends NTLMEngineImpl.NTLMMessage {
      protected final byte[] challenge;
      protected final int flags;
      protected String target;
      protected byte[] targetInfo;

      Type2Message(String var1) throws NTLMEngineException {
         this(Base64.decodeBase64(var1.getBytes(NTLMEngineImpl.DEFAULT_CHARSET)));
      }

      Type2Message(byte[] var1) throws NTLMEngineException {
         super((byte[])var1, 2);
         var1 = new byte[8];
         this.challenge = var1;
         this.readBytes(var1, 24);
         this.flags = this.readULong(20);
         this.target = null;
         if (this.getMessageLength() >= 20) {
            var1 = this.readSecurityBuffer(12);
            if (var1.length != 0) {
               this.target = new String(var1, NTLMEngineImpl.getCharset(this.flags));
            }
         }

         this.targetInfo = null;
         if (this.getMessageLength() >= 48) {
            var1 = this.readSecurityBuffer(40);
            if (var1.length != 0) {
               this.targetInfo = var1;
            }
         }

      }

      byte[] getChallenge() {
         return this.challenge;
      }

      int getFlags() {
         return this.flags;
      }

      String getTarget() {
         return this.target;
      }

      byte[] getTargetInfo() {
         return this.targetInfo;
      }
   }

   static class Type3Message extends NTLMEngineImpl.NTLMMessage {
      protected final boolean computeMic;
      protected final byte[] domainBytes;
      protected final byte[] exportedSessionKey;
      protected final byte[] hostBytes;
      protected byte[] lmResp;
      protected byte[] ntResp;
      protected final byte[] sessionKey;
      protected final byte[] type1Message;
      protected final int type2Flags;
      protected final byte[] type2Message;
      protected final byte[] userBytes;

      Type3Message(String var1, String var2, String var3, String var4, byte[] var5, int var6, String var7, byte[] var8) throws NTLMEngineException {
         this(var1, var2, var3, var4, var5, var6, var7, var8, (Certificate)null, (byte[])null, (byte[])null);
      }

      Type3Message(String var1, String var2, String var3, String var4, byte[] var5, int var6, String var7, byte[] var8, Certificate var9, byte[] var10, byte[] var11) throws NTLMEngineException {
         this(NTLMEngineImpl.RND_GEN, System.currentTimeMillis(), var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      }

      Type3Message(Random var1, long var2, String var4, String var5, String var6, String var7, byte[] var8, int var9, String var10, byte[] var11) throws NTLMEngineException {
         this(var1, var2, var4, var5, var6, var7, var8, var9, var10, var11, (Certificate)null, (byte[])null, (byte[])null);
      }

      Type3Message(Random var1, long var2, String var4, String var5, String var6, String var7, byte[] var8, int var9, String var10, byte[] var11, Certificate var12, byte[] var13, byte[] var14) throws NTLMEngineException {
         if (var1 != null) {
            this.type2Flags = var9;
            this.type1Message = var13;
            this.type2Message = var14;
            String var29 = NTLMEngineImpl.convertHost(var5);
            var5 = NTLMEngineImpl.convertDomain(var4);
            byte[] var25;
            if (var12 != null) {
               var25 = this.addGssMicAvsToTargetInfo(var11, var12);
               this.computeMic = true;
            } else {
               this.computeMic = false;
               var25 = var11;
            }

            byte[] var24;
            NTLMEngineImpl.CipherGen var26;
            label114: {
               var26 = new NTLMEngineImpl.CipherGen(var1, var2, var5, var6, var7, var8, var10, var25);
               var4 = null;
               boolean var10001;
               if ((8388608 & var9) != 0 && var11 != null && var10 != null) {
                  label85: {
                     try {
                        this.ntResp = var26.getNTLMv2Response();
                        this.lmResp = var26.getLMv2Response();
                     } catch (NTLMEngineException var17) {
                        var10001 = false;
                        break label85;
                     }

                     if ((var9 & 128) != 0) {
                        try {
                           var24 = var26.getLanManagerSessionKey();
                           break label114;
                        } catch (NTLMEngineException var15) {
                           var10001 = false;
                        }
                     } else {
                        try {
                           var24 = var26.getNTLMv2UserSessionKey();
                           break label114;
                        } catch (NTLMEngineException var16) {
                           var10001 = false;
                        }
                     }
                  }
               } else if ((524288 & var9) != 0) {
                  label93: {
                     try {
                        this.ntResp = var26.getNTLM2SessionResponse();
                        this.lmResp = var26.getLM2SessionResponse();
                     } catch (NTLMEngineException var20) {
                        var10001 = false;
                        break label93;
                     }

                     if ((var9 & 128) != 0) {
                        try {
                           var24 = var26.getLanManagerSessionKey();
                           break label114;
                        } catch (NTLMEngineException var18) {
                           var10001 = false;
                        }
                     } else {
                        try {
                           var24 = var26.getNTLM2SessionResponseUserSessionKey();
                           break label114;
                        } catch (NTLMEngineException var19) {
                           var10001 = false;
                        }
                     }
                  }
               } else {
                  label101: {
                     try {
                        this.ntResp = var26.getNTLMResponse();
                        this.lmResp = var26.getLMResponse();
                     } catch (NTLMEngineException var23) {
                        var10001 = false;
                        break label101;
                     }

                     if ((var9 & 128) != 0) {
                        try {
                           var24 = var26.getLanManagerSessionKey();
                           break label114;
                        } catch (NTLMEngineException var21) {
                           var10001 = false;
                        }
                     } else {
                        try {
                           var24 = var26.getNTLMUserSessionKey();
                           break label114;
                        } catch (NTLMEngineException var22) {
                           var10001 = false;
                        }
                     }
                  }
               }

               this.ntResp = new byte[0];
               this.lmResp = var26.getLMResponse();
               if ((var9 & 128) != 0) {
                  var24 = var26.getLanManagerSessionKey();
               } else {
                  var24 = var26.getLMUserSessionKey();
               }
            }

            if ((var9 & 16) != 0) {
               if ((1073741824 & var9) != 0) {
                  byte[] var27 = var26.getSecondaryKey();
                  this.exportedSessionKey = var27;
                  this.sessionKey = NTLMEngineImpl.RC4(var27, var24);
               } else {
                  this.sessionKey = var24;
                  this.exportedSessionKey = var24;
               }
            } else {
               if (this.computeMic) {
                  throw new NTLMEngineException("Cannot sign/seal: no exported session key");
               }

               this.sessionKey = null;
               this.exportedSessionKey = null;
            }

            Charset var28 = NTLMEngineImpl.getCharset(var9);
            if (var29 != null) {
               var24 = var29.getBytes(var28);
            } else {
               var24 = null;
            }

            this.hostBytes = var24;
            var24 = (byte[])var4;
            if (var5 != null) {
               var24 = var5.toUpperCase(Locale.ROOT).getBytes(var28);
            }

            this.domainBytes = var24;
            this.userBytes = var6.getBytes(var28);
         } else {
            throw new NTLMEngineException("Random generator not available");
         }
      }

      private byte[] addGssMicAvsToTargetInfo(byte[] var1, Certificate var2) throws NTLMEngineException {
         byte[] var4 = new byte[var1.length + 8 + 20];
         int var3 = var1.length - 4;
         System.arraycopy(var1, 0, var4, 0, var3);
         NTLMEngineImpl.writeUShort(var4, 6, var3);
         NTLMEngineImpl.writeUShort(var4, 4, var3 + 2);
         NTLMEngineImpl.writeULong(var4, 2, var3 + 4);
         NTLMEngineImpl.writeUShort(var4, 10, var3 + 8);
         NTLMEngineImpl.writeUShort(var4, 16, var3 + 10);

         try {
            var1 = var2.getEncoded();
            var1 = MessageDigest.getInstance("SHA-256").digest(var1);
            byte[] var7 = new byte[NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT.length + 20 + var1.length];
            NTLMEngineImpl.writeULong(var7, 53, 16);
            System.arraycopy(NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT, 0, var7, 20, NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT.length);
            System.arraycopy(var1, 0, var7, NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT.length + 20, var1.length);
            var1 = NTLMEngineImpl.getMD5().digest(var7);
         } catch (CertificateEncodingException var5) {
            throw new NTLMEngineException(var5.getMessage(), var5);
         } catch (NoSuchAlgorithmException var6) {
            throw new NTLMEngineException(var6.getMessage(), var6);
         }

         System.arraycopy(var1, 0, var4, var3 + 12, 16);
         return var4;
      }

      protected void buildMessage() {
         int var5 = this.ntResp.length;
         int var6 = this.lmResp.length;
         byte[] var13 = this.domainBytes;
         int var1;
         if (var13 != null) {
            var1 = var13.length;
         } else {
            var1 = 0;
         }

         var13 = this.hostBytes;
         int var2;
         if (var13 != null) {
            var2 = var13.length;
         } else {
            var2 = 0;
         }

         int var7 = this.userBytes.length;
         var13 = this.sessionKey;
         int var3;
         if (var13 != null) {
            var3 = var13.length;
         } else {
            var3 = 0;
         }

         byte var4;
         if (this.computeMic) {
            var4 = 16;
         } else {
            var4 = 0;
         }

         int var14 = var4 + 72;
         int var8 = var14 + var6;
         int var9 = var8 + var5;
         int var10 = var9 + var1;
         int var11 = var10 + var7;
         int var12 = var11 + var2;
         this.prepareResponse(var12 + var3, 3);
         this.addUShort(var6);
         this.addUShort(var6);
         this.addULong(var14);
         this.addUShort(var5);
         this.addUShort(var5);
         this.addULong(var8);
         this.addUShort(var1);
         this.addUShort(var1);
         this.addULong(var9);
         this.addUShort(var7);
         this.addUShort(var7);
         this.addULong(var10);
         this.addUShort(var2);
         this.addUShort(var2);
         this.addULong(var11);
         this.addUShort(var3);
         this.addUShort(var3);
         this.addULong(var12);
         this.addULong(this.type2Flags);
         this.addUShort(261);
         this.addULong(2600);
         this.addUShort(3840);
         var1 = -1;
         if (this.computeMic) {
            var1 = this.currentOutputPosition;
            this.currentOutputPosition += 16;
         }

         this.addBytes(this.lmResp);
         this.addBytes(this.ntResp);
         this.addBytes(this.domainBytes);
         this.addBytes(this.userBytes);
         this.addBytes(this.hostBytes);
         var13 = this.sessionKey;
         if (var13 != null) {
            this.addBytes(var13);
         }

         if (this.computeMic) {
            NTLMEngineImpl.HMACMD5 var15 = new NTLMEngineImpl.HMACMD5(this.exportedSessionKey);
            var15.update(this.type1Message);
            var15.update(this.type2Message);
            var15.update(this.messageContents);
            var13 = var15.getOutput();
            System.arraycopy(var13, 0, this.messageContents, var1, var13.length);
         }
      }

      public byte[] getEncryptedRandomSessionKey() {
         return this.sessionKey;
      }

      public byte[] getExportedSessionKey() {
         return this.exportedSessionKey;
      }
   }
}
