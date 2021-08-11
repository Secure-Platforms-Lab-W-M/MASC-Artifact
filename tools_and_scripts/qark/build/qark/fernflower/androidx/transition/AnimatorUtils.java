package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator.AnimatorListener;
import android.os.Build.VERSION;
import java.util.ArrayList;

class AnimatorUtils {
   private AnimatorUtils() {
   }

   static void addPauseListener(Animator var0, AnimatorListenerAdapter var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.addPauseListener(var1);
      }

   }

   static void pause(Animator var0) {
      if (VERSION.SDK_INT >= 19) {
         var0.pause();
      } else {
         ArrayList var3 = var0.getListeners();
         if (var3 != null) {
            int var1 = 0;

            for(int var2 = var3.size(); var1 < var2; ++var1) {
               AnimatorListener var4 = (AnimatorListener)var3.get(var1);
               if (var4 instanceof AnimatorUtils.AnimatorPauseListenerCompat) {
                  ((AnimatorUtils.AnimatorPauseListenerCompat)var4).onAnimationPause(var0);
               }
            }
         }

      }
   }

   static void resume(Animator var0) {
      if (VERSION.SDK_INT >= 19) {
         var0.resume();
      } else {
         ArrayList var3 = var0.getListeners();
         if (var3 != null) {
            int var1 = 0;

            for(int var2 = var3.size(); var1 < var2; ++var1) {
               AnimatorListener var4 = (AnimatorListener)var3.get(var1);
               if (var4 instanceof AnimatorUtils.AnimatorPauseListenerCompat) {
                  ((AnimatorUtils.AnimatorPauseListenerCompat)var4).onAnimationResume(var0);
               }
            }
         }

      }
   }

   interface AnimatorPauseListenerCompat {
      void onAnimationPause(Animator var1);

      void onAnimationResume(Animator var1);
   }
}
