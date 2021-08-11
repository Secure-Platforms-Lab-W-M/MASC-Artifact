package com.google.android.material.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.google.android.material.R.dimen;
import com.google.android.material.R.layout;
import java.util.ArrayList;

public class NavigationMenuPresenter implements MenuPresenter {
   private static final String STATE_ADAPTER = "android:menu:adapter";
   private static final String STATE_HEADER = "android:menu:header";
   private static final String STATE_HIERARCHY = "android:menu:list";
   NavigationMenuPresenter.NavigationMenuAdapter adapter;
   private MenuPresenter.Callback callback;
   boolean hasCustomItemIconSize;
   LinearLayout headerLayout;
   ColorStateList iconTintList;
   // $FF: renamed from: id int
   private int field_236;
   boolean isBehindStatusBar = true;
   Drawable itemBackground;
   int itemHorizontalPadding;
   int itemIconPadding;
   int itemIconSize;
   private int itemMaxLines;
   LayoutInflater layoutInflater;
   MenuBuilder menu;
   private NavigationMenuView menuView;
   final OnClickListener onClickListener = new OnClickListener() {
      public void onClick(View var1) {
         NavigationMenuItemView var5 = (NavigationMenuItemView)var1;
         NavigationMenuPresenter.this.setUpdateSuspended(true);
         MenuItemImpl var6 = var5.getItemData();
         boolean var4 = NavigationMenuPresenter.this.menu.performItemAction(var6, NavigationMenuPresenter.this, 0);
         boolean var3 = false;
         boolean var2 = var3;
         if (var6 != null) {
            var2 = var3;
            if (var6.isCheckable()) {
               var2 = var3;
               if (var4) {
                  NavigationMenuPresenter.this.adapter.setCheckedItem(var6);
                  var2 = true;
               }
            }
         }

         NavigationMenuPresenter.this.setUpdateSuspended(false);
         if (var2) {
            NavigationMenuPresenter.this.updateMenuView(false);
         }

      }
   };
   private int overScrollMode = -1;
   int paddingSeparator;
   private int paddingTopDefault;
   int textAppearance;
   boolean textAppearanceSet;
   ColorStateList textColor;

   private void updateTopPadding() {
      byte var2 = 0;
      int var1 = var2;
      if (this.headerLayout.getChildCount() == 0) {
         var1 = var2;
         if (this.isBehindStatusBar) {
            var1 = this.paddingTopDefault;
         }
      }

      NavigationMenuView var3 = this.menuView;
      var3.setPadding(0, var1, 0, var3.getPaddingBottom());
   }

   public void addHeaderView(View var1) {
      this.headerLayout.addView(var1);
      NavigationMenuView var2 = this.menuView;
      var2.setPadding(0, 0, 0, var2.getPaddingBottom());
   }

   public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public void dispatchApplyWindowInsets(WindowInsetsCompat var1) {
      int var2 = var1.getSystemWindowInsetTop();
      if (this.paddingTopDefault != var2) {
         this.paddingTopDefault = var2;
         this.updateTopPadding();
      }

      NavigationMenuView var3 = this.menuView;
      var3.setPadding(0, var3.getPaddingTop(), 0, var1.getSystemWindowInsetBottom());
      ViewCompat.dispatchApplyWindowInsets(this.headerLayout, var1);
   }

   public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public boolean flagActionItems() {
      return false;
   }

   public MenuItemImpl getCheckedItem() {
      return this.adapter.getCheckedItem();
   }

   public int getHeaderCount() {
      return this.headerLayout.getChildCount();
   }

   public View getHeaderView(int var1) {
      return this.headerLayout.getChildAt(var1);
   }

   public int getId() {
      return this.field_236;
   }

   public Drawable getItemBackground() {
      return this.itemBackground;
   }

   public int getItemHorizontalPadding() {
      return this.itemHorizontalPadding;
   }

   public int getItemIconPadding() {
      return this.itemIconPadding;
   }

   public int getItemMaxLines() {
      return this.itemMaxLines;
   }

   public ColorStateList getItemTextColor() {
      return this.textColor;
   }

   public ColorStateList getItemTintList() {
      return this.iconTintList;
   }

