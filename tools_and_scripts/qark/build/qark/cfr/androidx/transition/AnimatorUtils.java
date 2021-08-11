/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.Animator$AnimatorPauseListener
 *  android.animation.AnimatorListenerAdapter
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import java.util.ArrayList;

class AnimatorUtils {
    private AnimatorUtils() {
    }

    static void addPauseListener(Animator animator, AnimatorListenerAdapter animatorListenerAdapter) {
        if (Build.VERSION.SDK_INT >= 19) {
            animator.addPauseListener((Animator.AnimatorPauseListener)animatorListenerAdapter);
        }
    }

    static void pause(Animator animator) {
        if (Build.VERSION.SDK_INT >= 19) {
            animator.pause();
            return;
        }
        ArrayList arrayList = animator.getListeners();
        if (arrayList != null) {
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                Animator.AnimatorListener animatorListener = (Animator.AnimatorListener)arrayList.get(i);
                if (!(animatorListener instanceof AnimatorPauseListenerCompat)) continue;
                ((AnimatorPauseListenerCompat)animatorListener).onAnimationPause(animator);
            }
        }
    }

    static void resume(Animator animator) {
        if (Build.VERSION.SDK_INT >= 19) {
            animator.resume();
            return;
        }
        ArrayList arrayList = animator.getListeners();
        if (arrayList != null) {
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                Animator.AnimatorListener animatorListener = (Animator.AnimatorListener)arrayList.get(i);
                if (!(animatorListener instanceof AnimatorPauseListenerCompat)) continue;
                ((AnimatorPauseListenerCompat)animatorListener).onAnimationResume(animator);
            }
        }
    }

    static interface AnimatorPauseListenerCompat {
        public void onAnimationPause(Animator var1);

        public void onAnimationResume(Animator var1);
    }

}

