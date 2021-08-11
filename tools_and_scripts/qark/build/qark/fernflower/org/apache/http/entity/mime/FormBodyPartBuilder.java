package org.apache.http.entity.mime;

import java.util.Iterator;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

public class FormBodyPartBuilder {
   private ContentBody body;
   private final Header header;
   private String name;

   FormBodyPartBuilder() {
      this.header = new Header();
   }

   FormBodyPartBuilder(String var1, ContentBody var2) {
      this();
      this.name = var1;
      this.body = var2;
   }

   public static FormBodyPartBuilder create() {
      return new FormBodyPartBuilder();
   }

   public static FormBodyPartBuilder create(String var0, ContentBody var1) {
      return new FormBodyPartBuilder(var0, var1);
   }

   private static String encodeForHeader(String var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var3 = new StringBuilder();

         for(int var2 = 0; var2 < var0.length(); ++var2) {
            char var1 = var0.charAt(var2);
            if (var1 == '"' || var1 == '\\' || var1 == '\r') {
               var3.append("\\");
            }

            var3.append(var1);
         }

         return var3.toString();
      }
   }

   public FormBodyPartBuilder addField(String var1, String var2) {
      Args.notNull(var1, "Field name");
      this.header.addField(new MinimalField(var1, var2));
      return this;
   }

   public FormBodyPart build() {
      Asserts.notBlank(this.name, "Name");
      Asserts.notNull(this.body, "Content body");
      Header var2 = new Header();
      Iterator var1 = this.header.getFields().iterator();

      while(var1.hasNext()) {
         var2.addField((MinimalField)var1.next());
      }

      StringBuilder var3;
      if (var2.getField("Content-Disposition") == null) {
         var3 = new StringBuilder();
         var3.append("form-data; name=\"");
         var3.append(encodeForHeader(this.name));
         var3.append("\"");
         if (this.body.getFilename() != null) {
            var3.append("; filename=\"");
            var3.append(encodeForHeader(this.body.getFilename()));
            var3.append("\"");
         }

         var2.addField(new MinimalField("Content-Disposition", var3.toString()));
      }

      if (var2.getField("Content-Type") == null) {
         ContentBody var4 = this.body;
         ContentType var5;
         if (var4 instanceof AbstractContentBody) {
            var5 = ((AbstractContentBody)var4).getContentType();
         } else {
            var5 = null;
         }

         if (var5 != null) {
            var2.addField(new MinimalField("Content-Type", var5.toString()));
         } else {
            var3 = new StringBuilder();
            var3.append(this.body.getMimeType());
            if (this.body.getCharset() != null) {
               var3.append("; charset=");
               var3.append(this.body.getCharset());
            }

            var2.addField(new MinimalField("Content-Type", var3.toString()));
         }
      }

      if (var2.getField("Content-Transfer-Encoding") == null) {
         var2.addField(new MinimalField("Content-Transfer-Encoding", this.body.getTransferEncoding()));
      }

      return new FormBodyPart(this.name, this.body, var2);
   }

   public FormBodyPartBuilder removeFields(String var1) {
      Args.notNull(var1, "Field name");
      this.header.removeFields(var1);
      return this;
   }

   public FormBodyPartBuilder setBody(ContentBody var1) {
      this.body = var1;
      return this;
   }

   public FormBodyPartBuilder setField(String var1, String var2) {
      Args.notNull(var1, "Field name");
      this.header.setField(new MinimalField(var1, var2));
      return this;
   }

   public FormBodyPartBuilder setName(String var1) {
      this.name = var1;
      return this;
   }
}
