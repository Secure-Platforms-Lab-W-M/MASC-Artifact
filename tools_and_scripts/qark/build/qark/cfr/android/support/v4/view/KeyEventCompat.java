/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.KeyEvent
 *  android.view.KeyEvent$Callback
 *  android.view.KeyEvent$DispatcherState
 *  android.view.View
 */
package android.support.v4.view;

import android.view.KeyEvent;
import android.view.View;

@Deprecated
public final class KeyEventCompat {
    private KeyEventCompat() {
    }

    @Deprecated
    public static boolean dispatch(KeyEvent keyEvent, KeyEvent.Callback callback, Object object, Object object2) {
        return keyEvent.dispatch(callback, (KeyEvent.DispatcherState)object, object2);
    }

    @Deprecated
    public static Object getKeyDispatcherState(View view) {
        return view.getKeyDispatcherState();
    }

    @Deprecated
    public static boolean hasModifiers(KeyEvent keyEvent, int n) {
        return keyEvent.hasModifiers(n);
    }

    @Deprecated
    public static boolean hasNoModifiers(KeyEvent keyEvent) {
        return keyEvent.hasNoModifiers();
    }

    @Deprecated
    public static boolean isCtrlPressed(KeyEvent keyEvent) {
        return keyEvent.isCtrlPressed();
    }

    @Deprecated
    public static boolean isTracking(KeyEvent keyEvent) {
        return keyEvent.isTracking();
    }

    @Deprecated
    public static boolean metaStateHasModifiers(int n, int n2) {
        return KeyEvent.metaStateHasModifiers((int)n, (int)n2);
    }

    @Deprecated
    public static boolean metaStateHasNoModifiers(int n) {
        return KeyEvent.metaStateHasNoModifiers((int)n);
    }

    @Deprecated
    public static int normalizeMetaState(int n) {
        return KeyEvent.normalizeMetaState((int)n);
    }

    @Deprecated
    public static void startTracking(KeyEvent keyEvent) {
        keyEvent.startTracking();
    }
}

