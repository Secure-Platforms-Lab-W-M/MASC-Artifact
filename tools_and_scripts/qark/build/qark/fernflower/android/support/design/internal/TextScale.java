package android.support.design.internal;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Map;

@RequiresApi(14)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TextScale extends Transition {
   private static final String PROPNAME_SCALE = "android:textscale:scale";

   private void captureValues(TransitionValues var1) {
      if (var1.view instanceof TextView) {
         TextView var2 = (TextView)var1.view;
         var1.values.put("android:textscale:scale", var2.getScaleX());
      }
   }

   public void captureEndValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      if (var2 != null && var3 != null && var2.view instanceof TextView) {
         if (!(var3.view instanceof TextView)) {
            return null;
         } else {
            final TextView var7 = (TextView)var3.view;
            Map var8 = var2.values;
            Map var10 = var3.values;
            Object var6 = var8.get("android:textscale:scale");
            float var5 = 1.0F;
            float var4;
            if (var6 != null) {
               var4 = (Float)var8.get("android:textscale:scale");
            } else {
               var4 = 1.0F;
            }

            if (var10.get("android:textscale:scale") != null) {
               var5 = (Float)var10.get("android:textscale:scale");
            }

            if (var4 == var5) {
               return null;
            } else {
               ValueAnimator var9 = ValueAnimator.ofFloat(new float[]{var4, var5});
               var9.addUpdateListener(new AnimatorUpdateListener() {
                  public void onAnimationUpdate(ValueAnimator var1) {
                     float var2 = (Float)var1.getAnimatedValue();
                     var7.setScaleX(var2);
                     var7.setScaleY(var2);
                  }
               });
               return var9;
            }
         }
      } else {
         return null;
      }
   }
}
