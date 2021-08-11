package org.apache.http.cookie;

import java.io.Serializable;
import java.util.Comparator;

public class CookieIdentityComparator implements Serializable, Comparator {
   private static final long serialVersionUID = 4466565437490631532L;

   public int compare(Cookie var1, Cookie var2) {
      int var4 = var1.getName().compareTo(var2.getName());
      int var3 = var4;
      String var5;
      if (var4 == 0) {
         String var6 = var1.getDomain();
         if (var6 == null) {
            var5 = "";
         } else {
            var5 = var6;
            if (var6.indexOf(46) == -1) {
               StringBuilder var10 = new StringBuilder();
               var10.append(var6);
               var10.append(".local");
               var5 = var10.toString();
            }
         }

         String var7 = var2.getDomain();
         if (var7 == null) {
            var6 = "";
         } else {
            var6 = var7;
            if (var7.indexOf(46) == -1) {
               StringBuilder var11 = new StringBuilder();
               var11.append(var7);
               var11.append(".local");
               var6 = var11.toString();
            }
         }

         var3 = var5.compareToIgnoreCase(var6);
      }

      var4 = var3;
      if (var3 == 0) {
         var5 = var1.getPath();
         String var8 = var5;
         if (var5 == null) {
            var8 = "/";
         }

         var5 = var2.getPath();
         String var9 = var5;
         if (var5 == null) {
            var9 = "/";
         }

         var4 = var8.compareTo(var9);
      }

      return var4;
   }
}
