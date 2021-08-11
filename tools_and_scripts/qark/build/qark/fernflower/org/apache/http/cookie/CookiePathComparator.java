package org.apache.http.cookie;

import java.io.Serializable;
import java.util.Comparator;

public class CookiePathComparator implements Serializable, Comparator {
   public static final CookiePathComparator INSTANCE = new CookiePathComparator();
   private static final long serialVersionUID = 7523645369616405818L;

   private String normalizePath(Cookie var1) {
      String var2 = var1.getPath();
      String var3 = var2;
      if (var2 == null) {
         var3 = "/";
      }

      var2 = var3;
      if (!var3.endsWith("/")) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var3);
         var4.append('/');
         var2 = var4.toString();
      }

      return var2;
   }

   public int compare(Cookie var1, Cookie var2) {
      String var3 = this.normalizePath(var1);
      String var4 = this.normalizePath(var2);
      if (var3.equals(var4)) {
         return 0;
      } else if (var3.startsWith(var4)) {
         return -1;
      } else {
         return var4.startsWith(var3) ? 1 : 0;
      }
   }
}
