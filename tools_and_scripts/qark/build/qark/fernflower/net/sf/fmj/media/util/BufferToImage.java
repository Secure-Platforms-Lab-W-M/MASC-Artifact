package net.sf.fmj.media.util;

import java.util.Hashtable;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import org.atalk.android.util.java.awt.Image;
import org.atalk.android.util.java.awt.Point;
import org.atalk.android.util.java.awt.color.ColorSpace;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.java.awt.image.ComponentColorModel;
import org.atalk.android.util.java.awt.image.ComponentSampleModel;
import org.atalk.android.util.java.awt.image.DataBufferByte;
import org.atalk.android.util.java.awt.image.DataBufferInt;
import org.atalk.android.util.java.awt.image.DataBufferUShort;
import org.atalk.android.util.java.awt.image.DirectColorModel;
import org.atalk.android.util.java.awt.image.Raster;
import org.atalk.android.util.java.awt.image.SinglePixelPackedSampleModel;
import org.atalk.android.util.java.awt.image.WritableRaster;

public class BufferToImage {
   public BufferToImage(VideoFormat var1) {
   }

   public BufferedImage createBufferedImage(Buffer var1) {
      VideoFormat var16 = (VideoFormat)var1.getFormat();
      int var9 = var16.getSize().width;
      int var10 = var16.getSize().height;
      Class var15 = var16.getDataType();
      if (!(var16 instanceof RGBFormat)) {
         throw new UnsupportedOperationException();
      } else {
         RGBFormat var26 = (RGBFormat)var16;
         int var2 = var26.getBitsPerPixel();
         int var11 = var26.getRedMask();
         int var12 = var26.getGreenMask();
         int var3 = var26.getBlueMask();
         int var13 = var26.getLineStride();
         int var14 = var26.getPixelStride();
         boolean var4;
         if (var26.getFlipped() == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         WritableRaster var19;
         int var24;
         if (var15 != Format.byteArray) {
            if (var15 == Format.shortArray) {
               short[] var22 = (short[])((short[])var1.getData());
               if (var2 == 16) {
                  var24 = var22.length;
                  DataBufferUShort var23 = new DataBufferUShort(new short[][]{var22}, var24);
                  var19 = Raster.createWritableRaster(new SinglePixelPackedSampleModel(1, var9, var10, var13, new int[]{var11, var12, var3}), var23, new Point(0, 0));
                  return new BufferedImage(new DirectColorModel(var2, var11, var12, var3), var19, false, (Hashtable)null);
               } else {
                  throw new UnsupportedOperationException();
               }
            } else if (var15 == Format.intArray) {
               int[] var20 = (int[])((int[])var1.getData());
               var2 = var20.length;
               DataBufferInt var21 = new DataBufferInt(new int[][]{var20}, var2);
               var19 = Raster.createWritableRaster(new SinglePixelPackedSampleModel(3, var9, var10, new int[]{var11, var12, var3}), var21, new Point(0, 0));
               return new BufferedImage(new DirectColorModel(24, var11, var12, var3, 0), var19, false, (Hashtable)null);
            } else {
               throw new UnsupportedOperationException();
            }
         } else {
            byte[] var17 = (byte[])((byte[])var1.getData());
            DataBufferByte var18;
            if (var2 == 24) {
               var2 = var17.length;
               var18 = new DataBufferByte(new byte[][]{var17}, var2);
               var19 = Raster.createWritableRaster(new ComponentSampleModel(0, var9, var10, var14, var13, new int[]{var11 - 1, var12 - 1, var3 - 1}), var18, new Point(0, 0));
               return new BufferedImage(new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8}, false, false, 1, 0), var19, false, (Hashtable)null);
            } else if (var2 == 32) {
               var2 = var17.length;
               var18 = new DataBufferByte(new byte[][]{var17}, var2);
               var19 = Raster.createWritableRaster(new ComponentSampleModel(0, var9, var10, var14, var13, new int[]{var11 - 1, var12 - 1, var3 - 1, 3}), var18, new Point(0, 0));
               return new BufferedImage(new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 8}, true, false, 3, 0), var19, false, (Hashtable)null);
            } else if (var2 == 8) {
               var24 = var17.length;
               var18 = new DataBufferByte(new byte[][]{var17}, var24);
               var19 = Raster.createWritableRaster(new SinglePixelPackedSampleModel(0, var9, var10, var13, new int[]{var11, var12, var3}), var18, new Point(0, 0));
               return new BufferedImage(new DirectColorModel(var2, var11, var12, var3), var19, false, (Hashtable)null);
            } else {
               BufferedImage var25 = new BufferedImage(var9, var10, 1);
               int[] var27 = new int[var9 * var10];
               var2 = 0;
               if (var4) {
                  var2 = (var10 - 1) * var13;
               }

               int var5 = 0;

               for(int var6 = 0; var5 < var10; ++var5) {
                  int var8 = var2;

                  for(int var7 = 0; var7 < var9; ++var6) {
                     var27[var6] = ((0 + (var17[var8 + var11 - 1] & 255)) * 256 + (var17[var8 + var12 - 1] & 255)) * 256 + (var17[var8 + var3 - 1] & 255);
                     var8 += var14;
                     ++var7;
                  }

                  if (var4) {
                     var2 -= var13;
                  } else {
                     var2 += var13;
                  }
               }

               var25.setRGB(0, 0, var9, var10, var27, 0, var9);
               return var25;
            }
         }
      }
   }

   public Image createImage(Buffer var1) {
      return this.createBufferedImage(var1);
   }
}
