package androidx.appcompat.view.menu;

import android.graphics.drawable.Drawable;

public interface MenuView {
   int getWindowAnimations();

   void initialize(MenuBuilder var1);

   public interface ItemView {
      MenuItemImpl getItemData();

      void initialize(MenuItemImpl var1, int var2);

      boolean prefersCondensedTitle();

      void setCheckable(boolean var1);

      void setChecked(boolean var1);

      void setEnabled(boolean var1);

      void setIcon(Drawable var1);

      void setShortcut(boolean var1, char var2);

      void setTitle(CharSequence var1);

      boolean showsIcon();
   }
}
