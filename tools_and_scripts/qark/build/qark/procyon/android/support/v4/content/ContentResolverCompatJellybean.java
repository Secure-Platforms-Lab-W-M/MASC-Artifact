// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import android.os.CancellationSignal;
import android.database.Cursor;
import android.net.Uri;
import android.content.ContentResolver;
import android.os.OperationCanceledException;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(16)
@RequiresApi(16)
class ContentResolverCompatJellybean
{
    static boolean isFrameworkOperationCanceledException(final Exception ex) {
        return ex instanceof OperationCanceledException;
    }
    
    public static Cursor query(final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2, final Object o) {
        return contentResolver.query(uri, array, s, array2, s2, (CancellationSignal)o);
    }
}
