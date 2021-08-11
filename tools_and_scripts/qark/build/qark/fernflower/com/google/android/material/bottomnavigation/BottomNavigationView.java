package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R.attr;
import com.google.android.material.R.color;
import com.google.android.material.R.dimen;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;

public class BottomNavigationView extends FrameLayout {
   private static final int DEF_STYLE_RES;
   private static final int MENU_PRESENTER_ID = 1;
   private ColorStateList itemRippleColor;
   private final MenuBuilder menu;
   private MenuInflater menuInflater;
   final BottomNavigationMenuView menuView;
   private final BottomNavigationPresenter presenter;
   private BottomNavigationView.OnNavigationItemReselectedListener reselectedListener;
   private BottomNavigationView.OnNavigationItemSelectedListener selectedListener;

   static {
      DEF_STYLE_RES = style.Widget_Design_BottomNavigationView;
   }

   public BottomNavigationView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public BottomNavigationView(Context var1, AttributeSet var2) {
      this(var1, var2, attr.bottomNavigationStyle);
   }

   public BottomNavigationView(Context var1, AttributeSet var2, int var3) {
      super(ThemeEnforcement.createThemedContext(var1, var2, var3, DEF_STYLE_RES), var2, var3);
      this.presenter = new BottomNavigationPresenter();
      var1 = this.getContext();
      this.menu = new BottomNavigationMenu(var1);
      this.menuView = new BottomNavigationMenuView(var1);
      LayoutParams var4 = new LayoutParams(-2, -2);
      var4.gravity = 17;
      this.menuView.setLayoutParams(var4);
      this.presenter.setBottomNavigationMenuView(this.menuView);
      this.presenter.setId(1);
      this.menuView.setPresenter(this.presenter);
      this.menu.addMenuPresenter(this.presenter);
      this.presenter.initForMenu(this.getContext(), this.menu);
      TintTypedArray var6 = ThemeEnforcement.obtainTintedStyledAttributes(var1, var2, styleable.BottomNavigationView, var3, style.Widget_Design_BottomNavigationView, styleable.BottomNavigationView_itemTextAppearanceInactive, styleable.BottomNavigationView_itemTextAppearanceActive);
      if (var6.hasValue(styleable.BottomNavigationView_itemIconTint)) {
         this.menuView.setIconTintList(var6.getColorStateList(styleable.BottomNavigationView_itemIconTint));
      } else {
         BottomNavigationMenuView var5 = this.menuView;
         var5.setIconTintList(var5.createDefaultColorStateList(16842808));
      }

      this.setItemIconSize(var6.getDimensionPixelSize(styleable.BottomNavigationView_itemIconSize, this.getResources().getDimensionPixelSize(dimen.design_bottom_navigation_icon_size)));
      if (var6.hasValue(styleable.BottomNavigationView_itemTextAppearanceInactive)) {
         this.setItemTextAppearanceInactive(var6.getResourceId(styleable.BottomNavigationView_itemTextAppearanceInactive, 0));
      }

      if (var6.hasValue(styleable.BottomNavigationView_itemTextAppearanceActive)) {
         this.setItemTextAppearanceActive(var6.getResourceId(styleable.BottomNavigationView_itemTextAppearanceActive, 0));
      }

      if (var6.hasValue(styleable.BottomNavigationView_itemTextColor)) {
         this.setItemTextColor(var6.getColorStateList(styleable.BottomNavigationView_itemTextColor));
      }

      if (this.getBackground() == null || this.getBackground() instanceof ColorDrawable) {
         ViewCompat.setBackground(this, this.createMaterialShapeDrawableBackground(var1));
      }

      if (var6.hasValue(styleable.BottomNavigationView_elevation)) {
         ViewCompat.setElevation(this, (float)var6.getDimensionPixelSize(styleable.BottomNavigationView_elevation, 0));
      }

      ColorStateList var7 = MaterialResources.getColorStateList(var1, var6, styleable.BottomNavigationView_backgroundTint);
      DrawableCompat.setTintList(this.getBackground().mutate(), var7);
      this.setLabelVisibilityMode(var6.getInteger(styleable.BottomNavigationView_labelVisibilityMode, -1));
      this.setItemHorizontalTranslationEnabled(var6.getBoolean(styleable.BottomNavigationView_itemHorizontalTranslationEnabled, true));
      var3 = var6.getResourceId(styleable.BottomNavigationView_itemBackground, 0);
      if (var3 != 0) {
         this.menuView.setItemBackgroundRes(var3);
      } else {
         this.setItemRippleColor(MaterialResources.getColorStateList(var1, var6, styleable.BottomNavigationView_itemRippleColor));
      }

      if (var6.hasValue(styleable.BottomNavigationView_menu)) {
         this.inflateMenu(var6.getResourceId(styleable.BottomNavigationView_menu, 0));
      }

      var6.recycle();
      this.addView(this.menuView, var4);
      if (VERSION.SDK_INT < 21) {
         this.addCompatibilityTopDivider(var1);
      }

      this.menu.setCallback(new MenuBuilder.Callback() {
         public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
            if (BottomNavigationView.this.reselectedListener != null && var2.getItemId() == BottomNavigationView.this.getSelectedItemId()) {
               BottomNavigationView.this.reselectedListener.onNavigationItemReselected(var2);
               return true;
            } else {
               return BottomNavigationView.this.selectedListener != null && !BottomNavigationView.this.selectedListener.onNavigationItemSelected(var2);
            }
         }

         public void onMenuModeChange(MenuBuilder var1) {
         }
      });
      this.applyWindowInsets();
   }

   private void addCompatibilityTopDivider(Context var1) {
      View var2 = new View(var1);
      var2.setBackgroundColor(ContextCompat.getColor(var1, color.design_bottom_navigation_shadow_color));
      var2.setLayoutParams(new LayoutParams(-1, this.getResources().getDimensionPixelSize(dimen.design_bottom_navigation_shadow_height)));
      this.addView(var2);
   }

   private void applyWindowInsets() {
      ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2, ViewUtils.RelativePadding var3) {
            var3.bottom += var2.getSystemWindowInsetBottom();
            var3.applyToView(var1);
            return var2;
         }
      });
   }

   private MaterialShapeDrawable createMaterialShapeDrawableBackground(Context var1) {
      MaterialShapeDrawable var2 = new MaterialShapeDrawable();
      Drawable var3 = this.getBackground();
      if (var3 instanceof ColorDrawable) {
         var2.setFillColor(ColorStateList.valueOf(((ColorDrawable)var3).getColor()));
      }

      var2.initializeElevationOverlay(var1);
      return var2;
   }

   private MenuInflater getMenuInflater() {
      if (this.menuInflater == null) {
         this.menuInflater = new SupportMenuInflater(this.getContext());
      }

      return this.menuInflater;
   }

   public BadgeDrawable getBadge(int var1) {
      return this.menuView.getBadge(var1);
   }

   public Drawable getItemBackground() {
      return this.menuView.getItemBackground();
   }

   @Deprecated
   public int getItemBackgroundResource() {
      return this.menuView.getItemBackgroundRes();
   }

   public int getItemIconSize() {
      return this.menuView.getItemIconSize();
   }

   public ColorStateList getItemIconTintList() {
      return this.menuView.getIconTintList();
   }

   public ColorStateList getItemRippleColor() {
      return this.itemRippleColor;
   }

   public int getItemTextAppearanceActive() {
      return this.menuView.getItemTextAppearanceActive();
   }

   public int getItemTextAppearanceInactive() {
      return this.menuView.getItemTextAppearanceInactive();
   }

   public ColorStateList getItemTextColor() {
      return this.menuView.getItemTextColor();
   }

   public int getLabelVisibilityMode() {
      return this.menuView.getLabelVisibilityMode();
   }

   public int getMaxItemCount() {
      return 5;
   }

   public Menu getMenu() {
      return this.menu;
   }

   public BadgeDrawable getOrCreateBadge(int var1) {
      return this.menuView.getOrCreateBadge(var1);
   }

   public int getSelectedItemId() {
      return this.menuView.getSelectedItemId();
   }

   public void inflateMenu(int var1) {
      this.presenter.setUpdateSuspended(true);
      this.getMenuInflater().inflate(var1, this.menu);
      this.presenter.setUpdateSuspended(false);
      this.presenter.updateMenuView(true);
   }

   public boolean isItemHorizontalTranslationEnabled() {
      return this.menuView.isItemHorizontalTranslationEnabled();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      MaterialShapeUtils.setParentAbsoluteElevation(this);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof BottomNavigationView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         BottomNavigationView.SavedState var2 = (BottomNavigationView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.menu.restorePresenterStates(var2.menuPresenterState);
      }
   }

   protected Parcelable onSaveInstanceState() {
      BottomNavigationView.SavedState var1 = new BottomNavigationView.SavedState(super.onSaveInstanceState());
      var1.menuPresenterState = new Bundle();
      this.menu.savePresenterStates(var1.menuPresenterState);
      return var1;
   }

   public void removeBadge(int var1) {
      this.menuView.removeBadge(var1);
   }

   public void setElevation(float var1) {
      super.setElevation(var1);
      MaterialShapeUtils.setElevation(this, var1);
   }

   public void setItemBackground(Drawable var1) {
      this.menuView.setItemBackground(var1);
      this.itemRippleColor = null;
   }

   public void setItemBackgroundResource(int var1) {
      this.menuView.setItemBackgroundRes(var1);
      this.itemRippleColor = null;
   }

   public void setItemHorizontalTranslationEnabled(boolean var1) {
      if (this.menuView.isItemHorizontalTranslationEnabled() != var1) {
         this.menuView.setItemHorizontalTranslationEnabled(var1);
         this.presenter.updateMenuView(false);
      }

   }

   public void setItemIconSize(int var1) {
      this.menuView.setItemIconSize(var1);
   }

   public void setItemIconSizeRes(int var1) {
      this.setItemIconSize(this.getResources().getDimensionPixelSize(var1));
   }

   public void setItemIconTintList(ColorStateList var1) {
      this.menuView.setIconTintList(var1);
   }

   public void setItemRippleColor(ColorStateList var1) {
      if (this.itemRippleColor == var1) {
         if (var1 == null && this.menuView.getItemBackground() != null) {
            this.menuView.setItemBackground((Drawable)null);
         }

      } else {
         this.itemRippleColor = var1;
         if (var1 == null) {
            this.menuView.setItemBackground((Drawable)null);
         } else {
            var1 = RippleUtils.convertToRippleDrawableColor(var1);
            if (VERSION.SDK_INT >= 21) {
               this.menuView.setItemBackground(new RippleDrawable(var1, (Drawable)null, (Drawable)null));
            } else {
               GradientDrawable var2 = new GradientDrawable();
               var2.setCornerRadius(1.0E-5F);
               Drawable var3 = DrawableCompat.wrap(var2);
               DrawableCompat.setTintList(var3, var1);
               this.menuView.setItemBackground(var3);
            }
         }
      }
   }

   public void setItemTextAppearanceActive(int var1) {
      this.menuView.setItemTextAppearanceActive(var1);
   }

   public void setItemTextAppearanceInactive(int var1) {
      this.menuView.setItemTextAppearanceInactive(var1);
   }

   public void setItemTextColor(ColorStateList var1) {
      this.menuView.setItemTextColor(var1);
   }

   public void setLabelVisibilityMode(int var1) {
      if (this.menuView.getLabelVisibilityMode() != var1) {
         this.menuView.setLabelVisibilityMode(var1);
         this.presenter.updateMenuView(false);
      }

   }

   public void setOnNavigationItemReselectedListener(BottomNavigationView.OnNavigationItemReselectedListener var1) {
      this.reselectedListener = var1;
   }

   public void setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener var1) {
      this.selectedListener = var1;
   }

   public void setSelectedItemId(int var1) {
      MenuItem var2 = this.menu.findItem(var1);
      if (var2 != null && !this.menu.performItemAction(var2, this.presenter, 0)) {
         var2.setChecked(true);
      }

   }

   public interface OnNavigationItemReselectedListener {
      void onNavigationItemReselected(MenuItem var1);
   }

   public interface OnNavigationItemSelectedListener {
      boolean onNavigationItemSelected(MenuItem var1);
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
         ClassLoader var3 = var2;
         if (var2 == null) {
            var3 = this.getClass().getClassLoader();
         }

         this.readFromParcel(var1, var3);
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      private void readFromParcel(Parcel var1, ClassLoader var2) {
         this.menuPresenterState = var1.readBundle(var2);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeBundle(this.menuPresenterState);
      }
   }
}
