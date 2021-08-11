package android.support.v4.database;

import android.text.TextUtils;

public final class DatabaseUtilsCompat {
   private DatabaseUtilsCompat() {
   }

   public static String[] appendSelectionArgs(String[] var0, String[] var1) {
      if (var0 != null) {
         if (var0.length == 0) {
            return var1;
         } else {
            String[] var2 = new String[var0.length + var1.length];
            System.arraycopy(var0, 0, var2, 0, var0.length);
            System.arraycopy(var1, 0, var2, var0.length, var1.length);
            return var2;
         }
      } else {
         return var1;
      }
   }

   public static String concatenateWhere(String var0, String var1) {
      if (TextUtils.isEmpty(var0)) {
         return var1;
      } else if (TextUtils.isEmpty(var1)) {
         return var0;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("(");
         var2.append(var0);
         var2.append(") AND (");
         var2.append(var1);
         var2.append(")");
         return var2.toString();
      }
   }
}
