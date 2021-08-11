package android.support.v4.view;

import android.view.KeyEvent;
import android.view.View;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;

@Deprecated
public final class KeyEventCompat {
   private KeyEventCompat() {
   }

   @Deprecated
   public static boolean dispatch(KeyEvent var0, Callback var1, Object var2, Object var3) {
      return var0.dispatch(var1, (DispatcherState)var2, var3);
   }

   @Deprecated
   public static Object getKeyDispatcherState(View var0) {
      return var0.getKeyDispatcherState();
   }

   @Deprecated
   public static boolean hasModifiers(KeyEvent var0, int var1) {
      return var0.hasModifiers(var1);
   }

   @Deprecated
   public static boolean hasNoModifiers(KeyEvent var0) {
      return var0.hasNoModifiers();
   }

   @Deprecated
   public static boolean isCtrlPressed(KeyEvent var0) {
      return var0.isCtrlPressed();
   }

   @Deprecated
   public static boolean isTracking(KeyEvent var0) {
      return var0.isTracking();
   }

   @Deprecated
   public static boolean metaStateHasModifiers(int var0, int var1) {
      return KeyEvent.metaStateHasModifiers(var0, var1);
   }

   @Deprecated
   public static boolean metaStateHasNoModifiers(int var0) {
      return KeyEvent.metaStateHasNoModifiers(var0);
   }

   @Deprecated
   public static int normalizeMetaState(int var0) {
      return KeyEvent.normalizeMetaState(var0);
   }

   @Deprecated
   public static void startTracking(KeyEvent var0) {
      var0.startTracking();
   }
}
