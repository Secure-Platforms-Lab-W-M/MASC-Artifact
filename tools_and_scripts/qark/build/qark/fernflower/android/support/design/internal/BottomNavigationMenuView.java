package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.R$dimen;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.util.Pools;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class BottomNavigationMenuView extends ViewGroup implements MenuView {
   private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;
   private final int mActiveItemMaxWidth;
   private BottomNavigationItemView[] mButtons;
   private final int mInactiveItemMaxWidth;
   private final int mInactiveItemMinWidth;
   private int mItemBackgroundRes;
   private final int mItemHeight;
   private ColorStateList mItemIconTint;
   private final Pools.Pool mItemPool;
   private ColorStateList mItemTextColor;
   private MenuBuilder mMenu;
   private final OnClickListener mOnClickListener;
   private BottomNavigationPresenter mPresenter;
   private int mSelectedItemId;
   private int mSelectedItemPosition;
   private final TransitionSet mSet;
   private boolean mShiftingMode;
   private int[] mTempChildWidths;

   public BottomNavigationMenuView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public BottomNavigationMenuView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mItemPool = new Pools.SynchronizedPool(5);
      this.mShiftingMode = true;
      this.mSelectedItemId = 0;
      this.mSelectedItemPosition = 0;
      Resources var3 = this.getResources();
      this.mInactiveItemMaxWidth = var3.getDimensionPixelSize(R$dimen.design_bottom_navigation_item_max_width);
      this.mInactiveItemMinWidth = var3.getDimensionPixelSize(R$dimen.design_bottom_navigation_item_min_width);
      this.mActiveItemMaxWidth = var3.getDimensionPixelSize(R$dimen.design_bottom_navigation_active_item_max_width);
      this.mItemHeight = var3.getDimensionPixelSize(R$dimen.design_bottom_navigation_height);
      this.mSet = new AutoTransition();
      this.mSet.setOrdering(0);
      this.mSet.setDuration(115L);
      this.mSet.setInterpolator(new FastOutSlowInInterpolator());
      this.mSet.addTransition(new TextScale());
      this.mOnClickListener = new OnClickListener() {
         public void onClick(View var1) {
            MenuItemImpl var2 = ((BottomNavigationItemView)var1).getItemData();
            if (!BottomNavigationMenuView.this.mMenu.performItemAction(var2, BottomNavigationMenuView.this.mPresenter, 0)) {
               var2.setChecked(true);
            }
         }
      };
      this.mTempChildWidths = new int[5];
   }

   private BottomNavigationItemView getNewItem() {
      BottomNavigationItemView var1 = (BottomNavigationItemView)this.mItemPool.acquire();
      return var1 == null ? new BottomNavigationItemView(this.getContext()) : var1;
   }

   public void buildMenuView() {
      this.removeAllViews();
      BottomNavigationItemView[] var4 = this.mButtons;
      int var1;
      if (var4 != null) {
         int var2 = var4.length;

         for(var1 = 0; var1 < var2; ++var1) {
            BottomNavigationItemView var5 = var4[var1];
            this.mItemPool.release(var5);
         }
      }

      if (this.mMenu.size() == 0) {
         this.mSelectedItemId = 0;
         this.mSelectedItemPosition = 0;
         this.mButtons = null;
      } else {
         this.mButtons = new BottomNavigationItemView[this.mMenu.size()];
         boolean var3;
         if (this.mMenu.size() > 3) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mShiftingMode = var3;

         for(var1 = 0; var1 < this.mMenu.size(); ++var1) {
            this.mPresenter.setUpdateSuspended(true);
            this.mMenu.getItem(var1).setCheckable(true);
            this.mPresenter.setUpdateSuspended(false);
            BottomNavigationItemView var6 = this.getNewItem();
            this.mButtons[var1] = var6;
            var6.setIconTintList(this.mItemIconTint);
            var6.setTextColor(this.mItemTextColor);
            var6.setItemBackground(this.mItemBackgroundRes);
            var6.setShiftingMode(this.mShiftingMode);
            var6.initialize((MenuItemImpl)this.mMenu.getItem(var1), 0);
            var6.setItemPosition(var1);
            var6.setOnClickListener(this.mOnClickListener);
            this.addView(var6);
         }

         this.mSelectedItemPosition = Math.min(this.mMenu.size() - 1, this.mSelectedItemPosition);
         this.mMenu.getItem(this.mSelectedItemPosition).setChecked(true);
      }
   }

   @Nullable
   public ColorStateList getIconTintList() {
      return this.mItemIconTint;
   }

   public int getItemBackgroundRes() {
      return this.mItemBackgroundRes;
   }

   public ColorStateList getItemTextColor() {
      return this.mItemTextColor;
   }

   public int getSelectedItemId() {
      return this.mSelectedItemId;
   }

   public int getWindowAnimations() {
      return 0;
   }

   public void initialize(MenuBuilder var1) {
      this.mMenu = var1;
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
      var2 = MeasureSpec.getSize(var1);
      int var4 = this.getChildCount();
      int var7 = MeasureSpec.makeMeasureSpec(this.mItemHeight, 1073741824);
      int var10002;
      int var3;
      int[] var8;
      if (this.mShiftingMode) {
         var1 = var4 - 1;
         int var5 = Math.min(var2 - this.mInactiveItemMinWidth * var1, this.mActiveItemMaxWidth);
         int var6 = Math.min((var2 - var5) / var1, this.mInactiveItemMaxWidth);
         var2 = var2 - var5 - var6 * var1;

         for(var1 = 0; var1 < var4; ++var1) {
            var8 = this.mTempChildWidths;
            if (var1 == this.mSelectedItemPosition) {
               var3 = var5;
            } else {
               var3 = var6;
            }

            var8[var1] = var3;
            if (var2 > 0) {
               var8 = this.mTempChildWidths;
               var10002 = var8[var1]++;
               --var2;
            }
         }
      } else {
         if (var4 == 0) {
            var1 = 1;
         } else {
            var1 = var4;
         }

         var3 = Math.min(var2 / var1, this.mActiveItemMaxWidth);
         var2 -= var3 * var4;

         for(var1 = 0; var1 < var4; ++var1) {
            var8 = this.mTempChildWidths;
            var8[var1] = var3;
            if (var2 > 0) {
               var10002 = var8[var1]++;
               --var2;
            }
         }
      }

      var2 = 0;

      for(var1 = 0; var1 < var4; ++var1) {
         View var9 = this.getChildAt(var1);
         if (var9.getVisibility() != 8) {
            var9.measure(MeasureSpec.makeMeasureSpec(this.mTempChildWidths[var1], 1073741824), var7);
            var9.getLayoutParams().width = var9.getMeasuredWidth();
            var2 += var9.getMeasuredWidth();
         }
      }

      this.setMeasuredDimension(View.resolveSizeAndState(var2, MeasureSpec.makeMeasureSpec(var2, 1073741824), 0), View.resolveSizeAndState(this.mItemHeight, var7, 0));
   }

   public void setIconTintList(ColorStateList var1) {
      this.mItemIconTint = var1;
      BottomNavigationItemView[] var4 = this.mButtons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2].setIconTintList(var1);
         }

      }
   }

   public void setItemBackgroundRes(int var1) {
      this.mItemBackgroundRes = var1;
      BottomNavigationItemView[] var4 = this.mButtons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2].setItemBackground(var1);
         }

      }
   }

   public void setItemTextColor(ColorStateList var1) {
      this.mItemTextColor = var1;
      BottomNavigationItemView[] var4 = this.mButtons;
      if (var4 != null) {
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2].setTextColor(var1);
         }

      }
   }

   public void setPresenter(BottomNavigationPresenter var1) {
      this.mPresenter = var1;
   }

   void tryRestoreSelectedItemId(int var1) {
      int var3 = this.mMenu.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         MenuItem var4 = this.mMenu.getItem(var2);
         if (var1 == var4.getItemId()) {
            this.mSelectedItemId = var1;
            this.mSelectedItemPosition = var2;
            var4.setChecked(true);
            return;
         }
      }

   }

   public void updateMenuView() {
      int var2 = this.mMenu.size();
      if (var2 != this.mButtons.length) {
         this.buildMenuView();
      } else {
         int var3 = this.mSelectedItemId;

         int var1;
         for(var1 = 0; var1 < var2; ++var1) {
            MenuItem var4 = this.mMenu.getItem(var1);
            if (var4.isChecked()) {
               this.mSelectedItemId = var4.getItemId();
               this.mSelectedItemPosition = var1;
            }
         }

         if (var3 != this.mSelectedItemId) {
            TransitionManager.beginDelayedTransition(this, this.mSet);
         }

         for(var1 = 0; var1 < var2; ++var1) {
            this.mPresenter.setUpdateSuspended(true);
            this.mButtons[var1].initialize((MenuItemImpl)this.mMenu.getItem(var1), 0);
            this.mPresenter.setUpdateSuspended(false);
         }

      }
   }
}
