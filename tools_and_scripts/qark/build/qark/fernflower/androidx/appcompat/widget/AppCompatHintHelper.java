package androidx.appcompat.widget;

import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

class AppCompatHintHelper {
   private AppCompatHintHelper() {
   }

   static InputConnection onCreateInputConnection(InputConnection var0, EditorInfo var1, View var2) {
      if (var0 != null && var1.hintText == null) {
         for(ViewParent var3 = var2.getParent(); var3 instanceof View; var3 = var3.getParent()) {
            if (var3 instanceof WithHint) {
               var1.hintText = ((WithHint)var3).getHint();
               return var0;
            }
         }
      }

      return var0;
   }
}
