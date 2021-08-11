package javax.media;

public interface Duration {
   Time DURATION_UNBOUNDED = new Time(Long.MAX_VALUE);
   Time DURATION_UNKNOWN = new Time(9223372036854775806L);

   Time getDuration();
}
