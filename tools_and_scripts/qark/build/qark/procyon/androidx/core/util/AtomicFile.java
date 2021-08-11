// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.util;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import android.util.Log;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;

public class AtomicFile
{
    private final File mBackupName;
    private final File mBaseName;
    
    public AtomicFile(final File mBaseName) {
        this.mBaseName = mBaseName;
        final StringBuilder sb = new StringBuilder();
        sb.append(mBaseName.getPath());
        sb.append(".bak");
        this.mBackupName = new File(sb.toString());
    }
    
    private static boolean sync(final FileOutputStream fileOutputStream) {
        try {
            fileOutputStream.getFD().sync();
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }
    
    public void failWrite(final FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBaseName.delete();
                this.mBackupName.renameTo(this.mBaseName);
            }
            catch (IOException ex) {
                Log.w("AtomicFile", "failWrite: Got exception:", (Throwable)ex);
            }
        }
    }
    
    public void finishWrite(final FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBackupName.delete();
            }
            catch (IOException ex) {
                Log.w("AtomicFile", "finishWrite: Got exception:", (Throwable)ex);
            }
        }
    }
    
    public File getBaseFile() {
        return this.mBaseName;
    }
    
    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }
    
    public byte[] readFully() throws IOException {
        final FileInputStream openRead = this.openRead();
        int n = 0;
        try {
            byte[] array = new byte[openRead.available()];
            while (true) {
                final int read = openRead.read(array, n, array.length - n);
                if (read <= 0) {
                    break;
                }
                n += read;
                final int available = openRead.available();
                byte[] array2 = array;
                if (available > array.length - n) {
                    array2 = new byte[n + available];
                    System.arraycopy(array, 0, array2, 0, n);
                }
                array = array2;
            }
            return array;
        }
        finally {
            openRead.close();
        }
    }
    
    public FileOutputStream startWrite() throws IOException {
        if (this.mBaseName.exists()) {
            if (!this.mBackupName.exists()) {
                if (!this.mBaseName.renameTo(this.mBackupName)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Couldn't rename file ");
                    sb.append(this.mBaseName);
                    sb.append(" to backup file ");
                    sb.append(this.mBackupName);
                    Log.w("AtomicFile", sb.toString());
                }
            }
            else {
                this.mBaseName.delete();
            }
        }
        try {
            return new FileOutputStream(this.mBaseName);
        }
        catch (FileNotFoundException ex) {
            if (this.mBaseName.getParentFile().mkdirs()) {
                try {
                    return new FileOutputStream(this.mBaseName);
                }
                catch (FileNotFoundException ex2) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Couldn't create ");
                    sb2.append(this.mBaseName);
                    throw new IOException(sb2.toString());
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Couldn't create directory ");
            sb3.append(this.mBaseName);
            throw new IOException(sb3.toString());
        }
    }
}
