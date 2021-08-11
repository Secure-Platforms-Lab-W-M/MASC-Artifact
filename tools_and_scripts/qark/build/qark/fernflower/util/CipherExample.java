package util;

public class CipherExample {
   private String cipherName = "AES/CBC/PKCS5Padding";

   public String getCipherName() {
      return this.cipherName;
   }

   public CipherExample methodA() {
      this.cipherName = "AES/CBC/PKCS5Padding";
      return this;
   }

   public CipherExample methodB() {
      this.cipherName = "DES";
      return this;
   }
}
