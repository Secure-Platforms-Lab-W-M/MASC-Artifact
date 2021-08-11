/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 */
package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.lang.reflect.Method;

class DrawableCompatJellybeanMr1 {
    private static final String TAG = "DrawableCompatJellybeanMr1";
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;

    DrawableCompatJellybeanMr1() {
    }

    public static int getLayoutDirection(Drawable drawable) {
        if (!sGetLayoutDirectionMethodFetched) {
            try {
                sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", new Class[0]);
                sGetLayoutDirectionMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"DrawableCompatJellybeanMr1", (String)"Failed to retrieve getLayoutDirection() method", (Throwable)noSuchMethodException);
            }
            sGetLayoutDirectionMethodFetched = true;
        }
        if (sGetLayoutDirectionMethod != null) {
            try {
                int n = (Integer)sGetLayoutDirectionMethod.invoke((Object)drawable, new Object[0]);
                return n;
            }
            catch (Exception exception) {
                Log.i((String)"DrawableCompatJellybeanMr1", (String)"Failed to invoke getLayoutDirection() via reflection", (Throwable)exception);
                sGetLayoutDirectionMethod = null;
            }
        }
        return -1;
    }

    public static void setLayoutDirection(Drawable drawable, int n) {
        if (!sSetLayoutDirectionMethodFetched) {
            try {
                sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE);
                sSetLayoutDirectionMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"DrawableCompatJellybeanMr1", (String)"Failed to retrieve setLayoutDirection(int) method", (Throwable)noSuchMethodException);
            }
            sSetLayoutDirectionMethodFetched = true;
        }
        if (sSetLayoutDirectionMethod != null) {
            try {
                sSetLayoutDirectionMethod.invoke((Object)drawable, n);
                return;
            }
            catch (Exception exception) {
                Log.i((String)"DrawableCompatJellybeanMr1", (String)"Failed to invoke setLayoutDirection(int) via reflection", (Throwable)exception);
                sSetLayoutDirectionMethod = null;
            }
        }
    }
}

