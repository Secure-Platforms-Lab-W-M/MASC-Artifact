package android.support.v4.hardware.display;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.support.annotation.RequiresApi;
import android.view.Display;

@TargetApi(17)
@RequiresApi(17)
final class DisplayManagerJellybeanMr1 {
   public static Display getDisplay(Object var0, int var1) {
      return ((DisplayManager)var0).getDisplay(var1);
   }

   public static Object getDisplayManager(Context var0) {
      return var0.getSystemService("display");
   }

   public static Display[] getDisplays(Object var0) {
      return ((DisplayManager)var0).getDisplays();
   }

   public static Display[] getDisplays(Object var0, String var1) {
      return ((DisplayManager)var0).getDisplays(var1);
   }
}
