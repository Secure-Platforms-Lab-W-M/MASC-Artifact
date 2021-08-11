package com.sun.media.format;

import java.util.Hashtable;
import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.utility.FormatUtils;

public class WavAudioFormat extends AudioFormat {
   public static final int WAVE_FORMAT_ADPCM = 2;
   public static final int WAVE_FORMAT_ALAW = 6;
   public static final int WAVE_FORMAT_DIGIFIX = 22;
   public static final int WAVE_FORMAT_DIGISTD = 21;
   public static final int WAVE_FORMAT_DSPGROUP_TRUESPEECH = 34;
   public static final int WAVE_FORMAT_DVI_ADPCM = 17;
   public static final int WAVE_FORMAT_GSM610 = 49;
   public static final int WAVE_FORMAT_MPEG_LAYER3 = 85;
   public static final int WAVE_FORMAT_MSG723 = 66;
   public static final int WAVE_FORMAT_MSNAUDIO = 50;
   public static final int WAVE_FORMAT_MSRT24 = 130;
   public static final int WAVE_FORMAT_MULAW = 7;
   public static final int WAVE_FORMAT_OKI_ADPCM = 16;
   public static final int WAVE_FORMAT_PCM = 1;
   public static final int WAVE_FORMAT_SX7383 = 7175;
   public static final int WAVE_FORMAT_VOXWARE_AC10 = 113;
   public static final int WAVE_FORMAT_VOXWARE_AC16 = 114;
   public static final int WAVE_FORMAT_VOXWARE_AC20 = 115;
   public static final int WAVE_FORMAT_VOXWARE_AC8 = 112;
   public static final int WAVE_FORMAT_VOXWARE_METASOUND = 117;
   public static final int WAVE_FORMAT_VOXWARE_METAVOICE = 116;
   public static final int WAVE_FORMAT_VOXWARE_RT29H = 118;
   public static final int WAVE_FORMAT_VOXWARE_TQ40 = 121;
   public static final int WAVE_FORMAT_VOXWARE_TQ60 = 129;
   public static final int WAVE_FORMAT_VOXWARE_VR12 = 119;
   public static final int WAVE_FORMAT_VOXWARE_VR18 = 120;
   public static final int WAVE_IBM_FORMAT_ADPCM = 259;
   public static final int WAVE_IBM_FORMAT_ALAW = 258;
   public static final int WAVE_IBM_FORMAT_MULAW = 257;
   public static final Hashtable formatMapper = new Hashtable();
   public static final Hashtable reverseFormatMapper = new Hashtable();
   private int averageBytesPerSecond = -1;
   protected byte[] codecSpecificHeader;

   static {
      Hashtable var1 = formatMapper;
      Integer var0 = 1;
      var1.put(var0, "LINEAR");
      Hashtable var2 = formatMapper;
      Integer var13 = 2;
      var2.put(var13, "msadpcm");
      Hashtable var3 = formatMapper;
      Integer var14 = 6;
      var3.put(var14, "alaw");
      Hashtable var4 = formatMapper;
      Integer var15 = 7;
      var4.put(var15, "ULAW");
      Hashtable var5 = formatMapper;
      Integer var16 = 17;
      var5.put(var16, "ima4/ms");
      Hashtable var6 = formatMapper;
      Integer var17 = 34;
      var6.put(var17, "truespeech");
      Hashtable var7 = formatMapper;
      Integer var18 = 49;
      var7.put(var18, "gsm/ms");
      Hashtable var8 = formatMapper;
      Integer var19 = 50;
      var8.put(var19, "msnaudio");
      Hashtable var9 = formatMapper;
      Integer var20 = 85;
      var9.put(var20, "mpeglayer3");
      Hashtable var10 = formatMapper;
      Integer var21 = 112;
      var10.put(var21, "voxwareac8");
      Hashtable var11 = formatMapper;
      Integer var22 = 113;
      var11.put(var22, "voxwareac10");
      var11 = formatMapper;
      Integer var12 = 114;
      var11.put(var12, "voxwareac16");
      formatMapper.put(115, "voxwareac20");
      formatMapper.put(116, "voxwaremetavoice");
      formatMapper.put(117, "voxwaremetasound");
      formatMapper.put(118, "voxwarert29h");
      formatMapper.put(119, "voxwarevr12");
      formatMapper.put(120, "voxwarevr18");
      formatMapper.put(121, "voxwaretq40");
      formatMapper.put(129, "voxwaretq60");
      formatMapper.put(130, "msrt24");
      reverseFormatMapper.put("alaw", var14);
      reverseFormatMapper.put("gsm/ms", var18);
      reverseFormatMapper.put("ima4/ms", var16);
      reverseFormatMapper.put("linear", var0);
      reverseFormatMapper.put("mpeglayer3", var20);
      reverseFormatMapper.put("msadpcm", var13);
      reverseFormatMapper.put("msnaudio", var19);
      reverseFormatMapper.put("msrt24", 130);
      reverseFormatMapper.put("truespeech", var17);
      reverseFormatMapper.put("ulaw", var15);
      reverseFormatMapper.put("voxwareac10", var22);
      reverseFormatMapper.put("voxwareac16", var12);
      reverseFormatMapper.put("voxwareac20", 115);
      reverseFormatMapper.put("voxwareac8", var21);
      reverseFormatMapper.put("voxwaremetasound", 117);
      reverseFormatMapper.put("voxwaremetavoice", 116);
      reverseFormatMapper.put("voxwarert29h", 118);
      reverseFormatMapper.put("voxwaretq40", 121);
      reverseFormatMapper.put("voxwaretq60", 129);
      reverseFormatMapper.put("voxwarevr12", 119);
      reverseFormatMapper.put("voxwarevr18", 120);
   }

