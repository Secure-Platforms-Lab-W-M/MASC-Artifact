package dnsfilter.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.Environment;
import android.os.Build.VERSION;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class BootUpReceiver extends BroadcastReceiver {
   public Properties getConfig() {
      StringBuilder var1 = new StringBuilder();
      var1.append(Environment.getExternalStorageDirectory().getAbsolutePath());
      var1.append("/PersonalDNSFilter/dnsfilter.conf");
      File var4 = new File(var1.toString());

      try {
         FileInputStream var5 = new FileInputStream(var4);
         Properties var2 = new Properties();
         var2.load(var5);
         var5.close();
         return var2;
      } catch (Exception var3) {
         return null;
      }
   }

   public void onReceive(Context var1, Intent var2) {
      Properties var3 = this.getConfig();
      if (var3 != null && Boolean.parseBoolean(var3.getProperty("AUTOSTART", "false"))) {
         if (VERSION.SDK_INT >= 28) {
            var2 = new Intent(var1, DNSFilterService.class);
            VpnService.prepare(var1);
            var1.startForegroundService(var2);
            return;
         }

         DNSProxyActivity.BOOT_START = true;
         var2 = new Intent(var1, DNSProxyActivity.class);
         var2.addFlags(268435456);
         var1.startActivity(var2);
      }

   }
}
