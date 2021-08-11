package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

class AnimatorUtils {
   private static final AnimatorUtilsImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 19) {
         IMPL = new AnimatorUtilsApi19();
      } else {
         IMPL = new AnimatorUtilsApi14();
      }
   }

   static void addPauseListener(@NonNull Animator var0, @NonNull AnimatorListenerAdapter var1) {
      IMPL.addPauseListener(var0, var1);
   }

   static void pause(@NonNull Animator var0) {
      IMPL.pause(var0);
   }

   static void resume(@NonNull Animator var0) {
      IMPL.resume(var0);
   }
}
