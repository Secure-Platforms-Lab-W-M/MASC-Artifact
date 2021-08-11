package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.design.R$style;
import android.support.design.R$styleable;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.appcompat.R$attr;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;

public class NavigationView extends ScrimInsetsFrameLayout {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
   private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
   NavigationView.OnNavigationItemSelectedListener mListener;
   private int mMaxWidth;
   private final NavigationMenu mMenu;
   private MenuInflater mMenuInflater;
   private final NavigationMenuPresenter mPresenter;

   public NavigationView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NavigationView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public NavigationView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mPresenter = new NavigationMenuPresenter();
      ThemeUtils.checkAppCompatTheme(var1);
      this.mMenu = new NavigationMenu(var1);
      TintTypedArray var6 = TintTypedArray.obtainStyledAttributes(var1, var2, R$styleable.NavigationView, var3, R$style.Widget_Design_NavigationView);
      ViewCompat.setBackground(this, var6.getDrawable(R$styleable.NavigationView_android_background));
      if (var6.hasValue(R$styleable.NavigationView_elevation)) {
         ViewCompat.setElevation(this, (float)var6.getDimensionPixelSize(R$styleable.NavigationView_elevation, 0));
      }

      ViewCompat.setFitsSystemWindows(this, var6.getBoolean(R$styleable.NavigationView_android_fitsSystemWindows, false));
      this.mMaxWidth = var6.getDimensionPixelSize(R$styleable.NavigationView_android_maxWidth, 0);
      ColorStateList var5;
      if (var6.hasValue(R$styleable.NavigationView_itemIconTint)) {
         var5 = var6.getColorStateList(R$styleable.NavigationView_itemIconTint);
      } else {
         var5 = this.createDefaultColorStateList(16842808);
      }

      boolean var9 = false;
      int var4 = 0;
      if (var6.hasValue(R$styleable.NavigationView_itemTextAppearance)) {
         var4 = var6.getResourceId(R$styleable.NavigationView_itemTextAppearance, 0);
         var9 = true;
      }

      ColorStateList var8 = null;
      if (var6.hasValue(R$styleable.NavigationView_itemTextColor)) {
         var8 = var6.getColorStateList(R$styleable.NavigationView_itemTextColor);
      }

      if (!var9 && var8 == null) {
         var8 = this.createDefaultColorStateList(16842806);
      }

