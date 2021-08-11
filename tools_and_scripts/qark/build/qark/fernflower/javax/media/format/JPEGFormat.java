package javax.media.format;

import javax.media.Format;
import net.sf.fmj.codegen.FormatTraceUtils;
import net.sf.fmj.utility.FormatUtils;
import org.atalk.android.util.java.awt.Dimension;

public class JPEGFormat extends VideoFormat {
   public static final int DEC_402 = 3;
   public static final int DEC_411 = 4;
   public static final int DEC_420 = 1;
   public static final int DEC_422 = 0;
   public static final int DEC_444 = 2;
   int decimation = -1;
   int qFactor = -1;

   public JPEGFormat() {
      super("jpeg");
      this.dataType = Format.byteArray;
   }

   public JPEGFormat(Dimension var1, int var2, Class var3, float var4, int var5, int var6) {
      super("jpeg", var1, var2, var3, var4);
      this.qFactor = var5;
      this.decimation = var6;
   }

   public Object clone() {
      return new JPEGFormat(FormatUtils.clone(this.size), this.maxDataLength, this.dataType, this.frameRate, this.qFactor, this.decimation);
   }

   protected void copy(Format var1) {
      super.copy(var1);
      JPEGFormat var2 = (JPEGFormat)var1;
      this.qFactor = var2.qFactor;
      this.decimation = var2.decimation;
   }

   public boolean equals(Object var1) {
      boolean var2 = super.equals(var1);
      boolean var3 = false;
      if (!var2) {
         return false;
      } else if (!(var1 instanceof JPEGFormat)) {
         return false;
      } else {
         JPEGFormat var4 = (JPEGFormat)var1;
         var2 = var3;
         if (this.qFactor == var4.qFactor) {
            var2 = var3;
            if (this.decimation == var4.decimation) {
               var2 = true;
            }
         }

         return var2;
      }
   }

   public int getDecimation() {
      return this.decimation;
   }

   public int getQFactor() {
      return this.qFactor;
   }

   public Format intersects(Format var1) {
      Format var2 = super.intersects(var1);
      if (var1 instanceof JPEGFormat) {
         JPEGFormat var3 = (JPEGFormat)var2;
         JPEGFormat var4 = (JPEGFormat)var1;
         if (this.getClass().isAssignableFrom(var1.getClass())) {
            if (FormatUtils.specified(this.qFactor)) {
               var3.qFactor = this.qFactor;
            }

            if (FormatUtils.specified(this.decimation)) {
               var3.decimation = this.decimation;
            }
         } else if (var1.getClass().isAssignableFrom(this.getClass())) {
            if (!FormatUtils.specified(var3.qFactor)) {
               var3.qFactor = var4.qFactor;
            }

            if (!FormatUtils.specified(var3.decimation)) {
               var3.decimation = var4.decimation;
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
      } else if (!(var1 instanceof JPEGFormat)) {
         FormatTraceUtils.traceMatches(this, var1, true);
         return true;
      } else {
         JPEGFormat var4 = (JPEGFormat)var1;
         if (FormatUtils.matches(var4.qFactor, this.qFactor) && FormatUtils.matches(var4.decimation, this.decimation)) {
            var2 = true;
         }

         FormatTraceUtils.traceMatches(this, var1, var2);
         return var2;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("jpeg video format:");
      StringBuilder var2;
      if (FormatUtils.specified(this.size)) {
         var2 = new StringBuilder();
         var2.append(" size = ");
         var2.append(this.size.width);
         var2.append("x");
         var2.append(this.size.height);
         var1.append(var2.toString());
      }

      if (FormatUtils.specified(this.frameRate)) {
         var2 = new StringBuilder();
         var2.append(" FrameRate = ");
         var2.append(this.frameRate);
         var1.append(var2.toString());
      }

      if (FormatUtils.specified(this.maxDataLength)) {
         var2 = new StringBuilder();
         var2.append(" maxDataLength = ");
         var2.append(this.maxDataLength);
         var1.append(var2.toString());
      }

      if (FormatUtils.specified(this.dataType)) {
         var2 = new StringBuilder();
         var2.append(" dataType = ");
         var2.append(this.dataType);
         var1.append(var2.toString());
      }

      if (FormatUtils.specified(this.qFactor)) {
         var2 = new StringBuilder();
         var2.append(" q factor = ");
         var2.append(this.qFactor);
         var1.append(var2.toString());
      }

      if (FormatUtils.specified(this.decimation)) {
         var2 = new StringBuilder();
         var2.append(" decimation = ");
         var2.append(this.decimation);
         var1.append(var2.toString());
      }

      return var1.toString();
   }
}
