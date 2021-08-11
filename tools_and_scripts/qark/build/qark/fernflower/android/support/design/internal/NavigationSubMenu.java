package android.support.design.internal;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.SubMenuBuilder;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class NavigationSubMenu extends SubMenuBuilder {
   public NavigationSubMenu(Context var1, NavigationMenu var2, MenuItemImpl var3) {
      super(var1, var2, var3);
   }

   public void onItemsChanged(boolean var1) {
      super.onItemsChanged(var1);
      ((MenuBuilder)this.getParentMenu()).onItemsChanged(var1);
   }
}
