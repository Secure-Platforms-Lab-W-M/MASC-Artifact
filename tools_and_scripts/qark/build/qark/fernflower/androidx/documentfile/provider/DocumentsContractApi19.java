package androidx.documentfile.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.text.TextUtils;

class DocumentsContractApi19 {
   private static final int FLAG_VIRTUAL_DOCUMENT = 512;
   private static final String TAG = "DocumentFile";

   private DocumentsContractApi19() {
   }

   public static boolean canRead(Context var0, Uri var1) {
      if (var0.checkCallingOrSelfUriPermission(var1, 1) != 0) {
         return false;
      } else {
         return !TextUtils.isEmpty(getRawType(var0, var1));
      }
   }

   public static boolean canWrite(Context var0, Uri var1) {
      if (var0.checkCallingOrSelfUriPermission(var1, 2) != 0) {
         return false;
      } else {
         String var3 = getRawType(var0, var1);
         int var2 = queryForInt(var0, var1, "flags", 0);
         if (TextUtils.isEmpty(var3)) {
            return false;
         } else if ((var2 & 4) != 0) {
            return true;
         } else if ("vnd.android.document/directory".equals(var3) && (var2 & 8) != 0) {
            return true;
         } else {
            return !TextUtils.isEmpty(var3) && (var2 & 2) != 0;
         }
      }
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

   public static boolean exists(Context param0, Uri param1) {
      // $FF: Couldn't be decompiled
   }

   public static long getFlags(Context var0, Uri var1) {
      return queryForLong(var0, var1, "flags", 0L);
   }

   public static String getName(Context var0, Uri var1) {
      return queryForString(var0, var1, "_display_name", (String)null);
   }

   private static String getRawType(Context var0, Uri var1) {
      return queryForString(var0, var1, "mime_type", (String)null);
   }

   public static String getType(Context var0, Uri var1) {
      String var2 = getRawType(var0, var1);
      return "vnd.android.document/directory".equals(var2) ? null : var2;
   }

   public static boolean isDirectory(Context var0, Uri var1) {
      return "vnd.android.document/directory".equals(getRawType(var0, var1));
   }

   public static boolean isFile(Context var0, Uri var1) {
      String var2 = getRawType(var0, var1);
      return !"vnd.android.document/directory".equals(var2) && !TextUtils.isEmpty(var2);
   }

   public static boolean isVirtual(Context var0, Uri var1) {
      boolean var3 = DocumentsContract.isDocumentUri(var0, var1);
      boolean var2 = false;
      if (!var3) {
         return false;
      } else {
         if ((getFlags(var0, var1) & 512L) != 0L) {
            var2 = true;
         }

         return var2;
      }
   }

   public static long lastModified(Context var0, Uri var1) {
      return queryForLong(var0, var1, "last_modified", 0L);
   }

   public static long length(Context var0, Uri var1) {
      return queryForLong(var0, var1, "_size", 0L);
   }

   private static int queryForInt(Context var0, Uri var1, String var2, int var3) {
      return (int)queryForLong(var0, var1, var2, (long)var3);
   }

   private static long queryForLong(Context param0, Uri param1, String param2, long param3) {
      // $FF: Couldn't be decompiled
   }

   private static String queryForString(Context param0, Uri param1, String param2, String param3) {
      // $FF: Couldn't be decompiled
   }
}
