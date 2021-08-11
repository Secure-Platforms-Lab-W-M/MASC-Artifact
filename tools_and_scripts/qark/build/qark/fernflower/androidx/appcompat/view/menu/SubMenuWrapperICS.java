package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.core.internal.view.SupportSubMenu;

class SubMenuWrapperICS extends MenuWrapperICS implements SubMenu {
   private final SupportSubMenu mSubMenu;

   SubMenuWrapperICS(Context var1, SupportSubMenu var2) {
      super(var1, var2);
      this.mSubMenu = var2;
   }

   public void clearHeader() {
      this.mSubMenu.clearHeader();
   }

   public MenuItem getItem() {
      return this.getMenuItemWrapper(this.mSubMenu.getItem());
   }

   public SubMenu setHeaderIcon(int var1) {
      this.mSubMenu.setHeaderIcon(var1);
      return this;
   }

   public SubMenu setHeaderIcon(Drawable var1) {
      this.mSubMenu.setHeaderIcon(var1);
      return this;
   }

   public SubMenu setHeaderTitle(int var1) {
      this.mSubMenu.setHeaderTitle(var1);
      return this;
   }

   public SubMenu setHeaderTitle(CharSequence var1) {
      this.mSubMenu.setHeaderTitle(var1);
      return this;
   }

   public SubMenu setHeaderView(View var1) {
      this.mSubMenu.setHeaderView(var1);
      return this;
   }

   public SubMenu setIcon(int var1) {
      this.mSubMenu.setIcon(var1);
      return this;
   }

   public SubMenu setIcon(Drawable var1) {
      this.mSubMenu.setIcon(var1);
      return this;
   }
}
