// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.view.MotionEvent;

class MotionEventCompatEclair
{
    public static int findPointerIndex(final MotionEvent motionEvent, final int n) {
        return motionEvent.findPointerIndex(n);
    }
    
    public static int getPointerCount(final MotionEvent motionEvent) {
        return motionEvent.getPointerCount();
    }
    
    public static int getPointerId(final MotionEvent motionEvent, final int n) {
        return motionEvent.getPointerId(n);
    }
    
    public static float getX(final MotionEvent motionEvent, final int n) {
        return motionEvent.getX(n);
    }
    
    public static float getY(final MotionEvent motionEvent, final int n) {
        return motionEvent.getY(n);
    }
}
