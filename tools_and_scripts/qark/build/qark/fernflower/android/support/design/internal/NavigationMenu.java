package android.support.design.internal;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.SubMenu;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
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