   public WavAudioFormat(String var1) {
      super(var1);
   }

   public WavAudioFormat(String var1, double var2, int var4, int var5, int var6, int var7, int var8, int var9, float var10, Class var11, byte[] var12) {
      super(var1, var2, var4, var5, var8, var9, var6, (double)var7, var11);
      this.averageBytesPerSecond = var7;
      this.codecSpecificHeader = var12;
   }

   public WavAudioFormat(String var1, double var2, int var4, int var5, int var6, int var7, byte[] var8) {
      super(var1, var2, var4, var5, -1, -1, var6, (double)var7, byteArray);
      this.averageBytesPerSecond = var7;
      this.codecSpecificHeader = var8;
   }

   public Object clone() {
      return new WavAudioFormat(this.encoding, this.sampleRate, this.sampleSizeInBits, this.channels, this.frameSizeInBits, this.averageBytesPerSecond, this.endian, this.signed, (float)this.frameRate, this.dataType, this.codecSpecificHeader);
   }

   protected void copy(Format var1) {
      super.copy(var1);
      WavAudioFormat var2 = (WavAudioFormat)var1;
      this.averageBytesPerSecond = var2.averageBytesPerSecond;
      this.codecSpecificHeader = var2.codecSpecificHeader;
   }

   public boolean equals(Object var1) {
      boolean var2 = super.equals(var1);
      boolean var3 = false;
      if (!var2) {
         return false;
      } else if (!(var1 instanceof WavAudioFormat)) {
         return false;
      } else {
         WavAudioFormat var4 = (WavAudioFormat)var1;
         var2 = var3;
         if (this.averageBytesPerSecond == var4.averageBytesPerSecond) {
            var2 = var3;
            if (this.codecSpecificHeader == var4.codecSpecificHeader) {
               var2 = true;
            }
         }

         return var2;
      }
   }

   public int getAverageBytesPerSecond() {
      return this.averageBytesPerSecond;
   }

   public byte[] getCodecSpecificHeader() {
      return this.codecSpecificHeader;
   }

   public Format intersects(Format var1) {
      Format var2 = super.intersects(var1);
      if (var1 instanceof WavAudioFormat) {
         WavAudioFormat var3 = (WavAudioFormat)var2;
         WavAudioFormat var4 = (WavAudioFormat)var1;
         if (this.getClass().isAssignableFrom(var1.getClass())) {
            if (FormatUtils.specified(this.averageBytesPerSecond)) {
               var3.averageBytesPerSecond = this.averageBytesPerSecond;
            }

            if (FormatUtils.specified(this.codecSpecificHeader)) {
               var3.codecSpecificHeader = this.codecSpecificHeader;
               return var2;
            }
         } else if (var1.getClass().isAssignableFrom(this.getClass())) {
            if (!FormatUtils.specified(var3.averageBytesPerSecond)) {
               var3.averageBytesPerSecond = var4.averageBytesPerSecond;
            }

            if (!FormatUtils.specified(var3.codecSpecificHeader)) {
               var3.codecSpecificHeader = var4.codecSpecificHeader;
            }
         }
      }

      return var2;
   }

   public boolean matches(Format var1) {
      if (!super.matches(var1)) {
         return false;
      } else if (!(var1 instanceof WavAudioFormat)) {
         return true;
      } else {
         WavAudioFormat var2 = (WavAudioFormat)var1;
         return FormatUtils.matches(this.averageBytesPerSecond, var2.averageBytesPerSecond) && FormatUtils.matches(this.codecSpecificHeader, var2.codecSpecificHeader);
      }
   }
}
