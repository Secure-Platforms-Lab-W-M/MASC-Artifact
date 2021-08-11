package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$color;
import android.support.v7.appcompat.R$id;
import android.support.v7.appcompat.R$layout;
import android.support.v7.appcompat.R$style;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.FitWindowsViewGroup;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.LayoutInflater.Factory;
import android.view.LayoutInflater.Factory2;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window.Callback;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;

@RequiresApi(14)
class AppCompatDelegateImplV9 extends AppCompatDelegateImplBase implements MenuBuilder.Callback, Factory2 {
   private static final boolean IS_PRE_LOLLIPOP;
   private AppCompatDelegateImplV9.ActionMenuPresenterCallback mActionMenuPresenterCallback;
   ActionMode mActionMode;
   PopupWindow mActionModePopup;
   ActionBarContextView mActionModeView;
   private AppCompatViewInflater mAppCompatViewInflater;
   private boolean mClosingActionMenu;
   private DecorContentParent mDecorContentParent;
   private boolean mEnableDefaultActionBarUp;
   ViewPropertyAnimatorCompat mFadeAnim = null;
   private boolean mFeatureIndeterminateProgress;
   private boolean mFeatureProgress;
   int mInvalidatePanelMenuFeatures;
   boolean mInvalidatePanelMenuPosted;
   private final Runnable mInvalidatePanelMenuRunnable = new Runnable() {
      public void run() {
         if ((AppCompatDelegateImplV9.this.mInvalidatePanelMenuFeatures & 1) != 0) {
            AppCompatDelegateImplV9.this.doInvalidatePanelMenu(0);
         }

         if ((AppCompatDelegateImplV9.this.mInvalidatePanelMenuFeatures & 4096) != 0) {
            AppCompatDelegateImplV9.this.doInvalidatePanelMenu(108);
         }

         AppCompatDelegateImplV9 var1 = AppCompatDelegateImplV9.this;
         var1.mInvalidatePanelMenuPosted = false;
         var1.mInvalidatePanelMenuFeatures = 0;
      }
   };
   private boolean mLongPressBackDown;
   private AppCompatDelegateImplV9.PanelMenuPresenterCallback mPanelMenuPresenterCallback;
   private AppCompatDelegateImplV9.PanelFeatureState[] mPanels;
   private AppCompatDelegateImplV9.PanelFeatureState mPreparedPanel;
   Runnable mShowActionModePopup;
   private View mStatusGuard;
   private ViewGroup mSubDecor;
   private boolean mSubDecorInstalled;
   private Rect mTempRect1;
   private Rect mTempRect2;
   private TextView mTitleView;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      IS_PRE_LOLLIPOP = var0;
   }

   AppCompatDelegateImplV9(Context var1, Window var2, AppCompatCallback var3) {
      super(var1, var2, var3);
   }

   private void applyFixedSizeWindow() {
      ContentFrameLayout var1 = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
      View var2 = this.mWindow.getDecorView();
      var1.setDecorPadding(var2.getPaddingLeft(), var2.getPaddingTop(), var2.getPaddingRight(), var2.getPaddingBottom());
      TypedArray var3 = this.mContext.obtainStyledAttributes(R$styleable.AppCompatTheme);
      var3.getValue(R$styleable.AppCompatTheme_windowMinWidthMajor, var1.getMinWidthMajor());
      var3.getValue(R$styleable.AppCompatTheme_windowMinWidthMinor, var1.getMinWidthMinor());
      if (var3.hasValue(R$styleable.AppCompatTheme_windowFixedWidthMajor)) {
         var3.getValue(R$styleable.AppCompatTheme_windowFixedWidthMajor, var1.getFixedWidthMajor());
      }

      if (var3.hasValue(R$styleable.AppCompatTheme_windowFixedWidthMinor)) {
         var3.getValue(R$styleable.AppCompatTheme_windowFixedWidthMinor, var1.getFixedWidthMinor());
      }

      if (var3.hasValue(R$styleable.AppCompatTheme_windowFixedHeightMajor)) {
         var3.getValue(R$styleable.AppCompatTheme_windowFixedHeightMajor, var1.getFixedHeightMajor());
      }

      if (var3.hasValue(R$styleable.AppCompatTheme_windowFixedHeightMinor)) {
         var3.getValue(R$styleable.AppCompatTheme_windowFixedHeightMinor, var1.getFixedHeightMinor());
      }

      var3.recycle();
      var1.requestLayout();
   }

   private ViewGroup createSubDecor() {
      TypedArray var1 = this.mContext.obtainStyledAttributes(R$styleable.AppCompatTheme);
      if (var1.hasValue(R$styleable.AppCompatTheme_windowActionBar)) {
         if (var1.getBoolean(R$styleable.AppCompatTheme_windowNoTitle, false)) {
            this.requestWindowFeature(1);
         } else if (var1.getBoolean(R$styleable.AppCompatTheme_windowActionBar, false)) {
            this.requestWindowFeature(108);
         }

         if (var1.getBoolean(R$styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            this.requestWindowFeature(109);
         }

         if (var1.getBoolean(R$styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            this.requestWindowFeature(10);
         }

         this.mIsFloating = var1.getBoolean(R$styleable.AppCompatTheme_android_windowIsFloating, false);
         var1.recycle();
         this.mWindow.getDecorView();
         LayoutInflater var2 = LayoutInflater.from(this.mContext);
         ViewGroup var6 = null;
         if (!this.mWindowNoTitle) {
            if (this.mIsFloating) {
               var6 = (ViewGroup)var2.inflate(R$layout.abc_dialog_title_material, (ViewGroup)null);
               this.mOverlayActionBar = false;
               this.mHasActionBar = false;
            } else if (this.mHasActionBar) {
               TypedValue var7 = new TypedValue();
               this.mContext.getTheme().resolveAttribute(R$attr.actionBarTheme, var7, true);
               Object var9;
               if (var7.resourceId != 0) {
                  var9 = new ContextThemeWrapper(this.mContext, var7.resourceId);
               } else {
                  var9 = this.mContext;
               }

               var6 = (ViewGroup)LayoutInflater.from((Context)var9).inflate(R$layout.abc_screen_toolbar, (ViewGroup)null);
               this.mDecorContentParent = (DecorContentParent)var6.findViewById(R$id.decor_content_parent);
               this.mDecorContentParent.setWindowCallback(this.getWindowCallback());
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
               var6 = (ViewGroup)var2.inflate(R$layout.abc_screen_simple_overlay_action_mode, (ViewGroup)null);
            } else {
               var6 = (ViewGroup)var2.inflate(R$layout.abc_screen_simple, (ViewGroup)null);
            }

            if (VERSION.SDK_INT >= 21) {
               ViewCompat.setOnApplyWindowInsetsListener(var6, new OnApplyWindowInsetsListener() {
                  public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
                     int var3 = var2.getSystemWindowInsetTop();
                     int var4 = AppCompatDelegateImplV9.this.updateStatusGuard(var3);
                     WindowInsetsCompat var5 = var2;
                     if (var3 != var4) {
                        var5 = var2.replaceSystemWindowInsets(var2.getSystemWindowInsetLeft(), var4, var2.getSystemWindowInsetRight(), var2.getSystemWindowInsetBottom());
                     }

                     return ViewCompat.onApplyWindowInsets(var1, var5);
                  }
               });
            } else {
               ((FitWindowsViewGroup)var6).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                  public void onFitSystemWindows(Rect var1) {
                     var1.top = AppCompatDelegateImplV9.this.updateStatusGuard(var1.top);
                  }
               });
            }
         }

         if (var6 != null) {
            if (this.mDecorContentParent == null) {
               this.mTitleView = (TextView)var6.findViewById(R$id.title);
            }

            ViewUtils.makeOptionalFitsSystemWindows(var6);
            ContentFrameLayout var8 = (ContentFrameLayout)var6.findViewById(R$id.action_bar_activity_content);
            ViewGroup var3 = (ViewGroup)this.mWindow.findViewById(16908290);
            if (var3 != null) {
               while(var3.getChildCount() > 0) {
                  View var4 = var3.getChildAt(0);
                  var3.removeViewAt(0);
                  var8.addView(var4);
               }

               var3.setId(-1);
               var8.setId(16908290);
               if (var3 instanceof FrameLayout) {
                  ((FrameLayout)var3).setForeground((Drawable)null);
               }
            }

            this.mWindow.setContentView(var6);
            var8.setAttachListener(new ContentFrameLayout.OnAttachListener() {
               public void onAttachedFromWindow() {
               }

               public void onDetachedFromWindow() {
                  AppCompatDelegateImplV9.this.dismissPopups();
               }
            });
            return var6;
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
         IllegalStateException var5 = new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
         throw var5;
      }
   }

   private void ensureSubDecor() {
      if (!this.mSubDecorInstalled) {
         this.mSubDecor = this.createSubDecor();
         CharSequence var1 = this.getTitle();
         if (!TextUtils.isEmpty(var1)) {
            this.onTitleChanged(var1);
         }

         this.applyFixedSizeWindow();
         this.onSubDecorInstalled(this.mSubDecor);
         this.mSubDecorInstalled = true;
         AppCompatDelegateImplV9.PanelFeatureState var2 = this.getPanelState(0, false);
         if (!this.isDestroyed() && (var2 == null || var2.menu == null)) {
            this.invalidatePanelMenu(108);
         }
      }

   }

   private boolean initializePanelContent(AppCompatDelegateImplV9.PanelFeatureState var1) {
      if (var1.createdPanelView != null) {
         var1.shownPanelView = var1.createdPanelView;
         return true;
      } else if (var1.menu == null) {
         return false;
      } else {
         if (this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new AppCompatDelegateImplV9.PanelMenuPresenterCallback();
         }

         var1.shownPanelView = (View)var1.getListMenuView(this.mPanelMenuPresenterCallback);
         return var1.shownPanelView != null;
      }
   }

   private boolean initializePanelDecor(AppCompatDelegateImplV9.PanelFeatureState var1) {
      var1.setStyle(this.getActionBarThemedContext());
      var1.decorView = new AppCompatDelegateImplV9.ListMenuDecorView(var1.listPresenterContext);
      var1.gravity = 81;
      return true;
   }

   private boolean initializePanelMenu(AppCompatDelegateImplV9.PanelFeatureState var1) {
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
            var6.resolveAttribute(R$attr.actionBarTheme, var5, true);
            Theme var7 = null;
            if (var5.resourceId != 0) {
               var7 = var4.getResources().newTheme();
               var7.setTo(var6);
               var7.applyStyle(var5.resourceId, true);
               var7.resolveAttribute(R$attr.actionBarWidgetTheme, var5, true);
            } else {
               var6.resolveAttribute(R$attr.actionBarWidgetTheme, var5, true);
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

   private boolean onKeyDownPanel(int var1, KeyEvent var2) {
      if (var2.getRepeatCount() == 0) {
         AppCompatDelegateImplV9.PanelFeatureState var3 = this.getPanelState(var1, true);
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
            AppCompatDelegateImplV9.PanelFeatureState var6 = this.getPanelState(var1, true);
            if (var1 == 0) {
               DecorContentParent var7 = this.mDecorContentParent;
               if (var7 != null && var7.canShowOverflowMenu() && !ViewConfiguration.get(this.mContext).hasPermanentMenuKey()) {
                  if (!this.mDecorContentParent.isOverflowMenuShowing()) {
                     var3 = var5;
                     if (!this.isDestroyed()) {
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

   private void openPanel(AppCompatDelegateImplV9.PanelFeatureState var1, KeyEvent var2) {
      if (!var1.isOpen) {
         if (!this.isDestroyed()) {
            if (var1.featureId == 0) {
               Context var5 = this.mContext;
               boolean var3;
               if ((var5.getResources().getConfiguration().screenLayout & 15) == 4) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               boolean var4;
               if (var5.getApplicationInfo().targetSdkVersion >= 11) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               if (var3 && var4) {
                  return;
               }
            }

            Callback var12 = this.getWindowCallback();
            if (var12 != null && !var12.onMenuOpened(var1.featureId, var1.menu)) {
               this.closePanel(var1, true);
            } else {
               WindowManager var6 = (WindowManager)this.mContext.getSystemService("window");
               if (var6 != null) {
                  if (this.preparePanel(var1, var2)) {
                     byte var10;
                     label108: {
                        byte var11 = -2;
                        LayoutParams var7;
                        if (var1.decorView != null && !var1.refreshDecorView) {
                           if (var1.createdPanelView != null) {
                              var7 = var1.createdPanelView.getLayoutParams();
                              var10 = var11;
                              if (var7 != null) {
                                 var10 = var11;
                                 if (var7.width == -1) {
                                    var10 = -1;
                                 }
                              }
                              break label108;
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

                           LayoutParams var13 = var1.shownPanelView.getLayoutParams();
                           var7 = var13;
                           if (var13 == null) {
                              var7 = new LayoutParams(-2, -2);
                           }

                           int var9 = var1.background;
                           var1.decorView.setBackgroundResource(var9);
                           ViewParent var14 = var1.shownPanelView.getParent();
                           if (var14 != null && var14 instanceof ViewGroup) {
                              ((ViewGroup)var14).removeView(var1.shownPanelView);
                           }

                           var1.decorView.addView(var1.shownPanelView, var7);
                           if (!var1.shownPanelView.hasFocus()) {
                              var1.shownPanelView.requestFocus();
                           }
                        }

                        var10 = var11;
                     }

                     var1.isHandled = false;
                     android.view.WindowManager.LayoutParams var8 = new android.view.WindowManager.LayoutParams(var10, -2, var1.field_19, var1.field_20, 1002, 8519680, -3);
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

   private boolean performPanelShortcut(AppCompatDelegateImplV9.PanelFeatureState var1, int var2, KeyEvent var3, int var4) {
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

   private boolean preparePanel(AppCompatDelegateImplV9.PanelFeatureState var1, KeyEvent var2) {
      if (this.isDestroyed()) {
         return false;
      } else if (var1.isPrepared) {
         return true;
      } else {
         AppCompatDelegateImplV9.PanelFeatureState var5 = this.mPreparedPanel;
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
                     this.mActionMenuPresenterCallback = new AppCompatDelegateImplV9.ActionMenuPresenterCallback();
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
            if (!this.isDestroyed()) {
               var6.onPanelClosed(108, this.getPanelState(0, true).menu);
               return;
            }
         } else if (var6 != null && !this.isDestroyed()) {
            if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
               this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
               this.mInvalidatePanelMenuRunnable.run();
            }

            AppCompatDelegateImplV9.PanelFeatureState var3 = this.getPanelState(0, true);
            if (var3.menu != null && !var3.refreshMenuContent && var6.onPreparePanel(0, var3.createdPanelView, var3.menu)) {
               var6.onMenuOpened(108, var3.menu);
               this.mDecorContentParent.showOverflowMenu();
            }
         }

      } else {
         AppCompatDelegateImplV9.PanelFeatureState var5 = this.getPanelState(0, true);
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

   public void addContentView(View var1, LayoutParams var2) {
      this.ensureSubDecor();
      ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(var1, var2);
      this.mOriginalWindowCallback.onContentChanged();
   }

   View callActivityOnCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      if (this.mOriginalWindowCallback instanceof Factory) {
         var1 = ((Factory)this.mOriginalWindowCallback).onCreateView(var2, var3, var4);
         if (var1 != null) {
            return var1;
         }
      }

      return null;
   }

   void callOnPanelClosed(int var1, AppCompatDelegateImplV9.PanelFeatureState var2, Menu var3) {
      AppCompatDelegateImplV9.PanelFeatureState var5 = var2;
      Object var6 = var3;
      if (var3 == null) {
         AppCompatDelegateImplV9.PanelFeatureState var4 = var2;
         if (var2 == null) {
            var4 = var2;
            if (var1 >= 0) {
               AppCompatDelegateImplV9.PanelFeatureState[] var7 = this.mPanels;
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
         if (!this.isDestroyed()) {
            this.mOriginalWindowCallback.onPanelClosed(var1, (Menu)var6);
         }

      }
   }

   void checkCloseActionMenu(MenuBuilder var1) {
      if (!this.mClosingActionMenu) {
         this.mClosingActionMenu = true;
         this.mDecorContentParent.dismissPopups();
         Callback var2 = this.getWindowCallback();
         if (var2 != null && !this.isDestroyed()) {
            var2.onPanelClosed(108, var1);
         }

         this.mClosingActionMenu = false;
      }
   }

   void closePanel(int var1) {
      this.closePanel(this.getPanelState(var1, true), true);
   }

   void closePanel(AppCompatDelegateImplV9.PanelFeatureState var1, boolean var2) {
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

   public View createView(View var1, String var2, @NonNull Context var3, @NonNull AttributeSet var4) {
      if (this.mAppCompatViewInflater == null) {
         this.mAppCompatViewInflater = new AppCompatViewInflater();
      }

      boolean var5 = false;
      if (IS_PRE_LOLLIPOP) {
         boolean var6 = var4 instanceof XmlPullParser;
         var5 = true;
         if (var6) {
            if (((XmlPullParser)var4).getDepth() <= 1) {
               var5 = false;
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
      AppCompatDelegateImplV9.PanelFeatureState var3 = this.getPanelState(0, false);
      if (var3 != null && var3.menu != null) {
         var3.menu.close();
      }

   }

   boolean dispatchKeyEvent(KeyEvent var1) {
      int var3 = var1.getKeyCode();
      boolean var2 = true;
      if (var3 == 82 && this.mOriginalWindowCallback.dispatchKeyEvent(var1)) {
         return true;
      } else {
         var3 = var1.getKeyCode();
         if (var1.getAction() != 0) {
            var2 = false;
         }

         return var2 ? this.onKeyDown(var3, var1) : this.onKeyUp(var3, var1);
      }
   }

   void doInvalidatePanelMenu(int var1) {
      AppCompatDelegateImplV9.PanelFeatureState var2 = this.getPanelState(var1, true);
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

   AppCompatDelegateImplV9.PanelFeatureState findMenuPanel(Menu var1) {
      AppCompatDelegateImplV9.PanelFeatureState[] var4 = this.mPanels;
      int var2;
      if (var4 != null) {
         var2 = var4.length;
      } else {
         var2 = 0;
      }

      for(int var3 = 0; var3 < var2; ++var3) {
         AppCompatDelegateImplV9.PanelFeatureState var5 = var4[var3];
         if (var5 != null && var5.menu == var1) {
            return var5;
         }
      }

      return null;
   }

   @Nullable
   public View findViewById(@IdRes int var1) {
      this.ensureSubDecor();
      return this.mWindow.findViewById(var1);
   }

   protected AppCompatDelegateImplV9.PanelFeatureState getPanelState(int var1, boolean var2) {
      AppCompatDelegateImplV9.PanelFeatureState[] var3;
      label22: {
         var3 = this.mPanels;
         AppCompatDelegateImplV9.PanelFeatureState[] var4 = var3;
         if (var3 != null) {
            var3 = var3;
            if (var4.length > var1) {
               break label22;
            }
         }

         AppCompatDelegateImplV9.PanelFeatureState[] var5 = new AppCompatDelegateImplV9.PanelFeatureState[var1 + 1];
         if (var3 != null) {
            System.arraycopy(var3, 0, var5, 0, var3.length);
         }

         var3 = var5;
         this.mPanels = var5;
      }

      AppCompatDelegateImplV9.PanelFeatureState var6 = var3[var1];
      AppCompatDelegateImplV9.PanelFeatureState var7 = var6;
      if (var6 == null) {
         var6 = new AppCompatDelegateImplV9.PanelFeatureState(var1);
         var7 = var6;
         var3[var1] = var6;
      }

      return var7;
   }

   ViewGroup getSubDecor() {
      return this.mSubDecor;
   }

   public boolean hasWindowFeature(int var1) {
      var1 = this.sanitizeWindowFeatureId(var1);
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 != 5) {
               if (var1 != 10) {
                  if (var1 != 108) {
                     return var1 != 109 ? false : this.mOverlayActionBar;
                  } else {
                     return this.mHasActionBar;
                  }
               } else {
                  return this.mOverlayActionMode;
               }
            } else {
               return this.mFeatureIndeterminateProgress;
            }
         } else {
            return this.mFeatureProgress;
         }
      } else {
         return this.mWindowNoTitle;
      }
   }

   public void initWindowDecorActionBar() {
      this.ensureSubDecor();
      if (this.mHasActionBar) {
         if (this.mActionBar == null) {
            if (this.mOriginalWindowCallback instanceof Activity) {
               this.mActionBar = new WindowDecorActionBar((Activity)this.mOriginalWindowCallback, this.mOverlayActionBar);
            } else if (this.mOriginalWindowCallback instanceof Dialog) {
               this.mActionBar = new WindowDecorActionBar((Dialog)this.mOriginalWindowCallback);
            }

            if (this.mActionBar != null) {
               this.mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            }

         }
      }
   }

   public void installViewFactory() {
      LayoutInflater var1 = LayoutInflater.from(this.mContext);
      if (var1.getFactory() == null) {
         LayoutInflaterCompat.setFactory2(var1, this);
      } else {
         if (!(var1.getFactory2() instanceof AppCompatDelegateImplV9)) {
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
      this.applyDayNight();
   }

   public void onCreate(Bundle var1) {
      if (this.mOriginalWindowCallback instanceof Activity && NavUtils.getParentActivityName((Activity)this.mOriginalWindowCallback) != null) {
         ActionBar var2 = this.peekSupportActionBar();
         if (var2 == null) {
            this.mEnableDefaultActionBarUp = true;
            return;
         }

         var2.setDefaultDisplayHomeAsUpEnabled(true);
      }

   }

   public final View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      View var5 = this.callActivityOnCreateView(var1, var2, var3, var4);
      return var5 != null ? var5 : this.createView(var1, var2, var3, var4);
   }

   public View onCreateView(String var1, Context var2, AttributeSet var3) {
      return this.onCreateView((View)null, var1, var2, var3);
   }

   public void onDestroy() {
      if (this.mInvalidatePanelMenuPosted) {
         this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
      }

      super.onDestroy();
      if (this.mActionBar != null) {
         this.mActionBar.onDestroy();
      }

   }

   boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3 = true;
      if (var1 != 4) {
         if (var1 == 82) {
            this.onKeyDownPanel(0, var2);
            return true;
         }
      } else {
         if ((var2.getFlags() & 128) == 0) {
            var3 = false;
         }

         this.mLongPressBackDown = var3;
      }

      if (VERSION.SDK_INT < 11) {
         this.onKeyShortcut(var1, var2);
      }

      return false;
   }

   boolean onKeyShortcut(int var1, KeyEvent var2) {
      ActionBar var4 = this.getSupportActionBar();
      if (var4 != null && var4.onKeyShortcut(var1, var2)) {
         return true;
      } else {
         AppCompatDelegateImplV9.PanelFeatureState var6 = this.mPreparedPanel;
         if (var6 != null && this.performPanelShortcut(var6, var2.getKeyCode(), var2, 1)) {
            AppCompatDelegateImplV9.PanelFeatureState var5 = this.mPreparedPanel;
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
         AppCompatDelegateImplV9.PanelFeatureState var4 = this.getPanelState(0, false);
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
      if (var3 != null && !this.isDestroyed()) {
         AppCompatDelegateImplV9.PanelFeatureState var4 = this.findMenuPanel(var1.getRootMenu());
         if (var4 != null) {
            return var3.onMenuItemSelected(var4.featureId, var2);
         }
      }

      return false;
   }

   public void onMenuModeChange(MenuBuilder var1) {
      this.reopenMenu(var1, true);
   }

   boolean onMenuOpened(int var1, Menu var2) {
      if (var1 == 108) {
         ActionBar var3 = this.getSupportActionBar();
         if (var3 != null) {
            var3.dispatchMenuVisibilityChanged(true);
         }

         return true;
      } else {
         return false;
      }
   }

   void onPanelClosed(int var1, Menu var2) {
      if (var1 == 108) {
         ActionBar var3 = this.getSupportActionBar();
         if (var3 != null) {
            var3.dispatchMenuVisibilityChanged(false);
         }
      } else if (var1 == 0) {
         AppCompatDelegateImplV9.PanelFeatureState var4 = this.getPanelState(var1, true);
         if (var4.isOpen) {
            this.closePanel(var4, false);
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

   public void onStop() {
      ActionBar var1 = this.getSupportActionBar();
      if (var1 != null) {
         var1.setShowHideAnimationEnabled(false);
      }

   }

   void onSubDecorInstalled(ViewGroup var1) {
   }

   void onTitleChanged(CharSequence var1) {
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
      this.mOriginalWindowCallback.onContentChanged();
   }

   public void setContentView(View var1) {
      this.ensureSubDecor();
      ViewGroup var2 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var2.removeAllViews();
      var2.addView(var1);
      this.mOriginalWindowCallback.onContentChanged();
   }

   public void setContentView(View var1, LayoutParams var2) {
      this.ensureSubDecor();
      ViewGroup var3 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var3.removeAllViews();
      var3.addView(var1, var2);
      this.mOriginalWindowCallback.onContentChanged();
   }

   public void setSupportActionBar(Toolbar var1) {
      if (this.mOriginalWindowCallback instanceof Activity) {
         ActionBar var2 = this.getSupportActionBar();
         if (!(var2 instanceof WindowDecorActionBar)) {
            this.mMenuInflater = null;
            if (var2 != null) {
               var2.onDestroy();
            }

            if (var1 != null) {
               ToolbarActionBar var3 = new ToolbarActionBar(var1, ((Activity)this.mOriginalWindowCallback).getTitle(), this.mAppCompatWindowCallback);
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

   final boolean shouldAnimateActionModeView() {
      if (this.mSubDecorInstalled) {
         ViewGroup var1 = this.mSubDecor;
         if (var1 != null && ViewCompat.isLaidOut(var1)) {
            return true;
         }
      }

      return false;
   }

   public ActionMode startSupportActionMode(@NonNull ActionMode.Callback var1) {
      if (var1 != null) {
         ActionMode var2 = this.mActionMode;
         if (var2 != null) {
            var2.finish();
         }

         AppCompatDelegateImplV9.ActionModeCallbackWrapperV9 var3 = new AppCompatDelegateImplV9.ActionModeCallbackWrapperV9(var1);
         ActionBar var4 = this.getSupportActionBar();
         if (var4 != null) {
            this.mActionMode = var4.startActionMode(var3);
            if (this.mActionMode != null && this.mAppCompatCallback != null) {
               this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
            }
         }

         if (this.mActionMode == null) {
            this.mActionMode = this.startSupportActionModeFromWindow(var3);
         }

         return this.mActionMode;
      } else {
         throw new IllegalArgumentException("ActionMode callback can not be null.");
      }
   }

   ActionMode startSupportActionModeFromWindow(@NonNull ActionMode.Callback var1) {
      this.endOnGoingFadeAnimation();
      ActionMode var4 = this.mActionMode;
      if (var4 != null) {
         var4.finish();
      }

      Object var12 = var1;
      if (!(var1 instanceof AppCompatDelegateImplV9.ActionModeCallbackWrapperV9)) {
         var12 = new AppCompatDelegateImplV9.ActionModeCallbackWrapperV9(var1);
      }

      TypedValue var5 = null;
      ActionMode var8 = var5;
      if (this.mAppCompatCallback != null) {
         var8 = var5;
         if (!this.isDestroyed()) {
            try {
               var8 = this.mAppCompatCallback.onWindowStartingSupportActionMode((ActionMode.Callback)var12);
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
               var10.resolveAttribute(R$attr.actionBarTheme, var5, true);
               Object var11;
               if (var5.resourceId != 0) {
                  Theme var6 = this.mContext.getResources().newTheme();
                  var6.setTo(var10);
                  var6.applyStyle(var5.resourceId, true);
                  var11 = new ContextThemeWrapper(this.mContext, 0);
                  ((Context)var11).getTheme().setTo(var6);
               } else {
                  var11 = this.mContext;
               }

               this.mActionModeView = new ActionBarContextView((Context)var11);
               this.mActionModePopup = new PopupWindow((Context)var11, (AttributeSet)null, R$attr.actionModePopupWindowStyle);
               PopupWindowCompat.setWindowLayoutType(this.mActionModePopup, 2);
               this.mActionModePopup.setContentView(this.mActionModeView);
               this.mActionModePopup.setWidth(-1);
               ((Context)var11).getTheme().resolveAttribute(R$attr.actionBarSize, var5, true);
               int var2 = TypedValue.complexToDimensionPixelSize(var5.data, ((Context)var11).getResources().getDisplayMetrics());
               this.mActionModeView.setContentHeight(var2);
               this.mActionModePopup.setHeight(-2);
               this.mShowActionModePopup = new Runnable() {
                  public void run() {
                     AppCompatDelegateImplV9.this.mActionModePopup.showAtLocation(AppCompatDelegateImplV9.this.mActionModeView, 55, 0, 0);
                     AppCompatDelegateImplV9.this.endOnGoingFadeAnimation();
                     if (AppCompatDelegateImplV9.this.shouldAnimateActionModeView()) {
                        AppCompatDelegateImplV9.this.mActionModeView.setAlpha(0.0F);
                        AppCompatDelegateImplV9 var1 = AppCompatDelegateImplV9.this;
                        var1.mFadeAnim = ViewCompat.animate(var1.mActionModeView).alpha(1.0F);
                        AppCompatDelegateImplV9.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                           public void onAnimationEnd(View var1) {
                              AppCompatDelegateImplV9.this.mActionModeView.setAlpha(1.0F);
                              AppCompatDelegateImplV9.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                              AppCompatDelegateImplV9.this.mFadeAnim = null;
                           }

                           public void onAnimationStart(View var1) {
                              AppCompatDelegateImplV9.this.mActionModeView.setVisibility(0);
                           }
                        });
                     } else {
                        AppCompatDelegateImplV9.this.mActionModeView.setAlpha(1.0F);
                        AppCompatDelegateImplV9.this.mActionModeView.setVisibility(0);
                     }
                  }
               };
            } else {
               ViewStubCompat var13 = (ViewStubCompat)this.mSubDecor.findViewById(R$id.action_mode_bar_stub);
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
            ActionBarContextView var16 = this.mActionModeView;
            if (this.mActionModePopup != null) {
               var3 = false;
            }

            StandaloneActionMode var15 = new StandaloneActionMode(var14, var16, (ActionMode.Callback)var12, var3);
            if (((ActionMode.Callback)var12).onCreateActionMode(var15, var15.getMenu())) {
               var15.invalidate();
               this.mActionModeView.initForMode(var15);
               this.mActionMode = var15;
               if (this.shouldAnimateActionModeView()) {
                  this.mActionModeView.setAlpha(0.0F);
                  this.mFadeAnim = ViewCompat.animate(this.mActionModeView).alpha(1.0F);
                  this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                     public void onAnimationEnd(View var1) {
                        AppCompatDelegateImplV9.this.mActionModeView.setAlpha(1.0F);
                        AppCompatDelegateImplV9.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                        AppCompatDelegateImplV9.this.mFadeAnim = null;
                     }

                     public void onAnimationStart(View var1) {
                        AppCompatDelegateImplV9.this.mActionModeView.setVisibility(0);
                        AppCompatDelegateImplV9.this.mActionModeView.sendAccessibilityEvent(32);
                        if (AppCompatDelegateImplV9.this.mActionModeView.getParent() instanceof View) {
                           ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV9.this.mActionModeView.getParent());
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

      if (this.mActionMode != null && this.mAppCompatCallback != null) {
         this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
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
                     this.mStatusGuard = new View(this.mContext);
                     this.mStatusGuard.setBackgroundColor(this.mContext.getResources().getColor(R$color.abc_input_method_navigation_guard));
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

   private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
      ActionMenuPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         AppCompatDelegateImplV9.this.checkCloseActionMenu(var1);
      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         Callback var2 = AppCompatDelegateImplV9.this.getWindowCallback();
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
         if (AppCompatDelegateImplV9.this.mActionModePopup != null) {
            AppCompatDelegateImplV9.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImplV9.this.mShowActionModePopup);
         }

         if (AppCompatDelegateImplV9.this.mActionModeView != null) {
            AppCompatDelegateImplV9.this.endOnGoingFadeAnimation();
            AppCompatDelegateImplV9 var2 = AppCompatDelegateImplV9.this;
            var2.mFadeAnim = ViewCompat.animate(var2.mActionModeView).alpha(0.0F);
            AppCompatDelegateImplV9.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
               public void onAnimationEnd(View var1) {
                  AppCompatDelegateImplV9.this.mActionModeView.setVisibility(8);
                  if (AppCompatDelegateImplV9.this.mActionModePopup != null) {
                     AppCompatDelegateImplV9.this.mActionModePopup.dismiss();
                  } else if (AppCompatDelegateImplV9.this.mActionModeView.getParent() instanceof View) {
                     ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV9.this.mActionModeView.getParent());
                  }

                  AppCompatDelegateImplV9.this.mActionModeView.removeAllViews();
                  AppCompatDelegateImplV9.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                  AppCompatDelegateImplV9.this.mFadeAnim = null;
               }
            });
         }

         if (AppCompatDelegateImplV9.this.mAppCompatCallback != null) {
            AppCompatDelegateImplV9.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImplV9.this.mActionMode);
         }

         AppCompatDelegateImplV9.this.mActionMode = null;
      }

      public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
         return this.mWrapped.onPrepareActionMode(var1, var2);
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
         return AppCompatDelegateImplV9.this.dispatchKeyEvent(var1) || super.dispatchKeyEvent(var1);
      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         if (var1.getAction() == 0 && this.isOutOfBounds((int)var1.getX(), (int)var1.getY())) {
            AppCompatDelegateImplV9.this.closePanel(0);
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
      int field_19;
      // $FF: renamed from: y int
      int field_20;

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
               this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R$layout.abc_list_menu_item_layout);
               this.listMenuPresenter.setCallback(var1);
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
         AppCompatDelegateImplV9.PanelFeatureState.SavedState var2 = (AppCompatDelegateImplV9.PanelFeatureState.SavedState)var1;
         this.featureId = var2.featureId;
         this.wasLastOpen = var2.isOpen;
         this.frozenMenuState = var2.menuState;
         this.shownPanelView = null;
         this.decorView = null;
      }

      Parcelable onSaveInstanceState() {
         AppCompatDelegateImplV9.PanelFeatureState.SavedState var1 = new AppCompatDelegateImplV9.PanelFeatureState.SavedState();
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
         var2.resolveAttribute(R$attr.actionBarPopupTheme, var3, true);
         if (var3.resourceId != 0) {
            var2.applyStyle(var3.resourceId, true);
         }

         var2.resolveAttribute(R$attr.panelMenuListTheme, var3, true);
         if (var3.resourceId != 0) {
            var2.applyStyle(var3.resourceId, true);
         } else {
            var2.applyStyle(R$style.Theme_AppCompat_CompactMenu, true);
         }

         ContextThemeWrapper var4 = new ContextThemeWrapper(var1, 0);
         var4.getTheme().setTo(var2);
         this.listPresenterContext = var4;
         TypedArray var5 = var4.obtainStyledAttributes(R$styleable.AppCompatTheme);
         this.background = var5.getResourceId(R$styleable.AppCompatTheme_panelBackground, 0);
         this.windowAnimations = var5.getResourceId(R$styleable.AppCompatTheme_android_windowAnimationStyle, 0);
         var5.recycle();
      }

      private static class SavedState implements Parcelable {
         public static final Creator CREATOR = new ClassLoaderCreator() {
            public AppCompatDelegateImplV9.PanelFeatureState.SavedState createFromParcel(Parcel var1) {
               return AppCompatDelegateImplV9.PanelFeatureState.SavedState.readFromParcel(var1, (ClassLoader)null);
            }

            public AppCompatDelegateImplV9.PanelFeatureState.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
               return AppCompatDelegateImplV9.PanelFeatureState.SavedState.readFromParcel(var1, var2);
            }

            public AppCompatDelegateImplV9.PanelFeatureState.SavedState[] newArray(int var1) {
               return new AppCompatDelegateImplV9.PanelFeatureState.SavedState[var1];
            }
         };
         int featureId;
         boolean isOpen;
         Bundle menuState;

         SavedState() {
         }

         static AppCompatDelegateImplV9.PanelFeatureState.SavedState readFromParcel(Parcel var0, ClassLoader var1) {
            AppCompatDelegateImplV9.PanelFeatureState.SavedState var4 = new AppCompatDelegateImplV9.PanelFeatureState.SavedState();
            var4.featureId = var0.readInt();
            int var2 = var0.readInt();
            boolean var3 = true;
            if (var2 != 1) {
               var3 = false;
            }

            var4.isOpen = var3;
            if (var4.isOpen) {
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

         AppCompatDelegateImplV9 var5 = AppCompatDelegateImplV9.this;
         if (var3) {
            var1 = var4;
         }

         AppCompatDelegateImplV9.PanelFeatureState var6 = var5.findMenuPanel(var1);
         if (var6 != null) {
            if (var3) {
               AppCompatDelegateImplV9.this.callOnPanelClosed(var6.featureId, var6, var4);
               AppCompatDelegateImplV9.this.closePanel(var6, true);
               return;
            }

            AppCompatDelegateImplV9.this.closePanel(var6, var2);
         }

      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         if (var1 == null && AppCompatDelegateImplV9.this.mHasActionBar) {
            Callback var2 = AppCompatDelegateImplV9.this.getWindowCallback();
            if (var2 != null && !AppCompatDelegateImplV9.this.isDestroyed()) {
               var2.onMenuOpened(108, var1);
            }
         }

         return true;
      }
   }
}
