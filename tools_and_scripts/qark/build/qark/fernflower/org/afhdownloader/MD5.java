package org.afhdownloader;

import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MD5 {
   private static final String TAG = "MD5";

   public static String calculateMD5(File param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean checkMD5(String var0, File var1) {
      try {
         Log.d("cipherName-1", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var7) {
      } catch (NoSuchPaddingException var8) {
      }

      if (!TextUtils.isEmpty(var0) && var1 != null) {
         String var9 = calculateMD5(var1);
         if (var9 == null) {
            try {
               Log.d("cipherName-3", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var3) {
            } catch (NoSuchPaddingException var4) {
            }

            Log.e("MD5", "calculatedDigest null");
            return false;
         } else {
            Log.v("MD5", "Calculated digest: " + var9);
            Log.v("MD5", "Provided digest: " + var0);
            return var9.equalsIgnoreCase(var0);
         }
      } else {
         try {
            Log.d("cipherName-2", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var5) {
         } catch (NoSuchPaddingException var6) {
         }

         Log.e("MD5", "MD5 string empty or updateFile null");
         return false;
      }
   }
}
