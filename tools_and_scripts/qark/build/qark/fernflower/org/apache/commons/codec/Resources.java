package org.apache.commons.codec;

import java.io.InputStream;

public class Resources {
   public static InputStream getInputStream(String var0) {
      InputStream var1 = Resources.class.getClassLoader().getResourceAsStream(var0);
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unable to resolve required resource: ");
         var2.append(var0);
         throw new IllegalArgumentException(var2.toString());
      }
   }
}
