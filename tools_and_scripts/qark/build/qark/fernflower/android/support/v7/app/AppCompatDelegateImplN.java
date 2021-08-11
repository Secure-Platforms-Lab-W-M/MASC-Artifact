package android.support.v7.app;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.Window;
import android.view.Window.Callback;
import java.util.List;

@RequiresApi(24)
class AppCompatDelegateImplN extends AppCompatDelegateImplV23 {
   AppCompatDelegateImplN(Context var1, Window var2, AppCompatCallback var3) {
      super(var1, var2, var3);
   }

   Callback wrapWindowCallback(Callback var1) {
      return new AppCompatDelegateImplN.AppCompatWindowCallbackN(var1);
   }

   class AppCompatWindowCallbackN extends AppCompatDelegateImplV23.AppCompatWindowCallbackV23 {
      AppCompatWindowCallbackN(Callback var2) {
         super(var2);
      }

      public void onProvideKeyboardShortcuts(List var1, Menu var2, int var3) {
         AppCompatDelegateImplV9.PanelFeatureState var4 = AppCompatDelegateImplN.this.getPanelState(0, true);
         if (var4 != null && var4.menu != null) {
            super.onProvideKeyboardShortcuts(var1, var4.menu, var3);
         } else {
            super.onProvideKeyboardShortcuts(var1, var2, var3);
         }
      }
   }
}
