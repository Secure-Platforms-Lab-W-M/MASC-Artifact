package androidx.appcompat.view.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
   MenuBuilder mAdapterMenu;
   private int mExpandedIndex = -1;
   private boolean mForceShowIcon;
   private final LayoutInflater mInflater;
   private final int mItemLayoutRes;
   private final boolean mOverflowOnly;

   public MenuAdapter(MenuBuilder var1, LayoutInflater var2, boolean var3, int var4) {
      this.mOverflowOnly = var3;
      this.mInflater = var2;
      this.mAdapterMenu = var1;
      this.mItemLayoutRes = var4;
      this.findExpandedIndex();
   }

   void findExpandedIndex() {
      MenuItemImpl var3 = this.mAdapterMenu.getExpandedItem();
      if (var3 != null) {
         ArrayList var4 = this.mAdapterMenu.getNonActionItems();
         int var2 = var4.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            if ((MenuItemImpl)var4.get(var1) == var3) {
               this.mExpandedIndex = var1;
               return;
            }
         }
      }

      this.mExpandedIndex = -1;
   }

   public MenuBuilder getAdapterMenu() {
      return this.mAdapterMenu;
   }

   public int getCount() {
      ArrayList var1;
      if (this.mOverflowOnly) {
         var1 = this.mAdapterMenu.getNonActionItems();
      } else {
         var1 = this.mAdapterMenu.getVisibleItems();
      }

      return this.mExpandedIndex < 0 ? var1.size() : var1.size() - 1;
   }

   public boolean getForceShowIcon() {
      return this.mForceShowIcon;
   }

   public MenuItemImpl getItem(int var1) {
      ArrayList var4;
      if (this.mOverflowOnly) {
         var4 = this.mAdapterMenu.getNonActionItems();
      } else {
         var4 = this.mAdapterMenu.getVisibleItems();
      }

      int var3 = this.mExpandedIndex;
      int var2 = var1;
      if (var3 >= 0) {
         var2 = var1;
         if (var1 >= var3) {
            var2 = var1 + 1;
         }
      }

      return (MenuItemImpl)var4.get(var2);
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      View var7 = var2;
      if (var2 == null) {
         var7 = this.mInflater.inflate(this.mItemLayoutRes, var3, false);
      }

      int var5 = this.getItem(var1).getGroupId();
      int var4;
      if (var1 - 1 >= 0) {
         var4 = this.getItem(var1 - 1).getGroupId();
      } else {
         var4 = var5;
      }

      ListMenuItemView var8 = (ListMenuItemView)var7;
      boolean var6;
      if (this.mAdapterMenu.isGroupDividerEnabled() && var5 != var4) {
         var6 = true;
      } else {
         var6 = false;
      }

      var8.setGroupDividerEnabled(var6);
      MenuView.ItemView var9 = (MenuView.ItemView)var7;
      if (this.mForceShowIcon) {
         ((ListMenuItemView)var7).setForceShowIcon(true);
      }

      var9.initialize(this.getItem(var1), 0);
      return var7;
   }

   public void notifyDataSetChanged() {
      this.findExpandedIndex();
      super.notifyDataSetChanged();
   }

   public void setForceShowIcon(boolean var1) {
      this.mForceShowIcon = var1;
   }
}
