/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.content.Context;
import android.support.transition.SceneImpl;
import android.view.ViewGroup;

abstract class SceneStaticsImpl {
    SceneStaticsImpl() {
    }

    public abstract SceneImpl getSceneForLayout(ViewGroup var1, int var2, Context var3);
}

