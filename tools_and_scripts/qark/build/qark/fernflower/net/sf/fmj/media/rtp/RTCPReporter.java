package net.sf.fmj.media.rtp;

import java.net.InetAddress;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.fmj.media.Log;
import net.sf.fmj.media.rtp.util.RTPMediaThread;

public class RTCPReporter implements Runnable {
   private static final Logger logger = Logger.getLogger(RTCPReporter.class.getName());
   SSRCCache cache;
   boolean closed = false;
   String cname;
   InetAddress host;
   Random myrand;
   RTPMediaThread reportthread;
   boolean restart = false;
   RTCPTransmitter transmit;

   public RTCPReporter(SSRCCache var1, RTCPTransmitter var2) {
      this.cache = var1;
      this.setTransmitter(var2);
      RTPMediaThread var3 = new RTPMediaThread(this, "RTCP Reporter");
      this.reportthread = var3;
      var3.useControlPriority();
      this.reportthread.setDaemon(true);
      this.reportthread.start();
   }

   public void close(String param1) {
      // $FF: Couldn't be decompiled
   }

   public void releasessrc(String var1) {
      this.transmit.bye(var1);
   }

   public void run() {
      if (this.restart) {
         this.restart = false;
      }

      while(true) {
         SSRCCache var3 = this.cache;
         double var1 = var3.calcReportInterval(var3.ourssrc.sender, false);
         if (logger.isLoggable(Level.FINEST)) {
            Logger var30 = logger;
            StringBuilder var4 = new StringBuilder();
            var4.append("RTCP reporting for ");
            var4.append("running again after ");
            var4.append(var1);
            var4.append(" ms.");
            var30.finest(var4.toString());
         }

         RTPMediaThread var31 = this.reportthread;
         synchronized(var31){}

         label302: {
            Throwable var10000;
            boolean var10001;
            label303: {
               label292: {
                  InterruptedException var32;
                  try {
                     try {
                        this.reportthread.wait((long)var1);
                        break label292;
                     } catch (InterruptedException var28) {
                        var32 = var28;
                     }
                  } catch (Throwable var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label303;
                  }

                  try {
                     Log.dumpStack(var32);
                  } catch (Throwable var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label303;
                  }
               }

               label284:
               try {
                  break label302;
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label284;
               }
            }

            while(true) {
               Throwable var33 = var10000;

               try {
                  throw var33;
               } catch (Throwable var25) {
                  var10000 = var25;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (this.closed) {
            return;
         }

         if (!this.restart) {
            this.transmit.report();
         } else {
            this.restart = false;
         }
      }
   }

   public void setTransmitter(RTCPTransmitter var1) {
      this.transmit = var1;
   }
}
