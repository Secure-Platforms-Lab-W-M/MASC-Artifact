package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.collection.ArrayMap;
import androidx.core.internal.view.SupportMenuItem;
import androidx.core.internal.view.SupportSubMenu;
import java.util.Iterator;
import java.util.Map;

abstract class BaseMenuWrapper {
   final Context mContext;
   private Map mMenuItems;
   private Map mSubMenus;

   BaseMenuWrapper(Context var1) {
      this.mContext = var1;
   }

   final MenuItem getMenuItemWrapper(MenuItem var1) {
      if (var1 instanceof SupportMenuItem) {
         SupportMenuItem var3 = (SupportMenuItem)var1;
         if (this.mMenuItems == null) {
            this.mMenuItems = new ArrayMap();
         }

         MenuItem var2 = (MenuItem)this.mMenuItems.get(var1);
         Object var4 = var2;
         if (var2 == null) {
            var4 = new MenuItemWrapperICS(this.mContext, var3);
            this.mMenuItems.put(var3, var4);
         }

         return (MenuItem)var4;
      } else {
         return var1;
      }
   }

   final SubMenu getSubMenuWrapper(SubMenu var1) {
      if (var1 instanceof SupportSubMenu) {
         SupportSubMenu var3 = (SupportSubMenu)var1;
         if (this.mSubMenus == null) {
            this.mSubMenus = new ArrayMap();
         }

         SubMenu var2 = (SubMenu)this.mSubMenus.get(var3);
         Object var4 = var2;
         if (var2 == null) {
            var4 = new SubMenuWrapperICS(this.mContext, var3);
            this.mSubMenus.put(var3, var4);
         }

         return (SubMenu)var4;
      } else {
         return var1;
      }
   }

   final void internalClear() {
      Map var1 = this.mMenuItems;
      if (var1 != null) {
         var1.clear();
      }

      var1 = this.mSubMenus;
      if (var1 != null) {
         var1.clear();
      }

   }

   final void internalRemoveGroup(int var1) {
      Map var2 = this.mMenuItems;
      if (var2 != null) {
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            if (var1 == ((MenuItem)var3.next()).getGroupId()) {
               var3.remove();
            }
         }

      }
   }

   final void internalRemoveItem(int var1) {
      Map var2 = this.mMenuItems;
      if (var2 != null) {
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            if (var1 == ((MenuItem)var3.next()).getItemId()) {
               var3.remove();
               break;
            }
         }

      }
   }
}
