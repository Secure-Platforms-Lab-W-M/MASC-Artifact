package android.support.v7.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v7.appcompat.R$attr;
import android.support.v7.view.ActionMode;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.Window.Callback;
import java.lang.Thread.UncaughtExceptionHandler;

@RequiresApi(14)
abstract class AppCompatDelegateImplBase extends AppCompatDelegate {
   static final boolean DEBUG = false;
   static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
   private static final boolean SHOULD_INSTALL_EXCEPTION_HANDLER;
   private static boolean sInstalledExceptionHandler;
   private static final int[] sWindowBackgroundStyleable;
   ActionBar mActionBar;
   final AppCompatCallback mAppCompatCallback;
   final Callback mAppCompatWindowCallback;
   final Context mContext;
   private boolean mEatKeyUpEvent;
   boolean mHasActionBar;
   private boolean mIsDestroyed;
   boolean mIsFloating;
   private boolean mIsStarted;
   MenuInflater mMenuInflater;
   final Callback mOriginalWindowCallback;
   boolean mOverlayActionBar;
   boolean mOverlayActionMode;
   private CharSequence mTitle;
   final Window mWindow;
   boolean mWindowNoTitle;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      SHOULD_INSTALL_EXCEPTION_HANDLER = var0;
      if (SHOULD_INSTALL_EXCEPTION_HANDLER && !sInstalledExceptionHandler) {
         Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()) {
            // $FF: synthetic field
            final UncaughtExceptionHandler val$defHandler;

            {
               this.val$defHandler = var1;
            }

            private boolean shouldWrapException(Throwable var1) {
               if (!(var1 instanceof NotFoundException)) {
                  return false;
               } else {
                  String var2 = var1.getMessage();
                  return var2 != null && (var2.contains("drawable") || var2.contains("Drawable"));
               }
            }

            public void uncaughtException(Thread var1, Throwable var2) {
               if (this.shouldWrapException(var2)) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append(var2.getMessage());
                  var3.append(". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                  NotFoundException var4 = new NotFoundException(var3.toString());
                  var4.initCause(var2.getCause());
                  var4.setStackTrace(var2.getStackTrace());
                  this.val$defHandler.uncaughtException(var1, var4);
               } else {
                  this.val$defHandler.uncaughtException(var1, var2);
               }
            }
         });
         sInstalledExceptionHandler = true;
      }

      sWindowBackgroundStyleable = new int[]{16842836};
   }

   AppCompatDelegateImplBase(Context var1, Window var2, AppCompatCallback var3) {
      this.mContext = var1;
      this.mWindow = var2;
      this.mAppCompatCallback = var3;
      this.mOriginalWindowCallback = this.mWindow.getCallback();
      Callback var5 = this.mOriginalWindowCallback;
      if (!(var5 instanceof AppCompatDelegateImplBase.AppCompatWindowCallbackBase)) {
         this.mAppCompatWindowCallback = this.wrapWindowCallback(var5);
         this.mWindow.setCallback(this.mAppCompatWindowCallback);
         TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(var1, (AttributeSet)null, sWindowBackgroundStyleable);
         Drawable var6 = var4.getDrawableIfKnown(0);
         if (var6 != null) {
            this.mWindow.setBackgroundDrawable(var6);
         }

         var4.recycle();
      } else {
         throw new IllegalStateException("AppCompat has already installed itself into the Window");
      }
   }

   public boolean applyDayNight() {
      return false;
   }

   abstract boolean dispatchKeyEvent(KeyEvent var1);

   final Context getActionBarThemedContext() {
      Context var1 = null;
      ActionBar var2 = this.getSupportActionBar();
      if (var2 != null) {
         var1 = var2.getThemedContext();
      }

      Context var3 = var1;
      if (var1 == null) {
         var3 = this.mContext;
      }

      return var3;
   }

   public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
      return new AppCompatDelegateImplBase.ActionBarDrawableToggleImpl();
   }

   public MenuInflater getMenuInflater() {
      if (this.mMenuInflater == null) {
         this.initWindowDecorActionBar();
         ActionBar var1 = this.mActionBar;
         Context var2;
         if (var1 != null) {
            var2 = var1.getThemedContext();
         } else {
            var2 = this.mContext;
         }

         this.mMenuInflater = new SupportMenuInflater(var2);
      }

      return this.mMenuInflater;
   }

   public ActionBar getSupportActionBar() {
      this.initWindowDecorActionBar();
      return this.mActionBar;
   }

   final CharSequence getTitle() {
      Callback var1 = this.mOriginalWindowCallback;
      return var1 instanceof Activity ? ((Activity)var1).getTitle() : this.mTitle;
   }

   final Callback getWindowCallback() {
      return this.mWindow.getCallback();
   }

   abstract void initWindowDecorActionBar();

   final boolean isDestroyed() {
      return this.mIsDestroyed;
   }

   public boolean isHandleNativeActionModesEnabled() {
      return false;
   }

   final boolean isStarted() {
      return this.mIsStarted;
   }

   public void onDestroy() {
      this.mIsDestroyed = true;
   }

   abstract boolean onKeyShortcut(int var1, KeyEvent var2);

   abstract boolean onMenuOpened(int var1, Menu var2);

   abstract void onPanelClosed(int var1, Menu var2);

   public void onSaveInstanceState(Bundle var1) {
   }

   public void onStart() {
      this.mIsStarted = true;
   }

   public void onStop() {
      this.mIsStarted = false;
   }

   abstract void onTitleChanged(CharSequence var1);

   final ActionBar peekSupportActionBar() {
      return this.mActionBar;
   }

   public void setHandleNativeActionModesEnabled(boolean var1) {
   }

   public void setLocalNightMode(int var1) {
   }

   public final void setTitle(CharSequence var1) {
      this.mTitle = var1;
      this.onTitleChanged(var1);
   }

   abstract ActionMode startSupportActionModeFromWindow(ActionMode.Callback var1);

   Callback wrapWindowCallback(Callback var1) {
      return new AppCompatDelegateImplBase.AppCompatWindowCallbackBase(var1);
   }

   private class ActionBarDrawableToggleImpl implements ActionBarDrawerToggle.Delegate {
      ActionBarDrawableToggleImpl() {
      }

      public Context getActionBarThemedContext() {
         return AppCompatDelegateImplBase.this.getActionBarThemedContext();
      }

      public Drawable getThemeUpIndicator() {
         TintTypedArray var1 = TintTypedArray.obtainStyledAttributes(this.getActionBarThemedContext(), (AttributeSet)null, new int[]{R$attr.homeAsUpIndicator});
         Drawable var2 = var1.getDrawable(0);
         var1.recycle();
         return var2;
      }

      public boolean isNavigationVisible() {
         ActionBar var1 = AppCompatDelegateImplBase.this.getSupportActionBar();
         return var1 != null && (var1.getDisplayOptions() & 4) != 0;
      }

      public void setActionBarDescription(int var1) {
         ActionBar var2 = AppCompatDelegateImplBase.this.getSupportActionBar();
         if (var2 != null) {
            var2.setHomeActionContentDescription(var1);
         }

      }

      public void setActionBarUpIndicator(Drawable var1, int var2) {
         ActionBar var3 = AppCompatDelegateImplBase.this.getSupportActionBar();
         if (var3 != null) {
            var3.setHomeAsUpIndicator(var1);
            var3.setHomeActionContentDescription(var2);
         }

      }
   }

   class AppCompatWindowCallbackBase extends WindowCallbackWrapper {
      AppCompatWindowCallbackBase(Callback var2) {
         super(var2);
      }

      public boolean dispatchKeyEvent(KeyEvent var1) {
         return AppCompatDelegateImplBase.this.dispatchKeyEvent(var1) || super.dispatchKeyEvent(var1);
      }

      public boolean dispatchKeyShortcutEvent(KeyEvent var1) {
         return super.dispatchKeyShortcutEvent(var1) || AppCompatDelegateImplBase.this.onKeyShortcut(var1.getKeyCode(), var1);
      }

      public void onContentChanged() {
      }

      public boolean onCreatePanelMenu(int var1, Menu var2) {
         return var1 == 0 && !(var2 instanceof MenuBuilder) ? false : super.onCreatePanelMenu(var1, var2);
      }

      public boolean onMenuOpened(int var1, Menu var2) {
         super.onMenuOpened(var1, var2);
         AppCompatDelegateImplBase.this.onMenuOpened(var1, var2);
         return true;
      }

      public void onPanelClosed(int var1, Menu var2) {
         super.onPanelClosed(var1, var2);
         AppCompatDelegateImplBase.this.onPanelClosed(var1, var2);
      }

      public boolean onPreparePanel(int var1, View var2, Menu var3) {
         MenuBuilder var5;
         if (var3 instanceof MenuBuilder) {
            var5 = (MenuBuilder)var3;
         } else {
            var5 = null;
         }

         if (var1 == 0 && var5 == null) {
            return false;
         } else {
            if (var5 != null) {
               var5.setOverrideVisibleItems(true);
            }

            boolean var4 = super.onPreparePanel(var1, var2, var3);
            if (var5 != null) {
               var5.setOverrideVisibleItems(false);
            }

            return var4;
         }
      }
   }
}
