package org.apache.commons.text.lookup;

import java.util.Map;

final class MapStringLookup implements StringLookup {
   private final Map map;

   private MapStringLookup(Map var1) {
      this.map = var1;
   }

   // $FF: renamed from: on (java.util.Map) org.apache.commons.text.lookup.MapStringLookup
   static MapStringLookup method_44(Map var0) {
      return new MapStringLookup(var0);
   }

   Map getMap() {
      return this.map;
   }

   public String lookup(String var1) {
      Map var3 = this.map;
      Object var2 = null;
      if (var3 == null) {
         return null;
      } else {
         Object var5;
         try {
            var5 = var3.get(var1);
         } catch (NullPointerException var4) {
            return null;
         }

         var1 = (String)var2;
         if (var5 != null) {
            var1 = var5.toString();
         }

         return var1;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append(" [map=");
      var1.append(this.map);
      var1.append("]");
      return var1.toString();
   }
}
