/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.provider.DocumentsContract
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

@RequiresApi(value=19)
class DocumentsContractApi19 {
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";

    DocumentsContractApi19() {
    }

    public static boolean canRead(Context context, Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 1) != 0) {
            return false;
        }
        if (TextUtils.isEmpty((CharSequence)DocumentsContractApi19.getRawType(context, uri))) {
            return false;
        }
        return true;
    }

    public static boolean canWrite(Context context, Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            return false;
        }
        String string2 = DocumentsContractApi19.getRawType(context, uri);
        int n = DocumentsContractApi19.queryForInt(context, uri, "flags", 0);
        if (TextUtils.isEmpty((CharSequence)string2)) {
            return false;
        }
        if ((n & 4) != 0) {
            return true;
        }
        if ("vnd.android.document/directory".equals(string2) && (n & 8) != 0) {
            return true;
        }
        if (!TextUtils.isEmpty((CharSequence)string2) && (n & 2) != 0) {
            return true;
        }
        return false;
    }

    private static void closeQuietly(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
                return;
            }
            catch (Exception exception) {
                return;
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
        }
    }

    public static boolean delete(Context context, Uri uri) {
        try {
            boolean bl = DocumentsContract.deleteDocument((ContentResolver)context.getContentResolver(), (Uri)uri);
            return bl;
        }
        catch (Exception exception) {
            return false;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static boolean exists(Context context, Uri uri) {
        Uri uri2;
        Throwable throwable222;
        Object object;
        boolean bl;
        block4 : {
            object = context.getContentResolver();
            uri2 = null;
            context = null;
            bl = true;
            uri = object.query(uri, new String[]{"document_id"}, null, null, null);
            context = uri;
            uri2 = uri;
            int n = uri.getCount();
            if (n > 0) break block4;
            bl = false;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
        return bl;
        {
            catch (Throwable throwable222) {
            }
            catch (Exception exception) {}
            context = uri2;
            {
                object = new StringBuilder();
                context = uri2;
                object.append("Failed query: ");
                context = uri2;
                object.append(exception);
                context = uri2;
                Log.w((String)"DocumentFile", (String)object.toString());
            }
            DocumentsContractApi19.closeQuietly((AutoCloseable)uri2);
            return false;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)context);
        throw throwable222;
    }

    public static long getFlags(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "flags", 0L);
    }

    public static String getName(Context context, Uri uri) {
        return DocumentsContractApi19.queryForString(context, uri, "_display_name", null);
    }

    private static String getRawType(Context context, Uri uri) {
        return DocumentsContractApi19.queryForString(context, uri, "mime_type", null);
    }

    public static String getType(Context object, Uri uri) {
        if ("vnd.android.document/directory".equals(object = DocumentsContractApi19.getRawType((Context)object, uri))) {
            return null;
        }
        return object;
    }

    public static boolean isDirectory(Context context, Uri uri) {
        return "vnd.android.document/directory".equals(DocumentsContractApi19.getRawType(context, uri));
    }

    public static boolean isDocumentUri(Context context, Uri uri) {
        return DocumentsContract.isDocumentUri((Context)context, (Uri)uri);
    }

    public static boolean isFile(Context object, Uri uri) {
        if (!"vnd.android.document/directory".equals(object = DocumentsContractApi19.getRawType((Context)object, uri)) && !TextUtils.isEmpty((CharSequence)object)) {
            return true;
        }
        return false;
    }

    public static boolean isVirtual(Context context, Uri uri) {
        boolean bl = DocumentsContractApi19.isDocumentUri(context, uri);
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        if ((DocumentsContractApi19.getFlags(context, uri) & 512L) != 0L) {
            bl2 = true;
        }
        return bl2;
    }

    public static long lastModified(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "last_modified", 0L);
    }

    public static long length(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "_size", 0L);
    }

    private static int queryForInt(Context context, Uri uri, String string2, int n) {
        return (int)DocumentsContractApi19.queryForLong(context, uri, string2, n);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static long queryForLong(Context context, Uri uri, String charSequence, long l) {
        Throwable throwable222;
        Uri uri2;
        block4 : {
            ContentResolver contentResolver = context.getContentResolver();
            uri2 = null;
            context = null;
            uri = contentResolver.query(uri, new String[]{charSequence}, null, null, null);
            context = uri;
            uri2 = uri;
            if (!uri.moveToFirst()) break block4;
            context = uri;
            uri2 = uri;
            if (uri.isNull(0)) break block4;
            context = uri;
            uri2 = uri;
            long l2 = uri.getLong(0);
            DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
            return l2;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
        return l;
        {
            catch (Throwable throwable222) {
            }
            catch (Exception exception) {}
            context = uri2;
            {
                charSequence = new StringBuilder();
                context = uri2;
                charSequence.append("Failed query: ");
                context = uri2;
                charSequence.append(exception);
                context = uri2;
                Log.w((String)"DocumentFile", (String)charSequence.toString());
            }
            DocumentsContractApi19.closeQuietly((AutoCloseable)uri2);
            return l;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)context);
        throw throwable222;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static String queryForString(Context context, Uri uri, String charSequence, String string2) {
        Throwable throwable222;
        Uri uri2;
        block4 : {
            ContentResolver contentResolver = context.getContentResolver();
            uri2 = null;
            context = null;
            uri = contentResolver.query(uri, new String[]{charSequence}, null, null, null);
            context = uri;
            uri2 = uri;
            if (!uri.moveToFirst()) break block4;
            context = uri;
            uri2 = uri;
            if (uri.isNull(0)) break block4;
            context = uri;
            uri2 = uri;
            charSequence = uri.getString(0);
            DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
            return charSequence;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
        return string2;
        {
            catch (Throwable throwable222) {
            }
            catch (Exception exception) {}
            context = uri2;
            {
                charSequence = new StringBuilder();
                context = uri2;
                charSequence.append("Failed query: ");
                context = uri2;
                charSequence.append(exception);
                context = uri2;
                Log.w((String)"DocumentFile", (String)charSequence.toString());
            }
            DocumentsContractApi19.closeQuietly((AutoCloseable)uri2);
            return string2;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)context);
        throw throwable222;
    }
}

