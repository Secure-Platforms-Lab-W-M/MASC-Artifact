package android.support.v4.content;

import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

class EditorCompatGingerbread {
   public static void apply(@NonNull Editor var0) {
      try {
         var0.apply();
      } catch (AbstractMethodError var2) {
         var0.commit();
      }
   }
}
