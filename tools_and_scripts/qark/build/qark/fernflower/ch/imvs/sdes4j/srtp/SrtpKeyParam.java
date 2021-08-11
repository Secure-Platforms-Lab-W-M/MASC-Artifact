package ch.imvs.sdes4j.srtp;

import ch.imvs.sdes4j.KeyParam;
import org.apache.commons.codec.binary.ApacheBase64;
import org.apache.commons.codec.binary.StringUtils;

public class SrtpKeyParam implements KeyParam {
   public static final String KEYMETHOD_INLINE = "inline";
   private byte[] key;
   private final String keyMethod = "inline";
   private int lifetime;
   private int mki;
   private int mkiLength;

   public SrtpKeyParam(String var1) {
      if (var1.startsWith("inline:")) {
         String[] var5 = var1.substring("inline".length() + 1).split("\\|");
         byte[] var6 = ApacheBase64.decodeBase64(var5[0]);
         this.key = var6;
         if (var6.length != 0) {
            byte var3 = 1;
            int var2 = var3;
            if (var5.length > 1) {
               var2 = var3;
               if (!var5[1].contains(":")) {
                  if (var5[1].startsWith("2^")) {
                     this.lifetime = (int)Math.pow(2.0D, Double.valueOf(var5[1].substring(2)));
                  } else {
                     this.lifetime = Integer.valueOf(var5[1]);
                  }

                  var2 = 1 + 1;
               }
            }

            if (var5.length > var2 && var5[var2].contains(":")) {
               var5 = var5[var2].split(":");
               this.mki = Integer.valueOf(var5[0]);
               var2 = Integer.valueOf(var5[1]);
               this.mkiLength = var2;
               if (var2 < 1 || var2 > 128) {
                  throw new IllegalArgumentException("mki length must be in range 1..128 inclusive");
               }
            }
         } else {
            throw new IllegalArgumentException("key must be present");
         }
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unknown key method in <");
         var4.append(var1);
         var4.append(">");
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public SrtpKeyParam(String var1, byte[] var2, int var3, int var4, int var5) {
      if (var1.equals("inline")) {
         if (var5 >= 0 && var5 <= 128) {
            this.key = var2;
            this.lifetime = var3;
            this.mki = var4;
            this.mkiLength = var5;
         } else {
            throw new IllegalArgumentException("mki length must be in range 1..128 inclusive or 0 to indicate default");
         }
      } else {
         throw new IllegalArgumentException("key method must be inline");
      }
   }

   public String encode() {
      StringBuilder var1 = new StringBuilder();
      var1.append("inline");
      var1.append(':');
      var1.append(StringUtils.newStringUtf8(ApacheBase64.encodeBase64(this.key, false)));
      if (this.lifetime > 0) {
         var1.append('|');
         var1.append(this.lifetime);
      }

      if (this.mkiLength > 0) {
         var1.append('|');
         var1.append(this.mki);
         var1.append(':');
         var1.append(this.mkiLength);
      }

      return var1.toString();
   }

   public byte[] getKey() {
      return this.key;
   }

   public String getKeyMethod() {
      return "inline";
   }

   public int getLifetime() {
      return this.lifetime;
   }

   public int getMki() {
      return this.mki;
   }

   public int getMkiLength() {
      return this.mkiLength;
   }
}
