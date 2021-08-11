/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.provider.DocumentsContract
 *  android.util.Log
 */
package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.ArrayList;

@RequiresApi(value=21)
class DocumentsContractApi21 {
    private static final String TAG = "DocumentFile";

    DocumentsContractApi21() {
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

    public static Uri createDirectory(Context context, Uri uri, String string2) {
        return DocumentsContractApi21.createFile(context, uri, "vnd.android.document/directory", string2);
    }

    public static Uri createFile(Context context, Uri uri, String string2, String string3) {
        try {
            context = DocumentsContract.createDocument((ContentResolver)context.getContentResolver(), (Uri)uri, (String)string2, (String)string3);
            return context;
        }
        catch (Exception exception) {
            return null;
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
    public static Uri[] listFiles(Context object, Uri uri) {
        Throwable throwable222;
        Object object2;
        block6 : {
            Object object3 = object.getContentResolver();
            Object object4 = DocumentsContract.buildChildDocumentsUriUsingTree((Uri)uri, (String)DocumentsContract.getDocumentId((Uri)uri));
            ArrayList<Uri> arrayList = new ArrayList<Uri>();
            object = null;
            object2 = null;
            object3 = object3.query((Uri)object4, new String[]{"document_id"}, null, null, null);
            do {
                object4 = object3;
                object2 = object3;
                object = object3;
                if (object3.moveToNext()) {
                    object2 = object3;
                    object = object3;
                    arrayList.add(DocumentsContract.buildDocumentUriUsingTree((Uri)uri, (String)object3.getString(0)));
                    continue;
                }
                break;
            } while (true);
            {
                catch (Throwable throwable222) {
                    break block6;
                }
                catch (Exception exception) {}
                object2 = object;
                {
                    object3 = new StringBuilder();
                    object2 = object;
                    object3.append("Failed query: ");
                    object2 = object;
                    object3.append(exception);
                    object2 = object;
                    Log.w((String)"DocumentFile", (String)object3.toString());
                    object4 = object;
                }
            }
            DocumentsContractApi21.closeQuietly((AutoCloseable)object4);
            return arrayList.toArray((T[])new Uri[arrayList.size()]);
        }
        DocumentsContractApi21.closeQuietly((AutoCloseable)object2);
        throw throwable222;
    }

    public static Uri prepareTreeUri(Uri uri) {
        return DocumentsContract.buildDocumentUriUsingTree((Uri)uri, (String)DocumentsContract.getTreeDocumentId((Uri)uri));
    }

    public static Uri renameTo(Context context, Uri uri, String string2) {
        try {
            context = DocumentsContract.renameDocument((ContentResolver)context.getContentResolver(), (Uri)uri, (String)string2);
            return context;
        }
        catch (Exception exception) {
            return null;
        }
    }
}

