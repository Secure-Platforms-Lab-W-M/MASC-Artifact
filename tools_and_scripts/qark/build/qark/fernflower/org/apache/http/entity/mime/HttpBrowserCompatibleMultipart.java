package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

class HttpBrowserCompatibleMultipart extends AbstractMultipartForm {
   private final List parts;

   public HttpBrowserCompatibleMultipart(Charset var1, String var2, List var3) {
      super(var1, var2);
      this.parts = var3;
   }

   protected void formatMultipartHeader(FormBodyPart var1, OutputStream var2) throws IOException {
      Header var3 = var1.getHeader();
      writeField(var3.getField("Content-Disposition"), this.charset, var2);
      if (var1.getBody().getFilename() != null) {
         writeField(var3.getField("Content-Type"), this.charset, var2);
      }

   }

   public List getBodyParts() {
      return this.parts;
   }
}
