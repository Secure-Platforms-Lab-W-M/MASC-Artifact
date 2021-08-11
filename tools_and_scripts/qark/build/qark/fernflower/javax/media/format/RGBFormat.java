package javax.media.format;

import javax.media.Format;
import org.atalk.android.util.java.awt.Dimension;

public class RGBFormat extends VideoFormat {
   public static final int BIG_ENDIAN = 0;
   private static String ENCODING = "rgb";
   public static final int LITTLE_ENDIAN = 1;
   protected int bitsPerPixel = -1;
   protected int blueMask = -1;
   protected int endian = -1;
   protected int flipped = -1;
   protected int greenMask = -1;
   protected int lineStride = -1;
   protected int pixelStride = -1;
   protected int redMask = -1;

   public RGBFormat() {
      super(ENCODING);
      this.dataType = null;
   }

   public RGBFormat(Dimension var1, int var2, Class var3, float var4, int var5, int var6, int var7, int var8) {
      super(ENCODING, var1, var2, var3, var4);
      this.bitsPerPixel = var5;
      this.redMask = var6;
      this.greenMask = var7;
      this.blueMask = var8;
      if (var5 != -1 && var3 != null) {
         this.pixelStride = var5 / 8;
         if (var3 != byteArray) {
            this.pixelStride = 1;
         }
      } else {
         this.pixelStride = -1;
      }

      label24: {
         if (var1 != null) {
            var2 = this.pixelStride;
            if (var2 != -1) {
               this.lineStride = var2 * var1.width;
               break label24;
            }
         }

         this.lineStride = -1;
      }

      this.flipped = 0;
      if (var5 == 16 && var3 == byteArray) {
         this.endian = 1;
      } else {
         this.endian = -1;
      }
   }

   public RGBFormat(Dimension var1, int var2, Class var3, float var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      super(ENCODING, var1, var2, var3, var4);
      this.bitsPerPixel = var5;
      this.redMask = var6;
      this.greenMask = var7;
      this.blueMask = var8;
      this.pixelStride = var9;
      this.lineStride = var10;
      this.flipped = var11;
      this.endian = var12;
   }

   public Object clone() {
      RGBFormat var1 = new RGBFormat(this.size, this.maxDataLength, this.dataType, this.frameRate, this.bitsPerPixel, this.redMask, this.greenMask, this.blueMask, this.pixelStride, this.lineStride, this.flipped, this.endian);
      var1.copy(this);
      return var1;
   }

