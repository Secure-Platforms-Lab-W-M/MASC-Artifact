package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.LayoutInflater.Factory2;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window.Callback;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R.attr;
import androidx.appcompat.R.color;
import androidx.appcompat.R.id;
import androidx.appcompat.R.layout;
import androidx.appcompat.R.style;
import androidx.appcompat.R.styleable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.StandaloneActionMode;
import androidx.appcompat.view.SupportActionModeWrapper;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.DecorContentParent;
import androidx.appcompat.widget.FitWindowsViewGroup;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.appcompat.widget.ViewStubCompat;
import androidx.appcompat.widget.ViewUtils;
import androidx.collection.ArrayMap;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.view.KeyEventDispatcher;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.PopupWindowCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.Callback, Factory2 {
   private static final boolean DEBUG = false;
   static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
   private static final boolean IS_PRE_LOLLIPOP;
   private static final boolean sAlwaysOverrideConfiguration;
   private static boolean sInstalledExceptionHandler;
   private static final Map sLocalNightModes = new ArrayMap();
   private static final int[] sWindowBackgroundStyleable;
   ActionBar mActionBar;
   private AppCompatDelegateImpl.ActionMenuPresenterCallback mActionMenuPresenterCallback;
   ActionMode mActionMode;
   PopupWindow mActionModePopup;
   ActionBarContextView mActionModeView;
   private boolean mActivityHandlesUiMode;
   private boolean mActivityHandlesUiModeChecked;
   final AppCompatCallback mAppCompatCallback;
   private AppCompatViewInflater mAppCompatViewInflater;
   private AppCompatDelegateImpl.AppCompatWindowCallback mAppCompatWindowCallback;
   private AppCompatDelegateImpl.AutoNightModeManager mAutoBatteryNightModeManager;
   private AppCompatDelegateImpl.AutoNightModeManager mAutoTimeNightModeManager;
   private boolean mBaseContextAttached;
   private boolean mClosingActionMenu;
   final Context mContext;
   private boolean mCreated;
   private DecorContentParent mDecorContentParent;
   private boolean mEnableDefaultActionBarUp;
   ViewPropertyAnimatorCompat mFadeAnim;
   private boolean mFeatureIndeterminateProgress;
   private boolean mFeatureProgress;
   private boolean mHandleNativeActionModes;
   boolean mHasActionBar;
   final Object mHost;
   int mInvalidatePanelMenuFeatures;
   boolean mInvalidatePanelMenuPosted;
   private final Runnable mInvalidatePanelMenuRunnable;
   boolean mIsDestroyed;
   boolean mIsFloating;
   private int mLocalNightMode;
   private boolean mLongPressBackDown;
   MenuInflater mMenuInflater;
   boolean mOverlayActionBar;
   boolean mOverlayActionMode;
   private AppCompatDelegateImpl.PanelMenuPresenterCallback mPanelMenuPresenterCallback;
   private AppCompatDelegateImpl.PanelFeatureState[] mPanels;
   private AppCompatDelegateImpl.PanelFeatureState mPreparedPanel;
   Runnable mShowActionModePopup;
   private boolean mStarted;
   private View mStatusGuard;
   private ViewGroup mSubDecor;
   private boolean mSubDecorInstalled;
   private Rect mTempRect1;
   private Rect mTempRect2;
   private int mThemeResId;
   private CharSequence mTitle;
   private TextView mTitleView;
   Window mWindow;
   boolean mWindowNoTitle;

   static {
      int var0 = VERSION.SDK_INT;
      boolean var2 = false;
      boolean var1;
      if (var0 < 21) {
         var1 = true;
      } else {
         var1 = false;
      }

      IS_PRE_LOLLIPOP = var1;
      sWindowBackgroundStyleable = new int[]{16842836};
      var1 = var2;
      if (VERSION.SDK_INT >= 21) {
         var1 = var2;
         if (VERSION.SDK_INT <= 25) {
            var1 = true;
         }
      }

      sAlwaysOverrideConfiguration = var1;
      if (IS_PRE_LOLLIPOP && !sInstalledExceptionHandler) {
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

   }

   AppCompatDelegateImpl(Activity var1, AppCompatCallback var2) {
      this(var1, (Window)null, var2, var1);
   }

   AppCompatDelegateImpl(Dialog var1, AppCompatCallback var2) {
      this(var1.getContext(), var1.getWindow(), var2, var1);
   }

   AppCompatDelegateImpl(Context var1, Activity var2, AppCompatCallback var3) {
      this(var1, (Window)null, var3, var2);
   }

   AppCompatDelegateImpl(Context var1, Window var2, AppCompatCallback var3) {
      this(var1, var2, var3, var1);
   }

   private AppCompatDelegateImpl(Context var1, Window var2, AppCompatCallback var3, Object var4) {
      this.mFadeAnim = null;
      this.mHandleNativeActionModes = true;
      this.mLocalNightMode = -100;
      this.mInvalidatePanelMenuRunnable = new Runnable() {
         public void run() {
            if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 1) != 0) {
               AppCompatDelegateImpl.this.doInvalidatePanelMenu(0);
            }

            if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 4096) != 0) {
               AppCompatDelegateImpl.this.doInvalidatePanelMenu(108);
            }

            AppCompatDelegateImpl.this.mInvalidatePanelMenuPosted = false;
            AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures = 0;
         }
      };
      this.mContext = var1;
      this.mAppCompatCallback = var3;
      this.mHost = var4;
      if (this.mLocalNightMode == -100 && var4 instanceof Dialog) {
         AppCompatActivity var5 = this.tryUnwrapContext();
         if (var5 != null) {
            this.mLocalNightMode = var5.getDelegate().getLocalNightMode();
         }
      }

      if (this.mLocalNightMode == -100) {
         Integer var6 = (Integer)sLocalNightModes.get(this.mHost.getClass());
         if (var6 != null) {
            this.mLocalNightMode = var6;
            sLocalNightModes.remove(this.mHost.getClass());
         }
      }

      if (var2 != null) {
         this.attachToWindow(var2);
      }

      AppCompatDrawableManager.preload();
   }

   private boolean applyDayNight(boolean var1) {
      if (this.mIsDestroyed) {
         return false;
      } else {
         int var2 = this.calculateNightMode();
         var1 = this.updateForNightMode(this.mapNightMode(var2), var1);
         AppCompatDelegateImpl.AutoNightModeManager var3;
         if (var2 == 0) {
            this.getAutoTimeNightModeManager().setup();
         } else {
            var3 = this.mAutoTimeNightModeManager;
            if (var3 != null) {
               var3.cleanup();
            }
         }

         if (var2 == 3) {
            this.getAutoBatteryNightModeManager().setup();
            return var1;
         } else {
            var3 = this.mAutoBatteryNightModeManager;
            if (var3 != null) {
               var3.cleanup();
            }

            return var1;
         }
      }
   }

   private void applyFixedSizeWindow() {
      ContentFrameLayout var1 = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
      View var2 = this.mWindow.getDecorView();
      var1.setDecorPadding(var2.getPaddingLeft(), var2.getPaddingTop(), var2.getPaddingRight(), var2.getPaddingBottom());
      TypedArray var3 = this.mContext.obtainStyledAttributes(styleable.AppCompatTheme);
      var3.getValue(styleable.AppCompatTheme_windowMinWidthMajor, var1.getMinWidthMajor());
      var3.getValue(styleable.AppCompatTheme_windowMinWidthMinor, var1.getMinWidthMinor());
      if (var3.hasValue(styleable.AppCompatTheme_windowFixedWidthMajor)) {
         var3.getValue(styleable.AppCompatTheme_windowFixedWidthMajor, var1.getFixedWidthMajor());
      }

      if (var3.hasValue(styleable.AppCompatTheme_windowFixedWidthMinor)) {
         var3.getValue(styleable.AppCompatTheme_windowFixedWidthMinor, var1.getFixedWidthMinor());
      }

      if (var3.hasValue(styleable.AppCompatTheme_windowFixedHeightMajor)) {
         var3.getValue(styleable.AppCompatTheme_windowFixedHeightMajor, var1.getFixedHeightMajor());
      }

      if (var3.hasValue(styleable.AppCompatTheme_windowFixedHeightMinor)) {
         var3.getValue(styleable.AppCompatTheme_windowFixedHeightMinor, var1.getFixedHeightMinor());
      }

      var3.recycle();
      var1.requestLayout();
   }

   private void attachToWindow(Window var1) {
      if (this.mWindow == null) {
         Callback var2 = var1.getCallback();
         if (!(var2 instanceof AppCompatDelegateImpl.AppCompatWindowCallback)) {
            AppCompatDelegateImpl.AppCompatWindowCallback var4 = new AppCompatDelegateImpl.AppCompatWindowCallback(var2);
            this.mAppCompatWindowCallback = var4;
            var1.setCallback(var4);
            TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(this.mContext, (AttributeSet)null, sWindowBackgroundStyleable);
            Drawable var3 = var5.getDrawableIfKnown(0);
            if (var3 != null) {
               var1.setBackgroundDrawable(var3);
            }

            var5.recycle();
            this.mWindow = var1;
         } else {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
         }
      } else {
         throw new IllegalStateException("AppCompat has already installed itself into the Window");
      }
   }

   private int calculateNightMode() {
      int var1 = this.mLocalNightMode;
      return var1 != -100 ? var1 : getDefaultNightMode();
   }

   private void cleanupAutoManagers() {
      AppCompatDelegateImpl.AutoNightModeManager var1 = this.mAutoTimeNightModeManager;
      if (var1 != null) {
         var1.cleanup();
      }

      var1 = this.mAutoBatteryNightModeManager;
      if (var1 != null) {
         var1.cleanup();
      }

   }

   private ViewGroup createSubDecor() {
      TypedArray var1 = this.mContext.obtainStyledAttributes(styleable.AppCompatTheme);
      if (var1.hasValue(styleable.AppCompatTheme_windowActionBar)) {
         if (var1.getBoolean(styleable.AppCompatTheme_windowNoTitle, false)) {
            this.requestWindowFeature(1);
         } else if (var1.getBoolean(styleable.AppCompatTheme_windowActionBar, false)) {
            this.requestWindowFeature(108);
         }

         if (var1.getBoolean(styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            this.requestWindowFeature(109);
         }

         if (var1.getBoolean(styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            this.requestWindowFeature(10);
         }

         this.mIsFloating = var1.getBoolean(styleable.AppCompatTheme_android_windowIsFloating, false);
         var1.recycle();
         this.ensureWindow();
         this.mWindow.getDecorView();
         LayoutInflater var2 = LayoutInflater.from(this.mContext);
         ViewGroup var5 = null;
         if (!this.mWindowNoTitle) {
            if (this.mIsFloating) {
               var5 = (ViewGroup)var2.inflate(layout.abc_dialog_title_material, (ViewGroup)null);
               this.mOverlayActionBar = false;
               this.mHasActionBar = false;
            } else if (this.mHasActionBar) {
               TypedValue var6 = new TypedValue();
               this.mContext.getTheme().resolveAttribute(attr.actionBarTheme, var6, true);
               Object var7;
               if (var6.resourceId != 0) {
                  var7 = new ContextThemeWrapper(this.mContext, var6.resourceId);
               } else {
                  var7 = this.mContext;
               }

               var5 = (ViewGroup)LayoutInflater.from((Context)var7).inflate(layout.abc_screen_toolbar, (ViewGroup)null);
               DecorContentParent var8 = (DecorContentParent)var5.findViewById(id.decor_content_parent);
               this.mDecorContentParent = var8;
               var8.setWindowCallback(this.getWindowCallback());
               if (this.mOverlayActionBar) {
                  this.mDecorContentParent.initFeature(109);
               }

               if (this.mFeatureProgress) {
                  this.mDecorContentParent.initFeature(2);
               }

               if (this.mFeatureIndeterminateProgress) {
                  this.mDecorContentParent.initFeature(5);
               }
            }
         } else {
            if (this.mOverlayActionMode) {
               var5 = (ViewGroup)var2.inflate(layout.abc_screen_simple_overlay_action_mode, (ViewGroup)null);
            } else {
               var5 = (ViewGroup)var2.inflate(layout.abc_screen_simple, (ViewGroup)null);
            }

            if (VERSION.SDK_INT >= 21) {
               ViewCompat.setOnApplyWindowInsetsListener(var5, new OnApplyWindowInsetsListener() {
                  public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
                     int var3 = var2.getSystemWindowInsetTop();
                     int var4 = AppCompatDelegateImpl.this.updateStatusGuard(var3);
                     WindowInsetsCompat var5 = var2;
                     if (var3 != var4) {
                        var5 = var2.replaceSystemWindowInsets(var2.getSystemWindowInsetLeft(), var4, var2.getSystemWindowInsetRight(), var2.getSystemWindowInsetBottom());
                     }

                     return ViewCompat.onApplyWindowInsets(var1, var5);
                  }
               });
            } else {
               ((FitWindowsViewGroup)var5).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                  public void onFitSystemWindows(Rect var1) {
                     var1.top = AppCompatDelegateImpl.this.updateStatusGuard(var1.top);
                  }
               });
            }
         }

         if (var5 != null) {
            if (this.mDecorContentParent == null) {
               this.mTitleView = (TextView)var5.findViewById(id.title);
            }

            ViewUtils.makeOptionalFitsSystemWindows(var5);
            ContentFrameLayout var9 = (ContentFrameLayout)var5.findViewById(id.action_bar_activity_content);
            ViewGroup var3 = (ViewGroup)this.mWindow.findViewById(16908290);
            if (var3 != null) {
               while(var3.getChildCount() > 0) {
                  View var4 = var3.getChildAt(0);
                  var3.removeViewAt(0);
                  var9.addView(var4);
               }

               var3.setId(-1);
               var9.setId(16908290);
               if (var3 instanceof FrameLayout) {
                  ((FrameLayout)var3).setForeground((Drawable)null);
               }
            }

            this.mWindow.setContentView(var5);
            var9.setAttachListener(new ContentFrameLayout.OnAttachListener() {
               public void onAttachedFromWindow() {
               }

               public void onDetachedFromWindow() {
                  AppCompatDelegateImpl.this.dismissPopups();
               }
            });
            return var5;
         } else {
            StringBuilder var10 = new StringBuilder();
            var10.append("AppCompat does not support the current theme features: { windowActionBar: ");
            var10.append(this.mHasActionBar);
            var10.append(", windowActionBarOverlay: ");
            var10.append(this.mOverlayActionBar);
            var10.append(", android:windowIsFloating: ");
            var10.append(this.mIsFloating);
            var10.append(", windowActionModeOverlay: ");
            var10.append(this.mOverlayActionMode);
            var10.append(", windowNoTitle: ");
            var10.append(this.mWindowNoTitle);
            var10.append(" }");
            throw new IllegalArgumentException(var10.toString());
         }
      } else {
         var1.recycle();
         throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
      }
   }

   private void ensureSubDecor() {
      if (!this.mSubDecorInstalled) {
         this.mSubDecor = this.createSubDecor();
         CharSequence var1 = this.getTitle();
         if (!TextUtils.isEmpty(var1)) {
            DecorContentParent var2 = this.mDecorContentParent;
            if (var2 != null) {
               var2.setWindowTitle(var1);
            } else if (this.peekSupportActionBar() != null) {
               this.peekSupportActionBar().setWindowTitle(var1);
            } else {
               TextView var4 = this.mTitleView;
               if (var4 != null) {
                  var4.setText(var1);
               }
            }
         }

         this.applyFixedSizeWindow();
         this.onSubDecorInstalled(this.mSubDecor);
         this.mSubDecorInstalled = true;
         AppCompatDelegateImpl.PanelFeatureState var3 = this.getPanelState(0, false);
         if (!this.mIsDestroyed && (var3 == null || var3.menu == null)) {
            this.invalidatePanelMenu(108);
         }
      }

   }

   private void ensureWindow() {
      if (this.mWindow == null) {
         Object var1 = this.mHost;
         if (var1 instanceof Activity) {
            this.attachToWindow(((Activity)var1).getWindow());
         }
      }

      if (this.mWindow == null) {
         throw new IllegalStateException("We have not been given a Window");
      }
   }

   private AppCompatDelegateImpl.AutoNightModeManager getAutoBatteryNightModeManager() {
      if (this.mAutoBatteryNightModeManager == null) {
         this.mAutoBatteryNightModeManager = new AppCompatDelegateImpl.AutoBatteryNightModeManager(this.mContext);
      }

      return this.mAutoBatteryNightModeManager;
   }

   private void initWindowDecorActionBar() {
      this.ensureSubDecor();
      if (this.mHasActionBar) {
         if (this.mActionBar == null) {
            Object var1 = this.mHost;
            if (var1 instanceof Activity) {
               this.mActionBar = new WindowDecorActionBar((Activity)this.mHost, this.mOverlayActionBar);
            } else if (var1 instanceof Dialog) {
               this.mActionBar = new WindowDecorActionBar((Dialog)this.mHost);
            }

            ActionBar var2 = this.mActionBar;
            if (var2 != null) {
               var2.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            }

         }
      }
   }

   private boolean initializePanelContent(AppCompatDelegateImpl.PanelFeatureState var1) {
      if (var1.createdPanelView != null) {
         var1.shownPanelView = var1.createdPanelView;
         return true;
      } else if (var1.menu == null) {
         return false;
      } else {
         if (this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new AppCompatDelegateImpl.PanelMenuPresenterCallback();
         }

         var1.shownPanelView = (View)var1.getListMenuView(this.mPanelMenuPresenterCallback);
         return var1.shownPanelView != null;
      }
   }

   private boolean initializePanelDecor(AppCompatDelegateImpl.PanelFeatureState var1) {
      var1.setStyle(this.getActionBarThemedContext());
      var1.decorView = new AppCompatDelegateImpl.ListMenuDecorView(var1.listPresenterContext);
      var1.gravity = 81;
      return true;
   }

   private boolean initializePanelMenu(AppCompatDelegateImpl.PanelFeatureState var1) {
      Object var2;
      label28: {
         Context var4 = this.mContext;
         if (var1.featureId != 0) {
            var2 = var4;
            if (var1.featureId != 108) {
               break label28;
            }
         }

         var2 = var4;
         if (this.mDecorContentParent != null) {
            TypedValue var5 = new TypedValue();
            Theme var6 = var4.getTheme();
            var6.resolveAttribute(attr.actionBarTheme, var5, true);
            Theme var7 = null;
            if (var5.resourceId != 0) {
               var7 = var4.getResources().newTheme();
               var7.setTo(var6);
               var7.applyStyle(var5.resourceId, true);
               var7.resolveAttribute(attr.actionBarWidgetTheme, var5, true);
            } else {
               var6.resolveAttribute(attr.actionBarWidgetTheme, var5, true);
            }

            Theme var3 = var7;
            if (var5.resourceId != 0) {
               var3 = var7;
               if (var7 == null) {
                  var3 = var4.getResources().newTheme();
                  var3.setTo(var6);
               }

               var3.applyStyle(var5.resourceId, true);
            }

            var2 = var4;
            if (var3 != null) {
               var2 = new ContextThemeWrapper(var4, 0);
               ((Context)var2).getTheme().setTo(var3);
            }
         }
      }

      MenuBuilder var8 = new MenuBuilder((Context)var2);
      var8.setCallback(this);
      var1.setMenu(var8);
      return true;
   }

   private void invalidatePanelMenu(int var1) {
      this.mInvalidatePanelMenuFeatures |= 1 << var1;
      if (!this.mInvalidatePanelMenuPosted) {
         ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
         this.mInvalidatePanelMenuPosted = true;
      }

   }

   private boolean isActivityManifestHandlingUiMode() {
      if (!this.mActivityHandlesUiModeChecked && this.mHost instanceof Activity) {
         label45: {
            PackageManager var2 = this.mContext.getPackageManager();
            if (var2 == null) {
               return false;
            }

            NameNotFoundException var10000;
            label46: {
               boolean var10001;
               ActivityInfo var6;
               try {
                  var6 = var2.getActivityInfo(new ComponentName(this.mContext, this.mHost.getClass()), 0);
               } catch (NameNotFoundException var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label46;
               }

               boolean var1;
               label34: {
                  label33: {
                     if (var6 != null) {
                        try {
                           if ((var6.configChanges & 512) != 0) {
                              break label33;
                           }
                        } catch (NameNotFoundException var4) {
                           var10000 = var4;
                           var10001 = false;
                           break label46;
                        }
                     }

                     var1 = false;
                     break label34;
                  }

                  var1 = true;
               }

               try {
                  this.mActivityHandlesUiMode = var1;
                  break label45;
               } catch (NameNotFoundException var3) {
                  var10000 = var3;
                  var10001 = false;
               }
            }

            NameNotFoundException var7 = var10000;
            Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", var7);
            this.mActivityHandlesUiMode = false;
         }
      }

      this.mActivityHandlesUiModeChecked = true;
      return this.mActivityHandlesUiMode;
   }

   private boolean onKeyDownPanel(int var1, KeyEvent var2) {
      if (var2.getRepeatCount() == 0) {
         AppCompatDelegateImpl.PanelFeatureState var3 = this.getPanelState(var1, true);
         if (!var3.isOpen) {
            return this.preparePanel(var3, var2);
         }
      }

      return false;
   }

   private boolean onKeyUpPanel(int var1, KeyEvent var2) {
      if (this.mActionMode != null) {
         return false;
      } else {
         boolean var3;
         label54: {
            boolean var5 = false;
            AppCompatDelegateImpl.PanelFeatureState var6 = this.getPanelState(var1, true);
            if (var1 == 0) {
               DecorContentParent var7 = this.mDecorContentParent;
               if (var7 != null && var7.canShowOverflowMenu() && !ViewConfiguration.get(this.mContext).hasPermanentMenuKey()) {
                  if (!this.mDecorContentParent.isOverflowMenuShowing()) {
                     var3 = var5;
                     if (!this.mIsDestroyed) {
                        var3 = var5;
                        if (this.preparePanel(var6, var2)) {
                           var3 = this.mDecorContentParent.showOverflowMenu();
                        }
                     }
                  } else {
                     var3 = this.mDecorContentParent.hideOverflowMenu();
                  }
                  break label54;
               }
            }

            if (!var6.isOpen && !var6.isHandled) {
               var3 = var5;
               if (var6.isPrepared) {
                  boolean var4 = true;
                  if (var6.refreshMenuContent) {
                     var6.isPrepared = false;
                     var4 = this.preparePanel(var6, var2);
                  }

                  var3 = var5;
                  if (var4) {
                     this.openPanel(var6, var2);
                     var3 = true;
                  }
               }
            } else {
               var3 = var6.isOpen;
               this.closePanel(var6, true);
            }
         }

         if (var3) {
            AudioManager var8 = (AudioManager)this.mContext.getSystemService("audio");
            if (var8 != null) {
               var8.playSoundEffect(0);
               return var3;
            }

            Log.w("AppCompatDelegate", "Couldn't get audio manager");
         }

         return var3;
      }
   }

   private void openPanel(AppCompatDelegateImpl.PanelFeatureState var1, KeyEvent var2) {
      if (!var1.isOpen) {
         if (!this.mIsDestroyed) {
            if (var1.featureId == 0) {
               boolean var3;
               if ((this.mContext.getResources().getConfiguration().screenLayout & 15) == 4) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               if (var3) {
                  return;
               }
            }

            Callback var5 = this.getWindowCallback();
            if (var5 != null && !var5.onMenuOpened(var1.featureId, var1.menu)) {
               this.closePanel(var1, true);
            } else {
               WindowManager var6 = (WindowManager)this.mContext.getSystemService("window");
               if (var6 != null) {
                  if (this.preparePanel(var1, var2)) {
                     byte var10;
                     label98: {
                        byte var4 = -2;
                        LayoutParams var7;
                        if (var1.decorView != null && !var1.refreshDecorView) {
                           if (var1.createdPanelView != null) {
                              var7 = var1.createdPanelView.getLayoutParams();
                              var10 = var4;
                              if (var7 != null) {
                                 var10 = var4;
                                 if (var7.width == -1) {
                                    var10 = -1;
                                 }
                              }
                              break label98;
                           }
                        } else {
                           if (var1.decorView == null) {
                              if (!this.initializePanelDecor(var1) || var1.decorView == null) {
                                 return;
                              }
                           } else if (var1.refreshDecorView && var1.decorView.getChildCount() > 0) {
                              var1.decorView.removeAllViews();
                           }

                           if (!this.initializePanelContent(var1)) {
                              return;
                           }

                           if (!var1.hasPanelItems()) {
                              return;
                           }

                           LayoutParams var11 = var1.shownPanelView.getLayoutParams();
                           var7 = var11;
                           if (var11 == null) {
                              var7 = new LayoutParams(-2, -2);
                           }

                           int var9 = var1.background;
                           var1.decorView.setBackgroundResource(var9);
                           ViewParent var12 = var1.shownPanelView.getParent();
                           if (var12 instanceof ViewGroup) {
                              ((ViewGroup)var12).removeView(var1.shownPanelView);
                           }

                           var1.decorView.addView(var1.shownPanelView, var7);
                           if (!var1.shownPanelView.hasFocus()) {
                              var1.shownPanelView.requestFocus();
                           }
                        }

                        var10 = var4;
                     }

                     var1.isHandled = false;
                     android.view.WindowManager.LayoutParams var8 = new android.view.WindowManager.LayoutParams(var10, -2, var1.field_120, var1.field_121, 1002, 8519680, -3);
                     var8.gravity = var1.gravity;
                     var8.windowAnimations = var1.windowAnimations;
                     var6.addView(var1.decorView, var8);
                     var1.isOpen = true;
                  }
               }
            }
         }
      }
   }

   private boolean performPanelShortcut(AppCompatDelegateImpl.PanelFeatureState var1, int var2, KeyEvent var3, int var4) {
      if (var3.isSystem()) {
         return false;
      } else {
         boolean var5;
         label24: {
            boolean var6 = false;
            if (!var1.isPrepared) {
               var5 = var6;
               if (!this.preparePanel(var1, var3)) {
                  break label24;
               }
            }

            var5 = var6;
            if (var1.menu != null) {
               var5 = var1.menu.performShortcut(var2, var3, var4);
            }
         }

         if (var5 && (var4 & 1) == 0 && this.mDecorContentParent == null) {
            this.closePanel(var1, true);
         }

         return var5;
      }
   }

   private boolean preparePanel(AppCompatDelegateImpl.PanelFeatureState var1, KeyEvent var2) {
      if (this.mIsDestroyed) {
         return false;
      } else if (var1.isPrepared) {
         return true;
      } else {
         AppCompatDelegateImpl.PanelFeatureState var5 = this.mPreparedPanel;
         if (var5 != null && var5 != var1) {
            this.closePanel(var5, false);
         }

         Callback var10 = this.getWindowCallback();
         if (var10 != null) {
            var1.createdPanelView = var10.onCreatePanelView(var1.featureId);
         }

         boolean var3;
         if (var1.featureId != 0 && var1.featureId != 108) {
            var3 = false;
         } else {
            var3 = true;
         }

         if (var3) {
            DecorContentParent var6 = this.mDecorContentParent;
            if (var6 != null) {
               var6.setMenuPrepared();
            }
         }

         if (var1.createdPanelView == null && (!var3 || !(this.peekSupportActionBar() instanceof ToolbarActionBar))) {
            if (var1.menu == null || var1.refreshMenuContent) {
               if (var1.menu == null && (!this.initializePanelMenu(var1) || var1.menu == null)) {
                  return false;
               }

               if (var3 && this.mDecorContentParent != null) {
                  if (this.mActionMenuPresenterCallback == null) {
                     this.mActionMenuPresenterCallback = new AppCompatDelegateImpl.ActionMenuPresenterCallback();
                  }

                  this.mDecorContentParent.setMenu(var1.menu, this.mActionMenuPresenterCallback);
               }

               var1.menu.stopDispatchingItemsChanged();
               if (!var10.onCreatePanelMenu(var1.featureId, var1.menu)) {
                  var1.setMenu((MenuBuilder)null);
                  if (var3) {
                     DecorContentParent var7 = this.mDecorContentParent;
                     if (var7 != null) {
                        var7.setMenu((Menu)null, this.mActionMenuPresenterCallback);
                     }
                  }

                  return false;
               }

               var1.refreshMenuContent = false;
            }

            var1.menu.stopDispatchingItemsChanged();
            if (var1.frozenActionViewState != null) {
               var1.menu.restoreActionViewStates(var1.frozenActionViewState);
               var1.frozenActionViewState = null;
            }

            if (!var10.onPreparePanel(0, var1.createdPanelView, var1.menu)) {
               if (var3) {
                  DecorContentParent var8 = this.mDecorContentParent;
                  if (var8 != null) {
                     var8.setMenu((Menu)null, this.mActionMenuPresenterCallback);
                  }
               }

               var1.menu.startDispatchingItemsChanged();
               return false;
            }

            int var9;
            if (var2 != null) {
               var9 = var2.getDeviceId();
            } else {
               var9 = -1;
            }

            boolean var4;
            if (KeyCharacterMap.load(var9).getKeyboardType() != 1) {
               var4 = true;
            } else {
               var4 = false;
            }

            var1.qwertyMode = var4;
            var1.menu.setQwertyMode(var1.qwertyMode);
            var1.menu.startDispatchingItemsChanged();
         }

         var1.isPrepared = true;
         var1.isHandled = false;
         this.mPreparedPanel = var1;
         return true;
      }
   }

   private void reopenMenu(MenuBuilder var1, boolean var2) {
      DecorContentParent var4 = this.mDecorContentParent;
      if (var4 != null && var4.canShowOverflowMenu() && (!ViewConfiguration.get(this.mContext).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
         Callback var6 = this.getWindowCallback();
         if (this.mDecorContentParent.isOverflowMenuShowing() && var2) {
            this.mDecorContentParent.hideOverflowMenu();
            if (!this.mIsDestroyed) {
               var6.onPanelClosed(108, this.getPanelState(0, true).menu);
               return;
            }
         } else if (var6 != null && !this.mIsDestroyed) {
            if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
               this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
               this.mInvalidatePanelMenuRunnable.run();
            }

            AppCompatDelegateImpl.PanelFeatureState var3 = this.getPanelState(0, true);
            if (var3.menu != null && !var3.refreshMenuContent && var6.onPreparePanel(0, var3.createdPanelView, var3.menu)) {
               var6.onMenuOpened(108, var3.menu);
               this.mDecorContentParent.showOverflowMenu();
            }
         }

      } else {
         AppCompatDelegateImpl.PanelFeatureState var5 = this.getPanelState(0, true);
         var5.refreshDecorView = true;
         this.closePanel(var5, false);
         this.openPanel(var5, (KeyEvent)null);
      }
   }

   private int sanitizeWindowFeatureId(int var1) {
      if (var1 == 8) {
         Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
         return 108;
      } else if (var1 == 9) {
         Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
         return 109;
      } else {
         return var1;
      }
   }

   private boolean shouldInheritContext(ViewParent var1) {
      if (var1 == null) {
         return false;
      } else {
         for(View var2 = this.mWindow.getDecorView(); var1 != null; var1 = var1.getParent()) {
            if (var1 == var2 || !(var1 instanceof View)) {
               return false;
            }

            if (ViewCompat.isAttachedToWindow((View)var1)) {
               return false;
            }
         }

         return true;
      }
   }

   private void throwFeatureRequestIfSubDecorInstalled() {
      if (this.mSubDecorInstalled) {
         throw new AndroidRuntimeException("Window feature must be requested before adding content");
      }
   }

   private AppCompatActivity tryUnwrapContext() {
      for(Context var1 = this.mContext; var1 != null; var1 = ((ContextWrapper)var1).getBaseContext()) {
         if (var1 instanceof AppCompatActivity) {
            return (AppCompatActivity)var1;
         }

         if (!(var1 instanceof ContextWrapper)) {
            return null;
         }
      }

      return null;
   }

   private boolean updateForNightMode(int var1, boolean var2) {
      boolean var6 = false;
      int var4 = this.mContext.getApplicationContext().getResources().getConfiguration().uiMode & 48;
      int var3;
      if (var1 != 1) {
         if (var1 != 2) {
            var3 = var4;
         } else {
            var3 = 32;
         }
      } else {
         var3 = 16;
      }

      boolean var5;
      boolean var7;
      label67: {
         var7 = this.isActivityManifestHandlingUiMode();
         if (!sAlwaysOverrideConfiguration) {
            var5 = var6;
            if (var3 == var4) {
               break label67;
            }
         }

         var5 = var6;
         if (!var7) {
            var5 = var6;
            if (VERSION.SDK_INT >= 17) {
               var5 = var6;
               if (!this.mBaseContextAttached) {
                  var5 = var6;
                  if (this.mHost instanceof android.view.ContextThemeWrapper) {
                     label71: {
                        Configuration var8 = new Configuration();
                        var8.uiMode = var8.uiMode & -49 | var3;

                        try {
                           ((android.view.ContextThemeWrapper)this.mHost).applyOverrideConfiguration(var8);
                        } catch (IllegalStateException var9) {
                           Log.e("AppCompatDelegate", "updateForNightMode. Calling applyOverrideConfiguration() failed with an exception. Will fall back to using Resources.updateConfiguration()", var9);
                           var5 = false;
                           break label71;
                        }

                        var5 = true;
                     }
                  }
               }
            }
         }
      }

      var4 = this.mContext.getResources().getConfiguration().uiMode & 48;
      var6 = var5;
      Object var10;
      if (!var5) {
         var6 = var5;
         if (var4 != var3) {
            var6 = var5;
            if (var2) {
               var6 = var5;
               if (!var7) {
                  var6 = var5;
                  if (this.mBaseContextAttached) {
                     label49: {
                        if (VERSION.SDK_INT < 17) {
                           var6 = var5;
                           if (!this.mCreated) {
                              break label49;
                           }
                        }

                        var10 = this.mHost;
                        var6 = var5;
                        if (var10 instanceof Activity) {
                           ActivityCompat.recreate((Activity)var10);
                           var6 = true;
                        }
                     }
                  }
               }
            }
         }
      }

      var2 = var6;
      if (!var6) {
         var2 = var6;
         if (var4 != var3) {
            this.updateResourcesConfigurationForNightMode(var3, var7);
            var2 = true;
         }
      }

      if (var2) {
         var10 = this.mHost;
         if (var10 instanceof AppCompatActivity) {
            ((AppCompatActivity)var10).onNightModeChanged(var1);
         }
      }

      return var2;
   }

   private void updateResourcesConfigurationForNightMode(int var1, boolean var2) {
      Resources var4 = this.mContext.getResources();
      Configuration var3 = new Configuration(var4.getConfiguration());
      var3.uiMode = var4.getConfiguration().uiMode & -49 | var1;
      var4.updateConfiguration(var3, (DisplayMetrics)null);
      if (VERSION.SDK_INT < 26) {
         ResourcesFlusher.flush(var4);
      }

      var1 = this.mThemeResId;
      if (var1 != 0) {
         this.mContext.setTheme(var1);
         if (VERSION.SDK_INT >= 23) {
            this.mContext.getTheme().applyStyle(this.mThemeResId, true);
         }
      }

      if (var2) {
         Object var5 = this.mHost;
         if (var5 instanceof Activity) {
            Activity var6 = (Activity)var5;
            if (var6 instanceof LifecycleOwner) {
               if (((LifecycleOwner)var6).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                  var6.onConfigurationChanged(var3);
               }

               return;
            }

            if (this.mStarted) {
               var6.onConfigurationChanged(var3);
            }
         }
      }

   }

   public void addContentView(View var1, LayoutParams var2) {
      this.ensureSubDecor();
      ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(var1, var2);
      this.mAppCompatWindowCallback.getWrapped().onContentChanged();
   }

   public boolean applyDayNight() {
      return this.applyDayNight(true);
   }

   public void attachBaseContext(Context var1) {
      this.applyDayNight(false);
      this.mBaseContextAttached = true;
   }

   void callOnPanelClosed(int var1, AppCompatDelegateImpl.PanelFeatureState var2, Menu var3) {
      AppCompatDelegateImpl.PanelFeatureState var5 = var2;
      Object var6 = var3;
      if (var3 == null) {
         AppCompatDelegateImpl.PanelFeatureState var4 = var2;
         if (var2 == null) {
            var4 = var2;
            if (var1 >= 0) {
               AppCompatDelegateImpl.PanelFeatureState[] var7 = this.mPanels;
               var4 = var2;
               if (var1 < var7.length) {
                  var4 = var7[var1];
               }
            }
         }

         var5 = var4;
         var6 = var3;
         if (var4 != null) {
            var6 = var4.menu;
            var5 = var4;
         }
      }

      if (var5 == null || var5.isOpen) {
         if (!this.mIsDestroyed) {
            this.mAppCompatWindowCallback.getWrapped().onPanelClosed(var1, (Menu)var6);
         }

      }
   }

   void checkCloseActionMenu(MenuBuilder var1) {
      if (!this.mClosingActionMenu) {
         this.mClosingActionMenu = true;
         this.mDecorContentParent.dismissPopups();
         Callback var2 = this.getWindowCallback();
         if (var2 != null && !this.mIsDestroyed) {
            var2.onPanelClosed(108, var1);
         }

         this.mClosingActionMenu = false;
      }
   }

   void closePanel(int var1) {
      this.closePanel(this.getPanelState(var1, true), true);
   }

   void closePanel(AppCompatDelegateImpl.PanelFeatureState var1, boolean var2) {
      if (var2 && var1.featureId == 0) {
         DecorContentParent var3 = this.mDecorContentParent;
         if (var3 != null && var3.isOverflowMenuShowing()) {
            this.checkCloseActionMenu(var1.menu);
            return;
         }
      }

      WindowManager var4 = (WindowManager)this.mContext.getSystemService("window");
      if (var4 != null && var1.isOpen && var1.decorView != null) {
         var4.removeView(var1.decorView);
         if (var2) {
            this.callOnPanelClosed(var1.featureId, var1, (Menu)null);
         }
      }

      var1.isPrepared = false;
      var1.isHandled = false;
      var1.isOpen = false;
      var1.shownPanelView = null;
      var1.refreshDecorView = true;
      if (this.mPreparedPanel == var1) {
         this.mPreparedPanel = null;
      }

   }

   public View createView(View var1, String var2, Context var3, AttributeSet var4) {
      AppCompatViewInflater var7 = this.mAppCompatViewInflater;
      boolean var6 = false;
      if (var7 == null) {
         String var12 = this.mContext.obtainStyledAttributes(styleable.AppCompatTheme).getString(styleable.AppCompatTheme_viewInflaterClass);
         if (var12 != null && !AppCompatViewInflater.class.getName().equals(var12)) {
            label53:
            try {
               this.mAppCompatViewInflater = (AppCompatViewInflater)Class.forName(var12).getDeclaredConstructor().newInstance();
            } catch (Throwable var11) {
               StringBuilder var9 = new StringBuilder();
               var9.append("Failed to instantiate custom view inflater ");
               var9.append(var12);
               var9.append(". Falling back to default.");
               Log.i("AppCompatDelegate", var9.toString(), var11);
               this.mAppCompatViewInflater = new AppCompatViewInflater();
               break label53;
            }
         } else {
            this.mAppCompatViewInflater = new AppCompatViewInflater();
         }
      }

      boolean var5 = false;
      if (IS_PRE_LOLLIPOP) {
         if (var4 instanceof XmlPullParser) {
            var5 = var6;
            if (((XmlPullParser)var4).getDepth() > 1) {
               var5 = true;
            }
         } else {
            var5 = this.shouldInheritContext((ViewParent)var1);
         }
      }

      return this.mAppCompatViewInflater.createView(var1, var2, var3, var4, var5, IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
   }

   void dismissPopups() {
      DecorContentParent var1 = this.mDecorContentParent;
      if (var1 != null) {
         var1.dismissPopups();
      }

      if (this.mActionModePopup != null) {
         this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
         if (this.mActionModePopup.isShowing()) {
            try {
               this.mActionModePopup.dismiss();
            } catch (IllegalArgumentException var2) {
            }
         }

         this.mActionModePopup = null;
      }

      this.endOnGoingFadeAnimation();
      AppCompatDelegateImpl.PanelFeatureState var3 = this.getPanelState(0, false);
      if (var3 != null && var3.menu != null) {
         var3.menu.close();
      }

   }

   boolean dispatchKeyEvent(KeyEvent var1) {
      Object var5 = this.mHost;
      boolean var4 = var5 instanceof KeyEventDispatcher.Component;
      boolean var2 = true;
      if (var4 || var5 instanceof AppCompatDialog) {
         View var6 = this.mWindow.getDecorView();
         if (var6 != null && KeyEventDispatcher.dispatchBeforeHierarchy(var6, var1)) {
            return true;
         }
      }

      if (var1.getKeyCode() == 82 && this.mAppCompatWindowCallback.getWrapped().dispatchKeyEvent(var1)) {
         return true;
      } else {
         int var3 = var1.getKeyCode();
         if (var1.getAction() != 0) {
            var2 = false;
         }

         return var2 ? this.onKeyDown(var3, var1) : this.onKeyUp(var3, var1);
      }
   }

   void doInvalidatePanelMenu(int var1) {
      AppCompatDelegateImpl.PanelFeatureState var2 = this.getPanelState(var1, true);
      if (var2.menu != null) {
         Bundle var3 = new Bundle();
         var2.menu.saveActionViewStates(var3);
         if (var3.size() > 0) {
            var2.frozenActionViewState = var3;
         }

         var2.menu.stopDispatchingItemsChanged();
         var2.menu.clear();
      }

      var2.refreshMenuContent = true;
      var2.refreshDecorView = true;
      if ((var1 == 108 || var1 == 0) && this.mDecorContentParent != null) {
         var2 = this.getPanelState(0, false);
         if (var2 != null) {
            var2.isPrepared = false;
            this.preparePanel(var2, (KeyEvent)null);
         }
      }

   }

   void endOnGoingFadeAnimation() {
      ViewPropertyAnimatorCompat var1 = this.mFadeAnim;
      if (var1 != null) {
         var1.cancel();
      }

   }

   AppCompatDelegateImpl.PanelFeatureState findMenuPanel(Menu var1) {
      AppCompatDelegateImpl.PanelFeatureState[] var4 = this.mPanels;
      int var2;
      if (var4 != null) {
         var2 = var4.length;
      } else {
         var2 = 0;
      }

      for(int var3 = 0; var3 < var2; ++var3) {
         AppCompatDelegateImpl.PanelFeatureState var5 = var4[var3];
         if (var5 != null && var5.menu == var1) {
            return var5;
         }
      }

      return null;
   }

   public View findViewById(int var1) {
      this.ensureSubDecor();
      return this.mWindow.findViewById(var1);
   }

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

   final AppCompatDelegateImpl.AutoNightModeManager getAutoTimeNightModeManager() {
      if (this.mAutoTimeNightModeManager == null) {
         this.mAutoTimeNightModeManager = new AppCompatDelegateImpl.AutoTimeNightModeManager(TwilightManager.getInstance(this.mContext));
      }

      return this.mAutoTimeNightModeManager;
   }

   public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
      return new AppCompatDelegateImpl.ActionBarDrawableToggleImpl();
   }

   public int getLocalNightMode() {
      return this.mLocalNightMode;
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

   protected AppCompatDelegateImpl.PanelFeatureState getPanelState(int var1, boolean var2) {
      AppCompatDelegateImpl.PanelFeatureState[] var3;
      label22: {
         var3 = this.mPanels;
         AppCompatDelegateImpl.PanelFeatureState[] var4 = var3;
         if (var3 != null) {
            var3 = var3;
            if (var4.length > var1) {
               break label22;
            }
         }

         AppCompatDelegateImpl.PanelFeatureState[] var5 = new AppCompatDelegateImpl.PanelFeatureState[var1 + 1];
         if (var3 != null) {
            System.arraycopy(var3, 0, var5, 0, var3.length);
         }

         var3 = var5;
         this.mPanels = var5;
      }

      AppCompatDelegateImpl.PanelFeatureState var6 = var3[var1];
      AppCompatDelegateImpl.PanelFeatureState var7 = var6;
      if (var6 == null) {
         var6 = new AppCompatDelegateImpl.PanelFeatureState(var1);
         var7 = var6;
         var3[var1] = var6;
      }

      return var7;
   }

   ViewGroup getSubDecor() {
      return this.mSubDecor;
   }

   public ActionBar getSupportActionBar() {
      this.initWindowDecorActionBar();
      return this.mActionBar;
   }

   final CharSequence getTitle() {
      Object var1 = this.mHost;
      return var1 instanceof Activity ? ((Activity)var1).getTitle() : this.mTitle;
   }

   final Callback getWindowCallback() {
      return this.mWindow.getCallback();
   }

   public boolean hasWindowFeature(int var1) {
      boolean var3 = false;
      int var2 = this.sanitizeWindowFeatureId(var1);
      boolean var4 = true;
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 != 5) {
               if (var2 != 10) {
                  if (var2 != 108) {
                     if (var2 == 109) {
                        var3 = this.mOverlayActionBar;
                     }
                  } else {
                     var3 = this.mHasActionBar;
                  }
               } else {
                  var3 = this.mOverlayActionMode;
               }
            } else {
               var3 = this.mFeatureIndeterminateProgress;
            }
         } else {
            var3 = this.mFeatureProgress;
         }
      } else {
         var3 = this.mWindowNoTitle;
      }

      if (!var3) {
         if (this.mWindow.hasFeature(var1)) {
            return true;
         }

         var4 = false;
      }

      return var4;
   }

   public void installViewFactory() {
      LayoutInflater var1 = LayoutInflater.from(this.mContext);
      if (var1.getFactory() == null) {
         LayoutInflaterCompat.setFactory2(var1, this);
      } else {
         if (!(var1.getFactory2() instanceof AppCompatDelegateImpl)) {
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
         }

      }
   }

   public void invalidateOptionsMenu() {
      ActionBar var1 = this.getSupportActionBar();
      if (var1 == null || !var1.invalidateOptionsMenu()) {
         this.invalidatePanelMenu(0);
      }
   }

   public boolean isHandleNativeActionModesEnabled() {
      return this.mHandleNativeActionModes;
   }

   int mapNightMode(int var1) {
      if (var1 != -100) {
         if (var1 != -1) {
            if (var1 == 0) {
               if (VERSION.SDK_INT >= 23 && ((UiModeManager)this.mContext.getSystemService(UiModeManager.class)).getNightMode() == 0) {
                  return -1;
               }

               return this.getAutoTimeNightModeManager().getApplyableNightMode();
            }

            if (var1 != 1 && var1 != 2) {
               if (var1 == 3) {
                  return this.getAutoBatteryNightModeManager().getApplyableNightMode();
               }

               throw new IllegalStateException("Unknown value set for night mode. Please use one of the MODE_NIGHT values from AppCompatDelegate.");
            }
         }

         return var1;
      } else {
         return -1;
      }
   }

   boolean onBackPressed() {
      ActionMode var1 = this.mActionMode;
      if (var1 != null) {
         var1.finish();
         return true;
      } else {
         ActionBar var2 = this.getSupportActionBar();
         return var2 != null && var2.collapseActionView();
      }
   }

   public void onConfigurationChanged(Configuration var1) {
      if (this.mHasActionBar && this.mSubDecorInstalled) {
         ActionBar var2 = this.getSupportActionBar();
         if (var2 != null) {
            var2.onConfigurationChanged(var1);
         }
      }

      AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
      this.applyDayNight(false);
   }

   public void onCreate(Bundle var1) {
      this.mBaseContextAttached = true;
      this.applyDayNight(false);
      this.ensureWindow();
      Object var2 = this.mHost;
      if (var2 instanceof Activity) {
         String var4 = null;

         label20: {
            String var5;
            try {
               var5 = NavUtils.getParentActivityName((Activity)var2);
            } catch (IllegalArgumentException var3) {
               break label20;
            }

            var4 = var5;
         }

         if (var4 != null) {
            ActionBar var6 = this.peekSupportActionBar();
            if (var6 == null) {
               this.mEnableDefaultActionBarUp = true;
            } else {
               var6.setDefaultDisplayHomeAsUpEnabled(true);
            }
         }
      }

      this.mCreated = true;
   }

   public final View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return this.createView(var1, var2, var3, var4);
   }

   public View onCreateView(String var1, Context var2, AttributeSet var3) {
      return this.onCreateView((View)null, var1, var2, var3);
   }

   public void onDestroy() {
      markStopped(this);
      if (this.mInvalidatePanelMenuPosted) {
         this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
      }

      this.mStarted = false;
      this.mIsDestroyed = true;
      ActionBar var1 = this.mActionBar;
      if (var1 != null) {
         var1.onDestroy();
      }

      this.cleanupAutoManagers();
   }

   boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3 = true;
      if (var1 != 4) {
         if (var1 != 82) {
            return false;
         } else {
            this.onKeyDownPanel(0, var2);
            return true;
         }
      } else {
         if ((var2.getFlags() & 128) == 0) {
            var3 = false;
         }

         this.mLongPressBackDown = var3;
         return false;
      }
   }

   boolean onKeyShortcut(int var1, KeyEvent var2) {
      ActionBar var4 = this.getSupportActionBar();
      if (var4 != null && var4.onKeyShortcut(var1, var2)) {
         return true;
      } else {
         AppCompatDelegateImpl.PanelFeatureState var6 = this.mPreparedPanel;
         if (var6 != null && this.performPanelShortcut(var6, var2.getKeyCode(), var2, 1)) {
            AppCompatDelegateImpl.PanelFeatureState var5 = this.mPreparedPanel;
            if (var5 != null) {
               var5.isHandled = true;
            }

            return true;
         } else {
            if (this.mPreparedPanel == null) {
               var6 = this.getPanelState(0, true);
               this.preparePanel(var6, var2);
               boolean var3 = this.performPanelShortcut(var6, var2.getKeyCode(), var2, 1);
               var6.isPrepared = false;
               if (var3) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   boolean onKeyUp(int var1, KeyEvent var2) {
      if (var1 != 4) {
         if (var1 != 82) {
            return false;
         } else {
            this.onKeyUpPanel(0, var2);
            return true;
         }
      } else {
         boolean var3 = this.mLongPressBackDown;
         this.mLongPressBackDown = false;
         AppCompatDelegateImpl.PanelFeatureState var4 = this.getPanelState(0, false);
         if (var4 != null && var4.isOpen) {
            if (!var3) {
               this.closePanel(var4, true);
            }

            return true;
         } else {
            return this.onBackPressed();
         }
      }
   }

   public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
      Callback var3 = this.getWindowCallback();
      if (var3 != null && !this.mIsDestroyed) {
         AppCompatDelegateImpl.PanelFeatureState var4 = this.findMenuPanel(var1.getRootMenu());
         if (var4 != null) {
            return var3.onMenuItemSelected(var4.featureId, var2);
         }
      }

      return false;
   }

   public void onMenuModeChange(MenuBuilder var1) {
      this.reopenMenu(var1, true);
   }

   void onMenuOpened(int var1) {
      if (var1 == 108) {
         ActionBar var2 = this.getSupportActionBar();
         if (var2 != null) {
            var2.dispatchMenuVisibilityChanged(true);
         }
      }

   }

   void onPanelClosed(int var1) {
      if (var1 == 108) {
         ActionBar var2 = this.getSupportActionBar();
         if (var2 != null) {
            var2.dispatchMenuVisibilityChanged(false);
         }
      } else if (var1 == 0) {
         AppCompatDelegateImpl.PanelFeatureState var3 = this.getPanelState(var1, true);
         if (var3.isOpen) {
            this.closePanel(var3, false);
            return;
         }
      }

   }

   public void onPostCreate(Bundle var1) {
      this.ensureSubDecor();
   }

   public void onPostResume() {
      ActionBar var1 = this.getSupportActionBar();
      if (var1 != null) {
         var1.setShowHideAnimationEnabled(true);
      }

   }

   public void onSaveInstanceState(Bundle var1) {
      if (this.mLocalNightMode != -100) {
         sLocalNightModes.put(this.mHost.getClass(), this.mLocalNightMode);
      }

   }

   public void onStart() {
      this.mStarted = true;
      this.applyDayNight();
      markStarted(this);
   }

   public void onStop() {
      this.mStarted = false;
      markStopped(this);
      ActionBar var1 = this.getSupportActionBar();
      if (var1 != null) {
         var1.setShowHideAnimationEnabled(false);
      }

      if (this.mHost instanceof Dialog) {
         this.cleanupAutoManagers();
      }

   }

   void onSubDecorInstalled(ViewGroup var1) {
   }

   final ActionBar peekSupportActionBar() {
      return this.mActionBar;
   }

   public boolean requestWindowFeature(int var1) {
      var1 = this.sanitizeWindowFeatureId(var1);
      if (this.mWindowNoTitle && var1 == 108) {
         return false;
      } else {
         if (this.mHasActionBar && var1 == 1) {
            this.mHasActionBar = false;
         }

         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 5) {
                  if (var1 != 10) {
                     if (var1 != 108) {
                        if (var1 != 109) {
                           return this.mWindow.requestFeature(var1);
                        } else {
                           this.throwFeatureRequestIfSubDecorInstalled();
                           this.mOverlayActionBar = true;
                           return true;
                        }
                     } else {
                        this.throwFeatureRequestIfSubDecorInstalled();
                        this.mHasActionBar = true;
                        return true;
                     }
                  } else {
                     this.throwFeatureRequestIfSubDecorInstalled();
                     this.mOverlayActionMode = true;
                     return true;
                  }
               } else {
                  this.throwFeatureRequestIfSubDecorInstalled();
                  this.mFeatureIndeterminateProgress = true;
                  return true;
               }
            } else {
               this.throwFeatureRequestIfSubDecorInstalled();
               this.mFeatureProgress = true;
               return true;
            }
         } else {
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mWindowNoTitle = true;
            return true;
         }
      }
   }

   public void setContentView(int var1) {
      this.ensureSubDecor();
      ViewGroup var2 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var2.removeAllViews();
      LayoutInflater.from(this.mContext).inflate(var1, var2);
      this.mAppCompatWindowCallback.getWrapped().onContentChanged();
   }

   public void setContentView(View var1) {
      this.ensureSubDecor();
      ViewGroup var2 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var2.removeAllViews();
      var2.addView(var1);
      this.mAppCompatWindowCallback.getWrapped().onContentChanged();
   }

   public void setContentView(View var1, LayoutParams var2) {
      this.ensureSubDecor();
      ViewGroup var3 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var3.removeAllViews();
      var3.addView(var1, var2);
      this.mAppCompatWindowCallback.getWrapped().onContentChanged();
   }

   public void setHandleNativeActionModesEnabled(boolean var1) {
      this.mHandleNativeActionModes = var1;
   }

   public void setLocalNightMode(int var1) {
      if (this.mLocalNightMode != var1) {
         this.mLocalNightMode = var1;
         this.applyDayNight();
      }

   }

   public void setSupportActionBar(Toolbar var1) {
      if (this.mHost instanceof Activity) {
         ActionBar var2 = this.getSupportActionBar();
         if (!(var2 instanceof WindowDecorActionBar)) {
            this.mMenuInflater = null;
            if (var2 != null) {
               var2.onDestroy();
            }

            if (var1 != null) {
               ToolbarActionBar var3 = new ToolbarActionBar(var1, this.getTitle(), this.mAppCompatWindowCallback);
               this.mActionBar = var3;
               this.mWindow.setCallback(var3.getWrappedWindowCallback());
            } else {
               this.mActionBar = null;
               this.mWindow.setCallback(this.mAppCompatWindowCallback);
            }

            this.invalidateOptionsMenu();
         } else {
            throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
         }
      }
   }

   public void setTheme(int var1) {
      this.mThemeResId = var1;
   }

   public final void setTitle(CharSequence var1) {
      this.mTitle = var1;
      DecorContentParent var2 = this.mDecorContentParent;
      if (var2 != null) {
         var2.setWindowTitle(var1);
      } else if (this.peekSupportActionBar() != null) {
         this.peekSupportActionBar().setWindowTitle(var1);
      } else {
         TextView var3 = this.mTitleView;
         if (var3 != null) {
            var3.setText(var1);
         }

      }
   }

   final boolean shouldAnimateActionModeView() {
      if (this.mSubDecorInstalled) {
         ViewGroup var1 = this.mSubDecor;
         if (var1 != null && ViewCompat.isLaidOut(var1)) {
            return true;
         }
      }

      return false;
   }

   public ActionMode startSupportActionMode(ActionMode.Callback var1) {
      if (var1 != null) {
         ActionMode var2 = this.mActionMode;
         if (var2 != null) {
            var2.finish();
         }

         AppCompatDelegateImpl.ActionModeCallbackWrapperV9 var4 = new AppCompatDelegateImpl.ActionModeCallbackWrapperV9(var1);
         ActionBar var5 = this.getSupportActionBar();
         if (var5 != null) {
            var2 = var5.startActionMode(var4);
            this.mActionMode = var2;
            if (var2 != null) {
               AppCompatCallback var3 = this.mAppCompatCallback;
               if (var3 != null) {
                  var3.onSupportActionModeStarted(var2);
               }
            }
         }

         if (this.mActionMode == null) {
            this.mActionMode = this.startSupportActionModeFromWindow(var4);
         }

         return this.mActionMode;
      } else {
         throw new IllegalArgumentException("ActionMode callback can not be null.");
      }
   }

   ActionMode startSupportActionModeFromWindow(ActionMode.Callback var1) {
      this.endOnGoingFadeAnimation();
      ActionMode var4 = this.mActionMode;
      if (var4 != null) {
         var4.finish();
      }

      Object var12 = var1;
      if (!(var1 instanceof AppCompatDelegateImpl.ActionModeCallbackWrapperV9)) {
         var12 = new AppCompatDelegateImpl.ActionModeCallbackWrapperV9(var1);
      }

      TypedValue var5 = null;
      AppCompatCallback var6 = this.mAppCompatCallback;
      ActionMode var8 = var5;
      if (var6 != null) {
         var8 = var5;
         if (!this.mIsDestroyed) {
            try {
               var8 = var6.onWindowStartingSupportActionMode((ActionMode.Callback)var12);
            } catch (AbstractMethodError var7) {
               var8 = var5;
            }
         }
      }

      if (var8 != null) {
         this.mActionMode = var8;
      } else {
         ActionBarContextView var9 = this.mActionModeView;
         boolean var3 = true;
         if (var9 == null) {
            if (this.mIsFloating) {
               var5 = new TypedValue();
               Theme var10 = this.mContext.getTheme();
               var10.resolveAttribute(attr.actionBarTheme, var5, true);
               Object var11;
               if (var5.resourceId != 0) {
                  Theme var19 = this.mContext.getResources().newTheme();
                  var19.setTo(var10);
                  var19.applyStyle(var5.resourceId, true);
                  var11 = new ContextThemeWrapper(this.mContext, 0);
                  ((Context)var11).getTheme().setTo(var19);
               } else {
                  var11 = this.mContext;
               }

               this.mActionModeView = new ActionBarContextView((Context)var11);
               PopupWindow var20 = new PopupWindow((Context)var11, (AttributeSet)null, attr.actionModePopupWindowStyle);
               this.mActionModePopup = var20;
               PopupWindowCompat.setWindowLayoutType(var20, 2);
               this.mActionModePopup.setContentView(this.mActionModeView);
               this.mActionModePopup.setWidth(-1);
               ((Context)var11).getTheme().resolveAttribute(attr.actionBarSize, var5, true);
               int var2 = TypedValue.complexToDimensionPixelSize(var5.data, ((Context)var11).getResources().getDisplayMetrics());
               this.mActionModeView.setContentHeight(var2);
               this.mActionModePopup.setHeight(-2);
               this.mShowActionModePopup = new Runnable() {
                  public void run() {
                     AppCompatDelegateImpl.this.mActionModePopup.showAtLocation(AppCompatDelegateImpl.this.mActionModeView, 55, 0, 0);
                     AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                     if (AppCompatDelegateImpl.this.shouldAnimateActionModeView()) {
                        AppCompatDelegateImpl.this.mActionModeView.setAlpha(0.0F);
                        AppCompatDelegateImpl var1 = AppCompatDelegateImpl.this;
                        var1.mFadeAnim = ViewCompat.animate(var1.mActionModeView).alpha(1.0F);
                        AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                           public void onAnimationEnd(View var1) {
                              AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                              AppCompatDelegateImpl.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                              AppCompatDelegateImpl.this.mFadeAnim = null;
                           }

                           public void onAnimationStart(View var1) {
                              AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                           }
                        });
                     } else {
                        AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                     }
                  }
               };
            } else {
               ViewStubCompat var13 = (ViewStubCompat)this.mSubDecor.findViewById(id.action_mode_bar_stub);
               if (var13 != null) {
                  var13.setLayoutInflater(LayoutInflater.from(this.getActionBarThemedContext()));
                  this.mActionModeView = (ActionBarContextView)var13.inflate();
               }
            }
         }

         if (this.mActionModeView != null) {
            this.endOnGoingFadeAnimation();
            this.mActionModeView.killMode();
            Context var14 = this.mActionModeView.getContext();
            ActionBarContextView var17 = this.mActionModeView;
            if (this.mActionModePopup != null) {
               var3 = false;
            }

            StandaloneActionMode var15 = new StandaloneActionMode(var14, var17, (ActionMode.Callback)var12, var3);
            if (((ActionMode.Callback)var12).onCreateActionMode(var15, var15.getMenu())) {
               var15.invalidate();
               this.mActionModeView.initForMode(var15);
               this.mActionMode = var15;
               if (this.shouldAnimateActionModeView()) {
                  this.mActionModeView.setAlpha(0.0F);
                  ViewPropertyAnimatorCompat var18 = ViewCompat.animate(this.mActionModeView).alpha(1.0F);
                  this.mFadeAnim = var18;
                  var18.setListener(new ViewPropertyAnimatorListenerAdapter() {
                     public void onAnimationEnd(View var1) {
                        AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                        AppCompatDelegateImpl.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                        AppCompatDelegateImpl.this.mFadeAnim = null;
                     }

                     public void onAnimationStart(View var1) {
                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                        AppCompatDelegateImpl.this.mActionModeView.sendAccessibilityEvent(32);
                        if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                           ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                        }

                     }
                  });
               } else {
                  this.mActionModeView.setAlpha(1.0F);
                  this.mActionModeView.setVisibility(0);
                  this.mActionModeView.sendAccessibilityEvent(32);
                  if (this.mActionModeView.getParent() instanceof View) {
                     ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent());
                  }
               }

               if (this.mActionModePopup != null) {
                  this.mWindow.getDecorView().post(this.mShowActionModePopup);
               }
            } else {
               this.mActionMode = null;
            }
         }
      }

      var8 = this.mActionMode;
      if (var8 != null) {
         AppCompatCallback var16 = this.mAppCompatCallback;
         if (var16 != null) {
            var16.onSupportActionModeStarted(var8);
         }
      }

      return this.mActionMode;
   }

   int updateStatusGuard(int var1) {
      boolean var2 = false;
      boolean var8 = false;
      ActionBarContextView var9 = this.mActionModeView;
      byte var7 = 0;
      boolean var6 = var2;
      int var5 = var1;
      if (var9 != null) {
         var6 = var2;
         var5 = var1;
         if (var9.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams var15 = (MarginLayoutParams)this.mActionModeView.getLayoutParams();
            boolean var4 = false;
            var2 = false;
            boolean var13;
            int var14;
            if (this.mActionModeView.isShown()) {
               if (this.mTempRect1 == null) {
                  this.mTempRect1 = new Rect();
                  this.mTempRect2 = new Rect();
               }

               Rect var10 = this.mTempRect1;
               Rect var11 = this.mTempRect2;
               var10.set(0, var1, 0, 0);
               ViewUtils.computeFitSystemWindows(this.mSubDecor, var10, var11);
               int var3;
               if (var11.top == 0) {
                  var3 = var1;
               } else {
                  var3 = 0;
               }

               if (var15.topMargin != var3) {
                  var13 = true;
                  var15.topMargin = var1;
                  View var17 = this.mStatusGuard;
                  if (var17 == null) {
                     var17 = new View(this.mContext);
                     this.mStatusGuard = var17;
                     var17.setBackgroundColor(this.mContext.getResources().getColor(color.abc_input_method_navigation_guard));
                     this.mSubDecor.addView(this.mStatusGuard, -1, new LayoutParams(-1, var1));
                     var2 = var13;
                  } else {
                     LayoutParams var18 = var17.getLayoutParams();
                     var2 = var13;
                     if (var18.height != var1) {
                        var18.height = var1;
                        this.mStatusGuard.setLayoutParams(var18);
                        var2 = var13;
                     }
                  }
               }

               if (this.mStatusGuard != null) {
                  var13 = true;
               } else {
                  var13 = false;
               }

               var14 = var1;
               if (!this.mOverlayActionMode) {
                  var14 = var1;
                  if (var13) {
                     var14 = 0;
                  }
               }
            } else {
               var13 = var8;
               var2 = var4;
               var14 = var1;
               if (var15.topMargin != 0) {
                  var2 = true;
                  var15.topMargin = 0;
                  var14 = var1;
                  var13 = var8;
               }
            }

            var6 = var13;
            var5 = var14;
            if (var2) {
               this.mActionModeView.setLayoutParams(var15);
               var5 = var14;
               var6 = var13;
            }
         }
      }

      View var16 = this.mStatusGuard;
      if (var16 != null) {
         byte var12;
         if (var6) {
            var12 = var7;
         } else {
            var12 = 8;
         }

         var16.setVisibility(var12);
      }

      return var5;
   }

   private class ActionBarDrawableToggleImpl implements ActionBarDrawerToggle.Delegate {
      ActionBarDrawableToggleImpl() {
      }

      public Context getActionBarThemedContext() {
         return AppCompatDelegateImpl.this.getActionBarThemedContext();
      }

      public Drawable getThemeUpIndicator() {
         TintTypedArray var1 = TintTypedArray.obtainStyledAttributes(this.getActionBarThemedContext(), (AttributeSet)null, new int[]{attr.homeAsUpIndicator});
         Drawable var2 = var1.getDrawable(0);
         var1.recycle();
         return var2;
      }

      public boolean isNavigationVisible() {
         ActionBar var1 = AppCompatDelegateImpl.this.getSupportActionBar();
         return var1 != null && (var1.getDisplayOptions() & 4) != 0;
      }

      public void setActionBarDescription(int var1) {
         ActionBar var2 = AppCompatDelegateImpl.this.getSupportActionBar();
         if (var2 != null) {
            var2.setHomeActionContentDescription(var1);
         }

      }

      public void setActionBarUpIndicator(Drawable var1, int var2) {
         ActionBar var3 = AppCompatDelegateImpl.this.getSupportActionBar();
         if (var3 != null) {
            var3.setHomeAsUpIndicator(var1);
            var3.setHomeActionContentDescription(var2);
         }

      }
   }

   private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
      ActionMenuPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         AppCompatDelegateImpl.this.checkCloseActionMenu(var1);
      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         Callback var2 = AppCompatDelegateImpl.this.getWindowCallback();
         if (var2 != null) {
            var2.onMenuOpened(108, var1);
         }

         return true;
      }
   }

   class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
      private ActionMode.Callback mWrapped;

      public ActionModeCallbackWrapperV9(ActionMode.Callback var2) {
         this.mWrapped = var2;
      }

      public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
         return this.mWrapped.onActionItemClicked(var1, var2);
      }

      public boolean onCreateActionMode(ActionMode var1, Menu var2) {
         return this.mWrapped.onCreateActionMode(var1, var2);
      }

      public void onDestroyActionMode(ActionMode var1) {
         this.mWrapped.onDestroyActionMode(var1);
         if (AppCompatDelegateImpl.this.mActionModePopup != null) {
            AppCompatDelegateImpl.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup);
         }

         if (AppCompatDelegateImpl.this.mActionModeView != null) {
            AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
            AppCompatDelegateImpl var2 = AppCompatDelegateImpl.this;
            var2.mFadeAnim = ViewCompat.animate(var2.mActionModeView).alpha(0.0F);
            AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
               public void onAnimationEnd(View var1) {
                  AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                  if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                     AppCompatDelegateImpl.this.mActionModePopup.dismiss();
                  } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                     ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                  }

                  AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
                  AppCompatDelegateImpl.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                  AppCompatDelegateImpl.this.mFadeAnim = null;
               }
            });
         }

         if (AppCompatDelegateImpl.this.mAppCompatCallback != null) {
            AppCompatDelegateImpl.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImpl.this.mActionMode);
         }

         AppCompatDelegateImpl.this.mActionMode = null;
      }

      public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
         return this.mWrapped.onPrepareActionMode(var1, var2);
      }
   }

   class AppCompatWindowCallback extends WindowCallbackWrapper {
      AppCompatWindowCallback(Callback var2) {
         super(var2);
      }

      public boolean dispatchKeyEvent(KeyEvent var1) {
         return AppCompatDelegateImpl.this.dispatchKeyEvent(var1) || super.dispatchKeyEvent(var1);
      }

      public boolean dispatchKeyShortcutEvent(KeyEvent var1) {
         return super.dispatchKeyShortcutEvent(var1) || AppCompatDelegateImpl.this.onKeyShortcut(var1.getKeyCode(), var1);
      }

      public void onContentChanged() {
      }

      public boolean onCreatePanelMenu(int var1, Menu var2) {
         return var1 == 0 && !(var2 instanceof MenuBuilder) ? false : super.onCreatePanelMenu(var1, var2);
      }

      public boolean onMenuOpened(int var1, Menu var2) {
         super.onMenuOpened(var1, var2);
         AppCompatDelegateImpl.this.onMenuOpened(var1);
         return true;
      }

      public void onPanelClosed(int var1, Menu var2) {
         super.onPanelClosed(var1, var2);
         AppCompatDelegateImpl.this.onPanelClosed(var1);
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

      public void onProvideKeyboardShortcuts(List var1, Menu var2, int var3) {
         AppCompatDelegateImpl.PanelFeatureState var4 = AppCompatDelegateImpl.this.getPanelState(0, true);
         if (var4 != null && var4.menu != null) {
            super.onProvideKeyboardShortcuts(var1, var4.menu, var3);
         } else {
            super.onProvideKeyboardShortcuts(var1, var2, var3);
         }
      }

      public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback var1) {
         if (VERSION.SDK_INT >= 23) {
            return null;
         } else {
            return AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() ? this.startAsSupportActionMode(var1) : super.onWindowStartingActionMode(var1);
         }
      }

      public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback var1, int var2) {
         return AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() && var2 == 0 ? this.startAsSupportActionMode(var1) : super.onWindowStartingActionMode(var1, var2);
      }

      final android.view.ActionMode startAsSupportActionMode(android.view.ActionMode.Callback var1) {
         SupportActionModeWrapper.CallbackWrapper var3 = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImpl.this.mContext, var1);
         ActionMode var2 = AppCompatDelegateImpl.this.startSupportActionMode(var3);
         return var2 != null ? var3.getActionModeWrapper(var2) : null;
      }
   }

   private class AutoBatteryNightModeManager extends AppCompatDelegateImpl.AutoNightModeManager {
      private final PowerManager mPowerManager;

      AutoBatteryNightModeManager(Context var2) {
         super();
         this.mPowerManager = (PowerManager)var2.getSystemService("power");
      }

      IntentFilter createIntentFilterForBroadcastReceiver() {
         if (VERSION.SDK_INT >= 21) {
            IntentFilter var1 = new IntentFilter();
            var1.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
            return var1;
         } else {
            return null;
         }
      }

      public int getApplyableNightMode() {
         int var2 = VERSION.SDK_INT;
         byte var1 = 1;
         if (var2 >= 21) {
            if (this.mPowerManager.isPowerSaveMode()) {
               var1 = 2;
            }

            return var1;
         } else {
            return 1;
         }
      }

      public void onChange() {
         AppCompatDelegateImpl.this.applyDayNight();
      }
   }

   abstract class AutoNightModeManager {
      private BroadcastReceiver mReceiver;

      void cleanup() {
         if (this.mReceiver != null) {
            try {
               AppCompatDelegateImpl.this.mContext.unregisterReceiver(this.mReceiver);
            } catch (IllegalArgumentException var2) {
            }

            this.mReceiver = null;
         }

      }

      abstract IntentFilter createIntentFilterForBroadcastReceiver();

      abstract int getApplyableNightMode();

      boolean isListening() {
         return this.mReceiver != null;
      }

      abstract void onChange();

      void setup() {
         this.cleanup();
         IntentFilter var1 = this.createIntentFilterForBroadcastReceiver();
         if (var1 != null) {
            if (var1.countActions() != 0) {
               if (this.mReceiver == null) {
                  this.mReceiver = new BroadcastReceiver() {
                     public void onReceive(Context var1, Intent var2) {
                        AutoNightModeManager.this.onChange();
                     }
                  };
               }

               AppCompatDelegateImpl.this.mContext.registerReceiver(this.mReceiver, var1);
            }
         }
      }
   }

   private class AutoTimeNightModeManager extends AppCompatDelegateImpl.AutoNightModeManager {
      private final TwilightManager mTwilightManager;

      AutoTimeNightModeManager(TwilightManager var2) {
         super();
         this.mTwilightManager = var2;
      }

      IntentFilter createIntentFilterForBroadcastReceiver() {
         IntentFilter var1 = new IntentFilter();
         var1.addAction("android.intent.action.TIME_SET");
         var1.addAction("android.intent.action.TIMEZONE_CHANGED");
         var1.addAction("android.intent.action.TIME_TICK");
         return var1;
      }

      public int getApplyableNightMode() {
         return this.mTwilightManager.isNight() ? 2 : 1;
      }

      public void onChange() {
         AppCompatDelegateImpl.this.applyDayNight();
      }
   }

   private class ListMenuDecorView extends ContentFrameLayout {
      public ListMenuDecorView(Context var2) {
         super(var2);
      }

      private boolean isOutOfBounds(int var1, int var2) {
         return var1 < -5 || var2 < -5 || var1 > this.getWidth() + 5 || var2 > this.getHeight() + 5;
      }

      public boolean dispatchKeyEvent(KeyEvent var1) {
         return AppCompatDelegateImpl.this.dispatchKeyEvent(var1) || super.dispatchKeyEvent(var1);
      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         if (var1.getAction() == 0 && this.isOutOfBounds((int)var1.getX(), (int)var1.getY())) {
            AppCompatDelegateImpl.this.closePanel(0);
            return true;
         } else {
            return super.onInterceptTouchEvent(var1);
         }
      }

      public void setBackgroundResource(int var1) {
         this.setBackgroundDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
      }
   }

   protected static final class PanelFeatureState {
      int background;
      View createdPanelView;
      ViewGroup decorView;
      int featureId;
      Bundle frozenActionViewState;
      Bundle frozenMenuState;
      int gravity;
      boolean isHandled;
      boolean isOpen;
      boolean isPrepared;
      ListMenuPresenter listMenuPresenter;
      Context listPresenterContext;
      MenuBuilder menu;
      public boolean qwertyMode;
      boolean refreshDecorView;
      boolean refreshMenuContent;
      View shownPanelView;
      boolean wasLastOpen;
      int windowAnimations;
      // $FF: renamed from: x int
      int field_120;
      // $FF: renamed from: y int
      int field_121;

      PanelFeatureState(int var1) {
         this.featureId = var1;
         this.refreshDecorView = false;
      }

      void applyFrozenState() {
         MenuBuilder var1 = this.menu;
         if (var1 != null) {
            Bundle var2 = this.frozenMenuState;
            if (var2 != null) {
               var1.restorePresenterStates(var2);
               this.frozenMenuState = null;
            }
         }

      }

      public void clearMenuPresenters() {
         MenuBuilder var1 = this.menu;
         if (var1 != null) {
            var1.removeMenuPresenter(this.listMenuPresenter);
         }

         this.listMenuPresenter = null;
      }

      MenuView getListMenuView(MenuPresenter.Callback var1) {
         if (this.menu == null) {
            return null;
         } else {
            if (this.listMenuPresenter == null) {
               ListMenuPresenter var2 = new ListMenuPresenter(this.listPresenterContext, layout.abc_list_menu_item_layout);
               this.listMenuPresenter = var2;
               var2.setCallback(var1);
               this.menu.addMenuPresenter(this.listMenuPresenter);
            }

            return this.listMenuPresenter.getMenuView(this.decorView);
         }
      }

      public boolean hasPanelItems() {
         View var2 = this.shownPanelView;
         boolean var1 = false;
         if (var2 == null) {
            return false;
         } else if (this.createdPanelView != null) {
            return true;
         } else {
            if (this.listMenuPresenter.getAdapter().getCount() > 0) {
               var1 = true;
            }

            return var1;
         }
      }

      void onRestoreInstanceState(Parcelable var1) {
         AppCompatDelegateImpl.PanelFeatureState.SavedState var2 = (AppCompatDelegateImpl.PanelFeatureState.SavedState)var1;
         this.featureId = var2.featureId;
         this.wasLastOpen = var2.isOpen;
         this.frozenMenuState = var2.menuState;
         this.shownPanelView = null;
         this.decorView = null;
      }

      Parcelable onSaveInstanceState() {
         AppCompatDelegateImpl.PanelFeatureState.SavedState var1 = new AppCompatDelegateImpl.PanelFeatureState.SavedState();
         var1.featureId = this.featureId;
         var1.isOpen = this.isOpen;
         if (this.menu != null) {
            var1.menuState = new Bundle();
            this.menu.savePresenterStates(var1.menuState);
         }

         return var1;
      }

      void setMenu(MenuBuilder var1) {
         MenuBuilder var2 = this.menu;
         if (var1 != var2) {
            if (var2 != null) {
               var2.removeMenuPresenter(this.listMenuPresenter);
            }

            this.menu = var1;
            if (var1 != null) {
               ListMenuPresenter var3 = this.listMenuPresenter;
               if (var3 != null) {
                  var1.addMenuPresenter(var3);
               }
            }

         }
      }

      void setStyle(Context var1) {
         TypedValue var3 = new TypedValue();
         Theme var2 = var1.getResources().newTheme();
         var2.setTo(var1.getTheme());
         var2.resolveAttribute(attr.actionBarPopupTheme, var3, true);
         if (var3.resourceId != 0) {
            var2.applyStyle(var3.resourceId, true);
         }

         var2.resolveAttribute(attr.panelMenuListTheme, var3, true);
         if (var3.resourceId != 0) {
            var2.applyStyle(var3.resourceId, true);
         } else {
            var2.applyStyle(style.Theme_AppCompat_CompactMenu, true);
         }

         ContextThemeWrapper var4 = new ContextThemeWrapper(var1, 0);
         var4.getTheme().setTo(var2);
         this.listPresenterContext = var4;
         TypedArray var5 = var4.obtainStyledAttributes(styleable.AppCompatTheme);
         this.background = var5.getResourceId(styleable.AppCompatTheme_panelBackground, 0);
         this.windowAnimations = var5.getResourceId(styleable.AppCompatTheme_android_windowAnimationStyle, 0);
         var5.recycle();
      }

      private static class SavedState implements Parcelable {
         public static final Creator CREATOR = new ClassLoaderCreator() {
            public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel var1) {
               return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(var1, (ClassLoader)null);
            }

            public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
               return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(var1, var2);
            }

            public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int var1) {
               return new AppCompatDelegateImpl.PanelFeatureState.SavedState[var1];
            }
         };
         int featureId;
         boolean isOpen;
         Bundle menuState;

         SavedState() {
         }

         static AppCompatDelegateImpl.PanelFeatureState.SavedState readFromParcel(Parcel var0, ClassLoader var1) {
            AppCompatDelegateImpl.PanelFeatureState.SavedState var4 = new AppCompatDelegateImpl.PanelFeatureState.SavedState();
            var4.featureId = var0.readInt();
            int var2 = var0.readInt();
            boolean var3 = true;
            if (var2 != 1) {
               var3 = false;
            }

            var4.isOpen = var3;
            if (var3) {
               var4.menuState = var0.readBundle(var1);
            }

            return var4;
         }

         public int describeContents() {
            return 0;
         }

         public void writeToParcel(Parcel var1, int var2) {
            throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
         }
      }
   }

   private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
      PanelMenuPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         MenuBuilder var4 = var1.getRootMenu();
         boolean var3;
         if (var4 != var1) {
            var3 = true;
         } else {
            var3 = false;
         }

         AppCompatDelegateImpl var5 = AppCompatDelegateImpl.this;
         if (var3) {
            var1 = var4;
         }

         AppCompatDelegateImpl.PanelFeatureState var6 = var5.findMenuPanel(var1);
         if (var6 != null) {
            if (var3) {
               AppCompatDelegateImpl.this.callOnPanelClosed(var6.featureId, var6, var4);
               AppCompatDelegateImpl.this.closePanel(var6, true);
               return;
            }

            AppCompatDelegateImpl.this.closePanel(var6, var2);
         }

      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         if (var1 == null && AppCompatDelegateImpl.this.mHasActionBar) {
            Callback var2 = AppCompatDelegateImpl.this.getWindowCallback();
            if (var2 != null && !AppCompatDelegateImpl.this.mIsDestroyed) {
               var2.onMenuOpened(108, var1);
            }
         }

         return true;
      }
   }
}
