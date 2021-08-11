package org.apache.commons.lang3.text;

import java.util.Map;

@Deprecated
public abstract class StrLookup {
   private static final StrLookup NONE_LOOKUP = new StrLookup.MapStrLookup((Map)null);
   private static final StrLookup SYSTEM_PROPERTIES_LOOKUP = new StrLookup.SystemPropertiesStrLookup();

   protected StrLookup() {
   }

   public static StrLookup mapLookup(Map var0) {
      return new StrLookup.MapStrLookup(var0);
   }

   public static StrLookup noneLookup() {
      return NONE_LOOKUP;
   }

   public static StrLookup systemPropertiesLookup() {
      return SYSTEM_PROPERTIES_LOOKUP;
   }

   public abstract String lookup(String var1);

   static class MapStrLookup extends StrLookup {
      private final Map map;

      MapStrLookup(Map var1) {
         this.map = var1;
      }

      public String lookup(String var1) {
         Map var2 = this.map;
         if (var2 == null) {
            return null;
         } else {
            Object var3 = var2.get(var1);
            return var3 == null ? null : var3.toString();
         }
      }
   }

   private static class SystemPropertiesStrLookup extends StrLookup {
      private SystemPropertiesStrLookup() {
      }

      // $FF: synthetic method
      SystemPropertiesStrLookup(Object var1) {
         this();
      }

      public String lookup(String var1) {
         if (!var1.isEmpty()) {
            try {
               var1 = System.getProperty(var1);
               return var1;
            } catch (SecurityException var2) {
            }
         }

         return null;
      }
   }
}
