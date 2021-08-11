// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.multidex;

import java.io.InputStream;
import java.util.Iterator;
import android.content.SharedPreferences$Editor;
import java.util.ArrayList;
import java.util.List;
import android.os.Build$VERSION;
import android.content.SharedPreferences;
import android.content.Context;
import java.io.FileNotFoundException;
import java.util.zip.ZipOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import android.util.Log;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.io.File;
import java.nio.channels.FileLock;
import java.io.Closeable;

final class MultiDexExtractor implements Closeable
{
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
    
    MultiDexExtractor(final File p0, final File p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     4: new             Ljava/lang/StringBuilder;
        //     7: dup            
        //     8: invokespecial   java/lang/StringBuilder.<init>:()V
        //    11: astore_3       
        //    12: aload_3        
        //    13: ldc             "MultiDexExtractor("
        //    15: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    18: pop            
        //    19: aload_3        
        //    20: aload_1        
        //    21: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //    24: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    27: pop            
        //    28: aload_3        
        //    29: ldc             ", "
        //    31: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    34: pop            
        //    35: aload_3        
        //    36: aload_2        
        //    37: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //    40: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    43: pop            
        //    44: aload_3        
        //    45: ldc             ")"
        //    47: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    50: pop            
        //    51: ldc             "MultiDex"
        //    53: aload_3        
        //    54: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    57: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //    60: pop            
        //    61: aload_0        
        //    62: aload_1        
        //    63: putfield        androidx/multidex/MultiDexExtractor.sourceApk:Ljava/io/File;
        //    66: aload_0        
        //    67: aload_2        
        //    68: putfield        androidx/multidex/MultiDexExtractor.dexDir:Ljava/io/File;
        //    71: aload_0        
        //    72: aload_1        
        //    73: invokestatic    androidx/multidex/MultiDexExtractor.getZipCrc:(Ljava/io/File;)J
        //    76: putfield        androidx/multidex/MultiDexExtractor.sourceCrc:J
        //    79: new             Ljava/io/File;
        //    82: dup            
        //    83: aload_2        
        //    84: ldc             "MultiDex.lock"
        //    86: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    89: astore_1       
        //    90: aload_0        
        //    91: new             Ljava/io/RandomAccessFile;
        //    94: dup            
        //    95: aload_1        
        //    96: ldc             "rw"
        //    98: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   101: putfield        androidx/multidex/MultiDexExtractor.lockRaf:Ljava/io/RandomAccessFile;
        //   104: aload_0        
        //   105: aload_0        
        //   106: getfield        androidx/multidex/MultiDexExtractor.lockRaf:Ljava/io/RandomAccessFile;
        //   109: invokevirtual   java/io/RandomAccessFile.getChannel:()Ljava/nio/channels/FileChannel;
        //   112: putfield        androidx/multidex/MultiDexExtractor.lockChannel:Ljava/nio/channels/FileChannel;
        //   115: new             Ljava/lang/StringBuilder;
        //   118: dup            
        //   119: invokespecial   java/lang/StringBuilder.<init>:()V
        //   122: astore_2       
        //   123: aload_2        
        //   124: ldc             "Blocking on lock "
        //   126: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   129: pop            
        //   130: aload_2        
        //   131: aload_1        
        //   132: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //   135: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   138: pop            
        //   139: ldc             "MultiDex"
        //   141: aload_2        
        //   142: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   145: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //   148: pop            
        //   149: aload_0        
        //   150: aload_0        
        //   151: getfield        androidx/multidex/MultiDexExtractor.lockChannel:Ljava/nio/channels/FileChannel;
        //   154: invokevirtual   java/nio/channels/FileChannel.lock:()Ljava/nio/channels/FileLock;
        //   157: putfield        androidx/multidex/MultiDexExtractor.cacheLock:Ljava/nio/channels/FileLock;
        //   160: new             Ljava/lang/StringBuilder;
        //   163: dup            
        //   164: invokespecial   java/lang/StringBuilder.<init>:()V
        //   167: astore_2       
        //   168: aload_2        
        //   169: aload_1        
        //   170: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //   173: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   176: pop            
        //   177: aload_2        
        //   178: ldc             " locked"
        //   180: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   183: pop            
        //   184: ldc             "MultiDex"
        //   186: aload_2        
        //   187: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   190: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //   193: pop            
        //   194: return         
        //   195: aload_0        
        //   196: getfield        androidx/multidex/MultiDexExtractor.lockChannel:Ljava/nio/channels/FileChannel;
        //   199: invokestatic    androidx/multidex/MultiDexExtractor.closeQuietly:(Ljava/io/Closeable;)V
        //   202: aload_1        
        //   203: athrow         
        //   204: astore_1       
        //   205: goto            213
        //   208: astore_1       
        //   209: goto            213
        //   212: astore_1       
        //   213: aload_0        
        //   214: getfield        androidx/multidex/MultiDexExtractor.lockRaf:Ljava/io/RandomAccessFile;
        //   217: invokestatic    androidx/multidex/MultiDexExtractor.closeQuietly:(Ljava/io/Closeable;)V
        //   220: aload_1        
        //   221: athrow         
        //   222: astore_1       
        //   223: goto            195
        //   226: astore_1       
        //   227: goto            195
        //   230: astore_1       
        //   231: goto            195
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  104    115    212    213    Ljava/io/IOException;
        //  104    115    208    212    Ljava/lang/RuntimeException;
        //  104    115    204    208    Ljava/lang/Error;
        //  115    160    230    234    Ljava/io/IOException;
        //  115    160    226    230    Ljava/lang/RuntimeException;
        //  115    160    222    226    Ljava/lang/Error;
        //  160    194    212    213    Ljava/io/IOException;
        //  160    194    208    212    Ljava/lang/RuntimeException;
        //  160    194    204    208    Ljava/lang/Error;
        //  195    204    212    213    Ljava/io/IOException;
        //  195    204    208    212    Ljava/lang/RuntimeException;
        //  195    204    204    208    Ljava/lang/Error;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0195:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void clearDexDir() {
        final File[] listFiles = this.dexDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return file.getName().equals("MultiDex.lock") ^ true;
            }
        });
        if (listFiles == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to list secondary dex dir content (");
            sb.append(this.dexDir.getPath());
            sb.append(").");
            Log.w("MultiDex", sb.toString());
            return;
        }
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Trying to delete old file ");
            sb2.append(file.getPath());
            sb2.append(" of size ");
            sb2.append(file.length());
            Log.i("MultiDex", sb2.toString());
            if (!file.delete()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Failed to delete old file ");
                sb3.append(file.getPath());
                Log.w("MultiDex", sb3.toString());
            }
            else {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Deleted old file ");
                sb4.append(file.getPath());
                Log.i("MultiDex", sb4.toString());
            }
        }
    }
    
    private static void closeQuietly(final Closeable closeable) {
        try {
            closeable.close();
        }
        catch (IOException ex) {
            Log.w("MultiDex", "Failed to close resource", (Throwable)ex);
        }
    }
    
    private static void extract(ZipFile inputStream, final ZipEntry zipEntry, final File file, String tempFile) throws IOException, FileNotFoundException {
        inputStream = (ZipFile)inputStream.getInputStream(zipEntry);
        final StringBuilder sb = new StringBuilder();
        sb.append("tmp-");
        sb.append(tempFile);
        tempFile = (String)File.createTempFile(sb.toString(), ".zip", file.getParentFile());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Extracting ");
        sb2.append(((File)tempFile).getPath());
        Log.i("MultiDex", sb2.toString());
        try {
            final ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream((File)tempFile)));
            try {
                final ZipEntry zipEntry2 = new ZipEntry("classes.dex");
                zipEntry2.setTime(zipEntry.getTime());
                zipOutputStream.putNextEntry(zipEntry2);
                final byte[] array = new byte[16384];
                for (int i = ((InputStream)inputStream).read(array); i != -1; i = ((InputStream)inputStream).read(array)) {
                    zipOutputStream.write(array, 0, i);
                }
                zipOutputStream.closeEntry();
                zipOutputStream.close();
                if (!((File)tempFile).setReadOnly()) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Failed to mark readonly \"");
                    sb3.append(((File)tempFile).getAbsolutePath());
                    sb3.append("\" (tmp of \"");
                    sb3.append(file.getAbsolutePath());
                    sb3.append("\")");
                    throw new IOException(sb3.toString());
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Renaming to ");
                sb4.append(file.getPath());
                Log.i("MultiDex", sb4.toString());
                if (((File)tempFile).renameTo(file)) {
                    return;
                }
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Failed to rename \"");
                sb5.append(((File)tempFile).getAbsolutePath());
                sb5.append("\" to \"");
                sb5.append(file.getAbsolutePath());
                sb5.append("\"");
                throw new IOException(sb5.toString());
            }
            finally {
                zipOutputStream.close();
            }
        }
        finally {
            closeQuietly(inputStream);
            ((File)tempFile).delete();
        }
    }
    
    private static SharedPreferences getMultiDexPreferences(final Context context) {
        int n;
        if (Build$VERSION.SDK_INT < 11) {
            n = 0;
        }
        else {
            n = 4;
        }
        return context.getSharedPreferences("multidex.version", n);
    }
    
    private static long getTimeStamp(final File file) {
        final long lastModified = file.lastModified();
        if (lastModified == -1L) {
            return lastModified - 1L;
        }
        return lastModified;
    }
    
    private static long getZipCrc(final File file) throws IOException {
        final long zipCrc = ZipUtil.getZipCrc(file);
        if (zipCrc == -1L) {
            return zipCrc - 1L;
        }
        return zipCrc;
    }
    
    private static boolean isModified(final Context context, final File file, final long n, final String s) {
        final SharedPreferences multiDexPreferences = getMultiDexPreferences(context);
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("timestamp");
        if (multiDexPreferences.getLong(sb.toString(), -1L) == getTimeStamp(file)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("crc");
            if (multiDexPreferences.getLong(sb2.toString(), -1L) == n) {
                return false;
            }
        }
        return true;
    }
    
    private List<ExtractedDex> loadExistingExtractions(final Context context, final String s) throws IOException {
        Log.i("MultiDex", "loading existing secondary dex files");
        final StringBuilder sb = new StringBuilder();
        sb.append(this.sourceApk.getName());
        sb.append(".classes");
        final String string = sb.toString();
        final SharedPreferences multiDexPreferences = getMultiDexPreferences(context);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append("dex.number");
        final int int1 = multiDexPreferences.getInt(sb2.toString(), 1);
        final ArrayList list = new ArrayList<ExtractedDex>(int1 - 1);
        for (int i = 2; i <= int1; ++i) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append(i);
            sb3.append(".zip");
            final ExtractedDex extractedDex = new ExtractedDex(this.dexDir, sb3.toString());
            if (!extractedDex.isFile()) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Missing extracted secondary dex file '");
                sb4.append(extractedDex.getPath());
                sb4.append("'");
                throw new IOException(sb4.toString());
            }
            extractedDex.crc = getZipCrc(extractedDex);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(s);
            sb5.append("dex.crc.");
            sb5.append(i);
            final long long1 = multiDexPreferences.getLong(sb5.toString(), -1L);
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(s);
            sb6.append("dex.time.");
            sb6.append(i);
            final long long2 = multiDexPreferences.getLong(sb6.toString(), -1L);
            final long lastModified = extractedDex.lastModified();
            if (long2 != lastModified || long1 != extractedDex.crc) {
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("Invalid extracted dex: ");
                sb7.append(extractedDex);
                sb7.append(" (key \"");
                sb7.append(s);
                sb7.append("\"), expected modification time: ");
                sb7.append(long2);
                sb7.append(", modification time: ");
                sb7.append(lastModified);
                sb7.append(", expected crc: ");
                sb7.append(long1);
                sb7.append(", file crc: ");
                sb7.append(extractedDex.crc);
                throw new IOException(sb7.toString());
            }
            list.add(extractedDex);
        }
        return (List<ExtractedDex>)list;
    }
    
    private List<ExtractedDex> performExtractions() throws IOException {
        while (true) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.sourceApk.getName());
            sb.append(".classes");
            final String string = sb.toString();
            this.clearDexDir();
            final ArrayList<ExtractedDex> list = new ArrayList<ExtractedDex>();
            final ZipFile zipFile = new ZipFile(this.sourceApk);
            int n = 2;
        Label_0334_Outer:
            while (true) {
                Label_0663: {
                Label_0660:
                    while (true) {
                        Label_0652: {
                            try {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("classes");
                                sb2.append(2);
                                sb2.append(".dex");
                                StringBuilder sb8;
                                for (ZipEntry zipEntry = zipFile.getEntry(sb2.toString()); zipEntry != null; zipEntry = zipFile.getEntry(sb8.toString())) {
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append(string);
                                    sb3.append(n);
                                    sb3.append(".zip");
                                    final ExtractedDex extractedDex = new ExtractedDex(this.dexDir, sb3.toString());
                                    list.add(extractedDex);
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Extraction is needed for file ");
                                    sb4.append(extractedDex);
                                    Log.i("MultiDex", sb4.toString());
                                    int n2 = 0;
                                    int n3 = 0;
                                    while (n2 < 3 && n3 == 0) {
                                        ++n2;
                                        extract(zipFile, zipEntry, extractedDex, string);
                                        try {
                                            extractedDex.crc = getZipCrc(extractedDex);
                                            n3 = 1;
                                        }
                                        catch (IOException ex) {
                                            n3 = 0;
                                            final StringBuilder sb5 = new StringBuilder();
                                            sb5.append("Failed to read crc from ");
                                            sb5.append(extractedDex.getAbsolutePath());
                                            Log.w("MultiDex", sb5.toString(), (Throwable)ex);
                                        }
                                        final StringBuilder sb5 = new StringBuilder();
                                        sb5.append("Extraction ");
                                        if (n3 == 0) {
                                            break Label_0652;
                                        }
                                        final String s = "succeeded";
                                        sb5.append(s);
                                        sb5.append(" '");
                                        sb5.append(extractedDex.getAbsolutePath());
                                        sb5.append("': length ");
                                        sb5.append(extractedDex.length());
                                        sb5.append(" - crc: ");
                                        sb5.append(extractedDex.crc);
                                        Log.i("MultiDex", sb5.toString());
                                        if (n3 != 0) {
                                            break Label_0663;
                                        }
                                        extractedDex.delete();
                                        if (!extractedDex.exists()) {
                                            break Label_0660;
                                        }
                                        final StringBuilder sb6 = new StringBuilder();
                                        sb6.append("Failed to delete corrupted secondary dex '");
                                        sb6.append(extractedDex.getPath());
                                        sb6.append("'");
                                        Log.w("MultiDex", sb6.toString());
                                    }
                                    if (n3 == 0) {
                                        final StringBuilder sb7 = new StringBuilder();
                                        sb7.append("Could not create zip file ");
                                        sb7.append(extractedDex.getAbsolutePath());
                                        sb7.append(" for secondary dex (");
                                        sb7.append(n);
                                        sb7.append(")");
                                        throw new IOException(sb7.toString());
                                    }
                                    ++n;
                                    sb8 = new StringBuilder();
                                    sb8.append("classes");
                                    sb8.append(n);
                                    sb8.append(".dex");
                                }
                                try {
                                    zipFile.close();
                                    return list;
                                }
                                catch (IOException ex2) {
                                    Log.w("MultiDex", "Failed to close resource", (Throwable)ex2);
                                    return list;
                                }
                            }
                            finally {
                                try {
                                    zipFile.close();
                                }
                                catch (IOException ex3) {
                                    Log.w("MultiDex", "Failed to close resource", (Throwable)ex3);
                                }
                            }
                        }
                        final String s = "failed";
                        continue;
                    }
                    continue Label_0334_Outer;
                }
                continue Label_0334_Outer;
            }
        }
    }
    
    private static void putStoredApkInfo(final Context context, final String s, final long n, final long n2, final List<ExtractedDex> list) {
        final SharedPreferences$Editor edit = getMultiDexPreferences(context).edit();
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("timestamp");
        edit.putLong(sb.toString(), n);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append("crc");
        edit.putLong(sb2.toString(), n2);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append("dex.number");
        edit.putInt(sb3.toString(), list.size() + 1);
        int n3 = 2;
        for (final ExtractedDex extractedDex : list) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(s);
            sb4.append("dex.crc.");
            sb4.append(n3);
            edit.putLong(sb4.toString(), extractedDex.crc);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(s);
            sb5.append("dex.time.");
            sb5.append(n3);
            edit.putLong(sb5.toString(), extractedDex.lastModified());
            ++n3;
        }
        edit.commit();
    }
    
    @Override
    public void close() throws IOException {
        this.cacheLock.release();
        this.lockChannel.close();
        this.lockRaf.close();
    }
    
    List<? extends File> load(Context loadExistingExtractions, final String s, final boolean b) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("MultiDexExtractor.load(");
        sb.append(this.sourceApk.getPath());
        sb.append(", ");
        sb.append(b);
        sb.append(", ");
        sb.append(s);
        sb.append(")");
        Log.i("MultiDex", sb.toString());
        if (this.cacheLock.isValid()) {
            if (!b && !isModified(loadExistingExtractions, this.sourceApk, this.sourceCrc, s)) {
                try {
                    loadExistingExtractions = (Context)this.loadExistingExtractions(loadExistingExtractions, s);
                }
                catch (IOException ex) {
                    Log.w("MultiDex", "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", (Throwable)ex);
                    final List<ExtractedDex> performExtractions = this.performExtractions();
                    putStoredApkInfo(loadExistingExtractions, s, getTimeStamp(this.sourceApk), this.sourceCrc, performExtractions);
                    loadExistingExtractions = (Context)performExtractions;
                }
            }
            else {
                if (b) {
                    Log.i("MultiDex", "Forced extraction must be performed.");
                }
                else {
                    Log.i("MultiDex", "Detected that extraction must be performed.");
                }
                final List<ExtractedDex> performExtractions2 = this.performExtractions();
                putStoredApkInfo(loadExistingExtractions, s, getTimeStamp(this.sourceApk), this.sourceCrc, performExtractions2);
                loadExistingExtractions = (Context)performExtractions2;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("load found ");
            sb2.append(((List)loadExistingExtractions).size());
            sb2.append(" secondary dex files");
            Log.i("MultiDex", sb2.toString());
            return (List<? extends File>)loadExistingExtractions;
        }
        throw new IllegalStateException("MultiDexExtractor was closed");
    }
    
    private static class ExtractedDex extends File
    {
        public long crc;
        
        public ExtractedDex(final File file, final String s) {
            super(file, s);
            this.crc = -1L;
        }
    }
}
