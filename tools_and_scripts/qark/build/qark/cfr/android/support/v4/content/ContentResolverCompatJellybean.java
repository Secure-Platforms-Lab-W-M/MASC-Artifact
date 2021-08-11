/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.ContentResolver
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.CancellationSignal
 *  android.os.OperationCanceledException
 */
package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.support.annotation.RequiresApi;

@TargetApi(value=16)
@RequiresApi(value=16)
class ContentResolverCompatJellybean {
    ContentResolverCompatJellybean() {
    }

    static boolean isFrameworkOperationCanceledException(Exception exception) {
        return exception instanceof OperationCanceledException;
    }

    public static Cursor query(ContentResolver contentResolver, Uri uri, String[] arrstring, String string2, String[] arrstring2, String string3, Object object) {
        return contentResolver.query(uri, arrstring, string2, arrstring2, string3, (CancellationSignal)object);
    }
}

