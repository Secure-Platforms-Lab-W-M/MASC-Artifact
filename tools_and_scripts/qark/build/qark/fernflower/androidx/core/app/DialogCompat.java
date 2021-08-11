package androidx.core.app;

import android.app.Dialog;
import android.os.Build.VERSION;
import android.view.View;

public class DialogCompat {
   private DialogCompat() {
   }

   public static View requireViewById(Dialog var0, int var1) {
      if (VERSION.SDK_INT >= 28) {
         return var0.requireViewById(var1);
      } else {
         View var2 = var0.findViewById(var1);
         if (var2 != null) {
            return var2;
         } else {
            throw new IllegalArgumentException("ID does not reference a View inside this Dialog");
         }
      }
   }
}
