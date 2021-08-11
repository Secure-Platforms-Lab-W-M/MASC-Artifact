/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.MotionEvent
 */
package android.support.v4.view;

import android.view.MotionEvent;

class MotionEventCompatHoneycombMr1 {
    MotionEventCompatHoneycombMr1() {
    }

    static float getAxisValue(MotionEvent motionEvent, int n) {
        return motionEvent.getAxisValue(n);
    }

    static float getAxisValue(MotionEvent motionEvent, int n, int n2) {
        return motionEvent.getAxisValue(n, n2);
    }
}

