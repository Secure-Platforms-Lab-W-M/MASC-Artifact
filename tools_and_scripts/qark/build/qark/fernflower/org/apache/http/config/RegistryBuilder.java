package org.apache.http.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.util.Args;

public final class RegistryBuilder {
   private final Map items = new HashMap();

   RegistryBuilder() {
   }

   public static RegistryBuilder create() {
      return new RegistryBuilder();
   }

   public Registry build() {
      return new Registry(this.items);
   }

   public RegistryBuilder register(String var1, Object var2) {
      Args.notEmpty(var1, "ID");
      Args.notNull(var2, "Item");
      this.items.put(var1.toLowerCase(Locale.ROOT), var2);
      return this;
   }

   public String toString() {
      return this.items.toString();
   }
}
