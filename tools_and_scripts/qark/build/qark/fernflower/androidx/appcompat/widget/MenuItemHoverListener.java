package androidx.appcompat.widget;

import android.view.MenuItem;
import androidx.appcompat.view.menu.MenuBuilder;

public interface MenuItemHoverListener {
   void onItemHoverEnter(MenuBuilder var1, MenuItem var2);

   void onItemHoverExit(MenuBuilder var1, MenuItem var2);
}
