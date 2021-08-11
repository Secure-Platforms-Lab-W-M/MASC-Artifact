package org.apache.commons.text.lookup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

final class UrlEncoderStringLookup extends AbstractStringLookup {
   static final UrlEncoderStringLookup INSTANCE = new UrlEncoderStringLookup();

   private UrlEncoderStringLookup() {
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = StandardCharsets.UTF_8.name();

         try {
            String var3 = URLEncoder.encode(var1, var2);
            return var3;
         } catch (UnsupportedEncodingException var4) {
            throw IllegalArgumentExceptions.format(var4, "%s: source=%s, encoding=%s", var4, var1, var2);
         }
      }
   }
}
