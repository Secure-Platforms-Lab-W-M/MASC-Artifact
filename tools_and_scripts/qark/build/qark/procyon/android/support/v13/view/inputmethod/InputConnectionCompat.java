// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v13.view.inputmethod;

import android.os.Parcelable;
import android.net.Uri;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;
import android.support.annotation.RequiresApi;
import android.content.ClipDescription;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputConnection;
import android.os.Build$VERSION;

public final class InputConnectionCompat
{
    private static final InputConnectionCompatImpl IMPL;
    public static int INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
    
    static {
        if (Build$VERSION.SDK_INT >= 25) {
            IMPL = (InputConnectionCompatImpl)new InputContentInfoCompatApi25Impl();
        }
        else {
            IMPL = (InputConnectionCompatImpl)new InputContentInfoCompatBaseImpl();
        }
        InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;
    }
    
    public static boolean commitContent(@NonNull final InputConnection inputConnection, @NonNull final EditorInfo editorInfo, @NonNull final InputContentInfoCompat inputContentInfoCompat, final int n, @Nullable final Bundle bundle) {
        final ClipDescription description = inputContentInfoCompat.getDescription();
        final boolean b = false;
        final String[] contentMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        final int length = contentMimeTypes.length;
        int n2 = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n2 >= length) {
                break;
            }
            if (description.hasMimeType(contentMimeTypes[n2])) {
                b2 = true;
                break;
            }
            ++n2;
        }
        return b2 && InputConnectionCompat.IMPL.commitContent(inputConnection, inputContentInfoCompat, n, bundle);
    }
    
    @NonNull
    public static InputConnection createWrapper(@NonNull final InputConnection inputConnection, @NonNull final EditorInfo editorInfo, @NonNull final OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        }
        if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        if (onCommitContentListener != null) {
            return InputConnectionCompat.IMPL.createWrapper(inputConnection, editorInfo, onCommitContentListener);
        }
        throw new IllegalArgumentException("onCommitContentListener must be non-null");
    }
    
    private interface InputConnectionCompatImpl
    {
        boolean commitContent(@NonNull final InputConnection p0, @NonNull final InputContentInfoCompat p1, final int p2, @Nullable final Bundle p3);
        
        @NonNull
        InputConnection createWrapper(@NonNull final InputConnection p0, @NonNull final EditorInfo p1, @NonNull final OnCommitContentListener p2);
    }
    
    @RequiresApi(25)
    private static final class InputContentInfoCompatApi25Impl implements InputConnectionCompatImpl
    {
        @Override
        public boolean commitContent(@NonNull final InputConnection inputConnection, @NonNull final InputContentInfoCompat inputContentInfoCompat, final int n, @Nullable final Bundle bundle) {
            return inputConnection.commitContent((InputContentInfo)inputContentInfoCompat.unwrap(), n, bundle);
        }
        
        @Nullable
        @Override
        public InputConnection createWrapper(@Nullable final InputConnection inputConnection, @NonNull final EditorInfo editorInfo, @Nullable final OnCommitContentListener onCommitContentListener) {
            return (InputConnection)new InputConnectionWrapper(inputConnection, false) {
                public boolean commitContent(final InputContentInfo inputContentInfo, final int n, final Bundle bundle) {
                    return onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), n, bundle) || super.commitContent(inputContentInfo, n, bundle);
                }
            };
        }
    }
    
    static final class InputContentInfoCompatBaseImpl implements InputConnectionCompatImpl
    {
        private static String COMMIT_CONTENT_ACTION;
        private static String COMMIT_CONTENT_CONTENT_URI_KEY;
        private static String COMMIT_CONTENT_DESCRIPTION_KEY;
        private static String COMMIT_CONTENT_FLAGS_KEY;
        private static String COMMIT_CONTENT_LINK_URI_KEY;
        private static String COMMIT_CONTENT_OPTS_KEY;
        private static String COMMIT_CONTENT_RESULT_RECEIVER;
        
        static {
            InputContentInfoCompatBaseImpl.COMMIT_CONTENT_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
            InputContentInfoCompatBaseImpl.COMMIT_CONTENT_CONTENT_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
            InputContentInfoCompatBaseImpl.COMMIT_CONTENT_DESCRIPTION_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
            InputContentInfoCompatBaseImpl.COMMIT_CONTENT_LINK_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
            InputContentInfoCompatBaseImpl.COMMIT_CONTENT_OPTS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
            InputContentInfoCompatBaseImpl.COMMIT_CONTENT_FLAGS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
            InputContentInfoCompatBaseImpl.COMMIT_CONTENT_RESULT_RECEIVER = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
        }
        
        static boolean handlePerformPrivateCommand(@Nullable String s, @NonNull Bundle bundle, @NonNull final OnCommitContentListener onCommitContentListener) {
            final boolean equals = TextUtils.equals((CharSequence)InputContentInfoCompatBaseImpl.COMMIT_CONTENT_ACTION, (CharSequence)s);
            final boolean b = false;
            int n = 0;
            if (!equals) {
                return false;
            }
            if (bundle == null) {
                return false;
            }
            s = null;
            try {
                final Object o = s = (String)bundle.getParcelable(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_RESULT_RECEIVER);
                final Uri uri = (Uri)bundle.getParcelable(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_CONTENT_URI_KEY);
                s = (String)o;
                final ClipDescription clipDescription = (ClipDescription)bundle.getParcelable(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_DESCRIPTION_KEY);
                s = (String)o;
                final Uri uri2 = (Uri)bundle.getParcelable(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_LINK_URI_KEY);
                s = (String)o;
                final int int1 = bundle.getInt(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_FLAGS_KEY);
                s = (String)o;
                bundle = (Bundle)bundle.getParcelable(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_OPTS_KEY);
                s = (String)o;
                final boolean onCommitContent = onCommitContentListener.onCommitContent(new InputContentInfoCompat(uri, clipDescription, uri2), int1, bundle);
                if (o != null) {
                    if (onCommitContent) {
                        n = 1;
                    }
                    ((ResultReceiver)o).send(n, (Bundle)null);
                }
                return onCommitContent;
            }
            finally {
                if (s != null) {
                    int n2 = b ? 1 : 0;
                    if (false) {
                        n2 = 1;
                    }
                    ((ResultReceiver)s).send(n2, (Bundle)null);
                }
            }
        }
        
        @Override
        public boolean commitContent(@NonNull final InputConnection inputConnection, @NonNull final InputContentInfoCompat inputContentInfoCompat, final int n, @Nullable final Bundle bundle) {
            final Bundle bundle2 = new Bundle();
            bundle2.putParcelable(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_CONTENT_URI_KEY, (Parcelable)inputContentInfoCompat.getContentUri());
            bundle2.putParcelable(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_DESCRIPTION_KEY, (Parcelable)inputContentInfoCompat.getDescription());
            bundle2.putParcelable(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_LINK_URI_KEY, (Parcelable)inputContentInfoCompat.getLinkUri());
            bundle2.putInt(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_FLAGS_KEY, n);
            bundle2.putParcelable(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_OPTS_KEY, (Parcelable)bundle);
            return inputConnection.performPrivateCommand(InputContentInfoCompatBaseImpl.COMMIT_CONTENT_ACTION, bundle2);
        }
        
        @NonNull
        @Override
        public InputConnection createWrapper(@NonNull final InputConnection inputConnection, @NonNull final EditorInfo editorInfo, @NonNull final OnCommitContentListener onCommitContentListener) {
            if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
                return inputConnection;
            }
            return (InputConnection)new InputConnectionWrapper(inputConnection, false) {
                public boolean performPrivateCommand(final String s, final Bundle bundle) {
                    return InputContentInfoCompatBaseImpl.handlePerformPrivateCommand(s, bundle, onCommitContentListener) || super.performPrivateCommand(s, bundle);
                }
            };
        }
    }
    
    public interface OnCommitContentListener
    {
        boolean onCommitContent(final InputContentInfoCompat p0, final int p1, final Bundle p2);
    }
}
