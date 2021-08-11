package dnsfilter.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.VpnService;
import android.net.VpnService.Builder;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import dnsfilter.ConfigurationAccess;
import dnsfilter.DNSFilterManager;
import dnsfilter.DNSFilterProxy;
import dnsfilter.DNSResolver;
import dnsfilter.DNSServer;
import ip.IPPacket;
import ip.UDPPacket;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import util.ExecutionEnvironment;
import util.Logger;
import util.LoggerInterface;
import util.Utils;

public class DNSFilterService extends VpnService {
   private static String ADDRESS_IPV4 = "10.0.2.15";
   private static String ADDRESS_IPV6 = "fdc8:1095:91e1:aaaa:aaaa:aaaa:aaaa:aaa2";
   public static DNSFilterManager DNSFILTER = null;
   public static DNSFilterProxy DNSFILTERPROXY = null;
   private static boolean DNS_PROXY_PORT_IS_REDIRECTED = false;
   private static DNSFilterService INSTANCE = null;
   private static boolean JUST_STARTED = false;
   private static String KILL_DNSCRYPTPROXY = "killall dnscrypt-proxy";
   protected static Intent SERVICE = null;
   private static String START_DNSCRYPTPROXY = "dnscrypt-proxy";
   private static String VIRTUALDNS_IPV4 = "10.10.10.10";
   private static String VIRTUALDNS_IPV6 = "fdc8:1095:91e1:aaaa:aaaa:aaaa:aaaa:aaa1";
   private static boolean dnsProxyMode = false;
   protected static DNSFilterService.DNSReqForwarder dnsReqForwarder = new DNSFilterService.DNSReqForwarder();
   private static boolean is_running = false;
   private static boolean rootMode = false;
   private static int startCounter = 0;
   private static boolean vpnInAdditionToProxyMode = false;
   private boolean blocking = false;
   boolean dnsCryptProxyStartTriggered = false;
   boolean manageDNSCryptProxy = false;
   private int mtu;
   PendingIntent pendingIntent;
   private DNSFilterService.VPNRunner vpnRunner = null;

   public static void detectDNSServers() {
      // $FF: Couldn't be decompiled
   }

   @SuppressLint({"NewApi"})
   private void excludeApp(String var1, Builder var2) {
      try {
         var2.addDisallowedApplication(var1);
      } catch (NameNotFoundException var4) {
         LoggerInterface var5 = Logger.getLogger();
         StringBuilder var3 = new StringBuilder();
         var3.append("Error during app whitelisting:");
         var3.append(var4.getMessage());
         var5.logLine(var3.toString());
      }
   }

   private String getChannel() {
      NotificationManager var1 = (NotificationManager)this.getSystemService("notification");
      if (VERSION.SDK_INT >= 26) {
         var1.createNotificationChannel(new NotificationChannel("DNS Filter", "DNS Filter", 3));
      }

      return "DNS Filter";
   }

   @TargetApi(21)
   private static Network[] getConnectedNetworks(ConnectivityManager var0, int var1) {
      ArrayList var3 = new ArrayList();
      Network[] var4 = var0.getAllNetworks();

      for(int var2 = 0; var2 < var4.length; ++var2) {
         NetworkInfo var5 = var0.getNetworkInfo(var4[var2]);
         if (var5 != null && (var5.getType() == var1 || var1 == -1) && var5.isConnected()) {
            var3.add(var4[var2]);
         }
      }

      return (Network[])var3.toArray(new Network[var3.size()]);
   }

