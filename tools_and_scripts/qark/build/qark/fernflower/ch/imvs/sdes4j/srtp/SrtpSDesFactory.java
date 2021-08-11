package ch.imvs.sdes4j.srtp;

import ch.imvs.sdes4j.SDesFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class SrtpSDesFactory implements SDesFactory {
   // $FF: renamed from: r java.util.Random
   private Random field_168 = null;

   private Random getRandom() {
      if (this.field_168 == null) {
         try {
            this.field_168 = SecureRandom.getInstance("SHA1PRNG");
         } catch (NoSuchAlgorithmException var2) {
            this.field_168 = new SecureRandom();
         }
      }

      return this.field_168;
   }

   public SrtpCryptoAttribute createCryptoAttribute() {
      return new SrtpCryptoAttribute();
   }

   public SrtpCryptoAttribute createCryptoAttribute(int var1, String var2) {
      return this.createCryptoAttribute(var1, var2, (SrtpSessionParam[])null);
   }

   public SrtpCryptoAttribute createCryptoAttribute(int var1, String var2, SrtpSessionParam[] var3) {
      SrtpCryptoSuite var5 = this.createCryptoSuite(var2);
      byte[] var4 = new byte[(var5.getEncKeyLength() + var5.getSaltKeyLength()) / 8];
      this.getRandom().nextBytes(var4);
      return new SrtpCryptoAttribute(var1, var5, new SrtpKeyParam[]{new SrtpKeyParam("inline", var4, 0, 0, 0)}, var3);
   }

   public SrtpCryptoSuite createCryptoSuite(String var1) {
      return new SrtpCryptoSuite(var1);
   }

   public SrtpKeyParam createKeyParam(String var1) {
      return new SrtpKeyParam(var1);
   }

   public SrtpKeyParam[] createKeyParamArray(int var1) {
      return new SrtpKeyParam[var1];
   }

   public SrtpSessionParam createSessionParam(String var1) {
      return SrtpSessionParam.create(var1);
   }

   public SrtpSessionParam[] createSessionParamArray(int var1) {
      return new SrtpSessionParam[var1];
   }

   public void setRandomGenerator(Random var1) {
      this.field_168 = var1;
   }
}
