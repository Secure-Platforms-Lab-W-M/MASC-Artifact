package javax.media.control;

import javax.media.Control;

public interface BitRateControl extends Control {
   int getBitRate();

   int getMaxSupportedBitRate();

   int getMinSupportedBitRate();

   int setBitRate(int var1);
}
