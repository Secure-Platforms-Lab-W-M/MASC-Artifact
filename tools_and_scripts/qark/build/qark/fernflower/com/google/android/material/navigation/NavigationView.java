package com.google.android.material.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;

public class NavigationView extends ScrimInsetsFrameLayout {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
   private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
   NavigationView.OnNavigationItemSelectedListener listener;
   private final int maxWidth;
   private final NavigationMenu menu;
   private MenuInflater menuInflater;
   private OnGlobalLayoutListener onGlobalLayoutListener;
   private final NavigationMenuPresenter presenter;
   private final int[] tmpLocation;

   public NavigationView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NavigationView(Context var1, AttributeSet var2) {
      this(var1, var2, attr.navigationViewStyle);
   }

   public NavigationView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.presenter = new NavigationMenuPresenter();
      this.tmpLocation = new int[2];
      this.menu = new NavigationMenu(var1);
      TintTypedArray var9 = ThemeEnforcement.obtainTintedStyledAttributes(var1, var2, styleable.NavigationView, var3, style.Widget_Design_NavigationView);
      if (var9.hasValue(styleable.NavigationView_android_background)) {
         ViewCompat.setBackground(this, var9.getDrawable(styleable.NavigationView_android_background));
      }

      Drawable var10;
      if (this.getBackground() == null || this.getBackground() instanceof ColorDrawable) {
         var10 = this.getBackground();
         MaterialShapeDrawable var6 = new MaterialShapeDrawable();
         if (var10 instanceof ColorDrawable) {
            var6.setFillColor(ColorStateList.valueOf(((ColorDrawable)var10).getColor()));
         }

         var6.initializeElevationOverlay(var1);
         ViewCompat.setBackground(this, var6);
      }

      if (var9.hasValue(styleable.NavigationView_elevation)) {
         this.setElevation((float)var9.getDimensionPixelSize(styleable.NavigationView_elevation, 0));
      }

      this.setFitsSystemWindows(var9.getBoolean(styleable.NavigationView_android_fitsSystemWindows, false));
      this.maxWidth = var9.getDimensionPixelSize(styleable.NavigationView_android_maxWidth, 0);
      ColorStateList var13;
      if (var9.hasValue(styleable.NavigationView_itemIconTint)) {
         var13 = var9.getColorStateList(styleable.NavigationView_itemIconTint);
      } else {
         var13 = this.createDefaultColorStateList(16842808);
      }

      boolean var12 = false;
      int var4 = 0;
      if (var9.hasValue(styleable.NavigationView_itemTextAppearance)) {
         var4 = var9.getResourceId(styleable.NavigationView_itemTextAppearance, 0);
         var12 = true;
      }

      if (var9.hasValue(styleable.NavigationView_itemIconSize)) {
         this.setItemIconSize(var9.getDimensionPixelSize(styleable.NavigationView_itemIconSize, 0));
      }

      ColorStateList var11 = null;
      if (var9.hasValue(styleable.NavigationView_itemTextColor)) {
         var11 = var9.getColorStateList(styleable.NavigationView_itemTextColor);
      }

      ColorStateList var7 = var11;
      if (!var12) {
         var7 = var11;
         if (var11 == null) {
            var7 = this.createDefaultColorStateList(16842806);
         }
      }

      Drawable var8 = var9.getDrawable(styleable.NavigationView_itemBackground);
      var10 = var8;
      if (var8 == null) {
         var10 = var8;
         if (this.hasShapeAppearance(var9)) {
            var10 = this.createDefaultItemBackground(var9);
         }
      }

      int var5;
      if (var9.hasValue(styleable.NavigationView_itemHorizontalPadding)) {
         var5 = var9.getDimensionPixelSize(styleable.NavigationView_itemHorizontalPadding, 0);
         this.presenter.setItemHorizontalPadding(var5);
      }

