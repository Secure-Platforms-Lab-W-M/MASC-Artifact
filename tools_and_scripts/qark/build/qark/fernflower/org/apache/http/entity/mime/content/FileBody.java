package org.apache.http.entity.mime.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class FileBody extends AbstractContentBody {
   private final File file;
   private final String filename;

   public FileBody(File var1) {
      ContentType var3 = ContentType.DEFAULT_BINARY;
      String var2;
      if (var1 != null) {
         var2 = var1.getName();
      } else {
         var2 = null;
      }

      this(var1, var3, var2);
   }

   @Deprecated
   public FileBody(File var1, String var2) {
      this(var1, (ContentType)ContentType.create(var2), (String)null);
   }

   @Deprecated
   public FileBody(File var1, String var2, String var3) {
      this(var1, (String)null, var2, var3);
   }

   @Deprecated
   public FileBody(File var1, String var2, String var3, String var4) {
      this(var1, ContentType.create(var3, var4), var2);
   }

   public FileBody(File var1, ContentType var2) {
      String var3;
      if (var1 != null) {
         var3 = var1.getName();
      } else {
         var3 = null;
      }

      this(var1, var2, var3);
   }

   public FileBody(File var1, ContentType var2, String var3) {
      super(var2);
      Args.notNull(var1, "File");
      this.file = var1;
      if (var3 == null) {
         var3 = var1.getName();
      }

      this.filename = var3;
   }

   public long getContentLength() {
      return this.file.length();
   }

   public File getFile() {
      return this.file;
   }

   public String getFilename() {
      return this.filename;
   }

   public InputStream getInputStream() throws IOException {
      return new FileInputStream(this.file);
   }

   public String getTransferEncoding() {
      return "binary";
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      FileInputStream var3 = new FileInputStream(this.file);

      label171: {
         Throwable var10000;
         label170: {
            boolean var10001;
            byte[] var4;
            try {
               var4 = new byte[4096];
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label170;
            }

            while(true) {
               int var2;
               try {
                  var2 = var3.read(var4);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }

               if (var2 == -1) {
                  try {
                     var1.flush();
                     break label171;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var1.write(var4, 0, var2);
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var25 = var10000;
         var3.close();
         throw var25;
      }

      var3.close();
   }
}
