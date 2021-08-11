// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.content.ContentResolver;
import android.util.Log;
import java.util.ArrayList;
import android.provider.DocumentsContract;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class DocumentsContractApi21
{
    private static final String TAG = "DocumentFile";
    
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
    
    public static Uri createDirectory(final Context context, final Uri uri, final String s) {
        return createFile(context, uri, "vnd.android.document/directory", s);
    }
    
    public static Uri createFile(final Context context, final Uri uri, final String s, final String s2) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), uri, s, s2);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Uri[] listFiles(final Context context, final Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri buildChildDocumentsUriUsingTree = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri));
        final ArrayList<Uri> list = new ArrayList<Uri>();
        Object o = null;
        while (true) {
            try {
                try {
                    final Cursor query = contentResolver.query(buildChildDocumentsUriUsingTree, new String[] { "document_id" }, (String)null, (String[])null, (String)null);
                    Object o2;
                    while (true) {
                        o2 = query;
                        o = query;
                        if (!query.moveToNext()) {
                            break;
                        }
                        o = query;
                        list.add(DocumentsContract.buildDocumentUriUsingTree(uri, query.getString(0)));
                    }
                    closeQuietly((AutoCloseable)o2);
                }
                finally {}
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed query: ");
                sb.append(ex);
                o = context;
                Log.w("DocumentFile", sb.toString());
                final Object o2 = context;
                continue;
            }
            break;
        }
        return list.toArray(new Uri[list.size()]);
        closeQuietly((AutoCloseable)o);
    }
    
    public static Uri prepareTreeUri(final Uri uri) {
        return DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
    }
    
    public static Uri renameTo(final Context context, final Uri uri, final String s) {
        try {
            return DocumentsContract.renameDocument(context.getContentResolver(), uri, s);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
