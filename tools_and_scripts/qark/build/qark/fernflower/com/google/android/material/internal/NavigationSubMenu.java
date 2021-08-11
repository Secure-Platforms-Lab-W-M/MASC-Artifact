package com.google.android.material.internal;

import android.content.Context;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.SubMenuBuilder;

public class NavigationSubMenu extends SubMenuBuilder {
   public NavigationSubMenu(Context var1, NavigationMenu var2, MenuItemImpl var3) {
      super(var1, var2, var3);
   }

   public void onItemsChanged(boolean var1) {
      super.onItemsChanged(var1);
      ((MenuBuilder)this.getParentMenu()).onItemsChanged(var1);
   }
}
