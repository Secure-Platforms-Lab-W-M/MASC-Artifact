package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.SpinnerAdapter;
import androidx.appcompat.R.attr;
import androidx.appcompat.R.id;
import androidx.appcompat.R.styleable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.ViewPropertyAnimatorCompatSet;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionBarContainer;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.appcompat.widget.DecorToolbar;
import androidx.appcompat.widget.ScrollingTabContainerView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.view.ViewPropertyAnimatorUpdateListener;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class WindowDecorActionBar extends ActionBar implements ActionBarOverlayLayout.ActionBarVisibilityCallback {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   private static final long FADE_IN_DURATION_MS = 200L;
   private static final long FADE_OUT_DURATION_MS = 100L;
   private static final int INVALID_POSITION = -1;
   private static final String TAG = "WindowDecorActionBar";
   private static final Interpolator sHideInterpolator = new AccelerateInterpolator();
   private static final Interpolator sShowInterpolator = new DecelerateInterpolator();
   WindowDecorActionBar.ActionModeImpl mActionMode;
   private Activity mActivity;
   ActionBarContainer mContainerView;
   boolean mContentAnimations = true;
   View mContentView;
   Context mContext;
   ActionBarContextView mContextView;
   private int mCurWindowVisibility = 0;
   ViewPropertyAnimatorCompatSet mCurrentShowAnim;
   DecorToolbar mDecorToolbar;
   ActionMode mDeferredDestroyActionMode;
   ActionMode.Callback mDeferredModeDestroyCallback;
   private Dialog mDialog;
   private boolean mDisplayHomeAsUpSet;
   private boolean mHasEmbeddedTabs;
   boolean mHiddenByApp;
   boolean mHiddenBySystem;
   final ViewPropertyAnimatorListener mHideListener = new ViewPropertyAnimatorListenerAdapter() {
      public void onAnimationEnd(View var1) {
         if (WindowDecorActionBar.this.mContentAnimations && WindowDecorActionBar.this.mContentView != null) {
            WindowDecorActionBar.this.mContentView.setTranslationY(0.0F);
            WindowDecorActionBar.this.mContainerView.setTranslationY(0.0F);
         }

         WindowDecorActionBar.this.mContainerView.setVisibility(8);
         WindowDecorActionBar.this.mContainerView.setTransitioning(false);
         WindowDecorActionBar.this.mCurrentShowAnim = null;
         WindowDecorActionBar.this.completeDeferredDestroyActionMode();
         if (WindowDecorActionBar.this.mOverlayLayout != null) {
            ViewCompat.requestApplyInsets(WindowDecorActionBar.this.mOverlayLayout);
         }

      }
   };
   boolean mHideOnContentScroll;
   private boolean mLastMenuVisibility;
   private ArrayList mMenuVisibilityListeners = new ArrayList();
   private boolean mNowShowing = true;
   ActionBarOverlayLayout mOverlayLayout;
   private int mSavedTabPosition = -1;
   private WindowDecorActionBar.TabImpl mSelectedTab;
   private boolean mShowHideAnimationEnabled;
   final ViewPropertyAnimatorListener mShowListener = new ViewPropertyAnimatorListenerAdapter() {
      public void onAnimationEnd(View var1) {
         WindowDecorActionBar.this.mCurrentShowAnim = null;
         WindowDecorActionBar.this.mContainerView.requestLayout();
      }
   };
   private boolean mShowingForMode;
   ScrollingTabContainerView mTabScrollView;
   private ArrayList mTabs = new ArrayList();
   private Context mThemedContext;
   final ViewPropertyAnimatorUpdateListener mUpdateListener = new ViewPropertyAnimatorUpdateListener() {
      public void onAnimationUpdate(View var1) {
         ((View)WindowDecorActionBar.this.mContainerView.getParent()).invalidate();
      }
   };

   public WindowDecorActionBar(Activity var1, boolean var2) {
      this.mActivity = var1;
      View var3 = var1.getWindow().getDecorView();
      this.init(var3);
      if (!var2) {
         this.mContentView = var3.findViewById(16908290);
      }

   }

   public WindowDecorActionBar(Dialog var1) {
      this.mDialog = var1;
      this.init(var1.getWindow().getDecorView());
   }

   public WindowDecorActionBar(View var1) {
      this.init(var1);
   }

   static boolean checkShowingFlags(boolean var0, boolean var1, boolean var2) {
      if (var2) {
         return true;
      } else {
         return !var0 && !var1;
      }
   }

   private void cleanupTabs() {
      if (this.mSelectedTab != null) {
         this.selectTab((ActionBar.Tab)null);
      }

      this.mTabs.clear();
      ScrollingTabContainerView var1 = this.mTabScrollView;
      if (var1 != null) {
         var1.removeAllTabs();
      }

      this.mSavedTabPosition = -1;
   }

   private void configureTab(ActionBar.Tab var1, int var2) {
      WindowDecorActionBar.TabImpl var4 = (WindowDecorActionBar.TabImpl)var1;
      if (var4.getCallback() == null) {
         throw new IllegalStateException("Action Bar Tab must have a Callback");
      } else {
         var4.setPosition(var2);
         this.mTabs.add(var2, var4);
         int var3 = this.mTabs.size();
         ++var2;

         while(var2 < var3) {
            ((WindowDecorActionBar.TabImpl)this.mTabs.get(var2)).setPosition(var2);
            ++var2;
         }

      }
   }

   private void ensureTabsExist() {
      if (this.mTabScrollView == null) {
         ScrollingTabContainerView var1 = new ScrollingTabContainerView(this.mContext);
         if (this.mHasEmbeddedTabs) {
            var1.setVisibility(0);
            this.mDecorToolbar.setEmbeddedTabView(var1);
         } else {
            if (this.getNavigationMode() == 2) {
               var1.setVisibility(0);
               ActionBarOverlayLayout var2 = this.mOverlayLayout;
               if (var2 != null) {
                  ViewCompat.requestApplyInsets(var2);
               }
            } else {
               var1.setVisibility(8);
            }

            this.mContainerView.setTabContainer(var1);
         }

         this.mTabScrollView = var1;
      }
   }

   private DecorToolbar getDecorToolbar(View var1) {
      if (var1 instanceof DecorToolbar) {
         return (DecorToolbar)var1;
      } else if (var1 instanceof Toolbar) {
         return ((Toolbar)var1).getWrapper();
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Can't make a decor toolbar out of ");
         String var3;
         if (var1 != null) {
            var3 = var1.getClass().getSimpleName();
         } else {
            var3 = "null";
         }

         var2.append(var3);
         throw new IllegalStateException(var2.toString());
      }
   }

   private void hideForActionMode() {
      if (this.mShowingForMode) {
         this.mShowingForMode = false;
         ActionBarOverlayLayout var1 = this.mOverlayLayout;
         if (var1 != null) {
            var1.setShowingForActionMode(false);
         }

         this.updateVisibility(false);
      }

   }

   private void init(View var1) {
      ActionBarOverlayLayout var4 = (ActionBarOverlayLayout)var1.findViewById(id.decor_content_parent);
      this.mOverlayLayout = var4;
      if (var4 != null) {
         var4.setActionBarVisibilityCallback(this);
      }

      this.mDecorToolbar = this.getDecorToolbar(var1.findViewById(id.action_bar));
      this.mContextView = (ActionBarContextView)var1.findViewById(id.action_context_bar);
      ActionBarContainer var5 = (ActionBarContainer)var1.findViewById(id.action_bar_container);
      this.mContainerView = var5;
      DecorToolbar var10 = this.mDecorToolbar;
      if (var10 != null && this.mContextView != null && var5 != null) {
         this.mContext = var10.getContext();
         boolean var2;
         if ((this.mDecorToolbar.getDisplayOptions() & 4) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2) {
            this.mDisplayHomeAsUpSet = true;
         }

         ActionBarPolicy var7 = ActionBarPolicy.get(this.mContext);
         boolean var3;
         if (!var7.enableHomeButtonByDefault() && !var2) {
            var3 = false;
         } else {
            var3 = true;
         }

         this.setHomeButtonEnabled(var3);
         this.setHasEmbeddedTabs(var7.hasEmbeddedTabs());
         TypedArray var8 = this.mContext.obtainStyledAttributes((AttributeSet)null, styleable.ActionBar, attr.actionBarStyle, 0);
         if (var8.getBoolean(styleable.ActionBar_hideOnContentScroll, false)) {
            this.setHideOnContentScrollEnabled(true);
         }

         int var9 = var8.getDimensionPixelSize(styleable.ActionBar_elevation, 0);
         if (var9 != 0) {
            this.setElevation((float)var9);
         }

         var8.recycle();
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append(this.getClass().getSimpleName());
         var6.append(" can only be used with a compatible window decor layout");
         throw new IllegalStateException(var6.toString());
      }
   }

   private void setHasEmbeddedTabs(boolean var1) {
      this.mHasEmbeddedTabs = var1;
      if (!var1) {
         this.mDecorToolbar.setEmbeddedTabView((ScrollingTabContainerView)null);
         this.mContainerView.setTabContainer(this.mTabScrollView);
      } else {
         this.mContainerView.setTabContainer((ScrollingTabContainerView)null);
         this.mDecorToolbar.setEmbeddedTabView(this.mTabScrollView);
      }

      int var2 = this.getNavigationMode();
      boolean var3 = true;
      boolean var5;
      if (var2 == 2) {
         var5 = true;
      } else {
         var5 = false;
      }

      ScrollingTabContainerView var4 = this.mTabScrollView;
      ActionBarOverlayLayout var6;
      if (var4 != null) {
         if (var5) {
            var4.setVisibility(0);
            var6 = this.mOverlayLayout;
            if (var6 != null) {
               ViewCompat.requestApplyInsets(var6);
            }
         } else {
            var4.setVisibility(8);
         }
      }

      DecorToolbar var7 = this.mDecorToolbar;
      if (!this.mHasEmbeddedTabs && var5) {
         var1 = true;
      } else {
         var1 = false;
      }

      var7.setCollapsible(var1);
      var6 = this.mOverlayLayout;
      if (!this.mHasEmbeddedTabs && var5) {
         var1 = var3;
      } else {
         var1 = false;
      }

      var6.setHasNonEmbeddedTabs(var1);
   }

   private boolean shouldAnimateContextView() {
      return ViewCompat.isLaidOut(this.mContainerView);
   }

   private void showForActionMode() {
      if (!this.mShowingForMode) {
         this.mShowingForMode = true;
         ActionBarOverlayLayout var1 = this.mOverlayLayout;
         if (var1 != null) {
            var1.setShowingForActionMode(true);
         }

         this.updateVisibility(false);
      }

   }

   private void updateVisibility(boolean var1) {
      if (checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
         if (!this.mNowShowing) {
            this.mNowShowing = true;
            this.doShow(var1);
            return;
         }
      } else if (this.mNowShowing) {
         this.mNowShowing = false;
         this.doHide(var1);
      }

   }

   public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener var1) {
      this.mMenuVisibilityListeners.add(var1);
   }

   public void addTab(ActionBar.Tab var1) {
      this.addTab(var1, this.mTabs.isEmpty());
   }

   public void addTab(ActionBar.Tab var1, int var2) {
      this.addTab(var1, var2, this.mTabs.isEmpty());
   }

   public void addTab(ActionBar.Tab var1, int var2, boolean var3) {
      this.ensureTabsExist();
      this.mTabScrollView.addTab(var1, var2, var3);
      this.configureTab(var1, var2);
      if (var3) {
         this.selectTab(var1);
      }

   }

   public void addTab(ActionBar.Tab var1, boolean var2) {
      this.ensureTabsExist();
      this.mTabScrollView.addTab(var1, var2);
      this.configureTab(var1, this.mTabs.size());
      if (var2) {
         this.selectTab(var1);
      }

   }

   public void animateToMode(boolean var1) {
      if (var1) {
         this.showForActionMode();
      } else {
         this.hideForActionMode();
      }

      if (this.shouldAnimateContextView()) {
         ViewPropertyAnimatorCompat var2;
         ViewPropertyAnimatorCompat var3;
         if (var1) {
            var2 = this.mDecorToolbar.setupAnimatorToVisibility(4, 100L);
            var3 = this.mContextView.setupAnimatorToVisibility(0, 200L);
         } else {
            var3 = this.mDecorToolbar.setupAnimatorToVisibility(0, 200L);
            var2 = this.mContextView.setupAnimatorToVisibility(8, 100L);
         }

         ViewPropertyAnimatorCompatSet var4 = new ViewPropertyAnimatorCompatSet();
         var4.playSequentially(var2, var3);
         var4.start();
      } else if (var1) {
         this.mDecorToolbar.setVisibility(4);
         this.mContextView.setVisibility(0);
      } else {
         this.mDecorToolbar.setVisibility(0);
         this.mContextView.setVisibility(8);
      }
   }

   public boolean collapseActionView() {
      DecorToolbar var1 = this.mDecorToolbar;
      if (var1 != null && var1.hasExpandedActionView()) {
         this.mDecorToolbar.collapseActionView();
         return true;
      } else {
         return false;
      }
   }

   void completeDeferredDestroyActionMode() {
      ActionMode.Callback var1 = this.mDeferredModeDestroyCallback;
      if (var1 != null) {
         var1.onDestroyActionMode(this.mDeferredDestroyActionMode);
         this.mDeferredDestroyActionMode = null;
         this.mDeferredModeDestroyCallback = null;
      }

   }

   public void dispatchMenuVisibilityChanged(boolean var1) {
      if (var1 != this.mLastMenuVisibility) {
         this.mLastMenuVisibility = var1;
         int var3 = this.mMenuVisibilityListeners.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            ((ActionBar.OnMenuVisibilityListener)this.mMenuVisibilityListeners.get(var2)).onMenuVisibilityChanged(var1);
         }

      }
   }

   public void doHide(boolean var1) {
      ViewPropertyAnimatorCompatSet var4 = this.mCurrentShowAnim;
      if (var4 != null) {
         var4.cancel();
      }

      if (this.mCurWindowVisibility != 0 || !this.mShowHideAnimationEnabled && !var1) {
         this.mHideListener.onAnimationEnd((View)null);
      } else {
         this.mContainerView.setAlpha(1.0F);
         this.mContainerView.setTransitioning(true);
         var4 = new ViewPropertyAnimatorCompatSet();
         float var3 = (float)(-this.mContainerView.getHeight());
         float var2 = var3;
         if (var1) {
            int[] var5 = new int[]{0, 0};
            this.mContainerView.getLocationInWindow(var5);
            var2 = var3 - (float)var5[1];
         }

         ViewPropertyAnimatorCompat var6 = ViewCompat.animate(this.mContainerView).translationY(var2);
         var6.setUpdateListener(this.mUpdateListener);
         var4.play(var6);
         if (this.mContentAnimations) {
            View var7 = this.mContentView;
            if (var7 != null) {
               var4.play(ViewCompat.animate(var7).translationY(var2));
            }
         }

         var4.setInterpolator(sHideInterpolator);
         var4.setDuration(250L);
         var4.setListener(this.mHideListener);
         this.mCurrentShowAnim = var4;
         var4.start();
      }
   }

   public void doShow(boolean var1) {
      ViewPropertyAnimatorCompatSet var4 = this.mCurrentShowAnim;
      if (var4 != null) {
         var4.cancel();
      }

      this.mContainerView.setVisibility(0);
      if (this.mCurWindowVisibility == 0 && (this.mShowHideAnimationEnabled || var1)) {
         this.mContainerView.setTranslationY(0.0F);
         float var3 = (float)(-this.mContainerView.getHeight());
         float var2 = var3;
         if (var1) {
            int[] var7 = new int[]{0, 0};
            this.mContainerView.getLocationInWindow(var7);
            var2 = var3 - (float)var7[1];
         }

         this.mContainerView.setTranslationY(var2);
         var4 = new ViewPropertyAnimatorCompatSet();
         ViewPropertyAnimatorCompat var5 = ViewCompat.animate(this.mContainerView).translationY(0.0F);
         var5.setUpdateListener(this.mUpdateListener);
         var4.play(var5);
         if (this.mContentAnimations) {
            View var9 = this.mContentView;
            if (var9 != null) {
               var9.setTranslationY(var2);
               var4.play(ViewCompat.animate(this.mContentView).translationY(0.0F));
            }
         }

         var4.setInterpolator(sShowInterpolator);
         var4.setDuration(250L);
         var4.setListener(this.mShowListener);
         this.mCurrentShowAnim = var4;
         var4.start();
      } else {
         this.mContainerView.setAlpha(1.0F);
         this.mContainerView.setTranslationY(0.0F);
         if (this.mContentAnimations) {
            View var6 = this.mContentView;
            if (var6 != null) {
               var6.setTranslationY(0.0F);
            }
         }

         this.mShowListener.onAnimationEnd((View)null);
      }

      ActionBarOverlayLayout var8 = this.mOverlayLayout;
      if (var8 != null) {
         ViewCompat.requestApplyInsets(var8);
      }

   }

   public void enableContentAnimations(boolean var1) {
      this.mContentAnimations = var1;
   }

   public View getCustomView() {
      return this.mDecorToolbar.getCustomView();
   }

   public int getDisplayOptions() {
      return this.mDecorToolbar.getDisplayOptions();
   }

   public float getElevation() {
      return ViewCompat.getElevation(this.mContainerView);
   }

   public int getHeight() {
      return this.mContainerView.getHeight();
   }

   public int getHideOffset() {
      return this.mOverlayLayout.getActionBarHideOffset();
   }

   public int getNavigationItemCount() {
      int var1 = this.mDecorToolbar.getNavigationMode();
      if (var1 != 1) {
         return var1 != 2 ? 0 : this.mTabs.size();
      } else {
         return this.mDecorToolbar.getDropdownItemCount();
      }
   }

   public int getNavigationMode() {
      return this.mDecorToolbar.getNavigationMode();
   }

   public int getSelectedNavigationIndex() {
      int var2 = this.mDecorToolbar.getNavigationMode();
      if (var2 != 1) {
         int var1 = -1;
         if (var2 != 2) {
            return -1;
         } else {
            WindowDecorActionBar.TabImpl var3 = this.mSelectedTab;
            if (var3 != null) {
               var1 = var3.getPosition();
            }

            return var1;
         }
      } else {
         return this.mDecorToolbar.getDropdownSelectedPosition();
      }
   }

   public ActionBar.Tab getSelectedTab() {
      return this.mSelectedTab;
   }

   public CharSequence getSubtitle() {
      return this.mDecorToolbar.getSubtitle();
   }

   public ActionBar.Tab getTabAt(int var1) {
      return (ActionBar.Tab)this.mTabs.get(var1);
   }

   public int getTabCount() {
      return this.mTabs.size();
   }

   public Context getThemedContext() {
      if (this.mThemedContext == null) {
         TypedValue var2 = new TypedValue();
         this.mContext.getTheme().resolveAttribute(attr.actionBarWidgetTheme, var2, true);
         int var1 = var2.resourceId;
         if (var1 != 0) {
            this.mThemedContext = new ContextThemeWrapper(this.mContext, var1);
         } else {
            this.mThemedContext = this.mContext;
         }
      }

      return this.mThemedContext;
   }

   public CharSequence getTitle() {
      return this.mDecorToolbar.getTitle();
   }

   public boolean hasIcon() {
      return this.mDecorToolbar.hasIcon();
   }

   public boolean hasLogo() {
      return this.mDecorToolbar.hasLogo();
   }

   public void hide() {
      if (!this.mHiddenByApp) {
         this.mHiddenByApp = true;
         this.updateVisibility(false);
      }

   }

   public void hideForSystem() {
      if (!this.mHiddenBySystem) {
         this.mHiddenBySystem = true;
         this.updateVisibility(true);
      }

   }

   public boolean isHideOnContentScrollEnabled() {
      return this.mOverlayLayout.isHideOnContentScrollEnabled();
   }

   public boolean isShowing() {
      int var1 = this.getHeight();
      return this.mNowShowing && (var1 == 0 || this.getHideOffset() < var1);
   }

   public boolean isTitleTruncated() {
      DecorToolbar var1 = this.mDecorToolbar;
      return var1 != null && var1.isTitleTruncated();
   }

   public ActionBar.Tab newTab() {
      return new WindowDecorActionBar.TabImpl();
   }

   public void onConfigurationChanged(Configuration var1) {
      this.setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
   }

   public void onContentScrollStarted() {
      ViewPropertyAnimatorCompatSet var1 = this.mCurrentShowAnim;
      if (var1 != null) {
         var1.cancel();
         this.mCurrentShowAnim = null;
      }

   }

   public void onContentScrollStopped() {
   }

   public boolean onKeyShortcut(int var1, KeyEvent var2) {
      WindowDecorActionBar.ActionModeImpl var5 = this.mActionMode;
      if (var5 == null) {
         return false;
      } else {
         Menu var6 = var5.getMenu();
         if (var6 != null) {
            int var3;
            if (var2 != null) {
               var3 = var2.getDeviceId();
            } else {
               var3 = -1;
            }

            var3 = KeyCharacterMap.load(var3).getKeyboardType();
            boolean var4 = true;
            if (var3 == 1) {
               var4 = false;
            }

            var6.setQwertyMode(var4);
            return var6.performShortcut(var1, var2, 0);
         } else {
            return false;
         }
      }
   }

   public void onWindowVisibilityChanged(int var1) {
      this.mCurWindowVisibility = var1;
   }

   public void removeAllTabs() {
      this.cleanupTabs();
   }

   public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener var1) {
      this.mMenuVisibilityListeners.remove(var1);
   }

   public void removeTab(ActionBar.Tab var1) {
      this.removeTabAt(var1.getPosition());
   }

   public void removeTabAt(int var1) {
      if (this.mTabScrollView != null) {
         WindowDecorActionBar.TabImpl var5 = this.mSelectedTab;
         int var2;
         if (var5 != null) {
            var2 = var5.getPosition();
         } else {
            var2 = this.mSavedTabPosition;
         }

         this.mTabScrollView.removeTabAt(var1);
         var5 = (WindowDecorActionBar.TabImpl)this.mTabs.remove(var1);
         if (var5 != null) {
            var5.setPosition(-1);
         }

         int var4 = this.mTabs.size();

         for(int var3 = var1; var3 < var4; ++var3) {
            ((WindowDecorActionBar.TabImpl)this.mTabs.get(var3)).setPosition(var3);
         }

         if (var2 == var1) {
            if (this.mTabs.isEmpty()) {
               var5 = null;
            } else {
               var5 = (WindowDecorActionBar.TabImpl)this.mTabs.get(Math.max(0, var1 - 1));
            }

            this.selectTab(var5);
         }

      }
   }

   public boolean requestFocus() {
      ViewGroup var1 = this.mDecorToolbar.getViewGroup();
      if (var1 != null && !var1.hasFocus()) {
         var1.requestFocus();
         return true;
      } else {
         return false;
      }
   }

   public void selectTab(ActionBar.Tab var1) {
      int var3 = this.getNavigationMode();
      int var2 = -1;
      if (var3 != 2) {
         if (var1 != null) {
            var2 = var1.getPosition();
         }

         this.mSavedTabPosition = var2;
      } else {
         FragmentTransaction var4;
         if (this.mActivity instanceof FragmentActivity && !this.mDecorToolbar.getViewGroup().isInEditMode()) {
            var4 = ((FragmentActivity)this.mActivity).getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
         } else {
            var4 = null;
         }

         WindowDecorActionBar.TabImpl var5 = this.mSelectedTab;
         if (var5 == var1) {
            if (var5 != null) {
               var5.getCallback().onTabReselected(this.mSelectedTab, var4);
               this.mTabScrollView.animateToTab(var1.getPosition());
            }
         } else {
            ScrollingTabContainerView var7 = this.mTabScrollView;
            if (var1 != null) {
               var2 = var1.getPosition();
            }

            var7.setTabSelected(var2);
            var5 = this.mSelectedTab;
            if (var5 != null) {
               var5.getCallback().onTabUnselected(this.mSelectedTab, var4);
            }

            WindowDecorActionBar.TabImpl var6 = (WindowDecorActionBar.TabImpl)var1;
            this.mSelectedTab = var6;
            if (var6 != null) {
               var6.getCallback().onTabSelected(this.mSelectedTab, var4);
            }
         }

         if (var4 != null && !var4.isEmpty()) {
            var4.commit();
         }

      }
   }

   public void setBackgroundDrawable(Drawable var1) {
      this.mContainerView.setPrimaryBackground(var1);
   }

   public void setCustomView(int var1) {
      this.setCustomView(LayoutInflater.from(this.getThemedContext()).inflate(var1, this.mDecorToolbar.getViewGroup(), false));
   }

   public void setCustomView(View var1) {
      this.mDecorToolbar.setCustomView(var1);
   }

   public void setCustomView(View var1, ActionBar.LayoutParams var2) {
      var1.setLayoutParams(var2);
      this.mDecorToolbar.setCustomView(var1);
   }

   public void setDefaultDisplayHomeAsUpEnabled(boolean var1) {
      if (!this.mDisplayHomeAsUpSet) {
         this.setDisplayHomeAsUpEnabled(var1);
      }

   }

   public void setDisplayHomeAsUpEnabled(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 4;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 4);
   }

   public void setDisplayOptions(int var1) {
      if ((var1 & 4) != 0) {
         this.mDisplayHomeAsUpSet = true;
      }

      this.mDecorToolbar.setDisplayOptions(var1);
   }

   public void setDisplayOptions(int var1, int var2) {
      int var3 = this.mDecorToolbar.getDisplayOptions();
      if ((var2 & 4) != 0) {
         this.mDisplayHomeAsUpSet = true;
      }

      this.mDecorToolbar.setDisplayOptions(var1 & var2 | var2 & var3);
   }

   public void setDisplayShowCustomEnabled(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 16;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 16);
   }

   public void setDisplayShowHomeEnabled(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 2;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 2);
   }

   public void setDisplayShowTitleEnabled(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 8;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 8);
   }

   public void setDisplayUseLogoEnabled(boolean var1) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public void setElevation(float var1) {
      ViewCompat.setElevation(this.mContainerView, var1);
   }

   public void setHideOffset(int var1) {
      if (var1 != 0 && !this.mOverlayLayout.isInOverlayMode()) {
         throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to set a non-zero hide offset");
      } else {
         this.mOverlayLayout.setActionBarHideOffset(var1);
      }
   }

   public void setHideOnContentScrollEnabled(boolean var1) {
      if (var1 && !this.mOverlayLayout.isInOverlayMode()) {
         throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
      } else {
         this.mHideOnContentScroll = var1;
         this.mOverlayLayout.setHideOnContentScrollEnabled(var1);
      }
   }

   public void setHomeActionContentDescription(int var1) {
      this.mDecorToolbar.setNavigationContentDescription(var1);
   }

   public void setHomeActionContentDescription(CharSequence var1) {
      this.mDecorToolbar.setNavigationContentDescription(var1);
   }

   public void setHomeAsUpIndicator(int var1) {
      this.mDecorToolbar.setNavigationIcon(var1);
   }

   public void setHomeAsUpIndicator(Drawable var1) {
      this.mDecorToolbar.setNavigationIcon(var1);
   }

   public void setHomeButtonEnabled(boolean var1) {
      this.mDecorToolbar.setHomeButtonEnabled(var1);
   }

   public void setIcon(int var1) {
      this.mDecorToolbar.setIcon(var1);
   }

   public void setIcon(Drawable var1) {
      this.mDecorToolbar.setIcon(var1);
   }

   public void setListNavigationCallbacks(SpinnerAdapter var1, ActionBar.OnNavigationListener var2) {
      this.mDecorToolbar.setDropdownParams(var1, new NavItemSelectedListener(var2));
   }

   public void setLogo(int var1) {
      this.mDecorToolbar.setLogo(var1);
   }

   public void setLogo(Drawable var1) {
      this.mDecorToolbar.setLogo(var1);
   }

   public void setNavigationMode(int var1) {
      int var2 = this.mDecorToolbar.getNavigationMode();
      if (var2 == 2) {
         this.mSavedTabPosition = this.getSelectedNavigationIndex();
         this.selectTab((ActionBar.Tab)null);
         this.mTabScrollView.setVisibility(8);
      }

      ActionBarOverlayLayout var5;
      if (var2 != var1 && !this.mHasEmbeddedTabs) {
         var5 = this.mOverlayLayout;
         if (var5 != null) {
            ViewCompat.requestApplyInsets(var5);
         }
      }

      this.mDecorToolbar.setNavigationMode(var1);
      boolean var4 = false;
      if (var1 == 2) {
         this.ensureTabsExist();
         this.mTabScrollView.setVisibility(0);
         var2 = this.mSavedTabPosition;
         if (var2 != -1) {
            this.setSelectedNavigationItem(var2);
            this.mSavedTabPosition = -1;
         }
      }

      DecorToolbar var6 = this.mDecorToolbar;
      boolean var3;
      if (var1 == 2 && !this.mHasEmbeddedTabs) {
         var3 = true;
      } else {
         var3 = false;
      }

      var6.setCollapsible(var3);
      var5 = this.mOverlayLayout;
      var3 = var4;
      if (var1 == 2) {
         var3 = var4;
         if (!this.mHasEmbeddedTabs) {
            var3 = true;
         }
      }

      var5.setHasNonEmbeddedTabs(var3);
   }

   public void setSelectedNavigationItem(int var1) {
      int var2 = this.mDecorToolbar.getNavigationMode();
      if (var2 != 1) {
         if (var2 == 2) {
            this.selectTab((ActionBar.Tab)this.mTabs.get(var1));
         } else {
            throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
         }
      } else {
         this.mDecorToolbar.setDropdownSelectedPosition(var1);
      }
   }

   public void setShowHideAnimationEnabled(boolean var1) {
      this.mShowHideAnimationEnabled = var1;
      if (!var1) {
         ViewPropertyAnimatorCompatSet var2 = this.mCurrentShowAnim;
         if (var2 != null) {
            var2.cancel();
         }
      }

   }

   public void setSplitBackgroundDrawable(Drawable var1) {
   }

   public void setStackedBackgroundDrawable(Drawable var1) {
      this.mContainerView.setStackedBackground(var1);
   }

   public void setSubtitle(int var1) {
      this.setSubtitle(this.mContext.getString(var1));
   }

   public void setSubtitle(CharSequence var1) {
      this.mDecorToolbar.setSubtitle(var1);
   }

   public void setTitle(int var1) {
      this.setTitle(this.mContext.getString(var1));
   }

   public void setTitle(CharSequence var1) {
      this.mDecorToolbar.setTitle(var1);
   }

   public void setWindowTitle(CharSequence var1) {
      this.mDecorToolbar.setWindowTitle(var1);
   }

   public void show() {
      if (this.mHiddenByApp) {
         this.mHiddenByApp = false;
         this.updateVisibility(false);
      }

   }

   public void showForSystem() {
      if (this.mHiddenBySystem) {
         this.mHiddenBySystem = false;
         this.updateVisibility(true);
      }

   }

   public ActionMode startActionMode(ActionMode.Callback var1) {
      WindowDecorActionBar.ActionModeImpl var2 = this.mActionMode;
      if (var2 != null) {
         var2.finish();
      }

      this.mOverlayLayout.setHideOnContentScrollEnabled(false);
      this.mContextView.killMode();
      WindowDecorActionBar.ActionModeImpl var3 = new WindowDecorActionBar.ActionModeImpl(this.mContextView.getContext(), var1);
      if (var3.dispatchOnCreate()) {
         this.mActionMode = var3;
         var3.invalidate();
         this.mContextView.initForMode(var3);
         this.animateToMode(true);
         this.mContextView.sendAccessibilityEvent(32);
         return var3;
      } else {
         return null;
      }
   }

   public class ActionModeImpl extends ActionMode implements MenuBuilder.Callback {
      private final Context mActionModeContext;
      private ActionMode.Callback mCallback;
      private WeakReference mCustomView;
      private final MenuBuilder mMenu;

      public ActionModeImpl(Context var2, ActionMode.Callback var3) {
         this.mActionModeContext = var2;
         this.mCallback = var3;
         MenuBuilder var4 = (new MenuBuilder(var2)).setDefaultShowAsAction(1);
         this.mMenu = var4;
         var4.setCallback(this);
      }

      public boolean dispatchOnCreate() {
         this.mMenu.stopDispatchingItemsChanged();

         boolean var1;
         try {
            var1 = this.mCallback.onCreateActionMode(this, this.mMenu);
         } finally {
            this.mMenu.startDispatchingItemsChanged();
         }

         return var1;
      }

      public void finish() {
         if (WindowDecorActionBar.this.mActionMode == this) {
            if (!WindowDecorActionBar.checkShowingFlags(WindowDecorActionBar.this.mHiddenByApp, WindowDecorActionBar.this.mHiddenBySystem, false)) {
               WindowDecorActionBar.this.mDeferredDestroyActionMode = this;
               WindowDecorActionBar.this.mDeferredModeDestroyCallback = this.mCallback;
            } else {
               this.mCallback.onDestroyActionMode(this);
            }

            this.mCallback = null;
            WindowDecorActionBar.this.animateToMode(false);
            WindowDecorActionBar.this.mContextView.closeMode();
            WindowDecorActionBar.this.mDecorToolbar.getViewGroup().sendAccessibilityEvent(32);
            WindowDecorActionBar.this.mOverlayLayout.setHideOnContentScrollEnabled(WindowDecorActionBar.this.mHideOnContentScroll);
            WindowDecorActionBar.this.mActionMode = null;
         }
      }

      public View getCustomView() {
         WeakReference var1 = this.mCustomView;
         return var1 != null ? (View)var1.get() : null;
      }

      public Menu getMenu() {
         return this.mMenu;
      }

      public MenuInflater getMenuInflater() {
         return new SupportMenuInflater(this.mActionModeContext);
      }

      public CharSequence getSubtitle() {
         return WindowDecorActionBar.this.mContextView.getSubtitle();
      }

      public CharSequence getTitle() {
         return WindowDecorActionBar.this.mContextView.getTitle();
      }

      public void invalidate() {
         if (WindowDecorActionBar.this.mActionMode == this) {
            this.mMenu.stopDispatchingItemsChanged();

            try {
               this.mCallback.onPrepareActionMode(this, this.mMenu);
            } finally {
               this.mMenu.startDispatchingItemsChanged();
            }

         }
      }

      public boolean isTitleOptional() {
         return WindowDecorActionBar.this.mContextView.isTitleOptional();
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
      }

      public void onCloseSubMenu(SubMenuBuilder var1) {
      }

      public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
         ActionMode.Callback var3 = this.mCallback;
         return var3 != null ? var3.onActionItemClicked(this, var2) : false;
      }

      public void onMenuModeChange(MenuBuilder var1) {
         if (this.mCallback != null) {
            this.invalidate();
            WindowDecorActionBar.this.mContextView.showOverflowMenu();
         }
      }

      public boolean onSubMenuSelected(SubMenuBuilder var1) {
         if (this.mCallback == null) {
            return false;
         } else if (!var1.hasVisibleItems()) {
            return true;
         } else {
            (new MenuPopupHelper(WindowDecorActionBar.this.getThemedContext(), var1)).show();
            return true;
         }
      }

      public void setCustomView(View var1) {
         WindowDecorActionBar.this.mContextView.setCustomView(var1);
         this.mCustomView = new WeakReference(var1);
      }

      public void setSubtitle(int var1) {
         this.setSubtitle(WindowDecorActionBar.this.mContext.getResources().getString(var1));
      }

      public void setSubtitle(CharSequence var1) {
         WindowDecorActionBar.this.mContextView.setSubtitle(var1);
      }

      public void setTitle(int var1) {
         this.setTitle(WindowDecorActionBar.this.mContext.getResources().getString(var1));
      }

      public void setTitle(CharSequence var1) {
         WindowDecorActionBar.this.mContextView.setTitle(var1);
      }

      public void setTitleOptionalHint(boolean var1) {
         super.setTitleOptionalHint(var1);
         WindowDecorActionBar.this.mContextView.setTitleOptional(var1);
      }
   }

   public class TabImpl extends ActionBar.Tab {
      private ActionBar.TabListener mCallback;
      private CharSequence mContentDesc;
      private View mCustomView;
      private Drawable mIcon;
      private int mPosition = -1;
      private Object mTag;
      private CharSequence mText;

      public ActionBar.TabListener getCallback() {
         return this.mCallback;
      }

      public CharSequence getContentDescription() {
         return this.mContentDesc;
      }

      public View getCustomView() {
         return this.mCustomView;
      }

      public Drawable getIcon() {
         return this.mIcon;
      }

      public int getPosition() {
         return this.mPosition;
      }

      public Object getTag() {
         return this.mTag;
      }

      public CharSequence getText() {
         return this.mText;
      }

      public void select() {
         WindowDecorActionBar.this.selectTab(this);
      }

      public ActionBar.Tab setContentDescription(int var1) {
         return this.setContentDescription(WindowDecorActionBar.this.mContext.getResources().getText(var1));
      }

      public ActionBar.Tab setContentDescription(CharSequence var1) {
         this.mContentDesc = var1;
         if (this.mPosition >= 0) {
            WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
         }

         return this;
      }

      public ActionBar.Tab setCustomView(int var1) {
         return this.setCustomView(LayoutInflater.from(WindowDecorActionBar.this.getThemedContext()).inflate(var1, (ViewGroup)null));
      }

      public ActionBar.Tab setCustomView(View var1) {
         this.mCustomView = var1;
         if (this.mPosition >= 0) {
            WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
         }

         return this;
      }

      public ActionBar.Tab setIcon(int var1) {
         return this.setIcon(AppCompatResources.getDrawable(WindowDecorActionBar.this.mContext, var1));
      }

      public ActionBar.Tab setIcon(Drawable var1) {
         this.mIcon = var1;
         if (this.mPosition >= 0) {
            WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
         }

         return this;
      }

      public void setPosition(int var1) {
         this.mPosition = var1;
      }

      public ActionBar.Tab setTabListener(ActionBar.TabListener var1) {
         this.mCallback = var1;
         return this;
      }

      public ActionBar.Tab setTag(Object var1) {
         this.mTag = var1;
         return this;
      }

      public ActionBar.Tab setText(int var1) {
         return this.setText(WindowDecorActionBar.this.mContext.getResources().getText(var1));
      }

      public ActionBar.Tab setText(CharSequence var1) {
         this.mText = var1;
         if (this.mPosition >= 0) {
            WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
         }

         return this;
      }
   }
}
