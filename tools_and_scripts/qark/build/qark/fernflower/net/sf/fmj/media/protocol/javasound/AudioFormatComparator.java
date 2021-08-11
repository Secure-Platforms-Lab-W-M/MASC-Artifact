package net.sf.fmj.media.protocol.javasound;

import java.util.Comparator;
import javax.media.format.AudioFormat;

public class AudioFormatComparator implements Comparator {
   public int compare(AudioFormat var1, AudioFormat var2) {
      if (var1 == null && var2 == null) {
         return 0;
      } else if (var1 == null) {
         return -1;
      } else if (var2 == null) {
         return 1;
      } else if (var1.getSampleRate() > var2.getSampleRate()) {
         return 1;
      } else if (var1.getSampleRate() < var2.getSampleRate()) {
         return -1;
      } else if (var1.getChannels() > var2.getChannels()) {
         return 1;
      } else if (var1.getChannels() < var2.getChannels()) {
         return -1;
      } else if (var1.getSampleSizeInBits() > var2.getSampleSizeInBits()) {
         return 1;
      } else {
         return var1.getSampleSizeInBits() < var2.getSampleSizeInBits() ? -1 : 0;
      }
   }
}
