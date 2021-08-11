// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics;

import android.content.ContentResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.Context;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.Os;
import java.io.File;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl
{
    private static final String TAG = "TypefaceCompatApi21Impl";
    
    private File getFile(final ParcelFileDescriptor parcelFileDescriptor) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("/proc/self/fd/");
            sb.append(parcelFileDescriptor.getFd());
            final String readlink = Os.readlink(sb.toString());
            if (OsConstants.S_ISREG(Os.stat(readlink).st_mode)) {
                return new File(readlink);
            }
            return null;
        }
        catch (ErrnoException ex) {
            return null;
        }
    }
    
    @Override
    public Typeface createFromFontInfo(final Context context, CancellationSignal t, @NonNull FontsContractCompat.FontInfo[] openFileDescriptor, final int n) {
        if (openFileDescriptor.length < 1) {
            return null;
        }
        final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(openFileDescriptor, n);
        final ContentResolver contentResolver = context.getContentResolver();
        try {
            openFileDescriptor = (FontsContractCompat.FontInfo[])(Object)contentResolver.openFileDescriptor(bestInfo.getUri(), "r", (CancellationSignal)t);
            try {
                final File file = this.getFile((ParcelFileDescriptor)(Object)openFileDescriptor);
                if (file != null && file.canRead()) {
                    final Typeface fromFile = Typeface.createFromFile(file);
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return fromFile;
                }
                final FileInputStream fileInputStream = new FileInputStream(((ParcelFileDescriptor)(Object)openFileDescriptor).getFileDescriptor());
                try {
                    final Typeface fromInputStream = super.createFromInputStream(context, fileInputStream);
                    fileInputStream.close();
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return fromInputStream;
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
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    catch (Throwable t3) {
                        t.addSuppressed(t3);
                    }
                }
                else {
                    ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                }
            }
            throw context;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
