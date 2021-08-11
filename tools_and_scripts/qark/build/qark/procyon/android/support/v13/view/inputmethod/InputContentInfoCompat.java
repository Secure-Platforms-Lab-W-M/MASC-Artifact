// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v13.view.inputmethod;

import android.view.inputmethod.InputContentInfo;
import android.support.annotation.RequiresApi;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import android.content.ClipDescription;
import android.support.annotation.NonNull;
import android.net.Uri;

public final class InputContentInfoCompat
{
    private final InputContentInfoCompatImpl mImpl;
    
    public InputContentInfoCompat(@NonNull final Uri uri, @NonNull final ClipDescription clipDescription, @Nullable final Uri uri2) {
        if (Build$VERSION.SDK_INT >= 25) {
            this.mImpl = (InputContentInfoCompatImpl)new InputContentInfoCompatApi25Impl(uri, clipDescription, uri2);
            return;
        }
        this.mImpl = (InputContentInfoCompatImpl)new InputContentInfoCompatBaseImpl(uri, clipDescription, uri2);
    }
    
    private InputContentInfoCompat(@NonNull final InputContentInfoCompatImpl mImpl) {
        this.mImpl = mImpl;
    }
    
    @Nullable
    public static InputContentInfoCompat wrap(@Nullable final Object o) {
        if (o == null) {
            return null;
        }
        if (Build$VERSION.SDK_INT < 25) {
            return null;
        }
        return new InputContentInfoCompat((InputContentInfoCompatImpl)new InputContentInfoCompatApi25Impl(o));
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
    
    @RequiresApi(25)
    private static final class InputContentInfoCompatApi25Impl implements InputContentInfoCompatImpl
    {
        @NonNull
        final InputContentInfo mObject;
        
        InputContentInfoCompatApi25Impl(@NonNull final Uri uri, @NonNull final ClipDescription clipDescription, @Nullable final Uri uri2) {
            this.mObject = new InputContentInfo(uri, clipDescription, uri2);
        }
        
        InputContentInfoCompatApi25Impl(@NonNull final Object o) {
            this.mObject = (InputContentInfo)o;
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
    
    private static final class InputContentInfoCompatBaseImpl implements InputContentInfoCompatImpl
    {
        @NonNull
        private final Uri mContentUri;
        @NonNull
        private final ClipDescription mDescription;
        @Nullable
        private final Uri mLinkUri;
        
        InputContentInfoCompatBaseImpl(@NonNull final Uri mContentUri, @NonNull final ClipDescription mDescription, @Nullable final Uri mLinkUri) {
            this.mContentUri = mContentUri;
            this.mDescription = mDescription;
            this.mLinkUri = mLinkUri;
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
    
    private interface InputContentInfoCompatImpl
    {
        @NonNull
        Uri getContentUri();
        
        @NonNull
        ClipDescription getDescription();
        
        @Nullable
        Object getInputContentInfo();
        
        @Nullable
        Uri getLinkUri();
        
        void releasePermission();
        
        void requestPermission();
    }
}
