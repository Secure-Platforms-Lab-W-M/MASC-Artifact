package org.apache.http.entity.mime;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.util.Args;

public class FormBodyPart {
   private final ContentBody body;
   private final Header header;
   private final String name;

   @Deprecated
   public FormBodyPart(String var1, ContentBody var2) {
      Args.notNull(var1, "Name");
      Args.notNull(var2, "Body");
      this.name = var1;
      this.body = var2;
      this.header = new Header();
      this.generateContentDisp(var2);
      this.generateContentType(var2);
      this.generateTransferEncoding(var2);
   }

   FormBodyPart(String var1, ContentBody var2, Header var3) {
      Args.notNull(var1, "Name");
      Args.notNull(var2, "Body");
      this.name = var1;
      this.body = var2;
      if (var3 == null) {
         var3 = new Header();
      }

      this.header = var3;
   }

   public void addField(String var1, String var2) {
      Args.notNull(var1, "Field name");
      this.header.addField(new MinimalField(var1, var2));
   }

   @Deprecated
   protected void generateContentDisp(ContentBody var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("form-data; name=\"");
      var2.append(this.getName());
      var2.append("\"");
      if (var1.getFilename() != null) {
         var2.append("; filename=\"");
         var2.append(var1.getFilename());
         var2.append("\"");
      }

      this.addField("Content-Disposition", var2.toString());
   }

   @Deprecated
   protected void generateContentType(ContentBody var1) {
      ContentType var2;
      if (var1 instanceof AbstractContentBody) {
         var2 = ((AbstractContentBody)var1).getContentType();
      } else {
         var2 = null;
      }

      if (var2 != null) {
         this.addField("Content-Type", var2.toString());
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getMimeType());
         if (var1.getCharset() != null) {
            var3.append("; charset=");
            var3.append(var1.getCharset());
         }

         this.addField("Content-Type", var3.toString());
      }
   }

   @Deprecated
   protected void generateTransferEncoding(ContentBody var1) {
      this.addField("Content-Transfer-Encoding", var1.getTransferEncoding());
   }

   public ContentBody getBody() {
      return this.body;
   }

   public Header getHeader() {
      return this.header;
   }

   public String getName() {
      return this.name;
   }
}
