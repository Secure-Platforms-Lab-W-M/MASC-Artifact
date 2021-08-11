package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

abstract class MenuPopup implements ShowableListMenu, MenuPresenter, OnItemClickListener {
   private Rect mEpicenterBounds;

   protected static int measureIndividualMenuWidth(ListAdapter var0, ViewGroup var1, Context var2, int var3) {
      int var5 = 0;
      Object var13 = null;
      int var7 = 0;
      int var9 = MeasureSpec.makeMeasureSpec(0, 0);
      int var10 = MeasureSpec.makeMeasureSpec(0, 0);
      int var11 = var0.getCount();
      int var4 = 0;
      Object var12 = var1;

      for(View var14 = (View)var13; var4 < var11; var12 = var13) {
         int var8 = var0.getItemViewType(var4);
         int var6 = var7;
         if (var8 != var7) {
            var6 = var8;
            var14 = null;
         }

         var13 = var12;
         if (var12 == null) {
            var13 = new FrameLayout(var2);
         }

         var14 = var0.getView(var4, var14, (ViewGroup)var13);
         var14.measure(var9, var10);
         var8 = var14.getMeasuredWidth();
         if (var8 >= var3) {
            return var3;
         }

         var7 = var5;
         if (var8 > var5) {
            var7 = var8;
         }

         ++var4;
         var5 = var7;
         var7 = var6;
      }

      return var5;
   }

   protected static boolean shouldPreserveIconSpacing(MenuBuilder var0) {
      int var2 = var0.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         MenuItem var3 = var0.getItem(var1);
         if (var3.isVisible() && var3.getIcon() != null) {
            return true;
         }
      }

      return false;
   }

   protected static MenuAdapter toMenuAdapter(ListAdapter var0) {
      return var0 instanceof HeaderViewListAdapter ? (MenuAdapter)((HeaderViewListAdapter)var0).getWrappedAdapter() : (MenuAdapter)var0;
   }

   public abstract void addMenu(MenuBuilder var1);

   protected boolean closeMenuOnSubMenuOpened() {
      return true;
   }

   public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public Rect getEpicenterBounds() {
      return this.mEpicenterBounds;
   }

   public int getId() {
      return 0;
   }

   public MenuView getMenuView(ViewGroup var1) {
      throw new UnsupportedOperationException("MenuPopups manage their own views");
   }

   public void initForMenu(Context var1, MenuBuilder var2) {
   }

   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      ListAdapter var7 = (ListAdapter)var1.getAdapter();
      MenuBuilder var6 = toMenuAdapter(var7).mAdapterMenu;
      MenuItem var8 = (MenuItem)var7.getItem(var3);
      byte var9;
      if (this.closeMenuOnSubMenuOpened()) {
         var9 = 0;
      } else {
         var9 = 4;
      }

      var6.performItemAction(var8, this, var9);
   }

   public abstract void setAnchorView(View var1);

   public void setEpicenterBounds(Rect var1) {
      this.mEpicenterBounds = var1;
   }

   public abstract void setForceShowIcon(boolean var1);

   public abstract void setGravity(int var1);

   public abstract void setHorizontalOffset(int var1);

   public abstract void setOnDismissListener(OnDismissListener var1);

   public abstract void setShowTitle(boolean var1);

   public abstract void setVerticalOffset(int var1);
}
