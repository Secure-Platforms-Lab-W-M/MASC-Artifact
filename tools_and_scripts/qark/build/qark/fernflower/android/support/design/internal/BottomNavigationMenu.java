package android.support.design.internal;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.MenuItem;
import android.view.SubMenu;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class BottomNavigationMenu extends MenuBuilder {
   public static final int MAX_ITEM_COUNT = 5;

   public BottomNavigationMenu(Context var1) {
      super(var1);
   }

   protected MenuItem addInternal(int var1, int var2, int var3, CharSequence var4) {
      if (this.size() + 1 <= 5) {
         this.stopDispatchingItemsChanged();
         MenuItem var5 = super.addInternal(var1, var2, var3, var4);
         if (var5 instanceof MenuItemImpl) {
            ((MenuItemImpl)var5).setExclusiveCheckable(true);
         }

         this.startDispatchingItemsChanged();
         return var5;
      } else {
         throw new IllegalArgumentException("Maximum number of items supported by BottomNavigationView is 5. Limit can be checked with BottomNavigationView#getMaxItemCount()");
      }
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, CharSequence var4) {
      throw new UnsupportedOperationException("BottomNavigationView does not support submenus");
   }
}