      var5 = var9.getDimensionPixelSize(styleable.NavigationView_itemIconPadding, 0);
      this.setItemMaxLines(var9.getInt(styleable.NavigationView_itemMaxLines, 1));
      this.menu.setCallback(new MenuBuilder.Callback() {
         public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
            return NavigationView.this.listener != null && NavigationView.this.listener.onNavigationItemSelected(var2);
         }

         public void onMenuModeChange(MenuBuilder var1) {
         }
      });
      this.presenter.setId(1);
      this.presenter.initForMenu(var1, this.menu);
      this.presenter.setItemIconTintList(var13);
      this.presenter.setOverScrollMode(this.getOverScrollMode());
      if (var12) {
         this.presenter.setItemTextAppearance(var4);
      }

      this.presenter.setItemTextColor(var7);
      this.presenter.setItemBackground(var10);
      this.presenter.setItemIconPadding(var5);
      this.menu.addMenuPresenter(this.presenter);
      this.addView((View)this.presenter.getMenuView(this));
      if (var9.hasValue(styleable.NavigationView_menu)) {
         this.inflateMenu(var9.getResourceId(styleable.NavigationView_menu, 0));
      }

      if (var9.hasValue(styleable.NavigationView_headerLayout)) {
         this.inflateHeaderView(var9.getResourceId(styleable.NavigationView_headerLayout, 0));
      }

      var9.recycle();
      this.setupInsetScrimsListener();
   }

   private ColorStateList createDefaultColorStateList(int var1) {
      TypedValue var5 = new TypedValue();
      if (!this.getContext().getTheme().resolveAttribute(var1, var5, true)) {
         return null;
      } else {
         ColorStateList var4 = AppCompatResources.getColorStateList(this.getContext(), var5.resourceId);
         if (!this.getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, var5, true)) {
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

   private final Drawable createDefaultItemBackground(TintTypedArray var1) {
      int var2 = var1.getResourceId(styleable.NavigationView_itemShapeAppearance, 0);
      int var3 = var1.getResourceId(styleable.NavigationView_itemShapeAppearanceOverlay, 0);
      MaterialShapeDrawable var4 = new MaterialShapeDrawable(ShapeAppearanceModel.builder(this.getContext(), var2, var3).build());
      var4.setFillColor(MaterialResources.getColorStateList(this.getContext(), var1, styleable.NavigationView_itemShapeFillColor));
      return new InsetDrawable(var4, var1.getDimensionPixelSize(styleable.NavigationView_itemShapeInsetStart, 0), var1.getDimensionPixelSize(styleable.NavigationView_itemShapeInsetTop, 0), var1.getDimensionPixelSize(styleable.NavigationView_itemShapeInsetEnd, 0), var1.getDimensionPixelSize(styleable.NavigationView_itemShapeInsetBottom, 0));
   }

   private MenuInflater getMenuInflater() {
      if (this.menuInflater == null) {
         this.menuInflater = new SupportMenuInflater(this.getContext());
      }

      return this.menuInflater;
   }

   private boolean hasShapeAppearance(TintTypedArray var1) {
      return var1.hasValue(styleable.NavigationView_itemShapeAppearance) || var1.hasValue(styleable.NavigationView_itemShapeAppearanceOverlay);
   }

   private void setupInsetScrimsListener() {
      this.onGlobalLayoutListener = new OnGlobalLayoutListener() {
         public void onGlobalLayout() {
            NavigationView var5 = NavigationView.this;
            var5.getLocationOnScreen(var5.tmpLocation);
            int[] var6 = NavigationView.this.tmpLocation;
            boolean var4 = true;
            boolean var3;
            if (var6[1] == 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            NavigationView.this.presenter.setBehindStatusBar(var3);
            NavigationView.this.setDrawTopInsetForeground(var3);
            Context var7 = NavigationView.this.getContext();
            if (var7 instanceof Activity && VERSION.SDK_INT >= 21) {
               boolean var1;
               if (((Activity)var7).findViewById(16908290).getHeight() == NavigationView.this.getHeight()) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               boolean var2;
               if (Color.alpha(((Activity)var7).getWindow().getNavigationBarColor()) != 0) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               var5 = NavigationView.this;
               if (var1 && var2) {
                  var3 = var4;
               } else {
                  var3 = false;
               }

               var5.setDrawBottomInsetForeground(var3);
            }

         }
      };
      this.getViewTreeObserver().addOnGlobalLayoutListener(this.onGlobalLayoutListener);
   }

   public void addHeaderView(View var1) {
      this.presenter.addHeaderView(var1);
   }

   public MenuItem getCheckedItem() {
      return this.presenter.getCheckedItem();
   }

   public int getHeaderCount() {
      return this.presenter.getHeaderCount();
   }

   public View getHeaderView(int var1) {
      return this.presenter.getHeaderView(var1);
   }

   public Drawable getItemBackground() {
      return this.presenter.getItemBackground();
   }

   public int getItemHorizontalPadding() {
      return this.presenter.getItemHorizontalPadding();
   }

   public int getItemIconPadding() {
      return this.presenter.getItemIconPadding();
   }

   public ColorStateList getItemIconTintList() {
      return this.presenter.getItemTintList();
   }

   public int getItemMaxLines() {
      return this.presenter.getItemMaxLines();
   }

   public ColorStateList getItemTextColor() {
      return this.presenter.getItemTextColor();
   }

   public Menu getMenu() {
      return this.menu;
   }

   public View inflateHeaderView(int var1) {
      return this.presenter.inflateHeaderView(var1);
   }

   public void inflateMenu(int var1) {
      this.presenter.setUpdateSuspended(true);
      this.getMenuInflater().inflate(var1, this.menu);
      this.presenter.setUpdateSuspended(false);
      this.presenter.updateMenuView(false);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      MaterialShapeUtils.setParentAbsoluteElevation(this);
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (VERSION.SDK_INT < 16) {
         this.getViewTreeObserver().removeGlobalOnLayoutListener(this.onGlobalLayoutListener);
      } else {
         this.getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
      }
   }

   protected void onInsetsChanged(WindowInsetsCompat var1) {
      this.presenter.dispatchApplyWindowInsets(var1);
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      if (var3 != Integer.MIN_VALUE) {
         if (var3 == 0) {
            var1 = MeasureSpec.makeMeasureSpec(this.maxWidth, 1073741824);
         }
      } else {
         var1 = MeasureSpec.makeMeasureSpec(Math.min(MeasureSpec.getSize(var1), this.maxWidth), 1073741824);
      }

      super.onMeasure(var1, var2);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof NavigationView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         NavigationView.SavedState var2 = (NavigationView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.menu.restorePresenterStates(var2.menuState);
      }
   }

   protected Parcelable onSaveInstanceState() {
      NavigationView.SavedState var1 = new NavigationView.SavedState(super.onSaveInstanceState());
      var1.menuState = new Bundle();
      this.menu.savePresenterStates(var1.menuState);
      return var1;
   }

   public void removeHeaderView(View var1) {
      this.presenter.removeHeaderView(var1);
   }

   public void setCheckedItem(int var1) {
      MenuItem var2 = this.menu.findItem(var1);
      if (var2 != null) {
         this.presenter.setCheckedItem((MenuItemImpl)var2);
      }

   }

   public void setCheckedItem(MenuItem var1) {
      var1 = this.menu.findItem(var1.getItemId());
      if (var1 != null) {
         this.presenter.setCheckedItem((MenuItemImpl)var1);
      } else {
         throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
      }
   }

   public void setElevation(float var1) {
      if (VERSION.SDK_INT >= 21) {
         super.setElevation(var1);
      }

      MaterialShapeUtils.setElevation(this, var1);
   }

   public void setItemBackground(Drawable var1) {
      this.presenter.setItemBackground(var1);
   }

   public void setItemBackgroundResource(int var1) {
      this.setItemBackground(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setItemHorizontalPadding(int var1) {
      this.presenter.setItemHorizontalPadding(var1);
   }

   public void setItemHorizontalPaddingResource(int var1) {
      this.presenter.setItemHorizontalPadding(this.getResources().getDimensionPixelSize(var1));
   }

   public void setItemIconPadding(int var1) {
      this.presenter.setItemIconPadding(var1);
   }

   public void setItemIconPaddingResource(int var1) {
      this.presenter.setItemIconPadding(this.getResources().getDimensionPixelSize(var1));
   }

   public void setItemIconSize(int var1) {
      this.presenter.setItemIconSize(var1);
   }

   public void setItemIconTintList(ColorStateList var1) {
      this.presenter.setItemIconTintList(var1);
   }

   public void setItemMaxLines(int var1) {
      this.presenter.setItemMaxLines(var1);
   }

   public void setItemTextAppearance(int var1) {
      this.presenter.setItemTextAppearance(var1);
   }

   public void setItemTextColor(ColorStateList var1) {
      this.presenter.setItemTextColor(var1);
   }

   public void setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener var1) {
      this.listener = var1;
   }

   public void setOverScrollMode(int var1) {
      super.setOverScrollMode(var1);
      NavigationMenuPresenter var2 = this.presenter;
      if (var2 != null) {
         var2.setOverScrollMode(var1);
      }

   }

   public interface OnNavigationItemSelectedListener {
      boolean onNavigationItemSelected(MenuItem var1);
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

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeBundle(this.menuState);
      }
   }
}
