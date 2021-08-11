package androidx.core.content;

import android.content.SharedPreferences.Editor;

@Deprecated
public final class SharedPreferencesCompat {
   private SharedPreferencesCompat() {
   }

   @Deprecated
   public static final class EditorCompat {
      private static SharedPreferencesCompat.EditorCompat sInstance;
      private final SharedPreferencesCompat.EditorCompat.Helper mHelper = new SharedPreferencesCompat.EditorCompat.Helper();

      private EditorCompat() {
      }

      @Deprecated
      public static SharedPreferencesCompat.EditorCompat getInstance() {
         if (sInstance == null) {
            sInstance = new SharedPreferencesCompat.EditorCompat();
         }

         return sInstance;
      }

      @Deprecated
      public void apply(Editor var1) {
         this.mHelper.apply(var1);
      }

      private static class Helper {
         Helper() {
         }

         public void apply(Editor var1) {
            try {
               var1.apply();
            } catch (AbstractMethodError var3) {
               var1.commit();
            }
         }
      }
   }
}
