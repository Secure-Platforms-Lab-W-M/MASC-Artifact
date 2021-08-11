package ch.imvs.sdes4j;

import java.util.LinkedList;
import java.util.List;

public class CryptoAttribute {
   protected CryptoSuite cryptoSuite;
   protected KeyParam[] keyParams;
   protected SessionParam[] sessionParams = null;
   protected int tag;

   protected CryptoAttribute() {
   }

   public CryptoAttribute(int var1, CryptoSuite var2, KeyParam[] var3, SessionParam[] var4) {
      if (var1 <= 99999999 && var1 >= 0) {
         if (var2 != null) {
            if (var3 != null && var3.length != 0) {
               this.tag = var1;
               this.cryptoSuite = var2;
               this.keyParams = var3;
               if (var4 == null) {
                  this.sessionParams = new SessionParam[0];
               } else {
                  this.sessionParams = var4;
               }
            } else {
               throw new IllegalArgumentException("keyParams cannot be null or empty");
            }
         } else {
            throw new IllegalArgumentException("cryptoSuite cannot be null");
         }
      } else {
         throw new IllegalArgumentException("tag can have at most 10 digits and must be non-negative");
      }
   }

   public static CryptoAttribute create(String var0, SDesFactory var1) {
      CryptoAttribute var4 = var1.createCryptoAttribute();
      LinkedList var5 = new LinkedList();
      String[] var7 = var0.split("\\s");
      int var3 = var7.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         String var6 = var7[var2];
         if (var6.trim().length() > 0) {
            var5.add(var6);
         }
      }

      var4.setTag((String)var5.remove(0));
      var4.setCryptoSuite((String)var5.remove(0), var1);
      if (var5.size() >= 1) {
         var4.setKeyParams((String)var5.remove(0), var1);
         var4.setSessionParams(var5, var1);
         return var4;
      } else {
         throw new IllegalArgumentException("There must be at least one key parameter");
      }
   }

   public static CryptoAttribute create(String var0, String var1, String var2, String var3, SDesFactory var4) {
      CryptoAttribute var7 = var4.createCryptoAttribute();
      var7.setTag(var0);
      var7.setCryptoSuite(var1, var4);
      var7.setKeyParams(var2, var4);
      LinkedList var8 = new LinkedList();
      if (var3 != null) {
         String[] var9 = var3.split("\\s");
         int var6 = var9.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            var2 = var9[var5];
            if (var2.trim().length() > 0) {
               var8.add(var2);
            }
         }
      }

      var7.setSessionParams(var8, var4);
      return var7;
   }

   private void setCryptoSuite(String var1, SDesFactory var2) {
      this.cryptoSuite = var2.createCryptoSuite(var1);
   }

   private void setKeyParams(String var1, SDesFactory var2) {
      String[] var6 = var1.split(";");
      LinkedList var5 = new LinkedList();
      int var4 = var6.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         var5.add(var2.createKeyParam(var6[var3]));
      }

      this.keyParams = (KeyParam[])var5.toArray(var2.createKeyParamArray(0));
   }

   private void setSessionParams(List var1, SDesFactory var2) {
      LinkedList var3 = new LinkedList();

      while(var1.size() > 0) {
         var3.add(var2.createSessionParam((String)var1.remove(0)));
      }

      this.sessionParams = (SessionParam[])var3.toArray(var2.createSessionParamArray(0));
   }

   private void setTag(String var1) {
      int var2 = Integer.valueOf(var1);
      if (var2 <= 99999999 && var2 >= 0) {
         this.tag = var2;
      } else {
         throw new IllegalArgumentException("tag can have at most 10 digits and must be non-negative");
      }
   }

   public String encode() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.tag);
      var1.append(' ');
      var1.append(this.cryptoSuite.encode());
      var1.append(' ');
      var1.append(this.getKeyParamsString());
      SessionParam[] var2 = this.sessionParams;
      if (var2 != null && var2.length > 0) {
         var1.append(' ');
         var1.append(this.getSessionParamsString());
      }

      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof CryptoAttribute) {
         CryptoAttribute var2 = (CryptoAttribute)var1;
         return this.encode().equals(var2.encode());
      } else {
         return false;
      }
   }

   public CryptoSuite getCryptoSuite() {
      return this.cryptoSuite;
   }

   public KeyParam[] getKeyParams() {
      return this.keyParams;
   }

   public String getKeyParamsString() {
      StringBuilder var2 = new StringBuilder();
      int var1 = 0;

      while(true) {
         KeyParam[] var3 = this.keyParams;
         if (var1 >= var3.length) {
            return var2.toString();
         }

         var2.append(var3[var1].encode());
         if (var1 < this.keyParams.length - 1) {
            var2.append(';');
         }

         ++var1;
      }
   }

   public SessionParam[] getSessionParams() {
      return this.sessionParams;
   }

   public String getSessionParamsString() {
      SessionParam[] var2 = this.sessionParams;
      if (var2 != null && var2.length > 0) {
         StringBuilder var4 = new StringBuilder();
         int var1 = 0;

         while(true) {
            SessionParam[] var3 = this.sessionParams;
            if (var1 >= var3.length) {
               return var4.toString();
            }

            var4.append(var3[var1].encode());
            if (var1 < this.sessionParams.length - 1) {
               var4.append(' ');
            }

            ++var1;
         }
      } else {
         return null;
      }
   }

   public int getTag() {
      return this.tag;
   }

   public int hashCode() {
      return this.encode().hashCode();
   }
}
