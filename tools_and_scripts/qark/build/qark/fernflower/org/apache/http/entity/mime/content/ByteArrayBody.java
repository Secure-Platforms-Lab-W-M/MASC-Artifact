package org.apache.http.entity.mime.content;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class ByteArrayBody extends AbstractContentBody {
   private final byte[] data;
   private final String filename;

   public ByteArrayBody(byte[] var1, String var2) {
      this(var1, "application/octet-stream", var2);
   }

   @Deprecated
   public ByteArrayBody(byte[] var1, String var2, String var3) {
      this(var1, ContentType.create(var2), var3);
   }

   public ByteArrayBody(byte[] var1, ContentType var2, String var3) {
      super(var2);
      Args.notNull(var1, "byte[]");
      this.data = var1;
      this.filename = var3;
   }

   public String getCharset() {
      return null;
   }

   public long getContentLength() {
      return (long)this.data.length;
   }

   public String getFilename() {
      return this.filename;
   }

   public String getTransferEncoding() {
      return "binary";
   }

   public void writeTo(OutputStream var1) throws IOException {
      var1.write(this.data);
   }
}
