package ch.imvs.sdes4j;

import java.util.Random;

public interface SDesFactory {
   CryptoAttribute createCryptoAttribute();

   CryptoSuite createCryptoSuite(String var1);

   KeyParam createKeyParam(String var1);

   KeyParam[] createKeyParamArray(int var1);

   SessionParam createSessionParam(String var1);

   SessionParam[] createSessionParamArray(int var1);

   void setRandomGenerator(Random var1);
}
