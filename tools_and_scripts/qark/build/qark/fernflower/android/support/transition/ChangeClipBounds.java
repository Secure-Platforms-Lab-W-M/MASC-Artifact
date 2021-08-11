package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ChangeClipBounds extends Transition {
   private static final String PROPNAME_BOUNDS = "android:clipBounds:bounds";
   private static final String PROPNAME_CLIP = "android:clipBounds:clip";
   private static final String[] sTransitionProperties = new String[]{"android:clipBounds:clip"};

   public ChangeClipBounds() {
   }

   public ChangeClipBounds(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void captureValues(TransitionValues var1) {
      View var2 = var1.view;
      if (var2.getVisibility() != 8) {
         Rect var3 = ViewCompat.getClipBounds(var2);
         var1.values.put("android:clipBounds:clip", var3);
         if (var3 == null) {
            Rect var4 = new Rect(0, 0, var2.getWidth(), var2.getHeight());
            var1.values.put("android:clipBounds:bounds", var4);
         }
      }
   }

   public void captureEndValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(@NonNull ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      if (var2 != null && var3 != null) {
         if (var2.values.containsKey("android:clipBounds:clip")) {
            if (!var3.values.containsKey("android:clipBounds:clip")) {
               return null;
            } else {
               Rect var6 = (Rect)var2.values.get("android:clipBounds:clip");
               Rect var5 = (Rect)var3.values.get("android:clipBounds:clip");
               boolean var4;
               if (var5 == null) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               if (var6 == null && var5 == null) {
                  return null;
               } else {
                  Rect var7;
                  if (var6 == null) {
                     var6 = (Rect)var2.values.get("android:clipBounds:bounds");
                     var7 = var5;
                  } else if (var5 == null) {
                     var7 = (Rect)var3.values.get("android:clipBounds:bounds");
                  } else {
                     var7 = var5;
                  }

                  if (var6.equals(var7)) {
                     return null;
                  } else {
                     ViewCompat.setClipBounds(var3.view, var6);
                     RectEvaluator var9 = new RectEvaluator(new Rect());
                     ObjectAnimator var8 = ObjectAnimator.ofObject(var3.view, ViewUtils.CLIP_BOUNDS, var9, new Rect[]{var6, var7});
                     if (var4) {
                        var8.addListener(new AnimatorListenerAdapter(var3.view) {
                           // $FF: synthetic field
                           final View val$endView;

                           {
                              this.val$endView = var2;
                           }

                           public void onAnimationEnd(Animator var1) {
                              ViewCompat.setClipBounds(this.val$endView, (Rect)null);
                           }
                        });
                        return var8;
                     } else {
                        return var8;
                     }
                  }
               }
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public String[] getTransitionProperties() {
      return sTransitionProperties;
   }
}
