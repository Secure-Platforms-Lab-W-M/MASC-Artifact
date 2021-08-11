package androidx.core.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
   private final File mBackupName;
   private final File mBaseName;

   public AtomicFile(File var1) {
      this.mBaseName = var1;
      StringBuilder var2 = new StringBuilder();
      var2.append(var1.getPath());
      var2.append(".bak");
      this.mBackupName = new File(var2.toString());
   }

   private static boolean sync(FileOutputStream var0) {
      try {
         var0.getFD().sync();
         return true;
      } catch (IOException var1) {
         return false;
      }
   }

   public void delete() {
      this.mBaseName.delete();
      this.mBackupName.delete();
   }

   public void failWrite(FileOutputStream var1) {
      if (var1 != null) {
         sync(var1);

         try {
            var1.close();
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
            return;
         } catch (IOException var2) {
            Log.w("AtomicFile", "failWrite: Got exception:", var2);
         }
      }

   }

   public void finishWrite(FileOutputStream var1) {
      if (var1 != null) {
         sync(var1);

         try {
            var1.close();
            this.mBackupName.delete();
            return;
         } catch (IOException var2) {
            Log.w("AtomicFile", "finishWrite: Got exception:", var2);
         }
      }

   }

   public File getBaseFile() {
      return this.mBaseName;
   }

   public FileInputStream openRead() throws FileNotFoundException {
      if (this.mBackupName.exists()) {
         this.mBaseName.delete();
         this.mBackupName.renameTo(this.mBaseName);
      }

      return new FileInputStream(this.mBaseName);
   }

   public byte[] readFully() throws IOException {
      FileInputStream var5 = this.openRead();
      int var1 = 0;

      Throwable var10000;
      label215: {
         byte[] var3;
         boolean var10001;
         try {
            var3 = new byte[var5.available()];
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label215;
         }

         while(true) {
            int var2;
            try {
               var2 = var5.read(var3, var1, var3.length - var1);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }

            if (var2 <= 0) {
               var5.close();
               return var3;
            }

            var1 += var2;

            try {
               var2 = var5.available();
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            byte[] var4 = var3;

            try {
               if (var2 > var3.length - var1) {
                  var4 = new byte[var1 + var2];
                  System.arraycopy(var3, 0, var4, 0, var1);
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            var3 = var4;
         }
      }

      Throwable var26 = var10000;
      var5.close();
      throw var26;
   }

   public FileOutputStream startWrite() throws IOException {
      StringBuilder var1;
      if (this.mBaseName.exists()) {
         if (!this.mBackupName.exists()) {
            if (!this.mBaseName.renameTo(this.mBackupName)) {
               var1 = new StringBuilder();
               var1.append("Couldn't rename file ");
               var1.append(this.mBaseName);
               var1.append(" to backup file ");
               var1.append(this.mBackupName);
               Log.w("AtomicFile", var1.toString());
            }
         } else {
            this.mBaseName.delete();
         }
      }

      FileOutputStream var4;
      try {
         var4 = new FileOutputStream(this.mBaseName);
         return var4;
      } catch (FileNotFoundException var3) {
         if (this.mBaseName.getParentFile().mkdirs()) {
            try {
               var4 = new FileOutputStream(this.mBaseName);
               return var4;
            } catch (FileNotFoundException var2) {
               var1 = new StringBuilder();
               var1.append("Couldn't create ");
               var1.append(this.mBaseName);
               throw new IOException(var1.toString());
            }
         } else {
            var1 = new StringBuilder();
            var1.append("Couldn't create directory ");
            var1.append(this.mBaseName);
            throw new IOException(var1.toString());
         }
      }
   }
}
