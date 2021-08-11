package com.google.android.material.shape;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.internal.ViewUtils;

public class MaterialShapeUtils {
   private MaterialShapeUtils() {
   }

   static CornerTreatment createCornerTreatment(int var0) {
      if (var0 != 0) {
         return (CornerTreatment)(var0 != 1 ? createDefaultCornerTreatment() : new CutCornerTreatment());
      } else {
         return new RoundedCornerTreatment();
      }
   }

   static CornerTreatment createDefaultCornerTreatment() {
      return new RoundedCornerTreatment();
   }

   static EdgeTreatment createDefaultEdgeTreatment() {
      return new EdgeTreatment();
   }

   public static void setElevation(View var0, float var1) {
      Drawable var2 = var0.getBackground();
      if (var2 instanceof MaterialShapeDrawable) {
         ((MaterialShapeDrawable)var2).setElevation(var1);
      }

   }

   public static void setParentAbsoluteElevation(View var0) {
      Drawable var1 = var0.getBackground();
      if (var1 instanceof MaterialShapeDrawable) {
         setParentAbsoluteElevation(var0, (MaterialShapeDrawable)var1);
      }

   }

   public static void setParentAbsoluteElevation(View var0, MaterialShapeDrawable var1) {
      if (var1.isElevationOverlayEnabled()) {
         var1.setParentAbsoluteElevation(ViewUtils.getParentAbsoluteElevation(var0));
      }

   }
}
