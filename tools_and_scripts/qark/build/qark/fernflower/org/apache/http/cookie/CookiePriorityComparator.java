package org.apache.http.cookie;

import java.util.Comparator;
import java.util.Date;
import org.apache.http.impl.cookie.BasicClientCookie;

public class CookiePriorityComparator implements Comparator {
   public static final CookiePriorityComparator INSTANCE = new CookiePriorityComparator();

   private int getPathLength(Cookie var1) {
      String var2 = var1.getPath();
      return var2 != null ? var2.length() : 1;
   }

   public int compare(Cookie var1, Cookie var2) {
      int var3 = this.getPathLength(var1);
      var3 = this.getPathLength(var2) - var3;
      if (var3 == 0 && var1 instanceof BasicClientCookie && var2 instanceof BasicClientCookie) {
         Date var4 = ((BasicClientCookie)var1).getCreationDate();
         Date var5 = ((BasicClientCookie)var2).getCreationDate();
         if (var4 != null && var5 != null) {
            return (int)(var4.getTime() - var5.getTime());
         }
      }

      return var3;
   }
}
