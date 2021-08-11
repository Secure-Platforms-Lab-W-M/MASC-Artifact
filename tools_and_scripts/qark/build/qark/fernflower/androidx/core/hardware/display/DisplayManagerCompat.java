package androidx.core.hardware.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

public final class DisplayManagerCompat {
   public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
   private static final WeakHashMap sInstances = new WeakHashMap();
   private final Context mContext;

   private DisplayManagerCompat(Context var1) {
      this.mContext = var1;
   }

   public static DisplayManagerCompat getInstance(Context var0) {
      WeakHashMap var3 = sInstances;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label176: {
         DisplayManagerCompat var2;
         try {
            var2 = (DisplayManagerCompat)sInstances.get(var0);
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label176;
         }

         DisplayManagerCompat var1 = var2;
         if (var2 == null) {
            try {
               var1 = new DisplayManagerCompat(var0);
               sInstances.put(var0, var1);
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label176;
            }
         }

         label165:
         try {
            return var1;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label165;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public Display getDisplay(int var1) {
      if (VERSION.SDK_INT >= 17) {
         return ((DisplayManager)this.mContext.getSystemService("display")).getDisplay(var1);
      } else {
         Display var2 = ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay();
         return var2.getDisplayId() == var1 ? var2 : null;
      }
   }

   public Display[] getDisplays() {
      return VERSION.SDK_INT >= 17 ? ((DisplayManager)this.mContext.getSystemService("display")).getDisplays() : new Display[]{((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay()};
   }

   public Display[] getDisplays(String var1) {
      if (VERSION.SDK_INT >= 17) {
         return ((DisplayManager)this.mContext.getSystemService("display")).getDisplays(var1);
      } else {
         return var1 == null ? new Display[0] : new Display[]{((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay()};
      }
   }
}
