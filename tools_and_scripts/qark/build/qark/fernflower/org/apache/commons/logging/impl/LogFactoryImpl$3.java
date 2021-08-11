package org.apache.commons.logging.impl;

import java.security.PrivilegedAction;

class LogFactoryImpl$3 implements PrivilegedAction {
   // $FF: synthetic field
   private final LogFactoryImpl this$0;
   // $FF: synthetic field
   private final ClassLoader val$cl;

   LogFactoryImpl$3(LogFactoryImpl var1, ClassLoader var2) {
      this.this$0 = var1;
      this.val$cl = var2;
   }

   public Object run() {
      return this.val$cl.getParent();
   }
}
