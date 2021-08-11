package javax.media.protocol;

import javax.media.Time;

public interface Positionable {
   int RoundDown = 2;
   int RoundNearest = 3;
   int RoundUp = 1;

   boolean isRandomAccess();

   Time setPosition(Time var1, int var2);
}
