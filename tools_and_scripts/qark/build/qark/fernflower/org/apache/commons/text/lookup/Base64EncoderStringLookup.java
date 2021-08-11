package org.apache.commons.text.lookup;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

final class Base64EncoderStringLookup extends AbstractStringLookup {
   static final Base64EncoderStringLookup INSTANCE = new Base64EncoderStringLookup();

   private Base64EncoderStringLookup() {
   }

   public String lookup(String var1) {
      return var1 == null ? null : Base64.getEncoder().encodeToString(var1.getBytes(StandardCharsets.ISO_8859_1));
   }
}
