package org.apache.commons.text.lookup;

final class EnvironmentVariableStringLookup extends AbstractStringLookup {
   static final EnvironmentVariableStringLookup INSTANCE = new EnvironmentVariableStringLookup();

   private EnvironmentVariableStringLookup() {
   }

   public String lookup(String var1) {
      return var1 != null ? System.getenv(var1) : null;
   }
}
