package net.sf.fmj.media.renderer.audio.device;

import javax.media.format.AudioFormat;

public interface AudioOutput {
   int bufferAvailable();

   void dispose();

   void drain();

   void flush();

   double getGain();

   long getMediaNanoseconds();

   boolean getMute();

   float getRate();

   boolean initialize(AudioFormat var1, int var2);

   void pause();

   void resume();

   void setGain(double var1);

   void setMute(boolean var1);

   float setRate(float var1);

   int write(byte[] var1, int var2, int var3);
}
