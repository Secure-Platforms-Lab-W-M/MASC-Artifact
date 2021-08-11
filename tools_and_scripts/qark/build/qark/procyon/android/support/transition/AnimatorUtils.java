// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.animation.Animator;
import android.os.Build$VERSION;

class AnimatorUtils
{
    private static final AnimatorUtilsImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = new AnimatorUtilsApi19();
            return;
        }
        IMPL = new AnimatorUtilsApi14();
    }
    
    static void addPauseListener(@NonNull final Animator animator, @NonNull final AnimatorListenerAdapter animatorListenerAdapter) {
        AnimatorUtils.IMPL.addPauseListener(animator, animatorListenerAdapter);
    }
    
    static void pause(@NonNull final Animator animator) {
        AnimatorUtils.IMPL.pause(animator);
    }
    
    static void resume(@NonNull final Animator animator) {
        AnimatorUtils.IMPL.resume(animator);
    }
}
