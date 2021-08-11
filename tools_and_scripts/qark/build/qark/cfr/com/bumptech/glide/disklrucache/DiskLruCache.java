/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.os.StrictMode$ThreadPolicy$Builder
 */
package com.bumptech.glide.disklrucache;

import android.os.Build;
import android.os.StrictMode;
import com.bumptech.glide.disklrucache.StrictLineReader;
import com.bumptech.glide.disklrucache.Util;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache
implements Closeable {
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
    private final Callable<Void> cleanupCallable;
    private final File directory;
    final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new DiskLruCacheThreadFactory());
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap(0, 0.75f, true);
    private long maxSize;
    private long nextSequenceNumber = 0L;
    private int redundantOpCount;
    private long size = 0L;
    private final int valueCount;

    private DiskLruCache(File file, int n, int n2, long l) {
        this.cleanupCallable = new Callable<Void>(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public Void call() throws Exception {
                DiskLruCache diskLruCache = DiskLruCache.this;
                synchronized (diskLruCache) {
                    if (DiskLruCache.this.journalWriter == null) {
                        return null;
                    }
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                    return null;
                }
            }
        };
        this.directory = file;
        this.appVersion = n;
        this.journalFile = new File(file, "journal");
        this.journalFileTmp = new File(file, "journal.tmp");
        this.journalFileBackup = new File(file, "journal.bkp");
        this.valueCount = n2;
        this.maxSize = l;
    }

    private void checkNotClosed() {
        if (this.journalWriter != null) {
            return;
        }
        throw new IllegalStateException("cache is closed");
    }

    private static void closeWriter(Writer writer) throws IOException {
        if (Build.VERSION.SDK_INT < 26) {
            writer.close();
            return;
        }
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)new StrictMode.ThreadPolicy.Builder(threadPolicy).permitUnbufferedIo().build());
        try {
            writer.close();
            return;
        }
        finally {
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void completeEdit(Editor object, boolean bl) throws IOException {
        synchronized (this) {
            void var2_2;
            int n;
            Entry entry = ((Editor)object).entry;
            if (entry.currentEditor != object) {
                throw new IllegalStateException();
            }
            if (var2_2 != false && !entry.readable) {
                for (n = 0; n < this.valueCount; ++n) {
                    if (!((Editor)object).written[n]) {
                        object.abort();
                        object = new StringBuilder();
                        object.append("Newly created entry didn't create value for index ");
                        object.append(n);
                        throw new IllegalStateException(object.toString());
                    }
                    if (entry.getDirtyFile(n).exists()) continue;
                    object.abort();
                    return;
                }
            }
            n = 0;
            do {
                long l;
                if (n < this.valueCount) {
                    object = entry.getDirtyFile(n);
                    if (var2_2 != false) {
                        if (object.exists()) {
                            long l2;
                            File file = entry.getCleanFile(n);
                            object.renameTo(file);
                            l = entry.lengths[n];
                            Entry.access$1100((Entry)entry)[n] = l2 = file.length();
                            this.size = this.size - l + l2;
                        }
                    } else {
                        DiskLruCache.deleteIfExists((File)object);
                    }
                } else {
                    ++this.redundantOpCount;
                    entry.currentEditor = null;
                    if ((entry.readable | var2_2) != 0) {
                        entry.readable = true;
                        this.journalWriter.append("CLEAN");
                        this.journalWriter.append(' ');
                        this.journalWriter.append(entry.key);
                        this.journalWriter.append(entry.getLengths());
                        this.journalWriter.append('\n');
                        if (var2_2 != false) {
                            l = this.nextSequenceNumber;
                            this.nextSequenceNumber = 1L + l;
                            entry.sequenceNumber = l;
                        }
                    } else {
                        this.lruEntries.remove(entry.key);
                        this.journalWriter.append("REMOVE");
                        this.journalWriter.append(' ');
                        this.journalWriter.append(entry.key);
                        this.journalWriter.append('\n');
                    }
                    DiskLruCache.flushWriter(this.journalWriter);
                    if (this.size > this.maxSize || this.journalRebuildRequired()) {
                        this.executorService.submit(this.cleanupCallable);
                    }
                    return;
                }
                ++n;
            } while (true);
        }
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists()) {
            if (file.delete()) {
                return;
            }
            throw new IOException();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Editor edit(String string2, long l) throws IOException {
        synchronized (this) {
            long l2;
            void var2_2;
            Editor editor;
            this.checkNotClosed();
            Entry entry = this.lruEntries.get(string2);
            if (var2_2 != -1L && (entry == null || (l2 = entry.sequenceNumber) != var2_2)) {
                return null;
            }
            if (entry == null) {
                entry = new Entry(string2);
                this.lruEntries.put(string2, entry);
            } else {
                editor = entry.currentEditor;
                if (editor != null) {
                    return null;
                }
            }
            editor = new Editor(entry);
            entry.currentEditor = editor;
            this.journalWriter.append("DIRTY");
            this.journalWriter.append(' ');
            this.journalWriter.append(string2);
            this.journalWriter.append('\n');
            DiskLruCache.flushWriter(this.journalWriter);
            return editor;
        }
    }

    private static void flushWriter(Writer writer) throws IOException {
        if (Build.VERSION.SDK_INT < 26) {
            writer.flush();
            return;
        }
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)new StrictMode.ThreadPolicy.Builder(threadPolicy).permitUnbufferedIo().build());
        try {
            writer.flush();
            return;
        }
        finally {
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        }
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        return Util.readFully(new InputStreamReader(inputStream, Util.UTF_8));
    }

    private boolean journalRebuildRequired() {
        int n = this.redundantOpCount;
        if (n >= 2000 && n >= this.lruEntries.size()) {
            return true;
        }
        return false;
    }

    public static DiskLruCache open(File object, int n, int n2, long l) throws IOException {
        if (l > 0L) {
            if (n2 > 0) {
                Object object2 = new File((File)object, "journal.bkp");
                if (object2.exists()) {
                    File file = new File((File)object, "journal");
                    if (file.exists()) {
                        object2.delete();
                    } else {
                        DiskLruCache.renameTo((File)object2, file, false);
                    }
                }
                object2 = new DiskLruCache((File)object, n, n2, l);
                if (object2.journalFile.exists()) {
                    try {
                        DiskLruCache.super.readJournal();
                        DiskLruCache.super.processJournal();
                        return object2;
                    }
                    catch (IOException iOException) {
                        PrintStream printStream = System.out;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("DiskLruCache ");
                        stringBuilder.append(object);
                        stringBuilder.append(" is corrupt: ");
                        stringBuilder.append(iOException.getMessage());
                        stringBuilder.append(", removing");
                        printStream.println(stringBuilder.toString());
                        object2.delete();
                    }
                }
                object.mkdirs();
                object = new DiskLruCache((File)object, n, n2, l);
                DiskLruCache.super.rebuildJournal();
                return object;
            }
            throw new IllegalArgumentException("valueCount <= 0");
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    private void processJournal() throws IOException {
        DiskLruCache.deleteIfExists(this.journalFileTmp);
        Iterator<Entry> iterator = this.lruEntries.values().iterator();
        while (iterator.hasNext()) {
            int n;
            Entry entry = iterator.next();
            if (entry.currentEditor == null) {
                for (n = 0; n < this.valueCount; ++n) {
                    this.size += entry.lengths[n];
                }
                continue;
            }
            entry.currentEditor = null;
            for (n = 0; n < this.valueCount; ++n) {
                DiskLruCache.deleteIfExists(entry.getCleanFile(n));
                DiskLruCache.deleteIfExists(entry.getDirtyFile(n));
            }
            iterator.remove();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void readJournal() throws IOException {
        strictLineReader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
        string32 = strictLineReader.readLine();
        string6 = strictLineReader.readLine();
        string4 = strictLineReader.readLine();
        string3 = strictLineReader.readLine();
        string2 = strictLineReader.readLine();
        if (!("libcore.io.DiskLruCache".equals(string32) && "1".equals(string6) && Integer.toString(this.appVersion).equals(string4) && Integer.toString(this.valueCount).equals(string3) && (bl = "".equals(string2)))) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected journal header: [");
            stringBuilder.append(string32);
            stringBuilder.append(", ");
            stringBuilder.append(string6);
            stringBuilder.append(", ");
            stringBuilder.append(string3);
            stringBuilder.append(", ");
            stringBuilder.append(string2);
            stringBuilder.append("]");
            throw new IOException(stringBuilder.toString());
        }
        n = 0;
        ** GOTO lbl24
        {
            catch (Throwable throwable) {}
            throw throwable;
lbl24: // 1 sources:
            try {
                do {
                    this.readJournalLine(strictLineReader.readLine());
                    ++n;
                } while (true);
            }
            catch (EOFException eOFException) {}
            this.redundantOpCount = n - this.lruEntries.size();
            if (strictLineReader.hasUnterminatedLine()) {
                this.rebuildJournal();
                return;
            }
            this.journalWriter = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(this.journalFile, true), Util.US_ASCII));
            return;
        }
    }

    private void readJournalLine(String arrstring) throws IOException {
        int n = arrstring.indexOf(32);
        if (n != -1) {
            CharSequence charSequence2;
            CharSequence charSequence2;
            Object object;
            int n2 = n + 1;
            int n3 = arrstring.indexOf(32, n2);
            if (n3 == -1) {
                object = arrstring.substring(n2);
                charSequence2 = object;
                if (n == "REMOVE".length()) {
                    charSequence2 = object;
                    if (arrstring.startsWith("REMOVE")) {
                        this.lruEntries.remove(object);
                        return;
                    }
                }
            } else {
                charSequence2 = arrstring.substring(n2, n3);
            }
            Entry entry = this.lruEntries.get(charSequence2);
            object = entry;
            if (entry == null) {
                object = new Entry((String)charSequence2);
                this.lruEntries.put((String)charSequence2, (Entry)object);
            }
            if (n3 != -1 && n == "CLEAN".length() && arrstring.startsWith("CLEAN")) {
                arrstring = arrstring.substring(n3 + 1).split(" ");
                ((Entry)object).readable = true;
                ((Entry)object).currentEditor = null;
                ((Entry)object).setLengths(arrstring);
                return;
            }
            if (n3 == -1 && n == "DIRTY".length() && arrstring.startsWith("DIRTY")) {
                ((Entry)object).currentEditor = new Editor((Entry)object);
                return;
            }
            if (n3 == -1 && n == "READ".length() && arrstring.startsWith("READ")) {
                return;
            }
            charSequence2 = new StringBuilder();
            charSequence2.append("unexpected journal line: ");
            charSequence2.append((String)arrstring);
            throw new IOException(charSequence2.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected journal line: ");
        stringBuilder.append((String)arrstring);
        throw new IOException(stringBuilder.toString());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void rebuildJournal() throws IOException {
        synchronized (this) {
            BufferedWriter bufferedWriter;
            block9 : {
                if (this.journalWriter != null) {
                    DiskLruCache.closeWriter(this.journalWriter);
                }
                bufferedWriter = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(this.journalFileTmp), Util.US_ASCII));
                bufferedWriter.write("libcore.io.DiskLruCache");
                bufferedWriter.write("\n");
                bufferedWriter.write("1");
                bufferedWriter.write("\n");
                bufferedWriter.write(Integer.toString(this.appVersion));
                bufferedWriter.write("\n");
                bufferedWriter.write(Integer.toString(this.valueCount));
                bufferedWriter.write("\n");
                bufferedWriter.write("\n");
                for (Entry entry : this.lruEntries.values()) {
                    StringBuilder stringBuilder;
                    if (entry.currentEditor != null) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("DIRTY ");
                        stringBuilder.append(entry.key);
                        stringBuilder.append('\n');
                        bufferedWriter.write(stringBuilder.toString());
                        continue;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("CLEAN ");
                    stringBuilder.append(entry.key);
                    stringBuilder.append(entry.getLengths());
                    stringBuilder.append('\n');
                    bufferedWriter.write(stringBuilder.toString());
                }
                if (!this.journalFile.exists()) break block9;
                DiskLruCache.renameTo(this.journalFile, this.journalFileBackup, true);
            }
            DiskLruCache.renameTo(this.journalFileTmp, this.journalFile, false);
            this.journalFileBackup.delete();
            this.journalWriter = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(this.journalFile, true), Util.US_ASCII));
            return;
            finally {
                DiskLruCache.closeWriter(bufferedWriter);
            }
        }
    }

    private static void renameTo(File file, File file2, boolean bl) throws IOException {
        if (bl) {
            DiskLruCache.deleteIfExists(file2);
        }
        if (file.renameTo(file2)) {
            return;
        }
        throw new IOException();
    }

    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            this.remove(this.lruEntries.entrySet().iterator().next().getKey());
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            block5 : {
                Writer writer = this.journalWriter;
                if (writer != null) break block5;
                return;
            }
            for (Entry entry : new ArrayList<Entry>(this.lruEntries.values())) {
                if (entry.currentEditor == null) continue;
                entry.currentEditor.abort();
            }
            this.trimToSize();
            DiskLruCache.closeWriter(this.journalWriter);
            this.journalWriter = null;
            return;
        }
    }

    public void delete() throws IOException {
        this.close();
        Util.deleteContents(this.directory);
    }

    public Editor edit(String string2) throws IOException {
        return this.edit(string2, -1L);
    }

    public void flush() throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            this.trimToSize();
            DiskLruCache.flushWriter(this.journalWriter);
            return;
        }
    }

    public Value get(String object) throws IOException {
        synchronized (this) {
            boolean bl;
            Entry entry;
            block10 : {
                block9 : {
                    this.checkNotClosed();
                    entry = this.lruEntries.get(object);
                    if (entry != null) break block9;
                    return null;
                }
                bl = entry.readable;
                if (bl) break block10;
                return null;
            }
            File[] arrfile = entry.cleanFiles;
            int n = arrfile.length;
            for (int i = 0; i < n; ++i) {
                bl = arrfile[i].exists();
                if (bl) continue;
                return null;
            }
            ++this.redundantOpCount;
            this.journalWriter.append("READ");
            this.journalWriter.append(' ');
            this.journalWriter.append((CharSequence)object);
            this.journalWriter.append('\n');
            if (this.journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            object = new Value((String)object, entry.sequenceNumber, entry.cleanFiles, entry.lengths);
            return object;
        }
    }

    public File getDirectory() {
        return this.directory;
    }

    public long getMaxSize() {
        synchronized (this) {
            long l = this.maxSize;
            return l;
        }
    }

    public boolean isClosed() {
        synchronized (this) {
            Writer writer = this.journalWriter;
            boolean bl = writer == null;
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean remove(String charSequence) throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            Entry entry = this.lruEntries.get(charSequence);
            if (entry == null || entry.currentEditor != null) {
                return false;
            }
            for (int i = 0; i < this.valueCount; this.size -= Entry.access$1100((Entry)entry)[i], ++i) {
                File file = entry.getCleanFile(i);
                if (file.exists() && !file.delete()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("failed to delete ");
                    stringBuilder.append(file);
                    throw new IOException(stringBuilder.toString());
                }
                Entry.access$1100((Entry)entry)[i] = 0L;
            }
            ++this.redundantOpCount;
            this.journalWriter.append("REMOVE");
            this.journalWriter.append(' ');
            this.journalWriter.append(charSequence);
            this.journalWriter.append('\n');
            this.lruEntries.remove(charSequence);
            if (this.journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            return true;
        }
    }

    public void setMaxSize(long l) {
        synchronized (this) {
            this.maxSize = l;
            this.executorService.submit(this.cleanupCallable);
            return;
        }
    }

    public long size() {
        synchronized (this) {
            long l = this.size;
            return l;
        }
    }

    private static final class DiskLruCacheThreadFactory
    implements ThreadFactory {
        private DiskLruCacheThreadFactory() {
        }

        @Override
        public Thread newThread(Runnable runnable) {
            synchronized (this) {
                runnable = new Thread(runnable, "glide-disk-lru-cache-thread");
                runnable.setPriority(1);
                return runnable;
            }
        }
    }

    public final class Editor {
        private boolean committed;
        private final Entry entry;
        private final boolean[] written;

        private Editor(Entry entry) {
            this.entry = entry;
            DiskLruCache.this = entry.readable ? null : new boolean[((DiskLruCache)DiskLruCache.this).valueCount];
            this.written = DiskLruCache.this;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private InputStream newInputStream(int n) throws IOException {
            DiskLruCache diskLruCache = DiskLruCache.this;
            synchronized (diskLruCache) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    return null;
                }
                try {
                    return new FileInputStream(this.entry.getCleanFile(n));
                }
                catch (FileNotFoundException fileNotFoundException) {
                    return null;
                }
            }
        }

        public void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }

        public void abortUnlessCommitted() {
            if (!this.committed) {
                try {
                    this.abort();
                    return;
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }

        public void commit() throws IOException {
            DiskLruCache.this.completeEdit(this, true);
            this.committed = true;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public File getFile(int n) throws IOException {
            DiskLruCache diskLruCache = DiskLruCache.this;
            synchronized (diskLruCache) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    this.written[n] = true;
                }
                File file = this.entry.getDirtyFile(n);
                if (!DiskLruCache.this.directory.exists()) {
                    DiskLruCache.this.directory.mkdirs();
                }
                return file;
            }
        }

        public String getString(int n) throws IOException {
            InputStream inputStream = this.newInputStream(n);
            if (inputStream != null) {
                return DiskLruCache.inputStreamToString(inputStream);
            }
            return null;
        }

        public void set(int n, String string2) throws IOException {
            OutputStreamWriter outputStreamWriter;
            OutputStreamWriter outputStreamWriter2 = null;
            try {
                outputStreamWriter2 = outputStreamWriter = new OutputStreamWriter((OutputStream)new FileOutputStream(this.getFile(n)), Util.UTF_8);
            }
            catch (Throwable throwable) {
                Util.closeQuietly(outputStreamWriter2);
                throw throwable;
            }
            outputStreamWriter.write(string2);
            Util.closeQuietly(outputStreamWriter);
        }
    }

    private final class Entry {
        File[] cleanFiles;
        private Editor currentEditor;
        File[] dirtyFiles;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;

        private Entry(String charSequence) {
            this.key = charSequence;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            charSequence = new StringBuilder((String)charSequence).append('.');
            int n = charSequence.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; ++i) {
                charSequence.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, charSequence.toString());
                charSequence.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, charSequence.toString());
                charSequence.setLength(n);
            }
        }

        private IOException invalidLengths(String[] arrstring) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected journal line: ");
            stringBuilder.append(Arrays.toString(arrstring));
            throw new IOException(stringBuilder.toString());
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void setLengths(String[] arrstring) throws IOException {
            if (arrstring.length != DiskLruCache.this.valueCount) {
                throw this.invalidLengths(arrstring);
            }
            int n = 0;
            try {
                do {
                    if (n >= arrstring.length) {
                        return;
                    }
                    this.lengths[n] = Long.parseLong(arrstring[n]);
                    ++n;
                } while (true);
            }
            catch (NumberFormatException numberFormatException) {
                throw this.invalidLengths(arrstring);
            }
        }

        public File getCleanFile(int n) {
            return this.cleanFiles[n];
        }

        public File getDirtyFile(int n) {
            return this.dirtyFiles[n];
        }

        public String getLengths() throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            for (long l : this.lengths) {
                stringBuilder.append(' ');
                stringBuilder.append(l);
            }
            return stringBuilder.toString();
        }
    }

    public final class Value {
        private final File[] files;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;

        private Value(String string2, long l, File[] arrfile, long[] arrl) {
            this.key = string2;
            this.sequenceNumber = l;
            this.files = arrfile;
            this.lengths = arrl;
        }

        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public File getFile(int n) {
            return this.files[n];
        }

        public long getLength(int n) {
            return this.lengths[n];
        }

        public String getString(int n) throws IOException {
            return DiskLruCache.inputStreamToString(new FileInputStream(this.files[n]));
        }
    }

}

