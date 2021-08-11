package com.google.android.material.bottomappbar;

import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.ShapePath;

public class BottomAppBarTopEdgeTreatment extends EdgeTreatment implements Cloneable {
   private static final int ANGLE_LEFT = 180;
   private static final int ANGLE_UP = 270;
   private static final int ARC_HALF = 180;
   private static final int ARC_QUARTER = 90;
   private float cradleVerticalOffset;
   private float fabDiameter;
   private float fabMargin;
   private float horizontalOffset;
   private float roundedCornerRadius;

   public BottomAppBarTopEdgeTreatment(float var1, float var2, float var3) {
      this.fabMargin = var1;
      this.roundedCornerRadius = var2;
      this.cradleVerticalOffset = var3;
      if (var3 >= 0.0F) {
         this.horizontalOffset = 0.0F;
      } else {
         throw new IllegalArgumentException("cradleVerticalOffset must be positive.");
      }
   }

   float getCradleVerticalOffset() {
      return this.cradleVerticalOffset;
   }

   public void getEdgePath(float var1, float var2, float var3, ShapePath var4) {
      float var5 = this.fabDiameter;
      if (var5 == 0.0F) {
         var4.lineTo(var1, 0.0F);
      } else {
         var5 = (this.fabMargin * 2.0F + var5) / 2.0F;
         float var6 = var3 * this.roundedCornerRadius;
         var2 += this.horizontalOffset;
         var3 = this.cradleVerticalOffset * var3 + (1.0F - var3) * var5;
         if (var3 / var5 >= 1.0F) {
            var4.lineTo(var1, 0.0F);
         } else {
            float var7 = var5 + var6;
            float var9 = var3 + var6;
            float var10 = (float)Math.sqrt((double)(var7 * var7 - var9 * var9));
            var7 = var2 - var10;
            float var8 = var2 + var10;
            var9 = (float)Math.toDegrees(Math.atan((double)(var10 / var9)));
            var10 = 90.0F - var9;
            var4.lineTo(var7, 0.0F);
            var4.addArc(var7 - var6, 0.0F, var7 + var6, var6 * 2.0F, 270.0F, var9);
            var4.addArc(var2 - var5, -var5 - var3, var2 + var5, var5 - var3, 180.0F - var10, var10 * 2.0F - 180.0F);
            var4.addArc(var8 - var6, 0.0F, var8 + var6, var6 * 2.0F, 270.0F - var9, var9);
            var4.lineTo(var1, 0.0F);
         }
      }
   }

   float getFabCradleMargin() {
      return this.fabMargin;
   }

   float getFabCradleRoundedCornerRadius() {
      return this.roundedCornerRadius;
   }

   public float getFabDiameter() {
      return this.fabDiameter;
   }

   public float getHorizontalOffset() {
      return this.horizontalOffset;
   }

   void setCradleVerticalOffset(float var1) {
      this.cradleVerticalOffset = var1;
   }

   void setFabCradleMargin(float var1) {
      this.fabMargin = var1;
   }

   void setFabCradleRoundedCornerRadius(float var1) {
      this.roundedCornerRadius = var1;
   }

   public void setFabDiameter(float var1) {
      this.fabDiameter = var1;
   }

   void setHorizontalOffset(float var1) {
      this.horizontalOffset = var1;
   }
}
