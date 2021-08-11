package androidx.core.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
   private static final String ATTR_NAME = "name";
   private static final String ATTR_PATH = "path";
   private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
   private static final File DEVICE_ROOT = new File("/");
   private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
   private static final String TAG_CACHE_PATH = "cache-path";
   private static final String TAG_EXTERNAL = "external-path";
   private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
   private static final String TAG_EXTERNAL_FILES = "external-files-path";
   private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
   private static final String TAG_FILES_PATH = "files-path";
   private static final String TAG_ROOT_PATH = "root-path";
   private static HashMap sCache = new HashMap();
   private FileProvider.PathStrategy mStrategy;

   private static File buildPath(File var0, String... var1) {
      int var3 = var1.length;

      File var4;
      for(int var2 = 0; var2 < var3; var0 = var4) {
         String var5 = var1[var2];
         var4 = var0;
         if (var5 != null) {
            var4 = new File(var0, var5);
         }

         ++var2;
      }

      return var0;
   }

   private static Object[] copyOf(Object[] var0, int var1) {
      Object[] var2 = new Object[var1];
      System.arraycopy(var0, 0, var2, 0, var1);
      return var2;
   }

   private static String[] copyOf(String[] var0, int var1) {
      String[] var2 = new String[var1];
      System.arraycopy(var0, 0, var2, 0, var1);
      return var2;
   }

   private static FileProvider.PathStrategy getPathStrategy(Context param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static Uri getUriForFile(Context var0, String var1, File var2) {
      return getPathStrategy(var0, var1).getUriForFile(var2);
   }

   private static int modeToMode(String var0) {
      if ("r".equals(var0)) {
         return 268435456;
      } else if (!"w".equals(var0) && !"wt".equals(var0)) {
         if ("wa".equals(var0)) {
            return 704643072;
         } else if ("rw".equals(var0)) {
            return 939524096;
         } else if ("rwt".equals(var0)) {
            return 1006632960;
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append("Invalid mode: ");
            var1.append(var0);
            throw new IllegalArgumentException(var1.toString());
         }
      } else {
         return 738197504;
      }
   }

   private static FileProvider.PathStrategy parsePathStrategy(Context var0, String var1) throws IOException, XmlPullParserException {
      FileProvider.SimplePathStrategy var5 = new FileProvider.SimplePathStrategy(var1);
      ProviderInfo var3 = var0.getPackageManager().resolveContentProvider(var1, 128);
      if (var3 != null) {
         XmlResourceParser var6 = var3.loadXmlMetaData(var0.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
         if (var6 != null) {
            while(true) {
               int var2 = var6.next();
               if (var2 == 1) {
                  return var5;
               }

               if (var2 == 2) {
                  String var9 = var6.getName();
                  String var7 = var6.getAttributeValue((String)null, "name");
                  String var8 = var6.getAttributeValue((String)null, "path");
                  File[] var4 = null;
                  var3 = null;
                  File var11 = null;
                  if ("root-path".equals(var9)) {
                     var11 = DEVICE_ROOT;
                  } else if ("files-path".equals(var9)) {
                     var11 = var0.getFilesDir();
                  } else if ("cache-path".equals(var9)) {
                     var11 = var0.getCacheDir();
                  } else if ("external-path".equals(var9)) {
                     var11 = Environment.getExternalStorageDirectory();
                  } else {
                     File[] var12;
                     if ("external-files-path".equals(var9)) {
                        var12 = ContextCompat.getExternalFilesDirs(var0, (String)null);
                        if (var12.length > 0) {
                           var11 = var12[0];
                        }
                     } else if ("external-cache-path".equals(var9)) {
                        var12 = ContextCompat.getExternalCacheDirs(var0);
                        var11 = var4;
                        if (var12.length > 0) {
                           var11 = var12[0];
                        }
                     } else {
                        var11 = var4;
                        if (VERSION.SDK_INT >= 21) {
                           var11 = var3;
                           if ("external-media-path".equals(var9)) {
                              var4 = var0.getExternalMediaDirs();
                              var11 = var3;
                              if (var4.length > 0) {
                                 var11 = var4[0];
                              }
                           }
                        }
                     }
                  }

                  if (var11 != null) {
                     var5.addRoot(var7, buildPath(var11, var8));
                  }
               }
            }
         } else {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
         }
      } else {
         StringBuilder var10 = new StringBuilder();
         var10.append("Couldn't find meta-data for provider with authority ");
         var10.append(var1);
         throw new IllegalArgumentException(var10.toString());
      }
   }

   public void attachInfo(Context var1, ProviderInfo var2) {
      super.attachInfo(var1, var2);
      if (!var2.exported) {
         if (var2.grantUriPermissions) {
            this.mStrategy = getPathStrategy(var1, var2.authority);
         } else {
            throw new SecurityException("Provider must grant uri permissions");
         }
      } else {
         throw new SecurityException("Provider must not be exported");
      }
   }

   public int delete(Uri var1, String var2, String[] var3) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public String getType(Uri var1) {
      File var3 = this.mStrategy.getFileForUri(var1);
      int var2 = var3.getName().lastIndexOf(46);
      if (var2 >= 0) {
         String var4 = var3.getName().substring(var2 + 1);
         var4 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var4);
         if (var4 != null) {
            return var4;
         }
      }

      return "application/octet-stream";
   }

   public Uri insert(Uri var1, ContentValues var2) {
      throw new UnsupportedOperationException("No external inserts");
   }

   public boolean onCreate() {
      return true;
   }

   public ParcelFileDescriptor openFile(Uri var1, String var2) throws FileNotFoundException {
      return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(var1), modeToMode(var2));
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      File var13 = this.mStrategy.getFileForUri(var1);
      String[] var10 = var2;
      if (var2 == null) {
         var10 = COLUMNS;
      }

      var4 = new String[var10.length];
      Object[] var11 = new Object[var10.length];
      int var8 = 0;
      int var9 = var10.length;

      int var6;
      for(int var7 = 0; var7 < var9; var8 = var6) {
         var5 = var10[var7];
         if ("_display_name".equals(var5)) {
            var4[var8] = "_display_name";
            var11[var8] = var13.getName();
            var6 = var8 + 1;
         } else {
            var6 = var8;
            if ("_size".equals(var5)) {
               var4[var8] = "_size";
               var11[var8] = var13.length();
               var6 = var8 + 1;
            }
         }

         ++var7;
      }

      var10 = copyOf(var4, var8);
      var11 = copyOf(var11, var8);
      MatrixCursor var12 = new MatrixCursor(var10, 1);
      var12.addRow(var11);
      return var12;
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      throw new UnsupportedOperationException("No external updates");
   }

   interface PathStrategy {
      File getFileForUri(Uri var1);

      Uri getUriForFile(File var1);
   }

   static class SimplePathStrategy implements FileProvider.PathStrategy {
      private final String mAuthority;
      private final HashMap mRoots = new HashMap();

      SimplePathStrategy(String var1) {
         this.mAuthority = var1;
      }

      void addRoot(String var1, File var2) {
         if (!TextUtils.isEmpty(var1)) {
            File var5;
            try {
               var5 = var2.getCanonicalFile();
            } catch (IOException var4) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Failed to resolve canonical path for ");
               var3.append(var2);
               throw new IllegalArgumentException(var3.toString(), var4);
            }

            this.mRoots.put(var1, var5);
         } else {
            throw new IllegalArgumentException("Name must not be empty");
         }
      }

      public File getFileForUri(Uri var1) {
         String var4 = var1.getEncodedPath();
         int var2 = var4.indexOf(47, 1);
         String var3 = Uri.decode(var4.substring(1, var2));
         var4 = Uri.decode(var4.substring(var2 + 1));
         File var7 = (File)this.mRoots.get(var3);
         StringBuilder var8;
         if (var7 != null) {
            File var6 = new File(var7, var4);

            File var9;
            try {
               var9 = var6.getCanonicalFile();
            } catch (IOException var5) {
               var8 = new StringBuilder();
               var8.append("Failed to resolve canonical path for ");
               var8.append(var6);
               throw new IllegalArgumentException(var8.toString());
            }

            if (var9.getPath().startsWith(var7.getPath())) {
               return var9;
            } else {
               throw new SecurityException("Resolved path jumped beyond configured root");
            }
         } else {
            var8 = new StringBuilder();
            var8.append("Unable to find configured root for ");
            var8.append(var1);
            throw new IllegalArgumentException(var8.toString());
         }
      }

      public Uri getUriForFile(File var1) {
         String var4;
         try {
            var4 = var1.getCanonicalPath();
         } catch (IOException var7) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Failed to resolve canonical path for ");
            var2.append(var1);
            throw new IllegalArgumentException(var2.toString());
         }

         Entry var8 = null;

         Entry var10;
         for(Iterator var5 = this.mRoots.entrySet().iterator(); var5.hasNext(); var8 = var10) {
            Entry var3 = (Entry)var5.next();
            String var6 = ((File)var3.getValue()).getPath();
            var10 = var8;
            if (var4.startsWith(var6)) {
               if (var8 != null) {
                  var10 = var8;
                  if (var6.length() <= ((File)var8.getValue()).getPath().length()) {
                     continue;
                  }
               }

               var10 = var3;
            }
         }

         if (var8 != null) {
            String var13 = ((File)var8.getValue()).getPath();
            if (var13.endsWith("/")) {
               var13 = var4.substring(var13.length());
            } else {
               var13 = var4.substring(var13.length() + 1);
            }

            StringBuilder var12 = new StringBuilder();
            var12.append(Uri.encode((String)var8.getKey()));
            var12.append('/');
            var12.append(Uri.encode(var13, "/"));
            String var11 = var12.toString();
            return (new Builder()).scheme("content").authority(this.mAuthority).encodedPath(var11).build();
         } else {
            StringBuilder var9 = new StringBuilder();
            var9.append("Failed to find configured root that contains ");
            var9.append(var4);
            throw new IllegalArgumentException(var9.toString());
         }
      }
   }
}