   public MenuView getMenuView(ViewGroup var1) {
      if (this.menuView == null) {
         NavigationMenuView var3 = (NavigationMenuView)this.layoutInflater.inflate(layout.design_navigation_menu, var1, false);
         this.menuView = var3;
         var3.setAccessibilityDelegateCompat(new NavigationMenuPresenter.NavigationMenuViewAccessibilityDelegate(var3));
         if (this.adapter == null) {
            this.adapter = new NavigationMenuPresenter.NavigationMenuAdapter();
         }

         int var2 = this.overScrollMode;
         if (var2 != -1) {
            this.menuView.setOverScrollMode(var2);
         }

         this.headerLayout = (LinearLayout)this.layoutInflater.inflate(layout.design_navigation_item_header, this.menuView, false);
         this.menuView.setAdapter(this.adapter);
      }

      return this.menuView;
   }

   public View inflateHeaderView(int var1) {
      View var2 = this.layoutInflater.inflate(var1, this.headerLayout, false);
      this.addHeaderView(var2);
      return var2;
   }

   public void initForMenu(Context var1, MenuBuilder var2) {
      this.layoutInflater = LayoutInflater.from(var1);
      this.menu = var2;
      this.paddingSeparator = var1.getResources().getDimensionPixelOffset(dimen.design_navigation_separator_vertical_padding);
   }

   public boolean isBehindStatusBar() {
      return this.isBehindStatusBar;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      MenuPresenter.Callback var3 = this.callback;
      if (var3 != null) {
         var3.onCloseMenu(var1, var2);
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof Bundle) {
         Bundle var3 = (Bundle)var1;
         SparseArray var2 = var3.getSparseParcelableArray("android:menu:list");
         if (var2 != null) {
            this.menuView.restoreHierarchyState(var2);
         }

         Bundle var5 = var3.getBundle("android:menu:adapter");
         if (var5 != null) {
            this.adapter.restoreInstanceState(var5);
         }

         SparseArray var4 = var3.getSparseParcelableArray("android:menu:header");
         if (var4 != null) {
            this.headerLayout.restoreHierarchyState(var4);
         }
      }

   }

   public Parcelable onSaveInstanceState() {
      Bundle var1 = new Bundle();
      SparseArray var2;
      if (this.menuView != null) {
         var2 = new SparseArray();
         this.menuView.saveHierarchyState(var2);
         var1.putSparseParcelableArray("android:menu:list", var2);
      }

      NavigationMenuPresenter.NavigationMenuAdapter var3 = this.adapter;
      if (var3 != null) {
         var1.putBundle("android:menu:adapter", var3.createInstanceState());
      }

      if (this.headerLayout != null) {
         var2 = new SparseArray();
         this.headerLayout.saveHierarchyState(var2);
         var1.putSparseParcelableArray("android:menu:header", var2);
      }

      return var1;
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      return false;
   }

   public void removeHeaderView(View var1) {
      this.headerLayout.removeView(var1);
      if (this.headerLayout.getChildCount() == 0) {
         NavigationMenuView var2 = this.menuView;
         var2.setPadding(0, this.paddingTopDefault, 0, var2.getPaddingBottom());
      }

   }

   public void setBehindStatusBar(boolean var1) {
      if (this.isBehindStatusBar != var1) {
         this.isBehindStatusBar = var1;
         this.updateTopPadding();
      }

   }

   public void setCallback(MenuPresenter.Callback var1) {
      this.callback = var1;
   }

   public void setCheckedItem(MenuItemImpl var1) {
      this.adapter.setCheckedItem(var1);
   }

   public void setId(int var1) {
      this.field_236 = var1;
   }

   public void setItemBackground(Drawable var1) {
      this.itemBackground = var1;
      this.updateMenuView(false);
   }

   public void setItemHorizontalPadding(int var1) {
      this.itemHorizontalPadding = var1;
      this.updateMenuView(false);
   }

   public void setItemIconPadding(int var1) {
      this.itemIconPadding = var1;
      this.updateMenuView(false);
   }

   public void setItemIconSize(int var1) {
      if (this.itemIconSize != var1) {
         this.itemIconSize = var1;
         this.hasCustomItemIconSize = true;
         this.updateMenuView(false);
      }

   }

