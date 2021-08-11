package org.apache.commons.logging;

import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.Enumeration;

final class LogFactory$4 implements PrivilegedAction {
   // $FF: synthetic field
   private final ClassLoader val$loader;
   // $FF: synthetic field
   private final String val$name;

   LogFactory$4(ClassLoader var1, String var2) {
      this.val$loader = var1;
      this.val$name = var2;
   }

   public Object run() {
      try {
         if (this.val$loader != null) {
            return this.val$loader.getResources(this.val$name);
         } else {
            Enumeration var1 = ClassLoader.getSystemResources(this.val$name);
            return var1;
         }
      } catch (IOException var3) {
         if (LogFactory.isDiagnosticsEnabled()) {
            StringBuffer var2 = new StringBuffer();
            var2.append("Exception while trying to find configuration file ");
            var2.append(this.val$name);
            var2.append(":");
            var2.append(var3.getMessage());
            LogFactory.access$000(var2.toString());
         }

         return null;
      } catch (NoSuchMethodError var4) {
         return null;
      }
   }
}
