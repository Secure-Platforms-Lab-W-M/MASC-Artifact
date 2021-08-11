package util;

public class TimeoutTime {
   private long timeout = Long.MAX_VALUE;
   private TimoutNotificator toHandler;

   public TimeoutTime(TimoutNotificator var1) {
      this.toHandler = var1;
   }

   public long getTimeout() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.timeout;
      } finally {
         ;
      }

      return var1;
   }

   public void setTimeout(long var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 <= 0L) {
         label54:
         try {
            this.timeout = Long.MAX_VALUE;
            return;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label54;
         }
      } else {
         label56:
         try {
            this.timeout = this.toHandler.getCurrentTime() + var1;
            return;
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label56;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }
}
