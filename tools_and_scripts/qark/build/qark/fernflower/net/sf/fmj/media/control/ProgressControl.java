package net.sf.fmj.media.control;

public interface ProgressControl extends GroupControl {
   StringControl getAudioCodec();

   StringControl getAudioProperties();

   StringControl getBitRate();

   StringControl getFrameRate();

   StringControl getVideoCodec();

   StringControl getVideoProperties();
}