   public void setItemIconTintList(ColorStateList var1) {
      this.iconTintList = var1;
      this.updateMenuView(false);
   }

   public void setItemMaxLines(int var1) {
      this.itemMaxLines = var1;
      this.updateMenuView(false);
   }

   public void setItemTextAppearance(int var1) {
      this.textAppearance = var1;
      this.textAppearanceSet = true;
      this.updateMenuView(false);
   }

   public void setItemTextColor(ColorStateList var1) {
      this.textColor = var1;
      this.updateMenuView(false);
   }

   public void setOverScrollMode(int var1) {
      this.overScrollMode = var1;
      NavigationMenuView var2 = this.menuView;
      if (var2 != null) {
         var2.setOverScrollMode(var1);
      }

   }

   public void setUpdateSuspended(boolean var1) {
      NavigationMenuPresenter.NavigationMenuAdapter var2 = this.adapter;
      if (var2 != null) {
         var2.setUpdateSuspended(var1);
      }

   }

   public void updateMenuView(boolean var1) {
      NavigationMenuPresenter.NavigationMenuAdapter var2 = this.adapter;
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
      private MenuItemImpl checkedItem;
      private final ArrayList items = new ArrayList();
      private boolean updateSuspended;

      NavigationMenuAdapter() {
         this.prepareMenuItems();
      }

      private void appendTransparentIconIfMissing(int var1, int var2) {
         while(var1 < var2) {
            ((NavigationMenuPresenter.NavigationMenuTextItem)this.items.get(var1)).needsEmptyIcon = true;
            ++var1;
         }

      }

      private void prepareMenuItems() {
         if (!this.updateSuspended) {
            this.updateSuspended = true;
            this.items.clear();
            this.items.add(new NavigationMenuPresenter.NavigationMenuHeaderItem());
            int var5 = -1;
            int var2 = 0;
            boolean var11 = false;
            int var4 = 0;
            int var7 = NavigationMenuPresenter.this.menu.getVisibleItems().size();

            while(true) {
               boolean var10 = false;
               if (var4 >= var7) {
                  this.updateSuspended = false;
                  return;
               }

               MenuItemImpl var12 = (MenuItemImpl)NavigationMenuPresenter.this.menu.getVisibleItems().get(var4);
               if (var12.isChecked()) {
                  this.setCheckedItem(var12);
               }

               if (var12.isCheckable()) {
                  var12.setExclusiveCheckable(false);
               }

               int var15;
               if (!var12.hasSubMenu()) {
                  int var16 = var12.getGroupId();
                  if (var16 != var5) {
                     var2 = this.items.size();
                     if (var12.getIcon() != null) {
                        var10 = true;
                     }

                     var11 = var10;
                     var15 = var2;
                     var10 = var10;
                     if (var4 != 0) {
                        var15 = var2 + 1;
                        this.items.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, NavigationMenuPresenter.this.paddingSeparator));
                        var10 = var11;
                     }
                  } else {
                     var15 = var2;
                     var10 = var11;
                     if (!var11) {
                        var15 = var2;
                        var10 = var11;
                        if (var12.getIcon() != null) {
                           var10 = true;
                           this.appendTransparentIconIfMissing(var2, this.items.size());
                           var15 = var2;
                        }
                     }
                  }

                  NavigationMenuPresenter.NavigationMenuTextItem var17 = new NavigationMenuPresenter.NavigationMenuTextItem(var12);
                  var17.needsEmptyIcon = var10;
                  this.items.add(var17);
                  var5 = var16;
               } else {
                  SubMenu var13 = var12.getSubMenu();
                  if (var13.hasVisibleItems()) {
                     if (var4 != 0) {
                        this.items.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, 0));
                     }

                     this.items.add(new NavigationMenuPresenter.NavigationMenuTextItem(var12));
                     boolean var1 = false;
                     int var8 = this.items.size();
                     int var6 = 0;

                     boolean var3;
                     for(int var9 = var13.size(); var6 < var9; var1 = var3) {
                        MenuItemImpl var14 = (MenuItemImpl)var13.getItem(var6);
                        var3 = var1;
                        if (var14.isVisible()) {
                           var3 = var1;
                           if (!var1) {
                              var3 = var1;
                              if (var14.getIcon() != null) {
                                 var3 = true;
                              }
                           }

                           if (var14.isCheckable()) {
                              var14.setExclusiveCheckable(false);
                           }

                           if (var12.isChecked()) {
                              this.setCheckedItem(var12);
                           }

                           this.items.add(new NavigationMenuPresenter.NavigationMenuTextItem(var14));
                        }

                        ++var6;
                     }

                     if (var1) {
                        this.appendTransparentIconIfMissing(var8, this.items.size());
                     }
                  }

                  var15 = var2;
                  var10 = var11;
               }

