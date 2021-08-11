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
package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v13.view.inputmethod.EditorInfoCompat;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {
    private static final InputConnectionCompatImpl IMPL = Build.VERSION.SDK_INT >= 25 ? new InputContentInfoCompatApi25Impl() : new InputContentInfoCompatBaseImpl();
    public static int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    public static boolean commitContent(@NonNull InputConnection inputConnection, @NonNull EditorInfo arrstring, @NonNull InputContentInfoCompat inputContentInfoCompat, int n, @Nullable Bundle bundle) {
        boolean bl;
        ClipDescription clipDescription = inputContentInfoCompat.getDescription();
        boolean bl2 = false;
        arrstring = EditorInfoCompat.getContentMimeTypes((EditorInfo)arrstring);
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
        return IMPL.commitContent(inputConnection, inputContentInfoCompat, n, bundle);
    }

    @NonNull
    public static InputConnection createWrapper(@NonNull InputConnection inputConnection, @NonNull EditorInfo editorInfo, @NonNull OnCommitContentListener onCommitContentListener) {
        if (inputConnection != null) {
            if (editorInfo != null) {
                if (onCommitContentListener != null) {
                    return IMPL.createWrapper(inputConnection, editorInfo, onCommitContentListener);
                }
                throw new IllegalArgumentException("onCommitContentListener must be non-null");
            }
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        throw new IllegalArgumentException("inputConnection must be non-null");
    }

    private static interface InputConnectionCompatImpl {
        public boolean commitContent(@NonNull InputConnection var1, @NonNull InputContentInfoCompat var2, int var3, @Nullable Bundle var4);

        @NonNull
        public InputConnection createWrapper(@NonNull InputConnection var1, @NonNull EditorInfo var2, @NonNull OnCommitContentListener var3);
    }

    @RequiresApi(value=25)
    private static final class InputContentInfoCompatApi25Impl
    implements InputConnectionCompatImpl {
        private InputContentInfoCompatApi25Impl() {
        }

        @Override
        public boolean commitContent(@NonNull InputConnection inputConnection, @NonNull InputContentInfoCompat inputContentInfoCompat, int n, @Nullable Bundle bundle) {
            return inputConnection.commitContent((InputContentInfo)inputContentInfoCompat.unwrap(), n, bundle);
        }

        @Nullable
        @Override
        public InputConnection createWrapper(@Nullable InputConnection inputConnection, @NonNull EditorInfo editorInfo, final @Nullable OnCommitContentListener onCommitContentListener) {
            return new InputConnectionWrapper(inputConnection, false){

                public boolean commitContent(InputContentInfo inputContentInfo, int n, Bundle bundle) {
                    if (onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap((Object)inputContentInfo), n, bundle)) {
                        return true;
                    }
                    return super.commitContent(inputContentInfo, n, bundle);
                }
            };
        }

    }

    static final class InputContentInfoCompatBaseImpl
    implements InputConnectionCompatImpl {
        private static String COMMIT_CONTENT_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
        private static String COMMIT_CONTENT_CONTENT_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
        private static String COMMIT_CONTENT_DESCRIPTION_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
        private static String COMMIT_CONTENT_FLAGS_KEY;
        private static String COMMIT_CONTENT_LINK_URI_KEY;
        private static String COMMIT_CONTENT_OPTS_KEY;
        private static String COMMIT_CONTENT_RESULT_RECEIVER;

        static {
            COMMIT_CONTENT_LINK_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
            COMMIT_CONTENT_OPTS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
            COMMIT_CONTENT_FLAGS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
            COMMIT_CONTENT_RESULT_RECEIVER = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
        }

        InputContentInfoCompatBaseImpl() {
        }

        static boolean handlePerformPrivateCommand(@Nullable String string, @NonNull Bundle bundle, @NonNull OnCommitContentListener onCommitContentListener) {
            boolean bl;
            block12 : {
                ResultReceiver resultReceiver;
                int n;
                block13 : {
                    bl = TextUtils.equals((CharSequence)COMMIT_CONTENT_ACTION, (CharSequence)string);
                    int n2 = 0;
                    n = 0;
                    if (!bl) {
                        return false;
                    }
                    if (bundle == null) {
                        return false;
                    }
                    string = null;
                    try {
                        resultReceiver = (ResultReceiver)bundle.getParcelable(COMMIT_CONTENT_RESULT_RECEIVER);
                        string = resultReceiver;
                    }
                    catch (Throwable throwable) {
                        if (string != null) {
                            n = n2;
                            if (false) {
                                n = 1;
                            }
                            string.send(n, null);
                        }
                        throw throwable;
                    }
                    Uri uri = (Uri)bundle.getParcelable(COMMIT_CONTENT_CONTENT_URI_KEY);
                    string = resultReceiver;
                    ClipDescription clipDescription = (ClipDescription)bundle.getParcelable(COMMIT_CONTENT_DESCRIPTION_KEY);
                    string = resultReceiver;
                    Uri uri2 = (Uri)bundle.getParcelable(COMMIT_CONTENT_LINK_URI_KEY);
                    string = resultReceiver;
                    int n3 = bundle.getInt(COMMIT_CONTENT_FLAGS_KEY);
                    string = resultReceiver;
                    bundle = (Bundle)bundle.getParcelable(COMMIT_CONTENT_OPTS_KEY);
                    string = resultReceiver;
                    bl = onCommitContentListener.onCommitContent(new InputContentInfoCompat(uri, clipDescription, uri2), n3, bundle);
                    if (resultReceiver == null) break block12;
                    if (!bl) break block13;
                    n = 1;
                }
                resultReceiver.send(n, null);
            }
            return bl;
        }

        @Override
        public boolean commitContent(@NonNull InputConnection inputConnection, @NonNull InputContentInfoCompat inputContentInfoCompat, int n, @Nullable Bundle bundle) {
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable(COMMIT_CONTENT_CONTENT_URI_KEY, (Parcelable)inputContentInfoCompat.getContentUri());
            bundle2.putParcelable(COMMIT_CONTENT_DESCRIPTION_KEY, (Parcelable)inputContentInfoCompat.getDescription());
            bundle2.putParcelable(COMMIT_CONTENT_LINK_URI_KEY, (Parcelable)inputContentInfoCompat.getLinkUri());
            bundle2.putInt(COMMIT_CONTENT_FLAGS_KEY, n);
            bundle2.putParcelable(COMMIT_CONTENT_OPTS_KEY, (Parcelable)bundle);
            return inputConnection.performPrivateCommand(COMMIT_CONTENT_ACTION, bundle2);
        }

        @NonNull
        @Override
        public InputConnection createWrapper(@NonNull InputConnection inputConnection, @NonNull EditorInfo editorInfo, final @NonNull OnCommitContentListener onCommitContentListener) {
            if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
                return inputConnection;
            }
            return new InputConnectionWrapper(inputConnection, false){

                public boolean performPrivateCommand(String string, Bundle bundle) {
                    if (InputContentInfoCompatBaseImpl.handlePerformPrivateCommand(string, bundle, onCommitContentListener)) {
                        return true;
                    }
                    return super.performPrivateCommand(string, bundle);
                }
            };
        }

    }

    public static interface OnCommitContentListener {
        public boolean onCommitContent(InputContentInfoCompat var1, int var2, Bundle var3);
    }

}

