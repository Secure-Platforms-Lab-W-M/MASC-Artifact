package org.apache.http.impl.execchain;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.http.HttpClientConnection;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.conn.HttpClientConnectionManager;

class ConnectionHolder implements ConnectionReleaseTrigger, Cancellable, Closeable {
   private final Log log;
   private final HttpClientConnection managedConn;
   private final HttpClientConnectionManager manager;
   private final AtomicBoolean released;
   private volatile boolean reusable;
   private volatile Object state;
   private volatile TimeUnit timeUnit;
   private volatile long validDuration;

   public ConnectionHolder(Log var1, HttpClientConnectionManager var2, HttpClientConnection var3) {
      this.log = var1;
      this.manager = var2;
      this.managedConn = var3;
      this.released = new AtomicBoolean(false);
   }

   private void releaseConnection(boolean var1) {
      if (this.released.compareAndSet(false, true)) {
         Throwable var10000;
         boolean var10001;
         Throwable var85;
         label681: {
            HttpClientConnection var2 = this.managedConn;
            synchronized(var2){}
            if (var1) {
               try {
                  this.manager.releaseConnection(this.managedConn, this.state, this.validDuration, this.timeUnit);
               } catch (Throwable var81) {
                  var10000 = var81;
                  var10001 = false;
                  break label681;
               }
            } else {
               label680: {
                  label677: {
                     label678: {
                        IOException var3;
                        try {
                           try {
                              this.managedConn.close();
                              this.log.debug("Connection discarded");
                              break label677;
                           } catch (IOException var83) {
                              var3 = var83;
                           }
                        } catch (Throwable var84) {
                           var10000 = var84;
                           var10001 = false;
                           break label678;
                        }

                        try {
                           if (this.log.isDebugEnabled()) {
                              this.log.debug(var3.getMessage(), var3);
                           }
                        } catch (Throwable var82) {
                           var10000 = var82;
                           var10001 = false;
                           break label678;
                        }

                        try {
                           this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
                           break label680;
                        } catch (Throwable var79) {
                           var10000 = var79;
                           var10001 = false;
                           break label681;
                        }
                     }

                     var85 = var10000;

                     try {
                        this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
                        throw var85;
                     } catch (Throwable var77) {
                        var10000 = var77;
                        var10001 = false;
                        break label681;
                     }
                  }

                  try {
                     this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
                  } catch (Throwable var80) {
                     var10000 = var80;
                     var10001 = false;
                     break label681;
                  }
               }
            }

            label646:
            try {
               return;
            } catch (Throwable var78) {
               var10000 = var78;
               var10001 = false;
               break label646;
            }
         }

         while(true) {
            var85 = var10000;

            try {
               throw var85;
            } catch (Throwable var76) {
               var10000 = var76;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void abortConnection() {
      if (this.released.compareAndSet(false, true)) {
         HttpClientConnection var1 = this.managedConn;
         synchronized(var1){}

         Throwable var67;
         Throwable var10000;
         boolean var10001;
         label522: {
            label528: {
               label529: {
                  label530: {
                     IOException var2;
                     try {
                        try {
                           this.managedConn.shutdown();
                           this.log.debug("Connection discarded");
                           break label529;
                        } catch (IOException var65) {
                           var2 = var65;
                        }
                     } catch (Throwable var66) {
                        var10000 = var66;
                        var10001 = false;
                        break label530;
                     }

                     try {
                        if (this.log.isDebugEnabled()) {
                           this.log.debug(var2.getMessage(), var2);
                        }
                     } catch (Throwable var64) {
                        var10000 = var64;
                        var10001 = false;
                        break label530;
                     }

                     try {
                        this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
                        break label528;
                     } catch (Throwable var62) {
                        var10000 = var62;
                        var10001 = false;
                        break label522;
                     }
                  }

                  var67 = var10000;

                  try {
                     this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
                     throw var67;
                  } catch (Throwable var60) {
                     var10000 = var60;
                     var10001 = false;
                     break label522;
                  }
               }

               try {
                  this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
               } catch (Throwable var63) {
                  var10000 = var63;
                  var10001 = false;
                  break label522;
               }
            }

            label502:
            try {
               return;
            } catch (Throwable var61) {
               var10000 = var61;
               var10001 = false;
               break label502;
            }
         }

         while(true) {
            var67 = var10000;

            try {
               throw var67;
            } catch (Throwable var59) {
               var10000 = var59;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public boolean cancel() {
      boolean var1 = this.released.get();
      this.log.debug("Cancelling request execution");
      this.abortConnection();
      return var1 ^ true;
   }

   public void close() throws IOException {
      this.releaseConnection(false);
   }

   public boolean isReleased() {
      return this.released.get();
   }

   public boolean isReusable() {
      return this.reusable;
   }

   public void markNonReusable() {
      this.reusable = false;
   }

   public void markReusable() {
      this.reusable = true;
   }

   public void releaseConnection() {
      this.releaseConnection(this.reusable);
   }

   public void setState(Object var1) {
      this.state = var1;
   }

   public void setValidFor(long param1, TimeUnit param3) {
      // $FF: Couldn't be decompiled
   }
}
