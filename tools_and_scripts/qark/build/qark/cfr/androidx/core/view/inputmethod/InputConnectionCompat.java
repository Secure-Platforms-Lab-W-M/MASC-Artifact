/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ClipDescription
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.os.ResultReceiver
 *  android.text.TextUtils
 *  android.view.inputmethod.EditorInfo
 *  android.view.inputmethod.InputConnection
 *  android.view.inputmethod.InputConnectionWrapper
 *  android.view.inputmethod.InputContentInfo
 */
package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_INTEROP_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    public static boolean commitContent(InputConnection inputConnection, EditorInfo object, InputContentInfoCompat inputContentInfoCompat, int n, Bundle bundle) {
        boolean bl;
        ClipDescription clipDescription = inputContentInfoCompat.getDescription();
        boolean bl2 = false;
        String[] arrstring = EditorInfoCompat.getContentMimeTypes((EditorInfo)object);
        int n2 = arrstring.length;
        int n3 = 0;
        do {
            bl = bl2;
            if (n3 >= n2) break;
            if (clipDescription.hasMimeType(arrstring[n3])) {
                bl = true;
                break;
            }
            ++n3;
        } while (true);
        if (!bl) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo)inputContentInfoCompat.unwrap(), n, bundle);
        }
        n3 = EditorInfoCompat.getProtocol((EditorInfo)object);
        if (n3 != 2) {
            if (n3 != 3 && n3 != 4) {
                return false;
            }
            n3 = 0;
        } else {
            n3 = 1;
        }
        clipDescription = new Bundle();
        object = n3 != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
        clipDescription.putParcelable((String)object, (Parcelable)inputContentInfoCompat.getContentUri());
        object = n3 != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
        clipDescription.putParcelable((String)object, (Parcelable)inputContentInfoCompat.getDescription());
        object = n3 != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
        clipDescription.putParcelable((String)object, (Parcelable)inputContentInfoCompat.getLinkUri());
        object = n3 != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
        clipDescription.putInt((String)object, n);
        object = n3 != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
        clipDescription.putParcelable((String)object, (Parcelable)bundle);
        object = n3 != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT" : "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
        return inputConnection.performPrivateCommand((String)object, (Bundle)clipDescription);
    }

    public static InputConnection createWrapper(InputConnection inputConnection, EditorInfo editorInfo, final OnCommitContentListener onCommitContentListener) {
        if (inputConnection != null) {
            if (editorInfo != null) {
                if (onCommitContentListener != null) {
                    if (Build.VERSION.SDK_INT >= 25) {
                        return new InputConnectionWrapper(inputConnection, false){

                            public boolean commitContent(InputContentInfo inputContentInfo, int n, Bundle bundle) {
                                if (onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap((Object)inputContentInfo), n, bundle)) {
                                    return true;
                                }
                                return super.commitContent(inputContentInfo, n, bundle);
                            }
                        };
                    }
                    if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
                        return inputConnection;
                    }
                    return new InputConnectionWrapper(inputConnection, false){

                        public boolean performPrivateCommand(String string2, Bundle bundle) {
                            if (InputConnectionCompat.handlePerformPrivateCommand(string2, bundle, onCommitContentListener)) {
                                return true;
                            }
                            return super.performPrivateCommand(string2, bundle);
                        }
                    };
                }
                throw new IllegalArgumentException("onCommitContentListener must be non-null");
            }
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        throw new IllegalArgumentException("inputConnection must be non-null");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static boolean handlePerformPrivateCommand(String string2, Bundle bundle, OnCommitContentListener onCommitContentListener) {
        boolean bl;
        block9 : {
            ResultReceiver resultReceiver;
            int n;
            block10 : {
                int n2 = 0;
                if (bundle == null) {
                    return false;
                }
                if (TextUtils.equals((CharSequence)"androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", (CharSequence)string2)) {
                    n = 0;
                } else {
                    if (!TextUtils.equals((CharSequence)"android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", (CharSequence)string2)) {
                        return false;
                    }
                    n = 1;
                }
                string2 = null;
                boolean bl2 = false;
                String string3 = n != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
                try {
                    resultReceiver = (ResultReceiver)bundle.getParcelable(string3);
                    string3 = n != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
                    string2 = resultReceiver;
                    Uri uri = (Uri)bundle.getParcelable(string3);
                    string3 = n != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
                    string2 = resultReceiver;
                    ClipDescription clipDescription = (ClipDescription)bundle.getParcelable(string3);
                    string3 = n != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
                    string2 = resultReceiver;
                    Uri uri2 = (Uri)bundle.getParcelable(string3);
                    string3 = n != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
                    string2 = resultReceiver;
                    int n3 = bundle.getInt(string3);
                    string3 = n != 0 ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
                    string2 = resultReceiver;
                    bundle = (Bundle)bundle.getParcelable(string3);
                    bl = bl2;
                    if (uri != null) {
                        bl = bl2;
                        if (clipDescription != null) {
                            string2 = resultReceiver;
                            bl = onCommitContentListener.onCommitContent(new InputContentInfoCompat(uri, clipDescription, uri2), n3, bundle);
                        }
                    }
                    if (resultReceiver == null) break block9;
                    n = n2;
                    if (!bl) break block10;
                    n = 1;
                }
                catch (Throwable throwable) {
                    if (string2 != null) {
                        string2.send(0, null);
                    }
                    throw throwable;
                }
            }
            resultReceiver.send(n, null);
        }
        return bl;
    }

    public static interface OnCommitContentListener {
        public boolean onCommitContent(InputContentInfoCompat var1, int var2, Bundle var3);
    }

}

