/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorPauseListener
 *  android.animation.AnimatorListenerAdapter
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.AnimatorUtilsImpl;

@RequiresApi(value=19)
class AnimatorUtilsApi19
implements AnimatorUtilsImpl {
    AnimatorUtilsApi19() {
    }

    @Override
    public void addPauseListener(@NonNull Animator animator2, @NonNull AnimatorListenerAdapter animatorListenerAdapter) {
        animator2.addPauseListener((Animator.AnimatorPauseListener)animatorListenerAdapter);
    }

    @Override
    public void pause(@NonNull Animator animator2) {
        animator2.pause();
    }

    @Override
    public void resume(@NonNull Animator animator2) {
        animator2.resume();
    }
}

