package util;

import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CipherExample {
    private String cipherName = "AES/CBC/PKCS5Padding";

    public CipherExample methodA() {
        cipherName = "AES/CBC/PKCS5Padding";
        return this;
    }

    public CipherExample methodB() {
        cipherName = "DES";
        return this;
    }

    public String getCipherName(){
        return cipherName;
    }

   /* public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher c = Cipher.getInstance(new CipherExample().methodA().methodB().getCipherName());

        System.out.println(c.getAlgorithm());
    }
   */
}
