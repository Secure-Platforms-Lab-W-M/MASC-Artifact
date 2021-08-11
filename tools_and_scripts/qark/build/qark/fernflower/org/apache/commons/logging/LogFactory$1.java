package org.apache.commons.logging;

import java.security.PrivilegedAction;

final class LogFactory$1 implements PrivilegedAction {
   public Object run() {
      return LogFactory.directGetContextClassLoader();
   }
}
