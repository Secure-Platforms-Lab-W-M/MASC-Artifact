package dnsfilter;

import dnsfilter.remote.RemoteAccessServer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import util.ExecutionEnvironment;
import util.Logger;
import util.LoggerInterface;
import util.Utils;

public class DNSFilterManager extends ConfigurationAccess {
   private static String DOWNLOADED_FF_PREFIX = "# Downloaded by personalDNSFilter at: ";
   private static DNSFilterManager INSTANCE = new DNSFilterManager();
   private static LoggerInterface TRAFFIC_LOG;
   public static final String VERSION = "1504000";
   public static String WORKDIR = "";
   private static boolean aborted = false;
   private static String additionalHostsImportTS = "0";
   public static boolean debug;
   private static boolean filterHostsFileRemoveDuplicates;
   private static int filterListCacheSize = 500;
   private static long filterReloadIntervalDays;
   private static String filterReloadURL;
   private static String filterhostfile;
   private static BlockedHosts hostFilter = null;
   private static Hashtable hostsFilterOverRule = null;
   private static long nextReload;
   private static int okCacheSize = 500;
   private static boolean reloadUrlChanged;
   private static RemoteAccessServer remoteAccessManager;
   private static boolean updatingFilter = false;
   private static boolean validIndex;
   private DNSFilterManager.AutoFilterUpdater autoFilterUpdater;
   protected Properties config = null;
   private boolean reloading_filter = false;
   private boolean serverStopped = true;

   private DNSFilterManager() {
   }

   private void abortFilterUpdate() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static long access$302(long var0) {
      nextReload = var0;
      return var0;
   }

   // $FF: synthetic method
   static boolean access$400(DNSFilterManager var0) throws IOException {
      return var0.updateFilter();
   }

   // $FF: synthetic method
   static boolean access$502(boolean var0) {
      validIndex = var0;
      return var0;
   }

   // $FF: synthetic method
   static void access$600(DNSFilterManager var0, boolean var1) throws IOException {
      var0.reloadFilter(var1);
   }

   // $FF: synthetic method
   static long access$700() {
      return filterReloadIntervalDays;
   }

