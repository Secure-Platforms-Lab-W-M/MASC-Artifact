package javax.media;

public interface Controller extends Clock, Duration {
   Time LATENCY_UNKNOWN = new Time(Long.MAX_VALUE);
   int Prefetched = 500;
   int Prefetching = 400;
   int Realized = 300;
   int Realizing = 200;
   int Started = 600;
   int Unrealized = 100;

   void addControllerListener(ControllerListener var1);

   void close();

   void deallocate();

   Control getControl(String var1);

   Control[] getControls();

   Time getStartLatency();

   int getState();

   int getTargetState();

   void prefetch();

   void realize();

   void removeControllerListener(ControllerListener var1);
}
