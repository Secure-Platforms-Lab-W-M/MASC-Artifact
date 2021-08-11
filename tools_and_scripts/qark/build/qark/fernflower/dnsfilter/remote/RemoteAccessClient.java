package dnsfilter.remote;

import dnsfilter.ConfigurationAccess;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;
import util.Encryption;
import util.Logger;
import util.LoggerInterface;
import util.TimeoutListener;
import util.TimoutNotificator;
import util.Utils;

public class RemoteAccessClient extends ConfigurationAccess implements TimeoutListener {
   static int CON_TIMEOUT = 15000;
   static final int HEART_BEAT = 6;
   static final int LOG = 1;
   static final int LOG_LN = 2;
   static final int LOG_MSG = 3;
   static int READ_TIMEOUT = 15000;
   static final int UPD_CON_CNT = 5;
   static final int UPD_DNS = 4;
   private int con_cnt = -1;
   private LoggerInterface connectedLogger;
   private int ctrlConId = -1;
   private Socket ctrlcon;
   private String host;
   // $FF: renamed from: in java.io.InputStream
   private InputStream field_1;
   private String last_dns = "<unknown>";
   private OutputStream out;
   private int port;
   private RemoteAccessClient.RemoteStream remoteStream;
   private String remote_version;
   int timeOutCounter = 0;
   long timeout = Long.MAX_VALUE;
   boolean valid = false;

   public RemoteAccessClient(LoggerInterface var1, String var2, int var3, String var4) throws IOException {
      LoggerInterface var5 = var1;
      if (var1 == null) {
         var5 = Logger.getLogger();
      }

      this.connectedLogger = var5;
      Encryption.init_AES(var4);
      this.host = var2;
      this.port = var3;
      this.connect();
   }

   private void closeConnectionReconnect() {
      TimoutNotificator.getInstance().unregister(this);
      if (this.valid) {
         this.releaseConfiguration();
         Object var1 = new Object();
         synchronized(var1){}

         label238: {
            Throwable var10000;
            boolean var10001;
            label239: {
               label229: {
                  InterruptedException var2;
                  try {
                     try {
                        var1.wait(2000L);
                        break label229;
                     } catch (InterruptedException var32) {
                        var2 = var32;
                     }
                  } catch (Throwable var33) {
                     var10000 = var33;
                     var10001 = false;
                     break label239;
                  }

                  try {
                     var2.printStackTrace();
                  } catch (Throwable var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label239;
                  }
               }

               label221:
               try {
                  break label238;
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label221;
               }
            }

            while(true) {
               Throwable var34 = var10000;

               try {
                  throw var34;
               } catch (Throwable var29) {
                  var10000 = var29;
                  var10001 = false;
                  continue;
               }
            }
         }

         try {
            this.connect();
         } catch (IOException var28) {
            LoggerInterface var35 = this.connectedLogger;
            StringBuilder var3 = new StringBuilder();
            var3.append("Reconnect failed:");
            var3.append(var28.toString());
            var35.logLine(var3.toString());
            this.valid = false;
         }
      }
   }

   private void connect() throws IOException {
      Object[] var1 = this.initConnection();
      this.ctrlcon = (Socket)var1[1];
      this.field_1 = (InputStream)var1[2];
      this.out = (OutputStream)var1[3];
      this.ctrlcon.setSoTimeout(READ_TIMEOUT);
      this.ctrlConId = (Integer)var1[0];
      this.remoteStream = new RemoteAccessClient.RemoteStream(this.ctrlConId);
      this.valid = true;
   }

   private InputStream getInputStream() throws IOException {
      if (!this.valid) {
         throw new IOException("Not connected!");
      } else {
         return this.field_1;
      }
   }

   private OutputStream getOutputStream() throws IOException {
      if (!this.valid) {
         throw new IOException("Not connected!");
      } else {
         return this.out;
      }
   }

   private Object[] initConnection() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void processHeartBeat() {
      this.connectedLogger.message("Heart Beat!");
      this.timeOutCounter = 0;
      this.setTimeout(READ_TIMEOUT);
   }

   private void setTimeout(int var1) {
      this.timeout = System.currentTimeMillis() + (long)var1;
      TimoutNotificator.getInstance().register(this);
   }

