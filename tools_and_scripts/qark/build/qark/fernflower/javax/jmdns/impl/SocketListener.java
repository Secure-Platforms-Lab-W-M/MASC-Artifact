package javax.jmdns.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import javax.jmdns.impl.constants.DNSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SocketListener extends Thread {
   static Logger logger = LoggerFactory.getLogger(SocketListener.class.getName());
   private final JmDNSImpl _jmDNSImpl;

   SocketListener(JmDNSImpl var1) {
      StringBuilder var3 = new StringBuilder();
      var3.append("SocketListener(");
      String var2;
      if (var1 != null) {
         var2 = var1.getName();
      } else {
         var2 = "";
      }

      var3.append(var2);
      var3.append(")");
      super(var3.toString());
      this.setDaemon(true);
      this._jmDNSImpl = var1;
   }

   public JmDNSImpl getDns() {
      return this._jmDNSImpl;
   }

   public void run() {
      label116: {
         IOException var10000;
         label115: {
            byte[] var2;
            DatagramPacket var3;
            boolean var10001;
            try {
               var2 = new byte[8972];
               var3 = new DatagramPacket(var2, var2.length);
            } catch (IOException var15) {
               var10000 = var15;
               var10001 = false;
               break label115;
            }

            while(true) {
               boolean var1;
               try {
                  if (this._jmDNSImpl.isCanceling() || this._jmDNSImpl.isCanceled()) {
                     break label116;
                  }

                  var3.setLength(var2.length);
                  this._jmDNSImpl.getSocket().receive(var3);
                  if (this._jmDNSImpl.isCanceling() || this._jmDNSImpl.isCanceled() || this._jmDNSImpl.isClosing()) {
                     break label116;
                  }

                  var1 = this._jmDNSImpl.isClosed();
               } catch (IOException var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }

               if (var1) {
                  break label116;
               }

               label120: {
                  try {
                     if (this._jmDNSImpl.getLocalHost().shouldIgnorePacket(var3)) {
                        continue;
                     }
                  } catch (IOException var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label120;
                  }

                  DNSIncoming var4;
                  label105: {
                     try {
                        var4 = new DNSIncoming(var3);
                        if (var4.isValidResponseCode()) {
                           if (logger.isTraceEnabled()) {
                              logger.trace("{}.run() JmDNS in:{}", this.getName(), var4.print(true));
                           }
                           break label105;
                        }
                     } catch (IOException var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label120;
                     }

                     try {
                        if (logger.isDebugEnabled()) {
                           logger.debug("{}.run() JmDNS in message with error code: {}", this.getName(), var4.print(true));
                        }
                        continue;
                     } catch (IOException var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label120;
                     }
                  }

                  label95: {
                     try {
                        if (var4.isQuery()) {
                           if (var3.getPort() != DNSConstants.MDNS_PORT) {
                              this._jmDNSImpl.handleQuery(var4, var3.getAddress(), var3.getPort());
                           }
                           break label95;
                        }
                     } catch (IOException var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label120;
                     }

                     try {
                        this._jmDNSImpl.handleResponse(var4);
                        continue;
                     } catch (IOException var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label120;
                     }
                  }

                  try {
                     this._jmDNSImpl.handleQuery(var4, this._jmDNSImpl.getGroup(), DNSConstants.MDNS_PORT);
                     continue;
                  } catch (IOException var9) {
                     var10000 = var9;
                     var10001 = false;
                  }
               }

               IOException var18 = var10000;

               try {
                  Logger var5 = logger;
                  StringBuilder var6 = new StringBuilder();
                  var6.append(this.getName());
                  var6.append(".run() exception ");
                  var5.warn(var6.toString(), var18);
               } catch (IOException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }
            }
         }

         IOException var16 = var10000;
         if (!this._jmDNSImpl.isCanceling() && !this._jmDNSImpl.isCanceled() && !this._jmDNSImpl.isClosing() && !this._jmDNSImpl.isClosed()) {
            Logger var17 = logger;
            StringBuilder var19 = new StringBuilder();
            var19.append(this.getName());
            var19.append(".run() exception ");
            var17.warn(var19.toString(), var16);
            this._jmDNSImpl.recover();
         }
      }

      logger.trace("{}.run() exiting.", this.getName());
   }
}
