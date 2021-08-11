// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import java.lang.reflect.Method;
import android.util.Log;
import java.lang.reflect.Field;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.PopupWindow;
import android.os.Build$VERSION;

public final class PopupWindowCompat
{
    static final PopupWindowCompatBaseImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 23) {
            IMPL = (PopupWindowCompatBaseImpl)new PopupWindowCompatApi23Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (PopupWindowCompatBaseImpl)new PopupWindowCompatApi21Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (PopupWindowCompatBaseImpl)new PopupWindowCompatApi19Impl();
            return;
        }
        IMPL = new PopupWindowCompatBaseImpl();
    }
    
    private PopupWindowCompat() {
    }
    
    public static boolean getOverlapAnchor(final PopupWindow popupWindow) {
        return PopupWindowCompat.IMPL.getOverlapAnchor(popupWindow);
    }
    
    public static int getWindowLayoutType(final PopupWindow popupWindow) {
        return PopupWindowCompat.IMPL.getWindowLayoutType(popupWindow);
    }
    
    public static void setOverlapAnchor(final PopupWindow popupWindow, final boolean b) {
        PopupWindowCompat.IMPL.setOverlapAnchor(popupWindow, b);
    }
    
    public static void setWindowLayoutType(final PopupWindow popupWindow, final int n) {
        PopupWindowCompat.IMPL.setWindowLayoutType(popupWindow, n);
    }
    
    public static void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
        PopupWindowCompat.IMPL.showAsDropDown(popupWindow, view, n, n2, n3);
    }
    
    @RequiresApi(19)
    static class PopupWindowCompatApi19Impl extends PopupWindowCompatBaseImpl
    {
        @Override
        public void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
            popupWindow.showAsDropDown(view, n, n2, n3);
        }
    }
    
    @RequiresApi(21)
    static class PopupWindowCompatApi21Impl extends PopupWindowCompatApi19Impl
    {
        private static final String TAG = "PopupWindowCompatApi21";
        private static Field sOverlapAnchorField;
        
        static {
            try {
                (PopupWindowCompatApi21Impl.sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)ex);
            }
        }
        
        @Override
        public boolean getOverlapAnchor(final PopupWindow popupWindow) {
            final Field sOverlapAnchorField = PopupWindowCompatApi21Impl.sOverlapAnchorField;
            if (sOverlapAnchorField != null) {
                try {
                    return (boolean)sOverlapAnchorField.get(popupWindow);
                }
                catch (IllegalAccessException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", (Throwable)ex);
                }
            }
            return false;
        }
        
        @Override
        public void setOverlapAnchor(final PopupWindow popupWindow, final boolean b) {
            final Field sOverlapAnchorField = PopupWindowCompatApi21Impl.sOverlapAnchorField;
            if (sOverlapAnchorField != null) {
                try {
                    sOverlapAnchorField.set(popupWindow, b);
                }
                catch (IllegalAccessException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", (Throwable)ex);
                }
            }
        }
    }
    
    @RequiresApi(23)
    static class PopupWindowCompatApi23Impl extends PopupWindowCompatApi21Impl
    {
        @Override
        public boolean getOverlapAnchor(final PopupWindow popupWindow) {
            return popupWindow.getOverlapAnchor();
        }
        
        @Override
        public int getWindowLayoutType(final PopupWindow popupWindow) {
            return popupWindow.getWindowLayoutType();
        }
        
        @Override
        public void setOverlapAnchor(final PopupWindow popupWindow, final boolean overlapAnchor) {
            popupWindow.setOverlapAnchor(overlapAnchor);
        }
        
        @Override
        public void setWindowLayoutType(final PopupWindow popupWindow, final int windowLayoutType) {
            popupWindow.setWindowLayoutType(windowLayoutType);
        }
    }
    
    static class PopupWindowCompatBaseImpl
    {
        private static Method sGetWindowLayoutTypeMethod;
        private static boolean sGetWindowLayoutTypeMethodAttempted;
        private static Method sSetWindowLayoutTypeMethod;
        private static boolean sSetWindowLayoutTypeMethodAttempted;
        
        public boolean getOverlapAnchor(final PopupWindow popupWindow) {
            return false;
        }
        
        public int getWindowLayoutType(final PopupWindow popupWindow) {
            if (!PopupWindowCompatBaseImpl.sGetWindowLayoutTypeMethodAttempted) {
                try {
                    (PopupWindowCompatBaseImpl.sGetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", (Class<?>[])new Class[0])).setAccessible(true);
                }
                catch (Exception ex) {}
                PopupWindowCompatBaseImpl.sGetWindowLayoutTypeMethodAttempted = true;
            }
            final Method sGetWindowLayoutTypeMethod = PopupWindowCompatBaseImpl.sGetWindowLayoutTypeMethod;
            if (sGetWindowLayoutTypeMethod != null) {
                try {
                    return (int)sGetWindowLayoutTypeMethod.invoke(popupWindow, new Object[0]);
                }
                catch (Exception ex2) {
                    return 0;
                }
            }
            return 0;
        }
        
        public void setOverlapAnchor(final PopupWindow popupWindow, final boolean b) {
        }
        
        public void setWindowLayoutType(final PopupWindow popupWindow, final int n) {
            if (!PopupWindowCompatBaseImpl.sSetWindowLayoutTypeMethodAttempted) {
                try {
                    (PopupWindowCompatBaseImpl.sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE)).setAccessible(true);
                }
                catch (Exception ex) {}
                PopupWindowCompatBaseImpl.sSetWindowLayoutTypeMethodAttempted = true;
            }
            final Method sSetWindowLayoutTypeMethod = PopupWindowCompatBaseImpl.sSetWindowLayoutTypeMethod;
            if (sSetWindowLayoutTypeMethod != null) {
                try {
                    sSetWindowLayoutTypeMethod.invoke(popupWindow, n);
                }
                catch (Exception ex2) {}
            }
        }
        
        public void showAsDropDown(final PopupWindow popupWindow, final View view, int n, final int n2, final int n3) {
            if ((GravityCompat.getAbsoluteGravity(n3, ViewCompat.getLayoutDirection(view)) & 0x7) == 0x5) {
                n -= popupWindow.getWidth() - view.getWidth();
            }
            popupWindow.showAsDropDown(view, n, n2);
        }
    }
}
