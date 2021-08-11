package net.sf.fmj.media.renderer.audio;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import javax.media.format.AudioFormat;
import org.atalk.android.util.javax.sound.sampled.AudioFormat.Encoding;

public class JavaSoundUtils {
   public static boolean onlyStandardFormats = false;

   public static AudioFormat convertFormat(org.atalk.android.util.javax.sound.sampled.AudioFormat var0) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public static org.atalk.android.util.javax.sound.sampled.AudioFormat convertFormat(AudioFormat var0) {
      String var11 = var0.getEncoding();
      int var6 = var0.getChannels();
      double var3 = var0.getFrameRate();
      int var7 = var0.getFrameSizeInBits() / 8;
      double var1 = var0.getSampleRate();
      int var8 = var0.getSampleSizeInBits();
      boolean var10;
      if (var0.getEndian() == 1) {
         var10 = true;
      } else {
         var10 = false;
      }

      int var9 = var0.getSigned();
      Object var19;
      if ("LINEAR".equals(var11)) {
         if (var9 != 0) {
            if (var9 != 1) {
               throw new IllegalArgumentException("Signed/Unsigned must be specified");
            }

            var19 = Encoding.PCM_SIGNED;
         } else {
            var19 = Encoding.PCM_UNSIGNED;
         }
      } else if ("alaw".equals(var11)) {
         var19 = Encoding.ALAW;
      } else if ("ULAW".equals(var11)) {
         var19 = Encoding.ULAW;
      } else if (toMpegEncoding(var11) != null) {
         var19 = toMpegEncoding(var11);
      } else if (toVorbisEncoding(var11) != null) {
         var19 = toVorbisEncoding(var11);
      } else {
         var19 = new CustomEncoding(var11);
      }

      Class var21 = null;
      Class var12;
      if (!onlyStandardFormats) {
         label99: {
            Class var13;
            label98: {
               label104: {
                  boolean var10001;
                  try {
                     var12 = Class.forName("javazoom.spi.mpeg.sampled.file.MpegEncoding");
                  } catch (Exception var18) {
                     var10001 = false;
                     break label104;
                  }

                  var21 = var12;

                  try {
                     var13 = Class.forName("javazoom.spi.vorbis.sampled.file.VorbisEncoding");
                     break label98;
                  } catch (Exception var17) {
                     var10001 = false;
                  }
               }

               var12 = var21;
               var21 = null;
               break label99;
            }

            var21 = var13;
         }
      } else {
         var12 = null;
         var21 = null;
      }

      if (var19 == Encoding.PCM_SIGNED) {
         return new org.atalk.android.util.javax.sound.sampled.AudioFormat((float)var1, var8, var6, true, var10);
      } else if (var19 == Encoding.PCM_UNSIGNED) {
         return new org.atalk.android.util.javax.sound.sampled.AudioFormat((float)var1, var8, var6, false, var10);
      } else {
         org.atalk.android.util.javax.sound.sampled.AudioFormat var20;
         if (var12 != null && var12.isInstance(var19)) {
            label105: {
               Constructor var22;
               try {
                  var22 = Class.forName("javazoom.spi.mpeg.sampled.file.MpegAudioFormat").getConstructor(Encoding.class, Float.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Float.TYPE, Boolean.TYPE, Map.class);
               } catch (Exception var16) {
                  break label105;
               }

               float var5 = (float)var1;

               try {
                  var20 = (org.atalk.android.util.javax.sound.sampled.AudioFormat)var22.newInstance(var19, var5, var8, var6, var7, (float)var3, var10, new HashMap());
                  return var20;
               } catch (Exception var15) {
               }
            }

            var20 = null;
            return var20;
         } else if (var21 != null && var21.isInstance(var19)) {
            try {
               var20 = (org.atalk.android.util.javax.sound.sampled.AudioFormat)Class.forName("javazoom.spi.vorbis.sampled.file.VorbisAudioFormat").getConstructor(Encoding.class, Float.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Float.TYPE, Boolean.TYPE, Map.class).newInstance(var19, (float)var1, var8, var6, var7, (float)var3, var10, new HashMap());
            } catch (Exception var14) {
               var20 = null;
            }

            return var20;
         } else {
            return new org.atalk.android.util.javax.sound.sampled.AudioFormat((Encoding)var19, (float)var1, var8, var6, var7, (float)var3, var10);
         }
      }
   }

   private static Encoding toMpegEncoding(String param0) {
      // $FF: Couldn't be decompiled
   }

   private static Encoding toVorbisEncoding(String param0) {
      // $FF: Couldn't be decompiled
   }
}
