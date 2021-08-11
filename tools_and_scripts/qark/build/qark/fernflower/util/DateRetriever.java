package util;

import java.util.Calendar;

public class DateRetriever implements Runnable {
   private static int PRECISION_MILLIS = 1000;
   private static DateRetriever RETRIEVER_INSTANCE = new DateRetriever();
   private Thread _thread = null;
   private String current;
   private boolean picked = false;

   private String dateStr(Calendar var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.int2Str(var1.get(2) + 1));
      var2.append("/");
      var2.append(this.int2Str(var1.get(5)));
      var2.append("/");
      var2.append(var1.get(1));
      var2.append(" ");
      var2.append(this.int2Str(var1.get(11)));
      var2.append(":");
      var2.append(this.int2Str(var1.get(12)));
      var2.append(":");
      var2.append(this.int2Str(var1.get(13)));
      return var2.toString();
   }

   public static String getDateString() {
      return RETRIEVER_INSTANCE.retrieveDateString();
   }

   private String int2Str(int var1) {
      StringBuilder var2;
      if (var1 < 10) {
         var2 = new StringBuilder();
         var2.append("0");
         var2.append(var1);
         return var2.toString();
      } else {
         var2 = new StringBuilder();
         var2.append("");
         var2.append(var1);
         return var2.toString();
      }
   }

   private String retrieveDateString() {
      synchronized(this){}

      String var1;
      try {
         this.picked = true;
         if (this._thread != null) {
            var1 = this.current;
            return var1;
         }

         this.current = this.dateStr(Calendar.getInstance());
         this._thread = new Thread(this);
         this._thread.setDaemon(true);
         this._thread.start();
         var1 = this.current;
      } finally {
         ;
      }

      return var1;
   }

   private void waitMillis(long var1) {
      try {
         this.wait(var1);
      } catch (InterruptedException var4) {
         Logger.getLogger().logException(var4);
      }
   }

   public void run() {
      synchronized(this){}

      Throwable var10000;
      label132: {
         boolean var10001;
         try {
            this._thread = Thread.currentThread();
            this.waitMillis((long)PRECISION_MILLIS);
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label132;
         }

         while(true) {
            try {
               if (this.picked) {
                  this.current = this.dateStr(Calendar.getInstance());
                  this.picked = false;
                  this.waitMillis((long)PRECISION_MILLIS);
                  continue;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break;
            }

            try {
               this._thread = null;
               return;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var1 = var10000;
      throw var1;
   }
}
