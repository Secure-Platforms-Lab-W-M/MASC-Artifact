package okhttp3.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import okio.Okio;
import okio.Sink;
import okio.Source;

public interface FileSystem {
   FileSystem SYSTEM = new FileSystem() {
      public Sink appendingSink(File var1) throws FileNotFoundException {
         try {
            Sink var2 = Okio.appendingSink(var1);
            return var2;
         } catch (FileNotFoundException var3) {
            var1.getParentFile().mkdirs();
            return Okio.appendingSink(var1);
         }
      }

      public void delete(File var1) throws IOException {
         if (!var1.delete()) {
            if (var1.exists()) {
               StringBuilder var2 = new StringBuilder();
               var2.append("failed to delete ");
               var2.append(var1);
               throw new IOException(var2.toString());
            }
         }
      }

      public void deleteContents(File var1) throws IOException {
         File[] var4 = var1.listFiles();
         StringBuilder var5;
         if (var4 != null) {
            int var3 = var4.length;

            for(int var2 = 0; var2 < var3; ++var2) {
               var1 = var4[var2];
               if (var1.isDirectory()) {
                  this.deleteContents(var1);
               }

               if (!var1.delete()) {
                  var5 = new StringBuilder();
                  var5.append("failed to delete ");
                  var5.append(var1);
                  throw new IOException(var5.toString());
               }
            }

         } else {
            var5 = new StringBuilder();
            var5.append("not a readable directory: ");
            var5.append(var1);
            throw new IOException(var5.toString());
         }
      }

      public boolean exists(File var1) {
         return var1.exists();
      }

      public void rename(File var1, File var2) throws IOException {
         this.delete(var2);
         if (!var1.renameTo(var2)) {
            StringBuilder var3 = new StringBuilder();
            var3.append("failed to rename ");
            var3.append(var1);
            var3.append(" to ");
            var3.append(var2);
            throw new IOException(var3.toString());
         }
      }

      public Sink sink(File var1) throws FileNotFoundException {
         try {
            Sink var2 = Okio.sink(var1);
            return var2;
         } catch (FileNotFoundException var3) {
            var1.getParentFile().mkdirs();
            return Okio.sink(var1);
         }
      }

      public long size(File var1) {
         return var1.length();
      }

      public Source source(File var1) throws FileNotFoundException {
         return Okio.source(var1);
      }
   };

   Sink appendingSink(File var1) throws FileNotFoundException;

   void delete(File var1) throws IOException;

   void deleteContents(File var1) throws IOException;

   boolean exists(File var1);

   void rename(File var1, File var2) throws IOException;

   Sink sink(File var1) throws FileNotFoundException;

   long size(File var1);

   Source source(File var1) throws FileNotFoundException;
}