   protected void copy(Format var1) {
      super.copy(var1);
      if (var1 instanceof RGBFormat) {
         RGBFormat var2 = (RGBFormat)var1;
         this.bitsPerPixel = var2.bitsPerPixel;
         this.redMask = var2.redMask;
         this.greenMask = var2.greenMask;
         this.blueMask = var2.blueMask;
         this.pixelStride = var2.pixelStride;
         this.lineStride = var2.lineStride;
         this.flipped = var2.flipped;
         this.endian = var2.endian;
      }

   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof RGBFormat;
      boolean var3 = false;
      if (var2) {
         RGBFormat var4 = (RGBFormat)var1;
         var2 = var3;
         if (super.equals(var1)) {
            var2 = var3;
            if (this.bitsPerPixel == var4.bitsPerPixel) {
               var2 = var3;
               if (this.redMask == var4.redMask) {
                  var2 = var3;
                  if (this.greenMask == var4.greenMask) {
                     var2 = var3;
                     if (this.blueMask == var4.blueMask) {
                        var2 = var3;
                        if (this.pixelStride == var4.pixelStride) {
                           var2 = var3;
                           if (this.lineStride == var4.lineStride) {
                              var2 = var3;
                              if (this.endian == var4.endian) {
                                 var2 = var3;
                                 if (this.flipped == var4.flipped) {
                                    var2 = true;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public int getBitsPerPixel() {
      return this.bitsPerPixel;
   }

   public int getBlueMask() {
      return this.blueMask;
   }

   public int getEndian() {
      return this.endian;
   }

   public int getFlipped() {
      return this.flipped;
   }

   public int getGreenMask() {
      return this.greenMask;
   }

   public int getLineStride() {
      return this.lineStride;
   }

   public int getPixelStride() {
      return this.pixelStride;
   }

   public int getRedMask() {
      return this.redMask;
   }

   public Format intersects(Format var1) {
      Format var3 = super.intersects(var1);
      if (var3 == null) {
         return null;
      } else if (!(var1 instanceof RGBFormat)) {
         return var3;
      } else {
         RGBFormat var4 = (RGBFormat)var1;
         RGBFormat var5 = (RGBFormat)var3;
         int var2 = this.bitsPerPixel;
         if (var2 == -1) {
            var2 = var4.bitsPerPixel;
         }

         var5.bitsPerPixel = var2;
         var2 = this.pixelStride;
         if (var2 == -1) {
            var2 = var4.pixelStride;
         }

         var5.pixelStride = var2;
         var2 = this.lineStride;
         if (var2 == -1) {
            var2 = var4.lineStride;
         }

         var5.lineStride = var2;
         var2 = this.redMask;
         if (var2 == -1) {
            var2 = var4.redMask;
         }

         var5.redMask = var2;
         var2 = this.greenMask;
         if (var2 == -1) {
            var2 = var4.greenMask;
         }

         var5.greenMask = var2;
         var2 = this.blueMask;
         if (var2 == -1) {
            var2 = var4.blueMask;
         }

         var5.blueMask = var2;
         var2 = this.flipped;
         if (var2 == -1) {
            var2 = var4.flipped;
         }

         var5.flipped = var2;
         var2 = this.endian;
         if (var2 == -1) {
            var2 = var4.endian;
         }

         var5.endian = var2;
         return var5;
      }
   }

   public boolean matches(Format var1) {
      boolean var4 = super.matches(var1);
      boolean var5 = false;
      if (!var4) {
         return false;
      } else if (!(var1 instanceof RGBFormat)) {
         return true;
      } else {
         RGBFormat var6 = (RGBFormat)var1;
         int var2 = this.bitsPerPixel;
         int var3;
         if (var2 != -1) {
            var3 = var6.bitsPerPixel;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.redMask;
         if (var2 != -1) {
            var3 = var6.redMask;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.greenMask;
         if (var2 != -1) {
            var3 = var6.greenMask;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.blueMask;
         if (var2 != -1) {
            var3 = var6.blueMask;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.pixelStride;
         if (var2 != -1) {
            var3 = var6.pixelStride;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.endian;
         if (var2 != -1) {
            var3 = var6.endian;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.flipped;
         if (var2 != -1) {
            var3 = var6.flipped;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var4 = true;
         return var4;
      }
   }

   public Format relax() {
      RGBFormat var1 = (RGBFormat)super.relax();
      if (var1 == null) {
         return null;
      } else {
         var1.lineStride = -1;
         var1.pixelStride = -1;
         return var1;
      }
   }

   public String toString() {
      String var2 = this.getEncoding().toUpperCase();
      String var1 = var2;
      StringBuilder var4;
      if (this.size != null) {
         var4 = new StringBuilder();
         var4.append(var2);
         var4.append(", ");
         var4.append(this.size.width);
         var4.append("x");
         var4.append(this.size.height);
         var1 = var4.toString();
      }

      var2 = var1;
      StringBuilder var5;
      if (this.frameRate != -1.0F) {
         var5 = new StringBuilder();
         var5.append(var1);
         var5.append(", FrameRate=");
         var5.append((float)((int)(this.frameRate * 10.0F)) / 10.0F);
         var2 = var5.toString();
      }

      var1 = var2;
      if (this.maxDataLength != -1) {
         var4 = new StringBuilder();
         var4.append(var2);
         var4.append(", Length=");
         var4.append(this.maxDataLength);
         var1 = var4.toString();
      }

      var5 = new StringBuilder();
      var5.append(var1);
      var5.append(", ");
      var5.append(this.bitsPerPixel);
      var5.append("-bit");
      var1 = var5.toString();
      var5 = new StringBuilder();
      var5.append(var1);
      var5.append(", Masks=");
      var5.append(this.redMask);
      var5.append(":");
      var5.append(this.greenMask);
      var5.append(":");
      var5.append(this.blueMask);
      var2 = var5.toString();
      var1 = var2;
      if (this.pixelStride != 1) {
         var4 = new StringBuilder();
         var4.append(var2);
         var4.append(", PixelStride=");
         var4.append(this.pixelStride);
         var1 = var4.toString();
      }

      var5 = new StringBuilder();
      var5.append(var1);
      var5.append(", LineStride=");
      var5.append(this.lineStride);
      var2 = var5.toString();
      var1 = var2;
      if (this.flipped != -1) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var2);
         if (this.flipped == 1) {
            var1 = ", Flipped";
         } else {
            var1 = "";
         }

         var3.append(var1);
         var1 = var3.toString();
      }

      var2 = var1;
      if (this.dataType == byteArray) {
         var2 = var1;
         if (this.bitsPerPixel == 16) {
            var2 = var1;
            if (this.endian != -1) {
               var5 = new StringBuilder();
               var5.append(var1);
               if (this.endian == 0) {
                  var1 = ", BigEndian";
               } else {
                  var1 = ", LittleEndian";
               }

               var5.append(var1);
               var2 = var5.toString();
            }
         }
      }

      var1 = var2;
      if (this.dataType != null) {
         var1 = var2;
         if (this.dataType != Format.byteArray) {
            var4 = new StringBuilder();
            var4.append(var2);
            var4.append(", ");
            var4.append(this.dataType);
            var1 = var4.toString();
         }
      }

      return var1;
   }
}
