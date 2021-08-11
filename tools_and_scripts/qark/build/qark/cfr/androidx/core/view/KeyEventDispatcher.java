/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.ActionBar
 *  android.app.Activity
 *  android.app.Dialog
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnKeyListener
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.KeyEvent
 *  android.view.KeyEvent$Callback
 *  android.view.KeyEvent$DispatcherState
 *  android.view.View
 *  android.view.Window
 *  android.view.Window$Callback
 */
package androidx.core.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KeyEventDispatcher {
    private static boolean sActionBarFieldsFetched = false;
    private static Method sActionBarOnMenuKeyMethod = null;
    private static boolean sDialogFieldsFetched = false;
    private static Field sDialogKeyListenerField = null;

    private KeyEventDispatcher() {
    }

    private static boolean actionBarOnMenuKeyEventPre28(ActionBar actionBar, KeyEvent keyEvent) {
        Method method;
        if (!sActionBarFieldsFetched) {
            try {
                sActionBarOnMenuKeyMethod = actionBar.getClass().getMethod("onMenuKeyEvent", KeyEvent.class);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            sActionBarFieldsFetched = true;
        }
        if ((method = sActionBarOnMenuKeyMethod) != null) {
            try {
                boolean bl = (Boolean)method.invoke((Object)actionBar, new Object[]{keyEvent});
                return bl;
            }
            catch (InvocationTargetException invocationTargetException) {
                return false;
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        return false;
    }

    private static boolean activitySuperDispatchKeyEventPre28(Activity activity, KeyEvent keyEvent) {
        activity.onUserInteraction();
        Window window = activity.getWindow();
        if (window.hasFeature(8)) {
            ActionBar actionBar = activity.getActionBar();
            if (keyEvent.getKeyCode() == 82 && actionBar != null && KeyEventDispatcher.actionBarOnMenuKeyEventPre28(actionBar, keyEvent)) {
                return true;
            }
        }
        if (window.superDispatchKeyEvent(keyEvent)) {
            return true;
        }
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback((View)(window = window.getDecorView()), keyEvent)) {
            return true;
        }
        window = window != null ? window.getKeyDispatcherState() : null;
        return keyEvent.dispatch((KeyEvent.Callback)activity, (KeyEvent.DispatcherState)window, (Object)activity);
    }

    private static boolean dialogSuperDispatchKeyEventPre28(Dialog dialog, KeyEvent keyEvent) {
        DialogInterface.OnKeyListener onKeyListener = KeyEventDispatcher.getDialogKeyListenerPre28(dialog);
        if (onKeyListener != null && onKeyListener.onKey((DialogInterface)dialog, keyEvent.getKeyCode(), keyEvent)) {
            return true;
        }
        onKeyListener = dialog.getWindow();
        if (onKeyListener.superDispatchKeyEvent(keyEvent)) {
            return true;
        }
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback((View)(onKeyListener = onKeyListener.getDecorView()), keyEvent)) {
            return true;
        }
        onKeyListener = onKeyListener != null ? onKeyListener.getKeyDispatcherState() : null;
        return keyEvent.dispatch((KeyEvent.Callback)dialog, (KeyEvent.DispatcherState)onKeyListener, (Object)dialog);
    }

    public static boolean dispatchBeforeHierarchy(View view, KeyEvent keyEvent) {
        return ViewCompat.dispatchUnhandledKeyEventBeforeHierarchy(view, keyEvent);
    }

    public static boolean dispatchKeyEvent(Component component, View view, Window.Callback callback, KeyEvent keyEvent) {
        boolean bl = false;
        if (component == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 28) {
            return component.superDispatchKeyEvent(keyEvent);
        }
        if (callback instanceof Activity) {
            return KeyEventDispatcher.activitySuperDispatchKeyEventPre28((Activity)callback, keyEvent);
        }
        if (callback instanceof Dialog) {
            return KeyEventDispatcher.dialogSuperDispatchKeyEventPre28((Dialog)callback, keyEvent);
        }
        if (view != null && ViewCompat.dispatchUnhandledKeyEventBeforeCallback(view, keyEvent) || component.superDispatchKeyEvent(keyEvent)) {
            bl = true;
        }
        return bl;
    }

    private static DialogInterface.OnKeyListener getDialogKeyListenerPre28(Dialog dialog) {
        Field field;
        if (!sDialogFieldsFetched) {
            try {
                sDialogKeyListenerField = field = Dialog.class.getDeclaredField("mOnKeyListener");
                field.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
            sDialogFieldsFetched = true;
        }
        if ((field = sDialogKeyListenerField) != null) {
            try {
                dialog = (DialogInterface.OnKeyListener)field.get((Object)dialog);
                return dialog;
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        return null;
    }

    public static interface Component {
        public boolean superDispatchKeyEvent(KeyEvent var1);
    }

}

