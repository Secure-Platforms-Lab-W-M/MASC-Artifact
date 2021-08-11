package ch.imvs.sdes4j.srtp;

import ch.imvs.sdes4j.CryptoSuite;

public class SrtpCryptoSuite implements CryptoSuite {
   public static final String AES_192_CM_HMAC_SHA1_32 = "AES_192_CM_HMAC_SHA1_32";
   public static final String AES_192_CM_HMAC_SHA1_80 = "AES_192_CM_HMAC_SHA1_80";
   public static final String AES_256_CM_HMAC_SHA1_32 = "AES_256_CM_HMAC_SHA1_32";
   public static final String AES_256_CM_HMAC_SHA1_80 = "AES_256_CM_HMAC_SHA1_80";
   public static final String AES_CM_128_HMAC_SHA1_32 = "AES_CM_128_HMAC_SHA1_32";
   public static final String AES_CM_128_HMAC_SHA1_80 = "AES_CM_128_HMAC_SHA1_80";
   public static final int ENCRYPTION_AES128_CM = 1;
   public static final int ENCRYPTION_AES128_F8 = 2;
   public static final int ENCRYPTION_AES192_CM = 8;
   public static final int ENCRYPTION_AES256_CM = 9;
   public static final int ENCRYPTION_SEED128_CCM_80 = 6;
   public static final int ENCRYPTION_SEED128_CTR = 5;
   public static final int ENCRYPTION_SEED128_GCM_96 = 7;
   public static final String F8_128_HMAC_SHA1_80 = "F8_128_HMAC_SHA1_80";
   public static final int HASH_HMAC_SHA1 = 1;
   public static final int HASH_SEED128_CCM_80 = 3;
   public static final int HASH_SEED128_GCM_96 = 4;
   public static final String SEED_128_CCM_80 = "SEED_128_CCM_80";
   public static final String SEED_128_GCM_96 = "SEED_128_GCM_96";
   public static final String SEED_CTR_128_HMAC_SHA1_80 = "SEED_CTR_128_HMAC_SHA1_80";
   private int encKeyLength;
   private int encryptionAlgorithm;
   private int hashAlgoritm;
   private int saltKeyLength;
   private int srtcpAuthKeyLength;
   private int srtcpAuthTagLength;
   private long srtcpLifetime;
   private int srtpAuthKeyLength;
   private int srtpAuthTagLength;
   private long srtpLifetime;
   private final String suite;

