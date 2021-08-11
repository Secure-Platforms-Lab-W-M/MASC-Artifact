package javax.media.format;

import javax.media.Format;
import net.sf.fmj.codegen.FormatTraceUtils;
import net.sf.fmj.utility.FormatUtils;
import org.atalk.android.util.java.awt.Dimension;

public class H261Format extends VideoFormat {
   private static String ENCODING = "h261";
   protected int stillImageTransmission = -1;

   public H261Format() {
      super(ENCODING);
      this.dataType = Format.byteArray;
   }

   public H261Format(Dimension var1, int var2, Class var3, float var4, int var5) {
      super(ENCODING, var1, var2, var3, var4);
      this.stillImageTransmission = var5;
   }

   public Object clone() {
      return new H261Format(FormatUtils.clone(this.size), this.maxDataLength, this.dataType, this.frameRate, this.stillImageTransmission);
   }

   protected void copy(Format var1) {
      super.copy(var1);
      this.stillImageTransmission = ((H261Format)var1).stillImageTransmission;
   }

   public boolean equals(Object var1) {
      boolean var3 = super.equals(var1);
      boolean var2 = false;
      if (!var3) {
         return false;
      } else if (!(var1 instanceof H261Format)) {
         return false;
      } else {
         H261Format var4 = (H261Format)var1;
         if (this.stillImageTransmission == var4.stillImageTransmission) {
            var2 = true;
         }

         return var2;
      }
   }

   public int getStillImageTransmission() {
      return this.stillImageTransmission;
   }

   public Format intersects(Format var1) {
      Format var2 = super.intersects(var1);
      if (var1 instanceof H261Format) {
         H261Format var3 = (H261Format)var2;
         H261Format var4 = (H261Format)var1;
         if (this.getClass().isAssignableFrom(var1.getClass())) {
            if (FormatUtils.specified(this.stillImageTransmission)) {
               var3.stillImageTransmission = this.stillImageTransmission;
            }
         } else if (var1.getClass().isAssignableFrom(this.getClass()) && !FormatUtils.specified(var3.stillImageTransmission)) {
            var3.stillImageTransmission = var4.stillImageTransmission;
         }
      }

      FormatTraceUtils.traceIntersects(this, var1, var2);
      return var2;
   }

   public boolean matches(Format var1) {
      if (!super.matches(var1)) {
         FormatTraceUtils.traceMatches(this, var1, false);
         return false;
      } else if (!(var1 instanceof H261Format)) {
         FormatTraceUtils.traceMatches(this, var1, true);
         return true;
      } else {
         H261Format var3 = (H261Format)var1;
         boolean var2 = FormatUtils.matches(this.stillImageTransmission, var3.stillImageTransmission);
         FormatTraceUtils.traceMatches(this, var1, var2);
         return var2;
      }
   }

   public String toString() {
      return "H.261 video format";
   }
}