   private void triggerAction(String var1, String var2) throws IOException {
      ConfigurationAccess.ConfigurationAccessException var16;
      label37: {
         StringBuilder var4;
         IOException var10000;
         label39: {
            OutputStream var3;
            boolean var10001;
            try {
               var3 = this.getOutputStream();
               var4 = new StringBuilder();
               var4.append(var1);
               var4.append("\n");
               var3.write(var4.toString().getBytes());
            } catch (ConfigurationAccess.ConfigurationAccessException var9) {
               var16 = var9;
               var10001 = false;
               break label37;
            } catch (IOException var10) {
               var10000 = var10;
               var10001 = false;
               break label39;
            }

            if (var2 != null) {
               try {
                  var3 = this.getOutputStream();
                  var4 = new StringBuilder();
                  var4.append(var2);
                  var4.append("\n");
                  var3.write(var4.toString().getBytes());
               } catch (ConfigurationAccess.ConfigurationAccessException var7) {
                  var16 = var7;
                  var10001 = false;
                  break label37;
               } catch (IOException var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label39;
               }
            }

            try {
               this.getOutputStream().flush();
               var2 = Utils.readLineFromStream(this.getInputStream());
               if (!var2.equals("OK")) {
                  throw new ConfigurationAccess.ConfigurationAccessException(var2, (IOException)null);
               }

               return;
            } catch (ConfigurationAccess.ConfigurationAccessException var5) {
               var16 = var5;
               var10001 = false;
               break label37;
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
            }
         }

         IOException var12 = var10000;
         LoggerInterface var14 = this.connectedLogger;
         var4 = new StringBuilder();
         var4.append("Remote action ");
         var4.append(var1);
         var4.append(" failed! ");
         var4.append(var12.getMessage());
         var14.logLine(var4.toString());
         this.closeConnectionReconnect();
         throw var12;
      }

      ConfigurationAccess.ConfigurationAccessException var11 = var16;
      LoggerInterface var13 = this.connectedLogger;
      StringBuilder var15 = new StringBuilder();
      var15.append("Remote action failed! ");
      var15.append(var11.getMessage());
      var13.logLine(var15.toString());
      throw var11;
   }

   public void doBackup(String var1) throws IOException {
      this.triggerAction("doBackup()", var1);
   }

   public void doRestore(String var1) throws IOException {
      this.triggerAction("doRestore()", var1);
   }

   public void doRestoreDefaults() throws IOException {
      this.triggerAction("doRestoreDefaults()", (String)null);
   }

   public byte[] getAdditionalHosts(int var1) throws IOException {
      LoggerInterface var3;
      StringBuilder var4;
      try {
         DataOutputStream var7 = new DataOutputStream(this.getOutputStream());
         DataInputStream var2 = new DataInputStream(this.getInputStream());
         var7.write("getAdditionalHosts()\n".getBytes());
         var7.writeInt(var1);
         var7.flush();
         String var8 = Utils.readLineFromStream(var2);
         if (!var8.equals("OK")) {
            throw new ConfigurationAccess.ConfigurationAccessException(var8, (IOException)null);
         } else {
            byte[] var9 = new byte[var2.readInt()];
            var2.readFully(var9);
            return var9;
         }
      } catch (ConfigurationAccess.ConfigurationAccessException var5) {
         var3 = this.connectedLogger;
         var4 = new StringBuilder();
         var4.append("Remote action failed! ");
         var4.append(var5.getMessage());
         var3.logLine(var4.toString());
         throw var5;
      } catch (IOException var6) {
         var3 = this.connectedLogger;
         var4 = new StringBuilder();
         var4.append("Remote action getAdditionalHosts() failed! ");
         var4.append(var6.getMessage());
         var3.logLine(var4.toString());
         this.closeConnectionReconnect();
         throw var6;
      }
   }

   public String[] getAvailableBackups() throws IOException {
      // $FF: Couldn't be decompiled
   }

   public Properties getConfig() throws IOException {
      LoggerInterface var2;
      StringBuilder var3;
      try {
         this.getOutputStream().write("getConfig()\n".getBytes());
         this.getOutputStream().flush();
         InputStream var1 = this.getInputStream();
         String var8 = Utils.readLineFromStream(var1);
         if (!var8.equals("OK")) {
            throw new ConfigurationAccess.ConfigurationAccessException(var8, (IOException)null);
         } else {
            try {
               Properties var7 = (Properties)(new ObjectInputStream(var1)).readObject();
               return var7;
            } catch (ClassNotFoundException var4) {
               this.connectedLogger.logException(var4);
               throw new IOException(var4);
            }
         }
      } catch (ConfigurationAccess.ConfigurationAccessException var5) {
         var2 = this.connectedLogger;
         var3 = new StringBuilder();
         var3.append("Remote action failed! ");
         var3.append(var5.getMessage());
         var2.logLine(var3.toString());
         throw var5;
      } catch (IOException var6) {
         var2 = this.connectedLogger;
         var3 = new StringBuilder();
         var3.append("Remote action getConfig() failed! ");
         var3.append(var6.getMessage());
         var2.logLine(var3.toString());
         this.closeConnectionReconnect();
         throw var6;
      }
   }

