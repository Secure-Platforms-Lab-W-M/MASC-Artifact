package dnsfilter.remote;

import dnsfilter.ConfigurationAccess;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import util.AsyncLogger;
import util.Encryption;
import util.GroupedLogger;
import util.Logger;
import util.LoggerInterface;
import util.TimeoutListener;
import util.TimoutNotificator;
import util.Utils;

public class RemoteAccessServer implements Runnable {
   private static int sessionId = 0;
   private ServerSocket server;
   private HashMap sessions = new HashMap();
   boolean stopped = false;

   public RemoteAccessServer(int var1, String var2) throws IOException {
      Encryption.init_AES(var2);
      this.server = new ServerSocket(var1);
      (new Thread(this)).start();
      LoggerInterface var4 = Logger.getLogger();
      StringBuilder var3 = new StringBuilder();
      var3.append("Started RemoteAccess Server on port ");
      var3.append(var1);
      var4.logLine(var3.toString());
   }

   private String readStringFromStream(InputStream var1, byte[] var2) throws IOException {
      int var3 = Utils.readLineBytesFromStream(var1, var2, true, false);
      if (var3 == -1) {
         throw new EOFException("Stream is closed!");
      } else {
         return (new String(var2, 0, var3)).trim();
      }
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void stop() {
      this.stopped = true;
      Collection var2 = this.sessions.values();
      int var1 = 0;

      for(RemoteAccessServer.RemoteSession[] var4 = (RemoteAccessServer.RemoteSession[])var2.toArray(new RemoteAccessServer.RemoteSession[0]); var1 < var4.length; ++var1) {
         var4[var1].killSession();
      }

      try {
         this.server.close();
      } catch (IOException var3) {
         Logger.getLogger().logException(var3);
      }
   }

   private class RemoteSession implements Runnable, TimeoutListener {
      int connectedSessionId;
      boolean doReconnect;
      // $FF: renamed from: id int
      int field_2;
      // $FF: renamed from: in java.io.DataInputStream
      DataInputStream field_3;
      boolean killed;
      long lastHeartBeatConfirm;
      DataOutputStream out;
      LoggerInterface remoteLogger;
      Socket socket;
      long timeout;

      private RemoteSession(Socket var2, InputStream var3, OutputStream var4, int var5) throws IOException {
         this.connectedSessionId = -1;
         this.killed = false;
         this.doReconnect = false;
         this.timeout = Long.MAX_VALUE;
         this.lastHeartBeatConfirm = System.currentTimeMillis();
         this.field_2 = var5;
         this.socket = var2;
         this.out = new DataOutputStream(var4);
         this.field_3 = new DataInputStream(var3);
         RemoteAccessServer.this.sessions.put(var5, this);
         LoggerInterface var6 = Logger.getLogger();
         StringBuilder var7 = new StringBuilder();
         var7.append("New Remote Session ");
         var7.append(var5);
         var7.append(" from :");
         var7.append(var2);
         var6.logLine(var7.toString());
         (new Thread(this)).start();
      }

      // $FF: synthetic method
      RemoteSession(Socket var2, InputStream var3, OutputStream var4, int var5, Object var6) throws IOException {
         this(var2, var3, var4, var5);
      }

      private void attachStream() throws IOException {
         try {
            this.connectedSessionId = Integer.parseInt(Utils.readLineFromStream(this.field_3));
         } catch (Exception var27) {
            throw new IOException(var27);
         }

         this.remoteLogger = new AsyncLogger(new LoggerInterface() {
            public void closeLogger() {
            }

            public void log(String var1) {
               this.sendLog(1, var1);
            }

            public void logException(Exception var1) {
               StringWriter var2 = new StringWriter();
               var1.printStackTrace(new PrintWriter(var2));
               StringBuilder var3 = new StringBuilder();
               var3.append(var2.toString());
               var3.append("\n");
               this.log(var3.toString());
            }

            public void logLine(String var1) {
               this.sendLog(2, var1);
            }

            public void message(String var1) {
               this.sendLog(3, var1);
            }

            public void sendLog(int var1, String var2) {
               DataOutputStream var3 = RemoteSession.this.out;
               synchronized(var3){}

               Throwable var10000;
               boolean var10001;
               label193: {
                  label187: {
                     IOException var31;
                     try {
                        try {
                           RemoteSession.this.out.writeShort(5);
                           StringBuilder var34 = new StringBuilder();
                           var34.append(ConfigurationAccess.getLocal().openConnectionsCount());
                           var34.append("");
                           byte[] var35 = var34.toString().getBytes();
                           RemoteSession.this.out.writeShort(var35.length);
                           RemoteSession.this.out.write(var35);
                           RemoteSession.this.out.writeShort(4);
                           var35 = ConfigurationAccess.getLocal().getLastDNSAddress().getBytes();
                           RemoteSession.this.out.writeShort(var35.length);
                           RemoteSession.this.out.write(var35);
                           byte[] var32 = var2.getBytes();
                           RemoteSession.this.out.writeShort(var1);
                           RemoteSession.this.out.writeShort(var32.length);
                           RemoteSession.this.out.write(var32);
                           RemoteSession.this.out.flush();
                           break label187;
                        } catch (IOException var29) {
                           var31 = var29;
                        }
                     } catch (Throwable var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label193;
                     }

                     try {
                        RemoteSession.this.killSession();
                        LoggerInterface var4 = Logger.getLogger();
                        StringBuilder var5 = new StringBuilder();
                        var5.append("Exception during remote logging! ");
                        var5.append(var31.toString());
                        var4.logLine(var5.toString());
                     } catch (Throwable var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label193;
                     }
                  }

                  label179:
                  try {
                     return;
                  } catch (Throwable var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label179;
                  }
               }

               while(true) {
                  Throwable var33 = var10000;

                  try {
                     throw var33;
                  } catch (Throwable var26) {
                     var10000 = var26;
                     var10001 = false;
                     continue;
                  }
               }
            }
         });
         DataOutputStream var1 = this.out;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label214: {
            label207: {
               try {
                  try {
                     ((GroupedLogger)Logger.getLogger()).attachLogger(this.remoteLogger);
                     break label207;
                  } catch (ClassCastException var31) {
                  }
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label214;
               }

               try {
                  Logger.setLogger(new GroupedLogger(new LoggerInterface[]{Logger.getLogger(), this.remoteLogger}));
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label214;
               }
            }

            label199:
            try {
               this.out.write("OK\n".getBytes());
               this.doHeartBeat(RemoteAccessClient.READ_TIMEOUT);
               return;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label199;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      }

      private boolean checkLastConfirmedHeartBeat() {
         if (System.currentTimeMillis() - this.lastHeartBeatConfirm > (long)(2 * RemoteAccessClient.READ_TIMEOUT)) {
            Logger.getLogger().logLine("Heartbeat Confirmation not received - Dead Session!");
            this.killSession();
            return false;
         } else {
            return true;
         }
      }

      private void doHeartBeat(int param1) {
         // $FF: Couldn't be decompiled
      }

      private void executeAction(String var1) throws IOException {
         StringBuilder var5;
         ConfigurationAccess.ConfigurationAccessException var10000;
         DataOutputStream var32;
         label194: {
            boolean var10001;
            label196: {
               try {
                  if (var1.equals("getConfig()")) {
                     Properties var30 = ConfigurationAccess.getLocal().getConfig();
                     this.out.write("OK\n".getBytes());
                     ObjectOutputStream var33 = new ObjectOutputStream(this.out);
                     var33.writeObject(var30);
                     var33.flush();
                     break label196;
                  }
               } catch (ConfigurationAccess.ConfigurationAccessException var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label194;
               }

               byte[] var29;
               try {
                  if (var1.equals("readConfig()")) {
                     var29 = ConfigurationAccess.getLocal().readConfig();
                     this.out.write("OK\n".getBytes());
                     this.out.writeInt(var29.length);
                     this.out.write(var29);
                     this.out.flush();
                     break label196;
                  }
               } catch (ConfigurationAccess.ConfigurationAccessException var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label194;
               }

               try {
                  if (var1.equals("updateConfig()")) {
                     var29 = new byte[this.field_3.readInt()];
                     this.field_3.readFully(var29);
                     ConfigurationAccess.getLocal().updateConfig(var29);
                     this.out.write("OK\n".getBytes());
                     this.out.flush();
                     break label196;
                  }
               } catch (ConfigurationAccess.ConfigurationAccessException var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label194;
               }

               int var2;
               try {
                  if (var1.equals("getAdditionalHosts()")) {
                     var2 = this.field_3.readInt();
                     var29 = ConfigurationAccess.getLocal().getAdditionalHosts(var2);
                     this.out.write("OK\n".getBytes());
                     this.out.writeInt(var29.length);
                     this.out.write(var29);
                     this.out.flush();
                     break label196;
                  }
               } catch (ConfigurationAccess.ConfigurationAccessException var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label194;
               }

               try {
                  if (var1.equals("updateAdditionalHosts()")) {
                     var29 = new byte[this.field_3.readInt()];
                     this.field_3.readFully(var29);
                     ConfigurationAccess.getLocal().updateAdditionalHosts(var29);
                     this.out.write("OK\n".getBytes());
                     this.out.flush();
                     break label196;
                  }
               } catch (ConfigurationAccess.ConfigurationAccessException var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label194;
               }

               boolean var3;
               try {
                  if (var1.equals("updateFilter()")) {
                     var1 = Utils.readLineFromStream(this.field_3).replace(";", "\n");
                     var3 = Boolean.parseBoolean(Utils.readLineFromStream(this.field_3));
                     ConfigurationAccess.getLocal().updateFilter(var1, var3);
                     this.out.write("OK\n".getBytes());
                     this.out.flush();
                     break label196;
                  }
               } catch (ConfigurationAccess.ConfigurationAccessException var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label194;
               }

               try {
                  if (var1.equals("restart()")) {
                     ConfigurationAccess.getLocal().restart();
                     this.out.write("OK\n".getBytes());
                     this.out.flush();
                     break label196;
                  }
               } catch (ConfigurationAccess.ConfigurationAccessException var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label194;
               }

               try {
                  if (var1.equals("stop()")) {
                     ConfigurationAccess.getLocal().stop();
                     this.out.write("OK\n".getBytes());
                     this.out.flush();
                     break label196;
                  }
               } catch (ConfigurationAccess.ConfigurationAccessException var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label194;
               }

               try {
                  var3 = var1.equals("getFilterStatistics()");
               } catch (ConfigurationAccess.ConfigurationAccessException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label194;
               }

               var2 = 0;
               if (var3) {
                  try {
                     long[] var28 = ConfigurationAccess.getLocal().getFilterStatistics();
                     this.out.write("OK\n".getBytes());
                     this.out.writeLong(var28[0]);
                     this.out.writeLong(var28[1]);
                     this.out.flush();
                  } catch (ConfigurationAccess.ConfigurationAccessException var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label194;
                  }
               } else {
                  label197: {
                     try {
                        if (var1.equals("triggerUpdateFilter()")) {
                           ConfigurationAccess.getLocal().triggerUpdateFilter();
                           this.out.write("OK\n".getBytes());
                           this.out.flush();
                           break label197;
                        }
                     } catch (ConfigurationAccess.ConfigurationAccessException var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label194;
                     }

                     try {
                        if (var1.equals("doBackup()")) {
                           ConfigurationAccess.getLocal().doBackup(Utils.readLineFromStream(this.field_3));
                           this.out.write("OK\n".getBytes());
                           this.out.flush();
                           break label197;
                        }
                     } catch (ConfigurationAccess.ConfigurationAccessException var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label194;
                     }

                     try {
                        if (var1.equals("doRestore()")) {
                           ConfigurationAccess.getLocal().doRestore(Utils.readLineFromStream(this.field_3));
                           this.out.write("OK\n".getBytes());
                           this.out.flush();
                           break label197;
                        }
                     } catch (ConfigurationAccess.ConfigurationAccessException var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label194;
                     }

                     try {
                        if (var1.equals("doRestoreDefaults()")) {
                           ConfigurationAccess.getLocal().doRestoreDefaults();
                           this.out.write("OK\n".getBytes());
                           this.out.flush();
                           break label197;
                        }
                     } catch (ConfigurationAccess.ConfigurationAccessException var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label194;
                     }

                     try {
                        if (var1.equals("wakeLock()")) {
                           ConfigurationAccess.getLocal().wakeLock();
                           this.out.write("OK\n".getBytes());
                           this.out.flush();
                           break label197;
                        }
                     } catch (ConfigurationAccess.ConfigurationAccessException var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label194;
                     }

                     try {
                        if (var1.equals("releaseWakeLock()")) {
                           ConfigurationAccess.getLocal().releaseWakeLock();
                           this.out.write("OK\n".getBytes());
                           this.out.flush();
                           break label197;
                        }
                     } catch (ConfigurationAccess.ConfigurationAccessException var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label194;
                     }

                     String[] var27;
                     label142: {
                        try {
                           if (var1.equals("getAvailableBackups()")) {
                              var27 = ConfigurationAccess.getLocal().getAvailableBackups();
                              this.out.write("OK\n".getBytes());
                              var32 = this.out;
                              var5 = new StringBuilder();
                              var5.append(var27.length);
                              var5.append("\n");
                              var32.write(var5.toString().getBytes());
                              break label142;
                           }
                        } catch (ConfigurationAccess.ConfigurationAccessException var19) {
                           var10000 = var19;
                           var10001 = false;
                           break label194;
                        }

                        try {
                           StringBuilder var4 = new StringBuilder();
                           var4.append("Unknown action: ");
                           var4.append(var1);
                           throw new ConfigurationAccess.ConfigurationAccessException(var4.toString());
                        } catch (ConfigurationAccess.ConfigurationAccessException var7) {
                           var10000 = var7;
                           var10001 = false;
                           break label194;
                        }
                     }

                     while(true) {
                        label133: {
                           try {
                              if (var2 < var27.length) {
                                 var32 = this.out;
                                 var5 = new StringBuilder();
                                 var5.append(var27[var2]);
                                 var5.append("\n");
                                 var32.write(var5.toString().getBytes());
                                 break label133;
                              }
                           } catch (ConfigurationAccess.ConfigurationAccessException var18) {
                              var10000 = var18;
                              var10001 = false;
                              break label194;
                           }

                           try {
                              this.out.flush();
                              break;
                           } catch (ConfigurationAccess.ConfigurationAccessException var8) {
                              var10000 = var8;
                              var10001 = false;
                              break label194;
                           }
                        }

                        ++var2;
                     }
                  }
               }
            }

            try {
               return;
            } catch (ConfigurationAccess.ConfigurationAccessException var6) {
               var10000 = var6;
               var10001 = false;
            }
         }

         ConfigurationAccess.ConfigurationAccessException var31 = var10000;
         var32 = this.out;
         var5 = new StringBuilder();
         var5.append(var31.getMessage().replace("\n", "\t"));
         var5.append("\n");
         var32.write(var5.toString().getBytes());
         this.out.flush();
      }

      private void heartBeatConfirmed() {
         this.lastHeartBeatConfirm = System.currentTimeMillis();
      }

      public long getTimoutTime() {
         return this.timeout;
      }

      public void killSession() {
         if (!this.killed) {
            this.killed = true;
            TimoutNotificator.getInstance().unregister(this);
            if (this.remoteLogger != null) {
               this.remoteLogger.closeLogger();
               ((GroupedLogger)Logger.getLogger()).detachLogger(this.remoteLogger);
            }

            Utils.closeSocket(this.socket);
            RemoteAccessServer.this.sessions.remove(this.field_2);
            if (this.connectedSessionId != -1) {
               RemoteAccessServer.RemoteSession var1 = (RemoteAccessServer.RemoteSession)RemoteAccessServer.this.sessions.get(this.connectedSessionId);
               if (var1 != null) {
                  var1.killSession();
               }
            }

         }
      }

      public void reconnectSession(Socket var1, InputStream var2, OutputStream var3) throws IOException {
         this.doReconnect = true;
         Socket var4 = this.socket;
         this.socket = var1;
         this.out = new DataOutputStream(var1.getOutputStream());
         this.field_3 = new DataInputStream(var1.getInputStream());
         Utils.closeSocket(var4);
      }

      public void run() {
         byte[] var4 = new byte[1024];
         String var1 = "";

         LoggerInterface var24;
         StringBuilder var25;
         while(!this.killed) {
            String var2 = var1;

            IOException var10000;
            label115: {
               ConfigurationAccess.ConfigurationAccessException var27;
               label123: {
                  String var3;
                  boolean var10001;
                  try {
                     var3 = RemoteAccessServer.this.readStringFromStream(this.field_3, var4);
                  } catch (ConfigurationAccess.ConfigurationAccessException var20) {
                     var27 = var20;
                     var10001 = false;
                     break label123;
                  } catch (IOException var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label115;
                  }

                  var2 = var3;
                  var1 = var3;

                  label124: {
                     try {
                        if (!var3.equals("attach")) {
                           break label124;
                        }
                     } catch (ConfigurationAccess.ConfigurationAccessException var18) {
                        var27 = var18;
                        var10001 = false;
                        break label123;
                     } catch (IOException var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label115;
                     }

                     var2 = var3;
                     var1 = var3;

                     try {
                        this.attachStream();
                     } catch (ConfigurationAccess.ConfigurationAccessException var6) {
                        var27 = var6;
                        var10001 = false;
                        break label123;
                     } catch (IOException var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label115;
                     }

                     var1 = var3;
                     continue;
                  }

                  var2 = var3;
                  var1 = var3;

                  label125: {
                     try {
                        if (!var3.equals("releaseConfiguration()")) {
                           break label125;
                        }
                     } catch (ConfigurationAccess.ConfigurationAccessException var16) {
                        var27 = var16;
                        var10001 = false;
                        break label123;
                     } catch (IOException var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label115;
                     }

                     var2 = var3;
                     var1 = var3;

                     try {
                        this.killSession();
                     } catch (ConfigurationAccess.ConfigurationAccessException var8) {
                        var27 = var8;
                        var10001 = false;
                        break label123;
                     } catch (IOException var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label115;
                     }

                     var1 = var3;
                     continue;
                  }

                  var2 = var3;
                  var1 = var3;

                  label126: {
                     try {
                        if (var3.equals("confirmHeartBeat()")) {
                           break label126;
                        }
                     } catch (ConfigurationAccess.ConfigurationAccessException var14) {
                        var27 = var14;
                        var10001 = false;
                        break label123;
                     } catch (IOException var15) {
                        var10000 = var15;
                        var10001 = false;
                        break label115;
                     }

                     var2 = var3;
                     var1 = var3;

                     try {
                        this.executeAction(var3);
                     } catch (ConfigurationAccess.ConfigurationAccessException var12) {
                        var27 = var12;
                        var10001 = false;
                        break label123;
                     } catch (IOException var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label115;
                     }

                     var1 = var3;
                     continue;
                  }

                  var2 = var3;
                  var1 = var3;

                  try {
                     this.heartBeatConfirmed();
                  } catch (ConfigurationAccess.ConfigurationAccessException var10) {
                     var27 = var10;
                     var10001 = false;
                     break label123;
                  } catch (IOException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label115;
                  }

                  var1 = var3;
                  continue;
               }

               ConfigurationAccess.ConfigurationAccessException var26 = var27;
               LoggerInterface var23 = Logger.getLogger();
               StringBuilder var5 = new StringBuilder();
               var5.append("RemoteServer Exception processing ");
               var5.append(var1);
               var5.append("! ");
               var5.append(var26.toString());
               var23.logLine(var5.toString());
               continue;
            }

            IOException var22 = var10000;
            if (!this.doReconnect) {
               var1 = var2;
               if (!this.killed) {
                  var24 = Logger.getLogger();
                  var25 = new StringBuilder();
                  var25.append("Exception during RemoteServer Session read! ");
                  var25.append(var22.toString());
                  var24.logLine(var25.toString());
                  this.killSession();
                  break;
               }
            } else {
               Logger.getLogger().logLine("Reconnected Remote!");
               this.doReconnect = false;
               var1 = var2;
            }
         }

         var24 = Logger.getLogger();
         var25 = new StringBuilder();
         var25.append("Remote Session ");
         var25.append(this.field_2);
         var25.append(" closed! ");
         var25.append(this.socket);
         var24.logLine(var25.toString());
      }

      public void timeoutNotification() {
         if (this.checkLastConfirmedHeartBeat()) {
            this.doHeartBeat(RemoteAccessClient.READ_TIMEOUT);
         }

      }
   }
}
