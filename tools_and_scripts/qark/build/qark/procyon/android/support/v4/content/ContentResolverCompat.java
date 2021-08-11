// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import android.os.OperationCanceledException;
import android.os.Build$VERSION;
import android.database.Cursor;
import android.support.v4.os.CancellationSignal;
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
                        // iftrue(Label_0075:, cancellationSignal == null)
                        while (true) {
                            cancellationSignal.throwIfCanceled();
                            Label_0075: {
                                return contentResolver.query(uri, array, s, array2, s2);
                            }
                            continue;
                        }
                        // iftrue(Label_0060:, !ex instanceof OperationCanceledException)
                        throw new android.support.v4.os.OperationCanceledException();
                        Label_0060: {
                            throw;
                        }
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
