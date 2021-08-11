package com.google.android.material.shape;

import android.graphics.RectF;

public class CornerTreatment {
   @Deprecated
   public void getCornerPath(float var1, float var2, ShapePath var3) {
   }

   public void getCornerPath(ShapePath var1, float var2, float var3, float var4) {
      this.getCornerPath(var2, var3, var1);
   }

   public void getCornerPath(ShapePath var1, float var2, float var3, RectF var4, CornerSize var5) {
      this.getCornerPath(var1, var2, var3, var5.getCornerSize(var4));
   }
}
