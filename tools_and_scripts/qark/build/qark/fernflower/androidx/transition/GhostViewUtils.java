package androidx.transition;

import android.graphics.Matrix;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;

class GhostViewUtils {
   private GhostViewUtils() {
   }

   static GhostView addGhost(View var0, ViewGroup var1, Matrix var2) {
      return (GhostView)(VERSION.SDK_INT == 28 ? GhostViewPlatform.addGhost(var0, var1, var2) : GhostViewPort.addGhost(var0, var1, var2));
   }

   static void removeGhost(View var0) {
      if (VERSION.SDK_INT == 28) {
         GhostViewPlatform.removeGhost(var0);
      } else {
         GhostViewPort.removeGhost(var0);
      }
   }
}
