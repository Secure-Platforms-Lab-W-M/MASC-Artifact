/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.MotionEvent
 */
package android.support.v4.view;

import android.view.MotionEvent;

class MotionEventCompatEclair {
    MotionEventCompatEclair() {
    }

    public static int findPointerIndex(MotionEvent motionEvent, int n) {
        return motionEvent.findPointerIndex(n);
    }

    public static int getPointerCount(MotionEvent motionEvent) {
        return motionEvent.getPointerCount();
    }

    public static int getPointerId(MotionEvent motionEvent, int n) {
        return motionEvent.getPointerId(n);
    }

    public static float getX(MotionEvent motionEvent, int n) {
        return motionEvent.getX(n);
    }

    public static float getY(MotionEvent motionEvent, int n) {
        return motionEvent.getY(n);
    }
}

