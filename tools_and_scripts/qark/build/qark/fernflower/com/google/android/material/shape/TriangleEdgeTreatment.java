package com.google.android.material.shape;

public class TriangleEdgeTreatment extends EdgeTreatment {
   private final boolean inside;
   private final float size;

   public TriangleEdgeTreatment(float var1, boolean var2) {
      this.size = var1;
      this.inside = var2;
   }

   public void getEdgePath(float var1, float var2, float var3, ShapePath var4) {
      var4.lineTo(var2 - this.size * var3, 0.0F);
      float var5;
      if (this.inside) {
         var5 = this.size;
      } else {
         var5 = -this.size;
      }

      var4.lineTo(var2, var5 * var3);
      var4.lineTo(this.size * var3 + var2, 0.0F);
      var4.lineTo(var1, 0.0F);
   }
}