               ++var4;
               var2 = var15;
               var11 = var10;
            }
         }
      }

      public Bundle createInstanceState() {
         Bundle var4 = new Bundle();
         MenuItemImpl var3 = this.checkedItem;
         if (var3 != null) {
            var4.putInt("android:menu:checked", var3.getItemId());
         }

         SparseArray var5 = new SparseArray();
         int var1 = 0;

         for(int var2 = this.items.size(); var1 < var2; ++var1) {
            NavigationMenuPresenter.NavigationMenuItem var8 = (NavigationMenuPresenter.NavigationMenuItem)this.items.get(var1);
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

      public MenuItemImpl getCheckedItem() {
         return this.checkedItem;
      }

      public int getItemCount() {
         return this.items.size();
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         NavigationMenuPresenter.NavigationMenuItem var2 = (NavigationMenuPresenter.NavigationMenuItem)this.items.get(var1);
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

      int getRowCount() {
         int var1;
         if (NavigationMenuPresenter.this.headerLayout.getChildCount() == 0) {
            var1 = 0;
         } else {
            var1 = 1;
         }

         int var3;
         for(int var2 = 0; var2 < NavigationMenuPresenter.this.adapter.getItemCount(); var1 = var3) {
            var3 = var1;
            if (NavigationMenuPresenter.this.adapter.getItemViewType(var2) == 0) {
               var3 = var1 + 1;
            }

            ++var2;
         }

         return var1;
      }

      public void onBindViewHolder(NavigationMenuPresenter.ViewHolder var1, int var2) {
         int var3 = this.getItemViewType(var2);
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  NavigationMenuPresenter.NavigationMenuSeparatorItem var7 = (NavigationMenuPresenter.NavigationMenuSeparatorItem)this.items.get(var2);
                  var1.itemView.setPadding(0, var7.getPaddingTop(), 0, var7.getPaddingBottom());
               }
            } else {
               ((TextView)var1.itemView).setText(((NavigationMenuPresenter.NavigationMenuTextItem)this.items.get(var2)).getMenuItem().getTitle());
            }
         } else {
            NavigationMenuItemView var4 = (NavigationMenuItemView)var1.itemView;
            var4.setIconTintList(NavigationMenuPresenter.this.iconTintList);
            if (NavigationMenuPresenter.this.textAppearanceSet) {
               var4.setTextAppearance(NavigationMenuPresenter.this.textAppearance);
            }

            if (NavigationMenuPresenter.this.textColor != null) {
               var4.setTextColor(NavigationMenuPresenter.this.textColor);
            }

            Drawable var5;
            if (NavigationMenuPresenter.this.itemBackground != null) {
               var5 = NavigationMenuPresenter.this.itemBackground.getConstantState().newDrawable();
            } else {
               var5 = null;
            }

            ViewCompat.setBackground(var4, var5);
            NavigationMenuPresenter.NavigationMenuTextItem var6 = (NavigationMenuPresenter.NavigationMenuTextItem)this.items.get(var2);
            var4.setNeedsEmptyIcon(var6.needsEmptyIcon);
            var4.setHorizontalPadding(NavigationMenuPresenter.this.itemHorizontalPadding);
            var4.setIconPadding(NavigationMenuPresenter.this.itemIconPadding);
            if (NavigationMenuPresenter.this.hasCustomItemIconSize) {
               var4.setIconSize(NavigationMenuPresenter.this.itemIconSize);
            }

            var4.setMaxLines(NavigationMenuPresenter.this.itemMaxLines);
            var4.initialize(var6.getMenuItem(), 0);
         }
      }

      public NavigationMenuPresenter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  return var2 != 3 ? null : new NavigationMenuPresenter.HeaderViewHolder(NavigationMenuPresenter.this.headerLayout);
               } else {
                  return new NavigationMenuPresenter.SeparatorViewHolder(NavigationMenuPresenter.this.layoutInflater, var1);
               }
            } else {
               return new NavigationMenuPresenter.SubheaderViewHolder(NavigationMenuPresenter.this.layoutInflater, var1);
            }
         } else {
            return new NavigationMenuPresenter.NormalViewHolder(NavigationMenuPresenter.this.layoutInflater, var1, NavigationMenuPresenter.this.onClickListener);
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
            this.updateSuspended = true;
            var2 = 0;

            for(int var4 = this.items.size(); var2 < var4; ++var2) {
               var5 = (NavigationMenuPresenter.NavigationMenuItem)this.items.get(var2);
               if (var5 instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
                  MenuItemImpl var8 = ((NavigationMenuPresenter.NavigationMenuTextItem)var5).getMenuItem();
                  if (var8 != null && var8.getItemId() == var3) {
                     this.setCheckedItem(var8);
                     break;
                  }
               }
            }

            this.updateSuspended = false;
            this.prepareMenuItems();
         }

         SparseArray var7 = var1.getSparseParcelableArray("android:menu:action_views");
         if (var7 != null) {
            var2 = 0;

            for(var3 = this.items.size(); var2 < var3; ++var2) {
               var5 = (NavigationMenuPresenter.NavigationMenuItem)this.items.get(var2);
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
         if (this.checkedItem != var1) {
            if (var1.isCheckable()) {
               MenuItemImpl var2 = this.checkedItem;
               if (var2 != null) {
                  var2.setChecked(false);
               }

               this.checkedItem = var1;
               var1.setChecked(true);
            }
         }
      }

      public void setUpdateSuspended(boolean var1) {
         this.updateSuspended = var1;
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
      private final int paddingBottom;
      private final int paddingTop;

      public NavigationMenuSeparatorItem(int var1, int var2) {
         this.paddingTop = var1;
         this.paddingBottom = var2;
      }

      public int getPaddingBottom() {
         return this.paddingBottom;
      }

      public int getPaddingTop() {
         return this.paddingTop;
      }
   }

   private static class NavigationMenuTextItem implements NavigationMenuPresenter.NavigationMenuItem {
      private final MenuItemImpl menuItem;
      boolean needsEmptyIcon;

      NavigationMenuTextItem(MenuItemImpl var1) {
         this.menuItem = var1;
      }

      public MenuItemImpl getMenuItem() {
         return this.menuItem;
      }
   }

   private class NavigationMenuViewAccessibilityDelegate extends RecyclerViewAccessibilityDelegate {
      NavigationMenuViewAccessibilityDelegate(RecyclerView var2) {
         super(var2);
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         var2.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(NavigationMenuPresenter.this.adapter.getRowCount(), 0, false));
      }
   }

   private static class NormalViewHolder extends NavigationMenuPresenter.ViewHolder {
      public NormalViewHolder(LayoutInflater var1, ViewGroup var2, OnClickListener var3) {
         super(var1.inflate(layout.design_navigation_item, var2, false));
         this.itemView.setOnClickListener(var3);
      }
   }

   private static class SeparatorViewHolder extends NavigationMenuPresenter.ViewHolder {
      public SeparatorViewHolder(LayoutInflater var1, ViewGroup var2) {
         super(var1.inflate(layout.design_navigation_item_separator, var2, false));
      }
   }

   private static class SubheaderViewHolder extends NavigationMenuPresenter.ViewHolder {
      public SubheaderViewHolder(LayoutInflater var1, ViewGroup var2) {
         super(var1.inflate(layout.design_navigation_item_subheader, var2, false));
      }
   }

   private abstract static class ViewHolder extends RecyclerView.ViewHolder {
      public ViewHolder(View var1) {
         super(var1);
      }
   }
}
