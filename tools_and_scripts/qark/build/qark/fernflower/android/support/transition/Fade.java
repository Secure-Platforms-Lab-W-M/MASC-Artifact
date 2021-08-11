package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class Fade extends Visibility {
   // $FF: renamed from: IN int
   public static final int field_12 = 1;
   private static final String LOG_TAG = "Fade";
   public static final int OUT = 2;
   private static final String PROPNAME_TRANSITION_ALPHA = "android:fade:transitionAlpha";

   public Fade() {
   }

   public Fade(int var1) {
      this.setMode(var1);
   }

   public Fade(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, Styleable.FADE);
      this.setMode(TypedArrayUtils.getNamedInt(var3, (XmlResourceParser)var2, "fadingMode", 0, this.getMode()));
      var3.recycle();
   }

   private Animator createAnimation(final View var1, float var2, float var3) {
      if (var2 == var3) {
         return null;
      } else {
         ViewUtils.setTransitionAlpha(var1, var2);
         ObjectAnimator var4 = ObjectAnimator.ofFloat(var1, ViewUtils.TRANSITION_ALPHA, new float[]{var3});
         var4.addListener(new Fade.FadeAnimatorListener(var1));
         this.addListener(new TransitionListenerAdapter() {
            public void onTransitionEnd(@NonNull Transition var1x) {
               ViewUtils.setTransitionAlpha(var1, 1.0F);
               ViewUtils.clearNonTransitionAlpha(var1);
               var1x.removeListener(this);
            }
         });
         return var4;
      }
   }

   private static float getStartAlpha(TransitionValues var0, float var1) {
      if (var0 != null) {
         Float var2 = (Float)var0.values.get("android:fade:transitionAlpha");
         return var2 != null ? var2 : var1;
      } else {
         return var1;
      }
   }

   public void captureStartValues(@NonNull TransitionValues var1) {
      super.captureStartValues(var1);
      var1.values.put("android:fade:transitionAlpha", ViewUtils.getTransitionAlpha(var1.view));
   }

   public Animator onAppear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      float var5 = getStartAlpha(var3, 0.0F);
      if (var5 == 1.0F) {
         var5 = 0.0F;
      }

      return this.createAnimation(var2, var5, 1.0F);
   }

   public Animator onDisappear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      ViewUtils.saveNonTransitionAlpha(var2);
      return this.createAnimation(var2, getStartAlpha(var3, 1.0F), 0.0F);
   }

   private static class FadeAnimatorListener extends AnimatorListenerAdapter {
      private boolean mLayerTypeChanged = false;
      private final View mView;

      FadeAnimatorListener(View var1) {
         this.mView = var1;
      }

      public void onAnimationEnd(Animator var1) {
         ViewUtils.setTransitionAlpha(this.mView, 1.0F);
         if (this.mLayerTypeChanged) {
            this.mView.setLayerType(0, (Paint)null);
         }
      }

      public void onAnimationStart(Animator var1) {
         if (ViewCompat.hasOverlappingRendering(this.mView)) {
            if (this.mView.getLayerType() == 0) {
               this.mLayerTypeChanged = true;
               this.mView.setLayerType(2, (Paint)null);
            }
         }
      }
   }
}
