package javax.media.protocol;

import java.util.HashMap;

public class FileTypeDescriptor extends ContentDescriptor {
   public static final String AIFF = "audio.x_aiff";
   public static final String BASIC_AUDIO = "audio.basic";
   public static final String GSM = "audio.x_gsm";
   public static final String MIDI = "audio.midi";
   public static final String MPEG = "video.mpeg";
   public static final String MPEG_AUDIO = "audio.mpeg";
   public static final String MSVIDEO = "video.x_msvideo";
   public static final String QUICKTIME = "video.quicktime";
   public static final String RMF = "audio.rmf";
   public static final String VIVO = "video.vivo";
   public static final String WAVE = "audio.x_wav";

   public FileTypeDescriptor(String var1) {
      super(var1);
   }

   public String toString() {
      HashMap var1 = new HashMap();
      var1.put("video.quicktime", "QuickTime");
      var1.put("video.x_msvideo", "AVI");
      var1.put("video.mpeg", "MPEG Video");
      var1.put("video.vivo", "Vivo");
      var1.put("audio.basic", "Basic Audio (au)");
      var1.put("audio.x_wav", "WAV");
      var1.put("audio.x_aiff", "AIFF");
      var1.put("audio.midi", "MIDI");
      var1.put("audio.rmf", "RMF");
      var1.put("audio.x_gsm", "GSM");
      var1.put("audio.mpeg", "MPEG Audio");
      String var2 = (String)var1.get(this.getContentType());
      return var2 == null ? super.toString() : var2;
   }
}
