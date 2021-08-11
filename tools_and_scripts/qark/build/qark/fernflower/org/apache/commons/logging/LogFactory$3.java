package org.apache.commons.logging;

import java.security.PrivilegedAction;

final class LogFactory$3 implements PrivilegedAction {
   // $FF: synthetic field
   private final ClassLoader val$loader;
   // $FF: synthetic field
   private final String val$name;

   LogFactory$3(ClassLoader var1, String var2) {
      this.val$loader = var1;
      this.val$name = var2;
   }

   public Object run() {
      ClassLoader var1 = this.val$loader;
      return var1 != null ? var1.getResourceAsStream(this.val$name) : ClassLoader.getSystemResourceAsStream(this.val$name);
   }
}
