package util;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class AsyncBulkLogger implements LoggerInterface, Runnable {
   private boolean closed = false;
   private LoggerInterface out = null;
   private PipedInputStream pin;
   private PipedOutputStream pout;
   private PrintStream psout;
   private boolean timeStampEnabled = false;

   public AsyncBulkLogger(LoggerInterface var1) throws IOException {
      this.out = var1;
      this.logOpen();
   }

   private void logOpen() throws IOException {
      this.pin = new PipedInputStream(10240);
      this.pout = new PipedOutputStream(this.pin);
      this.psout = new PrintStream(this.pout, true);
      Thread var1 = new Thread(this);
      var1.setDaemon(true);
      var1.start();
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

   public void enableTimestamp(boolean var1) {
      this.timeStampEnabled = var1;
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
