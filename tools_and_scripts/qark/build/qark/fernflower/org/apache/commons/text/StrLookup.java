package org.apache.commons.text;

import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.text.lookup.StringLookup;

@Deprecated
public abstract class StrLookup implements StringLookup {
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

   public static StrLookup resourceBundleLookup(ResourceBundle var0) {
      return new StrLookup.ResourceBundleLookup(var0);
   }

   public static StrLookup systemPropertiesLookup() {
      return SYSTEM_PROPERTIES_LOOKUP;
   }

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

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(super.toString());
         var1.append(" [map=");
         var1.append(this.map);
         var1.append("]");
         return var1.toString();
      }
   }

   private static final class ResourceBundleLookup extends StrLookup {
      private final ResourceBundle resourceBundle;

      private ResourceBundleLookup(ResourceBundle var1) {
         this.resourceBundle = var1;
      }

      // $FF: synthetic method
      ResourceBundleLookup(ResourceBundle var1, Object var2) {
         this(var1);
      }

      public String lookup(String var1) {
         ResourceBundle var2 = this.resourceBundle;
         return var2 != null && var1 != null && var2.containsKey(var1) ? this.resourceBundle.getString(var1) : null;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(super.toString());
         var1.append(" [resourceBundle=");
         var1.append(this.resourceBundle);
         var1.append("]");
         return var1.toString();
      }
   }

   private static final class SystemPropertiesStrLookup extends StrLookup {
      private SystemPropertiesStrLookup() {
      }

      // $FF: synthetic method
      SystemPropertiesStrLookup(Object var1) {
         this();
      }

      public String lookup(String var1) {
         if (var1.length() > 0) {
            try {
               var1 = System.getProperty(var1);
               return var1;
            } catch (SecurityException var2) {
               return null;
            }
         } else {
            return null;
         }
      }
   }
}
