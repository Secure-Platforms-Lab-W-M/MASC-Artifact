package com.google.android.material.shape;

public class EdgeTreatment {
   public void getEdgePath(float var1, float var2, float var3, ShapePath var4) {
      var4.lineTo(var1, 0.0F);
   }

   @Deprecated
   public void getEdgePath(float var1, float var2, ShapePath var3) {
      this.getEdgePath(var1, var1 / 2.0F, var2, var3);
   }
}
