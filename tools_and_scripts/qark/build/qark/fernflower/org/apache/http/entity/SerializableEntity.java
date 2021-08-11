package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.apache.http.util.Args;

public class SerializableEntity extends AbstractHttpEntity {
   private Serializable objRef;
   private byte[] objSer;

   public SerializableEntity(Serializable var1) {
      Args.notNull(var1, "Source object");
      this.objRef = var1;
   }

   public SerializableEntity(Serializable var1, boolean var2) throws IOException {
      Args.notNull(var1, "Source object");
      if (var2) {
         this.createBytes(var1);
      } else {
         this.objRef = var1;
      }
   }

   private void createBytes(Serializable var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      ObjectOutputStream var3 = new ObjectOutputStream(var2);
      var3.writeObject(var1);
      var3.flush();
      this.objSer = var2.toByteArray();
   }

   public InputStream getContent() throws IOException, IllegalStateException {
      if (this.objSer == null) {
         this.createBytes(this.objRef);
      }

      return new ByteArrayInputStream(this.objSer);
   }

   public long getContentLength() {
      byte[] var1 = this.objSer;
      return var1 == null ? -1L : (long)var1.length;
   }

   public boolean isRepeatable() {
      return true;
   }

   public boolean isStreaming() {
      return this.objSer == null;
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      byte[] var2 = this.objSer;
      if (var2 == null) {
         ObjectOutputStream var3 = new ObjectOutputStream(var1);
         var3.writeObject(this.objRef);
         var3.flush();
      } else {
         var1.write(var2);
         var1.flush();
      }
   }
}
