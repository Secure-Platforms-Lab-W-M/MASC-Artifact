package net.sf.fmj.media.codec;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.AbstractCodec;
import net.sf.fmj.media.BufferQueueInputStream;
import net.sf.fmj.media.renderer.audio.JavaSoundUtils;
import net.sf.fmj.utility.FormatUtils;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.javax.sound.sampled.AudioInputStream;
import org.atalk.android.util.javax.sound.sampled.AudioSystem;
import org.atalk.android.util.javax.sound.sampled.UnsupportedAudioFileException;
import org.atalk.android.util.javax.sound.sampled.AudioFormat.Encoding;

public class JavaSoundCodec extends AbstractCodec {
   private static final int BITS_PER_BYTE = 8;
   private static final int MAX_BYTE = 255;
   private static final int MAX_BYTE_PLUS1 = 256;
   private static final int MAX_SIGNED_BYTE = 127;
   private static final int SIZEOF_INT = 4;
   private static final int SIZEOF_LONG = 8;
   private static final int SIZEOF_SHORT = 2;
   private static final Logger logger;
   private volatile AudioInputStream audioInputStream;
   private volatile AudioInputStream audioInputStreamConverted;
   private JavaSoundCodec.AudioInputStreamThread audioInputStreamThread;
   private BufferQueueInputStream bufferQueueInputStream;
   private int totalIn;
   private int totalOut;
   private boolean trace;

   static {
      logger = LoggerSingleton.logger;
   }

   public JavaSoundCodec() {
      Vector var2 = new Vector();
      var2.add(new AudioFormat("ULAW"));
      var2.add(new AudioFormat("alaw"));
      var2.add(new AudioFormat("LINEAR"));
      if (!JavaSoundUtils.onlyStandardFormats) {
         label60: {
            boolean var10001;
            int var1;
            String[] var3;
            label57: {
               try {
                  Class.forName("javazoom.spi.mpeg.sampled.file.MpegEncoding");
                  var3 = new String[9];
               } catch (Exception var7) {
                  var10001 = false;
                  break label57;
               }

               var3[0] = "MPEG1L1";
               var3[1] = "MPEG1L2";
               var3[2] = "MPEG1L3";
               var3[3] = "MPEG2DOT5L1";
               var3[4] = "MPEG2DOT5L2";
               var3[5] = "MPEG2DOT5L3";
               var3[6] = "MPEG2L1";
               var3[7] = "MPEG2L2";
               var3[8] = "MPEG2L3";
               var1 = 0;

               while(true) {
                  try {
                     if (var1 >= var3.length) {
                        break;
                     }

                     var2.add(new AudioFormat(var3[var1]));
                  } catch (Exception var6) {
                     var10001 = false;
                     break;
                  }

                  ++var1;
               }
            }

            try {
               Class.forName("javazoom.spi.vorbis.sampled.file.VorbisEncoding");
               var3 = new String[1];
            } catch (Exception var5) {
               var10001 = false;
               break label60;
            }

            var3[0] = "VORBISENC";
            var1 = 0;

            while(true) {
               try {
                  if (var1 >= var3.length) {
                     break;
                  }

                  var2.add(new AudioFormat(var3[var1]));
               } catch (Exception var4) {
                  var10001 = false;
                  break;
               }

               ++var1;
            }
         }
      }

      this.inputFormats = new Format[var2.size()];
      var2.toArray(this.inputFormats);
   }

