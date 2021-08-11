/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ClipDescription
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.inputmethod.InputContentInfo
 */
package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.view.inputmethod.InputContentInfo;

public final class InputContentInfoCompat {
    private final InputContentInfoCompatImpl mImpl;

    public InputContentInfoCompat(Uri uri, ClipDescription clipDescription, Uri uri2) {
        if (Build.VERSION.SDK_INT >= 25) {
            this.mImpl = new InputContentInfoCompatApi25Impl(uri, clipDescription, uri2);
            return;
        }
        this.mImpl = new InputContentInfoCompatBaseImpl(uri, clipDescription, uri2);
    }

    private InputContentInfoCompat(InputContentInfoCompatImpl inputContentInfoCompatImpl) {
        this.mImpl = inputContentInfoCompatImpl;
    }

    public static InputContentInfoCompat wrap(Object object) {
        if (object == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT < 25) {
            return null;
        }
        return new InputContentInfoCompat(new InputContentInfoCompatApi25Impl(object));
    }

    public Uri getContentUri() {
        return this.mImpl.getContentUri();
    }

    public ClipDescription getDescription() {
        return this.mImpl.getDescription();
    }

    public Uri getLinkUri() {
        return this.mImpl.getLinkUri();
    }

    public void releasePermission() {
        this.mImpl.releasePermission();
    }

    public void requestPermission() {
        this.mImpl.requestPermission();
    }

    public Object unwrap() {
        return this.mImpl.getInputContentInfo();
    }

    private static final class InputContentInfoCompatApi25Impl
    implements InputContentInfoCompatImpl {
        final InputContentInfo mObject;

        InputContentInfoCompatApi25Impl(Uri uri, ClipDescription clipDescription, Uri uri2) {
            this.mObject = new InputContentInfo(uri, clipDescription, uri2);
        }

        InputContentInfoCompatApi25Impl(Object object) {
            this.mObject = (InputContentInfo)object;
        }

        @Override
        public Uri getContentUri() {
            return this.mObject.getContentUri();
        }

        @Override
        public ClipDescription getDescription() {
            return this.mObject.getDescription();
        }

        @Override
        public Object getInputContentInfo() {
            return this.mObject;
        }

        @Override
        public Uri getLinkUri() {
            return this.mObject.getLinkUri();
        }

        @Override
        public void releasePermission() {
            this.mObject.releasePermission();
        }

        @Override
        public void requestPermission() {
            this.mObject.requestPermission();
        }
    }

    private static final class InputContentInfoCompatBaseImpl
    implements InputContentInfoCompatImpl {
        private final Uri mContentUri;
        private final ClipDescription mDescription;
        private final Uri mLinkUri;

        InputContentInfoCompatBaseImpl(Uri uri, ClipDescription clipDescription, Uri uri2) {
            this.mContentUri = uri;
            this.mDescription = clipDescription;
            this.mLinkUri = uri2;
        }

        @Override
        public Uri getContentUri() {
            return this.mContentUri;
        }

        @Override
        public ClipDescription getDescription() {
            return this.mDescription;
        }

        @Override
        public Object getInputContentInfo() {
            return null;
        }

        @Override
        public Uri getLinkUri() {
            return this.mLinkUri;
        }

        @Override
        public void releasePermission() {
        }

        @Override
        public void requestPermission() {
        }
    }

    private static interface InputContentInfoCompatImpl {
        public Uri getContentUri();

        public ClipDescription getDescription();

        public Object getInputContentInfo();

        public Uri getLinkUri();

        public void releasePermission();

        public void requestPermission();
    }

}

