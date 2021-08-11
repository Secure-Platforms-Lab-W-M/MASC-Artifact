package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.R$color;
import android.support.design.R$dimen;
import android.support.design.R$style;
import android.support.design.R$styleable;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.internal.BottomNavigationPresenter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R$attr;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class BottomNavigationView extends FrameLayout {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
   private static final int MENU_PRESENTER_ID = 1;
   private final MenuBuilder mMenu;
   private MenuInflater mMenuInflater;
   private final BottomNavigationMenuView mMenuView;
   private final BottomNavigationPresenter mPresenter;
   private BottomNavigationView.OnNavigationItemReselectedListener mReselectedListener;
   private BottomNavigationView.OnNavigationItemSelectedListener mSelectedListener;

   public BottomNavigationView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public BottomNavigationView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public BottomNavigationView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mPresenter = new BottomNavigationPresenter();
      ThemeUtils.checkAppCompatTheme(var1);
      this.mMenu = new BottomNavigationMenu(var1);
      this.mMenuView = new BottomNavigationMenuView(var1);
      LayoutParams var4 = new LayoutParams(-2, -2);
      var4.gravity = 17;
      this.mMenuView.setLayoutParams(var4);
      this.mPresenter.setBottomNavigationMenuView(this.mMenuView);
      this.mPresenter.setId(1);
      this.mMenuView.setPresenter(this.mPresenter);
      this.mMenu.addMenuPresenter(this.mPresenter);
      this.mPresenter.initForMenu(this.getContext(), this.mMenu);
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var1, var2, R$styleable.BottomNavigationView, var3, R$style.Widget_Design_BottomNavigationView);
      if (var5.hasValue(R$styleable.BottomNavigationView_itemIconTint)) {
         this.mMenuView.setIconTintList(var5.getColorStateList(R$styleable.BottomNavigationView_itemIconTint));
      } else {
         this.mMenuView.setIconTintList(this.createDefaultColorStateList(16842808));
      }

      if (var5.hasValue(R$styleable.BottomNavigationView_itemTextColor)) {
         this.mMenuView.setItemTextColor(var5.getColorStateList(R$styleable.BottomNavigationView_itemTextColor));
      } else {
         this.mMenuView.setItemTextColor(this.createDefaultColorStateList(16842808));
      }

      if (var5.hasValue(R$styleable.BottomNavigationView_elevation)) {
         ViewCompat.setElevation(this, (float)var5.getDimensionPixelSize(R$styleable.BottomNavigationView_elevation, 0));
      }

      var3 = var5.getResourceId(R$styleable.BottomNavigationView_itemBackground, 0);
      this.mMenuView.setItemBackgroundRes(var3);
      if (var5.hasValue(R$styleable.BottomNavigationView_menu)) {
         this.inflateMenu(var5.getResourceId(R$styleable.BottomNavigationView_menu, 0));
      }

      var5.recycle();
      this.addView(this.mMenuView, var4);
      if (VERSION.SDK_INT < 21) {
         this.addCompatibilityTopDivider(var1);
      }

      this.mMenu.setCallback(new MenuBuilder.Callback() {
         public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
            if (BottomNavigationView.this.mReselectedListener != null && var2.getItemId() == BottomNavigationView.this.getSelectedItemId()) {
               BottomNavigationView.this.mReselectedListener.onNavigationItemReselected(var2);
               return true;
            } else {
               return BottomNavigationView.this.mSelectedListener != null && !BottomNavigationView.this.mSelectedListener.onNavigationItemSelected(var2);
            }
         }

         public void onMenuModeChange(MenuBuilder var1) {
         }
      });
   }

   private void addCompatibilityTopDivider(Context var1) {
      View var2 = new View(var1);
      var2.setBackgroundColor(ContextCompat.getColor(var1, R$color.design_bottom_navigation_shadow_color));
      var2.setLayoutParams(new LayoutParams(-1, this.getResources().getDimensionPixelSize(R$dimen.design_bottom_navigation_shadow_height)));
      this.addView(var2);
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

   @DrawableRes
   public int getItemBackgroundResource() {
      return this.mMenuView.getItemBackgroundRes();
   }

   @Nullable
   public ColorStateList getItemIconTintList() {
      return this.mMenuView.getIconTintList();
   }

   @Nullable
   public ColorStateList getItemTextColor() {
      return this.mMenuView.getItemTextColor();
   }

   public int getMaxItemCount() {
      return 5;
   }

   @NonNull
   public Menu getMenu() {
      return this.mMenu;
   }

   @IdRes
   public int getSelectedItemId() {
      return this.mMenuView.getSelectedItemId();
   }

   public void inflateMenu(int var1) {
      this.mPresenter.setUpdateSuspended(true);
      this.getMenuInflater().inflate(var1, this.mMenu);
      this.mPresenter.setUpdateSuspended(false);
      this.mPresenter.updateMenuView(true);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof BottomNavigationView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         BottomNavigationView.SavedState var2 = (BottomNavigationView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.mMenu.restorePresenterStates(var2.menuPresenterState);
      }
   }

   protected Parcelable onSaveInstanceState() {
      BottomNavigationView.SavedState var1 = new BottomNavigationView.SavedState(super.onSaveInstanceState());
      var1.menuPresenterState = new Bundle();
      this.mMenu.savePresenterStates(var1.menuPresenterState);
      return var1;
   }

   public void setItemBackgroundResource(@DrawableRes int var1) {
      this.mMenuView.setItemBackgroundRes(var1);
   }

   public void setItemIconTintList(@Nullable ColorStateList var1) {
      this.mMenuView.setIconTintList(var1);
   }

   public void setItemTextColor(@Nullable ColorStateList var1) {
      this.mMenuView.setItemTextColor(var1);
   }

   public void setOnNavigationItemReselectedListener(@Nullable BottomNavigationView.OnNavigationItemReselectedListener var1) {
      this.mReselectedListener = var1;
   }

   public void setOnNavigationItemSelectedListener(@Nullable BottomNavigationView.OnNavigationItemSelectedListener var1) {
      this.mSelectedListener = var1;
   }

   public void setSelectedItemId(@IdRes int var1) {
      MenuItem var2 = this.mMenu.findItem(var1);
      if (var2 != null) {
         if (!this.mMenu.performItemAction(var2, this.mPresenter, 0)) {
            var2.setChecked(true);
         }
      }
   }

   public interface OnNavigationItemReselectedListener {
      void onNavigationItemReselected(@NonNull MenuItem var1);
   }

   public interface OnNavigationItemSelectedListener {
      boolean onNavigationItemSelected(@NonNull MenuItem var1);
   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public BottomNavigationView.SavedState createFromParcel(Parcel var1) {
            return new BottomNavigationView.SavedState(var1, (ClassLoader)null);
         }

         public BottomNavigationView.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new BottomNavigationView.SavedState(var1, var2);
         }

         public BottomNavigationView.SavedState[] newArray(int var1) {
            return new BottomNavigationView.SavedState[var1];
         }
      };
      Bundle menuPresenterState;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.readFromParcel(var1, var2);
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      private void readFromParcel(Parcel var1, ClassLoader var2) {
         this.menuPresenterState = var1.readBundle(var2);
      }

      public void writeToParcel(@NonNull Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeBundle(this.menuPresenterState);
      }
   }
}
