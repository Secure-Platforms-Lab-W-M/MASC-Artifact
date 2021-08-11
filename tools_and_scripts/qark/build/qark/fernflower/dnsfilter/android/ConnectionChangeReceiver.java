package dnsfilter.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import util.ExecutionEnvironment;
import util.Logger;
import util.LoggerInterface;

public class ConnectionChangeReceiver extends BroadcastReceiver implements Runnable {
   private static ConnectionChangeReceiver instance = new ConnectionChangeReceiver();

   public static ConnectionChangeReceiver getInstance() {
      return instance;
   }

   public void onReceive(Context var1, Intent var2) {
      synchronized(this){}

      try {
         if (ExecutionEnvironment.getEnvironment().debug()) {
            LoggerInterface var8 = Logger.getLogger();
            StringBuilder var3 = new StringBuilder();
            var3.append("Received Network Connection Event: ");
            var3.append(var2.getAction());
            var8.logLine(var3.toString());
         }

         DNSFilterService.possibleNetworkChange();
         (new Thread(this)).start();
      } catch (Exception var6) {
         Logger.getLogger().logException(var6);
      } finally {
         ;
      }

   }

   public void run() {
      try {
         Thread.sleep(10000L);
         DNSFilterService.possibleNetworkChange();
      } catch (InterruptedException var2) {
         Logger.getLogger().logException(var2);
      }
   }
}
