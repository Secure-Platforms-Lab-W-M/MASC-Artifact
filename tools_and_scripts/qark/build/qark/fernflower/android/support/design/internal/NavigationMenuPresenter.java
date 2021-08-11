package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.design.R$dimen;
import android.support.design.R$layout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class NavigationMenuPresenter implements MenuPresenter {
   private static final String STATE_ADAPTER = "android:menu:adapter";
   private static final String STATE_HEADER = "android:menu:header";
   private static final String STATE_HIERARCHY = "android:menu:list";
   NavigationMenuPresenter.NavigationMenuAdapter mAdapter;
   private MenuPresenter.Callback mCallback;
   LinearLayout mHeaderLayout;
   ColorStateList mIconTintList;
   private int mId;
   Drawable mItemBackground;
   LayoutInflater mLayoutInflater;
   MenuBuilder mMenu;
   private NavigationMenuView mMenuView;
   final OnClickListener mOnClickListener = new OnClickListener() {
      public void onClick(View var1) {
         NavigationMenuItemView var3 = (NavigationMenuItemView)var1;
         NavigationMenuPresenter.this.setUpdateSuspended(true);
         MenuItemImpl var4 = var3.getItemData();
         boolean var2 = NavigationMenuPresenter.this.mMenu.performItemAction(var4, NavigationMenuPresenter.this, 0);
         if (var4 != null && var4.isCheckable() && var2) {
            NavigationMenuPresenter.this.mAdapter.setCheckedItem(var4);
         }

         NavigationMenuPresenter.this.setUpdateSuspended(false);
         NavigationMenuPresenter.this.updateMenuView(false);
      }
   };
   int mPaddingSeparator;
   private int mPaddingTopDefault;
   int mTextAppearance;
   boolean mTextAppearanceSet;
   ColorStateList mTextColor;

   public void addHeaderView(@NonNull View var1) {
      this.mHeaderLayout.addView(var1);
      NavigationMenuView var2 = this.mMenuView;
      var2.setPadding(0, 0, 0, var2.getPaddingBottom());
   }

   public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public void dispatchApplyWindowInsets(WindowInsetsCompat var1) {
      int var2 = var1.getSystemWindowInsetTop();
      if (this.mPaddingTopDefault != var2) {
         this.mPaddingTopDefault = var2;
         if (this.mHeaderLayout.getChildCount() == 0) {
            NavigationMenuView var3 = this.mMenuView;
            var3.setPadding(0, this.mPaddingTopDefault, 0, var3.getPaddingBottom());
         }
      }

      ViewCompat.dispatchApplyWindowInsets(this.mHeaderLayout, var1);
   }

   public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public boolean flagActionItems() {
      return false;
   }

   public int getHeaderCount() {
      return this.mHeaderLayout.getChildCount();
   }

   public View getHeaderView(int var1) {
      return this.mHeaderLayout.getChildAt(var1);
   }

   public int getId() {
      return this.mId;
   }

   @Nullable
   public Drawable getItemBackground() {
      return this.mItemBackground;
   }

   @Nullable
   public ColorStateList getItemTextColor() {
      return this.mTextColor;
   }

   @Nullable
   public ColorStateList getItemTintList() {
      return this.mIconTintList;
   }

   public MenuView getMenuView(ViewGroup var1) {
      if (this.mMenuView == null) {
         this.mMenuView = (NavigationMenuView)this.mLayoutInflater.inflate(R$layout.design_navigation_menu, var1, false);
         if (this.mAdapter == null) {
            this.mAdapter = new NavigationMenuPresenter.NavigationMenuAdapter();
         }

         this.mHeaderLayout = (LinearLayout)this.mLayoutInflater.inflate(R$layout.design_navigation_item_header, this.mMenuView, false);
         this.mMenuView.setAdapter(this.mAdapter);
      }

      return this.mMenuView;
   }

   public View inflateHeaderView(@LayoutRes int var1) {
      View var2 = this.mLayoutInflater.inflate(var1, this.mHeaderLayout, false);
      this.addHeaderView(var2);
      return var2;
   }

   public void initForMenu(Context var1, MenuBuilder var2) {
      this.mLayoutInflater = LayoutInflater.from(var1);
      this.mMenu = var2;
      this.mPaddingSeparator = var1.getResources().getDimensionPixelOffset(R$dimen.design_navigation_separator_vertical_padding);
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      MenuPresenter.Callback var3 = this.mCallback;
      if (var3 != null) {
         var3.onCloseMenu(var1, var2);
      }
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof Bundle) {
         Bundle var3 = (Bundle)var1;
         SparseArray var2 = var3.getSparseParcelableArray("android:menu:list");
         if (var2 != null) {
            this.mMenuView.restoreHierarchyState(var2);
         }

         Bundle var5 = var3.getBundle("android:menu:adapter");
         if (var5 != null) {
            this.mAdapter.restoreInstanceState(var5);
         }

         SparseArray var4 = var3.getSparseParcelableArray("android:menu:header");
         if (var4 != null) {
            this.mHeaderLayout.restoreHierarchyState(var4);
         }
      }
   }

   public Parcelable onSaveInstanceState() {
      if (VERSION.SDK_INT >= 11) {
         Bundle var1 = new Bundle();
         SparseArray var2;
         if (this.mMenuView != null) {
            var2 = new SparseArray();
            this.mMenuView.saveHierarchyState(var2);
            var1.putSparseParcelableArray("android:menu:list", var2);
         }

         NavigationMenuPresenter.NavigationMenuAdapter var3 = this.mAdapter;
         if (var3 != null) {
            var1.putBundle("android:menu:adapter", var3.createInstanceState());
         }

         if (this.mHeaderLayout != null) {
            var2 = new SparseArray();
            this.mHeaderLayout.saveHierarchyState(var2);
            var1.putSparseParcelableArray("android:menu:header", var2);
            return var1;
         } else {
            return var1;
         }
      } else {
         return null;
      }
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      return false;
   }

   public void removeHeaderView(@NonNull View var1) {
      this.mHeaderLayout.removeView(var1);
      if (this.mHeaderLayout.getChildCount() == 0) {
         NavigationMenuView var2 = this.mMenuView;
         var2.setPadding(0, this.mPaddingTopDefault, 0, var2.getPaddingBottom());
      }
   }

   public void setCallback(MenuPresenter.Callback var1) {
      this.mCallback = var1;
   }

   public void setCheckedItem(MenuItemImpl var1) {
      this.mAdapter.setCheckedItem(var1);
   }

   public void setId(int var1) {
      this.mId = var1;
   }

   public void setItemBackground(@Nullable Drawable var1) {
      this.mItemBackground = var1;
      this.updateMenuView(false);
   }

   public void setItemIconTintList(@Nullable ColorStateList var1) {
      this.mIconTintList = var1;
      this.updateMenuView(false);
   }

   public void setItemTextAppearance(@StyleRes int var1) {
      this.mTextAppearance = var1;
      this.mTextAppearanceSet = true;
      this.updateMenuView(false);
   }

   public void setItemTextColor(@Nullable ColorStateList var1) {
      this.mTextColor = var1;
      this.updateMenuView(false);
   }

   public void setUpdateSuspended(boolean var1) {
      NavigationMenuPresenter.NavigationMenuAdapter var2 = this.mAdapter;
      if (var2 != null) {
         var2.setUpdateSuspended(var1);
      }
   }

   public void updateMenuView(boolean var1) {
      NavigationMenuPresenter.NavigationMenuAdapter var2 = this.mAdapter;
      if (var2 != null) {
         var2.update();
      }
   }

   private static class HeaderViewHolder extends NavigationMenuPresenter.ViewHolder {
      public HeaderViewHolder(View var1) {
         super(var1);
      }
   }

   private class NavigationMenuAdapter extends RecyclerView.Adapter {
      private static final String STATE_ACTION_VIEWS = "android:menu:action_views";
      private static final String STATE_CHECKED_ITEM = "android:menu:checked";
      private static final int VIEW_TYPE_HEADER = 3;
      private static final int VIEW_TYPE_NORMAL = 0;
      private static final int VIEW_TYPE_SEPARATOR = 2;
      private static final int VIEW_TYPE_SUBHEADER = 1;
      private MenuItemImpl mCheckedItem;
      private final ArrayList mItems = new ArrayList();
      private boolean mUpdateSuspended;

      NavigationMenuAdapter() {
         this.prepareMenuItems();
      }

      private void appendTransparentIconIfMissing(int var1, int var2) {
         while(var1 < var2) {
            ((NavigationMenuPresenter.NavigationMenuTextItem)this.mItems.get(var1)).needsEmptyIcon = true;
            ++var1;
         }

      }

      private void prepareMenuItems() {
         if (!this.mUpdateSuspended) {
            this.mUpdateSuspended = true;
            this.mItems.clear();
            this.mItems.add(new NavigationMenuPresenter.NavigationMenuHeaderItem());
            int var4 = -1;
            int var1 = 0;
            boolean var9 = false;
            int var3 = 0;
            int var6 = NavigationMenuPresenter.this.mMenu.getVisibleItems().size();

            while(true) {
               boolean var10 = false;
               if (var3 >= var6) {
                  this.mUpdateSuspended = false;
                  return;
               }

               MenuItemImpl var11 = (MenuItemImpl)NavigationMenuPresenter.this.mMenu.getVisibleItems().get(var3);
               if (var11.isChecked()) {
                  this.setCheckedItem(var11);
               }

               if (var11.isCheckable()) {
                  var11.setExclusiveCheckable(false);
               }

               if (!var11.hasSubMenu()) {
                  int var14 = var11.getGroupId();
                  if (var14 != var4) {
                     var1 = this.mItems.size();
                     var9 = var10;
                     if (var11.getIcon() != null) {
                        var9 = true;
                     }

                     if (var3 != 0) {
                        ++var1;
                        this.mItems.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, NavigationMenuPresenter.this.mPaddingSeparator));
                     }
                  } else if (!var9 && var11.getIcon() != null) {
                     var9 = true;
                     this.appendTransparentIconIfMissing(var1, this.mItems.size());
                  }

                  NavigationMenuPresenter.NavigationMenuTextItem var15 = new NavigationMenuPresenter.NavigationMenuTextItem(var11);
                  var15.needsEmptyIcon = var9;
                  this.mItems.add(var15);
                  var4 = var14;
               } else {
                  SubMenu var12 = var11.getSubMenu();
                  if (var12.hasVisibleItems()) {
                     if (var3 != 0) {
                        this.mItems.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, 0));
                     }

                     this.mItems.add(new NavigationMenuPresenter.NavigationMenuTextItem(var11));
                     boolean var2 = false;
                     int var7 = this.mItems.size();
                     int var5 = 0;

                     for(int var8 = var12.size(); var5 < var8; ++var5) {
                        MenuItemImpl var13 = (MenuItemImpl)var12.getItem(var5);
                        if (var13.isVisible()) {
                           if (!var2 && var13.getIcon() != null) {
                              var2 = true;
                           }

                           if (var13.isCheckable()) {
                              var13.setExclusiveCheckable(false);
                           }

                           if (var11.isChecked()) {
                              this.setCheckedItem(var11);
                           }

                           this.mItems.add(new NavigationMenuPresenter.NavigationMenuTextItem(var13));
                        }
                     }

                     if (var2) {
                        this.appendTransparentIconIfMissing(var7, this.mItems.size());
                     }
                  }
               }

               ++var3;
            }
         }
      }

      public Bundle createInstanceState() {
         Bundle var4 = new Bundle();
         MenuItemImpl var3 = this.mCheckedItem;
         if (var3 != null) {
            var4.putInt("android:menu:checked", var3.getItemId());
         }

         SparseArray var5 = new SparseArray();
         int var1 = 0;

         for(int var2 = this.mItems.size(); var1 < var2; ++var1) {
            NavigationMenuPresenter.NavigationMenuItem var8 = (NavigationMenuPresenter.NavigationMenuItem)this.mItems.get(var1);
            if (var8 instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
               MenuItemImpl var6 = ((NavigationMenuPresenter.NavigationMenuTextItem)var8).getMenuItem();
               View var9;
               if (var6 != null) {
                  var9 = var6.getActionView();
               } else {
                  var9 = null;
               }

               if (var9 != null) {
                  ParcelableSparseArray var7 = new ParcelableSparseArray();
                  var9.saveHierarchyState(var7);
                  var5.put(var6.getItemId(), var7);
               }
            }
         }

         var4.putSparseParcelableArray("android:menu:action_views", var5);
         return var4;
      }

      public int getItemCount() {
         return this.mItems.size();
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         NavigationMenuPresenter.NavigationMenuItem var2 = (NavigationMenuPresenter.NavigationMenuItem)this.mItems.get(var1);
         if (var2 instanceof NavigationMenuPresenter.NavigationMenuSeparatorItem) {
            return 2;
         } else if (var2 instanceof NavigationMenuPresenter.NavigationMenuHeaderItem) {
            return 3;
         } else if (var2 instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
            return ((NavigationMenuPresenter.NavigationMenuTextItem)var2).getMenuItem().hasSubMenu() ? 1 : 0;
         } else {
            throw new RuntimeException("Unknown item type.");
         }
      }

      public void onBindViewHolder(NavigationMenuPresenter.ViewHolder var1, int var2) {
         switch(this.getItemViewType(var2)) {
         case 0:
            NavigationMenuItemView var6 = (NavigationMenuItemView)var1.itemView;
            var6.setIconTintList(NavigationMenuPresenter.this.mIconTintList);
            if (NavigationMenuPresenter.this.mTextAppearanceSet) {
               var6.setTextAppearance(NavigationMenuPresenter.this.mTextAppearance);
            }

            if (NavigationMenuPresenter.this.mTextColor != null) {
               var6.setTextColor(NavigationMenuPresenter.this.mTextColor);
            }

            Drawable var4;
            if (NavigationMenuPresenter.this.mItemBackground != null) {
               var4 = NavigationMenuPresenter.this.mItemBackground.getConstantState().newDrawable();
            } else {
               var4 = null;
            }

            ViewCompat.setBackground(var6, var4);
            NavigationMenuPresenter.NavigationMenuTextItem var5 = (NavigationMenuPresenter.NavigationMenuTextItem)this.mItems.get(var2);
            var6.setNeedsEmptyIcon(var5.needsEmptyIcon);
            var6.initialize(var5.getMenuItem(), 0);
            return;
         case 1:
            ((TextView)var1.itemView).setText(((NavigationMenuPresenter.NavigationMenuTextItem)this.mItems.get(var2)).getMenuItem().getTitle());
            return;
         case 2:
            NavigationMenuPresenter.NavigationMenuSeparatorItem var3 = (NavigationMenuPresenter.NavigationMenuSeparatorItem)this.mItems.get(var2);
            var1.itemView.setPadding(0, var3.getPaddingTop(), 0, var3.getPaddingBottom());
            return;
         default:
         }
      }

      public NavigationMenuPresenter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         switch(var2) {
         case 0:
            return new NavigationMenuPresenter.NormalViewHolder(NavigationMenuPresenter.this.mLayoutInflater, var1, NavigationMenuPresenter.this.mOnClickListener);
         case 1:
            return new NavigationMenuPresenter.SubheaderViewHolder(NavigationMenuPresenter.this.mLayoutInflater, var1);
         case 2:
            return new NavigationMenuPresenter.SeparatorViewHolder(NavigationMenuPresenter.this.mLayoutInflater, var1);
         case 3:
            return new NavigationMenuPresenter.HeaderViewHolder(NavigationMenuPresenter.this.mHeaderLayout);
         default:
            return null;
         }
      }

      public void onViewRecycled(NavigationMenuPresenter.ViewHolder var1) {
         if (var1 instanceof NavigationMenuPresenter.NormalViewHolder) {
            ((NavigationMenuItemView)var1.itemView).recycle();
         }
      }

      public void restoreInstanceState(Bundle var1) {
         int var3 = var1.getInt("android:menu:checked", 0);
         int var2;
         NavigationMenuPresenter.NavigationMenuItem var5;
         if (var3 != 0) {
            this.mUpdateSuspended = true;
            var2 = 0;

            for(int var4 = this.mItems.size(); var2 < var4; ++var2) {
               var5 = (NavigationMenuPresenter.NavigationMenuItem)this.mItems.get(var2);
               if (var5 instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
                  MenuItemImpl var8 = ((NavigationMenuPresenter.NavigationMenuTextItem)var5).getMenuItem();
                  if (var8 != null && var8.getItemId() == var3) {
                     this.setCheckedItem(var8);
                     break;
                  }
               }
            }

            this.mUpdateSuspended = false;
            this.prepareMenuItems();
         }

         SparseArray var7 = var1.getSparseParcelableArray("android:menu:action_views");
         if (var7 != null) {
            var2 = 0;

            for(var3 = this.mItems.size(); var2 < var3; ++var2) {
               var5 = (NavigationMenuPresenter.NavigationMenuItem)this.mItems.get(var2);
               if (var5 instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
                  MenuItemImpl var6 = ((NavigationMenuPresenter.NavigationMenuTextItem)var5).getMenuItem();
                  if (var6 != null) {
                     View var9 = var6.getActionView();
                     if (var9 != null) {
                        ParcelableSparseArray var10 = (ParcelableSparseArray)var7.get(var6.getItemId());
                        if (var10 != null) {
                           var9.restoreHierarchyState(var10);
                        }
                     }
                  }
               }
            }

         }
      }

      public void setCheckedItem(MenuItemImpl var1) {
         if (this.mCheckedItem != var1) {
            if (var1.isCheckable()) {
               MenuItemImpl var2 = this.mCheckedItem;
               if (var2 != null) {
                  var2.setChecked(false);
               }

               this.mCheckedItem = var1;
               var1.setChecked(true);
            }
         }
      }

      public void setUpdateSuspended(boolean var1) {
         this.mUpdateSuspended = var1;
      }

      public void update() {
         this.prepareMenuItems();
         this.notifyDataSetChanged();
      }
   }

   private static class NavigationMenuHeaderItem implements NavigationMenuPresenter.NavigationMenuItem {
      NavigationMenuHeaderItem() {
      }
   }

   private interface NavigationMenuItem {
   }

   private static class NavigationMenuSeparatorItem implements NavigationMenuPresenter.NavigationMenuItem {
      private final int mPaddingBottom;
      private final int mPaddingTop;

      public NavigationMenuSeparatorItem(int var1, int var2) {
         this.mPaddingTop = var1;
         this.mPaddingBottom = var2;
      }

      public int getPaddingBottom() {
         return this.mPaddingBottom;
      }

      public int getPaddingTop() {
         return this.mPaddingTop;
      }
   }

   private static class NavigationMenuTextItem implements NavigationMenuPresenter.NavigationMenuItem {
      private final MenuItemImpl mMenuItem;
      boolean needsEmptyIcon;

      NavigationMenuTextItem(MenuItemImpl var1) {
         this.mMenuItem = var1;
      }

      public MenuItemImpl getMenuItem() {
         return this.mMenuItem;
      }
   }

   private static class NormalViewHolder extends NavigationMenuPresenter.ViewHolder {
      public NormalViewHolder(LayoutInflater var1, ViewGroup var2, OnClickListener var3) {
         super(var1.inflate(R$layout.design_navigation_item, var2, false));
         this.itemView.setOnClickListener(var3);
      }
   }

   private static class SeparatorViewHolder extends NavigationMenuPresenter.ViewHolder {
      public SeparatorViewHolder(LayoutInflater var1, ViewGroup var2) {
         super(var1.inflate(R$layout.design_navigation_item_separator, var2, false));
      }
   }

   private static class SubheaderViewHolder extends NavigationMenuPresenter.ViewHolder {
      public SubheaderViewHolder(LayoutInflater var1, ViewGroup var2) {
         super(var1.inflate(R$layout.design_navigation_item_subheader, var2, false));
      }
   }

   private abstract static class ViewHolder extends RecyclerView.ViewHolder {
      public ViewHolder(View var1) {
         super(var1);
      }
   }
}