   @TargetApi(21)
   private static String[] getDNSviaConnectivityManager() {
      if (VERSION.SDK_INT < 21) {
         return new String[0];
      } else {
         HashSet var5 = new HashSet();
         ConnectivityManager var6 = (ConnectivityManager)INSTANCE.getSystemService("connectivity");
         Network[] var4 = getConnectedNetworks(var6, 1);
         Network[] var3 = var4;
         if (var4.length == 0) {
            var3 = getConnectedNetworks(var6, -1);
         }

         int var2 = var3.length;

         for(int var0 = 0; var0 < var2; ++var0) {
            Network var7 = var3[var0];
            var6.getNetworkInfo(var7);
            List var8 = var6.getLinkProperties(var7).getDnsServers();

            for(int var1 = 0; var1 < var8.size(); ++var1) {
               var5.add(((InetAddress)var8.get(var1)).getHostAddress());
            }
         }

         return (String[])var5.toArray(new String[var5.size()]);
      }
   }

   private static String[] getDNSviaSysProps() {
      Exception var10000;
      label52: {
         HashSet var2;
         Method var3;
         String[] var4;
         boolean var10001;
         try {
            var2 = new HashSet();
            var3 = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            var4 = new String[4];
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label52;
         }

         var4[0] = "net.dns1";
         var4[1] = "net.dns2";
         var4[2] = "net.dns3";
         var4[3] = "net.dns4";

         int var1;
         try {
            var1 = var4.length;
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label52;
         }

         for(int var0 = 0; var0 < var1; ++var0) {
            String var5;
            try {
               var5 = (String)var3.invoke((Object)null, var4[var0]);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label52;
            }

            if (var5 != null) {
               try {
                  if (!var5.equals("")) {
                     var2.add(var5);
                  }
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label52;
               }
            }
         }

         try {
            String[] var12 = (String[])var2.toArray(new String[var2.size()]);
            return var12;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var11 = var10000;
      Logger.getLogger().logException(var11);
      return new String[0];
   }

   private ParcelFileDescriptor initVPN() throws Exception {
      this.mtu = Integer.parseInt(ConfigurationAccess.getLocal().getConfig().getProperty("MTU", "3000"));
      Builder var5 = new Builder(this);
      var5.setSession("DNS Filter");
      if (supportsIPVersion(4)) {
         var5.addAddress(ADDRESS_IPV4, 24).addDnsServer(VIRTUALDNS_IPV4).addRoute(VIRTUALDNS_IPV4, 32);
      }

      if (supportsIPVersion(6)) {
         var5.addAddress(ADDRESS_IPV6, 48).addDnsServer(VIRTUALDNS_IPV6).addRoute(VIRTUALDNS_IPV6, 128);
      }

      StringTokenizer var6 = new StringTokenizer(DNSFILTER.getConfig().getProperty("routeIPs", ""), ";");
      int var2 = var6.countTokens();
      int var1 = var2;
      if (var2 != 0) {
         var1 = var2;
         if (VERSION.SDK_INT < 21) {
            var1 = 0;
            Logger.getLogger().logLine("WARNING!: Setting 'routeIPs' not supported for Android version below 5.01!\n Setting ignored!");
         }
      }

      byte var4 = 0;

      for(var2 = 0; var2 < var1; ++var2) {
         String var7 = var6.nextToken().trim();
         LoggerInterface var8 = Logger.getLogger();
         StringBuilder var9 = new StringBuilder();
         var9.append("Additional route IP:");
         var9.append(var7);
         var8.logLine(var9.toString());

         UnknownHostException var10000;
         label90: {
            InetAddress var16;
            boolean var10001;
            try {
               var16 = InetAddress.getByName(var7);
            } catch (UnknownHostException var12) {
               var10000 = var12;
               var10001 = false;
               break label90;
            }

            short var3 = 32;

            label76: {
               try {
                  if (!(var16 instanceof Inet6Address)) {
                     break label76;
                  }
               } catch (UnknownHostException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label90;
               }

               var3 = 128;
            }

            try {
               var5.addRoute(InetAddress.getByName(var7), var3);
               continue;
            } catch (UnknownHostException var10) {
               var10000 = var10;
               var10001 = false;
            }
         }

         UnknownHostException var14 = var10000;
         Logger.getLogger().logException(var14);
      }

      if (VERSION.SDK_INT >= 21) {
         var5.addDisallowedApplication("dnsfilter.android");
      }

      var6 = new StringTokenizer(DNSFILTER.getConfig().getProperty("androidAppWhiteList", ""), ",");
      var2 = var6.countTokens();
      var1 = var2;
      if (var2 != 0) {
         var1 = var2;
         if (VERSION.SDK_INT < 21) {
            var1 = 0;
            Logger.getLogger().logLine("WARNING!: Application whitelisting not supported for Android version below 5.01!\n Setting ignored!");
         }
      }

      for(var2 = var4; var2 < var1; ++var2) {
         this.excludeApp(var6.nextToken().trim(), var5);
      }

      if (VERSION.SDK_INT >= 24 && VERSION.SDK_INT <= 27) {
         LoggerInterface var13 = Logger.getLogger();
         StringBuilder var15 = new StringBuilder();
         var15.append("Running on SDK");
         var15.append(VERSION.SDK_INT);
         var13.logLine(var15.toString());
         this.excludeApp("com.android.vending", var5);
         this.excludeApp("com.google.android.apps.docs", var5);
         this.excludeApp("com.google.android.apps.photos", var5);
         this.excludeApp("com.google.android.gm", var5);
         this.excludeApp("com.google.android.apps.translate", var5);
      }

      if (VERSION.SDK_INT >= 21) {
         var5.setBlocking(true);
         Logger.getLogger().logLine("Using Blocking Mode!");
         this.blocking = true;
      }

      var5.setMtu(this.mtu);
      return var5.setConfigureIntent(this.pendingIntent).establish();
   }

   public static void onReload() throws IOException {
      DNSFilterService var0 = INSTANCE;
      if (var0 != null) {
         var0.reload();
      } else {
         throw new IOException("Service instance is null!");
      }
   }

   public static void possibleNetworkChange() {
      detectDNSServers();
      if (rootMode) {
         dnsReqForwarder.updateForward();
      }

   }

   private static void runOSCommand(boolean var0, String var1) throws Exception {
      LoggerInterface var3 = Logger.getLogger();
      StringBuilder var4 = new StringBuilder();
      var4.append("Exec '");
      var4.append(var1);
      var4.append("' !");
      var3.logLine(var4.toString());
      Process var8 = Runtime.getRuntime().exec("su");
      DataOutputStream var9 = new DataOutputStream(var8.getOutputStream());
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append("\n");
      var9.writeBytes(var5.toString());
      var9.flush();
      var9.writeBytes("exit\n");
      var9.flush();
      InputStream var10 = var8.getInputStream();
      byte[] var6 = new byte[1024];

      while(true) {
         int var2 = var10.read(var6);
         if (var2 == -1) {
            var10 = var8.getErrorStream();

            while(true) {
               var2 = var10.read(var6);
               if (var2 == -1) {
                  var8.waitFor();
                  var2 = var8.exitValue();
                  if (var2 != 0 && !var0) {
                     StringBuilder var7 = new StringBuilder();
                     var7.append("Error in process execution: ");
                     var7.append(var2);
                     throw new Exception(var7.toString());
                  } else {
                     return;
                  }
               }

               Logger.getLogger().log(new String(var6, 0, var2));
            }
         }

         Logger.getLogger().log(new String(var6, 0, var2));
      }
   }

   private static void runOSCommand(final boolean var0, boolean var1, final String var2) throws Exception {
      if (!var1) {
         runOSCommand(var0, var2);
      } else {
         (new Thread(new Runnable() {
            public void run() {
               try {
                  DNSFilterService.runOSCommand(var0, var2);
               } catch (Exception var2x) {
                  var2x.printStackTrace();
                  Logger.getLogger().logException(var2x);
               }
            }
         })).start();
      }
   }

   private void setUpPortRedir() {
      if (!DNS_PROXY_PORT_IS_REDIRECTED) {
         try {
            runOSCommand(false, "iptables -t nat -A PREROUTING -p udp --dport 53 -j REDIRECT --to-port 5300");
            DNS_PROXY_PORT_IS_REDIRECTED = true;
         } catch (Exception var4) {
            LoggerInterface var2 = Logger.getLogger();
            StringBuilder var3 = new StringBuilder();
            var3.append("Exception during setting Port redirection:");
            var3.append(var4.toString());
            var2.logLine(var3.toString());
         }
      }
   }

   private boolean shutdown() {
      if (!is_running) {
         return true;
      } else {
         Exception var10000;
         Exception var1;
         label80: {
            boolean var10001;
            try {
               if (DNSFILTER != null && !DNSFILTER.canStop()) {
                  Logger.getLogger().logLine("Cannot stop - pending operation!");
                  return false;
               }
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label80;
            }

            try {
               this.unregisterReceiver(ConnectionChangeReceiver.getInstance());
            } catch (Exception var8) {
               var1 = var8;

               try {
                  var1.printStackTrace();
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label80;
               }
            }

            try {
               if (rootMode) {
                  dnsReqForwarder.clearForward();
               }
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label80;
            }

            try {
               if (this.vpnRunner != null) {
                  this.vpnRunner.stop();
               }
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
               break label80;
            }

            try {
               if (DNSFILTERPROXY != null) {
                  DNSFILTERPROXY.stop();
                  DNSFILTERPROXY = null;
                  Logger.getLogger().logLine("DNSFilterProxy Mode stopped!");
               }
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label80;
            }

            try {
               if (DNSFILTER != null) {
                  DNSFILTER.stop();
                  DNSFILTER = null;
                  Logger.getLogger().logLine("DNSFilter stopped!");
               }
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
               break label80;
            }

            try {
               this.stopService(SERVICE);
               SERVICE = null;
               is_running = false;
               Thread.sleep(200L);
               return true;
            } catch (Exception var2) {
               var10000 = var2;
               var10001 = false;
            }
         }

         var1 = var10000;
         Logger.getLogger().logException(var1);
         return false;
      }
   }

   public static boolean stop(boolean var0) {
      DNSFilterService var1 = INSTANCE;
      if (var1 == null) {
         return true;
      } else {
         if (var1.manageDNSCryptProxy && var0) {
            try {
               runOSCommand(false, KILL_DNSCRYPTPROXY);
               var1.dnsCryptProxyStartTriggered = false;
            } catch (Exception var3) {
               Logger.getLogger().logException(var3);
            }
         }

         if (var1.shutdown()) {
            INSTANCE = null;
            return true;
         } else {
            return false;
         }
      }
   }

   private static boolean supportsIPVersion(int var0) throws Exception {
      String var1 = DNSFilterManager.getInstance().getConfig().getProperty("ipVersionSupport", "4, 6");
      StringBuilder var2 = new StringBuilder();
      var2.append("");
      var2.append(var0);
      return var1.indexOf(var2.toString()) != -1;
   }

   public void onDestroy() {
      Logger.getLogger().logLine("destroyed");
      this.shutdown();
      super.onDestroy();
   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      AndroidEnvironment.initEnvironment(this);
      INSTANCE = this;
      SERVICE = var1;
      Exception var10000;
      boolean var10001;
      Exception var23;
      if (DNSFILTER != null) {
         Logger.getLogger().logLine("DNS Filter already running!");
      } else {
         label163: {
            label157: {
               StringBuilder var22;
               try {
                  var22 = new StringBuilder();
                  var22.append(DNSProxyActivity.WORKPATH.getAbsolutePath());
                  var22.append("/");
                  DNSFilterManager.WORKDIR = var22.toString();
                  DNSFILTER = DNSFilterManager.getInstance();
                  DNSFILTER.init();
                  JUST_STARTED = true;
                  dnsProxyMode = Boolean.parseBoolean(DNSFILTER.getConfig().getProperty("dnsProxyOnAndroid", "false"));
                  rootMode = Boolean.parseBoolean(DNSFILTER.getConfig().getProperty("rootModeOnAndroid", "false"));
                  vpnInAdditionToProxyMode = Boolean.parseBoolean(DNSFILTER.getConfig().getProperty("vpnInAdditionToProxyMode", "false"));
                  if (rootMode && !dnsProxyMode) {
                     rootMode = false;
                     Logger.getLogger().logLine("WARNING! Root Mode only possible in combination with DNS Proxy Mode!");
                  }
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label157;
               }

               try {
                  if (rootMode) {
                     dnsReqForwarder.clean();
                     dnsReqForwarder.updateForward();
                  }
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label157;
               }

               label144: {
                  try {
                     this.registerReceiver(ConnectionChangeReceiver.getInstance(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                     detectDNSServers();
                     if (!dnsProxyMode) {
                        break label144;
                     }

                     if (rootMode) {
                        this.setUpPortRedir();
                     }
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label157;
                  }

                  try {
                     DNSFILTERPROXY = new DNSFilterProxy(5300);
                     (new Thread(DNSFILTERPROXY)).start();
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label157;
                  }
               }

               label136: {
                  boolean var4;
                  try {
                     this.manageDNSCryptProxy = Boolean.parseBoolean(DNSFILTER.getConfig().getProperty("manageDNSCryptProxy", "false"));
                     if (!this.manageDNSCryptProxy) {
                        break label136;
                     }

                     var4 = this.dnsCryptProxyStartTriggered;
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label157;
                  }

                  if (!var4) {
                     try {
                        runOSCommand(true, false, KILL_DNSCRYPTPROXY);
                        var22 = new StringBuilder();
                        var22.append(START_DNSCRYPTPROXY);
                        var22.append(" ");
                        var22.append(DNSFILTER.getConfig().getProperty("dnsCryptProxyStartOptions", ""));
                        runOSCommand(false, true, var22.toString());
                        this.dnsCryptProxyStartTriggered = true;
                     } catch (Exception var16) {
                        var23 = var16;

                        try {
                           Logger.getLogger().logException(var23);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label157;
                        }
                     }
                  }
               }

               try {
                  is_running = true;
                  break label163;
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
               }
            }

            var23 = var10000;
            DNSFILTER = null;
            Logger.getLogger().logException(var23);
            return 1;
         }
      }

      label159: {
         label160: {
            try {
               this.pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, DNSProxyActivity.class), 0);
               if (dnsProxyMode && !vpnInAdditionToProxyMode) {
                  break label160;
               }
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label159;
            }

            ParcelFileDescriptor var24;
            try {
               var24 = this.initVPN();
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label159;
            }

            if (var24 != null) {
               try {
                  var2 = startCounter + 1;
                  startCounter = var2;
                  this.vpnRunner = new DNSFilterService.VPNRunner(var2, var24);
                  (new Thread(this.vpnRunner)).start();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label159;
               }
            } else {
               try {
                  Logger.getLogger().logLine("Error! Cannot get VPN Interface! Try restart!");
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label159;
               }
            }
         }

         Notification var26;
         label98: {
            label161: {
               android.app.Notification.Builder var25;
               label165: {
                  try {
                     if (VERSION.SDK_INT < 16) {
                        break label161;
                     }

                     if (VERSION.SDK_INT >= 26) {
                        var25 = new android.app.Notification.Builder(this, this.getChannel());
                        break label165;
                     }
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label159;
                  }

                  try {
                     var25 = new android.app.Notification.Builder(this);
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label159;
                  }
               }

               try {
                  var26 = var25.setContentTitle("DNSFilter is running!").setSmallIcon(2130771978).setContentIntent(this.pendingIntent).build();
                  break label98;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label159;
               }
            }

            try {
               var26 = new Notification(2130771978, "DNSFilter is running!", 0L);
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label159;
            }
         }

         try {
            this.startForeground(1, var26);
            return 1;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      var23 = var10000;
      Logger.getLogger().logException(var23);
      return 1;
   }

   public void reload() throws IOException {
      if (!dnsProxyMode || vpnInAdditionToProxyMode) {
         if (this.vpnRunner != null) {
            this.vpnRunner.stop();
         }

         DNSFILTER = DNSFilterManager.getInstance();

         ParcelFileDescriptor var2;
         try {
            var2 = this.initVPN();
         } catch (Exception var3) {
            throw new IOException("Cannot initialize VPN!", var3);
         }

         if (var2 == null) {
            throw new IOException("Error! Cannot get VPN Interface! Try restart!");
         }

         int var1 = startCounter + 1;
         startCounter = var1;
         this.vpnRunner = new DNSFilterService.VPNRunner(var1, var2);
         (new Thread(this.vpnRunner)).start();
      }

      JUST_STARTED = true;
      detectDNSServers();
   }

   protected static class DNSReqForwarder {
      String forwardip = null;
      String ipFilePath;

      protected DNSReqForwarder() {
         StringBuilder var1 = new StringBuilder();
         var1.append(ExecutionEnvironment.getEnvironment().getWorkDir());
         var1.append("forward_ip");
         this.ipFilePath = var1.toString();
      }

      public void clean() {
         File var2 = new File(this.ipFilePath);

         try {
            if (var2.exists()) {
               FileInputStream var3 = new FileInputStream(var2);
               String var1 = new String(Utils.readFully(var3, 100));
               var3.close();
               if (!var2.delete()) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Cannot delete ");
                  var5.append(this.ipFilePath);
                  throw new IOException(var5.toString());
               }

               Logger.getLogger().logLine("Cleaning up a previous redirect from previous not correctly terminated execution!");
               StringBuilder var6 = new StringBuilder();
               var6.append("iptables -t nat -D OUTPUT -p udp --dport 53 -j DNAT --to-destination ");
               var6.append(var1);
               var6.append(":5300");
               DNSFilterService.runOSCommand(false, var6.toString());
            }

         } catch (Exception var4) {
            Logger.getLogger().logLine(var4.toString());
         }
      }

      public void clearForward() {
         // $FF: Couldn't be decompiled
      }

      public String getALocalIpAddress() throws IOException {
         String var1 = null;
         Enumeration var4 = NetworkInterface.getNetworkInterfaces();

         while(var4.hasMoreElements()) {
            NetworkInterface var5 = (NetworkInterface)var4.nextElement();

            String var2;
            for(Enumeration var6 = var5.getInetAddresses(); var6.hasMoreElements(); var1 = var2) {
               InetAddress var3 = (InetAddress)var6.nextElement();
               var2 = var1;
               if (!var3.isLoopbackAddress()) {
                  var2 = var1;
                  if (var3 instanceof Inet4Address) {
                     String var7 = var3.getHostAddress();
                     if (var5.getName().startsWith("tun")) {
                        return var7;
                     }

                     var2 = var1;
                     if (var1 == null) {
                        var2 = var7;
                     }
                  }
               }
            }
         }

         return var1;
      }

      public void updateForward() {
         // $FF: Couldn't be decompiled
      }
   }

   class VPNRunner implements Runnable {
      // $FF: renamed from: id int
      int field_6;
      // $FF: renamed from: in java.io.FileInputStream
      FileInputStream field_7;
      FileOutputStream out;
      boolean stopped;
      Thread thread;
      ParcelFileDescriptor vpnInterface;

      private VPNRunner(int var2, ParcelFileDescriptor var3) {
         this.field_7 = null;
         this.out = null;
         this.thread = null;
         this.stopped = false;
         this.field_6 = var2;
         this.vpnInterface = var3;
         this.field_7 = new FileInputStream(var3.getFileDescriptor());
         this.out = new FileOutputStream(var3.getFileDescriptor());
         Logger.getLogger().logLine("VPN Connected!");
      }

      // $FF: synthetic method
      VPNRunner(int var2, ParcelFileDescriptor var3, Object var4) {
         this(var2, var3);
      }

      private void stop() {
         this.stopped = true;

         try {
            this.field_7.close();
            this.out.close();
            this.vpnInterface.close();
            if (this.thread != null) {
               this.thread.interrupt();
            }

         } catch (Exception var2) {
            Logger.getLogger().logException(var2);
         }
      }

      public void run() {
         LoggerInterface var4 = Logger.getLogger();
         StringBuilder var5 = new StringBuilder();
         var5.append("VPN Runner Thread ");
         var5.append(this.field_6);
         var5.append(" started!");
         var4.logLine(var5.toString());
         this.thread = Thread.currentThread();

         label150: {
            Exception var10000;
            Exception var28;
            label149: {
               int var2;
               boolean var10001;
               try {
                  var2 = Integer.parseInt(DNSFilterManager.getInstance().getConfig().getProperty("maxResolverCount", "100"));
               } catch (Exception var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label149;
               }

               while(true) {
                  int var3;
                  byte[] var26;
                  try {
                     if (this.stopped) {
                        break label150;
                     }

                     var26 = new byte[DNSServer.getBufSize()];
                     var3 = this.field_7.read(var26);
                     if (this.stopped) {
                        break label150;
                     }
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break;
                  }

                  boolean var1 = false;

                  StringBuilder var6;
                  LoggerInterface var29;
                  label143: {
                     try {
                        if (DNSResolver.getResolverCount() <= var2) {
                           break label143;
                        }

                        var29 = Logger.getLogger();
                        var6 = new StringBuilder();
                        var6.append("Max Resolver Count reached: ");
                        var6.append(var2);
                        var29.message(var6.toString());
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break;
                     }

                     var1 = true;
                  }

                  if (var3 <= 0) {
                     try {
                        if (!DNSFilterService.this.blocking) {
                           Thread.sleep(1000L);
                        }
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break;
                     }
                  } else {
                     label155: {
                        IOException var33;
                        label156: {
                           StringBuilder var7;
                           IPPacket var31;
                           LoggerInterface var32;
                           label134: {
                              try {
                                 var31 = new IPPacket(var26, 0, var3);
                                 if (var31.getVersion() != 6 || !DNSProxyActivity.debug) {
                                    break label134;
                                 }

                                 var32 = Logger.getLogger();
                                 var7 = new StringBuilder();
                                 var7.append("!!!IPV6 Packet!!! Protocol:");
                                 var7.append(var31.getProt());
                                 var32.logLine(var7.toString());
                                 var32 = Logger.getLogger();
                                 var7 = new StringBuilder();
                                 var7.append("SourceAddress:");
                                 var7.append(IPPacket.int2ip(var31.getSourceIP()));
                                 var32.logLine(var7.toString());
                                 var32 = Logger.getLogger();
                                 var7 = new StringBuilder();
                                 var7.append("DestAddress:");
                                 var7.append(IPPacket.int2ip(var31.getDestIP()));
                                 var32.logLine(var7.toString());
                                 var32 = Logger.getLogger();
                                 var7 = new StringBuilder();
                                 var7.append("TTL:");
                                 var7.append(var31.getTTL());
                                 var32.logLine(var7.toString());
                                 var32 = Logger.getLogger();
                                 var7 = new StringBuilder();
                                 var7.append("Length:");
                                 var7.append(var31.getLength());
                                 var32.logLine(var7.toString());
                                 if (var31.getProt() != 0) {
                                    break label134;
                                 }

                                 Logger.getLogger().logLine("Hopp by Hopp Header");
                                 var32 = Logger.getLogger();
                                 var7 = new StringBuilder();
                                 var7.append("NextHeader:");
                                 var7.append(var26[40] & 255);
                                 var32.logLine(var7.toString());
                                 var32 = Logger.getLogger();
                                 var7 = new StringBuilder();
                                 var7.append("Hdr Ext Len:");
                                 var7.append(var26[41] & 255);
                                 var32.logLine(var7.toString());
                              } catch (IOException var22) {
                                 var33 = var22;
                                 var10001 = false;
                                 break label156;
                              } catch (Exception var23) {
                                 var10000 = var23;
                                 var10001 = false;
                                 break label155;
                              }

                              if ((var26[40] & 255) == 58) {
                                 try {
                                    var32 = Logger.getLogger();
                                    var7 = new StringBuilder();
                                    var7.append("Received ICMP IPV6 Paket Type:");
                                    var7.append(var26[48] & 255);
                                    var32.logLine(var7.toString());
                                 } catch (IOException var20) {
                                    var33 = var20;
                                    var10001 = false;
                                    break label156;
                                 } catch (Exception var21) {
                                    var10000 = var21;
                                    var10001 = false;
                                    break label155;
                                 }
                              }
                           }

                           try {
                              if (var31.checkCheckSum() != 0) {
                                 throw new IOException("IP Header Checksum Error!");
                              }
                           } catch (IOException var18) {
                              var33 = var18;
                              var10001 = false;
                              break label156;
                           } catch (Exception var19) {
                              var10000 = var19;
                              var10001 = false;
                              break label155;
                           }

                           try {
                              if (var31.getProt() == 1 && DNSProxyActivity.debug) {
                                 var32 = Logger.getLogger();
                                 var7 = new StringBuilder();
                                 var7.append("Received ICMP Paket Type:");
                                 var7.append(var26[20] & 255);
                                 var32.logLine(var7.toString());
                              }
                           } catch (IOException var16) {
                              var33 = var16;
                              var10001 = false;
                              break label156;
                           } catch (Exception var17) {
                              var10000 = var17;
                              var10001 = false;
                              break label155;
                           }

                           UDPPacket var27;
                           try {
                              if (var31.getProt() != 17) {
                                 continue;
                              }

                              var27 = new UDPPacket(var26, 0, var3);
                              if (var27.checkCheckSum() != 0) {
                                 throw new IOException("UDP packet Checksum Error!");
                              }
                           } catch (IOException var14) {
                              var33 = var14;
                              var10001 = false;
                              break label156;
                           } catch (Exception var15) {
                              var10000 = var15;
                              var10001 = false;
                              break label155;
                           }

                           if (var1) {
                              continue;
                           }

                           try {
                              (new Thread(new DNSResolver(var27, this.out))).start();
                              continue;
                           } catch (IOException var12) {
                              var33 = var12;
                              var10001 = false;
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                              break label155;
                           }
                        }

                        IOException var30 = var33;

                        try {
                           var29 = Logger.getLogger();
                           var6 = new StringBuilder();
                           var6.append("IOEXCEPTION: ");
                           var6.append(var30.toString());
                           var29.logLine(var6.toString());
                           continue;
                        } catch (Exception var8) {
                           var10000 = var8;
                           var10001 = false;
                           break;
                        }
                     }

                     var28 = var10000;

                     try {
                        Logger.getLogger().logException(var28);
                     } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                        break;
                     }
                  }
               }
            }

            var28 = var10000;
            if (!this.stopped) {
               Logger.getLogger().logLine("VPN);Runner died!");
               Logger.getLogger().logException(var28);
            }
         }

         var4 = Logger.getLogger();
         var5 = new StringBuilder();
         var5.append("VPN Runner Thread ");
         var5.append(this.field_6);
         var5.append(" terminated!");
         var4.logLine(var5.toString());
      }
   }
}
