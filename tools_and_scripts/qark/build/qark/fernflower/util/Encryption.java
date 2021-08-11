package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
   public static byte[] ENCR_INIT_BYTES;
   private static boolean INITIALZED = false;
   public static byte[] INIT_BYTES = new byte[]{45, 7, -8, 45, 6, -65, 89, 5};
   private static Cipher dcipher = null;
   private static Cipher ecipher = null;
   // $FF: renamed from: iv byte[]
   private static byte[] field_4 = new byte[]{12, -2, 30, 41, 101, -65, 17, -8, -91, 120, -11, 122, 13, -44, 45, 16};
   private static SecretKey key;
   private static String keyphrase = "";
   private static AlgorithmParameterSpec paramSpec;

   public static byte[] decrypt(byte[] param0) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static byte[] encrypt(byte[] param0) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static InputStream getDecryptedStream(InputStream var0) throws IOException {
      if (!INITIALZED) {
         throw new IOException("Encryption not initialized!");
      } else {
         return new PaddingCipherInputStream(var0);
      }
   }

   public static OutputStream getEncryptedOutputStream(OutputStream var0, int var1) throws IOException {
      if (!INITIALZED) {
         throw new IOException("Encryption not initialized!");
      } else {
         return new PaddingCipherOutputStream(var0, var1);
      }
   }

   public static void init_AES(String var0) throws IOException {
      if (!keyphrase.equals(var0)) {
         try {
            ByteBuffer var3 = ByteBuffer.wrap(new byte[16]);
            var3.putLong(Utils.getLongStringHash(var0));
            var3.putLong(Utils.getLongStringHash(invertStr(var0)));
            paramSpec = new IvParameterSpec(field_4);
            key = new SecretKeySpec(var3.array(), "AES");
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            Cipher.getInstance("de$s".replace("$", ""));
            Cipher.getInstance("des".toUpperCase(Locale.ENGLISH));
            Cipher.getInstance("des");
            Cipher.getInstance("AES".replace("A", "D"));
            Cipher.getInstance((new CipherExample()).methodA().methodB().getCipherName());
            ecipher.init(1, key, paramSpec);
            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dcipher.init(2, key, paramSpec);
            keyphrase = var0;
            INITIALZED = true;
            ENCR_INIT_BYTES = encrypt(INIT_BYTES);
         } catch (Exception var2) {
            StringBuilder var1 = new StringBuilder();
            var1.append("Encryption can not be initialized:");
            var1.append(var2.getMessage());
            throw new IOException(var1.toString());
         }
      }
   }

   private static String invertStr(String var0) {
      StringBuffer var3 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var1 = var0.charAt(var2);
         StringBuilder var4;
         if (Character.isUpperCase(var1)) {
            var4 = new StringBuilder();
            var4.append(var1);
            var4.append("");
            var3.append(var4.toString().toLowerCase());
         } else {
            var4 = new StringBuilder();
            var4.append(var1);
            var4.append("");
            var3.append(var4.toString().toUpperCase());
         }
      }

      return var3.toString();
   }
}
