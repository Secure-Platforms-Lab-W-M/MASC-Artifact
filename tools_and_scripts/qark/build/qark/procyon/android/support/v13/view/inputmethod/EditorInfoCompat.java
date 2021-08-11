// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v13.view.inputmethod;

import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.view.inputmethod.EditorInfo;
import android.os.Build$VERSION;

public final class EditorInfoCompat
{
    private static final String[] EMPTY_STRING_ARRAY;
    public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
    public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
    private static final EditorInfoCompatImpl IMPL;
    
    static {
        EMPTY_STRING_ARRAY = new String[0];
        if (Build$VERSION.SDK_INT >= 25) {
            IMPL = (EditorInfoCompatImpl)new EditorInfoCompatApi25Impl();
            return;
        }
        IMPL = (EditorInfoCompatImpl)new EditorInfoCompatBaseImpl();
    }
    
    @NonNull
    public static String[] getContentMimeTypes(final EditorInfo editorInfo) {
        return EditorInfoCompat.IMPL.getContentMimeTypes(editorInfo);
    }
    
    public static void setContentMimeTypes(@NonNull final EditorInfo editorInfo, @Nullable final String[] array) {
        EditorInfoCompat.IMPL.setContentMimeTypes(editorInfo, array);
    }
    
    @RequiresApi(25)
    private static final class EditorInfoCompatApi25Impl implements EditorInfoCompatImpl
    {
        @NonNull
        @Override
        public String[] getContentMimeTypes(@NonNull final EditorInfo editorInfo) {
            final String[] contentMimeTypes = editorInfo.contentMimeTypes;
            if (contentMimeTypes != null) {
                return contentMimeTypes;
            }
            return EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
        
        @Override
        public void setContentMimeTypes(@NonNull final EditorInfo editorInfo, @Nullable final String[] contentMimeTypes) {
            editorInfo.contentMimeTypes = contentMimeTypes;
        }
    }
    
    private static final class EditorInfoCompatBaseImpl implements EditorInfoCompatImpl
    {
        private static String CONTENT_MIME_TYPES_KEY;
        
        static {
            EditorInfoCompatBaseImpl.CONTENT_MIME_TYPES_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
        }
        
        @NonNull
        @Override
        public String[] getContentMimeTypes(@NonNull final EditorInfo editorInfo) {
            if (editorInfo.extras == null) {
                return EditorInfoCompat.EMPTY_STRING_ARRAY;
            }
            final String[] stringArray = editorInfo.extras.getStringArray(EditorInfoCompatBaseImpl.CONTENT_MIME_TYPES_KEY);
            if (stringArray != null) {
                return stringArray;
            }
            return EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
        
        @Override
        public void setContentMimeTypes(@NonNull final EditorInfo editorInfo, @Nullable final String[] array) {
            if (editorInfo.extras == null) {
                editorInfo.extras = new Bundle();
            }
            editorInfo.extras.putStringArray(EditorInfoCompatBaseImpl.CONTENT_MIME_TYPES_KEY, array);
        }
    }
    
    private interface EditorInfoCompatImpl
    {
        @NonNull
        String[] getContentMimeTypes(@NonNull final EditorInfo p0);
        
        void setContentMimeTypes(@NonNull final EditorInfo p0, @Nullable final String[] p1);
    }
}
