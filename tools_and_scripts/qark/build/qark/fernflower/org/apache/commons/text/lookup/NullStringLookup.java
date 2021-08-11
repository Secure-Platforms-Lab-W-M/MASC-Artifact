package org.apache.commons.text.lookup;

final class NullStringLookup extends AbstractStringLookup {
   static final NullStringLookup INSTANCE = new NullStringLookup();

   private NullStringLookup() {
   }

   public String lookup(String var1) {
      return null;
   }
}
