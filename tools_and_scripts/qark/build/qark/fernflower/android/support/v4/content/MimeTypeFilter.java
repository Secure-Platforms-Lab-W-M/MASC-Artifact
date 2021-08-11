package android.support.v4.content;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;

public final class MimeTypeFilter {
   private MimeTypeFilter() {
   }

   public static String matches(@Nullable String var0, @NonNull String[] var1) {
      if (var0 == null) {
         return null;
      } else {
         String[] var5 = var0.split("/");
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            String var4 = var1[var2];
            if (mimeTypeAgainstFilter(var5, var4.split("/"))) {
               return var4;
            }
         }

         return null;
      }
   }

   public static String matches(@Nullable String[] var0, @NonNull String var1) {
      if (var0 == null) {
         return null;
      } else {
         String[] var5 = var1.split("/");
         int var3 = var0.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            String var4 = var0[var2];
            if (mimeTypeAgainstFilter(var4.split("/"), var5)) {
               return var4;
            }
         }

         return null;
      }
   }

   public static boolean matches(@Nullable String var0, @NonNull String var1) {
      return var0 == null ? false : mimeTypeAgainstFilter(var0.split("/"), var1.split("/"));
   }

   public static String[] matchesMany(@Nullable String[] var0, @NonNull String var1) {
      int var2 = 0;
      if (var0 == null) {
         return new String[0];
      } else {
         ArrayList var4 = new ArrayList();
         String[] var6 = var1.split("/");

         for(int var3 = var0.length; var2 < var3; ++var2) {
            String var5 = var0[var2];
            if (mimeTypeAgainstFilter(var5.split("/"), var6)) {
               var4.add(var5);
            }
         }

         return (String[])var4.toArray(new String[var4.size()]);
      }
   }

   private static boolean mimeTypeAgainstFilter(@NonNull String[] var0, @NonNull String[] var1) {
      if (var1.length == 2) {
         if (!var1[0].isEmpty() && !var1[1].isEmpty()) {
            if (var0.length != 2) {
               return false;
            } else if (!"*".equals(var1[0]) && !var1[0].equals(var0[0])) {
               return false;
            } else {
               return "*".equals(var1[1]) || var1[1].equals(var0[1]);
            }
         } else {
            throw new IllegalArgumentException("Ill-formatted MIME type filter. Type or subtype empty.");
         }
      } else {
         throw new IllegalArgumentException("Ill-formatted MIME type filter. Must be type/subtype.");
      }
   }
}
