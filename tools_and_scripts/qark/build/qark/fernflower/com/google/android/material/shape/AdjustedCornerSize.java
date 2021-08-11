package com.google.android.material.shape;

import android.graphics.RectF;
import java.util.Arrays;

public final class AdjustedCornerSize implements CornerSize {
   private final float adjustment;
   private final CornerSize other;

   public AdjustedCornerSize(float var1, CornerSize var2) {
      while(var2 instanceof AdjustedCornerSize) {
         var2 = ((AdjustedCornerSize)var2).other;
         var1 += ((AdjustedCornerSize)var2).adjustment;
      }

      this.other = var2;
      this.adjustment = var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof AdjustedCornerSize)) {
         return false;
      } else {
         AdjustedCornerSize var2 = (AdjustedCornerSize)var1;
         return this.other.equals(var2.other) && this.adjustment == var2.adjustment;
      }
   }

   public float getCornerSize(RectF var1) {
      return Math.max(0.0F, this.other.getCornerSize(var1) + this.adjustment);
   }

   public int hashCode() {
      return Arrays.hashCode(new Object[]{this.other, this.adjustment});
   }
}
