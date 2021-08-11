package org.apache.commons.text.lookup;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

final class UrlDecoderStringLookup extends AbstractStringLookup {
   static final UrlDecoderStringLookup INSTANCE = new UrlDecoderStringLookup();

   private UrlDecoderStringLookup() {
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = StandardCharsets.UTF_8.name();

         try {
            String var3 = URLDecoder.decode(var1, var2);
            return var3;
         } catch (UnsupportedEncodingException var4) {
            throw IllegalArgumentExceptions.format(var4, "%s: source=%s, encoding=%s", var4, var1, var2);
         }
      }
   }
}
