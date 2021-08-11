/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package androidx.transition;

import android.graphics.Canvas;
import android.os.Build;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class CanvasUtils {
    private static Method sInorderBarrierMethod;
    private static boolean sOrderMethodsFetched;
    private static Method sReorderBarrierMethod;

    private CanvasUtils() {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    static void enableZ(Canvas canvas, boolean bl) {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            if (bl) {
                canvas.enableZ();
                return;
            }
            canvas.disableZ();
            return;
        }
        if (Build.VERSION.SDK_INT == 28) throw new IllegalStateException("This method doesn't work on Pie!");
        if (!CanvasUtils.sOrderMethodsFetched) {
            try {
                CanvasUtils.sReorderBarrierMethod = method = Canvas.class.getDeclaredMethod("insertReorderBarrier", new Class[0]);
                method.setAccessible(true);
                CanvasUtils.sInorderBarrierMethod = method = Canvas.class.getDeclaredMethod("insertInorderBarrier", new Class[0]);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            CanvasUtils.sOrderMethodsFetched = true;
        }
        if (!bl) ** GOTO lbl24
        try {
            if (CanvasUtils.sReorderBarrierMethod != null) {
                CanvasUtils.sReorderBarrierMethod.invoke((Object)canvas, new Object[0]);
            }
lbl24: // 4 sources:
            if (bl != false) return;
            if (CanvasUtils.sInorderBarrierMethod == null) return;
            CanvasUtils.sInorderBarrierMethod.invoke((Object)canvas, new Object[0]);
            return;
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new RuntimeException(invocationTargetException.getCause());
        }
        catch (IllegalAccessException illegalAccessException) {
            return;
        }
    }
}

