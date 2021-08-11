package android.support.v7.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SubMenuBuilder extends MenuBuilder implements SubMenu {
   private MenuItemImpl mItem;
   private MenuBuilder mParentMenu;

   public SubMenuBuilder(Context var1, MenuBuilder var2, MenuItemImpl var3) {
      super(var1);
      this.mParentMenu = var2;
      this.mItem = var3;
   }

   public boolean collapseItemActionView(MenuItemImpl var1) {
      return this.mParentMenu.collapseItemActionView(var1);
   }

   boolean dispatchMenuItemSelected(MenuBuilder var1, MenuItem var2) {
      return super.dispatchMenuItemSelected(var1, var2) || this.mParentMenu.dispatchMenuItemSelected(var1, var2);
   }

   public boolean expandItemActionView(MenuItemImpl var1) {
      return this.mParentMenu.expandItemActionView(var1);
   }

   public String getActionViewStatesKey() {
      MenuItemImpl var2 = this.mItem;
      int var1;
      if (var2 != null) {
         var1 = var2.getItemId();
      } else {
         var1 = 0;
      }

      if (var1 == 0) {
         return null;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(super.getActionViewStatesKey());
         var3.append(":");
         var3.append(var1);
         return var3.toString();
      }
   }

   public MenuItem getItem() {
      return this.mItem;
   }

   public Menu getParentMenu() {
      return this.mParentMenu;
   }

   public MenuBuilder getRootMenu() {
      return this.mParentMenu.getRootMenu();
   }

   public boolean isQwertyMode() {
      return this.mParentMenu.isQwertyMode();
   }

   public boolean isShortcutsVisible() {
      return this.mParentMenu.isShortcutsVisible();
   }

   public void setCallback(MenuBuilder.Callback var1) {
      this.mParentMenu.setCallback(var1);
   }

   public SubMenu setHeaderIcon(int var1) {
      return (SubMenu)super.setHeaderIconInt(var1);
   }

   public SubMenu setHeaderIcon(Drawable var1) {
      return (SubMenu)super.setHeaderIconInt(var1);
   }

   public SubMenu setHeaderTitle(int var1) {
      return (SubMenu)super.setHeaderTitleInt(var1);
   }

   public SubMenu setHeaderTitle(CharSequence var1) {
      return (SubMenu)super.setHeaderTitleInt(var1);
   }

   public SubMenu setHeaderView(View var1) {
      return (SubMenu)super.setHeaderViewInt(var1);
   }

   public SubMenu setIcon(int var1) {
      this.mItem.setIcon(var1);
      return this;
   }

   public SubMenu setIcon(Drawable var1) {
      this.mItem.setIcon(var1);
      return this;
   }

   public void setQwertyMode(boolean var1) {
      this.mParentMenu.setQwertyMode(var1);
   }

   public void setShortcutsVisible(boolean var1) {
      this.mParentMenu.setShortcutsVisible(var1);
   }
}
