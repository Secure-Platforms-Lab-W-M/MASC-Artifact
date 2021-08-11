package org.apache.commons.codec.digest;

public enum HmacAlgorithms {
   HMAC_MD5("HmacMD5"),
   HMAC_SHA_1("HmacSHA1"),
   HMAC_SHA_224("HmacSHA224"),
   HMAC_SHA_256("HmacSHA256"),
   HMAC_SHA_384("HmacSHA384"),
   HMAC_SHA_512;

   private final String name;

   static {
      HmacAlgorithms var0 = new HmacAlgorithms("HMAC_SHA_512", 5, "HmacSHA512");
      HMAC_SHA_512 = var0;
   }

   private HmacAlgorithms(String var3) {
      this.name = var3;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return this.name;
   }
}
