package android.support.v4.hardware.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

public abstract class DisplayManagerCompat {
   public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
   private static final WeakHashMap sInstances = new WeakHashMap();

   DisplayManagerCompat() {
   }

   public static DisplayManagerCompat getInstance(Context var0) {
      WeakHashMap var3 = sInstances;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label355: {
         DisplayManagerCompat var2;
         try {
            var2 = (DisplayManagerCompat)sInstances.get(var0);
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label355;
         }

         Object var1 = var2;
         if (var2 == null) {
            label356: {
               try {
                  if (VERSION.SDK_INT >= 17) {
                     var1 = new DisplayManagerCompat.DisplayManagerCompatApi17Impl(var0);
                     break label356;
                  }
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label355;
               }

               try {
                  var1 = new DisplayManagerCompat.DisplayManagerCompatApi14Impl(var0);
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label355;
               }
            }

            try {
               sInstances.put(var0, var1);
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label355;
            }
         }

         label335:
         try {
            return (DisplayManagerCompat)var1;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label335;
         }
      }

      while(true) {
         Throwable var46 = var10000;

         try {
            throw var46;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            continue;
         }
      }
   }

   public abstract Display getDisplay(int var1);

   public abstract Display[] getDisplays();

   public abstract Display[] getDisplays(String var1);

   private static class DisplayManagerCompatApi14Impl extends DisplayManagerCompat {
      private final WindowManager mWindowManager;

      DisplayManagerCompatApi14Impl(Context var1) {
         this.mWindowManager = (WindowManager)var1.getSystemService("window");
      }

      public Display getDisplay(int var1) {
         Display var2 = this.mWindowManager.getDefaultDisplay();
         return var2.getDisplayId() == var1 ? var2 : null;
      }

      public Display[] getDisplays() {
         return new Display[]{this.mWindowManager.getDefaultDisplay()};
      }

      public Display[] getDisplays(String var1) {
         return var1 == null ? this.getDisplays() : new Display[0];
      }
   }

   @RequiresApi(17)
   private static class DisplayManagerCompatApi17Impl extends DisplayManagerCompat {
      private final DisplayManager mDisplayManager;

      DisplayManagerCompatApi17Impl(Context var1) {
         this.mDisplayManager = (DisplayManager)var1.getSystemService("display");
      }

      public Display getDisplay(int var1) {
         return this.mDisplayManager.getDisplay(var1);
      }

      public Display[] getDisplays() {
         return this.mDisplayManager.getDisplays();
      }

      public Display[] getDisplays(String var1) {
         return this.mDisplayManager.getDisplays(var1);
      }
   }
}
