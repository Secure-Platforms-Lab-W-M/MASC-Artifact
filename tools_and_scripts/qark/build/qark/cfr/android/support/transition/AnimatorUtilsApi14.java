/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.AnimatorUtilsImpl;
import java.util.ArrayList;

@RequiresApi(value=14)
class AnimatorUtilsApi14
implements AnimatorUtilsImpl {
    AnimatorUtilsApi14() {
    }

    @Override
    public void addPauseListener(@NonNull Animator animator2, @NonNull AnimatorListenerAdapter animatorListenerAdapter) {
    }

    @Override
    public void pause(@NonNull Animator animator2) {
        ArrayList arrayList = animator2.getListeners();
        if (arrayList != null) {
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                Animator.AnimatorListener animatorListener = (Animator.AnimatorListener)arrayList.get(i);
                if (!(animatorListener instanceof AnimatorPauseListenerCompat)) continue;
                ((AnimatorPauseListenerCompat)animatorListener).onAnimationPause(animator2);
            }
            return;
        }
    }

    @Override
    public void resume(@NonNull Animator animator2) {
        ArrayList arrayList = animator2.getListeners();
        if (arrayList != null) {
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                Animator.AnimatorListener animatorListener = (Animator.AnimatorListener)arrayList.get(i);
                if (!(animatorListener instanceof AnimatorPauseListenerCompat)) continue;
                ((AnimatorPauseListenerCompat)animatorListener).onAnimationResume(animator2);
            }
            return;
        }
    }

    static interface AnimatorPauseListenerCompat {
        public void onAnimationPause(Animator var1);

        public void onAnimationResume(Animator var1);
    }

}

