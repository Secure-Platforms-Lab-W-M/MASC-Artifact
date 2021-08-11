package util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class AsyncLogger implements LoggerInterface, Runnable {
   static final int LOG = 1;
   static final int LOG_EXC = 4;
   static final int LOG_LN = 2;
   static final int LOG_MSG = 3;
   private boolean closed = false;
   private LoggerInterface out = null;
   private PipedInputStream pin;
   private DataOutputStream pout;

   public AsyncLogger(LoggerInterface var1) throws IOException {
      this.out = var1;
      this.logOpen();
   }

   private void logOpen() throws IOException {
      this.pin = new PipedInputStream(10240);
      this.pout = new DataOutputStream(new PipedOutputStream(this.pin));
      Thread var1 = new Thread(this);
      var1.setDaemon(true);
      var1.start();
   }

   private void writeLog(int var1, byte[] var2) {
      try {
         this.pout.writeShort(var1);
         this.pout.writeInt(var2.length);
         this.pout.write(var2);
         this.pout.flush();
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public void closeLogger() {
      PipedInputStream var1 = this.pin;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label193: {
         label187: {
            IOException var2;
            try {
               try {
                  this.closed = true;
                  this.pout.close();
                  this.pin.notifyAll();
                  break label187;
               } catch (IOException var26) {
                  var2 = var26;
               }
            } catch (Throwable var27) {
               var10000 = var27;
               var10001 = false;
               break label193;
            }

            try {
               var2.printStackTrace();
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label193;
            }
         }

         label179:
         try {
            return;
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label179;
         }
      }

      while(true) {
         Throwable var28 = var10000;

         try {
            throw var28;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            continue;
         }
      }
   }

   public void log(String param1) {
      // $FF: Couldn't be decompiled
   }

   public void logException(Exception param1) {
      // $FF: Couldn't be decompiled
   }

   public void logLine(String param1) {
      // $FF: Couldn't be decompiled
   }

   public void message(String param1) {
      // $FF: Couldn't be decompiled
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }
}
