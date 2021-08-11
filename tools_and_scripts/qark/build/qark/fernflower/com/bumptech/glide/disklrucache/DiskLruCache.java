package com.bumptech.glide.disklrucache;

import android.os.StrictMode;
import android.os.Build.VERSION;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache implements Closeable {
   static final long ANY_SEQUENCE_NUMBER = -1L;
   private static final String CLEAN = "CLEAN";
   private static final String DIRTY = "DIRTY";
   static final String JOURNAL_FILE = "journal";
   static final String JOURNAL_FILE_BACKUP = "journal.bkp";
   static final String JOURNAL_FILE_TEMP = "journal.tmp";
   static final String MAGIC = "libcore.io.DiskLruCache";
   private static final String READ = "READ";
   private static final String REMOVE = "REMOVE";
   static final String VERSION_1 = "1";
   private final int appVersion;
   private final Callable cleanupCallable;
   private final File directory;
   final ThreadPoolExecutor executorService;
   private final File journalFile;
   private final File journalFileBackup;
   private final File journalFileTmp;
   private Writer journalWriter;
   private final LinkedHashMap lruEntries = new LinkedHashMap(0, 0.75F, true);
   private long maxSize;
   private long nextSequenceNumber = 0L;
   private int redundantOpCount;
   private long size = 0L;
   private final int valueCount;

   private DiskLruCache(File var1, int var2, int var3, long var4) {
      this.executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DiskLruCache.DiskLruCacheThreadFactory());
      this.cleanupCallable = new Callable() {
         public Void call() throws Exception {
            DiskLruCache var1 = DiskLruCache.this;
            synchronized(var1){}

            Throwable var10000;
            boolean var10001;
            label197: {
               try {
                  if (DiskLruCache.this.journalWriter == null) {
                     return null;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label197;
               }

               try {
                  DiskLruCache.this.trimToSize();
                  if (DiskLruCache.this.journalRebuildRequired()) {
                     DiskLruCache.this.rebuildJournal();
                     DiskLruCache.this.redundantOpCount = 0;
                  }
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label197;
               }

               label187:
               try {
                  return null;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label187;
               }
            }

            while(true) {
               Throwable var2 = var10000;

               try {
                  throw var2;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  continue;
               }
            }
         }
      };
      this.directory = var1;
      this.appVersion = var2;
      this.journalFile = new File(var1, "journal");
      this.journalFileTmp = new File(var1, "journal.tmp");
      this.journalFileBackup = new File(var1, "journal.bkp");
      this.valueCount = var3;
      this.maxSize = var4;
   }

   private void checkNotClosed() {
      if (this.journalWriter == null) {
         throw new IllegalStateException("cache is closed");
      }
   }

   private static void closeWriter(Writer var0) throws IOException {
      if (VERSION.SDK_INT < 26) {
         var0.close();
      } else {
         ThreadPolicy var1 = StrictMode.getThreadPolicy();
         StrictMode.setThreadPolicy((new Builder(var1)).permitUnbufferedIo().build());

         try {
            var0.close();
         } finally {
            StrictMode.setThreadPolicy(var1);
         }

      }
   }

   private void completeEdit(DiskLruCache.Editor var1, boolean var2) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label1803: {
         DiskLruCache.Entry var8;
         boolean var10001;
         label1798: {
            try {
               var8 = var1.entry;
               if (var8.currentEditor == var1) {
                  break label1798;
               }
            } catch (Throwable var191) {
               var10000 = var191;
               var10001 = false;
               break label1803;
            }

            try {
               throw new IllegalStateException();
            } catch (Throwable var188) {
               var10000 = var188;
               var10001 = false;
               break label1803;
            }
         }

         int var3;
         label1815: {
            if (var2) {
               label1808: {
                  try {
                     if (var8.readable) {
                        break label1808;
                     }
                  } catch (Throwable var189) {
                     var10000 = var189;
                     var10001 = false;
                     break label1803;
                  }

                  var3 = 0;

                  while(true) {
                     try {
                        if (var3 >= this.valueCount) {
                           break;
                        }

                        if (!var1.written[var3]) {
                           break label1815;
                        }

                        if (!var8.getDirtyFile(var3).exists()) {
                           var1.abort();
                           return;
                        }
                     } catch (Throwable var190) {
                        var10000 = var190;
                        var10001 = false;
                        break label1803;
                     }

                     ++var3;
                  }
               }
            }

            var3 = 0;

            long var4;
            while(true) {
               File var193;
               try {
                  if (var3 >= this.valueCount) {
                     break;
                  }

                  var193 = var8.getDirtyFile(var3);
               } catch (Throwable var187) {
                  var10000 = var187;
                  var10001 = false;
                  break label1803;
               }

               if (var2) {
                  try {
                     if (var193.exists()) {
                        File var9 = var8.getCleanFile(var3);
                        var193.renameTo(var9);
                        var4 = var8.lengths[var3];
                        long var6 = var9.length();
                        var8.lengths[var3] = var6;
                        this.size = this.size - var4 + var6;
                     }
                  } catch (Throwable var185) {
                     var10000 = var185;
                     var10001 = false;
                     break label1803;
                  }
               } else {
                  try {
                     deleteIfExists(var193);
                  } catch (Throwable var186) {
                     var10000 = var186;
                     var10001 = false;
                     break label1803;
                  }
               }

               ++var3;
            }

            label1754: {
               label1753: {
                  try {
                     ++this.redundantOpCount;
                     var8.currentEditor = null;
                     if (var8.readable | var2) {
                        var8.readable = true;
                        this.journalWriter.append("CLEAN");
                        this.journalWriter.append(' ');
                        this.journalWriter.append(var8.key);
                        this.journalWriter.append(var8.getLengths());
                        this.journalWriter.append('\n');
                        break label1753;
                     }
                  } catch (Throwable var184) {
                     var10000 = var184;
                     var10001 = false;
                     break label1803;
                  }

                  try {
                     this.lruEntries.remove(var8.key);
                     this.journalWriter.append("REMOVE");
                     this.journalWriter.append(' ');
                     this.journalWriter.append(var8.key);
                     this.journalWriter.append('\n');
                     break label1754;
                  } catch (Throwable var182) {
                     var10000 = var182;
                     var10001 = false;
                     break label1803;
                  }
               }

               if (var2) {
                  try {
                     var4 = (long)(this.nextSequenceNumber++);
                     var8.sequenceNumber = var4;
                  } catch (Throwable var183) {
                     var10000 = var183;
                     var10001 = false;
                     break label1803;
                  }
               }
            }

            try {
               flushWriter(this.journalWriter);
               if (this.size <= this.maxSize && !this.journalRebuildRequired()) {
                  return;
               }
            } catch (Throwable var181) {
               var10000 = var181;
               var10001 = false;
               break label1803;
            }

            try {
               this.executorService.submit(this.cleanupCallable);
            } catch (Throwable var180) {
               var10000 = var180;
               var10001 = false;
               break label1803;
            }

            return;
         }

         label1731:
         try {
            var1.abort();
            StringBuilder var192 = new StringBuilder();
            var192.append("Newly created entry didn't create value for index ");
            var192.append(var3);
            throw new IllegalStateException(var192.toString());
         } catch (Throwable var179) {
            var10000 = var179;
            var10001 = false;
            break label1731;
         }
      }

      Throwable var194 = var10000;
      throw var194;
   }

   private static void deleteIfExists(File var0) throws IOException {
      if (var0.exists()) {
         if (!var0.delete()) {
            throw new IOException();
         }
      }
   }

   private DiskLruCache.Editor edit(String var1, long var2) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label339: {
         DiskLruCache.Entry var6;
         boolean var10001;
         try {
            this.checkNotClosed();
            var6 = (DiskLruCache.Entry)this.lruEntries.get(var1);
         } catch (Throwable var37) {
            var10000 = var37;
            var10001 = false;
            break label339;
         }

         if (var2 != -1L) {
            if (var6 == null) {
               return null;
            }

            long var4;
            try {
               var4 = var6.sequenceNumber;
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label339;
            }

            if (var4 != var2) {
               return null;
            }
         }

         DiskLruCache.Editor var7;
         if (var6 == null) {
            try {
               var6 = new DiskLruCache.Entry(var1);
               this.lruEntries.put(var1, var6);
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label339;
            }
         } else {
            try {
               var7 = var6.currentEditor;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label339;
            }

            if (var7 != null) {
               return null;
            }
         }

         try {
            var7 = new DiskLruCache.Editor(var6);
            var6.currentEditor = var7;
            this.journalWriter.append("DIRTY");
            this.journalWriter.append(' ');
            this.journalWriter.append(var1);
            this.journalWriter.append('\n');
            flushWriter(this.journalWriter);
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label339;
         }

         return var7;
      }

      Throwable var38 = var10000;
      throw var38;
   }

   private static void flushWriter(Writer var0) throws IOException {
      if (VERSION.SDK_INT < 26) {
         var0.flush();
      } else {
         ThreadPolicy var1 = StrictMode.getThreadPolicy();
         StrictMode.setThreadPolicy((new Builder(var1)).permitUnbufferedIo().build());

         try {
            var0.flush();
         } finally {
            StrictMode.setThreadPolicy(var1);
         }

      }
   }

   private static String inputStreamToString(InputStream var0) throws IOException {
      return Util.readFully(new InputStreamReader(var0, Util.UTF_8));
   }

   private boolean journalRebuildRequired() {
      int var1 = this.redundantOpCount;
      return var1 >= 2000 && var1 >= this.lruEntries.size();
   }

   public static DiskLruCache open(File var0, int var1, int var2, long var3) throws IOException {
      if (var3 <= 0L) {
         throw new IllegalArgumentException("maxSize <= 0");
      } else if (var2 > 0) {
         File var5 = new File(var0, "journal.bkp");
         if (var5.exists()) {
            File var6 = new File(var0, "journal");
            if (var6.exists()) {
               var5.delete();
            } else {
               renameTo(var5, var6, false);
            }
         }

         DiskLruCache var11 = new DiskLruCache(var0, var1, var2, var3);
         if (var11.journalFile.exists()) {
            try {
               var11.readJournal();
               var11.processJournal();
               return var11;
            } catch (IOException var9) {
               PrintStream var7 = System.out;
               StringBuilder var8 = new StringBuilder();
               var8.append("DiskLruCache ");
               var8.append(var0);
               var8.append(" is corrupt: ");
               var8.append(var9.getMessage());
               var8.append(", removing");
               var7.println(var8.toString());
               var11.delete();
            }
         }

         var0.mkdirs();
         DiskLruCache var10 = new DiskLruCache(var0, var1, var2, var3);
         var10.rebuildJournal();
         return var10;
      } else {
         throw new IllegalArgumentException("valueCount <= 0");
      }
   }

   private void processJournal() throws IOException {
      deleteIfExists(this.journalFileTmp);
      Iterator var2 = this.lruEntries.values().iterator();

      while(true) {
         while(var2.hasNext()) {
            DiskLruCache.Entry var3 = (DiskLruCache.Entry)var2.next();
            int var1;
            if (var3.currentEditor == null) {
               for(var1 = 0; var1 < this.valueCount; ++var1) {
                  this.size += var3.lengths[var1];
               }
            } else {
               var3.currentEditor = null;

               for(var1 = 0; var1 < this.valueCount; ++var1) {
                  deleteIfExists(var3.getCleanFile(var1));
                  deleteIfExists(var3.getDirtyFile(var1));
               }

               var2.remove();
            }
         }

         return;
      }
   }

   private void readJournal() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void readJournalLine(String var1) throws IOException {
      int var2 = var1.indexOf(32);
      StringBuilder var5;
      if (var2 != -1) {
         int var3 = var2 + 1;
         int var4 = var1.indexOf(32, var3);
         String var9;
         if (var4 == -1) {
            String var6 = var1.substring(var3);
            var9 = var6;
            if (var2 == "REMOVE".length()) {
               var9 = var6;
               if (var1.startsWith("REMOVE")) {
                  this.lruEntries.remove(var6);
                  return;
               }
            }
         } else {
            var9 = var1.substring(var3, var4);
         }

         DiskLruCache.Entry var7 = (DiskLruCache.Entry)this.lruEntries.get(var9);
         DiskLruCache.Entry var10 = var7;
         if (var7 == null) {
            var10 = new DiskLruCache.Entry(var9);
            this.lruEntries.put(var9, var10);
         }

         if (var4 != -1 && var2 == "CLEAN".length() && var1.startsWith("CLEAN")) {
            String[] var8 = var1.substring(var4 + 1).split(" ");
            var10.readable = true;
            var10.currentEditor = null;
            var10.setLengths(var8);
         } else if (var4 == -1 && var2 == "DIRTY".length() && var1.startsWith("DIRTY")) {
            var10.currentEditor = new DiskLruCache.Editor(var10);
         } else if (var4 != -1 || var2 != "READ".length() || !var1.startsWith("READ")) {
            var5 = new StringBuilder();
            var5.append("unexpected journal line: ");
            var5.append(var1);
            throw new IOException(var5.toString());
         }
      } else {
         var5 = new StringBuilder();
         var5.append("unexpected journal line: ");
         var5.append(var1);
         throw new IOException(var5.toString());
      }
   }

   private void rebuildJournal() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label687: {
         boolean var10001;
         try {
            if (this.journalWriter != null) {
               closeWriter(this.journalWriter);
            }
         } catch (Throwable var76) {
            var10000 = var76;
            var10001 = false;
            break label687;
         }

         BufferedWriter var1;
         try {
            var1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), Util.US_ASCII));
         } catch (Throwable var75) {
            var10000 = var75;
            var10001 = false;
            break label687;
         }

         label688: {
            label674: {
               Iterator var2;
               try {
                  var1.write("libcore.io.DiskLruCache");
                  var1.write("\n");
                  var1.write("1");
                  var1.write("\n");
                  var1.write(Integer.toString(this.appVersion));
                  var1.write("\n");
                  var1.write(Integer.toString(this.valueCount));
                  var1.write("\n");
                  var1.write("\n");
                  var2 = this.lruEntries.values().iterator();
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label674;
               }

               label671:
               while(true) {
                  DiskLruCache.Entry var3;
                  StringBuilder var4;
                  while(true) {
                     try {
                        if (!var2.hasNext()) {
                           break label688;
                        }

                        var3 = (DiskLruCache.Entry)var2.next();
                        if (var3.currentEditor == null) {
                           break;
                        }

                        var4 = new StringBuilder();
                        var4.append("DIRTY ");
                        var4.append(var3.key);
                        var4.append('\n');
                        var1.write(var4.toString());
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label671;
                     }
                  }

                  try {
                     var4 = new StringBuilder();
                     var4.append("CLEAN ");
                     var4.append(var3.key);
                     var4.append(var3.getLengths());
                     var4.append('\n');
                     var1.write(var4.toString());
                  } catch (Throwable var72) {
                     var10000 = var72;
                     var10001 = false;
                     break;
                  }
               }
            }

            Throwable var78 = var10000;

            try {
               closeWriter(var1);
               throw var78;
            } catch (Throwable var69) {
               var10000 = var69;
               var10001 = false;
               break label687;
            }
         }

         try {
            closeWriter(var1);
            if (this.journalFile.exists()) {
               renameTo(this.journalFile, this.journalFileBackup, true);
            }
         } catch (Throwable var71) {
            var10000 = var71;
            var10001 = false;
            break label687;
         }

         label654:
         try {
            renameTo(this.journalFileTmp, this.journalFile, false);
            this.journalFileBackup.delete();
            this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
            return;
         } catch (Throwable var70) {
            var10000 = var70;
            var10001 = false;
            break label654;
         }
      }

      Throwable var77 = var10000;
      throw var77;
   }

   private static void renameTo(File var0, File var1, boolean var2) throws IOException {
      if (var2) {
         deleteIfExists(var1);
      }

      if (!var0.renameTo(var1)) {
         throw new IOException();
      }
   }

   private void trimToSize() throws IOException {
      while(this.size > this.maxSize) {
         this.remove((String)((java.util.Map.Entry)this.lruEntries.entrySet().iterator().next()).getKey());
      }

   }

   public void close() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label230: {
         Writer var1;
         boolean var10001;
         try {
            var1 = this.journalWriter;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label230;
         }

         if (var1 == null) {
            return;
         }

         Iterator var23;
         try {
            var23 = (new ArrayList(this.lruEntries.values())).iterator();
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label230;
         }

         while(true) {
            try {
               if (!var23.hasNext()) {
                  break;
               }

               DiskLruCache.Entry var2 = (DiskLruCache.Entry)var23.next();
               if (var2.currentEditor != null) {
                  var2.currentEditor.abort();
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label230;
            }
         }

         try {
            this.trimToSize();
            closeWriter(this.journalWriter);
            this.journalWriter = null;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label230;
         }

         return;
      }

      Throwable var24 = var10000;
      throw var24;
   }

   public void delete() throws IOException {
      this.close();
      Util.deleteContents(this.directory);
   }

   public DiskLruCache.Editor edit(String var1) throws IOException {
      return this.edit(var1, -1L);
   }

   public void flush() throws IOException {
      synchronized(this){}

      try {
         this.checkNotClosed();
         this.trimToSize();
         flushWriter(this.journalWriter);
      } finally {
         ;
      }

   }

   public DiskLruCache.Value get(String var1) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label475: {
         DiskLruCache.Entry var5;
         boolean var10001;
         try {
            this.checkNotClosed();
            var5 = (DiskLruCache.Entry)this.lruEntries.get(var1);
         } catch (Throwable var48) {
            var10000 = var48;
            var10001 = false;
            break label475;
         }

         if (var5 == null) {
            return null;
         }

         boolean var4;
         try {
            var4 = var5.readable;
         } catch (Throwable var47) {
            var10000 = var47;
            var10001 = false;
            break label475;
         }

         if (!var4) {
            return null;
         }

         int var3;
         File[] var6;
         try {
            var6 = var5.cleanFiles;
            var3 = var6.length;
         } catch (Throwable var46) {
            var10000 = var46;
            var10001 = false;
            break label475;
         }

         for(int var2 = 0; var2 < var3; ++var2) {
            try {
               var4 = var6[var2].exists();
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label475;
            }

            if (!var4) {
               return null;
            }
         }

         try {
            ++this.redundantOpCount;
            this.journalWriter.append("READ");
            this.journalWriter.append(' ');
            this.journalWriter.append(var1);
            this.journalWriter.append('\n');
            if (this.journalRebuildRequired()) {
               this.executorService.submit(this.cleanupCallable);
            }
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label475;
         }

         DiskLruCache.Value var50;
         try {
            var50 = new DiskLruCache.Value(var1, var5.sequenceNumber, var5.cleanFiles, var5.lengths);
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label475;
         }

         return var50;
      }

      Throwable var49 = var10000;
      throw var49;
   }

   public File getDirectory() {
      return this.directory;
   }

   public long getMaxSize() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.maxSize;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isClosed() {
      synchronized(this){}
      boolean var4 = false;

      Writer var2;
      try {
         var4 = true;
         var2 = this.journalWriter;
         var4 = false;
      } finally {
         if (var4) {
            ;
         }
      }

      boolean var1;
      if (var2 == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean remove(String var1) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label509: {
         DiskLruCache.Entry var4;
         boolean var10001;
         try {
            this.checkNotClosed();
            var4 = (DiskLruCache.Entry)this.lruEntries.get(var1);
         } catch (Throwable var46) {
            var10000 = var46;
            var10001 = false;
            break label509;
         }

         if (var4 == null) {
            return false;
         }

         try {
            if (var4.currentEditor != null) {
               return false;
            }
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label509;
         }

         int var2 = 0;

         File var3;
         label510: {
            while(true) {
               try {
                  if (var2 >= this.valueCount) {
                     break;
                  }

                  var3 = var4.getCleanFile(var2);
                  if (var3.exists() && !var3.delete()) {
                     break label510;
                  }
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label509;
               }

               try {
                  this.size -= var4.lengths[var2];
                  var4.lengths[var2] = 0L;
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label509;
               }

               ++var2;
            }

            try {
               ++this.redundantOpCount;
               this.journalWriter.append("REMOVE");
               this.journalWriter.append(' ');
               this.journalWriter.append(var1);
               this.journalWriter.append('\n');
               this.lruEntries.remove(var1);
               if (this.journalRebuildRequired()) {
                  this.executorService.submit(this.cleanupCallable);
               }
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label509;
            }

            return true;
         }

         label472:
         try {
            StringBuilder var47 = new StringBuilder();
            var47.append("failed to delete ");
            var47.append(var3);
            throw new IOException(var47.toString());
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label472;
         }
      }

      Throwable var48 = var10000;
      throw var48;
   }

   public void setMaxSize(long var1) {
      synchronized(this){}

      try {
         this.maxSize = var1;
         this.executorService.submit(this.cleanupCallable);
      } finally {
         ;
      }

   }

   public long size() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.size;
      } finally {
         ;
      }

      return var1;
   }

   private static final class DiskLruCacheThreadFactory implements ThreadFactory {
      private DiskLruCacheThreadFactory() {
      }

      // $FF: synthetic method
      DiskLruCacheThreadFactory(Object var1) {
         this();
      }

      public Thread newThread(Runnable var1) {
         synchronized(this){}

         Thread var4;
         try {
            var4 = new Thread(var1, "glide-disk-lru-cache-thread");
            var4.setPriority(1);
         } finally {
            ;
         }

         return var4;
      }
   }

   public final class Editor {
      private boolean committed;
      private final DiskLruCache.Entry entry;
      private final boolean[] written;

      private Editor(DiskLruCache.Entry var2) {
         this.entry = var2;
         boolean[] var3;
         if (var2.readable) {
            var3 = null;
         } else {
            var3 = new boolean[DiskLruCache.this.valueCount];
         }

         this.written = var3;
      }

      // $FF: synthetic method
      Editor(DiskLruCache.Entry var2, Object var3) {
         this(var2);
      }

      private InputStream newInputStream(int param1) throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void abort() throws IOException {
         DiskLruCache.this.completeEdit(this, false);
      }

      public void abortUnlessCommitted() {
         if (!this.committed) {
            try {
               this.abort();
               return;
            } catch (IOException var2) {
            }
         }

      }

      public void commit() throws IOException {
         DiskLruCache.this.completeEdit(this, true);
         this.committed = true;
      }

      public File getFile(int var1) throws IOException {
         DiskLruCache var2 = DiskLruCache.this;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label295: {
            label290: {
               try {
                  if (this.entry.currentEditor == this) {
                     if (!this.entry.readable) {
                        this.written[var1] = true;
                     }
                     break label290;
                  }
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label295;
               }

               try {
                  throw new IllegalStateException();
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label295;
               }
            }

            File var3;
            try {
               var3 = this.entry.getDirtyFile(var1);
               if (!DiskLruCache.this.directory.exists()) {
                  DiskLruCache.this.directory.mkdirs();
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label295;
            }

            label278:
            try {
               return var3;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label278;
            }
         }

         while(true) {
            Throwable var34 = var10000;

            try {
               throw var34;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               continue;
            }
         }
      }

      public String getString(int var1) throws IOException {
         InputStream var2 = this.newInputStream(var1);
         return var2 != null ? DiskLruCache.inputStreamToString(var2) : null;
      }

      public void set(int var1, String var2) throws IOException {
         OutputStreamWriter var3 = null;

         OutputStreamWriter var4;
         label68: {
            Throwable var10000;
            label72: {
               boolean var10001;
               try {
                  var4 = new OutputStreamWriter(new FileOutputStream(this.getFile(var1)), Util.UTF_8);
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label72;
               }

               var3 = var4;

               label63:
               try {
                  var4.write(var2);
                  break label68;
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label63;
               }
            }

            Throwable var11 = var10000;
            Util.closeQuietly(var3);
            throw var11;
         }

         Util.closeQuietly(var4);
      }
   }

   private final class Entry {
      File[] cleanFiles;
      private DiskLruCache.Editor currentEditor;
      File[] dirtyFiles;
      private final String key;
      private final long[] lengths;
      private boolean readable;
      private long sequenceNumber;

      private Entry(String var2) {
         this.key = var2;
         this.lengths = new long[DiskLruCache.this.valueCount];
         this.cleanFiles = new File[DiskLruCache.this.valueCount];
         this.dirtyFiles = new File[DiskLruCache.this.valueCount];
         StringBuilder var5 = (new StringBuilder(var2)).append('.');
         int var4 = var5.length();

         for(int var3 = 0; var3 < DiskLruCache.this.valueCount; ++var3) {
            var5.append(var3);
            this.cleanFiles[var3] = new File(DiskLruCache.this.directory, var5.toString());
            var5.append(".tmp");
            this.dirtyFiles[var3] = new File(DiskLruCache.this.directory, var5.toString());
            var5.setLength(var4);
         }

      }

      // $FF: synthetic method
      Entry(String var2, Object var3) {
         this(var2);
      }

      private IOException invalidLengths(String[] var1) throws IOException {
         StringBuilder var2 = new StringBuilder();
         var2.append("unexpected journal line: ");
         var2.append(Arrays.toString(var1));
         throw new IOException(var2.toString());
      }

      private void setLengths(String[] var1) throws IOException {
         if (var1.length != DiskLruCache.this.valueCount) {
            throw this.invalidLengths(var1);
         } else {
            int var2 = 0;

            while(true) {
               try {
                  if (var2 >= var1.length) {
                     return;
                  }

                  this.lengths[var2] = Long.parseLong(var1[var2]);
               } catch (NumberFormatException var4) {
                  throw this.invalidLengths(var1);
               }

               ++var2;
            }
         }
      }

      public File getCleanFile(int var1) {
         return this.cleanFiles[var1];
      }

      public File getDirtyFile(int var1) {
         return this.dirtyFiles[var1];
      }

      public String getLengths() throws IOException {
         StringBuilder var5 = new StringBuilder();
         long[] var6 = this.lengths;
         int var2 = var6.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            long var3 = var6[var1];
            var5.append(' ');
            var5.append(var3);
         }

         return var5.toString();
      }
   }

   public final class Value {
      private final File[] files;
      private final String key;
      private final long[] lengths;
      private final long sequenceNumber;

      private Value(String var2, long var3, File[] var5, long[] var6) {
         this.key = var2;
         this.sequenceNumber = var3;
         this.files = var5;
         this.lengths = var6;
      }

      // $FF: synthetic method
      Value(String var2, long var3, File[] var5, long[] var6, Object var7) {
         this(var2, var3, var5, var6);
      }

      public DiskLruCache.Editor edit() throws IOException {
         return DiskLruCache.this.edit(this.key, this.sequenceNumber);
      }

      public File getFile(int var1) {
         return this.files[var1];
      }

      public long getLength(int var1) {
         return this.lengths[var1];
      }

      public String getString(int var1) throws IOException {
         return DiskLruCache.inputStreamToString(new FileInputStream(this.files[var1]));
      }
   }
}
