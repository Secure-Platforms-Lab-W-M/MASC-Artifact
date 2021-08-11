package javax.media.control;

import javax.media.Control;

public interface QualityControl extends Control {
   float getPreferredQuality();

   float getQuality();

   boolean isTemporalSpatialTradeoffSupported();

   float setQuality(float var1);
}
