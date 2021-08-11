package javax.media.control;

import javax.media.Control;

public interface MpegAudioControl extends Control {
   int FIVE_CHANNELS_3_0_2_0 = 128;
   int FIVE_CHANNELS_3_2 = 256;
   int FOUR_CHANNELS_2_0_2_0 = 16;
   int FOUR_CHANNELS_2_2 = 32;
   int FOUR_CHANNELS_3_1 = 64;
   int LAYER_1 = 1;
   int LAYER_2 = 2;
   int LAYER_3 = 4;
   int SAMPLING_RATE_16 = 1;
   int SAMPLING_RATE_22_05 = 2;
   int SAMPLING_RATE_24 = 4;
   int SAMPLING_RATE_32 = 8;
   int SAMPLING_RATE_44_1 = 16;
   int SAMPLING_RATE_48 = 32;
   int SINGLE_CHANNEL = 1;
   int THREE_CHANNELS_2_1 = 4;
   int THREE_CHANNELS_3_0 = 8;
   int TWO_CHANNELS_DUAL = 4;
   int TWO_CHANNELS_STEREO = 2;

   int getAudioLayer();

   int getChannelLayout();

   boolean getLowFrequencyChannel();

   boolean getMultilingualMode();

   int getSupportedAudioLayers();

   int getSupportedChannelLayouts();

   int getSupportedSamplingRates();

   boolean isLowFrequencyChannelSupported();

   boolean isMultilingualModeSupported();

   int setAudioLayer(int var1);

   int setChannelLayout(int var1);

   boolean setLowFrequencyChannel(boolean var1);

   boolean setMultilingualMode(boolean var1);
}
