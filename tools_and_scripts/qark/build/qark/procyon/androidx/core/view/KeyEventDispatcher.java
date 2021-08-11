// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.os.Build$VERSION;
import android.view.Window$Callback;
import android.content.DialogInterface$OnKeyListener;
import android.content.DialogInterface;
import android.app.Dialog;
import android.view.KeyEvent$DispatcherState;
import android.view.View;
import android.view.Window;
import android.view.KeyEvent$Callback;
import android.app.Activity;
import java.lang.reflect.InvocationTargetException;
import android.view.KeyEvent;
import android.app.ActionBar;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class KeyEventDispatcher
{
    private static boolean sActionBarFieldsFetched;
    private static Method sActionBarOnMenuKeyMethod;
    private static boolean sDialogFieldsFetched;
    private static Field sDialogKeyListenerField;
    
    static {
        KeyEventDispatcher.sActionBarFieldsFetched = false;
        KeyEventDispatcher.sActionBarOnMenuKeyMethod = null;
        KeyEventDispatcher.sDialogFieldsFetched = false;
        KeyEventDispatcher.sDialogKeyListenerField = null;
    }
    
    private KeyEventDispatcher() {
    }
    
    private static boolean actionBarOnMenuKeyEventPre28(final ActionBar actionBar, final KeyEvent keyEvent) {
        if (!KeyEventDispatcher.sActionBarFieldsFetched) {
            try {
                KeyEventDispatcher.sActionBarOnMenuKeyMethod = actionBar.getClass().getMethod("onMenuKeyEvent", KeyEvent.class);
            }
            catch (NoSuchMethodException ex) {}
            KeyEventDispatcher.sActionBarFieldsFetched = true;
        }
        final Method sActionBarOnMenuKeyMethod = KeyEventDispatcher.sActionBarOnMenuKeyMethod;
        if (sActionBarOnMenuKeyMethod != null) {
            try {
                return (boolean)sActionBarOnMenuKeyMethod.invoke(actionBar, keyEvent);
            }
            catch (InvocationTargetException ex2) {
                return false;
            }
            catch (IllegalAccessException ex3) {}
        }
        return false;
    }
    
    private static boolean activitySuperDispatchKeyEventPre28(final Activity activity, final KeyEvent keyEvent) {
        activity.onUserInteraction();
        final Window window = activity.getWindow();
        if (window.hasFeature(8)) {
            final ActionBar actionBar = activity.getActionBar();
            if (keyEvent.getKeyCode() == 82 && actionBar != null && actionBarOnMenuKeyEventPre28(actionBar, keyEvent)) {
                return true;
            }
        }
        if (window.superDispatchKeyEvent(keyEvent)) {
            return true;
        }
        final View decorView = window.getDecorView();
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(decorView, keyEvent)) {
            return true;
        }
        KeyEvent$DispatcherState keyDispatcherState;
        if (decorView != null) {
            keyDispatcherState = decorView.getKeyDispatcherState();
        }
        else {
            keyDispatcherState = null;
        }
        return keyEvent.dispatch((KeyEvent$Callback)activity, keyDispatcherState, (Object)activity);
    }
    
    private static boolean dialogSuperDispatchKeyEventPre28(final Dialog dialog, final KeyEvent keyEvent) {
        final DialogInterface$OnKeyListener dialogKeyListenerPre28 = getDialogKeyListenerPre28(dialog);
        if (dialogKeyListenerPre28 != null && dialogKeyListenerPre28.onKey((DialogInterface)dialog, keyEvent.getKeyCode(), keyEvent)) {
            return true;
        }
        final Window window = dialog.getWindow();
        if (window.superDispatchKeyEvent(keyEvent)) {
            return true;
        }
        final View decorView = window.getDecorView();
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(decorView, keyEvent)) {
            return true;
        }
        KeyEvent$DispatcherState keyDispatcherState;
        if (decorView != null) {
            keyDispatcherState = decorView.getKeyDispatcherState();
        }
        else {
            keyDispatcherState = null;
        }
        return keyEvent.dispatch((KeyEvent$Callback)dialog, keyDispatcherState, (Object)dialog);
    }
    
    public static boolean dispatchBeforeHierarchy(final View view, final KeyEvent keyEvent) {
        return ViewCompat.dispatchUnhandledKeyEventBeforeHierarchy(view, keyEvent);
    }
    
    public static boolean dispatchKeyEvent(final Component component, final View view, final Window$Callback window$Callback, final KeyEvent keyEvent) {
        boolean b = false;
        if (component == null) {
            return false;
        }
        if (Build$VERSION.SDK_INT >= 28) {
            return component.superDispatchKeyEvent(keyEvent);
        }
        if (window$Callback instanceof Activity) {
            return activitySuperDispatchKeyEventPre28((Activity)window$Callback, keyEvent);
        }
        if (window$Callback instanceof Dialog) {
            return dialogSuperDispatchKeyEventPre28((Dialog)window$Callback, keyEvent);
        }
        if ((view != null && ViewCompat.dispatchUnhandledKeyEventBeforeCallback(view, keyEvent)) || component.superDispatchKeyEvent(keyEvent)) {
            b = true;
        }
        return b;
    }
    
    private static DialogInterface$OnKeyListener getDialogKeyListenerPre28(final Dialog dialog) {
        if (!KeyEventDispatcher.sDialogFieldsFetched) {
            try {
                (KeyEventDispatcher.sDialogKeyListenerField = Dialog.class.getDeclaredField("mOnKeyListener")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {}
            KeyEventDispatcher.sDialogFieldsFetched = true;
        }
        final Field sDialogKeyListenerField = KeyEventDispatcher.sDialogKeyListenerField;
        if (sDialogKeyListenerField != null) {
            try {
                return (DialogInterface$OnKeyListener)sDialogKeyListenerField.get(dialog);
            }
            catch (IllegalAccessException ex2) {}
        }
        return null;
    }
    
    public interface Component
    {
        boolean superDispatchKeyEvent(final KeyEvent p0);
    }
}