   public SrtpCryptoSuite(String var1) {
      this.suite = var1;
      if (var1.equals("AES_CM_128_HMAC_SHA1_80")) {
         this.encryptionAlgorithm = 1;
         this.hashAlgoritm = 1;
         this.encKeyLength = 128;
         this.saltKeyLength = 112;
         this.srtpAuthTagLength = 80;
         this.srtcpAuthTagLength = 80;
         this.srtpAuthKeyLength = 160;
         this.srtcpAuthKeyLength = 160;
         this.srtpLifetime = 281474976710656L;
         this.srtcpLifetime = 2147483648L;
      } else if (var1.equals("AES_CM_128_HMAC_SHA1_32")) {
         this.encryptionAlgorithm = 1;
         this.hashAlgoritm = 1;
         this.encKeyLength = 128;
         this.saltKeyLength = 112;
         this.srtpAuthTagLength = 32;
         this.srtcpAuthTagLength = 80;
         this.srtpAuthKeyLength = 160;
         this.srtcpAuthKeyLength = 160;
         this.srtpLifetime = 281474976710656L;
         this.srtcpLifetime = 2147483648L;
      } else if (var1.equals("F8_128_HMAC_SHA1_80")) {
         this.encryptionAlgorithm = 2;
         this.hashAlgoritm = 1;
         this.encKeyLength = 128;
         this.saltKeyLength = 112;
         this.srtpAuthTagLength = 80;
         this.srtcpAuthTagLength = 80;
         this.srtpAuthKeyLength = 160;
         this.srtcpAuthKeyLength = 160;
         this.srtpLifetime = 281474976710656L;
         this.srtcpLifetime = 2147483648L;
      } else if (!var1.equals("SEED_CTR_128_HMAC_SHA1_80")) {
         if (!var1.equals("SEED_128_CCM_80")) {
            if (!var1.equals("SEED_128_GCM_96")) {
               if (var1.equals("AES_192_CM_HMAC_SHA1_80")) {
                  this.encryptionAlgorithm = 8;
                  this.hashAlgoritm = 1;
                  this.encKeyLength = 192;
                  this.saltKeyLength = 112;
                  this.srtpAuthTagLength = 80;
                  this.srtcpAuthTagLength = 80;
                  this.srtpAuthKeyLength = 160;
                  this.srtcpAuthKeyLength = 160;
                  this.srtpLifetime = 2147483648L;
                  this.srtcpLifetime = 2147483648L;
               } else if (var1.equals("AES_192_CM_HMAC_SHA1_32")) {
                  this.encryptionAlgorithm = 8;
                  this.hashAlgoritm = 1;
                  this.encKeyLength = 192;
                  this.saltKeyLength = 112;
                  this.srtpAuthTagLength = 32;
                  this.srtcpAuthTagLength = 80;
                  this.srtpAuthKeyLength = 160;
                  this.srtcpAuthKeyLength = 160;
                  this.srtpLifetime = 2147483648L;
                  this.srtcpLifetime = 2147483648L;
               } else if (var1.equals("AES_256_CM_HMAC_SHA1_80")) {
                  this.encryptionAlgorithm = 9;
                  this.hashAlgoritm = 1;
                  this.encKeyLength = 256;
                  this.saltKeyLength = 112;
                  this.srtpAuthTagLength = 80;
                  this.srtcpAuthTagLength = 80;
                  this.srtpAuthKeyLength = 160;
                  this.srtcpAuthKeyLength = 160;
                  this.srtpLifetime = 2147483648L;
                  this.srtcpLifetime = 2147483648L;
               } else if (var1.equals("AES_256_CM_HMAC_SHA1_32")) {
                  this.encryptionAlgorithm = 9;
                  this.hashAlgoritm = 1;
                  this.encKeyLength = 256;
                  this.saltKeyLength = 112;
                  this.srtpAuthTagLength = 32;
                  this.srtcpAuthTagLength = 80;
                  this.srtpAuthKeyLength = 160;
                  this.srtcpAuthKeyLength = 160;
                  this.srtpLifetime = 2147483648L;
                  this.srtcpLifetime = 2147483648L;
               } else {
                  throw new IllegalArgumentException("Unknown crypto suite");
               }
            } else {
               this.encryptionAlgorithm = 7;
               this.hashAlgoritm = 4;
               this.encKeyLength = 128;
               this.saltKeyLength = 128;
               this.srtpAuthTagLength = 96;
               this.srtcpAuthTagLength = 96;
               this.srtpAuthKeyLength = -1;
               this.srtcpAuthKeyLength = -1;
               this.srtpLifetime = 281474976710656L;
               this.srtcpLifetime = 2147483648L;
               throw new UnsupportedOperationException("SEED parameters are not known for sure");
            }
         } else {
            this.encryptionAlgorithm = 6;
            this.hashAlgoritm = 3;
            this.encKeyLength = 128;
            this.saltKeyLength = 128;
            this.srtpAuthTagLength = 80;
            this.srtcpAuthTagLength = 80;
            this.srtpAuthKeyLength = -1;
            this.srtcpAuthKeyLength = -1;
            this.srtpLifetime = 281474976710656L;
            this.srtcpLifetime = 2147483648L;
            throw new UnsupportedOperationException("SEED parameters are not known for sure");
         }
      } else {
         this.encryptionAlgorithm = 5;
         this.hashAlgoritm = 1;
         this.encKeyLength = 128;
         this.saltKeyLength = 128;
         this.srtpAuthTagLength = 80;
         this.srtcpAuthTagLength = 80;
         this.srtpAuthKeyLength = -1;
         this.srtcpAuthKeyLength = -1;
         this.srtpLifetime = 281474976710656L;
         this.srtcpLifetime = 2147483648L;
         throw new UnsupportedOperationException("SEED parameters are not known for sure");
      }
   }

   public String encode() {
      return this.suite;
   }

   public boolean equals(Object var1) {
      return var1 instanceof SrtpCryptoSuite && var1 != null ? this.suite.equals(((SrtpCryptoSuite)var1).suite) : false;
   }

   public int getEncKeyLength() {
      return this.encKeyLength;
   }

   public int getEncryptionAlgorithm() {
      return this.encryptionAlgorithm;
   }

   public int getHashAlgorithm() {
      return this.hashAlgoritm;
   }

   public int getSaltKeyLength() {
      return this.saltKeyLength;
   }

   public int getSrtcpAuthKeyLength() {
      return this.srtcpAuthKeyLength;
   }

   public int getSrtcpAuthTagLength() {
      return this.srtcpAuthTagLength;
   }

   public long getSrtcpLifetime() {
      return this.srtcpLifetime;
   }

   public int getSrtpAuthKeyLength() {
      return this.srtpAuthKeyLength;
   }

   public int getSrtpAuthTagLength() {
      return this.srtpAuthTagLength;
   }

   public long getSrtpLifetime() {
      return this.srtpLifetime;
   }

   public int hashCode() {
      return this.suite.hashCode();
   }
}
