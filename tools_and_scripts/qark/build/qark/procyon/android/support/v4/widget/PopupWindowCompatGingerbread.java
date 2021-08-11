// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.widget.PopupWindow;
import java.lang.reflect.Method;

class PopupWindowCompatGingerbread
{
    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;
    
    static int getWindowLayoutType(final PopupWindow popupWindow) {
        Label_0031: {
            if (PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethodAttempted) {
                break Label_0031;
            }
            while (true) {
                try {
                    (PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", (Class<?>[])new Class[0])).setAccessible(true);
                    PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethodAttempted = true;
                    if (PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod != null) {
                        try {
                            return (int)PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod.invoke(popupWindow, new Object[0]);
                        }
                        catch (Exception ex) {}
                    }
                    return 0;
                }
                catch (Exception ex2) {
                    continue;
                }
                break;
            }
        }
    }
    
    static void setWindowLayoutType(final PopupWindow ex, final int n) {
        Label_0037: {
            if (PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethodAttempted) {
                break Label_0037;
            }
            while (true) {
                try {
                    (PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE)).setAccessible(true);
                    PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethodAttempted = true;
                    if (PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod == null) {
                        return;
                    }
                    try {
                        PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod.invoke(ex, n);
                    }
                    catch (Exception ex) {}
                }
                catch (Exception ex2) {
                    continue;
                }
                break;
            }
        }
    }
}