   public static byte[] createAuHeader(org.atalk.android.util.javax.sound.sampled.AudioFormat var0) {
      byte[] var3 = new byte[24];
      encodeIntBE(779316836, var3, 0);
      encodeIntBE(var3.length, var3, 4);
      encodeIntBE(-1, var3, 8);
      byte var2;
      if (var0.getEncoding() == Encoding.ALAW) {
         if (var0.getSampleSizeInBits() != 8) {
            return null;
         }

         var2 = 27;
      } else if (var0.getEncoding() == Encoding.ULAW) {
         if (var0.getSampleSizeInBits() != 8) {
            return null;
         }

         var2 = 1;
      } else {
         if (var0.getEncoding() != Encoding.PCM_SIGNED) {
            if (var0.getEncoding() == Encoding.PCM_UNSIGNED) {
               return null;
            }

            return null;
         }

         byte var1;
         if (var0.getSampleSizeInBits() == 8) {
            var1 = 2;
         } else if (var0.getSampleSizeInBits() == 16) {
            var1 = 3;
         } else if (var0.getSampleSizeInBits() == 24) {
            var1 = 4;
         } else {
            if (var0.getSampleSizeInBits() != 32) {
               return null;
            }

            var1 = 5;
         }

         var2 = var1;
         if (var0.getSampleSizeInBits() > 8) {
            var2 = var1;
            if (!var0.isBigEndian()) {
               return null;
            }
         }
      }

      encodeIntBE(var2, var3, 12);
      if (var0.getSampleRate() < 0.0F) {
         return null;
      } else {
         encodeIntBE((int)var0.getSampleRate(), var3, 16);
         if (var0.getChannels() < 0) {
            return null;
         } else {
            encodeIntBE(var0.getChannels(), var3, 20);
            return var3;
         }
      }
   }

   public static byte[] createWavHeader(org.atalk.android.util.javax.sound.sampled.AudioFormat var0) {
      if (var0.getEncoding() != Encoding.PCM_SIGNED && var0.getEncoding() != Encoding.PCM_UNSIGNED) {
         return null;
      } else if (var0.getSampleSizeInBits() == 8 && var0.getEncoding() != Encoding.PCM_UNSIGNED) {
         return null;
      } else if (var0.getSampleSizeInBits() == 16 && var0.getEncoding() != Encoding.PCM_SIGNED) {
         return null;
      } else {
         byte[] var1 = new byte[44];
         if (var0.getSampleSizeInBits() > 8 && var0.isBigEndian()) {
            encodeIntBE(1380533848, var1, 0);
         } else {
            encodeIntBE(1380533830, var1, 0);
         }

         encodeIntLE(var1.length + Integer.MAX_VALUE - 8, var1, 4);
         encodeIntBE(1463899717, var1, 8);
         encodeIntBE(1718449184, var1, 12);
         encodeIntLE(16, var1, 16);
         encodeShortLE((short)1, var1, 20);
         encodeShortLE((short)var0.getChannels(), var1, 22);
         encodeIntLE((int)var0.getSampleRate(), var1, 24);
         encodeIntLE((int)var0.getSampleRate() * var0.getChannels() * var0.getSampleSizeInBits() / 8, var1, 28);
         encodeShortLE((short)(var0.getChannels() * var0.getSampleSizeInBits() / 8), var1, 32);
         encodeShortLE((short)var0.getSampleSizeInBits(), var1, 34);
         encodeIntBE(1684108385, var1, 36);
         encodeIntLE(Integer.MAX_VALUE, var1, 40);
         return var1;
      }
   }

   private static void encodeIntBE(int var0, byte[] var1, int var2) {
      byte var4 = 0;
      int var3 = var0;

      for(var0 = var4; var0 < 4; ++var0) {
         int var5 = var3 & 255;
         int var6 = var5;
         if (var5 > 127) {
            var6 = var5 - 256;
         }

         var1[4 - var0 - 1 + var2] = (byte)var6;
         var3 >>= 8;
      }

   }

   private static void encodeIntLE(int var0, byte[] var1, int var2) {
      byte var4 = 0;
      int var3 = var0;

      for(var0 = var4; var0 < 4; ++var0) {
         int var5 = var3 & 255;
         int var6 = var5;
         if (var5 > 127) {
            var6 = var5 - 256;
         }

         var1[var2 + var0] = (byte)var6;
         var3 >>= 8;
      }

   }

   public static void encodeShortBE(short var0, byte[] var1, int var2) {
      byte var4 = 0;
      short var3 = var0;

      for(int var6 = var4; var6 < 2; ++var6) {
         int var5 = var3 & 255;
         int var7 = var5;
         if (var5 > 127) {
            var7 = var5 - 256;
         }

         var1[2 - var6 - 1 + var2] = (byte)var7;
         var3 = (short)(var3 >> 8);
      }

   }

