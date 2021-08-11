package javax.media.control;

import javax.media.Control;

public interface PortControl extends Control {
   int COMPACT_DISC = 32;
   int COMPOSITE_VIDEO = 128;
   int COMPOSITE_VIDEO_2 = 512;
   int HEADPHONE = 8;
   int LINE_IN = 2;
   int LINE_OUT = 16;
   int MICROPHONE = 1;
   int SPEAKER = 4;
   int SVIDEO = 64;
   int TV_TUNER = 256;

   int getPorts();

   int getSupportedPorts();

   int setPorts(int var1);
}
