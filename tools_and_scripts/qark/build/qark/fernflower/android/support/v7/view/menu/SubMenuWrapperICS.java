package android.support.v7.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportSubMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

@RequiresApi(14)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class SubMenuWrapperICS extends MenuWrapperICS implements SubMenu {
   SubMenuWrapperICS(Context var1, SupportSubMenu var2) {
      super(var1, var2);
   }

   public void clearHeader() {
      this.getWrappedObject().clearHeader();
   }

   public MenuItem getItem() {
      return this.getMenuItemWrapper(this.getWrappedObject().getItem());
   }

   public SupportSubMenu getWrappedObject() {
      return (SupportSubMenu)this.mWrappedObject;
   }

   public SubMenu setHeaderIcon(int var1) {
      this.getWrappedObject().setHeaderIcon(var1);
      return this;
   }

   public SubMenu setHeaderIcon(Drawable var1) {
      this.getWrappedObject().setHeaderIcon(var1);
      return this;
   }

   public SubMenu setHeaderTitle(int var1) {
      this.getWrappedObject().setHeaderTitle(var1);
      return this;
   }

   public SubMenu setHeaderTitle(CharSequence var1) {
      this.getWrappedObject().setHeaderTitle(var1);
      return this;
   }

   public SubMenu setHeaderView(View var1) {
      this.getWrappedObject().setHeaderView(var1);
      return this;
   }

   public SubMenu setIcon(int var1) {
      this.getWrappedObject().setIcon(var1);
      return this;
   }

   public SubMenu setIcon(Drawable var1) {
      this.getWrappedObject().setIcon(var1);
      return this;
   }
}
