package javax.media.format;

import javax.media.Format;
import net.sf.fmj.codegen.FormatTraceUtils;
import net.sf.fmj.utility.FormatUtils;
import org.atalk.android.util.java.awt.Dimension;

public class IndexedColorFormat extends VideoFormat {
   private static String ENCODING = "irgb";
   protected byte[] blueValues;
   protected byte[] greenValues;
   protected int lineStride;
   protected int mapSize;
   protected byte[] redValues;

   public IndexedColorFormat(Dimension var1, int var2, Class var3, float var4, int var5, int var6, byte[] var7, byte[] var8, byte[] var9) {
      super(ENCODING, var1, var2, var3, var4);
      this.lineStride = var5;
      this.mapSize = var6;
      this.redValues = var7;
      this.greenValues = var8;
      this.blueValues = var9;
   }

   public Object clone() {
      return new IndexedColorFormat(FormatUtils.clone(this.size), this.maxDataLength, this.dataType, this.frameRate, this.lineStride, this.mapSize, this.redValues, this.greenValues, this.blueValues);
   }

   protected void copy(Format var1) {
      super.copy(var1);
      IndexedColorFormat var2 = (IndexedColorFormat)var1;
      this.lineStride = var2.lineStride;
      this.mapSize = var2.mapSize;
      this.redValues = var2.redValues;
      this.greenValues = var2.greenValues;
      this.blueValues = var2.blueValues;
   }

   public boolean equals(Object var1) {
      boolean var2 = super.equals(var1);
      boolean var3 = false;
      if (!var2) {
         return false;
      } else if (!(var1 instanceof IndexedColorFormat)) {
         return false;
      } else {
         IndexedColorFormat var4 = (IndexedColorFormat)var1;
         var2 = var3;
         if (this.lineStride == var4.lineStride) {
            var2 = var3;
            if (this.mapSize == var4.mapSize) {
               var2 = var3;
               if (this.redValues == var4.redValues) {
                  var2 = var3;
                  if (this.greenValues == var4.greenValues) {
                     var2 = var3;
                     if (this.blueValues == var4.blueValues) {
                        var2 = true;
                     }
                  }
               }
            }
         }

         return var2;
      }
   }

   public byte[] getBlueValues() {
      return this.blueValues;
   }

   public byte[] getGreenValues() {
      return this.greenValues;
   }

   public int getLineStride() {
      return this.lineStride;
   }

   public int getMapSize() {
      return this.mapSize;
   }

   public byte[] getRedValues() {
      return this.redValues;
   }

   public Format intersects(Format var1) {
      Format var2 = super.intersects(var1);
      if (var1 instanceof IndexedColorFormat) {
         IndexedColorFormat var3 = (IndexedColorFormat)var2;
         IndexedColorFormat var4 = (IndexedColorFormat)var1;
         if (this.getClass().isAssignableFrom(var1.getClass())) {
            if (FormatUtils.specified(this.lineStride)) {
               var3.lineStride = this.lineStride;
            }

            if (FormatUtils.specified(this.mapSize)) {
               var3.mapSize = this.mapSize;
            }

            if (FormatUtils.specified(this.redValues)) {
               var3.redValues = this.redValues;
            }

            if (FormatUtils.specified(this.greenValues)) {
               var3.greenValues = this.greenValues;
            }

            if (FormatUtils.specified(this.blueValues)) {
               var3.blueValues = this.blueValues;
            }
         } else if (var1.getClass().isAssignableFrom(this.getClass())) {
            if (!FormatUtils.specified(var3.lineStride)) {
               var3.lineStride = var4.lineStride;
            }

            if (!FormatUtils.specified(var3.mapSize)) {
               var3.mapSize = var4.mapSize;
            }

            if (!FormatUtils.specified(var3.redValues)) {
               var3.redValues = var4.redValues;
            }

            if (!FormatUtils.specified(var3.greenValues)) {
               var3.greenValues = var4.greenValues;
            }

            if (!FormatUtils.specified(var3.blueValues)) {
               var3.blueValues = var4.blueValues;
            }
         }
      }

      FormatTraceUtils.traceIntersects(this, var1, var2);
      return var2;
   }

   public boolean matches(Format var1) {
      boolean var3 = super.matches(var1);
      boolean var2 = false;
      if (!var3) {
         FormatTraceUtils.traceMatches(this, var1, false);
         return false;
      } else if (!(var1 instanceof IndexedColorFormat)) {
         FormatTraceUtils.traceMatches(this, var1, true);
         return true;
      } else {
         IndexedColorFormat var4 = (IndexedColorFormat)var1;
         if (FormatUtils.matches(var4.lineStride, this.lineStride) && FormatUtils.matches(var4.mapSize, this.mapSize) && FormatUtils.matches(var4.redValues, this.redValues) && FormatUtils.matches(var4.greenValues, this.greenValues) && FormatUtils.matches(var4.blueValues, this.blueValues)) {
            var2 = true;
         }

         FormatTraceUtils.traceMatches(this, var1, var2);
         return var2;
      }
   }

   public Format relax() {
      IndexedColorFormat var1 = (IndexedColorFormat)super.relax();
      var1.lineStride = -1;
      return var1;
   }
}
