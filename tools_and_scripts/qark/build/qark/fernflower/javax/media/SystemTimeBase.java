package javax.media;

public final class SystemTimeBase implements TimeBase {
   static long offset = System.currentTimeMillis() * 1000000L;

   public long getNanoseconds() {
      return System.currentTimeMillis() * 1000000L - offset;
   }

   public Time getTime() {
      return new Time(this.getNanoseconds());
   }
}
