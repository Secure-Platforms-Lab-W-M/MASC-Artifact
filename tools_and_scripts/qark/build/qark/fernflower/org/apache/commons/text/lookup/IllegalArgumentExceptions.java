package org.apache.commons.text.lookup;

final class IllegalArgumentExceptions {
   private IllegalArgumentExceptions() {
   }

   static IllegalArgumentException format(String var0, Object... var1) {
      return new IllegalArgumentException(String.format(var0, var1));
   }

   static IllegalArgumentException format(Throwable var0, String var1, Object... var2) {
      return new IllegalArgumentException(String.format(var1, var2), var0);
   }
}
