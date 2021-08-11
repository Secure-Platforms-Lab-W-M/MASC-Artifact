package ch.imvs.sdes4j.srtp;

import ch.imvs.sdes4j.CryptoAttribute;

public class SrtpCryptoAttribute extends CryptoAttribute {
   SrtpCryptoAttribute() {
   }

   public SrtpCryptoAttribute(int var1, SrtpCryptoSuite var2, SrtpKeyParam[] var3, SrtpSessionParam[] var4) {
      if (var4 == null) {
         var4 = new SrtpSessionParam[0];
      }

      super(var1, var2, var3, var4);
   }

   public static SrtpCryptoAttribute create(String var0) {
      return (SrtpCryptoAttribute)CryptoAttribute.create(var0, new SrtpSDesFactory());
   }

   public static SrtpCryptoAttribute create(String var0, String var1, String var2, String var3) {
      return (SrtpCryptoAttribute)CryptoAttribute.create(var0, var1, var2, var3, new SrtpSDesFactory());
   }

   public SrtpCryptoSuite getCryptoSuite() {
      return (SrtpCryptoSuite)super.getCryptoSuite();
   }

   public SrtpKeyParam[] getKeyParams() {
      return (SrtpKeyParam[])((SrtpKeyParam[])super.getKeyParams());
   }

   public SrtpSessionParam[] getSessionParams() {
      return (SrtpSessionParam[])((SrtpSessionParam[])super.getSessionParams());
   }
}
