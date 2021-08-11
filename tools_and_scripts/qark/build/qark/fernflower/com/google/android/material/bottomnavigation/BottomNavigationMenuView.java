package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import androidx.appcompat.R.attr;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.util.Pools;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import com.google.android.material.R.dimen;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.TextScale;
import java.util.HashSet;

public class BottomNavigationMenuView extends ViewGroup implements MenuView {
   private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
   private static final int ITEM_POOL_SIZE = 5;
   private final int activeItemMaxWidth;
   private final int activeItemMinWidth;
   private SparseArray badgeDrawables;
   private BottomNavigationItemView[] buttons;
   private final int inactiveItemMaxWidth;
   private final int inactiveItemMinWidth;
   private Drawable itemBackground;
   private int itemBackgroundRes;
   private final int itemHeight;
   private boolean itemHorizontalTranslationEnabled;
   private int itemIconSize;
   private ColorStateList itemIconTint;
   private final Pools.Pool itemPool;
   private int itemTextAppearanceActive;
   private int itemTextAppearanceInactive;
   private final ColorStateList itemTextColorDefault;
   private ColorStateList itemTextColorFromUser;
   private int labelVisibilityMode;
   private MenuBuilder menu;
   private final OnClickListener onClickListener;
   private BottomNavigationPresenter presenter;
   private int selectedItemId;
   private int selectedItemPosition;
   private final TransitionSet set;
   private int[] tempChildWidths;

