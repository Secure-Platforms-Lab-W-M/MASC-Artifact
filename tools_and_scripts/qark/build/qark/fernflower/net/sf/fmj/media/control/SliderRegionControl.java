package net.sf.fmj.media.control;

public interface SliderRegionControl extends AtomicControl {
   long getMaxValue();

   long getMinValue();

   boolean isEnable();

   void setEnable(boolean var1);

   long setMaxValue(long var1);

   long setMinValue(long var1);
}
