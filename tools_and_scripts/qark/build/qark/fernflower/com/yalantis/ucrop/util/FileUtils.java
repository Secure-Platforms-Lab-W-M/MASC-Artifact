package com.yalantis.ucrop.util;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {
   private static final String TAG = "FileUtils";

   private FileUtils() {
   }

   public static void copyFile(String var0, String var1) throws IOException {
      if (!var0.equalsIgnoreCase(var1)) {
         Object var4 = null;
         FileChannel var3 = null;
         FileChannel var2 = (FileChannel)var4;

         FileChannel var25;
         FileChannel var27;
         label294: {
            Throwable var10000;
            label295: {
               boolean var10001;
               try {
                  var25 = (new FileInputStream(new File(var0))).getChannel();
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label295;
               }

               var2 = (FileChannel)var4;
               var3 = var25;

               try {
                  var27 = (new FileOutputStream(new File(var1))).getChannel();
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label295;
               }

               var2 = var27;
               var3 = var25;

               try {
                  var25.transferTo(0L, var25.size(), var27);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label295;
               }

               var2 = var27;
               var3 = var25;

               label275:
               try {
                  var25.close();
                  break label294;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label275;
               }
            }

            Throwable var26 = var10000;
            if (var3 != null) {
               var3.close();
            }

            if (var2 != null) {
               var2.close();
            }

            throw var26;
         }

         if (var25 != null) {
            var25.close();
         }

         if (var27 != null) {
            var27.close();
         }

      }
   }

   public static String getDataColumn(Context param0, Uri param1, String param2, String[] param3) {
      // $FF: Couldn't be decompiled
   }

   public static String getPath(Context var0, Uri var1) {
      boolean var2;
      if (VERSION.SDK_INT >= 19) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2 && DocumentsContract.isDocumentUri(var0, var1)) {
         if (isExternalStorageDocument(var1)) {
            String[] var7 = DocumentsContract.getDocumentId(var1).split(":");
            if ("primary".equalsIgnoreCase(var7[0])) {
               StringBuilder var9 = new StringBuilder();
               var9.append(Environment.getExternalStorageDirectory());
               var9.append("/");
               var9.append(var7[1]);
               return var9.toString();
            }

            return null;
         }

         if (isDownloadsDocument(var1)) {
            String var8 = DocumentsContract.getDocumentId(var1);
            if (!TextUtils.isEmpty(var8)) {
               try {
                  String var6 = getDataColumn(var0, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(var8)), (String)null, (String[])null);
                  return var6;
               } catch (NumberFormatException var5) {
                  Log.i("FileUtils", var5.getMessage());
                  return null;
               }
            }

            return null;
         }

         if (isMediaDocument(var1)) {
            String[] var3 = DocumentsContract.getDocumentId(var1).split(":");
            String var4 = var3[0];
            var1 = null;
            if ("image".equals(var4)) {
               var1 = Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(var4)) {
               var1 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(var4)) {
               var1 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            return getDataColumn(var0, var1, "_id=?", new String[]{var3[1]});
         }
      } else {
         if ("content".equalsIgnoreCase(var1.getScheme())) {
            if (isGooglePhotosUri(var1)) {
               return var1.getLastPathSegment();
            }

            return getDataColumn(var0, var1, (String)null, (String[])null);
         }

         if ("file".equalsIgnoreCase(var1.getScheme())) {
            return var1.getPath();
         }
      }

      return null;
   }

   public static boolean isDownloadsDocument(Uri var0) {
      return "com.android.providers.downloads.documents".equals(var0.getAuthority());
   }

   public static boolean isExternalStorageDocument(Uri var0) {
      return "com.android.externalstorage.documents".equals(var0.getAuthority());
   }

   public static boolean isGooglePhotosUri(Uri var0) {
      return "com.google.android.apps.photos.content".equals(var0.getAuthority());
   }

   public static boolean isMediaDocument(Uri var0) {
      return "com.android.providers.media.documents".equals(var0.getAuthority());
   }
}
