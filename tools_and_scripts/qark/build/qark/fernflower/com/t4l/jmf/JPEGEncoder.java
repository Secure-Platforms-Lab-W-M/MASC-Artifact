package com.t4l.jmf;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
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
import org.atalk.android.util.javax.imageio.IIOImage;
import org.atalk.android.util.javax.imageio.ImageIO;
import org.atalk.android.util.javax.imageio.ImageWriteParam;
import org.atalk.android.util.javax.imageio.ImageWriter;
import org.atalk.android.util.javax.imageio.metadata.IIOMetadata;

public class JPEGEncoder implements Codec {
   static Hashtable imageTable;
   private static final VideoFormat jpegFormat;
   private static final Logger logger;
   private static final RGBFormat rgbFormat;

   static {
      logger = LoggerSingleton.logger;
      jpegFormat = new JPEGFormat();
      rgbFormat = new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, -1, -1, -1, -1);
      imageTable = new Hashtable();
   }

   protected static int writeJPEG(BufferedImage var0, byte[] var1) throws IOException {
      ImageWriter var2 = (ImageWriter)ImageIO.getImageWritersByMIMEType("image/jpeg").next();
      ImageWriteParam var3 = var2.getDefaultWriteParam();
      var3.setCompressionMode(2);
      var3.setCompressionQuality(0.8F);
      CustomByteArrayOutputStream var4 = new CustomByteArrayOutputStream(var1);
      var2.setOutput(var4);
      var2.write((IIOMetadata)null, new IIOImage(var0, (List)null, (IIOMetadata)null), var3);
      return var4.getBytesWritten();
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
      return "JPEG Encoder";
   }

   public Format[] getSupportedInputFormats() {
      return new VideoFormat[]{rgbFormat};
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return new VideoFormat[]{jpegFormat};
      } else if (var1.relax().matches(rgbFormat)) {
         VideoFormat var2 = (VideoFormat)var1;
         return new VideoFormat[]{new JPEGFormat(var2.getSize(), -1, Format.byteArray, var2.getFrameRate(), -1, -1)};
      } else {
         return new Format[0];
      }
   }

   public void open() throws ResourceUnavailableException {
   }

   public int process(Buffer var1, Buffer var2) {
      Format var3 = var1.getFormat();
      Format var4 = var2.getFormat();
      return var3.relax().matches(rgbFormat) && var4.relax().matches(jpegFormat) ? this.processRGBtoJPEG(var1, var2) : 1;
   }

   protected int processRGBtoJPEG(Buffer var1, Buffer var2) {
      Hashtable var7 = imageTable;
      synchronized(var7){}

      Throwable var10000;
      boolean var10001;
      Throwable var101;
      label751: {
         label755: {
            label757: {
               int var3;
               RGBFormat var9;
               try {
                  var9 = (RGBFormat)var1.getFormat();
                  if ((VideoFormat)var2.getFormat() == null) {
                     var3 = var9.getSize().width;
                     int var4 = var9.getSize().height;
                     var2.setFormat(new JPEGFormat(new Dimension(var3, var4), var3 * var4 + 200, Format.byteArray, var9.getFrameRate(), -1, -1));
                  }
               } catch (Throwable var100) {
                  var10000 = var100;
                  var10001 = false;
                  break label757;
               }

               BufferedImage var6;
               Dimension var8;
               int[] var10;
               try {
                  var10 = (int[])((int[])var1.getData());
                  var8 = var9.getSize();
                  var6 = (BufferedImage)imageTable.get(var8);
               } catch (Throwable var99) {
                  var10000 = var99;
                  var10001 = false;
                  break label757;
               }

               BufferedImage var5 = var6;
               if (var6 == null) {
                  try {
                     var5 = new BufferedImage(var8.width, var8.height, 1);
                  } catch (Throwable var98) {
                     var10000 = var98;
                     var10001 = false;
                     break label757;
                  }
               }

               byte[] var105;
               label737: {
                  try {
                     RGBConverter.populateImage(var10, var1.getOffset(), var5, var9);
                     Object var104 = var2.getData();
                     if (var104 instanceof byte[]) {
                        var105 = (byte[])((byte[])var104);
                        break label737;
                     }
                  } catch (Throwable var97) {
                     var10000 = var97;
                     var10001 = false;
                     break label757;
                  }

                  try {
                     var105 = new byte[var8.width * var8.height + 200];
                     var2.setData(var105);
                  } catch (Throwable var96) {
                     var10000 = var96;
                     var10001 = false;
                     break label757;
                  }
               }

               label728:
               try {
                  var3 = writeJPEG(var5, var105);
                  imageTable.put(var8, var5);
                  var2.setLength(var3);
                  var2.setDiscard(var1.isDiscard());
                  var2.setDuration(var1.getDuration());
                  var2.setEOM(var1.isEOM());
                  var2.setFlags(var1.getFlags());
                  var2.setHeader((Object)null);
                  var2.setTimeStamp(var1.getTimeStamp());
                  var2.setSequenceNumber(var1.getSequenceNumber());
                  var2.setOffset(0);
                  break label755;
               } catch (Throwable var95) {
                  var10000 = var95;
                  var10001 = false;
                  break label728;
               }
            }

            var101 = var10000;

            try {
               Logger var102 = logger;
               Level var103 = Level.WARNING;
               StringBuilder var106 = new StringBuilder();
               var106.append("");
               var106.append(var101);
               var102.log(var103, var106.toString(), var101);
               return 1;
            } catch (Throwable var93) {
               var10000 = var93;
               var10001 = false;
               break label751;
            }
         }

         label724:
         try {
            return 0;
         } catch (Throwable var94) {
            var10000 = var94;
            var10001 = false;
            break label724;
         }
      }

      while(true) {
         var101 = var10000;

         try {
            throw var101;
         } catch (Throwable var92) {
            var10000 = var92;
            var10001 = false;
            continue;
         }
      }
   }

   public void reset() {
   }

   public Format setInputFormat(Format var1) {
      return var1.relax().matches(rgbFormat) ? var1 : null;
   }

   public Format setOutputFormat(Format var1) {
      return var1.relax().matches(jpegFormat) ? var1 : null;
   }
}
