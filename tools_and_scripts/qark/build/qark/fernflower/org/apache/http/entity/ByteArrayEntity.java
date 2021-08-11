package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.util.Args;

public class ByteArrayEntity extends AbstractHttpEntity implements Cloneable {
   // $FF: renamed from: b byte[]
   private final byte[] field_98;
   @Deprecated
   protected final byte[] content;
   private final int len;
   private final int off;

   public ByteArrayEntity(byte[] var1) {
      this(var1, (ContentType)null);
   }

   public ByteArrayEntity(byte[] var1, int var2, int var3) {
      this(var1, var2, var3, (ContentType)null);
   }

   public ByteArrayEntity(byte[] var1, int var2, int var3, ContentType var4) {
      Args.notNull(var1, "Source byte array");
      if (var2 >= 0 && var2 <= var1.length && var3 >= 0 && var2 + var3 >= 0 && var2 + var3 <= var1.length) {
         this.content = var1;
         this.field_98 = var1;
         this.off = var2;
         this.len = var3;
         if (var4 != null) {
            this.setContentType(var4.toString());
         }

      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("off: ");
         var5.append(var2);
         var5.append(" len: ");
         var5.append(var3);
         var5.append(" b.length: ");
         var5.append(var1.length);
         throw new IndexOutOfBoundsException(var5.toString());
      }
   }

   public ByteArrayEntity(byte[] var1, ContentType var2) {
      Args.notNull(var1, "Source byte array");
      this.content = var1;
      this.field_98 = var1;
      this.off = 0;
      this.len = var1.length;
      if (var2 != null) {
         this.setContentType(var2.toString());
      }

   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public InputStream getContent() {
      return new ByteArrayInputStream(this.field_98, this.off, this.len);
   }

   public long getContentLength() {
      return (long)this.len;
   }

   public boolean isRepeatable() {
      return true;
   }

   public boolean isStreaming() {
      return false;
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      var1.write(this.field_98, this.off, this.len);
      var1.flush();
   }
}
