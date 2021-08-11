package org.apache.commons.text.lookup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

class InterpolatorStringLookup extends AbstractStringLookup {
   static final AbstractStringLookup INSTANCE = new InterpolatorStringLookup();
   private static final char PREFIX_SEPARATOR = ':';
   private final StringLookup defaultStringLookup;
   private final Map stringLookupMap;

   InterpolatorStringLookup() {
      this((Map)null);
   }

   InterpolatorStringLookup(Map var1) {
      if (var1 == null) {
         var1 = new HashMap();
      }

      this((StringLookup)MapStringLookup.method_44((Map)var1));
   }

   InterpolatorStringLookup(Map var1, StringLookup var2, boolean var3) {
      super();
      this.defaultStringLookup = var2;
      this.stringLookupMap = new HashMap(var1.size());
      Iterator var4 = var1.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         this.stringLookupMap.put(toKey((String)var5.getKey()), var5.getValue());
      }

      if (var3) {
         StringLookupFactory.INSTANCE.addDefaultStringLookups(this.stringLookupMap);
      }

   }

   InterpolatorStringLookup(StringLookup var1) {
      this(new HashMap(), var1, true);
   }

   static String toKey(String var0) {
      return var0.toLowerCase(Locale.ROOT);
   }

   public Map getStringLookupMap() {
      return this.stringLookupMap;
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = var1.indexOf(58);
         String var3 = var1;
         if (var2 >= 0) {
            var3 = toKey(var1.substring(0, var2));
            String var4 = var1.substring(var2 + 1);
            StringLookup var5 = (StringLookup)this.stringLookupMap.get(var3);
            var3 = null;
            if (var5 != null) {
               var3 = var5.lookup(var4);
            }

            if (var3 != null) {
               return var3;
            }

            var3 = var1.substring(var2 + 1);
         }

         StringLookup var6 = this.defaultStringLookup;
         return var6 != null ? var6.lookup(var3) : null;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append(" [stringLookupMap=");
      var1.append(this.stringLookupMap);
      var1.append(", defaultStringLookup=");
      var1.append(this.defaultStringLookup);
      var1.append("]");
      return var1.toString();
   }
}
