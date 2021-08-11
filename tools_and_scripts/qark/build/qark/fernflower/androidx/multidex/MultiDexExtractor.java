package androidx.multidex;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class MultiDexExtractor implements Closeable {
   private static final int BUFFER_SIZE = 16384;
   private static final String DEX_PREFIX = "classes";
   static final String DEX_SUFFIX = ".dex";
   private static final String EXTRACTED_NAME_EXT = ".classes";
   static final String EXTRACTED_SUFFIX = ".zip";
   private static final String KEY_CRC = "crc";
   private static final String KEY_DEX_CRC = "dex.crc.";
   private static final String KEY_DEX_NUMBER = "dex.number";
   private static final String KEY_DEX_TIME = "dex.time.";
   private static final String KEY_TIME_STAMP = "timestamp";
   private static final String LOCK_FILENAME = "MultiDex.lock";
   private static final int MAX_EXTRACT_ATTEMPTS = 3;
   private static final long NO_VALUE = -1L;
   private static final String PREFS_FILE = "multidex.version";
   private static final String TAG = "MultiDex";
   private final FileLock cacheLock;
   private final File dexDir;
   private final FileChannel lockChannel;
   private final RandomAccessFile lockRaf;
   private final File sourceApk;
   private final long sourceCrc;

   MultiDexExtractor(File var1, File var2) throws IOException {
      StringBuilder var3 = new StringBuilder();
      var3.append("MultiDexExtractor(");
      var3.append(var1.getPath());
      var3.append(", ");
      var3.append(var2.getPath());
      var3.append(")");
      Log.i("MultiDex", var3.toString());
      this.sourceApk = var1;
      this.dexDir = var2;
      this.sourceCrc = getZipCrc(var1);
      var1 = new File(var2, "MultiDex.lock");
      RandomAccessFile var17 = new RandomAccessFile(var1, "rw");
      this.lockRaf = var17;

      Object var16;
      label57: {
         IOException var20;
         label56: {
            RuntimeException var19;
            label55: {
               Error var10000;
               label61: {
                  boolean var10001;
                  try {
                     this.lockChannel = var17.getChannel();
                  } catch (IOException var13) {
                     var20 = var13;
                     var10001 = false;
                     break label56;
                  } catch (RuntimeException var14) {
                     var19 = var14;
                     var10001 = false;
                     break label55;
                  } catch (Error var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label61;
                  }

                  StringBuilder var18;
                  label50: {
                     try {
                        var18 = new StringBuilder();
                        var18.append("Blocking on lock ");
                        var18.append(var1.getPath());
                        Log.i("MultiDex", var18.toString());
                        this.cacheLock = this.lockChannel.lock();
                        break label50;
                     } catch (IOException var10) {
                        var16 = var10;
                     } catch (RuntimeException var11) {
                        var16 = var11;
                     } catch (Error var12) {
                        var16 = var12;
                     }

                     try {
                        closeQuietly(this.lockChannel);
                        throw var16;
                     } catch (IOException var4) {
                        var20 = var4;
                        var10001 = false;
                        break label56;
                     } catch (RuntimeException var5) {
                        var19 = var5;
                        var10001 = false;
                        break label55;
                     } catch (Error var6) {
                        var10000 = var6;
                        var10001 = false;
                        break label61;
                     }
                  }

                  try {
                     var18 = new StringBuilder();
                     var18.append(var1.getPath());
                     var18.append(" locked");
                     Log.i("MultiDex", var18.toString());
                     return;
                  } catch (IOException var7) {
                     var20 = var7;
                     var10001 = false;
                     break label56;
                  } catch (RuntimeException var8) {
                     var19 = var8;
                     var10001 = false;
                     break label55;
                  } catch (Error var9) {
                     var10000 = var9;
                     var10001 = false;
                  }
               }

               var16 = var10000;
               break label57;
            }

            var16 = var19;
            break label57;
         }

         var16 = var20;
      }

      closeQuietly(this.lockRaf);
      throw var16;
   }

   private void clearDexDir() {
      File[] var3 = this.dexDir.listFiles(new FileFilter() {
         public boolean accept(File var1) {
            return var1.getName().equals("MultiDex.lock") ^ true;
         }
      });
      if (var3 == null) {
         StringBuilder var6 = new StringBuilder();
         var6.append("Failed to list secondary dex dir content (");
         var6.append(this.dexDir.getPath());
         var6.append(").");
         Log.w("MultiDex", var6.toString());
      } else {
         int var2 = var3.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            File var4 = var3[var1];
            StringBuilder var5 = new StringBuilder();
            var5.append("Trying to delete old file ");
            var5.append(var4.getPath());
            var5.append(" of size ");
            var5.append(var4.length());
            Log.i("MultiDex", var5.toString());
            if (!var4.delete()) {
               var5 = new StringBuilder();
               var5.append("Failed to delete old file ");
               var5.append(var4.getPath());
               Log.w("MultiDex", var5.toString());
            } else {
               var5 = new StringBuilder();
               var5.append("Deleted old file ");
               var5.append(var4.getPath());
               Log.i("MultiDex", var5.toString());
            }
         }

      }
   }

   private static void closeQuietly(Closeable var0) {
      try {
         var0.close();
      } catch (IOException var1) {
         Log.w("MultiDex", "Failed to close resource", var1);
      }
   }

   private static void extract(ZipFile var0, ZipEntry var1, File var2, String var3) throws IOException, FileNotFoundException {
      InputStream var80 = var0.getInputStream(var1);
      StringBuilder var6 = new StringBuilder();
      var6.append("tmp-");
      var6.append(var3);
      File var84 = File.createTempFile(var6.toString(), ".zip", var2.getParentFile());
      var6 = new StringBuilder();
      var6.append("Extracting ");
      var6.append(var84.getPath());
      Log.i("MultiDex", var6.toString());

      Throwable var10000;
      Throwable var82;
      label603: {
         boolean var10001;
         ZipOutputStream var85;
         try {
            var85 = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(var84)));
         } catch (Throwable var79) {
            var10000 = var79;
            var10001 = false;
            break label603;
         }

         label604: {
            label594: {
               int var4;
               byte[] var81;
               try {
                  ZipEntry var7 = new ZipEntry("classes.dex");
                  var7.setTime(var1.getTime());
                  var85.putNextEntry(var7);
                  var81 = new byte[16384];
                  var4 = var80.read(var81);
               } catch (Throwable var78) {
                  var10000 = var78;
                  var10001 = false;
                  break label594;
               }

               while(true) {
                  if (var4 == -1) {
                     try {
                        var85.closeEntry();
                        break label604;
                     } catch (Throwable var76) {
                        var10000 = var76;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     var85.write(var81, 0, var4);
                     var4 = var80.read(var81);
                  } catch (Throwable var77) {
                     var10000 = var77;
                     var10001 = false;
                     break;
                  }
               }
            }

            var82 = var10000;

            try {
               var85.close();
               throw var82;
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label603;
            }
         }

         boolean var5;
         StringBuilder var83;
         label581: {
            try {
               var85.close();
               if (var84.setReadOnly()) {
                  var83 = new StringBuilder();
                  var83.append("Renaming to ");
                  var83.append(var2.getPath());
                  Log.i("MultiDex", var83.toString());
                  var5 = var84.renameTo(var2);
                  break label581;
               }
            } catch (Throwable var75) {
               var10000 = var75;
               var10001 = false;
               break label603;
            }

            try {
               var83 = new StringBuilder();
               var83.append("Failed to mark readonly \"");
               var83.append(var84.getAbsolutePath());
               var83.append("\" (tmp of \"");
               var83.append(var2.getAbsolutePath());
               var83.append("\")");
               throw new IOException(var83.toString());
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label603;
            }
         }

         if (var5) {
            closeQuietly(var80);
            var84.delete();
            return;
         }

         label570:
         try {
            var83 = new StringBuilder();
            var83.append("Failed to rename \"");
            var83.append(var84.getAbsolutePath());
            var83.append("\" to \"");
            var83.append(var2.getAbsolutePath());
            var83.append("\"");
            throw new IOException(var83.toString());
         } catch (Throwable var73) {
            var10000 = var73;
            var10001 = false;
            break label570;
         }
      }

      var82 = var10000;
      closeQuietly(var80);
      var84.delete();
      throw var82;
   }

   private static SharedPreferences getMultiDexPreferences(Context var0) {
      byte var1;
      if (VERSION.SDK_INT < 11) {
         var1 = 0;
      } else {
         var1 = 4;
      }

      return var0.getSharedPreferences("multidex.version", var1);
   }

   private static long getTimeStamp(File var0) {
      long var3 = var0.lastModified();
      long var1 = var3;
      if (var3 == -1L) {
         var1 = var3 - 1L;
      }

      return var1;
   }

   private static long getZipCrc(File var0) throws IOException {
      long var3 = ZipUtil.getZipCrc(var0);
      long var1 = var3;
      if (var3 == -1L) {
         var1 = var3 - 1L;
      }

      return var1;
   }

   private static boolean isModified(Context var0, File var1, long var2, String var4) {
      SharedPreferences var6 = getMultiDexPreferences(var0);
      StringBuilder var5 = new StringBuilder();
      var5.append(var4);
      var5.append("timestamp");
      if (var6.getLong(var5.toString(), -1L) == getTimeStamp(var1)) {
         StringBuilder var7 = new StringBuilder();
         var7.append(var4);
         var7.append("crc");
         if (var6.getLong(var7.toString(), -1L) == var2) {
            return false;
         }
      }

      return true;
   }

   private List loadExistingExtractions(Context var1, String var2) throws IOException {
      Log.i("MultiDex", "loading existing secondary dex files");
      StringBuilder var11 = new StringBuilder();
      var11.append(this.sourceApk.getName());
      var11.append(".classes");
      String var17 = var11.toString();
      SharedPreferences var15 = getMultiDexPreferences(var1);
      StringBuilder var12 = new StringBuilder();
      var12.append(var2);
      var12.append("dex.number");
      int var4 = var15.getInt(var12.toString(), 1);
      ArrayList var18 = new ArrayList(var4 - 1);

      for(int var3 = 2; var3 <= var4; ++var3) {
         StringBuilder var13 = new StringBuilder();
         var13.append(var17);
         var13.append(var3);
         var13.append(".zip");
         String var19 = var13.toString();
         MultiDexExtractor.ExtractedDex var20 = new MultiDexExtractor.ExtractedDex(this.dexDir, var19);
         StringBuilder var16;
         if (!var20.isFile()) {
            var16 = new StringBuilder();
            var16.append("Missing extracted secondary dex file '");
            var16.append(var20.getPath());
            var16.append("'");
            throw new IOException(var16.toString());
         }

         var20.crc = getZipCrc(var20);
         StringBuilder var14 = new StringBuilder();
         var14.append(var2);
         var14.append("dex.crc.");
         var14.append(var3);
         long var5 = var15.getLong(var14.toString(), -1L);
         var14 = new StringBuilder();
         var14.append(var2);
         var14.append("dex.time.");
         var14.append(var3);
         long var7 = var15.getLong(var14.toString(), -1L);
         long var9 = var20.lastModified();
         if (var7 != var9 || var5 != var20.crc) {
            var16 = new StringBuilder();
            var16.append("Invalid extracted dex: ");
            var16.append(var20);
            var16.append(" (key \"");
            var16.append(var2);
            var16.append("\"), expected modification time: ");
            var16.append(var7);
            var16.append(", modification time: ");
            var16.append(var9);
            var16.append(", expected crc: ");
            var16.append(var5);
            var16.append(", file crc: ");
            var16.append(var20.crc);
            throw new IOException(var16.toString());
         }

         var18.add(var20);
      }

      return var18;
   }

   private List performExtractions() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private static void putStoredApkInfo(Context var0, String var1, long var2, long var4, List var6) {
      Editor var10 = getMultiDexPreferences(var0).edit();
      StringBuilder var8 = new StringBuilder();
      var8.append(var1);
      var8.append("timestamp");
      var10.putLong(var8.toString(), var2);
      var8 = new StringBuilder();
      var8.append(var1);
      var8.append("crc");
      var10.putLong(var8.toString(), var4);
      var8 = new StringBuilder();
      var8.append(var1);
      var8.append("dex.number");
      var10.putInt(var8.toString(), var6.size() + 1);
      int var7 = 2;

      for(Iterator var11 = var6.iterator(); var11.hasNext(); ++var7) {
         MultiDexExtractor.ExtractedDex var12 = (MultiDexExtractor.ExtractedDex)var11.next();
         StringBuilder var9 = new StringBuilder();
         var9.append(var1);
         var9.append("dex.crc.");
         var9.append(var7);
         var10.putLong(var9.toString(), var12.crc);
         var9 = new StringBuilder();
         var9.append(var1);
         var9.append("dex.time.");
         var9.append(var7);
         var10.putLong(var9.toString(), var12.lastModified());
      }

      var10.commit();
   }

   public void close() throws IOException {
      this.cacheLock.release();
      this.lockChannel.close();
      this.lockRaf.close();
   }

   List load(Context var1, String var2, boolean var3) throws IOException {
      StringBuilder var4 = new StringBuilder();
      var4.append("MultiDexExtractor.load(");
      var4.append(this.sourceApk.getPath());
      var4.append(", ");
      var4.append(var3);
      var4.append(", ");
      var4.append(var2);
      var4.append(")");
      Log.i("MultiDex", var4.toString());
      if (!this.cacheLock.isValid()) {
         throw new IllegalStateException("MultiDexExtractor was closed");
      } else {
         List var6;
         List var8;
         if (!var3 && !isModified(var1, this.sourceApk, this.sourceCrc, var2)) {
            label23: {
               try {
                  var8 = this.loadExistingExtractions(var1, var2);
               } catch (IOException var5) {
                  Log.w("MultiDex", "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", var5);
                  var8 = this.performExtractions();
                  putStoredApkInfo(var1, var2, getTimeStamp(this.sourceApk), this.sourceCrc, var8);
                  var6 = var8;
                  break label23;
               }

               var6 = var8;
            }
         } else {
            if (var3) {
               Log.i("MultiDex", "Forced extraction must be performed.");
            } else {
               Log.i("MultiDex", "Detected that extraction must be performed.");
            }

            var8 = this.performExtractions();
            putStoredApkInfo(var1, var2, getTimeStamp(this.sourceApk), this.sourceCrc, var8);
            var6 = var8;
         }

         StringBuilder var7 = new StringBuilder();
         var7.append("load found ");
         var7.append(var6.size());
         var7.append(" secondary dex files");
         Log.i("MultiDex", var7.toString());
         return var6;
      }
   }

   private static class ExtractedDex extends File {
      public long crc = -1L;

      public ExtractedDex(File var1, String var2) {
         super(var1, var2);
      }
   }
}
