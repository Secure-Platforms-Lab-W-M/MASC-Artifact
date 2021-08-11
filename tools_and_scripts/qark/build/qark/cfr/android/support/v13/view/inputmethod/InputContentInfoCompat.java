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
package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.inputmethod.InputContentInfo;

public final class InputContentInfoCompat {
    private final InputContentInfoCompatImpl mImpl;

    public InputContentInfoCompat(@NonNull Uri uri, @NonNull ClipDescription clipDescription, @Nullable Uri uri2) {
        if (Build.VERSION.SDK_INT >= 25) {
            this.mImpl = new InputContentInfoCompatApi25Impl(uri, clipDescription, uri2);
            return;
        }
        this.mImpl = new InputContentInfoCompatBaseImpl(uri, clipDescription, uri2);
    }

    private InputContentInfoCompat(@NonNull InputContentInfoCompatImpl inputContentInfoCompatImpl) {
        this.mImpl = inputContentInfoCompatImpl;
    }

    @Nullable
    public static InputContentInfoCompat wrap(@Nullable Object object) {
        if (object == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT < 25) {
            return null;
        }
        return new InputContentInfoCompat(new InputContentInfoCompatApi25Impl(object));
    }

    @NonNull
    public Uri getContentUri() {
        return this.mImpl.getContentUri();
    }

    @NonNull
    public ClipDescription getDescription() {
        return this.mImpl.getDescription();
    }

    @Nullable
    public Uri getLinkUri() {
        return this.mImpl.getLinkUri();
    }

    public void releasePermission() {
        this.mImpl.releasePermission();
    }

    public void requestPermission() {
        this.mImpl.requestPermission();
    }

    @Nullable
    public Object unwrap() {
        return this.mImpl.getInputContentInfo();
    }

    @RequiresApi(value=25)
    private static final class InputContentInfoCompatApi25Impl
    implements InputContentInfoCompatImpl {
        @NonNull
        final InputContentInfo mObject;

        InputContentInfoCompatApi25Impl(@NonNull Uri uri, @NonNull ClipDescription clipDescription, @Nullable Uri uri2) {
            this.mObject = new InputContentInfo(uri, clipDescription, uri2);
        }

        InputContentInfoCompatApi25Impl(@NonNull Object object) {
            this.mObject = (InputContentInfo)object;
        }

        @NonNull
        @Override
        public Uri getContentUri() {
            return this.mObject.getContentUri();
        }

        @NonNull
        @Override
        public ClipDescription getDescription() {
            return this.mObject.getDescription();
        }

        @Nullable
        @Override
        public Object getInputContentInfo() {
            return this.mObject;
        }

        @Nullable
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
        @NonNull
        private final Uri mContentUri;
        @NonNull
        private final ClipDescription mDescription;
        @Nullable
        private final Uri mLinkUri;

        InputContentInfoCompatBaseImpl(@NonNull Uri uri, @NonNull ClipDescription clipDescription, @Nullable Uri uri2) {
            this.mContentUri = uri;
            this.mDescription = clipDescription;
            this.mLinkUri = uri2;
        }

        @NonNull
        @Override
        public Uri getContentUri() {
            return this.mContentUri;
        }

        @NonNull
        @Override
        public ClipDescription getDescription() {
            return this.mDescription;
        }

        @Nullable
        @Override
        public Object getInputContentInfo() {
            return null;
        }

        @Nullable
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
        @NonNull
        public Uri getContentUri();

        @NonNull
        public ClipDescription getDescription();

        @Nullable
        public Object getInputContentInfo();

        @Nullable
        public Uri getLinkUri();

        public void releasePermission();

        public void requestPermission();
    }

}

