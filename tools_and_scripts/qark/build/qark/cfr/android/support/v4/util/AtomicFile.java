/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package android.support.v4.util;

import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
    private final File mBackupName;
    private final File mBaseName;

    public AtomicFile(File file) {
        this.mBaseName = file;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(file.getPath());
        stringBuilder.append(".bak");
        this.mBackupName = new File(stringBuilder.toString());
    }

    static boolean sync(FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.getFD().sync();
            }
            catch (IOException iOException) {
                return false;
            }
        }
        return true;
    }

    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }

    public void failWrite(FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            AtomicFile.sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBaseName.delete();
                this.mBackupName.renameTo(this.mBaseName);
                return;
            }
            catch (IOException iOException) {
                Log.w((String)"AtomicFile", (String)"failWrite: Got exception:", (Throwable)iOException);
                return;
            }
        }
    }

    public void finishWrite(FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            AtomicFile.sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBackupName.delete();
                return;
            }
            catch (IOException iOException) {
                Log.w((String)"AtomicFile", (String)"finishWrite: Got exception:", (Throwable)iOException);
                return;
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
        int n;
        byte[] arrby;
        FileInputStream fileInputStream = this.openRead();
        int n2 = 0;
        try {
            arrby = new byte[fileInputStream.available()];
            do {
                if ((n = fileInputStream.read(arrby, n2, arrby.length - n2)) > 0) break block4;
                break;
            } while (true);
        }
        catch (Throwable throwable) {
            fileInputStream.close();
            throw throwable;
        }
        {
            block4 : {
                fileInputStream.close();
                return arrby;
            }
            n2 += n;
            n = fileInputStream.available();
            if (n <= arrby.length - n2) continue;
            byte[] arrby2 = new byte[n2 + n];
            System.arraycopy(arrby, 0, arrby2, 0, n2);
            arrby = arrby2;
            continue;
        }
    }

    public FileOutputStream startWrite() throws IOException {
        Object object;
        if (this.mBaseName.exists()) {
            if (!this.mBackupName.exists()) {
                if (!this.mBaseName.renameTo(this.mBackupName)) {
                    object = new StringBuilder();
                    object.append("Couldn't rename file ");
                    object.append(this.mBaseName);
                    object.append(" to backup file ");
                    object.append(this.mBackupName);
                    Log.w((String)"AtomicFile", (String)object.toString());
                }
            } else {
                this.mBaseName.delete();
            }
        }
        try {
            object = new FileOutputStream(this.mBaseName);
            return object;
        }
        catch (FileNotFoundException fileNotFoundException) {
            if (this.mBaseName.getParentFile().mkdirs()) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(this.mBaseName);
                    return fileOutputStream;
                }
                catch (FileNotFoundException fileNotFoundException2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Couldn't create ");
                    stringBuilder.append(this.mBaseName);
                    throw new IOException(stringBuilder.toString());
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't create directory ");
            stringBuilder.append(this.mBaseName);
            throw new IOException(stringBuilder.toString());
        }
    }
}

