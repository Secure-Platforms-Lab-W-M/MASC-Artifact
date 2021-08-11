package net.sf.fmj.media.format;

import javax.media.Format;
import javax.media.format.VideoFormat;
import net.sf.fmj.codegen.FormatTraceUtils;
import net.sf.fmj.utility.FormatUtils;
import org.atalk.android.util.java.awt.Dimension;

public class GIFFormat extends VideoFormat {
   public GIFFormat() {
      super("gif");
      this.dataType = Format.byteArray;
   }

   public GIFFormat(Dimension var1, int var2, Class var3, float var4) {
      super("gif", var1, var2, var3, var4);
   }

   public Object clone() {
      return new GIFFormat(FormatUtils.clone(this.size), this.maxDataLength, this.dataType, this.frameRate);
   }

   protected void copy(Format var1) {
      super.copy(var1);
      GIFFormat var2 = (GIFFormat)var1;
   }

   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else if (!(var1 instanceof GIFFormat)) {
         return false;
      } else {
         GIFFormat var2 = (GIFFormat)var1;
         return true;
      }
   }

   public Format intersects(Format var1) {
      Format var2 = super.intersects(var1);
      if (var1 instanceof GIFFormat) {
         GIFFormat var3 = (GIFFormat)var2;
         var3 = (GIFFormat)var1;
         if (!this.getClass().isAssignableFrom(var1.getClass())) {
            var1.getClass().isAssignableFrom(this.getClass());
         }
      }

      FormatTraceUtils.traceIntersects(this, var1, var2);
      return var2;
   }

   public boolean matches(Format var1) {
      if (!super.matches(var1)) {
         FormatTraceUtils.traceMatches(this, var1, false);
         return false;
      } else if (!(var1 instanceof GIFFormat)) {
         FormatTraceUtils.traceMatches(this, var1, true);
         return true;
      } else {
         GIFFormat var2 = (GIFFormat)var1;
         FormatTraceUtils.traceMatches(this, var1, true);
         return true;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("GIF video format:");
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

      return var1.toString();
   }
}
