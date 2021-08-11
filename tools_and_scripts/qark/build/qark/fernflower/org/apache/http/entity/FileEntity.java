package org.apache.http.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.util.Args;

public class FileEntity extends AbstractHttpEntity implements Cloneable {
   protected final File file;

   public FileEntity(File var1) {
      this.file = (File)Args.notNull(var1, "File");
   }

   @Deprecated
   public FileEntity(File var1, String var2) {
      this.file = (File)Args.notNull(var1, "File");
      this.setContentType(var2);
   }

   public FileEntity(File var1, ContentType var2) {
      this.file = (File)Args.notNull(var1, "File");
      if (var2 != null) {
         this.setContentType(var2.toString());
      }

   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public InputStream getContent() throws IOException {
      return new FileInputStream(this.file);
   }

   public long getContentLength() {
      return this.file.length();
   }

   public boolean isRepeatable() {
      return true;
   }

   public boolean isStreaming() {
      return false;
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
