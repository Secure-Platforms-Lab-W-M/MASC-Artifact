/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.graphics.Matrix;
import android.os.Build;
import android.support.transition.GhostViewApi14;
import android.support.transition.GhostViewApi21;
import android.support.transition.GhostViewImpl;
import android.view.View;
import android.view.ViewGroup;

class GhostViewUtils {
    private static final GhostViewImpl.Creator CREATOR = Build.VERSION.SDK_INT >= 21 ? new GhostViewApi21.Creator() : new GhostViewApi14.Creator();

    GhostViewUtils() {
    }

    static GhostViewImpl addGhost(View view, ViewGroup viewGroup, Matrix matrix) {
        return CREATOR.addGhost(view, viewGroup, matrix);
    }

    static void removeGhost(View view) {
        CREATOR.removeGhost(view);
    }
}

