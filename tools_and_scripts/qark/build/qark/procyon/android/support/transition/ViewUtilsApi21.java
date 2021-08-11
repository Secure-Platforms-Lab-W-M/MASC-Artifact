// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import java.lang.reflect.InvocationTargetException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.graphics.Matrix;
import android.view.View;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class ViewUtilsApi21 extends ViewUtilsApi19
{
    private static final String TAG = "ViewUtilsApi21";
    private static Method sSetAnimationMatrixMethod;
    private static boolean sSetAnimationMatrixMethodFetched;
    private static Method sTransformMatrixToGlobalMethod;
    private static boolean sTransformMatrixToGlobalMethodFetched;
    private static Method sTransformMatrixToLocalMethod;
    private static boolean sTransformMatrixToLocalMethodFetched;
    
    private void fetchSetAnimationMatrix() {
        if (!ViewUtilsApi21.sSetAnimationMatrixMethodFetched) {
            try {
                (ViewUtilsApi21.sSetAnimationMatrixMethod = View.class.getDeclaredMethod("setAnimationMatrix", Matrix.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi21", "Failed to retrieve setAnimationMatrix method", (Throwable)ex);
            }
            ViewUtilsApi21.sSetAnimationMatrixMethodFetched = true;
        }
    }
    
    private void fetchTransformMatrixToGlobalMethod() {
        if (!ViewUtilsApi21.sTransformMatrixToGlobalMethodFetched) {
            try {
                (ViewUtilsApi21.sTransformMatrixToGlobalMethod = View.class.getDeclaredMethod("transformMatrixToGlobal", Matrix.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToGlobal method", (Throwable)ex);
            }
            ViewUtilsApi21.sTransformMatrixToGlobalMethodFetched = true;
        }
    }
    
    private void fetchTransformMatrixToLocalMethod() {
        if (!ViewUtilsApi21.sTransformMatrixToLocalMethodFetched) {
            try {
                (ViewUtilsApi21.sTransformMatrixToLocalMethod = View.class.getDeclaredMethod("transformMatrixToLocal", Matrix.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToLocal method", (Throwable)ex);
            }
            ViewUtilsApi21.sTransformMatrixToLocalMethodFetched = true;
        }
    }
    
    @Override
    public void setAnimationMatrix(@NonNull final View view, final Matrix matrix) {
        this.fetchSetAnimationMatrix();
        final Method sSetAnimationMatrixMethod = ViewUtilsApi21.sSetAnimationMatrixMethod;
        if (sSetAnimationMatrixMethod != null) {
            try {
                sSetAnimationMatrixMethod.invoke(view, matrix);
            }
            catch (IllegalAccessException ex) {
                throw new RuntimeException(ex.getCause());
            }
            catch (InvocationTargetException ex2) {}
        }
    }
    
    @Override
    public void transformMatrixToGlobal(@NonNull final View view, @NonNull final Matrix matrix) {
        this.fetchTransformMatrixToGlobalMethod();
        final Method sTransformMatrixToGlobalMethod = ViewUtilsApi21.sTransformMatrixToGlobalMethod;
        if (sTransformMatrixToGlobalMethod != null) {
            try {
                sTransformMatrixToGlobalMethod.invoke(view, matrix);
            }
            catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause());
            }
            catch (IllegalAccessException ex2) {}
        }
    }
    
    @Override
    public void transformMatrixToLocal(@NonNull final View view, @NonNull final Matrix matrix) {
        this.fetchTransformMatrixToLocalMethod();
        final Method sTransformMatrixToLocalMethod = ViewUtilsApi21.sTransformMatrixToLocalMethod;
        if (sTransformMatrixToLocalMethod != null) {
            try {
                sTransformMatrixToLocalMethod.invoke(view, matrix);
            }
            catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause());
            }
            catch (IllegalAccessException ex2) {}
        }
    }
}
