/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.inputmethod.EditorInfo
 */
package android.support.v13.view.inputmethod;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.inputmethod.EditorInfo;

public final class EditorInfoCompat {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
    public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
    private static final EditorInfoCompatImpl IMPL = Build.VERSION.SDK_INT >= 25 ? new EditorInfoCompatApi25Impl() : new EditorInfoCompatBaseImpl();

    @NonNull
    public static String[] getContentMimeTypes(EditorInfo editorInfo) {
        return IMPL.getContentMimeTypes(editorInfo);
    }

    public static void setContentMimeTypes(@NonNull EditorInfo editorInfo, @Nullable String[] arrstring) {
        IMPL.setContentMimeTypes(editorInfo, arrstring);
    }

    @RequiresApi(value=25)
    private static final class EditorInfoCompatApi25Impl
    implements EditorInfoCompatImpl {
        private EditorInfoCompatApi25Impl() {
        }

        @NonNull
        @Override
        public String[] getContentMimeTypes(@NonNull EditorInfo arrstring) {
            arrstring = arrstring.contentMimeTypes;
            if (arrstring != null) {
                return arrstring;
            }
            return EMPTY_STRING_ARRAY;
        }

        @Override
        public void setContentMimeTypes(@NonNull EditorInfo editorInfo, @Nullable String[] arrstring) {
            editorInfo.contentMimeTypes = arrstring;
        }
    }

    private static final class EditorInfoCompatBaseImpl
    implements EditorInfoCompatImpl {
        private static String CONTENT_MIME_TYPES_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";

        private EditorInfoCompatBaseImpl() {
        }

        @NonNull
        @Override
        public String[] getContentMimeTypes(@NonNull EditorInfo arrstring) {
            if (arrstring.extras == null) {
                return EMPTY_STRING_ARRAY;
            }
            arrstring = arrstring.extras.getStringArray(CONTENT_MIME_TYPES_KEY);
            if (arrstring != null) {
                return arrstring;
            }
            return EMPTY_STRING_ARRAY;
        }

        @Override
        public void setContentMimeTypes(@NonNull EditorInfo editorInfo, @Nullable String[] arrstring) {
            if (editorInfo.extras == null) {
                editorInfo.extras = new Bundle();
            }
            editorInfo.extras.putStringArray(CONTENT_MIME_TYPES_KEY, arrstring);
        }
    }

    private static interface EditorInfoCompatImpl {
        @NonNull
        public String[] getContentMimeTypes(@NonNull EditorInfo var1);

        public void setContentMimeTypes(@NonNull EditorInfo var1, @Nullable String[] var2);
    }

}

