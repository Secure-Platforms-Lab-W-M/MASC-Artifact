// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import java.lang.reflect.InvocationTargetException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(18)
class ViewGroupUtilsApi18 extends ViewGroupUtilsApi14
{
    private static final String TAG = "ViewUtilsApi18";
    private static Method sSuppressLayoutMethod;
    private static boolean sSuppressLayoutMethodFetched;
    
    private void fetchSuppressLayoutMethod() {
        if (!ViewGroupUtilsApi18.sSuppressLayoutMethodFetched) {
            try {
                (ViewGroupUtilsApi18.sSuppressLayoutMethod = ViewGroup.class.getDeclaredMethod("suppressLayout", Boolean.TYPE)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi18", "Failed to retrieve suppressLayout method", (Throwable)ex);
            }
            ViewGroupUtilsApi18.sSuppressLayoutMethodFetched = true;
        }
    }
    
    @Override
    public ViewGroupOverlayImpl getOverlay(@NonNull final ViewGroup viewGroup) {
        return new ViewGroupOverlayApi18(viewGroup);
    }
    
    @Override
    public void suppressLayout(@NonNull final ViewGroup viewGroup, final boolean b) {
        this.fetchSuppressLayoutMethod();
        final Method sSuppressLayoutMethod = ViewGroupUtilsApi18.sSuppressLayoutMethod;
        if (sSuppressLayoutMethod != null) {
            try {
                sSuppressLayoutMethod.invoke(viewGroup, b);
            }
            catch (InvocationTargetException ex) {
                Log.i("ViewUtilsApi18", "Error invoking suppressLayout method", (Throwable)ex);
            }
            catch (IllegalAccessException ex2) {
                Log.i("ViewUtilsApi18", "Failed to invoke suppressLayout method", (Throwable)ex2);
            }
        }
    }
}
