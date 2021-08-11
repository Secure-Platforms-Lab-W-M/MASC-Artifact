/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorListenerAdapter
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;

interface AnimatorUtilsImpl {
    public void addPauseListener(@NonNull Animator var1, @NonNull AnimatorListenerAdapter var2);

    public void pause(@NonNull Animator var1);

    public void resume(@NonNull Animator var1);
}

