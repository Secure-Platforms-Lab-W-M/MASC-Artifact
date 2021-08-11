package androidx.documentfile.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;

class TreeDocumentFile extends DocumentFile {
   private Context mContext;
   private Uri mUri;

   TreeDocumentFile(DocumentFile var1, Context var2, Uri var3) {
      super(var1);
      this.mContext = var2;
      this.mUri = var3;
   }

   private static void closeQuietly(AutoCloseable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }
   }

   private static Uri createFile(Context var0, Uri var1, String var2, String var3) {
      try {
         Uri var5 = DocumentsContract.createDocument(var0.getContentResolver(), var1, var2, var3);
         return var5;
      } catch (Exception var4) {
         return null;
      }
   }

   public boolean canRead() {
      return DocumentsContractApi19.canRead(this.mContext, this.mUri);
   }

   public boolean canWrite() {
      return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
   }

   public DocumentFile createDirectory(String var1) {
      Uri var2 = createFile(this.mContext, this.mUri, "vnd.android.document/directory", var1);
      return var2 != null ? new TreeDocumentFile(this, this.mContext, var2) : null;
   }

   public DocumentFile createFile(String var1, String var2) {
      Uri var3 = createFile(this.mContext, this.mUri, var1, var2);
      return var3 != null ? new TreeDocumentFile(this, this.mContext, var3) : null;
   }

   public boolean delete() {
      try {
         boolean var1 = DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
         return var1;
      } catch (Exception var3) {
         return false;
      }
   }

   public boolean exists() {
      return DocumentsContractApi19.exists(this.mContext, this.mUri);
   }

   public String getName() {
      return DocumentsContractApi19.getName(this.mContext, this.mUri);
   }

   public String getType() {
      return DocumentsContractApi19.getType(this.mContext, this.mUri);
   }

   public Uri getUri() {
      return this.mUri;
   }

   public boolean isDirectory() {
      return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
   }

   public boolean isFile() {
      return DocumentsContractApi19.isFile(this.mContext, this.mUri);
   }

   public boolean isVirtual() {
      return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
   }

   public long lastModified() {
      return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
   }

   public long length() {
      return DocumentsContractApi19.length(this.mContext, this.mUri);
   }

   public DocumentFile[] listFiles() {
      // $FF: Couldn't be decompiled
   }

   public boolean renameTo(String param1) {
      // $FF: Couldn't be decompiled
   }
}
