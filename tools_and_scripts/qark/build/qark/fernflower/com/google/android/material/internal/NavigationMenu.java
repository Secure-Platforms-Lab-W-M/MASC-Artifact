package com.google.android.material.internal;

import android.content.Context;
import android.view.SubMenu;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;

public class NavigationMenu extends MenuBuilder {
   public NavigationMenu(Context var1) {
      super(var1);
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, CharSequence var4) {
      MenuItemImpl var6 = (MenuItemImpl)this.addInternal(var1, var2, var3, var4);
      NavigationSubMenu var5 = new NavigationSubMenu(this.getContext(), this, var6);
      var6.setSubMenu(var5);
      return var5;
   }
}
