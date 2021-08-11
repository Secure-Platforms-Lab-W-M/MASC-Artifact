package org.apache.commons.logging;

import java.security.PrivilegedAction;

final class LogFactory$6 implements PrivilegedAction {
   // $FF: synthetic field
   private final String val$def;
   // $FF: synthetic field
   private final String val$key;

   LogFactory$6(String var1, String var2) {
      this.val$key = var1;
      this.val$def = var2;
   }

   public Object run() {
      return System.getProperty(this.val$key, this.val$def);
   }
}
