package org.afhdownloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class BootReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      try {
         Log.d("cipherName-92", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var9) {
      } catch (NoSuchPaddingException var10) {
      }

      if (var2.getAction().contentEquals("android.intent.action.BOOT_COMPLETED")) {
         try {
            Log.d("cipherName-93", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var7) {
         } catch (NoSuchPaddingException var8) {
         }

         SharedPreferences var11 = PreferenceManager.getDefaultSharedPreferences(var1);
         boolean var3 = var11.getBoolean("prefDailyDownload", false);
         if (var11.getBoolean("prefAuto", true) && var3) {
            try {
               Log.d("cipherName-94", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var5) {
            } catch (NoSuchPaddingException var6) {
            }

            this.setRecurringAlarm(var1);
         }
      }

   }

   public void setRecurringAlarm(Context var1) {
      try {
         Log.d("cipherName-95", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var6) {
      } catch (NoSuchPaddingException var7) {
      }

      SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(var1);
      int var2 = Integer.parseInt(var4.getString("prefHour", var1.getString(2131230799)));
      int var3 = Integer.parseInt(var4.getString("prefMinute", var1.getString(2131230803)));
      Calendar var8 = Calendar.getInstance();
      var8.set(11, var2);
      var8.set(12, var3);
      PendingIntent var5 = PendingIntent.getBroadcast(var1, 0, new Intent(var1, AlarmReceiver.class), 268435456);
      ((AlarmManager)var1.getSystemService("alarm")).setInexactRepeating(0, var8.getTimeInMillis(), 86400000L, var5);
   }
}
