package javax.media.control;

import javax.media.Control;

public interface BufferControl extends Control {
   long DEFAULT_VALUE = -1L;
   long MAX_VALUE = -2L;

   long getBufferLength();

   boolean getEnabledThreshold();

   long getMinimumThreshold();

   long setBufferLength(long var1);

   void setEnabledThreshold(boolean var1);

   long setMinimumThreshold(long var1);
}
