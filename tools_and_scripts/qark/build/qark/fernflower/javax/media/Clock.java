package javax.media;

public interface Clock {
   Time RESET = new Time(Long.MAX_VALUE);

   long getMediaNanoseconds();

   Time getMediaTime();

   float getRate();

   Time getStopTime();

   Time getSyncTime();

   TimeBase getTimeBase();

   Time mapToTimeBase(Time var1) throws ClockStoppedException;

   void setMediaTime(Time var1);

   float setRate(float var1);

   void setStopTime(Time var1);

   void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException;

   void stop();

   void syncStart(Time var1);
}
