package com.t4l.jmf;

import javax.media.format.RGBFormat;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.image.BufferedImage;

public class RGBConverter {
   public static void flipVertical(int[] var0, int var1, int var2) {
      int[] var6 = new int[var1];
      int[] var7 = new int[var1];

      for(int var3 = 0; var3 < var2 / 2; ++var3) {
         int var4 = var3 * var1;
         int var5 = (var2 - 1 - var3) * var1;
         System.arraycopy(var0, var4, var6, 0, var1);
         System.arraycopy(var0, var5, var7, 0, var1);
         System.arraycopy(var6, 0, var0, var5, var1);
         System.arraycopy(var7, 0, var0, var4, var1);
      }

   }

   private static int getShift(int var0) {
      int var1 = var0;

      int var2;
      for(var2 = 0; var1 != 255; var1 /= 2) {
         if (var1 < 255) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Unsupported mask: ");
            var3.append(Integer.toString(var0, 16));
            throw new IllegalArgumentException(var3.toString());
         }

         ++var2;
      }

      return var2;
   }

   public static void populateArray(BufferedImage var0, int[] var1, RGBFormat var2) {
      int var3 = var0.getType();
      int var7 = var0.getWidth();
      int var8 = var0.getHeight();
      if (var2 != null) {
         int var9 = var2.getLineStride();
         if (var1.length < var9 * var8) {
            StringBuilder var15 = new StringBuilder();
            var15.append("Illegal array size: ");
            var15.append(var1.length);
            var15.append("<");
            var15.append(var9 * var8);
            throw new IllegalArgumentException(var15.toString());
         } else {
            if (var3 != 2 && var3 != 3 && var3 != 1) {
               var0.getRGB(0, 0, var7, var8, var1, 0, var7);
            } else {
               var0.getRaster().getDataElements(0, 0, var7, var8, var1);
            }

            int var5 = var2.getRedMask();
            int var4 = var2.getGreenMask();
            var3 = var2.getBlueMask();
            if (var5 != 16711680 || var4 != 65280 || var3 != 255 || var2.getLineStride() != var7 || var2.getPixelStride() != 1) {
               int var10 = getShift(var5);
               int var11 = getShift(var4);
               int var12 = getShift(var3);
               int var13 = var2.getPixelStride();

               for(var3 = var8 - 1; var3 >= 0; --var3) {
                  for(int var6 = var7 - 1; var6 >= 0; --var6) {
                     int var14 = var3 * var7 + var6;
                     var1[var3 * var9 + var6 * var13] = ((var1[var14] >> 16 & 255) << var10) + ((var1[var14] >> 8 & 255) << var11) + ((var1[var14] >> 0 & 255) << var12);
                  }
               }
            }

            if (var2.getFlipped() == 1) {
               flipVertical(var1, var7, var8);
            }

         }
      } else {
         throw null;
      }
   }

   public static void populateImage(int[] var0, int var1, BufferedImage var2, RGBFormat var3) {
      int var5 = var2.getType();
      byte var4;
      if (var5 != 2 && var5 != 3) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      processData(var0, var1, var3, var4);
      var1 = var2.getWidth();
      int var6 = var2.getHeight();
      if (var5 != 2 && var5 != 1) {
         var2.setRGB(0, 0, var1, var6, var0, 0, var1);
      } else {
         var2.getRaster().setDataElements(0, 0, var1, var6, var0);
      }
   }

   private static void processData(int[] var0, int var1, RGBFormat var2, int var3) {
      Dimension var15 = var2.getSize();
      int var9 = var15.width;
      int var10 = var15.height;
      int var5 = var2.getRedMask();
      int var4 = var2.getGreenMask();
      int var6 = var2.getBlueMask();
      int var11 = getShift(var5);
      int var12 = getShift(var4);
      int var13 = getShift(var6);
      int var14 = var2.getLineStride() - var9;
      if (var1 == 0 && var2.getPixelStride() == 1 && var14 == 0 && var5 == 16711680 && var4 == 65280 && var6 == 255) {
         if (var3 != 1) {
            for(var1 = 0; var1 < var9 * var10; ++var1) {
               var0[var1] = (var0[var1] & 16777215) - 16777216;
            }

         }
      } else {
         byte var8 = 0;
         byte var7 = 0;
         int var16;
         if (var3 == 2) {
            var4 = 0;

            for(var3 = var7; var4 < var10; var3 = var6) {
               var6 = 0;

               for(var16 = var3; var6 < var9; ++var6) {
                  var5 = var0[var16 + var1];
                  var0[var4 * var9 + var6] = ((var5 >> var11 & 255) << 16) - 16777216 + ((var5 >> var12 & 255) << 8) + (var5 >> var13 & 255);
                  ++var16;
               }

               var6 = var16 + var14;
               ++var4;
               var5 = var5;
            }
         } else {
            var7 = 0;
            var3 = var8;

            for(var4 = var7; var4 < var10; var3 = var16) {
               var8 = 0;
               var16 = var3;
               var3 = var6;

               for(var6 = var8; var6 < var9; ++var6) {
                  int var17 = var0[var16 + var1];
                  var0[var4 * var9 + var6] = ((var17 >> var11 & 255) << 16) + ((var17 >> var12 & 255) << 8) + (var17 >> var13 & 255);
                  ++var16;
               }

               var16 += var14;
               ++var4;
               var6 = var3;
            }
         }

         if (var2.getFlipped() == 1) {
            flipVertical(var0, var9, var10);
         }

      }
   }
}
