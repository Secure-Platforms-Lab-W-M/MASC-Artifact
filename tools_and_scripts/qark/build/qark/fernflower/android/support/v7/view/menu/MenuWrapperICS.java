package android.support.v7.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.support.v4.internal.view.SupportMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

@RequiresApi(14)
class MenuWrapperICS extends BaseMenuWrapper implements Menu {
   MenuWrapperICS(Context var1, SupportMenu var2) {
      super(var1, var2);
   }

   public MenuItem add(int var1) {
      return this.getMenuItemWrapper(((SupportMenu)this.mWrappedObject).add(var1));
   }

   public MenuItem add(int var1, int var2, int var3, int var4) {
      return this.getMenuItemWrapper(((SupportMenu)this.mWrappedObject).add(var1, var2, var3, var4));
   }

   public MenuItem add(int var1, int var2, int var3, CharSequence var4) {
      return this.getMenuItemWrapper(((SupportMenu)this.mWrappedObject).add(var1, var2, var3, var4));
   }

   public MenuItem add(CharSequence var1) {
      return this.getMenuItemWrapper(((SupportMenu)this.mWrappedObject).add(var1));
   }

   public int addIntentOptions(int var1, int var2, int var3, ComponentName var4, Intent[] var5, Intent var6, int var7, MenuItem[] var8) {
      MenuItem[] var9 = null;
      if (var8 != null) {
         var9 = new MenuItem[var8.length];
      }

      var2 = ((SupportMenu)this.mWrappedObject).addIntentOptions(var1, var2, var3, var4, var5, var6, var7, var9);
      if (var9 != null) {
         var1 = 0;

         for(var3 = var9.length; var1 < var3; ++var1) {
            var8[var1] = this.getMenuItemWrapper(var9[var1]);
         }
      }

      return var2;
   }

   public SubMenu addSubMenu(int var1) {
      return this.getSubMenuWrapper(((SupportMenu)this.mWrappedObject).addSubMenu(var1));
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, int var4) {
      return this.getSubMenuWrapper(((SupportMenu)this.mWrappedObject).addSubMenu(var1, var2, var3, var4));
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, CharSequence var4) {
      return this.getSubMenuWrapper(((SupportMenu)this.mWrappedObject).addSubMenu(var1, var2, var3, var4));
   }

   public SubMenu addSubMenu(CharSequence var1) {
      return this.getSubMenuWrapper(((SupportMenu)this.mWrappedObject).addSubMenu(var1));
   }

   public void clear() {
      this.internalClear();
      ((SupportMenu)this.mWrappedObject).clear();
   }

   public void close() {
      ((SupportMenu)this.mWrappedObject).close();
   }

   public MenuItem findItem(int var1) {
      return this.getMenuItemWrapper(((SupportMenu)this.mWrappedObject).findItem(var1));
   }

   public MenuItem getItem(int var1) {
      return this.getMenuItemWrapper(((SupportMenu)this.mWrappedObject).getItem(var1));
   }

   public boolean hasVisibleItems() {
      return ((SupportMenu)this.mWrappedObject).hasVisibleItems();
   }

   public boolean isShortcutKey(int var1, KeyEvent var2) {
      return ((SupportMenu)this.mWrappedObject).isShortcutKey(var1, var2);
   }

   public boolean performIdentifierAction(int var1, int var2) {
      return ((SupportMenu)this.mWrappedObject).performIdentifierAction(var1, var2);
   }

   public boolean performShortcut(int var1, KeyEvent var2, int var3) {
      return ((SupportMenu)this.mWrappedObject).performShortcut(var1, var2, var3);
   }

   public void removeGroup(int var1) {
      this.internalRemoveGroup(var1);
      ((SupportMenu)this.mWrappedObject).removeGroup(var1);
   }

   public void removeItem(int var1) {
      this.internalRemoveItem(var1);
      ((SupportMenu)this.mWrappedObject).removeItem(var1);
   }

   public void setGroupCheckable(int var1, boolean var2, boolean var3) {
      ((SupportMenu)this.mWrappedObject).setGroupCheckable(var1, var2, var3);
   }

   public void setGroupEnabled(int var1, boolean var2) {
      ((SupportMenu)this.mWrappedObject).setGroupEnabled(var1, var2);
   }

   public void setGroupVisible(int var1, boolean var2) {
      ((SupportMenu)this.mWrappedObject).setGroupVisible(var1, var2);
   }

   public void setQwertyMode(boolean var1) {
      ((SupportMenu)this.mWrappedObject).setQwertyMode(var1);
   }

   public int size() {
      return ((SupportMenu)this.mWrappedObject).size();
   }
}