      Drawable var7 = var6.getDrawable(R$styleable.NavigationView_itemBackground);
      this.mMenu.setCallback(new MenuBuilder.Callback() {
         public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
            return NavigationView.this.mListener != null && NavigationView.this.mListener.onNavigationItemSelected(var2);
         }

         public void onMenuModeChange(MenuBuilder var1) {
         }
      });
      this.mPresenter.setId(1);
      this.mPresenter.initForMenu(var1, this.mMenu);
      this.mPresenter.setItemIconTintList(var5);
      if (var9) {
         this.mPresenter.setItemTextAppearance(var4);
      }

      this.mPresenter.setItemTextColor(var8);
      this.mPresenter.setItemBackground(var7);
      this.mMenu.addMenuPresenter(this.mPresenter);
      this.addView((View)this.mPresenter.getMenuView(this));
      if (var6.hasValue(R$styleable.NavigationView_menu)) {
         this.inflateMenu(var6.getResourceId(R$styleable.NavigationView_menu, 0));
      }

      if (var6.hasValue(R$styleable.NavigationView_headerLayout)) {
         this.inflateHeaderView(var6.getResourceId(R$styleable.NavigationView_headerLayout, 0));
      }

      var6.recycle();
   }

   private ColorStateList createDefaultColorStateList(int var1) {
      TypedValue var5 = new TypedValue();
      if (!this.getContext().getTheme().resolveAttribute(var1, var5, true)) {
         return null;
      } else {
         ColorStateList var4 = AppCompatResources.getColorStateList(this.getContext(), var5.resourceId);
         if (!this.getContext().getTheme().resolveAttribute(R$attr.colorPrimary, var5, true)) {
            return null;
         } else {
            var1 = var5.data;
            int var2 = var4.getDefaultColor();
            int[] var8 = DISABLED_STATE_SET;
            int[] var6 = CHECKED_STATE_SET;
            int[] var7 = EMPTY_STATE_SET;
            int var3 = var4.getColorForState(DISABLED_STATE_SET, var2);
            return new ColorStateList(new int[][]{var8, var6, var7}, new int[]{var3, var1, var2});
         }
      }
   }

   private MenuInflater getMenuInflater() {
      if (this.mMenuInflater == null) {
         this.mMenuInflater = new SupportMenuInflater(this.getContext());
      }

      return this.mMenuInflater;
   }

   public void addHeaderView(@NonNull View var1) {
      this.mPresenter.addHeaderView(var1);
   }

   public int getHeaderCount() {
      return this.mPresenter.getHeaderCount();
   }

   public View getHeaderView(int var1) {
      return this.mPresenter.getHeaderView(var1);
   }

   @Nullable
   public Drawable getItemBackground() {
      return this.mPresenter.getItemBackground();
   }

   @Nullable
   public ColorStateList getItemIconTintList() {
      return this.mPresenter.getItemTintList();
   }

   @Nullable
   public ColorStateList getItemTextColor() {
      return this.mPresenter.getItemTextColor();
   }

   public Menu getMenu() {
      return this.mMenu;
   }

   public View inflateHeaderView(@LayoutRes int var1) {
      return this.mPresenter.inflateHeaderView(var1);
   }

   public void inflateMenu(int var1) {
      this.mPresenter.setUpdateSuspended(true);
      this.getMenuInflater().inflate(var1, this.mMenu);
      this.mPresenter.setUpdateSuspended(false);
      this.mPresenter.updateMenuView(false);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void onInsetsChanged(WindowInsetsCompat var1) {
      this.mPresenter.dispatchApplyWindowInsets(var1);
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      if (var3 != Integer.MIN_VALUE) {
         if (var3 != 0) {
            if (var3 != 1073741824) {
            }
         } else {
            var1 = MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
         }
      } else {
         var1 = MeasureSpec.makeMeasureSpec(Math.min(MeasureSpec.getSize(var1), this.mMaxWidth), 1073741824);
      }

      super.onMeasure(var1, var2);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof NavigationView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         NavigationView.SavedState var2 = (NavigationView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.mMenu.restorePresenterStates(var2.menuState);
      }
   }

   protected Parcelable onSaveInstanceState() {
      NavigationView.SavedState var1 = new NavigationView.SavedState(super.onSaveInstanceState());
      var1.menuState = new Bundle();
      this.mMenu.savePresenterStates(var1.menuState);
      return var1;
   }

   public void removeHeaderView(@NonNull View var1) {
      this.mPresenter.removeHeaderView(var1);
   }

   public void setCheckedItem(@IdRes int var1) {
      MenuItem var2 = this.mMenu.findItem(var1);
      if (var2 != null) {
         this.mPresenter.setCheckedItem((MenuItemImpl)var2);
      }
   }

   public void setItemBackground(@Nullable Drawable var1) {
      this.mPresenter.setItemBackground(var1);
   }

   public void setItemBackgroundResource(@DrawableRes int var1) {
      this.setItemBackground(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setItemIconTintList(@Nullable ColorStateList var1) {
      this.mPresenter.setItemIconTintList(var1);
   }

   public void setItemTextAppearance(@StyleRes int var1) {
      this.mPresenter.setItemTextAppearance(var1);
   }

   public void setItemTextColor(@Nullable ColorStateList var1) {
      this.mPresenter.setItemTextColor(var1);
   }

   public void setNavigationItemSelectedListener(@Nullable NavigationView.OnNavigationItemSelectedListener var1) {
      this.mListener = var1;
   }

   public interface OnNavigationItemSelectedListener {
      boolean onNavigationItemSelected(@NonNull MenuItem var1);
   }

   public static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public NavigationView.SavedState createFromParcel(Parcel var1) {
            return new NavigationView.SavedState(var1, (ClassLoader)null);
         }

         public NavigationView.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new NavigationView.SavedState(var1, var2);
         }

         public NavigationView.SavedState[] newArray(int var1) {
            return new NavigationView.SavedState[var1];
         }
      };
      public Bundle menuState;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.menuState = var1.readBundle(var2);
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(@NonNull Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeBundle(this.menuState);
      }
   }
}