   public long[] getFilterStatistics() throws IOException {
      long var1;
      long var3;
      LoggerInterface var6;
      StringBuilder var7;
      try {
         DataOutputStream var10 = new DataOutputStream(this.getOutputStream());
         DataInputStream var5 = new DataInputStream(this.getInputStream());
         var10.write("getFilterStatistics()\n".getBytes());
         var10.flush();
         String var11 = Utils.readLineFromStream(var5);
         if (!var11.equals("OK")) {
            throw new ConfigurationAccess.ConfigurationAccessException(var11, (IOException)null);
         }

         var1 = var5.readLong();
         var3 = var5.readLong();
      } catch (ConfigurationAccess.ConfigurationAccessException var8) {
         var6 = this.connectedLogger;
         var7 = new StringBuilder();
         var7.append("Remote action failed! ");
         var7.append(var8.getMessage());
         var6.logLine(var7.toString());
         throw var8;
      } catch (IOException var9) {
         var6 = this.connectedLogger;
         var7 = new StringBuilder();
         var7.append("Remote action  getFilterStatistics() failed! ");
         var7.append(var9.getMessage());
         var6.logLine(var7.toString());
         this.closeConnectionReconnect();
         throw var9;
      }

      return new long[]{var1, var3};
   }

   public String getLastDNSAddress() {
      return this.last_dns;
   }

   public long getTimoutTime() {
      return this.timeout;
   }

   public String getVersion() throws IOException {
      return this.remote_version;
   }

   public boolean isLocal() {
      return false;
   }

   public int openConnectionsCount() {
      return this.con_cnt;
   }

   public byte[] readConfig() throws IOException {
      LoggerInterface var2;
      StringBuilder var3;
      try {
         this.getOutputStream().write("readConfig()\n".getBytes());
         this.getOutputStream().flush();
         DataInputStream var1 = new DataInputStream(this.getInputStream());
         String var6 = Utils.readLineFromStream(var1);
         if (!var6.equals("OK")) {
            throw new ConfigurationAccess.ConfigurationAccessException(var6, (IOException)null);
         } else {
            byte[] var7 = new byte[var1.readInt()];
            var1.readFully(var7);
            return var7;
         }
      } catch (ConfigurationAccess.ConfigurationAccessException var4) {
         var2 = this.connectedLogger;
         var3 = new StringBuilder();
         var3.append("Remote action failed! ");
         var3.append(var4.getMessage());
         var2.logLine(var3.toString());
         throw var4;
      } catch (IOException var5) {
         var2 = this.connectedLogger;
         var3 = new StringBuilder();
         var3.append("Remote action readConfig() failed! ");
         var3.append(var5.getMessage());
         var2.logLine(var3.toString());
         this.closeConnectionReconnect();
         throw var5;
      }
   }

   public void releaseConfiguration() {
      TimoutNotificator.getInstance().unregister(this);
      this.valid = false;
      if (this.remoteStream != null) {
         this.remoteStream.close();
      }

      if (this.ctrlcon != null) {
         try {
            this.out.write("releaseConfiguration()".getBytes());
            this.out.flush();
         } catch (IOException var4) {
            LoggerInterface var2 = this.connectedLogger;
            StringBuilder var3 = new StringBuilder();
            var3.append("Exception during remote configuration release: ");
            var3.append(var4.toString());
            var2.logLine(var3.toString());
            Utils.closeSocket(this.ctrlcon);
         }
      }

      this.ctrlcon = null;
      this.remoteStream = null;
   }

   public void releaseWakeLock() throws IOException {
      this.triggerAction("releaseWakeLock()", (String)null);
   }

   public void restart() throws IOException {
      this.triggerAction("restart()", (String)null);
   }

   public void stop() throws IOException {
      this.triggerAction("stop()", (String)null);
   }

