/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package androidx.multidex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import androidx.multidex.ZipUtil;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class MultiDexExtractor
implements Closeable {
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

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    MultiDexExtractor(File file, File object) throws IOException {
        void var1_2;
        block10 : {
            void var1_6;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MultiDexExtractor(");
            stringBuilder.append(file.getPath());
            stringBuilder.append(", ");
            stringBuilder.append(object.getPath());
            stringBuilder.append(")");
            Log.i((String)"MultiDex", (String)stringBuilder.toString());
            this.sourceApk = file;
            this.dexDir = object;
            this.sourceCrc = MultiDexExtractor.getZipCrc(file);
            file = new File((File)object, "MultiDex.lock");
            this.lockRaf = object = new RandomAccessFile(file, "rw");
            this.lockChannel = object.getChannel();
            object = new StringBuilder();
            object.append("Blocking on lock ");
            object.append(file.getPath());
            Log.i((String)"MultiDex", (String)object.toString());
            this.cacheLock = this.lockChannel.lock();
            try {
                object = new StringBuilder();
                object.append(file.getPath());
                object.append(" locked");
                Log.i((String)"MultiDex", (String)object.toString());
                return;
            }
            catch (Error error) {
            }
            catch (RuntimeException runtimeException) {
            }
            catch (IOException iOException) {
                // empty catch block
            }
            MultiDexExtractor.closeQuietly(this.lockRaf);
            throw var1_6;
            catch (Error error) {
                break block10;
            }
            catch (RuntimeException runtimeException) {
                break block10;
            }
            catch (IOException iOException) {}
        }
        MultiDexExtractor.closeQuietly(this.lockChannel);
        throw var1_2;
    }

    private void clearDexDir() {
        File[] arrfile = this.dexDir.listFiles(new FileFilter(){

            @Override
            public boolean accept(File file) {
                return file.getName().equals("MultiDex.lock") ^ true;
            }
        });
        if (arrfile == null) {
            arrfile = new StringBuilder();
            arrfile.append("Failed to list secondary dex dir content (");
            arrfile.append(this.dexDir.getPath());
            arrfile.append(").");
            Log.w((String)"MultiDex", (String)arrfile.toString());
            return;
        }
        for (File file : arrfile) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Trying to delete old file ");
            stringBuilder.append(file.getPath());
            stringBuilder.append(" of size ");
            stringBuilder.append(file.length());
            Log.i((String)"MultiDex", (String)stringBuilder.toString());
            if (!file.delete()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete old file ");
                stringBuilder.append(file.getPath());
                Log.w((String)"MultiDex", (String)stringBuilder.toString());
                continue;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Deleted old file ");
            stringBuilder.append(file.getPath());
            Log.i((String)"MultiDex", (String)stringBuilder.toString());
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
            return;
        }
        catch (IOException iOException) {
            Log.w((String)"MultiDex", (String)"Failed to close resource", (Throwable)iOException);
            return;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static void extract(ZipFile closeable, ZipEntry object, File file, String object2) throws IOException, FileNotFoundException {
        closeable = closeable.getInputStream((ZipEntry)object);
        Object object3 = new StringBuilder();
        object3.append("tmp-");
        object3.append((String)object2);
        object2 = File.createTempFile(object3.toString(), ".zip", file.getParentFile());
        object3 = new StringBuilder();
        object3.append("Extracting ");
        object3.append(object2.getPath());
        Log.i((String)"MultiDex", (String)object3.toString());
        object3 = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream((File)object2)));
        ZipEntry zipEntry = new ZipEntry("classes.dex");
        zipEntry.setTime(object.getTime());
        object3.putNextEntry(zipEntry);
        object = new byte[16384];
        int n = closeable.read((byte[])object);
        while (n != -1) {
            object3.write((byte[])object, 0, n);
            n = closeable.read((byte[])object);
        }
        object3.closeEntry();
        {
            catch (Throwable throwable) {
                object3.close();
                throw throwable;
            }
        }
        try {
            object3.close();
            if (object2.setReadOnly()) {
                object = new StringBuilder();
                object.append("Renaming to ");
                object.append(file.getPath());
                Log.i((String)"MultiDex", (String)object.toString());
                boolean bl = object2.renameTo(file);
                if (bl) {
                    return;
                }
                object = new StringBuilder();
                object.append("Failed to rename \"");
                object.append(object2.getAbsolutePath());
                object.append("\" to \"");
                object.append(file.getAbsolutePath());
                object.append("\"");
                throw new IOException(object.toString());
            }
            object = new StringBuilder();
            object.append("Failed to mark readonly \"");
            object.append(object2.getAbsolutePath());
            object.append("\" (tmp of \"");
            object.append(file.getAbsolutePath());
            object.append("\")");
            throw new IOException(object.toString());
        }
        catch (Throwable throwable) {
            throw throwable;
        }
        finally {
            MultiDexExtractor.closeQuietly(closeable);
            object2.delete();
        }
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        int n = Build.VERSION.SDK_INT < 11 ? 0 : 4;
        return context.getSharedPreferences("multidex.version", n);
    }

    private static long getTimeStamp(File file) {
        long l;
        long l2 = l = file.lastModified();
        if (l == -1L) {
            l2 = l - 1L;
        }
        return l2;
    }

    private static long getZipCrc(File file) throws IOException {
        long l;
        long l2 = l = ZipUtil.getZipCrc(file);
        if (l == -1L) {
            l2 = l - 1L;
        }
        return l2;
    }

    private static boolean isModified(Context context, File serializable, long l, String string2) {
        context = MultiDexExtractor.getMultiDexPreferences(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("timestamp");
        if (context.getLong(stringBuilder.toString(), -1L) == MultiDexExtractor.getTimeStamp(serializable)) {
            serializable = new StringBuilder();
            serializable.append(string2);
            serializable.append("crc");
            if (context.getLong(serializable.toString(), -1L) == l) {
                return false;
            }
        }
        return true;
    }

    private List<ExtractedDex> loadExistingExtractions(Context object, String string2) throws IOException {
        Log.i((String)"MultiDex", (String)"loading existing secondary dex files");
        CharSequence charSequence = new StringBuilder();
        charSequence.append(this.sourceApk.getName());
        charSequence.append(".classes");
        charSequence = charSequence.toString();
        object = MultiDexExtractor.getMultiDexPreferences((Context)object);
        Serializable serializable = new StringBuilder();
        serializable.append(string2);
        serializable.append("dex.number");
        int n = object.getInt(serializable.toString(), 1);
        serializable = new ArrayList(n - 1);
        for (int i = 2; i <= n; ++i) {
            Object object2 = new StringBuilder();
            object2.append((String)charSequence);
            object2.append(i);
            object2.append(".zip");
            object2 = object2.toString();
            object2 = new ExtractedDex(this.dexDir, (String)object2);
            if (object2.isFile()) {
                object2.crc = MultiDexExtractor.getZipCrc((File)object2);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append("dex.crc.");
                stringBuilder.append(i);
                long l = object.getLong(stringBuilder.toString(), -1L);
                stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append("dex.time.");
                stringBuilder.append(i);
                long l2 = object.getLong(stringBuilder.toString(), -1L);
                long l3 = object2.lastModified();
                if (l2 == l3 && l == object2.crc) {
                    serializable.add(object2);
                    continue;
                }
                object = new StringBuilder();
                object.append("Invalid extracted dex: ");
                object.append(object2);
                object.append(" (key \"");
                object.append(string2);
                object.append("\"), expected modification time: ");
                object.append(l2);
                object.append(", modification time: ");
                object.append(l3);
                object.append(", expected crc: ");
                object.append(l);
                object.append(", file crc: ");
                object.append(object2.crc);
                throw new IOException(object.toString());
            }
            object = new StringBuilder();
            object.append("Missing extracted secondary dex file '");
            object.append(object2.getPath());
            object.append("'");
            throw new IOException(object.toString());
        }
        return serializable;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private List<ExtractedDex> performExtractions() throws IOException {
        block18 : {
            var4_1 = this;
            var5_4 /* !! */  = new StringBuilder();
            var5_4 /* !! */ .append(var4_1.sourceApk.getName());
            var5_4 /* !! */ .append(".classes");
            var9_6 = var5_4 /* !! */ .toString();
            this.clearDexDir();
            var7_7 = new ArrayList<ExtractedDex>();
            var8_8 = new ZipFile(var4_1.sourceApk);
            try {
                var4_1 = new StringBuilder();
                var4_1.append("classes");
                var4_1.append(2);
                var4_1.append(".dex");
                var4_1 = var8_8.getEntry(var4_1.toString());
                var2_9 = 2;
                do {
                    if (var4_1 != null) {
                        var5_4 /* !! */  = new StringBuilder();
                        var5_4 /* !! */ .append(var9_6);
                        var5_4 /* !! */ .append(var2_9);
                        var5_4 /* !! */ .append(".zip");
                        var5_4 /* !! */  = var5_4 /* !! */ .toString();
                        var10_14 = new ExtractedDex(this.dexDir, (String)var5_4 /* !! */ );
                        var7_7.add(var10_14);
                        var6_12 /* !! */  = new StringBuilder();
                        var6_12 /* !! */ .append("Extraction is needed for file ");
                        var6_12 /* !! */ .append(var10_14);
                        Log.i((String)"MultiDex", (String)var6_12 /* !! */ .toString());
                        var3_11 = 0;
                        var1_10 = false;
                    } else {
                        try {
                            var8_8.close();
                            return var7_7;
                        }
                        catch (IOException var4_2) {
                            Log.w((String)"MultiDex", (String)"Failed to close resource", (Throwable)var4_2);
                            return var7_7;
                        }
                    }
lbl38: // 2 sources:
                    if (var3_11 >= 3) break block18;
                    if (!var1_10) {
                        MultiDexExtractor.extract(var8_8, (ZipEntry)var4_1, var10_14, var9_6);
                        try {
                            var10_14.crc = MultiDexExtractor.getZipCrc(var10_14);
                            var1_10 = true;
                        }
                        catch (IOException var6_13) {
                            var11_15 = new StringBuilder();
                            var11_15.append("Failed to read crc from ");
                            var11_15.append(var10_14.getAbsolutePath());
                            Log.w((String)"MultiDex", (String)var11_15.toString(), (Throwable)var6_13);
                            var1_10 = false;
                        }
                        var11_15 = new StringBuilder();
                        var11_15.append("Extraction ");
                        var6_12 /* !! */  = var1_10 != false ? "succeeded" : "failed";
                        var11_15.append((String)var6_12 /* !! */ );
                        var11_15.append(" '");
                        var11_15.append(var10_14.getAbsolutePath());
                        var11_15.append("': length ");
                        var11_15.append(var10_14.length());
                        var11_15.append(" - crc: ");
                        var11_15.append(var10_14.crc);
                        Log.i((String)"MultiDex", (String)var11_15.toString());
                        if (!var1_10) {
                            var10_14.delete();
                            if (var10_14.exists()) {
                                var6_12 /* !! */  = new StringBuilder();
                                var6_12 /* !! */ .append("Failed to delete corrupted secondary dex '");
                                var6_12 /* !! */ .append(var10_14.getPath());
                                var6_12 /* !! */ .append("'");
                                Log.w((String)"MultiDex", (String)var6_12 /* !! */ .toString());
                            }
                        }
                        break;
                    }
                    if (var1_10) {
                        var4_1 = new StringBuilder();
                        var4_1.append("classes");
                        var4_1.append(++var2_9);
                        var4_1.append(".dex");
                        var4_1 = var8_8.getEntry(var4_1.toString());
                        continue;
                    }
                    break block18;
                    break;
                } while (true);
            }
            catch (Throwable var4_3) {
                try {
                    var8_8.close();
                    throw var4_3;
                }
                catch (IOException var5_5) {
                    Log.w((String)"MultiDex", (String)"Failed to close resource", (Throwable)var5_5);
                }
                throw var4_3;
            }
            ++var3_11;
            ** GOTO lbl38
        }
        var4_1 = new StringBuilder();
        var4_1.append("Could not create zip file ");
        var4_1.append(var10_14.getAbsolutePath());
        var4_1.append(" for secondary dex (");
        var4_1.append(var2_9);
        var4_1.append(")");
        throw new IOException(var4_1.toString());
    }

    private static void putStoredApkInfo(Context context, String string2, long l, long l2, List<ExtractedDex> object) {
        context = MultiDexExtractor.getMultiDexPreferences(context).edit();
        Serializable serializable = new StringBuilder();
        serializable.append(string2);
        serializable.append("timestamp");
        context.putLong(serializable.toString(), l);
        serializable = new StringBuilder();
        serializable.append(string2);
        serializable.append("crc");
        context.putLong(serializable.toString(), l2);
        serializable = new StringBuilder();
        serializable.append(string2);
        serializable.append("dex.number");
        context.putInt(serializable.toString(), object.size() + 1);
        int n = 2;
        object = object.iterator();
        while (object.hasNext()) {
            serializable = (ExtractedDex)object.next();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("dex.crc.");
            stringBuilder.append(n);
            context.putLong(stringBuilder.toString(), serializable.crc);
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("dex.time.");
            stringBuilder.append(n);
            context.putLong(stringBuilder.toString(), serializable.lastModified());
            ++n;
        }
        context.commit();
    }

    @Override
    public void close() throws IOException {
        this.cacheLock.release();
        this.lockChannel.close();
        this.lockRaf.close();
    }

    List<? extends File> load(Context list, String charSequence, boolean bl) throws IOException {
        List<ExtractedDex> list2 = new StringBuilder();
        list2.append("MultiDexExtractor.load(");
        list2.append(this.sourceApk.getPath());
        list2.append(", ");
        list2.append(bl);
        list2.append(", ");
        list2.append(charSequence);
        list2.append(")");
        Log.i((String)"MultiDex", (String)list2.toString());
        if (this.cacheLock.isValid()) {
            if (!bl && !MultiDexExtractor.isModified((Context)list, this.sourceApk, this.sourceCrc, charSequence)) {
                try {
                    list = list2 = this.loadExistingExtractions((Context)list, (String)charSequence);
                }
                catch (IOException iOException) {
                    Log.w((String)"MultiDex", (String)"Failed to reload existing extracted secondary dex files, falling back to fresh extraction", (Throwable)iOException);
                    List<ExtractedDex> list3 = this.performExtractions();
                    MultiDexExtractor.putStoredApkInfo(list, charSequence, MultiDexExtractor.getTimeStamp(this.sourceApk), this.sourceCrc, list3);
                    list = list3;
                }
            } else {
                if (bl) {
                    Log.i((String)"MultiDex", (String)"Forced extraction must be performed.");
                } else {
                    Log.i((String)"MultiDex", (String)"Detected that extraction must be performed.");
                }
                list2 = this.performExtractions();
                MultiDexExtractor.putStoredApkInfo((Context)list, charSequence, MultiDexExtractor.getTimeStamp(this.sourceApk), this.sourceCrc, list2);
                list = list2;
            }
            charSequence = new StringBuilder();
            charSequence.append("load found ");
            charSequence.append(list.size());
            charSequence.append(" secondary dex files");
            Log.i((String)"MultiDex", (String)charSequence.toString());
            return list;
        }
        throw new IllegalStateException("MultiDexExtractor was closed");
    }

    private static class ExtractedDex
    extends File {
        public long crc = -1L;

        public ExtractedDex(File file, String string2) {
            super(file, string2);
        }
    }

}

