package com.sun.media.format;

import javax.media.Format;
import javax.media.format.VideoFormat;
import net.sf.fmj.codegen.FormatTraceUtils;
import net.sf.fmj.utility.FormatUtils;
import org.atalk.android.util.java.awt.Dimension;

public class AviVideoFormat extends VideoFormat {
   protected int bitsPerPixel = -1;
   protected int clrImportant = -1;
   protected int clrUsed = -1;
   protected byte[] codecSpecificHeader;
   protected int imageSize = -1;
   protected int planes = -1;
   protected int xPelsPerMeter = -1;
   protected int yPelsPerMeter = -1;

   public AviVideoFormat(String var1) {
      super(var1);
   }

   public AviVideoFormat(String var1, Dimension var2, int var3, Class var4, float var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, byte[] var13) {
      super(var1, var2, var3, var4, var5);
      this.planes = var6;
      this.bitsPerPixel = var7;
      this.imageSize = var8;
      this.xPelsPerMeter = var9;
      this.yPelsPerMeter = var10;
      this.clrUsed = var11;
      this.clrImportant = var12;
      this.codecSpecificHeader = var13;
   }

   public Object clone() {
      return new AviVideoFormat(this.encoding, this.size, this.maxDataLength, this.dataType, this.frameRate, this.planes, this.bitsPerPixel, this.imageSize, this.xPelsPerMeter, this.yPelsPerMeter, this.clrUsed, this.clrImportant, this.codecSpecificHeader);
   }

   protected void copy(Format var1) {
      super.copy(var1);
      AviVideoFormat var2 = (AviVideoFormat)var1;
      this.planes = var2.planes;
      this.bitsPerPixel = var2.bitsPerPixel;
      this.imageSize = var2.imageSize;
      this.xPelsPerMeter = var2.xPelsPerMeter;
      this.yPelsPerMeter = var2.yPelsPerMeter;
      this.clrUsed = var2.clrUsed;
      this.clrImportant = var2.clrImportant;
      this.codecSpecificHeader = var2.codecSpecificHeader;
   }

   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else if (!(var1 instanceof AviVideoFormat)) {
         return false;
      } else {
         AviVideoFormat var2 = (AviVideoFormat)var1;
         return this.planes == var2.planes && this.bitsPerPixel == var2.bitsPerPixel && this.imageSize == var2.imageSize && this.xPelsPerMeter == var2.xPelsPerMeter && this.yPelsPerMeter == var2.yPelsPerMeter && this.clrUsed == var2.clrUsed && this.clrImportant == var2.clrImportant && FormatUtils.byteArraysEqual(this.codecSpecificHeader, var2.codecSpecificHeader);
      }
   }

   public int getBitsPerPixel() {
      return this.bitsPerPixel;
   }

   public int getClrImportant() {
      return this.clrImportant;
   }

   public int getClrUsed() {
      return this.clrUsed;
   }

   public byte[] getCodecSpecificHeader() {
      return this.codecSpecificHeader;
   }

   public int getImageSize() {
      return this.imageSize;
   }

   public int getPlanes() {
      return this.planes;
   }

   public int getXPelsPerMeter() {
      return this.xPelsPerMeter;
   }

   public int getYPelsPerMeter() {
      return this.yPelsPerMeter;
   }

   public Format intersects(Format var1) {
      Format var2 = super.intersects(var1);
      if (var1 instanceof AviVideoFormat) {
         AviVideoFormat var3 = (AviVideoFormat)var2;
         AviVideoFormat var4 = (AviVideoFormat)var1;
         if (this.getClass().isAssignableFrom(var1.getClass())) {
            if (FormatUtils.specified(this.planes)) {
               var3.planes = this.planes;
            }

            if (FormatUtils.specified(this.bitsPerPixel)) {
               var3.bitsPerPixel = this.bitsPerPixel;
            }

            if (FormatUtils.specified(this.imageSize)) {
               var3.imageSize = this.imageSize;
            }

            if (FormatUtils.specified(this.xPelsPerMeter)) {
               var3.xPelsPerMeter = this.xPelsPerMeter;
            }

            if (FormatUtils.specified(this.yPelsPerMeter)) {
               var3.yPelsPerMeter = this.yPelsPerMeter;
            }

            if (FormatUtils.specified(this.clrUsed)) {
               var3.clrUsed = this.clrUsed;
            }

            if (FormatUtils.specified(this.clrImportant)) {
               var3.clrImportant = this.clrImportant;
            }

            if (FormatUtils.specified(this.codecSpecificHeader)) {
               var3.codecSpecificHeader = this.codecSpecificHeader;
               return var2;
            }
         } else if (var1.getClass().isAssignableFrom(this.getClass())) {
            if (FormatUtils.specified(this.planes)) {
               var3.planes = var4.planes;
            }

            if (FormatUtils.specified(this.bitsPerPixel)) {
               var3.bitsPerPixel = var4.bitsPerPixel;
            }

            if (FormatUtils.specified(this.imageSize)) {
               var3.imageSize = var4.imageSize;
            }

            if (FormatUtils.specified(this.xPelsPerMeter)) {
               var3.xPelsPerMeter = var4.xPelsPerMeter;
            }

            if (FormatUtils.specified(this.yPelsPerMeter)) {
               var3.yPelsPerMeter = var4.yPelsPerMeter;
            }

            if (FormatUtils.specified(this.clrUsed)) {
               var3.clrUsed = var4.clrUsed;
            }

            if (FormatUtils.specified(this.clrImportant)) {
               var3.clrImportant = var4.clrImportant;
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
      } else if (!(var1 instanceof AviVideoFormat)) {
         return true;
      } else {
         AviVideoFormat var2 = (AviVideoFormat)var1;
         return FormatUtils.matches(this.planes, var2.planes) && FormatUtils.matches(this.bitsPerPixel, var2.bitsPerPixel) && FormatUtils.matches(this.imageSize, var2.imageSize) && FormatUtils.matches(this.xPelsPerMeter, var2.xPelsPerMeter) && FormatUtils.matches(this.yPelsPerMeter, var2.yPelsPerMeter) && FormatUtils.matches(this.clrUsed, var2.clrUsed) && FormatUtils.matches(this.clrImportant, var2.clrImportant);
      }
   }

   public Format relax() {
      AviVideoFormat var1 = (AviVideoFormat)super.relax();
      var1.imageSize = -1;
      FormatTraceUtils.traceRelax(this, var1);
      return var1;
   }

   public String toString() {
      byte[] var2 = this.codecSpecificHeader;
      int var1;
      if (var2 == null) {
         var1 = 0;
      } else {
         var1 = var2.length;
      }

      StringBuilder var3 = new StringBuilder();
      var3.append(super.toString());
      var3.append(" ");
      var3.append(var1);
      var3.append(" extra bytes");
      return var3.toString();
   }
}
