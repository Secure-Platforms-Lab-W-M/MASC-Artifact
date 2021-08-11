package org.apache.http.conn.util;

import java.net.IDN;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.util.Args;

public final class PublicSuffixMatcher {
   private final Map exceptions;
   private final Map rules;

   public PublicSuffixMatcher(Collection var1) {
      Args.notNull(var1, "Domain suffix lists");
      this.rules = new ConcurrentHashMap();
      this.exceptions = new ConcurrentHashMap();
      Iterator var6 = var1.iterator();

      while(true) {
         DomainType var2;
         List var7;
         do {
            if (!var6.hasNext()) {
               return;
            }

            PublicSuffixList var3 = (PublicSuffixList)var6.next();
            var2 = var3.getType();
            Iterator var4 = var3.getRules().iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               this.rules.put(var5, var2);
            }

            var7 = var3.getExceptions();
         } while(var7 == null);

         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            this.exceptions.put(var9, var2);
         }
      }
   }

   public PublicSuffixMatcher(Collection var1, Collection var2) {
      this(DomainType.UNKNOWN, var1, var2);
   }

   public PublicSuffixMatcher(DomainType var1, Collection var2, Collection var3) {
      Args.notNull(var1, "Domain type");
      Args.notNull(var2, "Domain suffix rules");
      this.rules = new ConcurrentHashMap(var2.size());
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         String var4 = (String)var5.next();
         this.rules.put(var4, var1);
      }

      this.exceptions = new ConcurrentHashMap();
      if (var3 != null) {
         var5 = var3.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            this.exceptions.put(var6, var1);
         }
      }

   }

   private static boolean hasEntry(Map var0, String var1, DomainType var2) {
      if (var0 == null) {
         return false;
      } else {
         DomainType var3 = (DomainType)var0.get(var1);
         return var3 != null && (var2 == null || var3.equals(var2));
      }
   }

   private boolean hasException(String var1, DomainType var2) {
      return hasEntry(this.exceptions, var1, var2);
   }

   private boolean hasRule(String var1, DomainType var2) {
      return hasEntry(this.rules, var1, var2);
   }

   public String getDomainRoot(String var1) {
      return this.getDomainRoot(var1, (DomainType)null);
   }

   public String getDomainRoot(String var1, DomainType var2) {
      if (var1 == null) {
         return null;
      } else if (var1.startsWith(".")) {
         return null;
      } else {
         var1 = var1.toLowerCase(Locale.ROOT);

         String var4;
         String var5;
         for(var5 = null; var1 != null; var1 = var4) {
            var4 = IDN.toUnicode(var1);
            if (this.hasException(var4, var2)) {
               return var1;
            }

            if (this.hasRule(var4, var2)) {
               return var5;
            }

            int var3 = var1.indexOf(46);
            if (var3 != -1) {
               var4 = var1.substring(var3 + 1);
            } else {
               var4 = null;
            }

            if (var4 != null) {
               StringBuilder var6 = new StringBuilder();
               var6.append("*.");
               var6.append(IDN.toUnicode(var4));
               if (this.hasRule(var6.toString(), var2)) {
                  return var5;
               }
            }

            var5 = var1;
         }

         return var5;
      }
   }

   public boolean matches(String var1) {
      return this.matches(var1, (DomainType)null);
   }

   public boolean matches(String var1, DomainType var2) {
      boolean var3 = false;
      if (var1 == null) {
         return false;
      } else {
         if (var1.startsWith(".")) {
            var1 = var1.substring(1);
         }

         if (this.getDomainRoot(var1, var2) == null) {
            var3 = true;
         }

         return var3;
      }
   }
}
