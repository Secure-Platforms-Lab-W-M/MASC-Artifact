package com.google.android.material.shape;

import android.graphics.RectF;
import java.util.Arrays;

public final class AbsoluteCornerSize implements CornerSize {
   private final float size;

   public AbsoluteCornerSize(float var1) {
      this.size = var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof AbsoluteCornerSize)) {
         return false;
      } else {
         AbsoluteCornerSize var2 = (AbsoluteCornerSize)var1;
         return this.size == var2.size;
      }
   }

   public float getCornerSize() {
      return this.size;
   }

   public float getCornerSize(RectF var1) {
      return this.size;
   }

   public int hashCode() {
      return Arrays.hashCode(new Object[]{this.size});
   }
}
