package org.apache.http.entity.mime.content;

import java.nio.charset.Charset;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public abstract class AbstractContentBody implements ContentBody {
   private final ContentType contentType;

   @Deprecated
   public AbstractContentBody(String var1) {
      this(ContentType.parse(var1));
   }

   public AbstractContentBody(ContentType var1) {
      Args.notNull(var1, "Content type");
      this.contentType = var1;
   }

   public String getCharset() {
      Charset var1 = this.contentType.getCharset();
      return var1 != null ? var1.name() : null;
   }

   public ContentType getContentType() {
      return this.contentType;
   }

   public String getMediaType() {
      String var2 = this.contentType.getMimeType();
      int var1 = var2.indexOf(47);
      return var1 != -1 ? var2.substring(0, var1) : var2;
   }

   public String getMimeType() {
      return this.contentType.getMimeType();
   }

   public String getSubType() {
      String var2 = this.contentType.getMimeType();
      int var1 = var2.indexOf(47);
      return var1 != -1 ? var2.substring(var1 + 1) : null;
   }
}
