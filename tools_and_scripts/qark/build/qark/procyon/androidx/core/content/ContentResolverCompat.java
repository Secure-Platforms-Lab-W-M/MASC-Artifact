// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content;

import android.os.OperationCanceledException;
import android.os.Build$VERSION;
import android.database.Cursor;
import androidx.core.os.CancellationSignal;
import android.net.Uri;
import android.content.ContentResolver;

public final class ContentResolverCompat
{
    private ContentResolverCompat() {
    }
    
    public static Cursor query(final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2, final CancellationSignal cancellationSignal) {
        while (true) {
            Label_0062: {
                if (Build$VERSION.SDK_INT < 16) {
                    break Label_0062;
                }
                Label_0023: {
                    if (cancellationSignal == null) {
                        final Object cancellationSignalObject = null;
                        break Label_0023;
                    }
                    try {
                        final Object cancellationSignalObject = cancellationSignal.getCancellationSignalObject();
                        return contentResolver.query(uri, array, s, array2, s2, (android.os.CancellationSignal)cancellationSignalObject);
                        // iftrue(Label_0060:, !ex instanceof OperationCanceledException)
                        throw new androidx.core.os.OperationCanceledException();
                        Label_0072: {
                            return contentResolver.query(uri, array, s, array2, s2);
                        }
                        final Exception ex;
                        Label_0060:
                        throw ex;
                        // iftrue(Label_0072:, cancellationSignal == null)
                        cancellationSignal.throwIfCanceled();
                        return contentResolver.query(uri, array, s, array2, s2);
                    }
                    catch (Exception ex2) {}
                }
            }
            final Exception ex2;
            final Exception ex = ex2;
            continue;
        }
    }
}
