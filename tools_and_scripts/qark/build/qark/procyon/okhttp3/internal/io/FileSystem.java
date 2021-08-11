// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.io;

import okio.Source;
import java.io.IOException;
import java.io.FileNotFoundException;
import okio.Okio;
import okio.Sink;
import java.io.File;

public interface FileSystem
{
    public static final FileSystem SYSTEM = new FileSystem() {
        @Override
        public Sink appendingSink(final File file) throws FileNotFoundException {
            try {
                return Okio.appendingSink(file);
            }
            catch (FileNotFoundException ex) {
                file.getParentFile().mkdirs();
                return Okio.appendingSink(file);
            }
        }
        
        @Override
        public void delete(final File file) throws IOException {
            if (!file.delete() && file.exists()) {
                throw new IOException("failed to delete " + file);
            }
        }
        
        @Override
        public void deleteContents(File file) throws IOException {
            final File[] listFiles = file.listFiles();
            if (listFiles == null) {
                throw new IOException("not a readable directory: " + file);
            }
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                file = listFiles[i];
                if (file.isDirectory()) {
                    this.deleteContents(file);
                }
                if (!file.delete()) {
                    throw new IOException("failed to delete " + file);
                }
            }
        }
        
        @Override
        public boolean exists(final File file) {
            return file.exists();
        }
        
        @Override
        public void rename(final File file, final File file2) throws IOException {
            this.delete(file2);
            if (!file.renameTo(file2)) {
                throw new IOException("failed to rename " + file + " to " + file2);
            }
        }
        
        @Override
        public Sink sink(final File file) throws FileNotFoundException {
            try {
                return Okio.sink(file);
            }
            catch (FileNotFoundException ex) {
                file.getParentFile().mkdirs();
                return Okio.sink(file);
            }
        }
        
        @Override
        public long size(final File file) {
            return file.length();
        }
        
        @Override
        public Source source(final File file) throws FileNotFoundException {
            return Okio.source(file);
        }
    };
    
    Sink appendingSink(final File p0) throws FileNotFoundException;
    
    void delete(final File p0) throws IOException;
    
    void deleteContents(final File p0) throws IOException;
    
    boolean exists(final File p0);
    
    void rename(final File p0, final File p1) throws IOException;
    
    Sink sink(final File p0) throws FileNotFoundException;
    
    long size(final File p0);
    
    Source source(final File p0) throws FileNotFoundException;
}
