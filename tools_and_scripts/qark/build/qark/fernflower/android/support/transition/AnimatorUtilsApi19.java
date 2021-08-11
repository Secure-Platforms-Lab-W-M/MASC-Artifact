package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class AnimatorUtilsApi19 implements AnimatorUtilsImpl {
   public void addPauseListener(@NonNull Animator var1, @NonNull AnimatorListenerAdapter var2) {
      var1.addPauseListener(var2);
   }

   public void pause(@NonNull Animator var1) {
      var1.pause();
   }

   public void resume(@NonNull Animator var1) {
      var1.resume();
   }
}
