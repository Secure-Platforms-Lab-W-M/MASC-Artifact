package androidx.core.graphics;

import android.graphics.PointF;
import androidx.core.util.Preconditions;

public final class PathSegment {
   private final PointF mEnd;
   private final float mEndFraction;
   private final PointF mStart;
   private final float mStartFraction;

   public PathSegment(PointF var1, float var2, PointF var3, float var4) {
      this.mStart = (PointF)Preconditions.checkNotNull(var1, "start == null");
      this.mStartFraction = var2;
      this.mEnd = (PointF)Preconditions.checkNotNull(var3, "end == null");
      this.mEndFraction = var4;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof PathSegment)) {
         return false;
      } else {
         PathSegment var2 = (PathSegment)var1;
         return Float.compare(this.mStartFraction, var2.mStartFraction) == 0 && Float.compare(this.mEndFraction, var2.mEndFraction) == 0 && this.mStart.equals(var2.mStart) && this.mEnd.equals(var2.mEnd);
      }
   }

   public PointF getEnd() {
      return this.mEnd;
   }

   public float getEndFraction() {
      return this.mEndFraction;
   }

   public PointF getStart() {
      return this.mStart;
   }

   public float getStartFraction() {
      return this.mStartFraction;
   }

   public int hashCode() {
      int var4 = this.mStart.hashCode();
      float var1 = this.mStartFraction;
      int var3 = 0;
      int var2;
      if (var1 != 0.0F) {
         var2 = Float.floatToIntBits(var1);
      } else {
         var2 = 0;
      }

      int var5 = this.mEnd.hashCode();
      var1 = this.mEndFraction;
      if (var1 != 0.0F) {
         var3 = Float.floatToIntBits(var1);
      }

      return ((var4 * 31 + var2) * 31 + var5) * 31 + var3;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("PathSegment{start=");
      var1.append(this.mStart);
      var1.append(", startFraction=");
      var1.append(this.mStartFraction);
      var1.append(", end=");
      var1.append(this.mEnd);
      var1.append(", endFraction=");
      var1.append(this.mEndFraction);
      var1.append('}');
      return var1.toString();
   }
}
