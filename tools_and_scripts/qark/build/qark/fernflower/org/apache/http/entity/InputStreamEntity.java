package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.util.Args;

public class InputStreamEntity extends AbstractHttpEntity {
   private final InputStream content;
   private final long length;

   public InputStreamEntity(InputStream var1) {
      this(var1, -1L);
   }

   public InputStreamEntity(InputStream var1, long var2) {
      this(var1, var2, (ContentType)null);
   }

   public InputStreamEntity(InputStream var1, long var2, ContentType var4) {
      this.content = (InputStream)Args.notNull(var1, "Source input stream");
      this.length = var2;
      if (var4 != null) {
         this.setContentType(var4.toString());
      }

   }

   public InputStreamEntity(InputStream var1, ContentType var2) {
      this(var1, -1L, var2);
   }

   public InputStream getContent() throws IOException {
      return this.content;
   }

   public long getContentLength() {
      return this.length;
   }

   public boolean isRepeatable() {
      return false;
   }

   public boolean isStreaming() {
      return true;
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      InputStream var5 = this.content;

      label408: {
         Throwable var10000;
         label412: {
            int var2;
            byte[] var6;
            boolean var10001;
            label406: {
               try {
                  var6 = new byte[4096];
                  if (this.length >= 0L) {
                     break label406;
                  }
               } catch (Throwable var48) {
                  var10000 = var48;
                  var10001 = false;
                  break label412;
               }

               while(true) {
                  try {
                     var2 = var5.read(var6);
                  } catch (Throwable var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label412;
                  }

                  if (var2 == -1) {
                     break label408;
                  }

                  try {
                     var1.write(var6, 0, var2);
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label412;
                  }
               }
            }

            long var3;
            try {
               var3 = this.length;
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label412;
            }

            while(true) {
               if (var3 <= 0L) {
                  break label408;
               }

               try {
                  var2 = var5.read(var6, 0, (int)Math.min(4096L, var3));
               } catch (Throwable var46) {
                  var10000 = var46;
                  var10001 = false;
                  break;
               }

               if (var2 == -1) {
                  break label408;
               }

               try {
                  var1.write(var6, 0, var2);
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break;
               }

               var3 -= (long)var2;
            }
         }

         Throwable var49 = var10000;
         var5.close();
         throw var49;
      }

      var5.close();
   }
}
