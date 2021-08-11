package androidx.transition;

import android.view.View;

public abstract class VisibilityPropagation extends TransitionPropagation {
   private static final String PROPNAME_VIEW_CENTER = "android:visibilityPropagation:center";
   private static final String PROPNAME_VISIBILITY = "android:visibilityPropagation:visibility";
   private static final String[] VISIBILITY_PROPAGATION_VALUES = new String[]{"android:visibilityPropagation:visibility", "android:visibilityPropagation:center"};

   private static int getViewCoordinate(TransitionValues var0, int var1) {
      if (var0 == null) {
         return -1;
      } else {
         int[] var2 = (int[])((int[])var0.values.get("android:visibilityPropagation:center"));
         return var2 == null ? -1 : var2[var1];
      }
   }

   public void captureValues(TransitionValues var1) {
      View var4 = var1.view;
      Integer var3 = (Integer)var1.values.get("android:visibility:visibility");
      Integer var2 = var3;
      if (var3 == null) {
         var2 = var4.getVisibility();
      }

      var1.values.put("android:visibilityPropagation:visibility", var2);
      int[] var5 = new int[2];
      var4.getLocationOnScreen(var5);
      var5[0] += Math.round(var4.getTranslationX());
      var5[0] += var4.getWidth() / 2;
      var5[1] += Math.round(var4.getTranslationY());
      var5[1] += var4.getHeight() / 2;
      var1.values.put("android:visibilityPropagation:center", var5);
   }

   public String[] getPropagationProperties() {
      return VISIBILITY_PROPAGATION_VALUES;
   }

   public int getViewVisibility(TransitionValues var1) {
      if (var1 == null) {
         return 8;
      } else {
         Integer var2 = (Integer)var1.values.get("android:visibilityPropagation:visibility");
         return var2 == null ? 8 : var2;
      }
   }

   public int getViewX(TransitionValues var1) {
      return getViewCoordinate(var1, 0);
   }

   public int getViewY(TransitionValues var1) {
      return getViewCoordinate(var1, 1);
   }
}
