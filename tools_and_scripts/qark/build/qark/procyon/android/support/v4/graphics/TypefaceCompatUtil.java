// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics;

import android.os.ParcelFileDescriptor;
import java.nio.MappedByteBuffer;
import android.content.ContentResolver;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.support.annotation.RequiresApi;
import java.io.File;
import java.nio.ByteBuffer;
import android.content.res.Resources;
import android.content.Context;
import java.io.IOException;
import java.io.Closeable;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
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
    
    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(final Context context, final Resources resources, final int n) {
        final File tempFile = getTempFile(context);
        ByteBuffer mmap = null;
        if (tempFile == null) {
            return null;
        }
        try {
            if (copyToFile(tempFile, resources, n)) {
                mmap = mmap(tempFile);
            }
            return mmap;
        }
        finally {
            tempFile.delete();
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
            return false;
        }
        closeQuietly(closeable2);
    }
    
    public static File getTempFile(final Context context) {
        final StringBuilder sb = new StringBuilder();
        sb.append(".font");
        sb.append(Process.myPid());
        sb.append("-");
        sb.append(Process.myTid());
        sb.append("-");
        final String string = sb.toString();
        for (int i = 0; i < 100; ++i) {
            final File cacheDir = context.getCacheDir();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append(i);
            final File file = new File(cacheDir, sb2.toString());
            try {
                if (file.createNewFile()) {
                    return file;
                }
            }
            catch (IOException ex) {}
        }
        return null;
    }
    
    @RequiresApi(19)
    public static ByteBuffer mmap(final Context context, CancellationSignal t, Uri openFileDescriptor) {
        final ContentResolver contentResolver = context.getContentResolver();
        try {
            openFileDescriptor = (Uri)contentResolver.openFileDescriptor(openFileDescriptor, "r", (CancellationSignal)t);
            try {
                final FileInputStream fileInputStream = new FileInputStream(((ParcelFileDescriptor)openFileDescriptor).getFileDescriptor());
                try {
                    final FileChannel channel = fileInputStream.getChannel();
                    final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                    fileInputStream.close();
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    return map;
                }
                catch (Throwable t) {
                    try {
                        throw t;
                    }
                    finally {}
                }
                finally {
                    t = null;
                }
                if (t != null) {
                    try {
                        fileInputStream.close();
                    }
                    catch (Throwable t2) {
                        t.addSuppressed(t2);
                    }
                }
                else {
                    fileInputStream.close();
                }
                throw context;
            }
            catch (Throwable t) {
                try {
                    throw t;
                }
                finally {}
            }
            finally {
                t = null;
            }
            if (openFileDescriptor != null) {
                if (t != null) {
                    try {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    catch (Throwable t3) {
                        t.addSuppressed(t3);
                    }
                }
                else {
                    ((ParcelFileDescriptor)openFileDescriptor).close();
                }
            }
            throw context;
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @RequiresApi(19)
    private static ByteBuffer mmap(final File file) {
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            Throwable t = null;
            try {
                final FileChannel channel = fileInputStream.getChannel();
                final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                fileInputStream.close();
                return map;
            }
            catch (Throwable t) {
                try {
                    throw t;
                }
                finally {}
            }
            finally {
                t = null;
            }
            if (t != null) {
                try {
                    fileInputStream.close();
                }
                catch (Throwable t2) {
                    t.addSuppressed(t2);
                }
            }
            else {
                fileInputStream.close();
            }
            throw file;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
