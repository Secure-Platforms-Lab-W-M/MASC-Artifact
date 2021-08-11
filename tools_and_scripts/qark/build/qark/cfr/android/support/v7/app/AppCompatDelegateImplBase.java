/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.View
 *  android.view.Window
 *  android.view.Window$Callback
 */
package android.support.v7.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionMode;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.TintTypedArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;

@RequiresApi(value=14)
abstract class AppCompatDelegateImplBase
extends AppCompatDelegate {
    static final boolean DEBUG = false;
    static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
    private static final boolean SHOULD_INSTALL_EXCEPTION_HANDLER;
    private static boolean sInstalledExceptionHandler;
    private static final int[] sWindowBackgroundStyleable;
    ActionBar mActionBar;
    final AppCompatCallback mAppCompatCallback;
    final Window.Callback mAppCompatWindowCallback;
    final Context mContext;
    private boolean mEatKeyUpEvent;
    boolean mHasActionBar;
    private boolean mIsDestroyed;
    boolean mIsFloating;
    private boolean mIsStarted;
    MenuInflater mMenuInflater;
    final Window.Callback mOriginalWindowCallback;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private CharSequence mTitle;
    final Window mWindow;
    boolean mWindowNoTitle;

    static {
        boolean bl = Build.VERSION.SDK_INT < 21;
        SHOULD_INSTALL_EXCEPTION_HANDLER = bl;
        if (SHOULD_INSTALL_EXCEPTION_HANDLER && !sInstalledExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()){
                final /* synthetic */ Thread.UncaughtExceptionHandler val$defHandler;
                {
                    this.val$defHandler = uncaughtExceptionHandler;
                }

                private boolean shouldWrapException(Throwable object) {
                    if (object instanceof Resources.NotFoundException) {
                        if ((object = object.getMessage()) != null && (object.contains("drawable") || object.contains("Drawable"))) {
                            return true;
                        }
                        return false;
                    }
                    return false;
                }

                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    if (this.shouldWrapException(throwable)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(throwable.getMessage());
                        stringBuilder.append(". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                        stringBuilder = new Resources.NotFoundException(stringBuilder.toString());
                        stringBuilder.initCause(throwable.getCause());
                        stringBuilder.setStackTrace(throwable.getStackTrace());
                        this.val$defHandler.uncaughtException(thread, (Throwable)((Object)stringBuilder));
                        return;
                    }
                    this.val$defHandler.uncaughtException(thread, throwable);
                }
            });
            sInstalledExceptionHandler = true;
        }
        sWindowBackgroundStyleable = new int[]{16842836};
    }

    AppCompatDelegateImplBase(Context object, Window window, AppCompatCallback appCompatCallback) {
        this.mContext = object;
        this.mWindow = window;
        this.mAppCompatCallback = appCompatCallback;
        this.mOriginalWindowCallback = this.mWindow.getCallback();
        window = this.mOriginalWindowCallback;
        if (!(window instanceof AppCompatWindowCallbackBase)) {
            this.mAppCompatWindowCallback = this.wrapWindowCallback((Window.Callback)window);
            this.mWindow.setCallback(this.mAppCompatWindowCallback);
            object = TintTypedArray.obtainStyledAttributes((Context)object, null, sWindowBackgroundStyleable);
            window = object.getDrawableIfKnown(0);
            if (window != null) {
                this.mWindow.setBackgroundDrawable((Drawable)window);
            }
            object.recycle();
            return;
        }
        throw new IllegalStateException("AppCompat has already installed itself into the Window");
    }

    @Override
    public boolean applyDayNight() {
        return false;
    }

    abstract boolean dispatchKeyEvent(KeyEvent var1);

    final Context getActionBarThemedContext() {
        Context context = null;
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            context = actionBar.getThemedContext();
        }
        if (context == null) {
            return this.mContext;
        }
        return context;
    }

    @Override
    public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl();
    }

    @Override
    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.initWindowDecorActionBar();
            ActionBar actionBar = this.mActionBar;
            actionBar = actionBar != null ? actionBar.getThemedContext() : this.mContext;
            this.mMenuInflater = new SupportMenuInflater((Context)actionBar);
        }
        return this.mMenuInflater;
    }

    @Override
    public ActionBar getSupportActionBar() {
        this.initWindowDecorActionBar();
        return this.mActionBar;
    }

    final CharSequence getTitle() {
        Window.Callback callback = this.mOriginalWindowCallback;
        if (callback instanceof Activity) {
            return ((Activity)callback).getTitle();
        }
        return this.mTitle;
    }

    final Window.Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }

    abstract void initWindowDecorActionBar();

    final boolean isDestroyed() {
        return this.mIsDestroyed;
    }

    @Override
    public boolean isHandleNativeActionModesEnabled() {
        return false;
    }

    final boolean isStarted() {
        return this.mIsStarted;
    }

    @Override
    public void onDestroy() {
        this.mIsDestroyed = true;
    }

    abstract boolean onKeyShortcut(int var1, KeyEvent var2);

    abstract boolean onMenuOpened(int var1, Menu var2);

    abstract void onPanelClosed(int var1, Menu var2);

    @Override
    public void onSaveInstanceState(Bundle bundle) {
    }

    @Override
    public void onStart() {
        this.mIsStarted = true;
    }

    @Override
    public void onStop() {
        this.mIsStarted = false;
    }

    abstract void onTitleChanged(CharSequence var1);

    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }

    @Override
    public void setHandleNativeActionModesEnabled(boolean bl) {
    }

    @Override
    public void setLocalNightMode(int n) {
    }

    @Override
    public final void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        this.onTitleChanged(charSequence);
    }

    abstract ActionMode startSupportActionModeFromWindow(ActionMode.Callback var1);

    Window.Callback wrapWindowCallback(Window.Callback callback) {
        return new AppCompatWindowCallbackBase(callback);
    }

    private class ActionBarDrawableToggleImpl
    implements ActionBarDrawerToggle.Delegate {
        ActionBarDrawableToggleImpl() {
        }

        @Override
        public Context getActionBarThemedContext() {
            return AppCompatDelegateImplBase.this.getActionBarThemedContext();
        }

        @Override
        public Drawable getThemeUpIndicator() {
            TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.getActionBarThemedContext(), null, new int[]{R.attr.homeAsUpIndicator});
            Drawable drawable2 = tintTypedArray.getDrawable(0);
            tintTypedArray.recycle();
            return drawable2;
        }

        @Override
        public boolean isNavigationVisible() {
            ActionBar actionBar = AppCompatDelegateImplBase.this.getSupportActionBar();
            if (actionBar != null && (actionBar.getDisplayOptions() & 4) != 0) {
                return true;
            }
            return false;
        }

        @Override
        public void setActionBarDescription(int n) {
            ActionBar actionBar = AppCompatDelegateImplBase.this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeActionContentDescription(n);
                return;
            }
        }

        @Override
        public void setActionBarUpIndicator(Drawable drawable2, int n) {
            ActionBar actionBar = AppCompatDelegateImplBase.this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(drawable2);
                actionBar.setHomeActionContentDescription(n);
                return;
            }
        }
    }

    class AppCompatWindowCallbackBase
    extends WindowCallbackWrapper {
        AppCompatWindowCallbackBase(Window.Callback callback) {
            super(callback);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (!AppCompatDelegateImplBase.this.dispatchKeyEvent(keyEvent) && !super.dispatchKeyEvent(keyEvent)) {
                return false;
            }
            return true;
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            if (!super.dispatchKeyShortcutEvent(keyEvent) && !AppCompatDelegateImplBase.this.onKeyShortcut(keyEvent.getKeyCode(), keyEvent)) {
                return false;
            }
            return true;
        }

        @Override
        public void onContentChanged() {
        }

        @Override
        public boolean onCreatePanelMenu(int n, Menu menu) {
            if (n == 0 && !(menu instanceof MenuBuilder)) {
                return false;
            }
            return super.onCreatePanelMenu(n, menu);
        }

        @Override
        public boolean onMenuOpened(int n, Menu menu) {
            super.onMenuOpened(n, menu);
            AppCompatDelegateImplBase.this.onMenuOpened(n, menu);
            return true;
        }

        @Override
        public void onPanelClosed(int n, Menu menu) {
            super.onPanelClosed(n, menu);
            AppCompatDelegateImplBase.this.onPanelClosed(n, menu);
        }

        @Override
        public boolean onPreparePanel(int n, View view, Menu menu) {
            MenuBuilder menuBuilder = menu instanceof MenuBuilder ? (MenuBuilder)menu : null;
            if (n == 0 && menuBuilder == null) {
                return false;
            }
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(true);
            }
            boolean bl = super.onPreparePanel(n, view, menu);
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(false);
                return bl;
            }
            return bl;
        }
    }

}

