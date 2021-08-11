package javax.media.format;

import javax.media.Format;
import org.atalk.android.util.java.awt.Dimension;

public class YUVFormat extends VideoFormat {
   private static String ENCODING = "yuv";
   public static final int YUV_111 = 8;
   public static final int YUV_411 = 1;
   public static final int YUV_420 = 2;
   public static final int YUV_422 = 4;
   public static final int YUV_SIGNED = 64;
   public static final int YUV_YUYV = 32;
   public static final int YUV_YVU9 = 16;
   protected int offsetU = -1;
   protected int offsetV = -1;
   protected int offsetY = -1;
   protected int strideUV = -1;
   protected int strideY = -1;
   protected int yuvType = -1;

   public YUVFormat() {
      super(ENCODING);
   }

   public YUVFormat(int var1) {
      super(ENCODING);
      this.yuvType = var1;
   }

   public YUVFormat(Dimension var1, int var2, Class var3, float var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      super(ENCODING, var1, var2, var3, var4);
      this.yuvType = var5;
      this.strideY = var6;
      this.strideUV = var7;
      this.offsetY = var8;
      this.offsetU = var9;
      this.offsetV = var10;
   }

   public Object clone() {
      YUVFormat var1 = new YUVFormat(this.size, this.maxDataLength, this.dataType, this.frameRate, this.yuvType, this.strideY, this.strideUV, this.offsetY, this.offsetU, this.offsetV);
      var1.copy(this);
      return var1;
   }

   protected void copy(Format var1) {
      super.copy(var1);
      if (var1 instanceof YUVFormat) {
         YUVFormat var2 = (YUVFormat)var1;
         this.yuvType = var2.yuvType;
         this.strideY = var2.strideY;
         this.strideUV = var2.strideUV;
         this.offsetY = var2.offsetY;
         this.offsetU = var2.offsetU;
         this.offsetV = var2.offsetV;
      }

   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof YUVFormat;
      boolean var3 = false;
      if (var2) {
         YUVFormat var4 = (YUVFormat)var1;
         var2 = var3;
         if (super.equals(var1)) {
            var2 = var3;
            if (this.yuvType == var4.yuvType) {
               var2 = var3;
               if (this.strideY == var4.strideY) {
                  var2 = var3;
                  if (this.strideUV == var4.strideUV) {
                     var2 = var3;
                     if (this.offsetY == var4.offsetY) {
                        var2 = var3;
                        if (this.offsetU == var4.offsetU) {
                           var2 = var3;
                           if (this.offsetV == var4.offsetV) {
                              var2 = true;
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

   public int getOffsetU() {
      return this.offsetU;
   }

   public int getOffsetV() {
      return this.offsetV;
   }

   public int getOffsetY() {
      return this.offsetY;
   }

   public int getStrideUV() {
      return this.strideUV;
   }

   public int getStrideY() {
      return this.strideY;
   }

   public int getYuvType() {
      return this.yuvType;
   }

   public Format intersects(Format var1) {
      Format var3 = super.intersects(var1);
      if (var3 == null) {
         return null;
      } else if (!(var1 instanceof YUVFormat)) {
         return var3;
      } else {
         YUVFormat var4 = (YUVFormat)var1;
         YUVFormat var5 = (YUVFormat)var3;
         int var2 = this.yuvType;
         if (var2 == -1) {
            var2 = var4.yuvType;
         }

         var5.yuvType = var2;
         var2 = this.strideY;
         if (var2 == -1) {
            var2 = var4.strideY;
         }

         var5.strideY = var2;
         var2 = this.strideUV;
         if (var2 == -1) {
            var2 = var4.strideUV;
         }

         var5.strideUV = var2;
         var2 = this.offsetY;
         if (var2 == -1) {
            var2 = var4.offsetY;
         }

         var5.offsetY = var2;
         var2 = this.offsetU;
         if (var2 == -1) {
            var2 = var4.offsetU;
         }

         var5.offsetU = var2;
         var2 = this.offsetV;
         if (var2 == -1) {
            var2 = var4.offsetV;
         }

         var5.offsetV = var2;
         return var5;
      }
   }

   public boolean matches(Format var1) {
      boolean var4 = super.matches(var1);
      boolean var5 = false;
      if (!var4) {
         return false;
      } else if (!(var1 instanceof YUVFormat)) {
         return true;
      } else {
         YUVFormat var6 = (YUVFormat)var1;
         int var2 = this.yuvType;
         int var3;
         if (var2 != -1) {
            var3 = var6.yuvType;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.strideY;
         if (var2 != -1) {
            var3 = var6.strideY;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.strideUV;
         if (var2 != -1) {
            var3 = var6.strideUV;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.offsetY;
         if (var2 != -1) {
            var3 = var6.offsetY;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.offsetU;
         if (var2 != -1) {
            var3 = var6.offsetU;
            if (var3 != -1) {
               var4 = var5;
               if (var2 != var3) {
                  return var4;
               }
            }
         }

         var2 = this.offsetV;
         if (var2 != -1) {
            var3 = var6.offsetV;
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
      YUVFormat var1 = (YUVFormat)super.relax();
      if (var1 == null) {
         return null;
      } else {
         var1.strideY = -1;
         var1.strideUV = -1;
         var1.offsetY = -1;
         var1.offsetU = -1;
         var1.offsetV = -1;
         return var1;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("YUV Video Format: Size = ");
      var1.append(this.size);
      var1.append(" MaxDataLength = ");
      var1.append(this.maxDataLength);
      var1.append(" DataType = ");
      var1.append(this.dataType);
      var1.append(" yuvType = ");
      var1.append(this.yuvType);
      var1.append(" StrideY = ");
      var1.append(this.strideY);
      var1.append(" StrideUV = ");
      var1.append(this.strideUV);
      var1.append(" OffsetY = ");
      var1.append(this.offsetY);
      var1.append(" OffsetU = ");
      var1.append(this.offsetU);
      var1.append(" OffsetV = ");
      var1.append(this.offsetV);
      var1.append("\n");
      return var1.toString();
   }
}
