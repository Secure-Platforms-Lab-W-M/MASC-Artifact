// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.content.ContentResolver;
import android.util.Log;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class DocumentsContractApi19
{
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";
    
    public static boolean canRead(final Context context, final Uri uri) {
        return context.checkCallingOrSelfUriPermission(uri, 1) == 0 && !TextUtils.isEmpty((CharSequence)getRawType(context, uri));
    }
    
    public static boolean canWrite(final Context context, final Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            return false;
        }
        final String rawType = getRawType(context, uri);
        final int queryForInt = queryForInt(context, uri, "flags", 0);
        return !TextUtils.isEmpty((CharSequence)rawType) && ((queryForInt & 0x4) != 0x0 || ("vnd.android.document/directory".equals(rawType) && (queryForInt & 0x8) != 0x0) || (!TextUtils.isEmpty((CharSequence)rawType) && (queryForInt & 0x2) != 0x0));
    }
    
    private static void closeQuietly(final AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            }
            catch (Exception ex2) {}
            catch (RuntimeException ex) {
                throw ex;
            }
        }
    }
    
    public static boolean delete(final Context context, final Uri uri) {
        try {
            return DocumentsContract.deleteDocument(context.getContentResolver(), uri);
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean exists(Context query, final Uri uri) {
        final ContentResolver contentResolver = query.getContentResolver();
        Object o = null;
        query = null;
        boolean b = true;
        try {
            try {
                final Context context = (Context)(o = (query = (Context)contentResolver.query(uri, new String[] { "document_id" }, (String)null, (String[])null, (String)null)));
                if (((Cursor)context).getCount() <= 0) {
                    b = false;
                }
                closeQuietly((AutoCloseable)context);
                return b;
            }
            finally {}
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed query: ");
            sb.append(ex);
            Log.w("DocumentFile", sb.toString());
            closeQuietly((AutoCloseable)o);
            return false;
        }
        closeQuietly((AutoCloseable)query);
    }
    
    public static long getFlags(final Context context, final Uri uri) {
        return queryForLong(context, uri, "flags", 0L);
    }
    
    public static String getName(final Context context, final Uri uri) {
        return queryForString(context, uri, "_display_name", null);
    }
    
    private static String getRawType(final Context context, final Uri uri) {
        return queryForString(context, uri, "mime_type", null);
    }
    
    public static String getType(final Context context, final Uri uri) {
        final String rawType = getRawType(context, uri);
        if ("vnd.android.document/directory".equals(rawType)) {
            return null;
        }
        return rawType;
    }
    
    public static boolean isDirectory(final Context context, final Uri uri) {
        return "vnd.android.document/directory".equals(getRawType(context, uri));
    }
    
    public static boolean isDocumentUri(final Context context, final Uri uri) {
        return DocumentsContract.isDocumentUri(context, uri);
    }
    
    public static boolean isFile(final Context context, final Uri uri) {
        final String rawType = getRawType(context, uri);
        return !"vnd.android.document/directory".equals(rawType) && !TextUtils.isEmpty((CharSequence)rawType);
    }
    
    public static boolean isVirtual(final Context context, final Uri uri) {
        final boolean documentUri = isDocumentUri(context, uri);
        boolean b = false;
        if (!documentUri) {
            return false;
        }
        if ((getFlags(context, uri) & 0x200L) != 0x0L) {
            b = true;
        }
        return b;
    }
    
    public static long lastModified(final Context context, final Uri uri) {
        return queryForLong(context, uri, "last_modified", 0L);
    }
    
    public static long length(final Context context, final Uri uri) {
        return queryForLong(context, uri, "_size", 0L);
    }
    
    private static int queryForInt(final Context context, final Uri uri, final String s, final int n) {
        return (int)queryForLong(context, uri, s, n);
    }
    
    private static long queryForLong(Context query, final Uri uri, final String s, final long n) {
        final ContentResolver contentResolver = query.getContentResolver();
        Object o = null;
        query = null;
        try {
            try {
                final Object o2 = o = (query = (Context)contentResolver.query(uri, new String[] { s }, (String)null, (String[])null, (String)null));
                if (((Cursor)o2).moveToFirst()) {
                    query = (Context)o2;
                    o = o2;
                    if (!((Cursor)o2).isNull(0)) {
                        query = (Context)o2;
                        o = o2;
                        final long long1 = ((Cursor)o2).getLong(0);
                        closeQuietly((AutoCloseable)o2);
                        return long1;
                    }
                }
                closeQuietly((AutoCloseable)o2);
                return n;
            }
            finally {}
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed query: ");
            sb.append(ex);
            Log.w("DocumentFile", sb.toString());
            closeQuietly((AutoCloseable)o);
            return n;
        }
        closeQuietly((AutoCloseable)query);
    }
    
    private static String queryForString(Context query, final Uri uri, String string, final String s) {
        final ContentResolver contentResolver = query.getContentResolver();
        Object o = null;
        query = null;
        try {
            try {
                final Object o2 = o = (query = (Context)contentResolver.query(uri, new String[] { string }, (String)null, (String[])null, (String)null));
                if (((Cursor)o2).moveToFirst()) {
                    query = (Context)o2;
                    o = o2;
                    if (!((Cursor)o2).isNull(0)) {
                        query = (Context)o2;
                        o = o2;
                        string = ((Cursor)o2).getString(0);
                        closeQuietly((AutoCloseable)o2);
                        return string;
                    }
                }
                closeQuietly((AutoCloseable)o2);
                return s;
            }
            finally {}
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed query: ");
            sb.append(ex);
            Log.w("DocumentFile", sb.toString());
            closeQuietly((AutoCloseable)o);
            return s;
        }
        closeQuietly((AutoCloseable)query);
    }
}
