// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.graphics;

import android.os.ParcelFileDescriptor;
import java.nio.MappedByteBuffer;
import android.content.ContentResolver;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import android.os.StrictMode$ThreadPolicy;
import android.util.Log;
import java.io.FileOutputStream;
import android.os.StrictMode;
import java.io.InputStream;
import java.io.File;
import java.nio.ByteBuffer;
import android.content.res.Resources;
import android.content.Context;
import java.io.IOException;
import java.io.Closeable;

public class TypefaceCompatUtil
{
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";
    
    private TypefaceCompatUtil() {
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ex) {}
        }
    }
    
    public static ByteBuffer copyToDirectBuffer(Context tempFile, final Resources resources, final int n) {
        tempFile = (Context)getTempFile(tempFile);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!copyToFile((File)tempFile, resources, n)) {
                return null;
            }
            return mmap((File)tempFile);
        }
        finally {
            ((File)tempFile).delete();
        }
    }
    
    public static boolean copyToFile(final File file, final Resources resources, final int n) {
        Closeable openRawResource = null;
        try {
            return copyToFile(file, (InputStream)(openRawResource = resources.openRawResource(n)));
        }
        finally {
            closeQuietly(openRawResource);
        }
    }
    
    public static boolean copyToFile(final File file, final InputStream inputStream) {
        Closeable closeable = null;
        Closeable closeable2 = null;
        final StrictMode$ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        try {
            try {
                final Closeable closeable3 = closeable = (closeable2 = new FileOutputStream(file, (boolean)(0 != 0)));
                final byte[] array = new byte[1024];
                while (true) {
                    closeable2 = closeable3;
                    closeable = closeable3;
                    final int read = inputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    closeable2 = closeable3;
                    closeable = closeable3;
                    ((FileOutputStream)closeable3).write(array, 0, read);
                }
                closeQuietly(closeable3);
                StrictMode.setThreadPolicy(allowThreadDiskWrites);
                return true;
            }
            finally {}
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error copying resource contents to temp file: ");
            sb.append(ex.getMessage());
            Log.e("TypefaceCompatUtil", sb.toString());
            closeQuietly(closeable);
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
            return false;
        }
        closeQuietly(closeable2);
        StrictMode.setThreadPolicy(allowThreadDiskWrites);
    }
    
    public static File getTempFile(Context cacheDir) {
        cacheDir = (Context)cacheDir.getCacheDir();
        if (cacheDir == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(".font");
        sb.append(Process.myPid());
        sb.append("-");
        sb.append(Process.myTid());
        sb.append("-");
        final String string = sb.toString();
        for (int i = 0; i < 100; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append(i);
            final File file = new File((File)cacheDir, sb2.toString());
            try {
                if (file.createNewFile()) {
                    return file;
                }
            }
            catch (IOException ex) {}
        }
        return null;
    }
    
    public static ByteBuffer mmap(Context openFileDescriptor, CancellationSignal cancellationSignal, final Uri uri) {
        final ContentResolver contentResolver = openFileDescriptor.getContentResolver();
        try {
            openFileDescriptor = (Context)contentResolver.openFileDescriptor(uri, "r", cancellationSignal);
            if (openFileDescriptor == null) {
                if (openFileDescriptor != null) {
                    ((ParcelFileDescriptor)openFileDescriptor).close();
                }
                return null;
            }
            try {
                cancellationSignal = (CancellationSignal)new FileInputStream(((ParcelFileDescriptor)openFileDescriptor).getFileDescriptor());
                try {
                    final FileChannel channel = ((FileInputStream)cancellationSignal).getChannel();
                    final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                    ((FileInputStream)cancellationSignal).close();
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    return map;
                }
                finally {
                    try {}
                    finally {
                        try {
                            ((FileInputStream)cancellationSignal).close();
                        }
                        finally {
                            final Throwable t;
                            ((Throwable)uri).addSuppressed(t);
                        }
                    }
                }
            }
            finally {
                try {}
                finally {
                    if (openFileDescriptor != null) {
                        try {
                            ((ParcelFileDescriptor)openFileDescriptor).close();
                        }
                        finally {
                            final Throwable t2;
                            ((Throwable)cancellationSignal).addSuppressed(t2);
                        }
                    }
                }
            }
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    private static ByteBuffer mmap(File file) {
        try {
            file = (File)new FileInputStream(file);
            try {
                final FileChannel channel = ((FileInputStream)file).getChannel();
                final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                ((FileInputStream)file).close();
                return map;
            }
            finally {
                try {}
                finally {
                    try {
                        ((FileInputStream)file).close();
                    }
                    finally {
                        final Throwable t;
                        final Throwable t2;
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (IOException ex) {
            return null;
        }
    }
}
