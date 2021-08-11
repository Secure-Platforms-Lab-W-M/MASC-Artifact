package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

class HttpStrictMultipart extends AbstractMultipartForm {
   private final List parts;

   public HttpStrictMultipart(Charset var1, String var2, List var3) {
      super(var1, var2);
      this.parts = var3;
   }

   protected void formatMultipartHeader(FormBodyPart var1, OutputStream var2) throws IOException {
      Iterator var3 = var1.getHeader().iterator();

      while(var3.hasNext()) {
         writeField((MinimalField)var3.next(), var2);
      }

   }

   public List getBodyParts() {
      return this.parts;
   }
}
