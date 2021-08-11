package org.apache.commons.logging.impl;

import java.security.PrivilegedAction;

final class SimpleLog$1 implements PrivilegedAction {
   // $FF: synthetic field
   private final String val$name;

   SimpleLog$1(String var1) {
      this.val$name = var1;
   }

   public Object run() {
      ClassLoader var1 = SimpleLog.access$000();
      return var1 != null ? var1.getResourceAsStream(this.val$name) : ClassLoader.getSystemResourceAsStream(this.val$name);
   }
}
