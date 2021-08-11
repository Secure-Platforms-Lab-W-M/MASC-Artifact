package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$layout;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;

class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
   private static final String TAG = "ActionMenuPresenter";
   private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
   ActionMenuPresenter.ActionButtonSubmenu mActionButtonPopup;
   private int mActionItemWidthLimit;
   private boolean mExpandedActionViewsExclusive;
   private int mMaxItems;
   private boolean mMaxItemsSet;
   private int mMinCellSize;
   int mOpenSubMenuId;
   ActionMenuPresenter.OverflowMenuButton mOverflowButton;
   ActionMenuPresenter.OverflowPopup mOverflowPopup;
   private Drawable mPendingOverflowIcon;
   private boolean mPendingOverflowIconSet;
   private ActionMenuPresenter.ActionMenuPopupCallback mPopupCallback;
   final ActionMenuPresenter.PopupPresenterCallback mPopupPresenterCallback = new ActionMenuPresenter.PopupPresenterCallback();
   ActionMenuPresenter.OpenOverflowRunnable mPostedOpenRunnable;
   private boolean mReserveOverflow;
   private boolean mReserveOverflowSet;
   private View mScrapActionButtonView;
   private boolean mStrictWidthLimit;
   private int mWidthLimit;
   private boolean mWidthLimitSet;

   public ActionMenuPresenter(Context var1) {
      super(var1, R$layout.abc_action_menu_layout, R$layout.abc_action_menu_item_layout);
   }

   private View findViewForItem(MenuItem var1) {
      ViewGroup var4 = (ViewGroup)this.mMenuView;
      if (var4 == null) {
         return null;
      } else {
         int var3 = var4.getChildCount();

         for(int var2 = 0; var2 < var3; ++var2) {
            View var5 = var4.getChildAt(var2);
            if (var5 instanceof MenuView.ItemView && ((MenuView.ItemView)var5).getItemData() == var1) {
               return var5;
            }
         }

         return null;
      }
   }

   public void bindItemView(MenuItemImpl var1, MenuView.ItemView var2) {
      var2.initialize(var1, 0);
      ActionMenuView var3 = (ActionMenuView)this.mMenuView;
      ActionMenuItemView var4 = (ActionMenuItemView)var2;
      var4.setItemInvoker(var3);
      if (this.mPopupCallback == null) {
         this.mPopupCallback = new ActionMenuPresenter.ActionMenuPopupCallback();
      }

      var4.setPopupCallback(this.mPopupCallback);
   }

   public boolean dismissPopupMenus() {
      return this.hideOverflowMenu() | this.hideSubMenus();
   }

   public boolean filterLeftoverView(ViewGroup var1, int var2) {
      return var1.getChildAt(var2) == this.mOverflowButton ? false : super.filterLeftoverView(var1, var2);
   }

   public boolean flagActionItems() {
      ActionMenuPresenter var16 = this;
      int var3;
      ArrayList var15;
      if (this.mMenu != null) {
         var15 = this.mMenu.getVisibleItems();
         var3 = var15.size();
      } else {
         var15 = null;
         var3 = 0;
      }

      int var1 = this.mMaxItems;
      int var9 = this.mActionItemWidthLimit;
      int var11 = MeasureSpec.makeMeasureSpec(0, 0);
      ViewGroup var17 = (ViewGroup)this.mMenuView;
      int var4 = 0;
      int var6 = 0;
      byte var8 = 0;
      boolean var5 = false;

      int var2;
      int var7;
      for(var2 = 0; var2 < var3; var1 = var7) {
         MenuItemImpl var18 = (MenuItemImpl)var15.get(var2);
         if (var18.requiresActionButton()) {
            ++var4;
         } else if (var18.requestsActionButton()) {
            ++var6;
         } else {
            var5 = true;
         }

         var7 = var1;
         if (var16.mExpandedActionViewsExclusive) {
            var7 = var1;
            if (var18.isActionViewExpanded()) {
               var7 = 0;
            }
         }

         ++var2;
      }

      var2 = var1;
      if (var16.mReserveOverflow) {
         label161: {
            if (!var5) {
               var2 = var1;
               if (var4 + var6 <= var1) {
                  break label161;
               }
            }

            var2 = var1 - 1;
         }
      }

      int var22 = var2 - var4;
      SparseBooleanArray var25 = var16.mActionButtonGroups;
      var25.clear();
      var6 = 0;
      var2 = 0;
      if (var16.mStrictWidthLimit) {
         var1 = var16.mMinCellSize;
         var2 = var9 / var1;
         var6 = var1 + var9 % var1 / var2;
      }

      var7 = 0;
      var1 = var8;
      ViewGroup var23 = var17;
      var4 = var9;

      int var10;
      for(var9 = var3; var7 < var9; var22 = var10) {
         MenuItemImpl var19 = (MenuItemImpl)var15.get(var7);
         View var20;
         if (var19.requiresActionButton()) {
            var20 = this.getItemView(var19, this.mScrapActionButtonView, var23);
            if (this.mScrapActionButtonView == null) {
               this.mScrapActionButtonView = var20;
            }

            if (this.mStrictWidthLimit) {
               var2 -= ActionMenuView.measureChildForCells(var20, var6, var2, var11, 0);
            } else {
               var20.measure(var11, var11);
            }

            var10 = var20.getMeasuredWidth();
            var4 -= var10;
            var3 = var1;
            if (var1 == 0) {
               var3 = var10;
            }

            var1 = var19.getGroupId();
            if (var1 != 0) {
               var25.put(var1, true);
            }

            var19.setIsActionButton(true);
            var10 = var22;
            var1 = var3;
         } else if (!var19.requestsActionButton()) {
            var19.setIsActionButton(false);
            var10 = var22;
         } else {
            int var12 = var19.getGroupId();
            boolean var14 = var25.get(var12);
            boolean var13;
            if (var22 <= 0 && !var14 || var4 <= 0 || this.mStrictWidthLimit && var2 <= 0) {
               var13 = false;
            } else {
               var13 = true;
            }

            var3 = var22;
            if (var13) {
               var20 = this.getItemView(var19, this.mScrapActionButtonView, var23);
               if (this.mScrapActionButtonView == null) {
                  this.mScrapActionButtonView = var20;
               }

               if (this.mStrictWidthLimit) {
                  var22 = ActionMenuView.measureChildForCells(var20, var6, var2, var11, 0);
                  var2 -= var22;
                  if (var22 == 0) {
                     var13 = false;
                  }
               } else {
                  var20.measure(var11, var11);
               }

               var10 = var20.getMeasuredWidth();
               var4 -= var10;
               var22 = var1;
               if (var1 == 0) {
                  var22 = var10;
               }

               boolean var21;
               if (this.mStrictWidthLimit) {
                  if (var4 >= 0) {
                     var21 = true;
                  } else {
                     var21 = false;
                  }

                  var13 &= var21;
               } else {
                  if (var4 + var22 > 0) {
                     var21 = true;
                  } else {
                     var21 = false;
                  }

                  var13 &= var21;
               }
            } else {
               var22 = var1;
            }

            if (var13 && var12 != 0) {
               var25.put(var12, true);
               var1 = var3;
            } else if (var14) {
               var25.put(var12, false);
               var10 = 0;

               for(var1 = var3; var10 < var7; var1 = var3) {
                  MenuItemImpl var24 = (MenuItemImpl)var15.get(var10);
                  var3 = var1;
                  if (var24.getGroupId() == var12) {
                     var3 = var1;
                     if (var24.isActionButton()) {
                        var3 = var1 + 1;
                     }

                     var24.setIsActionButton(false);
                  }

                  ++var10;
               }
            } else {
               var1 = var3;
            }

            var3 = var1;
            if (var13) {
               var3 = var1 - 1;
            }

            var19.setIsActionButton(var13);
            var10 = var3;
            var1 = var22;
         }

         ++var7;
      }

      return true;
   }

   public View getItemView(MenuItemImpl var1, View var2, ViewGroup var3) {
      View var5 = var1.getActionView();
      if (var5 == null || var1.hasCollapsibleActionView()) {
         var5 = super.getItemView(var1, var2, var3);
      }

      byte var4;
      if (var1.isActionViewExpanded()) {
         var4 = 8;
      } else {
         var4 = 0;
      }

      var5.setVisibility(var4);
      ActionMenuView var6 = (ActionMenuView)var3;
      LayoutParams var7 = var5.getLayoutParams();
      if (!var6.checkLayoutParams(var7)) {
         var5.setLayoutParams(var6.generateLayoutParams(var7));
      }

      return var5;
   }

   public MenuView getMenuView(ViewGroup var1) {
      MenuView var2 = this.mMenuView;
      MenuView var3 = super.getMenuView(var1);
      if (var2 != var3) {
         ((ActionMenuView)var3).setPresenter(this);
      }

      return var3;
   }

   public Drawable getOverflowIcon() {
      ActionMenuPresenter.OverflowMenuButton var1 = this.mOverflowButton;
      if (var1 != null) {
         return var1.getDrawable();
      } else {
         return this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null;
      }
   }

   public boolean hideOverflowMenu() {
      if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
         ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
         this.mPostedOpenRunnable = null;
         return true;
      } else {
         ActionMenuPresenter.OverflowPopup var1 = this.mOverflowPopup;
         if (var1 != null) {
            var1.dismiss();
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean hideSubMenus() {
      ActionMenuPresenter.ActionButtonSubmenu var1 = this.mActionButtonPopup;
      if (var1 != null) {
         var1.dismiss();
         return true;
      } else {
         return false;
      }
   }

   public void initForMenu(@NonNull Context var1, @Nullable MenuBuilder var2) {
      super.initForMenu(var1, var2);
      Resources var6 = var1.getResources();
      ActionBarPolicy var5 = ActionBarPolicy.get(var1);
      if (!this.mReserveOverflowSet) {
         this.mReserveOverflow = var5.showsOverflowMenuButton();
      }

      if (!this.mWidthLimitSet) {
         this.mWidthLimit = var5.getEmbeddedMenuWidthLimit();
      }

      if (!this.mMaxItemsSet) {
         this.mMaxItems = var5.getMaxActionButtons();
      }

      int var3 = this.mWidthLimit;
      if (this.mReserveOverflow) {
         if (this.mOverflowButton == null) {
            this.mOverflowButton = new ActionMenuPresenter.OverflowMenuButton(this.mSystemContext);
            if (this.mPendingOverflowIconSet) {
               this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
               this.mPendingOverflowIcon = null;
               this.mPendingOverflowIconSet = false;
            }

            int var4 = MeasureSpec.makeMeasureSpec(0, 0);
            this.mOverflowButton.measure(var4, var4);
         }

         var3 -= this.mOverflowButton.getMeasuredWidth();
      } else {
         this.mOverflowButton = null;
      }

      this.mActionItemWidthLimit = var3;
      this.mMinCellSize = (int)(var6.getDisplayMetrics().density * 56.0F);
      this.mScrapActionButtonView = null;
   }

   public boolean isOverflowMenuShowPending() {
      return this.mPostedOpenRunnable != null || this.isOverflowMenuShowing();
   }

   public boolean isOverflowMenuShowing() {
      ActionMenuPresenter.OverflowPopup var1 = this.mOverflowPopup;
      return var1 != null && var1.isShowing();
   }

   public boolean isOverflowReserved() {
      return this.mReserveOverflow;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      this.dismissPopupMenus();
      super.onCloseMenu(var1, var2);
   }

   public void onConfigurationChanged(Configuration var1) {
      if (!this.mMaxItemsSet) {
         this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons();
      }

      if (this.mMenu != null) {
         this.mMenu.onItemsChanged(true);
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof ActionMenuPresenter.SavedState) {
         ActionMenuPresenter.SavedState var2 = (ActionMenuPresenter.SavedState)var1;
         if (var2.openSubMenuId > 0) {
            MenuItem var3 = this.mMenu.findItem(var2.openSubMenuId);
            if (var3 != null) {
               this.onSubMenuSelected((SubMenuBuilder)var3.getSubMenu());
            }
         }

      }
   }

   public Parcelable onSaveInstanceState() {
      ActionMenuPresenter.SavedState var1 = new ActionMenuPresenter.SavedState();
      var1.openSubMenuId = this.mOpenSubMenuId;
      return var1;
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      if (!var1.hasVisibleItems()) {
         return false;
      } else {
         SubMenuBuilder var6;
         for(var6 = var1; var6.getParentMenu() != this.mMenu; var6 = (SubMenuBuilder)var6.getParentMenu()) {
         }

         View var8 = this.findViewForItem(var6.getItem());
         if (var8 == null) {
            return false;
         } else {
            this.mOpenSubMenuId = var1.getItem().getItemId();
            boolean var5 = false;
            int var3 = var1.size();
            int var2 = 0;

            boolean var4;
            while(true) {
               var4 = var5;
               if (var2 >= var3) {
                  break;
               }

               MenuItem var7 = var1.getItem(var2);
               if (var7.isVisible() && var7.getIcon() != null) {
                  var4 = true;
                  break;
               }

               ++var2;
            }

            this.mActionButtonPopup = new ActionMenuPresenter.ActionButtonSubmenu(this.mContext, var1, var8);
            this.mActionButtonPopup.setForceShowIcon(var4);
            this.mActionButtonPopup.show();
            super.onSubMenuSelected(var1);
            return true;
         }
      }
   }

   public void onSubUiVisibilityChanged(boolean var1) {
      if (var1) {
         super.onSubMenuSelected((SubMenuBuilder)null);
      } else {
         if (this.mMenu != null) {
            this.mMenu.close(false);
         }

      }
   }

   public void setExpandedActionViewsExclusive(boolean var1) {
      this.mExpandedActionViewsExclusive = var1;
   }

   public void setItemLimit(int var1) {
      this.mMaxItems = var1;
      this.mMaxItemsSet = true;
   }

   public void setMenuView(ActionMenuView var1) {
      this.mMenuView = var1;
      var1.initialize(this.mMenu);
   }

   public void setOverflowIcon(Drawable var1) {
      ActionMenuPresenter.OverflowMenuButton var2 = this.mOverflowButton;
      if (var2 != null) {
         var2.setImageDrawable(var1);
      } else {
         this.mPendingOverflowIconSet = true;
         this.mPendingOverflowIcon = var1;
      }
   }

   public void setReserveOverflow(boolean var1) {
      this.mReserveOverflow = var1;
      this.mReserveOverflowSet = true;
   }

   public void setWidthLimit(int var1, boolean var2) {
      this.mWidthLimit = var1;
      this.mStrictWidthLimit = var2;
      this.mWidthLimitSet = true;
   }

   public boolean shouldIncludeItem(int var1, MenuItemImpl var2) {
      return var2.isActionButton();
   }

   public boolean showOverflowMenu() {
      if (this.mReserveOverflow && !this.isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
         this.mPostedOpenRunnable = new ActionMenuPresenter.OpenOverflowRunnable(new ActionMenuPresenter.OverflowPopup(this.mContext, this.mMenu, this.mOverflowButton, true));
         ((View)this.mMenuView).post(this.mPostedOpenRunnable);
         super.onSubMenuSelected((SubMenuBuilder)null);
         return true;
      } else {
         return false;
      }
   }

   public void updateMenuView(boolean var1) {
      super.updateMenuView(var1);
      ((View)this.mMenuView).requestLayout();
      int var3;
      ArrayList var4;
      if (this.mMenu != null) {
         var4 = this.mMenu.getActionItems();
         var3 = var4.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            ActionProvider var5 = ((MenuItemImpl)var4.get(var2)).getSupportActionProvider();
            if (var5 != null) {
               var5.setSubUiVisibilityListener(this);
            }
         }
      }

      if (this.mMenu != null) {
         var4 = this.mMenu.getNonActionItems();
      } else {
         var4 = null;
      }

      boolean var7 = false;
      boolean var6 = var7;
      if (this.mReserveOverflow) {
         var6 = var7;
         if (var4 != null) {
            var3 = var4.size();
            var6 = false;
            if (var3 == 1) {
               var6 = ((MenuItemImpl)var4.get(0)).isActionViewExpanded() ^ true;
            } else if (var3 > 0) {
               var6 = true;
            }
         }
      }

      if (var6) {
         if (this.mOverflowButton == null) {
            this.mOverflowButton = new ActionMenuPresenter.OverflowMenuButton(this.mSystemContext);
         }

         ViewGroup var8 = (ViewGroup)this.mOverflowButton.getParent();
         if (var8 != this.mMenuView) {
            if (var8 != null) {
               var8.removeView(this.mOverflowButton);
            }

            ActionMenuView var9 = (ActionMenuView)this.mMenuView;
            var9.addView(this.mOverflowButton, var9.generateOverflowButtonLayoutParams());
         }
      } else {
         ActionMenuPresenter.OverflowMenuButton var10 = this.mOverflowButton;
         if (var10 != null && var10.getParent() == this.mMenuView) {
            ((ViewGroup)this.mMenuView).removeView(this.mOverflowButton);
         }
      }

      ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
   }

   private class ActionButtonSubmenu extends MenuPopupHelper {
      public ActionButtonSubmenu(Context var2, SubMenuBuilder var3, View var4) {
         super(var2, var3, var4, false, R$attr.actionOverflowMenuStyle);
         if (!((MenuItemImpl)var3.getItem()).isActionButton()) {
            Object var5;
            if (ActionMenuPresenter.this.mOverflowButton == null) {
               var5 = (View)ActionMenuPresenter.this.mMenuView;
            } else {
               var5 = ActionMenuPresenter.this.mOverflowButton;
            }

            this.setAnchorView((View)var5);
         }

         this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
      }

      protected void onDismiss() {
         ActionMenuPresenter var1 = ActionMenuPresenter.this;
         var1.mActionButtonPopup = null;
         var1.mOpenSubMenuId = 0;
         super.onDismiss();
      }
   }

   private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
      ActionMenuPopupCallback() {
      }

      public ShowableListMenu getPopup() {
         return ActionMenuPresenter.this.mActionButtonPopup != null ? ActionMenuPresenter.this.mActionButtonPopup.getPopup() : null;
      }
   }

   private class OpenOverflowRunnable implements Runnable {
      private ActionMenuPresenter.OverflowPopup mPopup;

      public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup var2) {
         this.mPopup = var2;
      }

      public void run() {
         if (ActionMenuPresenter.this.mMenu != null) {
            ActionMenuPresenter.this.mMenu.changeMenuMode();
         }

         View var1 = (View)ActionMenuPresenter.this.mMenuView;
         if (var1 != null && var1.getWindowToken() != null && this.mPopup.tryShow()) {
            ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
         }

         ActionMenuPresenter.this.mPostedOpenRunnable = null;
      }
   }

   private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
      private final float[] mTempPts = new float[2];

      public OverflowMenuButton(Context var2) {
         super(var2, (AttributeSet)null, R$attr.actionOverflowButtonStyle);
         this.setClickable(true);
         this.setFocusable(true);
         this.setVisibility(0);
         this.setEnabled(true);
         TooltipCompat.setTooltipText(this, this.getContentDescription());
         this.setOnTouchListener(new ForwardingListener(this) {
            public ShowableListMenu getPopup() {
               return ActionMenuPresenter.this.mOverflowPopup == null ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup();
            }

            public boolean onForwardingStarted() {
               ActionMenuPresenter.this.showOverflowMenu();
               return true;
            }

            public boolean onForwardingStopped() {
               if (ActionMenuPresenter.this.mPostedOpenRunnable != null) {
                  return false;
               } else {
                  ActionMenuPresenter.this.hideOverflowMenu();
                  return true;
               }
            }
         });
      }

      public boolean needsDividerAfter() {
         return false;
      }

      public boolean needsDividerBefore() {
         return false;
      }

      public boolean performClick() {
         if (super.performClick()) {
            return true;
         } else {
            this.playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
         }
      }

      protected boolean setFrame(int var1, int var2, int var3, int var4) {
         boolean var8 = super.setFrame(var1, var2, var3, var4);
         Drawable var9 = this.getDrawable();
         Drawable var10 = this.getBackground();
         if (var9 != null && var10 != null) {
            int var5 = this.getWidth();
            var2 = this.getHeight();
            var1 = Math.max(var5, var2) / 2;
            int var6 = this.getPaddingLeft();
            int var7 = this.getPaddingRight();
            var3 = this.getPaddingTop();
            var4 = this.getPaddingBottom();
            var5 = (var5 + (var6 - var7)) / 2;
            var2 = (var2 + (var3 - var4)) / 2;
            DrawableCompat.setHotspotBounds(var10, var5 - var1, var2 - var1, var5 + var1, var2 + var1);
         }

         return var8;
      }
   }

   private class OverflowPopup extends MenuPopupHelper {
      public OverflowPopup(Context var2, MenuBuilder var3, View var4, boolean var5) {
         super(var2, var3, var4, var5, R$attr.actionOverflowMenuStyle);
         this.setGravity(8388613);
         this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
      }

      protected void onDismiss() {
         if (ActionMenuPresenter.this.mMenu != null) {
            ActionMenuPresenter.this.mMenu.close();
         }

         ActionMenuPresenter.this.mOverflowPopup = null;
         super.onDismiss();
      }
   }

   private class PopupPresenterCallback implements MenuPresenter.Callback {
      PopupPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         if (var1 instanceof SubMenuBuilder) {
            var1.getRootMenu().close(false);
         }

         MenuPresenter.Callback var3 = ActionMenuPresenter.this.getCallback();
         if (var3 != null) {
            var3.onCloseMenu(var1, var2);
         }

      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         boolean var2 = false;
         if (var1 == null) {
            return false;
         } else {
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)var1).getItem().getItemId();
            MenuPresenter.Callback var3 = ActionMenuPresenter.this.getCallback();
            if (var3 != null) {
               var2 = var3.onOpenSubMenu(var1);
            }

            return var2;
         }
      }
   }

   private static class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public ActionMenuPresenter.SavedState createFromParcel(Parcel var1) {
            return new ActionMenuPresenter.SavedState(var1);
         }

         public ActionMenuPresenter.SavedState[] newArray(int var1) {
            return new ActionMenuPresenter.SavedState[var1];
         }
      };
      public int openSubMenuId;

      SavedState() {
      }

      SavedState(Parcel var1) {
         this.openSubMenuId = var1.readInt();
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.openSubMenuId);
      }
   }
}