   public void timeoutNotification() {
      ++this.timeOutCounter;
      if (this.timeOutCounter == 2) {
         this.connectedLogger.message("Remote Session is Dead!");
         this.connectedLogger.logLine("Remote Session is Dead! - Closing...!");
         this.timeOutCounter = 0;
         this.closeConnectionReconnect();
      } else {
         this.setTimeout(READ_TIMEOUT);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("REMOTE -> ");
      var1.append(this.host);
      var1.append(":");
      var1.append(this.port);
      return var1.toString();
   }

   public void triggerUpdateFilter() throws IOException {
      this.triggerAction("triggerUpdateFilter()", (String)null);
   }

   public void updateAdditionalHosts(byte[] var1) throws IOException {
      LoggerInterface var2;
      StringBuilder var3;
      try {
         DataOutputStream var7 = new DataOutputStream(this.getOutputStream());
         DataInputStream var8 = new DataInputStream(this.getInputStream());
         var7.write("updateAdditionalHosts()\n".getBytes());
         var7.writeInt(var1.length);
         var7.write(var1);
         var7.flush();
         String var6 = Utils.readLineFromStream(var8);
         if (!var6.equals("OK")) {
            throw new ConfigurationAccess.ConfigurationAccessException(var6, (IOException)null);
         }
      } catch (ConfigurationAccess.ConfigurationAccessException var4) {
         var2 = this.connectedLogger;
         var3 = new StringBuilder();
         var3.append("Remote action failed! ");
         var3.append(var4.getMessage());
         var2.logLine(var3.toString());
         throw var4;
      } catch (IOException var5) {
         var2 = this.connectedLogger;
         var3 = new StringBuilder();
         var3.append("Remote action updateAdditionalHosts() failed! ");
         var3.append(var5.getMessage());
         var2.logLine(var3.toString());
         this.closeConnectionReconnect();
         throw var5;
      }
   }

   public void updateConfig(byte[] var1) throws IOException {
      LoggerInterface var2;
      StringBuilder var3;
      try {
         InputStream var7 = this.getInputStream();
         DataOutputStream var8 = new DataOutputStream(this.getOutputStream());
         var8.write("updateConfig()\n".getBytes());
         var8.writeInt(var1.length);
         var8.write(var1);
         var8.flush();
         String var6 = Utils.readLineFromStream(var7);
         if (!var6.equals("OK")) {
            throw new ConfigurationAccess.ConfigurationAccessException(var6, (IOException)null);
         }
      } catch (ConfigurationAccess.ConfigurationAccessException var4) {
         var2 = this.connectedLogger;
         var3 = new StringBuilder();
         var3.append("Remote action failed! ");
         var3.append(var4.getMessage());
         var2.logLine(var3.toString());
         throw var4;
      } catch (IOException var5) {
         var2 = this.connectedLogger;
         var3 = new StringBuilder();
         var3.append("Remote action updateConfig() failed! ");
         var3.append(var5.getMessage());
         var2.logLine(var3.toString());
         this.closeConnectionReconnect();
         throw var5;
      }
   }

   public void updateFilter(String var1, boolean var2) throws IOException {
      LoggerInterface var3;
      StringBuilder var4;
      try {
         OutputStream var8 = this.getOutputStream();
         InputStream var9 = this.getInputStream();
         StringBuilder var5 = new StringBuilder();
         var5.append("updateFilter()\n");
         var5.append(var1.replace("\n", ";"));
         var5.append("\n");
         var5.append(var2);
         var5.append("\n");
         var8.write(var5.toString().getBytes());
         var8.flush();
         var1 = Utils.readLineFromStream(var9);
         if (!var1.equals("OK")) {
            throw new ConfigurationAccess.ConfigurationAccessException(var1, (IOException)null);
         }
      } catch (ConfigurationAccess.ConfigurationAccessException var6) {
         var3 = this.connectedLogger;
         var4 = new StringBuilder();
         var4.append("Remote action failed! ");
         var4.append(var6.getMessage());
         var3.logLine(var4.toString());
         throw var6;
      } catch (IOException var7) {
         var3 = this.connectedLogger;
         var4 = new StringBuilder();
         var4.append("Remote action  updateFilter() failed! ");
         var4.append(var7.getMessage());
         var3.logLine(var4.toString());
         this.closeConnectionReconnect();
         throw var7;
      }
   }

   public void wakeLock() throws IOException {
      this.triggerAction("wakeLock()", (String)null);
   }

   private class RemoteStream implements Runnable {
      // $FF: renamed from: in java.io.DataInputStream
      DataInputStream field_0;
      DataOutputStream out;
      boolean stopped = false;
      Socket streamCon;
      int streamConId;

      public RemoteStream(int var2) throws IOException {
         Object[] var3 = RemoteAccessClient.this.initConnection();
         this.streamCon = (Socket)var3[1];
         this.field_0 = new DataInputStream((InputStream)var3[2]);
         this.out = new DataOutputStream((OutputStream)var3[3]);
         this.streamConId = (Integer)var3[0];

         try {
            DataOutputStream var7 = this.out;
            StringBuilder var9 = new StringBuilder();
            var9.append("attach\n");
            var9.append(var2);
            var9.append("\n");
            var7.write(var9.toString().getBytes());
            this.out.flush();
            String var8 = Utils.readLineFromStream(this.field_0);
            if (!var8.equals("OK")) {
               throw new IOException(var8);
            }
         } catch (IOException var6) {
            LoggerInterface var4 = RemoteAccessClient.this.connectedLogger;
            StringBuilder var5 = new StringBuilder();
            var5.append("Remote action attach Remote Stream failed! ");
            var5.append(var6.getMessage());
            var4.logLine(var5.toString());
            RemoteAccessClient.this.closeConnectionReconnect();
            throw var6;
         }

         (new Thread(this)).start();
      }

      private void confirmHeartBeat() {
         // $FF: Couldn't be decompiled
      }

      private byte[] getBuffer(byte[] var1, int var2, int var3, int var4) throws IOException {
         if (var2 < var3 && var1.length > var3) {
            return new byte[var3];
         } else if (var2 < var3) {
            return var1;
         } else if (var2 > var4) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Buffer Overflow: ");
            var5.append(var2);
            var5.append(" bytes!");
            throw new IOException(var5.toString());
         } else {
            return new byte[var2];
         }
      }

      public void close() {
         this.stopped = true;
         if (this.streamCon != null) {
            DataOutputStream var1 = this.out;
            synchronized(var1){}

            Throwable var10000;
            boolean var10001;
            label213: {
               label205: {
                  IOException var2;
                  try {
                     try {
                        this.out.write("releaseConfiguration()".getBytes());
                        this.out.flush();
                        break label205;
                     } catch (IOException var28) {
                        var2 = var28;
                     }
                  } catch (Throwable var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label213;
                  }

                  try {
                     LoggerInterface var3 = RemoteAccessClient.this.connectedLogger;
                     StringBuilder var4 = new StringBuilder();
                     var4.append("Exception during remote configuration release: ");
                     var4.append(var2.toString());
                     var3.logLine(var4.toString());
                  } catch (Throwable var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label213;
                  }
               }

               label197:
               try {
                  Utils.closeSocket(this.streamCon);
                  return;
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label197;
               }
            }

            while(true) {
               Throwable var30 = var10000;

               try {
                  throw var30;
               } catch (Throwable var25) {
                  var10000 = var25;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      public void run() {
         byte[] var3 = new byte[2048];

         Exception var10000;
         label76:
         while(true) {
            short var1;
            short var2;
            boolean var10001;
            try {
               if (this.stopped) {
                  return;
               }

               var1 = this.field_0.readShort();
               var2 = this.field_0.readShort();
               var3 = this.getBuffer(var3, var2, 2048, 1024000);
               this.field_0.readFully(var3, 0, var2);
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break;
            }

            switch(var1) {
            case 1:
               try {
                  RemoteAccessClient.this.connectedLogger.log(new String(var3, 0, var2));
                  break;
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label76;
               }
            case 2:
               try {
                  RemoteAccessClient.this.connectedLogger.logLine(new String(var3, 0, var2));
                  break;
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label76;
               }
            case 3:
               try {
                  RemoteAccessClient.this.connectedLogger.message(new String(var3, 0, var2));
                  break;
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label76;
               }
            case 4:
               try {
                  RemoteAccessClient.this.last_dns = new String(var3, 0, var2);
                  break;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label76;
               }
            case 5:
               try {
                  RemoteAccessClient.this.con_cnt = Integer.parseInt(new String(var3, 0, var2));
                  break;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label76;
               }
            case 6:
               try {
                  RemoteAccessClient.this.processHeartBeat();
                  this.confirmHeartBeat();
                  break;
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label76;
               }
            default:
               try {
                  StringBuilder var15 = new StringBuilder();
                  var15.append("Unknown message type: ");
                  var15.append(var1);
                  throw new IOException(var15.toString());
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label76;
               }
            }
         }

         Exception var14 = var10000;
         if (!this.stopped) {
            LoggerInterface var4 = RemoteAccessClient.this.connectedLogger;
            StringBuilder var5 = new StringBuilder();
            var5.append("Exception during RemoteStream read! ");
            var5.append(var14.toString());
            var4.logLine(var5.toString());
            RemoteAccessClient.this.closeConnectionReconnect();
         }

      }
   }
}
