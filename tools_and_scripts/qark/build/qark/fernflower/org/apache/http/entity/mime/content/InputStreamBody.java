package org.apache.http.entity.mime.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class InputStreamBody extends AbstractContentBody {
   private final String filename;
   // $FF: renamed from: in java.io.InputStream
   private final InputStream field_113;

   public InputStreamBody(InputStream var1, String var2) {
      this(var1, ContentType.DEFAULT_BINARY, var2);
   }

   @Deprecated
   public InputStreamBody(InputStream var1, String var2, String var3) {
      this(var1, ContentType.create(var2), var3);
   }

   public InputStreamBody(InputStream var1, ContentType var2) {
      this(var1, (ContentType)var2, (String)null);
   }

   public InputStreamBody(InputStream var1, ContentType var2, String var3) {
      super(var2);
      Args.notNull(var1, "Input stream");
      this.field_113 = var1;
      this.filename = var3;
   }

   public long getContentLength() {
      return -1L;
   }

   public String getFilename() {
      return this.filename;
   }

   public InputStream getInputStream() {
      return this.field_113;
   }

   public String getTransferEncoding() {
      return "binary";
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");

      label171: {
         Throwable var10000;
         label170: {
            boolean var10001;
            byte[] var3;
            try {
               var3 = new byte[4096];
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label170;
            }

            while(true) {
               int var2;
               try {
                  var2 = this.field_113.read(var3);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break;
               }

               if (var2 == -1) {
                  try {
                     var1.flush();
                     break label171;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var1.write(var3, 0, var2);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var24 = var10000;
         this.field_113.close();
         throw var24;
      }

      this.field_113.close();
   }
}