   public static void encodeShortLE(short var0, byte[] var1, int var2) {
      byte var4 = 0;
      short var3 = var0;

      for(int var6 = var4; var6 < 2; ++var6) {
         int var5 = var3 & 255;
         int var7 = var5;
         if (var5 > 127) {
            var7 = var5 - 256;
         }

         var1[var2 + var6] = (byte)var7;
         var3 = (short)(var3 >> 8);
      }

   }

   private static byte[] fakeHeader(org.atalk.android.util.javax.sound.sampled.AudioFormat var0) {
      Object var4 = null;
      Class var1 = null;
      Class var2 = null;
      Class var3 = (Class)var4;
      if (!JavaSoundUtils.onlyStandardFormats) {
         label51: {
            var1 = var2;

            label45: {
               label52: {
                  boolean var10001;
                  try {
                     var2 = Class.forName("javazoom.spi.mpeg.sampled.file.MpegAudioFormat");
                  } catch (Exception var6) {
                     var10001 = false;
                     break label52;
                  }

                  var1 = var2;

                  try {
                     var3 = Class.forName("javazoom.spi.vorbis.sampled.file.VorbisAudioFormat");
                     break label45;
                  } catch (Exception var5) {
                     var10001 = false;
                  }
               }

               var3 = (Class)var4;
               break label51;
            }

            var1 = var2;
         }
      }

      if (var1 != null && var1.isInstance(var0)) {
         return new byte[0];
      } else if (var3 != null && var3.isInstance(var0)) {
         return new byte[0];
      } else {
         byte[] var8 = createAuHeader(var0);
         if (var8 != null) {
            return var8;
         } else {
            byte[] var7 = createWavHeader(var0);
            return var7 != null ? var7 : null;
         }
      }
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return new Format[]{new AudioFormat("LINEAR")};
      } else {
         org.atalk.android.util.javax.sound.sampled.AudioFormat var9 = JavaSoundUtils.convertFormat((AudioFormat)var1);
         org.atalk.android.util.javax.sound.sampled.AudioFormat[] var7 = AudioSystem.getTargetFormats(Encoding.PCM_UNSIGNED, var9);
         org.atalk.android.util.javax.sound.sampled.AudioFormat[] var8 = AudioSystem.getTargetFormats(Encoding.PCM_SIGNED, var9);
         StringBuilder var6 = null;
         Class var3 = null;
         Class var4 = null;
         Class var5 = var6;
         if (!JavaSoundUtils.onlyStandardFormats) {
            label91: {
               var3 = var4;

               label84: {
                  label92: {
                     boolean var10001;
                     try {
                        var4 = Class.forName("javazoom.spi.mpeg.sampled.file.MpegAudioFormat");
                     } catch (Exception var11) {
                        var10001 = false;
                        break label92;
                     }

                     var3 = var4;

                     try {
                        var5 = Class.forName("javazoom.spi.vorbis.sampled.file.VorbisAudioFormat");
                        break label84;
                     } catch (Exception var10) {
                        var10001 = false;
                     }
                  }

                  var5 = var6;
                  break label91;
               }

               var3 = var4;
            }
         }

         org.atalk.android.util.javax.sound.sampled.AudioFormat[] var12;
         if (var3 != null && var3.isInstance(var9)) {
            var12 = new org.atalk.android.util.javax.sound.sampled.AudioFormat[]{new org.atalk.android.util.javax.sound.sampled.AudioFormat(Encoding.PCM_SIGNED, var9.getSampleRate(), 16, var9.getChannels(), var9.getChannels() * 2, var9.getSampleRate(), false)};
         } else if (var5 != null && var5.isInstance(var9)) {
            var12 = new org.atalk.android.util.javax.sound.sampled.AudioFormat[]{new org.atalk.android.util.javax.sound.sampled.AudioFormat(Encoding.PCM_SIGNED, var9.getSampleRate(), 16, var9.getChannels(), var9.getChannels() * 2, var9.getSampleRate(), false)};
         } else {
            var12 = new org.atalk.android.util.javax.sound.sampled.AudioFormat[0];
         }

         Format[] var13 = new Format[var7.length + var8.length + var12.length];

         int var2;
         Logger var15;
         for(var2 = 0; var2 < var7.length; ++var2) {
            var13[var2] = JavaSoundUtils.convertFormat(var7[var2]);
            var15 = logger;
            var6 = new StringBuilder();
            var6.append("getSupportedOutputFormats: ");
            var6.append(var13[var2]);
            var15.finer(var6.toString());
         }

         for(var2 = 0; var2 < var8.length; ++var2) {
            var13[var7.length + var2] = JavaSoundUtils.convertFormat(var8[var2]);
            var15 = logger;
            var6 = new StringBuilder();
            var6.append("getSupportedOutputFormats: ");
            var6.append(var13[var7.length + var2]);
            var15.finer(var6.toString());
         }

         for(var2 = 0; var2 < var12.length; ++var2) {
            var13[var7.length + var8.length + var2] = JavaSoundUtils.convertFormat(var12[var2]);
            var15 = logger;
            var6 = new StringBuilder();
            var6.append("getSupportedOutputFormats: ");
            var6.append(var13[var7.length + var8.length + var2]);
            var15.finer(var6.toString());
         }

         for(var2 = 0; var2 < var13.length; ++var2) {
            AudioFormat var14 = (AudioFormat)var13[var2];
            if (FormatUtils.specified(((AudioFormat)var1).getSampleRate()) && !FormatUtils.specified(var14.getSampleRate())) {
               var13[var2] = null;
            }
         }

         return var13;
      }
   }

   public void open() throws ResourceUnavailableException {
      super.open();
      this.bufferQueueInputStream = new BufferQueueInputStream();
      org.atalk.android.util.javax.sound.sampled.AudioFormat var1 = JavaSoundUtils.convertFormat((AudioFormat)this.inputFormat);
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append("javaSoundAudioFormat converted (in)=");
      var3.append(var1);
      var2.fine(var3.toString());
      byte[] var4 = fakeHeader(var1);
      if (var4 != null) {
         if (var4.length > 0) {
            Buffer var7 = new Buffer();
            var7.setData(var4);
            var7.setLength(var4.length);
            this.bufferQueueInputStream.put(var7);
         }

         JavaSoundCodec.AudioInputStreamThread var6 = new JavaSoundCodec.AudioInputStreamThread(this.bufferQueueInputStream);
         this.audioInputStreamThread = var6;
         var6.start();
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Unable to reconstruct header for format: ");
         var5.append(this.inputFormat);
         throw new ResourceUnavailableException(var5.toString());
      }
   }

   public int process(Buffer param1, Buffer param2) {
      // $FF: Couldn't be decompiled
   }

   void setTrace(boolean var1) {
      this.trace = var1;
   }

   private class AudioInputStreamThread extends Thread {
      private final BufferQueueInputStream bufferQueueInputStream;

      public AudioInputStreamThread(BufferQueueInputStream var2) {
         this.bufferQueueInputStream = var2;
      }

      public void run() {
         Logger var2;
         Level var3;
         StringBuilder var4;
         try {
            JavaSoundCodec.this.audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(this.bufferQueueInputStream));
         } catch (UnsupportedAudioFileException var5) {
            var2 = JavaSoundCodec.logger;
            var3 = Level.WARNING;
            var4 = new StringBuilder();
            var4.append("");
            var4.append(var5);
            var2.log(var3, var4.toString(), var5);
            return;
         } catch (IOException var6) {
            var2 = JavaSoundCodec.logger;
            var3 = Level.WARNING;
            var4 = new StringBuilder();
            var4.append("");
            var4.append(var6);
            var2.log(var3, var4.toString(), var6);
            return;
         }

         org.atalk.android.util.javax.sound.sampled.AudioFormat var1 = JavaSoundUtils.convertFormat((AudioFormat)JavaSoundCodec.this.outputFormat);
         var2 = JavaSoundCodec.logger;
         StringBuilder var8 = new StringBuilder();
         var8.append("javaSoundAudioFormat converted (out)=");
         var8.append(var1);
         var2.fine(var8.toString());
         JavaSoundCodec var7 = JavaSoundCodec.this;
         var7.audioInputStreamConverted = AudioSystem.getAudioInputStream(var1, var7.audioInputStream);
      }
   }
}
