package util.conpool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.net.ssl.SSLSocketFactory;
import util.ExecutionEnvironment;
import util.TimeoutListener;
import util.TimeoutTime;
import util.TimoutNotificator;

public class Connection implements TimeoutListener {
   private static Hashtable CUSTOM_HOSTS = getCustomHosts();
   private static String CUSTOM_HOSTS_FILE_NAME = null;
   private static byte[] NO_IP = new byte[]{0, 0, 0, 0};
   private static int POOLTIMEOUT_SECONDS = 300;
   private static HashSet connAquired = new HashSet();
   private static HashMap connPooled = new HashMap();
   private static TimoutNotificator toNotify = TimoutNotificator.getNewInstance();
   boolean aquired = true;
   // $FF: renamed from: in util.conpool.PooledConnectionInputStream
   private PooledConnectionInputStream field_10;
   private PooledConnectionOutputStream out;
   String poolKey;
   private Socket socket = null;
   private InputStream socketIn;
   private OutputStream socketOut;
   boolean ssl = false;
   TimeoutTime timeout;
   boolean valid = true;

   private Connection(String var1, int var2, int var3, boolean var4, SSLSocketFactory var5, Proxy var6) throws IOException {
      InetAddress var7 = null;
      if (CUSTOM_HOSTS != null) {
         var7 = (InetAddress)CUSTOM_HOSTS.get(var1);
      }

      InetAddress var8 = var7;
      if (var7 == null) {
         if (var6 == Proxy.NO_PROXY) {
            var8 = InetAddress.getByName(var1);
         } else {
            var8 = InetAddress.getByAddress(var1, NO_IP);
         }
      }

      InetSocketAddress var9 = new InetSocketAddress(var8, var2);
      this.poolKey = poolKey(var1, var2, var4, var6);
      this.initConnection(var9, var3, var4, var5, var6);
      this.timeout = new TimeoutTime(toNotify);
   }

   private Connection(InetSocketAddress var1, int var2, boolean var3, SSLSocketFactory var4, Proxy var5) throws IOException {
      this.poolKey = poolKey(var1.getAddress().getHostAddress(), var1.getPort(), var3, var5);
      this.initConnection(var1, var2, var3, var4, var5);
      this.timeout = new TimeoutTime(toNotify);
   }

   public static void addCustomHost(InetAddress var0) {
      synchronized(Connection.class){}

      try {
         if (CUSTOM_HOSTS == null) {
            CUSTOM_HOSTS = new Hashtable();
         }

         CUSTOM_HOSTS.put(var0.getHostName(), var0);
      } finally {
         ;
      }

   }

   public static Connection connect(String var0, int var1) throws IOException {
      return connect(var0, var1, -1, false, (SSLSocketFactory)null, Proxy.NO_PROXY);
   }

   public static Connection connect(String var0, int var1, int var2) throws IOException {
      return connect(var0, var1, var2, false, (SSLSocketFactory)null, Proxy.NO_PROXY);
   }

   public static Connection connect(String var0, int var1, int var2, boolean var3, SSLSocketFactory var4, Proxy var5) throws IOException {
      Connection var7 = poolRemove(poolKey(var0, var1, var3, var5));
      Connection var6 = var7;
      if (var7 == null) {
         var6 = new Connection(var0, var1, var2, var3, var4, var5);
      }

      var6.initStreams();
      connAquired.add(var6);
      return var6;
   }

   public static Connection connect(InetSocketAddress var0) throws IOException {
      return connect((InetSocketAddress)var0, -1);
   }

   public static Connection connect(InetSocketAddress var0, int var1) throws IOException {
      return connect(var0, var1, false, (SSLSocketFactory)null, Proxy.NO_PROXY);
   }

   public static Connection connect(InetSocketAddress var0, int var1, boolean var2, SSLSocketFactory var3, Proxy var4) throws IOException {
      Connection var6 = poolRemove(poolKey(var0.getAddress().getHostAddress(), var0.getPort(), var2, var4));
      Connection var5 = var6;
      if (var6 == null) {
         var5 = new Connection(var0, var1, var2, var3, var4);
      }

      var5.initStreams();
      connAquired.add(var5);
      return var5;
   }

