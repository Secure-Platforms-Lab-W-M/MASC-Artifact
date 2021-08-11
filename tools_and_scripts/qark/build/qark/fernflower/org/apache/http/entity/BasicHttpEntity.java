package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.impl.io.EmptyInputStream;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

public class BasicHttpEntity extends AbstractHttpEntity {
   private InputStream content;
   private long length = -1L;

   public InputStream getContent() throws IllegalStateException {
      boolean var1;
      if (this.content != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Asserts.check(var1, "Content has not been provided");
      return this.content;
   }

   public long getContentLength() {
      return this.length;
   }

   public boolean isRepeatable() {
      return false;
   }

   public boolean isStreaming() {
      InputStream var1 = this.content;
      return var1 != null && var1 != EmptyInputStream.INSTANCE;
   }

   public void setContent(InputStream var1) {
      this.content = var1;
   }

   public void setContentLength(long var1) {
      this.length = var1;
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      InputStream var3 = this.getContent();

      Throwable var10000;
      label123: {
         boolean var10001;
         byte[] var4;
         try {
            var4 = new byte[4096];
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
