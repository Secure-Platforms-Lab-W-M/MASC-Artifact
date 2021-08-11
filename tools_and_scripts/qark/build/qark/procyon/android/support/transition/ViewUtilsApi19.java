// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import java.lang.reflect.InvocationTargetException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class ViewUtilsApi19 extends ViewUtilsApi18
{
    private static final String TAG = "ViewUtilsApi19";
    private static Method sGetTransitionAlphaMethod;
    private static boolean sGetTransitionAlphaMethodFetched;
    private static Method sSetTransitionAlphaMethod;
    private static boolean sSetTransitionAlphaMethodFetched;
    
    private void fetchGetTransitionAlphaMethod() {
        if (!ViewUtilsApi19.sGetTransitionAlphaMethodFetched) {
            try {
                (ViewUtilsApi19.sGetTransitionAlphaMethod = View.class.getDeclaredMethod("getTransitionAlpha", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi19", "Failed to retrieve getTransitionAlpha method", (Throwable)ex);
            }
            ViewUtilsApi19.sGetTransitionAlphaMethodFetched = true;
        }
    }
    
    private void fetchSetTransitionAlphaMethod() {
        if (!ViewUtilsApi19.sSetTransitionAlphaMethodFetched) {
            try {
                (ViewUtilsApi19.sSetTransitionAlphaMethod = View.class.getDeclaredMethod("setTransitionAlpha", Float.TYPE)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi19", "Failed to retrieve setTransitionAlpha method", (Throwable)ex);
            }
            ViewUtilsApi19.sSetTransitionAlphaMethodFetched = true;
        }
    }
    
    @Override
    public void clearNonTransitionAlpha(@NonNull final View view) {
    }
    
    @Override
    public float getTransitionAlpha(@NonNull final View view) {
        this.fetchGetTransitionAlphaMethod();
        final Method sGetTransitionAlphaMethod = ViewUtilsApi19.sGetTransitionAlphaMethod;
        if (sGetTransitionAlphaMethod != null) {
            try {
                return (float)sGetTransitionAlphaMethod.invoke(view, new Object[0]);
            }
            catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause());
            }
            catch (IllegalAccessException ex2) {}
        }
        return super.getTransitionAlpha(view);
    }
    
    @Override
    public void saveNonTransitionAlpha(@NonNull final View view) {
    }
    
    @Override
    public void setTransitionAlpha(@NonNull final View view, final float alpha) {
        this.fetchSetTransitionAlphaMethod();
        final Method sSetTransitionAlphaMethod = ViewUtilsApi19.sSetTransitionAlphaMethod;
        if (sSetTransitionAlphaMethod != null) {
            try {
                sSetTransitionAlphaMethod.invoke(view, alpha);
            }
            catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause());
            }
            catch (IllegalAccessException ex2) {}
            return;
        }
        view.setAlpha(alpha);
    }
}
