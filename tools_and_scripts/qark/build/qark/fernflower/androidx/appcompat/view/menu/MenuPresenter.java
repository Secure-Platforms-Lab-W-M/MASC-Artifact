package androidx.appcompat.view.menu;

import android.content.Context;
import android.os.Parcelable;
import android.view.ViewGroup;

public interface MenuPresenter {
   boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2);

   boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2);

   boolean flagActionItems();

   int getId();

   MenuView getMenuView(ViewGroup var1);

   void initForMenu(Context var1, MenuBuilder var2);

   void onCloseMenu(MenuBuilder var1, boolean var2);

   void onRestoreInstanceState(Parcelable var1);

   Parcelable onSaveInstanceState();

   boolean onSubMenuSelected(SubMenuBuilder var1);

   void setCallback(MenuPresenter.Callback var1);

   void updateMenuView(boolean var1);

   public interface Callback {
      void onCloseMenu(MenuBuilder var1, boolean var2);

      boolean onOpenSubMenu(MenuBuilder var1);
   }
}
