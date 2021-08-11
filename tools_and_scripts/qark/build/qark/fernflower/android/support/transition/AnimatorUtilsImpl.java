package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;

interface AnimatorUtilsImpl {
   void addPauseListener(@NonNull Animator var1, @NonNull AnimatorListenerAdapter var2);

   void pause(@NonNull Animator var1);

   void resume(@NonNull Animator var1);
}