   public BottomNavigationMenuView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public BottomNavigationMenuView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.itemPool = new Pools.SynchronizedPool(5);
      this.selectedItemId = 0;
      this.selectedItemPosition = 0;
      this.badgeDrawables = new SparseArray(5);
      Resources var3 = this.getResources();
      this.inactiveItemMaxWidth = var3.getDimensionPixelSize(dimen.design_bottom_navigation_item_max_width);
      this.inactiveItemMinWidth = var3.getDimensionPixelSize(dimen.design_bottom_navigation_item_min_width);
      this.activeItemMaxWidth = var3.getDimensionPixelSize(dimen.design_bottom_navigation_active_item_max_width);
      this.activeItemMinWidth = var3.getDimensionPixelSize(dimen.design_bottom_navigation_active_item_min_width);
      this.itemHeight = var3.getDimensionPixelSize(dimen.design_bottom_navigation_height);
      this.itemTextColorDefault = this.createDefaultColorStateList(16842808);
      AutoTransition var4 = new AutoTransition();
      this.set = var4;
      var4.setOrdering(0);
      this.set.setDuration(115L);
      this.set.setInterpolator(new FastOutSlowInInterpolator());
      this.set.addTransition(new TextScale());
      this.onClickListener = new OnClickListener() {
         public void onClick(View var1) {
            MenuItemImpl var2 = ((BottomNavigationItemView)var1).getItemData();
            if (!BottomNavigationMenuView.this.menu.performItemAction(var2, BottomNavigationMenuView.this.presenter, 0)) {
               var2.setChecked(true);
            }

         }
      };
      this.tempChildWidths = new int[5];
   }

   private BottomNavigationItemView getNewItem() {
      BottomNavigationItemView var2 = (BottomNavigationItemView)this.itemPool.acquire();
      BottomNavigationItemView var1 = var2;
      if (var2 == null) {
         var1 = new BottomNavigationItemView(this.getContext());
      }

      return var1;
   }

   private boolean isShifting(int var1, int var2) {
      if (var1 == -1) {
         if (var2 > 3) {
            return true;
         }
      } else if (var1 == 0) {
         return true;
      }

      return false;
   }

   private boolean isValidId(int var1) {
      return var1 != -1;
   }

   private void removeUnusedBadges() {
      HashSet var3 = new HashSet();

      int var1;
      for(var1 = 0; var1 < this.menu.size(); ++var1) {
         var3.add(this.menu.getItem(var1).getItemId());
      }

      for(var1 = 0; var1 < this.badgeDrawables.size(); ++var1) {
         int var2 = this.badgeDrawables.keyAt(var1);
         if (!var3.contains(var2)) {
            this.badgeDrawables.delete(var2);
         }
      }

   }

   private void setBadgeIfNeeded(BottomNavigationItemView var1) {
      int var2 = var1.getId();
      if (this.isValidId(var2)) {
         BadgeDrawable var3 = (BadgeDrawable)this.badgeDrawables.get(var2);
         if (var3 != null) {
            var1.setBadge(var3);
         }

      }
   }

   private void validateMenuItemId(int var1) {
      if (!this.isValidId(var1)) {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1);
         var2.append(" is not a valid view id");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public void buildMenuView() {
      this.removeAllViews();
      BottomNavigationItemView[] var4 = this.buttons;
      int var1;
      if (var4 != null) {
         int var2 = var4.length;

         for(var1 = 0; var1 < var2; ++var1) {
            BottomNavigationItemView var5 = var4[var1];
            if (var5 != null) {
               this.itemPool.release(var5);
               var5.removeBadge();
            }
         }
      }

      if (this.menu.size() == 0) {
         this.selectedItemId = 0;
         this.selectedItemPosition = 0;
         this.buttons = null;
      } else {
         this.removeUnusedBadges();
         this.buttons = new BottomNavigationItemView[this.menu.size()];
         boolean var3 = this.isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());

         for(var1 = 0; var1 < this.menu.size(); ++var1) {
            this.presenter.setUpdateSuspended(true);
            this.menu.getItem(var1).setCheckable(true);
            this.presenter.setUpdateSuspended(false);
            BottomNavigationItemView var6 = this.getNewItem();
            this.buttons[var1] = var6;
            var6.setIconTintList(this.itemIconTint);
            var6.setIconSize(this.itemIconSize);
            var6.setTextColor(this.itemTextColorDefault);
            var6.setTextAppearanceInactive(this.itemTextAppearanceInactive);
            var6.setTextAppearanceActive(this.itemTextAppearanceActive);
            var6.setTextColor(this.itemTextColorFromUser);
            Drawable var7 = this.itemBackground;
            if (var7 != null) {
               var6.setItemBackground(var7);
            } else {
               var6.setItemBackground(this.itemBackgroundRes);
            }

            var6.setShifting(var3);
            var6.setLabelVisibilityMode(this.labelVisibilityMode);
            var6.initialize((MenuItemImpl)this.menu.getItem(var1), 0);
            var6.setItemPosition(var1);
            var6.setOnClickListener(this.onClickListener);
            if (this.selectedItemId != 0 && this.menu.getItem(var1).getItemId() == this.selectedItemId) {
               this.selectedItemPosition = var1;
            }

            this.setBadgeIfNeeded(var6);
            this.addView(var6);
         }

         var1 = Math.min(this.menu.size() - 1, this.selectedItemPosition);
         this.selectedItemPosition = var1;
         this.menu.getItem(var1).setChecked(true);
      }
   }

   public ColorStateList createDefaultColorStateList(int var1) {
      TypedValue var5 = new TypedValue();
      if (!this.getContext().getTheme().resolveAttribute(var1, var5, true)) {
         return null;
      } else {
         ColorStateList var4 = AppCompatResources.getColorStateList(this.getContext(), var5.resourceId);
         if (!this.getContext().getTheme().resolveAttribute(attr.colorPrimary, var5, true)) {
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

   BottomNavigationItemView findItemView(int var1) {
      this.validateMenuItemId(var1);
      BottomNavigationItemView[] var4 = this.buttons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            BottomNavigationItemView var5 = var4[var2];
            if (var5.getId() == var1) {
               return var5;
            }
         }
      }

      return null;
   }

   BadgeDrawable getBadge(int var1) {
      return (BadgeDrawable)this.badgeDrawables.get(var1);
   }

   SparseArray getBadgeDrawables() {
      return this.badgeDrawables;
   }

   public ColorStateList getIconTintList() {
      return this.itemIconTint;
   }

   public Drawable getItemBackground() {
      BottomNavigationItemView[] var1 = this.buttons;
      return var1 != null && var1.length > 0 ? var1[0].getBackground() : this.itemBackground;
   }

   @Deprecated
   public int getItemBackgroundRes() {
      return this.itemBackgroundRes;
   }

   public int getItemIconSize() {
      return this.itemIconSize;
   }

   public int getItemTextAppearanceActive() {
      return this.itemTextAppearanceActive;
   }

   public int getItemTextAppearanceInactive() {
      return this.itemTextAppearanceInactive;
   }

   public ColorStateList getItemTextColor() {
      return this.itemTextColorFromUser;
   }

   public int getLabelVisibilityMode() {
      return this.labelVisibilityMode;
   }

   BadgeDrawable getOrCreateBadge(int var1) {
      this.validateMenuItemId(var1);
      BadgeDrawable var3 = (BadgeDrawable)this.badgeDrawables.get(var1);
      BadgeDrawable var2 = var3;
      if (var3 == null) {
         var2 = BadgeDrawable.create(this.getContext());
         this.badgeDrawables.put(var1, var2);
      }

      BottomNavigationItemView var4 = this.findItemView(var1);
      if (var4 != null) {
         var4.setBadge(var2);
      }

      return var2;
   }

   public int getSelectedItemId() {
      return this.selectedItemId;
   }

   public int getWindowAnimations() {
      return 0;
   }

   public void initialize(MenuBuilder var1) {
      this.menu = var1;
   }

   public boolean isItemHorizontalTranslationEnabled() {
      return this.itemHorizontalTranslationEnabled;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = this.getChildCount();
      var4 -= var2;
      var5 -= var3;
      var3 = 0;

      for(var2 = 0; var2 < var6; ++var2) {
         View var7 = this.getChildAt(var2);
         if (var7.getVisibility() != 8) {
            if (ViewCompat.getLayoutDirection(this) == 1) {
               var7.layout(var4 - var3 - var7.getMeasuredWidth(), 0, var4 - var3, var5);
            } else {
               var7.layout(var3, 0, var7.getMeasuredWidth() + var3, var5);
            }

            var3 += var7.getMeasuredWidth();
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var8 = MeasureSpec.getSize(var1);
      int var3 = this.menu.getVisibleItems().size();
      int var6 = this.getChildCount();
      int var7 = MeasureSpec.makeMeasureSpec(this.itemHeight, 1073741824);
      int var4;
      int[] var9;
      View var11;
      int var10002;
      if (this.isShifting(this.labelVisibilityMode, var3) && this.itemHorizontalTranslationEnabled) {
         var11 = this.getChildAt(this.selectedItemPosition);
         var2 = this.activeItemMinWidth;
         var1 = var2;
         if (var11.getVisibility() != 8) {
            var11.measure(MeasureSpec.makeMeasureSpec(this.activeItemMaxWidth, Integer.MIN_VALUE), var7);
            var1 = Math.max(var2, var11.getMeasuredWidth());
         }

         byte var10;
         if (var11.getVisibility() != 8) {
            var10 = 1;
         } else {
            var10 = 0;
         }

         var2 = var3 - var10;
         var4 = Math.min(var8 - this.inactiveItemMinWidth * var2, Math.min(var1, this.activeItemMaxWidth));
         if (var2 == 0) {
            var1 = 1;
         } else {
            var1 = var2;
         }

         int var5 = Math.min((var8 - var4) / var1, this.inactiveItemMaxWidth);
         var2 = var8 - var4 - var5 * var2;

         for(var1 = 0; var1 < var6; var2 = var3) {
            if (this.getChildAt(var1).getVisibility() != 8) {
               var9 = this.tempChildWidths;
               if (var1 == this.selectedItemPosition) {
                  var3 = var4;
               } else {
                  var3 = var5;
               }

               var9[var1] = var3;
               var3 = var2;
               if (var2 > 0) {
                  var9 = this.tempChildWidths;
                  var10002 = var9[var1]++;
                  var3 = var2 - 1;
               }
            } else {
               this.tempChildWidths[var1] = 0;
               var3 = var2;
            }

            ++var1;
         }
      } else {
         if (var3 == 0) {
            var1 = 1;
         } else {
            var1 = var3;
         }

         var4 = Math.min(var8 / var1, this.activeItemMaxWidth);
         var2 = var8 - var4 * var3;

         for(var1 = 0; var1 < var6; var2 = var3) {
            if (this.getChildAt(var1).getVisibility() != 8) {
               var9 = this.tempChildWidths;
               var9[var1] = var4;
               var3 = var2;
               if (var2 > 0) {
                  var10002 = var9[var1]++;
                  var3 = var2 - 1;
               }
            } else {
               this.tempChildWidths[var1] = 0;
               var3 = var2;
            }

            ++var1;
         }
      }

      var2 = 0;

      for(var1 = 0; var1 < var6; ++var1) {
         var11 = this.getChildAt(var1);
         if (var11.getVisibility() != 8) {
            var11.measure(MeasureSpec.makeMeasureSpec(this.tempChildWidths[var1], 1073741824), var7);
            var11.getLayoutParams().width = var11.getMeasuredWidth();
            var2 += var11.getMeasuredWidth();
         }
      }

      this.setMeasuredDimension(View.resolveSizeAndState(var2, MeasureSpec.makeMeasureSpec(var2, 1073741824), 0), View.resolveSizeAndState(this.itemHeight, var7, 0));
   }

   void removeBadge(int var1) {
      this.validateMenuItemId(var1);
      BadgeDrawable var2 = (BadgeDrawable)this.badgeDrawables.get(var1);
      BottomNavigationItemView var3 = this.findItemView(var1);
      if (var3 != null) {
         var3.removeBadge();
      }

      if (var2 != null) {
         this.badgeDrawables.remove(var1);
      }

   }

   void setBadgeDrawables(SparseArray var1) {
      this.badgeDrawables = var1;
      BottomNavigationItemView[] var4 = this.buttons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            BottomNavigationItemView var5 = var4[var2];
            var5.setBadge((BadgeDrawable)var1.get(var5.getId()));
         }
      }

   }

   public void setIconTintList(ColorStateList var1) {
      this.itemIconTint = var1;
      BottomNavigationItemView[] var4 = this.buttons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2].setIconTintList(var1);
         }
      }

   }

   public void setItemBackground(Drawable var1) {
      this.itemBackground = var1;
      BottomNavigationItemView[] var4 = this.buttons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2].setItemBackground(var1);
         }
      }

   }

   public void setItemBackgroundRes(int var1) {
      this.itemBackgroundRes = var1;
      BottomNavigationItemView[] var4 = this.buttons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2].setItemBackground(var1);
         }
      }

   }

   public void setItemHorizontalTranslationEnabled(boolean var1) {
      this.itemHorizontalTranslationEnabled = var1;
   }

   public void setItemIconSize(int var1) {
      this.itemIconSize = var1;
      BottomNavigationItemView[] var4 = this.buttons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2].setIconSize(var1);
         }
      }

   }

   public void setItemTextAppearanceActive(int var1) {
      this.itemTextAppearanceActive = var1;
      BottomNavigationItemView[] var4 = this.buttons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            BottomNavigationItemView var5 = var4[var2];
            var5.setTextAppearanceActive(var1);
            ColorStateList var6 = this.itemTextColorFromUser;
            if (var6 != null) {
               var5.setTextColor(var6);
            }
         }
      }

   }

   public void setItemTextAppearanceInactive(int var1) {
      this.itemTextAppearanceInactive = var1;
      BottomNavigationItemView[] var4 = this.buttons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            BottomNavigationItemView var5 = var4[var2];
            var5.setTextAppearanceInactive(var1);
            ColorStateList var6 = this.itemTextColorFromUser;
            if (var6 != null) {
               var5.setTextColor(var6);
            }
         }
      }

   }

   public void setItemTextColor(ColorStateList var1) {
      this.itemTextColorFromUser = var1;
      BottomNavigationItemView[] var4 = this.buttons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2].setTextColor(var1);
         }
      }

   }

   public void setLabelVisibilityMode(int var1) {
      this.labelVisibilityMode = var1;
   }

   public void setPresenter(BottomNavigationPresenter var1) {
      this.presenter = var1;
   }

   void tryRestoreSelectedItemId(int var1) {
      int var3 = this.menu.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         MenuItem var4 = this.menu.getItem(var2);
         if (var1 == var4.getItemId()) {
            this.selectedItemId = var1;
            this.selectedItemPosition = var2;
            var4.setChecked(true);
            return;
         }
      }

   }

   public void updateMenuView() {
      MenuBuilder var5 = this.menu;
      if (var5 != null) {
         if (this.buttons != null) {
            int var2 = var5.size();
            if (var2 != this.buttons.length) {
               this.buildMenuView();
            } else {
               int var3 = this.selectedItemId;

               int var1;
               for(var1 = 0; var1 < var2; ++var1) {
                  MenuItem var6 = this.menu.getItem(var1);
                  if (var6.isChecked()) {
                     this.selectedItemId = var6.getItemId();
                     this.selectedItemPosition = var1;
                  }
               }

               if (var3 != this.selectedItemId) {
                  TransitionManager.beginDelayedTransition(this, this.set);
               }

               boolean var4 = this.isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());

               for(var1 = 0; var1 < var2; ++var1) {
                  this.presenter.setUpdateSuspended(true);
                  this.buttons[var1].setLabelVisibilityMode(this.labelVisibilityMode);
                  this.buttons[var1].setShifting(var4);
                  this.buttons[var1].initialize((MenuItemImpl)this.menu.getItem(var1), 0);
                  this.presenter.setUpdateSuspended(false);
               }

            }
         }
      }
   }
}
