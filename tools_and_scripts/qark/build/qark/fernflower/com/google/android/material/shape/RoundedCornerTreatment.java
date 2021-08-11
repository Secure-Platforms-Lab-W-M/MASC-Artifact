package com.google.android.material.shape;

public class RoundedCornerTreatment extends CornerTreatment {
   float radius = -1.0F;

   public RoundedCornerTreatment() {
   }

   @Deprecated
   public RoundedCornerTreatment(float var1) {
      this.radius = var1;
   }

   public void getCornerPath(ShapePath var1, float var2, float var3, float var4) {
      var1.reset(0.0F, var4 * var3, 180.0F, 180.0F - var2);
      var1.addArc(0.0F, 0.0F, var4 * 2.0F * var3, 2.0F * var4 * var3, 180.0F, var2);
   }
}
