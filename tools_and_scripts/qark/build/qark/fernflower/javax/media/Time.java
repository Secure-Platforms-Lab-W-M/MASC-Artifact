package javax.media;

import java.io.Serializable;

public class Time implements Serializable {
   private static final double NANO_TO_SEC = 1.0E-9D;
   public static final long ONE_SECOND = 1000000000L;
   public static final Time TIME_UNKNOWN = new Time(9223372036854775806L);
   protected long nanoseconds;

   public Time(double var1) {
      this.nanoseconds = this.secondsToNanoseconds(var1);
   }

   public Time(long var1) {
      this.nanoseconds = var1;
   }

   public long getNanoseconds() {
      return this.nanoseconds;
   }

   public double getSeconds() {
      return (double)this.nanoseconds * 1.0E-9D;
   }

   protected long secondsToNanoseconds(double var1) {
      return (long)(1.0E9D * var1);
   }
}
