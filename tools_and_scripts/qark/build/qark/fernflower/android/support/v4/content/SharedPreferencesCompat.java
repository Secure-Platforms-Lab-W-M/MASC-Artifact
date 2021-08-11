package android.support.v4.content;

import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

public final class SharedPreferencesCompat {
   private SharedPreferencesCompat() {
   }

   public static final class EditorCompat {
      private static SharedPreferencesCompat.EditorCompat sInstance;
      private final SharedPreferencesCompat.EditorCompat.Helper mHelper = new SharedPreferencesCompat.EditorCompat.Helper();

      private EditorCompat() {
      }

      public static SharedPreferencesCompat.EditorCompat getInstance() {
         if (sInstance == null) {
            sInstance = new SharedPreferencesCompat.EditorCompat();
         }

         return sInstance;
      }

      public void apply(@NonNull Editor var1) {
         this.mHelper.apply(var1);
      }

      private static class Helper {
         Helper() {
         }

         public void apply(@NonNull Editor var1) {
            try {
               var1.apply();
            } catch (AbstractMethodError var3) {
               var1.commit();
            }
         }
      }
   }
}
