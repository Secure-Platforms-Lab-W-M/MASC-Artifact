package android.support.transition;

import android.graphics.Matrix;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;

class GhostViewUtils {
   private static final GhostViewImpl.Creator CREATOR;

   static {
      if (VERSION.SDK_INT >= 21) {
         CREATOR = new GhostViewApi21.Creator();
      } else {
         CREATOR = new GhostViewApi14.Creator();
      }
   }

   static GhostViewImpl addGhost(View var0, ViewGroup var1, Matrix var2) {
      return CREATOR.addGhost(var0, var1, var2);
   }

   static void removeGhost(View var0) {
      CREATOR.removeGhost(var0);
   }
}
