package android.support.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ChangeScroll extends Transition {
   private static final String[] PROPERTIES = new String[]{"android:changeScroll:x", "android:changeScroll:y"};
   private static final String PROPNAME_SCROLL_X = "android:changeScroll:x";
   private static final String PROPNAME_SCROLL_Y = "android:changeScroll:y";

   public ChangeScroll() {
   }

   public ChangeScroll(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void captureValues(TransitionValues var1) {
      var1.values.put("android:changeScroll:x", var1.view.getScrollX());
      var1.values.put("android:changeScroll:y", var1.view.getScrollY());
   }

   public void captureEndValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   @Nullable
   public Animator createAnimator(@NonNull ViewGroup var1, @Nullable TransitionValues var2, @Nullable TransitionValues var3) {
      if (var2 != null && var3 != null) {
         View var8 = var3.view;
         int var4 = (Integer)var2.values.get("android:changeScroll:x");
         int var5 = (Integer)var3.values.get("android:changeScroll:x");
         int var6 = (Integer)var2.values.get("android:changeScroll:y");
         int var7 = (Integer)var3.values.get("android:changeScroll:y");
         ObjectAnimator var9 = null;
         ObjectAnimator var10 = null;
         if (var4 != var5) {
            var8.setScrollX(var4);
            var9 = ObjectAnimator.ofInt(var8, "scrollX", new int[]{var4, var5});
         }

         if (var6 != var7) {
            var8.setScrollY(var6);
            var10 = ObjectAnimator.ofInt(var8, "scrollY", new int[]{var6, var7});
         }

         return TransitionUtils.mergeAnimators(var9, var10);
      } else {
         return null;
      }
   }

   @Nullable
   public String[] getTransitionProperties() {
      return PROPERTIES;
   }
}
