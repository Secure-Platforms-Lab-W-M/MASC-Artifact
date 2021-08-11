// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import java.lang.reflect.InvocationTargetException;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(22)
class ViewUtilsApi22 extends ViewUtilsApi21
{
    private static final String TAG = "ViewUtilsApi22";
    private static Method sSetLeftTopRightBottomMethod;
    private static boolean sSetLeftTopRightBottomMethodFetched;
    
    @SuppressLint({ "PrivateApi" })
    private void fetchSetLeftTopRightBottomMethod() {
        if (!ViewUtilsApi22.sSetLeftTopRightBottomMethodFetched) {
            try {
                (ViewUtilsApi22.sSetLeftTopRightBottomMethod = View.class.getDeclaredMethod("setLeftTopRightBottom", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi22", "Failed to retrieve setLeftTopRightBottom method", (Throwable)ex);
            }
            ViewUtilsApi22.sSetLeftTopRightBottomMethodFetched = true;
        }
    }
    
    @Override
    public void setLeftTopRightBottom(final View view, final int n, final int n2, final int n3, final int n4) {
        this.fetchSetLeftTopRightBottomMethod();
        final Method sSetLeftTopRightBottomMethod = ViewUtilsApi22.sSetLeftTopRightBottomMethod;
        if (sSetLeftTopRightBottomMethod != null) {
            try {
                sSetLeftTopRightBottomMethod.invoke(view, n, n2, n3, n4);
            }
            catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause());
            }
            catch (IllegalAccessException ex2) {}
        }
    }
}
