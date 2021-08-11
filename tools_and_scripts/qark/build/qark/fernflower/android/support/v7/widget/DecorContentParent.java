package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.MenuPresenter;
import android.util.SparseArray;
import android.view.Menu;
import android.view.Window.Callback;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public interface DecorContentParent {
   boolean canShowOverflowMenu();

   void dismissPopups();

   CharSequence getTitle();

   boolean hasIcon();

   boolean hasLogo();

   boolean hideOverflowMenu();

   void initFeature(int var1);

   boolean isOverflowMenuShowPending();

   boolean isOverflowMenuShowing();

   void restoreToolbarHierarchyState(SparseArray var1);

   void saveToolbarHierarchyState(SparseArray var1);

   void setIcon(int var1);

   void setIcon(Drawable var1);

   void setLogo(int var1);

   void setMenu(Menu var1, MenuPresenter.Callback var2);

   void setMenuPrepared();

   void setUiOptions(int var1);

   void setWindowCallback(Callback var1);

   void setWindowTitle(CharSequence var1);

   boolean showOverflowMenu();
}
