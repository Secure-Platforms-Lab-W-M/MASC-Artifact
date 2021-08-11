package android.support.v4.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class DocumentsContractApi21 {
   private static final String TAG = "DocumentFile";

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

   public static Uri createDirectory(Context var0, Uri var1, String var2) {
      return createFile(var0, var1, "vnd.android.document/directory", var2);
   }

   public static Uri createFile(Context var0, Uri var1, String var2, String var3) {
      try {
         Uri var5 = DocumentsContract.createDocument(var0.getContentResolver(), var1, var2, var3);
         return var5;
      } catch (Exception var4) {
         return null;
      }
   }

   public static Uri[] listFiles(Context param0, Uri param1) {
      // $FF: Couldn't be decompiled
   }

   public static Uri prepareTreeUri(Uri var0) {
      return DocumentsContract.buildDocumentUriUsingTree(var0, DocumentsContract.getTreeDocumentId(var0));
   }

   public static Uri renameTo(Context var0, Uri var1, String var2) {
      try {
         Uri var4 = DocumentsContract.renameDocument(var0.getContentResolver(), var1, var2);
         return var4;
      } catch (Exception var3) {
         return null;
      }
   }
}
