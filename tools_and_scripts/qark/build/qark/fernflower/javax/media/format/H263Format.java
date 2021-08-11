package javax.media.format;

import javax.media.Format;
import net.sf.fmj.codegen.FormatTraceUtils;
import net.sf.fmj.utility.FormatUtils;
import org.atalk.android.util.java.awt.Dimension;

public class H263Format extends VideoFormat {
   private static String ENCODING = "h263";
   protected int advancedPrediction = -1;
   protected int arithmeticCoding = -1;
   protected int errorCompensation = -1;
   protected int hrDB = -1;
   protected int pbFrames = -1;
   protected int unrestrictedVector = -1;

   public H263Format() {
      super(ENCODING);
      this.dataType = Format.byteArray;
   }

   public H263Format(Dimension var1, int var2, Class var3, float var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      super(ENCODING, var1, var2, var3, var4);
      this.advancedPrediction = var5;
      this.arithmeticCoding = var6;
      this.errorCompensation = var7;
      this.hrDB = var8;
      this.pbFrames = var9;
      this.unrestrictedVector = var10;
   }

   public Object clone() {
      return new H263Format(FormatUtils.clone(this.size), this.maxDataLength, this.dataType, this.frameRate, this.advancedPrediction, this.arithmeticCoding, this.errorCompensation, this.hrDB, this.pbFrames, this.unrestrictedVector);
   }

   protected void copy(Format var1) {
      super.copy(var1);
      H263Format var2 = (H263Format)var1;
      this.advancedPrediction = var2.advancedPrediction;
      this.arithmeticCoding = var2.arithmeticCoding;
      this.errorCompensation = var2.errorCompensation;
      this.hrDB = var2.hrDB;
      this.pbFrames = var2.pbFrames;
      this.unrestrictedVector = var2.unrestrictedVector;
   }

   public boolean equals(Object var1) {
      boolean var2 = super.equals(var1);
      boolean var3 = false;
      if (!var2) {
         return false;
      } else if (!(var1 instanceof H263Format)) {
         return false;
      } else {
         H263Format var4 = (H263Format)var1;
         var2 = var3;
         if (this.advancedPrediction == var4.advancedPrediction) {
            var2 = var3;
            if (this.arithmeticCoding == var4.arithmeticCoding) {
               var2 = var3;
               if (this.errorCompensation == var4.errorCompensation) {
                  var2 = var3;
                  if (this.hrDB == var4.hrDB) {
                     var2 = var3;
                     if (this.pbFrames == var4.pbFrames) {
                        var2 = var3;
                        if (this.unrestrictedVector == var4.unrestrictedVector) {
                           var2 = true;
                        }
                     }
                  }
               }
            }
         }

         return var2;
      }
   }

   public int getAdvancedPrediction() {
      return this.advancedPrediction;
   }

   public int getArithmeticCoding() {
      return this.arithmeticCoding;
   }

   public int getErrorCompensation() {
      return this.errorCompensation;
   }

   public int getHrDB() {
      return this.hrDB;
   }

   public int getPBFrames() {
      return this.pbFrames;
   }

   public int getUnrestrictedVector() {
      return this.unrestrictedVector;
   }

   public Format intersects(Format var1) {
      Format var2 = super.intersects(var1);
      if (var1 instanceof H263Format) {
         H263Format var3 = (H263Format)var2;
         H263Format var4 = (H263Format)var1;
         if (this.getClass().isAssignableFrom(var1.getClass())) {
            if (FormatUtils.specified(this.advancedPrediction)) {
               var3.advancedPrediction = this.advancedPrediction;
            }

            if (FormatUtils.specified(this.arithmeticCoding)) {
               var3.arithmeticCoding = this.arithmeticCoding;
            }

            if (FormatUtils.specified(this.errorCompensation)) {
               var3.errorCompensation = this.errorCompensation;
            }

            if (FormatUtils.specified(this.hrDB)) {
               var3.hrDB = this.hrDB;
            }

            if (FormatUtils.specified(this.pbFrames)) {
               var3.pbFrames = this.pbFrames;
            }

            if (FormatUtils.specified(this.unrestrictedVector)) {
               var3.unrestrictedVector = this.unrestrictedVector;
            }
         } else if (var1.getClass().isAssignableFrom(this.getClass())) {
            if (!FormatUtils.specified(var3.advancedPrediction)) {
               var3.advancedPrediction = var4.advancedPrediction;
            }

            if (!FormatUtils.specified(var3.arithmeticCoding)) {
               var3.arithmeticCoding = var4.arithmeticCoding;
            }

            if (!FormatUtils.specified(var3.errorCompensation)) {
               var3.errorCompensation = var4.errorCompensation;
            }

            if (!FormatUtils.specified(var3.hrDB)) {
               var3.hrDB = var4.hrDB;
            }

            if (!FormatUtils.specified(var3.pbFrames)) {
               var3.pbFrames = var4.pbFrames;
            }

            if (!FormatUtils.specified(var3.unrestrictedVector)) {
               var3.unrestrictedVector = var4.unrestrictedVector;
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
      } else if (!(var1 instanceof H263Format)) {
         FormatTraceUtils.traceMatches(this, var1, true);
         return true;
      } else {
         H263Format var4 = (H263Format)var1;
         if (FormatUtils.matches(this.advancedPrediction, var4.advancedPrediction) && FormatUtils.matches(this.arithmeticCoding, var4.arithmeticCoding) && FormatUtils.matches(this.errorCompensation, var4.errorCompensation) && FormatUtils.matches(this.hrDB, var4.hrDB) && FormatUtils.matches(this.pbFrames, var4.pbFrames) && FormatUtils.matches(this.unrestrictedVector, var4.unrestrictedVector)) {
            var2 = true;
         }

         FormatTraceUtils.traceMatches(this, var1, var2);
         return var2;
      }
   }

   public String toString() {
      return "H.263 video format";
   }
}
