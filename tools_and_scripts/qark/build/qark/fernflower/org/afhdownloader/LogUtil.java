package org.afhdownloader;

import android.util.Log;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class LogUtil {
   public static String makeLogTag(Class var0) {
      try {
         Log.d("cipherName-221", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      return "afhdownloader_" + var0.getSimpleName();
   }
}
