// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics.drawable;

import android.util.Log;
import android.graphics.drawable.Drawable;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(17)
@RequiresApi(17)
class DrawableCompatJellybeanMr1
{
    private static final String TAG = "DrawableCompatJellybeanMr1";
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;
    
    public static int getLayoutDirection(final Drawable drawable) {
        while (true) {
            if (!DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched) {
                while (true) {
                    try {
                        (DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", (Class<?>[])new Class[0])).setAccessible(true);
                        DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched = true;
                        if (DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod != null) {
                            final Method method = DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod;
                            final Drawable drawable2 = drawable;
                            final int n = 0;
                            final Object[] array = new Object[n];
                            final Object o = method.invoke(drawable2, array);
                            final Integer n2 = (Integer)o;
                            final int intValue = n2;
                            return intValue;
                        }
                        return -1;
                    }
                    catch (NoSuchMethodException ex) {
                        Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve getLayoutDirection() method", (Throwable)ex);
                        continue;
                    }
                    break;
                }
                try {
                    final Method method = DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod;
                    final Drawable drawable2 = drawable;
                    final int n = 0;
                    final Object[] array = new Object[n];
                    final Object o = method.invoke(drawable2, array);
                    final Integer n2 = (Integer)o;
                    final int intValue2;
                    final int intValue = intValue2 = n2;
                    return intValue2;
                }
                catch (Exception ex2) {
                    Log.i("DrawableCompatJellybeanMr1", "Failed to invoke getLayoutDirection() via reflection", (Throwable)ex2);
                    DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod = null;
                }
                return -1;
            }
            continue;
        }
    }
    
    public static boolean setLayoutDirection(final Drawable drawable, final int n) {
        while (true) {
            if (!DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched) {
                while (true) {
                    try {
                        (DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE)).setAccessible(true);
                        DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched = true;
                        if (DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod != null) {
                            final Method method = DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod;
                            final Drawable drawable2 = drawable;
                            final int n2 = 1;
                            final Object[] array = new Object[n2];
                            final int n3 = 0;
                            final int n4 = n;
                            final Integer n5 = n4;
                            array[n3] = n5;
                            method.invoke(drawable2, array);
                            return true;
                        }
                        return false;
                    }
                    catch (NoSuchMethodException ex) {
                        Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve setLayoutDirection(int) method", (Throwable)ex);
                        continue;
                    }
                    break;
                }
                try {
                    final Method method = DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod;
                    final Drawable drawable2 = drawable;
                    final int n2 = 1;
                    final Object[] array = new Object[n2];
                    final int n3 = 0;
                    final int n4 = n;
                    final Integer n5 = n4;
                    array[n3] = n5;
                    method.invoke(drawable2, array);
                    return true;
                }
                catch (Exception ex2) {
                    Log.i("DrawableCompatJellybeanMr1", "Failed to invoke setLayoutDirection(int) via reflection", (Throwable)ex2);
                    DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod = null;
                }
                return false;
            }
            continue;
        }
    }
}
