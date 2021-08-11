package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.view.ActionMode;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.SpinnerAdapter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class ActionBar {
   public static final int DISPLAY_HOME_AS_UP = 4;
   public static final int DISPLAY_SHOW_CUSTOM = 16;
   public static final int DISPLAY_SHOW_HOME = 2;
   public static final int DISPLAY_SHOW_TITLE = 8;
   public static final int DISPLAY_USE_LOGO = 1;
   @Deprecated
   public static final int NAVIGATION_MODE_LIST = 1;
   @Deprecated
   public static final int NAVIGATION_MODE_STANDARD = 0;
   @Deprecated
   public static final int NAVIGATION_MODE_TABS = 2;

   public abstract void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener var1);

   @Deprecated
   public abstract void addTab(ActionBar.Tab var1);

   @Deprecated
   public abstract void addTab(ActionBar.Tab var1, int var2);

   @Deprecated
   public abstract void addTab(ActionBar.Tab var1, int var2, boolean var3);

   @Deprecated
   public abstract void addTab(ActionBar.Tab var1, boolean var2);

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean closeOptionsMenu() {
      return false;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean collapseActionView() {
      return false;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void dispatchMenuVisibilityChanged(boolean var1) {
   }

   public abstract View getCustomView();

   public abstract int getDisplayOptions();

   public float getElevation() {
      return 0.0F;
   }

   public abstract int getHeight();

   public int getHideOffset() {
      return 0;
   }

   @Deprecated
   public abstract int getNavigationItemCount();

   @Deprecated
   public abstract int getNavigationMode();

   @Deprecated
   public abstract int getSelectedNavigationIndex();

   @Deprecated
   @Nullable
   public abstract ActionBar.Tab getSelectedTab();

   @Nullable
   public abstract CharSequence getSubtitle();

   @Deprecated
   public abstract ActionBar.Tab getTabAt(int var1);

   @Deprecated
   public abstract int getTabCount();

   public Context getThemedContext() {
      return null;
   }

   @Nullable
   public abstract CharSequence getTitle();

   public abstract void hide();

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean invalidateOptionsMenu() {
      return false;
   }

   public boolean isHideOnContentScrollEnabled() {
      return false;
   }

   public abstract boolean isShowing();

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean isTitleTruncated() {
      return false;
   }

   @Deprecated
   public abstract ActionBar.Tab newTab();

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void onConfigurationChanged(Configuration var1) {
   }

   void onDestroy() {
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean onKeyShortcut(int var1, KeyEvent var2) {
      return false;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean onMenuKeyEvent(KeyEvent var1) {
      return false;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean openOptionsMenu() {
      return false;
   }

   @Deprecated
   public abstract void removeAllTabs();

   public abstract void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener var1);

   @Deprecated
   public abstract void removeTab(ActionBar.Tab var1);

   @Deprecated
   public abstract void removeTabAt(int var1);

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   boolean requestFocus() {
      return false;
   }

   @Deprecated
   public abstract void selectTab(ActionBar.Tab var1);

   public abstract void setBackgroundDrawable(@Nullable Drawable var1);

   public abstract void setCustomView(int var1);

   public abstract void setCustomView(View var1);

   public abstract void setCustomView(View var1, ActionBar.LayoutParams var2);

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setDefaultDisplayHomeAsUpEnabled(boolean var1) {
   }

   public abstract void setDisplayHomeAsUpEnabled(boolean var1);

   public abstract void setDisplayOptions(int var1);

   public abstract void setDisplayOptions(int var1, int var2);

   public abstract void setDisplayShowCustomEnabled(boolean var1);

   public abstract void setDisplayShowHomeEnabled(boolean var1);

   public abstract void setDisplayShowTitleEnabled(boolean var1);

   public abstract void setDisplayUseLogoEnabled(boolean var1);

   public void setElevation(float var1) {
      if (var1 != 0.0F) {
         throw new UnsupportedOperationException("Setting a non-zero elevation is not supported in this action bar configuration.");
      }
   }

   public void setHideOffset(int var1) {
      if (var1 != 0) {
         throw new UnsupportedOperationException("Setting an explicit action bar hide offset is not supported in this action bar configuration.");
      }
   }

   public void setHideOnContentScrollEnabled(boolean var1) {
      if (var1) {
         throw new UnsupportedOperationException("Hide on content scroll is not supported in this action bar configuration.");
      }
   }

   public void setHomeActionContentDescription(@StringRes int var1) {
   }

   public void setHomeActionContentDescription(@Nullable CharSequence var1) {
   }

   public void setHomeAsUpIndicator(@DrawableRes int var1) {
   }

   public void setHomeAsUpIndicator(@Nullable Drawable var1) {
   }

   public void setHomeButtonEnabled(boolean var1) {
   }

   public abstract void setIcon(@DrawableRes int var1);

   public abstract void setIcon(Drawable var1);

   @Deprecated
   public abstract void setListNavigationCallbacks(SpinnerAdapter var1, ActionBar.OnNavigationListener var2);

   public abstract void setLogo(@DrawableRes int var1);

   public abstract void setLogo(Drawable var1);

   @Deprecated
   public abstract void setNavigationMode(int var1);

   @Deprecated
   public abstract void setSelectedNavigationItem(int var1);

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setShowHideAnimationEnabled(boolean var1) {
   }

   public void setSplitBackgroundDrawable(Drawable var1) {
   }

   public void setStackedBackgroundDrawable(Drawable var1) {
   }

   public abstract void setSubtitle(int var1);

   public abstract void setSubtitle(CharSequence var1);

   public abstract void setTitle(@StringRes int var1);

   public abstract void setTitle(CharSequence var1);

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setWindowTitle(CharSequence var1) {
   }

   public abstract void show();

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public ActionMode startActionMode(ActionMode.Callback var1) {
      return null;
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface DisplayOptions {
   }

   public static class LayoutParams extends MarginLayoutParams {
      public int gravity;

      public LayoutParams(int var1) {
         this(-2, -1, var1);
      }

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.gravity = 0;
         this.gravity = 8388627;
      }

      public LayoutParams(int var1, int var2, int var3) {
         super(var1, var2);
         this.gravity = 0;
         this.gravity = var3;
      }

      public LayoutParams(@NonNull Context var1, AttributeSet var2) {
         super(var1, var2);
         this.gravity = 0;
         TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.ActionBarLayout);
         this.gravity = var3.getInt(R$styleable.ActionBarLayout_android_layout_gravity, 0);
         var3.recycle();
      }

      public LayoutParams(ActionBar.LayoutParams var1) {
         super(var1);
         this.gravity = 0;
         this.gravity = var1.gravity;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
         this.gravity = 0;
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface NavigationMode {
   }

   public interface OnMenuVisibilityListener {
      void onMenuVisibilityChanged(boolean var1);
   }

   @Deprecated
   public interface OnNavigationListener {
      boolean onNavigationItemSelected(int var1, long var2);
   }

   @Deprecated
   public abstract static class Tab {
      public static final int INVALID_POSITION = -1;

      public abstract CharSequence getContentDescription();

      public abstract View getCustomView();

      public abstract Drawable getIcon();

      public abstract int getPosition();

      public abstract Object getTag();

      public abstract CharSequence getText();

      public abstract void select();

      public abstract ActionBar.Tab setContentDescription(@StringRes int var1);

      public abstract ActionBar.Tab setContentDescription(CharSequence var1);

      public abstract ActionBar.Tab setCustomView(int var1);

      public abstract ActionBar.Tab setCustomView(View var1);

      public abstract ActionBar.Tab setIcon(@DrawableRes int var1);

      public abstract ActionBar.Tab setIcon(Drawable var1);

      public abstract ActionBar.Tab setTabListener(ActionBar.TabListener var1);

      public abstract ActionBar.Tab setTag(Object var1);

      public abstract ActionBar.Tab setText(int var1);

      public abstract ActionBar.Tab setText(CharSequence var1);
   }

   @Deprecated
   public interface TabListener {
      void onTabReselected(ActionBar.Tab var1, FragmentTransaction var2);

      void onTabSelected(ActionBar.Tab var1, FragmentTransaction var2);

      void onTabUnselected(ActionBar.Tab var1, FragmentTransaction var2);
   }
}
