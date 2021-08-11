package net.sf.fmj.media;

import javax.media.format.AudioFormat;

public class AudioFormatCompleter {
   public static AudioFormat complete(AudioFormat var0) {
      return var0.getSampleSizeInBits() > 8 && var0.getEndian() == -1 ? new AudioFormat(var0.getEncoding(), var0.getSampleRate(), var0.getSampleSizeInBits(), var0.getChannels(), 1, var0.getSigned(), var0.getFrameSizeInBits(), var0.getFrameRate(), var0.getDataType()) : var0;
   }
}
