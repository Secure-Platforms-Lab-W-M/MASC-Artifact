package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class FileLogger implements LoggerInterface, Runnable {
   private boolean closed = false;
   private int curSlot = 0;
   private long curSlotSize = 0L;
   private OutputStream fout = null;
   private String header;
   private String logFolderPath;
   private String name;
   private PipedInputStream pin;
   private PipedOutputStream pout;
   private PrintStream psout;
   private int slotCount;
   private long slotSize;
   private boolean timeStampEnabled = false;

   public FileLogger(String var1, String var2, long var3, int var5, String var6) throws IOException {
      if (var3 >= 1L && var5 >= 1) {
         StringBuilder var7 = new StringBuilder();
         var7.append(var1);
         var7.append("/");
         var7.append(var2);
         this.logFolderPath = var7.toString();
         this.name = var2;
         this.slotSize = var3;
         this.slotCount = var5;
         this.header = var6;
         this.logOpen();
      } else {
         throw new IllegalArgumentException("slotSize and slotCount must not be less than 1");
      }
   }

   private OutputStream getOutputStream() throws IOException {
      if (this.curSlotSize < this.slotSize) {
         return this.fout;
      } else {
         this.fout.flush();
         this.fout.close();
         this.curSlot = (this.curSlot + 1) % this.slotCount;
         StringBuilder var1 = new StringBuilder();
         var1.append(this.logFolderPath);
         var1.append("/");
         var1.append(this.name);
         var1.append("_");
         var1.append(this.curSlot);
         var1.append(".log");
         this.fout = new FileOutputStream(new File(var1.toString()));
         this.curSlotSize = 0L;
         if (this.header != null) {
            OutputStream var3 = this.fout;
            StringBuilder var2 = new StringBuilder();
            var2.append(this.header);
            var2.append("\r\n");
            var3.write(var2.toString().getBytes());
            this.fout.flush();
         }

         return this.fout;
      }
   }

   private void logOpen() throws IOException {
      File var6 = new File(this.logFolderPath);
      if (!var6.exists()) {
         var6.mkdirs();
      }

      long var2 = 0L;

      long var4;
      StringBuilder var8;
      for(int var1 = 0; var1 < this.slotCount; var2 = var4) {
         var8 = new StringBuilder();
         var8.append(this.logFolderPath);
         var8.append("/");
         var8.append(this.name);
         var8.append("_");
         var8.append(var1);
         var8.append(".log");
         var6 = new File(var8.toString());
         var4 = var2;
         if (var6.exists()) {
            var4 = var2;
            if (var6.lastModified() > var2) {
               var4 = var6.lastModified();
               this.curSlotSize = var6.length();
               this.curSlot = var1;
            }
         }

         ++var1;
      }

      var8 = new StringBuilder();
      var8.append(this.logFolderPath);
      var8.append("/");
      var8.append(this.name);
      var8.append("_");
      var8.append(this.curSlot);
      var8.append(".log");
      this.fout = new FileOutputStream(new File(var8.toString()), true);
      if (this.curSlotSize == 0L && this.header != null) {
         OutputStream var9 = this.fout;
         StringBuilder var7 = new StringBuilder();
         var7.append(this.header);
         var7.append("\r\n");
         var9.write(var7.toString().getBytes());
         this.fout.flush();
      }

      this.pin = new PipedInputStream(10240);
      this.pout = new PipedOutputStream(this.pin);
      this.psout = new PrintStream(this.pout, true);
      Thread var10 = new Thread(this);
      var10.setDaemon(true);
      var10.start();
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
                  this.fout.close();
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

   public void message(String var1) {
      this.log(var1);
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }
}