   private void checkHostName(String var1) throws IOException {
      if (var1.length() > 253) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid Hostname: ");
         var2.append(var1);
         throw new IOException(var2.toString());
      }
   }

   private void copyFromAssets(String var1, String var2) throws IOException {
      InputStream var4 = ExecutionEnvironment.getEnvironment().getAsset(var1);
      StringBuilder var3 = new StringBuilder();
      var3.append("./");
      var3.append(WORKDIR);
      var3.append(var2);
      File var5 = new File(var3.toString());
      var5.getParentFile().mkdirs();
      Utils.copyFully(var4, new FileOutputStream(var5), true);
   }

   private void copyLocalFile(String var1, String var2) throws IOException {
      StringBuilder var3 = new StringBuilder();
      var3.append(WORKDIR);
      var3.append(var1);
      File var4 = new File(var3.toString());
      var3 = new StringBuilder();
      var3.append(WORKDIR);
      var3.append(var2);
      Utils.copyFile(var4, new File(var3.toString()));
   }

   private void createDefaultConfiguration() {
      try {
         StringBuilder var1 = new StringBuilder();
         var1.append(WORKDIR);
         var1.append(".");
         (new File(var1.toString())).mkdir();
         this.copyFromAssets("dnsfilter.conf", "dnsfilter.conf");
         var1 = new StringBuilder();
         var1.append(WORKDIR);
         var1.append("additionalHosts.txt");
         if (!(new File(var1.toString())).exists()) {
            this.copyFromAssets("additionalHosts.txt", "additionalHosts.txt");
         }

         var1 = new StringBuilder();
         var1.append(WORKDIR);
         var1.append("VERSION.TXT");
         File var3 = new File(var1.toString());
         var3.createNewFile();
         FileOutputStream var4 = new FileOutputStream(var3);
         var4.write("1504000".getBytes());
         var4.flush();
         var4.close();
         Logger.getLogger().logLine("Default configuration created successfully!");
      } catch (IOException var2) {
         Logger.getLogger().logLine("FAILED creating default Configuration!");
         Logger.getLogger().logException(var2);
      }
   }

   private String getFilterReloadURL(Properties var1) {
      String var5 = var1.getProperty("filterAutoUpdateURL", "");
      String var11 = var1.getProperty("filterAutoUpdateURL_switchs", "");
      StringTokenizer var8 = new StringTokenizer(var5, ";");
      StringTokenizer var9 = new StringTokenizer(var11, ";");
      int var3 = var8.countTokens();
      var5 = "";
      var11 = "";

      String var6;
      for(int var2 = 0; var2 < var3; var11 = var6) {
         String var10 = var8.nextToken().trim();
         boolean var4 = true;
         if (var9.hasMoreTokens()) {
            var4 = Boolean.parseBoolean(var9.nextToken().trim());
         }

         String var7 = var5;
         var6 = var11;
         if (var4) {
            StringBuilder var12 = new StringBuilder();
            var12.append(var5);
            var12.append(var11);
            var12.append(var10);
            var7 = var12.toString();
            var6 = "; ";
         }

         ++var2;
         var5 = var7;
      }

      return var5;
   }

   public static DNSFilterManager getInstance() {
      return INSTANCE;
   }

   private void initEnv() {
      debug = false;
      filterReloadURL = null;
      filterhostfile = null;
      filterReloadIntervalDays = 4L;
      nextReload = 0L;
      reloadUrlChanged = false;
      filterHostsFileRemoveDuplicates = false;
      validIndex = true;
      hostFilter = null;
      hostsFilterOverRule = null;
      additionalHostsImportTS = "0";
      this.reloading_filter = false;
   }

   private byte[] mergeAndPersistConfig(byte[] var1) throws IOException {
      String var6 = new String(var1);
      int var2 = var6.indexOf("\n#!!!filterHostsFile =");
      byte var4 = 0;
      boolean var14;
      if (var2 != -1) {
         var14 = true;
      } else {
         var14 = false;
      }

      String var13 = var6;
      if (var14) {
         var13 = var6.replace("\n#!!!filterHostsFile =", "\nfilterHostsFile =");
      }

      Properties var7 = new Properties();
      var7.load(new ByteArrayInputStream(var13.getBytes()));
      String[] var8 = (String[])var7.keySet().toArray(new String[0]);
      BufferedReader var11 = new BufferedReader(new InputStreamReader(ExecutionEnvironment.getEnvironment().getAsset("dnsfilter.conf")));
      StringBuilder var15 = new StringBuilder();
      var15.append(WORKDIR);
      var15.append("dnsfilter.conf");
      File var9 = new File(var15.toString());
      FileOutputStream var10 = new FileOutputStream(var9);
      boolean var5 = var7.getProperty("previousAutoUpdateURL", "").trim().equals("https://adaway.org/hosts.txt; https://hosts-file.net/ad_servers.txt; https://hosts-file.net/emd.txt");

      while(true) {
         var6 = var11.readLine();
         var13 = var6;
         StringBuilder var21;
         if (var6 == null) {
            var11.close();
            Properties var17 = new Properties();
            var17.load(ExecutionEnvironment.getEnvironment().getAsset("dnsfilter.conf"));
            boolean var16 = true;

            boolean var19;
            for(var2 = var4; var2 < var8.length; var16 = var19) {
               var19 = var16;
               if (!var17.containsKey(var8[var2])) {
                  if (var16) {
                     var10.write("\r\n# Merged custom config from previous config file:\r\n".getBytes());
                  }

                  var19 = false;
                  var21 = new StringBuilder();
                  var21.append(var8[var2]);
                  var21.append(" = ");
                  var21.append(var7.getProperty(var8[var2], ""));
                  var6 = var21.toString();
                  StringBuilder var23 = new StringBuilder();
                  var23.append(var6);
                  var23.append("\r\n");
                  var10.write(var23.toString().getBytes());
               }

               ++var2;
            }

            var10.flush();
            var10.close();
            Logger.getLogger().logLine("Merged configuration 'dnsfilter.conf' after update to version 1504000!");
            FileInputStream var18 = new FileInputStream(var9);
            byte[] var22 = Utils.readFully(var18, 1024);
            var18.close();
            return var22;
         }

         StringBuilder var12;
         if (var5 && var6.startsWith("filterAutoUpdateURL")) {
            LoggerInterface var20 = Logger.getLogger();
            var12 = new StringBuilder();
            var12.append("Taking over default configuration: ");
            var12.append(var6);
            var20.logLine(var12.toString());
         } else {
            for(int var3 = 0; var3 < var8.length; var13 = var6) {
               var12 = new StringBuilder();
               var12.append(var8[var3]);
               var12.append(" =");
               var6 = var13;
               if (var13.startsWith(var12.toString())) {
                  if (var8[var3].equals("filterHostsFile") && var14) {
                     var15 = new StringBuilder();
                     var15.append("#!!!filterHostsFile = ");
                     var15.append(var7.getProperty(var8[var3], ""));
                     var6 = var15.toString();
                  } else {
                     var15 = new StringBuilder();
                     var15.append(var8[var3]);
                     var15.append(" = ");
                     var15.append(var7.getProperty(var8[var3], ""));
                     var6 = var15.toString();
                  }
               }

               ++var3;
            }
         }

         var21 = new StringBuilder();
         var21.append(var13);
         var21.append("\r\n");
         var10.write(var21.toString().getBytes());
      }
   }

   private String[] parseHosts(String var1) throws IOException {
      if (!var1.startsWith("#") && !var1.startsWith("!") && !var1.trim().equals("")) {
         StringTokenizer var2 = new StringTokenizer(var1);
         String[] var3;
         if (var2.countTokens() >= 2) {
            var3 = new String[]{var2.nextToken().trim(), var2.nextToken().trim()};
         } else {
            var3 = new String[]{"127.0.0.1", var2.nextToken().trim()};
         }

         this.checkHostName(var3[1]);
         return var3;
      } else {
         return null;
      }
   }

   private void rebuildIndex() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void reloadFilter(boolean var1) throws IOException {
      label771: {
         Throwable var10000;
         label773: {
            StringBuilder var4;
            File var79;
            File var81;
            boolean var10001;
            File var82;
            try {
               ExecutionEnvironment.getEnvironment().wakeLock();
               StringBuilder var3 = new StringBuilder();
               var3.append(WORKDIR);
               var3.append(filterhostfile);
               var79 = new File(var3.toString());
               var4 = new StringBuilder();
               var4.append(WORKDIR);
               var4.append(filterhostfile);
               var4.append(".DLD_CNT");
               var81 = new File(var4.toString());
               StringBuilder var5 = new StringBuilder();
               var5.append(WORKDIR);
               var5.append("additionalHosts.txt");
               var82 = new File(var5.toString());
               if (!var82.exists()) {
                  var82.createNewFile();
               }
            } catch (Throwable var78) {
               var10000 = var78;
               var10001 = false;
               break label773;
            }

            boolean var2;
            label777: {
               try {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("");
                  var6.append(var82.lastModified());
                  var2 = var6.toString().equals(additionalHostsImportTS);
                  if (var79.exists() && var81.exists() && !reloadUrlChanged) {
                     nextReload = filterReloadIntervalDays * 24L * 60L * 60L * 1000L + var81.lastModified();
                     break label777;
                  }
               } catch (Throwable var77) {
                  var10000 = var77;
                  var10001 = false;
                  break label773;
               }

               try {
                  nextReload = 0L;
               } catch (Throwable var76) {
                  var10000 = var76;
                  var10001 = false;
                  break label773;
               }
            }

            label775: {
               try {
                  var4 = new StringBuilder();
                  var4.append(WORKDIR);
                  var4.append(filterhostfile);
                  var4.append(".idx");
                  var81 = new File(var4.toString());
                  if (!var81.exists() || !validIndex || !BlockedHosts.checkIndexVersion(var81.getAbsolutePath())) {
                     break label775;
                  }

                  hostFilter = BlockedHosts.loadPersistedIndex(var81.getAbsolutePath(), false, okCacheSize, filterListCacheSize, hostsFilterOverRule);
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label773;
               }

               if (!(var2 ^ true)) {
                  break label771;
               }

               try {
                  if (var79.exists() && nextReload != 0L) {
                     (new Thread(new DNSFilterManager.AsyncIndexBuilder())).start();
                  }
                  break label771;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label773;
               }
            }

            try {
               if (!var79.exists() || nextReload == 0L) {
                  break label771;
               }
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label773;
            }

            if (!var1) {
               label736:
               try {
                  this.rebuildIndex();
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break label736;
               }
            } else {
               label738:
               try {
                  (new Thread(new DNSFilterManager.AsyncIndexBuilder())).start();
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label738;
               }
            }
            break label771;
         }

         Throwable var80 = var10000;
         ExecutionEnvironment.getEnvironment().releaseWakeLock();
         throw var80;
      }

      ExecutionEnvironment.getEnvironment().releaseWakeLock();
   }

   private boolean updateFilter() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void updateIndexReloadInfoConfFile(String var1, String var2) {
      IOException var10000;
      label98: {
         StringBuilder var7;
         ByteArrayOutputStream var9;
         BufferedReader var10;
         boolean var10001;
         try {
            var9 = new ByteArrayOutputStream();
            var7 = new StringBuilder();
            var7.append(WORKDIR);
            var7.append("dnsfilter.conf");
            var10 = new BufferedReader(new InputStreamReader(new FileInputStream(var7.toString())));
         } catch (IOException var20) {
            var10000 = var20;
            var10001 = false;
            break label98;
         }

         boolean var4 = false;
         boolean var3 = false;

         while(true) {
            String var24;
            try {
               var24 = var10.readLine();
            } catch (IOException var15) {
               var10000 = var15;
               var10001 = false;
               break;
            }

            String var8 = var24;
            if (var24 == null) {
               if (!var4 && var1 != null) {
                  try {
                     var7 = new StringBuilder();
                     var7.append("previousAutoUpdateURL = ");
                     var7.append(var1);
                     var7.append("\r\n");
                     var9.write(var7.toString().getBytes());
                  } catch (IOException var13) {
                     var10000 = var13;
                     var10001 = false;
                     break;
                  }
               }

               StringBuilder var21;
               if (!var3 && var2 != null) {
                  try {
                     var21 = new StringBuilder();
                     var21.append("additionalHosts_lastImportTS = ");
                     var21.append(var2);
                     var21.append("\r\n");
                     var9.write(var21.toString().getBytes());
                  } catch (IOException var12) {
                     var10000 = var12;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var9.flush();
                  var10.close();
                  var21 = new StringBuilder();
                  var21.append(WORKDIR);
                  var21.append("dnsfilter.conf");
                  FileOutputStream var23 = new FileOutputStream(var21.toString());
                  var23.write(var9.toByteArray());
                  var23.flush();
                  var23.close();
                  return;
               } catch (IOException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }
            }

            boolean var5;
            boolean var6;
            label105: {
               if (var1 != null) {
                  label103: {
                     try {
                        if (!var8.startsWith("previousAutoUpdateURL")) {
                           break label103;
                        }
                     } catch (IOException var19) {
                        var10000 = var19;
                        var10001 = false;
                        break;
                     }

                     var5 = true;

                     try {
                        var7 = new StringBuilder();
                        var7.append("previousAutoUpdateURL = ");
                        var7.append(var1);
                        var24 = var7.toString();
                     } catch (IOException var14) {
                        var10000 = var14;
                        var10001 = false;
                        break;
                     }

                     var6 = var3;
                     break label105;
                  }
               }

               var5 = var4;
               var6 = var3;
               var24 = var24;
               if (var2 != null) {
                  label104: {
                     var5 = var4;
                     var6 = var3;
                     var24 = var8;

                     try {
                        if (!var8.startsWith("additionalHosts_lastImportTS")) {
                           break label104;
                        }
                     } catch (IOException var18) {
                        var10000 = var18;
                        var10001 = false;
                        break;
                     }

                     var6 = true;

                     try {
                        var7 = new StringBuilder();
                        var7.append("additionalHosts_lastImportTS = ");
                        var7.append(var2);
                        var24 = var7.toString();
                     } catch (IOException var17) {
                        var10000 = var17;
                        var10001 = false;
                        break;
                     }

                     var5 = var4;
                  }
               }
            }

            try {
               StringBuilder var25 = new StringBuilder();
               var25.append(var24);
               var25.append("\r\n");
               var9.write(var25.toString().getBytes());
            } catch (IOException var16) {
               var10000 = var16;
               var10001 = false;
               break;
            }

            var4 = var5;
            var3 = var6;
         }
      }

      IOException var22 = var10000;
      Logger.getLogger().logException(var22);
   }

   private void writeDownloadInfoFile(int var1, long var2) throws IOException {
      StringBuilder var4 = new StringBuilder();
      var4.append(WORKDIR);
      var4.append(filterhostfile);
      var4.append(".DLD_CNT");
      FileOutputStream var6 = new FileOutputStream(var4.toString());
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append("\n");
      var6.write(var5.toString().getBytes());
      var5 = new StringBuilder();
      var5.append(var2);
      var5.append("\n");
      var6.write(var5.toString().getBytes());
      var6.flush();
      var6.close();
   }

   private void writeNewEntries(boolean var1, HashSet var2, BufferedWriter var3) throws IOException {
      String var5 = "";
      if (!var1) {
         var5 = "!";
      }

      if (hostsFilterOverRule == null) {
         hostsFilterOverRule = new Hashtable();
         hostFilter.setHostsFilterOverRule(hostsFilterOverRule);
      }

      Iterator var8 = var2.iterator();

      while(var8.hasNext()) {
         String var6 = (String)var8.next();
         hostsFilterOverRule.remove(var6);
         hostFilter.clearCache(var6);
         boolean var4;
         if (var1 && hostFilter.contains(var6)) {
            var4 = true;
         } else {
            var4 = false;
         }

         if (!var4) {
            StringBuilder var7 = new StringBuilder();
            var7.append("\n");
            var7.append(var5);
            var7.append(var6);
            var3.write(var7.toString());
            if (!var1) {
               hostsFilterOverRule.put(var6, false);
            } else {
               hostFilter.update(var6);
            }
         }

         if (var1) {
            hostFilter.updatePersist();
         }
      }

   }

   public boolean canStop() {
      return this.reloading_filter ^ true;
   }

   public void doBackup(String var1) throws IOException {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append("backup/");
         var2.append(var1);
         var2.append("/dnsfilter.conf");
         this.copyLocalFile("dnsfilter.conf", var2.toString());
         var2 = new StringBuilder();
         var2.append("backup/");
         var2.append(var1);
         var2.append("/additionalHosts.txt");
         this.copyLocalFile("additionalHosts.txt", var2.toString());
         var2 = new StringBuilder();
         var2.append("backup/");
         var2.append(var1);
         var2.append("/VERSION.TXT");
         this.copyLocalFile("VERSION.TXT", var2.toString());
      } catch (IOException var3) {
         throw new ConfigurationAccess.ConfigurationAccessException(var3.getMessage(), var3);
      }
   }

   public void doRestore(String var1) throws IOException {
      IOException var10000;
      label38: {
         boolean var10001;
         try {
            if (!this.canStop()) {
               throw new IOException("Cannot stop! Pending Operation!");
            }
         } catch (IOException var6) {
            var10000 = var6;
            var10001 = false;
            break label38;
         }

         label32: {
            StringBuilder var2;
            try {
               this.stop();
               var2 = new StringBuilder();
               var2.append("backup/");
               var2.append(var1);
               var2.append("/dnsfilter.conf");
               this.copyLocalFile(var2.toString(), "dnsfilter.conf");
               var2 = new StringBuilder();
               var2.append("backup/");
               var2.append(var1);
               var2.append("/additionalHosts.txt");
               this.copyLocalFile(var2.toString(), "additionalHosts.txt");
               var2 = new StringBuilder();
               var2.append("backup/");
               var2.append(var1);
               var2.append("/VERSION.TXT");
               this.copyLocalFile(var2.toString(), "VERSION.TXT");
               if (this.config == null) {
                  break label32;
               }

               var1 = this.config.getProperty("filterHostsFile");
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break label38;
            }

            if (var1 != null) {
               try {
                  var2 = new StringBuilder();
                  var2.append(WORKDIR);
                  var2.append(var1);
                  (new File(var2.toString())).delete();
               } catch (IOException var4) {
                  var10000 = var4;
                  var10001 = false;
                  break label38;
               }
            }
         }

         try {
            this.init();
            ExecutionEnvironment.getEnvironment().onReload();
            return;
         } catch (IOException var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      IOException var7 = var10000;
      throw new ConfigurationAccess.ConfigurationAccessException(var7.getMessage(), var7);
   }

   public void doRestoreDefaults() throws IOException {
      IOException var10000;
      label38: {
         boolean var10001;
         try {
            if (!this.canStop()) {
               throw new IOException("Cannot stop! Pending Operation!");
            }
         } catch (IOException var6) {
            var10000 = var6;
            var10001 = false;
            break label38;
         }

         label32: {
            String var1;
            try {
               this.stop();
               this.copyFromAssets("dnsfilter.conf", "dnsfilter.conf");
               this.copyFromAssets("additionalHosts.txt", "additionalHosts.txt");
               if (this.config == null) {
                  break label32;
               }

               var1 = this.config.getProperty("filterHostsFile");
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break label38;
            }

            if (var1 != null) {
               try {
                  StringBuilder var2 = new StringBuilder();
                  var2.append(WORKDIR);
                  var2.append(var1);
                  (new File(var2.toString())).delete();
               } catch (IOException var4) {
                  var10000 = var4;
                  var10001 = false;
                  break label38;
               }
            }
         }

         try {
            this.init();
            ExecutionEnvironment.getEnvironment().onReload();
            return;
         } catch (IOException var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      IOException var7 = var10000;
      throw new ConfigurationAccess.ConfigurationAccessException(var7.getMessage(), var7);
   }

   public byte[] getAdditionalHosts(int var1) throws IOException {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append(WORKDIR);
         var2.append("additionalHosts.txt");
         File var5 = new File(var2.toString());
         if (var5.length() > (long)var1) {
            return null;
         } else {
            FileInputStream var6 = new FileInputStream(var5);
            byte[] var3 = Utils.readFully(var6, 1024);
            var6.close();
            return var3;
         }
      } catch (IOException var4) {
         throw new ConfigurationAccess.ConfigurationAccessException(var4.getMessage(), var4);
      }
   }

   public String[] getAvailableBackups() throws IOException {
      StringBuilder var3 = new StringBuilder();
      var3.append(WORKDIR);
      var3.append("backup");
      File var4 = new File(var3.toString());
      String[] var7 = new String[0];
      if (var4.exists()) {
         var7 = var4.list();
      }

      ArrayList var8 = new ArrayList();
      int var2 = var7.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         String var5 = var7[var1];
         StringBuilder var6 = new StringBuilder();
         var6.append(WORKDIR);
         var6.append("backup/");
         var6.append(var5);
         if ((new File(var6.toString())).isDirectory()) {
            var8.add(var5);
         }
      }

      Collections.sort(var8);
      return (String[])var8.toArray(new String[0]);
   }

   public Properties getConfig() throws IOException {
      try {
         if (this.config == null) {
            byte[] var1 = this.readConfig();
            this.config = new Properties();
            this.config.load(new ByteArrayInputStream(var1));
         }

         Properties var3 = this.config;
         return var3;
      } catch (IOException var2) {
         Logger.getLogger().logException(var2);
         return null;
      }
   }

   public long[] getFilterStatistics() {
      return new long[]{DNSResponsePatcher.getOkCount(), DNSResponsePatcher.getFilterCount()};
   }

   public String getLastDNSAddress() {
      return DNSCommunicator.getInstance().getLastDNSAddress();
   }

   public String getVersion() {
      return "1504000";
   }

   public void init() throws IOException {
      // $FF: Couldn't be decompiled
   }

   public int openConnectionsCount() {
      return DNSResolver.getResolverCount();
   }

   public byte[] readConfig() throws ConfigurationAccess.ConfigurationAccessException {
      StringBuilder var1 = new StringBuilder();
      var1.append(WORKDIR);
      var1.append("dnsfilter.conf");
      File var2 = new File(var1.toString());
      File var9 = var2;
      if (!var2.exists()) {
         LoggerInterface var10 = Logger.getLogger();
         StringBuilder var3 = new StringBuilder();
         var3.append(var2);
         var3.append(" not found! - creating default config!");
         var10.logLine(var3.toString());
         this.createDefaultConfiguration();
         var1 = new StringBuilder();
         var1.append(WORKDIR);
         var1.append("dnsfilter.conf");
         var9 = new File(var1.toString());
      }

      IOException var10000;
      label52: {
         byte[] var14;
         boolean var10001;
         try {
            FileInputStream var12 = new FileInputStream(var9);
            var14 = Utils.readFully(var12, 1024);
            var12.close();
            var1 = new StringBuilder();
            var1.append(WORKDIR);
            var1.append("additionalHosts.txt");
            var9 = new File(var1.toString());
            if (!var9.exists()) {
               var9.createNewFile();
               FileOutputStream var16 = new FileOutputStream(var9);
               Utils.copyFully(ExecutionEnvironment.getEnvironment().getAsset("additionalHosts.txt"), var16, true);
            }
         } catch (IOException var8) {
            var10000 = var8;
            var10001 = false;
            break label52;
         }

         try {
            var1 = new StringBuilder();
            var1.append(WORKDIR);
            var1.append("VERSION.TXT");
            var2 = new File(var1.toString());
         } catch (IOException var7) {
            var10000 = var7;
            var10001 = false;
            break label52;
         }

         String var17 = "";

         try {
            if (var2.exists()) {
               FileInputStream var11 = new FileInputStream(var2);
               var17 = new String(Utils.readFully(var11, 100));
               var11.close();
            }
         } catch (IOException var6) {
            var10000 = var6;
            var10001 = false;
            break label52;
         }

         byte[] var13 = var14;

         try {
            if (!var17.equals("1504000")) {
               LoggerInterface var15 = Logger.getLogger();
               StringBuilder var4 = new StringBuilder();
               var4.append("Updated version! Previous version:");
               var4.append(var17);
               var4.append(", current version:");
               var4.append("1504000");
               var15.logLine(var4.toString());
               this.createDefaultConfiguration();
               var13 = this.mergeAndPersistConfig(var14);
            }

            return var13;
         } catch (IOException var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      IOException var18 = var10000;
      Logger.getLogger().logException(var18);
      throw new ConfigurationAccess.ConfigurationAccessException(var18.getMessage(), var18);
   }

   public int[] readHostFileEntry(InputStream var1, byte[] var2) throws IOException {
      byte var4 = 0;
      int var3 = var1.read();

      int var5;
      while(var3 == 35) {
         var5 = Utils.skipLine(var1);
         var3 = var5;
         if (var5 != -1) {
            var3 = Utils.skipWhitespace(var1, var5);
         }
      }

      if (var3 == -1) {
         return new int[]{0, -1};
      } else if (var2.length == 0) {
         throw new IOException("Buffer Overflow!");
      } else {
         if (var3 == 42) {
            var4 = 1;
         }

         var2[0] = (byte)var3;
         boolean var12 = false;
         int var10 = 1;
         int var6 = var3;
         byte var7 = var4;

         while(var6 != -1 && var6 != 10) {
            boolean var8 = var12;
            int var9 = var6;
            var4 = var7;
            var3 = var10;

            while(true) {
               var10 = var3;
               var7 = var4;
               var6 = var9;
               var12 = var8;
               if (var9 == -1) {
                  break;
               }

               var10 = var3;
               var7 = var4;
               var6 = var9;
               var12 = var8;
               if (var9 == 10) {
                  break;
               }

               byte var13;
               boolean var14;
               label84: {
                  var9 = var1.read();
                  if (var9 != 9 && var9 != 32) {
                     var10 = var3;
                     var13 = var4;
                     var5 = var9;
                     var14 = var8;
                     if (var9 != 13) {
                        break label84;
                     }
                  }

                  if (var8) {
                     Utils.skipLine(var1);
                     return new int[]{var4, var3};
                  }

                  var14 = true;
                  var13 = 0;
                  var5 = Utils.skipWhitespace(var1, var9);
                  var10 = 0;
               }

               if (var5 == 42) {
                  var13 = 1;
               }

               var3 = var10;
               var4 = var13;
               var9 = var5;
               var8 = var14;
               if (var5 != -1) {
                  if (var10 == var2.length) {
                     throw new IOException("Buffer Overflow!");
                  }

                  if (var5 < 32 && var5 < 9 && var5 > 13) {
                     StringBuilder var11 = new StringBuilder();
                     var11.append("Non Printable character: ");
                     var11.append(var5);
                     var11.append("(");
                     var11.append((char)var5);
                     var11.append(")");
                     throw new IOException(var11.toString());
                  }

                  var2[var10] = (byte)var5;
                  var3 = var10 + 1;
                  var4 = var13;
                  var9 = var5;
                  var8 = var14;
               }
            }
         }

         return new int[]{var7, var10 - 1};
      }
   }

   public void releaseConfiguration() {
   }

   public void releaseWakeLock() {
      ExecutionEnvironment.getEnvironment().releaseWakeLock();
   }

   public void restart() throws IOException {
      try {
         this.stop();
         this.init();
         ExecutionEnvironment.getEnvironment().onReload();
      } catch (IOException var2) {
         throw new ConfigurationAccess.ConfigurationAccessException(var2.getMessage(), var2);
      }
   }

   public void stop() throws IOException {
      if (!this.serverStopped) {
         this.abortFilterUpdate();
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label313: {
            try {
               if (this.autoFilterUpdater != null) {
                  this.autoFilterUpdater.stop();
                  this.autoFilterUpdater = null;
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label313;
            }

            try {
               this.notifyAll();
               if (hostFilter != null) {
                  hostFilter.clear();
               }
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label313;
            }

            try {
               DNSResponsePatcher.init((Set)null, (LoggerInterface)null);
               if (TRAFFIC_LOG != null) {
                  TRAFFIC_LOG.closeLogger();
                  Logger.removeLogger("TrafficLogger");
               }
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label313;
            }

            label298:
            try {
               this.serverStopped = true;
               ExecutionEnvironment.getEnvironment().releaseAllWakeLocks();
               return;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               break label298;
            }
         }

         while(true) {
            Throwable var1 = var10000;

            try {
               throw var1;
            } catch (Throwable var27) {
               var10000 = var27;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void triggerUpdateFilter() {
      // $FF: Couldn't be decompiled
   }

   public void updateAdditionalHosts(byte[] var1) throws IOException {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append(WORKDIR);
         var2.append("additionalHosts.txt");
         FileOutputStream var4 = new FileOutputStream(var2.toString());
         var4.write(var1);
         var4.flush();
         var4.close();
      } catch (IOException var3) {
         throw new ConfigurationAccess.ConfigurationAccessException(var3.getMessage(), var3);
      }
   }

   public void updateConfig(byte[] var1) throws IOException {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append(WORKDIR);
         var2.append("dnsfilter.conf");
         FileOutputStream var4 = new FileOutputStream(var2.toString());
         var4.write(var1);
         var4.flush();
         var4.close();
         this.config.load(new ByteArrayInputStream(var1));
         Logger.getLogger().message("Config Changed!\nRestart might be required!");
      } catch (IOException var3) {
         throw new ConfigurationAccess.ConfigurationAccessException(var3.getMessage(), var3);
      }
   }

   public void updateFilter(String param1, boolean param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void wakeLock() {
      ExecutionEnvironment.getEnvironment().wakeLock();
   }

   private class AsyncIndexBuilder implements Runnable {
      private AsyncIndexBuilder() {
      }

      // $FF: synthetic method
      AsyncIndexBuilder(Object var2) {
         this();
      }

      public void run() {
         DNSFilterManager.this.reloading_filter = true;

         try {
            DNSFilterManager.this.rebuildIndex();
         } catch (IOException var4) {
            Logger.getLogger().logException(var4);
         } finally {
            DNSFilterManager.this.reloading_filter = false;
         }

      }
   }

   private class AutoFilterUpdater implements Runnable {
      private Object monitor;
      boolean running = false;
      boolean stopRequest = false;

      public AutoFilterUpdater() {
         this.monitor = DNSFilterManager.INSTANCE;
      }

      private void waitUntilNextFilterReload() throws InterruptedException {
         Object var1 = this.monitor;
         synchronized(var1){}

         while(true) {
            Throwable var10000;
            boolean var10001;
            label145: {
               try {
                  if (DNSFilterManager.nextReload > System.currentTimeMillis() && !this.stopRequest) {
                     this.monitor.wait(10000L);
                     continue;
                  }
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label145;
               }

               label137:
               try {
                  return;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label137;
               }
            }

            while(true) {
               Throwable var2 = var10000;

               try {
                  throw var2;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }

      public void stop() {
         // $FF: Couldn't be decompiled
      }
   }
}
