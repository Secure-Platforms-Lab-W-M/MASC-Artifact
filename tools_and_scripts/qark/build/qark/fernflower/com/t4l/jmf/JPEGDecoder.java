package com.t4l.jmf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.JPEGFormat;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.javax.imageio.ImageIO;
import org.atalk.android.util.javax.imageio.ImageReadParam;
import org.atalk.android.util.javax.imageio.ImageReader;
import org.atalk.android.util.javax.imageio.stream.ImageInputStream;

public class JPEGDecoder implements Codec {
   static Hashtable imageTable;
   private static final JPEGFormat jpegFormat;
   private static final Logger logger;
   private static final RGBFormat rgbFormat;

   static {
      logger = LoggerSingleton.logger;
      jpegFormat = new JPEGFormat();
      rgbFormat = new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, -1, -1, -1, -1);
      imageTable = new Hashtable();
   }

   protected static void readJPEG(byte[] var0, BufferedImage var1) throws IOException {
      ImageInputStream var2 = ImageIO.createImageInputStream(new ByteArrayInputStream(var0));
      ImageReader var3 = (ImageReader)ImageIO.getImageReaders(var2).next();
      if (var3 != null) {
         var3.setInput(var2, false);
         ImageReadParam var4 = var3.getDefaultReadParam();
         var4.setDestination(var1);
         var3.read(0, var4);
      } else {
         throw new UnsupportedOperationException("This image is unsupported.");
      }
   }

   public void close() {
      // $FF: Couldn't be decompiled
   }

   public Object getControl(String var1) {
      return null;
   }

   public Object[] getControls() {
      return new String[0];
   }

   public String getName() {
      return "JPEG Decoder";
   }

   public Format[] getSupportedInputFormats() {
      return new VideoFormat[]{jpegFormat};
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return new VideoFormat[]{rgbFormat};
      } else if (var1.relax().matches(jpegFormat)) {
         VideoFormat var2 = (VideoFormat)var1;
         return new VideoFormat[]{new RGBFormat(var2.getSize(), -1, Format.intArray, var2.getFrameRate(), 32, 16711680, 65280, 255)};
      } else {
         return new Format[0];
      }
   }

   public void open() throws ResourceUnavailableException {
   }

   public int process(Buffer var1, Buffer var2) {
      Format var3 = var1.getFormat();
      Format var4 = var2.getFormat();
      return !var3.relax().matches(jpegFormat) || var4 != null && !var4.relax().matches(rgbFormat) ? 1 : this.processJPEGtoRGB(var1, var2);
   }

   protected int processJPEGtoRGB(Buffer var1, Buffer var2) {
      Hashtable var7 = imageTable;
      synchronized(var7){}

      Throwable var10000;
      boolean var10001;
      Throwable var100;
      label751: {
         label755: {
            label757: {
               VideoFormat var5;
               try {
                  var5 = (VideoFormat)var1.getFormat();
                  if ((RGBFormat)var2.getFormat() == null) {
                     int var3 = var5.getSize().width;
                     int var4 = var5.getSize().height;
                     var2.setFormat(new RGBFormat(new Dimension(var3, var4), var3 * var4, Format.intArray, var5.getFrameRate(), 32, 16711680, 65280, 255, 1, var3, 0, 1));
                  }
               } catch (Throwable var99) {
                  var10000 = var99;
                  var10001 = false;
                  break label757;
               }

               BufferedImage var6;
               byte[] var8;
               Dimension var9;
               try {
                  var8 = (byte[])((byte[])var1.getData());
                  var9 = var5.getSize();
                  var6 = (BufferedImage)imageTable.get(var9);
               } catch (Throwable var98) {
                  var10000 = var98;
                  var10001 = false;
                  break label757;
               }

               BufferedImage var102 = var6;
               if (var6 == null) {
                  try {
                     var102 = new BufferedImage(var9.width, var9.height, 1);
                  } catch (Throwable var97) {
                     var10000 = var97;
                     var10001 = false;
                     break label757;
                  }
               }

               int[] var105;
               label737: {
                  try {
                     readJPEG(var8, var102);
                     imageTable.put(var9, var102);
                     Object var104 = var2.getData();
                     if (var104 instanceof int[]) {
                        var105 = (int[])((int[])var104);
                        break label737;
                     }
                  } catch (Throwable var96) {
                     var10000 = var96;
                     var10001 = false;
                     break label757;
                  }

                  try {
                     var105 = new int[var102.getWidth() * var102.getHeight()];
                     var2.setData(var105);
                  } catch (Throwable var95) {
                     var10000 = var95;
                     var10001 = false;
                     break label757;
                  }
               }

               label728:
               try {
                  RGBConverter.populateArray(var102, var105, (RGBFormat)var2.getFormat());
                  var2.setDiscard(var1.isDiscard());
                  var2.setDuration(var1.getDuration());
                  var2.setEOM(var1.isEOM());
                  var2.setFlags(var1.getFlags());
                  var2.setHeader((Object)null);
                  var2.setTimeStamp(var1.getTimeStamp());
                  var2.setSequenceNumber(var1.getSequenceNumber());
                  var2.setOffset(0);
                  var2.setLength(var102.getWidth() * var102.getHeight());
                  break label755;
               } catch (Throwable var94) {
                  var10000 = var94;
                  var10001 = false;
                  break label728;
               }
            }

            var100 = var10000;

            try {
               Logger var101 = logger;
               Level var103 = Level.WARNING;
               StringBuilder var106 = new StringBuilder();
               var106.append("");
               var106.append(var100);
               var101.log(var103, var106.toString(), var100);
               return 1;
            } catch (Throwable var92) {
               var10000 = var92;
               var10001 = false;
               break label751;
            }
         }

         label724:
         try {
            return 0;
         } catch (Throwable var93) {
            var10000 = var93;
            var10001 = false;
            break label724;
         }
      }

      while(true) {
         var100 = var10000;

         try {
            throw var100;
         } catch (Throwable var91) {
            var10000 = var91;
            var10001 = false;
            continue;
         }
      }
   }

   public void reset() {
   }

   public Format setInputFormat(Format var1) {
      return var1.relax().matches(jpegFormat) ? var1 : null;
   }

   public Format setOutputFormat(Format var1) {
      return var1.relax().matches(rgbFormat) ? var1 : null;
   }
}
