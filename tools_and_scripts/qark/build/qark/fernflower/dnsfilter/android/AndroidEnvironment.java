package dnsfilter.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import util.ExecutionEnvironment;
import util.ExecutionEnvironmentInterface;
import util.Logger;

public class AndroidEnvironment implements ExecutionEnvironmentInterface {
   private static AndroidEnvironment INSTANCE = new AndroidEnvironment();
   private static Context ctx = null;
   private static Stack wakeLooks = new Stack();

   static {
      ExecutionEnvironment.setEnvironment(INSTANCE);
   }

   public static void initEnvironment(Context var0) {
      ctx = var0;
   }

   public boolean debug() {
      return DNSProxyActivity.debug;
   }

   public InputStream getAsset(String var1) throws IOException {
      return ctx.getAssets().open(var1);
   }

   public String getWorkDir() {
      StringBuilder var1 = new StringBuilder();
      var1.append(DNSProxyActivity.WORKPATH);
      var1.append("/");
      return var1.toString();
   }

   public boolean hasNetwork() {
      NetworkInfo var1 = ((ConnectivityManager)ctx.getSystemService("connectivity")).getActiveNetworkInfo();
      return var1 != null && var1.isConnected();
   }

   public void onReload() throws IOException {
      DNSFilterService.onReload();
   }

   public void releaseAllWakeLocks() {
      while(!wakeLooks.isEmpty()) {
         Object[] var2;
         try {
            var2 = (Object[])wakeLooks.pop();
         } catch (Exception var3) {
            Logger.getLogger().logException(var3);
            return;
         }

         WifiLock var1 = (WifiLock)var2[0];
         WakeLock var4 = (WakeLock)var2[1];
         var1.release();
         var4.release();
         Logger.getLogger().logLine("Released WIFI lock and partial wake lock!");
      }

   }

   public void releaseWakeLock() {
      Object[] var2;
      try {
         var2 = (Object[])wakeLooks.pop();
      } catch (Exception var3) {
         Logger.getLogger().logException(var3);
         return;
      }

      WifiLock var1 = (WifiLock)var2[0];
      WakeLock var4 = (WakeLock)var2[1];
      var1.release();
      var4.release();
      Logger.getLogger().logLine("Released WIFI lock and partial wake lock!");
   }

   public void wakeLock() {
      WifiLock var1 = ((WifiManager)ctx.getApplicationContext().getSystemService("wifi")).createWifiLock(1, "personalHttpProxy");
      var1.acquire();
      WakeLock var2 = ((PowerManager)ctx.getSystemService("power")).newWakeLock(1, "personalHttpProxy");
      var2.acquire();
      wakeLooks.push(new Object[]{var1, var2});
      Logger.getLogger().logLine("Aquired WIFI lock and partial wake lock!");
   }
}
