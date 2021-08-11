// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.animation.Animator;

interface AnimatorUtilsImpl
{
    void addPauseListener(@NonNull final Animator p0, @NonNull final AnimatorListenerAdapter p1);
    
    void pause(@NonNull final Animator p0);
    
    void resume(@NonNull final Animator p0);
}
