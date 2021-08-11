package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;

@TargetApi(14)
@RequiresApi(14)
class MenuItemCompatIcs {
   public static boolean collapseActionView(MenuItem var0) {
      return var0.collapseActionView();
   }

   public static boolean expandActionView(MenuItem var0) {
      return var0.expandActionView();
   }

   public static boolean isActionViewExpanded(MenuItem var0) {
      return var0.isActionViewExpanded();
   }

   public static MenuItem setOnActionExpandListener(MenuItem var0, MenuItemCompatIcs.SupportActionExpandProxy var1) {
      return var0.setOnActionExpandListener(new MenuItemCompatIcs.OnActionExpandListenerWrapper(var1));
   }

   static class OnActionExpandListenerWrapper implements OnActionExpandListener {
      private MenuItemCompatIcs.SupportActionExpandProxy mWrapped;

      public OnActionExpandListenerWrapper(MenuItemCompatIcs.SupportActionExpandProxy var1) {
         this.mWrapped = var1;
      }

      public boolean onMenuItemActionCollapse(MenuItem var1) {
         return this.mWrapped.onMenuItemActionCollapse(var1);
      }

      public boolean onMenuItemActionExpand(MenuItem var1) {
         return this.mWrapped.onMenuItemActionExpand(var1);
      }
   }

   interface SupportActionExpandProxy {
      boolean onMenuItemActionCollapse(MenuItem var1);

      boolean onMenuItemActionExpand(MenuItem var1);
   }
}
