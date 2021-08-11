package android.support.v4.view;

import android.view.KeyEvent;
import android.view.View;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;

class KeyEventCompatEclair {
   public static boolean dispatch(KeyEvent var0, Callback var1, Object var2, Object var3) {
      return var0.dispatch(var1, (DispatcherState)var2, var3);
   }

   public static Object getKeyDispatcherState(View var0) {
      return var0.getKeyDispatcherState();
   }

   public static boolean isTracking(KeyEvent var0) {
      return var0.isTracking();
   }

   public static void startTracking(KeyEvent var0) {
      var0.startTracking();
   }
}
