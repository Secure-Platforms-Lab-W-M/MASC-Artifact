package com.sun.media.vfw;

import com.sun.media.format.AviVideoFormat;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import org.atalk.android.util.java.awt.Dimension;

public class BitMapInfo {
   public int biBitCount;
   public int biClrImportant;
   public int biClrUsed;
   public int biHeight;
   public int biPlanes;
   public int biSizeImage;
   public int biWidth;
   public int biXPelsPerMeter;
   public int biYPelsPerMeter;
   public byte[] extraBytes;
   public int extraSize;
   public String fourcc;

   public BitMapInfo() {
      this.fourcc = "";
      this.biPlanes = 1;
      this.biBitCount = 24;
   }

   public BitMapInfo(String var1, int var2, int var3) {
      this.fourcc = var1;
      this.biPlanes = 1;
      this.biBitCount = 24;
      this.biWidth = var2;
      this.biHeight = var3;
      if (var1.equals("RGB")) {
         this.biSizeImage = this.biWidth * this.biHeight * (this.biBitCount / 8);
      }

   }

   public BitMapInfo(String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.fourcc = var1;
      this.biPlanes = var4;
      this.biBitCount = var5;
      this.biWidth = var2;
      this.biHeight = var3;
      this.biSizeImage = var6;
      this.biClrUsed = var7;
      this.biClrImportant = var8;
   }

   public BitMapInfo(VideoFormat var1) {
      if (var1 instanceof RGBFormat) {
         RGBFormat var4 = (RGBFormat)var1;
         this.fourcc = var4.getEncoding().toUpperCase();
         this.biPlanes = 1;
         this.biBitCount = var4.getBitsPerPixel();
         if (var4.getSize() == null) {
            this.biWidth = 320;
            this.biHeight = 240;
         } else {
            this.biWidth = var4.getSize().width;
            this.biHeight = var4.getSize().height;
         }

         int var2 = this.biBitCount;
         if (var2 == -1) {
            this.biSizeImage = -2;
         } else {
            this.biSizeImage = this.biWidth * this.biHeight * (var2 / 8);
         }

         this.biClrUsed = 0;
         this.biClrImportant = 0;
      } else if (var1 instanceof AviVideoFormat) {
         AviVideoFormat var3 = (AviVideoFormat)var1;
         this.fourcc = var3.getEncoding();
         this.biPlanes = var3.getPlanes();
         this.biBitCount = var3.getBitsPerPixel();
         this.biWidth = var3.getSize().width;
         this.biHeight = var3.getSize().height;
         this.biSizeImage = var3.getImageSize();
         this.biClrUsed = var3.getClrUsed();
         this.biClrImportant = var3.getClrImportant();
      } else if (var1 instanceof YUVFormat) {
         if (((YUVFormat)var1).getYuvType() == 2) {
            this.fourcc = "YV12";
            this.biBitCount = 12;
         } else {
            this.fourcc = var1.getEncoding();
            this.biBitCount = 24;
         }

         this.biWidth = 320;
         this.biHeight = 240;
         this.biPlanes = 1;
         this.biSizeImage = -1;
      } else {
         this.fourcc = var1.getEncoding();
         this.biBitCount = 24;
         this.biWidth = 320;
         this.biHeight = 240;
         this.biPlanes = 1;
         this.biSizeImage = -1;
      }
   }

   public VideoFormat createVideoFormat(Class var1) {
      return this.createVideoFormat(var1, -1.0F);
   }

   public VideoFormat createVideoFormat(Class var1, float var2) {
      int var3;
      int var4;
      int var6;
      if (this.fourcc.equals("RGB")) {
         var3 = this.biBitCount;
         short var12;
         if (var3 == 32) {
            if (var1 == int[].class) {
               var3 = 16711680;
               var4 = 65280;
               var12 = 255;
            } else {
               var3 = 3;
               var4 = 2;
               var12 = 1;
            }
         } else if (var3 == 24) {
            var3 = 3;
            var4 = 2;
            var12 = 1;
         } else if (var3 == 16) {
            var3 = 31744;
            var4 = 992;
            var12 = 31;
         } else {
            var3 = -1;
            var4 = -1;
            var12 = -1;
         }

         int var7;
         if (var1 == int[].class) {
            var6 = this.biBitCount / 32;
            var7 = this.biSizeImage / 4;
         } else if (var1 == byte[].class) {
            var6 = this.biBitCount / 8;
            var7 = this.biSizeImage;
         } else {
            if (var1 != short[].class) {
               throw new IllegalArgumentException();
            }

            var6 = this.biBitCount / 16;
            var7 = this.biSizeImage / 2;
         }

         int var8 = this.biWidth;
         return new RGBFormat(new Dimension(this.biWidth, this.biHeight), var7, var1, var2, this.biBitCount, var3, var4, var12, var6, var6 * var8, 1, 1);
      } else {
         int var5;
         Dimension var11;
         if (this.fourcc.equals("YV12")) {
            var11 = new Dimension(this.biWidth, this.biHeight);
            var3 = this.biSizeImage;
            var4 = this.biWidth;
            var5 = var4 / 2;
            var6 = this.biHeight;
            return new YUVFormat(var11, var3, byte[].class, var2, 2, var4, var5, 0, var4 * var6 / 4 + var4 * var6, var4 * var6);
         } else if (this.fourcc.equals("I420")) {
            var11 = new Dimension(this.biWidth, this.biHeight);
            var3 = this.biSizeImage;
            var4 = this.biWidth;
            var5 = var4 / 2;
            var6 = this.biHeight;
            return new YUVFormat(var11, var3, byte[].class, var2, 2, var4, var5, 0, var4 * var6, var4 * var6 + var6 * var4 / 4);
         } else {
            String var9 = this.fourcc;
            Dimension var10 = new Dimension(this.biWidth, this.biHeight);
            var3 = this.biSizeImage;
            return new AviVideoFormat(var9, var10, var3, var1, var2, this.biPlanes, this.biBitCount, var3, this.biXPelsPerMeter, this.biYPelsPerMeter, this.biClrUsed, this.biClrImportant, this.extraBytes);
         }
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Size = ");
      var1.append(this.biWidth);
      var1.append(" x ");
      var1.append(this.biHeight);
      var1.append("\tPlanes = ");
      var1.append(this.biPlanes);
      var1.append("\tBitCount = ");
      var1.append(this.biBitCount);
      var1.append("\tFourCC = ");
      var1.append(this.fourcc);
      var1.append("\tSizeImage = ");
      var1.append(this.biSizeImage);
      var1.append("\nClrUsed = ");
      var1.append(this.biClrUsed);
      var1.append("\nClrImportant = ");
      var1.append(this.biClrImportant);
      var1.append("\nExtraSize = ");
      var1.append(this.extraSize);
      var1.append("\n");
      return var1.toString();
   }
}
