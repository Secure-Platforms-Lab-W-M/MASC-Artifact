/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.graphics.Matrix;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

@RequiresApi(value=14)
interface GhostViewImpl {
    public void reserveEndViewTransition(ViewGroup var1, View var2);

    public void setVisibility(int var1);

    public static interface Creator {
        public GhostViewImpl addGhost(View var1, ViewGroup var2, Matrix var3);

        public void removeGhost(View var1);
    }

}

