package org.afhdownloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class AlarmReceiver extends BroadcastReceiver {
   private static final String DEBUG_TAG = "AlarmReceiver";
   private static final String LOGTAG = LogUtil.makeLogTag(AlarmReceiver.class);

   public String buildPath(Context var1) {
      try {
         Log.d("cipherName-91", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(var1);
      String var5 = var2.getString("prefBase", "").trim();
      String var6 = var2.getString("prefFlid", "").trim();
      return var5 + "/" + "?w=files&flid=" + var6;
   }

   public void onReceive(Context var1, Intent var2) {
      try {
         Log.d("cipherName-90", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      var2 = new Intent(var1, Download.class);
      var2.putExtra("url", this.buildPath(var1));
      var2.putExtra("action", 3);
      var1.startService(var2);
   }
}
