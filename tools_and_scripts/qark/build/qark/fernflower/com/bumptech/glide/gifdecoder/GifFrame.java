package com.bumptech.glide.gifdecoder;

class GifFrame {
   static final int DISPOSAL_BACKGROUND = 2;
   static final int DISPOSAL_NONE = 1;
   static final int DISPOSAL_PREVIOUS = 3;
   static final int DISPOSAL_UNSPECIFIED = 0;
   int bufferFrameStart;
   int delay;
   int dispose;
   // $FF: renamed from: ih int
   int field_140;
   boolean interlace;
   // $FF: renamed from: iw int
   int field_141;
   // $FF: renamed from: ix int
   int field_142;
   // $FF: renamed from: iy int
   int field_143;
   int[] lct;
   int transIndex;
   boolean transparency;
}
