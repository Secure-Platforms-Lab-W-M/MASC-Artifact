package org.apache.commons.text.lookup;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

final class Base64DecoderStringLookup extends AbstractStringLookup {
   static final Base64DecoderStringLookup INSTANCE = new Base64DecoderStringLookup();

   private Base64DecoderStringLookup() {
   }

   public String lookup(String var1) {
      return var1 == null ? null : new String(Base64.getDecoder().decode(var1), StandardCharsets.ISO_8859_1);
   }
}