   private static Hashtable getCustomHosts() {
      String var0 = CUSTOM_HOSTS_FILE_NAME;
      Hashtable var1 = null;
      Hashtable var2 = null;
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var11 = new StringBuilder();
         var11.append(ExecutionEnvironment.getEnvironment().getWorkDir());
         var11.append(CUSTOM_HOSTS_FILE_NAME);
         File var3 = new File(var11.toString());
         Hashtable var12 = var1;

         IOException var10000;
         label69: {
            boolean var10001;
            try {
               if (!var3.exists()) {
                  return var2;
               }
            } catch (IOException var10) {
               var10000 = var10;
               var10001 = false;
               break label69;
            }

            var12 = var1;

            try {
               var1 = new Hashtable();
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
               break label69;
            }

            var12 = var1;

            BufferedReader var15;
            try {
               var15 = new BufferedReader(new InputStreamReader(new FileInputStream(var3)));
            } catch (IOException var8) {
               var10000 = var8;
               var10001 = false;
               break label69;
            }

            while(true) {
               var12 = var1;

               String var4;
               try {
                  var4 = var15.readLine();
               } catch (IOException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }

               var2 = var1;
               if (var4 == null) {
                  return var2;
               }

               var12 = var1;

               String[] var14;
               try {
                  var14 = parseHosts(var4);
               } catch (IOException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break;
               }

               if (var14 != null) {
                  var12 = var1;

                  try {
                     var1.put(var14[1], InetAddress.getByName(var14[0]));
                  } catch (IOException var5) {
                     var10000 = var5;
                     var10001 = false;
                     break;
                  }
               }
            }
         }

         IOException var13 = var10000;
         var13.printStackTrace();
         return var12;
      }
   }

   private void initConnection(InetSocketAddress var1, int var2, boolean var3, SSLSocketFactory var4, Proxy var5) throws IOException {
      int var6 = var2;
      if (var2 < 0) {
         var6 = 0;
      }

      if (var5 == Proxy.NO_PROXY) {
         this.socket = new Socket();
         this.socket.connect(var1, var6);
      } else {
         if (!(var5 instanceof HttpProxy)) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Only ");
            var7.append(HttpProxy.class.getName());
            var7.append(" supported for creating connection over tunnel!");
            throw new IOException(var7.toString());
         }

         this.socket = ((HttpProxy)var5).openTunnel(var1, var6);
      }

      if (var3) {
         this.socket.setSoTimeout(var6);
         SSLSocketFactory var8 = var4;
         if (var4 == null) {
            var8 = (SSLSocketFactory)SSLSocketFactory.getDefault();
         }

         this.socket = var8.createSocket(this.socket, var1.getHostName(), var1.getPort(), true);
         this.ssl = true;
      }

      this.socketIn = this.socket.getInputStream();
      this.socketOut = this.socket.getOutputStream();
      if (var3) {
         this.socket.setSoTimeout(0);
      }

   }

   private void initStreams() {
      this.field_10 = new PooledConnectionInputStream(this.socketIn);
      this.out = new PooledConnectionOutputStream(this.socketOut);
   }

   public static void invalidate() {
      HashMap var2 = connPooled;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label600: {
         Vector[] var3;
         try {
            var3 = (Vector[])connPooled.values().toArray(new Vector[0]);
         } catch (Throwable var60) {
            var10000 = var60;
            var10001 = false;
            break label600;
         }

         int var0 = 0;

         while(true) {
            Connection[] var4;
            try {
               if (var0 >= var3.length) {
                  break;
               }

               var4 = (Connection[])var3[var0].toArray(new Connection[0]);
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label600;
            }

            int var1 = 0;

            while(true) {
               try {
                  if (var1 >= var4.length) {
                     break;
                  }

                  var4[var1].release(false);
               } catch (Throwable var59) {
                  var10000 = var59;
                  var10001 = false;
                  break label600;
               }

               ++var1;
            }

            ++var0;
         }

         Connection[] var61;
         try {
            var61 = (Connection[])connAquired.toArray(new Connection[0]);
         } catch (Throwable var57) {
            var10000 = var57;
            var10001 = false;
            break label600;
         }

         var0 = 0;

         while(true) {
            try {
               if (var0 >= var61.length) {
                  break;
               }

               var61[var0].release(false);
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label600;
            }

            ++var0;
         }

         label564:
         try {
            return;
         } catch (Throwable var55) {
            var10000 = var55;
            var10001 = false;
            break label564;
         }
      }

      while(true) {
         Throwable var62 = var10000;

         try {
            throw var62;
         } catch (Throwable var54) {
            var10000 = var54;
            var10001 = false;
            continue;
         }
      }
   }

   private boolean isAlive() {
      // $FF: Couldn't be decompiled
   }

   private static String[] parseHosts(String var0) {
      if (!var0.startsWith("#") && !var0.trim().equals("")) {
         StringTokenizer var1 = new StringTokenizer(var0);
         return var1.countTokens() >= 2 ? new String[]{var1.nextToken().trim(), var1.nextToken().trim()} : new String[]{"127.0.0.1", var1.nextToken().trim()};
      } else {
         return null;
      }
   }

   private static String poolKey(String var0, int var1, boolean var2, Proxy var3) {
      StringBuilder var4;
      if (var2) {
         var4 = new StringBuilder();
         var4.append(var0);
         var4.append(":");
         var4.append(var1);
         var4.append(":ssl:");
         var4.append(var3.hashCode());
         return var4.toString();
      } else {
         var4 = new StringBuilder();
         var4.append(var0);
         var4.append(":");
         var4.append(var1);
         var4.append(":plain:");
         var4.append(var3.hashCode());
         return var4.toString();
      }
   }

   public static Connection poolRemove(String var0) {
      HashMap var5 = connPooled;
      synchronized(var5){}

      Throwable var10000;
      boolean var10001;
      label680: {
         Vector var6;
         try {
            var6 = (Vector)connPooled.get(var0);
         } catch (Throwable var78) {
            var10000 = var78;
            var10001 = false;
            break label680;
         }

         Connection var3 = null;
         if (var6 == null) {
            label652:
            try {
               return null;
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label652;
            }
         } else {
            label684: {
               boolean var1 = false;

               while(!var1) {
                  Connection var4;
                  try {
                     if (var6.isEmpty()) {
                        break;
                     }

                     var4 = (Connection)var6.remove(var6.size() - 1);
                     if (var4.aquired) {
                        throw new IllegalStateException("Inconsistent Connection State - Cannot take already aquired connection from pool!");
                     }
                  } catch (Throwable var77) {
                     var10000 = var77;
                     var10001 = false;
                     break label684;
                  }

                  boolean var2;
                  try {
                     var4.aquired = true;
                     toNotify.unregister(var4);
                     var2 = var4.isAlive();
                  } catch (Throwable var76) {
                     var10000 = var76;
                     var10001 = false;
                     break label684;
                  }

                  var3 = var4;
                  var1 = var2;
                  if (!var2) {
                     try {
                        var4.release(false);
                     } catch (Throwable var75) {
                        var10000 = var75;
                        var10001 = false;
                        break label684;
                     }

                     var3 = null;
                     var1 = var2;
                  }
               }

               try {
                  if (var6.isEmpty()) {
                     connPooled.remove(var0);
                  }
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label684;
               }

               label654:
               try {
                  return var3;
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label654;
               }
            }
         }
      }

      while(true) {
         Throwable var79 = var10000;

         try {
            throw var79;
         } catch (Throwable var71) {
            var10000 = var71;
            var10001 = false;
            continue;
         }
      }
   }

   public static void poolReuse(Connection var0) {
      HashMap var3 = connPooled;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label265: {
         try {
            if (!var0.aquired) {
               throw new IllegalStateException("Inconsistent Connection State - Cannot release non aquired connection");
            }
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label265;
         }

         Vector var2;
         try {
            var0.aquired = false;
            var2 = (Vector)connPooled.get(var0.poolKey);
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label265;
         }

         Vector var1 = var2;
         if (var2 == null) {
            try {
               var1 = new Vector();
               connPooled.put(var0.poolKey, var1);
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label265;
            }
         }

         label248:
         try {
            toNotify.register(var0);
            var0.timeout.setTimeout((long)(POOLTIMEOUT_SECONDS * 1000));
            var1.add(var0);
            return;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label248;
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

   public static void setCustomHostsFile(String var0) {
      CUSTOM_HOSTS_FILE_NAME = var0;
   }

   public static void setPoolTimeoutSeconds(int var0) {
      POOLTIMEOUT_SECONDS = var0;
   }

   public String getDestination() {
      return this.poolKey;
   }

   public InputStream getInputStream() {
      return this.field_10;
   }

   public OutputStream getOutputStream() {
      return this.out;
   }

   public long getTimoutTime() {
      return this.timeout.getTimeout();
   }

   public long[] getTraffic() {
      if (!this.aquired) {
         throw new IllegalStateException("Inconsistent Connection State - Connection is not aquired!");
      } else {
         return new long[]{this.field_10.getTraffic(), this.out.getTraffic()};
      }
   }

   public void release(boolean var1) {
      if (this.valid) {
         connAquired.remove(this);
         if (var1) {
            this.field_10.invalidate();
            this.out.invalidate();

            try {
               this.socket.setSoTimeout(0);
            } catch (SocketException var3) {
               this.release(false);
               return;
            }

            poolReuse(this);
         } else {
            try {
               this.valid = false;
               if (!this.ssl) {
                  this.socket.shutdownOutput();
                  this.socket.shutdownInput();
               }

               this.socket.close();
            } catch (IOException var4) {
            }
         }
      }
   }

   public void setSoTimeout(int var1) throws SocketException {
      this.socket.setSoTimeout(var1);
   }

   public void timeoutNotification() {
      HashMap var2 = connPooled;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label291: {
         Vector var3;
         try {
            var3 = (Vector)connPooled.get(this.poolKey);
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label291;
         }

         if (var3 == null) {
            label281:
            try {
               return;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label281;
            }
         } else {
            label295: {
               boolean var1;
               try {
                  var1 = var3.remove(this);
                  if (var3.isEmpty()) {
                     connPooled.remove(this.poolKey);
                  }
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label295;
               }

               try {
                  ;
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label295;
               }

               if (var1) {
                  this.release(false);
               }

               return;
            }
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
}
