package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.Args;

public class DecompressingEntity extends HttpEntityWrapper {
   private static final int BUFFER_SIZE = 2048;
   private InputStream content;
   private final InputStreamFactory inputStreamFactory;

   public DecompressingEntity(HttpEntity var1, InputStreamFactory var2) {
      super(var1);
      this.inputStreamFactory = var2;
   }

   private InputStream getDecompressingStream() throws IOException {
      return new LazyDecompressingInputStream(this.wrappedEntity.getContent(), this.inputStreamFactory);
   }

   public InputStream getContent() throws IOException {
      if (this.wrappedEntity.isStreaming()) {
         if (this.content == null) {
            this.content = this.getDecompressingStream();
         }

         return this.content;
      } else {
         return this.getDecompressingStream();
      }
   }

   public Header getContentEncoding() {
      return null;
   }

   public long getContentLength() {
      return -1L;
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      InputStream var3 = this.getContent();

      Throwable var10000;
      label123: {
         boolean var10001;
         byte[] var4;
         try {
            var4 = new byte[2048];
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label123;
         }

         while(true) {
            int var2;
            try {
               var2 = var3.read(var4);
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break;
            }

            if (var2 == -1) {
               var3.close();
               return;
            }

            try {
               var1.write(var4, 0, var2);
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var17 = var10000;
      var3.close();
      throw var17;
   }
}
