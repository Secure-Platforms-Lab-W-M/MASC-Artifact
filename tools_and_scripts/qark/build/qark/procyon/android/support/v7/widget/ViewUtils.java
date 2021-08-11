// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import java.lang.reflect.InvocationTargetException;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.graphics.Rect;
import android.view.View;
import android.os.Build$VERSION;
import java.lang.reflect.Method;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ViewUtils
{
    private static final String TAG = "ViewUtils";
    private static Method sComputeFitSystemWindowsMethod;
    
    static {
        if (Build$VERSION.SDK_INT >= 18) {
            try {
                ViewUtils.sComputeFitSystemWindowsMethod = View.class.getDeclaredMethod("computeFitSystemWindows", Rect.class, Rect.class);
                if (!ViewUtils.sComputeFitSystemWindowsMethod.isAccessible()) {
                    ViewUtils.sComputeFitSystemWindowsMethod.setAccessible(true);
                }
            }
            catch (NoSuchMethodException ex) {
                Log.d("ViewUtils", "Could not find method computeFitSystemWindows. Oh well.");
            }
        }
    }
    
    private ViewUtils() {
    }
    
    public static void computeFitSystemWindows(final View view, final Rect rect, final Rect rect2) {
        final Method sComputeFitSystemWindowsMethod = ViewUtils.sComputeFitSystemWindowsMethod;
        if (sComputeFitSystemWindowsMethod != null) {
            try {
                sComputeFitSystemWindowsMethod.invoke(view, rect, rect2);
            }
            catch (Exception ex) {
                Log.d("ViewUtils", "Could not invoke computeFitSystemWindows", (Throwable)ex);
            }
        }
    }
    
    public static boolean isLayoutRtl(final View view) {
        return ViewCompat.getLayoutDirection(view) == 1;
    }
    
    public static void makeOptionalFitsSystemWindows(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            while (true) {
                while (true) {
                    Label_0081: {
                        try {
                            final Method method = view.getClass().getMethod("makeOptionalFitsSystemWindows", (Class<?>[])new Class[0]);
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                                method.invoke(view, new Object[0]);
                                return;
                            }
                            break Label_0081;
                        }
                        catch (IllegalAccessException ex) {
                            Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", (Throwable)ex);
                            return;
                        }
                        catch (InvocationTargetException ex2) {
                            Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", (Throwable)ex2);
                            return;
                        }
                        catch (NoSuchMethodException ex3) {
                            Log.d("ViewUtils", "Could not find method makeOptionalFitsSystemWindows. Oh well...");
                            return;
                        }
                        break;
                    }
                    continue;
                }
            }
        }
    }
}
