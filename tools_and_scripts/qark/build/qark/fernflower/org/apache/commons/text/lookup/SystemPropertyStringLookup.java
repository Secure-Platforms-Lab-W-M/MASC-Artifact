package org.apache.commons.text.lookup;

final class SystemPropertyStringLookup extends AbstractStringLookup {
   static final SystemPropertyStringLookup INSTANCE = new SystemPropertyStringLookup();

   private SystemPropertyStringLookup() {
   }

   public String lookup(String var1) {
      try {
         var1 = System.getProperty(var1);
         return var1;
      } catch (SecurityException var2) {
      } catch (NullPointerException var3) {
      } catch (IllegalArgumentException var4) {
      }

      return null;
   }
}
