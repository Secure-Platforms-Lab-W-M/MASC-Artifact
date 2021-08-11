package net.sf.fmj.media.util;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.RGBFormat;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.Graphics;
import org.atalk.android.util.java.awt.Image;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.java.awt.image.ComponentColorModel;
import org.atalk.android.util.java.awt.image.ComponentSampleModel;
import org.atalk.android.util.java.awt.image.DataBuffer;
import org.atalk.android.util.java.awt.image.DataBufferByte;
import org.atalk.android.util.java.awt.image.DataBufferInt;
import org.atalk.android.util.java.awt.image.DirectColorModel;
import org.atalk.android.util.java.awt.image.ImageObserver;

public class ImageToBuffer {
   private static BufferedImage convert(Image var0) {
      BufferedImage var1 = new BufferedImage(var0.getWidth((ImageObserver)null), var0.getHeight((ImageObserver)null), 1);
      Graphics var2 = var1.getGraphics();
      var2.drawImage(var0, 0, 0, (ImageObserver)null);
      var2.dispose();
      return var1;
   }

   public static Buffer createBuffer(Image var0, float var1) {
      BufferedImage var15;
      if (var0 instanceof BufferedImage) {
         var15 = (BufferedImage)var0;
      } else {
         var15 = convert(var0);
      }

      DataBuffer var11 = var15.getRaster().getDataBuffer();
      int var6;
      Object var10;
      StringBuilder var17;
      Class var19;
      if (var11 instanceof DataBufferInt) {
         int[] var9 = ((DataBufferInt)var11).getData();
         var10 = var9;
         var6 = var9.length;
         var19 = Format.intArray;
      } else {
         if (!(var11 instanceof DataBufferByte)) {
            var17 = new StringBuilder();
            var17.append("Unknown or unsupported data buffer type: ");
            var17.append(var11);
            throw new IllegalArgumentException(var17.toString());
         }

         byte[] var20 = ((DataBufferByte)var11).getData();
         var10 = var20;
         var6 = var20.length;
         var19 = Format.byteArray;
      }

      int var2 = var15.getType();
      Buffer var12 = new Buffer();
      Dimension var13 = new Dimension(var15.getWidth(), var15.getHeight());
      int var3;
      int var4;
      int var5;
      if (var2 == 5) {
         var2 = 24;
         var3 = 1;
         var4 = 2;
         var5 = 3;
      } else if (var2 == 4) {
         var2 = 32;
         var3 = 255;
         var4 = 65280;
         var5 = 16711680;
      } else if (var2 == 1) {
         var2 = 32;
         var3 = 16711680;
         var4 = 65280;
         var5 = 255;
      } else if (var2 == 2) {
         var2 = 32;
         var3 = 16711680;
         var4 = 65280;
         var5 = 255;
      } else if (var15.getColorModel() instanceof ComponentColorModel && var15.getSampleModel() instanceof ComponentSampleModel) {
         ComponentColorModel var14 = (ComponentColorModel)var15.getColorModel();
         ComponentSampleModel var18 = (ComponentSampleModel)var15.getSampleModel();
         int[] var21 = var18.getBandOffsets();
         if (var11 instanceof DataBufferInt) {
            var4 = var21[0];
            var3 = var21[1];
            var2 = var21[2];
            var3 = 255 << var3;
            var5 = 32;
            var4 = 255 << var4;
            var2 = 255 << var2;
         } else {
            if (!(var11 instanceof DataBufferByte)) {
               var17 = new StringBuilder();
               var17.append("Unsupported buffered image type: ");
               var17.append(var2);
               throw new IllegalArgumentException(var17.toString());
            }

            var5 = var18.getPixelStride();
            var4 = var21[0];
            var3 = var21[1];
            var2 = var21[2];
            var5 *= 8;
            ++var4;
            ++var2;
            ++var3;
         }

         int var7 = var3;
         int var8 = var2;
         var2 = var5;
         var3 = var4;
         var4 = var7;
         var5 = var8;
      } else {
         if (!(var15.getColorModel() instanceof DirectColorModel)) {
            var17 = new StringBuilder();
            var17.append("Unsupported buffered image type: ");
            var17.append(var2);
            throw new IllegalArgumentException(var17.toString());
         }

         DirectColorModel var16 = (DirectColorModel)var15.getColorModel();
         if (!(var11 instanceof DataBufferInt)) {
            var17 = new StringBuilder();
            var17.append("Unsupported buffered image type: ");
            var17.append(var2);
            throw new IllegalArgumentException(var17.toString());
         }

         var3 = var16.getRedMask();
         var4 = var16.getGreenMask();
         var5 = var16.getBlueMask();
         var2 = 32;
      }

      var12.setFormat(new RGBFormat(var13, -1, var19, var1, var2, var3, var4, var5));
      var12.setData(var10);
      var12.setLength(var6);
      var12.setOffset(0);
      return var12;
   }
}
