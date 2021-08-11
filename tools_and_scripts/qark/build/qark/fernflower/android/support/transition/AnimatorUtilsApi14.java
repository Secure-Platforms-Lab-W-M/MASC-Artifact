package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator.AnimatorListener;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;

@RequiresApi(14)
class AnimatorUtilsApi14 implements AnimatorUtilsImpl {
   public void addPauseListener(@NonNull Animator var1, @NonNull AnimatorListenerAdapter var2) {
   }

   public void pause(@NonNull Animator var1) {
      ArrayList var4 = var1.getListeners();
      if (var4 != null) {
         int var2 = 0;

         for(int var3 = var4.size(); var2 < var3; ++var2) {
            AnimatorListener var5 = (AnimatorListener)var4.get(var2);
            if (var5 instanceof AnimatorUtilsApi14.AnimatorPauseListenerCompat) {
               ((AnimatorUtilsApi14.AnimatorPauseListenerCompat)var5).onAnimationPause(var1);
            }
         }

      }
   }

   public void resume(@NonNull Animator var1) {
      ArrayList var4 = var1.getListeners();
      if (var4 != null) {
         int var2 = 0;

         for(int var3 = var4.size(); var2 < var3; ++var2) {
            AnimatorListener var5 = (AnimatorListener)var4.get(var2);
            if (var5 instanceof AnimatorUtilsApi14.AnimatorPauseListenerCompat) {
               ((AnimatorUtilsApi14.AnimatorPauseListenerCompat)var5).onAnimationResume(var1);
            }
         }

      }
   }

   interface AnimatorPauseListenerCompat {
      void onAnimationPause(Animator var1);

      void onAnimationResume(Animator var1);
   }
}
