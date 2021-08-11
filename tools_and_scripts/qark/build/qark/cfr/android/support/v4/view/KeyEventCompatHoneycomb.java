/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.KeyEvent
 */
package android.support.v4.view;

import android.view.KeyEvent;

class KeyEventCompatHoneycomb {
    KeyEventCompatHoneycomb() {
    }

    public static boolean metaStateHasModifiers(int n, int n2) {
        return KeyEvent.metaStateHasModifiers((int)n, (int)n2);
    }

    public static boolean metaStateHasNoModifiers(int n) {
        return KeyEvent.metaStateHasNoModifiers((int)n);
    }

    public static int normalizeMetaState(int n) {
        return KeyEvent.normalizeMetaState((int)n);
    }
}

